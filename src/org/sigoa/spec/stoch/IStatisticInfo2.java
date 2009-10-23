/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-28
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.stoch.IStatisticInfo2.java
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

package org.sigoa.spec.stoch;

/**
 * This enhanced statistic info record bears additional statistic
 * information.
 *
 * @author Thomas Weise
 * @version 1.0.0
 * @see IStatisticInfo
 */
public interface IStatisticInfo2 extends IStatisticInfo {
  /**
   * Obtain the median of the visited collection items.
   *
   * @return The median of the visited collection items.
   */
  public abstract double getMedian();

  /**
   * Obtain the first quantil - 25% of all values are smaller than or equal
   * to this one. (This is the first quartil.)
   *
   * @return The first quantil - 25% of all values are smaller than or
   *         equal to this one. (This is the first quartil.)
   */
  public abstract double getQuantil25();

  /**
   * Obtain the second quantil - 75% of all values are smaller than or
   * equal to this one. (This is the third quartil.)
   *
   * @return The second quantil - 75% of all values are smaller than or
   *         equal to this one. (This is the third quartil.)
   */
  public abstract double getQuantil75();

  /**
   * Obtain the interquartil range, which is the range between the first
   * and the second quantil (the first and the third quartil).
   *
   * @return The interquartil range, which is the range between the first
   *         and the second quantil (the first and the third quartil).
   */
  public abstract double getInterquartilRange();

  /**
   * Obtain the skewness of the values visited.
   *
   * @return The skewness of the values visited.
   */
  public abstract double getSkewness();

  /**
   * Obtain the kurtosis of the values visited.
   *
   * @return The kurtosis of the values visited.
   */
  public abstract double getKurtosis();

}
