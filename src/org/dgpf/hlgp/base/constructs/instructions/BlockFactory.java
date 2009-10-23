/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-01
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.machine.hl.constructs.instructions.BlockFactory.java
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
import org.dgpf.hlgp.base.Instruction;
import org.dgpf.hlgp.base.InstructionFactory;
import org.dgpf.hlgp.base.constructs.Call;
import org.sigoa.refimpl.genomes.tree.INodeFactory;
import org.sigoa.refimpl.genomes.tree.Node;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * The factory for if-then-else instructions.
 * 
 * @author Thomas Weise
 */
public class BlockFactory extends InstructionFactory {
  /** the serial version uid */
  private static final long serialVersionUID = 1;

  /**
   * The globally shared factory for the instruction block.
   */
  public static final INodeFactory BLOCK_FACTORY = new BlockFactory(
      Block.class);

  /**
   * Create a new node factory.
   * 
   * @param clazz
   *          the instruction clazz
   */
  protected BlockFactory(final Class<? extends Block> clazz) {
    this(clazz, Integer.MAX_VALUE);
  }
  
  /**
   * Create a new node factory.
   * 
   * @param clazz
   *          the instruction clazz
   *          
   * @param maxChildren
   *          the maximum count of children
   */
  protected BlockFactory(final Class<? extends Block> clazz, final int maxChildren) {
    super(clazz,1, Math.max(1, maxChildren));
  }

  /**
   * read resolve
   * 
   * @return the resolved factory
   */
  @Override
  protected Object readResolve() {
    return BLOCK_FACTORY;
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
      final int pos) {// we allow blocks inside of blocks but no functions
    return (super.isChildAllowed(clazz, pos)
        && (!(Function.class.isAssignableFrom(clazz))) && ((Instruction.class
        .isAssignableFrom(clazz)
        || Call.class.isAssignableFrom(clazz) || (DeclVar.class
        .isAssignableFrom(clazz)))));
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
    return new Block(children);
  }
}
