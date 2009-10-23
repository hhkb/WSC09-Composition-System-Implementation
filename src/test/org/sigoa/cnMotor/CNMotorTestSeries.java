/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-22
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.gcd.GCDTestSeries.java
 * Last modification: 2007-09-22
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

package test.org.sigoa.cnMotor;

import java.io.Serializable;
import java.util.List;

import org.sfc.collections.CollectionUtils;
import org.sfc.parallel.SfcThread;
import org.sfc.utils.Utils;
import org.sigoa.refimpl.genomes.doubleString.reproduction.DoubleStringEditor;
import org.sigoa.refimpl.genomes.doubleString.reproduction.FixedLengthDoubleStringCrossover;
import org.sigoa.refimpl.genomes.string.FixedLengthStringMutator;
import org.sigoa.refimpl.genomes.string.StringCreator;
import org.sigoa.refimpl.go.algorithms.ea.EA;
import org.sigoa.refimpl.go.algorithms.ea.ElitistEA;
import org.sigoa.refimpl.go.embryogeny.CopyEmbryogeny;
import org.sigoa.refimpl.go.selection.TournamentSelectionR;
import org.sigoa.refimpl.utils.testSeries.EDataGrouping;
import org.sigoa.refimpl.utils.testSeries.ea.EATestSeries;
import org.sigoa.refimpl.utils.testSeries.ea.IEAFactory;
import org.sigoa.spec.go.embryogeny.IEmbryogeny;
import org.sigoa.spec.go.reproduction.ICreator;
import org.sigoa.spec.go.reproduction.ICrossover;
import org.sigoa.spec.go.reproduction.IMutator;
import org.sigoa.spec.go.selection.ISelectionAlgorithm;
import org.sigoa.spec.simulation.ISimulationProvider;

/**
 * The standard settings for the gcd problem
 * 
 * @author Thomas Weise
 */
public class CNMotorTestSeries extends EATestSeries {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * The factory for elitist eas
   */
  public static final IEAFactory ELITIST_EA = new IEAFactory() {
    /**
     * Create the evolutionary algorithm
     * 
     * @return the new evolutionary algorithm, ready to use
     */
    public EA<?, ?> createEA() {
      return new ElitistEA<Serializable, Serializable>() {
        /**
         * the serial version uid
         */
        private static final long serialVersionUID = 1;

        @Override
        protected ISelectionAlgorithm<Serializable, Serializable> createSelectionAlgorithm() {
          return new TournamentSelectionR<Serializable, Serializable>(
              true, this.getNextPopulationSize(), 3);
        }

        /**
         * this method is invoked after each iteration is performed
         * 
         * @param index
         *          the index of the current itertion
         */
        @Override
        protected void afterIteration(final long index) {
          super.afterIteration(index);
          // waitSecs( 3*60 );
        }
      };
    }

    /**
     * Obtain the name of this ea factory
     * 
     * @return the name of this ea factory
     */
    @Override
    public String toString() {
      return "elitist ea"; //$NON-NLS-1$
    }
  };

  /**
   * Wait for the given amount of seconds
   * 
   * @param secs
   *          the seconds
   */
  static final void waitSecs(int secs) {
    int f;
    for (f = secs; f > 0; f--) {
      Utils.invokeGC();
      SfcThread.pause(1000l);
    }
  }

  /**
   * the string editor
   */
  static final DoubleStringEditor EDITOR = new DoubleStringEditor(1, // granularity
      5, // minlength
      5, // maxlength

      new double[] {// minimal values
      0d, // min 1
          0d, // min 2
          0.05d, // min 3
          0d, // min 4
          0d // min 5
      },

      new double[] {// maximal values
      1.4d, // max 1
          1.4d, // max 2
          1d, // max 3
          1d, // max 4
          1d // max 5
      }

  );

  /**
   * Create a new parameterized test series based on evolutionary
   * algorithms
   * 
   * @param dir
   *          the directory to write to
   */
  public CNMotorTestSeries(final Object dir) {
    super(dir, EDataGrouping.RUN, -1);

    this.setMaxGenerations(Integer.MAX_VALUE);
    this.setArchiveSize(2);
    this.setSteadyState(false);
    this.setMutationRate(0.35d);
    this.setCrossoverRate(0.7d);
    this.setPopulationSize(20);
    this.setTestCasesChanging(true);
    this.setTestCaseCount(1);
    this.setEAFactory(ELITIST_EA);

    this.selectForLogging(null);
  }

  /**
   * Obtain the number of available processors.
   * 
   * @return the number of available processors.
   */
  @Override
  protected int getProcessorCount() {
    return 1;
  }

  /**
   * create the embryogeny
   * 
   * @return the embryogeny
   */
  @Override
  protected IEmbryogeny<?, ?> createEmbryogeny() {
    return CopyEmbryogeny.COPY_EMBRYOGENY;
  }

  /**
   * create the creator
   * 
   * @return the creator
   */
  @Override
  protected ICreator<?> createCreator() {
    return new StringCreator<double[]>(EDITOR);
  }

  /**
   * create the mutator
   * 
   * @return the mutator
   */
  @Override
  @SuppressWarnings("unchecked")
  protected IMutator<?> createMutator() {
    return new FixedLengthStringMutator<double[]>(EDITOR, false);
  }

  /**
   * create the crossover
   * 
   * @return the crossover
   */
  @Override
  protected ICrossover<?> createCrossover() {
    return new FixedLengthDoubleStringCrossover(EDITOR);
  }

  /**
   * Obtain the simulation provider needed.
   * 
   * @return the simulation provider needed
   */
  @Override
  protected final ISimulationProvider[] createSimulationProviders() {
    return new ISimulationProvider[] { new MotorProvider() };
  }

  /**
   * Create the objective functions
   * 
   * @return the list of objective functions
   */
  @Override
  protected List<?> createObjectives() {
    List<Object> l;

    l = CollectionUtils.createList();
    l.add(new MinimizeSqrErrorObjective());

    return l;
  }

  /**
   * the main program called at startup
   * 
   * @param args
   *          the command line arguments
   */
  public static void main(String[] args) {
    new CNMotorTestSeries("").start(); //$NON-NLS-1$
  }
}
