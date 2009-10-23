/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-21
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.vm.net.objectives.MaximizeIdleTimeObjective.java
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

package org.dgpf.vm.net.objectives;

import java.io.Serializable;

import org.dgpf.vm.base.VirtualMachineProgram;
import org.sigoa.refimpl.go.objectives.ObjectiveFunction;
import org.sigoa.refimpl.go.objectives.ObjectiveState;
import org.sigoa.spec.go.objectives.IObjectiveFunction;
import org.sigoa.spec.simulation.ISimulation;

/**
 * This objective function puts pressure into the direction of very few
 * steps.
 * 
 * @author Thomas Weise
 */
public class MaximizeIdleTimeObjective
    extends
    ObjectiveFunction<VirtualMachineProgram<Serializable>, ObjectiveState, Serializable, //
    ISimulation<VirtualMachineProgram<Serializable>>> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The globally shared instance of this objective function
   */
  public static final IObjectiveFunction<VirtualMachineProgram<Serializable>, ObjectiveState, Serializable, //
  ISimulation<VirtualMachineProgram<Serializable>>> MAXIMIZE_IDLE_TIME_OBJECTIVE = new MaximizeIdleTimeObjective();

  /**
   * Create the objective function
   */
  protected MaximizeIdleTimeObjective() {
    super();
  }

  /**
   * read resolve
   * 
   * @return the read resolution
   */
  private final Object readResolve() {
    if (this.getClass() == MaximizeIdleTimeObjective.class)
      return MAXIMIZE_IDLE_TIME_OBJECTIVE;
    return this;
  }

  /**
   * write replace
   * 
   * @return the write replacement
   */
  private final Object writeReplace() {
    return readResolve();
  }

  /**
   * Append this object's textual representation to a string builder.
   * 
   * @param sb
   *          The string builder to append to.
   * @see #toString()
   */
  @Override
  public void toStringBuilder(final StringBuilder sb) {
    sb.append("idle"); //$NON-NLS-1$
  }

  /**
   * Obtain the id of the required simulator. If <code>null</code> is
   * returned, no simulation will be needed/performed for objective
   * function.
   * 
   * @return The id of the simulator required for the evaluation of this
   *         objective function.
   */
  @Override
  public Serializable getRequiredSimulationId() {
    return INetVirtualMachineSimulationInformation.class;
  }

  /**
   * This method is called after any simulation/evaluation is performed.
   * After this method returns, an objective value must have been stored in
   * the state record.
   * 
   * @param individual
   *          The individual that should be evaluated next.
   * @param state
   *          The state record.
   * @param staticData
   *          not used, <code>null</code>
   * @param simulation
   *          The simulation (<code>null</code> if no simulation is
   *          required as indivicated by
   *          <code>getRequiredSimulationSteps</code>).
   * @throws NullPointerException
   *           if <code>individual==null</code> or
   *           <code>state==null</code> or if
   *           <code>simulator==null</code> but a simulation is required.
   */
  @Override
  public void endEvaluation(
      final VirtualMachineProgram<Serializable> individual,
      final ObjectiveState state, final Serializable staticData,
      final ISimulation<VirtualMachineProgram<Serializable>> simulation) {

    INetVirtualMachineSimulationInformation in;

    in = ((INetVirtualMachineSimulationInformation) simulation);

    state.setObjectiveValue(in.getPerformedSteps() - in.getIdleSteps());
  }
}
