/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-19
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.gcd.AllGCDSeries.java
 * Last modification: 2007-11-19
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

import org.sigoa.refimpl.utils.testSeries.multi.ITestSeriesFactory;
import org.sigoa.refimpl.utils.testSeries.multi.MultiTestSeries;

import test.org.dgpf.election.aggregation.ElectionAggregationTestSeries;
import test.org.dgpf.election.eRBGP.ElectionERBGPTestSeries;
import test.org.dgpf.election.fraglets.ElectionFragletTestSeries;
import test.org.dgpf.election.hlgp.ElectionHLGPTestSeries;
import test.org.dgpf.election.lgp.ElectionLGPTestSeries;
import test.org.dgpf.election.rbgp.ElectionRBGPTestSeries;

/**
 * This program performs all gcd test series.
 * 
 * @author Thomas Weise
 */
public class AllElectionSeries {

  // /**
  // * the base dir
  // */
  // public static final CanonicalFile BASE_DIR = IO.getFile("election");
  // //$NON-NLS-1$

  /**
   * the rbgp election series factory
   */
  public static final ITestSeriesFactory RBGP = new ITestSeriesFactory() {

    public ElectionTestSeries create(final Object dir) {
      return new ElectionRBGPTestSeries(dir);
    }

    /**
     * obtain the name of the series
     * 
     * @return the name of the series
     */
    public String getName() {
      return "rbgp"; //$NON-NLS-1$
    }
  };

  /**
   * the lgp election series factory
   */
  public static final ITestSeriesFactory LGP = new ITestSeriesFactory() {

    public ElectionTestSeries create(final Object dir) {
      return new ElectionLGPTestSeries(dir);
    }

    /**
     * obtain the name of the series
     * 
     * @return the name of the series
     */
    public String getName() {
      return "lgp"; //$NON-NLS-1$
    }
  };

  /**
   * the hlgp election series factory
   */
  public static final ITestSeriesFactory HLGP = new ITestSeriesFactory() {

    public ElectionTestSeries create(final Object dir) {
      return new ElectionHLGPTestSeries(dir);
    }

    /**
     * obtain the name of the series
     * 
     * @return the name of the series
     */
    public String getName() {
      return "hlgp"; //$NON-NLS-1$
    }
  };

  /**
   * the fraglets election series factory
   */
  public static final ITestSeriesFactory FRAGLETS = new ITestSeriesFactory() {

    public ElectionTestSeries create(final Object dir) {
      return new ElectionFragletTestSeries(dir);
    }

    /**
     * obtain the name of the series
     * 
     * @return the name of the series
     */
    public String getName() {
      return "fraglets"; //$NON-NLS-1$
    }
  };

  /**
   * the fraglets election series factory
   */
  public static final ITestSeriesFactory AGGREGATION = new ITestSeriesFactory() {

    public ElectionTestSeries create(final Object dir) {
      return new ElectionAggregationTestSeries(dir);
    }

    /**
     * obtain the name of the series
     * 
     * @return the name of the series
     */
    public String getName() {
      return "aggregation"; //$NON-NLS-1$
    }
  };

  /**
   * the fraglets election series factory
   */
  public static final ITestSeriesFactory ERBGP = new ITestSeriesFactory() {

    public ElectionTestSeries create(final Object dir) {
      return new ElectionERBGPTestSeries(dir);
    }

    /**
     * obtain the name of the series
     * 
     * @return the name of the series
     */
    public String getName() {
      return "eRBGP"; //$NON-NLS-1$
    }
  };

  /**
   * the main program called at startup
   * 
   * @param args
   *          the command line arguments
   */
  public static void main(String[] args) {
    ITestSeriesFactory[] f;
    MultiTestSeries mts;

    f = new ITestSeriesFactory[] { RBGP, LGP, /* HLGP, */FRAGLETS, // AGGREGATION,
        ERBGP };
    mts = new MultiTestSeries(f, ""); //$NON-NLS-1$

    mts.start();
  }

}
