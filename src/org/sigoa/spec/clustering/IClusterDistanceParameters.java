/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-11
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.clustering.IClusterDistanceParameters.java
 * Last modification: 2006-12-11
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

package org.sigoa.spec.clustering;

import java.io.Serializable;
import java.util.List;

import org.sigoa.spec.go.IIndividual;

/**
 * This parameters are common to all modules that use distance measures in
 * order to determine the distance between two clusters of individuals.
 *
 * @param <G>
 *          the genotype
 * @param <PP>
 *          the phenotype
 * @author Thomas Weise
 */
public interface IClusterDistanceParameters<G extends Serializable, PP extends Serializable>
    extends Serializable {

  /**
   * Set the distance measure function to be used in order to the distance
   * between two clusters of individuals.
   *
   * @param measure
   *          the distance measure function to be used
   * @throws NullPointerException
   *           if <code>measure==null</code>
   */
  public abstract void setClusterDistanceMeasure(
      final IDistanceMeasure<List<IIndividual<G, PP>>> measure);

  /**
   * Obtain the distance measure used for the internal computation of the
   * distance between two clusters of individuals.
   *
   * @return the distance measure used for the internal computations
   */
  public abstract IDistanceMeasure<List<IIndividual<G, PP>>> getClusterDistanceMeasure();

}
