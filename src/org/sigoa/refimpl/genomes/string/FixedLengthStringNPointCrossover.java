/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-16
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.string.FixedLengthStringNPointCrossover.java
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
public class FixedLengthStringNPointCrossover<G extends Serializable>
    extends StringCrossover<G> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

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
  public FixedLengthStringNPointCrossover(final StringEditor<G> editor,
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
  public FixedLengthStringNPointCrossover(final StringEditor<G> editor) {
    this(editor, Math.min(400, editor.m_maxLength - 1));
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
  public synchronized G crossover(final G source1, final G source2,
      final IRandomizer random) {
    final StringEditor<G> e;
    int l, i, ot, f, gg;
    G g;
    int[] sp;
    boolean b;

    e = this.m_editor;
    gg = e.m_granularity;
    l = Math.min(e.getLength(source1), e.getLength(source2)) / gg;
    if (l <= 1)
      return (random.nextBoolean() ? source1 : source2);

    sp = this.m_points;
    i = this.getSplitCount(random, Math.min(sp.length, l) - 1);
    this.getCrossoverPoints(l, random, sp, i);

    ot = l * gg;
    g = e.createGenotype(ot);
    b = true;

    ot = l * gg;
    b = true;
    for (/*--i*/; i >= 0; i--) {
      f = sp[i] * gg;
      e.moveElements(b ? source1 : source2, f, g, f, ot - f);
      ot = f;
      b = (!b);
    }

    return g;
  }
}
