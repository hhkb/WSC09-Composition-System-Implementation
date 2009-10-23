/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-29
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.utils.testSeries.ea.EATestSeries.java
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

package org.sigoa.refimpl.utils.testSeries.ea;

import java.io.File;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.sfc.io.Files;
import org.sfc.io.TextWriter;
import org.sfc.io.writerProvider.FileTextWriterProvider;
import org.sigoa.refimpl.adaptation.MaxIterationRule;
import org.sigoa.refimpl.go.algorithms.ea.EA;
import org.sigoa.refimpl.go.algorithms.ea.ElitistEA;
import org.sigoa.refimpl.go.evaluation.Evaluator;
import org.sigoa.refimpl.pipe.EqualObjectiveDegenerator;
import org.sigoa.refimpl.pipe.SorterPipe;
import org.sigoa.refimpl.pipe.stat.IndividualPrinterPipe;
import org.sigoa.refimpl.pipe.stat.ObjectivePrinterPipe;
import org.sigoa.refimpl.pipe.stat.PrinterPipe;
import org.sigoa.refimpl.simulation.IterationHookInvoker;
import org.sigoa.refimpl.utils.testSeries.EDataGrouping;
import org.sigoa.refimpl.utils.testSeries.ISuccessFilter;
import org.sigoa.refimpl.utils.testSeries.LoggingSelection;
import org.sigoa.refimpl.utils.testSeries.ParameterizedTestSeries;
import org.sigoa.refimpl.utils.testSeries.SuccessFilterPipe;
import org.sigoa.spec.adaptation.IRule;
import org.sigoa.spec.go.IOptimizer;
import org.sigoa.spec.go.evaluation.IEvaluator;
import org.sigoa.spec.go.reproduction.ICrossoverParameters;
import org.sigoa.spec.go.reproduction.IHysteresisReproduction;
import org.sigoa.spec.go.reproduction.IMutatorParameters;
import org.sigoa.spec.go.reproduction.ISteadyStateReproduction;
import org.sigoa.spec.pipe.IPipe;

/**
 * A test series for evolutionary algorithms
 * 
 * @author Thomas Weise
 */
