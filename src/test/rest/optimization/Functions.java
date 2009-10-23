/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-29
 * Creator          : Thomas Weise
 * Original Filename: test.rest.optimization.Functions.java
 * Last modification: 2006-12-29
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

package test.rest.optimization;

import org.sfc.io.TextWriter;
import org.sfc.math.IRealFunction2d;

/**
 * the dummy test class
 * 
 * @author Thomas Weise
 */
public class Functions {

  /**
   * the minimum x value
   */
  public static final double MIN_X = -10;

  /**
   * the maximum x value
   */
  public static final double MAX_X = Math.abs(MIN_X);

  /**
   * the maximum x value
   */
  public static final double X_RANGE = Math.rint(MAX_X - MIN_X);

  /**
   * the minimum y value
   */
  public static final double MIN_Y = MIN_X;

  /**
   * the maximum y value
   */
  public static final double MAX_Y = MAX_X;

  /**
   * the maximum y value
   */
  public static final double Y_RANGE = Math.rint(MAX_Y - MIN_Y);

  /**
   * the first real function
   */
  public static final IRealFunction2d[] F = new IRealFunction2d[] {

  new IRealFunction2d() {
    public double f(final double x, final double y) {
      double z;
      z = 0.1 * Math.sqrt((x * x) + (y * y));
      return Math.sin(10 * z)
          * Math.exp(-z)
          + (0.1 * Math.min(Math.abs(Math.cosh(z)), Math.abs(Math.sin(x))));
    }
  }, new IRealFunction2d() {
    public double f(final double x, final double y) {
      double d, e;

      d = (MAX_X - Math.abs(x));
      d *= d;
      e = (MAX_Y - Math.abs(y));
      e *= e;
      return (d * e) * ((x * x) * Math.cos(y) - (y * y) * Math.sin(x));
    }
  } };

  /**
   * the steps
   */
  public static final int STEPS = 100;// 500;

  /**
   * the value matrix
   */
  public static final double[][][] MATRIX = new double[STEPS][STEPS][F.length];

  /**
   * the minima
   */
  public static final double[] MIN = new double[F.length];

  /**
   * the domain matrix
   */
  public static final double[][][] MATRIXD = new double[STEPS][STEPS][2];

  /**
   * the sum
   */
  public static final double[][] SUM = new double[STEPS][STEPS];

  /**
   * the sum
   */
  public static final double[][] PARETO = new double[STEPS][STEPS];

  /**
   * the first range min
   */
  public static final double[] MIN_R = new double[] { 0.3, 0.4 };

  /**
   * the first range min
   */
  public static final double[] MAX_R = new double[] { 0.7, 0.8 };

  /**
   * the sum
   */
  public static final double[][] MOI = new double[STEPS][STEPS];

  /**
   * the sum
   */
  public static final int[][] MOIC = new int[STEPS][STEPS];

