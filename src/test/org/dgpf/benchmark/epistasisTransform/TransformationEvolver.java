/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-27
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.benchmark.epistasisTransform.TransformationEvolver.java
 * Last modification: 2007-11-27
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

package test.org.dgpf.benchmark.epistasisTransform;

import java.io.File;
import java.util.List;

import org.sfc.collections.CollectionUtils;
import org.sfc.io.IO;
import org.sfc.io.writerProvider.FileTextWriterProvider;
import org.sfc.math.Mathematics;
import org.sigoa.refimpl.genomes.intString.IntStringEditor;
import org.sigoa.refimpl.genomes.string.FixedLengthStringNPointCrossover;
import org.sigoa.refimpl.genomes.string.StringEditor;
import org.sigoa.refimpl.genomes.string.StringRandomGenePermutator;
import org.sigoa.refimpl.go.OptimizationInfo;
import org.sigoa.refimpl.go.algorithms.ea.EA;
import org.sigoa.refimpl.go.algorithms.ea.ElitistEA;
import org.sigoa.refimpl.go.comparators.ParetoComparator;
import org.sigoa.refimpl.go.embryogeny.CopyEmbryogeny;
import org.sigoa.refimpl.go.evaluation.Evaluator;
import org.sigoa.refimpl.go.reproduction.Creator;
import org.sigoa.refimpl.jobsystem.JobInfo;
import org.sigoa.refimpl.jobsystem.multiprocessor.MultiProcessorJobSystem;
import org.sigoa.refimpl.pipe.EqualObjectiveDegenerator;
import org.sigoa.refimpl.pipe.stat.IndividualPrinterPipe;
import org.sigoa.refimpl.pipe.stat.ObjectivePrinterPipe;
import org.sigoa.refimpl.simulation.SimulationManager;
import org.sigoa.spec.go.IOptimizationInfo;
import org.sigoa.spec.go.embryogeny.IEmbryogeny;
import org.sigoa.spec.go.evaluation.IEvaluator;
import org.sigoa.spec.go.objectives.IObjectiveFunction;
import org.sigoa.spec.go.reproduction.ICreator;
import org.sigoa.spec.go.reproduction.ICrossover;
import org.sigoa.spec.go.reproduction.IMutator;
import org.sigoa.spec.jobsystem.IJobSystem;
import org.sigoa.spec.simulation.ISimulation;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * This ea finds good transformations
 * 
 * @author Thomas Weise
 */
public class TransformationEvolver {
  /**
   * the number of bits
   */
  public static final int K = 2;

  /**
   * the number of numbers
   */
  public static final int N = (1 << K);

  /**
   * the string editor
   */
  public static final StringEditor<int[]> EDITOR = new IntStringEditor(1,
      N, N);

  /**
   * the crossover
   */
  public static final ICrossover<int[]> CROSS = new FixedLengthStringNPointCrossover<int[]>(
      EDITOR);

  /**
   * the mutator
   */
  public static final IMutator<int[]> MUT = new StringRandomGenePermutator<int[]>(
      EDITOR);

  /**
   * the creator
   */
  public static final ICreator<int[]> CREAT = new Creator<int[]>() {
    private static final long serialVersionUID = 1;

    public int[] create(final IRandomizer random) {
      int[] x;
      int i, j, k;

      x = new int[N];
      for (i = (x.length - 1); i >= 0; i--) {
        xxx: for (;;) {
          k = random.nextInt(N);
          for (j = (N - 1); j > i; j--) {
            if (x[j] == k)
              continue xxx;
          }
          break;
        }
        x[i] = k;
      }
      return x;
    }
  };

  /**
   * the objectives
   */
  public static final List<IObjectiveFunction<int[], ?, ?, ISimulation<int[]>>> TEST = CollectionUtils
      .createList();
  static {
    TEST.add(new TObjectiveFunction());
  }

  /**
   * the main program called at startup
   * 
   * @param args
   *          the command line arguments
   */
  @SuppressWarnings("unchecked")
  public static void main(String[] args) {

    IEvaluator<int[]> eval;
    EA<int[], int[]> opt;
    IOptimizationInfo<int[], int[]> oi;
    IJobSystem s;
    int pp;
    File x, c, bo;// , ao;

    pp = Mathematics.nextPrime((10 * 1024) + 1);
    System.out.println("population size: " + pp);//$NON-NLS-1$

    x = IO.getFile("E:\\temp\\111");//$NON-NLS-1$

    c = new File(x, "c"); //$NON-NLS-1$
    c.mkdirs();
    bo = new File(x, "bo"); //$NON-NLS-1$
    bo.mkdirs();
    // ao = new File(x, "ao"); //$NON-NLS-1$
    // ao.mkdirs();

    eval = new Evaluator<int[]>(TEST, 1, false);

    opt = new ElitistEA<int[], int[]>(pp, 0.8, 0.2, false, false, 128);

    opt.addPopulationPipe(new EqualObjectiveDegenerator<int[],int[]>(0.3d));
    opt.addNonPrevailedPipe(new IndividualPrinterPipe<int[], int[]>(
        new FileTextWriterProvider(c, "c"), false)); //$NON-NLS-1$
    opt.addNonPrevailedPipe(new ObjectivePrinterPipe<int[], int[]>(
        new FileTextWriterProvider(bo, "bo"), false)); //$NON-NLS-1$
    // opt.addPopulationPipe(new ObjectivePrinterPipe<int[], int[]>(
    // new FileTextWriterProvider(ao, "ao"), false)); //$NON-NLS-1$

    oi = new OptimizationInfo<int[], int[]>(eval,//
        ParetoComparator.PARETO_COMPARATOR,//
        ((IEmbryogeny) (CopyEmbryogeny.COPY_EMBRYOGENY)),//
        CREAT,//
        MUT,//
        CROSS,//
        null);

    s = new MultiProcessorJobSystem(new SimulationManager());
    s.executeOptimization(opt, new JobInfo<int[], int[]>(oi));

    s.start();

  }
}
