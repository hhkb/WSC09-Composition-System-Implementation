/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-16
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.election.fraglets.FragletElectionProvider.java
 * Last modification: 2007-12-16
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
 
package test.org.dgpf.election.fraglets;

import org.dgpf.fraglets.base.FragletStore;
import org.dgpf.fraglets.net.FragletNetParameters;
import org.dgpf.vm.net.NetworkProvider;
import org.sigoa.spec.simulation.ISimulation;

/**
 * The fraglet provider.
 *
 * @author Thomas Weise
 */
public class FragletElectionProvider extends NetworkProvider<FragletStore> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

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
  public FragletElectionProvider(final FragletNetParameters parameters,
      final boolean randomizeScenarios, final int scenarioCount) {
    super(FragletElectionNetwork.class, parameters, randomizeScenarios,
        scenarioCount);
  }

  /**
   * Create a new instance of this simulator.
   * 
   * @return The newly created simulator instance.
   */
  @Override
  public ISimulation<?> createSimulation() {
    return new FragletElectionNetwork(this);
  }
}