/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-26
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.objectives.ovcs.TruncatingAverageOVC.java
 * Last modification: 2007-03-26
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

import org.sigoa.refimpl.go.objectives.ObjectiveUtils;
import org.sigoa.spec.go.OptimizationUtils;

/**
 * This objective value computer truncates its results.
 *
 * @author Thomas Weise
 */
public class TruncatingAverageOVC extends PrecisionOVC {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new truncating average objective value computer.
   *
   * @param precision
   *          the precision value.
   */
  public TruncatingAverageOVC(final double precision) {
    super(precision);
  }

  /**
   * Check whether this ovc equals another object
   *
   * @param o
   *          the object to compare with
   * @return <code>true</code> if and only if this ovc equals to the
   *         object <code>o</code>, <code>false</code> other wise
   */
  @Override
  public boolean equals(final Object o) {
    return ((o == this) || ((o != null) && (o.getClass() == this
        .getClass())));
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
    int i, j;
    double d;

    j = results.length;
    if (j <= 0)
      return OptimizationUtils.WORST;

    d = 0.0d;
    for (i = (j - 1); i >= 0; i--) {
      d += results[i];
    }

    return ((Double.isNaN(d)) ? OptimizationUtils.WORST : (Double
        .isInfinite(d) ? d : ObjectiveUtils.truncate((d / j),
        this.m_precision)));
  }
}
