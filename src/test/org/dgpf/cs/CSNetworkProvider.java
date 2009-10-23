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

package test.org.dgpf.cs;

import java.io.Serializable;

import org.dgpf.vm.net.IVirtualMachineNetworkParameters;
import org.dgpf.vm.net.NetworkProvider;
import org.sfc.math.Mathematics;
import org.sigoa.spec.simulation.ISimulation;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * The provider for cs networks
 * 
 * @param <MT>
 *          the virtual machine memory type
 * @author Thomas Weise
 */
public class CSNetworkProvider<MT extends Serializable> extends
    NetworkProvider<MT> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the cs dwelling times
   */
  final int[][] m_csLengths;

  /**
   * the time each node will dwell at least inside the cs
   */
  final int m_baseDwell;

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
   * @param baseDwell
   *          the time each node will dwell at least inside the cs
   */
  public CSNetworkProvider(
      final Class<? extends CSNetwork<MT, ?, ?>> clazz,
      final IVirtualMachineNetworkParameters<MT> parameters,
      final boolean randomizeScenarios, final int scenarioCount,
      final int baseDwell) {
    super(clazz, parameters, randomizeScenarios, scenarioCount);

    int c, i;
    int[][] d;

    this.m_csLengths = d = new int[scenarioCount][];
    c = 100;
    for (i = (scenarioCount - 1); i >= 0; i--) {
      c = Mathematics.nextPrime(c + 36);
      d[i] = new int[c];
    }

    this.m_baseDwell = Math.max(baseDwell, 10);
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
  @SuppressWarnings("unchecked")
  public CSNetworkProvider(
      final IVirtualMachineNetworkParameters<MT> parameters,
      final boolean randomizeScenarios, final int scenarioCount,
      final int baseDwell) {
    this(((Class<? extends CSNetwork<MT, ?, ?>>) (CSNetwork.class)),
        parameters, randomizeScenarios, scenarioCount, baseDwell);
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
    int[][] d2;
    int[] d;
    int i, j;
    final int b;

    b = this.m_baseDwell;
    d2 = this.m_csLengths;
    for (i = (d2.length - 1); i >= 0; i--) {
      d = d2[i];
      for (j = (d.length - 1); j >= 0; j--) {
        d[j] = b + random.nextInt(b << 1);
      }
    }

    super.createScenarios(random, iteration);
  }

  /**
   * Create a new instance of this simulator.
   * 
   * @return The newly created simulator instance.
   */
  @Override
  @SuppressWarnings("unchecked")
  public ISimulation<?> createSimulation() {
    return new CSNetwork(this);
  }
}
