/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-29
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.gcd.GCDParameterIterator.java
 * Last modification: 2007-09-29
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

package test.org.dgpf.gcd;

import org.sigoa.refimpl.utils.testSeries.ParameterIterator;

/**
 * The parameter iterator for the gcd problem
 * 
 * @author Thomas Weise
 */
public class GCDParameterIterator extends ParameterIterator {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the parameter titles
   */
  public static final String[] TITLES = new String[] {//
  "population size",//$NON-NLS-1$
      "scenario count",//$NON-NLS-1$
      "change scenarios",//$NON-NLS-1$
      "steady state",//$NON-NLS-1$
      "prevent convergence",//$NON-NLS-1$
      // "random walk",//$NON-NLS-1$
      "mutation rate",//$NON-NLS-1$
      "crossover rate",//$NON-NLS-1$
  };

  /**
   * the value matrix
   */
  private static final Object[][] MATRIX = new Object[][] {//
  new Integer[] {//

      // ps = 0
          Integer.valueOf(16),//
          Integer.valueOf(32),//
          Integer.valueOf(64),//
          Integer.valueOf(128),//
          Integer.valueOf(256),//
          Integer.valueOf(512),//
          Integer.valueOf(1024),//
          Integer.valueOf(2 * 1024),//
          Integer.valueOf(4 * 1024),//
      },

      // sc = 1
      new Integer[] { Integer.valueOf(1), Integer.valueOf(10) },//

      // ct==2
      new Boolean[] { Boolean.TRUE, Boolean.FALSE },//

      // ss=3
      new Boolean[] { Boolean.TRUE, Boolean.FALSE },//

      // ovp=4
      new Double[] { Double.valueOf(0d), Double.valueOf(0.3d),
          Double.valueOf(0.6d) },//

      // // rw==5
      // new Boolean[] { Boolean.TRUE, Boolean.FALSE },//

      // mr=6
      new Double[] { Double.valueOf(0.2d), Double.valueOf(0.8d) },//

      // cr=7
      new Double[] { Double.valueOf(0.2d), Double.valueOf(0.8d) },//
  };

  /**
   * the shared instance of this parameter iterator
   */
  public static final ParameterIterator GCD_PARAMETER_ITERATOR = new GCDParameterIterator();

  /**
   * Create the gcd parameter iterator
   */
  private GCDParameterIterator() {
    super(MATRIX);
  }

  // /**
  // * Check whether the given parameter configuration is correct
  // *
  // * @param config
  // * the configuration
  // * @return <code>true</code> if and only if the configuration is
  // * correct, <code>false</code> otherwise
  // */
  // @Override
  // protected boolean checkConfiguration(final Object[] config) {
  // if (config[5] == Boolean.TRUE) {
  // return ((config[4] == Boolean.FALSE) && (config[3] == Boolean.FALSE));
  // }
  //
  // return super.checkConfiguration(config);
  // }

}
