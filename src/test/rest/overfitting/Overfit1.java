/*
 * Copyright (c) 2006 Thomas Weise
 *
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-01-07 15:26:24
 * Original Filename: examples.Test.java
 * Version          : 2.0.0
 * Last modification: 2006-04-10
 *                by: Thomas Weise
 *
 * License          : GNU LESSER GENERAL PUBLIC LICENSE
 *                    Version 2.1, February 1999
 *                    You should have received a copy of this license along
 *                    with this library; if not, write to the Free Software
 *                    Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 *                    MA 02111-1307, USA or download the license under
 *                    http://www.gnu.org/copyleft/lesser.html.
 *
 * Warranty         : This software is provided "as is" without any
 *                    warranty; without even the implied warranty of
 *                    merchantability or fitness for a particular purpose.
 *                    See the Gnu Lesser General Public License for more
 *                    details.
 *
 * STRONG WARNING   : This file is an experimental test file. Anything you
 *                    perform may result in unexpected, unintend or even
 *                    catastrophical results.
 */

package test.rest.overfitting;

import java.util.Arrays;

import org.sfc.io.TextWriter;
import org.sigoa.refimpl.stoch.Randomizer;

/**
 * The default skeleton of a test class. This class does nothing, it is
 * just copied when a new test class is needed.
 * 
 * @author Thomas Weise
 */
public class Overfit1 {

  /**
   * the sxamples
   */
  private static final double[] SAMPLESX = new double[3];

  /**
   * the ysamples
   */
  private static final double[] SAMPLESY = SAMPLESX.clone();

/**
 * the original function
 * @param d
 * @return
 */
  private static final double origF(final double d){
    return d;
  }
  
  /**
   * the x samples
   */
  private static final double[] YSAMPLES = new double[SAMPLESX.length * 2 + 11];

  static {
    int i;
    double s,d;

    d = SAMPLESX.length - 1;
    s = 0;
    for (i = (SAMPLESX.length - 1); i >= 0; i--) {
      SAMPLESX[i] = s+(i / d);
      SAMPLESY[i] = origF(SAMPLESX[i]);
    }

    System.arraycopy(SAMPLESX, 0, YSAMPLES, 0, SAMPLESX.length);
    for (i = (SAMPLESX.length); i < YSAMPLES.length - 12; i++) {
      YSAMPLES[i] = (0.5d * (SAMPLESX[i - SAMPLESX.length] + SAMPLESX[i
          - SAMPLESX.length + 1]));
    }

    YSAMPLES[YSAMPLES.length - 1] = -1 / d;
    YSAMPLES[YSAMPLES.length - 2] = 1 + 1 / d;

    YSAMPLES[YSAMPLES.length - 3] = -2 / d;
    YSAMPLES[YSAMPLES.length - 4] = 1 + 2 / d;

    YSAMPLES[YSAMPLES.length - 5] = -3 / d;
    YSAMPLES[YSAMPLES.length - 6] = 1 + 3 / d;

    YSAMPLES[YSAMPLES.length - 7] = -0.5 / d;
    YSAMPLES[YSAMPLES.length - 8] = 1 + 0.5 / d;

    YSAMPLES[YSAMPLES.length - 9] = -2.5 / d;
    YSAMPLES[YSAMPLES.length - 10] = 1 + 2.5 / d;

    YSAMPLES[YSAMPLES.length - 11] = -3.5 / d;
    YSAMPLES[YSAMPLES.length - 12] = 1 + 3.5 / d;
    Arrays.sort(YSAMPLES);
  }

  /**
   * f
   * 
   * @param d
   *          the data
   * @param x
   *          the value
   * @return the value
   */
  private static final double f(final double[] d, final double x) {
    int i;
    double r;

    r = 0d;
    for (i = d.length; i > 0; i--) {
      r += d[i - 1] * Math.pow(x, i);
    }

    return r;
  }

  /**
   * obtain the fitness
   * 
   * @param d
   *          the function
   * @param s
   *          the sample
   * @param v
   *          the values
   * @return the result
   */
  private static final double fit(final double[] d, final double[] s,
      final double[] v) {
    double x;
    int i;

    x = 0;
    for (i = (s.length - 1); i >= 0; i--) {
      x += Math.abs(v[i] - f(d, s[i]));
    }
    return x;
  }

  /**
   * compute a function that fits
   * 
   * @return the function
   */
  public static final double[] getFunction() {
    double[] a, b, c;
    final Randomizer r;
    int i, j, cr, k;
    double f1a, f1b;// , f2a, f2b;

    r = new Randomizer();
    // t = 2.0d / SAMPLESX.length;
    a = new double[SAMPLESX.length+2];
    b = new double[SAMPLESX.length+2];
    for (i = (a.length - 1); i >= 0; i--) {
      a[i] = r.nextDouble();
      b[i] = r.nextDouble();
    }

    cr = 0;

    f1a = fit(a, SAMPLESX, SAMPLESY);
    do {
      f1b = fit(b, SAMPLESX, SAMPLESY);

      if ((++cr % 10000) == 0)
        System.out.println(f1a + " " + f1b); //$NON-NLS-1$

      if (f1b < f1a) {
        f1a = f1b;
        c = a;
        a = b;
        b = c;
      }

      System.arraycopy(a, 0, b, 0, a.length);

      for (k = r.nextInt(a.length); k >= 0; k--) {
        j = r.nextInt(a.length);
        if (r.nextBoolean())
          b[j] = r.nextNormal(a[j], 0.01);
        else
          b[j] = r.nextNormal(a[j], 0.1);

      }

    } while (f1a > 0.05);

    return a;
  }

  /**
   * the f
   */
  private static double[] F = getFunction();

  // new double[] { 0.8094178096185658,
  // 0.66275472587691, -0.38346154985227343, -0.35991112048605567,
  // 0.11591441474756328, 0.03547932550436405, -0.3157838452974216,
  // -0.1018413882364843, 0.5703535888822986, 0.35707070865422996,
  // 0.4457587155837554, 0.12448706892877509, -0.20623107188956835,
  // -0.5879739260299849, -1.1346443567718687, 0.5811302916434978,
  // 0.48353712751665545, -0.6153480128744281, 0.06045807996116298,
  // 0.4595328567933496 };

  /**
   * The main routine.
   * 
   * @param p_args
   *          The comment line arguments.
   */
  public static void main(String[] p_args) {
    TextWriter w;
    int i;

    w = new TextWriter("E:\\d\\plain.txt"); //$NON-NLS-1$
    for (i = (SAMPLESX.length - 1); i >= 0; i--) {
      w.ensureNewLine();
      w.writeDouble(SAMPLESX[i]);
      w.writeCSVSeparator();
      w.writeDouble(SAMPLESY[i]);
    }
    w.release();

    w = new TextWriter("E:\\d\\func.txt"); //$NON-NLS-1$
    for (i = (YSAMPLES.length - 1); i >= 0; i--) {
      w.ensureNewLine();
      w.writeDouble(YSAMPLES[i]);
      w.writeCSVSeparator();
      w.writeDouble(f(F, YSAMPLES[i]));

    }
    w.release();
  }

}
