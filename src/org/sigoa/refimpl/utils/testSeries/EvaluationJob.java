/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-10-11
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.utils.testSeries.EvaluationJob.java
 * Last modification: 2007-10-11
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

import org.sigoa.refimpl.go.IndividualFactory;
import org.sigoa.refimpl.go.Optimizer;
import org.sigoa.spec.go.IIndividual;
import org.sigoa.spec.go.IIndividualFactory;

/**
 * this internal class just serves as evaluator for a single individual
 * 
 * @author Thomas Weise
 */
final class EvaluationJob extends Optimizer<Serializable, Serializable> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the genotype
   */
  Serializable m_gd;

  /**
   * the phenotype
   */
  Serializable m_pt;

  /**
   * the objective function count
   */
  private final int m_oc;

  /**
   * the individual
   */
  IIndividual<Serializable, Serializable> m_ind;

  /**
   * Create a new optimizer.
   * 
   * @param oc
   *          the objective function count
   */
  EvaluationJob(final int oc) {
    super();
    this.m_oc = oc;
  }

  /**
   * This method does the work of the optimizer.
   */
  @Override
  @SuppressWarnings("unchecked")
  protected void doRun() {
    IIndividual<Serializable, Serializable> ind;
    Serializable p;
    IIndividualFactory<Serializable, Serializable> s;

    s = this.getIndividualFactory();
    if (s == null)
      s = ((IIndividualFactory) (IndividualFactory.DEFAULT_INDIVIDUAL_FACTORY));
    this.m_ind = ind = s.createIndividual(this.m_oc);

    ind.setGenotype(this.m_gd);
    p = this.m_pt;
    if (p == null) {
      p = this.getEmbryogeny().hatch(this.m_gd);
    }
    ind.setPhenotype(p);

    this.getEvaluator().evaluate(ind);

    this.m_gd = null;
    this.m_pt = null;
  }

}
