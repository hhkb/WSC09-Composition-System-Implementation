/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-21
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.vm.objectives.MinimizeSizeObjective.java
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
 * minimal size.
 * 
 * @author Thomas Weise
 */
public class MinimizeSizeObjective
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
  ISimulation<VirtualMachineProgram<Serializable>>> MINIMIZE_SIZE_OBJECTIVE = new MinimizeSizeObjective();

  /**
   * the minimum size
   */
  private final int m_minSize;

  /**
   * the maximum size
   */
  private final int m_maxSize;

  /**
   * Create the objective function
   */
  protected MinimizeSizeObjective() {
    this(-1, -1);
  }

  /**
   * Create a new minimize size objective.
   * 
   * @param minSize
   *          the minimum size
   * @param maxSize
   *          the maximum size
   */
  public MinimizeSizeObjective(final int minSize, final int maxSize) {
    super();
    this.m_minSize = ((minSize > 1) ? minSize : 1);
    this.m_maxSize = ((maxSize > this.m_minSize) ? maxSize
        : Integer.MAX_VALUE);
  }

  /**
   * Obtain the minimum size
   * 
   * @return the minimum size
   */
  public int getMinSize() {
    return this.m_minSize;
  }

  /**
   * Obtain the maximum size
   * 
   * @return the maximum size
   */
  public int getMaxSize() {
    return this.m_maxSize;
  }

  /**
   * read resolve
   * 
   * @return the read resolution
   */
  private final Object readResolve() {
    if ((this.getClass() == MinimizeSizeObjective.class)
        && (this.m_maxSize <= 1) && (this.m_maxSize >= Integer.MAX_VALUE))
      return MINIMIZE_SIZE_OBJECTIVE;
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
    int s;

    if (individual != null) {
      s = individual.getSize();

      if ((s <= 0) || (s > this.m_maxSize))
        return OptimizationUtils.WORST;

      return Math.max(s, this.m_minSize);
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
    sb.append("size"); //$NON-NLS-1$
  }

}
