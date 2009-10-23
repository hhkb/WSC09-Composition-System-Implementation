/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-04-18
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.symbolicRegression.scalar.real.DefaultRealLanguage.java
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

package org.dgpf.symbolicRegression.scalar.real;

import org.sigoa.refimpl.genomes.tree.INodeFactory;
import org.sigoa.refimpl.genomes.tree.reproduction.NodeFactorySet;

/**
 * The default real language
 * 
 * @author Thomas Weise
 */
public class DefaultRealLanguage extends NodeFactorySet {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * The node factories
   */
  private static final INodeFactory[] FACTORIES = new INodeFactory[] {
      Add.ADD_FACTORY,//
      Constant.CONSTANT_FACTORY,//
      Div.DIV_FACTORY,//
      Abs.ABS_FACTORY,//
      Exp.EXP_FACTORY,//
      Ln.LN_FACTORY,//
      Mul.MUL_FACTORY,//
      Pwr.PWR_FACTORY,//
      Sub.SUB_FACTORY,//
      X.X_FACTORY,//
      ArcCos.ARCCOS_FACTORY,//
      ArcSin.ARCSIN_FACTORY,//
      ArcTan.ARCTAN_FACTORY,//
      Cos.COS_FACTORY,//
      Sin.SIN_FACTORY,//
      Tan.TAN_FACTORY,//
      Max.MAX_FACTORY,//
      Min.MIN_FACTORY,//
  };

  /**
   * the weights
   */
  private static final double[] WEIGHTS = new double[] {// 
  1.0d,// Add.ADD_FACTORY,//
      1.0d,// Constant.CONSTANT_FACTORY,//
      1.0d,// Div.DIV_FACTORY,//
      1.0d,// Abs.ABS_FACTORY,//
      1.0d,// Exp.EXP_FACTORY,//
      1.0d,// Ln.LN_FACTORY,//
      1.0d,// Mul.MUL_FACTORY,//
      1.0d,// Pwr.PWR_FACTORY,//
      1.0d,// Sub.SUB_FACTORY,//
      1.0d,// X.X_FACTORY,//
      1.0d,// ArcCos.ARCCOS_FACTORY,//
      1.0d,// ArcSin.ARCSIN_FACTORY,//
      1.0d,// ArcTan.ARCTAN_FACTORY,//
      1.0d,// Cos.COS_FACTORY,//
      1.0d,// Sin.SIN_FACTORY,//
      1.0d,// Tan.TAN_FACTORY,//
      1.0d,// Max.MAX_FACTORY,//
      1.0d,// Min.MIN_FACTORY,//
  };

  /**
   * the default real language
   */
  public static final DefaultRealLanguage DEFAULT_REAL_LANGUAGE = new DefaultRealLanguage();

  /**
   * Create a new default rbgp language
   */
  public DefaultRealLanguage() {
    super(FACTORIES, WEIGHTS, null);// X.X_FACTORY);
  }

  /**
   * Create a new node factory set.
   * 
   * @param inherit
   *          a selector to inherite from
   * @param factories
   *          the factories to select from
   * @param weights
   *          their weights
   */
  protected DefaultRealLanguage(final DefaultRealLanguage inherit,
      final INodeFactory[] factories, final double[] weights) {
    super(inherit, factories, weights, null);//X.X_FACTORY);
  }

}
