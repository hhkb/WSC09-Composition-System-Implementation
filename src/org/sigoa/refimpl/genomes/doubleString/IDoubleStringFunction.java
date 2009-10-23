/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-10
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.doubleString.IDoubleStringFunction.java
 * Last modification: 2006-12-10
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

package org.sigoa.refimpl.genomes.doubleString;

/**
 * A mathematical function that evaluates a vector of <code>double</code>
 * and returns a scalar (<code>double</code>) result.
 *
 * @author Thomas Weise
 */
public interface IDoubleStringFunction {

  /**
   * Compute the result scalar value from a vector of <code>double</code>.
   *
   * @param x
   *          the vector
   * @return the scalar result value
   */
  public abstract double compute(final double[] x);

}
