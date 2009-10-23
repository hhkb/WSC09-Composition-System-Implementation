/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-22 10:19:28
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.comparators.FitnessComparator.java
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

import java.io.Serializable;

import org.sigoa.refimpl.go.ImplementationBase;
import org.sigoa.spec.go.IComparator;
import org.sigoa.spec.go.IIndividual;

/**
 * This special comparator compares two individuals according to their
 * fitness values. Therefore, this comparator must only be applied after
 * the fitness assignment process. Applying it before the fitness
 * assignment process took place will result in unpredictable behavior.
 *
 * @author Thomas Weise
 */
public class FitnessComparator extends
    ImplementationBase<Serializable, Serializable> implements IComparator {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The default, shared instance.
   */
  public static final IComparator FITNESS_COMPARATOR = new FitnessComparator();

  /**
   * Compare two individuals with each other. The comparison is performed
   * on the base of the fitness values.
   *
   * @param o1
   *          The first individual to be compared.
   * @param o2
   *          The second individual to be compared.
   * @return a negative value if o1 prevails o2, a positive value if o2
   *         prevails o1 and 0 if non of them prevails the other one.
   */
  public int compare(final IIndividual<?, ?> o1, final IIndividual<?, ?> o2) {
    if (o1 == o2)
      return 0;
    return ComparatorUtils.preciseToNormal(this.preciseCompare(o1, o2));
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
    double d1, d2, d3;
    if (o1 == o2)
      return 0.0d;
    d1 = o1.getFitness();
    d2 = o2.getFitness();
    d3 = (d1 - d2);

    if (Double.isNaN(d3))
      return ComparatorUtils.preciseCompareFallback(o1, o2, 0, o1
          .getObjectiveValueCount());

    return d3;
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
    return ((this.getClass() == FitnessComparator.class) && (o.getClass() == FitnessComparator.class));
  }

  /**
   * Resolve this comparator at deserialization.
   *
   * @return The right comparator instance.
   */
  private final Object readResolve() {
    if (this.getClass() == FitnessComparator.class)
      return FitnessComparator.FITNESS_COMPARATOR;
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
