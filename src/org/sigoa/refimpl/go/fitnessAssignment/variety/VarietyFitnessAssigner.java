/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-01-23
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.fitnessAssignment.variety.VarietyFitnessAssigner.java
 * Last modification: 2008-05-12
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

package org.sigoa.refimpl.go.fitnessAssignment.variety;

import java.io.Serializable;
import java.util.Arrays;

import org.sfc.math.Mathematics;
import org.sigoa.refimpl.go.fitnessAssignment.FitnessAssigner;
import org.sigoa.spec.go.IComparator;
import org.sigoa.spec.go.IIndividual;

/**
 * The base class for variety fitness assignment
 * 
 * @param <G>
 *          The genotype.
 * @param <PP>
 *          The phenotype.
 * @author Thomas Weise
 */
public class VarietyFitnessAssigner<G extends Serializable, PP extends Serializable>
    extends FitnessAssigner<G, PP> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the losses
   */
  private transient int[] m_losses;

  /**
   * the objective ranges
   */
  private transient double[] m_oRanges;

  /**
   * the objective minima
   */
  private transient double[] m_oMin;

  /**
   * the share values
   */
  private transient double[] m_share;

  /**
   * the maximum loss
   */
  private transient double m_penalty;

  /**
   * Remove all infeasible elements from the population.
   * 
   * @param source
   *          The buffered source population.
   * @param size
   *          The count of individuals in the population array.
   * @return the number of remaining individuals
   */
  protected final int removeInfeasible(final IIndividual<G, PP>[] source,
      final int size) {
    int i, j, len;
    final double infeasible;
    IIndividual<G, PP> x;

    if (size <= 0)
      return 0;

    infeasible = (1.1d * (1 + size + Math.sqrt(size)));

    len = size;
    for (i = (len - 1); i >= 0; i--) {
      x = source[i];
      inner: for (j = (x.getObjectiveValueCount() - 1); j >= 0; j--) {
        if (!(Mathematics.isNumber(x.getObjectiveValue(j)))) {
          x.setFitness(infeasible);
          this.output(x);
          if ((--len) <= 0)
            return 0;
          source[i] = source[len];
          break inner;
        }
      }
    }

    return len;
  }

  /**
   * the share power
   */
  private static final double SHARE_POWER = -16d;

  /**
   * the minimum share
   */
  private static final double MIN_SHARE = Math.exp(SHARE_POWER);

  /**
   * the share factor
   */
  private static final double SHARE_FACTOR = 1.0d / (1 - MIN_SHARE);

  /**
   * Perform a sharing.
   * 
   * @param value
   *          a positive value between 0 and max, the smaller the worst
   * @return the sharing value between 0 and 1 to multiply a fitness with
   *         (the larger the worst)
   */
  protected static final double share(final double value) {
    if (value <= 0d)
      return 1d;
    if (value >= 1d)
      return 0d;

    return (Math.exp(SHARE_POWER * value) - MIN_SHARE) * SHARE_FACTOR;
  }

  /**
   * Compute the objective ranges.
   * 
   * @param source
   *          The buffered source population.
   * @param len
   *          the length
   * @return the new length
   */
  protected final int computeShare(final IIndividual<G, PP>[] source,
      final int len) {
    double[] min, range, shares;
    int i, j, k;
    double d, s;
    int oc;
    final double maxDist;
    double maxShare, minShare, curShare;
    IIndividual<G, PP> x, y;

    if (len <= 0)
      return 0;

    oc = this.getObjectiveValueCount();
    if (len > 0)
      oc = Math.max(oc, source[0].getObjectiveValueCount());

    min = this.m_oMin;
    if ((min == null) || (min.length < oc)) {
      this.m_oMin = min = new double[oc];
      this.m_oRanges = range = new double[oc];
    } else
      range = this.m_oRanges;

    Arrays.fill(min, 0, oc, Double.POSITIVE_INFINITY);
    Arrays.fill(range, 0, oc, Double.NEGATIVE_INFINITY);

    // find minima and maxima
    for (i = (len - 1); i >= 0; i--) {
      x = source[i];

      for (j = (oc - 1); j >= 0; j--) {
        d = x.getObjectiveValue(j);
        if (d < min[j])
          min[j] = d;
        if (d > range[j])
          range[j] = d;
      }
    }

    // find ranges
    for (j = (oc - 1); j >= 0; j--) {
      d = range[j] - min[j];
      range[j] = ((d <= 0d) ? 1d : (1 / d));
    }

    shares = this.m_share;
    if ((shares == null) || (shares.length < len)) {
      this.m_share = shares = new double[len];
    }

    maxShare = Double.NEGATIVE_INFINITY;
    minShare = Double.POSITIVE_INFINITY;
    maxDist = (1.0d / Math.sqrt(oc));
    Arrays.fill(shares, 0, len, 0d);
    for (i = (len - 1); i > 0; i--) {
      x = source[i];
      for (j = (oc - 1); j >= 0; j--) {
        min[j] = x.getObjectiveValue(j);
      }

      curShare = shares[i];
      for (j = (i - 1); j >= 0; j--) {
        y = source[j];
        s = 0d;
        for (k = (oc - 1); k >= 0; k--) {
          d = ((min[k] - y.getObjectiveValue(k)) * range[k]);
          s += (d * d);
        }

        s = share(Math.sqrt(s) * maxDist);
        curShare += s;
        shares[j] += s;
      }

      shares[i] = curShare;
      if (curShare > maxShare)
        maxShare = curShare;
      if (curShare < minShare)
        minShare = curShare;
    }
    curShare = shares[0];
    if (curShare > maxShare)
      maxShare = curShare;
    if (curShare < minShare)
      minShare = curShare;

    maxShare = (maxShare - minShare);

    if ((maxShare > 0d) && (!(Double.isInfinite(maxShare)))
        && (!(Double.isInfinite(minShare)))) {
      maxShare = (1d / maxShare);
      for (i = (len - 1); i >= 0; i--) {
        shares[i] = ((shares[i] - minShare) * maxShare);
      }
    }

    return len;
  }

  /**
   * Compute the statistics.
   * 
   * @param source
   *          The buffered source population.
   * @param size
   *          The count of individuals in the population array.
   * @return the number of remaining individuals
   */
  protected final int computeStatistics(final IIndividual<G, PP>[] source,
      final int size) {
    final IComparator c;
    int i, j, r;
    int[] losses;
    IIndividual<G, PP> x;

    if (size <= 0)
      return 0;

    c = this.getComparator();

    losses = this.m_losses;
    if ((losses == null) || (losses.length < size)) {
      this.m_losses = losses = new int[size];
    }

    Arrays.fill(losses, 0, size, 0);

    for (i = (size - 1); i > 0; i--) {
      x = source[i];
      for (j = (i - 1); j >= 0; j--) {
        if (i != j) {
          r = c.compare(x, source[j]);
          if (r < 0) {
            losses[j]++;
          } else {
            if (r > 0) {
              losses[i]++;
            }
          }
        }
      }
    }

    j = 0;
    for (i = (size - 1); i >= 0; i--) {
      j = Math.max(j, losses[i]);
    }

    this.m_penalty = Math
        .max(1d, (j > 0) ? Math.sqrt(j) : Math.sqrt(size));

    return size;
  }

  /**
   * Assign the fitness.
   * 
   * @param source
   *          The buffered source population.
   * @param size
   *          The count of individuals in the population array.
   */
  @Override
  protected void process(final IIndividual<G, PP>[] source, final int size) {
    int len;
    int g, i;
    final double[] share;
    final double shareFactor;
    final int[] losses;
    double f;
    IIndividual<G, PP> x;

    len = this.removeInfeasible(source, size);
    if (len <= 0)
      return;

    len = this.computeStatistics(source, len);
    if (len <= 0)
      return;

    len = this.computeShare(source, len);
    if (len <= 0)
      return;

    share = this.m_share;
    losses = this.m_losses;
    shareFactor = this.m_penalty;// Math.sqrt(len);

    for (i = 0; i < len; i++) {
      x = source[i];

      g = losses[i];
      f = share[i];
      if (g > 0) {
        f = (g + (shareFactor * f));
      }

      x.setFitness(f);
      this.output(x);
    }
  }
}
