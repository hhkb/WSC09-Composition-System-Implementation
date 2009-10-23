/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-18
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.gcd.lgp.GCDLGPVMFactory.java
 * Last modification: 2007-11-18
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

package test.org.dgpf.gcd.lgp;

import org.dgpf.lgp.base.LGPMemory;
import org.dgpf.lgp.base.LGPProgram;
import org.dgpf.lgp.single.LGPParameters;
import org.dgpf.lgp.single.LGPVM;
import org.dgpf.vm.base.IHostedVirtualMachineFactory;
import org.dgpf.vm.base.VirtualMachineParameters;

/**
 * The vm factory for linear genetic programming of the gcd problem
 * 
 * @author Thomas Weise
 */
public class GCDLGPVMFactory
    implements
    IHostedVirtualMachineFactory<LGPMemory, LGPProgram, GCDLGPVMProvider, LGPVM<GCDLGPVMProvider>> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the common shared instance of this factory
   */
  public static final IHostedVirtualMachineFactory<LGPMemory, LGPProgram, GCDLGPVMProvider, LGPVM<GCDLGPVMProvider>> FACTORY = new GCDLGPVMFactory();

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
  public LGPVM<GCDLGPVMProvider> createVirtualMachine(
      final VirtualMachineParameters<LGPMemory> parameters,
      final GCDLGPVMProvider host) {
    return (new GCDLGPVM((LGPParameters) parameters, host));
  }
}
