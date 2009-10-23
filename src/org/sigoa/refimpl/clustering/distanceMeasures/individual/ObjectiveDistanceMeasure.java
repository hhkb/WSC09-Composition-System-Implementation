/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-12
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.clustering.distanceMeasures.individual.ObjectiveDistanceMeasure.java
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

package org.sigoa.refimpl.clustering.distanceMeasures.individual;

import java.io.Serializable;
import java.util.List;

import org.sigoa.refimpl.clustering.ObjectiveCluster;
import org.sigoa.refimpl.go.ImplementationBase;
import org.sigoa.refimpl.go.Individual;
import org.sigoa.spec.clustering.IIndividualDistanceMeasure;
import org.sigoa.spec.go.IIndividual;

/**
 * This is the base class for all distance measures in objective space.
 *
 * @param <G>
 *          The genotype.
 * @param <PP>
 *          The phenotype.
 * @author Thomas Weise
 */
public abstract class ObjectiveDistanceMeasure<G extends Serializable, PP extends Serializable>
    extends ImplementationBase<G, PP> implements
    IIndividualDistanceMeasure<G, PP> {

  /**
   * the protected constructor
   */
  protected ObjectiveDistanceMeasure() {
    super();
  }

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
  public IIndividual<G, PP> getNucleus(
      final List<IIndividual<G, PP>> cluster) {
    double[] c;
    int s, i, j, dc;
    IIndividual<G, PP> in, in2;
    double md, d2;
    IIndividual<G, PP> inx;

    s = cluster.size();
    if (s <= 0)
      throw new IllegalArgumentException();

    in = cluster.get(0);
    if (cluster instanceof ObjectiveCluster) {
      inx = ((ObjectiveCluster<G, PP>) cluster).getCenterIndividual();
    } else {
      dc = in.getObjectiveValueCount();
      c = new double[dc];
      dc--;
      for (i = (s - 1); i >= 0; i--) {
        in2 = cluster.get(i);
        for (j = dc; j >= 0; j--) {
          c[j] += in2.getObjectiveValue(j);
        }
      }

      inx = new Individual<G, PP>(dc + 1);// this uses not the individual
      // factory..
      for (j = dc; j >= 0; j--) {
        inx.setObjectiveValue(j, c[j] / s);
      }
    }

    md = this.distance(in, inx);
    for (--s; s > 0; s--) {
      in2 = cluster.get(s);
      d2 = this.distance(in2, inx);
      if (d2 < md) {
        md = d2;
        in = in2;
      }
    }
    return in;
  }
}
