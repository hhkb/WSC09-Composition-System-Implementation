/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-03-07
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.byzantine.ByzantineNetwork.java
 * Last modification: 2008-03-07
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

package org.dgpf.vm.net.byzantine;

import java.io.Serializable;

import org.dgpf.vm.base.VirtualMachineProgram;
import org.dgpf.vm.net.BroadcastNetwork;
import org.dgpf.vm.net.Message;
import org.dgpf.vm.net.NetVirtualMachine;

/**
 * The critical section network
 * 
 * @param <MT>
 *          the virtual machine memory type
 * @param <PT>
 *          the virtual machine program type
 * @author Thomas Weise
 */
public class ByzantineIntBroadcastNetwork<MT extends Serializable, PT extends VirtualMachineProgram<MT>>
    extends BroadcastNetwork<MT, PT, int[]> {// BroadcastNetwork<MT, PT,
  // int[]> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the traitors
   */
  private final int[][] m_traitors;

  /**
   * the current traitors
   */
  private int[] m_traits;

  // /**
  // * the errors
  // */
  // private final int[][] m_errors;
  //
  // /**
  // * the current errors
  // */
  // private int[] m_err;
  //
  // /**
  // * the error index
  // */
  // private int m_errIdx;
  /**
   * the byzantine manager
   */
  private final ByzantineManager<MT> m_manager;

  /**
   * Create a new network.
   * 
   * @param provider
   *          the network provider
   */
  @SuppressWarnings("unchecked")
  public ByzantineIntBroadcastNetwork(
      final ByzantineIntBroadcastNetworkProvider<MT> provider) {
    super(provider);

    this.m_traitors = provider.m_traitors;
    // this.m_errors = provider.m_errors;
    this.m_manager = new ByzantineManager<MT>(provider);
  }

  /**
   * This method is called right before the simulation begins.
   * 
   * @throws IllegalStateException
   *           if this simulation is already running.
   */
  @Override
  public void beginSimulation() {
    int i;

    super.beginSimulation();

    i = this.getSimulationIndex();

    this.m_traits = this.m_traitors[i];
    this.m_manager.reset(i);
    // this.m_err = this.m_errors[i];
    // this.m_errIdx = 0;
  }

  /**
   * deliver the message <code>message</code> to the node at index
   * <code>to</code>.
   * 
   * @param message
   *          the message to be delivered
   * @param to
   *          the node to deliver to
   */
  @SuppressWarnings("unchecked")
  @Override
  protected void deliver(final Message<int[]> message,
      final NetVirtualMachine<MT, PT, int[]> to) {
    final int k;

    k = this.m_traits[message.m_owner.m_index];

    if (this.m_manager.modify(message,
        ((k & ByzantineType.SEND_DIFFERENT_ERRORS) != 0),
        ((k & ByzantineType.SELDOMLY_RECEIVED) != 0))) {
      super.deliver(message, to);
    }
  }

  // /**
  // * modify the given message
  // *
  // * @param message
  // * the message
  // * @param chg
  // * should the message be changed
  // * @param drop
  // * should the message dropped
  // * @return <code>true</code> if the message should be used,
  // * <code>false</code> if message processing stops here
  // */
  // private final boolean modify(final Message<int[]> message,
  // final boolean chg, final boolean drop) {
  // int j, i;
  // int[] d, e;
  //
  // i = this.m_errIdx;
  // e = this.m_err;
  //
  // if (drop) {
  // j = e[i];
  // this.m_errIdx = ((i + 1) % e.length);
  // if ((j & 1) == 0) {
  // NetVirtualMachine.disposeMessage(message);
  // return false;
  // }
  // }
  //
  // if (chg) {
  // d = message.m_data;
  // for (j = (message.m_size - 1); j >= 0; j--) {
  // d[j] ^= e[i];
  // i = ((i + 1) % e.length);
  // }
  // this.m_errIdx = i;
  // }
  //
  // return true;
  // }

  /**
   * Deliver a certain message.
   * 
   * @param message
   *          the message to be delivered
   */
  @Override
  protected void send(final Message<int[]> message) {
    final int k;

    k = this.m_traits[message.m_owner.m_index];

    if (this.m_manager.modify(message,
        ((k & ByzantineType.SEND_SAME_ERRORS) != 0),
        ((k & ByzantineType.SELDOMLY_SEND) != 0))) {
      super.send(message);
    }
  }
}
