/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-12
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.clustering.distanceMeasures.DistanceUtils.java
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

package org.sigoa.refimpl.clustering.distanceMeasures;

import java.io.Serializable;
import java.util.List;

import org.sfc.utils.ErrorUtils;
import org.sigoa.refimpl.clustering.distanceMeasures.cluster.ClusterCenterDistance;
import org.sigoa.refimpl.clustering.distanceMeasures.individual.ObjectiveNorms;
import org.sigoa.spec.clustering.IDistanceMeasure;
import org.sigoa.spec.clustering.IIndividualDistanceMeasure;
import org.sigoa.spec.go.IIndividual;

/**
 * This class provides utilities for distance measures.
 *
 * @author Thomas Weise
 */
public final class DistanceUtils {

  /**
   * default distance measure
   */
  public static final IIndividualDistanceMeasure<?, ?> DEFAULT_INDIVIDUAL_DM = ObjectiveNorms.EUCLIDIAN_DISTANCE;

  /**
   * the forbidden constructor
   */
  private DistanceUtils() {
    ErrorUtils.doNotCall();
  }

  /**
   * create the default cluster distance measure
   *
   * @param idm
   *          the individual distance measure to be used
   * @param <G>
   *          the genotype
   * @param <PP>
   *          the phenotype
   * @return the cluster distance measure
   */
  public static final <G extends Serializable, PP extends Serializable> IDistanceMeasure<List<IIndividual<G, PP>>> createDefaultClusterDM(
      final IIndividualDistanceMeasure<G, PP> idm) {
    // return new ClusterMaxDistance<G, PP>(idm);
    return new ClusterCenterDistance<G, PP>(idm);
  }

}
