/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-01
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.machine.hl.constructs.instructions.OptionFactory.java
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

import org.dgpf.hlgp.base.Function;
import org.dgpf.hlgp.base.InstructionFactory;
import org.dgpf.hlgp.base.constructs.expressions.And;
import org.dgpf.hlgp.base.constructs.expressions.Comparison;
import org.dgpf.hlgp.base.constructs.expressions.Not;
import org.dgpf.hlgp.base.constructs.expressions.Or;
import org.dgpf.hlgp.base.constructs.expressions.Xor;
import org.sigoa.refimpl.genomes.tree.Node;

/**
 * The factory for optional instructions.
 * 
 * @author Thomas Weise
 */
abstract class OptionFactory extends InstructionFactory {

  /**
   * Create a new option factory.
   * 
   * @param clazz
   *          the node clazz
   * @param minChildren
   *          the minimum count of children
   * @param maxChildren
   *          the maximum count of children
   */
  OptionFactory(final Class<? extends Node> clazz, final int minChildren,
      final int maxChildren) {
    super(clazz, minChildren, maxChildren);
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

      if (pos == 0) {
        // if (Expression.class.isAssignableFrom(clazz)
        // || Call.class.isAssignableFrom(clazz))
        // return true;
        if (And.class.isAssignableFrom(clazz)
            || Not.class.isAssignableFrom(clazz)
            || Or.class.isAssignableFrom(clazz)
            || Xor.class.isAssignableFrom(clazz)
            || Comparison.class.isAssignableFrom(clazz))
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

}
