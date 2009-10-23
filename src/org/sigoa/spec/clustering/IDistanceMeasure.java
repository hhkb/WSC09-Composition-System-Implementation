/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-11
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.clustering.IDistanceMeasure.java
 * Last modification: 2006-12-11
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

package org.sigoa.spec.clustering;

import java.io.Serializable;

/**
 * A distance measure is able to compute a scalar distance between two
 * objects.
 *
 * @param <T>
 *          the object type
 * @author Thomas Weise
 */
public interface IDistanceMeasure<T> extends Serializable {

  /**
   * Compute the distance between two objects.
   *
   * @param o1
   *          the first object
   * @param o2
   *          the second object
   * @return the distance between <code>o1</code> and <code>o2</code>
   *
   * @throws NullPointerException if <code>o1 == null || o2 == null</code>
   */
  public abstract double distance(final T o1, final T o2);

}
