/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-23
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.aggregation.AggregationProvider.java
 * Last modification: 2007-12-23
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

package org.dgpf.aggregation.net;

import org.dgpf.vm.net.NetworkProvider;
import org.sigoa.spec.simulation.ISimulation;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * The aggregation provider
 */
public class AggregationProvider extends NetworkProvider<double[]> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the values
   */
  final double[][] m_values;

  /**
   * the results
   */
  final double[] m_results;

  /**
   * the aggregation function
   */
  private final IAggregationFunction m_function;

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
  public AggregationProvider(final AggregationParameters parameters,
      final boolean randomizeScenarios, final int scenarioCount) {
    super(AggregationNetwork.class, parameters, randomizeScenarios,
        scenarioCount);

    this.m_values = new double[scenarioCount][parameters
        .getMaxVirtualMachines()];
    this.m_results = new double[scenarioCount];
    this.m_function = parameters.m_function;
  }

  /**
   * Create a new instance of this simulator.
   * 
   * @return The newly created simulator instance.
   */
  @Override
  public ISimulation<?> createSimulation() {
    return new AggregationNetwork(this);
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
    int i, j, c, k, x;
    final double[][] vals;
    double[] vv;
    final double[] res;
    final IAggregationFunction f;
    double r, d, e;

    super.createScenarios(random, iteration);

    f = this.m_function;
    vals = this.m_values;
    res = this.m_results;

    for (i = (vals.length - 1); i >= 0; i--) {
      c = this.getVMCount(i);
      vv = vals[i];

      for (j = (c - 1); j >= 0; j--) {
        vv[j] = (random.nextDouble() * 1e3);
      }

      r = 0d;
      outer: for (x = 10000; x >= 0; x--) {
        r = f.computeAggregate(vv, c);

        if (r <= 0d) {
          vv[random.nextInt(c)] = (random.nextDouble() * 1e3);
          continue outer;
        }

        for (k = (vals.length - 1); k > i; k--) {
          d = res[k];
          e = Math.abs(r - d);
          if ((e <= 0d) || ((e / Math.min(d, e)) < 0.11d)) {
            vv[random.nextInt(c)] = (random.nextDouble() * 1e3);
            continue outer;
          }
        }
        break;
      }

      res[i] = r;
    }
  }
}
