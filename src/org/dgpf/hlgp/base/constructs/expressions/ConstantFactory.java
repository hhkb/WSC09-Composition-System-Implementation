/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-01
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.machine.hl.constructs.expressions.ConstantFactory.java
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
 * The factory for constant expressions.
 *
 * @author Thomas Weise
 */
public class ConstantFactory extends ExpressionFactory {
  /** the serial version uid */
  private static final long serialVersionUID = 1;

  /**
   * The globally shared factory for the constant expression.
   */
  public static final INodeFactory CONSTANT_FACTORY = new ConstantFactory(
      Constant.class);

  /**
   * Create a new node factory.
   *
   * @param clazz
   *          the class
   */
  protected ConstantFactory(final Class<? extends Constant> clazz) {
    super(clazz, 0, 0);
  }

  /**
   * read resolve
   *
   * @return the resolved factory
   */
  @Override
  protected Object readResolve() {
    return CONSTANT_FACTORY;
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
    int i;

    do {
      i = ((int) (random.nextExponential(1)));
    } while ((i <= 0) || (i > 32));

    if (i >= 32)
      return new Constant(random.nextInt());

    i = random.nextInt(1 << i);
    return new Constant(random.nextBoolean() ? i : -i);
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
    Constant c;
    int ov, nv;

    c = ((Constant) copyNode(source));
    ov = c.m_value;
    do {
      nv = ((int) (random.nextNormal(c.m_value, random
          .nextExponential(1.0d))));
    } while (nv == ov);

    return c;
  }
}
