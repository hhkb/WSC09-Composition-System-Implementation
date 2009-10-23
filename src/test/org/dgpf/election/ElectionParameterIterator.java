/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-29
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.election.ElectionParameterIterator.java
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

package test.org.dgpf.election;

import org.sigoa.refimpl.utils.testSeries.ParameterIterator;

/**
 * The parameter iterator for the election problem
 * 
 * @author Thomas Weise
 */
public class ElectionParameterIterator extends ParameterIterator {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the parameter titles
   */
  public static final String[] TITLES = new String[] {//
  "objective", //$NON-NLS-1$
  };

  /**
   * the value matrix
   */
  private static final Object[][] MATRIX = new Object[][] {//  
  new Boolean[] { Boolean.TRUE, Boolean.FALSE },//
  };

  /**
   * the shared instance of this parameter iterator
   */
  public static final ParameterIterator ELECTION_PARAMETER_ITERATOR = new ElectionParameterIterator();

  /**
   * Create the gcd parameter iterator
   */
  private ElectionParameterIterator() {
    super(MATRIX);
  }

}
