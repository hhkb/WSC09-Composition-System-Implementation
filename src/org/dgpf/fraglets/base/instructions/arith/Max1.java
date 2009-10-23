/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-15
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.fraglets.base.instructions.arith.Max1.java
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

package org.dgpf.fraglets.base.instructions.arith;

import org.dgpf.fraglets.base.Fraglet;
import org.dgpf.fraglets.base.FragletProgram;
import org.dgpf.fraglets.base.FragletStore;
import org.dgpf.fraglets.base.Instruction;
import org.dgpf.fraglets.base.InstructionSet;
import org.dgpf.fraglets.base.InstructionUtils;
import org.dgpf.vm.base.VirtualMachine;

/**
 * The max instruction computes the maximum of two values.
 * <code> [max X n1 n2 tail] => [X ((n1>n2)?n1:n2) tail] </code>
 * 
 * @author Thomas Weise
 */
public class Max1 extends
    Instruction<VirtualMachine<FragletStore, FragletProgram>> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new max instruction
   * 
   * @param is
   *          the instruction set
   */
  public Max1(final InstructionSet is) {
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
  public boolean execute(
      final VirtualMachine<FragletStore, FragletProgram> vm,
      final Fraglet fraglet) {

    Fraglet fn;
    int[] od, nd;
    int l, i1, i2;

    l = fraglet.m_len;
    if (l <= 1)
      return true;

    fn = vm.m_memory.allocate();
    if (fn == null) {
      vm.setError();
      return true;
    }

    od = fraglet.m_code;
    for (i1 = 1; i1 < l; i1++) {
      if (!(InstructionUtils.isInstruction(od[i1])))
        break;
    }

    for (i2 = (i1 + 1); i2 < l; i2++) {
      if (!(InstructionUtils.isInstruction(od[i2])))
        break;
    }
    nd = fn.m_code;
    if (i2 >= l) {
      System.arraycopy(od, 1, nd, 0, fn.m_len = (l - 1));
    } else {
      System.arraycopy(od, 1, nd, 0, i1 - 1);
      nd[i1 - 1] = InstructionUtils.encodeData(Math.max(InstructionUtils
          .decodeData(od[i1]), InstructionUtils.decodeData(od[i2])));
      System.arraycopy(od, i1 + 1, nd, i1, i2 - i1 - 1);
      System.arraycopy(od, i2 + 1, nd, i2 - 2, l - i2 - 1);
    }

    vm.m_memory.enterFraglet(fn, vm);
    return true;

    // fn = vm.m_memory.allocate();
    // if (fn == null) {
    // vm.setError();
    // return true;
    // }
    //
    // od = fraglet.m_code;
    // nd = fn.m_code;
    //
    // nd[0] = od[1];
    // if (l > 2) {
    // if (l == 3) {
    // nd[1] = od[2];
    // } else {
    // v1 = od[2];
    // v2 = od[3];
    //
    // if (!(InstructionUtils.isInstruction(v1))) {
    // v1 = InstructionUtils.decodeData(v1);
    // d = true;
    // } else
    // d = false;
    //
    // if (!(InstructionUtils.isInstruction(v2))) {
    // v2 = InstructionUtils.decodeData(v2);
    // d = true;
    // }
    //
    // v1 = Math.max(v1, v2);
    // if (d)
    // v1 = InstructionUtils.encodeData(v1);
    //
    // nd[1] = v1;
    //
    // System.arraycopy(od, 4, nd, 2, l - 4);
    // }
    // fn.m_len = (l - 2);
    // } else
    // fn.m_len = 1;
    //
    // vm.m_memory.enterFraglet(fn, vm);
    // return true;
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
    sb.append("max1"); //$NON-NLS-1$
  }
}
