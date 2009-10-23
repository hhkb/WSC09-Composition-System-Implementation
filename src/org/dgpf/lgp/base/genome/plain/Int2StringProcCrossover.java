/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-06-18
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.lgp.base.genome.plain.Int2StringProcCrossover.java
 * Last modification: 2008-06-18
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

import org.sigoa.refimpl.genomes.string.DefaultStringCrossover;
import org.sigoa.refimpl.go.reproduction.Crossover;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * the int2string
 * 
 * @author Thomas Weise
 */
public class Int2StringProcCrossover extends Crossover<int[][]> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the internal crossover
   */
  private final DefaultStringCrossover<int[]> m_cross;

  /**
   * Create a new multi-mutator.
   * 
   * @param editor
   *          the string editor to be used to process the genome
   * @param variableLength
   *          <code>true</code> if and only if the genome is of variable
   *          length, <code>false</code> for fixed length.
   */
  public Int2StringProcCrossover(final Int2StringEditor editor) {
    super();
    this.m_cross = new DefaultStringCrossover<int[]>(editor.m_editor, true);
  }

  /**
   * Perform one single recombination/crossover. This simple default
   * implementation just returns one of the two source genotypes chosen
   * randomly.
   * 
   * @param source1
   *          The first source genotype.
   * @param source2
   *          The second source genotype.
   * @param random
   *          The randomizer to be used.
   * @return The resulting genotype.
   * @throws NullPointerException
   *           if <code>source1==null||source2==null||random==null</code>.
   */
  @Override
  public int[][] crossover(final int[][] source1, final int[][] source2,
      final IRandomizer random) {
    int l;
    int[][] res;
    int[] x;

    l = Math.min(source1.length, source2.length);
    if (l <= 0)
      return super.crossover(source1, source2, random);

    res = (random.nextBoolean() ? source1.clone() : source2.clone());

    l = random.nextInt(l);
    x = this.m_cross.crossover(source1[l], source2[l], random);
    if (x == res[l])
      return super.crossover(source1, source2, random);
    res[l] = x;

    return res;
  }
}
