/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-29
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.utils.testSeries.IndividualEvaluator.java
 * Last modification: 2007-11-29
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

package org.sigoa.refimpl.utils.testSeries;

import java.io.Serializable;

import org.sfc.scoped.DefaultReferenceCounted;
import org.sigoa.refimpl.jobsystem.JobInfo;
import org.sigoa.spec.go.IIndividual;
import org.sigoa.spec.go.IOptimizationInfo;
import org.sigoa.spec.jobsystem.IJobInfo;
import org.sigoa.spec.jobsystem.IJobSystem;
import org.sigoa.spec.jobsystem.IWaitable;

/**
 * An evaluator is able to evaluate single individuals
 * 
 * @author Thomas Weise
 */
public final class IndividualEvaluator extends DefaultReferenceCounted {
  /**
   * the job system.
   */
  private final IJobSystem m_js;

  /**
   * the optimization info record
   */
  private final IOptimizationInfo<?, ?> m_info;

  /**
   * the ossize
   */
  private final int m_osSize;

  /**
   * Create a new evaluator
   * 
   * @param js
   *          the job system
   * @param info
   *          the optimization info
   * @param ossize
   *          the number of objectives
   */
  IndividualEvaluator(final IJobSystem js,
      final IOptimizationInfo<?, ?> info, final int ossize) {
    super();
    this.m_js = js;
    this.m_info = info;
    this.m_osSize = ossize;
  }

  /**
   * This method can be used to evaluate a single phenotype.
   * 
   * @param genotype
   *          the genotype (or <code>null</code>)
   * @param phenotype
   *          the phenotype to be evaluated (or <code>null</code>)
   * @return an individual record containing the evaluated data
   */
  @SuppressWarnings("unchecked")
  public IIndividual<?, ?> evaluate(final Serializable genotype,
      final Serializable phenotype) {
    IWaitable w;
    EvaluationJob b;

    b = new EvaluationJob(this.m_osSize);
    b.m_gd = genotype;
    b.m_pt = phenotype;

    w = this.m_js.executeOptimization(b,
        ((IJobInfo) (new JobInfo<Serializable, Serializable>(
            (IOptimizationInfo) this.m_info))));

    w.waitFor(false);
//    System.out.println(b.m_ind);
    return b.m_ind;
  }

  /**
   * Dispose this evaluator
   */
  @Override
  protected void dispose() {
    this.m_js.abort();
    super.dispose();
  }

}
