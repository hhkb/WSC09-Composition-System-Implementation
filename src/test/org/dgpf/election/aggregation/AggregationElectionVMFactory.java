/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-22
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.election.aggregation.AggregationNetVMFactory.java
 * Last modification: 2007-12-22
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
import org.dgpf.aggregation.net.DefaultAggregationNetVMFactory;
import org.dgpf.vm.net.INetVirtualMachineFactory;
import org.dgpf.vm.net.NetVirtualMachine;
import org.dgpf.vm.net.Network;

/**
 * the factory for aggregation network factories
 * 
 * @author Thomas Weise
 */
public class AggregationElectionVMFactory extends
    DefaultAggregationNetVMFactory {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the globally shared default factory for aggregation vms
   */
  public static final INetVirtualMachineFactory<double[], AggregationNetProgram, double[]> AGGREGATION_ELECTION_VM_FACTORY = new AggregationElectionVMFactory();

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
  public NetVirtualMachine<double[], AggregationNetProgram, double[]> createNetVirtualMachine(
      final Network<double[], AggregationNetProgram, double[]> network,
      final int index) {
    return new AggregationElectionVM(network, index);
  }

  /**
   * read resolve
   * 
   * @return the read resolution
   */
  private final Object readResolve() {
    if (this.getClass() == AggregationElectionVMFactory.class)
      return AGGREGATION_ELECTION_VM_FACTORY;
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