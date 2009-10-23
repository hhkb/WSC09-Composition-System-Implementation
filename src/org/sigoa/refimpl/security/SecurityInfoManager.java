/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-22 ${time}
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.security.SecurityInfoManager.java
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
package org.sigoa.refimpl.security;

import java.security.Permission;

import org.sfc.security.SfcSecurityManager;
import org.sigoa.spec.events.IEvent;
import org.sigoa.spec.security.ISecurityInfo;
import org.sigoa.spec.security.SecurityUtils;

// ..begin "Imports"

// ..end "Imports"

/**
 * This security manager replaces the default java security management. It
 * uses the security information of the single optimization jobs in order
 * to determine what a job is allowed to do.
 */
public class SecurityInfoManager extends SfcSecurityManager implements
    ISecurityInfo {
  /**
   * the serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * Create this security manager.
   */
  public SecurityInfoManager() {
    super(true);
  }

  /**
   * Throws a <code>SecurityException</code> if the requested access,
   * specified by the given permission, is not permitted based on the
   * security policy currently in effect.
   * <p>
   * This method deferes to the security info of the job if called from
   * within an executor, otherwise calls the inherited version of this
   * method.
   *
   * @param perm
   *          the requested permission.
   * @exception SecurityException
   *              if access is not permitted based on the current security
   *              policy.
   * @exception NullPointerException
   *              if the permission argument is <code>null</code>.
   * @see ISecurityInfo#checkPermission(Permission)
   */
  @Override
  public void checkPermission(final Permission perm) {
    ISecurityInfo isf;
    if ((isf = SecurityUtils.getCurrentSecurityInfo()) != null)
      isf.checkPermission(perm);
    else
      super.checkPermission(perm);
  }

  /**
   * Throws a <code>SecurityException</code> if the specified security
   * context is denied access to the resource specified by the given
   * permission. The context must be a security context returned by a
   * previous call to <code>getSecurityContext</code> and the access
   * control decision is based upon the configured security policy for that
   * security context.
   * <p>
   * This method deferes to the security info of the job if called from
   * within an executor, otherwise calls the inherited version of this
   * method.
   *
   * @param perm
   *          the specified permission
   * @param context
   *          a system-dependent security context.
   * @exception SecurityException
   *              if the specified security context is not an instance of
   *              <code>AccessControlContext</code> (e.g., is
   *              <code>null</code>), or is denied access to the
   *              resource specified by the given permission.
   * @exception NullPointerException
   *              if the permission argument is <code>null</code>.
   * @see ISecurityInfo#checkPermission(Permission, Object)
   */
  @Override
  public void checkPermission(final Permission perm, final Object context) {
    ISecurityInfo isf;
    if ((isf = SecurityUtils.getCurrentSecurityInfo()) != null)
      isf.checkPermission(perm, context);
    else
      super.checkPermission(perm, context);
  }

  /**
   * Check if the specified event may be propagated.
   *
   * @param event
   *          The event to be checked.
   * @throws SecurityException
   *           If <code>event==null</code> or if it is not permitted.
   */
  public void checkEvent(final IEvent event) {
    ISecurityInfo isf;
    if ((isf = SecurityUtils.getCurrentSecurityInfo()) != null)
      isf.checkEvent(event);
    throw new SecurityException();
  }
}
