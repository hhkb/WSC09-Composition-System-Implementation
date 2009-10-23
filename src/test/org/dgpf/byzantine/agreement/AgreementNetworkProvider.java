/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-03-07
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.cs.CSNetworkProvider.java
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

import org.dgpf.vm.net.IVirtualMachineNetworkParameters;
import org.dgpf.vm.net.byzantine.ByzantineIntBroadcastNetworkProvider;
import org.sigoa.spec.simulation.ISimulation;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * The provider for cs networks
 * 
 * @param <MT>
 *          the virtual machine memory type
 * @author Thomas Weise
 */
public class AgreementNetworkProvider<MT extends Serializable> extends
    ByzantineIntBroadcastNetworkProvider<MT> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the cs dwelling times
   */
  final int[] m_values;

  /**
   * Create a new network provider
   * 
   * @param clazz
   *          the network class
   * @param parameters
   *          the parameters
   * @param randomizeScenarios
   *          <code>true</code> if and only if the scenarios should be
   *          changed before each generation
   * @param scenarioCount
   *          the number of scenarios
   */
  public AgreementNetworkProvider(
      final Class<? extends AgreementNetwork<MT, ?>> clazz,
      final IVirtualMachineNetworkParameters<MT> parameters,
      final boolean randomizeScenarios, final int scenarioCount) {
    super(clazz, parameters, randomizeScenarios, scenarioCount, -1);

    this.m_values = new int[scenarioCount];
  }

  /**
   * Create a new network provider
   * 
   * @param parameters
   *          the parameters
   * @param randomizeScenarios
   *          <code>true</code> if and only if the scenarios should be
   *          changed before each generation
   * @param scenarioCount
   *          the number of scenarios
   */
  @SuppressWarnings("unchecked")
  public AgreementNetworkProvider(
      final IVirtualMachineNetworkParameters<MT> parameters,
      final boolean randomizeScenarios, final int scenarioCount) {
    this(
        ((Class<? extends AgreementNetwork<MT, ?>>) (AgreementNetwork.class)),
        parameters, randomizeScenarios, scenarioCount);
  }

  /**
   * Create a new instance of this simulator.
   * 
   * @return The newly created simulator instance.
   */
  @Override
  @SuppressWarnings("unchecked")
  public ISimulation<?> createSimulation() {
    return new AgreementNetwork(this);
  }

  /**
   * Create the scenarios
   * 
   * @param random
   *          the randomizer to be used for scenario creation
   * @param iteration
   *          the index of the iteration
   */
  @Override
  protected void createScenarios(final IRandomizer random,
      final long iteration) {
    int[] v;
    int i;

    super.createScenarios(random, iteration);

    v = this.m_values;
    for (i = (v.length - 1); i >= 0; i--) {
      do {
        v[i] = random.nextInt();
      } while (Math.abs(v[i]) < 10);
    }
  }

}
