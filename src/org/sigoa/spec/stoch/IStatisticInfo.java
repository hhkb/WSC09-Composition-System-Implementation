/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-28
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.stoch.StatisticInfo.java
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

import java.io.Serializable;

/**
 * This interface is common to all objects that are capable of providing
 * statistical information about themselvs.
 *
 * @author Thomas Weise
 * @version 1.0.0
 */
public interface IStatisticInfo extends Serializable {
  /**
   * Obtain the count of elements evaluated.
   *
   * @return The count of elements evaluated.
   */
  public abstract long getCount();

  /**
   * Obtain the minimum of the visited elements.
   *
   * @return The minimum of the visited elements.
   */
  public abstract double getMinimum();

  /**
   * Obtain the maximum of the visited elements.
   *
   * @return The maximum of the visited elements.
   */
  public abstract double getMaximum();

  /**
   * Obtain the average of the visited elements.
   *
   * @return The average of the visited elements.
   */
  public abstract double getAverage();

  /**
   * Obtain the variance of the visited elements.
   *
   * @return The variance of the visited elements.
   */
  public abstract double getVariance();

  /**
   * Obtain the coefficient of variation.
   *
   * @return The coefficient of variation.
   */
  public abstract double getCoefficientOfVariation();

  /**
   * Obtain the standard deviation of the visited elements.
   *
   * @return The standard deviation of the visited elements.
   */
  public abstract double getStdDev();

  /**
   * Obtain the sum of all elements visited.
   *
   * @return The sum of all elements visited.
   */
  public abstract double getSum();

  /**
   * Obtain the sum of the squares of all elements visited.
   *
   * @return The sum of the squares of all elements visited.
   */
  public abstract double getSumSqr();

  /**
   * Obtain the range of the values visited.
   *
   * @return The range of the values visited.
   */
  public abstract double getRange();

}
