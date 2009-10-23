/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-05-27
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.rbgp.base.DefaultActionSet.java
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

package org.dgpf.rbgp.base;

import org.dgpf.rbgp.base.actions.AddAction;
import org.dgpf.rbgp.base.actions.DivAction;
import org.dgpf.rbgp.base.actions.ModAction;
import org.dgpf.rbgp.base.actions.MulAction;
import org.dgpf.rbgp.base.actions.NotAction;
import org.dgpf.rbgp.base.actions.SetAction;
import org.dgpf.rbgp.base.actions.SubAction;
import org.dgpf.rbgp.base.actions.TerminateAction;

/**
 * A default action set to inherit from
 * 
 * @author Thomas Weise
 */
public class DefaultActionSet extends ActionSet {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * The globally shared default action set
   */
  public static final DefaultActionSet DEFAULT_ACTION_SET = new DefaultActionSet(
      true);

  /**
   * the set action
   */
  private final Action<?> m_setAction;

  /**
   * the add action
   */
  private final Action<?> m_addAction;

  /**
   * the sub action
   */
  private final Action<?> m_subAction;

  /**
   * the not action
   */
  private final Action<?> m_notAction;

  /**
   * the mul action
   */
  private final Action<?> m_mulAction;

  /**
   * the div action
   */
  private final Action<?> m_divAction;

  /**
   * the mod action
   */
  private final Action<?> m_modAction;

  /**
   * The terminate action.
   */
  private final Action<?> m_terminateAction;

  /**
   * Create a new default action set
   * 
   * @param canTerminate
   *          whether or not nodes are allowed to terminate themselves
   */
  public DefaultActionSet(final boolean canTerminate) {
    super();

    this.m_setAction = new SetAction(this);
    this.m_addAction = new AddAction(this);
    this.m_subAction = new SubAction(this);
    this.m_notAction = new NotAction(this);
    this.m_mulAction = new MulAction(this);
    this.m_divAction = new DivAction(this);
    this.m_modAction = new ModAction(this);
    this.m_terminateAction = (canTerminate ? new TerminateAction(this)
        : null);
  }

  /**
   * Obtain the mul action.
   * 
   * @return the mul action
   */
  public final Action<?> getMulAction() {
    return this.m_mulAction;
  }

  /**
   * Obtain the div action.
   * 
   * @return the div action
   */
  public final Action<?> getDivAction() {
    return this.m_divAction;
  }

  /**
   * Obtain the mod action.
   * 
   * @return the nod action
   */
  public final Action<?> getModAction() {
    return this.m_modAction;
  }

  /**
   * Obtain the set action.
   * 
   * @return the set action
   */
  public final Action<?> getSetAction() {
    return this.m_setAction;
  }

  /**
   * Obtain the add action.
   * 
   * @return the add action
   */
  public final Action<?> getAddAction() {
    return this.m_addAction;
  }

  /**
   * Obtain the sub action.
   * 
   * @return the sub action
   */
  public final Action<?> getSubAction() {
    return this.m_subAction;
  }

  /**
   * Obtain the not action.
   * 
   * @return the not action
   */
  public final Action<?> getNotAction() {
    return this.m_notAction;
  }

  /**
   * Obtain the terminate action.
   * 
   * @return the terminate action
   */
  public final Action<?> getTerminateAction() {
    return this.m_terminateAction;
  }
}
