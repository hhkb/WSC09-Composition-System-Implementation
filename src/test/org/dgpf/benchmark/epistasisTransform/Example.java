/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : TODO
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.benchmark.epistasisTransform.Example.java
 * Last modification: TODO
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

package test.org.dgpf.benchmark.epistasisTransform;

import org.sfc.math.Mathematics;
import org.sfc.text.TextUtils;
import org.sigoa.refimpl.stoch.Randomizer;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * an example for the whole thing
 */
public class Example {

  /**
   * the e mappings
   */
  public static final int[][] E_MAPPINGS = new int[][] {
      new int[] { 0, 1 },
      new int[] { 0, 1, 3, 2 },
      new int[] { 0, 5, 3, 6, 7, 2, 4, 1 },
      new int[] { 0, 13, 11, 6, 7, 10, 12, 1, 15, 2, 4, 9, 8, 5, 3, 14 },
      new int[] { 0, 29, 27, 6, 23, 10, 12, 17, 15, 18, 20, 9, 24, 5, 3,
          30, 31, 2, 4, 25, 8, 21, 19, 14, 16, 13, 11, 22, 7, 26, 28, 1 },
      new int[] { 0, 61, 59, 6, 55, 10, 12, 49, 47, 18, 20, 41, 24, 37,
          35, 30, 31, 34, 36, 25, 40, 21, 19, 46, 48, 13, 11, 54, 7, 58,
          60, 1, 63, 2, 4, 57, 8, 53, 51, 14, 16, 45, 43, 22, 39, 26, 28,
          33, 32, 29, 27, 38, 23, 42, 44, 17, 15, 50, 52, 9, 56, 5, 3, 62 } };

  /**
   * the example
   */
  public static final boolean[] EXAMPLE = new boolean[] { false, true,
      false, false, false, true, true, false, false, false, false, false,
      true, true, true, false, true, false, false, false, false };

  /**
   * the neutrality
   */
  public static final int MU = 2;

  /**
   * the epistasis
   */
  public static final int ETA = 4;

  /**
   * the length
   */
  public static final int N = 6;

  /**
   * the length
   */
  public static final int M = 2;

  /**
   * the length
   */
  public static final int T = 4;

  /**
   * the length
   */
  public static final int O = 1;

  /**
   * the best
   */
  public static boolean[] BEST;

  /**
   * the tests
   */
  public static boolean[][] TC;

  /**
   * init
   */
  public static final void init() {
    int i, l, j;
    boolean[] x;
    IRandomizer r;

    i = N;
    BEST = new boolean[i];
    for (--i; i >= 0; i--) {
      BEST[i] = ((i & 1) != 0);
    }

    r = new Randomizer();
    r.setDefaultSeed();
    TC = new boolean[T][];

    main: for (;;) {
      for (l = (T - 1); l >= 0; l--) {
        TC[l] = x = BEST.clone();
        for (i = O; i > 0; i--) {
          do {
            j = r.nextInt(x.length);
          } while (x[j] != BEST[j]);
          x[j] = (!(BEST[j]));
        }
      }

      for (l = N - 1; l >= 0; l--) {
        j = 0;
        for (i = T - 1; i >= 0; i--) {
          if (TC[i][l] != BEST[l])
            j++;
        }
        if (j >= (T - j))
          continue main;
      }
      break main;
    }

    System.out.println("best " + TextUtils.toBitString(BEST)); //$NON-NLS-1$
    for (l = 0; l < T; l++) {
      System.out.println("test" + l + //$NON-NLS-1$
          ": " + TextUtils.toBitString(TC[l])); //$NON-NLS-1$
    }
  }

  /**
   * apply neutrality
   * 
   * @param n
   *          the bits
   * @return the neutralized bits
   */
  public static final boolean[] neutrality(final boolean[] n) {
    int l, l1, i, j, k;
    boolean[] res;

    l1 = n.length;
    l = Mathematics.ceilDiv(l1, MU);

    res = new boolean[l];

    j = 0;
    l = 0;
    k = 0;
    for (i = 0; i < l1; i++) {
      if (n[i])
        j++;
      else
        j--;
      l++;
      if (l >= MU) {
        l = 0;
        res[k++] = (j >= 0);
        j = 0;
      }
    }
    if (l > 0)
      res[k++] = (j >= 0);

    return res;
  }

  /**
   * perform the epistasis mapping
   * 
   * @param in
   *          the input
   * @param out
   *          the output
   * @param s
   *          the start
   * @param c
   *          the count
   */
  private static void emap(final boolean[] in, final boolean[] out,
      final int s, final int c) {
    int x, i, j;
    if (c <= 0)
      return;
    x = 0;
    for (i = s, j = c; j > 0; i++, j--) {
      x <<= 1;
      x |= (in[i] ? 1 : 0);
    }

    x = E_MAPPINGS[c][x];
    for (i = (s + c - 1), j = c; j > 0; i--, j--) {
      out[i] = ((x & 1) != 0);
      x >>>= 1;
    }
  }

