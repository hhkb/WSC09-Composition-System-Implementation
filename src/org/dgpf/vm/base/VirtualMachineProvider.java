/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-14
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.vm.VirtualMachineProvider.java
 * Last modification: 2007-09-14
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

package org.dgpf.vm.base;

import java.io.Serializable;

import org.sigoa.refimpl.simulation.RandomizedScenarioSimulationProvider;
import org.sigoa.spec.simulation.ISimulation;

/**
 * A provider for virtual machines.
 * 
 * @param <MT>
 *          the virtual machine memory type
 * @param <PT>
 *          the program type
 * @author Thomas Weise
 */
public class VirtualMachineProvider<MT extends Serializable, PT extends VirtualMachineProgram<MT>>
    extends RandomizedScenarioSimulationProvider {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the virtual machine parameters
   */
  private final VirtualMachineParameters<MT> m_parameters;

  /**
   * the virtual machine factory
   */
  private final IVirtualMachineFactory<MT, PT> m_factory;

  /**
   * Create a new virtual machine provider
   * 
   * @param parameters
   *          the virtual machine parameters
   * @param clazz
   *          the class of virtual machines to be created
   * @param randomizeScenarios
   *          <code>true</code> if and only if the scenarios should be
   *          changed before each generation
   * @param scenarioCount
   *          the number of scenarios
   */
  public VirtualMachineProvider(
      final VirtualMachineParameters<MT> parameters,
      final Class<? extends VirtualMachine<MT, PT>> clazz,
      final boolean randomizeScenarios, final int scenarioCount) {
    this(parameters, ((IVirtualMachineFactory<MT, PT>) null), clazz,
        randomizeScenarios, scenarioCount);
  }

  /**
   * Create a new virtual machine provider
   * 
   * @param parameters
   *          the virtual machine parameters
   * @param factory
   *          the virtual machine factory
   * @param clazz
   *          the class of virtual machines to be created
   * @param randomizeScenarios
   *          <code>true</code> if and only if the scenarios should be
   *          changed before each generation
   * @param scenarioCount
   *          the number of scenarios
   */
  public VirtualMachineProvider(
      final VirtualMachineParameters<MT> parameters,
      final IVirtualMachineFactory<MT, PT> factory,
      final Class<? extends VirtualMachine<MT, PT>> clazz,
      final boolean randomizeScenarios, final int scenarioCount) {
    super(clazz, randomizeScenarios, scenarioCount);
    this.m_parameters = parameters;
    this.m_factory = factory;
  }

  /**
   * Create a new virtual machine provider
   * 
   * @param parameters
   *          the virtual machine parameters
   * @param factory
   *          the virtual machine factory
   * @param clazz
   *          the class of virtual machines to be created
   * @param randomizeScenarios
   *          <code>true</code> if and only if the scenarios should be
   *          changed before each generation
   * @param scenarioCount
   *          the number of scenarios
   */
  @SuppressWarnings("unchecked")
  public VirtualMachineProvider(
      final VirtualMachineParameters<MT> parameters,
      final IHostedVirtualMachineFactory<MT, PT, ?, ?> factory,
      final Class<? extends VirtualMachine<MT, PT>> clazz,
      final boolean randomizeScenarios, final int scenarioCount) {
    super(clazz, randomizeScenarios, scenarioCount);

    this.m_parameters = parameters;
    this.m_factory = new HostedVirtualMachineFactoryWrapper<MT, PT, Serializable>(
        ((Serializable) (this)), ((IHostedVirtualMachineFactory) factory));
  }

  /**
   * Create a new instance of this simulator.
   * 
   * @return The newly created simulator instance.
   */
  @Override
  public ISimulation<?> createSimulation() {
    if (this.m_factory != null)
      return this.m_factory.createVirtualMachine(this.m_parameters);
    return super.createSimulation();
  }

}
