/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-12-20
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.io.binary.GrayCodedBitStringOutputStream.java
 * Last modification: 2007-10-22
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

package org.sfc.io.binary;

/**
 * A bit string output stream which utilizes gray coding.
 * 
 * @author Thomas Weise
 */
public class GrayCodedBinaryOutputStream extends BinaryOutputStream {

  /**
   * create a new gray coded bit string output stream
   */
  public GrayCodedBinaryOutputStream() {
    super();
  }

  /**
   * Write <code>n<code> bits into the output stream.
   * @param data  the data bits to be written
   * @param n the count of bits of <code>data</code> to be written
   */
  @Override
  public void writeBits(final int data, final int n) {
    int i, shift;
    boolean b, c;

    shift = (1 << (n - 1));
    b = ((data & shift) != 0);
    i = (b ? shift : 0);

    do {
      shift >>>= 1;
      c = ((data & shift) != 0);
      if (b ^ c) {
        i |= shift;
      }

      b = c;
    } while ((shift & 0xfffffffe) != 0);
    // (shift != 1);

    super.writeBits(i, n);
  }
}
