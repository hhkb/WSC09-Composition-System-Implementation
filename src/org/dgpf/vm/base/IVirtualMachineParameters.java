/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-08-29
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.vm.IVirtualMachineParameters.java
 * Last modification: 2007-08-29
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
 * This interface defines the basic set of parameters for a virtual
 * machine.
 * 
 * @param <MT>
 *          the virtual machine memory type
 * @author Thomas Weise
 */
public interface IVirtualMachineParameters<MT extends Serializable>
    extends Serializable {

  /**
   * Obtain the memory size defined in this parameter set.
   * 
   * @return the memory size defined in this parameter set
   */
  public abstract int getMemorySize();

}
