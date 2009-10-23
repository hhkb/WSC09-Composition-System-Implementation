package test.org.dgpf.benchmark.geccoTests;

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
 * @author Locu
 */
public class GPBenchmarkEpistasisRuggedness extends GPBenchmarkTestSeries {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the titles
   */
  private static final String[] TITLES = new String[] { "epistasis", //$NON-NLS-1$
      "ruggedness" }; //$NON-NLS-1$

  /**
   * the phenotype size iterator
   */
  private static final NumericIterator RUGGEDNESS = new NumericIterator(
      0d, 1d, 0.03125, false, (CascadedIterator<?>) null);

  /**
   * the epistasis iterator
   */
  private static final NumericIterator EPISTASIS = new NumericIterator(2,
      63, 3, true, RUGGEDNESS);

  /**
   * the iterator for the population sizes to test
   */
  private static final Iterator<Object[]> ITERATOR = new MultiCascadingIterator(
      new IStepable[] { EPISTASIS, RUGGEDNESS });

  /**
   * Create a new population size benchmark.
   * 
   * @param dir
   *          the directory
   */
  public GPBenchmarkEpistasisRuggedness(final Object dir) {
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

    this.getTunableModel().setEpistasisDifficultyLevel(
        ((Number) (parameters[0])).intValue());

    int pl;// , mr;

    pl = 10 * 8;

    this.getTunableModel().setRuggednessDifficultyLevel(
        GPBenchmarkRuggednessPhenotypeSize.rugged(pl,
            ((Number) (parameters[1])).doubleValue()));

    this.getTunableModel().setPhenotypeLength(pl);

    super.setupParameters(parameters);
  }

  /**
   * the main program called at startup
   * 
   * @param args
   *          the command line arguments
   */
  public static void main(final String[] args) {
    GPBenchmarkEpistasisRuggedness x;

    x = new GPBenchmarkEpistasisRuggedness("epistasisPhenoLen"); //$NON-NLS-1$
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
