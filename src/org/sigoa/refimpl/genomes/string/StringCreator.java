/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-16
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.string.StringCreator.java
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

import org.sigoa.spec.go.reproduction.ICreator;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * a creator able to create bit strings
 * 
 * @param <G>
 *          the genotype
 * @author Thomas Weise
 */
public class StringCreator<G extends Serializable> extends
    StringReproductionOperator<G> implements ICreator<G> {

  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * Instantiate a new string reproduction operation
   * 
   * @param editor
   *          the string editor
   */
  public StringCreator(final StringEditor<G> editor) {
    super(editor);
  }

  /**
   * Compute the length of a new genotype.
   * 
   * @param random
   *          a randomizer which is maybe useful
   * @return the length of the bit string to be created (in genes)
   */
  protected int getNewLength(final IRandomizer random) {
    int i, l, k;
    double d;

    k = this.m_editor.m_minLength;
    l = (this.m_editor.m_maxLength - k);
    d = Math.sqrt(l);

    do {
      i = (int) ((d * random.nextDouble()) + random.nextExponential(0.3d));
    } while (i > l);

    return (i + k);
  }

  /**
   * Create a single new random genotype
   * 
   * @param random
   *          The randomizer to be used.
   * @return The resulting genotype.
   * @throws NullPointerException
   *           if <code>random==null</code>.
   */
  public G create(final IRandomizer random) {
    int l;
    G g;

    l = (this.getNewLength(random) * this.m_editor.m_granularity);
    g = this.m_editor.createGenotype(l);
    this.m_editor.storeRandomElements(g, 0, l, random);
    return g;
  }
}
