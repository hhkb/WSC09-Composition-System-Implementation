/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-22
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.utils.ErrorUtils.java
 * Last modification: 2007-08-03
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

package org.sfc.utils;

/**
 * This class provides some utilties for errors.
 * 
 * @author Thomas Weise
 */
public final class ErrorUtils {

  /**
   * the do not call error
   */
  private static final String DO_NOT_CALL_ERROR = "I told you not to call this method!!!";//$NON-NLS-1$ 

  /**
   * This method should be called inside methods that must never be
   * invoked, such as private constructors of method collection objects.
   */
  public static final void doNotCall() {
    RuntimeException t;

    t = new RuntimeException(DO_NOT_CALL_ERROR);
    onError(t);
    throw t;
  }

  /**
   * This method is invoked whenever an error occurs.
   * 
   * @param e
   *          the error to process
   */
  public static final void onError(final String e) {
    onError(new Throwable(e));
  }

  /**
   * This method is invoked whenever an error occurs.
   * 
   * @param t
   *          the error to process
   */
  public static final synchronized void onError(final Throwable t) {
    try {
      Log.getLog().log(t);
    } catch (Throwable tr) {//

    }
  }

  /**
   * Check two errors for equality.
   * 
   * @param t1
   *          The first error.
   * @param t2
   *          The second error.
   * @return <code>true</code> if and only if the identify exactly the
   *         same error.
   */
  public static final boolean testErrorEqual(final Throwable t1,
      final Throwable t2) {

    StackTraceElement[] st1, st2;
    int i;

    if (t1 == t2)
      return true;

    if ((t1 == null) || (t2 == null))
      return false;

    if (Utils.testEqual(t1.getClass(), t2.getClass())
        && Utils.testEqual(t1.getMessage(), t2.getMessage())
        && Utils.testEqual(t1.getLocalizedMessage(), t2
            .getLocalizedMessage())) {
      st1 = t1.getStackTrace();
      st2 = t2.getStackTrace();

      i = st1.length;
      if (i != st2.length)
        return false;

      for (--i; i >= 0; i--) {
        if (!(Utils.testEqual(st1[i], st2[i])))
          return false;
      }
      return true;
    }
    return false;
  }

  /**
   * This class cannot be instantiated.
   */
  private ErrorUtils() {
    doNotCall();
  }
}
