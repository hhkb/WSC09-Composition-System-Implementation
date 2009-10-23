/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-02-27
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.lgp.base.Instruction.java
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

package org.dgpf.lgp.base;

import org.dgpf.utils.FixedSetElement;
import org.dgpf.vm.base.VirtualMachine;

/**
 * the base class for instructions
 * 
 * @param <V>
 *          the vm type
 * @author Thomas Weise
 */
public abstract class Instruction<V extends VirtualMachine<LGPMemory, LGPProgram>>
    extends FixedSetElement<InstructionSet> {
  /**
   * the no-operation string
   */
  public static final String NOP = "!nop! "; //$NON-NLS-1$

  /**
   * the characters of the no string
   */
  protected static final char[] NOP_CHARS = NOP.toCharArray();

  /**
   * Create a new instruction for a instruction set.
   * 
   * @param isb
   *          the instruction set builder
   */
  public Instruction(final InstructionSet isb) {
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
  public abstract void execute(final V vm, final int param1,
      final int param2, final int param3);

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
  public abstract void toStringBuilder(final int param1, final int param2,
      final int param3, final StringBuilder sb);

  /**
   * Checks whether this instruction equals another object
   * 
   * @param o
   *          the object to compare with
   * @return <code>true</code> if the objects equal each other,<code>false/<code> otherwise
   */
  @Override
  public boolean equals(final Object o) {
    return ((o != null) && (o.getClass() == this.getClass()) && (((Instruction<?>) o).m_id == this.m_id));
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
  public boolean isNop(final int param1, final int param2, final int param3) {
    return false;
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
  public boolean pushesOnStack(final int param1, final int param2,
      final int param3) {
    return false;
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
  public void postProcessParameters(final int[][] program,
      final int function, final int index,
      final ILGPParameters parameters) {
    //
  }
}
