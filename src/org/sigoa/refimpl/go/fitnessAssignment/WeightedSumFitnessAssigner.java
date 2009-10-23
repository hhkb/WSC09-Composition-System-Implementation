/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-29
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.fitnessAssignment.WeightedSumFitnessAssigner.java
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
import org.sigoa.spec.go.OptimizationUtils;
import org.sigoa.spec.go.fitnessAssignment.IFitnessAssigner;

/**
 * The weighted sum fitness assignment process. Individuals get a fitness
 * which is equal to the weighted sum of their objectives.
 *
 * @param <G>
 *          The genotype.
 * @param <PP>
 *          The phenotype.
 * @author Thomas Weise
 */
public class WeightedSumFitnessAssigner<G extends Serializable, PP extends Serializable>
    extends Pipe<G, PP> implements IFitnessAssigner<G, PP> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The weights.
   */
  private final double[] m_weights;

  /**
   * Create a new weighted sum fitness assigner.
   *
   * @param weights
   *          the weights of the single objective values.
   * @throws NullPointerException
   *           if <code>weights==nul</code>
   * @throws IllegalArgumentException
   *           if <code>weights.length&lt;=0</code>
   */
  public WeightedSumFitnessAssigner(final double[] weights) {
    super();
    if (weights.length <= 0)
      throw new IllegalArgumentException();
    this.m_weights = weights.clone();
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
    double s;
    double[] w;
    int i;

    if (individual == null)
      throw new NullPointerException();

    w = this.m_weights;
    s = 0.0d;
    for (i = (individual.getObjectiveValueCount() - 1); i >= 0; i--) {
      s += (w[i] * individual.getObjectiveValue(i));
    }

    if (Double.isNaN(s))
      individual.setFitness(OptimizationUtils.WORST_NUMERIC);
    else if (s == OptimizationUtils.BEST)
      individual.setFitness(OptimizationUtils.BEST_NUMERIC);
    else if (s == OptimizationUtils.WORST)
      individual.setFitness(OptimizationUtils.WORST_NUMERIC);
    else
      individual.setFitness(s);

    this.output(individual);
  }
}
