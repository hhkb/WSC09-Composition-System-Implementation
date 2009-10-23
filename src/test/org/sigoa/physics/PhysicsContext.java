/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-05-10
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.physics.PhysicsContext.java
 * Last modification: 2008-05-10
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

package test.org.sigoa.physics;

import java.io.Serializable;
import java.util.Arrays;

import org.dgpf.symbolicRegression.ComputationContext;
import org.dgpf.symbolicRegression.scalar.real.RealExpression;
import org.sfc.math.Mathematics;
import org.sfc.math.Physics;
import org.sigoa.refimpl.stoch.Randomizer;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * The context for physical function evaluation
 * 
 * @author Thomas Weise
 */
public class PhysicsContext extends ComputationContext<RealExpression> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the interesting physical constants we want to find
   */
  private static final double[] CONSTANTS = new double[] {
      Physics.SPEED_OF_LIGHT,//
      Physics.PLANCK,//
      Physics.ELEMENTARY_CHARGE,//
      Physics.NEWTON_GRAVITATION,//
      Physics.ELECTRON_MASS,//
      Physics.PROTON_MASS,//
  // Physics.GAS_CONSTANT,//
  // Physics.AVOGADRO,//
  };

  /**
   * the objectives
   */
  static final MinimizeDevObjective[] OBJECTIVES = new MinimizeDevObjective[] {
      new MinimizeDevObjective(0, "speed_of_light"), //$NON-NLS-1$
      new MinimizeDevObjective(1, "planck"), //$NON-NLS-1$
      new MinimizeDevObjective(2, "elementary_charge"), //$NON-NLS-1$
      new MinimizeDevObjective(3, "gravitation"), //$NON-NLS-1$
      new MinimizeDevObjective(4, "electron_mass"), //$NON-NLS-1$
      new MinimizeDevObjective(5, "proton_mass"), //$NON-NLS-1$    
  };

  /**
   * the number of steps
   */
  public static final int CNT = CONSTANTS.length;

  /**
   * the combinations
   */
  private static final int[][] COMBOS;

  /**
   * the combos
   */
  static {
    int i0, i1, i2, i3, i4, i5, /* i6, i7, */l;

    l = Mathematics.factorial(CNT);
    COMBOS = new int[l][];

    l = 0;
    for (i0 = 0; i0 < CNT; i0++) {
      for (i1 = 0; i1 < CNT; i1++) {
        if (i1 == i0)
          continue;
        for (i2 = 0; i2 < CNT; i2++) {
          if ((i2 == i0) || (i2 == i1))
            continue;
          for (i3 = 0; i3 < CNT; i3++) {
            if ((i3 == i0) || (i3 == i1) || (i3 == i2))
              continue;
            for (i4 = 0; i4 < CNT; i4++) {
              if ((i4 == i0) || (i4 == i1) || (i4 == i2) || (i4 == i3))
                continue;
              for (i5 = 0; i5 < CNT; i5++) {
                if ((i5 == i0) || (i5 == i1) || (i5 == i2) || (i5 == i3)
                    || (i5 == i4))
                  continue;
                /*
                 * for (i6 = 0; i6 < CNT; i6++) { if ((i6 == i0) || (i6 ==
                 * i1) || (i6 == i2) || (i6 == i3) || (i6 == i4) || (i6 ==
                 * i5)) continue; for (i7 = 0; i7 < CNT; i7++) { if ((i7 ==
                 * i0) || (i7 == i1) || (i7 == i2) || (i7 == i3) || (i7 ==
                 * i4) || (i7 == i5) || (i7 == i6)) continue;
                 */
                COMBOS[l++] = new int[] { /* i7, i6, */i5, i4, i3, i2,
                    i1, i0 };
                /*
                 * } }
                 */
              }
            }
          }
        }
      }
    }

  }

  /**
   * the results
   */
  private final double[][] m_results;

  // /**
  // * the number of solutions
  // */
  // int m_n;

  /**
   * the negligible
   */
  private int m_neg;

  // /**
  // * the result
  // */
  // double m_res;
  //
  // /**
  // * the total
  // */
  // double m_total;

  /**
   * the result
   */
  final double[] m_result;

  /**
   * the buffer
   */
  private final double[] m_buf;

  /**
   * the internal randomizer
   */
  private final IRandomizer m_random;

  /**
   * create a new physics context
   */
  public PhysicsContext() {
    super();
    this.m_results = new double[CNT][CNT];
    this.m_result = new double[CNT];
    this.m_buf = new double[CNT];
    this.m_random = new Randomizer();
  }

  // /**
  // * This method is called right before the simulation begins.
  // *
  // * @throws IllegalStateException
  // * if this simulation is already running or the
  // * {@link #beginIndividual(Serializable)} method has not yet
  // * been called or {@link #endIndividual()} has already been
  // * called.
  // */
  // @Override
  // public void beginSimulation() {
  // int i, j, k, v, cv;
  // final double[][] res;
  // final RealExpressionBase f;
  // double r, x, q, total, tc, s, ss;
  // double[] rs;
  // int[] curC;
  //
  // super.beginSimulation();
  //
  // this.m_res = Double.POSITIVE_INFINITY;
  // this.m_total = Double.POSITIVE_INFINITY;
  // this.m_n = 0;
  //
  // f = this.getSimulated();
  // if (f.getWeight() > 100) {
  // this.m_neg = Integer.MAX_VALUE;
  // return;
  // }
  //
  // this.m_neg = 0;
  // res = this.m_results;
  //
  // k = 0;
  // for (i = (MAX - 1); i >= 0; i--) {
  // this.clear();
  // r = f.compute(i, this);
  // if ((this.getErrors() <= 0) && Mathematics.isNumber(r)) {
  //
  // this.m_neg += super.getNegligible();
  //
  // rs = res[k++];
  // for (j = (CNT - 1); j >= 0; j--) {
  // x = Math.abs(r - CONSTANTS[j]) / CONSTANTS[j];
  // rs[j] = (Mathematics.isNumber(x) ? x : Double.POSITIVE_INFINITY);
  // }
  // }
  // }
  //
  // v = 0;
  // total = ss = r = Double.POSITIVE_INFINITY;
  // if (k > 0) {
  // for (i = (((COMBOS.length) / (Mathematics.factorial(CNT - k))) - 1); i
  // >= 0; i--) {
  // curC = COMBOS[i];
  //
  // x = 0d;
  // cv = 0;
  // tc = 0d;
  // s = 0d;
  // /* inner: */for (j = (k - 1); j >= 0; j--) {
  // q = res[j][curC[j]];
  // tc += Math.min(20d, Math.rint(q * 10d));
  // s += q;
  // if (q < 0.1d) {
  // x += q;
  // cv++;
  // // //allow at most four
  // // if (cv >= 4)
  // // break inner;
  // }
  // }
  //
  // if ((cv > v) || ((cv == v) && (cv > 0) && (x < r))
  // || ((cv == v) && (x <= r) && (tc < total))) {
  // r = x;
  // total = tc;
  // v = cv;
  // ss = s;
  // }
  // }
  //
  // this.m_n = v;
  // this.m_res = ((v > 0) ? (r / v) : ss);
  // this.m_total = total;
  // }
  // }

  /**
   * This method is called right before the simulation begins.
   * 
   * @throws IllegalStateException
   *           if this simulation is already running or the
   *           {@link #beginIndividual(Serializable)} method has not yet
   *           been called or {@link #endIndividual()} has already been
   *           called.
   */
  @Override
  public void beginSimulation() {
    int i, j, z, n, v;
    final int l;
    final double[][] res;
    final RealExpression f;
    double r, x;
    double[] rs;
    int[] curC;
    final double[] result, buf;

    result = this.m_result;
    this.m_neg = Integer.MAX_VALUE;
    Arrays.fill(result, Double.POSITIVE_INFINITY);

    super.beginSimulation();

    f = this.getSimulated();

    // i = f.getWeight();
    // if ((i < 10) || (i > 100))
    // return;

    res = this.m_results;

    n = 0;
    for (i = (CNT - 1); i >= 0; i--) {
      this.clear();
      r = f.compute(i, this);
      if (this.getErrors() > 0)
        return;
      if (r == 0d)
        return;

      n += super.getNegligible();

      rs = res[i];
      for (j = (CNT - 1); j >= 0; j--) {
        x = Math.abs(r - CONSTANTS[j]) / CONSTANTS[j];
        rs[j] = ((Mathematics.isNumber(x)) ? transform(x) : MAX);
      }

    }

    buf = this.m_buf;
    this.m_neg = n;

    l = COMBOS.length;
    v = this.m_random.nextInt(l);
    main: for (i=(l-1); i >= 0; i--) {
      curC = COMBOS[(i + v)%l];

      for (j = (CNT - 1); j >= 0; j--) {
        z = curC[j];
        if ((buf[z] = res[j][z]) > result[z])
          continue main;
      }

      System.arraycopy(buf, 0, result, 0, CNT);
    }
  }

  /**
   * the values
   */
  private static final double[] VALS = new double[] {//
  1e-5d,//
      3e-5d,//
      9e-5d,//
      2.7e-4d,//
      8.1e-4d,//
      2.43e-3d,//
      7.29e-3d,//
      2.187e-2d,//
      6.561e-2d,//
      1.9683e-1d,//
      5.9049e-1d,//
      1.77147d,//
      5.31441d,//
      15.94323d, };

  /**
   * the values
   */
  static final double[] VALS2 = {//
  1e-5d,//
      2e-5d,//
      3e-5d,//
      4e-5,//
      5e-5,//
      6e-5,//
      7e-5,//
      8e-5,//
      9e-5,//
      1e-4,//
      1.1e-4,//
      1.2e-4,//
      1.3e-4,//
      1.4e-4,//
      1.5e-4,//
  };

  /**
   * the maximum
   */
  private static final double MAX = VALS2[VALS2.length - 1];


  /**
   * the transformation
   * 
   * @param res
   *          the result
   * @return its transformed version
   */
  private static final double transform(final double res) {
    int i;

    for (i = (VALS.length - 1); i >= 0; i--) {
      if (res > VALS[i])
        return VALS2[i];
    }
    return res;
  }

  /**
   * Obtain the number of negligible terms
   * 
   * @return the number of negligible terms
   */
  @Override
  public final double getNegligible() {
    return this.m_neg;
  }

}
