/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-29
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.utils.testSeries.TestSeries.java
 * Last modification: 2007-09-29
 *                by: Thomas Weise
 *
 * License          : GNU LESSER GENERAL PUBLIC LICENSE
 *                    Version 2.1, February 1999
 *                    You should have received a copy of this license along
 *                    with this library; if not, write to theFree Software
 *                    Foundation, Inc. 51 Franklin Street, Fifth Floor,
 *                    Boston, MA 02110-1301, USA or download the license
 *                    under http://www.gnu.org/licenses/lgpl.html or
 *                    http://www.gnu.org/copyleft/lesser.html.
 *
 * Warranty         : This software is provided "as is" without any
 *                    warranty; without even the implied warranty of
 *                    merchantability or fitness for a particular purpose.
 *                    See the Gnu Lesser General Public License for more
 *                    details.
 */

package org.sigoa.refimpl.utils.testSeries;

import java.io.Serializable;
import java.util.List;

import org.sfc.parallel.SfcThread;
import org.sfc.utils.ErrorUtils;
import org.sfc.utils.Utils;
import org.sigoa.refimpl.go.OptimizationInfo;
import org.sigoa.refimpl.go.Population;
import org.sigoa.refimpl.go.comparators.ParetoComparator;
import org.sigoa.refimpl.go.embryogeny.CopyEmbryogeny;
import org.sigoa.refimpl.jobsystem.JobInfo;
import org.sigoa.refimpl.jobsystem.multiprocessor.MultiProcessorJobSystem;
import org.sigoa.refimpl.jobsystem.singleprocessor.SingleProcessorJobSystem;
import org.sigoa.refimpl.pipe.SorterPipe;
import org.sigoa.refimpl.simulation.IterationHookInvoker;
import org.sigoa.refimpl.simulation.SimulationManager;
import org.sigoa.spec.go.IComparator;
import org.sigoa.spec.go.IIndividualFactory;
import org.sigoa.spec.go.IOptimizationInfo;
import org.sigoa.spec.go.IOptimizer;
import org.sigoa.spec.go.IPopulation;
import org.sigoa.spec.go.embryogeny.IEmbryogeny;
import org.sigoa.spec.go.evaluation.IEvaluator;
import org.sigoa.spec.go.reproduction.ICreator;
import org.sigoa.spec.go.reproduction.ICrossover;
import org.sigoa.spec.go.reproduction.IMutator;
import org.sigoa.spec.jobsystem.IActivity;
import org.sigoa.spec.jobsystem.IJobInfo;
import org.sigoa.spec.jobsystem.IJobSystem;
import org.sigoa.spec.jobsystem.IWaitable;
import org.sigoa.spec.pipe.IPipeIn;
import org.sigoa.spec.simulation.ISimulationManager;
import org.sigoa.spec.simulation.ISimulationProvider;

/**
 * The base class for all test series
 * 
 * @author Thomas Weise
 */
public abstract class TestSeries extends SfcThread {
  /**
   * the maximum runs this test series is running
   */
  private volatile int m_maxRuns;

  /**
   * the activity synchronizer
   */
  private final Object m_activitySync;

  /**
   * the internal activities which can be aborted
   */
  private volatile IActivity m_jobSystem;

  /**
   * Create a new test series
   */
  protected TestSeries() {
    this(0);
  }

  /**
   * Create a new test series
   * 
   * @param maxRuns
   *          the maximum runs this test series is running
   */
  protected TestSeries(final int maxRuns) {
    super();
    this.m_maxRuns = ((maxRuns <= 0) ? Integer.MAX_VALUE : maxRuns);
    this.m_activitySync = new Object();
  }

  /**
   * Obtain the number of available processors.
   * 
   * @return the number of available processors.
   */
  protected int getProcessorCount() {
    return Runtime.getRuntime().availableProcessors();
  }

