/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-29
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.fitnessAssignment.FixedFitnessAssigner.java
 * Last modification: 2006-11-29
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

package org.sigoa.refimpl.go.fitnessAssignment;

import java.io.Serializable;

import org.sigoa.refimpl.pipe.Pipe;
import org.sigoa.spec.go.IIndividual;
import org.sigoa.spec.go.fitnessAssignment.IFitnessAssigner;

/**
 * This fitness assignment process assigns the exact same fitness to all
 * individuals.
 * 
 * @param <G>
 *          The genotype.
 * @param <PP>
 *          The phenotype.
 * @author Thomas Weise
 */
public class FixedFitnessAssigner<G extends Serializable, PP extends Serializable>
    extends Pipe<G, PP> implements IFitnessAssigner<G, PP> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The globally shared instance of the fixed fitness assigner for fitness
   * 1.
   */
  public static final IFitnessAssigner<?, ?> FIXED_FITNESS_ASSIGNER_1 = new FixedFitnessAssigner<Serializable, Serializable>(
      1d);

  /**
   * the fixed fitness to be assigned
   */
  private final double m_fitness;

  /**
   * Create a new fixed fitness assigner.
   * 
   * @param fitness
   *          the fitness value to be assigned
   */
  public FixedFitnessAssigner(final double fitness) {
    super();
    this.m_fitness = fitness;
  }

  /**
   * Assign a fitness value to an individual by computing the sum of its
   * objective values.
   * 
   * @param individual
   *          The new individual to be written.
   * @throws NullPointerException
   *           if <code>individual</code> is <code>null</code>.
   */
  @Override
  public void write(final IIndividual<G, PP> individual) {

    if (individual == null)
      throw new NullPointerException();

    individual.setFitness(this.m_fitness);

    this.output(individual);
  }
}
