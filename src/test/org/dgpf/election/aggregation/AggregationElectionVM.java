/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-21
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.election.aggregation.AggregationElectionVM.java
 * Last modification: 2007-12-21
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

package test.org.dgpf.election.aggregation;

import org.dgpf.aggregation.net.AggregationNetProgram;
import org.dgpf.aggregation.net.AggregationNetVM;
import org.dgpf.vm.net.Network;

/**
 * The aggregation election virtual machine.
 * 
 * @author Thomas Weise
 */
public class AggregationElectionVM extends AggregationNetVM {

  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new network virtual machine.
   * 
   * @param network
   *          the network
   * @param index
   *          the index
   */
  @SuppressWarnings("unchecked")
  public AggregationElectionVM(
      final Network<double[], AggregationNetProgram, double[]> network,
      final int index) {
    super(network, index);
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
    this.m_memory[0] = this.getId();
  }

}