  /**
   * split the boolean value
   * 
   * @param in
   *          the input value
   * @return the output list
   */
  private static final boolean[][] split(final boolean[] in) {
    boolean[][] b;
    int i;

    b = new boolean[M][N];
    for (i = (M * N) - 1; i >= 0; i--) {
      if (i < in.length) {
        b[i % M][i / M] = in[i];
      } else {
        b[i % M][i / M] = (!(BEST[i / M]));
      }
    }
    return b;
  }

  /**
   * perform the emapping
   * 
   * @param in
   *          the input
   * @param out
   *          the output
   * @return the mapping result
   */
  private static final boolean[] emap(final boolean[] in) {
    boolean[] out;
    int i, l;

    l = in.length;
    out = new boolean[l];
    for (i = 0; i < (l / ETA); i++) {
      emap(in, out, i * ETA, ETA);
    }
    i = l % ETA;
    emap(in, out, l - i, i);

    return out;
  }

  /**
   * the hamming distance
   * 
   * @param b1
   *          the first bit string
   * @param b2
   *          the second bit string
   * @return their hamming distance
   */
  private static final int h(final boolean[] b1, final boolean[] b2) {
    int i, j;

    i = Math.min(b1.length, b2.length);
    j = (Math.max(b1.length, b2.length) - i);

    for (--i; i >= 0; i--) {
      if (b1[i] != b2[i])
        j++;
    }

    return j;
  }

  /**
   * Write the input array <code>in</code> to the output array
   * <code>out</code> while downsizing it to the degree of
   * <code>mu</code>.
   * 
   * @param in
   *          the input
   * @param out
   *          the output
   * @param size
   *          the number of bits
   * @param mu
   *          the neutrality factor
   * @return the new number of bits
   */
  public static final int neutrality(final byte[] in, final byte[] out,
      final int size, final int mu) {
    int i, b, j, o, k, l, z;

    o = 0;
    j = 0;
    b = 0;
    k = 0;
    l = 0;
    z = 0;
    for (i = 0; i < size; i++) {
      if ((i & 0x7) == 0)
        b = in[i >>> 3];

      if ((b & 1) != 0)
        j++;
      else
        j--;
      b >>>= 1;

      if ((++k) >= mu) {
        if (j >= 0)
          o |= (1<<l);
        j = 0;
        k = 0;
        if ((++l) >= 8) {
          l = 0;
          out[z++] = (byte) o;
          o = 0;
        }
      }
    }

    if (k > 0) {
      if (j >= 0)
        o |= (1<<l);
      l++;
    }
    if (l > 0) {
      out[z] = (byte) o;
    }

    return ((z << 3) + l);
  }

  /**
   * compute the objective value
   * 
   * @param b
   *          the bit string
   * @return its objective value
   */
  private static final int doTests(final boolean[] b) {
    int s, i, l;
    boolean[] x, tc;

    x = new boolean[N];
    s = 0;
    for (i = 0; i < TC.length; i++) {
      tc = TC[i];
      for (l = N - 1; l >= 0; l--) {
        x[l] = (BEST[l] ? (b[l] || (!tc[l])) : (b[l] && (!tc[l])));
      }

      l = h(x, BEST);
      System.out.println("test" + i + //$NON-NLS-1$
          " " + TextUtils.toBitString(x) + //$NON-NLS-1$
          " h=" + l);//$NON-NLS-1$
      s += l;
    }
    return s;
  }

  /**
   * the main routine
   * 
   * @param args
   *          the arguments
   */
  public static void main(final String[] args) {
    byte[] b1 = new byte[] { (byte) 0xf0, (byte) 0xf0, (byte) 0xf0 };
    byte[] b2;

    b2 = new byte[10];
    System.out.println(neutrality(b1, b2, 8 * 3, 4));

    boolean[] b;
    boolean[][] x;
    int i;
    int[] f;

    System.out.println("example " + TextUtils.toBitString(EXAMPLE) + //$NON-NLS-1$
        " len=" + EXAMPLE.length); //$NON-NLS-1$
    System.out.println();
    b = neutrality(EXAMPLE);
    System.out.println("neutral " + TextUtils.toBitString(b) + //$NON-NLS-1$
        " len=" + b.length); //$NON-NLS-1$
    System.out.println();
    b = emap(b);
    System.out.println("epistasis " + TextUtils.toBitString(b) + //$NON-NLS-1$
        " len=" + b.length); //$NON-NLS-1$
    System.out.println();
    init();
    System.out.println();
    x = split(b);
    for (i = 0; i < x.length; i++) {
      System.out.println("v" + i + //$NON-NLS-1$
          ": " + TextUtils.toBitString(x[i]));//$NON-NLS-1$
    }

    f = new int[x.length];
    for (i = 0; i < x.length; i++) {
      System.out.println();
      System.out.println("===================== v" + i);//$NON-NLS-1$
      f[i] = doTests(x[i]);
      System.out.println("f=" + f[i]);//$NON-NLS-1$
      System.out.println();
    }
  }
}
