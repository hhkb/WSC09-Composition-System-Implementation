/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-28
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.selection.RandomSelectionR.java
 * Last modification: 2006-11-28
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

package org.sigoa.refimpl.go.selection;

import java.io.Serializable;

import org.sigoa.spec.go.IIndividual;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * The random selection with replacement.
 *
 * @param <G>
 *          The genotype.
 * @param <PP>
 *          The phenotype.
 * @author Thomas Weise
 */
public class RandomSelectionR<G extends Serializable, PP extends Serializable>
    extends SelectionAlgorithm<G, PP> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new random selection algorithm with replacement.
   *
   * @param selectionSize
   *          The initial selection size.
   * @throws IllegalArgumentException
   *           if <code>selectionSize&lt;=0</code>.
   */
  public RandomSelectionR(final int selectionSize) {
    super(true, selectionSize);
  }

  /**
   * Process the buffered population.
   *
   * @param source
   *          The buffered source population.
   * @param size
   *          The count of individuals in the population array.
   */
  @Override
  protected void process(final IIndividual<G, PP>[] source, final int size) {
    int i;
    IRandomizer r;

    r = this.getRandomizer();
    for (i = this.getPassThroughCount(); i > 0; i--) {
      this.output(source[r.nextInt(size)]);
    }
  }

}
