/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-01
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.machine.hl.constructs.instructions.IfThenElse.java
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

package org.dgpf.hlgp.base.constructs.instructions;

import org.dgpf.hlgp.base.Construct;
import org.dgpf.hlgp.base.Expression;
import org.dgpf.hlgp.base.Instruction;
import org.dgpf.hlgp.base.compiler.HLGPCompiler;
import org.dgpf.lgp.base.ECondition;
import org.dgpf.lgp.base.programBuilder.Variable;
import org.sfc.text.TextUtils;
import org.sigoa.refimpl.genomes.tree.INodeFactory;
import org.sigoa.refimpl.genomes.tree.Node;

/**
 * This instruction is an if-then-else construct
 * 
 * @author Thomas Weise
 */
public class IfThenElse extends Instruction {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /** the if-text */
  private static final char[] IF = "if(".toCharArray(); //$NON-NLS-1$

  /** the else-text */
  private static final char[] ELSE = "} else {".toCharArray(); //$NON-NLS-1$

  /**
   * Create a new if-then-else instruction
   * 
   * @param children
   *          the child nodes
   */
  public IfThenElse(final Node[] children) {
    super(children);
  }

  /**
   * Transform this program into its human readable representation.
   * 
   * @param sb
   *          the string builder to write to
   * @param indent
   *          the indent
   */
  @Override
  protected void toStringBuilder(final StringBuilder sb, final int indent) {
    int c, i;

    c = this.size();
    if (c > 0) {
      sb.append(IF);
      this.get(0).toStringBuilder(sb);
      sb.append(')');
      sb.append(' ');
      sb.append('{');
      if (c > 1) {
        i = (indent + 2);
        sb.append(TextUtils.LINE_SEPARATOR);
        TextUtils.appendSpaces(sb, i);
        nodeToStringBuilder(this.get(1), sb, i);
        sb.append(TextUtils.LINE_SEPARATOR);
        TextUtils.appendSpaces(sb, indent);
        if (c > 2) {
          // sb.append('}');
          // sb.append(TextUtils.LINE_SEPARATOR);
          // TextUtils.appendSpaces(sb, indent);
          sb.append(ELSE);
          sb.append(TextUtils.LINE_SEPARATOR);
          TextUtils.appendSpaces(sb, i);
          nodeToStringBuilder(this.get(2), sb, i);
          sb.append(TextUtils.LINE_SEPARATOR);
          TextUtils.appendSpaces(sb, indent);
        }
      }
      sb.append('}');
    }

  }

  /**
   * Obtain the factory which deals with nodes of the same type as this
   * node.
   * 
   * @return the factory which deals with nodes of the same type as this
   *         node
   */
  @Override
  public INodeFactory getFactory() {
    return IfThenElseFactory.IF_THEN_ELSE_FACTORY;
  }

  /**
   * Compile this construct.
   * 
   * @param compiler
   *          the compiler to use
   */
  @Override
  public void compile(final HLGPCompiler compiler) {
    Construct n;
    Variable v;
    boolean b;
    int jp, jp2, ve;

    if (this.size() > 1) {
      n = ((Construct) (this.get(0)));
      if (n instanceof Expression) {
        v = ((Expression) n).provideResultVariable(compiler);
      } else
        v = null;

      if (v == null) {
        b = true;
        compiler.beginConstruct();
        v = compiler.allocateTarget();
      } else
        b = false;

      n.compile(compiler);

      jp = compiler.addDummy();
      ve = v.encode();

      if (b)
        compiler.endConstruct();

      ((Construct) (this.get(1))).compile(compiler);
      if (this.size() > 2) {
        jp2 = compiler.addDummy();
        ((Construct) (this.get(2))).compile(compiler);
      } else
        jp2 = -1;

      compiler.replaceJump(jp, ECondition.NULL, ve,
          (jp2 >= 0) ? (jp2 + 1 - jp) : compiler.getPosition() - jp);
      if (jp2 >= 0) {
        compiler.replaceJump(jp2, ECondition.TRUE, Variable.FFFF, compiler
            .getPosition()
            - jp2);
      }
    }
  }
}
