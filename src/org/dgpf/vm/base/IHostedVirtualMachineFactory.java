/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-14
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.vm.IHostedVirtualMachineFactory.java
 * Last modification: 2007-09-14
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

package org.dgpf.vm.base;

import java.io.Serializable;

/**
 * A factory for hosted virtual machines
 * 
 * @param <MT>
 *          the virtual machine memory type
 * @param <PT>
 *          the program type
 * @param <HT>
 *          the host type
 * @param <VT>
 *          the virtual machine type
 * @author Thomas Weise
 */
public interface IHostedVirtualMachineFactory<MT extends Serializable, PT extends VirtualMachineProgram<MT>, HT extends Serializable, VT extends HostedVirtualMachine<MT, PT, HT>>
    extends Serializable {

  /**
   * Create a new virtual machine with the given parameters.
   * 
   * @param parameters
   *          the parameters used for virtual machine creation
   * @param host
   *          the host of the virtual machine
   * @return the new virtual machine
   */
  public VT createVirtualMachine(
      final VirtualMachineParameters<MT> parameters, final HT host);
}
