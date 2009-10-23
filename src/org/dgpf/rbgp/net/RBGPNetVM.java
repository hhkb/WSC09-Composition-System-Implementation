/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-05-27
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.rbgp.net.RBGPNetVM.java
 * Last modification: 2007-05-27
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

package org.dgpf.rbgp.net;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;

import org.dgpf.rbgp.base.RBGPMemory;
import org.dgpf.rbgp.base.RBGPProgramBase;
import org.dgpf.rbgp.base.Symbol;
import org.dgpf.rbgp.base.SymbolSet;
import org.dgpf.vm.base.EVirtualMachineState;
import org.dgpf.vm.net.Message;
import org.dgpf.vm.net.NetVirtualMachine;
import org.dgpf.vm.net.Network;
import org.sfc.text.TextUtils;
import org.sfc.utils.Classes;

/**
 * A virtual machine able to execute a symbolic classifier.
 * 
 * @author Thomas Weise
 */
public class RBGPNetVM extends
    NetVirtualMachine<RBGPMemory, RBGPProgramBase, int[]> implements
    Serializable {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the symbol set
   */
  protected final SymbolSet m_symbols;

  /**
   * the symbols
   */
  private final Symbol[] m_sym;

  /**
   * the index of the receive symbol
   */
  private final int m_recIdx;

  /**
   * the send index
   */
  private final int m_sendIdx;

  /**
   * the message size
   */
  private final int m_msgSize;

  /**
   * the messages received
   */
  private Message<int[]> m_received;

  /**
   * the symbol for incoming messages
   */
  private final int m_incoming;

  /**
   * the idle time counter
   */
  private int m_idleCounter;

  /**
   * the send flag
   */
  private boolean m_send;

  /**
   * Create a new virtual machine with the default symbol set.
   * 
   * @param network
   *          the network
   * @param index
   *          the index
   */
  @SuppressWarnings("unchecked")
  public RBGPNetVM(
      final Network<RBGPMemory, RBGPProgramBase, int[]> network,
      final int index) {
    super(network, index);

    DefaultNetSymbolSet s;
    RBGPNetParametersBase p;

    p = ((RBGPNetParametersBase) (this.getParameters()));

    s = ((DefaultNetSymbolSet) (p.getSymbols()));
    this.m_symbols = s;
    this.m_sym = s.getElements();

    this.m_sendIdx = s.m_srIndex;
    this.m_msgSize = s.m_srCount;
    this.m_recIdx = (this.m_sendIdx + this.m_msgSize);
    this.m_incoming = s.m_incomingMsgSymbol.m_id;
  }

  /**
   * Create a message data record
   * 
   * @return the new message data record
   */
  @Override
  protected int[] createMessageData() {
    return new int[this.m_msgSize];
  }

  /**
   * Obtain the symbol set.
   * 
   * @return the symbol set
   */
  public final SymbolSet getSymbols() {
    return this.m_symbols;
  }

  /**
   * Create a memory block
   * 
   * @return the vm memory
   */
  @Override
  protected RBGPMemory createMemory() {
    return new RBGPMemory(this.m_memorySize,
        ((RBGPNetParameters) (this.m_parameters)).useBufferendMemory());
  }

  /**
   * Initialize this vm
   */
  @Override
  public void beginSimulation() {
    int[] is, r;
    int i;
    Symbol[] s;

    this.m_idleCounter = 0;
    this.m_send = false;

    disposeMessages(this.m_received);
    this.m_received = null;

    super.beginSimulation();

    is = this.m_memory.m_mem2;
    r = this.m_memory.m_mem1;

    Arrays.fill(is, 0);
    Arrays.fill(r, 0);

    s = this.m_sym;
    for (i = (is.length - 1); i >= 0; i--) {
      is[i] = s[i].getInitialValue(this);
      if (s[i].m_writeProtected)
        r[i] = is[i];
    }
  }

  /**
   * Set a symbol's value
   * 
   * @param sym
   *          the symbol
   * @param value
   *          the new value
   */
  public final void setValue(final Symbol sym, final int value) {
    this.m_memory.m_mem2[sym.m_id] = value;
    this.m_memory.m_mem1[sym.m_id] = value;
  }

  /**
   * Obtain a symbol's value.
   * 
   * @param sym
   *          the symbol
   * @return the symbol's value
   */
  public final int getValue(final Symbol sym) {
    return this.m_memory.m_mem1[sym.m_id];
  }

  /**
   * Obtain a symbol's output value.
   * 
   * @param sym
   *          the symbol
   * @return the symbol's value
   */
  public final int getNextValue(final Symbol sym) {
    return this.m_memory.m_mem2[sym.m_id];
  }

  /**
   * Serializes the parameters of the constructor of this object.
   * 
   * @param sb
   *          the string builder
   * @param indent
   *          an optional parameter denoting the indentation
   */
  @Override
  protected final void javaParametersToStringBuilder(
      final StringBuilder sb, final int indent) {
    Field m;

    m = Classes.findStaticField(this.m_symbols);
    if (m != null) {
      TextUtils.appendStaticField(m, sb);
    } else
      // this.m_symbols.javaToStringBuilder(sb, indent);
      this.m_symbols.javaToStringBuilder(sb, indent);// nonsense, but what
    // ever
  }

  /**
   * Perform a single simulation step.
   * 
   * @return a new virtual machine state, see {@link EVirtualMachineState}
   */
  @Override
  protected EVirtualMachineState doStep() {
    int[] i, z;
    Symbol[] s;
    int j;
    Message<int[]> message;
    EVirtualMachineState e;
    Message<int[]> m;
    EVirtualMachineState x;

    z = this.m_memory.m_mem2;

    message = this.m_received;
    if (message != null) {
      System.arraycopy(message.m_data, 0, z, this.m_recIdx,//
          message.m_size);
      this.m_received = message.m_next;

      disposeMessage(message);// ??

      z[this.m_incoming] = 1;
      e = EVirtualMachineState.CHANGED;
    } else
      e = EVirtualMachineState.NOTHING;

    i = this.m_memory.m_mem1;
    this.m_memory.m_mem1 = z;// this.m_memory.m_mem2;
    this.m_memory.m_mem2 = i;
    s = this.m_sym;

    for (j = (i.length - 1); j >= 0; j--) {
      if (s[j].m_copy)
        i[j] = z[j];
      else
        i[j] = 0;
    }

    x = this.m_program.perform(this);
    if (x == EVirtualMachineState.ERROR)
      return x;

    if (this.m_send) {

      m = this.allocateMessage();
      if (m != null) {
        System.arraycopy(i, this.m_sendIdx, m.m_data, 0,
            m.m_size = this.m_msgSize);
        this.send(m);
        e = EVirtualMachineState.CHANGED;
      } else
        e = EVirtualMachineState.ERROR;

      this.m_send = false;
    }

    e = EVirtualMachineState.combineStates(e, x);
    if (e == EVirtualMachineState.NOTHING)
      this.m_idleCounter++;
    return e;
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
   * Broadcast the stuff out
   */
  public final void send() {
    this.m_send = true;
  }

  /**
   * Append this object's textual representation to a string builder.
   * 
   * @param sb
   *          The string builder to append to.
   * @see #toString()
   */
  @Override
  public final void toStringBuilder(final StringBuilder sb) {

    sb.append("symbols: "); //$NON-NLS-1$
    this.m_symbols.toStringBuilder(sb);
    sb.append(TextUtils.LINE_SEPARATOR);
    this.m_memory.toStringBuilder(sb);

    if (this.m_program != null) {
      sb.append(TextUtils.LINE_SEPARATOR);
      this.m_program.toStringBuilder(sb);
    }

  }

  /**
   * Obtain the number of steps where this vm was idle.
   * 
   * @return the number of steps where this vm was idle
   */
  @Override
  public final int getIdleSteps() {
    return this.m_idleCounter;
  }
}
