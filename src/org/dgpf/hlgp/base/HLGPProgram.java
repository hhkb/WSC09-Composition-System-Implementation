/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-01
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.hlgp.base.HLGPProgram.java
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

package org.dgpf.hlgp.base;

import org.dgpf.hlgp.base.compiler.HLGPCompiler;
import org.dgpf.lgp.base.EIndirection;
import org.sfc.text.TextUtils;
import org.sigoa.refimpl.genomes.tree.INodeFactory;
import org.sigoa.refimpl.genomes.tree.Node;

/**
 * The root for all high-level programs
 * 
 * @author Thomas Weise
 */
public class HLGPProgram extends Instruction {

  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The empty program
   */
  public static final HLGPProgram EMPTY_PROGRAM = new HLGPProgram(null);

  /**
   * Create a new node
   * 
   * @param children
   *          the child nodes
   */
  public HLGPProgram(final Node[] children) {
    super(children);
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
    return HLGPProgramFactory.PROGRAM_FACTORY;
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
    int i, j, k;
    Construct c;

    j = this.size();
    if (j <= 0) {
      sb.append('{');
      sb.append('}');
    } else {
      k = 0;
      for (i = 0; i < j; i++) {
        if (i > 0)
          sb.append(TextUtils.LINE_SEPARATOR);
        c = ((Construct) (this.get(i)));
        if (c instanceof Function) {
          ((Function) c).doToStringBuilder(sb, k++);
        } else {
          Node.nodeToStringBuilder(c, sb, indent);
        }
      }
    }
  }

  /**
   * Compile this construct.
   * 
   * @param compiler
   *          the compiler to use
   */
  @Override
  public void compile(final HLGPCompiler compiler) {
    int s, i, j, fc;
    Node n;

    this.declareVariables(compiler);

    if (compiler.getVariableCount() <= 0) {
      compiler.allocateVariable(EIndirection.GLOBAL, 0, false);
    }

    s = this.size();
    fc = 0;

    for (i = (s - 1); i >= 0; i--) {
      if (this.get(i) instanceof Function) {
        compiler.declareFunction();
        fc++;
      }
    }

    for (i = (fc - 1); i >= 0; i--) {
      compiler.getFunction(i).setParameterCount(
          this.getParameterCount(i, compiler));
    }

    j = 0;
    for (i = 0; i < s; i++) {
      n = this.get(i);
      if (n instanceof Function) {
        compiler.beginFunction(compiler.getFunction(j++));
        ((Function) n).compile(compiler);
        compiler.endFunction();
      }
    }
  }
}
