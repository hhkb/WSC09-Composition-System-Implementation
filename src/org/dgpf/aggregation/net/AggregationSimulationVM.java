/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-23
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.aggregation.AggregationVM.java
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

import java.util.Arrays;

import org.dgpf.vm.net.Network;
import org.dgpf.vm.objectives.error.IErrorInformation;

/**
 * The virtual machine used for aggregation protocol evolution.
 * 
 * @author Thomas Weise
 */
public class AggregationSimulationVM extends AggregationNetVM implements
    IErrorInformation {

  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the wanted result
   */
  private double m_res;

  /**
   * Create a new network virtual machine.
   * 
   * @param network
   *          the network
   * @param index
   *          the index
   */
  @SuppressWarnings("unchecked")
  public AggregationSimulationVM(
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
    AggregationNetwork n;

    super.beginSimulation();
    Arrays.fill(this.m_memory, 0d);

    n = ((AggregationNetwork) (this.getNetwork()));

    this.m_res = n.m_results[n.m_scenario];
    this.m_memory[0] = n.m_values[n.m_scenario][n.m_vmIdx++];
  }

  /**
   * Obtain the absolute error
   * 
   * @return the absolute error
   */
  public double getAbsoluteError() {
    return Math.abs(this.getRawError());
  }

  /**
   * Obtain the square error
   * 
   * @return the square error
   */
  public double getSquareError() {
    double d;

    d = this.getRawError();
    d *= d;
    if (Double.isInfinite(d) || Double.isNaN(d))
      return Double.MAX_VALUE;
    return d;
  }

  /**
   * Obtain the raw error
   * 
   * @return the raw error
   */
  public double getRawError() {
    double d;

    d = (this.m_memory[1] - this.m_res);
    if (Double.isInfinite(d)) {
      if (d < 0)
        return -Double.MAX_VALUE;
      return Double.MAX_VALUE;
    }
    if (Double.isNaN(d))
      return Double.MAX_VALUE;
    return d;
  }
}
