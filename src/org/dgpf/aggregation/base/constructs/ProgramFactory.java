/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-27
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.aggregation.base.constructs.ProgramFactory.java
 * Last modification: 2007-03-27
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

package org.dgpf.aggregation.base.constructs;

import org.sigoa.refimpl.genomes.tree.INodeFactory;
import org.sigoa.refimpl.genomes.tree.Node;
import org.sigoa.refimpl.genomes.tree.NodeFactory;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * The program factory
 * 
 * @author Thomas Weise
 */
public class ProgramFactory extends NodeFactory {
  /** the serial version uid */
  private static final long serialVersionUID = 1;

  /**
   * The globally shared default program factory
   */
  public static final INodeFactory PROGRAM_FACTORY = new ProgramFactory(
      Program.class);

  /**
   * Create a new node factory.
   * 
   * @param clazz
   *          the node clazz
   */
  protected ProgramFactory(final Class<? extends Program> clazz) {
    super(clazz, 1, 2);
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
    return (Part.class.isAssignableFrom(clazz) && (pos >= 0) && (pos <= 1));
  }

  /**
   * read resolve the constant factory
   * 
   * @return the constant factory
   */
  @Override
  protected final Object readResolve() {
    return PROGRAM_FACTORY;
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
    return new Program(children);
  }

}
