/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-19
 * Crossover          : Thomas Weise
 * Original Filename: org.dgpf.lgp.base.genome.plain.Int2StringLGPCrossover.java
 * Last modification: 2007-11-19
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

package org.dgpf.lgp.base.genome.plain;

import org.dgpf.lgp.base.ILGPParameters;
import org.dgpf.lgp.base.genome.Int2StringPostProcessor;
import org.sigoa.refimpl.genomes.string.FixedLengthStringNPointCrossover;
import org.sigoa.refimpl.genomes.string.VariableLengthStringNPointCrossover;
import org.sigoa.refimpl.go.reproduction.IterativeMultiCrossover;
import org.sigoa.refimpl.go.reproduction.PostProcessingCrossover;
import org.sigoa.spec.go.reproduction.ICrossover;

/**
 * The int[][] lgp crossover
 * 
 * @author Thomas Weise
 */
public class Int2StringLGPCrossover extends
    PostProcessingCrossover<int[][]> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the parameters
   */
  private final ILGPParameters m_parameters;

  /**
   * Create a new int[][]-lgp crossover.
   * 
   * @param editor
   *          the editor
   * @param parameters
   *          the parameters
   */
  @SuppressWarnings("unchecked")
  public Int2StringLGPCrossover(final Int2StringLGPEditor editor,
      final ILGPParameters parameters) {
    super(new IterativeMultiCrossover<int[][]>(//
        new ICrossover[] {//
        new FixedLengthStringNPointCrossover<int[][]>(editor),//
            new VariableLengthStringNPointCrossover<int[][]>(editor),//
            new Int2StringProcCrossover(editor) },// 
        new double[] { 3d, 1d, 3d }));
    this.m_parameters = parameters;
  }

  /**
   * The postprocessing routing.
   * 
   * @param genotype
   *          the genotype
   * @return the postprocessed genotype.
   */
  @Override
  protected int[][] postprocess(final int[][] genotype) {
    return Int2StringPostProcessor
        .postProcess(genotype, this.m_parameters);
  }
}
