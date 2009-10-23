/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-05-27
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.rbgp.net.base.DefaultActionSet.java
 * Last modification: 2007-05-27
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

package org.dgpf.rbgp.net;

import org.dgpf.rbgp.base.Action;
import org.dgpf.rbgp.net.actions.SendAction;

/**
 * A default action set to inherit from
 * 
 * @author Thomas Weise
 */
public class DefaultNetActionSet extends
    org.dgpf.rbgp.base.DefaultActionSet {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * The globally shared default action set
   */
  public static final DefaultNetActionSet DEFAULT_NET_ACTION_SET = new DefaultNetActionSet(
      false);

  /**
   * the send action
   */
  private final Action<?> m_sendAction;

  /**
   * Create a new default action set
   * 
   * @param canTerminate
   *          whether or not nodes are allowed to terminate themselves
   */
  public DefaultNetActionSet(final boolean canTerminate) {
    super(canTerminate);
    this.m_sendAction = new SendAction(this);
  }

  /**
   * Obtain the send action.
   * 
   * @return the send action
   */
  public final Action<?> getSendAction() {
    return this.m_sendAction;
  }
}
