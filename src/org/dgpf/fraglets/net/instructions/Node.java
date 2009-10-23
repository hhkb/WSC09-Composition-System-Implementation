/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-15
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.fraglets.net.instructions.Node.java
 * Last modification: 2007-12-15
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

package org.dgpf.fraglets.net.instructions;

import org.dgpf.fraglets.base.Fraglet;
import org.dgpf.fraglets.base.Instruction;
import org.dgpf.fraglets.base.InstructionSet;
import org.dgpf.fraglets.base.InstructionUtils;
import org.dgpf.fraglets.net.FragletNetVM;

/**
 * The inserts the id of a node into a fraglet.
 * 
 * @author Thomas Weise
 */
public class Node extends Instruction<FragletNetVM> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new dup instruction
   * 
   * @param is
   *          the instruction set
   */
  public Node(final InstructionSet is) {
    super(is);
  }

  /**
   * Execute an instruction on a fraglet store.
   * 
   * @param vm
   *          the virtual machine
   * @param fraglet
   *          the invoking fraglet
   * @return <code>true</code> if the fraglet was destroyed during the
   *         execution of the instruction,<code>false</code> if it
   *         remains as-is in the fraglet store.
   */
  @Override
  public boolean execute(final FragletNetVM vm, final Fraglet fraglet) {
    Fraglet fn;
    int[] od, nd;
    int l;

    l = fraglet.m_len;
    if (l <= 1)
      return true;

    fn = vm.m_memory.allocate();
    if (fn == null) {
      vm.setError();
      return true;
    }

    od = fraglet.m_code;
    nd = fn.m_code;

    nd[0] = od[1];
    if (l > 2) {
      System.arraycopy(od, 2, nd, 2, l - 2);
    }

    nd[1] = InstructionUtils.encodeData(vm.getId());

    fn.m_len = l;

    vm.m_memory.enterFraglet(fn, vm);
    return true;
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
    sb.append("node"); //$NON-NLS-1$
  }
}
