/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-10-22
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.benchmark.GPBenchmarkSuccessFilter.java
 * Last modification: 2007-10-22
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

package org.dgpf.benchmark;

import org.sigoa.refimpl.genomes.bitString.ExactBitString;
import org.sigoa.refimpl.utils.testSeries.ISuccessFilter;
import org.sigoa.spec.go.IIndividual;

/**
 * The gp benchmark success filter is used to determine whether an
 * individual is a solution, a overfitted solution, or no solution at all.
 * 
 * @author Thomas Weise
 */
public class GPBenchmarkSuccessFilter implements
    ISuccessFilter<ExactBitString> {
  /**
   * the tunable model
   */
  private final TunableModel m_tunableModel;

  /**
   * Create a new GPBenchmarkSuccessFilter.
   * 
   * @param tunableModel
   *          the tunable model
   */
  public GPBenchmarkSuccessFilter(final TunableModel tunableModel) {
    super();
    this.m_tunableModel = tunableModel;
  }

  /**
   * Determine whether an output of a run would be considered as solution
   * judging by its objective values.
   * 
   * @param objectives
   *          an array containing the objective values of the individual
   * @return <code>true</code> if and only if the objective values
   *         indicate success, <code>false</code> otherwise
   */
  public boolean isSuccess(final double[] objectives) {
    return this.m_tunableModel.isSuccess(objectives);
  }

  /**
   * Determine whether an output of a run would be considered as solution
   * judging by its objective values.
   * 
   * @param individual
   *          the individual record
   * @return <code>true</code> if and only if the objective values
   *         indicate success, <code>false</code> otherwise
   */
  public boolean isSuccess(final IIndividual<?, ExactBitString> individual) {
    ExactBitString b;

    if (individual == null)
      return false;
    b = individual.getPhenotype();
    if (b == null)
      return false;
    return this.m_tunableModel.isSuccess(b.getData(), b.getLength());
  }

  /**
   * Determine whether a successful individual is overfitted or not.
   * 
   * @param phenotype
   *          the phenotype to check
   * @return <code>true</code> if the phenotype does not represent a
   *         general solution to the problem, <code>false</code>
   *         otherwise
   */
  public boolean isOverfitted(final ExactBitString phenotype) {
    if (phenotype == null)
      return true;
    return this.m_tunableModel.isOverfitted(phenotype.getData(), phenotype
        .getLength());
  }

}
