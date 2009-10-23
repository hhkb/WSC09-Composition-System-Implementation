/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-22 10:19:28
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.comparators.TieredParetoComparator.java
 * Version          : 1.0.0
 * Last modification: 2007-05-08
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

import org.sigoa.spec.go.IComparator;
import org.sigoa.spec.go.IIndividual;

/**
 * This comparator compares two individuals according to
 * pareto-optimization in a tiered approach. In the constructor you can
 * define the end indices of the objective functions belonging to the
 * tiers. When comparing, the first tier is compared first. If one of the
 * individuals dominates the other in this tier, this is returned.
 * Otherwise, the next tier is compared and so on.
 *
 * @author Thomas Weise
 */
public class TieredParetoComparator extends ComparatorBase implements
    IComparator {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the different tiers of the comparator
   */
  private final int[] m_tiers;

  /**
   * Create a new tiered pareto comparator.
   *
   * @param tiers
   *          the indices of the ends of the tiers.
   * @throws NullPointerException
   *           if <code>tiers==null</code>
   */
  public TieredParetoComparator(final int[] tiers) {
    super();
    this.m_tiers = tiers.clone();
  }

  /**
   * Compare two individuals with each other. The comparison is performed
   * only on the base of <code>count</code> objective values starting
   * with the <code>from</code><sup>th</sup> one.
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
  @Override
  protected int compare(final IIndividual<?, ?> o1,
      final IIndividual<?, ?> o2, final int from, final int to) {
    int tierEnd, i, s, j, z;
    final int[] tiers;

    tiers = this.m_tiers;
    i = from;
    s = 0;
    for (z = 0; z < tiers.length; z++) {
      tierEnd = (tiers[z] + from);
      for (;;) {
        j = Double.compare(o1.getObjectiveValue(i), o2
            .getObjectiveValue(i));
        if (j < 0) {
          if (s == 0)
            s = -1;
          else if (s > 0)
            return 0;
        } else if (j > 0) {
          if (s == 0)
            s = 1;
          else if (s < 0)
            return 0;
        }
        i++;
        if (i >= to)
          return (s * (tiers.length - z + 1));
        if (i >= tierEnd)
          break;
      }
      if (s != 0)
        return (s * (tiers.length - z + 1));
    }

    // s = 0;
    for (; i < to; i++) {
      j = Double.compare(o1.getObjectiveValue(i), o2.getObjectiveValue(i));
      if (j < 0) {
        if (s == 0)
          s = -1;
        else if (s > 0)
          return 0;
      } else if (j > 0) {
        if (s == 0)
          s = 1;
        else if (s < 0)
          return 0;
      }
    }

    return s;
  }

  /**
   * Check whether this comparator is equal to another object.
   *
   * @param o
   *          The object to compare with.
   * @return <code>true</code> if and only if<code>o</code> represents
   *         the same comparator as this object, <code>false</code>
   *         otherwise.
   */
  @Override
  public boolean equals(final Object o) {
    int[] t1, t2;
    int i;

    if (super.equals(o)) {
      t2 = ((TieredParetoComparator) o).m_tiers;
      t1 = this.m_tiers;

      i = t1.length;
      if (i != t2.length)
        return false;

      for (--i; i >= 0; i--) {
        if (t1[i] != t2[i])
          return false;
      }

      return true;
    }

    return false;
  }
}
