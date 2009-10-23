/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-02-16
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.tree.INodeFactory.java
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

package org.sigoa.refimpl.genomes.tree;

import org.sigoa.spec.go.reproduction.IMutator;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * This interface produces nodes.
 *
 * @author Thomas Weise
 */
public interface INodeFactory extends IMutator<Node> {

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
  public abstract Node create(final Node[] children,
      final IRandomizer random);

  /**
   * Get the minimum count of children required by the node type managed by
   * this factory.
   *
   * @return the minimum count of children required to create a new node
   */
  public abstract int getMinChildren();

  /**
   * Get the maximum count of children allowed by the node type managed by
   * this factory.
   *
   * @return the minimum count of children allowed to create a new node
   */
  public abstract int getMaxChildren();

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
  public abstract boolean isChildAllowed(
      final Class<? extends Node> clazz, final int pos);

  /**
   * Obtain the class of nodes managed by this factory.
   *
   * @return the class of nodes managed by this factory
   */
  public abstract Class<? extends Node> getNodeClass();

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
  public abstract Node mutate(final Node source, final IRandomizer random);
}
