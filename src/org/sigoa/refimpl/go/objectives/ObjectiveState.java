/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-27
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.objectives.ObjectiveState.java
 * Last modification: 2008-05-19
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

import org.sigoa.refimpl.go.ImplementationBase;
import org.sigoa.spec.go.OptimizationUtils;
import org.sigoa.spec.go.objectives.IObjectiveState;

/**
 * The default <code>IObjectiveState</code>-implementation.
 * 
 * @author Thomas Weise
 */
public class ObjectiveState extends
    ImplementationBase<Serializable, Serializable> implements
    IObjectiveState {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The objective value.
   */
  private double m_value;

  /**
   * Create a new objective state instance.
   */
  public ObjectiveState() {
    super();
    this.m_value = OptimizationUtils.WORST;
  }

  /**
   * Set the objective value of this objective state object.
   * 
   * @param value
   *          The objective value to be stored inside this object.
   */
  public void setObjectiveValue(final double value) {
    this.m_value = OptimizationUtils.formatObjectiveValue(value);
  }

  /**
   * Obtain the objective value of this objective state object.
   * 
   * @return The objective value stored inside this object.
   */
  public double getObjectiveValue() {
    return this.m_value;
  }

  /**
   * This method is called whenever one evaluation has been completed fully
   * and the data stored in this state is no longer required.
   */
  public void clear() {
    this.m_value = OptimizationUtils.WORST;
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
    sb.append(this.m_value);
  }
}
