/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-10-09
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.election.lgp.LGPElectionNetwork.java
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

package test.org.dgpf.election.lgp;

import org.dgpf.lgp.base.EIndirection;
import org.dgpf.lgp.base.LGPMemory;
import org.dgpf.lgp.base.LGPProgram;

import test.org.dgpf.election.ElectionNetwork;
import test.org.dgpf.election.IElectionInformation;

/**
 * A square grid network for elections
 * 
 * @author Thomas Weise
 */
public class LGPElectionNetwork extends
    ElectionNetwork<LGPMemory, LGPProgram, int[]> implements
    IElectionInformation {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new network.
   * 
   * @param provider
   *          the network provider
   */
  public LGPElectionNetwork(final LGPElectionProvider provider) {
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
    return EIndirection.GLOBAL.read(1, this.getVirtualMachine(index));
  }
}
