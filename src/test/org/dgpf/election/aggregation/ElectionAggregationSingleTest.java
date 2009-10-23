/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-22
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.election.aggregation.ElectionAggregationSingleTest.java
 * Last modification: 2007-12-22
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

package test.org.dgpf.election.aggregation;


/**
 * a single test for the aggregation election
 * 
 * @author Thomas Weise
 */
public class ElectionAggregationSingleTest extends
    ElectionAggregationTestSeries {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1L;

  /**
   * Create a new test series.
   * 
   * @param dir
   *          the directory to store the test data in
   */
  public ElectionAggregationSingleTest(final Object dir) {
    super(dir);
    this.setMaxRuns(Integer.MAX_VALUE);
    this.selectForLogging(BEST_INDIVIDUALS);
    this.selectForLogging(BEST_OBJECTIVES);
  }

  /**
   * This method is called for setting up the parameters
   * 
   * @param parameters
   *          the parameters
   */
  @Override
  protected void setupParameters(final Object[] parameters) {
    super.setupParameters(parameters);
    this.setPopulationSize(40);
    this.setConvergencePrevention(0.3d);
    this.setCrossoverRate(0.5d);
    this.setMutationRate(0.5d);
    this.setMaxGenerations(10);
    this.setMaxRuns(Integer.MAX_VALUE);
    this.setSteadyState(true);
    this.setTestCaseCount(5);
    this.setTestCasesChanging(true);
  }

  /**
   * Obtain the name for the series. This method is used if and only if the
   * data grouping involves at least the series level.
   * 
   * @return the name for the series directory
   */
  @Override
  protected String getSeriesName() {
    return "test"; //$NON-NLS-1$
  }

  /**
   * the main program called at startup
   * 
   * @param args
   *          the command line arguments
   */
  public static void main(String[] args) {
    new ElectionAggregationSingleTest("e:\\temp\\q").start(); //$NON-NLS-1$
  }

}
