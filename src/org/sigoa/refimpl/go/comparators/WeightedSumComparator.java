/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-22 10:19:28
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.comparators.WeightedSumComparator.java
 * Version          : 1.0.0
 * Last modification: 2006-11-22
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
 * This comparator compares two individuals according to the weighted sum
 * of their objective values.
 *
 * @author Thomas Weise
 */
public class WeightedSumComparator extends ComparatorBase implements
    IComparator {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The weights for the single objective values.
   */
  private final double[] m_weights;

  /**
   * Create a new weighted sum comparator.
   *
   * @param weights
   *          the weights of the single objective values.
   * @throws NullPointerException
   *           if <code>weights==nul</code>
   * @throws IllegalArgumentException
   *           if <code>weights.length&lt;=0</code>
   */
  public WeightedSumComparator(final double[] weights) {
    super();
    if (weights.length <= 0)
      throw new IllegalArgumentException();
    this.m_weights = weights.clone();
  }

  /**
   * Precisely compare two individuals with each other. The comparison is
   * performed only on the base of <code>count</code> objective values
   * starting with the <code>from</code><sup>th</sup> one.
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
  protected double preciseCompare(final IIndividual<?, ?> o1,
      final IIndividual<?, ?> o2, final int from, final int to) {
    int i;
    double s, d1;
    double[] w;

    w = this.m_weights;
    if ((to - from) != w.length)
      throw new IllegalArgumentException();

    s = 0;
    for (i = from; i < to; i++) {
      d1 = (w[i - from] * (o1.getObjectiveValue(i) - o2
          .getObjectiveValue(i)));
      if (Double.isNaN(d1)
          || (Double.isInfinite(d1) && (((d1 < 0) && (s > 0)) || ((d1 > 0) && (s < 0))))) {
        return ComparatorUtils.preciseCompareFallback(o1, o2, from, to);
      }
      s += d1;

    }

    if (Double.isNaN(s)) {
      return ComparatorUtils.preciseCompareFallback(o1, o2, from, to);
    }
    return s;
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
    return ComparatorUtils.preciseToNormal(this.preciseCompare(o1, o2,
        from, to));
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
    double[] w1, w2;
    int i;

    if (super.equals(o)) {
      w1 = ((WeightedSumComparator) o).m_weights;
      w2 = this.m_weights;
      i = w2.length;
      if (i != w1.length)
        return false;

      for (--i; i >= 0; i--) {
        if (w1[i] != w2[i])
          return false;
      }
      return true;
    }
    return false;
  }
}
