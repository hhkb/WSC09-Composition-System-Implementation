/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-05-06
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.benchmark.adrulyte.EAParameterIterator.java
 * Last modification: 2008-05-06
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

package test.org.dgpf.benchmark.adrulyte;

import org.sigoa.refimpl.utils.testSeries.ParameterIterator;

/**
 * the parameter iterator
 * 
 * @author Thomas Weise
 */
public class EAParameterIterator extends ParameterIterator {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the parameter titles
   */
  public static final String[] TITLES = new String[] {//
  "fitness assignment",//$NON-NLS-1$
      "selection algorithm",//$NON-NLS-1$
      "steady state",//$NON-NLS-1$
      "convergence prevention",//$NON-NLS-1$
      "problem", //$NON-NLS-1$
  };

  /**
   * the value matrix
   */
  private static final Object[][] MATRIX = new Object[][] {//
  // fitness assignment
      new Integer[] { //
      Integer.valueOf(0),//
          Integer.valueOf(1),//
          Integer.valueOf(2),//
      },//
      //
      // selection algorithm
      new Integer[] { //
      Integer.valueOf(0),//
          Integer.valueOf(1),//
          Integer.valueOf(2),//
          Integer.valueOf(3),//
      },//
      //
      // steady state
      new Boolean[] { Boolean.TRUE, Boolean.FALSE },//
      //
      // convergence prevention
      new Double[] { Double.valueOf(0.0d), Double.valueOf(0.3d) },//
      //
      // problem
      new Double[] { Double.valueOf(0.0d),//
          Double.valueOf(1.0d),//
          Double.valueOf(2.0d),//
          Double.valueOf(3.0d),//
          Double.valueOf(4.0d),//
          Double.valueOf(5.0d),//
          Double.valueOf(6.0d),//
          Double.valueOf(7.0d),//
          Double.valueOf(8.0d),//
          Double.valueOf(9.0d),//
          Double.valueOf(10.0d),//
      },//
  };

  /**
   * the shared instance of this parameter iterator
   */
  public static final ParameterIterator ITERATOR = new EAParameterIterator();

  /**
   * Create the gcd parameter iterator
   */
  private EAParameterIterator() {
    super(MATRIX);
  }
}
