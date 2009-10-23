/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-11
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.clustering.distanceMeasures.individual.ObjectivePNorm.java
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

package org.sigoa.refimpl.clustering.distanceMeasures.individual;

import java.io.Serializable;

import org.sfc.math.Mathematics;
import org.sigoa.spec.go.IIndividual;

/**
 * the p-norm distance measure for the objective space.
 *
 * @param <G>
 *          the genotype
 * @param <PP>
 *          the phenotype
 * @author Thomas Weise
 */
public class ObjectivePNorm<G extends Serializable, PP extends Serializable>
    extends ObjectiveDistanceMeasure<G, PP> {
  /**
   * the serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the p-value
   */
  private final double m_p;

  /**
   * Create a new euclidian norm.
   */
  public ObjectivePNorm() {
    this(2.0d);
  }

  /**
   * create a new p norm with the specified <code>p</code>-value
   *
   * @param p
   *          the p value
   */
  public ObjectivePNorm(final double p) {
    super();
    this.m_p = p;
  }

  /**
   * Compute the distance between two individuals.
   *
   * @param ind1
   *          the first individual
   * @param ind2
   *          the second individual
   * @return the distance between <code>ind1</code> and <code>ind2</code>
   */
  @SuppressWarnings("unchecked")
  public double distance(final IIndividual<G, PP> ind1,
      final IIndividual<G, PP> ind2) {
    double sum, q, p;
    int i;

    p = this.m_p;
    if (Double.isInfinite(p))
      return ObjectiveNorms.INFINITY_NORM_DISTANCE.distance(
          ((IIndividual) ind1), ((IIndividual) ind2));
    if (p == 1.0d)
      return ObjectiveNorms.MANHATTAN_DISTANCE.distance(
          ((IIndividual) ind1), ((IIndividual) ind2));
    if (p == 2.0d)
      return ObjectiveNorms.EUCLIDIAN_DISTANCE.distance(
          ((IIndividual) ind1), ((IIndividual) ind2));

    sum = 0.0d;

    for (i = (ind1.getObjectiveValueCount() - 1); i >= 0; i--) {
      q = Math.pow(ind1.getObjectiveValue(i) - ind2.getObjectiveValue(i),
          p);
      if (Mathematics.isNumber(q))
        sum += q;
    }
    return Math.pow(sum, 1.0d / p);
  }

  /**
   * Compute the distance between two individuals.
   *
   * @param ind1
   *          the first individual
   * @param ind2
   *          the second individual
   * @param multipliers
   *          the multipliers for normalization
   * @return the <i>normalized</i> distance between <code>o1</code> and
   *         <code>o2</code>
   * @throws NullPointerException
   *           if <code>o1 == null || o2 == null  || divisors==null</code>
   */
  @SuppressWarnings("unchecked")
  public double distance(final IIndividual<G, PP> ind1,
      final IIndividual<G, PP> ind2, final double[] multipliers) {
    double sum, q, p;
    int i;

    p = this.m_p;
    if (Double.isInfinite(p))
      return ObjectiveNorms.INFINITY_NORM_DISTANCE.distance(
          ((IIndividual) ind1), ((IIndividual) ind2), multipliers);
    if (p == 1.0d)
      return ObjectiveNorms.MANHATTAN_DISTANCE.distance(
          ((IIndividual) ind1), ((IIndividual) ind2), multipliers);
    if (p == 2.0d)
      return ObjectiveNorms.EUCLIDIAN_DISTANCE.distance(
          ((IIndividual) ind1), ((IIndividual) ind2), multipliers);

    sum = 0.0d;

    for (i = (ind1.getObjectiveValueCount() - 1); i >= 0; i--) {
      q = Math
          .pow(
              ((ind1.getObjectiveValue(i) - ind2.getObjectiveValue(i)) * multipliers[i]),
              p);
      if (Mathematics.isNumber(q))
        sum += q;
    }
    return Math.pow(sum, 1.0d / p);
  }
}
