/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-14
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.vm.IVirtualMachineFactory.java
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
 * A factory for simple virtual machines
 * 
 * @param <MT>
 *          the virtual machine memory type
 * @param <PT>
 *          the program type
 * @author Thomas Weise
 */
public interface IVirtualMachineFactory<MT extends Serializable, PT extends VirtualMachineProgram<MT>>
    extends Serializable {

  /**
   * Create a new virtual machine with the given parameters.
   * 
   * @param parameters
   *          the parameters used for virtual machine creation
   * @return the new virtual machine
   */
  public VirtualMachine<MT, PT> createVirtualMachine(
      final VirtualMachineParameters<MT> parameters);
}