  static {
    int v, i, j, k;//, l, m, u;
    double x, y;
//    boolean b;
    System.out.println("init"); //$NON-NLS-1$
    k = (STEPS - 1);
    for (v = (F.length - 1); v >= 0; v--) {
      for (i = k; i >= 0; i--) {
        for (j = k; j >= 0; j--) {
          x = MIN_X + ((i * X_RANGE) / STEPS);
          y = MIN_Y + ((j * Y_RANGE) / STEPS);
          MATRIXD[i][j][0] = x;
          MATRIXD[i][j][1] = y;
          MATRIX[i][j][v] = F[v].f(x, y);
        }
      }
    }

    for (v = (F.length - 1); v >= 0; v--) {
      x = Double.NEGATIVE_INFINITY;
      y = Double.POSITIVE_INFINITY;
      for (i = k; i >= 0; i--) {
        for (j = k; j >= 0; j--) {
          x = Math.max(x, MATRIX[i][j][v]);
          y = Math.min(y, MATRIX[i][j][v]);
        }
      }
      MIN[v] = y;

      for (i = k; i >= 0; i--) {
        for (j = k; j >= 0; j--) {
          MATRIX[i][j][v] = (MATRIX[i][j][v] - y) / (x - y);
        }
      }
    }
//    System.out.println("sum"); //$NON-NLS-1$
//    for (i = k; i >= 0; i--) {
//      for (j = k; j >= 0; j--) {
//        for (v = (F.length - 1); v >= 0; v--) {
//          SUM[i][j] += MATRIX[i][j][v];
//        }
//      }
//    }
//    System.out.println("pareto"); //$NON-NLS-1$
//    for (i = k; i >= 0; i--) {
//      for (j = k; j >= 0; j--) {
//        u = 0;
//        for (l = k; l >= 0; l--) {
//          outer: for (m = k; m >= 0; m--) {
//            if ((l != i) && (m != j)) {
//              b = false;
//              for (v = (F.length - 1); v >= 0; v--) {
//                if (MATRIX[i][j][v] < MATRIX[l][m][v])
//                  continue outer;
//                else if (MATRIX[i][j][v] > MATRIX[l][m][v])
//                  b = true;
//              }
//
//              if (b)
//                u++;
//            }
//          }
//        }
//
//        PARETO[i][j] = u;
//      }
//    }
//    System.out.println("moi"); //$NON-NLS-1$
//    boolean outside, inside;
//    for (i = k; i >= 0; i--) {
//      for (j = k; j >= 0; j--) {
//        outside = inside = false;
//        for (v = (F.length - 1); v >= 0; v--) {
//          if ((MATRIX[i][j][v] < MIN_R[v]) || (MATRIX[i][j][v] > MAX_R[v]))
//            outside = true;
//          else
//            inside = true;
//        }
//        if (inside) {
//          MOIC[i][j] = (outside) ? 1 : 0;
//        } else
//          MOIC[i][j] = 2;
//      }
//    }
//
//    for (i = k; i >= 0; i--) {
//      for (j = k; j >= 0; j--) {
//        u = 0;
//        for (l = k; l >= 0; l--) {
//          outer: for (m = k; m >= 0; m--) {
//            if ((l != i) && (m != j)) {
//              b = false;
//
//              if (MOIC[i][j] < MOIC[l][m])
//                continue outer;
//              if (MOIC[i][j] > MOIC[l][m])
//                b = true;
//              else {
//
//                for (v = (F.length - 1); v >= 0; v--) {
//                  if (MATRIX[i][j][v] < MATRIX[l][m][v])
//                    continue outer;
//                  else if (MATRIX[i][j][v] > MATRIX[l][m][v])
//                    b = true;
//                }
//              }
//
//              if (b)
//                u++;
//            }
//          }
//        }
//
//        MOI[i][j] = u;
//      }
//    }

  }

  /**
   * dont print?
   * 
   * @param i
   *          i
   * @param j
   *          j
   * @return <code>true</code> if this line is to be skipped
   */
  private static final boolean dontPrint(final int i, final int j) {
    return false;// (((i % 3) != 0) || ((j % 3) != 0));
  }

