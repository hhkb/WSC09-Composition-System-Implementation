/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-22
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.adaptation.MaxIterationRule.java
 * Last modification: 2007-09-22
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

import org.sigoa.refimpl.adaptation.actions.AbortAction;
import org.sigoa.refimpl.adaptation.conditions.IterationCondition;
import org.sigoa.spec.adaptation.IAction;

/**
 * A rule that aborts a job after a given number of maximum iterations,
 * precomposed for your convenience.
 * 
 * @param <S>
 *          the subject type
 * @author Thomas Weise
 */
public class MaxIterationRule<S> extends CompoundRule<S> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1L;

  /**
   * Create a new maximum iteration rule.
   * 
   * @param maxIterations
   *          the maximum number of iterations allowed
   */
  @SuppressWarnings("unchecked")
  public MaxIterationRule(final int maxIterations) {
    super(new IterationCondition<S>(maxIterations),
        (IAction<S>) (AbortAction.ABORT_ACTION));
  }

}
