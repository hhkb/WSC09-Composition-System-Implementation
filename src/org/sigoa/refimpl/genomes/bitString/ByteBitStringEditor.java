/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-18
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.bitString.ByteBitStringEditor.java
 * Last modification: 2007-11-18
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

package org.sigoa.refimpl.genomes.bitString;

import org.sfc.math.BinaryMath;
import org.sigoa.refimpl.genomes.string.StringEditor;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * This is the correct editor class for binary strings which are stored in
 * byte arrays.
 * 
 * @author Thomas Weise
 */
public class ByteBitStringEditor extends StringEditor<byte[]> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * Instantiate a new string editor operation
   * 
   * @param granularity
   *          the number of elements a gene of the genotype consists of
   * @param minLength
   *          the minimum length in genes of a genotype
   * @param maxLength
   *          the maximum length in genes of a genotype
   * @throws IllegalArgumentException
   *           if
   *           <code>granularity &lt;=0||minLength&lt;=0||maxLength&lt;minLength</code>
   */
  public ByteBitStringEditor(final int granularity, final int minLength,
      final int maxLength) {
    super(granularity, minLength, maxLength);
  }

  /**
   * Copy a genotype
   * 
   * @param genotype
   *          the genotype
   * @return the copy of the genotype
   */
  @Override
  public byte[] copy(final byte[] genotype) {
    return genotype.clone();
  }

  /**
   * Obtain the length of the genotype in elements.
   * 
   * @param genotype
   *          the genotype
   * @return its length, or 0 if the length could not be determined
   */
  @Override
  public int getLength(final byte[] genotype) {
    return ((genotype != null) ? (genotype.length << 3) : 0);
  }

  /**
   * Create a new, uninitialized string of the given length (in elements).
   * 
   * @param length
   *          the length of the string to be created
   * @return the new string
   */
  @Override
  public byte[] createGenotype(final int length) {
    int l;

    l = (length >>> 3);
    if ((l << 3) != length)
      l++;
    return new byte[l];
  }

  /**
   * Store <code>count</code> random elements into <code>genotype</code>
   * beginning at position <code>start</code>.
   * 
   * @param genotype
   *          the genotype
   * @param start
   *          the start index
   * @param count
   *          the number of elements to change
   * @param random
   *          the randomizer
   */
  @Override
  public void storeRandomElements(final byte[] genotype, final int start,
      final int count, final IRandomizer random) {
    int i, s;

    s = start;
    for (i = count; i > 0; i--, s++) {
      BinaryMath.setBit(genotype, s, random.nextBoolean());
    }
  }

  /**
   * Change <code>count</code> elements in <code>genotype</code>
   * beginning at position <code>start</code>.
   * 
   * @param genotype
   *          the genotype
   * @param start
   *          the start index
   * @param count
   *          the number of elements to change
   * @param random
   *          the randomizer
   */
  @Override
  public void changeElements(final byte[] genotype, final int start,
      final int count, final IRandomizer random) {
    BinaryMath.toggleBits(genotype, start, count);
  }

  /**
   * Copy <code>count</code> elements beginning with the one at index
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
  @Override
  public void moveElements(final byte[] source, final int sourceIdx,
      final byte[] dest, final int destIdx, final int count) {
    BinaryMath.moveBits(source, sourceIdx, dest, destIdx, count);
  }
}
