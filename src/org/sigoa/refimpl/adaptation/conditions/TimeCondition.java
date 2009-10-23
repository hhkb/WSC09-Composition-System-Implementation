/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-12
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.adaptation.conditions.TimeCondition.java
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

package org.sigoa.refimpl.adaptation.conditions;

import org.sigoa.spec.adaptation.ICondition;

/**
 * the time condition triggers after a specified time
 * @param <S> the subject
 * @author Thomas Weise
 */
public class TimeCondition<S> implements ICondition<S> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1L;

  /**
   * the time until the condition will trigger
   */
  private final long m_delay;

  /**
   * the start
   */
  private long m_start;

  /**
   * create a new time condition
   *
   * @param delay
   *          the (relative) time which must elapse until the condition
   *          triggers
   * @throws IllegalArgumentException
   *           if <code>delay&lt;=0</code>
   */
  public TimeCondition(final long delay) {
    super();
    if (delay <= 0)
      throw new IllegalArgumentException();
    this.m_delay = delay;
    this.reset();
  }

  /**
   * This method returns true if the condition is met, false otherwise.
   *
   * @param subject
   *          The subject to check the condition on.
   * @return true if and only if this condition is met, false otherwise
   */
  public boolean evaluate(final S subject) {
    return ((System.currentTimeMillis() - this.m_start) >= this.m_delay);
  }

  /**
   * reset the start time
   */
  public void reset() {
    this.m_start = System.currentTimeMillis();
  }
}
