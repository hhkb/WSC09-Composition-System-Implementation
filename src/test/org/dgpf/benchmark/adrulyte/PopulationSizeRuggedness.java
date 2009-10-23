package test.org.dgpf.benchmark.adrulyte;

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
 * The population size test
 */
public class PopulationSizeRuggedness extends GPBenchmarkTestSeries {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the titles
   */
  private static final String[] TITLES = new String[] { "ruggedness", //$NON-NLS-1$
      "population size" }; //$NON-NLS-1$

  /**
   * the population size iterator
   */
  private static final NumericIterator POPULATION_SIZE = new NumericIterator(
      4, 12, 1, true, (CascadedIterator<?>) null);

  /**
   * the ruggedness iterator
   */
  private static final NumericIterator RUGGEDNESS = //
  new NumericIterator(0, 10, 1, true, POPULATION_SIZE);

  /**
   * the iterator for the population sizes to test
   */
  private static final Iterator<Object[]> ITERATOR = new MultiCascadingIterator(
      new IStepable[] { RUGGEDNESS, POPULATION_SIZE });

  /**
   * Create a new population size benchmark.
   * 
   * @param dir
   *          the directory
   */
  public PopulationSizeRuggedness(final Object dir) {
    super(dir, EDataGrouping.RUN, TITLES, ITERATOR);

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

    this.getTunableModel().setDeceptive(false);
    this.getTunableModel().setRuggednessDifficultyLevel(
        ((Number) (parameters[0])).intValue());

    this.getTunableModel().recalculateInternalValues();

    this.setPopulationSize(//
        1 << ((Number) (parameters[1])).intValue());

    super.setupParameters(parameters);
  }

  /**
   * the main program called at startup
   * 
   * @param args
   *          the command line arguments
   */
  public static void main(final String[] args) {
    PopulationSizeRuggedness x;

    x = new PopulationSizeRuggedness("e:\\temp\\ps-rug"); //$NON-NLS-1$
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
