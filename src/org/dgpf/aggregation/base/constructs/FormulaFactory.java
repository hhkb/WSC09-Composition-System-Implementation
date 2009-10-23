/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-27
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.aggregation.base.constructs.FomulaFactory.java
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
 * The formula factory
 * 
 * @author Thomas Weise
 */
public class FormulaFactory extends NodeFactory {
  /** the serial version uid */
  private static final long serialVersionUID = 1;

  /**
   * The globally shared default formula factory
   */
  public static final INodeFactory FORMULA_FACTORY = new FormulaFactory(
      Formula.class);

  /**
   * Create a new node factory.
   * 
   * @param clazz
   *          the node clazz
   */
  protected FormulaFactory(final Class<? extends Formula> clazz) {
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
    return ((pos == 0) && Program.canBeArbitrarilyNested(clazz));
  }

  /**
   * read resolve the constant factory
   * 
   * @return the constant factory
   */
  @Override
  protected final Object readResolve() {
    return FORMULA_FACTORY;
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
  @Override
  public Node mutate(final Node source, final IRandomizer random) {
    Formula x;

    x = ((Formula) copyNode(source));
    x.m_index = random.nextInt();
    return x;
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
    return new Formula(random.nextInt(), children);
  }

}
