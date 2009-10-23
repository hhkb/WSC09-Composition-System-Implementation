/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-28
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.stoch.StatisticInfo.java
 * Last modification: 2006-11-28
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

package org.sigoa.refimpl.stoch;

/**
 * The standard, simple implementation of the statistic information record.
 *
 * @author Thomas Weise
 */
public class StatisticInfo extends StatisticInfoBase {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new statistic info bag.
   */
  public StatisticInfo() {
    super();
  }

  /**
   * Append a value to the statistic info record.
   *
   * @param d
   *          The value to apped.
   */
  public void append(final double d) {
    long l;

    l = this.m_count;
    if (l <= 0) {
      this.m_count = 1L;
      this.m_max = d;
      this.m_min = d;
      this.m_sum = d;
      this.m_sumSqr = (d * d);
    } else {
      this.m_count = (l + 1L);
      if (d > this.m_max)
        this.m_max = d;
      else if (d < this.m_min)
        this.m_min = d;
      this.m_sum += d;
      this.m_sumSqr += (d * d);
    }
  }
}
