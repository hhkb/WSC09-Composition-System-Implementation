/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-17
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.math.BinaryMath.java
 * Last modification: 2007-11-17
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

package org.sfc.math;

import org.sfc.utils.ErrorUtils;

/**
 * A class for binary mathematics
 * 
 * @author Thomas Weise
 */
public final class BinaryMath {

  /**
   * the forbidden constructor
   */
  private BinaryMath() {
    ErrorUtils.doNotCall();
  }

  // byte[] as base

  /**
   * Obtain the value of the bit at position <code>index</code> in
   * <code>data</code>.
   * 
   * @param index
   *          the position
   * @param data
   *          the data array
   * @return the value of the bit
   */
  public static final boolean getBit(final byte[] data, final int index) {
    return ((data[index >>> 3] & (1 << (index & 7))) != 0);
  }

  /**
   * Set the value of the bit at position <code>index</code> in
   * <code>data</code>.
   * 
   * @param index
   *          the position
   * @param data
   *          the data array
   * @param value
   *          the new value of the bit
   */
  public static final void setBit(final byte[] data, final int index,
      final boolean value) {

    if (value)
      data[index >>> 3] |= (1 << (index & 7));
    else
      data[index >>> 3] &= (~(1 << (index & 7)));
  }

  /**
   * Toggle the value of the bit at position <code>index</code> in
   * <code>data</code>.
   * 
   * @param index
   *          the position
   * @param data
   *          the data array
   */
  public static final void toggleBit(final byte[] data, final int index) {
    data[index >>> 3] ^= (1 << (index & 7));
  }

  /**
   * Toggle the value of <code>count</code> bits starting at position
   * <code>index</code> in <code>data</code>.
   * 
   * @param index
   *          the position
   * @param count
   *          the number of bits to be toggled
   * @param data
   *          the data array
   */
  public static final void toggleBits(final byte[] data, final int index,
      final int count) {

    int c, sib, si7, sb;

    if (count <= 0)
      return;

    sib = (index >>> 3);

    si7 = (1 << (index & 7));

    sb = data[sib];

    for (c = (count - 1); c >= 0; c--) {
      sb ^= si7;

      if ((si7 <<= 1) == 256) {
        si7 = 1;
        data[sib] = (byte) sb;

        if (c <= 0)
          return;
        sib++;
        sb = data[sib];
      }
    }
    data[sib] = (byte) sb;

  }

  /**
   * Copy <code>count</code> bits beginning with the one at index
   * <code>sourceIdx</code> to the position <code>destIdx</code> in
   * <code>dest</code>.
   * 
   * @param source
   *          the source
   * @param sourceIdx
   *          the source index
   * @param dest
   *          the destination
   * @param destIdx
   *          the destination index
   * @param count
   *          the number of elements to copy
   */
  public static final void moveBits(final byte[] source,
      final int sourceIdx, final byte[] dest, final int destIdx,
      final int count) {

    int c, sib, dib, di7, si7, sb, db;

    if (count <= 0)
      return;

    if (sourceIdx > destIdx) {

      sib = (sourceIdx >>> 3);
      dib = (destIdx >>> 3);

      si7 = (1 << (sourceIdx & 7));
      di7 = (1 << (destIdx & 7));

      sb = source[sib];
      db = dest[dib];

      for (c = (count - 1); c >= 0; c--) {
        if ((sb & si7) == 0)
          db &= (~di7);
        else
          db |= di7;

        if ((di7 <<= 1) == 256) {
          di7 = 1;
          dest[dib] = (byte) db;
          if (c <= 0)
            return;
          dib++;
          db = dest[dib];
        }

        if ((si7 <<= 1) == 256) {
          si7 = 1;
          if (c <= 0)
            break;
          sib++;
          sb = source[sib];
        }

      }
      dest[dib] = (byte) db;
    }

    else {

      sib = (sourceIdx + count - 1);
      dib = (destIdx + count - 1);

      si7 = (1 << (sib & 7));
      di7 = (1 << (dib & 7));
      sib >>>= 3;
      dib >>>= 3;
      sb = source[sib];
      db = dest[dib];

      for (c = (count - 1); c >= 0; c--) {
        if ((sb & si7) == 0)
          db &= (~di7);
        else
          db |= di7;

        if ((di7 >>>= 1) == 0) {
          di7 = 128;
          dest[dib] = (byte) db;
          if (c <= 0)
            return;
          dib--;
          db = dest[dib];
        }

        if ((si7 >>>= 1) == 0) {
          si7 = 128;
          if (c <= 0)
            break;
          sib--;// if((--sib)<0) return;
          sb = source[sib];
        }
      }
      dest[dib] = (byte) db;

    }
  }

  /**
   * Count the value of "one" bits.
   * 
   * @param data
   *          the array with the data
   * @return the number of bits with value 1
   */
  private static final int countOneBitsInternal(final byte[] data) {
    int i, s, d;

    s = 0;
    for (i = (data.length - 1); i >= 0; i--) {
      d = data[i];
      while (d != 0) {
        s += (d & 1);
        d >>>= 1;
      }
    }

    return s;
  }

  /**
   * The matrix with the one bits
   */
  private static final int[] ONE_MATRIX = new int[256];
  static {
    int i;
    byte[] b;

    b = new byte[1];

    for (i = 255; i >= 0; i--) {
      b[0] = (byte) i;
      ONE_MATRIX[i] = countOneBitsInternal(b);
    }
  }

  /**
   * Count the number of "one" bits.
   * 
   * @param data
   *          the array with the data
   * @return the number of bits with value 1
   */
  public static final int countOneBits(final byte[] data) {
    int i, s;

    s = 0;
    for (i = (data.length - 1); i >= 0; i--) {
      s += ONE_MATRIX[data[i] & 0xff];
    }

    return s;
  }

