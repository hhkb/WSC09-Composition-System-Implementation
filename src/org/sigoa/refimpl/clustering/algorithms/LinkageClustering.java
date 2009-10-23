/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-12
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.clustering.algorithms.LinkageClustering.java
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

package org.sigoa.refimpl.clustering.algorithms;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.List;

import org.sigoa.refimpl.clustering.ClusteringAlgorithm2;
import org.sigoa.refimpl.clustering.ObjectiveCluster;
import org.sigoa.spec.clustering.IDistanceMeasure;
import org.sigoa.spec.clustering.IIndividualDistanceMeasure;
import org.sigoa.spec.go.IIndividual;

/**
 * The linkage clustering algorithm
 *
 * @param <G>
 *          the genotype
 * @param <PP>
 *          the phenotype
 * @author Thomas Weise
 */
public class LinkageClustering<G extends Serializable, PP extends Serializable>
    extends ClusteringAlgorithm2<G, PP> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the internal cluster list.
   */
  private transient List<IIndividual<G, PP>>[] m_clusters;

  /**
   * the internal cache.
   */
  private transient List<IIndividual<G, PP>>[] m_cache;

  /**
   * Create a new pass through algorithm instance.
   *
   * @param count
   *          The initial selection size.
   * @param distanceMeasure
   *          the distance measure to be used for individual distance
   *          measuring - if this is <code>null</code> a default distance
   *          measure will be applied
   * @param clusterDistance
   *          the cluster distance distance measure for cluster distances
   * @throws IllegalArgumentException
   *           if <code>count&lt;=0</code>.
   */
  @SuppressWarnings("unchecked")
  public LinkageClustering(final int count,
      final IIndividualDistanceMeasure<G, PP> distanceMeasure,
      final IDistanceMeasure<List<IIndividual<G, PP>>> clusterDistance) {
    super(count, distanceMeasure, clusterDistance);

    List<IIndividual<G, PP>>[] x;
    int i;

    x = new List[count];
    this.m_clusters = new List[count];
    this.m_cache = new List[0];

    for (i = (count - 1); i >= 0; i--) {
      x[i] = new ObjectiveCluster<G, PP>();
    }
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
    List<IIndividual<G, PP>>[] clusters, cache;
    List<IIndividual<G, PP>> c1, c2;
    int i, j, k, im1, im2;
    double dist, d2;
    final IDistanceMeasure<List<IIndividual<G, PP>>> dm;
    final IIndividualDistanceMeasure<G, PP> dm2;

    cache = this.m_cache;

    j = cache.length;
    if (size > j) {
      k = ((size * 3) >>> 1);
      clusters = new List[k];
      System.arraycopy(cache, 0, clusters, 0, j);
      cache = clusters;
      for (; j < k; j++) {
        cache[j] = new ObjectiveCluster<G, PP>();
      }
      clusters = new List[k];
    } else
      clusters = this.m_clusters;

    c1 = null;// has no effect
    for (i = (size - 1); i >= 0; i--) {
      c1 = cache[i];
      c1.add(source[i]);
      clusters[i] = c1;
    }

    i = (size - 1);
    dm = this.getClusterDistanceMeasure();
    while (i >= pass) {
      dist = Double.POSITIVE_INFINITY;
      im1 = -1;
      im2 = -1;

      for (j = i; j > 0; j--) {
        c1 = clusters[j];
        for (k = (j - 1); k >= 0; k--) {
          d2 = dm.distance(c1, clusters[k]);
          if (d2 < dist) {
            dist = d2;
            im1 = j;
            im2 = k;
          }
        }
      }

      if (im1 < 0)
        im1 = 1;
      if (im2 < 0)
        im2 = 0;

      c1 = clusters[im1];
      c2 = clusters[im2];
      c2.addAll(c1);
      c1.clear();
      clusters[im1] = clusters[i];
      i--;
    }

    dm2 = this.getIndividualDistanceMeasure();
    for (; i >= 0; i--) {
      c1 = clusters[i];
      this.output((c1.size() == 1) ? c1.get(0) : dm2.getNucleus(c1));
      c1.clear();
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
    int i;
    List<IIndividual<G, PP>>[] x;

    i = this.getPassThroughCount();

    x = new List[i];
    this.m_clusters = new List[i];
    this.m_cache = new List[0];

    for (--i; i >= 0; i--) {
      x[i] = new ObjectiveCluster<G, PP>();
    }
  }
}
