/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-19
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.lgp.base.ILGPParameters.java
 * Last modification: 2007-11-19
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

package org.dgpf.lgp.base;

import org.dgpf.vm.base.IVirtualMachineParameters;

/**
 * The lgp vm parameter interface
 * 
 * @author Thomas Weise
 */
public interface ILGPParameters extends
    IVirtualMachineParameters<LGPMemory> {

  /**
   * Obtain the instructions available to the programs
   * 
   * @return the instructions available to the programs
   */
  public abstract InstructionSet getInstructions();

  /**
   * obtain the global memory size of the hosted vms.
   * 
   * @return the global memory size of the hosted vms
   */
  public abstract int getGlobalMemorySize();

  /**
   * obtain the local memory size of the hosted vms.
   * 
   * @return the local memory size of the hosted vms
   */
  public abstract int getLocalMemorySize();

  /**
   * Obtain the maximum call depth of the hosted vms.
   * 
   * @return the maximum call depth of the hosted vms
   */
  public abstract int getMaxCallDepth();

  /**
   * Obtain the stack size.
   * 
   * @return the stack size
   */
  public abstract int getStackSize();
}
