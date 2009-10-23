/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-01
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.hlgp.base.ConstructFactory.java
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

import org.dgpf.hlgp.base.constructs.instructions.Block;
import org.sigoa.refimpl.genomes.tree.Node;
import org.sigoa.refimpl.genomes.tree.NodeFactory;

/**
 * This factory creates high-level construct
 * 
 * @author Thomas Weise
 */
public abstract class ConstructFactory extends NodeFactory {
  /**
   * Create a new node factory.
   * 
   * @param clazz
   *          the node clazz
   * @param minChildren
   *          the minimum count of children
   * @param maxChildren
   *          the maximum count of children
   */
  protected ConstructFactory(final Class<? extends Node> clazz,
      final int minChildren, final int maxChildren) {
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
    return (super.isChildAllowed(clazz, pos) && (!(HLGPProgram.class
        .isAssignableFrom(clazz))) && (!(Block.class.isAssignableFrom(clazz))));
  }
}
