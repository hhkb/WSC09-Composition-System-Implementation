/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-29
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.lgp.net.LGPNetVM.java
 * Last modification: 2007-11-29
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

package org.dgpf.lgp.net;

import org.dgpf.lgp.base.EIndirection;
import org.dgpf.lgp.base.Instruction;
import org.dgpf.lgp.base.InstructionSet;
import org.dgpf.lgp.base.LGPMemory;
import org.dgpf.lgp.base.LGPProgram;
import org.dgpf.lgp.base.LGPVMFrame;
import org.dgpf.lgp.single.LGPVM;
import org.dgpf.vm.base.EVirtualMachineState;
import org.dgpf.vm.net.Message;
import org.dgpf.vm.net.NetVirtualMachine;
import org.dgpf.vm.net.Network;
import org.sfc.text.TextUtils;

/**
 * The linear genetic programming network virtual machine.
 * 
 * @author Thomas Weise
 */
public class LGPNetVM extends
    NetVirtualMachine<LGPMemory, LGPProgram, int[]> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the data text.
   */
  private static final char[] DATA = LGPVM.DATA_STR.toCharArray();

  /**
   * the interrupts text.
   */
  private static final char[] INTERRUPTS = LGPVM.INTERRUPTS_STR
      .toCharArray();

  /**
   * the missed interrupts
   */
  private static final char[] MISSED_INTS = LGPVM.MISSED_INTS_STR
      .toCharArray();

  /**
   * passive steps
   */
  private static final char[] PASSIVE_STEPS = LGPVM.PASSIVE_STEPS_STR
      .toCharArray();

  /**
   * the frame text.
   */
  private static final char[] FRAME = LGPVM.FRAME_STR.toCharArray();

  /**
   * the frame text.
   */
  private static final char[] FRAME2 = LGPVM.FRAME2_STR.toCharArray();

  /**
   * the count of passive steps.
   */
  private int m_passiveSteps;

  /**
   * the jump flag
   */
  private boolean m_jump;

  /**
   * if we're currently in an interrupt
   */
  private boolean m_interrupt;

  /**
   * the count of interrupts missed
   */
  private int m_missedInterrupts;

  /**
   * the count of interrupts performed
   */
  private int m_interrupts;

  /**
   * the code
   */
  private int[][] m_code;

  /**
   * the instructions
   */
  private final Instruction<LGPNetVM>[] m_instructions;

  /**
   * the messages received
   */
  private Message<int[]> m_received;

  /**
   * Create a new network virtual machine.
   * 
   * @param network
   *          the network
   * @param index
   *          the index
   */
  @SuppressWarnings("unchecked")
  public LGPNetVM(final Network<LGPMemory, LGPProgram, int[]> network, final int index) {
    super(network, index);

    InstructionSet s;

    s = ((LGPNetParameters) (network.getParameters())).getInstructions();
    if (s == null)
      s = LGPNetVMInstructionSet.DEFAULT_NET_VM_INSTRUCTION_SET;

    this.m_instructions = ((Instruction<LGPNetVM>[]) (s.getElements()));
  }

  /**
   * Create a memory block
   * 
   * @return the vm memory
   */
  @Override
  protected final LGPMemory createMemory() {
    LGPNetParameters p;

    p = ((LGPNetParameters) (this.getParameters()));
    return new LGPMemory(p.m_globalSize, p.m_localSize, p.m_stackSize,
        p.m_maxCallDepth);
  }

  /**
   * Create a message data record
   * 
   * @return the new message data record
   */
  @Override
  protected int[] createMessageData() {
    return new int[this.m_memory.m_stackSize];
  }

  /**
   * This method is called right before the simulation begins.
   * 
   * @throws IllegalStateException
   *           if this simulation is already running.
   */
  @Override
  public void beginSimulation() {

    disposeMessages(this.m_received);
    this.m_received = null;

    super.beginSimulation();

    this.m_memory.clear();
    this.m_passiveSteps = 0;

    this.m_jump = false;
    this.m_missedInterrupts = 0;
    this.m_interrupts = 0;
    this.m_interrupt = false;
    this.m_memory.m_frame.m_code = this.m_code[0];
  }

  /**
   * call a procedure
   * 
   * @param proc
   *          the procedure to be called
   * @return <code>true</code> if the call was successfull,
   *         <code>false</code> if it could not be performed
   */
  public boolean call(final int proc) {
    int sp;
    LGPVMFrame f;

    f = this.m_memory.m_frame;
    sp = f.m_sp;
    f.m_sp = 0;
    return this.call(proc, f.m_stack, sp);
  }

  /**
   * call a procedure
   * 
   * @param proc
   *          the procedure to be called
   * @param parameters
   *          the procedure parameters
   * @param parameterSize
   *          the count of parameters
   * @return <code>true</code> if the call was successfull,
   *         <code>false</code> if it could not be performed
   */
  private final boolean call(final int proc, final int[] parameters,
      final int parameterSize) {
    LGPVMFrame f2;
    int[] x;
    final LGPMemory m;

    if ((proc < 0) || (proc >= this.m_code.length)) {
      this.setError();
      return false;
    }

    m = this.m_memory;
    f2 = m.call();
    if (f2 == null) {
      this.setError();
      return false;
    }

    if (parameterSize > 0) {
      x = f2.m_local;
      if (parameterSize > x.length) {
        this.setError();
        return false;
      }

      System.arraycopy(parameters, 0, f2.m_local, 0, parameterSize);
    }

    f2.m_code = this.m_code[proc];
    return true;
  }

  /**
   * Process an incoming message.
   * 
   * @param message
   *          the message received
   */
  @Override
  protected void handleMessage(final Message<int[]> message) {
    message.m_next = this.m_received;
    this.m_received = message;
  }

  /**
   * Perform a single simulation step.
   * 
   * @return a new virtual machine state, see {@link EVirtualMachineState}
   */
  @Override
  protected EVirtualMachineState doStep() {
    LGPVMFrame f, g;
    int ip;
    int[] in;
    final LGPMemory m;
    Message<int[]> message;

    m = this.m_memory;

    message = this.m_received;
    if ((message != null) && (!(this.m_interrupt))) {
      this.m_received = message.m_next;
      this.interrupt(1, message.m_data, message.m_size);
      disposeMessage(message);// ??
      return EVirtualMachineState.CHANGED;
    }

    f = m.m_frame;
    if (f == null) {
      this.m_passiveSteps++;
      return EVirtualMachineState.NOTHING;
    }

    // r = 1;
    in = f.m_code;
    ip = f.m_ip;

    if (ip < in.length) {

      this.m_instructions[in[ip]].execute(this, in[ip + 1], in[ip + 2],
          in[ip + 3]);

      // this.m_instructions[Mathematics.modulo(in[ip],
      // this.m_instructionCount)].execute(this, in[ip + 1], in[ip + 2],
      // in[ip + 3]);
    }

    if (this.m_jump) {// in case a jump was performed
      ip = f.m_ip;
      this.m_jump = false;
    } else
      ip += 4;

    if (ip >= in.length) {
      g = m.m_frame;
      if (g != f) {// a procedure was called
        while ((g != null) && (g.m_next != f)) {
          // if multiple calls have been performed
          g = g.m_next;
        }
        if (g != null)// normally, something else cannot happen
        {
          g.m_next = f.m_next;
          if (f.m_interrupt)
            g.m_interrupt = true;
        } else {
          return EVirtualMachineState.ERROR;// should not happen
        }
        // r = -1;
      } else {
        m.m_frame = f.m_next;
        if (f.m_interrupt)
          this.m_interrupt = false;
      }
      // new - return value, interrupts dont have none
      if ((f.m_sp > 0) && (!(f.m_interrupt))) {
        if (m.m_frame != null)
          EIndirection.STACK.write(0, f.m_stack[0], this);
        // this.push(f.m_stack[0]);// store the return value
        else
          m.m_global[0] = f.m_stack[0];
      }

      m.dispose(f);// drop the frame
      // this behavior accomplishes tail recursion resolution
    } else {
      f.m_ip = ip;
    }

    return EVirtualMachineState.CHANGED;
    // (((this.m_stackErrors <= 0) && (this.m_memErrors <= 0)) ? r
    // : -1);
  }

  /**
   * Broadcast a message.
   */
  public void send() {
    final LGPVMFrame f;
    int sp;
    final Message<int[]> m;

    f = this.m_memory.m_frame;
    if (f == null) {
      this.setError();
      return;
    }

    m = this.allocateMessage();
    if (m == null) {
      this.setError();
      return;
    }

    sp = f.m_sp;
    m.m_size = sp;
    System.arraycopy(f.m_stack, 0, m.m_data, 0, sp);
    f.m_sp = 0;

    this.send(m);
  }

  /**
   * Check whether we're currently inside an interrupt.
   * 
   * @return <code>true</code> if and only if the current code was
   *         invoked as interrupt, <code>false</code> otherwise
   */
  public boolean isInterrupt() {
    return this.m_interrupt;
  }

  /**
   * Invoke the specified procedure as interrupt. If an interrupt is
   * already running, nothing will be done and the interrupt call will be
   * ignored/lost.
   * 
   * @param proc
   *          the interrupt routine.
   * @param parameters
   *          the parameters of the interrupt
   * @param parameterSize
   *          the count of parameters
   */
  public void interrupt(final int proc, final int[] parameters,
      final int parameterSize) {
    if (this.m_interrupt)
      this.m_missedInterrupts++;
    else {
      this.m_interrupt = true;
      if (this.call(proc, parameters, parameterSize))
        this.m_interrupts++;
      if (this.m_memory.m_frame != null)
        this.m_memory.m_frame.m_interrupt = true;
    }
  }

  /**
   * Obtain the count of interrupts that could not be performed because an
   * interrupt was already running.
   * 
   * @return the count of interrupts that could not be performed because an
   *         interrupt was already running
   */
  public int getMissedInterrupts() {
    return this.m_missedInterrupts;
  }

  /**
   * Obtain the count of interrupts performed. Only those interrupts which
   * resulted in a successfull invokation of the interrupt handler are
   * counted.
   * 
   * @return the count of interrupts performed
   */
  public int getPerformedInterrupts() {
    return this.m_interrupts;
  }

  /**
   * Perform an absolute jump to the instruction at the specified index
   * inside the current procedure. If the index is negative, it will be set
   * to <code>0</code>. If the vm is in passive mode, nothing will be
   * done. If the index is outside the procedure (because it is too large)
   * this will lead to the termination of the procedure.
   * 
   * @param index
   *          the absolute index of the instruction to jump to
   */
  public void jumpAbsolute(final int index) {
    LGPVMFrame f;

    f = this.m_memory.m_frame;
    if (f != null) {
      f.m_ip = ((index > 0) ? (index << 2) : 0);
      this.m_jump = true;
    }
  }

  /**
   * Perform a relative jump. If the resulting index is negative, it will
   * be set to <code>0</code>. If the vm is in passive mode, nothing
   * will be done. If the resulting index is outside the procedure (because
   * it is too large) this will lead to the termination of the procedure.
   * 
   * @param index
   *          the relative index of the instruction to jump to
   */
  public void jumpRelative(final int index) {
    LGPVMFrame f;
    int j;

    f = this.m_memory.m_frame;
    if (f != null) {
      j = (f.m_ip + (index << 2));
      f.m_ip = ((j > 0) ? j : 0);
      this.m_jump = true;
    }
  }

  /**
   * Begin the simulation of the specified individual.
   * 
   * @param what
   *          the individual
   * @throws NullPointerException
   *           if <code>what</code> is <code>null</code>.
   * @throws IllegalStateException
   *           if this simulation is already running.
   */
  @Override
  public void beginIndividual(final LGPProgram what) {
    super.beginIndividual(what);
    this.m_code = (what != null) ? what.getCode()
        : LGPProgram.EMPTY_PROGRAM.getCode();
  }

  /**
   * End the individual.
   */
  @Override
  public void endIndividual() {
    super.endIndividual();
    this.m_code = null;
  }

  /**
   * Append this virtual machine's textual representation to a string
   * builder.
   * 
   * @param sb
   *          the string builder to append to.
   */
  @Override
  public void toStringBuilder(final StringBuilder sb) {
    LGPVMFrame f;
    int i, j;
    int[][] code;

    code = this.m_code;
    f = this.m_memory.m_frame;
    if ((code != null) && (f != null)) {
      i = 0;
      for (; f != null; f = f.m_next) {
        for (j = (code.length - 1); j >= 0; j--) {
          if (code[j] == f.m_code)
            break;
        }

        sb.append(FRAME);
        sb.append(i++);
        sb.append(FRAME2);
        LGPProgram.appendFunctionName(j, sb);
        sb.append(TextUtils.LINE_SEPARATOR);
        f.toStringBuilder(sb);
        sb.append(TextUtils.LINE_SEPARATOR);
      }
      sb.append(TextUtils.LINE_SEPARATOR);
    }

    sb.append(DATA);
    TextUtils.append(this.m_memory.m_global, sb);

    sb.append(INTERRUPTS);
    sb.append(this.m_interrupts);
    sb.append(MISSED_INTS);
    sb.append(this.m_missedInterrupts);

    sb.append(PASSIVE_STEPS);
    sb.append(this.m_passiveSteps);

    sb.append(TextUtils.LINE_SEPARATOR);
    sb.append(TextUtils.LINE_SEPARATOR);

    super.toStringBuilder(sb);
  }

  /**
   * Obtain the number of steps where this vm was idle.
   * 
   * @return the number of steps where this vm was idle
   */
  @Override
  public final int getIdleSteps() {
    return this.m_passiveSteps;
  }
}
