/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-06
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.go.reproduction.IHysteresisReproduction.java
 * Last modification: 2007-09-06
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

import org.sigoa.spec.go.IHysteresis;

/**
 * <p>
 * This interface is common to reproduction facilities that support
 * hysteresis.
 * </p>
 * <p>
 * Normally, a reproduction facility will initialize the fitness and
 * objective values of a new individual with
 * {@link org.sigoa.spec.go.OptimizationUtils#WORST}. A facility that
 * supports hysteresis instead uses the evaluation results of the parents.
 * </p>
 * 
 * @author Thomas Weise
 */
public interface IHysteresisReproduction extends IHysteresis {

  /**
   * Define whether this reproduction facility should apply hysteresis or
   * not.
   * 
   * @param hysteresis
   *          <code>true</code> if hysteresis should be applied,
   *          <code>false</code> otherwise
   */
  public abstract void setHysteresisUsage(final boolean hysteresis);

}
