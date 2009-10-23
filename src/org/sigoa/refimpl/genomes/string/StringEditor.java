/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-17
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.string.StringEditor.java
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

package org.sigoa.refimpl.genomes.string;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;

import org.sfc.utils.Classes;
import org.sigoa.refimpl.go.ImplementationBase;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * This class represents the base for all string-chromosome reproduction
 * operations
 * 
 * @param <G>
 *          the genotype
 * @author Thomas Weise
 */
public abstract class StringEditor<G extends Serializable> extends
    ImplementationBase<G, Serializable> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the number of elements a gene of the genotype consists of
   */
  final int m_granularity;

  /**
   * the minimum length in genes of a genotype
   */
  final int m_minLength;

  /**
   * the maximum length in genes of a genotype
   */
  final int m_maxLength;

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
  protected StringEditor(final int granularity, final int minLength,
      final int maxLength) {
    super();
    if ((granularity <= 0) || (minLength <= 0) || (maxLength < minLength))
      throw new IllegalArgumentException();
    this.m_granularity = granularity;
    this.m_minLength = minLength;
    this.m_maxLength = maxLength;
  }

  /**
   * the number of elements a gene of the genotype consists of
   * 
   * @return the number of elements a gene of the genotype consists of
   */
  public int getGranularity() {
    return this.m_granularity;
  }

  /**
   * Obtain the minimum length of new genotypes in genes
   * 
   * @return the minimum length of new genotypes in genes
   */
  public int getMinLength() {
    return this.m_minLength;
  }

  /**
   * Obtain the maximum length of new genotypes in genes
   * 
   * @return the maximum length of new genotypes in genes
   */
  public int getMaxLength() {
    return this.m_maxLength;
  }

  /**
   * Copy a genotype
   * 
   * @param genotype
   *          the genotype
   * @return the copy of the genotype
   */
  public G copy(final G genotype) {
    return Classes.clone(genotype);
  }

  /**
   * Obtain the length of the genotype in elements.
   * 
   * @param genotype
   *          the genotype
   * @return its length, or 0 if the length could not be determined
   */
  public int getLength(final G genotype) {
    if (genotype.getClass().isArray()) {
      return Array.getLength(genotype);
    }
    if (genotype instanceof Collection)
      return ((Collection<?>) genotype).size();
    return 0;
  }

  /**
   * Create a new, uninitialized string of the given length (in elements).
   * 
   * @param length
   *          the length of the string to be created
   * @return the new string
   */
  public abstract G createGenotype(final int length);

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
  public abstract void storeRandomElements(final G genotype,
      final int start, final int count, final IRandomizer random);

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
  public void changeElements(final G genotype, final int start,
      final int count, final IRandomizer random) {
    this.storeRandomElements(genotype, start, count, random);
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
  public void moveElements(final G source, final int sourceIdx,
      final G dest, final int destIdx, final int count) {
    System.arraycopy(source, sourceIdx, dest, destIdx, count);
  }
}