  /**
   * the main routine
   * 
   * @param args
   *          the arguments
   */
  public static void main(final String[] args) {
    int i, j, k;
    TextWriter out;
//    double d;
    System.out.println("print"); //$NON-NLS-1$

    for (k = 0; k < F.length; k++) {
      out = new TextWriter("E:\\series\\series" + (k + 1) + //$NON-NLS-1$
          ".txt"); //$NON-NLS-1$
      for (i = 0; i < STEPS; i++) {
        for (j = 0; j < STEPS; j++) {
          if (dontPrint(i, j))
            continue;

          out.ensureNewLine();
          out.writeDouble(MATRIXD[i][j][0]);
          out.writeCSVSeparator();
          out.writeDouble(MATRIXD[i][j][1]);
          out.writeCSVSeparator();
          out.writeDouble(MATRIX[i][j][k]);
        }
        out.flush();
      }
      out.release();

      out = new TextWriter("E:\\series\\min" + (k + 1) + //$NON-NLS-1$
          ".txt"); //$NON-NLS-1$
      for (i = 0; i < STEPS; i++) {
        for (j = 0; j < STEPS; j++) {
          if (dontPrint(i, j))
            continue;

          out.ensureNewLine();
          out.writeDouble(MATRIXD[i][j][0]);
          out.writeCSVSeparator();
          out.writeDouble(MATRIXD[i][j][1]);
          out.writeCSVSeparator();
          out.writeDouble((MATRIX[i][j][k] <= 0d) ? 1 : 0);
          
          if(MATRIX[i][j][k]<=0){
            System.out.println(i+" " +j); //$NON-NLS-1$
          }
        }
        out.flush();
      }
      out.release();
    }

//    out = new TextWriter("E:\\series\\sum.txt"); //$NON-NLS-1$
//    for (i = 0; i < STEPS; i++) {
//      for (j = 0; j < STEPS; j++) {
//        if (dontPrint(i, j))
//          continue;
//
//        out.ensureNewLine();
//        out.writeDouble(MATRIXD[i][j][0]);
//        out.writeCSVSeparator();
//        out.writeDouble(MATRIXD[i][j][1]);
//        out.writeCSVSeparator();
//        out.writeDouble(SUM[i][j]);
//      }
//      out.flush();
//    }
//    out.release();
//
//    d = Double.MAX_VALUE;
//    for (i = 0; i < STEPS; i++) {
//      for (j = 0; j < STEPS; j++) {
//        if (dontPrint(i, j))
//          continue;
//        d = Math.min(SUM[i][j], d);
//      }
//    }
//
//    out = new TextWriter("E:\\series\\sumOpt.txt"); //$NON-NLS-1$
//    for (i = 0; i < STEPS; i++) {
//      for (j = 0; j < STEPS; j++) {
//        if (dontPrint(i, j))
//          continue;
//
//        out.ensureNewLine();
//        out.writeDouble(MATRIXD[i][j][0]);
//        out.writeCSVSeparator();
//        out.writeDouble(MATRIXD[i][j][1]);
//        out.writeCSVSeparator();
//        out.writeInt((SUM[i][j] <= d) ? 1 : 0);
//      }
//      out.flush();
//    }
//    out.release();
//
//    out = new TextWriter("E:\\series\\pareto.txt"); //$NON-NLS-1$
//    for (i = 0; i < STEPS; i++) {
//      for (j = 0; j < STEPS; j++) {
//        if (dontPrint(i, j))
//          continue;
//
//        out.ensureNewLine();
//        out.writeDouble(MATRIXD[i][j][0]);
//        out.writeCSVSeparator();
//        out.writeDouble(MATRIXD[i][j][1]);
//        out.writeCSVSeparator();
//        out.writeDouble(PARETO[i][j]);
//      }
//      out.flush();
//    }
//    out.release();
//
//    out = new TextWriter("E:\\series\\paretoOpt.txt"); //$NON-NLS-1$
//    for (i = 0; i < STEPS; i++) {
//      for (j = 0; j < STEPS; j++) {
//        if (dontPrint(i, j))
//          continue;
//
//        out.ensureNewLine();
//        out.writeDouble(MATRIXD[i][j][0]);
//        out.writeCSVSeparator();
//        out.writeDouble(MATRIXD[i][j][1]);
//        out.writeCSVSeparator();
//        out.writeInt((PARETO[i][j] <= 0) ? 1 : 0);
//      }
//      out.flush();
//    }
//    out.release();
//
//    out = new TextWriter("E:\\series\\moi.txt"); //$NON-NLS-1$
//    for (i = 0; i < STEPS; i++) {
//      for (j = 0; j < STEPS; j++) {
//        if (dontPrint(i, j))
//          continue;
//
//        out.ensureNewLine();
//        out.writeDouble(MATRIXD[i][j][0]);
//        out.writeCSVSeparator();
//        out.writeDouble(MATRIXD[i][j][1]);
//        out.writeCSVSeparator();
//        out.writeDouble(MOI[i][j]);
//      }
//      out.flush();
//    }
//    out.release();
//
//    out = new TextWriter("E:\\series\\moiOpt.txt"); //$NON-NLS-1$
//    for (i = 0; i < STEPS; i++) {
//      for (j = 0; j < STEPS; j++) {
//        if (dontPrint(i, j))
//          continue;
//
//        out.ensureNewLine();
//        out.writeDouble(MATRIXD[i][j][0]);
//        out.writeCSVSeparator();
//        out.writeDouble(MATRIXD[i][j][1]);
//        out.writeCSVSeparator();
//        out.writeInt((MOI[i][j] <= 0) ? 1 : 0);
//      }
//      out.flush();
//    }
//    out.release();
//
//    out = new TextWriter("E:\\series\\moiC.txt"); //$NON-NLS-1$
//    for (i = 0; i < STEPS; i++) {
//      for (j = 0; j < STEPS; j++) {
//        if (dontPrint(i, j))
//          continue;
//
//        out.ensureNewLine();
//        out.writeDouble(MATRIXD[i][j][0]);
//        out.writeCSVSeparator();
//        out.writeDouble(MATRIXD[i][j][1]);
//        out.writeCSVSeparator();
//        out.writeInt(MOIC[i][j]);
//      }
//      out.flush();
//    }
//    out.release();
  }

}
