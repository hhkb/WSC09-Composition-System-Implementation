/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-23
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.gcd.rbgp.ElectionRBGPTestSeries.java
 * Last modification: 2007-09-23
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

package test.org.dgpf.cs.rbgp;

import java.io.Serializable;

import org.sigoa.refimpl.utils.testSeries.ISuccessFilter;
import org.sigoa.refimpl.utils.testSeries.successFilters.FirstObjectiveZeroFilter;

/**
 * The test series for the election rbgp algorithm.
 * 
 * @author Thomas Weise
 */
public class CSRBGPResultTest {

  /** */
  @SuppressWarnings("unchecked")
  private static final Serializable S = CSRBGPTestSeries.EMBRYOGENY
      .hatch(new byte[] {-80, 0, 16, 4, 4, 64, 48, 105, -81, -100, -9, -41, 87, 61, -67, 79, 47, -36, -60, -115, -125, -10, -38, -121, 78, -30, -23, 111, -15, 89, 97, -91, -9, 100, 41, -77, -19, 84, -91, -20, -108, 122, -60, 58, -60, -9, 71, -81, -80, -1, 41, -45, -13, -111, 44, 68, 27, -111, 79, -36, 48, 104, -81, -39, -46, 126, 97, 29, -110, -123, 23, -73, 44, 70, 24, -111, -21, -10, -55, 110, -90, -116, -6, -5, -43, 15, -25, 31, -9, -75, 25, -8, -52, -16, -90, -9, 100, 40, -77, -9, 70, -113, -15, -49, 66, -76, 17, -7, -9, 71, -113, -80, 31, -115, 24, 67, -89});

  /**
   * the main program called at startup
   * 
   * @param args
   *          the command line arguments
   */
  public static final void main(String[] args) {
    ISuccessFilter<Serializable> f;
    CSRBGPTestSeries x;

    x = new CSRBGPTestSeries("e:\\temp\\q");//$NON-NLS-1$

    f = new FirstObjectiveZeroFilter(x.createIndividualEvaluator());
    x.abort();

    System.out.println(S);
    System.out.println();
    System.out.println(f.isOverfitted(S));
  }
}
