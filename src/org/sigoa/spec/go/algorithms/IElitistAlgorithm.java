/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-14
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.go.algorithms.IElitistAlgorithm.java
 * Last modification: 2006-12-14
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

package org.sigoa.spec.go.algorithms;

import java.io.Serializable;

/**
 * This interface is common to elitist optimization algorithms that
 * preserve an archive of optimal elements.
 *
 * @author Thomas Weise
 */
public interface IElitistAlgorithm extends Serializable {

  /**
   * Obtain the maximal archive size.
   *
   * @return the maximal archive size
   */
  public abstract int getMaxArchiveSize();

  /**
   * Set the maximal archive size.
   *
   * @param maxSize
   *          the new maximal archive size
   * @throws IllegalArgumentException
   *           if <code>maxSize&lt;=0</code>
   */
  public abstract void setMaxArchiveSize(final int maxSize);

}
