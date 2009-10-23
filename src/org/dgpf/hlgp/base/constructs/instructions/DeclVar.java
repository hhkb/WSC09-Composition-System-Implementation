/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-01
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.machine.hl.constructs.instructions.DeclVar.java
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

import org.dgpf.hlgp.base.Instruction;
import org.dgpf.hlgp.base.compiler.HLGPCompiler;
import org.dgpf.lgp.base.EIndirection;
import org.dgpf.lgp.base.programBuilder.Variable;
import org.sigoa.refimpl.genomes.tree.INodeFactory;

/**
 * This instruction declares a variable
 * 
 * @author Thomas Weise
 */
public class DeclVar extends Instruction {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the declarative text
   */
  private static final char[] TEXT = "decl ".toCharArray(); //$NON-NLS-1$

  /**
   * the indirection declaration
   */
  private final EIndirection m_indir;

  /**
   * the index
   */
  private final int m_index;

  /**
   * Create a new store instruction
   * 
   * @param indir
   *          the indirection
   * @param index
   *          the index
   */
  public DeclVar(final EIndirection indir, final int index) {
    super(null);
    this.m_indir = indir;
    this.m_index = index;
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
    if (this.m_indir == null)
      sb.append((Object) null);
    else
      this.m_indir.appendQualifiedName(sb);
    sb.append(',');
    sb.append(' ');
    sb.append(this.m_index);
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
    sb.append(TEXT);
    sb.append(this.m_indir.name());
    sb.append(':');
    sb.append(this.m_index);
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
    return DeclVarFactory.DECL_VAR_FACTORY;
  }

  /**
   * Check whether this node equals another object.
   * 
   * @param o
   *          the other object
   * @return <code>true</code> if the other object equals this node
   */
  @Override
  public boolean equals(final Object o) {
    DeclVar f;
    if (super.equals(o)) {
      f = ((DeclVar) o);
      return ((f.m_index == this.m_index) && (f.m_indir == this.m_indir));
    }
    return false;
  }

  /**
   * declare this variable
   * 
   * @param c
   *          the compiler to declare to
   */
  public void declare(final HLGPCompiler c) {
    Variable v;
    EIndirection id;

    switch (this.m_indir) {

    case LOCAL_LOCAL:
    case LOCAL_GLOBAL: {
      v = c
          .resolveTypedVariable(
              this.m_index,
              (this.m_indir == EIndirection.LOCAL_GLOBAL) ? EIndirection.GLOBAL
                  : EIndirection.LOCAL);
      if (v != null) {
        id = v.getIndirection();
        if ((id == EIndirection.LOCAL)
            || (id == EIndirection.GLOBAL_LOCAL)) {
          c
              .allocateVariable(EIndirection.LOCAL_LOCAL, v.getIndex(),
                  false);
          break;
        }
        if ((id == EIndirection.GLOBAL)
            || (id == EIndirection.GLOBAL_GLOBAL)) {
          c.allocateVariable(EIndirection.LOCAL_GLOBAL, v.getIndex(),
              false);
          break;
        }
      }
      c.allocateVariable(EIndirection.LOCAL, this.m_index, false);
      break;
    }

    case GLOBAL_LOCAL:
    case GLOBAL_GLOBAL: {
      v = c
          .resolveTypedVariable(
              this.m_index,
              (this.m_indir == EIndirection.GLOBAL_GLOBAL) ? EIndirection.GLOBAL
                  : EIndirection.LOCAL);
      if (v != null) {
        id = v.getIndirection();
        if ((id == EIndirection.LOCAL)
            || (id == EIndirection.GLOBAL_LOCAL)) {
          c.allocateVariable(EIndirection.GLOBAL_LOCAL, v.getIndex(),
              false);
          break;
        }
        if ((id == EIndirection.GLOBAL)
            || (id == EIndirection.GLOBAL_GLOBAL)) {
          c.allocateVariable(EIndirection.GLOBAL_GLOBAL, v.getIndex(),
              false);
          break;
        }
      }
      c.allocateVariable(EIndirection.GLOBAL, this.m_index, false);
      break;
    }

    default: {
      c.allocateVariable(this.m_indir, this.m_index, false);
      break;
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
    //
  }
}
