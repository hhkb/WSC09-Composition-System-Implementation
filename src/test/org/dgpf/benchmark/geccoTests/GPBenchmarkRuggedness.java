package test.org.dgpf.benchmark.geccoTests;

import java.util.Iterator;

import org.sigoa.refimpl.utils.testSeries.EDataGrouping;
import org.sigoa.refimpl.utils.testSeries.LoggingSelection;
import org.sigoa.refimpl.utils.testSeries.ParameterizedTestSeries;

import test.org.dgpf.benchmark.GPBenchmarkTestSeries;
import test.org.dgpf.benchmark.populationSize.DoubleIterator;

/**
 * 
 * @author Locu
 *
 */
public class GPBenchmarkRuggedness extends GPBenchmarkTestSeries {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the titles
   */
  private static final String[] TITLES = new String[] { "ruggedness level" }; //$NON-NLS-1$

  /**
   * the iterator for the population sizes to test
   */
//  private static final Iterator<Object[]> ITERATOR = new IntegerIterator(
//      1, 10, 10);
  private static final Iterator<Object[]> ITERATOR = new DoubleIterator(
      0, 10, 0.5);

  /**
   * Create a new population size benchmark.
   * 
   * @param dir
   *          the directory
   */
  public GPBenchmarkRuggedness(final Object dir) {
    super(dir, EDataGrouping.RUN, TITLES, ITERATOR);

    this.setPopulationSize(1000);
    this.setMaxGenerations(1000);
    this.setCrossoverRate(0.8d);
    this.setMutationRate(0.2d);
    this.setConvergencePrevention(0d);
    this.setHysteresis(0d);
    this.setSteadyState(false);
  }

  /**
   * This method is called for setting up the parameters
   * 
   * @param parameters
   *          the parameters
   */
  @Override
  protected void setupParameters(final Object[] parameters) {
    this.getTunableModel().setRuggednessDifficultyLevel(
          ((Number) (parameters[0])).doubleValue());
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
    GPBenchmarkRuggedness x;

    x = new GPBenchmarkRuggedness("d:\\temp\\newModel\\ruggednessTest3"); //$NON-NLS-1$
    x.selectForLogging(LoggingSelection.RESULT_INDIVIDUALS);
    x.selectForLogging(LoggingSelection.RESULT_OBJECTIVES);
    x.selectForLogging(LoggingSelection.OBSERVE_SUCCESS);
//    x.selectForLogging(BEST_OBJECTIVES);
    x.selectForLogging(ParameterizedTestSeries.RUN_PARAMETERS);

    x.setSteadyState(false);
    
    //TunableModel einstellungen!
    x.getTunableModel().setPhenotypeLength( 80 );

    x.start();

  }

}