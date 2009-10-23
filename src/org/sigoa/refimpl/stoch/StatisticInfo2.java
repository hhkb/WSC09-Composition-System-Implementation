/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-28
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.stoch.StatisticInfo2.java
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

import java.util.Arrays;

import org.sigoa.spec.stoch.IStatisticInfo;
import org.sigoa.spec.stoch.IStatisticInfo2;

/**
 * The default implementation of the statistic info 2 interface.
 *
 * @author Thomas Weise
 */
public class StatisticInfo2 extends StatisticInfoBase implements
    IStatisticInfo2 {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The value extractor for arrays of doubles.
   */
  public static final IValueExtractor<double[]> DOUBLE_ARRAY_VE = new IValueExtractor<double[]>() {
    public final double getValue(final double[] collection, final int index) {
      return collection[index];
    }
  };

  /**
   * The median of all values.
   *
   * @see #getMedian()
   */
  double m_med;

  /**
   * The first quantil - 25% of all values are smaller than or equal to
   * this one.
   *
   * @see #getQuantil25
   */
  double m_quantil25;

  /**
   * The second quantil - 75% of all values are smaller than or equal to
   * this one.
   *
   * @see #getQuantil75
   */
  double m_quantil75;

  /**
   * The skewness of the values visited.
   *
   * @see #getSkewness()
   */
  double m_skewness;

  /**
   * The kurtosis of the values visited.
   *
   * @see #getKurtosis
   */
  double m_kurtosis;

  /**
   * Create a new <code>IStatisticInfo2</code>-record.
   */
  public StatisticInfo2() {
    super();
  }

  /**
   * Obtain the median of the visited collection items.
   *
   * @return The median of the visited collection items.
   */
  public double getMedian() {
    return this.m_med;
  }

  /**
   * Obtain the first quantil - 25% of all values are smaller than or equal
   * to this one. (This is the first quartil.)
   *
   * @return The first quantil - 25% of all values are smaller than or
   *         equal to this one. (This is the first quartil.)
   */
  public double getQuantil25() {
    return this.m_quantil25;
  }

  /**
   * Obtain the second quantil - 75% of all values are smaller than or
   * equal to this one. (This is the third quartil.)
   *
   * @return The second quantil - 75% of all values are smaller than or
   *         equal to this one. (This is the third quartil.)
   */
  public double getQuantil75() {
    return this.m_quantil75;
  }

  /**
   * Obtain the interquartil range, which is the range between the first
   * and the second quantil (the first and the third quartil).
   *
   * @return The interquartil range, which is the range between the first
   *         and the second quantil (the first and the third quartil).
   */
  public double getInterquartilRange() {
    return Math.max(0.0d, (this.m_quantil75 - this.m_quantil25));
  }

  /**
   * Obtain the skewness of the values visited.
   *
   * @return The skewness of the values visited.
   */
  public double getSkewness() {
    return this.m_skewness;
  }

  /**
   * Obtain the kurtosis of the values visited.
   *
   * @return The kurtosis of the values visited.
   */
  public double getKurtosis() {
    return this.m_kurtosis;
  }

  /**
   * Clear the statistic info record.
   */
  @Override
  public void clear() {
    super.clear();
    this.m_med = Double.NaN;
    this.m_kurtosis = Double.NaN;
    this.m_skewness = Double.NaN;
    this.m_quantil25 = Double.NaN;
    this.m_quantil75 = Double.NaN;
  }

  /**
   * Gather statistic info from a sorted data source.
   *
   * @param src
   *          The data source. The data in this source is sorted in a way
   *          that consecutive access via <code>ext</code> will obtain
   *          consecutive data items. If this data source is sorted
   *          ascending or descending plays no role.
   * @param ext
   *          The data value extractor interface.
   * @param index
   *          The start index.
   * @param count
   *          The count of elements to visit, beginning from
   *          <code>index</code>
   * @param <CollectionType>
   *          The type of the collection to be visited.
   */
  public <CollectionType> void gatherInfoSorted(final CollectionType src,
      final IValueExtractor<CollectionType> ext, final int index,
      final int count) {
    double sum, sumSqr, min, max, kurtosis, med, quantil25, quantil75, skewness, d, da, var, d3, d4, d5;
    int from, to, incr, i;

    this.clear();

    if (count <= 0)
      return;

    // find minimum, maximum, median and the quantiles
    min = ext.getValue(src, index);
    if (count > 1) {
      max = ext.getValue(src, (index + count - 1));

      i = (index + (count >>> 1));
      med = ext.getValue(src, i);
      if ((count & 1) == 0) {
        med = (0.5d * (med + ext.getValue(src, (i - 1))));
      }

      if (count > 3) {
        i = (count >>> 2);
        quantil25 = ext.getValue(src, index + i - 1);
        quantil75 = ext.getValue(src, index + count - i);
      } else {
        quantil25 = med;
        quantil75 = max;
      }
    } else {
      med = min;
      max = min;
      quantil25 = min;
      quantil75 = min;
    }

    // check if the collection was sorted inversely or not.
    if (min > max) {
      d = max;
      max = min;
      min = d;
      d = quantil25;
      quantil25 = quantil75;
      quantil75 = d;
    }

    sum = 0.0d;
    sumSqr = 0.0d;
    kurtosis = 0;//Double.NaN;
    skewness = 0;//Double.NaN;

    // check a which end we can find the absolute smallest numbers, so that
    // precision increases if we start there with summing up
    if (Math.abs(min) > Math.abs(max)) {
      from = (index + count - 1);
      to = (index - 1);
      incr = -1;
    } else {
      from = index;
      to = (index + count);
      incr = 1;
    }

    for (i = from; i != to; i += incr) {
      d = ext.getValue(src, i);
      sum += d;
      sumSqr += (d * d);
    }

    if (count > 1) {
      da = Math.max(min, Math.min(max, (sum / count)));
      d3 = 0.0d;
      d4 = 0.0d;

      for (i = from; i != to; i += incr) {
        d = (ext.getValue(src, i) - da);
        d5 = (d * d);
        d3 += (d5 * d);
        d4 += (d5 * d5);
      }

      d5 = count;
      var = Math.max(0.0d, ((sumSqr - ((sum * sum) / d5)) / (d5 - 1)));
      if ((var > 0.0d) && (count > 2)) {
        da = (d5 - 1d);
        d = (da * (d5 - 2d));
        skewness = ((d5 / d) * (d3 / Math.pow(var, 1.5d)));
        if (count > 3) {
          kurtosis = ((((d5 * (d5 + 1d)) / (d * (d5 - 3d))) * (d4 / (var * var))) - ((3d * da * da) / ((d5 - 2) * (d5 - 3))));
        }
      }
    }

    this.m_count = count;
    this.m_max = max;
    this.m_min = min;
    this.m_med = med;
    this.m_sum = sum;
    this.m_sumSqr = sumSqr;
    this.m_kurtosis = kurtosis;
    this.m_skewness = skewness;
    this.m_quantil25 = quantil25;
    this.m_quantil75 = quantil75;
  }

  /**
   * Gather statistic info from an unsorted data source.
   *
   * @param src
   *          The data source.
   * @param ext
   *          The data value extractor interface.
   * @param index
   *          The start index.
   * @param count
   *          The count of elements to visit, beginning from
   *          <code>index</code>
   * @param <CollectionType>
   *          The type of the collection to be visited.
   */
  public <CollectionType> void gatherInfoUnsorted(
      final CollectionType src, final IValueExtractor<CollectionType> ext,
      final int index, final int count) {
    double[] d;
    int i, j;

    this.clear();

    if (count > 0) {
      d = new double[count];

      for (i = index, j = count; j > 0; j--, i++) {
        d[i] = ext.getValue(src, i);
      }

      Arrays.sort(d);
      this.gatherInfoSorted(d, DOUBLE_ARRAY_VE, 0, count);
    }
  }

  /**
   * Assign this statistic info record to the contents of another one.
   *
   * @param r
   *          The other statistic info record.
   */
  @Override
  public void assign(final IStatisticInfo r) {
    super.assign(r);

    IStatisticInfo2 x;
    if (r instanceof IStatisticInfo2) {
      x = ((IStatisticInfo2) r);
      this.m_kurtosis = x.getKurtosis();
      this.m_med = x.getMedian();
      this.m_quantil25 = x.getQuantil25();
      this.m_quantil75 = x.getQuantil75();
      this.m_skewness = x.getSkewness();
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
    IStatisticInfo2 x;
    if (super.equals(o) && (o instanceof IStatisticInfo2)) {
      x = ((IStatisticInfo2) o);

      if (Double.compare(this.getInterquartilRange(), x
          .getInterquartilRange()) != 0)
        return false;
      if (Double.compare(this.getKurtosis(), x.getKurtosis()) != 0)
        return false;
      if (Double.compare(this.getMedian(), x.getMedian()) != 0)
        return false;
      if (Double.compare(this.getQuantil25(), x.getQuantil25()) != 0)
        return false;
      if (Double.compare(this.getQuantil75(), x.getQuantil75()) != 0)
        return false;
      if (Double.compare(this.getSkewness(), x.getSkewness()) != 0)
        return false;

      return true;
    }
    return false;
  }
}