  /**
   * Internally perform one iteration
   * 
   * @param iteration
   *          the iteration number
   */
  @SuppressWarnings("unchecked")
  protected void iteration(final int iteration) {
    IOptimizer<?, ?> ea;
    IOptimizationInfo<?, ?> oi;
    IJobSystem js;
    IWaitable w;
    IPopulation<?, ?> results;
    SorterPipe<?, ?> p;

    ea = this.createOptimizer();
    results = new Population<Serializable, Serializable>() {
      private static final long serialVersionUID = 1;

      @Override
      public void eof() {
        super.eof();
        TestSeries.this.handleResults(this);
      }
    };
    p = new SorterPipe<Serializable, Serializable>();
    p.setOutputPipe((IPipeIn) results);
    ea.setOutputPipe((IPipeIn) p);

    oi = this.doCreateOptimizationInfo();

    js = this.createJobSystem();
    w = js.executeOptimization((IOptimizer) ea,
        (IJobInfo) new JobInfo<Serializable, Serializable>(
            (IOptimizationInfo) oi));

    try {
      synchronized (this.m_activitySync) {
        if (!(this.isRunning()))
          return;
        this.m_jobSystem = js;
        js.start();
      }

      w.waitFor(true);

      js.abort();

      js.abort();
      js.waitFor(false);

      results.clear();
    } finally {
      synchronized (this.m_activitySync) {
        this.m_jobSystem = null;
      }
    }
  }

  /**
   * This method is invoked after each test run and lets you process all
   * the non-prevailed individuals that are produced by the optimizer. The
   * <code>results</code>-array is sorted with a sorter pipe.
   * 
   * @param results
   *          the results of the optimization process for postprocessing
   */
  protected void handleResults(final IPopulation<?, ?> results) {
    //
  }

  /**
   * Abort this thread.
   */
  @Override
  protected void doAbort() {
    IActivity t;

    synchronized (this.m_activitySync) {
      t = this.m_jobSystem;
      if (t != null)
        try {
          t.abort();
        } catch (Throwable tt) {
          ErrorUtils.onError(tt);
        }
    }
    super.doAbort();
  }

  /**
   * this method is called before each iteration
   * 
   * @param iteration
   *          the iteration number
   */
  protected void beforeIteration(final int iteration) {
    //
  }

  /**
   * this method is called after each iteration
   * 
   * @param iteration
   *          the iteration number
   * @param timeSpent
   *          the time spent in this iteration
   */
  protected void afterIteration(final int iteration, final long timeSpent) {
    //
  }

  /**
   * Run this test series
   */
  @Override
  protected void doRun() {
    int iteration;
    long t;

    iteration = 1;

    while ((iteration <= this.m_maxRuns) && (this.isRunning())) {
      this.beforeIteration(iteration);
      t = System.currentTimeMillis();
      this.iteration(iteration);
      this.afterIteration(iteration, Math.max(0, System
          .currentTimeMillis()
          - t));
      this.cleanUp();
      iteration++;
    }
  }

  /**
   * Set the maximum runs this test series is running.
   * 
   * @param maxRuns
   *          the maximum runs this test series is running
   */
  public void setMaxRuns(final int maxRuns) {
    this.m_maxRuns = ((maxRuns <= 0) ? Integer.MAX_VALUE : maxRuns);
  }

  /**
   * Obtain the maximum runs this test series is running.
   * 
   * @return the maximum runs this test series is running
   */
  public int getMaxRuns() {
    return this.m_maxRuns;
  }

  /**
   * Do some cleanup
   */
  protected void cleanUp() {
    int i;
    for (i = 12; i >= 0; i--) {
      SfcThread.pause(100);
      Utils.invokeGC();
    }
  }

  /**
   * Obtain the simulation providers needed.
   * 
   * @return the simulation providers needed
   */
  protected ISimulationProvider[] createSimulationProviders() {
    return new ISimulationProvider[0];
  }

