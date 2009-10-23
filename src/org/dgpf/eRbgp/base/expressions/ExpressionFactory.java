/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-04-17
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.eRbgp.base.expressions.ExpressionFactory.java
 * Last modification: 2008-04-17
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

package org.dgpf.eRbgp.base.expressions;

import org.dgpf.rbgp.base.EComparison;
import org.sigoa.refimpl.genomes.tree.INodeFactory;
import org.sigoa.refimpl.genomes.tree.Node;
import org.sigoa.refimpl.genomes.tree.NodeFactory;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * The factory class for expressions
 * 
 * @author Thomas Weise
 */
public abstract class ExpressionFactory extends NodeFactory {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The shared and-factory
   */
  public static final INodeFactory AND_FACTORY = new ExpressionFactory(
      And.class, 2, 2) {
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
      return new And(children);
    }
  };

  /**
   * The shared and-factory
   */
  public static final INodeFactory OR_FACTORY = new ExpressionFactory(
      Or.class, 2, 2) {
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
      return new Or(children);
    }
  };

  /**
   * The shared and-factory
   */
  public static final INodeFactory NOT_FACTORY = new ExpressionFactory(
      Not.class, 1, 1) {
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
      return new Not(children);
    }
  };

  /**
   * The shared and-factory
   */
  public static final INodeFactory READ_FACTORY = new ExpressionFactory(
      Read.class, 0, 0) {
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
      return Read.CACHE[random.nextInt(Read.CACHE.length)];
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
      Read r;

      do {
        r = Read.CACHE[random.nextInt(Read.CACHE.length)];
      } while (r == source);
      return r;
    }
  };

  /**
   * The shared and-factory
   */
  public static final INodeFactory READ_INDIRECT_FACTORY = new ExpressionFactory(
      ReadInd.class, 1, 1) {
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
      return new ReadInd(children);
    }

    // /**
    // * This method performs one single mutation on the nodes values. It
    // * must not change its children or any other the members of
    // * <code>Node</code>.
    // *
    // * @param source
    // * The source node.
    // * @param random
    // * The randomizer to be used.
    // * @return The resulting node or the source node if no mutation was
    // * available.
    // * @throws NullPointerException
    // * if <code>source==null||random==null</code>.
    // */
    // @Override
    // public Node mutate(final Node source, final IRandomizer random) {
    // ReadInd r;
    //
    // do {
    // r = ReadInd.CACHE[random.nextInt(ReadInd.CACHE.length)];
    // } while (r == source);
    // return r;
    // }
  };

  /**
   * The shared and-factory
   */
  public static final INodeFactory ADD_FACTORY = new ExpressionFactory(
      Add.class, 2, 2) {
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
      return new Add(children);
    }
  };

  /**
   * The shared and-factory
   */
  public static final INodeFactory SUB_FACTORY = new ExpressionFactory(
      Sub.class, 2, 2) {
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
      return new Sub(children);
    }
  };

  /**
   * The shared and-factory
   */
  public static final INodeFactory MUL_FACTORY = new ExpressionFactory(
      Mul.class, 2, 2) {
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
      return new Mul(children);
    }
  };

  /**
   * The shared and-factory
   */
  public static final INodeFactory DIV_FACTORY = new ExpressionFactory(
      Div.class, 2, 2) {
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
      return new Div(children);
    }
  };

  /**
   * the list of comparison values
   */
  static final EComparison[] COMPS = EComparison.values();

  /**
   * The shared and-factory
   */
  public static final INodeFactory COMPARE_FACTORY = new ExpressionFactory(
      Compare.class, 2, 2) {
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
      return new Compare(children, COMPS[random.nextInt(COMPS.length)]);
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
      Compare c;
      EComparison ex;

      c = (Compare) copyNode(source);

      do {
        ex = COMPS[random.nextInt(COMPS.length)];
      } while (ex == c.m_comparison);

      c.m_comparison = ex;

      return c;
    }
  };

  /**
   * The shared and-factory
   */
  public static final INodeFactory CONSTANT_FACTORY = new ExpressionFactory(
      Constant.class, 0, 0) {
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
      int i;

      i = 0;
      do {
        i += (1 << random.nextInt(5));
        if (random.nextBoolean())
          i = -i;
      } while (random.nextBoolean());

      return new Constant(i);
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
      Constant c;
      int ex, v;

      c = (Constant) copyNode(source);
      v = c.m_value;

      do {
        ex = (int) (v + random.nextGaussian());
      } while (ex == v);

      c.m_value = ex;

      return c;
    }
  };

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
  protected ExpressionFactory(final Class<? extends Expression> clazz,
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
    return super.isChildAllowed(clazz, pos)
        && (Expression.class.isAssignableFrom(clazz));
  }

}
