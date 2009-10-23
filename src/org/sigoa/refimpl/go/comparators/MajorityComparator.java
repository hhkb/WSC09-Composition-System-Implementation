/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-22 10:19:28
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.comparators.MajorityComparator.java
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
 * This comparator compares two individuals checking which one has better
 * values in most of the fitness functions.
 * 
 * @author Thomas Weise
 */
public class MajorityComparator extends ComparatorBase implements
    IComparator {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The default, shared instance.
   */
  public static final IComparator MAJORITY_COMPARATOR = new MajorityComparator();

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
    int s, i, j;

    s = 0;
    for (i = from; i < to; i++) {
      j = Double.compare(o1.getObjectiveValue(i), o2.getObjectiveValue(i));
      if (j < 0)
        s--;
      else if (j > 0)
        s++;
    }

    return s;
  }

  /**
   * Resolve this comparator at deserialization.
   * 
   * @return The right comparator instance.
   */
  private final Object readResolve() {
    if (this.getClass() == MajorityComparator.class)
      return MajorityComparator.MAJORITY_COMPARATOR;
    return this;
  }

  /**
   * The write replace method.
   * 
   * @return the comparator instance to be written
   */
  private final Object writeReplace() {
    return this.readResolve();
  }
}
