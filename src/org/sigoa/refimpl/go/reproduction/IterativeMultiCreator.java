/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-19
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.reproduction.IterativeMultiCreator.java
 * Last modification: 2006-12-19
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

package org.sigoa.refimpl.go.reproduction;

import java.io.Serializable;

import org.sigoa.refimpl.utils.IterativeSelector;
import org.sigoa.refimpl.utils.Selector;
import org.sigoa.spec.go.reproduction.ICreator;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * This class defines a creator the uses multiple other creators to
 * performs its work and delegates to them. If one of the other creators
 * does not work out, it just tries the next one.
 *
 * @param <G>
 *          The genotype.
 * @author Thomas Weise
 */
public class IterativeMultiCreator<G extends Serializable> extends
    MultiCreator<G> {

  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new multi-creator.
   *
   * @param data
   *          the objects to select from
   * @param weights
   *          their weights
   * @throws NullPointerException
   *           if <code>data==null || weights==null</code>
   * @throws IllegalArgumentException
   *           <code>data.length != weights.length || weights.length&lt;=0 </code>
   */
  public IterativeMultiCreator(final ICreator<? extends G>[] data,
      final double[] weights) {
    this(null, data, weights);
  }

  /**
   * Create a new creator by adding additional selectables to an existing
   * selector.
   *
   * @param inherit
   *          a selector to inherite from
   * @param data
   *          the objects to select from
   * @param weights
   *          their weights
   * @throws NullPointerException
   *           if <code>data==null || weights==null</code>
   * @throws IllegalArgumentException
   *           <code>data.length != weights.length || weights.length&lt;=0 </code>
   */
  public IterativeMultiCreator(
      final Selector<ICreator<? extends G>> inherit,
      final ICreator<? extends G>[] data, final double[] weights) {
    super(inherit, data, weights);
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
  @Override
  public G create(final IRandomizer random) {
    G g;
    IterativeSelector<ICreator<? extends G>>.Session s;
    int i;

    g = this.select(random).create(random);
    if (g != null)
      return g;

    s = this.openSession(random);
    for (i = s.getRemaining(); i > 0; i--) {
      g = s.next().create(random);
      if (g != null)
        break;
    }

    s.close();
    return g;
  }
}
