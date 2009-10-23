/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-16
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.string.StringDeletionMutator.java
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
 * This mutator deletes some genes from a string.
 * 
 * @param <G>
 *          the genotype
 * @author Thomas Weise
 */
public class StringDeletionMutator<G extends Serializable> extends
    StringReproductionOperator<G> implements IMutator<G> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the maximum number of genes to be deleted
   */
  final int m_maxDeletions;

  /**
   * Instantiate a new string reproduction operation
   * 
   * @param editor
   *          the string editor
   * @param maxDeletions
   *          the maximum number of genes to be deleted
   * @throws IllegalArgumentException
   *           if <code>maxInserts&lt;1</code>
   */
  public StringDeletionMutator(final StringEditor<G> editor,
      final int maxDeletions) {
    super(editor);
    if (maxDeletions < 1)
      throw new IllegalArgumentException();
    this.m_maxDeletions = maxDeletions;
  }

  /**
   * Instantiate a new string reproduction operation
   * 
   * @param editor
   *          the string editor
   * @throws IllegalArgumentException
   *           if <code>maxInserts&lt;1</code>
   */
  public StringDeletionMutator(final StringEditor<G> editor) {
    this(editor, Math.min(400, editor.m_maxLength - 1));
  }

  /**
   * Obtain the maximum number of genes to be deleted
   * 
   * @return the maximum number of genes to be deleted
   */
  public int getMaxDeletions() {
    return this.m_maxDeletions;
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

    if (l <= 1)
      return source;

    do {
      c = (1 + (int) (0.5d + random.nextExponential(1d)));
    } while (((l - c) < 1) || (c > this.m_maxDeletions));

    p = random.nextInt(l - c);
    l *= gg;
    c *= gg;
    p *= gg;
    g = e.createGenotype(l - c);
    e.moveElements(source, 0, g, 0, p);
    e.moveElements(source, p + c, g, p, l - p - c);

    return g;
  }
}