  /**
   * Create the evaluator.
   * 
   * @return the evaluator
   */
  protected abstract IEvaluator<?> createEvaluator();

  /**
   * Create the comparator
   * 
   * @return the comparator
   */
  protected IComparator createComparator() {
    return ParetoComparator.PARETO_COMPARATOR;
  }

  /**
   * Create the individual factory.
   * 
   * @return the individual factory
   */
  protected IIndividualFactory<?, ?> createIndividualFactory() {
    return null;
  }

  /**
   * Create the objective functions
   * 
   * @return the list of objective functions
   */
  protected abstract List<?> createObjectives();

  /**
   * Create the simulation manager and add all the simulation providers
   * created by {@link #createSimulationProviders()} to it.
   * 
   * @return the simulation manager
   */
  protected ISimulationManager createSimulationManager() {
    SimulationManager m;
    ISimulationProvider[] p;
    int i;

    m = new SimulationManager();

    p = this.createSimulationProviders();
    for (i = (p.length - 1); i >= 0; i--)
      m.addProvider(p[i]);

    return m;
  }

  /**
   * Create an individual evaluator
   * 
   * @return the new individual evaluator
   */
  public IndividualEvaluator createIndividualEvaluator() {
    IJobSystem js;

    js = this.createJobSystem();
    js.start();

    IterationHookInvoker.invokeBeforeIterationHook(js
        .getSimulationManager(), 0);

    return new IndividualEvaluator(js, this.doCreateOptimizationInfo(),
        this.createObjectives().size());
  }

  // /**
  // * This method can be used to evaluate a single phenotype.
  // *
  // * @param genotype
  // * the genotype (or <code>null</code>)
  // * @param phenotype
  // * the phenotype to be evaluated (or <code>null</code>)
  // * @return an individual record containing the evaluated data
  // */
  // public IIndividual<?, ?> evaluate(final Serializable genotype,
  // final Serializable phenotype) {
  // return this.evaluate(genotype, phenotype, 1)[0];
  // }
  //
  // /**
  // * This method can be used to evaluate a single phenotype.
  // *
  // * @param genotype
  // * the genotype (or <code>null</code>)
  // * @param phenotype
  // * the phenotype to be evaluated (or <code>null</code>)
  // * @param iterations
  // * the number of test iterations to perform
  // * @return an individual record containing the evaluated data
  // */
  // @SuppressWarnings("unchecked")
  // public IIndividual<?, ?>[] evaluate(final Serializable genotype,
  // final Serializable phenotype, final int iterations) {
  // IIndividual<Serializable, Serializable>[] ind;
  // IIndividual<Serializable, Serializable> indi;
  // EvaluationJob ev;
  // IJobSystem js;
  // IOptimizationInfo<?, ?> oi;
  // IWaitable w;
  // ISimulationManager sm;
  // int i;
  //
  // oi = this.doCreateOptimizationInfo();
  //
  // ind = new IIndividual[iterations];
  //
  // js = this.createJobSystem();
  // sm = js.getSimulationManager();
  // js.start();
  //
  // for (i = 0; i < iterations; i++) {
  //
  // ind[i] = indi = (IIndividual<Serializable, Serializable>) (oi
  // .getIndividualFactory().createIndividual(this.createObjectives()
  // .size()));
  //
  // indi.setGenotype(genotype);
  // indi.setPhenotype(phenotype);
  //
  // ev = new EvaluationJob(indi);
  //
  // IterationHookInvoker.invokeBeforeIterationHook(sm, i);
  //
  // w = js.executeOptimization(ev,
  // ((IJobInfo) (new JobInfo<Serializable, Serializable>(
  // (IOptimizationInfo) oi))));
  //
  // w.waitFor(false);
  //
  // IterationHookInvoker.invokeAfterIterationHook(sm, i);
  // }
  //
  // js.abort();
  // js.waitFor(false);
  //
  // return ind;
  // }

