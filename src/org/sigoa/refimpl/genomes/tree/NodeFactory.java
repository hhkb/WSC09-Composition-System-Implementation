/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-02-28
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.tree.NodeFactory.java
 * Last modification: 2007-02-28
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

import org.sigoa.spec.stoch.IRandomizer;

/**
 * A simple standard implementation of the node factory
 * 
 * @author Thomas Weise
 */
public abstract class NodeFactory implements INodeFactory {
  /**
   * the node class managed
   */
  private transient final Class<? extends Node> m_clazz;

  /**
   * the minimum allowed children
   */
  private transient int m_minChildren;

  /**
   * the maximum allowed children
   */
  private transient int m_maxChildren;

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
  protected NodeFactory(final Class<? extends Node> clazz,
      final int minChildren, final int maxChildren) {
    super();
    this.m_clazz = clazz;
    this.m_minChildren = minChildren;
    this.m_maxChildren = maxChildren;
  }

  /**
   * Create a new node factory with zero to infinite many children.
   * 
   * @param clazz
   *          the node clazz
   */
  protected NodeFactory(final Class<? extends Node> clazz) {
    this(clazz, 0, Integer.MAX_VALUE);
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
  public boolean isChildAllowed(final Class<? extends Node> clazz,
      final int pos) {
    return ((clazz != null) && (this.m_maxChildren > pos) && (pos >= 0));
  }

  /**
   * Get the minimum count of children required by the node type managed by
   * this factory.
   * 
   * @return the minimum count of children required to create a new node
   */
  public int getMinChildren() {
    return this.m_minChildren;
  }

  /**
   * Get the maximum count of children allowed by the node type managed by
   * this factory.
   * 
   * @return the minimum count of children allowed to create a new node
   */
  public int getMaxChildren() {
    return this.m_maxChildren;
  }

  /**
   * Obtain the class of nodes managed by this factory.
   * 
   * @return the class of nodes managed by this factory
   */
  public Class<? extends Node> getNodeClass() {
    return this.m_clazz;
  }

  /**
   * Obtain the read instance of this factory.
   * 
   * @return the read instance of this factory
   */
  protected Object readResolve() {
    return this;
  }

  /**
   * replace this factory instance for writing
   * 
   * @return the factory to be written
   */
  protected Object writeReplace() {
    return this.readResolve();
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
  public Node mutate(final Node source, final IRandomizer random) {
    return source;
  }

  /**
   * Copy a node.
   * 
   * @param source
   *          the source node
   * @return the copy of the source node
   */
  protected static final Node copyNode(final Node source) {
    return source.copy();
  }
}
