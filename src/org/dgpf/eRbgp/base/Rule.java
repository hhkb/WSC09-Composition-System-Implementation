/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-04-16
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.eRbgp.base.Rule.java
 * Last modification: 2008-04-16
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

package org.dgpf.eRbgp.base;

import org.dgpf.eRbgp.base.expressions.Expression;
import org.dgpf.rbgp.base.RBGPMemory;
import org.dgpf.rbgp.base.RBGPProgramBase;
import org.dgpf.vm.base.VirtualMachine;
import org.sigoa.refimpl.genomes.tree.INodeFactory;
import org.sigoa.refimpl.genomes.tree.Node;

/**
 * An tree rbgp rule
 * 
 * @author Thomas Weise
 */
public class Rule extends
    Construct<VirtualMachine<RBGPMemory, RBGPProgramBase>> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new node
   * 
   * @param children
   *          the child nodes
   */
  public Rule(final Node[] children) {
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
    return RuleFactory.RULE_FACTORY;
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
    if (((Expression) (this.get(0))).execute(vm) != 0) {
      return ((Construct) (this.get(1))).execute(vm);
    }
    return 0;
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
    sb.append("if "); //$NON-NLS-1$
    this.get(0).toStringBuilder(sb);
    sb.append(" then ");//$NON-NLS-1$
    this.get(1).toStringBuilder(sb);
  }
}
