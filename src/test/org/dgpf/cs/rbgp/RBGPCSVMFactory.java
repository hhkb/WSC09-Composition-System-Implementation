/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-03-13
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.cs.rbgp.RBGPVMFactory.java
 * Last modification: 2008-03-13
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

package test.org.dgpf.cs.rbgp;

import org.dgpf.rbgp.base.RBGPMemory;
import org.dgpf.rbgp.base.RBGPProgramBase;
import org.dgpf.rbgp.net.DefaultNetRBGPVMFactory;
import org.dgpf.vm.net.INetVirtualMachineFactory;
import org.dgpf.vm.net.NetVirtualMachine;
import org.dgpf.vm.net.Network;

import test.org.dgpf.cs.CSNetwork;

/**
 * the virtual machine factory
 * 
 * @author Thomas Weise
 */
public class RBGPCSVMFactory extends DefaultNetRBGPVMFactory {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the globally shared default factory for rbgp vms
   */
  public static final INetVirtualMachineFactory<RBGPMemory, RBGPProgramBase, int[]> DEFAULT_CS_VM_FACTORY = new RBGPCSVMFactory();

  /**
   * Create a new networked virtual machine to be placed into the given
   * network.
   * 
   * @param network
   *          the network the new vm will be part of
   * @param index
   *          the index
   * @return the new virtual machine
   */
  @Override
  @SuppressWarnings("unchecked")
  public NetVirtualMachine<RBGPMemory, RBGPProgramBase, int[]> createNetVirtualMachine(
      final Network<RBGPMemory, RBGPProgramBase, int[]> network,
      final int index) {
    return new RBGPCSVM((CSNetwork) network, index);
  }

  /**
   * read resolve
   * 
   * @return the read resolution
   */
  private final Object readResolve() {
    if (this.getClass() == RBGPCSVMFactory.class)
      return DEFAULT_CS_VM_FACTORY;
    return this;
  }

  /**
   * write replace
   * 
   * @return the write replacement
   */
  private final Object writeReplace() {
    return readResolve();
  }

}
