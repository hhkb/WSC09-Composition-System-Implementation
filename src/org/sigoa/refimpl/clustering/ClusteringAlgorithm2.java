/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-12
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.clustering.ClusteringAlgorithm2.java
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

package org.sigoa.refimpl.clustering;

import java.io.Serializable;
import java.util.List;

import org.sigoa.refimpl.clustering.distanceMeasures.DistanceUtils;
import org.sigoa.spec.clustering.IClusterDistanceParameters;
import org.sigoa.spec.clustering.IDistanceMeasure;
import org.sigoa.spec.clustering.IIndividualDistanceMeasure;
import org.sigoa.spec.clustering.IIndividualDistanceParameters;
import org.sigoa.spec.go.IIndividual;

/**
 * An extension of the <code>ClusteringAlgorithm</code>-class which also
 * implements the <code>IClusterDistanceParameters</code>-interface.
 *
 * @param <G>
 *          the genotype
 * @param <PP>
 *          the phenotype
 * @author Thomas Weise
 */
public abstract class ClusteringAlgorithm2<G extends Serializable, PP extends Serializable>
    extends ClusteringAlgorithm<G, PP> implements
    IClusterDistanceParameters<G, PP> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the distance measure
   */
  private IDistanceMeasure<List<IIndividual<G, PP>>> m_clsDist;

  /**
   * Create a new clustering algorithm instance.
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
  protected ClusteringAlgorithm2(final int count,
      final IIndividualDistanceMeasure<G, PP> distanceMeasure,
      final IDistanceMeasure<List<IIndividual<G, PP>>> clusterDistance) {
    super(count, distanceMeasure);

    this.m_clsDist = ((clusterDistance != null) ? clusterDistance
        : DistanceUtils.createDefaultClusterDM(this
            .getIndividualDistanceMeasure()));
  }

  /**
   * Set the distance measure function to be used in order to the distance
   * between two clusters of individuals.
   *
   * @param measure
   *          the distance measure function to be used
   * @throws NullPointerException
   *           if <code>measure==null</code>
   */
  public void setClusterDistanceMeasure(
      final IDistanceMeasure<List<IIndividual<G, PP>>> measure) {
    if (measure == null)
      throw new NullPointerException();
    this.m_clsDist = measure;
  }

  /**
   * Obtain the distance measure used for the internal computation of the
   * distance between two clusters of individuals.
   *
   * @return the distance measure used for the internal computations
   */
  public IDistanceMeasure<List<IIndividual<G, PP>>> getClusterDistanceMeasure() {
    return this.m_clsDist;
  }

  /**
   * Set the distance measure function to be used to determine the distance
   * between two individuals.
   *
   * @param measure
   *          the distance measure function to be used
   * @throws NullPointerException
   *           if <code>measure==null</code>
   */
  @Override
  @SuppressWarnings("unchecked")
  public void setIndividualDistanceMeasure(
      final IIndividualDistanceMeasure<G, PP> measure) {
    IIndividualDistanceParameters<G, PP> x;
    if (this.m_clsDist instanceof IIndividualDistanceParameters) {
      x = ((IIndividualDistanceParameters<G, PP>) (this.m_clsDist));
      if (x.getIndividualDistanceMeasure() == this.m_indDist) {
        x.setIndividualDistanceMeasure(measure);
      }
    }

    super.setIndividualDistanceMeasure(measure);
  }
}
