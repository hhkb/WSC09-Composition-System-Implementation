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
public class Example2 {

  /**
   * the r
   * 
   * @param gamma
   *          the gamma
   * @param f
   *          the f
   * @param m
   *          the m value
   * @return the result
   */
  private static final int r(final int gamma, final int m, final int f) {
    int a, b;

    if (gamma <= 0)
      return f;
    if (f <= 0)
      return 0;

    if (gamma > (m / 2)) {
      return r(gamma - 1, m, r(gamma - 2, m, (f + 1) % m + 1));// -1)%m+1
                                                                // ;
    }

    a = m / (gamma + 1);
    b = m / gamma;

    if ((f + a) % b == 0)
      return Math.min(m, (f + 1));

    if ((f + a) % b == 1)
      return Math.max(1, (f - 1));

    return f;
  }

  /**
   * the r
   * 
   * @param gamma
   *          the gamma
   * @param m
   *          the m value
   * @param f
   *          the f
   * @return the result
   */
  private static final int directR(final int gamma, final int m,
      final int f) {
    int a, b;

    if (gamma <= 0)
      return f;
    if (f <= 0)
      return 0;

    // //this should never be executed
    // if (gamma > (m / 2)) {
    // return directR(gamma - 1, m,directR(gamma - 2, m,(f + 1) % m +
    // 1));// -1)%m+1 ;
    // }

    a = m / (gamma + 1);
    b = m / gamma;

    if ((f + a) % b == 0)
      return Math.min(m, (f + 1));

    if ((f + a) % b == 1)
      return Math.max(1, (f - 1));

    return f;
  }

  /**
   * Create the r mapping table for a given gamma and m.
   * 
   * @param gamma
   *          the gamma
   * @param m
   *          the m value
   * @return the lookup table
   */
  private static final int[] createRLookupTable(final int gamma,
      final int m) {
    int max, i, j;
    int[] t1, t2, t3, ts;

    t1 = new int[m + 1];
    max = Math.min((m >>> 1), gamma);

    for (i = m; i > 0; i--) {
      t1[i] = directR(max, m, i);
    }

    if (max < gamma) {
      t2 = new int[m + 1];
      t3 = new int[m + 1];

      for (i = m; i > 0; i--) {
        t2[i] = directR(max - 1, m, i);
      }

      for (j = (max + 1); j <= gamma; j++) {
        ts = t3;
        t3 = t2;
        t2 = t1;
        t1 = ts;

        for (i = m; i > 0; i--) {
          t1[i] = t2[t3[(i + 1) % m + 1]];
        }
      }

    }

    return t1;
  }

  /**
   * the main routine
   * 
   * @param args
   *          the arguments
   */
  public static void main(final String[] args) {

    int i,g, m;// , j;

    g = 48;
    m = 30;
    // TextWriter t;

    for (i = 0; i <= m; i++) {
      System.out.println(i + ": " + r(g, m, i)); //$NON-NLS-1$
    }

    System.out.println(TextUtils.toString(createRLookupTable(g, m)));

    // t = new TextWriter("E:\\test.csv"); //$NON-NLS-1$
    //
    // for (j = 0; j <= M; j++) {
    // System.out.println(j);
    // System.out.flush();
    // t.ensureNewLine();
    // for (i = 0; i <= M*2; i++) {
    // t.writeInt(r(i, j));
    // t.writeCSVSeparator();
    // }
    //
    // }
    //
    // t.release();
  }
}
