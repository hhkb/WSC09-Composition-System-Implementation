/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-05-09
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.dmc.ProfitObjectiveFunction.java
 * Last modification: 2007-05-09
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

package test.org.sigoa.dmc.dmc07;

import java.io.Serializable;

import org.sigoa.refimpl.go.objectives.ObjectiveFunction;
import org.sigoa.refimpl.go.objectives.ObjectiveState;

/**
 * The cost-misclassification objective function.
 *
 * @author Thomas Weise
 */
public class ProfitObjectiveFunction
    extends
    ObjectiveFunction<ClassifierSystem, ObjectiveState, Serializable, ClassificationSimulation> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a cost-misclassification objective function.
   */
  public ProfitObjectiveFunction() {
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
  public void endEvaluation(final ClassifierSystem individual,
      final ObjectiveState state, final Serializable staticState,
      final ClassificationSimulation simulation) {

    state.setObjectiveValue(ClassificationObjectiveFunction.modify(
        -simulation.getProfit(), individual.getSize()));
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
    return ClassificationSimulation.class;
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
    sb.append("Costs"); //$NON-NLS-1$
  }

}
