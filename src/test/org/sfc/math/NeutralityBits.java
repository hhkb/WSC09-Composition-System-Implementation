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
public class NeutralityBits {

  /**
   * @param bitSize
   * @param input
   * @param output
   * @param neutralityCount
   * @return dasf
   */
  public static int neutrality2(final byte[] input, final byte[] output,
      final int bitSize, final int neutralityCount) {
    int outputIndex, i, majorityValue, shiftCount, outputValue, outputValueIndex;
    byte currentByte;

    outputIndex = 0;
    i = 0;
    majorityValue = 0;
    shiftCount = 0;
    outputValue = 0;
    outputValueIndex = 0;

    currentByte = 0;

    for (i = 0; i < bitSize; ++i) {
      if ((i & 7) == 0)
        currentByte = input[(i >>> 3)];

      if ((currentByte & 1) == 0)
        --majorityValue;
      else
        ++majorityValue;

      currentByte >>>= 1;

      ++shiftCount;

      if (shiftCount >= neutralityCount) {
        if (majorityValue >= 0)
          outputValue |= (1 << outputValueIndex);

        shiftCount = 0;
        majorityValue = 0;
        ++outputValueIndex;

        if (outputValueIndex >= 8) {
          output[outputIndex] = (byte) outputValue;
          ++outputIndex;
          outputValueIndex = 0;
          outputValue = 0;
        }
      }
    }

    if (shiftCount > 0) {
      if (majorityValue >= 0)
        outputValue |= (1 << outputValueIndex);
      ++outputValueIndex;
    }

    if (outputValueIndex > 0) {
      output[outputIndex] = (byte) outputValue;
    }

    return (outputIndex << 3) + outputValueIndex;
  }
  
  /**
   * Write the input array <code>in</code> to the output array
   * <code>out</code> while downsizing it to the degree of
   * <code>internNeutralityDifficultyLevel</code>.
   * 
   * @param in
   *          the input
   * @param out
   *          the output
   * @param size
   *          the number of bits
   *          @param mu
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
          o |= (1 << l);
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
        o |= (1 << l);
      l++;
    }
    if (l > 0) {
      out[z] = (byte) o;
    }

    return ((z << 3) + l);
  }

  /**
   * the main routine
   * 
   * @param args
   *          the arguments
   */
  public static void main(final String[] args) {
    byte[] b1, b2;
    final int maxLen, maxBits;
    final IRandomizer rand;
    int test, len, neutr, di, si, c1, v;
    boolean exp;

    maxLen = 1000;
    maxBits = (maxLen << 3);
    b1 = new byte[maxLen];
    b2 = new byte[maxLen];
    rand = new Randomizer();

    for (test = 100000; test >= 0; test--) {

      do {
        len = rand.nextInt(maxBits);
      } while (len <= 0);

      do {
        neutr = rand.nextInt(maxBits);
      } while (neutr <= 0);

      rand.nextBytes(b1);
      rand.nextBytes(b2);
      neutrality(b1, b2, len, neutr);
      c1 = 0;
      v = 0;
      di = 0;
      for (si = 0; si < len; si++) {
        if (BinaryMath.getBit(b1, si))
          c1++;
        else
          c1--;

        v++;
        if (v >= neutr) {
          v = 0;
          exp = (c1 >= 0);
          c1 = 0;
          if (exp != BinaryMath.getBit(b2, di++)) {
            System.out.println("error"); //$NON-NLS-1$
          }
        }

      }

      if (v > 0) {
        exp = (c1 >= 0);
        if (exp != BinaryMath.getBit(b2, di++)) {
          System.out.println("error"); //$NON-NLS-1$
        }
      }
    }

  }

}
