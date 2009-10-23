/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-01-03
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.objectives.StaticObjectiveState.java
 * Last modification: 2007-01-03
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

/**
 * A static state record which stores exactly one double.
 *
 * @author Thomas Weise
 */
public class StaticObjectiveState extends
    ImplementationBase<Serializable, Serializable> implements Serializable {
  /**
   * the serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The objective value.
   */
  double m_value;

  /**
   * the double static state.
   */
  public StaticObjectiveState() {
    super();
  }

  /**
   * Call the function to clear the stored value.
   */
  public void clear() {
    this.m_value = OptimizationUtils.WORST;
  }

  /**
   * Set the value of this state object.
   *
   * @param value
   *          The value to be stored inside this object.
   */
  public void setValue(final double value) {
    this.m_value = value;
  }

  /**
   * Obtain the value of this state object.
   *
   * @return The value stored inside this object.
   */
  public double getValue() {
    return this.m_value;
  }

}
