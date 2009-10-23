/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-01
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.machine.hl.constructs.instructions.StoreFactory.java
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

import org.dgpf.hlgp.base.InstructionFactory;
import org.dgpf.lgp.base.EIndirection;
import org.sigoa.refimpl.genomes.tree.INodeFactory;
import org.sigoa.refimpl.genomes.tree.Node;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * The factory for variable declarations instructions.
 * 
 * @author Thomas Weise
 */
public class DeclVarFactory extends InstructionFactory {
  /** the serial version uid */
  private static final long serialVersionUID = 1;

  /**
   * declare a variable
   */
  private static final EIndirection[] DECL = new EIndirection[] {
      EIndirection.LOCAL, EIndirection.LOCAL, EIndirection.LOCAL,
      EIndirection.LOCAL, EIndirection.LOCAL, EIndirection.GLOBAL,
      EIndirection.GLOBAL, EIndirection.GLOBAL_LOCAL,
      EIndirection.GLOBAL_GLOBAL, EIndirection.LOCAL_GLOBAL,
      EIndirection.LOCAL_LOCAL, };

  /**
   * The globally shared factory for the variable declaration instruction.
   */
  public static final INodeFactory DECL_VAR_FACTORY = new DeclVarFactory(
      DeclVar.class);

  /**
   * Create a new node factory.
   * 
   * @param clazz
   *          the class
   */
  protected DeclVarFactory(final Class<? extends DeclVar> clazz) {
    super(clazz, 0, 0);
  }

  /**
   * read resolve
   * 
   * @return the resolved factory
   */
  @Override
  protected Object readResolve() {
    return DECL_VAR_FACTORY;
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
    return new DeclVar(DECL[random.nextInt(DECL.length)], random.nextInt());
  }
}
