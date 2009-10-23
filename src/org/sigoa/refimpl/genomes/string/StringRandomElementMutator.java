/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-16
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.string.StringRandomElementMutator.java
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
 * This mutator changes the elements of a string at random positions.
 * 
 * @param <G>
 *          the genotype
 * @author Thomas Weise
 */
public class StringRandomElementMutator<G extends Serializable> extends
    StringElementMutator<G> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * Instantiate a new string reproduction operation
   * 
   * @param editor
   *          the string editor
   * @param maxChanges
   *          the maximum number of elements (not genes) to be changed
   * @throws IllegalArgumentException
   *           if <code>maxChanges&lt;1</code>
   */
  public StringRandomElementMutator(final StringEditor<G> editor,
      final int maxChanges) {
    super(editor, maxChanges);
  }

  /**
   * Instantiate a new string reproduction operation
   * 
   * @param editor
   *          the string editor
   * @throws IllegalArgumentException
   *           if <code>maxChanges&lt;1</code>
   */
  public StringRandomElementMutator(final StringEditor<G> editor) {
    this(editor, Math.min(400, editor.m_maxLength));
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
    G res;
    int i, l, mac;

    if ((source == null) || (random == null))
      throw new NullPointerException();

    l = this.m_editor.getLength(source);
    if (l < 1)
      return source;

    res = this.m_editor.copy(source);
    mac = this.m_maxChanges;

    do {
      i = (1 + (int) (0.5d + random.nextExponential(1.0d)));
    } while ((i > l) || (i > mac));

    for (; i > 0; i--) {
      this.m_editor.changeElements(res, random.nextInt(l), 1, random);
    }

    return res;
  }

}
