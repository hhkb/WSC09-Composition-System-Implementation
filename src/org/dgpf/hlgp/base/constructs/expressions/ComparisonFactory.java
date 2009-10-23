/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-01
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.machine.hl.constructs.expressions.ComparisonFactory.java
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

import org.dgpf.hlgp.base.Expression;
import org.dgpf.hlgp.base.ExpressionFactory;
import org.dgpf.hlgp.base.constructs.Call;
import org.sfc.math.Mathematics;
import org.sigoa.refimpl.genomes.tree.INodeFactory;
import org.sigoa.refimpl.genomes.tree.Node;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * The factory for comparison expressions.
 *
 * @author Thomas Weise
 */
public class ComparisonFactory extends ExpressionFactory {
  /** the serial version uid */
  private static final long serialVersionUID = 1;

  /**
   * The globally shared factory for the comparison expression.
   */
  public static final INodeFactory COMPARISON_FACTORY = new ComparisonFactory(
      Comparison.class);

  /**
   * the comparisons 1
   */
  private static final EComparison[][] CMP = new EComparison[][] {
      { EComparison.LT, EComparison.LTE, EComparison.EQ, EComparison.GTE,
          EComparison.GT, EComparison.NEQ, },
      { EComparison.B, EComparison.BE, EComparison.EQ, EComparison.AE,
          EComparison.A, EComparison.NEQ, } };

  static {
    int i, j;
    EComparison[] x;
    EComparison d;

    for (i = (CMP.length - 1); i >= 0; i--) {
      x = CMP[i];
      for (j = (x.length - 1); j >= 0; j--) {
        d = x[j];
        d.m_index = j;
        d.m_label = i;
      }
    }
  }

  /**
   * Create a new node factory.
   *
   * @param clazz
   *          the class
   */
  protected ComparisonFactory(final Class<? extends Comparison> clazz) {
    super(clazz, 2, 2);
  }

  /**
   * read resolve
   *
   * @return the resolved factory
   */
  @Override
  protected Object readResolve() {
    return COMPARISON_FACTORY;
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
    i = random.nextInt(CMP.length);
    return new Comparison(CMP[i][random.nextInt(CMP[i].length)], children);
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
    EComparison o, n;
    Comparison r;
    EComparison[] c;
    int i;

    r = ((Comparison) (copyNode(source)));
    o = r.m_comp;
    do {
      if ((o == EComparison.EQ) || (o == EComparison.NEQ)) {
        n = (random.nextBoolean() ? CMP[0][random.nextInt(CMP[0].length)]
            : CMP[1][random.nextInt(CMP[1].length)]);
      } else {
        c = CMP[o.m_label];

        do {
          i = Mathematics.modulo(
              ((int) (random.nextNormal(o.m_index, 1))), c.length);
        } while (i == o.m_index);
        n = c[i];
      }
    } while (n == o);

    r.m_comp = n;

    return r;
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
    return (super.isChildAllowed(clazz, pos) && ((Expression.class
        .isAssignableFrom(clazz) && (!(Comparison.class
        .isAssignableFrom(clazz)))) || Call.class.isAssignableFrom(clazz)));
  }
}
