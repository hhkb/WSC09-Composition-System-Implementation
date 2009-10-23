/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-02-16
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.tree.reproduction.SubTreeDeleteMutator.java
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
import org.sigoa.spec.go.reproduction.IMutator;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * A mutator that removes a sub-tree.
 *
 * @author Thomas Weise
 */
public class SubTreeDeleteMutator extends Mutator<Node> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The globally shared sub-tree delete mutator
   */
  public static final IMutator<Node> SUB_TREE_DELETE_MUTATOR = new SubTreeDeleteMutator() {
    private static final long serialVersionUID = 1;

    private final Object readResolve() {
      return SUB_TREE_DELETE_MUTATOR;
    }

    private final Object writeReplace() {
      return SUB_TREE_DELETE_MUTATOR;
    }
  };

  /**
   * create a new sub tree delete mutator
   */
  protected SubTreeDeleteMutator() {
    super();
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
    Node res, x;
    int w, s, pos;
    INodeFactory f;

    w = source.getWeight();
    if (w <= 1) {
      if (random == null)
        throw new NullPointerException();
      return source;
    }

    p1 = Path.allocate();
    res = source;
    main: for (--w; w > 0; w--) {
      do {
        //
      } while (p1.findRandomPath(source, random) == source);

      s = (p1.size() - 1);
      x = p1.get(s);
      f = x.getFactory();
      pos = p1.getIndex(s);
      s = x.size();
      if (f.getMinChildren() < s) {
        if (s > 0) {
          for (--s; s > pos; s--) {
            if (!(f.isChildAllowed(x.get(s).getClass(), s - 1)))
              continue main;
          }
        }

        res = p1.replaceEnd(null);
        break;
      }
    }

    p1.dispose();
    return res;
  }
}
