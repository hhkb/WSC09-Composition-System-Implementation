/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-12
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.clustering.IIndividualDistanceMeasure.java
 * Last modification: 2007-07-03
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
 * This distance measure is to be applied to individual records.
 *
 * @param <G>
 *          The genotype.
 * @param <PP>
 *          The phenotype.
 * @author Thomas Weise
 */
public interface IIndividualDistanceMeasure<G extends Serializable, PP extends Serializable>
    extends IDistanceMeasure<IIndividual<G, PP>> {

  /**
   * Compute the distance between two individuals.
   *
   * @param o1
   *          the first individual
   * @param o2
   *          the second individual
   * @param multipliers
   *          the multipliers for normalization
   * @return the <i>normalized</i> distance between <code>o1</code> and
   *         <code>o2</code>
   * @throws NullPointerException
   *           if <code>o1 == null || o2 == null  || divisors==null</code>
   */
  public abstract double distance(final IIndividual<G, PP> o1,
      final IIndividual<G, PP> o2, final double[] multipliers);

  /**
   * Obtain the nucleus element of a cluster - the element closest to the
   * cluster's center.
   *
   * @param cluster
   *          the cluster
   * @return the nuclues
   * @throws IllegalArgumentException
   *           if <code>cluster.size()&lt;=0</code>
   * @throws NullPointerException
   *           if <code>cluster==null</code>
   */
  public abstract IIndividual<G, PP> getNucleus(
      final List<IIndividual<G, PP>> cluster);
}
