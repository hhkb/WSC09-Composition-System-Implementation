/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-02-16
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.tree.reproduction.SubTreeExchangeCrossover.java
 * Last modification: 2007-02-16
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

package org.sigoa.refimpl.genomes.tree.reproduction;

import org.sigoa.refimpl.genomes.tree.INodeFactory;
import org.sigoa.refimpl.genomes.tree.Node;
import org.sigoa.refimpl.genomes.tree.Path;
import org.sigoa.refimpl.go.reproduction.Crossover;
import org.sigoa.spec.go.reproduction.ICrossover;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * A tree crossover operator that exchanges two sub-trees.
 *
 * @author Thomas Weise
 */
public class SubTreeExchangeCrossover extends Crossover<Node> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The globally shared instance of the tree crossover.
   */
  public static final ICrossover<Node> SUB_TREE_EXCHANGE_CROSSOVER = new SubTreeExchangeCrossover() {
    private static final long serialVersionUID = 1;

    private final Object readResolve() {
      return SUB_TREE_EXCHANGE_CROSSOVER;
    }

    private final Object writeReplace() {
      return SUB_TREE_EXCHANGE_CROSSOVER;
    }
  };

  /**
   * The protected constructor.
   */
  protected SubTreeExchangeCrossover() {
    super();
  }

  /**
   * Perform one single recombination/crossover. This simple default
   * implementation just returns one of the two source genotypes chosen
   * randomly.
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
  public Node crossover(final Node source1, final Node source2,
      final IRandomizer random) {
    Path p1, p2;
    int w1, w2, l, v;
    Node res;
    INodeFactory f;

    w1 = source1.getWeight();
    if (w1 <= 1) {
      if (source2.getWeight() > 1)
        return crossover(source2, source1, random);
      if (random == null)
        throw new NullPointerException();
      return source2;
    }

    p1 = Path.allocate();
    p2 = Path.allocate();
    res = source1;
    v = source2.getWeight();

    main: for (; w1 >= 0; w1--) {

      do {//
      } while (p1.findRandomPath(source1, random) == source1);

      l = (p1.size() - 1);
      f = p1.get(l).getFactory();
      l = p1.getIndex(l);
      for (w2 = v; w2 >= 0; w2--) {
        res = p2.findRandomPath(source2, random);
        if (f.isChildAllowed(res.getClass(), l)) {
          res = p1.replaceEnd(res);
          break main;
        }

      }

      res = source1;
    }

    p1.dispose();
    p2.dispose();

    if ((res == source1) && (random.nextBoolean()))
      return source2;
    return res;
  }

}
