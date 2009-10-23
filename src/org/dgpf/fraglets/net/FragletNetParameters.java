/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-15
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.fraglets.net.FragletNetParameters.java
 * Last modification: 2007-12-15
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

package org.dgpf.fraglets.net;

import org.dgpf.fraglets.base.FragletStore;
import org.dgpf.fraglets.base.IFragletParameters;
import org.dgpf.fraglets.base.InstructionSet;
import org.dgpf.vm.net.INetVirtualMachineFactory;
import org.dgpf.vm.net.VirtualMachineNetworkParameters;

/**
 * The fraglet network parameters
 * 
 * @author Thomas Weise
 */
public class FragletNetParameters extends
    VirtualMachineNetworkParameters<FragletStore> implements
    IFragletParameters {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the maximum fraglet count
   */
  private final int m_maxFragletCount;

  /**
   * the instruction set
   */
  private final InstructionSet m_instr;

  /**
   * Create a new virtual machine network parameter set.
   * 
   * @param instr
   *          the instruction set
   * @param maxFragletCount
   *          the maximum number of fraglets in total
   * @param memorySize
   *          the memory size which is the maximum number of different
   *          fraglets allowed
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
   *          the maximum message size which equals the maximum length of a
   *          fraglet
   * @param minDelay
   *          the minimum message delay
   * @param maxDelay
   *          the maximum message delay
   */
  public FragletNetParameters(final NetInstructionSet instr,
      final int maxFragletCount, final int memorySize, final int minVMs,
      final int maxVMs,
      final INetVirtualMachineFactory<FragletStore, ?, ?> factory,
      final int maxMessages, final int maxMessageSize, final int minDelay,
      final int maxDelay) {
    super(memorySize, minVMs, maxVMs, (factory != null) ? factory
        : DefaultFragletNetVMFactory.DEFAULT_FRAGLET_NET_VM_FACTORY,
        maxMessages, maxMessageSize, minDelay, maxDelay);
    this.m_maxFragletCount = maxFragletCount;
    this.m_instr = instr;
  }

  /**
   * Obtain the maximum size of a fraglet.
   * 
   * @return the maximum size of a fraglet
   */
  public final int getMaxFragletSize() {
    return this.getMaxMessageSize();
  }

  /**
   * Obtain the maximum total number of fraglets.
   * 
   * @return the maximum total number of fraglets
   */
  public final int getMaxFragletCount() {
    return this.m_maxFragletCount;
  }

  /**
   * Obtain the fraglet instruction set
   * 
   * @return the fraglet instruction set
   */
  public final InstructionSet getInstructionSet() {
    return this.m_instr;
  }
}
