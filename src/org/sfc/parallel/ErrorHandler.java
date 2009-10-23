/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-02-25
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.parallel.ErrorHandler.java
 * Last modification: 2007-02-25
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

package org.sfc.parallel;

import org.sfc.utils.ErrorUtils;
import org.sfc.utils.Utils;

/**
 * Instances of this class are capable of handling errors
 *
 * @author Thomas Weise
 */
public class ErrorHandler {

  /**
   * It is recommended that derived classes use this method for
   * error-processing.
   *
   * @param t
   *          the error object
   */
  protected void onError(final Throwable t) {
    checkError(t);
  }

  /**
   * check the given error
   *
   * @param t
   *          the error
   */
  static final void checkError(final Throwable t) {
    if (t instanceof VirtualMachineError)
      Utils.invokeGC();
    ErrorUtils.onError(t);
  }

}
