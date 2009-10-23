/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-05-15
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.go.reproduction.ISteadyStateReproduction.java
 * Last modification: 2007-05-15
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

package org.sigoa.spec.go.reproduction;

import java.io.Serializable;

/**
 * This interface is common to all algorithms that can be steady state.
 *
 * @author Thomas Weise
 */
public interface ISteadyStateReproduction extends Serializable {

  /**
   * Returns whether this algorithm is steady state or not.
   *
   * @return <code>true</code> if and only if this algorithm is steady
   *         state, <code>false</code> otherwise
   */
  public abstract boolean isSteadyState();

  /**
   * Set this algorithm to be steady state.
   *
   * @param steadyState
   *          The new steady state property: <code>true</code> if and
   *          only if this algorithm should become steady state,
   *          <code>false</code> if it should not be steady state anymore
   */
  public abstract void setSteadyState(final boolean steadyState);
}
