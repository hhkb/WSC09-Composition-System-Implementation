/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-28
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.stoch.StatisticInfo.java
 * Last modification: 2008-05-15
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

import org.sfc.text.TextUtils;
import org.sfc.text.Textable;
import org.sfc.utils.ErrorUtils;
import org.sigoa.spec.stoch.IStatisticInfo;

/**
 * The internal base class for statistic information records.
 * 
 * @author Thomas Weise
 */
class StatisticInfoBase extends Textable implements IStatisticInfo,
    Cloneable {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The minimum value.
   * 
   * @see #getMinimum()
   */
  double m_min;

  /**
   * The maximum value.
   * 
   * @see #getMaximum()
   */
  double m_max;

  /**
   * The sum of all values.
   * 
   * @see #getSum()
   */
  double m_sum;

  /**
   * The count of all values.
   * 
   * @see #getCount()
   */
  long m_count;

  /**
   * The sum of the squares of all values.
   * 
   * @see #getSumSqr()
   */
  double m_sumSqr;

  /**
   * Create a new statistic info bag.
   */
  StatisticInfoBase() {
    super();
    this.clear();
  }

  /**
   * Obtain the count of elements evaluated.
   * 
   * @return The count of elements evaluated.
   */
  public long getCount() {
    return this.m_count;
  }

  /**
   * Obtain the minimum of the visited elements.
   * 
   * @return The minimum of the visited elements.
   */
  public double getMinimum() {
    return this.m_min;
  }

  /**
   * Obtain the maximum of the visited elements.
   * 
   * @return The maximum of the visited elements.
   */
  public double getMaximum() {
    return this.m_max;
  }

  /**
   * Obtain the average of the visited elements.
   * 
   * @return The average of the visited elements.
   */
  public double getAverage() {
    long l;

    l = this.m_count;
    if (l <= 0)
      return Double.NaN;

    return Math.min(this.m_max, Math.max(this.m_min, (this.m_sum / l)));
  }

  /**
   * Obtain the variance of the visited elements.
   * 
   * @return The variance of the visited elements.
   */
  public double getVariance() {
    double s;
    long l;

    l = this.m_count;
    if (l <= 1) {
      if (l <= 0)
        return Double.NaN;
      return 0.0d;
    }

    s = this.m_sum;

    return Math.max(0.0d, ((this.m_sumSqr - ((s * s) / l)) / (l - 1)));
  }

  /**
   * Obtain the coefficient of variation.
   * 
   * @return The coefficient of variation.
   */
  public double getCoefficientOfVariation() {
    double s;
    long l;

    l = this.m_count;
    s = this.m_sum;

    if (s == 0.0d)
      return Double.NaN;

    if (l <= 1) {
      if (l <= 0)
        return Double.NaN;
      return 0.0d;
    }

    return ((l * Math.sqrt(Math.max(0.0d,
        ((this.m_sumSqr - ((s * s) / l)) / (l - 1))))) / s);
  }

  /**
   * Obtain the standard deviation of the visited elements.
   * 
   * @return The standard deviation of the visited elements.
   */
  public double getStdDev() {
    return Math.sqrt(this.getVariance());
  }

  /**
   * Obtain the sum of all elements visited.
   * 
   * @return The sum of all elements visited.
   */
  public double getSum() {
    return this.m_sum;
  }

  /**
   * Obtain the sum of the squares of all elements visited.
   * 
   * @return The sum of the squares of all elements visited.
   */
  public double getSumSqr() {
    return this.m_sumSqr;
  }

  /**
   * Obtain the range of the values visited.
   * 
   * @return The range of the values visited.
   */
  public double getRange() {
    return Math.max(0.0d, (this.m_max - this.m_min));
  }

  /**
   * Clear the statistic info record.
   */
  public void clear() {
    this.m_count = 0L;
    this.m_max = Double.NaN;
    this.m_min = Double.NaN;
    this.m_sum = 0.0d;
    this.m_sumSqr = 0.0d;
  }

  /**
   * Assign this statistic info record to the contents of another one.
   * 
   * @param r
   *          The other statistic info record.
   */
  public void assign(final IStatisticInfo r) {
    if (r == null)
      throw new NullPointerException();
    this.clear();
    this.m_count = r.getCount();
    this.m_max = r.getMaximum();
    this.m_min = r.getMinimum();
    this.m_sum = r.getSum();
    this.m_sumSqr = r.getSumSqr();
  }

  /**
   * Copy this statistic info record.
   * 
   * @return A copy of this statistic info record.
   */
  @Override
  public Object clone() {
    try {
      return super.clone();
    } catch (CloneNotSupportedException e) {
      ErrorUtils.onError(e);
      return null; // should not happen
    }
  }

  /**
   * Compare this statistic info record with another one.
   * 
   * @param o
   *          The other statistic info record.
   * @return <code>true</code> if and only if the records hold the same
   *         data.
   */
  @Override
  public boolean equals(final Object o) {
    IStatisticInfo x;
    if (o == this)
      return true;
    if (o instanceof IStatisticInfo) {
      x = ((IStatisticInfo) o);

      if (Double.compare(this.m_count, x.getCount()) != 0)
        return false;
      if (Double.compare(this.m_max, x.getMaximum()) != 0)
        return false;
      if (Double.compare(this.m_min, x.getMinimum()) != 0)
        return false;
      if (Double.compare(this.m_sum, x.getSum()) != 0)
        return false;
      if (Double.compare(this.m_sumSqr, x.getSumSqr()) != 0)
        return false;

      if (Double.compare(this.getAverage(), x.getAverage()) != 0)
        return false;
      if (Double.compare(this.getCoefficientOfVariation(), x
          .getCoefficientOfVariation()) != 0)
        return false;
      if (Double.compare(this.getRange(), x.getRange()) != 0)
        return false;
      if (Double.compare(this.getStdDev(), x.getStdDev()) != 0)
        return false;

      return true;
    }
    return false;
  }

  /** the count */
  private static final char[] COUNT = "count :".toCharArray(); //$NON-NLS-1$

  /** the min */
  private static final char[] MIN = "min   :".toCharArray(); //$NON-NLS-1$

  /** the max */
  private static final char[] MAX = "max   :".toCharArray(); //$NON-NLS-1$

  /** the range */
  private static final char[] RANGE = "range :".toCharArray(); //$NON-NLS-1$

  /** the sum */
  private static final char[] SUM = "sum x :".toCharArray(); //$NON-NLS-1$

  /** the sum of squares */
  private static final char[] SUM_SQR = "sum x²:".toCharArray(); //$NON-NLS-1$

  /** the average */
  private static final char[] AVG = "avg   :".toCharArray(); //$NON-NLS-1$

  /** the variance */
  private static final char[] VAR = "var   :".toCharArray(); //$NON-NLS-1$

  /** the stddev */
  private static final char[] STDDEV = "stddev:".toCharArray(); //$NON-NLS-1$

  /**
   * Append this object's textual representation to a string builder.
   * 
   * @param sb
   *          The string builder to append to.
   * @see #toString()
   */
  @Override
  public void toStringBuilder(final StringBuilder sb) {
    sb.append(COUNT);
    sb.append(this.getCount());
    sb.append(TextUtils.LINE_SEPARATOR);

    sb.append(MIN);
    sb.append(this.getMinimum());
    sb.append(TextUtils.LINE_SEPARATOR);

    sb.append(MAX);
    sb.append(this.getMaximum());
    sb.append(TextUtils.LINE_SEPARATOR);

    sb.append(RANGE);
    sb.append(this.getRange());
    sb.append(TextUtils.LINE_SEPARATOR);

    sb.append(SUM);
    sb.append(this.getSum());
    sb.append(TextUtils.LINE_SEPARATOR);

    sb.append(SUM_SQR);
    sb.append(this.getSumSqr());
    sb.append(TextUtils.LINE_SEPARATOR);

    sb.append(AVG);
    sb.append(this.getAverage());
    sb.append(TextUtils.LINE_SEPARATOR);

    sb.append(VAR);
    sb.append(this.getVariance());
    sb.append(TextUtils.LINE_SEPARATOR);

    sb.append(STDDEV);
    sb.append(this.getStdDev());
    sb.append(TextUtils.LINE_SEPARATOR);
  }
}
