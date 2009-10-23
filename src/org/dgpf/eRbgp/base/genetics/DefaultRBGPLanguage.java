/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-04-18
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.eRbgp.base.genetics.DefaultRBGPLanguage.java
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

package org.dgpf.eRbgp.base.genetics;

import org.dgpf.eRbgp.base.InternalProgramFactory;
import org.dgpf.eRbgp.base.RuleFactory;
import org.dgpf.eRbgp.base.actions.SetAction;
import org.dgpf.eRbgp.base.actions.SetActionInd;
import org.dgpf.eRbgp.base.actions.SetFactory;
import org.dgpf.eRbgp.base.expressions.ExpressionFactory;
import org.dgpf.eRbgp.base.expressions.Read;
import org.dgpf.rbgp.base.SymbolSet;
import org.sigoa.refimpl.genomes.tree.INodeFactory;
import org.sigoa.refimpl.genomes.tree.reproduction.NodeFactorySet;

/**
 * The default rbgp language
 * 
 * @author Thomas Weise
 */
public class DefaultRBGPLanguage extends NodeFactorySet {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * The node factories
   */
  private static final INodeFactory[] FACTORIES = new INodeFactory[] {
      RuleFactory.RULE_FACTORY,//
      SetFactory.SET_FACTORY,//
      SetFactory.SET_IND_FACTORY,//
      ExpressionFactory.ADD_FACTORY,//
      ExpressionFactory.AND_FACTORY,//
      ExpressionFactory.COMPARE_FACTORY,//
      ExpressionFactory.CONSTANT_FACTORY,//
      ExpressionFactory.DIV_FACTORY,//
      ExpressionFactory.MUL_FACTORY,//
      ExpressionFactory.NOT_FACTORY,//
      ExpressionFactory.OR_FACTORY,//
      ExpressionFactory.READ_FACTORY,//
      ExpressionFactory.READ_INDIRECT_FACTORY,//
      ExpressionFactory.SUB_FACTORY,//
  };

  /**
   * the weights
   */
  private static final double[] WEIGHTS = new double[] {// 
  1d,// RuleFactory.RULE_FACTORY,//
      1d,// SetFactory.SET_FACTORY,//
      0.3d,// SetFactory.SET_IND_FACTORY,//
      1d,// ExpressionFactory.ADD_FACTORY,//
      1d,// ExpressionFactory.AND_FACTORY,//
      1d,// ExpressionFactory.COMPARE_FACTORY,//
      1d,// ExpressionFactory.CONSTANT_FACTORY,//
      0.8d,// ExpressionFactory.DIV_FACTORY,//
      0.8d,// ExpressionFactory.MUL_FACTORY,//
      1d,// ExpressionFactory.NOT_FACTORY,//
      1d,// ExpressionFactory.OR_FACTORY,//
      1d,// ExpressionFactory.READ_FACTORY,//
      0.3d,// ExpressionFactory.READ_INDIRECT_FACTORY,//
      1d,// ExpressionFactory.SUB_FACTORY,//
  };

  /**
   * Create a new default rbgp language
   * 
   * @param s
   *          the symbol set
   */
  public DefaultRBGPLanguage(final SymbolSet s) {
    super(FACTORIES, WEIGHTS,
        InternalProgramFactory.INTERNAL_PROGRAM_FACTORY);

    SetAction.initSymbols(s);
    SetActionInd.initSymbols(s);
    Read.initSymbols(s);
    // ReadInd.initSymbols(s);
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
  protected DefaultRBGPLanguage(final DefaultRBGPLanguage inherit,
      final INodeFactory[] factories, final double[] weights) {
    super(inherit, factories, weights,
        InternalProgramFactory.INTERNAL_PROGRAM_FACTORY);
  }

}
