/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-04-16
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.eRbgp.base.InternalProgram.java
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

import org.dgpf.rbgp.base.RBGPMemory;
import org.dgpf.rbgp.base.RBGPProgramBase;
import org.dgpf.vm.base.VirtualMachine;
import org.sfc.text.TextUtils;
import org.sigoa.refimpl.genomes.tree.INodeFactory;
import org.sigoa.refimpl.genomes.tree.Node;

/**
 * the internal tree rbgp program
 * 
 * @author Thomas Weise
 */
public class InternalProgram extends
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
  public InternalProgram(final Node[] children) {
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
    return InternalProgramFactory.INTERNAL_PROGRAM_FACTORY;
  }

  /**
   * Execute this construct
   * 
   * @param vm
   *          the virtual machine
   * @return an arbitrary return value
   */
  @Override
  public int execute(final VirtualMachine<RBGPMemory, RBGPProgramBase> vm) {
    int i, ret, nr;

    ret = 0;
    for (i = (this.size() - 1); i >= 0; i--) {
      nr = ((Rule) (this.get(i))).execute(vm);
      if (nr > ret)
        ret = nr;
    }

    return ret;
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
    int i;
    for (i = (this.size() - 1); i >= 0; i--) {
      this.get(i).toStringBuilder(sb);
      if (i > 0)
        sb.append(TextUtils.LINE_SEPARATOR);
    }
  }
}
