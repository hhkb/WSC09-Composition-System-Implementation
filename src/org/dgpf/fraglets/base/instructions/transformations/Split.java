/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-15
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.fraglets.base.instructions.transformations.Split.java
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

package org.dgpf.fraglets.base.instructions.transformations;

import org.dgpf.fraglets.base.Fraglet;
import org.dgpf.fraglets.base.FragletProgram;
import org.dgpf.fraglets.base.FragletStore;
import org.dgpf.fraglets.base.Instruction;
import org.dgpf.fraglets.base.InstructionSet;
import org.dgpf.fraglets.base.InstructionUtils;
import org.dgpf.vm.base.VirtualMachine;

/**
 * The split instruction splits a fraglet into two.
 * <code>[split seq1 * seq2] => [seq1], [seq2]</code>
 * 
 * @author Thomas Weise
 */
public class Split extends
    Instruction<VirtualMachine<FragletStore, FragletProgram>> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new split instruction
   * 
   * @param is
   *          the instruction set
   */
  public Split(final InstructionSet is) {
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
    int l, sp, x;
    final int mi, s;
    final InstructionSet is;

    l = fraglet.m_len;
    if (l <= 1)
      return true;

    is = vm.m_memory.m_instructions;
    s = is.size();
    if (s <= 0)
      return true;

    mi = is.getMarkerId();

    od = fraglet.m_code;

    outer: {
      for (sp = 0; sp < l; sp++) {
        x = od[sp];
        if (InstructionUtils.isInstruction(x)) {
          if (InstructionUtils.decodeInstruction(x, s) == mi)
            break outer;
        }
      }
      return true;
    }

    fn = vm.m_memory.allocate();
    if (fn == null) {
      vm.setError();
      return true;
    }

    nd = fn.m_code;

    if (sp == 1) {
      l -= 2;
      System.arraycopy(od, 2, nd, 0, l);
      fn.m_len = l;
      vm.m_memory.enterFraglet(fn, vm);
      return true;
    }

    if (sp == (l - 1)) {
      l -= 2;
      System.arraycopy(od, 1, nd, 0, l);
      fn.m_len = l;
      vm.m_memory.enterFraglet(fn, vm);
      return true;
    }

    System.arraycopy(od, 1, nd, 0, sp - 1);
    fn.m_len = (sp - 1);
    vm.m_memory.enterFraglet(fn, vm);

    fn = vm.m_memory.allocate();
    if (fn == null) {
      vm.setError();
      return true;
    }

    sp++;
    l -= sp;
    System.arraycopy(od, sp, fn.m_code, 0, l);
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
    sb.append("split"); //$NON-NLS-1$
  }
}
