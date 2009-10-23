/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-22 10:19:28
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.go.IComparator.java
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

package org.sigoa.spec.go;

import java.io.Serializable;

/**
 * This interface allows the optimization algorithms to compare
 * individuals.
 *
 * @author Thomas Weise
 * @version 1.0.0
 */
public interface IComparator extends
    java.util.Comparator<IIndividual<?, ?>>, Serializable {
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
   * @throws IllegalArgumentException
   *           if the individuals have different counts of objective
   *           values.
   * @throws NullPointerException
   *           if one the individuals is <code>null</code>.
   * @see #preciseCompare(IIndividual, IIndividual)
   */
  public abstract int compare(final IIndividual<?, ?> o1,
      final IIndividual<?, ?> o2);

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
  public abstract double preciseCompare(final IIndividual<?, ?> o1,
      final IIndividual<?, ?> o2);

}
