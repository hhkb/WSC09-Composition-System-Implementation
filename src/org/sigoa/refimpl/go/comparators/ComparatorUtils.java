/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-02-02
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.comparators.ComparatorUtils.java
 * Last modification: 22007-05-08
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

package org.sigoa.refimpl.go.comparators;

import org.sfc.utils.ErrorUtils;
import org.sigoa.spec.go.IIndividual;

/**
 * Some utility classes for comparison.
 *
 * @author Thomas Weise
 */
public final class ComparatorUtils {

  /**
   * the internal fallback comparator
   */
  private static final ComparatorBase FALLBACK = ((ComparatorBase) (MajorityComparator.MAJORITY_COMPARATOR));

  /**
   * The forbidden constructor.
   */
  private ComparatorUtils() {
    ErrorUtils.doNotCall();
  }

  /**
   * Translate a double value to an integer, as needed if values returned
   * by {@link org.sigoa.spec.go.IComparator#preciseCompare} need to be
   * used in
   * {@link org.sigoa.spec.go.IComparator#compare(IIndividual, IIndividual)}.
   *
   * @param d1
   *          the double value
   * @return an int value representing the double <code>d1</code>
   */
  public static final int preciseToNormal(final double d1) {

    if (Double.isNaN(d1))
      return 0;
    if (d1 == 0.0d)
      return 0;

    if (d1 < 0.0d) {
      if ((Double.isInfinite(d1)) || (d1 > -0.5))
        return -1;
      if (d1 < Integer.MIN_VALUE)
        return Integer.MIN_VALUE;
      return Math.min(-1, (int) (Math.round(d1)));
    }
    if ((Double.isInfinite(d1)) || (d1 < 0.5))
      return 1;
    if (d1 > Integer.MAX_VALUE)
      return Integer.MAX_VALUE;
    return Math.max(1, (int) (Math.round(d1)));
  }

  /**
   * The fallback if comparison fails.
   *
   * @param o1
   *          The first individual to be compared.
   * @param o2
   *          The second individual to be compared.
   * @param from
   *          the index of the first objective value to be taken into
   *          consideration
   * @param to
   *          the exclusive last index of the comparison
   * @return a negative value if o1 prevails o2, a positive value if o2
   *         prevails o1 and 0 if non of them prevails the other one.
   */
  public static final int compareFallback(final IIndividual<?, ?> o1,
      final IIndividual<?, ?> o2, final int from, final int to) {
    return FALLBACK.compare(o1, o2, from, to);
  }

  /**
   * The fall back routine if precise comparison fails.
   *
   * @param o1
   *          The first individual to be compared.
   * @param o2
   *          The second individual to be compared.
   * @param from
   *          the index of the first objective value to be taken into
   *          consideration
   * @param to
   *          the exclusive last index of the comparison
   * @return a negative value if o1 prevails o2, a positive value if o2
   *         prevails o1 and 0 if non of them prevails the other one.
   * @throws IllegalArgumentException
   *           if the individuals have different counts of objective
   *           values.
   * @throws NullPointerException
   *           if one the individuals is <code>null</code>.
   */
  public static final double preciseCompareFallback(
      final IIndividual<?, ?> o1, final IIndividual<?, ?> o2,
      final int from, final int to) {
    return FALLBACK.preciseCompare(o1, o2, from, to);
  }
}
