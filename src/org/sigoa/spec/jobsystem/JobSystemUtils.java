/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-27
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.jobsystem.JobSystemUtils.java
 * Last modification: 2007-02-23
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

package org.sigoa.spec.jobsystem;

/**
 * This class provides additional job system utilitis.
 *
 * @author Thomas Weise
 */
public final class JobSystemUtils {

  /**
   * An <code>IWaitable</code>-instance that does wait for nothing. Its
   * <code>waitFor</code>-routine returns immediately.
   */
  public static final IWaitable NO_WAIT_WAITABLE = new IWaitable() {
    public boolean waitFor(final boolean interruptible) {
      return false;
    }
  };

  /**
   * Obtain the current host.
   *
   * @return The host that runs the code calling this method, or
   *         <code>null</code> if this method is not called by a host
   *         thread.
   */
  public static final IHost getCurrentHost() {
    Thread t;

    t = Thread.currentThread();
    if (t instanceof IHost)
      return ((IHost) t);
    return null;
  }

  /**
   * You cannot instantiate this class.
   */
  private JobSystemUtils() {
    throw new RuntimeException();
  }
}
