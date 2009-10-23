/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-14
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.lgp.single.LGPParameters.java
 * Last modification: 2007-11-14
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

import java.lang.reflect.Field;

import org.dgpf.lgp.base.ILGPParameters;
import org.dgpf.lgp.base.InstructionSet;
import org.dgpf.lgp.base.LGPMemory;
import org.dgpf.vm.net.INetVirtualMachineFactory;
import org.dgpf.vm.net.VirtualMachineNetworkParameters;
import org.sfc.text.TextUtils;
import org.sfc.utils.Classes;

/**
 * The virtual machine parameters
 * 
 * @author Thomas Weise
 */
public class LGPNetParameters extends
    VirtualMachineNetworkParameters<LGPMemory> implements ILGPParameters {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the default parameter settings
   */
  public static final LGPNetParameters DEFAULT_PARAMETERS = new LGPNetParameters(
      null, 4, 4, 4, 10, 4, 16,
      DefaultLGPNetVMFactory.DEFAULT_LGP_NET_VM_FACTORY, 2000, 1, 32);

  /**
   * the instructions
   */
  private final InstructionSet m_instructions;

  /**
   * the global memory size of the hosted virtual machines
   */
  final int m_globalSize;

  /**
   * the local memory size of the hosted virtual machines
   */
  final int m_localSize;

  /**
   * the stack size
   */
  final int m_stackSize;

  /**
   * the maximum call depths of the hosted virtual machines.
   */
  final int m_maxCallDepth;

  /**
   * Create a linear genetic programming parameter set
   * 
   * @param instructions
   *          the instruction set
   * @param globalSize
   *          the global memory size
   * @param localSize
   *          the local memory size
   * @param stackSize
   *          the stack size
   * @param maxCallDepth
   *          the maximum call depth
   * @param minVMs
   *          the minimum number of virtual machines
   * @param maxVMs
   *          the maximum number of virtual machines
   * @param factory
   *          the networked virtual machine factory of this parameter
   *          setting
   * @param maxMessages
   *          the maximum messages a node is allowed to send
   * @param minDelay
   *          the minimum message delay
   * @param maxDelay
   *          the maximum message delay
   */
  @SuppressWarnings("unchecked")
  public LGPNetParameters(final LGPNetVMInstructionSet instructions,
      final int globalSize, final int localSize, final int stackSize,
      final int maxCallDepth, final int minVMs, final int maxVMs,
      final INetVirtualMachineFactory<LGPMemory, ?, ?> factory,
      final int maxMessages, final int minDelay, final int maxDelay

  ) {
    super(globalSize + (maxCallDepth * (localSize + stackSize)), minVMs,
        maxVMs, (factory != null) ? factory
            : DefaultLGPNetVMFactory.DEFAULT_LGP_NET_VM_FACTORY,
        maxMessages, stackSize, minDelay, maxDelay);

    this.m_instructions = ((instructions != null) ? instructions
        : LGPNetVMInstructionSet.DEFAULT_NET_VM_INSTRUCTION_SET);

    if ((globalSize < 1) || (localSize < 1) || (stackSize < 1)
        || (maxCallDepth < 0))
      throw new IllegalArgumentException();
    this.m_globalSize = globalSize;
    this.m_maxCallDepth = maxCallDepth;
    this.m_localSize = localSize;
    this.m_stackSize = stackSize;
  }

  /**
   * Obtain the instructions available to the programs
   * 
   * @return the instructions available to the programs
   */
  public final InstructionSet getInstructions() {
    return this.m_instructions;
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

    m = Classes.findStaticField(this.m_instructions);
    if (m != null)
      TextUtils.appendStaticField(m, sb);
    else
      this.m_instructions.javaToStringBuilder(sb, indent);// nonsense, but
    // whatever
  }

  /**
   * obtain the global memory size of the hosted vms.
   * 
   * @return the global memory size of the hosted vms
   */
  public int getGlobalMemorySize() {
    return this.m_globalSize;
  }

  /**
   * obtain the local memory size of the hosted vms.
   * 
   * @return the local memory size of the hosted vms
   */
  public int getLocalMemorySize() {
    return this.m_localSize;
  }

  /**
   * Obtain the maximum call depth of the hosted vms.
   * 
   * @return the maximum call depth of the hosted vms
   */
  public int getMaxCallDepth() {
    return this.m_maxCallDepth;
  }

  /**
   * Obtain the stack size.
   * 
   * @return the stack size
   */
  public int getStackSize() {
    return this.m_stackSize;
  }
}
