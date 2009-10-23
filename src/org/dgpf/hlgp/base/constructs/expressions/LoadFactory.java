/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-01
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.machine.hl.constructs.expressions.LoadFactory.java
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

package org.dgpf.hlgp.base.constructs.expressions;

import org.dgpf.hlgp.base.ExpressionFactory;
import org.sigoa.refimpl.genomes.tree.INodeFactory;
import org.sigoa.refimpl.genomes.tree.Node;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * The factory for load expressions.
 *
 * @author Thomas Weise
 */
public class LoadFactory extends ExpressionFactory {
  /** the serial version uid */
  private static final long serialVersionUID = 1;

  /**
   * The globally shared factory for the load expression.
   */
  public static final INodeFactory LOAD_FACTORY = new LoadFactory(
      Load.class);

  /**
   * Create a new node factory.
   *
   * @param clazz
   *          the class
   */
  protected LoadFactory(final Class<? extends Load> clazz) {
    super(clazz, 0, 0);
  }

  /**
   * read resolve
   *
   * @return the resolved factory
   */
  @Override
  protected Object readResolve() {
    return LOAD_FACTORY;
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
    return new Load(random.nextInt());
  }
}
