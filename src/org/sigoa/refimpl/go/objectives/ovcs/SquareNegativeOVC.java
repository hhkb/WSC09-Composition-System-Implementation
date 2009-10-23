/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-29
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.objectives.ovcs.SquareNegativeOVC.java
 * Last modification: 2007-03-29
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

package org.sigoa.refimpl.go.objectives.ovcs;

import java.io.Serializable;

import org.sigoa.refimpl.go.ImplementationBase;
import org.sigoa.spec.go.OptimizationUtils;
import org.sigoa.spec.go.objectives.IObjectiveValueComputer;

/**
 * This ovc values bad results higher than good ones and computes an
 * average shifted in the worst direction.
 *
 * @author Thomas Weise
 */
public class SquareNegativeOVC extends
    ImplementationBase<Serializable, Serializable> implements
    IObjectiveValueComputer {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the globally shared instance of the nevative ovc
   */
  public static final IObjectiveValueComputer SQUARE_NEGATIVE_OVC = new SquareNegativeOVC();

  /**
   * the hidden constructor
   */
  protected SquareNegativeOVC() {
    super();
  }

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
  public double computeObjectiveValue(final double[] results) {
    int l, d, ss, v;
    double s;

    d = results.length;
    ss = 0;
    s = 0.0d;
    for (l = (d - 1); l >= 0; l--) {
      s += (results[l] * (v = (d * d)));
      ss += v;
      d = l;
    }

    if (Double.isNaN(s))
      return OptimizationUtils.WORST;
    return s / ss;
  }

  /**
   * read resolve
   *
   * @return the resolved result
   */
  private final Object readResolve() {
    return SQUARE_NEGATIVE_OVC;
  }

  /**
   * write replace
   *
   * @return the resolved result
   */
  private final Object writeReplace() {
    return SQUARE_NEGATIVE_OVC;
  }
}
