/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-03-07
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.cs.CSNetwork.java
 * Last modification: 2008-03-07
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

package test.org.dgpf.byzantine.agreement;

import java.io.Serializable;

import org.dgpf.vm.base.VirtualMachineProgram;
import org.dgpf.vm.net.Message;
import org.dgpf.vm.net.byzantine.ByzantineIntBroadcastNetwork;

/**
 * The critical section network
 * 
 * @param <MT>
 *          the virtual machine memory type
 * @param <PT>
 *          the virtual machine program type
 * @author Thomas Weise
 */
public class AgreementNetwork<MT extends Serializable, PT extends VirtualMachineProgram<MT>>
    extends ByzantineIntBroadcastNetwork<MT, PT> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the values
   */
  private final int[] m_vals;

  /**
   * the current value
   */
  int m_cur;

  /**
   * Create a new network.
   * 
   * @param provider
   *          the network provider
   */
  @SuppressWarnings("unchecked")
  public AgreementNetwork(final AgreementNetworkProvider<MT> provider) {
    super(provider);
    this.m_vals = provider.m_values;
  }

  /**
   * This method is called right before the simulation begins.
   * 
   * @throws IllegalStateException
   *           if this simulation is already running.
   */
  @Override
  public void beginSimulation() {
    super.beginSimulation();
    this.m_cur = this.m_vals[this.getSimulationIndex()];
  }

  /**
   * Deliver a certain message.
   * 
   * @param message
   *          the message to be delivered
   */
  @Override
  protected void send(final Message<int[]> message) {
    int i;
    int[] d;

    d = message.m_data;
    loop: for (i = (message.m_size - 1); i >= 0; i--) {
      if (d[i] == 0) {
        d[i] = this.m_cur;
        break loop;
      }
    }

    super.send(message);
  }
}
