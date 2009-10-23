/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-18
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.lgp.base.genome.plain.Int2StringEditor.java
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

package org.dgpf.lgp.base.genome.plain;

import org.sigoa.refimpl.genomes.intString.IntStringEditor;
import org.sigoa.refimpl.genomes.string.StringCreator;
import org.sigoa.refimpl.genomes.string.StringEditor;
import org.sigoa.refimpl.genomes.string.VariableLengthStringMutator;
import org.sigoa.spec.go.reproduction.IMutator;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * The editor for two dimensional integer string genomes.
 * 
 * @author Thomas Weise
 */
public class Int2StringEditor extends StringEditor<int[][]> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the internal string creator
   */
  private final StringCreator<int[]> m_creator;

  /**
   * the internal string mutator
   */
  private final IMutator<int[]> m_mutator;
  /**
   * the internal editor
   */
  final IntStringEditor m_editor;

  /**
   * Instantiate a new string editor operation
   * 
   * @param minLength1
   *          the minimum length in genes of a genotype, where each gene is
   *          a <code>int[]</code>-array
   * @param maxLength1
   *          the maximum length in genes of a genotype, where each gene is
   *          a <code>int[]</code>-array
   * @param minLength2
   *          the minimum length in genes of a genotype, where each gene is
   *          a number of integers
   * @param maxLength2
   *          the maximum length in genes of a genotype, where each gene is
   *          a number of integers
   * @param geneSize2
   *          the number of integers forming a gene in the
   *          <code>int[]</code> arrays
   * @throws IllegalArgumentException
   *           if <code>minLength&lt;=0||maxLength&lt;minLength</code>
   */
  public Int2StringEditor(final int minLength1, final int maxLength1,
      final int minLength2, final int maxLength2, final int geneSize2) {
    super(1, minLength1, maxLength1);

    this.m_editor = new IntStringEditor(geneSize2, minLength2, maxLength2);

    this.m_creator = new StringCreator<int[]>(this.m_editor);
    this.m_mutator = new VariableLengthStringMutator<int[]>(this.m_editor);
  }

  /**
   * Copy a genotype
   * 
   * @param genotype
   *          the genotype
   * @return the copy of the genotype
   */
  @Override
  public int[][] copy(final int[][] genotype) {
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
  public int getLength(final int[][] genotype) {
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
  public int[][] createGenotype(final int length) {
    return new int[length][];
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
  public void storeRandomElements(final int[][] genotype, final int start,
      final int count, final IRandomizer random) {
    int i, s;

    for (i = count, s = start; i > 0; i--, s++) {
      genotype[s] = this.m_creator.create(random);
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
  public void changeElements(final int[][] genotype, final int start,
      final int count, final IRandomizer random) {
    int i, s;
    int[] d;

    for (i = count, s = start; i > 0; s++, i--) {
      d = genotype[s];
      if (d == null)
        d = this.m_creator.create(random);
      else
        d = this.m_mutator.mutate(d, random);

      genotype[s] = d;
    }
  }
}
