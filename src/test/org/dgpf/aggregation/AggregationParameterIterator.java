/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-23
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.aggregation.AggregationParameterIterator.java
 * Last modification: 2007-12-23
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

package test.org.dgpf.aggregation;

import org.sigoa.refimpl.utils.testSeries.ParameterIterator;

/**
 * The aggregation parameter iterator
 * 
 * @author Thomas Weise
 */
public class AggregationParameterIterator extends ParameterIterator {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the parameter titles
   */
  public static final String[] TITLES = new String[] {//
  "population size",//$NON-NLS-1$
      // "scenario count",//$NON-NLS-1$
      // "change scenarios",//$NON-NLS-1$
      "steady state",//$NON-NLS-1$
      "prevent convergence",//$NON-NLS-1$      
  };

  /**
   * the value matrix
   */
  private static final Object[][] MATRIX = new Object[][] {//
  new Integer[] { //     
      Integer.valueOf(2048),//
          Integer.valueOf(4096) },//
      // new Integer[] { Integer.valueOf(1), Integer.valueOf(10) },//
      // new Boolean[] { Boolean.TRUE, Boolean.FALSE },//
      new Boolean[] { Boolean.TRUE, Boolean.FALSE },//
      new Double[] { Double.valueOf(0.0d), Double.valueOf(0.3d) },//
  };

  /**
   * the shared instance of this parameter iterator
   */
  public static final ParameterIterator AGGREGATION_PARAMETER_ITERATOR = new AggregationParameterIterator();

  /**
   * Create the gcd parameter iterator
   */
  private AggregationParameterIterator() {
    super(MATRIX);
  }

}
