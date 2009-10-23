/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-11
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.clustering.distanceMeasures.individual.ObjectiveNorms.java
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
import org.sfc.utils.ErrorUtils;
import org.sigoa.spec.clustering.IIndividualDistanceMeasure;
import org.sigoa.spec.go.IIndividual;

/**
 * Some standard norms in objective space.
 *
 * @author Thomas Weise
 */
public final class ObjectiveNorms {

  /**
   * The euclidian distance measure
   */
  public static final IIndividualDistanceMeasure<?, ?> EUCLIDIAN_DISTANCE = new ObjectiveDistanceMeasure<Serializable, Serializable>() {
    private static final long serialVersionUID = 1;

    public final double distance(
        final IIndividual<Serializable, Serializable> ind1,
        final IIndividual<Serializable, Serializable> ind2) {
      double s, q;
      int i;

      s = 0.0d;
      for (i = (ind1.getObjectiveValueCount() - 1); i >= 0; i--) {
        q = (ind1.getObjectiveValue(i) - ind2.getObjectiveValue(i));
        q *= q;
        if (Mathematics.isNumber(q))
          s += q;
      }
      return Math.sqrt(s);
    }

    public double distance(
        final IIndividual<Serializable, Serializable> ind1,
        final IIndividual<Serializable, Serializable> ind2,
        final double[] multipliers) {
      double s, q;
      int i;

      s = 0.0d;
      for (i = (ind1.getObjectiveValueCount() - 1); i >= 0; i--) {
        q = ((ind1.getObjectiveValue(i) - ind2.getObjectiveValue(i)) * multipliers[i]);
        q *= q;
        if (Mathematics.isNumber(q))
          s += q;
      }
      return Math.sqrt(s);
    }

    private final Object readResolve() {
      return EUCLIDIAN_DISTANCE;
    }
  };

  /**
   * The manhattan distance measure
   */
  public static final IIndividualDistanceMeasure<?, ?> MANHATTAN_DISTANCE = new ObjectiveDistanceMeasure<Serializable, Serializable>() {
    private static final long serialVersionUID = 1;

    public final double distance(
        final IIndividual<Serializable, Serializable> ind1,
        final IIndividual<Serializable, Serializable> ind2) {
      double s, q;
      int i;

      s = 0.0d;
      for (i = (ind1.getObjectiveValueCount() - 1); i >= 0; i--) {
        q = Math
            .abs(ind1.getObjectiveValue(i) - ind2.getObjectiveValue(i));
        if (Mathematics.isNumber(q))
          s += q;
      }
      return s;
    }

    public double distance(
        final IIndividual<Serializable, Serializable> ind1,
        final IIndividual<Serializable, Serializable> ind2,
        final double[] multipliers) {
      double s, q;
      int i;

      s = 0.0d;
      for (i = (ind1.getObjectiveValueCount() - 1); i >= 0; i--) {
        q = (((ind1.getObjectiveValue(i) - ind2.getObjectiveValue(i)) * multipliers[i]));
        if (Mathematics.isNumber(q))
          s += q;
      }
      return Math.sqrt(s);
    }

    private final Object readResolve() {
      return MANHATTAN_DISTANCE;
    }
  };

  /**
   * The infinity norm distance
   */
  public static final IIndividualDistanceMeasure<?, ?> INFINITY_NORM_DISTANCE = new ObjectiveDistanceMeasure<Serializable, Serializable>() {
    private static final long serialVersionUID = 1;

    public final double distance(
        final IIndividual<Serializable, Serializable> ind1,
        final IIndividual<Serializable, Serializable> ind2) {
      double s, q;
      int i;

      s = 0.0d;
      for (i = (ind1.getObjectiveValueCount() - 1); i >= 0; i--) {
        q = Math
            .abs(ind1.getObjectiveValue(i) - ind2.getObjectiveValue(i));
        if (Mathematics.isNumber(q) && (q > s))
          s = q;
      }
      return s;
    }

    public double distance(
        final IIndividual<Serializable, Serializable> ind1,
        final IIndividual<Serializable, Serializable> ind2,
        final double[] multipliers) {
      double s, q;
      int i;

      s = 0.0d;
      for (i = (ind1.getObjectiveValueCount() - 1); i >= 0; i--) {
        q = (Math.abs(ind1.getObjectiveValue(i)
            - ind2.getObjectiveValue(i)) * multipliers[i]);
        if (Mathematics.isNumber(q) && (q > s))
          s = q;
      }
      return Math.sqrt(s);
    }

    private final Object readResolve() {
      return INFINITY_NORM_DISTANCE;
    }
  };

  /**
   * the hidden and forbidden constructor
   */
  private ObjectiveNorms() {
    ErrorUtils.doNotCall();
  }

}
