/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-02-18
 * Creator          : Thomas Weise
 * Original Filename: oorg.dgpf.lgp.net.instructions.net.Send.java
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

package org.dgpf.lgp.net.instructions.net;

import org.dgpf.lgp.base.ECondition;
import org.dgpf.lgp.base.EIndirection;
import org.dgpf.lgp.base.InstructionSet;
import org.dgpf.lgp.base.instructions.ctrl.ControlInstruction;
import org.dgpf.lgp.net.LGPNetVM;

/**
 * the conditional call instruction
 * 
 * @author Thomas Weise
 */
public class Send extends ControlInstruction<LGPNetVM> {
  /** serial version uid */
  private static final long serialVersionUID = 1L;

  /**
   * the call
   */
  private static final char[] OP_TXT = "send".toCharArray(); //$NON-NLS-1$

  /**
   * Create a new instruction for a instruction set.
   * 
   * @param isb
   *          the instruction set builder
   */
  public Send(final InstructionSet isb) {
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
    e = ECondition.decode(param1);

    if (e == ECondition.FALSE) {
      sb.append(NOP_CHARS);
    }

    super.toStringBuilder(param1, param2, param3, sb);
    sb.append(OP_TXT);
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
  public void execute(final LGPNetVM vm, final int param1,
      final int param2, final int param3) {
    if (ECondition.decode(param1).check(EIndirection.read(vm, param2)))
      vm.send();
  }

  // /**
  // * Post-process the parameters
  // *
  // * @param program
  // * the program
  // * @param function
  // * the function index
  // * @param index
  // * the instruction index
  // * @param parameters
  // * the lgp vm parameters
  // */
  // @Override
  // public void postProcessParameters(final int[][] program,
  // final int function, final int index, final ILGPParameters parameters)
  // {
  // int[] x;
  // x = program[function];
  // x[index + 2] = EIndirection.reencode(x[index + 2], parameters);
  //  }
}
