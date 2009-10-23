/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-01
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.hlgp.base.Construct.java
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
import org.dgpf.hlgp.base.constructs.Call;
import org.dgpf.hlgp.base.constructs.instructions.DeclVar;
import org.sigoa.refimpl.genomes.tree.Node;

/**
 * The base class for constructs
 * 
 * @author Thomas Weise
 */
public abstract class Construct extends Node {
  /**
   * Create a new construct
   * 
   * @param children
   *          the child nodes
   */
  public Construct(final Node[] children) {
    super(children);
  }

  /**
   * Append an access to a memory cell to a strinb builder.
   * 
   * @param index
   *          the index
   * @param sb
   *          the string builder
   */
  protected static final void appendAccess(final int index,
      final StringBuilder sb) {
    sb.append('[');
    sb.append(index);
    sb.append(']');
  }

  /**
   * Compile this construct.
   * 
   * @param compiler
   *          the compiler to use
   */
  public void compile(final HLGPCompiler compiler) {
    int s, i;
    Node n;

    this.declareVariables(compiler);

    s = this.size();
    for (i = 0; i < s; i++) {
      n = this.get(i);
      if ((n instanceof Construct) && (!(n instanceof DeclVar))) {
        ((Construct) n).compile(compiler);
        if (n instanceof Call)
          compiler.pendingPop();
      }
    }
  }

  /**
   * Obtain the count of parameters of a function
   * 
   * @param func
   *          the function
   * @param c
   *          the compiler
   * @return the parameter count
   */
  protected int getParameterCount(final int func, final HLGPCompiler c) {
    int i, pc;

    pc = 0;
    for (i = (this.size() - 1); i >= 0; i--) {
      pc = Math.max(pc, ((Construct) (this.get(i))).getParameterCount(
          func, c));
    }

    return pc;
  }

  /**
   * Parse this node list and declare all variables.
   * 
   * @param c
   *          the compiler
   */
  protected final void declareVariables(final HLGPCompiler c) {
    int s, i;
    Node n;

    s = this.size();
    for (i = 0; i < s; i++) {
      n = this.get(i);
      if (n instanceof DeclVar)
        ((DeclVar) n).declare(c);
    }
  }
}
