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

package test.rest.fitnessLandscape;

import java.io.File;
import java.util.Arrays;

import org.sfc.io.CanonicalFile;
import org.sfc.io.IO;
import org.sfc.io.TextWriter;
import org.sfc.parallel.SfcThread;
import org.sigoa.refimpl.stoch.Randomizer;

/**
 * The default skeleton of a test class. This class does nothing, it is
 * just copied when a new test class is needed.
 * 
 * @author Thomas Weise
 */
public class ProblemLandscape {
  /**
   * the bit count
   */
  private static final int MAX = 60;

  /**
   * the bit count
   */
  private static final int T = 8000;

  /**
   * the fitness values
   */
  private static final int[][] FITNESS = new int[MAX][MAX];

  /**
   * the standard deviation
   */
  static final double STDDEV = Math.pow(MAX, 0.3d);

  /**
   * the statistical data
   */
  static final int[][][] STAT = new int[T][MAX][MAX];

  /**
   * the number of executions
   */
  static final int TIMES = (1 + ((MAX * MAX * 1000) / Runtime.getRuntime()
      .availableProcessors()));

  /**
   * the total number
   */
  static int TOTAL = 0;

  /**
   * the peak function
   * 
   * @param a
   * @param b
   * @return the peak
   */
  private static final double peak(final double a, final double b) {
    return Math.exp(-Math.sqrt((a * a) + (b * b)));
  }

  /**
   * the fitness function
   * 
   * @param a
   *          a
   * @param b
   *          b
   * @return the result
   */
  private static final int fitness(final double a, final double b) {
    double x, y, r, i, j;
    int k, s, l;

    x = (a - (0.5d * MAX));
    y = (b - (0.5d * MAX));

    r = 0.025 * Math.sqrt((x * x) + (y * y));

    x += 0.05 * MAX;
    y += 0.05 * MAX;
    k = 7;
    l = 3;
    for (i = -0.45; i <= 0.45; i += 0.15d) {
      for (j = -0.45d; j <= 0.45d; j += 0.15d) {

        do {
          do {
            s = (k % 15) - 7;
            k = (((k * 523) + 409) % 631);
          } while (s == 0);
          l = (l * 29 + 11) % 13;
        } while (l < 5);

        r += (s * peak(x - i * MAX, y - j * MAX)) / 3.5d;
      }
    }

    r -= 3 * peak(x - 0.1d * MAX, y -= 0.3d * MAX);

    // // 15
    // r -= peak(x - 0.1 * MAX, y - 0.1 * MAX);
    // r -= 3d * peak(x + 0.1 * MAX, y + 0.1 * MAX);
    // r += peak(x, y);
    //    
    // r -= 2d * peak(x - 0.3 * MAX, y - 0.2 * MAX);
    // r -= 4d * peak(x + 0.3 * MAX, y + 0.3 * MAX);
    // r += 2*peak(x+0.2*MAX, y-0.2*MAX);
    // r += 1.5*peak(x+0.1*MAX, y+0.1*MAX);

    return (int) (Math.rint(r * 100000));
  }

  static {
    int a, b, d, md;

    md = Integer.MAX_VALUE;
    for (a = (MAX - 1); a >= 0; a--) {
      for (b = (MAX - 1); b >= 0; b--) {
        FITNESS[a][b] = d = //          
        fitness(a, b - 0.1d) + //
            fitness(a, b) + //
            fitness(a, b + 0.1d) + //          
            fitness(a - 0.1, b - 0.1d) + //
            fitness(a - 0.1d, b) + //
            fitness(a - 0.1d, b + 0.1d) + //          
            fitness(a + 0.1, b - 0.1d) + //
            fitness(a + 0.1d, b) + //
            fitness(a + 0.1d, b + 0.1d);//

        if (d < md) {
          md = d;
          System.out.println(a + " " + b + //$NON-NLS-1$
              ": " + d);//$NON-NLS-1$
        }
      }
    }
  }

  /**
   * mutate
   * 
   * @param v
   *          the value
   * @param r
   * @return the result
   */
  private static final int mutate(final int v, final Randomizer r) {
    int i;
    do {
      i = (int) (Math.rint(r.nextNormal(v, STDDEV)));
    } while ((i < 0) || (i >= MAX));
    return i;
  }

