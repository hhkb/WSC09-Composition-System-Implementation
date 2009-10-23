/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-22
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.security.ISecurityInfo.java
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

import java.io.Serializable;
import java.security.Permission;

import org.sigoa.spec.events.IEvent;

/**
 * This interface provides the security information for jobs. It can be
 * obtained from the <code>IHost</code>-interface provided by all
 * threads of the executors. <code>ISecurityInfo</code> is very similar
 * to the java class <code>SecurityManager</code> and used as a delegate
 * by the internal security management.
 *
 * @see SecurityManager
 * @author Thomas Weise
 * @version 1.0.0
 */
public interface ISecurityInfo extends Serializable {
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
   */
  public abstract void checkPermission(final Permission perm);

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
   */
  public abstract void checkPermission(final Permission perm,
      final Object context);

  /**
   * Check if the specified event may be propagated.
   *
   * @param event
   *          The event to be checked.
   * @throws SecurityException
   *           If <code>event==null</code> or if it is not permitted.
   */
  public abstract void checkEvent(final IEvent event);
}
