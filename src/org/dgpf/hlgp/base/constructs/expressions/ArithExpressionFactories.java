/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-01
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.machine.hl.constructs.expressions.ArithExpressionFactories.java
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
import org.sigoa.refimpl.genomes.tree.INodeFactory;
import org.sigoa.refimpl.genomes.tree.Node;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * The factory for constant expressions.
 *
 * @author Thomas Weise
 */
public abstract class ArithExpressionFactories extends ExpressionFactory {
  /** the serial version uid */
  private static final long serialVersionUID = 1;

  /**
   * The globally shared default add factory
   */
  public static final INodeFactory ADD_FACTORY = new ArithExpressionFactories(
      Add.class, 2, 2) {
    private static final long serialVersionUID = 1;

    public Node create(final Node[] children, final IRandomizer random) {
      return new Add(children);
    }

    @Override
    protected final Object readResolve() {
      return ADD_FACTORY;
    }
  };

  /**
   * The globally shared default sub factory
   */
  public static final INodeFactory SUB_FACTORY = new ArithExpressionFactories(
      Sub.class, 2, 2) {
    private static final long serialVersionUID = 1;

    public Node create(final Node[] children, final IRandomizer random) {
      return new Sub(children);
    }

    @Override
    protected final Object readResolve() {
      return SUB_FACTORY;
    }
  };

  /**
   * The globally shared default mul factory
   */
  public static final INodeFactory MUL_FACTORY = new ArithExpressionFactories(
      Mul.class, 2, 2) {
    private static final long serialVersionUID = 1;

    public Node create(final Node[] children, final IRandomizer random) {
      return new Mul(children);
    }

    @Override
    protected final Object readResolve() {
      return MUL_FACTORY;
    }
  };

  /**
   * The globally shared default div factory
   */
  public static final INodeFactory DIV_FACTORY = new ArithExpressionFactories(
      Div.class, 2, 2) {
    private static final long serialVersionUID = 1;

    public Node create(final Node[] children, final IRandomizer random) {
      return new Div(children);
    }

    @Override
    protected final Object readResolve() {
      return DIV_FACTORY;
    }
  };

  /**
   * The globally shared default mod factory
   */
  public static final INodeFactory MOD_FACTORY = new ArithExpressionFactories(
      Mod.class, 2, 2) {
    private static final long serialVersionUID = 1;

    public Node create(final Node[] children, final IRandomizer random) {
      return new Mod(children);
    }

    @Override
    protected final Object readResolve() {
      return MOD_FACTORY;
    }
  };

  /**
   * The globally shared default and factory
   */
  public static final INodeFactory AND_FACTORY = new ArithExpressionFactories(
      And.class, 2, 2) {
    private static final long serialVersionUID = 1;

    public Node create(final Node[] children, final IRandomizer random) {
      return new And(children);
    }

    @Override
    protected final Object readResolve() {
      return AND_FACTORY;
    }
  };

  /**
   * The globally shared default or factory
   */
  public static final INodeFactory OR_FACTORY = new ArithExpressionFactories(
      Or.class, 2, 2) {
    private static final long serialVersionUID = 1;

    public Node create(final Node[] children, final IRandomizer random) {
      return new Or(children);
    }

    @Override
    protected final Object readResolve() {
      return OR_FACTORY;
    }
  };

  /**
   * The globally shared default xor factory
   */
  public static final INodeFactory XOR_FACTORY = new ArithExpressionFactories(
      Xor.class, 2, 2) {
    private static final long serialVersionUID = 1;

    public Node create(final Node[] children, final IRandomizer random) {
      return new Xor(children);
    }

    @Override
    protected final Object readResolve() {
      return XOR_FACTORY;
    }
  };

  /**
   * The globally shared default not factory
   */
  public static final INodeFactory NOT_FACTORY = new ArithExpressionFactories(
      Not.class, 1, 1) {
    private static final long serialVersionUID = 1;

    public Node create(final Node[] children, final IRandomizer random) {
      return new Not(children);
    }

    @Override
    protected final Object readResolve() {
      return NOT_FACTORY;
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
  protected ArithExpressionFactories(final Class<? extends Node> clazz,
      final int minChildren, final int maxChildren) {
    super(clazz, 2, 2);
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