  /**
   * Count the number of "zero" bits.
   * 
   * @param data
   *          the array with the data
   * @return the number of bits with value 0
   */
  public static final int countZeroBits(final byte[] data) {
    return (data.length << 3) - countOneBits(data);
  }

  // / int[] as base

  /**
   * Obtain the value of the bit at position <code>index</code> in
   * <code>data</code>.
   * 
   * @param index
   *          the position
   * @param data
   *          the data array
   * @return the value of the bit
   */
  public static final boolean getBit(final int[] data, final int index) {
    return ((data[index >>> 5] & (1 << (index & 31))) != 0);
  }

  /**
   * Set the value of the bit at position <code>index</code> in
   * <code>data</code>.
   * 
   * @param index
   *          the position
   * @param data
   *          the data array
   * @param value
   *          the new value of the bit
   */
  public static final void setBit(final int[] data, final int index,
      final boolean value) {

    if (value)
      data[index >>> 5] |= (1 << (index & 31));
    else
      data[index >>> 5] &= (~(1 << (index & 31)));
  }

  /**
   * Toggle the value of the bit at position <code>index</code> in
   * <code>data</code>.
   * 
   * @param index
   *          the position
   * @param data
   *          the data array
   */
  public static final void toggleBit(final int[] data, final int index) {
    data[index >>> 5] ^= (1 << (index & 31));
  }

  /**
   * Toggle the value of <code>count</code> bits starting at position
   * <code>index</code> in <code>data</code>.
   * 
   * @param index
   *          the position
   * @param count
   *          the number of bits to be toggled
   * @param data
   *          the data array
   */
  public static final void toggleBits(final int[] data, final int index,
      final int count) {

    int c, sib, si7, sb;

    if (count <= 0)
      return;

    sib = (index >>> 5);

    si7 = (1 << (index & 31));

    sb = data[sib];

    for (c = (count - 1); c >= 0; c--) {
      sb ^= si7;

      if ((si7 <<= 1) == 0) {
        si7 = 1;
        data[sib] = sb;

        if (c <= 0)
          return;
        sib++;
        sb = data[sib];
      }
    }
    data[sib] = sb;

  }

  /**
   * Copy <code>count</code> bits beginning with the one at index
   * <code>sourceIdx</code> to the position <code>destIdx</code> in
   * <code>dest</code>.
   * 
   * @param source
   *          the source
   * @param sourceIdx
   *          the source index
   * @param dest
   *          the destination
   * @param destIdx
   *          the destination index
   * @param count
   *          the number of elements to copy
   */
  public static final void moveBits(final int[] source,
      final int sourceIdx, final int[] dest, final int destIdx,
      final int count) {

    int c, sib, dib, di7, si7, sb, db;

    if (count <= 0)
      return;

    if (sourceIdx > destIdx) {

      sib = (sourceIdx >>> 5);
      dib = (destIdx >>> 5);

      si7 = (1 << (sourceIdx & 31));
      di7 = (1 << (destIdx & 31));

      sb = source[sib];
      db = dest[dib];

      for (c = (count - 1); c >= 0; c--) {
        if ((sb & si7) == 0)
          db &= (~di7);
        else
          db |= di7;

        if ((di7 <<= 1) == 0) {
          di7 = 1;
          dest[dib] = db;
          if (c <= 0)
            return;
          dib++;
          db = dest[dib];
        }

        if ((si7 <<= 1) == 0) {
          si7 = 1;

          if (c <= 0)
            break;
          sib++;
          sb = source[sib];
        }

      }
      dest[dib] = db;
    }

    else {

      sib = (sourceIdx + count - 1);
      dib = (destIdx + count - 1);

      si7 = (1 << (sib & 31));
      di7 = (1 << (dib & 31));
      sib >>>= 5;
      dib >>>= 5;
      sb = source[sib];
      db = dest[dib];

      for (c = (count - 1); c >= 0; c--) {
        if ((sb & si7) == 0)
          db &= (~di7);
        else
          db |= di7;

        if ((di7 >>>= 1) == 0) {
          di7 = 0x80000000;
          dest[dib] = db;
          if (c <= 0)
            return;
          dib--;
          db = dest[dib];
        }

        if ((si7 >>>= 1) == 0) {
          si7 = 0x80000000;

          if (c <= 0)
            break;
          sib--;
          sb = source[sib];
        }

      }
      dest[dib] = db;

    }
  }

  /**
   * Count the value of "one" bits.
   * 
   * @param data
   *          the array with the data
   * @return the number of bits with value 1
   */
  public static final int countOneBits(final int[] data) {
    int i, s, d;

    s = 0;
    for (i = (data.length - 1); i >= 0; i--) {
      d = data[i];
      while (d != 0) {
        s += (d & 1);
        d >>>= 1;
      }
    }

    return s;
  }

  /**
   * Count the number of "zero" bits.
   * 
   * @param data
   *          the array with the data
   * @return the number of bits with value 0
   */
  public static final int countZeroBits(final int[] data) {
    return (data.length << 5) - countOneBits(data);
  }

  /**
   * Obtain the number of bits needed to store the number <code>val</code>.
   * 
   * @param val
   *          the value
   * @return the number of bits needed to store <code>val</code>
   */
  public static final int getBits(final int val) {
    int v, i;

    if (val == 0)
      return 1;
    if (val < 0)
      return 32;

    for (v = (val - 1), i = 0; v != 0; i++, v >>>= 1) {
      //
    }

    return ((val == (1 << i)) ? i : (i + 1));
  }
}
