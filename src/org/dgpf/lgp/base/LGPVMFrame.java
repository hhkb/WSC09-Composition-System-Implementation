/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-17
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.machine.ll.vm.VMFrame.java
 * Last modification: 2006-12-17
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

import java.util.Arrays;

import org.sfc.collections.buffers.DirectBufferable;
import org.sfc.text.TextUtils;

/**
 * A RBGPNetVM-Frame is used for each call-operation.
 * 
 * @author Thomas Weise
 */
public final class LGPVMFrame extends DirectBufferable<LGPVMFrame> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 0;

  /**
   * the text for the is-interrupt value
   */
  private static final char[] IS_INTERRUPT = (TextUtils.LINE_SEPARATOR + "interrupt   : ").toCharArray(); //$NON-NLS-1$

  /**
   * the data text.
   */
  private static final char[] DATA = (TextUtils.LINE_SEPARATOR + "local mem   : ").toCharArray(); //$NON-NLS-1$

  /**
   * stack pointer
   */
  private static final char[] STACK_PTR = (TextUtils.LINE_SEPARATOR + "stack ptr   : ").toCharArray(); //$NON-NLS-1$

  /**
   * stack
   */
  private static final char[] STACK = (TextUtils.LINE_SEPARATOR + "stack       : ").toCharArray(); //$NON-NLS-1$

  /**
   * the ip text.
   */
  private static final char[] IP = ("ip          : ").toCharArray(); //$NON-NLS-1$

  /**
   * the code
   */
  public int[] m_code;

  /**
   * the instruction pointer
   */
  public int m_ip;

  /**
   * the interrupt flag
   */
  public boolean m_interrupt;

  /**
   * the local memory.
   */
  public final int[] m_local;

  /**
   * the stack
   */
  public final int[] m_stack;

  /**
   * the stack pointer
   */
  public int m_sp;

  /**
   * Create a new vm frame.
   * 
   * @param localSize
   *          the size of the local memory of the frame
   * @param stackSize
   *          the stack size
   */
  LGPVMFrame(final int localSize, final int stackSize) {
    super();
    this.m_local = new int[localSize];
    this.m_stack = new int[stackSize];
  }

  // /**
  // * Obtain the next frame (the frame of the calling or interrupted
  // * procedure).
  // *
  // * @return the next frame (the frame of the calling or interrupted
  // * procedure)
  // */
  // public LGPVMFrame getNextFrame() {
  // return this.m_next;
  // }
  //
  // /**
  // * Obtain the current instruction pointer.
  // *
  // * @return the current instruction pointer
  // */
  // public int getIp() {
  // return this.m_ip;
  // }
  //
  // /**
  // * Obtain the code of the procedure this frame belongs to.
  // *
  // * @return the code of the procedure this frame belongs to
  // */
  // public int[] getProcCode() {
  // return this.m_code.clone();
  // }
  //
  // /**
  // * Check whether this frame was invoked as interrupt. Notice that the
  // * code called from within an interrupt routine has this flag not set -
  // * it is only <code>true</code> for the interrupt's main routine.
  // *
  // * @return <code>true</code> if and only if this frame is an interrupt
  // * frame, <code>false</code> otherwise
  // */
  // public boolean isInterrupt() {
  // return this.m_interrupt;
  // }
  //
  // /**
  // * Read the value at the specified address.
  // *
  // * @param address
  // * the address to read from
  // * @return the value at that address
  // */
  // public int read(final int address) {
  // int[] mem;
  //
  // mem = this.m_local;
  //
  // return mem[Mathematics.modulo(address, mem.length)];
  // }
  //
  // /**
  // * Write a value to a specified memory address.
  // *
  // * @param address
  // * the memory address to write to
  // * @param value
  // * the value to be stored at this address
  // */
  // public void write(final int address, final int value) {
  // int[] mem;
  //
  // mem = this.m_local;
  //
  // mem[Mathematics.modulo(value, mem.length)] = value;
  // }

  /**
   * Clear this frame
   */
  final void clear() {
    this.m_code = null;
    this.m_interrupt = false;
    this.m_ip = 0;
    Arrays.fill(this.m_local, 0);
    Arrays.fill(this.m_stack, 0);
    this.m_sp = 0;
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
    sb.append(IP);
    sb.append(this.m_ip);
    sb.append(IS_INTERRUPT);
    sb.append(this.m_interrupt);
    sb.append(DATA);
    TextUtils.append(this.m_local, sb);

    sb.append(STACK);
    TextUtils.append(this.m_stack, sb);
    sb.append(STACK_PTR);
    sb.append(this.m_sp);
  }
}
