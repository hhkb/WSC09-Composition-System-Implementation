/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-10-09
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.vm.objectives.VMObjectiveUtils.java
 * Last modification: 2007-10-09
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

package org.dgpf.vm.objectives;

import org.sfc.utils.ErrorUtils;

/**
 * The virtual machine objective utils
 * 
 * @author Thomas Weise
 */
public final class VMObjectiveUtils {

  /**
   * This method regularizes the given value.
   * 
   * @param value
   *          the value
   * @return the regularized result
   */
  public static final double regularize(final double value) {
    if (value >= 0) {
      return Math.rint(Math.log1p(value));
    }
    return Math.rint(Math.log1p(-value));
  }

  /**
   * the forbidden constructor
   */
  private VMObjectiveUtils() {
    ErrorUtils.doNotCall();
  }
}
