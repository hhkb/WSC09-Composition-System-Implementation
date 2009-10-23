/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-08
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.algorithms.ea.EA.java
 * Last modification: 2008-04-15
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

package org.sigoa.refimpl.go.algorithms.ea;

import java.io.Serializable;

import org.sigoa.refimpl.go.Population;
import org.sigoa.refimpl.go.algorithms.IterativeOptimizer;
import org.sigoa.refimpl.go.embryogeny.EmbryogenyPipe;
import org.sigoa.refimpl.go.evaluation.ParallelEvaluatorPipe;
import org.sigoa.refimpl.go.evaluation.SequentialEvaluatorPipe;
import org.sigoa.refimpl.go.fitnessAssignment.PrevalenceFitnessAssigner2;
import org.sigoa.refimpl.go.reproduction.CreatorPipe;
import org.sigoa.refimpl.go.reproduction.CrossoverPipe;
import org.sigoa.refimpl.go.reproduction.MutatorPipe;
import org.sigoa.refimpl.go.selection.TournamentSelectionR;
import org.sigoa.refimpl.pipe.CopyPipe;
import org.sigoa.refimpl.pipe.NoEofPipe;
import org.sigoa.refimpl.pipe.NonPrevalenceFilter;
import org.sigoa.refimpl.pipe.Pipeline;
import org.sigoa.spec.go.IIndividual;
import org.sigoa.spec.go.IPassThroughParameters;
import org.sigoa.spec.go.IPopulation;
import org.sigoa.spec.go.algorithms.IEA;
import org.sigoa.spec.go.embryogeny.IEmbryogenyPipe;
import org.sigoa.spec.go.evaluation.IEvaluatorPipe;
import org.sigoa.spec.go.fitnessAssignment.IFitnessAssigner;
import org.sigoa.spec.go.reproduction.ICreatorPipe;
import org.sigoa.spec.go.reproduction.ICrossoverParameters;
import org.sigoa.spec.go.reproduction.ICrossoverPipe;
import org.sigoa.spec.go.reproduction.IHysteresisReproduction;
import org.sigoa.spec.go.reproduction.IMutatorParameters;
import org.sigoa.spec.go.reproduction.IMutatorPipe;
import org.sigoa.spec.go.reproduction.ISteadyStateReproduction;
import org.sigoa.spec.go.selection.ISelectionAlgorithm;
import org.sigoa.spec.jobsystem.IExecutionInfo;
import org.sigoa.spec.jobsystem.IHost;
import org.sigoa.spec.jobsystem.IJobInfo;
import org.sigoa.spec.pipe.IPipe;
import org.sigoa.spec.pipe.IPipeIn;

/**
 * The base class for evolutionary algorithms.
 * 
 * @param <G>
 *          the genotype
 * @param <PP>
 *          the phenotype
 * @author Thomas Weise
 */
