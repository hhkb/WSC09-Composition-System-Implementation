/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-04-16
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.eRbgp.base.InternalProgramFactory.java
 * Last modification: 2008-04-16
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

package org.dgpf.eRbgp.base;

import org.dgpf.eRbgp.base.actions.Action;
import org.dgpf.eRbgp.base.expressions.Expression;
import org.sigoa.refimpl.genomes.tree.INodeFactory;
import org.sigoa.refimpl.genomes.tree.Node;
import org.sigoa.refimpl.genomes.tree.NodeFactory;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * The rule factory
 * 
 * @author Thomas Weise
 */
public class RuleFactory extends NodeFactory {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the internal program factory
   */
  public static final INodeFactory RULE_FACTORY = new RuleFactory();

  /**
   * Create a new node factory.
   * 
   * @param clazz
   *          the node clazz
   */
  protected RuleFactory() {
    super(Rule.class, 2, 2);
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
  @Override
  public Node create(final Node[] children, final IRandomizer random) {
    return new Rule(children);
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
    return ((pos == 0) && (Expression.class.isAssignableFrom(clazz)))
        || ((pos == 1) && (Action.class.isAssignableFrom(clazz)));
  }
}
