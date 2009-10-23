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
public class Example7 {

  /**
   * the map
   */
  private static final int M = 26;

  /**
   * @param map
   *          the map
   * @return the rugged val
   */
  private static final int delta(final int[] map) {
    int i, s;

    s = 0;
    for (i = (map.length - 1); i > 0; i--) {
      s += Math.abs(map[i] - map[i - 1]);
    }

    return s;
  }

  /**
   * perform the basic permutation
   * 
   * @param start
   *          the start
   * @param list
   *          the list
   * @param gamma
   *          the gamma
   * @param q
   *          the q
   */
  private static final void basicPermutate(final int start,
      final int[] list, final int gamma, final int q) {
    int i, j, t;

    if (gamma <= 0)
      return;

    basicPermutate(start, list, gamma - 1, q);
    j = q;
    i = j - gamma;
    t = list[i];
    list[i] = list[j];
    list[j] = t;
  }

  /**
   * compute
   * 
   * @param gamma
   * @param list
   * @param q
   */
  private static final void permutate(final int gamma, final int[] list,
      final int q) {
    int j, start, max;

    max = (q * (q - 1)) >>> 1;

    if (gamma <= 0)
      start = 0;
    else {
      start = q - (int) (0.5d + Math.sqrt(0.25 + ((max - gamma) << 1)))
          -1;
    }

    for (j = 1; j < start; j++) {
      list[j] = ((j & 1) != 0) ? (q - j + 1) : j;
    }
    for (; j <= q; j++) {
      list[j] = ((start & 1) != 0) ? (q - j + 1) : j;
    }

    j= ((q-start-1)*(q-start))>>>1;
    j= max-j;
    j =gamma-j+ ((start*(start-1))>>>1);
    
    basicPermutate(start + 1, list, j, q);
  }

  /**
   * compute
   * 
   * @param gamma
   * @return x
   */
  private static final int comp(final int gamma) {
    int i, j, k;
    int Q = M - 1;

    if (gamma <= 0)
      return 0;

    i = (int) ((Q + .5d) - Math.sqrt(Q * Q - Q + 2.25 - (2 * gamma)));
    j = (gamma - (Q * (i - 1) - ((i * (i - 1)) / 2)) - 1);
    k = ((Q * (Q - 1)) >>> 1);

    if ((i & 1) == 1) {
      return (gamma - ((Q - 1) * (i >>> 1)) + ((i >>> 1) * (i >>> 1)));
    }

    return k - (Q * ((i >>> 1) - 1)) + (((i >>> 1) - 1) * ((i >>> 1) - 1))
        + ((i >>> 1) - 1) - j;
  }

  /**
   * the main routine
   * 
   * @param args
   *          the arguments
   */
  public static void main(final String[] args) {
    int[] map, d;
    int i;

    map = new int[M];
    for (i = (map.length - 1); i >= 0; i--)
      map[i] = i;

    for (i = 0; i <= ((M * (M - 1)) >>> 1) - M + 1; i++) {
      d = map.clone();
      permutate(i, d, M - 1);
      System.out.println(i + " - " + comp(i) + //$NON-NLS-1$
          ": " + TextUtils.toString(d) + //$NON-NLS-1$
          " - " + delta(d)); //$NON-NLS-1$
      System.out.flush();
    }
  }
}
