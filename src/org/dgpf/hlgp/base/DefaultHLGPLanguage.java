/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-02
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.hlgp.base.DefaultHLGPLanguage.java
 * Last modification: 2007-03-02
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

package org.dgpf.hlgp.base;

import org.dgpf.hlgp.base.constructs.CallFactory;
import org.dgpf.hlgp.base.constructs.TerminateFactory;
import org.dgpf.hlgp.base.constructs.expressions.ArithExpressionFactories;
import org.dgpf.hlgp.base.constructs.expressions.ComparisonFactory;
import org.dgpf.hlgp.base.constructs.expressions.ConstantFactory;
import org.dgpf.hlgp.base.constructs.expressions.LoadFactory;
import org.dgpf.hlgp.base.constructs.instructions.BlockFactory;
import org.dgpf.hlgp.base.constructs.instructions.DeclVarFactory;
import org.dgpf.hlgp.base.constructs.instructions.ForFactory;
import org.dgpf.hlgp.base.constructs.instructions.IfThenElseFactory;
import org.dgpf.hlgp.base.constructs.instructions.ReturnFactory;
import org.dgpf.hlgp.base.constructs.instructions.StoreFactory;
import org.dgpf.hlgp.base.constructs.instructions.WhileFactory;
import org.sigoa.refimpl.genomes.tree.INodeFactory;
import org.sigoa.refimpl.genomes.tree.reproduction.NodeFactorySet;

/**
 * The default high level language
 * 
 * @author Thomas Weise
 */
public final class DefaultHLGPLanguage extends NodeFactorySet {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * The node factories
   */
  private static final INodeFactory[] FACTORIES = new INodeFactory[] {

  FunctionFactory.FUNCTION_FACTORY,//

      CallFactory.CALL_FACTORY,//

      BlockFactory.BLOCK_FACTORY,//
      IfThenElseFactory.IF_THEN_ELSE_FACTORY,//
      StoreFactory.STORE_FACTORY,//
      WhileFactory.WHILE_FACTORY,//
      ForFactory.FOR_FACTORY,//
      DeclVarFactory.DECL_VAR_FACTORY,//
      ReturnFactory.RETURN_FACTORY,//

      ConstantFactory.CONSTANT_FACTORY,//
      ComparisonFactory.COMPARISON_FACTORY, LoadFactory.LOAD_FACTORY,//
      ArithExpressionFactories.ADD_FACTORY,//
      ArithExpressionFactories.SUB_FACTORY,//
      ArithExpressionFactories.MUL_FACTORY,//
      ArithExpressionFactories.DIV_FACTORY,//
      ArithExpressionFactories.MOD_FACTORY,//
      ArithExpressionFactories.AND_FACTORY,//
      ArithExpressionFactories.OR_FACTORY,//
      ArithExpressionFactories.XOR_FACTORY,//
      ArithExpressionFactories.NOT_FACTORY,//

  };

  /**
   * the weights
   */
  private static final double[] WEIGHTS = new double[] { 1.0d,// func

      1.0d,// call

      5.0d,// block
      0.2d,// if
      1.0d,// store
      0.2d,// while
      0.2d,// for
      0.3,// decl var
      0.3,// return

      1.0d,// const
      1.0d,// cmp
      1.0d,// load
      1.0d,// add
      1.0d,// sub
      1.0d,// mul
      1.0d,// div
      1.0d,// mod
      1.0d,// and
      1.0d,// or
      1.0d,// xor
      1.0d,// not
  };

  /**
   * the default language
   */
  public static final NodeFactorySet DEFAULT_LANGUAGE = new DefaultHLGPLanguage(
      false);

  /**
   * Create a new default language.
   * 
   * @param canTerminate
   *          Can the programs terminate themselves?
   */
  public DefaultHLGPLanguage(final boolean canTerminate) {
    super(canTerminate ? produce() : FACTORIES,// 
        canTerminate ? produce2() : WEIGHTS,// 
        HLGPProgramFactory.PROGRAM_FACTORY);
  }

  /**
   * create the node factories
   * 
   * @return the node factories
   */
  private static final INodeFactory[] produce() {
    INodeFactory[] f;
    int l;

    l = FACTORIES.length;
    f = new INodeFactory[l + 1];
    System.arraycopy(FACTORIES, 0, f, 0, l);
    f[l] = TerminateFactory.TERMINATE_FACTORY;

    return f;
  }

  /**
   * create the node factories
   * 
   * @return the node factories
   */
  private static final double[] produce2() {
    double[] f;
    int l;

    l = WEIGHTS.length;
    f = new double[l + 1];
    System.arraycopy(WEIGHTS, 0, f, 0, l);
    f[l] = 1d;

    return f;
  }

  /**
   * the read resolver
   * 
   * @return the default language
   */
  private final Object readResolve() {
    return DEFAULT_LANGUAGE;
  }

  /**
   * the write resolver
   * 
   * @return the default language
   */
  private final Object writeReplace() {
    return DEFAULT_LANGUAGE;
  }
}
