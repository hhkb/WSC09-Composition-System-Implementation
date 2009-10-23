/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-01
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.machine.hl.constructs.instructions.Store.java
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
import org.dgpf.lgp.base.programBuilder.Variable;
import org.sigoa.refimpl.genomes.tree.INodeFactory;
import org.sigoa.refimpl.genomes.tree.Node;

/**
 * This instruction stores a value from an expression into a variable
 * 
 * @author Thomas Weise
 */
public class Store extends Instruction {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the index
   */
  private final int m_index;

  /**
   * Create a new store instruction
   * 
   * @param index
   *          the index of the variable
   * @param what
   *          the value to be stored
   */
  public Store(final int index, final Node[] what) {
    super(what);
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
    sb.append(this.m_index);
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
    appendAccess(this.m_index, sb);
    sb.append(' ');
    sb.append('=');
    sb.append(' ');
    this.get(0).toStringBuilder(sb);
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
    return StoreFactory.STORE_FACTORY;
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
    return super.equals(o) && (((Store) o).m_index == this.m_index);
  }

  /**
   * Compile this construct.
   * 
   * @param compiler
   *          the compiler to use
   */
  @Override
  public void compile(final HLGPCompiler compiler) {
    Variable s, d;
    Construct e;

    if (this.size() >= 1) {

      e = ((Construct) (this.get(0)));
      if (e instanceof Expression) {
        s = ((Expression) e).provideResultVariable(compiler);
      } else
        s = null;

      d = compiler.resolveVariable(this.m_index);

      if (s != null) {
        compiler.move(d, s);
      } else {
        compiler.pushTarget(d);
        e.compile(compiler);
        compiler.popTarget();
      }
    }
  }
}
