/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-20
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.vm.net.NetworkProvider.java
 * Last modification: 2007-09-20
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

package org.dgpf.vm.net;

import java.io.Serializable;

import org.sfc.math.Mathematics;
import org.sigoa.refimpl.simulation.RandomizedScenarioSimulationProvider;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * The simulation provider for virtual machine networks.
 * 
 * @param <MT>
 *          the virtual machine memory type
 * @author Thomas Weise
 */
public class NetworkProvider<MT extends Serializable> extends
    RandomizedScenarioSimulationProvider {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the maximum id
   */
  public static final int MAX_ID = (1 << 11);

  /**
   * the virtual machine network parameters
   */
  protected final IVirtualMachineNetworkParameters<MT> m_parameters;

  /**
   * the minimum vms
   */
  private final int m_minVM;

  /**
   * the vm range
   */
  private final int m_vmRange;

  /**
   * the minimum delay
   */
  private final int m_minDelay;

  /**
   * the delay range
   */
  private final int m_delayRange;

  /**
   * the vm count
   */
  protected final int[] m_vmCount;

  /**
   * the delays
   */
  final int[][] m_delays;

  /**
   * the steps
   */
  final int[][] m_steps;

  /**
   * the id buffer
   */
  final int[][] m_ids;

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
  public NetworkProvider(final Class<? extends Network<MT, ?, ?>> clazz,
      final IVirtualMachineNetworkParameters<MT> parameters,
      final boolean randomizeScenarios, final int scenarioCount) {
    super(clazz, randomizeScenarios, scenarioCount);

    int c;

    this.m_parameters = parameters;

    this.m_minVM = Math.max(3, parameters.getMinVirtualMachines());
    this.m_vmRange = Math.max(0, parameters.getMaxVirtualMachines()
        - this.m_minVM) + 1;

    this.m_ids = new int[scenarioCount][this.m_minVM + this.m_vmRange];
    this.m_vmCount = new int[scenarioCount];

    this.m_minDelay = Math.max(0, parameters.getMinMessageDelay());
    this.m_delayRange = Math.max(0, parameters.getMaxMessageDelay()
        - this.m_minDelay) + 1;

    c = Mathematics.nextPrime((this.m_vmRange + this.m_minVM) * 128);
    this.m_delays = new int[scenarioCount][c];
    c = Mathematics.nextPrime((int) (Math.pow(c, 1.02d)));
    this.m_steps = new int[scenarioCount][c];
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
    int[] a;
    int i, m, x, z;

    for (z = (this.m_vmCount.length - 1); z >= 0; z--) {
      this.m_vmCount[z] = x = (this.m_minVM + random
          .nextInt(this.m_vmRange));

      m = this.m_minDelay;
      x = this.m_delayRange;
      a = this.m_delays[z];
      for (i = (a.length - 1); i >= 0; i--) {
        a[i] = (m + random.nextInt(x));
      }

      a = this.m_steps[z];
      for (i = (a.length - 1); i >= 0; i--) {
        do {
          x = random.nextInt();
        } while (x < 0);
        a[i] = x;
      }

      a = this.m_ids[z];
      for (i = (a.length - 1); i >= 0; i--) {
        a[i] = random.nextInt(MAX_ID);
      }
    }
  }

  /**
   * Obtain the virtual machine parameters of this network provider.
   * 
   * @return the parameters of this network provider
   */
  public final IVirtualMachineNetworkParameters<MT> getParameters() {
    return this.m_parameters;
  }

  /**
   * Obtain the number of vms in a given scenario.
   * 
   * @param scenario
   *          the scenario index
   * @return the number of vms in the scenario
   */
  protected int getVMCount(final int scenario) {
    return this.m_vmCount[scenario];
  }
}
