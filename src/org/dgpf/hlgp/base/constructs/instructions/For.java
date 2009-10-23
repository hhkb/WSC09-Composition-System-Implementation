/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-01
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.machine.hl.constructs.instructions.While.java
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
import org.dgpf.lgp.base.EIndirection;
import org.dgpf.lgp.base.programBuilder.Variable;
import org.sfc.text.TextUtils;
import org.sigoa.refimpl.genomes.tree.INodeFactory;
import org.sigoa.refimpl.genomes.tree.Node;

/**
 * This instruction is a for loop
 * 
 * @author Thomas Weise
 */
public class For extends Instruction {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /** the for-text */
  private static final char[] FOR = "for ".toCharArray(); //$NON-NLS-1$

  /** the different for-types */
  static final ECondition[] FOR_TYPES = new ECondition[] {
      ECondition.GREATER, ECondition.ABOVE, ECondition.GREATER_OR_EQUAL,
      ECondition.ABOVE_OR_EQUAL, ECondition.LESS_OR_EQUAL,
      ECondition.BELOW_OR_EQUAL, ECondition.LESS, ECondition.BELOW,

  };

  /** the direction */
  ECondition m_dir;

  /**
   * Create a new while instruction
   * 
   * @param children
   *          the child nodes
   * @param dir
   *          the loop direction
   */
  public For(final ECondition dir, final Node[] children) {
    super(children);
    this.m_dir = dir;
  }

  /**
   * Serializes the parameters of the constructor of this object.
   * 
   * @param sb
   *          the string builder
   * @param indent
   *          an optional parameter denoting the indentation
   */
  @Override
  protected void javaParametersToStringBuilder(final StringBuilder sb,
      final int indent) {
    if (this.m_dir == null)
      sb.append((Object) null);
    else
      this.m_dir.appendQualifiedName(sb);
    sb.append(',');
    sb.append(' ');
    super.javaParametersToStringBuilder(sb, indent);
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
    if (c >= 3) {
      sb.append(FOR);
      this.get(0).toStringBuilder(sb);
      sb.append(' ');
      sb.append('[');
      this.m_dir.toStringBuilder(sb);
      sb.append(']');
      sb.append(' ');
      this.get(1).toStringBuilder(sb);

      sb.append(' ');
      sb.append('{');
      if (c > 1) {
        i = (indent + 2);
        sb.append(TextUtils.LINE_SEPARATOR);
        TextUtils.appendSpaces(sb, i);
        nodeToStringBuilder(this.get(2), sb, i);
        sb.append(TextUtils.LINE_SEPARATOR);
        TextUtils.appendSpaces(sb, indent);
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
    return ForFactory.FOR_FACTORY;
  }

  /**
   * Compile this construct.
   * 
   * @param compiler
   *          the compiler to use
   */
  @Override
  public void compile(final HLGPCompiler compiler) {
    Construct c;
    Variable v, t, r;
    int start, cr, jp, p;
    boolean alloc;
    ECondition ccc;

    this.declareVariables(compiler);

    if (this.size() > 2) {

      ccc = this.m_dir;

      compiler.beginConstruct();
      v = compiler.allocateVariable(EIndirection.LOCAL, 0, false);
      c = ((Construct) (this.get(0)));
      if (c instanceof Expression) {
        t = ((Expression) c).provideResultVariable(compiler);
      } else
        t = null;
      if (t != null) {
        compiler.move(v, t);
        c.compile(compiler);
      } else {
        compiler.pushTarget(v);
        c.compile(compiler);
        compiler.popTarget();
      }

      start = compiler.getPosition();

      c = ((Construct) (this.get(1)));
      if (c instanceof Expression) {
        t = ((Expression) c).provideResultVariable(compiler);
      } else
        t = null;

      compiler.beginConstruct();

      if (t == null) {
        t = compiler.allocateTarget();
        alloc = true;
      } else
        alloc = false;

      c.compile(compiler);

      if (alloc) {
        compiler.popTarget();
      }

      r = compiler.allocateVariable(EIndirection.LOCAL, 0, true);
      compiler.compare(r, v, t);
      cr = r.encode();

      jp = compiler.addDummy();// compiler.jump(ECondition.TRUE, r, 0);
      compiler.endConstruct();

      ((Construct) (this.get(2))).compile(compiler);

      switch (ccc) {
      case NC_AND_NZ:
      case NC:
      case N_Z_AND_S_EXOR_O:
      case S_EXOR_O: {
        p = compiler.sub(v, v, Variable.ONE);
        break;
      }
      default: {
        p = compiler.add(v, v, Variable.ONE);
        break;
      }
      }

      // p = (compiler.getPosition() + 1);
      compiler.jump(ECondition.TRUE, Variable.NULL, start - p - 1);
      compiler.replaceJump(jp, ccc.invert(), cr, p - jp + 2);

      compiler.endConstruct();

    }
  }
}
