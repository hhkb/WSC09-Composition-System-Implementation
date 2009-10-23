/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-10-08
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.vm.net.LinearGridNetwork.java
 * Last modification: 2007-10-08
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
 * This type of network uses a fixed grid as topology.
 * 
 * @param <MT>
 *          the virtual machine memory type
 * @param <PT>
 *          the virtual machine program type
 * @param <MDT>
 *          the message data type
 * @author Thomas Weise
 */
public class LinearNetwork<MT extends Serializable, PT extends VirtualMachineProgram<MT>, MDT extends Serializable>
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
  @SuppressWarnings("unchecked")
  public LinearNetwork(final NetworkProvider<MT> provider) {
    super(provider);

    NetVirtualMachine<MT, PT, MDT>[] vms;
    int i;

    vms = this.m_vms;
    i = (vms.length - 1);

    vms[i].m_neighbors = new NetVirtualMachine[] { vms[--i] };

    for (; i > 0; i--) {
      vms[i].m_neighbors = new NetVirtualMachine[] { vms[i - 1],
          vms[i + 1] };
    }

    vms[0].m_neighbors = new NetVirtualMachine[] { vms[1] };
    vms[0].m_neighborCount = 1;
  }

  /**
   * Initialize the network
   */
  @Override
  protected void initNetwork() {
    NetVirtualMachine<MT, PT, MDT>[] vms;
    int i;

    super.initNetwork();

    vms = this.m_vms;
    i = (this.m_vmCount - 1);
    vms[i].m_neighborCount = 1;

    for (--i; i > 0; i--)
      vms[i].m_neighborCount = 2;
  }
}