public class EA<G extends Serializable, PP extends Serializable> extends
    IterativeOptimizer<G, PP> implements ISteadyStateReproduction,
    IHysteresisReproduction, IEA<G, PP> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the steady state property
   */
  private volatile boolean m_steadyState;

  /**
   * the hysteresis property
   */
  private volatile boolean m_hysteresis;

  /**
   * the first internal population
   */
  private volatile IPopulation<G, PP> m_pop1;

  /**
   * the second internal population
   */
  private volatile IPopulation<G, PP> m_pop2;

  /**
   * the next population size
   */
  private volatile int m_nextPopulationSize;

  /**
   * the inner pipeline
   */
  private final Pipeline<G, PP> m_innerPipe;

  /**
   * the mutation rate
   */
  private volatile double m_mutationRate;

  /**
   * the crossover rate
   */
  private volatile double m_crossoverRate;

  /**
   * the output pipe
   */
  private volatile IPipe<G, PP> m_outputPipe;

  /**
   * Create a new evolutionary algorithm.
   */
  public EA() {
    this(1024, 0.2, 0.8);
  }

  /**
   * Create a new evolutionary algorithm.
   * 
   * @param initialPopulationSize
   *          the initial size of the population
   * @param mutationRate
   *          the mutation rate
   * @param crossoverRate
   *          the crossover rate
   * @param steadyState
   *          <code>true</code> if and only if this algorithm is steady
   *          state, <code>false</code> otherwise
   * @param hysteresis
   *          should hysteresis be applied?
   * @throws IllegalArgumentException
   *           if
   *           <code>(initialPopulationSize &lt;= 0) || Double.isNaN(mutationRate)
   || (mutationRate &lt; 0.0d) || (mutationRate &gt; 1.0)
   || Double.isNaN(crossoverRate) || (crossoverRate &lt; 0.0d)
   || (crossoverRate &gt; 1.0)</code>
   */
  public EA(final int initialPopulationSize, final double mutationRate,
      final double crossoverRate, final boolean steadyState,
      final boolean hysteresis) {
    super();

    if ((initialPopulationSize <= 0) || Double.isNaN(mutationRate)
        || (mutationRate < 0.0d) || (mutationRate > 1.0)
        || Double.isNaN(crossoverRate) || (crossoverRate < 0.0d)
        || (crossoverRate > 1.0))
      throw new IllegalArgumentException();

    this.m_nextPopulationSize = initialPopulationSize;
    this.m_crossoverRate = crossoverRate;
    this.m_mutationRate = mutationRate;
    this.m_steadyState = steadyState;
    this.m_hysteresis = hysteresis;

    this.m_innerPipe = this.createPipeline();

    this.m_pop1 = new Population<G, PP>(initialPopulationSize);
    this.m_pop2 = new Population<G, PP>(initialPopulationSize);
  }

  /**
   * Create a new steady state, non-hysteresis Evolutionary algorithm.
   * 
   * @param initialPopulationSize
   *          the initial size of the population
   * @param mutationRate
   *          the mutation rate
   * @param crossoverRate
   *          the crossover rate
   * @throws IllegalArgumentException
   *           if
   *           <code>(initialPopulationSize &lt;= 0) || Double.isNaN(mutationRate)
   || (mutationRate &lt; 0.0d) || (mutationRate &gt; 1.0)
   || Double.isNaN(crossoverRate) || (crossoverRate &lt; 0.0d)
   || (crossoverRate &gt; 1.0)</code>
   */
  public EA(final int initialPopulationSize, final double mutationRate,
      final double crossoverRate) {
    this(initialPopulationSize, mutationRate, crossoverRate, true, false);
  }

  /**
   * Create the pipeline which should internally be used for the individual
   * creation and evaluation.
   * 
   * @return the reproduction pipeline
   */
  protected Pipeline<G, PP> createPipeline() {
    Pipeline<G, PP> p;
    IPipe<G, PP> x;

    p = new Pipeline<G, PP>();

    x = this.createFitnessAssigner();
    if (x != null)
      p.add(x);
    x = this.createSelectionAlgorithm();
    if (x != null)
      p.add(x);
    x = this.createCrossoverPipe();
    if (x != null) {
      p.add(x);
      if (x instanceof IHysteresisReproduction) {
        ((IHysteresisReproduction) x)
            .setHysteresisUsage(this.m_hysteresis);
      }
      if (x instanceof ISteadyStateReproduction) {
        ((ISteadyStateReproduction) x).setSteadyState(this.m_steadyState);
      }
    }

    x = this.createMutatorPipe();
    if (x != null) {
      p.add(x);
      if (x instanceof IHysteresisReproduction) {
        ((IHysteresisReproduction) x)
            .setHysteresisUsage(this.m_hysteresis);
      }
      if (x instanceof ISteadyStateReproduction) {
        ((ISteadyStateReproduction) x).setSteadyState(this.m_steadyState);
      }
    }
    x = this.createCreatorPipe();
    if (x != null)
      p.add(x);
    x = this.createEmbryogenyPipe();
    if (x != null)
      p.add(x);
    x = this.createEvaluatorPipe();
    if (x != null)
      p.add(x);

    return p;
  }

  /**
   * Create the creation pipe to be used by this ea. This method is invoked
   * by <code>createPipeline</code>.
   * 
   * @return the creation pipe to be used by this ea
   */
  protected ICreatorPipe<G, PP> createCreatorPipe() {
    return new CreatorPipe<G, PP>(this.getNextPopulationSize());
  }

  /**
   * Create the mutation pipe to be used by this ea. This method is invoked
   * by <code>createPipeline</code>.
   * 
   * @return the mutation pipe to be used by this ea
   */
  protected IMutatorPipe<G, PP> createMutatorPipe() {
    return new MutatorPipe<G, PP>(this.isSteadyState(), this
        .usesHysteresis(), this.getMutationRate());
  }

  /**
   * Create the crossover pipe to be used by this ea. This method is
   * invoked by <code>createPipeline</code>.
   * 
   * @return the crossover pipe to be used by this ea
   */
  protected ICrossoverPipe<G, PP> createCrossoverPipe() {
    return new CrossoverPipe<G, PP>(this.isSteadyState(), this
        .usesHysteresis(), this.getCrossoverRate());
  }

  /**
   * Create the selection algorithm to be used by this ea. This method is
   * invoked by <code>createPipeline</code>.
   * 
   * @return the selection algorithm to be used by this ea
   */
  protected ISelectionAlgorithm<G, PP> createSelectionAlgorithm() {
    return new TournamentSelectionR<G, PP>(true, this
        .getNextPopulationSize(), 5);
  }

  /**
   * Create the embryogeny pipe to be used by this ea. This method is
   * invoked by <code>createPipeline</code>.
   * 
   * @return the embryogeny pipe to be used by this ea
   */
  protected IEmbryogenyPipe<G, PP> createEmbryogenyPipe() {
    return new EmbryogenyPipe<G, PP>();
  }

  /**
   * Create the evaluator pipe to be used by this ea. This method is
   * invoked by <code>createPipeline</code>.
   * 
   * @return the evaluator pipe to be used by this ea
   */
  protected IEvaluatorPipe<G, PP> createEvaluatorPipe() {
    IHost h;
    IJobInfo<G, PP> ji;
    IExecutionInfo ei;
    int maxP;

    maxP = 1;
    h = this.getHost();
    if (h != null) {
      ji = h.getJobInfo();
      if (ji != null) {
        ei = ji.getExecutionInfo();
        if (ei != null) {
          maxP = ei.getMaxProcessorCount();
        }
      }
    }

    if (maxP > 1)
      return new ParallelEvaluatorPipe<G, PP>();
    return new SequentialEvaluatorPipe<G, PP>();
  }

  /**
   * Create the fitness assigner to be used by this ea. This method is
   * invoked by <code>createPipeline</code>.
   * 
   * @return the fitness assigner pipe to be used by this ea
   */
  protected IFitnessAssigner<G, PP> createFitnessAssigner() {
    return new PrevalenceFitnessAssigner2<G, PP>();
  }

  /**
   * Obtain the current population size.
   * 
   * @return the current population size of the evolutionary algorithm
   */
  public int getPopulationSize() {
    return this.m_pop2.size();
  }

  /**
   * Obtain the population size that will be set in the next generation.
   * 
   * @return the next population size of the evolutionary algorithm
   */
  public int getNextPopulationSize() {
    return this.m_nextPopulationSize;
  }

  /**
   * Set the population size for the next generation. Setting this property
   * will take effect in the next generation.
   * 
   * @param size
   *          The next population size.
   * @throws IllegalArgumentException
   *           if <code>size&lt;0</code>
   */
  public void setNextPopulationSize(final int size) {
    int i;
    IPipe<G, PP> p;
    Pipeline<G, PP> x;

    if (size <= 0)
      throw new IllegalArgumentException();
    this.m_nextPopulationSize = size;

    x = this.m_innerPipe;
    for (i = (x.size() - 1); i >= 0; i--) {
      p = x.get(i);
      if (p instanceof IPassThroughParameters) {
        ((IPassThroughParameters) p).setPassThroughCount(size);
      }
    }
  }

  /**
   * this method is invoked for each iteration performed
   * 
   * @param index
   *          the index of the current itertion
   */
  @Override
  protected void iteration(final long index) {
    synchronized (this.m_pop1) {
      this.m_pop1.writeToPipe(this.m_innerPipe);
    }
  }

  /**
   * this method is invoked before each iteration is performed
   * 
   * @param index
   *          the index of the current itertion
   */
  @Override
  protected void beforeIteration(final long index) {//
    IPopulation<G, PP> p;
    super.beforeIteration(index);

    synchronized (this) {
      p = this.m_pop1;
      this.m_pop1 = this.m_pop2;
      this.m_pop2 = p;
    }
    p.clear();
    this.m_innerPipe.setOutputPipe(p);
  }

  /**
   * this method is invoked after each iteration is performed
   * 
   * @param index
   *          the index of the current itertion
   */
  @Override
  protected void afterIteration(final long index) {//
    this.getHost().flushJobs();
    super.afterIteration(index);
  }

  /**
   * Set the mutation rate. The mutation rate denotes the fraction of
   * individuals that will undergo a mutation.
   * 
   * @param rate
   *          the mutation rate which must be in <code>[0,1]</code>
   * @throws IllegalArgumentException
   *           if <code>rate</code> not in <code>[0,1]</code>
   */
  public void setMutationRate(final double rate) {
    int i;
    IPipe<G, PP> p;
    Pipeline<G, PP> x;

    if (Double.isNaN(rate) || (rate < 0) || (rate > 1))
      throw new IllegalArgumentException();

    this.m_mutationRate = rate;

    x = this.m_innerPipe;
    synchronized (x) {
      for (i = (x.size() - 1); i >= 0; i--) {
        p = x.get(i);
        if (p instanceof IMutatorParameters) {
          ((IMutatorParameters) p).setMutationRate(rate);
        }
      }
    }

  }

  /**
   * Get the mutation rate. The mutation rate denotes the fraction of
   * individuals that will undergo a mutation.
   * 
   * @return the mutation rate
   */
  public double getMutationRate() {
    return this.m_mutationRate;
  }

  /**
   * Set the crossover rate. The crossover rate denotes the fraction of
   * individuals that will undergo a crossover.
   * 
   * @param rate
   *          the crossover rate which must be in <code>[0,1]</code>
   * @throws IllegalArgumentException
   *           if <code>rate</code> not in <code>[0,1]</code>
   */
  public void setCrossoverRate(final double rate) {
    int i;
    IPipe<G, PP> p;
    Pipeline<G, PP> x;

    if (Double.isNaN(rate) || (rate < 0) || (rate > 1))
      throw new IllegalArgumentException();

    this.m_crossoverRate = rate;

    x = this.m_innerPipe;
    synchronized (x) {
      for (i = (x.size() - 1); i >= 0; i--) {
        p = x.get(i);
        if (p instanceof ICrossoverParameters) {
          ((ICrossoverParameters) p).setCrossoverRate(rate);
        }
      }
    }
  }

  /**
   * Obtain the inner pipeline used to compute the individuals.
   * 
   * @return the inner pipeline used to compute the individuals
   */
  public Pipeline<G, PP> getPipeline() {
    return this.m_innerPipe;
  }

  /**
   * Write a new individual into the pipe.
   * 
   * @param individual
   *          The new individual to be written.
   * @throws NullPointerException
   *           if <code>individual</code> is <code>null</code>.
   */
  @Override
  public synchronized void write(final IIndividual<G, PP> individual) {
    this.m_pop2.write(individual);
    // this.m_innerPipe.write(individual);
  }

  /**
   * Propagate an <code>eof</code> to the next pipe stage if needed.
   */
  @Override
  public void eof() {
    // this.m_innerPipe.eof();//TODO: is this correct here?
  }

  /**
   * Get the crossover rate. The crossover rate denotes the fraction of
   * individuals that will undergo a crossover.
   * 
   * @return the crossover rate
   */
  public double getCrossoverRate() {
    return this.m_crossoverRate;
  }

  /**
   * Set that this activity is finished. This leads to a transition into
   * the state <code>TERMINATED</code>.
   */
  @Override
  protected void finished() {
    IPipeIn<? super G, ? super PP> p1;
    IPipe<G, PP> p2, p3;

    p1 = this.getOutputPipe();

    if (p1 != null) {
      p2 = new NonPrevalenceFilter<G, PP>();
      p2.setOutputPipe(p1);
      p3 = new NoEofPipe<G, PP>();
      p3.setOutputPipe(p2);
      this.outputResults(p3);
      p2.eof();
    }

    super.finished();
  }

  /**
   * This method is called when we've finished our work. A pipe is passed
   * in to which <em>all</em> individuals evolved that could possible be
   * solutions should be written to. The pipe performs a filtering for
   * non-prevailed individuals and allows only those to pass.
   * 
   * @param dest
   *          the destination pipe to write to
   */
  protected void outputResults(final IPipeIn<G, PP> dest) {
    synchronized (this.m_pop1) {
      this.m_pop1.writeToPipe(dest);
    }

    synchronized (this.m_pop2) {
      this.m_pop2.writeToPipe(dest);
    }
  }

  /**
   * Reset this optimization algorithm. This does also reset the iteration
   * counter but doesn't affect the total iteration counter.
   */
  @Override
  public void reset() {
    super.reset();
    this.m_innerPipe.eof();
    synchronized (this.m_pop1) {
      this.m_pop1.clear();
    }
    synchronized (this.m_pop2) {
      this.m_pop2.clear();
    }
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
    Pipeline<G, PP> ip;
    int i;
    IPipe<G, PP> p;

    if (steadyState != this.m_steadyState) {
      this.m_steadyState = steadyState;

      ip = this.m_innerPipe;
      for (i = (ip.size() - 1); i >= 0; i--) {
        p = ip.get(i);
        if (p instanceof ISteadyStateReproduction) {
          ((ISteadyStateReproduction) p).setSteadyState(steadyState);
        }
      }
    }
  }

  /**
   * Does this reproduction facility apply hysteresis?
   * 
   * @return <code>true</code> if hysteresis should be applied,
   *         <code>false</code> otherwise
   */
  public boolean usesHysteresis() {
    return this.m_hysteresis;
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
    Pipeline<G, PP> ip;
    int i;
    IPipe<G, PP> p;

    if (hysteresis != this.m_hysteresis) {
      this.m_hysteresis = hysteresis;

      ip = this.m_innerPipe;
      for (i = (ip.size() - 1); i >= 0; i--) {
        p = ip.get(i);
        if (p instanceof IHysteresisReproduction) {
          ((IHysteresisReproduction) p).setHysteresisUsage(hysteresis);
        }
      }
    }
  }

  /**
   * Add a new pipeline to which, after each generation, the best
   * individuals should be written.
   * 
   * @param output
   *          the output pipeline
   * @throws NullPointerException
   *           if <code>output==null</code>
   */
  public synchronized void addNonPrevailedPipe(final IPipe<G, PP> output) {
    CopyPipe<G, PP> cp;

    if (output == null)
      throw new NullPointerException();

    if (this.m_outputPipe == null) {
      this.m_innerPipe.add(cp = new CopyPipe<G, PP>());
      this.m_outputPipe = new NonPrevalenceFilter<G, PP>();
      cp.setCopyPipe(this.m_outputPipe);
    }

    this.m_outputPipe.setOutputPipe(output);
    this.m_outputPipe = output;
  }

  /**
   * Add a new pipeline to which, after each generation, all individuals
   * should be written. Warning: this pipeline must write all the incoming
   * individuals to its output, otherwise the EA will not work.
   * 
   * @param output
   *          the output pipeline
   * @throws NullPointerException
   *           if <code>output==null</code>
   */
  public synchronized void addPopulationPipe(final IPipe<G, PP> output) {
    if (output == null)
      throw new NullPointerException();
    this.m_innerPipe.add(output);
  }
}
