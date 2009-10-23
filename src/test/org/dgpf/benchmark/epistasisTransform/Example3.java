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

import org.sfc.text.TextUtils;

/**
 * an example for the whole thing
 */
public class Example3 {

  /**
   * the map
   */
  private static final int M = 10;

  /**
   * the ruggedness
   */
  private static final int[][] RUGGEDNESS = new int[1000 * M][];

  /**
   * the ruggedness
   */
  private static final int[] RUGGEDNESSC = new int[1000 * M];

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
   * check a ruggedness
   * 
   * @param map
   */
  private static final void checkRugged(final int[] map) {
    int i;

    i = ruggedVal(map);
    if (RUGGEDNESS[i] == null)
      RUGGEDNESS[i] = map.clone();
    RUGGEDNESSC[i]++;
  }

  /**
   * @param map
   *          the map
   * @param start
   *          the start
   */
  static final void checkMap(final int[] map, final int start) {
    int i, t;

    checkRugged(map);

    for (i = (start - 1); i > 0; i--) {
      t = map[start];
      map[start] = map[i];
      map[i] = t;

      checkMap(map, start - 1);

      map[i] = map[start];
      map[start] = t;
    }

  }

  /**
   * 
   */
  static int t1;

  /**
   * 
   */
  static int t2;

  /**
   * the ruggedness
   * 
   * @param l
   *          the integer list
   * @param gamma
   *          the gamma
   */
  static final void r(final int[] l, final int gamma) {
    int i, j, t;

    if (gamma <= 0)
      return;

    if (gamma < l.length - 1) {
      r(l, gamma - 1);
      j = l.length - 1;
      i = j - gamma;// ((gamma % (l.length - 1)));

      t = l[i];
      l[i] = l[j];
      l[j] = t;
    } else {

     
      
      i = (l.length >>> 1);
      j = l.length - 1;
      t = l[i];
      l[i] = l[j];
      l[j] = t;
      j = i-1;
      t = l[i];
      l[i] = l[j];
      l[j] = t;

      r(l, gamma - l.length + 1);
      
      //
    }

  }

  /**
   * the main routine
   * 
   * @param args
   *          the arguments
   */
  public static void main(final String[] args) {
    int[] map, d;
    int i, s, e;

    map = new int[M];
    for (i = (map.length - 1); i >= 0; i--)
      map[i] = i;

    d = map.clone();

    checkMap(map, map.length - 1);

    for (s = 0; s < RUGGEDNESS.length; s++) {
      if (RUGGEDNESS[s] != null)
        break;
    }
    for (e = (RUGGEDNESS.length - 1); e >= s; e--) {
      if (RUGGEDNESS[e] != null)
        break;
    }

    // for (i = s; i <= e; i++) {
    // System.out.println(i + ": (" + //$NON-NLS-1$
    // RUGGEDNESSC[i] + ") - " + //$NON-NLS-1$
    // TextUtils.toString(RUGGEDNESS[i]));
    // }
    //
    // System.exit(0);

    // for (t1 = 1; t1 < map.length; t1++) {
    // for (t2 = 1; t2 < map.length; t2++) {
    // System.arraycopy(d, 0, map, 0, d.length);
    // r(map, map.length - 1);
    // if (ruggedVal(map) == 18) {
    // System.out.println(TextUtils.toString(map));
    // System.out.println(t1);
    // System.out.println(t2);
    // }
    // }
    // }

    System.out.println();
    for (i = s; i <= e; i++) {
      System.arraycopy(d, 0, map, 0, d.length);
      r(map, i - s);
      System.out.println((i - s) + " " + //$NON-NLS-1$
          ruggedVal(map) + ": " + TextUtils.toString(map));//$NON-NLS-1$
    }
  }
}
