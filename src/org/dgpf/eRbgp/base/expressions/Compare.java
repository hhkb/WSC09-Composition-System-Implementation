/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-04-17
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.eRbgp.base.expressions.And.java
 * Last modification: 2008-04-17
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

package org.dgpf.eRbgp.base.expressions;

import org.dgpf.eRbgp.base.Construct;
import org.dgpf.rbgp.base.EComparison;
import org.dgpf.rbgp.base.RBGPMemory;
import org.dgpf.rbgp.base.RBGPProgramBase;
import org.dgpf.vm.base.VirtualMachine;
import org.sigoa.refimpl.genomes.tree.INodeFactory;
import org.sigoa.refimpl.genomes.tree.Node;

/**
 * the add
 * 
 * @author Thomas Weise
 */
public class Compare extends Expression {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the comparison
   */
  EComparison m_comparison;

  /**
   * Create a new node
   * 
   * @param children
   *          the child nodes
   * @param comparison
   *          the comparison
   */
  public Compare(final Node[] children, final EComparison comparison) {
    super(children);
    this.m_comparison = comparison;
  }

  /**
   * Execute this construct
   * 
   * @param vm
   *          the virtual machine
   * @return an arbitrary return value
   */
  @Override
  @SuppressWarnings("unchecked")
  public int execute(final VirtualMachine<RBGPMemory, RBGPProgramBase> vm) {
    return (this.m_comparison.compare(((Construct) (this.get(0)))
        .execute(vm), ((Construct) (this.get(1))).execute(vm)) ? (-1) : 0);
  }

  /**
   * Append this object's textual representation to a string builder.
   * 
   * @param sb
   *          The string builder to append to.
   * @see #toString()
   */
  @Override
  public void toStringBuilder(final StringBuilder sb) {
    final boolean b;
    final EComparison e;

    e = this.m_comparison;
    b = ((e != EComparison.FALSE) && (e != EComparison.TRUE));
    if (b) {
      sb.append('(');
      this.get(0).toStringBuilder(sb);
    }

    e.toStringBuilder(sb);

    if (b) {
      this.get(1).toStringBuilder(sb);
      sb.append(')');
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
    return ExpressionFactory.ADD_FACTORY;
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
    super.javaParametersToStringBuilder(sb, indent);
    sb.append(", "); //$NON-NLS-1$
    this.m_comparison.javaToStringBuilder(sb, indent);
  }

  /**
   * check for equality
   * 
   * @param o
   *          the object to compare with
   * @return <code>true</code> if and only if the two objects equal
   */
  @Override
  public boolean equals(final Object o) {
    if (!(super.equals(o)))
      return false;
    return ((Compare) o).m_comparison.equals(this.m_comparison);
  }
}
