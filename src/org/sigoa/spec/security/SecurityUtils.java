/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-22
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.security.SecurityUtils.java
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

package org.sigoa.spec.security;

import org.sigoa.spec.jobsystem.JobSystemUtils;
import org.sigoa.spec.jobsystem.IHost;
import org.sigoa.spec.jobsystem.IJobInfo;

/**
 * The security utility collection.
 *
 * @author Thomas Weise
 */
public final class SecurityUtils {

  /**
   * Obtain the current security info.
   *
   * @return An instance of <code>ISecurityInfo</code> if there is one,
   *         or <code>null</code> if none is accessible.
   * @throws SecurityException
   *           If there is something suspect going on.
   */
  public static final ISecurityInfo getCurrentSecurityInfo() {
    IHost h;
    IJobInfo<?,?> ji;
    ISecurityInfo si;

    h = JobSystemUtils.getCurrentHost();
    if (h != null) {
      ji = h.getJobInfo();
      if (ji != null) {
        si = ji.getSecurityInfo();
        if (si != null)
          return si;
      }
      throw new SecurityException();
    }

    return null;
  }

  /**
   * Obtain the system security manager security info interface.
   *
   * @return The system security manager security info interface or
   *         <code>null</code> if either no security manager is installed
   *         or if it doesn't support the <code>ISecurityInfo</code>-
   *         interface.
   */
  public static final ISecurityInfo getSecurityInfoManager() {
    java.lang.SecurityManager s;

    s = System.getSecurityManager();
    if (s instanceof ISecurityInfo)
      return ((ISecurityInfo) s);
    return null;
  }

  /**
   * You cannot instantiate this class.
   */
  private SecurityUtils() {
    throw new RuntimeException();
  }
}
