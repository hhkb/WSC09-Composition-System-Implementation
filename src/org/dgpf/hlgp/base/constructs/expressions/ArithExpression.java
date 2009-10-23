/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-06
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.machine.hl.constructs.expressions.ArithExpression.java
 * Last modification: 2007-03-06
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

import org.dgpf.hlgp.base.Construct;
import org.dgpf.hlgp.base.Expression;
import org.dgpf.hlgp.base.compiler.HLGPCompiler;
import org.dgpf.lgp.base.programBuilder.Variable;
import org.sigoa.refimpl.genomes.tree.Node;

/**
 * The arith expression
 * 
 * @author Thomas Weise
 */
public abstract class ArithExpression extends Expression {
  /**
   * Create a new arithmetic expression
   * 
   * @param children
   *          the child nodes
   */
  public ArithExpression(final Node[] children) {
    super(children);
  }

  /**
   * Perform the compilation
   * 
   * @param dest
   *          the destination variable
   * @param src1
   *          the first source variable
   * @param src2
   *          the second source variable
   * @param c
   *          the compiler
   */
  protected abstract void doCompile(final Variable dest,
      final Variable src1, final Variable src2, final HLGPCompiler c);

  /**
   * Compile this construct.
   * 
   * @param compiler
   *          the compiler to use
   */
  @Override
  public void compile(final HLGPCompiler compiler) {
    Variable d, s1, s2;
    boolean b1, b2;
    Construct n;

    if (this.size() >= 2) {
      d = this.provideResultVariable(compiler);
      if (d == null)
        d = compiler.getTarget();

      n = ((Construct) (this.get(0)));
      if (n instanceof Expression) {
        s1 = ((Expression) n).provideResultVariable(compiler);
      } else
        s1 = null;

      if (s1 == null) {
        b1 = true;
        compiler.beginConstruct();
        s1 = compiler.allocateTarget();
      } else
        b1 = false;

      n.compile(compiler);

      if (b1)
        compiler.popTarget();

      n = ((Construct) (this.get(1)));
      if (n instanceof Expression) {
        s2 = ((Expression) n).provideResultVariable(compiler);
      } else
        s2 = null;

      if (s2 == null) {
        b2 = true;
        if (!b1)
          compiler.beginConstruct();
        s2 = compiler.allocateTarget();
      } else
        b2 = false;

      n.compile(compiler);

      if (b2)
        compiler.popTarget();

      this.doCompile(d, s1, s2, compiler);

      if (b1 || b2)
        compiler.endConstruct();
    }
  }

}
