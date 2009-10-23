/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-02-18
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.machine.ll.instructions.ControlInstruction.java
 * Last modification: 2007-02-18
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

package org.dgpf.lgp.base.instructions.ctrl;

import org.dgpf.lgp.base.ECondition;
import org.dgpf.lgp.base.EIndirection;
import org.dgpf.lgp.base.ILGPParameters;
import org.dgpf.lgp.base.Instruction;
import org.dgpf.lgp.base.InstructionSet;
import org.dgpf.lgp.base.LGPMemory;
import org.dgpf.lgp.base.LGPProgram;
import org.dgpf.vm.base.VirtualMachine;

/**
 * the relative conditional call instruction
 * 
 * @param <V>
 *          the virtual machine type
 * @author Thomas Weise
 */
public abstract class ControlInstruction<V extends VirtualMachine<LGPMemory, LGPProgram>>
    extends Instruction<V> {

  /**
   * the first if string
   */
  private static final char[] IF1 = "if(".toCharArray(); //$NON-NLS-1$

  /**
   * the second if string
   */
  private static final char[] IF2 = ") ".toCharArray(); //$NON-NLS-1$

  /**
   * Create a new instruction for a instruction set.
   * 
   * @param isb
   *          the instruction set builder
   */
  protected ControlInstruction(final InstructionSet isb) {
    super(isb);
  }

  /**
   * Write this instruction's human readable representation to a string
   * builder.
   * 
   * @param param1
   *          the fist parameter
   * @param param2
   *          the second parameter
   * @param param3
   *          the third parameter
   * @param sb
   *          the string builder to write to
   */
  @Override
  public void toStringBuilder(final int param1, final int param2,
      final int param3, final StringBuilder sb) {
    ECondition e;
    EIndirection i;
    e = ECondition.decode(param1);
    if (e != ECondition.TRUE) {
      sb.append(IF1);
      i = EIndirection.decodeIndirection(param2);
      i.toStringBuilder(EIndirection.decodeIndex(i, param2), true, sb);
      sb.append(':');
      e.toStringBuilder(sb);
      sb.append(IF2);
    }
  }

  /**
   * Checks whether this instruction is useless.
   * 
   * @param condition
   *          the condition
   * @param param2
   *          the second parameter
   * @param dest
   *          the destination
   * @return <code>true</code> if and only if this instruction does
   *         nothing,<code>false</code> otherwise
   */
  protected boolean isUseless(final ECondition condition,
      final int param2, final int dest) {
    return (condition == ECondition.FALSE);
  }

  /**
   * Post-process the parameters
   * 
   * @param program
   *          the program
   * @param function
   *          the function index
   * @param index
   *          the instruction index
   * @param parameters
   *          the lgp vm parameters
   */
  @Override
  public void postProcessParameters(final int[][] program,
      final int function, final int index, final ILGPParameters parameters) {
    program[function][index + 1] = ECondition
        .reencode(program[function][index + 1]);
    program[function][index + 2] = EIndirection.reencode(
        program[function][index + 2], parameters);
  }

  /**
   * Check whether this instruction is a no-op or not.
   * 
   * @param param1
   *          the fist parameter
   * @param param2
   *          the second parameter
   * @param param3
   *          the third parameter
   * @return <code>true</code> if and only if this instruction does
   *         nothing,<code>false</code> otherwise
   */
  @Override
  public boolean isNop(final int param1, final int param2, final int param3) {
    return (ECondition.decode(param1) == ECondition.FALSE);
  }
}
