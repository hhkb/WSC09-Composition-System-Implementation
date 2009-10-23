/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-27
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.go.objectives.IObjectiveValueComputer.java
 * Last modification: 2006-11-27
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

package org.sigoa.spec.go.objectives;

import java.io.Serializable;

/**
 * An optional interface which can be used by
 * <code>IObjectiveFunction</code>s. Each objective function has a
 * method <code>computeObjectiveValue</code> used to determine the final
 * objective value of an individual evaluation from n single tests of an
 * individual. This method is inherited from this interface. One may use
 * this interface in order to seperate the computation of such values from
 * the objective function itself. The objective function would then use
 * another instance of this interface in order to perform the calculation.
 *
 * @author Thomas Weise
 */
public interface IObjectiveValueComputer extends Serializable {
  /**
   * This method is called by the objective function in order to determine
   * the final objective value. The evaluator may perform multiple
   * simulations, where the objective function stores an objective value
   * into its state record for each single one. These values are now put
   * into an array of double, so the objective function may decide on a
   * final value based on these results of single simulations. The
   * <code>results</code>-array is sorted in ascending order.
   *
   * @param results
   *          The results of the single simulations. (Sorted in ascending
   *          order)
   * @return A final objective value based on the results passed in.
   */
  public abstract double computeObjectiveValue(final double[] results);
}
