/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-05-10
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.math.Physics.java
 * Last modification: 2008-05-10
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

package org.sfc.math;

import org.sfc.utils.ErrorUtils;

/**
 * The physical methods
 * 
 * @author Thomas Weise
 */
public class Physics extends ConstantSet {

  /**
   * speed of light
   */
  public static final double SPEED_OF_LIGHT = 299792458d;

  /**
   * newton gravitation
   */
  public static final double NEWTON_GRAVITATION = 6.6742867e-11d;

  /**
   * planck
   */
  public static final double PLANCK = 6.6260689633e-34d;

  /**
   * elementary charge
   */
  public static final double ELEMENTARY_CHARGE = 1.60217648740e-19d;
  
  /**
   * electron mass
   */
  public static final double ELECTRON_MASS =  9.1093821545e-31d;
  
  /**
   * proton mass
   */
  public static final double PROTON_MASS =  1.67262163783e-27d;
  
  /**
   * avogadro
   */
  public static final double AVOGADRO =  6.022141510e23d;
  
  /**
   * gas constant
   */
  public static final double GAS_CONSTANT =  8.31447215d;

  /**
   * Obtain a list of all constants, including pi and e
   * 
   * @return the array of all mathematical constants
   */
  public static final double[] listConstants() {
    return listConstants(double.class, null);
  }

  /**
   * the forbidden constructor
   */
  private Physics() {
    ErrorUtils.doNotCall();
  }
}
