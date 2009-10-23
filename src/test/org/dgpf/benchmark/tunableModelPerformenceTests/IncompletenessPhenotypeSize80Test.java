package test.org.dgpf.benchmark.tunableModelPerformenceTests;

import java.util.Iterator;

import org.sfc.collections.iterators.CascadedIterator;
import org.sfc.collections.iterators.NumericIterator;
import org.sfc.parallel.simulation.IStepable;
import org.sigoa.refimpl.utils.testSeries.EDataGrouping;
import org.sigoa.refimpl.utils.testSeries.LoggingSelection;
import org.sigoa.refimpl.utils.testSeries.MultiCascadingIterator;
import org.sigoa.refimpl.utils.testSeries.ParameterizedTestSeries;

import test.org.dgpf.benchmark.GPBenchmarkTestSeries;

/**
 * @author Ich
 */
public class IncompletenessPhenotypeSize80Test extends
    GPBenchmarkTestSeries {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the titles
   */
  private static final String[] TITLES = new String[] { "test case count", //$NON-NLS-1$
      "incompleteness" }; //$NON-NLS-1$

  /**
   * the phenotype size iterator
   */
  private static final NumericIterator INCOMPLETENESS = new NumericIterator(
      0, 40, 1, true, (CascadedIterator<?>) null);

  /**
   * the epistasis iterator
   */
  private static final NumericIterator TEST_CASE_COUNT = new NumericIterator(
      1, 20, 1, false, INCOMPLETENESS);

  /**
   * the iterator for the population sizes to test
   */
  private static final Iterator<Object[]> ITERATOR = new MultiCascadingIterator(
      new IStepable[] { TEST_CASE_COUNT, INCOMPLETENESS });

  /**
   * Create a new population size benchmark.
   * 
   * @param dir
   *          the directory
   */
  public IncompletenessPhenotypeSize80Test(final Object dir) {
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

    this.getTunableModel().setTestCaseCount(
        ((Number) (parameters[0])).intValue());
    this.getTunableModel().setInternalIncompletenessDifficultyLevel(
        ((Number) (parameters[1])).intValue());
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
    IncompletenessPhenotypeSize80Test x;

    x = new IncompletenessPhenotypeSize80Test(
        "incompletenessPhenotypeSize80"); //$NON-NLS-1$
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
