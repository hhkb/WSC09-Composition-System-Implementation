/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-02-16
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.tree.reproduction.SubTreeReplaceMutator.java
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

import org.sigoa.refimpl.genomes.tree.Node;
import org.sigoa.refimpl.genomes.tree.Path;
import org.sigoa.refimpl.go.reproduction.Mutator;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * A mutator that replaces a sub-tree.
 *
 * @author Thomas Weise
 */
public class SubTreeReplaceMutator extends Mutator<Node> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the sub tree creator
   */
  private final TreeCreator m_subTreeCreator;

  /**
   * create a new sub tree replace mutator
   *
   * @param subTreeCreator
   *          the sub tree creator
   * @throws IllegalArgumentException
   *           if <code>subTreeCreator==null</code>
   */
  public SubTreeReplaceMutator(final TreeCreator subTreeCreator) {
    super();
    if (subTreeCreator == null)
      throw new IllegalArgumentException();
    this.m_subTreeCreator = subTreeCreator;
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
  @Override
  public Node mutate(final Node source, final IRandomizer random) {
    Path p1;
    Node dst;
    int w, l;

    w = source.getWeight();
    if (w <= 1) {
      if (random == null)
        throw new NullPointerException();
      return source;
    }

    p1 = Path.allocate();
    dst = source;

    for (; w >= 0; w--) {
      do {//
      } while (p1.findRandomPath(source, random) == source);

      l = (p1.size() - 1);
      dst = this.m_subTreeCreator.create(p1.get(l).getFactory(), p1
          .getIndex(l), random);
      if (dst != null) {
        dst = p1.replaceEnd(dst);
        break;
      }
      dst = source;

    }

    p1.dispose();

    return dst;
  }

}
