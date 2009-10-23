/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-19
 * Mutator          : Thomas Weise
 * Original Filename: org.dgpf.lgp.base.genome.plain.Int2StringLGPMutator.java
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
import org.sigoa.refimpl.genomes.string.StringConsecutiveElementMutator;
import org.sigoa.refimpl.genomes.string.StringDeletionMutator;
import org.sigoa.refimpl.genomes.string.StringInsertionMutator;
import org.sigoa.refimpl.genomes.string.StringRandomElementMutator;
import org.sigoa.refimpl.go.reproduction.IterativeMultiMutator;
import org.sigoa.refimpl.go.reproduction.PostProcessingMutator;
import org.sigoa.spec.go.reproduction.IMutator;

/**
 * The int[][] lgp mutator
 * 
 * @author Thomas Weise
 */
public class Int2StringLGPMutator extends PostProcessingMutator<int[][]> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the parameters
   */
  private final ILGPParameters m_parameters;

  /**
   * Create a new int[][]-lgp mutator.
   * 
   * @param editor
   *          the editor
   * @param parameters
   *          the parameters
   */
  @SuppressWarnings("unchecked")
  public Int2StringLGPMutator(final Int2StringLGPEditor editor,
      final ILGPParameters parameters) {
    super(new IterativeMultiMutator<int[][]>(new IMutator[] {
        new StringConsecutiveElementMutator<int[][]>(editor),
        new StringRandomElementMutator<int[][]>(editor),
        new StringInsertionMutator<int[][]>(editor),
        new StringDeletionMutator<int[][]>(editor) }, new double[] { 0.5d,
        0.43d, 0.02d, 0.05d })

    );
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
