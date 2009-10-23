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

import org.sfc.io.TextWriter;
import org.sfc.io.TextWriterPrintStream;
import org.sfc.text.TextUtils;

/**
 * an example for the whole thing
 */
public class Example5 {

  /**
   * the map
   */
  static final int M = 10;

  /**
   * x
   */
  static final int MAX = ((M * (M - 1)) >>> 1);

  /**
   * the start config
   */
  static final int[] START = new int[M];
  static {
    int i;
    for (i = (M - 1); i > 0; i--) {
      START[i] = i;
    }
  }

  /**
   * the minimum
   */
  static final int MIN = ruggedVal(START);

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
   */
  static final void checkMap(final int[][] maps, final int[][] chg,
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

    basicCheckMap(maps, map, chg, q);

    if (isSpecial(q)) {// (((q - minq) % (map.length - 1) == 0)) {

      for (i = (M - 1); i > 1; i--) {
        for (j = (i - 1); j > 0; j--) {
          t = map[j];
          map[j] = map[i];
          map[i] = t;

          chg[q][2] = i;
          chg[q][3] = j;

          basicCheckMap(maps, map, chg, q);

          map[i] = map[j];
          map[j] = t;
        }
      }

    }

  }


  /**
   * @param q
   * @return
   */
  private static final boolean isSpecial(final int q) {
    return (((q - MIN) % (M - 1) == 0));
  }

  /**
   * @param maps
   *          the map
   * @param chg
   * @param q
   * @param map
   */
  static final void basicCheckMap(final int[][] maps, final int[] map,
      final int[][] chg, final int q) {
    int i, j, t, z;

    if(isSpecial(q)&&(q>(MIN-1))){
      i = indexOf(map,maps[q-1][chg[q-1][0]]);       
    }
    else i = (M-1);
    

    
//    for (i = (M - 1); i > 1; i--) {
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
         checkMap(maps, chg, q);
        }

        map[i] = map[j];
        map[j] = t;
      }

//    }
  }
  
  /**
   * find the given number
   * @param a 
   * @param v
   * @return x
   */
  private static final int indexOf(final int[] a, final int v){
    int i;
    
    for(i=(a.length-1);a[i]!=v;i--){
//      if(v==a[i])return i;
    }
    return i;
  }

  /**
   * the main routine
   * 
   * @param args
   *          the arguments
   */
  public static void main(final String[] args) {
    int[][] m;

     System
        .setOut(new TextWriterPrintStream(new TextWriter("E:\\test.txt"))); //$NON-NLS-1$

   

    m = new int[MAX+2][];
    m[MIN] = START.clone();

    checkMap(m, new int[m.length][4], MIN);

  }
}
