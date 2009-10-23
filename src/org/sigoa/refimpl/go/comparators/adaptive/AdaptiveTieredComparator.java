/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-04-12
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.comparators.AdaptiveTieredComparator.java
 * Last modification: 2008-04-12
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

package org.sigoa.refimpl.go.comparators.adaptive;

import java.io.Serializable;

import org.sfc.math.Mathematics;
import org.sigoa.spec.go.IIndividual;
import org.sigoa.spec.go.OptimizationUtils;
import org.sigoa.spec.pipe.IPipe;

/**
 * <p>
 * This comparator performs a tired comparison in an adaptive manner. This
 * comparator adapts itself to the objective values of the individuals that
 * pass through it. Notice that this tiering approach not necessarily
 * specifies ranges for all objectives
 * </p>
 * 
 * @author Thomas Weise
 */
public class AdaptiveTieredComparator extends DecisiveParetoComparator
    implements IPipe<Serializable, Serializable> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the tiers
   */
  private final int[] m_tiers;

  /**
   * the fractions
   */
  private final int[] m_fractions;

  /**
   * the pareto ranges
   */
  private volatile double[] m_ranges;

  /**
   * the pareto ranges
   */
  private volatile double[] m_newRanges;

  /**
   * Create a new adaptive tired comparator.
   * 
   * @param fractions
   *          the fractions to be used for comparison
   * @param decisiveClasses
   *          the number of classes needed for decisiveness of the
   *          objective
   */
  public AdaptiveTieredComparator(final int[][] fractions,
      final int[] decisiveClasses) {
    super(decisiveClasses, flatten(fractions, decisiveClasses));
    int i, j, k, s, l;
    int[] t, f, fs;

    s = fractions.length;
    this.m_tiers = t = new int[s];
    l = decisiveClasses.length;
    this.m_fractions = fs = new int[l];

    k = 0;
    for (i = 0; i < s; i++) {
      f = fractions[i];
      t[i] = fractions[i].length;

      for (j = 0; j < f.length; j++) {
        fs[k++] = f[j];
      }
    }

    this.m_ranges = new double[l];
    this.m_newRanges = new double[l];
  }

  /**
   * flattens the tier representation
   * 
   * @param fractions
   *          the fractions to be used for comparison
   * @param decisiveClasses
   *          the number of classes needed for decisiveness of the
   *          objective
   * @return the flattened representation
   */
  private static final int[] flatten(final int[][] fractions,
      final int[] decisiveClasses) {
    final int[] res;
    int[] x;
    int i, j, k;

    i = decisiveClasses.length;
    res = decisiveClasses.clone();

    k = 0;
    for (i = 0; i < fractions.length; i++) {
      x = fractions[i];
      for (j = 0; j < x.length; j++) {
        res[k] = Math.max(res[k], x[j]);
        k++;
      }
    }

    return res;
  }

  /**
   * Compare two individuals with each other. The comparison is performed
   * only on the base of <code>count</code> objective values starting
   * with the <code>from</code><sup>th</sup> one.
   * 
   * @param o1
   *          The first individual to be compared.
   * @param o2
   *          The second individual to be compared.
   * @param from
   *          the index of the first objective value to be taken into
   *          consideration
   * @param to
   *          the exclusive last index of the comparison
   * @return a negative value if o1 prevails o2, a positive value if o2
   *         prevails o1 and 0 if non of them prevails the other one.
   */
  @Override
  protected int compare(final IIndividual<?, ?> o1,
      final IIndividual<?, ?> o2, final int from, final int to) {

    double v1, v2, d;
    int tierEnd, i, s, j, z;
    final int[] tiers;
    final double[] r;
    final boolean[] decisive;

    tiers = this.m_tiers;
    i = from;
    r = this.m_ranges;
    decisive = this.m_decisive;

    main: for (z = 0; z < tiers.length; z++) {
      tierEnd = (tiers[z] + from);
      s = 0;

      inner: for (;;) {
        if (decisive[i]) {
          v1 = o1.getObjectiveValue(i);
          v2 = o2.getObjectiveValue(i);

          // perform the single comparison
          j = Double.compare(v1, v2);

          if (j != 0) {
            if (v1 <= OptimizationUtils.BEST)
              j = -2;
            else if (v2 <= OptimizationUtils.BEST)
              j = 2;
            else {
              d = (v1 - v2);
              if (Math.abs(d) >= r[i])
                j = 2;
              else
                j = 1;

              if (d < 0d)
                j = -j;
            }
          }
          // end comparison

          // update the comparison
          if (j < 0) {
            if (s > 0)
              break main;
            if (j < s)
              s = j;
          } else {
            if (j > 0) {
              if (s < 0)
                break main;
              if (j > s)
                s = j;
            }
          }
        }
        i++;
        if ((i >= to) || (i >= tierEnd)) {
          if (Math.abs(s) > 1)
            return s;
          if (i >= to)
            break main;
          break inner;
        }
      }

      if (s != 0)
        break main;
    }

    return super.compare(o1, o2, from, to);
  }

  /**
   * process the data
   */
  @Override
  void processData() {
    final double[][] classes;
    final double[] r;
    double[] mix;
    final int[] f, sc;
    int i, j;
    double v;

    super.processData();

    classes = this.m_classes;
    f = this.m_fractions;
    sc = this.m_classCount;
    i = f.length;

    r = this.m_newRanges;
    for (--i; i >= 0; i--) {
      mix = classes[i];
      v = 0d;
      j = Math.min(sc[i], f[i]);

      if (j > 1) {
        v = (mix[j - 1] - mix[0]);
        if (!(Mathematics.isNumber(v)))
          v = 0d;
      }

      r[i] = v;
    }

    this.m_newRanges = this.m_ranges;
    this.m_ranges = r;
  }

}
