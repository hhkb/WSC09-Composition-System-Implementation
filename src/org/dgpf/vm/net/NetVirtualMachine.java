/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-20
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.vm.net.NetVirtualMachine.java
 * Last modification: 2007-09-20
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

package org.dgpf.vm.net;

import java.io.Serializable;

import org.dgpf.vm.base.HostedVirtualMachine;
import org.dgpf.vm.base.VirtualMachineProgram;
import org.dgpf.vm.net.objectives.INetVirtualMachineSimulationInformation;

/**
 * The base class for all virtual machines that run in a network.
 * 
 * @param <MT>
 *          the virtual machine memory type
 * @param <PT>
 *          the program type
 * @param <MDT>
 *          the message data type
 * @author Thomas Weise
 */
public abstract class NetVirtualMachine<MT extends Serializable, PT extends VirtualMachineProgram<MT>, MDT extends Serializable>
    extends HostedVirtualMachine<MT, PT, NetworkProvider<MT>> implements
    INetVirtualMachineSimulationInformation {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the node index
   */
  public final int m_index;

  /**
   * the network
   */
  protected final Network<MT, PT, MDT> m_network;

  /**
   * the maximum message size
   */
  protected final int m_maxMessageSize;

  /**
   * the maximum number of messages a node is allowed to send
   */
  protected final int m_maxMessages;

  /**
   * the messages already sent
   */
  private int m_messageCount;

  /**
   * the message cache
   */
  private Message<MDT> m_mcache;

  /**
   * the id of this vm
   */
  int m_id;

  /**
   * no more messages for this vm
   */
  boolean m_alive;

  /**
   * the neighboring vms (those which can directly be reached by messages
   */
  NetVirtualMachine<MT, PT, MDT>[] m_neighbors;

  /**
   * the number of neighboring vms
   */
  int m_neighborCount;

  /**
   * Create a new networked hosted virtual machine.
   * 
   * @param network
   *          the network
   * @param index
   *          the index
   */
  protected NetVirtualMachine(final Network<MT, PT, MDT> network,
      final int index) {
    super(network.m_parameters, network.m_provider);
    this.m_network = network;

    IVirtualMachineNetworkParameters<MT> p;

    p = this.m_network.m_parameters;
    this.m_maxMessageSize = Math.max(1, p.getMaxMessageSize());
    this.m_maxMessages = Math.max(1, p.getMaxMessages());
    this.m_index = index;
  }

  /**
   * Obtain the network this virtual machine is part of.
   * 
   * @return the network this virtual machine is part of
   */
  public final Network<MT, PT, MDT> getNetwork() {
    return this.m_network;
  }

  /**
   * Create a message data record
   * 
   * @return the new message data record
   */
  protected abstract MDT createMessageData();

  /**
   * allocate a new message
   * 
   * @return the new message
   */
  protected final Message<MDT> allocateMessage() {
    Message<MDT> m;
    int c;

    c = (this.m_messageCount + 1);
    m = this.m_mcache;
    if (m != null) {
      this.m_mcache = m.m_next;
      m.m_next = null;
      this.m_messageCount = c;
      return m;
    }

    if (c > this.m_maxMessages) {
      this.setError();
      return null;
    }
    this.m_messageCount = c;

    return new Message<MDT>(this.createMessageData(), this);
  }

  /**
   * Broadcast the given message.
   * 
   * @param message
   *          the message to be sent
   */
  protected final void send(final Message<MDT> message) {
    this.m_network.send(message);
  }

  /**
   * Process an incoming message.
   * 
   * @param message
   *          the message received
   */
  protected void handleMessage(final Message<MDT> message) {
    //
  }

  /**
   * Obtain the number of messages sent.
   * 
   * @return the number of messages sent
   */
  public final int getMessageCount() {
    return this.m_messageCount;
  }

  /**
   * This method is called right before the simulation begins.
   * 
   * @throws IllegalStateException
   *           if this simulation is already running.
   */
  @Override
  public void beginSimulation() {
    super.beginSimulation();

    this.m_alive = true;
    this.m_messageCount = 0;
  }

  /**
   * Obtain the id of this virtual machine
   * 
   * @return the id of this virtual machine
   */
  public final int getId() {
    return this.m_id;
  }

  /**
   * Obtain the neighboring vm at the specified index
   * 
   * @param index
   *          the index of the neighbor we want to get
   * @return the neighboring vm at the specified index
   */
  public final NetVirtualMachine<MT, PT, MDT> getNeighbor(final int index) {
    return this.m_neighbors[index];
  }

  /**
   * Obtain the number of neighbors.
   * 
   * @return the number of neighbors
   */
  public final int getNeighborCount() {
    return this.m_neighborCount;
  }

  /**
   * Obtain the number of steps where this vm was idle.
   * 
   * @return the number of steps where this vm was idle
   */
  public int getIdleSteps() {
    return 0;
  }

  /**
   * dispose a given message
   * 
   * @param msg
   *          the message to be disposed
   * @param <MTX>
   *          the memory type
   */
  public static final <MTX extends Serializable> void disposeMessage(
      final Message<MTX> msg) {
    msg.m_next = msg.m_owner.m_mcache;
    msg.m_owner.m_mcache = msg;
  }

  /**
   * dispose a given message
   * 
   * @param msg
   *          the message to be disposed
   * @param <MTX>
   *          the memory type
   */
  protected static final <MTX extends Serializable> void disposeMessages(
      final Message<MTX> msg) {
    Message<MTX> m, n;

    for (m = msg; m != null; m = n) {
      n = m.m_next;
      m.m_next = m.m_owner.m_mcache;
      m.m_owner.m_mcache = m;
    }
  }
}
