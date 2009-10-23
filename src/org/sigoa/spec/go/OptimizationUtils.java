/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-27
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.go.OptimizationUtils.java
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

package org.sigoa.spec.go;

/**
 * Some utilities that may be used by objective functions.
 *
 * @author Thomas Weise
 */
public final class OptimizationUtils {
  /**
   * The worst possible objective value.
   */
  public static final double WORST = Double.POSITIVE_INFINITY;

  /**
   * The best possible objective value.
   */
  public static final double BEST = -WORST;// Double.NEGATIVE_INFINITY;

  /**
   * The worst possible numeric objective value.
   */
  public static final double WORST_NUMERIC = Double.MAX_VALUE;

  /**
   * The best possible numeric objective value.
   */
  public static final double BEST_NUMERIC = -WORST_NUMERIC;

  /**
   * This method ensures that an objective value is never <code>NaN</code>.
   *
   * @param value
   *          The value to be formatted.
   * @return The value, or <code>WORST</code> if
   *         <code>Double.isNaN(value)</code>.
   */
  public static final double formatObjectiveValue(final double value) {
    if (Double.isNaN(value))
      return WORST;
    return value;
  }

  /**
   * This internal constructor cannot be invoked.
   */
  private OptimizationUtils() {
    throw new RuntimeException();
  }
}
