/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-05-27
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.lcs.classifier.actions.SetAction.java
 * Last modification: 2007-05-27
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

package org.dgpf.rbgp.base.actions;

import org.dgpf.rbgp.base.Action;
import org.dgpf.rbgp.base.ActionSet;
import org.dgpf.rbgp.base.RBGPMemory;
import org.dgpf.rbgp.base.Symbol;
import org.dgpf.vm.base.EVirtualMachineState;
import org.dgpf.vm.base.VirtualMachine;
import org.dgpf.vm.base.VirtualMachineProgram;
import org.sfc.utils.Utils;

/**
 * The set action.
 * 
 * @author Thomas Weise
 */
public class SetAction
    extends
    Action<VirtualMachine<RBGPMemory, ? extends VirtualMachineProgram<RBGPMemory>>> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new action.
   * 
   * @param actionSet
   *          The action set
   */
  public SetAction(final ActionSet actionSet) {
    super(actionSet);
  }

  /**
   * Perform this action on a given <code>RBGPNetVM</code>
   * 
   * @param sym1
   *          the first parameter symbol
   * @param sym2
   *          the second parameter symbol
   * @param vm
   *          the virtual machine
   * @return the resulting virtual machine state
   */
  @Override
  public EVirtualMachineState perform(
      final Symbol sym1,
      final Symbol sym2,
      final VirtualMachine<RBGPMemory, ? extends VirtualMachineProgram<RBGPMemory>> vm) {
    int[] i;
    int k, l;

    if (sym1.m_writeProtected)
      return EVirtualMachineState.ERROR;

    k = vm.m_memory.m_mem1[sym2.m_id];
    i = vm.m_memory.m_mem2;
    l = sym1.m_id;
    if (i[l] != k) {
      i[l] = k;
      return EVirtualMachineState.CHANGED;
    }

    return EVirtualMachineState.NOTHING;
  }

  /**
   * Append this object's textual representation to a string builder.
   * 
   * @param sb
   *          The string builder to append to.
   * @param sym1
   *          the first parameter symbol
   * @param sym2
   *          the second parameter symbol
   * @see #toString()
   */
  @Override
  public final void toStringBuilder(final StringBuilder sb,
      final Symbol sym1, final Symbol sym2) {
    sym1.toStringBuilder(sb);
    sb.append(AddAction.TP1);
    sb.append('=');
    sym2.toStringBuilder(sb);
    if (!(sym2.isWriteProtected()))
      sb.append(AddAction.T);
  }

  /**
   * Check whether this action is useless.
   * 
   * @param sym1
   *          the first parameter symbol
   * @param sym2
   *          the second parameter symbol
   * @return <code>true</code> if this action is useless,<code>false</code>
   *         otherwise
   */
  @Override
  public final boolean isUseless(final Symbol sym1, final Symbol sym2) {
    return (super.isUseless(sym1, sym2) || Utils.testEqual(sym1, sym2));
  }
}
