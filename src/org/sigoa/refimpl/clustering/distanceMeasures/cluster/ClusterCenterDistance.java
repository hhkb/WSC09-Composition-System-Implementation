/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-12
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.clustering.distanceMeasures.cluster.ClusterCenterDistance.java
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

package org.sigoa.refimpl.clustering.distanceMeasures.cluster;

import java.io.Serializable;
import java.util.List;

import org.sigoa.refimpl.clustering.ObjectiveCluster;
import org.sigoa.spec.clustering.IIndividualDistanceMeasure;
import org.sigoa.spec.go.IIndividual;

/**
 * This distance measure computes the distance between the cluster centers.
 * Warning: this distance measure strictly bases on the objective space and
 * therefore needs an objective-value based internal distance measure for
 * individuals.
 *
 * @param <G>
 *          the genotype
 * @param <PP>
 *          the phenotype
 * @author Thomas Weise
 */
public class ClusterCenterDistance<G extends Serializable, PP extends Serializable>
    extends ClusterDistanceMeasure<G, PP> {
  /**
   * the serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new maximum cluster distance measure.
   */
  public ClusterCenterDistance() {
    this(null);
  }

  /**
   * Create a new cluster center distance measure.
   *
   * @param distanceMeasure
   *          the distance measure to be used - if this is
   *          <code>null</code> a default distance measure will be
   *          applied
   */
  public ClusterCenterDistance(
      final IIndividualDistanceMeasure<G, PP> distanceMeasure) {
    super(distanceMeasure);
  }

  /**
   * Compute the distance between two clusters.
   *
   * @param o1
   *          the first cluster
   * @param o2
   *          the second cluster
   * @return the distance between <code>o1</code> and <code>o2</code>
   */
  public double distance(final List<IIndividual<G, PP>> o1,
      final List<IIndividual<G, PP>> o2) {
    IIndividualDistanceMeasure<G, PP> in;
    double s, d1, d2;
    int l1, l2, i, j;

    in = this.getIndividualDistanceMeasure();

    if ((o1 instanceof ObjectiveCluster)
        && (o2 instanceof ObjectiveCluster)) {
      return in.distance(((ObjectiveCluster<G, PP>) o1)
          .getCenterIndividual(), ((ObjectiveCluster<G, PP>) o2)
          .getCenterIndividual());
    }

    l1 = o1.size();
    if (l1 <= 0)
      return 0;
    l2 = o2.size();
    if (l2 <= 0)
      return 0;

    s = 0.0;
    for (i = this.getObjectiveValueCount(); i >= 0; i--) {
      d1 = 0.0d;
      for (j = (l1 - 1); j >= 0; j--)
        d1 += o1.get(j).getObjectiveValue(i);

      d2 = 0.0d;
      for (j = (l2 - 1); j >= 0; j--)
        d2 += o2.get(j).getObjectiveValue(i);

      d1 = ((d1 / l1) + (d2 / l2));
      s += (d1 * d1);
    }

    return Math.sqrt(s);
  }
}
