/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-12
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.clustering.distanceMeasures.cluster.ClusterMaxDistance.java
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

import org.sigoa.spec.clustering.IIndividualDistanceMeasure;
import org.sigoa.spec.go.IIndividual;

/**
 * This distance measure computes the maximum distance between two
 * clusters.
 *
 * @param <G>
 *          the genotype
 * @param <PP>
 *          the phenotype
 * @author Thomas Weise
 */
public class ClusterMaxDistance<G extends Serializable, PP extends Serializable>
    extends ClusterDistanceMeasure<G, PP> {
  /**
   * the serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new maximum cluster distance measure.
   */
  public ClusterMaxDistance() {
    this(null);
  }

  /**
   * Create a new maximum cluster distance measure.
   *
   * @param distanceMeasure
   *          the distance measure to be used - if this is
   *          <code>null</code> a default distance measure will be
   *          applied
   */
  public ClusterMaxDistance(
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
  public double distance(final List<IIndividual<G, PP>> o1, final List<IIndividual<G, PP>> o2) {
    int i, j, s2;
    double m, d;
    IIndividual<G, PP> x;
    final IIndividualDistanceMeasure<G, PP> z;

    m = Double.NEGATIVE_INFINITY;
    s2 = (o2.size() - 1);
    z = this.getIndividualDistanceMeasure();

    for (i = (o1.size() - 1); i >= 0; i--) {
      x = o1.get(i);
      for (j = s2; j >= 0; j--) {
        d = z.distance(x, o2.get(j));
        if (d > m)
          m = d;
      }
    }

    return m;
  }
}