  /**
   * the run method
   * 
   * @param vs
   * @param b
   * @param rand
   */
  static final void run(final int[][][] vs, final boolean[][] b,
      final Randomizer rand) {
    int k, l, i1, j1, i2, j2;

    for (i1 = (b.length - 1); i1 >= 0; i1--) {
      Arrays.fill(b[i1], true);
    }

    i1 = rand.nextInt(MAX);
    j1 = rand.nextInt(MAX);

    for (k = 0; k < T; k++) {

      if (b[i1][j1]) {
        b[i1][j1] = false;
        for (l = k; l < T; l++) {
          vs[l][i1][j1]++;
        }
      }

      // if (rand.nextBoolean()) {
      // i2 = mutate(i1, rand);
      // j2 = j1;
      // } else {
      // i2 = i1;
      // j2 = mutate(j1, rand);
      // }

      do {
        i2 = mutate(i1, rand);
        j2 = mutate(j1, rand);
      } while ((i1 == i2) && (j1 == j2));

      if (FITNESS[i1][j1] >= FITNESS[i2][j2]) {
        i1 = i2;
        j1 = j2;
      }
    }
  }

  /**
   * print some predefined data
   * 
   * @param data
   *          the data
   * @param multiplier
   *          the multiplier
   * @param dest
   *          the destination
   */
  private static final void doPrint(final Object dest, final int[][] data,
      final double multiplier) {
    TextWriter w;
    int i, j;
    double v;

    try {
      w = new TextWriter(dest);
      try {

        for (i = (MAX - 1); i >= 0; i--) {
          for (j = (MAX - 1); j >= 0; j--) {
            v = ((data[i][j]) * multiplier);
            writeLine(i, j, v, w);

            // if (i > 0) {
            // writeLine(i - 0.999d, j, v, w);
            // if (j > 0)
            // writeLine(i - 0.999d, j - 0.999d, v, w);
            // if (j < (MAX - 1))
            // writeLine(i - 0.999d, j + 0.999d, v, w);
            // }
            //
            // if (i > (MAX - 1)) {
            // writeLine(i + 0.999d, j, v, w);
            // if (j > 0)
            // writeLine(i + 0.999d, j - 0.999d, v, w);
            // if (j < (MAX - 1))
            // writeLine(i + 0.999d, j + 0.999d, v, w);
            // }
            //
            // if (j > 0)
            // writeLine(i, j - 0.999d, v, w);
            // if (j < (MAX - 1))
            // writeLine(i, j + 0.999d, v, w);
          }
        }

      } finally {
        w.release();
      }
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

  /**
   * write a line
   * 
   * @param w
   *          the writer
   * @param a
   *          a
   * @param b
   *          b
   * @param c
   *          c
   */
  private static final void writeLine(final double a, final double b,
      final double c, final TextWriter w) {
    w.ensureNewLine();
    w.writeDouble(a);
    w.writeCSVSeparator();
    w.writeDouble(b);
    w.writeCSVSeparator();
    w.writeDouble(c);
  }

  /**
   * The main routine.
   * 
   * @param p_args
   *          The comment line arguments.
   */
  public static void main(String[] p_args) {
    CanonicalFile f;
    int z;
    SfcThread[] t;

    f = IO.getFile("test"); //$NON-NLS-1$
    f.mkdirs();

    doPrint(new File(f, "fitness.txt"), FITNESS, 1d);//$NON-NLS-1$

    t = new SfcThread[Runtime.getRuntime().availableProcessors()];
    synchronized (STAT) {
      for (z = 0; z < t.length; z++) {
        t[z] = new RunThread();
        t[z].start();
      }
    }

    for (z = 0; z < t.length; z++) {
      t[z].waitFor(false);
    }

    for (z = 0; z < T; z++) {
      doPrint(new File(f, "pl" + z + ".txt"),//$NON-NLS-1$//$NON-NLS-2$
          STAT[z], 1.0d / TOTAL);
    }
  }

  /**
   * the thread
   * 
   * @author Thomas Weise
   */
  private static final class RunThread extends SfcThread {

    /**
     * Allocates a new <code>SfcThread</code> object.
     */
    protected RunThread() {
      super();
    }

    /**
     * Perform the work of this thread.
     */
    @Override
    protected void doRun() {
      int[][][] vs;
      boolean[][] b;
      int i, j, k;
      Randomizer rand;

      synchronized (STAT) {
        System.out.println(this);
      }

      b = new boolean[MAX][MAX];
      vs = new int[T][MAX][MAX];
      rand = new Randomizer();

      for (i = TIMES; i > 0; i--) {
        ProblemLandscape.run(vs, b, rand);
        if ((i % (MAX * MAX)) == 0)
          System.out.println(i);
      }

      synchronized (STAT) {
        TOTAL += TIMES;
        for (i = (T - 1); i >= 0; i--) {
          for (j = (MAX - 1); j >= 0; j--) {
            for (k = (MAX - 1); k >= 0; k--) {
              STAT[i][j][k] += vs[i][j][k];
            }
          }
        }
      }
    }
  }
}
