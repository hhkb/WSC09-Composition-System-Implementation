/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-04-18
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.eRbgp.base.actions.SetFactory.java
 * Last modification: 2008-04-18
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

package org.dgpf.eRbgp.base.actions;

import org.dgpf.eRbgp.base.expressions.Expression;
import org.dgpf.rbgp.base.Symbol;
import org.sigoa.refimpl.genomes.tree.INodeFactory;
import org.sigoa.refimpl.genomes.tree.Node;
import org.sigoa.refimpl.genomes.tree.NodeFactory;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * the set factory
 * 
 * @author Thomas Weise
 */
public abstract class SetFactory extends NodeFactory {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The shared set action-factory
   */
  public static final INodeFactory SET_FACTORY = new SetFactory(
      SetAction.class) {
    /**
     * The serial version uid.
     */
    private static final long serialVersionUID = 1;

    /**
     * Create a new node using the specified children.
     * 
     * @param children
     *          the children of the node to be, or <code>null</code> if
     *          no children are wanted
     * @param random
     *          the randomizer to be used for the node's creation
     * @return the new node
     */
    public Node create(final Node[] children, final IRandomizer random) {
      return new SetAction(children, SetAction.CACHE[random
          .nextInt(SetAction.CACHE.length)]);
    }

    /**
     * This method performs one single mutation on the nodes values. It
     * must not change its children or any other the members of
     * <code>Node</code>.
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
      SetAction c;
      Symbol ex;

      c = (SetAction) copyNode(source);

      do {
        ex = SetAction.CACHE[random.nextInt(SetAction.CACHE.length)];
      } while (ex == c.m_symbol);

      c.m_symbol = ex;

      return c;
    }
  };

  /**
   * The shared set action-factory
   */
  public static final INodeFactory SET_IND_FACTORY = new SetFactory(
      SetActionInd.class) {
    /**
     * The serial version uid.
     */
    private static final long serialVersionUID = 1;

    /**
     * Create a new node using the specified children.
     * 
     * @param children
     *          the children of the node to be, or <code>null</code> if
     *          no children are wanted
     * @param random
     *          the randomizer to be used for the node's creation
     * @return the new node
     */
    public Node create(final Node[] children, final IRandomizer random) {
      return new SetActionInd(children, SetActionInd.CACHE[random
          .nextInt(SetActionInd.CACHE.length)]);
    }

    /**
     * This method performs one single mutation on the nodes values. It
     * must not change its children or any other the members of
     * <code>Node</code>.
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
      SetActionInd c;
      Symbol ex;

      c = (SetActionInd) copyNode(source);

      do {
        ex = SetActionInd.CACHE[random.nextInt(SetActionInd.CACHE.length)];
      } while (ex == c.m_symbol);

      c.m_symbol = ex;

      return c;
    }
  };

  /**
   * Create a new node factory.
   * 
   * @param clazz
   *          the node clazz
   */
  protected SetFactory(final Class<? extends Action<?>> clazz) {
    super(clazz, 1, 1);
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
    return (pos == 0) && (Expression.class.isAssignableFrom(clazz));
  }

}
