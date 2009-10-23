/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-12
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.adaptation.CompoundRule.java
 * Last modification: 2006-12-12
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

package org.sigoa.refimpl.adaptation;

import org.sigoa.spec.adaptation.IAction;
import org.sigoa.spec.adaptation.ICondition;
import org.sigoa.spec.adaptation.IRule;

/**
 * An <code>IRule</code>-implementation which consists of a condition
 * and an action.
 *
 * @param <S>
 *          the subject type
 * @author Thomas Weise
 */
public class CompoundRule<S> implements IRule<S> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1L;

  /**
   * the condition part of the rule
   */
  private final ICondition<S> m_condition;

  /**
   * the action part of this rule
   */
  private final IAction<S> m_action;

  /**
   * create a new compound rule
   *
   * @param condition
   *          the condition part
   * @param action
   *          the action part
   * @throws NullPointerException
   *           if <code>(action == null) || (condition == null)</code>
   */
  public CompoundRule(final ICondition<S> condition,
      final IAction<S> action) {
    super();
    if ((action == null) || (condition == null))
      throw new NullPointerException();
    this.m_action = action;
    this.m_condition = condition;
  }

  /**
   * Obtain the conditional part of this rule. If the conditional part
   * evaluates to <code>true</code> for an object, the action part may be
   * carried out on this object.
   *
   * @return The condition that must be met in order to execute this rule's
   *         action.
   */
  public ICondition<S> getCondition() {
    return this.m_condition;
  }

  /**
   * Obtain the action that should be carried out if the conditional part
   * of the rule is met.
   *
   * @return The action part of this rule that should be executed if the
   *         condition part evaluates to <code>true</code>.
   */
  public IAction<S> getAction() {
    return this.m_action;
  }
}
