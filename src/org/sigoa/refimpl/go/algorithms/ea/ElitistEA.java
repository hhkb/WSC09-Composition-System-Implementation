/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-14
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.algorithms.ea.ElitistEA.java
 * Last modification: 2007-06-27
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

import org.sfc.math.Mathematics;
import org.sigoa.refimpl.clustering.algorithms.ClassClustering;
import org.sigoa.refimpl.clustering.algorithms.NNearestNeighborClustering;
import org.sigoa.refimpl.go.Population;
import org.sigoa.refimpl.pipe.CopyPipe;
import org.sigoa.refimpl.pipe.NoEofPipe;
import org.sigoa.refimpl.pipe.NonPrevalenceFilter;
import org.sigoa.refimpl.pipe.Pipeline;
import org.sigoa.spec.clustering.IClusteringAlgorithm;
import org.sigoa.spec.go.IPassThroughParameters;
import org.sigoa.spec.go.IPopulation;
import org.sigoa.spec.go.algorithms.IElitistAlgorithm;
import org.sigoa.spec.jobsystem.EActivityState;
import org.sigoa.spec.pipe.IPipe;
import org.sigoa.spec.pipe.IPipeIn;

/**
 * the base class for elitist evolutionary algorithms
 * 
 * @param <G>
 *          the genotype
 * @param <PP>
 *          the phenotype
 * @author Thomas Weise
 */
