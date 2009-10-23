/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-12
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.adaptation.ICondition.java
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
 * This interface denotes the conditional part of a rule that must be
 * fulfilled in order to execute the action part.
 *
 * @param <S>
 *          The type of the subject this condition can be checked on.
 * @author Thomas Weise
 * @version 1.0.0
 */
public interface ICondition<S> extends Serializable {
  /**
   * This method returns true if the condition is met, false otherwise.
   *
   * @param subject
   *          The subject to check the condition on.
   * @return true if and only if this condition is met, false otherwise
   */
  public abstract boolean evaluate(final S subject);

}
