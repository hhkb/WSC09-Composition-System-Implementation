/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-12-19
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.io.binary.GrayCodedBitStringInputStream.java
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
 * An extension of the bit string input stream which uses gray coding.
 * 
 * @author Thomas Weise
 */
public class GrayCodedBinaryInputStream extends BinaryInputStream {
  /**
   * Create a new gray coded bit string input stream
   */
  public GrayCodedBinaryInputStream() {
    super();
  }

  /**
   * Read the next <code>n</code> bits (if possible).
   * <p>
   * All other read methods of this stream delegate their work to this
   * routine. If this method is overriden, in order to provide a gray
   * coding, for example, all other methods will also use the gray coding
   * automatically.
   * <p>
   * If the end of the data is reached, before all of the bits could be
   * read, the remaining bits are set to 0.
   * 
   * @param n
   *          the count of bits to read (<code>0 &lt;= n &lt;= 32</code>)
   * @return the next <code>n</code> bits
   * @throws IllegalArgumentException
   *           if <code>n &lt;0 || n &gt;32</code>
   */
  @Override
  public int readBits(final int n) {
    int i, shift, data;
    boolean b;

    data = super.readBits(n);
    if (data == 0)
      return 0;

    shift = (1 << (n - 1));
    b = ((data & shift) != 0);
    i = (b ? shift : 0);

    do {
      shift >>>= 1;

      b ^= ((data & shift) != 0);
      if (b) {
        i |= shift;
      }
    } while ((shift & 0xfffffffe) != 0);
    // (shift != 1);

    return i;
  }
}
