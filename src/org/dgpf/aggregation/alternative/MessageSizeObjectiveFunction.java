/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-27
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.aggregation.simulation.MessageSizeObjectiveFunction.java
 * Last modification: 2007-03-27
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

import java.io.Serializable;

import org.dgpf.aggregation.net.AggregationNetProgram;
import org.sigoa.refimpl.go.objectives.ObjectiveFunction;
import org.sigoa.refimpl.go.objectives.ObjectiveState;
import org.sigoa.spec.go.objectives.IObjectiveFunction;
import org.sigoa.spec.simulation.ISimulation;

/**
 * This fitness function puts pressure into the direction of smaller
 * programs.
 * 
 * @author Thomas Weise
 */
public class MessageSizeObjectiveFunction
    extends
    ObjectiveFunction<AggregationNetProgram, ObjectiveState, Serializable, ISimulation<AggregationNetProgram>> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * The globally shared instance of the message size objective function
   */
  public static final IObjectiveFunction<AggregationNetProgram, ObjectiveState, Serializable, ISimulation<AggregationNetProgram>> MSG_SIZE_OBJECTIVE_FUNCTION = new MessageSizeObjectiveFunction();

  /**
   * Create a new size fitness function.
   */
  protected MessageSizeObjectiveFunction() {
    super();
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
   * @param staticState
   *          the static state record
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
  public void endEvaluation(final AggregationNetProgram individual,
      final ObjectiveState state, final Serializable staticState,
      final ISimulation<AggregationNetProgram> simulation) {
    state.setObjectiveValue(individual.m_in.length);
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
    sb.append("msgSize"); //$NON-NLS-1$
  }

  /**
   * read resolve
   * 
   * @return the resolution result
   */
  private final Object readResolve() {
    return MSG_SIZE_OBJECTIVE_FUNCTION;
  }

  /**
   * write replace
   * 
   * @return the resolution result
   */
  private final Object writeReplace() {
    return MSG_SIZE_OBJECTIVE_FUNCTION;
  }
}
