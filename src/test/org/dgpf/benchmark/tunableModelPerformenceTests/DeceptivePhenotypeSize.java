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
public class DeceptivePhenotypeSize extends GPBenchmarkTestSeries {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the titles
   */
  private static final String[] TITLES = new String[] { "deceptive", //$NON-NLS-1$
      "phenotype size" }; //$NON-NLS-1$

  /**
   * the phenotype size iterator
   */
  private static final NumericIterator PHENOTYPE_SIZE = new NumericIterator(
      8, 200, 8, true, (CascadedIterator<?>) null);

  /**
   * the epistasis iterator
   */
  private static final NumericIterator RUGGEDNESS = new NumericIterator(
      0d, 10d, 0.5, false, PHENOTYPE_SIZE);

  /**
   * the iterator for the population sizes to test
   */
  private static final Iterator<Object[]> ITERATOR = new MultiCascadingIterator(
      new IStepable[] { RUGGEDNESS, PHENOTYPE_SIZE });

  /**
   * Create a new population size benchmark.
   * 
   * @param dir
   *          the directory
   */
  public DeceptivePhenotypeSize(final Object dir) {
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
   * compute the ruggedness
   * 
   * @param pl
   *          the phenotype len
   * @param v
   *          the value
   * @return the ruggedness
   */
  static final int rugged(final int pl, final double v) {
    int mr;

    mr = ((pl * (pl - 1)) >>> 1);

    return Math.min(mr, Math.max(1, (int) (Math.round((((//
        v * mr)))))));
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
    this.getTunableModel().setPhenotypeLength(
        ((Number) (parameters[1])).intValue());
    this.getTunableModel().setDeceptive(true);
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
    DeceptivePhenotypeSize x;

    x = new DeceptivePhenotypeSize("deceptivePhenoLen"); //$NON-NLS-1$
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