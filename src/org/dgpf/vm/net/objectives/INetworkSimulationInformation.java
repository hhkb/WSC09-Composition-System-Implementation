/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-10-09
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.vm.net.objectives.INetworkSimulationInformation.java
 * Last modification: 2007-10-09
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

package org.dgpf.vm.net.objectives;

import org.dgpf.vm.net.NetVirtualMachine;

/**
 * This interfaces allows you to access simulation information for virtual
 * machine networks.
 * 
 * @author Thomas Weise
 */
public interface INetworkSimulationInformation extends
    INetVirtualMachineSimulationInformation {

  /**
   * Obtain the number of virtual machines in the network.
   * 
   * @return the number of virtual machines in the network
   */
  public abstract int getVirtualMachineCount();

  /**
   * Access the virtual machine at the given index
   * 
   * @param index
   *          the index
   * @return the virtual machine at that position
   */
  public abstract NetVirtualMachine<?, ?, ?> getVirtualMachine(
      final int index);

  /**
   * Check whether the given id is valid or not.
   * 
   * @param id
   *          the id to be checked
   * @return <code>true</code> if and only if <code>id</code> is a
   *         valid id
   */
  public abstract boolean isValidId(final int id);
}
