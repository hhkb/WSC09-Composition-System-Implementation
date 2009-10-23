/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-15
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.clustering.algorithms.NNearestNeighborClustering.java
 * Last modification: 2006-12-15
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

package org.sigoa.refimpl.clustering.algorithms;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;

import org.sigoa.refimpl.clustering.ClusteringAlgorithm;
import org.sigoa.refimpl.go.Individual;
import org.sigoa.refimpl.go.comparators.HierarchicalComparator;
import org.sigoa.spec.clustering.IIndividualDistanceMeasure;
import org.sigoa.spec.go.IIndividual;

/**
 * this clustering algorithm only allows those individuals to pass which
 * kth nearest neighbor is the farthest away
 *
 * @param <G>
 *          the genotype
 * @param <PP>
 *          the phenotype
 * @author Thomas Weise
 */
public class NNearestNeighborClustering<G extends Serializable, PP extends Serializable>
    extends ClusteringAlgorithm<G, PP> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the cache
   */
  private transient Holder<G, PP>[] m_cache;

  /**
   * the k value
   */
  private final int m_k;

  /**
   * the minima
   */
  private double[] m_mins;

  /**
   * the maxima
   */
  private double[] m_maxs;

  /**
   * the internal comparator
   */
  private static final Comparator<Holder<?, ?>> CMP = new Comparator<Holder<?, ?>>() {

    public final int compare(final Holder<?, ?> o1, final Holder<?, ?> o2) {
      int i, j;
      double[] d1, d2;
      d1 = o1.m_minDists;
      d2 = o2.m_minDists;
      for (i = (d1.length - 1); i >= 0; i--) {
        j = Double.compare(d1[i], d2[i]);
        if (j != 0)
          return j;
      }

      return HierarchicalComparator.HIERARCHICAL_COMPARATOR.compare(
          o1.m_indi, o2.m_indi);
    }
  };

  /**
   * Create a new pass through algorithm instance.
   *
   * @param count
   *          The initial selection size.
   * @param distanceMeasure
   *          the distance measure to be used for individual distance
   *          measuring - if this is <code>null</code> a default distance
   *          measure will be applied
   * @param k
   *          the index of the neighbor used for comparisons.
   * @throws IllegalArgumentException
   *           if <code>count&lt;=0||k&lt;1</code>.
   */
  @SuppressWarnings("unchecked")
  public NNearestNeighborClustering(final int count,
      final IIndividualDistanceMeasure<G, PP> distanceMeasure, final int k) {
    super(count, distanceMeasure);

    if (k < 1)
      throw new IllegalArgumentException();

    Holder<G, PP>[] c;
    int i;

    c = new Holder[count];
    for (i = (count - 1); i >= 0; i--) {
      c[i] = new Holder<G, PP>(k);
    }
    this.m_cache = c;

    this.m_k = k;
    this.m_mins = new double[16];
    this.m_maxs = new double[16];
  }

  /**
   * Process the buffered population.
   *
   * @param source
   *          The buffered source population.
   * @param size
   *          The count of individuals in the population array.
   * @param pass
   *          the number of individuals allowed to pass
   */
  @Override
  @SuppressWarnings("unchecked")
  protected void doProcess(final IIndividual<G, PP>[] source,
      final int size, final int pass) {
    int j, i, k, z;
    Holder<G, PP>[] hs;
    Holder<G, PP> h1, h2;
    double d;
    double[] dx, dy, mins, maxs;
    IIndividualDistanceMeasure<G, PP> xx;
    IIndividual<G, PP> ii;
    boolean b1, b2;

    // ensure the holders are available
    k = this.m_k;
    hs = this.m_cache;
    j = hs.length;
    if (j < size) {
      i = (size * 3) >>> 1;
      hs = new Holder[i];
      System.arraycopy(this.m_cache, 0, hs, 0, j);
      for (--i; i >= j; i--)
        hs[i] = new Holder<G, PP>(k);
      this.m_cache = hs;
    }

    xx = this.getIndividualDistanceMeasure();

    // grab the normalization lists
    mins = this.m_mins;
    ii = source[0];
    z = ii.getObjectiveValueCount();

    if (z > mins.length) {
      this.m_mins = mins = new double[z];
      this.m_maxs = maxs = new double[z];
    } else
      maxs = this.m_maxs;

    Arrays.fill(mins, 0, z, Double.POSITIVE_INFINITY);
    Arrays.fill(maxs, 0, z, Double.NEGATIVE_INFINITY);
    --z;

    // initialize the holders, clear the single distance arrays
    // and initialize the normalization matrix
    for (i = (size - 1); i >= 0; i--) {
      h1 = hs[i];
      h1.m_indi = ii = source[i];
      Arrays.fill(h1.m_minDists, Double.POSITIVE_INFINITY);
      for (j = z; j >= 0; j--) {
        d = ii.getObjectiveValue(z);
        if (d < mins[j])
          mins[j] = d;
        if (d > maxs[j])
          maxs[j] = d;
      }
    }

    // normalize the normalization arrays
    for (i = z; i >= 0; i--) {
      d = 1.0d / (maxs[i] - mins[i]);
      if ((d > Double.MIN_VALUE) && (d == d)) {
        maxs[i] = d;
      } else {
        maxs[i] = 1d;
      }
    }

    // build the distance matrix
    for (i = (size - 1); i > 0; i--) {
      h1 = hs[i];
      dx = h1.m_minDists;
      for (j = (i - 1); j >= 0; j--) {
        h2 = hs[j];
        d = xx.distance(h1.m_indi, h2.m_indi, maxs);
        dy = h2.m_minDists;

        n1: for (z = 0; z < k; z++) {
          if (dy[z] > d) {
            System.arraycopy(dy, z, dy, z + 1, k - z - 1);
            dy[z] = d;
            break n1;
          }
        }

        n2: for (z = 0; z < k; z++) {
          if (dx[z] > d) {
            System.arraycopy(dx, z, dx, z + 1, k - z - 1);
            dx[z] = d;
            break n2;
          }
        }
      }
    }

    // sort according to the distance matrix
    Arrays.sort(hs, 0, size, CMP);

    h1 = hs[(size - 1)];
    this.output(h1.m_indi);
    h1.m_done = true;
    j = (pass - 1);
    b1 = false;
    // pass on the unique individuals
    outer: while (j > 0) {
      b2 = true;
      inner: for (i = (size - 2); i >= 0; i--) {
        h2 = hs[i];
        if (h2.m_done)
          continue inner;
        if (b1
            || (!(Individual.testObjectivesEqual(h2.m_indi, h1.m_indi)))) {
          b2 = false;
          this.output(h2.m_indi);
          h2.m_done = true;
          if ((--j) <= 0)
            break outer;
          h1 = h2;
        }
      }
      b1 |= b2;
    }

    // pass on the rest and clear the matrix
    for (i = (size - 1); i >= 0; i--) {
      h1 = hs[i];
      h1.m_indi = null;
      h1.m_done = false;
    }

  }

  /**
   * the internal holder class
   *
   * @param <X>
   *          the genotype
   * @param <Y>
   *          the phenotype
   * @author Thomas Weise
   */
  private static final class Holder<X extends Serializable, Y extends Serializable> {
    /**
     * the individual
     */
    IIndividual<X, Y> m_indi;

    /**
     * the minimum distance
     */
    final double[] m_minDists;

    /**
     * the done flag
     */
    boolean m_done;

    /**
     * create a new holder
     *
     * @param k
     *          the length of the distance array
     */
    Holder(final int k) {
      super();
      this.m_minDists = new double[k];
    }
  }

  /**
   * Deserialize the linkage clustering algorithm.
   *
   * @param stream
   *          The stream to deserialize from.
   * @throws IOException
   *           if io fucks up.
   * @throws ClassNotFoundException
   *           if the class could not be found.
   */
  @SuppressWarnings("unchecked")
  private final void readObject(final ObjectInputStream stream)
      throws IOException, ClassNotFoundException {
    stream.defaultReadObject();

    Holder<G, PP>[] c;
    int i;

    i = this.getPassThroughCount();
    c = new Holder[i];
    for (--i; i >= 0; i--) {
      c[i] = new Holder<G, PP>(this.m_k);
    }
    this.m_cache = c;

  }
}
