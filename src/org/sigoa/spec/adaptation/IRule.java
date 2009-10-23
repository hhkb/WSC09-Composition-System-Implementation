/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-12
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.adaptation.IRule.java
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

package org.sigoa.spec.adaptation;

import java.io.Serializable;

/**
 * A rule consists of a condition that should be met and a an action that
 * should be carried out if the condition is met.
 *
 * @param <S>
 *          The type of the subject this rule can be defined for.
 * @author Thomas Weise
 * @version 1.0.0
 */
public interface IRule<S> extends Serializable  {
  /**
   * Obtain the conditional part of this rule. If the conditional part
   * evaluates to <code>true</code> for an object, the action part may be
   * carried out on this object.
   *
   * @return The condition that must be met in order to execute this rule's
   *         action.
   */
  public abstract ICondition<S> getCondition();

  /**
   * Obtain the action that should be carried out if the conditional part
   * of the rule is met.
   *
   * @return The action part of this rule that should be executed if the
   *         condition part evaluates to <code>true</code>.
   */
  public abstract IAction<S> getAction();

}
