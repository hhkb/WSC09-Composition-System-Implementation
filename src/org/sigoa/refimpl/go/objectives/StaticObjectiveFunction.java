/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-01-03
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.objectives.StaticObjectiveFunction.java
 * Last modification: 2007-01-03
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

package org.sigoa.refimpl.go.objectives;

import java.io.Serializable;

import org.sigoa.spec.go.OptimizationUtils;
import org.sigoa.spec.go.objectives.IObjectiveState;
import org.sigoa.spec.go.objectives.IObjectiveValueComputer;
import org.sigoa.spec.simulation.ISimulation;

/**
 * This objective function computes its value once for each individual and
 * stores the result, regardless how often an individual is checked.
 *
 * @param <PP>
 *          The phenotype of the individuals to evaluate.
 * @param <ST>
 *          The type of state object used by this objective function.
 * @param <SI>
 *          The simulator type involved in the individual evaluation.
 * @param <SS>
 *          the type of static state used, if any needed
 * @author Thomas Weise
 */
public class StaticObjectiveFunction<PP extends Serializable, ST extends IObjectiveState, SS extends StaticObjectiveState, SI extends ISimulation<PP>>
    extends ObjectiveFunction<PP, ST, SS, SI> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new static objective function.
   */
  protected StaticObjectiveFunction() {
    this(null);
  }

  /**
   * Create a new static objective function.
   *
   * @param ovc
   *          The objective value computer to be used. If this is
   *          <code>null</code>, the
   *          <code>ObjectiveUtils.WORST_OVC</code> is used as default.
   */
  protected StaticObjectiveFunction(final IObjectiveValueComputer ovc) {
    super(ovc);
  }

  /**
   * After all objective functions have decided that the individual is
   * sane, this function is called for of them.
   *
   * @param individual
   *          The individual to be evaluated.
   * @param staticState
   *          the static state record
   * @throws NullPointerException
   *           if <code>individual==null</code>
   */
  @Override
  public void beginIndividual(final PP individual, final SS staticState) {
    staticState.m_value = this.computeValue(individual, staticState);
  }

  /**
   * This method is called only once per individual and is used to compute
   * its objective value.
   *
   * @param individual
   *          The individual to be checked.
   * @param staticState
   *          the static state record
   * @return the objective value
   */
  protected double computeValue(final PP individual,
      final SS staticState) {
    return OptimizationUtils.WORST;
  }

  /**
   * Create a new static state record.
   *
   * @return the new static state record
   */
  @Override
  @SuppressWarnings("unchecked")
  public SS createStaticState() {
    return ((SS) (new StaticObjectiveState()));
  }

  /**
   * This method is called by the evaluator in order to determine the final
   * objective value for this objective. The evaluator may perform multiple
   * simulations, where the objective function stores an objective value
   * into its state record for each single one. These values are now put
   * into an array of double, so the objective function may decide on a
   * final value based on these results of single simulations. The
   * <code>results</code>-array is sorted in ascending order.
   *
   * @param results
   *          The results of the single simulations. (Sorted in ascending
   *          order)
   * @param staticState
   *          the static state record
   * @return A final objective value based on the results passed in.
   */
  @Override
  public double computeObjectiveValue(final double[] results,
      final StaticObjectiveState staticState) {
    return OptimizationUtils.formatObjectiveValue(staticState.m_value);
  }
}
