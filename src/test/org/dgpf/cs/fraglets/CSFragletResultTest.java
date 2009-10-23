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

package test.org.dgpf.cs.fraglets;

import java.io.Serializable;

import org.sigoa.refimpl.utils.testSeries.ISuccessFilter;
import org.sigoa.refimpl.utils.testSeries.successFilters.FirstObjectiveZeroFilter;

/**
 * The test series for the election rbgp algorithm.
 * 
 * @author Thomas Weise
 */
public class CSFragletResultTest {

  /** */
  @SuppressWarnings("unchecked")
  private static final Serializable S = CSFragletTestSeries.EMBRYOGENY
      .hatch(new int[][] {{-269676243, -192305279, 448659320, -1817082198}, {-2021791776, 1736848834, 412463605}, {-605122653}, {-1443343605, -793383370, -1794867427, -1961066294, -45931676}, {1322028038, -22736824, 1547350333, -664140496, -1683680367, 1541288220, -1170392022, -431298216, 1213464785, 113829190, 1414919039, 2118792111}});
  
//  /** */
//  @SuppressWarnings("unchecked")
//  private static final Serializable S2 = CSFragletTestSeries.EMBRYOGENY
//      .hatch(new int[][] {
//          { 801199428, -434448262, 285872125 },
//          { 1532389515, -1526161955, -609776468, 804877806, 244158492,
//              -957354312, -808985119, -451529297, -666952485, 867840929,
//              1006724085, -1462071593, -2064010757, -1768022317 },
//          { 1560852144, -756260454 },
//          { 1557439741, 2124585948, -380033365, 358860207 },
//          { 1409807029, -315152101, 1334580351, -1078015365, 1930383306,
//              1517339889, -1596650, -878442789, -981178662 } });

  /**
   * the main program called at startup
   * 
   * @param args
   *          the command line arguments
   */
  public static final void main(String[] args) {
    ISuccessFilter<Serializable> f;
    CSFragletTestSeries x;

    x = new CSFragletTestSeries("e:\\temp\\q");//$NON-NLS-1$

    f = new FirstObjectiveZeroFilter(x.createIndividualEvaluator());
    x.abort();
//    
//    System.out.println(S2);
//    System.out.println();
//    System.out.println(f.isOverfitted(S2));

    System.out.println(S);
    System.out.println();
    System.out.println(f.isOverfitted(S));
  }
}
