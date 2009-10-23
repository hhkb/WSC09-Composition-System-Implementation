/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-27
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.objectives.ObjectiveUtils.java
 * Last modification: 2006-11-27
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

package org.sigoa.refimpl.go.objectives;

import org.sfc.utils.ErrorUtils;
import org.sigoa.spec.go.OptimizationUtils;
import org.sigoa.spec.go.objectives.IObjectiveValueComputer;

/**
 * Some utilities that may be used by objective functions.
 * 
 * @author Thomas Weise
 */
public final class ObjectiveUtils {

  /**
   * An objective value computer which returns the worst objective value.
   */
  public static final IObjectiveValueComputer WORST_OVC = new IObjectiveValueComputer() {

    private static final long serialVersionUID = 1;

    private final Object readResolve() {
      return WORST_OVC;
    }

    @Override
    public boolean equals(final Object o) {
      return ((o == this) || ((o != null) && (o.getClass() == this
          .getClass())));
    }

    public double computeObjectiveValue(final double[] results) {
      int i;
      double d, e;

      i = results.length;
      if (i <= 0)
        return OptimizationUtils.WORST;

      d = results[0];
      for (--i; i > 0; i--) {
        e = results[i];
        if (Double.isNaN(e))
          return OptimizationUtils.WORST;
        if (e > d)
          d = e;
      }

      return ((Double.isNaN(d)) ? OptimizationUtils.WORST : d);
    }
  };

  /**
   * An objective value computer which returns the average of the worst two
   * objective value.
   */
  public static final IObjectiveValueComputer WORST2_OVC = new IObjectiveValueComputer() {

    private static final long serialVersionUID = 1;

    private final Object readResolve() {
      return WORST2_OVC;
    }

    @Override
    public boolean equals(final Object o) {
      return ((o == this) || ((o != null) && (o.getClass() == this
          .getClass())));
    }

    public double computeObjectiveValue(final double[] results) {
      int i;
      double d1, d2, e;

      i = results.length;
      if (i <= 0)
        return OptimizationUtils.WORST;

      d1 = d2 = results[0];
      for (--i; i > 0; i--) {
        e = results[i];
        if (Double.isNaN(e))
          return OptimizationUtils.WORST;
        if (e > d1) {
          d2 = d1;
          d1 = e;
        } else if (e > d2) {
          d2 = e;
        }
      }

      if (d1 != d2)
        d1 = ((0.5d * d1) + (0.5d * d2));

      return ((Double.isNaN(d1)) ? OptimizationUtils.WORST : d1);
    }
  };

  /**
   * An objective value computer which returns the best objective value.
   */
  public static final IObjectiveValueComputer BEST_OVC = new IObjectiveValueComputer() {

    private static final long serialVersionUID = 1;

    private final Object readResolve() {
      return BEST_OVC;
    }

    @Override
    public boolean equals(final Object o) {
      return ((o == this) || ((o != null) && (o.getClass() == this
          .getClass())));
    }

    public final double computeObjectiveValue(final double[] results) {
      int i;
      double d, e;

      i = results.length;
      if (i <= 0)
        return OptimizationUtils.WORST;

      d = results[0];
      for (--i; i > 0; i--) {
        e = results[i];
        if (Double.isNaN(e))
          return OptimizationUtils.WORST;
        if (e < d)
          d = e;
      }

      return ((Double.isNaN(d)) ? OptimizationUtils.WORST : d);
    }
  };

  /**
   * An objective value computer which returns the average objective value.
   */
  public static final IObjectiveValueComputer AVG_OVC = new IObjectiveValueComputer() {

    private static final long serialVersionUID = 1;

    private final Object readResolve() {
      return AVG_OVC;
    }

    @Override
    public boolean equals(final Object o) {
      return ((o == this) || ((o != null) && (o.getClass() == this
          .getClass())));
    }

    public double computeObjectiveValue(final double[] results) {
      int i, j;
      double d;

      j = results.length;
      if (j <= 0)
        return OptimizationUtils.WORST;

      d = 0.0d;
      for (i = (j - 1); i >= 0; i--) {
        d += results[i];
      }

      return ((Double.isNaN(d)) ? OptimizationUtils.WORST : (d / j));
    }
  };

  /**
   * This internal constructor cannot be invoked.
   */
  private ObjectiveUtils() {
    ErrorUtils.doNotCall();
  }

  /**
   * Truncate a value to the specified precision. The return value will be
   * the input value but truncated to precision powers of e.
   * 
   * @param value
   *          the value
   * @param precision
   *          a precision index which denotes powers of e
   * @return the truncated value.
   */
  public static final double truncate(final double value,
      final double precision) {
    double f, v;
    boolean b;

    if (precision <= 0.0d)
      return value;

    if (value == 0.0d)
      return 0.0d;

    if (value < 0.0d) {
      v = -value;
      b = true;
    } else {
      v = value;
      b = false;
    }

    f = Math.exp(precision - Math.rint(Math.log(v)));
    v = ((Math.rint(v * f)) / f);
    return (b ? (-v) : v);
  }

  /**
   * Weight an objective value in order to make it worse.
   * 
   * @param ov
   *          the objective value
   * @param w
   *          the weight - a positive real number
   * @return the worsified objective value
   */
  public static final double worsifyObjectiveValue(final double ov,
      final double w) {
    if (ov == 0)
      return 1d;
    if (ov > 0)
      return (ov * w);
    return (ov / w);
  }
}
