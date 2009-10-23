/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-20
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.vm.net.VirtualMachineNetworkParameters.java
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

import org.dgpf.vm.base.VirtualMachineParameters;

/**
 * The parameters for virtual machine networks
 * 
 * @param <MT>
 *          the virtual machine memory type, for example <code>int[]</code>
 *          or <code>double[]</code>
 * @author Thomas Weise
 */
public class VirtualMachineNetworkParameters<MT extends Serializable>
    extends VirtualMachineParameters<MT> implements
    IVirtualMachineNetworkParameters<MT> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the minimum number of virtual machines
   */
  private final int m_minVMs;

  /**
   * the maximum number of virtual machines
   */
  private final int m_maxVMs;

  /**
   * the networked virtual machine factory of this parameter setting
   */
  private final INetVirtualMachineFactory<MT, ?, ?> m_factory;

  /**
   * the maximum messages a node is allowed to send
   */
  private final int m_maxMessages;

  /**
   * the maximum message size
   */
  private final int m_maxMessageSize;

  /**
   * the minimum message delay
   */
  private final int m_minDelay;

  /**
   * the maximum message delay
   */
  private final int m_maxDelay;

  /**
   * Create a new virtual machine network parameter set.
   * 
   * @param memorySize
   *          the memory size
   * @param minVMs
   *          the minimum number of virtual machines
   * @param maxVMs
   *          the maximum number of virtual machines
   * @param factory
   *          the networked virtual machine factory of this parameter
   *          setting
   * @param maxMessages
   *          the maximum messages a node is allowed to send
   * @param maxMessageSize
   *          the maximum message size
   * @param minDelay
   *          the minimum message delay
   * @param maxDelay
   *          the maximum message delay
   */
  public VirtualMachineNetworkParameters(final int memorySize,
      final int minVMs, final int maxVMs,
      final INetVirtualMachineFactory<MT, ?, ?> factory,
      final int maxMessages, final int maxMessageSize, final int minDelay,
      final int maxDelay) {
    super(memorySize);

    this.m_minVMs = Math.max(3, minVMs);
    this.m_maxVMs = ((maxVMs >= this.m_minVMs) ? maxVMs
        : (this.m_minVMs + 3));
    this.m_factory = factory;
    this.m_maxMessages = Math.max(1, maxMessages);
    this.m_maxMessageSize = Math.max(1, maxMessageSize);
    this.m_minDelay = ((minDelay > 0) ? minDelay : 1);// Math.max(0,
    // minDelay);
    this.m_maxDelay = ((maxDelay >= this.m_minDelay) ? maxDelay
        : (this.m_minDelay + 15));
  }

  /**
   * Create a new virtual machine network parameter set by copying another
   * one.
   * 
   * @param copy
   *          the parameters to copy
   */
  public VirtualMachineNetworkParameters(
      final IVirtualMachineNetworkParameters<MT> copy) {
    this(copy.getMemorySize(), copy.getMinVirtualMachines(), copy
        .getMaxVirtualMachines(), copy.getNetVirtualMachineFactory(), copy
        .getMaxMessages(), copy.getMaxMessageSize(), copy
        .getMinMessageDelay(), copy.getMaxMessageDelay());
  }

  /**
   * The minimum number of virtual machines to be in a simulation.
   * 
   * @return minimum number of virtual machines to be in a simulation
   */
  public final int getMinVirtualMachines() {
    return this.m_minVMs;
  }

  /**
   * The maximum number of virtual machines to be in a simulation.
   * 
   * @return maximum number of virtual machines to be in a simulation
   */
  public final int getMaxVirtualMachines() {
    return this.m_maxVMs;
  }

  /**
   * Obtain the virtual machine factory for the vms that will be running
   * inside the network.
   * 
   * @return the virtual machine factory of this parameter set
   */
  public final INetVirtualMachineFactory<MT, ?, ?> getNetVirtualMachineFactory() {
    return this.m_factory;
  }

  /**
   * The maximum messages a node is allowed to send.
   * 
   * @return The maximum messages a node is allowed to send.
   */
  public final int getMaxMessages() {
    return this.m_maxMessages;
  }

  /**
   * The maximum size of a message
   * 
   * @return the maximum size of a message
   */
  public final int getMaxMessageSize() {
    return this.m_maxMessageSize;
  }

  /**
   * Obtain the minimum message delay.
   * 
   * @return the minimum message delay
   */
  public final int getMinMessageDelay() {
    return this.m_minDelay;
  }

  /**
   * Obtain the maximum message delay.
   * 
   * @return the maximum message delay
   */
  public final int getMaxMessageDelay() {
    return this.m_maxDelay;
  }
}
