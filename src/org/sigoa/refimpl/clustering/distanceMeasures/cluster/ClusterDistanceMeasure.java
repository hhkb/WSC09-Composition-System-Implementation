/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-12
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.clustering.distanceMeasures.cluster.ClusterDistanceMeasure.java
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

import org.sigoa.refimpl.clustering.distanceMeasures.DistanceUtils;
import org.sigoa.refimpl.go.ImplementationBase;
import org.sigoa.spec.clustering.IDistanceMeasure;
import org.sigoa.spec.clustering.IIndividualDistanceMeasure;
import org.sigoa.spec.clustering.IIndividualDistanceParameters;
import org.sigoa.spec.go.IIndividual;

/**
 * This is the base class for distance measures for clusters
 *
 * @param <G>
 *          the genotype
 * @param <PP>
 *          the phenotype
 * @author Thomas Weise
 */
public abstract class ClusterDistanceMeasure<G extends Serializable, PP extends Serializable>
    extends ImplementationBase<G, PP> implements
    IDistanceMeasure<List<IIndividual<G, PP>>>,
    IIndividualDistanceParameters<G, PP> {
  /**
   * the distance measure
   */
  private IIndividualDistanceMeasure<G, PP> m_indDist;

  /**
   * Create a new cluster distance measure
   *
   * @param distanceMeasure
   *          the distance measure to be used - if this is
   *          <code>null</code> a default distance measure will be
   *          applied
   */
  @SuppressWarnings("unchecked")
  protected ClusterDistanceMeasure(
      final IIndividualDistanceMeasure<G, PP> distanceMeasure) {
    super();

    IDistanceMeasure<?> d;

    if (distanceMeasure != null) {
      this.m_indDist = distanceMeasure;
    } else {
      d = DistanceUtils.DEFAULT_INDIVIDUAL_DM;
      this.m_indDist = ((IIndividualDistanceMeasure<G, PP>) (d));
    }
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
  public void setIndividualDistanceMeasure(
      final IIndividualDistanceMeasure<G, PP> measure) {
    if (measure == null)
      throw new NullPointerException();
    this.m_indDist = measure;
  }

  /**
   * Obtain the distance measure used for the internal computation of the
   * distance between two individuals.
   *
   * @return the distance measure used for the internal computations
   */
  public IIndividualDistanceMeasure<G, PP> getIndividualDistanceMeasure() {
    return this.m_indDist;
  }
}
