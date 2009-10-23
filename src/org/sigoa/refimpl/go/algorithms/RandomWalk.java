/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-28
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.algorithms.RandomWalk.java
 * Last modification: 2007-09-28
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

package org.sigoa.refimpl.go.algorithms;

import java.io.Serializable;

import org.sigoa.refimpl.go.algorithms.ea.ElitistEA;
import org.sigoa.refimpl.go.fitnessAssignment.FixedFitnessAssigner;
import org.sigoa.refimpl.go.selection.RandomSelectionR;
import org.sigoa.refimpl.pipe.NoEofPipe;
import org.sigoa.spec.go.fitnessAssignment.IFitnessAssigner;
import org.sigoa.spec.go.selection.ISelectionAlgorithm;
import org.sigoa.spec.pipe.IPipeIn;

/**
 * This class represents a random walk algorithm. The random walk works on
 * basis of an elitist evolutionary algorithm that keeps an archive of the
 * best individuals encountered but performs random selection and a dummy
 * fitness assignment process.
 * 
 * @param <G>
 *          the genotype
 * @param <PP>
 *          the phenotype
 * @author Thomas Weise
 */
public class RandomWalk<G extends Serializable, PP extends Serializable>
    extends ElitistEA<G, PP> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new elitist evolutionary algorithm.
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
  public RandomWalk(final int initialPopulationSize,
      final double mutationRate, final double crossoverRate,
      final int maxArchiveSize) {
    super(initialPopulationSize, mutationRate, crossoverRate, false,
        false, maxArchiveSize);
  }

  /**
   * Create a new elitist evolutionary algorithm.
   */
  public RandomWalk() {
    this(1024, 0.2, 0.8, 128);
  }

  /**
   * Create the selection algorithm to be used by this ea. This method is
   * invoked by <code>createPipeline</code>.
   * 
   * @return the selection algorithm to be used by this ea
   */
  @Override
  protected ISelectionAlgorithm<G, PP> createSelectionAlgorithm() {
    return new RandomSelectionR<G, PP>(this.getNextPopulationSize());
  }

  /**
   * Create the fitness assigner to be used by this ea. This method is
   * invoked by <code>createPipeline</code>.
   * 
   * @return the fitness assigner pipe to be used by this ea
   */
  @Override
  @SuppressWarnings("unchecked")
  protected IFitnessAssigner<G, PP> createFitnessAssigner() {
    return (IFitnessAssigner) (FixedFitnessAssigner.FIXED_FITNESS_ASSIGNER_1);
  }

  /**
   * Create the output pipeline for the archive.
   * 
   * @return the output pipeline for the archive
   */
  @Override
  protected IPipeIn<G, PP> createArchiveOutput() {
    NoEofPipe<G, PP> x;

    x = new NoEofPipe<G, PP>();

    x.setOutputPipe(this.createEvaluatorPipe());

    return x;
  }

}
