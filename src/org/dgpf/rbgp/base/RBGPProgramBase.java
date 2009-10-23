/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-05-27
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.rbgp.single.RBGPProgramBase.java
 * Last modification: 2007-05-27
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

package org.dgpf.rbgp.base;

import java.io.Serializable;

import org.dgpf.vm.base.EVirtualMachineState;
import org.dgpf.vm.base.VirtualMachine;
import org.dgpf.vm.base.VirtualMachineProgram;

/**
 * A symbolic classifier
 * 
 * @author Thomas Weise
 */
public abstract class RBGPProgramBase extends
    VirtualMachineProgram<RBGPMemory> implements Serializable {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * perform the classification on a vm.
   * 
   * @param vm
   *          the vm to work on
   * @return the resulting virtual machine state
   */
  public abstract EVirtualMachineState perform(
      final VirtualMachine<RBGPMemory, ? extends RBGPProgramBase> vm);

}
