/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-18
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.intString.IntStringEditor.java
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

package org.sigoa.refimpl.genomes.intString;

import org.sigoa.refimpl.genomes.string.StringEditor;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * This is the editor for integer strings.
 * 
 * @author Thomas Weise
 */
public class IntStringEditor extends StringEditor<int[]> {
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
  public IntStringEditor(final int granularity, final int minLength,
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
  public int[] copy(final int[] genotype) {
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
  public int getLength(final int[] genotype) {
    return (genotype != null) ? genotype.length : 0;
  }

  /**
   * Create a new, uninitialized string of the given length (in elements).
   * 
   * @param length
   *          the length of the string to be created
   * @return the new string
   */
  @Override
  public int[] createGenotype(final int length) {
    return new int[length];
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
  public void storeRandomElements(final int[] genotype, final int start,
      final int count, final IRandomizer random) {
    int i, s;

    for (i = count, s = start; i > 0; s++, i--) {
      genotype[s] = random.nextInt();
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
  public void changeElements(final int[] genotype, final int start,
      final int count, final IRandomizer random) {

    int i, s;

    switch (random.nextInt(3)) {

    case 1: {
      this.storeRandomElements(genotype, start, count, random);
      return;
    }

      // case 1: {
      // //BinaryMath.toggleBits(genotype, start << 5, count << 5);
      // for(i=(start+count-1);i>=start;i--){
      // genotype[i]=~genotype[i];
      // }
      // return;
      // }

    case 0: {
      for (s = start, i = count; i > 0; s++, i--) {
        genotype[s] ^= (1 << random.nextInt(32));
      }
      return;
    }

    default: {
      for (s = start, i = count; i > 0; s++, i--) {
        if (random.nextBoolean())
          genotype[s]++;
        else
          genotype[s]--;
      }
      return;
    }

    }

  }

}
