/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-01
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.machine.hl.constructs.instructions.WhileFactory.java
 * Last modification: 2007-03-01
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

package org.dgpf.hlgp.base.constructs.instructions;

import org.dgpf.hlgp.base.Expression;
import org.dgpf.hlgp.base.Function;
import org.dgpf.hlgp.base.InstructionFactory;
import org.dgpf.hlgp.base.constructs.Call;
import org.dgpf.hlgp.base.constructs.expressions.Comparison;
import org.dgpf.lgp.base.ECondition;
import org.sfc.math.Mathematics;
import org.sigoa.refimpl.genomes.tree.INodeFactory;
import org.sigoa.refimpl.genomes.tree.Node;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * The factory for while instructions.
 * 
 * @author Thomas Weise
 */
public class ForFactory extends InstructionFactory {
  /** the serial version uid */
  private static final long serialVersionUID = 1;

  /**
   * The globally shared factory for the while instruction.
   */
  public static final INodeFactory FOR_FACTORY = new ForFactory(For.class);

  /**
   * Create a new node factory.
   * 
   * @param clazz
   *          the class
   */
  protected ForFactory(final Class<? extends For> clazz) {
    super(clazz, 3, 3);
  }

  /**
   * read resolve
   * 
   * @return the resolved factory
   */
  @Override
  protected Object readResolve() {
    return FOR_FACTORY;
  }

  /**
   * Create a new node using the specified children.
   * 
   * @param children
   *          the children of the node to be, or <code>null</code> if no
   *          children are wanted
   * @param random
   *          the randomizer to be used for the node's creation
   * @return the new node
   */
  public Node create(final Node[] children, final IRandomizer random) {
    return new For(For.FOR_TYPES[random.nextInt(For.FOR_TYPES.length)],
        children);
  }

  /**
   * Checks whether the given class of child nodes is allowed.
   * 
   * @param clazz
   *          the child node class we want to know about
   * @param pos
   *          the position where the child should be placed
   * @return <code>true</code> if and only if the node-typed managed by
   *         this factory allows children that are instances of
   *         <code>clazz</code>
   */
  @Override
  public boolean isChildAllowed(final Class<? extends Node> clazz,
      final int pos) {

    if (super.isChildAllowed(clazz, pos)) {

      if (pos <= 1) {
        if ((Expression.class.isAssignableFrom(clazz) && (!(Comparison.class
            .isAssignableFrom(clazz))))
            || Call.class.isAssignableFrom(clazz))
          return true;
      } else {
        if (Function.class.isAssignableFrom(clazz)
            || DeclVar.class.isAssignableFrom(clazz))
          return false;
        if (Block.class.isAssignableFrom(clazz))
          return true;
        /*
         * if (Instruction.class.isAssignableFrom(clazz) ||
         * Call.class.isAssignableFrom(clazz)) return true;
         */
      }
    }
    return false;
  }

  /**
   * This method performs one single mutation on the nodes values. It must
   * not change its children or any other the members of <code>Node</code>.
   * 
   * @param source
   *          The source node.
   * @param random
   *          The randomizer to be used.
   * @return The resulting node or the source node if no mutation was
   *         available.
   * @throws NullPointerException
   *           if <code>source==null||random==null</code>.
   */
  @Override
  public Node mutate(final Node source, final IRandomizer random) {
    For f;
    int i, j;
    ECondition[] c;
    ECondition nc;

    f = ((For) copyNode(source));
    c = For.FOR_TYPES;
    nc = f.m_dir;
    for (i = (c.length - 1); i >= 0; i--) {
      if (c[i] == nc)
        break;
    }

    do {
      j = Mathematics.modulo((int) (random.nextNormal(i, 1)), c.length);
    } while (j == i);

    f.m_dir = c[j];
    return f;
  }
}
