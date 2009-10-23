/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-21
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.vm.net.BroadcastNetwork.java
 * Last modification: 2007-09-21
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

import org.dgpf.vm.base.VirtualMachineProgram;

/**
 * A network implementation where each message is broadcasted to all nodes.
 * 
 * @param <MT>
 *          the virtual machine memory type
 * @param <PT>
 *          the virtual machine program type
 * @param <MDT>
 *          the message data type
 * @author Thomas Weise
 */
public class BroadcastNetwork<MT extends Serializable, PT extends VirtualMachineProgram<MT>, MDT extends Serializable>
    extends Network<MT, PT, MDT> {

  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new network.
   * 
   * @param provider
   *          the network provider
   */
  public BroadcastNetwork(final NetworkProvider<MT> provider) {
    super(provider);

    NetVirtualMachine<MT, PT, MDT>[] vms;
    int i;

    vms = this.m_vms;
    for (i = (vms.length - 1); i >= 0; i--) {
      vms[i].m_neighbors = vms;
    }
  }

  /**
   * Deliver a certain message.
   * 
   * @param message
   *          the message to be delivered
   */
  @Override
  protected void send(final Message<MDT> message) {
    int i, j;
    Message<MDT> m;
    final NetVirtualMachine<MT, PT, MDT>[] vms;
    final NetVirtualMachine<?, ?, MDT> v1;
    NetVirtualMachine<MT, PT, MDT> v2;

    v1 = message.m_owner;
    i = j = (this.m_terminatedIdx - 1);
    vms = this.m_vms;

    for (--i; i >= 0; i--) {
      v2 = vms[i];
      if (v2 != v1) {
        if ((--j) > 0) {
          if ((m = copyMessage(message)) == null)
            return;
        } else
          m = message;
        this.deliver(m, v2);
      }
    }
  }

  /**
   * Initialize the network
   */
  @Override
  protected void initNetwork() {
    NetVirtualMachine<MT, PT, MDT>[] vms;
    int i, j;

    super.initNetwork();

    vms = this.m_vms;
    i = this.m_vmCount;
    for (j = (i - 1); j >= 0; j--) {
      vms[j].m_neighborCount = i;
    }
  }
}
