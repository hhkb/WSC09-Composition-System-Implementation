/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-20
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.vm.net.IVirtualMachineNetworkParameters.java
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

import org.dgpf.vm.base.IVirtualMachineParameters;

/**
 * The common interface for virtual machine network parameters
 * 
 * @param <MT>
 *          the virtual machine memory type
 * @author Thomas Weise
 */
public interface IVirtualMachineNetworkParameters<MT extends Serializable>
    extends IVirtualMachineParameters<MT> {

  /**
   * The minimum number of virtual machines to be in a simulation.
   * 
   * @return minimum number of virtual machines to be in a simulation
   */
  public abstract int getMinVirtualMachines();

  /**
   * The maximum number of virtual machines to be in a simulation.
   * 
   * @return maximum number of virtual machines to be in a simulation
   */
  public abstract int getMaxVirtualMachines();

  /**
   * Obtain the virtual machine factory for the vms that will be running
   * inside the network.
   * 
   * @return the virtual machine factory of this parameter set
   */
  public abstract INetVirtualMachineFactory<MT, ?, ?> getNetVirtualMachineFactory();

  /**
   * The maximum messages a node is allowed to send.
   * 
   * @return The maximum messages a node is allowed to send.
   */
  public abstract int getMaxMessages();

  /**
   * The maximum size of a message
   * 
   * @return the maximum size of a message
   */
  public abstract int getMaxMessageSize();

  /**
   * Obtain the minimum message delay.
   * 
   * @return the minimum message delay
   */
  public abstract int getMinMessageDelay();

  /**
   * Obtain the maximum message delay.
   * 
   * @return the maximum message delay
   */
  public abstract int getMaxMessageDelay();
}
