/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-10-09
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.election.rbgp.RBGPElectionNetwork.java
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

package test.org.dgpf.election.rbgp;

import org.dgpf.rbgp.base.RBGPMemory;
import org.dgpf.rbgp.base.RBGPProgramBase;
import org.dgpf.rbgp.base.Symbol;
import org.dgpf.rbgp.net.RBGPNetVM;

import test.org.dgpf.election.ElectionNetwork;

/**
 * A square grid network for elections
 * 
 * @author Thomas Weise
 */
public class RBGPElectionNetwork extends
    ElectionNetwork<RBGPMemory, RBGPProgramBase, int[]> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the first variable
   */
  private static final Symbol VAR1 = ElectionRBGPTestSeries.PARAMETERS
      .getSymbols().getVariable(0);

  /**
   * Create a new network.
   * 
   * @param provider
   *          the network provider
   */
  public RBGPElectionNetwork(final RBGPElectionProvider provider) {
    super(provider);
  }

  /**
   * Obtain the result of the <code>index</code>th virtual machine.
   * 
   * @param index
   *          the index of the vm
   * @return the result of the <code>index</code>th virtual machine
   */
  @Override
  protected int doGetResult(final int index) {
    return ((RBGPNetVM) (this.getVirtualMachine(index)))
        .getNextValue(VAR1);
  }
}
