/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-02-27
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.machine.ll.instructions.arith.ArithInstruction.java
 * Last modification: 2007-02-27
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

package org.dgpf.lgp.base.instructions.arith;

import org.dgpf.lgp.base.EIndirection;
import org.dgpf.lgp.base.ILGPParameters;
import org.dgpf.lgp.base.Instruction;
import org.dgpf.lgp.base.InstructionSet;
import org.dgpf.lgp.base.LGPMemory;
import org.dgpf.lgp.base.LGPProgram;
import org.dgpf.vm.base.VirtualMachine;

/**
 * the base class for arithmetic instructions
 * 
 * @author Thomas Weise
 */
public abstract class ArithInstruction extends
    Instruction<VirtualMachine<LGPMemory, LGPProgram>> {

  /**
   * Create a new instruction for a instruction set.
   * 
   * @param isb
   *          the instruction set builder
   */
  public ArithInstruction(final InstructionSet isb) {
    super(isb);
  }

  /**
   * Check if this instruction is useless on base of the indirection and
   * index values.
   * 
   * @param indir1
   *          the first indirection value
   * @param index1
   *          the first index value
   * @param indir2
   *          the second indirection value
   * @param index2
   *          the second index value
   * @param indir3
   *          the third indirection value
   * @param index3
   *          the third index value
   * @return <code>true</code> if and only if this instruction does
   *         nothing,<code>false</code> otherwise
   */
  protected boolean isUseless(final EIndirection indir1, final int index1,
      final EIndirection indir2, final int index2,
      final EIndirection indir3, final int index3) {
    return (indir1 == EIndirection.CONSTANT)
        && (indir2 != EIndirection.STACK)
        && (indir3 != EIndirection.STACK);
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
    EIndirection e1, e2, e3;
    e1 = EIndirection.decodeIndirection(param1);
    e2 = EIndirection.decodeIndirection(param2);
    e3 = EIndirection.decodeIndirection(param3);
    return this.isUseless(e1, EIndirection.decodeIndex(e1, param1), e2,
        EIndirection.decodeIndex(e2, param2), e3, EIndirection
            .decodeIndex(e3, param3));
  }

  /**
   * Checks whether this instruction may leave something on the stack or
   * not.
   * 
   * @param param1
   *          the fist parameter
   * @param param2
   *          the second parameter
   * @param param3
   *          the third parameter
   * @return <code>true</code> if and only if this instruction may leave
   *         data on the stack,<code>false</code> otherwise.
   */
  @Override
  public boolean pushesOnStack(final int param1, final int param2,
      final int param3) {
    return (EIndirection.decodeIndirection(param1) == EIndirection.STACK)
        && (EIndirection.decodeIndirection(param2) != EIndirection.STACK)
        && (EIndirection.decodeIndirection(param3) != EIndirection.STACK);
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
    int[] x;
    x = program[function];
    x[index + 1] = EIndirection.reencode(x[index + 1], parameters);
    x[index + 2] = EIndirection.reencode(x[index + 2], parameters);
    x[index + 3] = EIndirection.reencode(x[index + 3], parameters);
  }
}
