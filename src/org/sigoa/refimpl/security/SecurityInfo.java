/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-22 ${time}
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.security.SecurityInfo.java
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

import java.io.FilePermission;
import java.io.SerializablePermission;
import java.security.Permission;

import org.sigoa.refimpl.events.ErrorEvent;
import org.sigoa.spec.events.IEvent;
import org.sigoa.spec.security.ISecurityInfo;

import sun.security.util.SecurityConstants;

//..begin "Imports"

//..end "Imports"

/**
 * Class provides the default implementation of the
 * <code>ISecurityInfo</code>-interface.
 *
 * @see ISecurityInfo
 */
public class SecurityInfo extends java.lang.SecurityManager implements
    ISecurityInfo {
  /**
   * the serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The name of the event posting permission.
   */
  private static final String       EVENT_POST_PERM       = "postEvents";       //$NON-NLS-1$

  /**
   * The permission to post events in a host's queue.
   */
  protected static final Permission EVENT_POST_PERMISSION = new SerializablePermission(
                                                              EVENT_POST_PERM);

  /**
   * A method internally called in order to check a permission.
   *
   * @param perm
   *          The permission to be checked.
   */
  private static final void doCheckPermission(final Permission perm) {
    if (perm.equals(SecurityConstants.MODIFY_THREAD_PERMISSION)
        || perm.equals(SecurityConstants.MODIFY_THREADGROUP_PERMISSION)
        || perm.getName().equals("exitVM")) {//$NON-NLS-1$
      throw new SecurityException();
    }

    if ((perm instanceof FilePermission)
        && (((FilePermission) perm).getActions().indexOf("execute")//$NON-NLS-1$
        >= 0)) {
      throw new SecurityException();
    }
  }

  /**
   * Throws a <code>SecurityException</code> if the requested access,
   * specified by the given permission, is not permitted based on the
   * security policy currently in effect.
   * <p>
   * This method calls <code>AccessController.checkPermission</code> with
   * the given permission.
   *
   * @param perm
   *          the requested permission.
   * @exception SecurityException
   *              if access is not permitted based on the current security
   *              policy.
   * @exception NullPointerException
   *              if the permission argument is <code>null</code>.
   * @see ISecurityInfo#checkPermission(java.security.Permission)
   */
  @Override
  public void checkPermission(final Permission perm) {
    doCheckPermission(perm);
    if (perm.getName().equals(EVENT_POST_PERM))
      return;
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
   * If <code>context</code> is an instance of
   * <code>AccessControlContext</code> then the
   * <code>AccessControlContext.checkPermission</code> method is invoked
   * with the specified permission.
   * <p>
   * If <code>context</code> is not an instance of
   * <code>AccessControlContext</code> then a
   * <code>SecurityException</code> is thrown.
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
   * @see ISecurityInfo#checkPermission(java.security.Permission, Object)
   */
  @Override
  public void checkPermission(final java.security.Permission perm,
      final Object context) {
    doCheckPermission(perm);
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
    Class<?> c;

    if (event == null)
      throw new SecurityException();
    c = event.getClass();
    if (c != ErrorEvent.class)
      throw new SecurityException();
  }
}
