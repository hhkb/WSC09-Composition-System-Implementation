/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-10-22
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.benchmark.GPBenchmarkEvaluator.java
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
import org.sigoa.spec.go.IIndividual;
import org.sigoa.spec.go.evaluation.IEvaluator;

/**
 * A fast evaluator component for the gp benchmark
 * 
 * @author Thomas Weise
 */
public class GPBenchmarkEvaluator implements IEvaluator<ExactBitString> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the tunable model
   */
  private final TunableModel m_model;

  /**
   * create a new evaluator for the gp benchmark
   * 
   * @param model
   *          the tunable model
   */
  @SuppressWarnings("unchecked")
  public GPBenchmarkEvaluator(final TunableModel model) {
    super();

    this.m_model = model;
  }

  /**
   * Evaluate an individual by computing all its objective values.
   * 
   * @param individual
   *          The individual to be evaluated.
   * @throws NullPointerException
   *           if the individual or its phenotype or the host is
   *           <code>null</code> or if no simulation manager is
   *           available.
   */
  public final void evaluate(
      final IIndividual<?, ExactBitString> individual) {
    int i, l;
    final TunableModel m;
    ExactBitString s;
    byte[] b;

    individual.clearEvaluation();

    s = individual.getPhenotype();
    b = s.getData();
    l = s.getLength();
    m = this.m_model;

    i = m.getFunctionalObjectiveFunctionCount();
    individual
        .setObjectiveValue(i, m.nonfunctionalObjectiveFunction(b, l));

    for (--i; i >= 0; i--) {
      individual.setObjectiveValue(i, m.functionalObjectiveFunction(b, l,i));
    }

  }

  /**
   * Obtain the count of the objective values which are set by this
   * evaluator.
   * 
   * @return The count of objective functions applied.
   */
  public int getObjectiveValueCount() {
    return this.m_model.getFunctionalObjectiveFunctionCount() + 1;
  }
}
