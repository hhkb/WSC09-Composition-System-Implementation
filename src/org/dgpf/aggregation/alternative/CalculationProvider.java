/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-22
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.aggregation.simulation.CalculationProvider.java
 * Last modification: 2006-12-22
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

package org.dgpf.aggregation.alternative;

import org.dgpf.aggregation.net.IAggregationFunction;
import org.sigoa.refimpl.simulation.SimulationProvider;
import org.sigoa.spec.simulation.ISimulation;

/**
 * The provider for simulations of single virtual machines.
 *
 * @author Thomas Weise
 */
public class CalculationProvider extends SimulationProvider {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the parameters
   */
  private final CalculationParameters m_cp;

  /**
   * the function
   */
  private final IAggregationFunction m_af;

  /**
   * Instantiate the simple network simulation provider.
   *
   * @param parameters
   *          the virtual machine parameters that go for the simulations.
   * @param f
   *          the function to be approximated
   */
  public CalculationProvider(final CalculationParameters parameters,
      final IAggregationFunction f) {
    super(Calculation.class);
    this.m_af = f;
    this.m_cp = parameters;
  }

  /**
   * Create a new instance of this simulator.
   *
   * @return The newly created simulator instance.
   */
  @Override
  public ISimulation<?> createSimulation() {
    return new Calculation(this.m_cp, this.m_af);
  }
}
