/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-13
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.vm.VMUtils.java
 * Last modification: 2007-11-13
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

package org.dgpf.vm.base;

import org.sfc.utils.ErrorUtils;

/**
 * Some utilities for virtual machines
 * 
 * @author Thomas Weise
 */
public final class VMUtils {

  /**
   * the hidden, forbidden constructor
   */
  private VMUtils() {
    ErrorUtils.doNotCall();
  }

  /**
   * Perform a save integer division.
   * 
   * @param arg1
   *          the dividend
   * @param arg2
   *          the divisor
   * @return the result
   */
  public static final int saveDivision(final int arg1, final int arg2) {
    if (arg2 != 0)
      return (arg1 / arg2);
    if (arg1 > 0)
      return Integer.MAX_VALUE;
    if (arg1 < 0)
      return Integer.MIN_VALUE;
    return 0;
  }

  /**
   * Perform a save integer modulo division.
   * 
   * @param arg1
   *          the modulo dividend
   * @param arg2
   *          the divisor
   * @return the result
   */
  public static final int saveModulo(final int arg1, final int arg2) {
    if (arg2 != 0)
      return (arg1 % arg2);
    return (arg1 - (saveDivision(arg1, arg2) * arg2));
  }
}
