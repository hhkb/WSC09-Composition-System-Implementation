/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-16
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.string.VariableLengthhStringCrossover.java
 * Last modification: 2007-11-16
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

import org.sigoa.spec.stoch.IRandomizer;

/**
 * This crossover operation performes a crossover
 * 
 * @param <G>
 *          the genotype
 * @author Thomas Weise
 */
public class VariableLengthStringNPointCrossover<G extends Serializable>
    extends StringCrossover<G> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the crossover point coordinates
   */
  transient int[] m_points2;

  /**
   * Instantiate a new string reproduction operation
   * 
   * @param editor
   *          the string editor
   * @param maxPoints
   *          the maximum number of crossover points
   * @throws IllegalArgumentException
   *           if <code>maxPoints&lt;1</code>
   */
  public VariableLengthStringNPointCrossover(final StringEditor<G> editor,
      final int maxPoints) {
    super(editor, maxPoints);
  }

  /**
   * Instantiate a new string reproduction operation
   * 
   * @param editor
   *          the string editor
   * @throws IllegalArgumentException
   *           if <code>maxPoints&lt;1</code>
   */
  public VariableLengthStringNPointCrossover(final StringEditor<G> editor) {
    this(editor, Math.min(400, editor.m_maxLength - 1));
  }

  /**
   * initialize this object
   * 
   * @param maxPoints
   *          the count of coordinates needed
   */
  @Override
  void init(final int maxPoints) {
    super.init(maxPoints);
    this.m_points2 = new int[maxPoints + 1];
  }

  /**
   * Perform one single recombination/crossover.
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
  public G crossover(final G source1, final G source2,
      final IRandomizer random) {
    final StringEditor<G> e;
    int l1, l2, i, j, ot1, f1, ot2, f2, gg, nl, v;
    G g;
    int[] sp1, sp2;
    boolean b;

    e = this.m_editor;
    gg = e.m_granularity;

    l1 = e.getLength(source1) / gg;
    l2 = e.getLength(source2) / gg;

    if (l1 <= 1)
      return source2;
    if (l2 <= 1)
      return source1;

    sp1 = this.m_points;
    sp2 = this.m_points2;
    v = 1000;
    for (;;) {
      i = this.getSplitCount(random, Math
          .min(sp1.length, Math.min(l1, l2)) - 1);
      this.getCrossoverPoints(l1, random, sp1, i);
      this.getCrossoverPoints(l2, random, sp2, i);
      // compute the length of the new genotype in genes
      b = true;
      nl = 0;
      ot1 = l1;
      ot2 = l2;
      for (j = i; j >= 0; j--) {
        f1 = sp1[j];
        f2 = sp2[j];

        if (b)
          nl += (ot1 - f1);
        else
          nl += (ot2 - f2);

        ot1 = f1;
        ot2 = f2;
        b = !b;
      }

      if ((nl >= this.m_editor.m_minLength)
          && (nl <= this.m_editor.m_maxLength))
        break;
      if ((--v) <= 0)
        return (random.nextBoolean() ? source1 : source2);
    }

    nl *= gg;
    g = e.createGenotype(nl);

    // perform the crossover
    b = true;
    ot1 = l1 * gg;
    ot2 = l2 * gg;
    for (; i >= 0; i--) {
      f1 = sp1[i] * gg;
      f2 = sp2[i] * gg;

      if (b) {
        j = (ot1 - f1);
        nl -= j;
        e.moveElements(source1, f1, g, nl, j);
        b = false;
      } else {
        j = (ot2 - f2);
        nl -= j;
        e.moveElements(source2, f2, g, nl, j);
        b = true;
      }

      ot1 = f1;
      ot2 = f2;
    }

    return g;
  }
}