public class ElitistEA<G extends Serializable, PP extends Serializable>
    extends EA<G, PP> implements IElitistAlgorithm {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the first internal archive
   */
  private volatile IPopulation<G, PP> m_arch1;

  /**
   * the second internal archive
   */
  private volatile IPopulation<G, PP> m_arch2;

  /**
   * the maximum archive size
   */
  private volatile int m_maxArchiveSize;

  /**
   * the pipe filling the archive
   */
  private final Pipeline<G, PP> m_archivePipe;

  /**
   * the output pipeline for the archive.
   */
  private final IPipeIn<G, PP> m_archOut;

  /**
   * the archive input pipe
   */
  private final IPipeIn<G, PP> m_archIn;

  /**
   * Create a new elitist evolutionary algorithm.
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
   * @param maxArchiveSize
   *          the maximum size of the archive containing the optimal
   *          individuals
   * @throws IllegalArgumentException
   *           if
   *           <code>(initialPopulationSize &lt;= 0) || Double.isNaN(mutationRate)
   || (mutationRate &lt; 0.0d) || (mutationRate &gt; 1.0)
   || Double.isNaN(crossoverRate) || (crossoverRate &lt; 0.0d)
   || (crossoverRate &gt; 1.0) || (maxArchiveSize &lt; 0) </code>
   */
  public ElitistEA(final int initialPopulationSize,
      final double mutationRate, final double crossoverRate,
      final boolean steadyState, final boolean hysteresis,
      final int maxArchiveSize) {
    super(initialPopulationSize, mutationRate, crossoverRate, steadyState,
        hysteresis);

    CopyPipe<G, PP> c;
    NoEofPipe<G, PP> a2;

    this.m_arch1 = new Population<G, PP>();
    this.m_arch2 = new Population<G, PP>();

    if (maxArchiveSize < 0)
      throw new IllegalArgumentException();
    this.m_maxArchiveSize = maxArchiveSize;

    this.m_archivePipe = this.createArchivePipeline();
    c = new CopyPipe<G, PP>();
    this.getPipeline().add(c);
    // a2 = new NoEofPipe<G, PP>();
    // a2.setOutputPipe(this.m_archivePipe);
    // c.setCopyPipe(a2);
    c.setCopyPipe(this.m_archivePipe);
    this.m_archIn = a2 = new NoEofPipe<G, PP>();
    a2.setOutputPipe(this.m_archivePipe);

    this.m_archOut = this.createArchiveOutput();
  }

  /**
   * Create a new elitist, steady-state evolutionary algorithm.
   * 
   * @param initialPopulationSize
   *          the initial size of the population
   * @param mutationRate
   *          the mutation rate
   * @param crossoverRate
   *          the crossover rate
   * @param maxArchiveSize
   *          the maximum size of the archive containing the optimal
   *          individuals
   * @throws IllegalArgumentException
   *           if
   *           <code>(initialPopulationSize &lt;= 0) || Double.isNaN(mutationRate)
   || (mutationRate &lt; 0.0d) || (mutationRate &gt; 1.0)
   || Double.isNaN(crossoverRate) || (crossoverRate &lt; 0.0d)
   || (crossoverRate &gt; 1.0) || (maxArchiveSize &lt; 0) </code>
   */
  public ElitistEA(final int initialPopulationSize,
      final double mutationRate, final double crossoverRate,
      final int maxArchiveSize) {
    this(initialPopulationSize, mutationRate, crossoverRate, true, false,
        maxArchiveSize);
  }

  /**
   * Create a new elitist, steady-state evolutionary algorithm.
   */
  public ElitistEA() {
    this(1024, 0.2, 0.8, 128);
  }

  /**
   * Create a new elitist evolutionary algorithm.
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
  public ElitistEA(final int initialPopulationSize,
      final double mutationRate, final double crossoverRate) {
    this(initialPopulationSize, mutationRate, crossoverRate, Mathematics
        .nextPrime((int) (Math.sqrt(initialPopulationSize) + 0.5d)));
  }

  /**
   * Create the output pipeline for the archive.
   * 
   * @return the output pipeline for the archive
   */
  protected IPipeIn<G, PP> createArchiveOutput() {
    NoEofPipe<G, PP> x;

    x = new NoEofPipe<G, PP>();

    x.setOutputPipe(this.getPipeline());

    return x;
  }

  /**
   * Obtain the maximal archive size.
   * 
   * @return the maximal archive size
   */
  public int getMaxArchiveSize() {
    return this.m_maxArchiveSize;
  }

  /**
   * this method is invoked for each iteration performed
   * 
   * @param index
   *          the index of the current itertion
   */
  @Override
  protected void iteration(final long index) {
    synchronized (this.m_arch1) {
      this.m_arch1.writeToPipe(this.m_archOut);
      this.m_arch1.writeToPipe(this.m_archIn);
    }

    super.iteration(index);
  }

  /**
   * Set the maximal archive size.
   * 
   * @param maxSize
   *          the new maximal archive size
   * @throws IllegalArgumentException
   *           if <code>maxSize&lt;=0</code>
   */
  public void setMaxArchiveSize(final int maxSize) {
    Pipeline<G, PP> p;
    int i;
    IPipe<G, PP> x;

    if (maxSize < 0)
      throw new IllegalArgumentException();
    this.m_maxArchiveSize = maxSize;

    p = this.m_archivePipe;
    synchronized (p) {
      for (i = (p.size() - 1); i >= 0; i--) {
        x = p.get(i);
        if (x instanceof IPassThroughParameters) {
          ((IPassThroughParameters) x).setPassThroughCount(maxSize);
        }
      }
    }
  }

  /**
   * Create the clustering algorithm to be used if the archive threats to
   * overflow.
   * 
   * @return the clustering algorithm to be applied in order to prune the
   *         archive
   */
  protected IClusteringAlgorithm<G, PP> createClusteringAlgorithm() {
    return new ClassClustering<G, PP>(this.m_maxArchiveSize, null, null,
        new NNearestNeighborClustering<G, PP>(this.m_maxArchiveSize, null,
            Mathematics.nextPrime((int) (Math.sqrt(this
                .getMaxArchiveSize())))));
  }

  /**
   * create the pipeline which will be used to fill the archive
   * 
   * @return the pipeline which will be used to fill the archive
   */
  protected Pipeline<G, PP> createArchivePipeline() {
    Pipeline<G, PP> p;
    IPipe<G, PP> x;

    p = new Pipeline<G, PP>();

    p.add(new NonPrevalenceFilter<G, PP>());
    x = this.createClusteringAlgorithm();
    if (x != null)
      p.add(x);

    return p;
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

    p = this.m_arch1;
    this.m_arch1 = this.m_arch2;
    this.m_arch2 = p;
    p.clear();
    this.m_archivePipe.setOutputPipe(p);

    super.beforeIteration(index);
  }

  /**
   * Obtain the inner pipeline which leads to the archive.
   * 
   * @return the inner pipeline which leads to the archive
   */
  protected Pipeline<G, PP> getArchivePipeline() {
    return this.m_archivePipe;
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
  @Override
  protected void outputResults(final IPipeIn<G, PP> dest) {
    synchronized (this.m_arch1) {
      this.m_arch1.writeToPipe(dest);
    }

    synchronized (this.m_arch2) {
      this.m_arch2.writeToPipe(dest);
    }

    super.outputResults(dest);
  }

  /**
   * Normally, an optimizer can be used only once. After it has been run
   * and finished its work, it cannot be run again as defined by the
   * <code>EActivityState</code> semantics. This method allows you to
   * reset the activity state to <code>INITIALIZED</code>. Therefore,
   * you can use the optimizer again. This method also calls
   * <code>reset</code> in order to make the optimizer virgin again.
   * 
   * @throws IllegalStateException
   *           if the algorithm is currently running or being initialized.
   * @see EActivityState
   * @see #reset()
   */
  @Override
  public void reuse() {
    super.reuse();
    synchronized (this.m_arch1) {
      this.m_arch1.clear();
    }

    synchronized (this.m_arch2) {
      this.m_arch2.clear();
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
  @Override
  public synchronized void addNonPrevailedPipe(final IPipe<G, PP> output) {
    if (output == null)
      throw new NullPointerException();
    this.m_archivePipe.add(output);
  }
}