public abstract class EATestSeries extends ParameterizedTestSeries
    implements ISteadyStateReproduction, IMutatorParameters,
    ICrossoverParameters, IHysteresisReproduction {
  /**
   * the default maximum generations
   */
  public static final int DEFAULT_MAX_GENERATIONS = 500;

  /**
   * the default population size
   */
  public static final int DEFAULT_POPULATION_SIZE = 1024;

  /**
   * the default archive size
   */
  public static final int DEFAULT_ARCHIVE_SIZE = DEFAULT_POPULATION_SIZE >>> 3;

  /**
   * the default crossover rate
   */
  public static final double DEFAULT_CROSSOVER_RATE = 0.7d;

  /**
   * the default mutation rate
   */
  public static final double DEFAULT_MUTATION_RATE = (1.0d - DEFAULT_CROSSOVER_RATE);

  /**
   * log the best individuals that are built each generation
   */
  public static final LoggingSelection BEST_INDIVIDUALS = //
  new LoggingSelection("bestIndividuals"); //$NON-NLS-1$

  /**
   * log the objective values of the best individuals that are built each
   * generation
   */
  public static final LoggingSelection BEST_OBJECTIVES = //
  new LoggingSelection("bestObjectives");//$NON-NLS-1$

  /**
   * the maximum generations allowed per test series
   */
  private volatile int m_maxGenerations;

  /**
   * the ea factory
   */
  private volatile IEAFactory m_eaFactory;

  /**
   * the steady state value
   */
  private volatile boolean m_steadyState;

  /**
   * the mutation rate
   */
  private volatile double m_mutationRate;

  /**
   * the crossover rate
   */
  private volatile double m_crossoverRate;

  /**
   * should hysteresis be applied
   */
  private volatile boolean m_usesHysteresis;

  /**
   * the hysteresis level to be applied
   */
  private volatile double m_hysteresis;

  /**
   * the population size.
   */
  private volatile int m_populationSize;

  /**
   * the archive size
   */
  private volatile int m_archiveSize;

  /**
   * the number of test cases
   */
  private volatile int m_testCases;

  /**
   * <code>true</code> if and only if the test cases change
   */
  private volatile boolean m_changingTestCases;

  /**
   * the degree of convergence prevention
   */
  private volatile double m_convergencePrevention;

  /**
   * the success text writer
   */
  private transient TextWriter m_success;

  /**
   * the success individual text writer
   */
  private transient TextWriter m_successOutput;

  /**
   * terminate successful runs
   */
  private boolean m_terminateOnSuccess;

  /**
   * the limit for the best individuals
   */
  private volatile int m_biLimit;

  /**
   * Create a new parameterized test series based on evolutionary
   * algorithms
   * 
   * @param dir
   *          the directory to write to
   * @param grouping
   *          how the data should be grouped
   * @param maxRuns
   *          the maximum runs this test series is running
   */
  public EATestSeries(final Object dir, final EDataGrouping grouping,
      final int maxRuns) {
    this(dir, grouping, maxRuns, null, null);
  }

  /**
   * Create a new parameterized test series based on evolutionary
   * algorithms
   * 
   * @param titles
   *          the titles of the test series parameters
   * @param iterator
   *          the parameter iterator
   * @param dir
   *          the directory to write to
   * @param grouping
   *          how the data should be grouped
   * @param maxRuns
   *          the maximum runs this test series is running
   */
  public EATestSeries(final Object dir, final EDataGrouping grouping,
      final int maxRuns, final String[] titles,
      final Iterator<Object[]> iterator) {
    super(dir, grouping, maxRuns, titles, iterator);

    BEST_INDIVIDUALS.getPrefix();
    BEST_OBJECTIVES.getPrefix();

    this.m_maxGenerations = DEFAULT_MAX_GENERATIONS;
    this.m_eaFactory = EAFactories.PLAIN_EA;
    this.m_populationSize = DEFAULT_POPULATION_SIZE;// 1024;
    this.m_archiveSize = DEFAULT_ARCHIVE_SIZE;// 128;
    this.m_testCases = 1;
    this.m_crossoverRate = DEFAULT_CROSSOVER_RATE;// 0.7d;
    this.m_mutationRate = DEFAULT_MUTATION_RATE;// 0.7d;
    this.m_terminateOnSuccess = true;
    this.m_biLimit = -1;
  }

  /**
   * Obtain the ea factory
   * 
   * @return the ea factory
   */
  public IEAFactory getEAFactory() {
    return this.m_eaFactory;
  }

  /**
   * Set the ea factory
   * 
   * @param factory
   *          the factory for evolutionary algorithms to be used
   */
  public void setEAFactory(final IEAFactory factory) {
    if (factory != null)
      this.m_eaFactory = factory;
  }

  /**
   * Obtain the maximum number of generations to perform
   * 
   * @return the maximum number of generations to perform
   */
  public int getMaxGenerations() {
    return this.m_maxGenerations;
  }

  /**
   * Set the maximum number of generations to perform
   * 
   * @param maxGenerations
   *          the maximum number of generations to perform
   */
  public void setMaxGenerations(final int maxGenerations) {
    this.m_maxGenerations = maxGenerations;
  }

  /**
   * Creates the optimizer to be used for the current run
   * 
   * @return the optimizer to be used for the current run
   */
  @Override
  protected IOptimizer<?, ?> createOptimizer() {
    EA<?, ?> ea;
    ea = this.createEA();
    this.initializeEA(ea);
    this.setupEALogging(ea);
    return ea;
  }

  /**
   * create the evolutionary algorithm
   * 
   * @return the ea
   */
  protected EA<?, ?> createEA() {
    return this.m_eaFactory.createEA();
  }

  /**
   * Setup the logging for the evolutionary algorithm
   * 
   * @param ea
   *          the evolutionary algorithm
   */
  @SuppressWarnings("unchecked")
  protected synchronized void setupEALogging(final EA<?, ?> ea) {
    File f, f2;
    String pre;
    ISuccessFilter<?> filter;
    PrinterPipe p;

    f = this.getCurrentDirectory();

    if (this.getDataGrouping().ordinal() >= EDataGrouping.SERIES.ordinal())
      pre = ""; //$NON-NLS-1$
    else
      pre = this.getSeriesName() + Files.EXTENSION_SEPARATOR_STR;

    if (this.isLogging(BEST_INDIVIDUALS)
        || this.isLogging(BEST_OBJECTIVES)) {
      ea.addNonPrevailedPipe(//
          (IPipe) new SorterPipe<Serializable, Serializable>());
    }

    if (this.isLogging(BEST_INDIVIDUALS)) {
      f2 = Files.createDirectoryInside(f, pre
          + BEST_INDIVIDUALS.getPrefix());
      if (f2 != null) {
        p = new IndividualPrinterPipe<Serializable, Serializable>(//
            new FileTextWriterProvider(f2, null), true);
        p.setLimit(this.m_biLimit);
        ea.addNonPrevailedPipe(p);
        if (this.isVerbose())
          this.log("Logging best individuals of each generation to " + f2); //$NON-NLS-1$
      }
    }

    if (this.isLogging(BEST_OBJECTIVES)) {
      f2 = Files.createDirectoryInside(f, pre
          + BEST_OBJECTIVES.getPrefix());
      if (f2 != null) {
        if (this.isVerbose())
          this
              .log("Logging objectives of the best individuals of each generation to " + f2); //$NON-NLS-1$

        ea.addNonPrevailedPipe(//
            (IPipe) (new ObjectivePrinterPipe<Serializable, Serializable>(//
                new FileTextWriterProvider(f2, null), false)));
      }
    }

    if (this.isLogging(LoggingSelection.ALL_INDIVIDUALS)
        || this.isLogging(LoggingSelection.ALL_OBJECTIVES)) {
      ea.addPopulationPipe(//
          (IPipe) new SorterPipe<Serializable, Serializable>());
    }

    if (this.isLogging(LoggingSelection.ALL_OBJECTIVES)) {
      f2 = Files.createDirectoryInside(f, pre
          + LoggingSelection.ALL_OBJECTIVES.getPrefix());
      if (f2 != null) {
        if (this.isVerbose())
          this.log("Logging all objectives to " + f2); //$NON-NLS-1$

        ea.addPopulationPipe(//
            (IPipe) (new ObjectivePrinterPipe<Serializable, Serializable>(//
                new FileTextWriterProvider(f2, null), false)));
      }
    }

    if (this.isLogging(LoggingSelection.ALL_INDIVIDUALS)) {
      f2 = Files.createDirectoryInside(f, pre
          + LoggingSelection.ALL_INDIVIDUALS.getPrefix());
      if (f2 != null) {
        if (this.isVerbose())
          this.log("Logging all individuals to " + f2); //$NON-NLS-1$

        ea.addPopulationPipe(//
            (IPipe) (new IndividualPrinterPipe<Serializable, Serializable>(//
                new FileTextWriterProvider(f2, null), true)));
      }
    }

    if (((filter = this.getSuccessFilter()) != null)
        && (this.isLogging(LoggingSelection.OBSERVE_SUCCESS) || this
            .isLogging(LoggingSelection.OBSERVE_SUCCESS_INDIVIDUALS))) {
      try {
        if (this.isLogging(LoggingSelection.OBSERVE_SUCCESS)) {
          this.m_success = new TextWriter(Files.createFileInside(f,
              LoggingSelection.OBSERVE_SUCCESS.getPrefix(),
              Files.TEXT_FILE_SUFFIX));
        }
        if (this.isLogging(LoggingSelection.OBSERVE_SUCCESS_INDIVIDUALS)) {
          this.m_successOutput = new TextWriter(Files.createFileInside(f,
              LoggingSelection.OBSERVE_SUCCESS_INDIVIDUALS.getPrefix(),
              Files.TEXT_FILE_SUFFIX));
        }
        ea.addNonPrevailedPipe(//
            (SuccessFilterPipe) new SuccessFilterPipe<Serializable>(
                (ISuccessFilter) filter, this.m_terminateOnSuccess,
                (IOptimizer) ea, this.m_success, this.m_successOutput));
      } catch (Throwable t) {
        this.log(t);
      }
    }

  }

  /**
   * Limits the number of individuals allowed to be printed per iteration
   * in the best-individuals pipe
   * 
   * @param limit
   *          the maximum number of individuals that may be printed per
   *          iteration in the best-individuals pipe (-1 stands for
   *          infinite)
   */
  public void setBILimit(final int limit) {
    this.m_biLimit = limit;
  }

  /**
   * Obtain the maximum number of individuals that may be printed per
   * iteration in the best-individuals pipe
   * 
   * @return the maximum number of individuals that may be printed per
   *         iteration in the best-individuals pipe (-1 stands for
   *         infinite)
   */
  public int getBILimit() {
    return this.m_biLimit;
  }

  /**
   * this method is called after each iteration
   * 
   * @param iteration
   *          the iteration number
   * @param timeSpent
   *          the time spent in this iteration
   */
  @Override
  protected void afterIteration(final int iteration, final long timeSpent) {
    try {
      if (this.m_success != null) {
        this.m_success.close();
      }
    } catch (Throwable t) {
      this.log(t);
    } finally {
      this.m_success = null;
    }

    try {
      if (this.m_successOutput != null) {
        this.m_successOutput.close();
      }
    } catch (Throwable t) {
      this.log(t);
    } finally {
      this.m_successOutput = null;
    }

    super.afterIteration(iteration, timeSpent);
  }

  /**
   * Returns whether this algorithm is steady state or not.
   * 
   * @return <code>true</code> if and only if this algorithm is steady
   *         state, <code>false</code> otherwise
   */
  public boolean isSteadyState() {
    return this.m_steadyState;
  }

  /**
   * Set this algorithm to be steady state.
   * 
   * @param steadyState
   *          The new steady state property: <code>true</code> if and
   *          only if this algorithm should become steady state,
   *          <code>false</code> if it should not be steady state anymore
   */
  public synchronized void setSteadyState(final boolean steadyState) {
    this.m_steadyState = steadyState;
  }

  /**
   * Set the mutation rate. The mutation rate denotes the fraction of
   * individuals that will undergo a mutation.
   * 
   * @param rate
   *          the mutation rate which must be in <code>[0,1]</code>
   */
  public synchronized void setMutationRate(final double rate) {
    this.m_mutationRate = rate;
  }

  /**
   * Get the mutation rate. The mutation rate denotes the fraction of
   * individuals that will undergo a mutation.
   * 
   * @return the mutation rate
   */
  public synchronized double getMutationRate() {
    return this.m_mutationRate;
  }

  /**
   * Set the crossover rate. The crossover rate denotes the fraction of
   * individuals that will undergo a crossover.
   * 
   * @param rate
   *          the crossover rate which must be in <code>[0,1]</code>
   */
  public synchronized void setCrossoverRate(final double rate) {
    this.m_crossoverRate = rate;
  }

  /**
   * Get the crossover rate. The crossover rate denotes the fraction of
   * individuals that will undergo a crossover.
   * 
   * @return the mutation rate
   */
  public synchronized double getCrossoverRate() {
    return this.m_crossoverRate;
  }

  /**
   * Define whether this reproduction facility should apply hysteresis or
   * not.
   * 
   * @param hysteresis
   *          <code>true</code> if hysteresis should be applied,
   *          <code>false</code> otherwise
   */
  public synchronized void setHysteresisUsage(final boolean hysteresis) {
    this.m_usesHysteresis = hysteresis;
  }

  /**
   * Does this component apply hysteresis?
   * 
   * @return <code>true</code> if hysteresis is/cab be applied by this
   *         component, <code>false</code> otherwise
   */
  public boolean usesHysteresis() {
    return this.m_usesHysteresis;
  }

  /**
   * Set the hysteresis to be used in the evaluation
   * 
   * @param hysteresis
   *          the hysteresis
   */
  public synchronized void setHysteresis(final double hysteresis) {
    this.m_hysteresis = hysteresis;
  }

  /**
   * Obtain the hysteresis used
   * 
   * @return the hysteresis used
   */
  public synchronized double getHysteresis() {
    return (this.m_usesHysteresis ? this.m_hysteresis : 0.0d);
  }

  /**
   * Get the population size
   * 
   * @return the population size
   */
  public synchronized int getPopulationSize() {
    return this.m_populationSize;
  }

  /**
   * Set the population size
   * 
   * @param size
   *          the population size
   */
  public synchronized void setPopulationSize(final int size) {
    if (size > 0)
      this.m_populationSize = size;
  }

  /**
   * Get the archive size
   * 
   * @return the archive size
   */
  public synchronized int getArchiveSize() {
    return this.m_archiveSize;
  }

  /**
   * Set the archive size
   * 
   * @param size
   *          the archive size
   */
  public synchronized void setArchiveSize(final int size) {
    if (size > 0)
      this.m_archiveSize = size;
  }

  /**
   * Obtain the number of test cases
   * 
   * @return the number of test cases
   */
  public synchronized int getTestCaseCount() {
    return this.m_testCases;
  }

  /**
   * set the number of test cases
   * 
   * @param count
   *          the number of test cases
   */
  public synchronized void setTestCaseCount(final int count) {
    if (count > 0)
      this.m_testCases = count;
  }

  /**
   * check whether the test cases change or not
   * 
   * @return <code>true</code> if and only if test cases are changed each
   *         generation, <code>false</code> if they remain constant
   */
  public boolean areTestCasesChanging() {
    return this.m_changingTestCases;
  }

  /**
   * set whether the test cases should change or not
   * 
   * @param set
   *          <code>true</code> if and only if test cases are changed
   *          each generation, <code>false</code> if they remain constant
   */
  public synchronized void setTestCasesChanging(final boolean set) {
    this.m_changingTestCases = set;
  }

  /**
   * set the degree of convergence prevention
   * 
   * @param p
   *          the degree of convergence prevention
   */
  public synchronized void setConvergencePrevention(final double p) {
    if ((p >= 0d) && (p <= 1d))
      this.m_convergencePrevention = p;
  }

  /**
   * obtain the degree of convergence prevention
   * 
   * @return the degree of convergence prevention
   */
  public synchronized double getConvergencePrevention() {
    return this.m_convergencePrevention;
  }

  /**
   * Create the evaluator.
   * 
   * @return the evaluator
   */
  @Override
  @SuppressWarnings("unchecked")
  protected IEvaluator<?> createEvaluator() {
    return new Evaluator<Serializable>(((List) (this.createObjectives())),
        this.m_testCases, this.m_changingTestCases,
        (this.m_usesHysteresis ? this.m_hysteresis : 0.0d));
  }

  /**
   * initialize the given evolutionary algorithm
   * 
   * @param ea
   *          the evolutionary algorithm
   */
  @SuppressWarnings("unchecked")
  protected synchronized void initializeEA(final EA<?, ?> ea) {

    // / initialize the parameters

    ea.addIterationHook(IterationHookInvoker.ITERATION_HOOK_INVOKER);
    // ea.setGCIntervall(100);

    if (this.m_maxGenerations > 0) {
      ea.addRule((IRule) (new MaxIterationRule<Object>(
          this.m_maxGenerations)));
    }

    ea.setSteadyState(this.m_steadyState);
    ea.setMutationRate(this.m_mutationRate);
    ea.setCrossoverRate(this.m_crossoverRate);
    ea.setHysteresisUsage(this.m_usesHysteresis);
    ea.setNextPopulationSize(this.m_populationSize);

    if (ea instanceof ElitistEA)
      ((ElitistEA) ea).setMaxArchiveSize(this.m_archiveSize);

    if (this.m_convergencePrevention > 0d)
      ea.addPopulationPipe(//
          (IPipe) new EqualObjectiveDegenerator<Serializable, Serializable>(
              this.m_convergencePrevention));
  }

  /**
   * Log the parameters to the given text writer
   * 
   * @param w
   *          the text writer
   */
  @Override
  protected void writeParameters(final TextWriter w) {

    w.ensureNewLine();
    w.write("+++ actively set:");//$NON-NLS-1$

    super.writeParameters(w);
    w.ensureNewLine();
    w.write("---------------------------------"); //$NON-NLS-1$
    w.ensureNewLine();
    w.write("+++ all parameters:");//$NON-NLS-1$

    w.ensureNewLine();
    w.write("population size: "); //$NON-NLS-1$
    w.writeInt(this.m_populationSize);

    w.ensureNewLine();
    w.write("archive size: "); //$NON-NLS-1$
    w.writeInt(this.m_archiveSize);

    w.ensureNewLine();
    w.write("steady state?: "); //$NON-NLS-1$
    w.writeBoolean(this.m_steadyState);

    w.ensureNewLine();
    w.write("convergence prevention?: "); //$NON-NLS-1$
    w.writeDouble(this.m_convergencePrevention);

    w.ensureNewLine();
    w.write("# test cases: "); //$NON-NLS-1$
    w.writeInt(this.m_testCases);

    w.ensureNewLine();
    w.write("test cases change?: "); //$NON-NLS-1$
    w.writeBoolean(this.m_changingTestCases);

    w.ensureNewLine();
    w.write("crossover rate: "); //$NON-NLS-1$
    w.writeDouble(this.m_crossoverRate);

    w.ensureNewLine();
    w.write("mutation rate: "); //$NON-NLS-1$
    w.writeDouble(this.m_mutationRate);

    w.ensureNewLine();
    w.write("max generations: "); //$NON-NLS-1$
    w.writeInt(this.m_maxGenerations);

    w.ensureNewLine();
    w.write("ea factory: "); //$NON-NLS-1$
    w.write(this.m_eaFactory != null ? (this.m_eaFactory.toString())
        : null);

    w.ensureNewLine();
    w.write("uses hysteresis?: "); //$NON-NLS-1$
    w.writeBoolean(this.m_usesHysteresis);

    if (this.m_usesHysteresis) {

      w.ensureNewLine();
      w.write("hysteresis: "); //$NON-NLS-1$
      w.writeDouble(this.m_hysteresis);
    }

  }

  /**
   * Set whether the runs should be aborted if a perfect solution has been
   * found.
   * 
   * @param terminateRunsOnSuccess
   *          <code>true</code> if and only if the test runs should be
   *          aborted when a perfect solution has been found,
   *          <code>false</code> if they can continue
   */
  public void setTerminateRunsOnSuccess(
      final boolean terminateRunsOnSuccess) {
    this.m_terminateOnSuccess = terminateRunsOnSuccess;
  }

  /**
   * Check whether the runs should be aborted if a perfect solution has
   * been found.
   * 
   * @return <code>true</code> if and only if the test runs are aborted
   *         when a perfect solution has been found, <code>false</code>
   *         if they can continue
   */
  public boolean terminatesRunsOnSuccess() {
    return this.m_terminateOnSuccess;
  }
}
