/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-04-11
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.objectives.wrapper.ObjectiveFunctionWrapper.java
 * Last modification: 2008-04-11
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

package org.sigoa.refimpl.go.objectives.wrapper;

import java.io.Serializable;

import org.sigoa.refimpl.go.objectives.ObjectiveFunction;
import org.sigoa.spec.go.objectives.IObjectiveFunction;
import org.sigoa.spec.go.objectives.IObjectiveState;
import org.sigoa.spec.simulation.ISimulation;

/**
 * A wrapper for objective functions
 * 
 * @author Thomas Weise
 */
public class ObjectiveFunctionWrapper
    extends
    ObjectiveFunction<Serializable, IObjectiveState, Serializable, ISimulation<Serializable>> {

  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the objective function to relay to
   */
  private final IObjectiveFunction<Serializable, IObjectiveState, Serializable, ISimulation<Serializable>> m_func;

  /**
   * the default value
   */
  double m_default;

  /**
   * whether or not the objective should be delegated to
   */
  boolean m_use;

  /**
   * Create a new objective function wrapper
   * 
   * @param objective
   *          the objective function
   */
  @SuppressWarnings("unchecked")
  public ObjectiveFunctionWrapper(
      final IObjectiveFunction<? extends Serializable, ? extends IObjectiveState, ? extends Serializable, ? extends ISimulation<?>> objective) {
    super();
    this.m_func = ((IObjectiveFunction<Serializable, IObjectiveState, Serializable, ISimulation<Serializable>>) objective);
  }

  /**
   * <p>
   * This method is called before any simulation or optimization will be
   * performed. It is used to check whether an individual is worth being
   * processed further. Normally, this method will return <code>true</code>
   * or performs only a very fast and simple check.
   * </p>
   * <p>
   * This method is only called once per individual.
   * </p>
   * <p>
   * If this method returns
   * <code>false/<code>, all objective values of the individual will be set
   * to positive infinity and also no other objective function is invoked
   * on the individual
   * </p>
   *
   * @param individual
   *          The individual to be checked.
   *  @param staticState the static state record
   * @return <code>true</code> if and only if the individual is worth
   *         being examined. <code>false</code> if it can be thrown away.
   *
   * @throws NullPointerException if <code>individual==null</code>
   */
  @Override
  public boolean sanityCheck(final Serializable individual,
      final Serializable staticState) {
    if (this.m_use)
      return this.m_func.sanityCheck(individual, staticState);
    return super.sanityCheck(individual, staticState);
  }

  /**
   * Create a new static state record.
   * 
   * @return the new static state record
   */
  @Override
  public Serializable createStaticState() {
    return this.m_func.createStaticState();
  }

  /**
   * Destroy a static state record no longer needed.
   * 
   * @param staticState
   *          the static state record no longer needed
   */
  @Override
  public void destroyStaticState(final Serializable staticState) {
    this.m_func.destroyStaticState(staticState);
  }

  /**
   * Create a new state object for this objective function.
   * 
   * @param staticState
   *          the static state record
   * @return The new state container.
   */
  @Override
  public IObjectiveState createState(final Serializable staticState) {
    return new InternalState(this.m_func.createState(staticState));
  }

  /**
   * Destroy a state object no longer needed.
   * 
   * @param state
   *          The state container to be destroyed.
   * @param staticState
   *          the static state record
   * @throws NullPointerException
   *           if <code>state==null</code>.
   */
  @Override
  public void destroyState(final IObjectiveState state,
      final Serializable staticState) {
    this.m_func.destroyState(((InternalState) state).m_state, staticState);
  }

  /**
   * After all objective functions have decided that the individual is
   * sane, this function is called for of them.
   * 
   * @param individual
   *          The individual to be evaluated.
   * @param staticState
   *          the static state record
   * @throws NullPointerException
   *           if <code>individual==null</code>
   */
  @Override
  public void beginIndividual(final Serializable individual,
      final Serializable staticState) {
    if (this.m_use)
      this.m_func.beginIndividual(individual, staticState);
  }

  /**
   * This function is called at the end of an individual's inspection
   * before {@link #computeObjectiveValue(double[], Serializable)}.
   * 
   * @param individual
   *          The individual to be evaluated.
   * @param staticState
   *          the static state record
   * @throws NullPointerException
   *           if <code>individual==null</code>
   */
  @Override
  public void endIndividual(final Serializable individual,
      final Serializable staticState) {
    if (this.m_use)
      this.m_func.endIndividual(individual, staticState);
  }

  /**
   * Obtain the count of simulation steps that will be needed in order to
   * evaluate the specified individual.
   * 
   * @param individual
   *          The individual to be evaluated.
   * @param state
   *          The state container.
   * @param staticState
   *          the static state record
   * @return The count of simulation steps needed. Values 0 indidicate that
   *         no simulation needs to be performed for this objective
   *         function.
   * @throws NullPointerException
   *           if <code>individual==null</code> or
   *           <code>state==null</code>
   */
  @Override
  public long getRequiredSimulationSteps(final Serializable individual,
      final IObjectiveState state, final Serializable staticState) {
    if (this.m_use)
      this.m_func.getRequiredSimulationSteps(individual, state,
          staticState);
    return 0l;
  }

  /**
   * This method is called before any evaluation/simulation is performed.
   * If this objective function requires a simulation and needs to inspect
   * the simulation's state, the return value is the simulation step where
   * the first inspection should be scheduled. If this is not the case or
   * the return value is = 0 or exceeds the total count of simulation
   * steps, no inspection will occure.
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
   * @return The time index of the first inspection of the simulation.
   * @throws NullPointerException
   *           if <code>individual==null</code> or
   *           <code>state==null</code> or if
   *           <code>simulator==null</code> but a simulation is required.
   */
  @Override
  public long beginEvaluation(final Serializable individual,
      final IObjectiveState state, final Serializable staticState,
      final ISimulation<Serializable> simulation) {
    if (this.m_use)
      return this.m_func.beginEvaluation(individual,
          ((InternalState) state).m_state, staticState, simulation);
    return 0l;
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
   * @param simulation
   *          The simulation (<code>null</code> if no simulation is
   *          required as indivicated by
   *          <code>getRequiredSimulationSteps</code>).
   * @param staticState
   *          the static state record
   * @throws NullPointerException
   *           if <code>individual==null</code> or
   *           <code>state==null</code>
   */
  @Override
  public void endEvaluation(final Serializable individual,
      final IObjectiveState state, final Serializable staticState,
      final ISimulation<Serializable> simulation) {
    if (this.m_use)
      this.m_func.endEvaluation(individual,
          ((InternalState) state).m_state, staticState, simulation);
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
    return this.m_func.getRequiredSimulationId();
  }

  /**
   * This method is called by the evaluator in order to determine the final
   * objective value for this objective. The evaluator may perform multiple
   * simulations, where the objective function stores an objective value
   * into its state record for each single one. These values are now put
   * into an array of double, so the objective function may decide on a
   * final value based on these results of single simulations. The
   * <code>results</code>-array is sorted in ascending order.
   * 
   * @param results
   *          The results of the single simulations. (Sorted in ascending
   *          order)
   * @param staticState
   *          the static state record
   * @return A final objective value based on the results passed in.
   */
  @Override
  public double computeObjectiveValue(final double[] results,
      final Serializable staticState) {
    if (this.m_use)
      return this.m_func.computeObjectiveValue(results, staticState);
    return this.m_default;
  }

  /**
   * the internal state class
   * 
   * @author Thomas Weise
   */
  private final class InternalState implements IObjectiveState,
      Serializable {
    /**
     * The serial version uid.
     */
    private static final long serialVersionUID = 1;

    /**
     * the state
     */
    final IObjectiveState m_state;

    /**
     * Create a new internal state
     * 
     * @param s
     *          the source state
     */
    InternalState(final IObjectiveState s) {
      super();
      this.m_state = s;
    }

    /**
     * Obtain the objective value of this objective state object.
     * 
     * @return The objective value stored inside this object.
     */
    public double getObjectiveValue() {
      return ObjectiveFunctionWrapper.this.m_use ? this.m_state
          .getObjectiveValue() : ObjectiveFunctionWrapper.this.m_default;
    }

    /**
     * This method is called whenever one evaluation has been completed
     * fully and the data stored in this state is no longer required.
     */
    public void clear() {
      if (ObjectiveFunctionWrapper.this.m_use)
        this.m_state.clear();
    }
  }
}