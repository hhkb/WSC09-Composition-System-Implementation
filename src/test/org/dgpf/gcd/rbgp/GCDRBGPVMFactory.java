/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-22
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.gcd.rbgp.GCDRBGPVMFactory.java
 * Last modification: 2007-09-22
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

package test.org.dgpf.gcd.rbgp;

import org.dgpf.rbgp.base.RBGPMemory;
import org.dgpf.rbgp.base.RBGPProgram;
import org.dgpf.rbgp.single.RBGPParameters;
import org.dgpf.rbgp.single.RBGPVM;
import org.dgpf.vm.base.IHostedVirtualMachineFactory;
import org.dgpf.vm.base.VirtualMachineParameters;

/**
 * the gcd classifier factory
 * 
 * @author Thomas Weise
 */
public class GCDRBGPVMFactory
    implements
    IHostedVirtualMachineFactory<RBGPMemory, RBGPProgram, GCDRBGPVMProvider, RBGPVM<GCDRBGPVMProvider>> {

  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the common shared instance of this factory
   */
  public static final IHostedVirtualMachineFactory<RBGPMemory, RBGPProgram, GCDRBGPVMProvider, RBGPVM<GCDRBGPVMProvider>> FACTORY = new GCDRBGPVMFactory();

  /**
   * Create a new virtual machine with the given parameters.
   * 
   * @param parameters
   *          the parameters used for virtual machine creation
   * @param host
   *          the host of the virtual machine
   * @return the new virtual machine
   */
  @SuppressWarnings("unchecked")
  public RBGPVM<GCDRBGPVMProvider> createVirtualMachine(
      final VirtualMachineParameters<RBGPMemory> parameters,
      final GCDRBGPVMProvider host) {
    return (new GCDRBGPVM((RBGPParameters) parameters, host));
  }
}
