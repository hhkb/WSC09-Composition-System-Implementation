/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-03-13
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.rbgp.net.symbols.NetSizeSymbol.java
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

package org.dgpf.rbgp.net.symbols;

import org.dgpf.rbgp.base.RBGPMemory;
import org.dgpf.rbgp.base.Symbol;
import org.dgpf.rbgp.net.DefaultNetSymbolSet;
import org.dgpf.rbgp.net.RBGPNetVM;
import org.dgpf.vm.base.VirtualMachine;
import org.dgpf.vm.base.VirtualMachineProgram;

/**
 * this symbol helps the vm to determine how many vms are in the network
 * 
 * @author Thomas Weise
 */
public class NetSizeSymbol extends Symbol {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new symbol.
   * 
   * @param symbolSet
   *          The symbol set
   */
  public NetSizeSymbol(final DefaultNetSymbolSet symbolSet) {
    super(symbolSet, "netSize", 0, false, true); //$NON-NLS-1$
  }

  /**
   * Obtain the initial value of this symbol
   * 
   * @param vm
   *          the virtual machine
   * @return the initial value of this symbol
   */
  @Override
  public int getInitialValue(
      final VirtualMachine<RBGPMemory, ? extends VirtualMachineProgram<RBGPMemory>> vm) {
    return ((RBGPNetVM) vm).getNetwork().getVirtualMachineCount();
  }
}
