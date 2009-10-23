/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-12
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.adaptation.actions.AbortAction.java
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

package org.sigoa.refimpl.adaptation.actions;

import org.sigoa.spec.adaptation.IAction;
import org.sigoa.spec.jobsystem.IActivity;

/**
 * this action aborts the current optimizer
 *
 * @param <S>
 *          the subject type
 * @author Thomas Weise
 */
public class AbortAction<S> implements IAction<S> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1L;

  /**
   * The globally shared default instance of the abort action
   */
  public static final IAction<Object> ABORT_ACTION = new AbortAction<Object>();

  /**
   * create a new abort optimizer action
   */
  public AbortAction() {
    super();
  }

  /**
   * Perform the defined action on the object <code>subject</code>.
   *
   * @param subject
   *          The subject to execute this rule on.
   */
  public void perform(final S subject) {
    if (subject instanceof IActivity) {
      ((IActivity) subject).abort();
    }
  }
}
