/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-12
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.go.IHysteresis.java
 * Last modification: 2007-09-12
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

package org.sigoa.spec.go;

import java.io.Serializable;

/**
 * A component that implements this interface may allow hystersis to
 * happen. Hysteresis means that the objective or fitness values of the
 * parents of an individual may have influence on its objective or fitness
 * values.
 * 
 * @author Thomas Weise
 */
public interface IHysteresis extends Serializable {

  /**
   * Does this component apply hysteresis?
   * 
   * @return <code>true</code> if hysteresis is/cab be applied by this
   *         component, <code>false</code> otherwise
   */
  public abstract boolean usesHysteresis();
}
