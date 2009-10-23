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

package test.org.sigoa.physics;

import java.util.List;

import org.dgpf.symbolicRegression.objectives.MinimizeNegligibleObjective;
import org.dgpf.symbolicRegression.scalar.real.DefaultRealLanguage;
import org.sfc.collections.CollectionUtils;
import org.sigoa.refimpl.genomes.tree.objectives.MinimizeWeightObjective;
import org.sigoa.refimpl.genomes.tree.reproduction.DefaultTreeMutator;
import org.sigoa.refimpl.genomes.tree.reproduction.SubTreeExchangeCrossover;
import org.sigoa.refimpl.genomes.tree.reproduction.TreeCreator;
import org.sigoa.refimpl.go.embryogeny.CopyEmbryogeny;
import org.sigoa.refimpl.simulation.SimulationProvider;
import org.sigoa.refimpl.utils.testSeries.EDataGrouping;
import org.sigoa.refimpl.utils.testSeries.ea.EATestSeries;
import org.sigoa.spec.go.embryogeny.IEmbryogeny;
import org.sigoa.spec.go.reproduction.ICreator;
import org.sigoa.spec.go.reproduction.ICrossover;
import org.sigoa.spec.go.reproduction.IMutator;
import org.sigoa.spec.simulation.ISimulationProvider;

import test.org.dgpf.election.ElectionTestSeries;

/**
 * The standard settings for the gcd problem
 * 
 * @author Thomas Weise
 */
public class PhysicTestSeries extends EATestSeries {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new parameterized test series based on evolutionary
   * algorithms
   * 
   * @param dir
   *          the directory to write to
   */
  public PhysicTestSeries(final Object dir) {
    super(dir, EDataGrouping.RUN, -1);

    this.setMaxGenerations(Integer.MAX_VALUE);
    this.setSteadyState(true);
    this.setMutationRate(0.4d);
    this.setCrossoverRate(0.7d);
    this.setPopulationSize(16 * 1024);
    this.setTestCasesChanging(true);
    this.setTestCaseCount(1);
    this.setEAFactory(ElectionTestSeries.EA_FACTORY);

    this.selectForLogging(BEST_OBJECTIVES);
    this.selectForLogging(BEST_INDIVIDUALS);
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
    return new TreeCreator(DefaultRealLanguage.DEFAULT_REAL_LANGUAGE);
  }

  /**
   * create the mutator
   * 
   * @return the mutator
   */
  @Override
  @SuppressWarnings("unchecked")
  protected IMutator<?> createMutator() {
    return new DefaultTreeMutator(new TreeCreator(
        DefaultRealLanguage.DEFAULT_REAL_LANGUAGE, 4, 0.2d, null));
  }

  /**
   * create the crossover
   * 
   * @return the crossover
   */
  @Override
  protected ICrossover<?> createCrossover() {
    return SubTreeExchangeCrossover.SUB_TREE_EXCHANGE_CROSSOVER;
  }

  /**
   * Obtain the simulation provider needed.
   * 
   * @return the simulation provider needed
   */
  @Override
  protected final ISimulationProvider[] createSimulationProviders() {
    return new ISimulationProvider[] { new SimulationProvider(
        PhysicsContext.class) };
  }

  /**
   * Create the objective functions
   * 
   * @return the list of objective functions
   */
  @Override
  protected List<?> createObjectives() {
    List<Object> l;
    int i;

    l = CollectionUtils.createList();
    for (i = 0; i < PhysicsContext.CNT; i++) {
      l.add(PhysicsContext.OBJECTIVES[i]);
    }
    l.add(MinimizeNegligibleObjective.MINIMIZE_NEGLIGIBLE_OBJECTIVE);
    l.add(MinimizeWeightObjective.MINIMIZE_WEIGHT_OBJECTIVE);

    return l;
  }

  // /**
  // * the internal comparator
  // */
  // private static final IComparator COMPARATOR = new ParetoComparator() {
  // /**
  // * The serial version uid.
  // */
  // private static final long serialVersionUID = 1;
  //
  // /**
  // * Compare two individuals with each other. The comparison is performed
  // * only on the base of <code>count</code> objective values starting
  // * with the <code>from</code><sup>th</sup> one.
  // *
  // * @param o1
  // * The first individual to be compared.
  // * @param o2
  // * The second individual to be compared.
  // * @param from
  // * the index of the first objective value to be taken into
  // * consideration
  // * @param to
  // * the exclusive last index of the comparison
  // * @return a negative value if o1 prevails o2, a positive value if o2
  // * prevails o1 and 0 if non of them prevails the other one.
  // */
  // @Override
  // protected int compare(final IIndividual<?, ?> o1,
  // final IIndividual<?, ?> o2, final int from, final int to) {
  // int i, j, a, b;
  // double d;
  //
  // for (j = 0; j < PhysicsContext.VALS2.length; j++) {
  // a = 0;
  // b = 0;
  // d = PhysicsContext.VALS2[j];
  // for (i = (PhysicsContext.CNT - 1); i >= 0; i--) {
  // if (o1.getObjectiveValue(i) < d)
  // a++;
  // if (o2.getObjectiveValue(i) < d)
  // b++;
  // }
  //
  // if ((a != b) && ((a > 1) || (b > 1))) {
  // return (b - a);
  // }
  // }
  //
  // return super.compare(o1, o2, from, to);
  // }
  //
  // };
  //
  // /**
  // * Create the comparator
  // *
  // * @return the comparator
  //   */
  //  @Override
  //  protected IComparator createComparator() {
  //    return COMPARATOR;
  //  }

  /**
   * the main program called at startup
   * 
   * @param args
   *          the command line arguments
   */
  public static void main(String[] args) {
    new PhysicTestSeries("").start(); //$NON-NLS-1$
  }
}
