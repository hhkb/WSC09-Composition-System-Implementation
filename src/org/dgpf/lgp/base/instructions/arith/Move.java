/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-02-18
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.machine.ll.instructions.arith.Move.java
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

package org.dgpf.lgp.base.instructions.arith;

import org.dgpf.lgp.base.EIndirection;
import org.dgpf.lgp.base.InstructionSet;
import org.dgpf.lgp.base.LGPMemory;
import org.dgpf.lgp.base.LGPProgram;
import org.dgpf.vm.base.VirtualMachine;

/**
 * This instruction stores a value from one location in another one.
 * 
 * @author Thomas Weise
 */
public class Move extends ArithInstruction {

  /** the serial version uid */
  private static final long serialVersionUID = 1L;

  /**
   * Create a new instruction for a instruction set.
   * 
   * @param isb
   *          the instruction set builder
   */
  public Move(final InstructionSet isb) {
    super(isb);
  }

  /**
   * Execute this instruction on the specified vm.
   * 
   * @param vm
   *          the vm
   * @param param1
   *          the fist parameter
   * @param param2
   *          the second parameter
   * @param param3
   *          the third parameter
   */
  @Override
  public void execute(final VirtualMachine<LGPMemory, LGPProgram> vm,
      final int param1, final int param2, final int param3) {
    EIndirection.write(vm, param1, EIndirection.read(vm, param2));
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
  @Override
  protected boolean isUseless(final EIndirection indir1, final int index1,
      final EIndirection indir2, final int index2,
      final EIndirection indir3, final int index3) {
    return (super
        .isUseless(indir1, index1, indir2, index2, indir3, index3) || //
    ((indir2 == indir1) && (index2 == index1)));
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
    EIndirection indir1, indir2;
    int v1, v2;

    indir1 = EIndirection.decodeIndirection(param1);
    indir2 = EIndirection.decodeIndirection(param2);

    if ((indir1 == EIndirection.CONSTANT)
        && (indir2 == EIndirection.STACK)) {
      EIndirection.STACK.toStringBuilder(0, true, sb);
    } else {
      v1 = EIndirection.decodeIndex(indir1, param1);
      v2 = EIndirection.decodeIndex(indir2, param2);

      if (this.isUseless(indir1, v1, indir2, v2, EIndirection.CONSTANT, 0)) {
        sb.append(NOP_CHARS);
      }

      indir1.toStringBuilder(v1, false, sb);
      indir2.toStringBuilder(v2, true, sb);
    }
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
        && (EIndirection.decodeIndirection(param2) != EIndirection.STACK);
  }
}
