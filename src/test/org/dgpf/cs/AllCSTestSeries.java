/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : TODO
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.cs.AllCSTestSeries.java
 * Last modification: TODO
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

package test.org.dgpf.cs;

import org.sigoa.refimpl.utils.testSeries.TestSeries;
import org.sigoa.refimpl.utils.testSeries.multi.ITestSeriesFactory;
import org.sigoa.refimpl.utils.testSeries.multi.MultiTestSeries;

import test.org.dgpf.cs.eRBGP.CSeRBGPTestSeries;
import test.org.dgpf.cs.fraglets.CSFragletTestSeries;
import test.org.dgpf.cs.rbgp.CSRBGPTestSeries;

/**
 * all the cs test series
 * 
 * @author Thomas Weise
 */
public class AllCSTestSeries {

  /**
   * the main program called at startup
   * 
   * @param args
   *          the command line arguments
   */
  public static void main(String[] args) {
    MultiTestSeries mts;

    mts = new MultiTestSeries(//
        new ITestSeriesFactory[] {//

        new ITestSeriesFactory() {//
              public TestSeries create(final Object dir) {//
                return new CSFragletTestSeries(dir);
              }//

              public String getName() {//
                return "fraglets";//$NON-NLS-1$
              }
            },//

            new ITestSeriesFactory() {//
              public TestSeries create(final Object dir) {//
                return new CSRBGPTestSeries(dir);
              }//

              public String getName() {//
                return "rbgp";//$NON-NLS-1$
              }
            },//

            new ITestSeriesFactory() {//
              public TestSeries create(final Object dir) {//
                return new CSeRBGPTestSeries(dir);
              }//

              public String getName() {//
                return "eRBGP";//$NON-NLS-1$
              }
            },//

        },// 
        "" //$NON-NLS-1$
    );

    mts.start();
  }
}
