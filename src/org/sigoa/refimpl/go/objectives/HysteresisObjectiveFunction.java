/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-22
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.objectives.HysteresisObjectiveFunction.java
 * Last modification: 2007-09-22
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

package org.sigoa.refimpl.go.objectives;

import java.io.Serializable;

import org.sigoa.spec.go.IHysteresis;
import org.sigoa.spec.go.objectives.IObjectiveState;
import org.sigoa.spec.go.objectives.IObjectiveValueComputer;
import org.sigoa.spec.simulation.ISimulation;

/**
 * The default implementation of the <code>IObjectiveFunction</code>-interface.
 * 
 * @param <PP>
 *          The phenotype of the individuals to evaluate.
 * @param <D>
 *          The type of state object used by this objective function.
 * @param <SI>
 *          The simulator type involved in the individual evaluation.
 * @param <SS>
 *          the type of static state used, if any needed
 * @author Thomas Weise
 * @version 1.0.0
 */
public class HysteresisObjectiveFunction<PP extends Serializable, D extends IObjectiveState, SS extends Serializable, SI extends ISimulation<PP>>
    extends ObjectiveFunction<PP, D, SS, SI> implements IHysteresis {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * <code>true</code> if hysteresis is used, <code>false</code>
   * otherwise
   */
  private volatile boolean m_hysteresis;

  /**
   * Create a new objective function.
   */
  protected HysteresisObjectiveFunction() {
    this(null);
  }

  /**
   * Create a new objective function.
   * 
   * @param ovc
   *          The objective value computer to be used. If this is
   *          <code>null</code>, the <code>ObjectiveUtils.AVG_OVC</code>
   *          is used as default.
   */
  protected HysteresisObjectiveFunction(final IObjectiveValueComputer ovc) {
    this(ovc, true);
  }

  /**
   * Create a new objective function.
   * 
   * @param ovc
   *          The objective value computer to be used. If this is
   *          <code>null</code>, the <code>ObjectiveUtils.AVG_OVC</code>
   *          is used as default.
   * @param usesHysteresis
   *          <code>true</code> if and only if hysteresis should be used,
   *          <code>false</code> otherwise
   */
  protected HysteresisObjectiveFunction(final IObjectiveValueComputer ovc,
      final boolean usesHysteresis) {
    super(ovc);
    this.m_hysteresis = usesHysteresis;
  }

  /**
   * Set whether hystersis should be used or not.
   * 
   * @param usesHysteresis
   *          <code>true</code> if and only if hysteresis should be used,
   *          <code>false</code> otherwise
   */
  public void setUsesHysteresis(final boolean usesHysteresis) {
    this.m_hysteresis = usesHysteresis;
  }

  /**
   * Does this component apply hysteresis?
   * 
   * @return <code>true</code> if hysteresis is/cab be applied by this
   *         component, <code>false</code> otherwise
   */
  public boolean usesHysteresis() {
    return this.m_hysteresis;
  }
}
