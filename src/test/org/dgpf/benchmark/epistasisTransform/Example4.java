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

import java.util.Arrays;

import org.sfc.text.TextUtils;

/**
 * an example for the whole thing
 */
public class Example4 {

  /**
   * the map
   */
  private static final int M = 10;

  /**
   * x
   */
  private static final int MAX = ((M * (M - 1)) >>> 1);

  /**
   * @param map
   *          the map
   * @return the rugged val
   */
  private static final int ruggedVal(final int[] map) {
    int i, s;

    s = 0;
    for (i = (map.length - 1); i > 0; i--) {
      s += Math.abs(map[i] - map[i - 1]);
      // if (i < (map.length - 1))
      // s += Math.abs(map[i] - map[i + 1]);
    }

    return s;
  }

  /**
   * test
   * 
   * @param maps
   *          maps
   * @param q
   *          the q value
   * @param chg
   * @return the return value
   */
  private static final int test(final int[][] maps, int[][] chg,
      final int q) {
    int x, z;

    x = ruggedVal(maps[q]);
    if (x != q)
      return 0;

    if (x >= MAX) {
      for (z = 0; z <= MAX; z++) {
        if (maps[z] != null) {
          if (maps[z - 1] != null) {
            if (z>(minq+1)) {
              if((maps[z][chg[z][1]] != maps[z-1][chg[z - 1][0]]))
                return 2;
//              if((maps[z][chg[z][0]] != maps[z][chg[z][1]]+1))return 2;
            }
          }
        }
      }
      
      System.out.println();
      System.out.println();
      for (z = 0; z <= MAX; z++) {
        if (maps[z] != null) {
          System.out.println(z + ": (" + //$NON-NLS-1$
              chg[z][0] + "," + //$NON-NLS-1$
              chg[z][1] + "),(" + //$NON-NLS-1$
              chg[z][2] + "," + //$NON-NLS-1$
              chg[z][3] + ") [" + //$NON-NLS-1$
              maps[z][chg[z][3]] + "," + //$NON-NLS-1$
              maps[z][chg[z][2]] + "],[" + //$NON-NLS-1$
              maps[z][chg[z][1]] + "," + //$NON-NLS-1$
              maps[z][chg[z][0]] + "]," + //$NON-NLS-1$
              TextUtils.toString(maps[z]));
        }
      }
      System.out.flush();

      return 2;
    }
    return 1;
  }

  /**
   * @param maps
   *          the map
   * @param chg
   * @param r
   * @return x
   */
  static final boolean checkMap(final int[][] maps, final int[][] chg,
      final int r) {

    int i, j, t, q;
    int[] map;

    q = r;
    if (maps[q + 1] == null)
      map = maps[q].clone();
    else {
      map = maps[q + 1];
      System.arraycopy(maps[q], 0, map, 0, map.length);
    }
    q++;
    maps[q] = map;
    Arrays.fill(chg[q], 0);

    if (basicCheckMap(maps, map, chg, q)) {
      // return true;
    }

    if (isSpecial(q)) {// (((q - minq) % (map.length - 1) == 0)) {

      for (i = (M - 1); i > 1; i--) {
        for (j = (i - 1); j > 0; j--) {
          t = map[j];
          map[j] = map[i];
          map[i] = t;

          chg[q][2] = i;
          chg[q][3] = j;

          if (basicCheckMap(maps, map, chg, q)) {
            // return true;
          }

          map[i] = map[j];
          map[j] = t;
        }
      }

    }

    return false;
  }

  /**
   * 
   */
  static int minq = 0;

  /**
   * @param q
   * @return
   */
  private static final boolean isSpecial(final int q) {
    return (((q - minq) % (M - 1) == 0));
  }

  /**
   * @param maps
   *          the map
   * @param chg
   * @param q
   * @param map
   * @return x
   */
  static final boolean basicCheckMap(final int[][] maps, final int[] map,
      final int[][] chg, final int q) {
    int i, j, t, z;

    for (i = (M - 1); i > 1; i--) {
      for (j = (i - 1); j > 0; j--) {
        t = map[j];
        map[j] = map[i];
        map[i] = t;

        chg[q][0] = i;
        chg[q][1] = j;

        z = test(maps, chg, q);

        // if (z > 1)
        // return true;
        if (z != 0) {
          if (checkMap(maps, chg, q))
            return true;
        }

        map[i] = map[j];
        map[j] = t;
      }

    }

    return false;
  }

  /**
   * the main routine
   * 
   * @param args
   *          the arguments
   */
  public static void main(final String[] args) {
    int[] map;
    int[][] m;
    int i;

    // System.setOut(new TextWriterPrintStream(new
    // TextWriter("E:\\test.txt"))); //$NON-NLS-1$

    map = new int[M];
    for (i = (map.length - 1); i >= 0; i--)
      map[i] = i;

    // map = new int[] { 0, 2, 3, 4, 5, 6, 7, 8, 9, 1 };
    minq = i = ruggedVal(map);
    m = new int[M * M * M][];
    m[i] = map;

    checkMap(m, new int[m.length][4], i);

  }
}
