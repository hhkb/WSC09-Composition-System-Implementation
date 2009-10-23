/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-10-09
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.vm.net.objectives.INetVirtualMachineSimulationInformation.java
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

import org.dgpf.vm.objectives.IVirtualMachineSimulationInformation;

/**
 * the networked virtual machine information
 * 
 * @author Thomas Weise
 */
public interface INetVirtualMachineSimulationInformation extends
    IVirtualMachineSimulationInformation {

  /**
   * Obtain the number of neighbors.
   * 
   * @return the number of neighbors
   */
  public abstract int getNeighborCount();

  /**
   * Obtain the number of steps where this vm was idle.
   * 
   * @return the number of steps where this vm was idle
   */
  public abstract int getIdleSteps();

  /**
   * Obtain the number of messages sent.
   * 
   * @return the number of messages sent
   */
  public abstract int getMessageCount();
}
