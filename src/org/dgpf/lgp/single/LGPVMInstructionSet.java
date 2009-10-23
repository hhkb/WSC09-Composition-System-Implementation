/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-18
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.lgp.single.LGPVMInstructionSet.java
 * Last modification: 2007-11-18
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

package org.dgpf.lgp.single;

import org.dgpf.lgp.base.InstructionSet;
import org.dgpf.lgp.single.instructions.ctrl.Call;
import org.dgpf.lgp.single.instructions.ctrl.JumpRelative;
import org.dgpf.lgp.single.instructions.ctrl.Terminate;

/**
 * The instruction set for single vms.
 * 
 * @author Thomas Weise
 */
public class LGPVMInstructionSet extends InstructionSet {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the default instruction set.
   */
  @SuppressWarnings("unchecked")
  public static final LGPVMInstructionSet DEFAULT_SINGLE_VM_INSTRUCTION_SET = new LGPVMInstructionSet(
      false) {
    private static final long serialVersionUID = 1;

    private final Object readResolve() {
      return DEFAULT_SINGLE_VM_INSTRUCTION_SET;
    }

    private final Object writeReplace() {
      return DEFAULT_SINGLE_VM_INSTRUCTION_SET;
    }
  };

  /**
   * Create a new instruction set.
   * 
   * @param canTerminate
   *          can programs terminate themselves?
   */
  public LGPVMInstructionSet(final boolean canTerminate) {
    super(canTerminate);
  }

  /**
   * Add the default instructions to this instruction set.
   * 
   * @param canTerminate
   *          can programs terminate themselves?
   */
  @Override
  protected void addDefaultInstructions(final boolean canTerminate) {
    super.addDefaultInstructions(canTerminate);
    new Call(this);
    new JumpRelative(this);
    if (canTerminate)
      new Terminate(this);
  }
}