  /**
   * Create the job system
   * 
   * @return the job system
   */
  protected IJobSystem createJobSystem() {
    if (this.getProcessorCount() > 1) {
      return new MultiProcessorJobSystem(this.createSimulationManager()) {
        @Override
        protected void onError(final Throwable t) {
          TestSeries.this.onError(t);
        }
      };
    }

    return new SingleProcessorJobSystem(this.createSimulationManager()) {
      @Override
      protected void onError(final Throwable t) {
        TestSeries.this.onError(t);
      }
    };
  }

  /**
   * This method is called whenever an error occured.
   * 
   * @param t
   *          the error
   */
  @Override
  protected void onError(final Throwable t) {
    this.cleanUp();
    super.onError(t);
  }

  /**
   * create the embryogeny
   * 
   * @return the embryogeny
   */
  @SuppressWarnings("unchecked")
  protected IEmbryogeny<?, ?> createEmbryogeny() {
    return CopyEmbryogeny.COPY_EMBRYOGENY;
  }

  /**
   * Creates the optimizer to be used for the current run
   * 
   * @return the optimizer to be used for the current run
   */
  protected abstract IOptimizer<?, ?> createOptimizer();

  /**
   * create the creator
   * 
   * @return the creator
   */
  protected abstract ICreator<?> createCreator();

  /**
   * create the mutator
   * 
   * @return the mutator
   */
  protected abstract IMutator<?> createMutator();

  /**
   * create the crossover
   * 
   * @return the crossover
   */
  protected abstract ICrossover<?> createCrossover();

  /**
   * Create a new optimization information record.
   * 
   * @param comparator
   *          The comparator to be used for the optimization. If this is
   *          <code>null</code>, an instance of the
   *          <code>ParetoComparator</code> is used.
   * @param evaluator
   *          The evaluator to be used in order to determine the objective
   *          function values of the individuals. This parameter must not
   *          be <code>null</code>
   * @param embryogeny
   *          The breeder instance to be used in order to create a
   *          phenotype from a genotype.
   * @param creator
   *          The creator instance to be used in order to create new
   *          genotype instances. This parameter must not be
   *          <code>null</code>
   * @param mutator
   *          The mutator which creates one new genotype instance from an
   *          existing one. This parameter must not be <code>null</code>
   * @param crossover
   *          The crossover instance used to create one new genotype
   *          instance from two existing ones. This parameter must not be
   *          <code>null</code>
   * @param individualFactory
   *          the individual factory to be used, if <code>null</code>, a
   *          default factory is used
   * @return the new optimization info record
   * @throws NullPointerException
   *           if the
   *           <code>evaluator==null || breeder==null || creator==null || mutator==null || crossover==null</code>.
   * @throws IllegalArgumentException
   *           if
   *           <code>(comparator == FitnessComparator.FITNESS_COMPARATOR)</code>
   */
  @SuppressWarnings("unchecked")
  protected IOptimizationInfo<?, ?> createOptimizationInfo(
      final IEvaluator<?> evaluator, final IComparator comparator,
      final IEmbryogeny<?, ?> embryogeny, final ICreator<?> creator,
      final IMutator<?> mutator, final ICrossover<?> crossover,
      final IIndividualFactory<?, ?> individualFactory) {
    return new OptimizationInfo<Serializable, Serializable>(
        (IEvaluator) evaluator, comparator, (IEmbryogeny) embryogeny,
        (ICreator) creator, (IMutator) mutator, (ICrossover) crossover,
        (IIndividualFactory) individualFactory);
  }

  /**
   * Create the optimization information record internally.
   * 
   * @return the optimization information record
   */
  protected IOptimizationInfo<?, ?> doCreateOptimizationInfo() {
    return this.createOptimizationInfo(this.createEvaluator(),//
        this.createComparator(),//
        this.createEmbryogeny(),//
        this.createCreator(), //
        this.createMutator(),//
        this.createCrossover(),//
        this.createIndividualFactory());
  }
}
