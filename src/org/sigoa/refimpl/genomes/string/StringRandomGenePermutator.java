/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-16
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.string.StringRandomGeneMutator.java
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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import org.sigoa.spec.go.reproduction.IMutator;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * This mutator exchanges the positions of two single genes in the string.
 * 
 * @param <G>
 *          the genotype
 * @author Thomas Weise
 */
public class StringRandomGenePermutator<G extends Serializable> extends
    StringReproductionOperator<G> implements IMutator<G> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the temporary genotype
   */
  private transient G m_temp;

  /**
   * the maximum allowed permutations
   */
  private final int m_maxPermutations;

  /**
   * Instantiate a new string reproduction operation
   * 
   * @param editor
   *          the string editor
   * @param maxPermutations
   *          the maximum allowed permutations
   * @throws IllegalArgumentException
   *           if <code>maxPermutations&lt;1</code>
   */
  public StringRandomGenePermutator(final StringEditor<G> editor,
      final int maxPermutations) {
    super(editor);
    if (maxPermutations <= 0)
      throw new IllegalArgumentException();
    this.m_maxPermutations = maxPermutations;
    this.init();
  }

  /**
   * Instantiate a new string reproduction operation
   * 
   * @param editor
   *          the string editor
   */
  public StringRandomGenePermutator(final StringEditor<G> editor) {
    this(editor, Integer.MAX_VALUE);
  }

  /**
   * Perform one single mutation.
   * 
   * @param source
   *          The source genotype.
   * @param random
   *          The randomizer to be used.
   * @return The resulting genotype.
   * @throws NullPointerException
   *           if <code>source==null||random==null</code>.
   */
  public synchronized G mutate(final G source, final IRandomizer random) {
    G g;
    int l, i, j, v, c;
    StringEditor<G> ed;

    ed = this.m_editor;

    l = ed.getLength(source);
    v = ed.m_granularity;
    l /= v;
    if (l <= 1)
      return source;

    g = ed.copy(source);

    for (c = Math.min(this.m_maxPermutations, (int) (random
        .nextExponential(0.1))); c >= 0; c--) {
      i = random.nextInt(l);
      do {
        j = random.nextInt(l);
      } while (j == i);

      ed.moveElements(g, i, this.m_temp, 0, v);
      ed.moveElements(g, j, g, i, v);
      ed.moveElements(this.m_temp, 0, g, j, v);
    }

    return g;
  }

  /**
   * initialize this object
   */
  private final void init() {
    this.m_temp = this.m_editor
        .createGenotype(this.m_editor.m_granularity);
  }

  /**
   * Deserialize the crossover.
   * 
   * @param stream
   *          The stream to deserialize from.
   * @throws IOException
   *           if io fucks up.
   * @throws ClassNotFoundException
   *           if the class could not be found.
   */
  private final void readObject(final ObjectInputStream stream)
      throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    this.init();
  }
}
