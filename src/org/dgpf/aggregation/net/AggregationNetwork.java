/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-23
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.aggregation.AggregationNetwork.java
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

import org.dgpf.vm.net.SquareGridNetwork;
import org.dgpf.vm.objectives.error.IErrorInformation;

/**
 * The aggregation network.
 * 
 * @author Thomas Weise
 */
public class AggregationNetwork extends
    SquareGridNetwork<double[], AggregationNetProgram, double[]> implements
    IErrorInformation {
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
   * the scenario
   */
  int m_scenario;

  /**
   * the vm index
   */
  int m_vmIdx;

  /**
   * Create a new network.
   * 
   * @param provider
   *          the network provider
   */
  public AggregationNetwork(final AggregationProvider provider) {
    super(provider);
    this.m_values = provider.m_values;
    this.m_results = provider.m_results;
  }

  /**
   * Begin the simulation of the specified individual.
   * 
   * @param what
   *          the individual
   * @throws NullPointerException
   *           if <code>what</code> is <code>null</code>.
   * @throws IllegalStateException
   *           if this simulation is already running.
   */
  @Override
  public void beginIndividual(final AggregationNetProgram what) {
    this.m_scenario = 0;
    super.beginIndividual(what);
  }

  /**
   * This method is called right before the simulation begins.
   * 
   * @throws IllegalStateException
   *           if this simulation is already running.
   */
  @Override
  public void beginSimulation() {
    this.m_vmIdx = 0;
    super.beginSimulation();
    this.m_scenario++;
  }

  /**
   * Obtain the result of the <code>index</code>th virtual machine.
   * 
   * @param index
   *          the index of the vm
   * @return the result of the <code>index</code>th virtual machine
   */
  public double getResult(final int index) {
    return this.getVirtualMachine(index).m_memory[1];
  }

  /**
   * Obtain the absolute error
   * 
   * @return the absolute error
   */
  public double getAbsoluteError() {
    double d;
    int i;

    d = 0d;
    for (i = (this.getVirtualMachineCount() - 1); i >= 0; i--) {
      d += ((IErrorInformation) (this.getVirtualMachine(i)))
          .getAbsoluteError();
    }

    if (Double.isInfinite(d) || Double.isNaN(d))
      return Double.MAX_VALUE;
    return d;
  }

  /**
   * Obtain the square error
   * 
   * @return the square error
   */
  public double getSquareError() {

    double d;
    int i;

    d = 0d;
    for (i = (this.getVirtualMachineCount() - 1); i >= 0; i--) {
      d += ((IErrorInformation) (this.getVirtualMachine(i)))
          .getSquareError();
    }

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
    int i;

    d = 0d;
    for (i = (this.getVirtualMachineCount() - 1); i >= 0; i--) {
      d += ((IErrorInformation) (this.getVirtualMachine(i))).getRawError();
    }

    if (Double.isInfinite(d)) {
      if (d < 0d)
        return -Double.MAX_VALUE;
      return Double.MAX_VALUE;
    }
    if (Double.isNaN(d))
      return Double.MAX_VALUE;
    return d;
  }
}
