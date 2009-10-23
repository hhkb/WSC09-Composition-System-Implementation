/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-02-01
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.cnMotor.MotorProvider.java
 * Last modification: 2008-02-01
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

package test.org.sigoa.cnMotor;

import org.sigoa.refimpl.simulation.SimulationProvider;
import org.sigoa.spec.simulation.ISimulation;

/**
 * The motor provider
 * 
 * @author Thomas Weise
 */
public class MotorProvider extends SimulationProvider {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new motor provider
   */
  public MotorProvider() {
    super(MotorAccessor.class);
  }

  /**
   * Create a new instance of this simulator.
   * 
   * @return The newly created simulator instance.
   */
  @Override
  public ISimulation<?> createSimulation() {
    return new MotorAccessor();
  }
}
