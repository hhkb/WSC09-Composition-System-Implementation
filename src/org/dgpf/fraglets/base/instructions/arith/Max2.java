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
 * <code> [max2 X] [X a] [X b] => [X ((a>b)?a:b) tail] </code>
 * 
 * @author Thomas Weise
 */
public class Max2 extends
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
  public Max2(final InstructionSet is) {
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

    Fraglet s1, s2;
    int[] nd;
    int l, c, i1, i2;
    boolean b;

    l = fraglet.m_len;
    if (l <= 1)
      return true;

    c = fraglet.m_code[1];
    s1 = vm.m_memory.findFraglet(c, true);
    s2 = vm.m_memory.findFraglet(c, true);

    if (s1 == null) {
      if (s2 != null) {
        vm.m_memory.enterFraglet(s2, vm);
      }
      return false;
    }

    if (s2 == null) {
      vm.m_memory.enterFraglet(s1, vm);
      return false;
    }

    b = false;

    nd = s1.m_code;
    i2 = Integer.MIN_VALUE;
    for (l = 1; l < s1.m_len; l++) {
      i1 = nd[l];
      if (!(InstructionUtils.isInstruction(i1))) {
        i2 = InstructionUtils.decodeData(i1);
        b = true;
        break;
      }
    }

    nd = s2.m_code;
    for (l = 1; l < s2.m_len; l++) {
      i1 = nd[l];
      if (!(InstructionUtils.isInstruction(i1))) {
        i2 = Math.max(i2, InstructionUtils.decodeData(i1));
        b = true;
        break;
      }
    }

    vm.m_memory.dispose(s1);
    vm.m_memory.dispose(s2);

    if (b) {
      s1 = vm.m_memory.allocate();
      if (s1 != null) {
        s1.m_code[0] = c;
        s1.m_code[1] = InstructionUtils.encodeData(i2);
        s1.m_len = 2;
        vm.m_memory.enterFraglet(s1, vm);

        l = fraglet.m_len;
        if (l > 2) {
          s1 = vm.m_memory.allocate();
          if (s1 != null) {
            l -= 2;
            System.arraycopy(fraglet.m_code, 2, s1.m_code, 0, l);
            s1.m_len = l;
            vm.m_memory.enterFraglet(s1, vm);
          } else
            vm.setError();
        }
      } else
        vm.setError();
    }

    return false;
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
    sb.append("max2"); //$NON-NLS-1$
  }
}
