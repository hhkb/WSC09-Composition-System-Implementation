/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-01-07
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.math.IRealFunction2d.java
 * Last modification: 2007-01-07
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

package org.sfc.math;

/**
 * This interface allows us to define two-dimensional real functions
 * 
 * @author Thomas Weise
 */
public interface IRealFunction2d {
  /**
   * Compute the function
   * 
   * @param x
   *          the x value
   * @param y
   *          the y value
   * @return the function value
   */
  public abstract double f(final double x, final double y);

}
