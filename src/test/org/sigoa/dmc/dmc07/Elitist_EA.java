/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-10
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.dmc.Elitist_EA.java
 * Last modification: 2006-12-10
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

package test.org.sigoa.dmc.dmc07;

import java.io.File;
import java.util.List;

import org.sfc.collections.CollectionUtils;
import org.sfc.io.writerProvider.FileTextWriterProvider;
import org.sfc.math.Mathematics;
import org.sigoa.refimpl.genomes.bitString.ByteBitStringEditor;
import org.sigoa.refimpl.genomes.string.StringCreator;
import org.sigoa.refimpl.genomes.string.StringEditor;
import org.sigoa.refimpl.genomes.string.VariableLengthStringMutator;
import org.sigoa.refimpl.genomes.string.VariableLengthStringNPointCrossover;
import org.sigoa.refimpl.go.OptimizationInfo;
import org.sigoa.refimpl.go.Population;
import org.sigoa.refimpl.go.algorithms.ea.EA;
import org.sigoa.refimpl.go.algorithms.ea.ElitistEA;
import org.sigoa.refimpl.go.comparators.TieredParetoComparator;
import org.sigoa.refimpl.go.evaluation.Evaluator;
import org.sigoa.refimpl.jobsystem.JobInfo;
import org.sigoa.refimpl.jobsystem.multiprocessor.MultiProcessorJobSystem;
import org.sigoa.refimpl.pipe.Pipeline;
import org.sigoa.refimpl.pipe.stat.IndividualPrinterPipe;
import org.sigoa.refimpl.pipe.stat.ObjectivePrinterPipe;
import org.sigoa.refimpl.simulation.SimulationManager;
import org.sigoa.spec.go.IComparator;
import org.sigoa.spec.go.IOptimizationInfo;
import org.sigoa.spec.go.IPopulation;
import org.sigoa.spec.go.embryogeny.IEmbryogeny;
import org.sigoa.spec.go.evaluation.IEvaluator;
import org.sigoa.spec.go.objectives.IObjectiveFunction;
import org.sigoa.spec.go.reproduction.ICreator;
import org.sigoa.spec.go.reproduction.ICrossover;
import org.sigoa.spec.go.reproduction.IMutator;
import org.sigoa.spec.jobsystem.IJobSystem;
import org.sigoa.spec.simulation.ISimulation;
import org.sigoa.spec.simulation.ISimulationManager;

/**
 * Here we try to evolve an algorithm which enumerates primes.
 * 
 * @author Thomas Weise
 */
public class Elitist_EA {

  /**
   * the editor
   */
  public static final StringEditor<byte[]> EDITOR = new ByteBitStringEditor(
      ClassifierSystem.GRANULARITY, 2, 55);

  /**
   * the default mutator
   */
  @SuppressWarnings("unchecked")
  public static final IMutator<byte[]> MUTATOR = new VariableLengthStringMutator<byte[]>(
      EDITOR);

  /**
   * the default creator
   */
  public static final ICreator<byte[]> CREATOR = new StringCreator<byte[]>(
      EDITOR);

  /**
   * the default crossover
   */
  public static final ICrossover<byte[]> CROSSOVER = new VariableLengthStringNPointCrossover<byte[]>(
      EDITOR);

  /**
   * The embryogeny
   */
  public static final IEmbryogeny<byte[], ClassifierSystem> EMBRYOGENY = ClassifierEmbryogeny.CLASSIFIER_EMBRYOGENY;

  /**
   * the simulation manager
   */
  public static final ISimulationManager SIMULATION_MANAGER = new SimulationManager();

  static {
    SIMULATION_MANAGER.addProvider(ClassificationSimulation.PROVIDER);
  }

  /**
   * the objectives
   */
  public static final List<IObjectiveFunction<ClassifierSystem, ?, ?, ISimulation<ClassifierSystem>>> OBJECTIVES = CollectionUtils
      .createList();

  /**
   * the initializer
   */
  @SuppressWarnings("unchecked")
  private static final void init() {
    IObjectiveFunction<ClassifierSystem, ?, ?, ISimulation<ClassifierSystem>> x;
    IObjectiveFunction<?, ?, ?, ?> y;

    y = new ProfitObjectiveFunction();
    x = ((IObjectiveFunction<ClassifierSystem, ?, ?, ISimulation<ClassifierSystem>>) y);
    OBJECTIVES.add(x);

    y = new SizeObjectiveFunction();
    x = ((IObjectiveFunction<ClassifierSystem, ?, ?, ISimulation<ClassifierSystem>>) y);
    OBJECTIVES.add(x);

    // secondary
    y = new ClassificationObjectiveFunction(EClasses.A);
    x = ((IObjectiveFunction<ClassifierSystem, ?, ?, ISimulation<ClassifierSystem>>) y);
    OBJECTIVES.add(x);

    y = new ClassificationObjectiveFunction(EClasses.B);
    x = ((IObjectiveFunction<ClassifierSystem, ?, ?, ISimulation<ClassifierSystem>>) y);
    OBJECTIVES.add(x);

    y = new ClassificationObjectiveFunction(EClasses.N);
    x = ((IObjectiveFunction<ClassifierSystem, ?, ?, ISimulation<ClassifierSystem>>) y);
    OBJECTIVES.add(x);
  }

  static {
    init();
  }

  /**
   * the shared comparator
   */
  public static final IComparator COMPARATOR = new TieredParetoComparator(
      new int[] { 2, 100 });

  /**
   * base dir
   */
  static String s_bd = "";// "E:\\temp\\"; //$NON-NLS-1$

  /**
   * the main program called at startup
   * 
   * @param args
   *          the command line arguments
   */
  @SuppressWarnings("unchecked")
  public static void main(String[] args) {

    IEvaluator<ClassifierSystem> eval;
    EA<byte[], ClassifierSystem> opt;
    IPopulation<byte[], ClassifierSystem> out;
    IOptimizationInfo<byte[], ClassifierSystem> oi;
    IJobSystem s;
    int pp;

    eval = new Evaluator<ClassifierSystem>(OBJECTIVES);

    pp = Mathematics.nextPrime((10 * 1024) + 1);// ((4 * 1024) + 1);

    System.out.println("Population-Size: " + pp); //$NON-NLS-1$

    opt = new ElitistEA<byte[], ClassifierSystem>(pp, 0.3, 0.7) {
      private static final long serialVersionUID = 1;

      @Override
      protected Pipeline<byte[], ClassifierSystem> createArchivePipeline() {
        Pipeline<byte[], ClassifierSystem> p;
        p = super.createArchivePipeline();
        p.add(new IndividualPrinterPipe<byte[], ClassifierSystem>(
            new FileTextWriterProvider(new File(s_bd + "c"), //$NON-NLS-1$
                "c"), false)); //$NON-NLS-1$
        p.add(new ObjectivePrinterPipe<byte[], ClassifierSystem>(
            new FileTextWriterProvider(new File(s_bd + "bo"), //$NON-NLS-1$
                "bo"), false)); //$NON-NLS-1$
        return p;
      }

    };

    out = new Population<byte[], ClassifierSystem>();
    opt.setOutputPipe(out);

    oi = new OptimizationInfo<byte[], ClassifierSystem>(eval,//
        COMPARATOR,//
        EMBRYOGENY,//
        CREATOR,//
        MUTATOR,//
        CROSSOVER,//
        null);

    s = new MultiProcessorJobSystem(SIMULATION_MANAGER);
    s.executeOptimization(opt, new JobInfo<byte[], ClassifierSystem>(oi));
    s.start();

    try {
      System.in.read();
    } catch (Throwable t) {
      //
    }

  }

}
