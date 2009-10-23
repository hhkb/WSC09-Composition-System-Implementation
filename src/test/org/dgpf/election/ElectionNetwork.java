/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-14
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.election.ElectionNetwork.java
 * Last modification: 2007-12-14
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

package test.org.dgpf.election;

import java.io.Serializable;

import org.dgpf.vm.base.VirtualMachineProgram;
import org.dgpf.vm.net.LinearNetwork;
import org.dgpf.vm.net.NetworkProvider;

/**
 * The basic network for election algorithms
 * 
 * @param <MT>
 *          the virtual machine memory type
 * @param <PT>
 *          the virtual machine program type
 * @param <MDT>
 *          the message data type
 * @author Thomas Weise
 */
public abstract class ElectionNetwork<MT extends Serializable, PT extends VirtualMachineProgram<MT>, MDT extends Serializable>
    extends LinearNetwork<MT, PT, MDT> implements IElectionInformation {

  /**
   * the results
   */
  private final int[] m_results;

  /**
   * Create a new network.
   * 
   * @param provider
   *          the network provider
   */
  @SuppressWarnings("unchecked")
  public ElectionNetwork(final NetworkProvider<MT> provider) {
    super(provider);
    this.m_results = new int[provider.getParameters()
        .getMaxVirtualMachines()];
  }

  /**
   * Obtain the result for the virtual machine at the given index
   * 
   * @param index
   *          the virtual machine at the given index
   * @return the result for it
   */
  protected abstract int doGetResult(final int index);

  /**
   * This method is called when the simulation has ended
   * 
   * @throws IllegalStateException
   *           If this simulation is not yet running.
   */
  @Override
  public void endSimulation() {
    int i, c;
//    final int l;
//    boolean d;
    int[] results;

    c =  this.getVirtualMachineCount();
//    l=c;
//    d = true;
    results = this.m_results;
    for (--c; c >= 0; c--) {
      i = this.doGetResult(c);
      // if (this.isValidId(i)) {
      // if (i != this.getVirtualMachine(c).getId())
      // d = false;
      //      }
      results[c] = i;
    }

    // if (d)
    // Arrays.fill(results, 0, Math.max(1, (l / 3)), -1);

    super.endSimulation();
  }

  /**
   * Obtain the result of the <code>index</code>th virtual machine.
   * 
   * @param index
   *          the index of the vm
   * @return the result of the <code>index</code>th virtual machine
   */
  public int getResult(final int index) {
    return this.m_results[index];
  }
}
