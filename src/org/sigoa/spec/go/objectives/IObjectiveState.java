/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-27
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.go.objectives.IObjectiveState.java
 * Last modification: 2006-11-27
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
package org.sigoa.spec.go.objectives;

import java.io.Serializable;

/**
 * This interface is common to all state objects used by objective
 * functions. A state object is only used for one single evaluation of an
 * individual.
 *
 * @author Thomas Weise
 * @version 1.0.0
 */
public interface IObjectiveState extends Serializable {
  /**
   * Obtain the objective value of this objective state object.
   *
   * @return The objective value stored inside this object.
   */
  public abstract double getObjectiveValue();

  /**
   * This method is called whenever one evaluation has been completed fully
   * and the data stored in this state is no longer required.
   */
  public abstract void clear();

}
