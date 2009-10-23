/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-21
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.vm.objectives.StaticProgramObjective.java
 * Last modification: 2007-09-21
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

package org.dgpf.vm.objectives;

import java.io.Serializable;

import org.dgpf.vm.base.VirtualMachineProgram;
import org.sigoa.refimpl.go.objectives.ObjectiveState;
import org.sigoa.refimpl.go.objectives.StaticObjectiveFunction;
import org.sigoa.refimpl.go.objectives.StaticObjectiveState;
import org.sigoa.spec.simulation.ISimulation;

/**
 * The base class for static program objective functions.
 * 
 * @param <MT>
 *          the virtual machine memory type
 * @param <PT>
 *          the program type
 * @author Thomas Weise
 */
public class StaticProgramObjective<MT extends Serializable, PT extends VirtualMachineProgram<MT>>
    extends
    StaticObjectiveFunction<PT, ObjectiveState, StaticObjectiveState, ISimulation<PT>> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new static program objective function
   */
  protected StaticProgramObjective() {
    super();
  }

  /**
   * The program sanity check
   * 
   * @param individual
   *          The individual to be checked.
   * @param staticState
   *          the static state record
   * @return <code>true</code> if and only if the individual is worth
   *         being examined. <code>false</code> if it can be thrown away.
   * @throws NullPointerException
   *           if <code>individual==null</code>
   */
  @Override
  public boolean sanityCheck(final PT individual,
      final StaticObjectiveState staticState) {
    return ((individual != null) && (!(individual.isUseless())));
  }

}
