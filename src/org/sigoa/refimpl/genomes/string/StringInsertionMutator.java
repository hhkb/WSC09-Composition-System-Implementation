/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-16
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.string.StringInsertionMutator.java
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

import org.sigoa.spec.go.reproduction.IMutator;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * This mutatorinserts some new genes into a string.
 * 
 * @param <G>
 *          the genotype
 * @author Thomas Weise
 */
public class StringInsertionMutator<G extends Serializable> extends
    StringReproductionOperator<G> implements IMutator<G> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the maximum number of genes to be inserted
   */
  final int m_maxInserts;

  /**
   * Instantiate a new string reproduction operation
   * 
   * @param editor
   *          the string editor
   * @param maxInserts
   *          the maximum number of genes to be inserted
   * @throws IllegalArgumentException
   *           if <code>maxInserts&lt;1</code>
   */
  public StringInsertionMutator(final StringEditor<G> editor,
      final int maxInserts) {
    super(editor);
    if (maxInserts < 1)
      throw new IllegalArgumentException();
    this.m_maxInserts = maxInserts;
  }

  /**
   * Instantiate a new string reproduction operation
   * 
   * @param editor
   *          the string editor
   * @throws IllegalArgumentException
   *           if <code>maxInserts&lt;1</code>
   */
  public StringInsertionMutator(final StringEditor<G> editor) {
    this(editor, Math.min(400, editor.m_maxLength));
  }

  /**
   * Obtain the maximum number of genes to be inserted
   * 
   * @return the maximum number of genes to be inserted
   */
  public int getMaxInserts() {
    return this.m_maxInserts;
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
  public G mutate(final G source, final IRandomizer random) {
    int l, c, p, gg;
    final StringEditor<G> e;
    final G g;

    e = this.m_editor;
    gg = e.m_granularity;
    l = (e.getLength(source) / gg);

    if (l >= e.m_maxLength)
      return source;

    do {
      c = (1 + (int) (0.5d + random.nextExponential(1d)));
    } while (((l + c) > e.m_maxLength) || (c > this.m_maxInserts));

    p = random.nextInt(l + 1);
    l *= gg;
    c *= gg;
    p *= gg;
    g = e.createGenotype(l + c);
    e.moveElements(source, 0, g, 0, p);
    e.storeRandomElements(g, p, c, random);
    e.moveElements(source, p, g, p + c, l - p);

    return g;
  }
}
