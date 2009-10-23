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

package org.dgpf.vm.net.byzantine;

import java.io.Serializable;
import java.util.Arrays;

import org.dgpf.vm.net.IVirtualMachineNetworkParameters;
import org.dgpf.vm.net.NetworkProvider;
import org.sigoa.spec.simulation.ISimulation;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * The provider for cs networks
 * 
 * @param <MT>
 *          the virtual machine memory type
 * @author Thomas Weise
 */
public class ByzantineIntBroadcastNetworkProvider<MT extends Serializable>
    extends NetworkProvider<MT> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the traitors
   */
  final int[][] m_traitors;

  /**
   * the errors
   */
  final int[][] m_errors;

  /**
   * the byzantine errors the might occur
   */
  private final int m_mask;

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
   * @param byzantineMask
   *          the byzantine errors the might occur
   */
  public ByzantineIntBroadcastNetworkProvider(
      final Class<? extends ByzantineIntBroadcastNetwork<MT, ?>> clazz,
      final IVirtualMachineNetworkParameters<MT> parameters,
      final boolean randomizeScenarios, final int scenarioCount,
      final int byzantineMask) {
    super(clazz, parameters, randomizeScenarios, scenarioCount);

    int i = parameters.getMaxVirtualMachines();
    this.m_mask = byzantineMask;
    this.m_traitors = new int[scenarioCount][i];
    this.m_errors = new int[scenarioCount][i * parameters.getMaxMessages()
        * Math.min(2, parameters.getMaxMessageSize())];
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
   * @param baseDwell
   *          the time each node will dwell at least inside the cs
   * @param byzantineMask
   *          the byzantine errors the might occur
   */
  @SuppressWarnings("unchecked")
  public ByzantineIntBroadcastNetworkProvider(
      final IVirtualMachineNetworkParameters<MT> parameters,
      final boolean randomizeScenarios, final int scenarioCount,
      final int byzantineMask) {
    this(
        ((Class<? extends ByzantineIntBroadcastNetwork<MT, ?>>) (ByzantineIntBroadcastNetwork.class)),
        parameters, randomizeScenarios, scenarioCount, byzantineMask);
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
   * @param baseDwell
   *          the time each node will dwell at least inside the cs
   */
  public ByzantineIntBroadcastNetworkProvider(
      final IVirtualMachineNetworkParameters<MT> parameters,
      final boolean randomizeScenarios, final int scenarioCount) {
    this(parameters, randomizeScenarios, scenarioCount, -1);
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
    final int[][] traitors;
    final int[][] errors;
    int[] zz;
    int c, vmc, x, y, l;
    final int mask;

    super.createScenarios(random, iteration);

    traitors = this.m_traitors;
    errors = this.m_errors;
    mask = this.m_mask;

    for (c = (traitors.length - 1); c >= 0; c--) {
      zz = traitors[c];

      Arrays.fill(zz, 0);
      if (mask != 0) {
        vmc = this.m_vmCount[c];

        x = random.nextInt(vmc);
        y = random.nextInt(vmc);
        if ((y > 0) && (y < x))
          x = y;

        for (; x > 0; x--) {
          
          do {
            y = random.nextInt(vmc);
          } while (zz[y] != 0);
          
          do {
            l = random.nextInt(ByzantineType.ERROR_SET) & mask;
          } while ((l == 0) || // 
              (((l & ByzantineType.SEND_DIFFERENT_ERRORS) != 0) && // 
              ((l & ByzantineType.SEND_SAME_ERRORS) != 0)));
          
          zz[y] = l;
        }
      }

      zz = errors[c];
      for (x = (zz.length - 1); x >= 0; x--) {
        zz[x] = random.nextInt();
      }
    }    
  }

  /**
   * Create a new instance of this simulator.
   * 
   * @return The newly created simulator instance.
   */
  @Override
  @SuppressWarnings("unchecked")
  public ISimulation<?> createSimulation() {
    return new ByzantineIntBroadcastNetwork(this);
  }
}
