/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-02-16
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.tree.reproduction.SubTreeInsertMutator.java
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
import org.sigoa.refimpl.go.reproduction.Mutator;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * A mutator that inserts a sub-tree.
 *
 * @author Thomas Weise
 */
public class SubTreeInsertMutator extends Mutator<Node> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the sub tree creator
   */
  private final TreeCreator m_subTreeCreator;

  /**
   * create a new sub tree insert mutator
   *
   * @param subTreeCreator
   *          the sub tree creator
   * @throws IllegalArgumentException
   *           if <code>subTreeCreator==null</code>
   */
  public SubTreeInsertMutator(final TreeCreator subTreeCreator) {
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
    Node res, e;
    INodeFactory f;
    int w, s, pos, z, i;

    p1 = Path.allocate();
    res = source;
    main: for (w = source.getWeight(); w >= 0; w--) {
      e = p1.findRandomPath(source, random);
      f = e.getFactory();
      s = e.size();
      if (f.getMaxChildren() > s) {
        check: {
          next: for (z = s; z >= 0; z--) {
            pos = random.nextInt(s + 1);
            for (i = pos; i < s; i++) {
              if (!(f.isChildAllowed(e.get(i).getClass(), i + 1)))
                continue next;
            }
            break check;
          }
          continue main;
        }

        res = this.m_subTreeCreator.create(f, pos, random);
        if (res != null) {
          res = p1.replaceEnd(Path.insert(e, res, pos));
          break;
        }
        res = source;
      }
    }
    p1.dispose();

    return res;
  }

}
