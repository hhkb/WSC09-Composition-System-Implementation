/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-10
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.doubleString.DoubleStringObjectiveFunction.java
 * Last modification: 2006-12-10
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

package org.sigoa.refimpl.genomes.doubleString;

import org.sigoa.refimpl.go.objectives.ObjectiveState;
import org.sigoa.refimpl.go.objectives.StaticObjectiveFunction;
import org.sigoa.refimpl.go.objectives.StaticObjectiveState;
import org.sigoa.spec.simulation.ISimulation;

/**
 * A very simple fitness function that just evaluates a vector of doubles.
 * Using this fitness function makes only sense if there are also other
 * aspects to be evaluated different from mathematical functions.
 * Otherwise, using the {@link DoubleStringFunctionEvaluator} would be more
 * appropriate.
 *
 * @see DoubleStringFunctionEvaluator
 * @see IDoubleStringFunction
 * @author Thomas Weise
 */
public class DoubleStringObjectiveFunction
    extends
    StaticObjectiveFunction<double[], ObjectiveState, StaticObjectiveState, ISimulation<double[]>> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the double vector function which should be optimized.
   */
  private final IDoubleStringFunction m_f;

  /**
   * Create a new <code>DoubleStringObjectiveFunction</code>.
   *
   * @param f
   *          the function which should be optimized
   * @throws NullPointerException
   *           if <code>f==null</code>
   */
  public DoubleStringObjectiveFunction(final IDoubleStringFunction f) {
    super();
    if (f == null)
      throw new NullPointerException();
    this.m_f = f;
  }

  /**
   * This method is called only once per individual and is used to compute
   * its objective value.
   *
   * @param individual
   *          The individual to be checked.
   * @param staticData
   *          the static data record
   * @return the objective value
   */
  @Override
  protected double computeValue(final double[] individual,
      final StaticObjectiveState staticData) {
    return this.m_f.compute(individual);
  }
}
