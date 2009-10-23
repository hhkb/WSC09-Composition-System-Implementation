/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-05-08
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.comparators.ComparatorBase.java
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

import java.io.Serializable;

import org.sigoa.refimpl.go.ImplementationBase;
import org.sigoa.spec.go.IComparator;
import org.sigoa.spec.go.IIndividual;
import org.sigoa.spec.go.OptimizationUtils;

/**
 * The comparator base
 * 
 * @author Thomas Weise
 */
public abstract class ComparatorBase extends
    ImplementationBase<Serializable, Serializable> implements IComparator {

  /**
   * The protected constructor
   */
  protected ComparatorBase() {
    super();
  }

  /**
   * Compare two individuals with each other. The comparison is performed
   * on the base of the objective values.
   * 
   * @param o1
   *          The first individual to be compared.
   * @param o2
   *          The second individual to be compared.
   * @return a negative value if o1 prevails o2, a positive value if o2
   *         prevails o1 and 0 if non of them prevails the other one.
   */
  public int compare(final IIndividual<?, ?> o1, final IIndividual<?, ?> o2) {
    int i, c;

    // i = o1.getObjectiveValueCount();
    // if (i != o2.getObjectiveValueCount())
    // throw new IllegalArgumentException();

    c = o1.getObjectiveValueCount();
    i = preprocess(o1, o2, c);
    if (i > 0)
      return (i - 2);

    return this.compare(o1, o2, 0, c);
  }

  /**
   * Preprocess two individuals, check for NaNs and infinities.
   * 
   * @param o1
   *          The first individual to be compared.
   * @param o2
   *          The second individual to be compared.
   * @param c
   *          the objective value count
   * @return 0: all ok, 1: ind 1 is better, 2: no decision, 3: ind 2 is
   *         better
   */
  static final int preprocess(final IIndividual<?, ?> o1,
      final IIndividual<?, ?> o2, final int c) {
    int i, nanC1, worstC1, nanC2, worstC2;
    double v;

    nanC1 = 0;
    worstC1 = 0;
    nanC2 = 0;
    worstC2 = 0;

    for (i = (o1.getObjectiveValueCount() - 1); i >= 0; i--) {

      v = o1.getObjectiveValue(i);
      if (Double.isNaN(v))
        nanC1++;
      else if (v >= OptimizationUtils.WORST)
        worstC1++;

      v = o2.getObjectiveValue(i);
      if (Double.isNaN(v))
        nanC2++;
      else if (v >= OptimizationUtils.WORST)
        worstC2++;
    }

    if ((nanC1 > 0) || (nanC2 > 0) || (worstC1 > 0) || (worstC2 > 0)) {
      if (nanC1 > nanC2)
        return 3;
      if (nanC2 > nanC1)
        return 1;
      if (worstC1 > worstC2)
        return 3;
      if (worstC2 > worstC1)
        return 1;
      return 2;
    }
    return 0;
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
  protected abstract int compare(final IIndividual<?, ?> o1,
      final IIndividual<?, ?> o2, final int from, final int to);

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
  protected double preciseCompare(final IIndividual<?, ?> o1,
      final IIndividual<?, ?> o2, final int from, final int to) {
    return this.compare(o1, o2, from, to);
  }

  /**
   * Compute a scalar difference between two individuals. This method must
   * always be compatible to <code>compare</code>, meaning that if
   * <code>compare</code> returns a valu <code>&lt;0</code>,
   * <code>preciseCompare</code> must also be <code>&lt;0</code>, if
   * <code>compare</code> returns a valu <code>&gt;0</code>,
   * <code>preciseCompare</code> must also be <code>&gt;0</code> and if
   * if <code>compare==0</code>, <code>preciseCompare</code> must also
   * be <code>0</code>. If <code>compare</code> throws and exception,
   * <code>preciseCompare</code> must also throw such an exception.
   * 
   * @param o1
   *          The first individual to be compared.
   * @param o2
   *          The second individual to be compared.
   * @return a negative value if o1 prevails o2, a positive value if o2
   *         prevails o1 and 0 if non of them prevails the other one.
   * @throws IllegalArgumentException
   *           if the individuals have different counts of objective
   *           values.
   * @throws NullPointerException
   *           if one the individuals is <code>null</code>.
   * @see #compare(IIndividual, IIndividual)
   */
  public double preciseCompare(final IIndividual<?, ?> o1,
      final IIndividual<?, ?> o2) {
    int i;

    i = o1.getObjectiveValueCount();
    if (i != o2.getObjectiveValueCount())
      throw new IllegalArgumentException();

    return this.preciseCompare(o1, o2, 0, i);
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
    if (this == o)
      return true;
    if (o == null)
      return false;
    return (this.getClass() == o.getClass());
  }
}
