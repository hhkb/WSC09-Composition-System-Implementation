/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-05-06
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.benchmark.adrulyte.EpistasisSettings.java
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

import org.sigoa.refimpl.utils.testSeries.EDataGrouping;
import org.sigoa.refimpl.utils.testSeries.LoggingSelection;
import org.sigoa.refimpl.utils.testSeries.ParameterizedTestSeries;

import test.org.dgpf.benchmark.GPBenchmarkTestSeries;

/**
 * the epistasis test series
 * 
 * @author Thomas Weise
 */
public class RuggednessSettings extends GPBenchmarkTestSeries {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the ea factory
   */
  private static final EAFactory FACTORY = new EAFactory();

  /**
   * Create a new population size benchmark.
   * 
   * @param dir
   *          the directory
   */
  public RuggednessSettings(final Object dir) {
    super(dir, EDataGrouping.RUN, EAParameterIterator.TITLES,
        EAParameterIterator.ITERATOR);

    this.setMaxGenerations(1000);
    this.setCrossoverRate(0.8d);
    this.setMutationRate(0.2d);
    this.setEAFactory(FACTORY);
  }

  /**
   * This method is called for setting up the parameters
   * 
   * @param parameters
   *          the parameters
   */
  @Override
  protected void setupParameters(final Object[] parameters) {
    int i;

    i = 0;
    // fitness assignment
    FACTORY.m_fitnessAssignment = ((Number) (parameters[i++])).intValue();

    // selection algorithm
    FACTORY.m_selection = ((Number) (parameters[i++])).intValue();

    // steady state
    this.setSteadyState(((Boolean) (parameters[i++])).booleanValue());

    // convergence prevention
    this.setConvergencePrevention(((Number) (parameters[i++]))
        .doubleValue());

    //ruggedness
    this.getTunableModel().setRuggednessDifficultyLevel(
        ((Number) (parameters[i++])).intValue());

    this.getTunableModel().recalculateInternalValues();

    super.setupParameters(parameters);
  }

  /**
   * the main program called at startup
   * 
   * @param args
   *          the command line arguments
   */
  public static void main(final String[] args) {
    RuggednessSettings x;

    x = new RuggednessSettings("ruggedness-all"); //$NON-NLS-1$
    x.selectForLogging(LoggingSelection.RESULT_INDIVIDUALS);
    x.selectForLogging(LoggingSelection.RESULT_OBJECTIVES);
    x.selectForLogging(LoggingSelection.OBSERVE_SUCCESS);
    x.selectForLogging(ParameterizedTestSeries.RUN_PARAMETERS);

    x.setSteadyState(false);

    // TunableModel einstellungen!
    x.getTunableModel().setPhenotypeLength(80);

    x.start();
  }
}
