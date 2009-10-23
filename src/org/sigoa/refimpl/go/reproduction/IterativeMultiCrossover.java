/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-19
 * Crossover        : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.reproduction.IterativeMultiCrossover.java
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

import org.sfc.utils.Utils;
import org.sigoa.refimpl.utils.IterativeSelector;
import org.sigoa.refimpl.utils.Selector;
import org.sigoa.spec.go.reproduction.ICrossover;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * This class defines a crossover the uses multiple other crossovers to
 * performs its work and delegates to them. If one of the other crossover
 * instances does not work out, it just tries the next one.
 *
 * @param <G>
 *          The genotype.
 * @author Thomas Weise
 */
public class IterativeMultiCrossover<G extends Serializable> extends
    MultiCrossover<G> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new multi-crossover.
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
  public IterativeMultiCrossover(final ICrossover<G>[] data,
      final double[] weights) {
    this(null, data, weights);
  }

  /**
   * Create a new crossover by adding additional selectables to an existing
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
  public IterativeMultiCrossover(final Selector<ICrossover<G>> inherit,
      final ICrossover<G>[] data, final double[] weights) {
    super(inherit, data, weights);
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
  @Override
  public G crossover(final G source1, final G source2,
      final IRandomizer random) {
    IterativeSelector<ICrossover<G>>.Session s;
    G g;
    int i;

    s = this.openSession(random);
    g = null;
    for (i = s.getRemaining(); i > 0; i--) {
      g = s.next().crossover(source1, source2, random);
      if ((g != null)
          && (!(Utils.testEqualDeep(g, source1) || Utils.testEqualDeep(g,
              source2))))
        break;
    }

    s.close();
    return ((g != null) ? g : (random.nextBoolean() ? source1 : source2));
  }

}
