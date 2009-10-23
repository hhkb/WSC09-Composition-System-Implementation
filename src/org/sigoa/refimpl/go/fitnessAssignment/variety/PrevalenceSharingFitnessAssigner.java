/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-29
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.fitnessAssignment.DifferenceAssigner.java
 * Last modification: 2006-11-29
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

import org.sfc.math.Mathematics;
import org.sigoa.refimpl.go.fitnessAssignment.FitnessAssigner;
import org.sigoa.spec.go.IComparator;
import org.sigoa.spec.go.IIndividual;
import org.sigoa.spec.go.OptimizationUtils;

/**
 * Rate the individuals according to their differences in the objective
 * values.
 * 
 * @param <G>
 *          The genotype.
 * @param <PP>
 *          The phenotype.
 * @author Thomas Weise
 */
public class PrevalenceSharingFitnessAssigner<G extends Serializable, PP extends Serializable>
    extends FitnessAssigner<G, PP> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the fitness of infeasible solution candiates
   */
  private static final double INFEASIBLE_FITNESS = OptimizationUtils.WORST_NUMERIC;

  /**
   * the non-prevailed bonus
   */
  private static final double NON_PREV_BONUS = 1d;

  /**
   * the max winner value
   */
  private static final double MAX_WINNERS = NON_PREV_BONUS
      + Math.nextUp(Math.sqrt(3));

  /**
   * the max looser value
   */
  private static final double MAX_LOOSERS = MAX_WINNERS
      + Math.nextUp(Math.sqrt(3));

  /**
   * the win counts
   */
  private int[] m_win;

  /**
   * the loss counts
   */
  private int[] m_loss;

  /**
   * the equal scores
   */
  private int[] m_equal;

  /**
   * Create a new difference fitness assigner.
   */
  public PrevalenceSharingFitnessAssigner() {
    super();

    this.m_win = new int[1000];
    this.m_loss = new int[1000];
    this.m_equal = new int[1000];
  }

  /**
   * Compute a share value
   * 
   * @param others
   *          the number of equal elements
   * @return the share value
   */
  private static final double share(final int others) {
    return (1.0d / Math.log(Math.E + others));
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
    int[] win, loss, equal;
    int len, i, j, r, k, minWin, minLoss, minEven, maxWin, maxLoss, // 
    maxEven, win1, loose1, even1, win2, loose2, even2, has0;
    final IComparator c;
    double d, s, res;
    long u;
    final double scaleWin, scaleLoss, scaleEven;
    IIndividual<G, PP> x, y;

    c = this.getComparator();

    // remove infeasible elements from population
    len = size;
    for (i = (len - 1); i >= 0; i--) {
      x = source[i];
      for (j = (x.getObjectiveValueCount() - 1); j >= 0; j--) {
        if (!(Mathematics.isNumber(x.getObjectiveValue(j)))) {
          x.setFitness(INFEASIBLE_FITNESS);
          this.output(x);
          if ((--len) <= 0)
            return;
          source[i] = source[len];
        }
      }
    }

    // set counter arrays
    win = this.m_win;
    if (win.length < len) {
      this.m_win = win = new int[len];
      this.m_loss = loss = new int[len];
      this.m_equal = equal = new int[len];
    } else {
      equal = this.m_equal;
      loss = this.m_loss;
    }

    // compute coordinates
    for (i = (len - 1); i >= 0; i--) {
      x = source[i];
      inner: for (j = (i - 1); j >= 0; j--) {
        if (i != j) {
          y = source[j];
          r = c.compare(x, y);
          if (r < 0) {
            win[i]++;
            loss[j]++;
          } else {
            if (r > 0) {
              loss[i]++;
              win[j]++;
            } else {
              // check whether the elements are equal
              k = x.getObjectiveValueCount();
              if (k != y.getObjectiveValueCount())
                continue inner;

              for (--k; k >= 0; k--) {
                if (!(Mathematics.approximatelyEqual(x
                    .getObjectiveValue(k), y.getObjectiveValue(k))))
                  continue inner;
              }

              equal[j] = (++equal[i]);
            }
          }
        }
      }
    }

    minLoss = minEven = minWin = Integer.MAX_VALUE;
    maxLoss = maxEven = maxWin = Integer.MIN_VALUE;

    // normalize wins, losses, and evens
    for (i = (len - 1); i >= 0; i--) {
      r = loss[i];
      minLoss = Math.min(r, minLoss);
      maxLoss = Math.max(r, maxLoss);

      k = win[i];
      minWin = Math.min(k, minWin);
      maxWin = Math.max(k, maxWin);

      k = (len - r - k);
      minEven = Math.min(k, minEven);
      maxEven = Math.max(k, maxEven);
    }

    u = (maxLoss - minLoss);
    scaleLoss = ((u > 0) ? (1.0d / (u * u)) : 1d);

    u = (maxWin - minWin);
    scaleWin = ((u > 0) ? (1.0d / (u * u)) : 1d);

    u = (maxEven - minEven);
    scaleEven = ((u > 0) ? (1.0d / (u * u)) : 1d);

    // compute the fitness
    for (i = (len - 1); i >= 0; i--) {
      x = source[i];

      loose1 = loss[i];
      win1 = win[i];
      even1 = (len - loose1 - win1);

      if (loose1 <= 0) {
        // the non-prevailed elements perform equality sharing
        k = equal[i];

        if (k > 0) {
          // there exist some equal elements
          res = (NON_PREV_BONUS - share(k));
        } else {
          // unique non-prevailed
          res = 0d;
        }
      } else {
        // divide the field into winners and loosers
        // winners are those who win more comparisons than they loose
        // loosers are those who loose at least as often as they win
        if (loose1 < win1)
          res = MAX_WINNERS;
        else
          res = MAX_LOOSERS;

        // process all other elements
        // has0 is the number of elements with distance 0 in comparison
        // space
        has0 = 0;
        d = Double.POSITIVE_INFINITY;
        for (j = (len - 1); j >= 0; j--) {
          if (i != j) {
            win2 = win[j];
            loose2 = loss[j];
            even2 = (len - win2 - loose2);

            u = (win1 - win2);
            s = ((u * u) * scaleWin);

            u = (loose1 - loose2);
            s += ((u * u) * scaleLoss);

            u = (even1 - even2);
            s += ((u * u) * scaleEven);

            s = Math.sqrt(s);
            if (s <= 0d) {
              has0++;
            } else {
              if (s < d)
                d = s;
            }
          }
        }

        // this should not happen
        if (Double.isInfinite(d)) {
          if (has0 < 0)
            res = INFEASIBLE_FITNESS;
        } else {
          // share in comparison space
          if (has0 > 0)
            d *= share(has0);

          // share in objective space is not needed, since it is included
          // in the comparison space sharing, otherwise it would look like:
          // k = equal[i];
          // if (k > 0)
          // d *= share(k);

          res -= d;
        }
      }

      x.setFitness(res);
      this.output(x);
    }
  }
}
