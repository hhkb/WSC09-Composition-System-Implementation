/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-21
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.vm.objectives.MinimizeWeightObjective.java
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
import org.sigoa.refimpl.go.objectives.StaticObjectiveState;
import org.sigoa.spec.go.OptimizationUtils;
import org.sigoa.spec.go.objectives.IObjectiveFunction;
import org.sigoa.spec.simulation.ISimulation;

/**
 * This objective function puts pressure into the direction of programs of
 * minimal weight.
 * 
 * @author Thomas Weise
 */
public class MinimizeWeightObjective
    extends
    StaticProgramObjective<Serializable, VirtualMachineProgram<Serializable>> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The globally shared instance of this objective function
   */
  public static final IObjectiveFunction<VirtualMachineProgram<Serializable>, ObjectiveState, StaticObjectiveState, //
  ISimulation<VirtualMachineProgram<Serializable>>> MINIMIZE_WEIGHT_OBJECTIVE = new MinimizeWeightObjective();

  /**
   * the minimum weight
   */
  private final int m_minWeight;

  /**
   * the maximum weight
   */
  private final int m_maxWeight;

  /**
   * Create the objective function
   */
  protected MinimizeWeightObjective() {
    this(-1, -1);
  }

  /**
   * Create a new minimize weight objective.
   * 
   * @param minWeight
   *          the minimum weight
   * @param maxWeight
   *          the maximum weight
   */
  public MinimizeWeightObjective(final int minWeight, final int maxWeight) {
    super();
    this.m_minWeight = ((minWeight > 1) ? minWeight : 1);
    this.m_maxWeight = ((maxWeight > this.m_minWeight) ? maxWeight
        : Integer.MAX_VALUE);
  }

  /**
   * Obtain the minimum weight
   * 
   * @return the minimum weight
   */
  public int getMinWeight() {
    return this.m_minWeight;
  }

  /**
   * Obtain the maximum weight
   * 
   * @return the maximum weight
   */
  public int getMaxWeight() {
    return this.m_maxWeight;
  }

  /**
   * read resolve
   * 
   * @return the read resolution
   */
  private final Object readResolve() {
    if ((this.getClass() == MinimizeWeightObjective.class)
        && (this.m_maxWeight <= 1)
        && (this.m_maxWeight >= Integer.MAX_VALUE))
      return MINIMIZE_WEIGHT_OBJECTIVE;
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
   * This method is called only once per individual and is used to compute
   * its objective value.
   * 
   * @param individual
   *          The individual to be checked.
   * @param staticData
   *          the static data record
   * @return the objective value
   */
  @Override
  protected double computeValue(
      final VirtualMachineProgram<Serializable> individual,
      final StaticObjectiveState staticData) {
    int w;

    if (individual != null) {
      w = individual.getWeight();
      if ((w <= 0) || (w > this.m_maxWeight))
        return OptimizationUtils.WORST;

      return Math.max(w, this.m_minWeight);
    }
    return OptimizationUtils.WORST;
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
    sb.append("weight"); //$NON-NLS-1$
  }

}
