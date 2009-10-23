/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-29
 * Creator          : Thomas Weise
 * Original Filename: test.Dummy.java
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

package test.org.sfc.math;

import org.sfc.math.BinaryMath;
import org.sigoa.refimpl.stoch.Randomizer;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * the dummy test class
 * 
 * @author Thomas Weise
 */
public class BinaryMathMoveBits {

  /**
   * the main routine
   * 
   * @param args
   *          the arguments
   */
  public static void main(final String[] args) {
    byte[] b1, b2, b3;
    final int maxLen, maxBits;
    final IRandomizer rand;
    int test, ss, ds, l, i;

    maxLen = 1000;
    maxBits = (maxLen << 3);
    b1 = new byte[maxLen];
    b2 = new byte[maxLen];
    b3 = new byte[maxLen];
    rand = new Randomizer();

    for (test = 10000; test >= 0; test--) {
      rand.nextBytes(b1);
      rand.nextBytes(b2);
      rand.nextBytes(b3);

      l = rand.nextInt(maxBits);
      ss = rand.nextInt(maxBits - l);
      ds = rand.nextInt(maxBits - l);

      BinaryMath.moveBits(b1, ss, b2, ds, l);
      BinaryMath.moveBits(b2, ds, b3, ss, l);

      for (i = (ss + l - 1); i >= ss; i--) {
        if (BinaryMath.getBit(b1, i) != BinaryMath.getBit(b3, i)) {
          System.out.println("error"); //$NON-NLS-1$
        }
      }
    }

    for (test = 10000; test >= 0; test--) {
      rand.nextBytes(b1);
      System.arraycopy(b1, 0, b2, 0, maxLen);

      l = rand.nextInt(maxBits);
      ss = rand.nextInt(maxBits - l);
      ds = rand.nextInt(maxBits - l);

      BinaryMath.moveBits(b1, ss, b1, ds, l);

      for (i = 0; i <l; i++) {
        if (BinaryMath.getBit(b1, ds+i) != BinaryMath.getBit(b2, ss+i)) {
          System.out.println("error"); //$NON-NLS-1$
        }
      }
    }
    
  }

}
