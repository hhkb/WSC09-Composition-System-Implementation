/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-01
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.hlgp.base.HLGPProgramFactory.java
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

package org.dgpf.hlgp.base;

import org.dgpf.hlgp.base.constructs.instructions.DeclVar;
import org.sigoa.refimpl.genomes.tree.INodeFactory;
import org.sigoa.refimpl.genomes.tree.Node;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * The program factory creates new programs
 * 
 * @author Thomas Weise
 */
public class HLGPProgramFactory extends InstructionFactory {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The globally shared default program factory
   */
  public static final INodeFactory PROGRAM_FACTORY = new HLGPProgramFactory(
      HLGPProgram.class, 1) {
    private static final long serialVersionUID = 1;

    @Override
    protected final Object readResolve() {
      return PROGRAM_FACTORY;
    }
  };

  /**
   * Create a new node factory.
   * 
   * @param clazz
   *          the program class
   * @param minFunctions
   *          the minimum count of allowed functions
   */
  public HLGPProgramFactory(final Class<? extends HLGPProgram> clazz,
      final int minFunctions) {
    super(clazz, Math.max(1, minFunctions), Integer.MAX_VALUE);
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
    return new HLGPProgram(children);
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
    return (super.isChildAllowed(clazz, pos)
        && (Function.class.isAssignableFrom(clazz)) || (DeclVar.class
        .isAssignableFrom(clazz))||
        (Function.class
            .isAssignableFrom(clazz)));
  }
}
