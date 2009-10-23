/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-02
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.evaluation.ParallelEvaluatorPipe.java
 * Last modification: 2006-12-02
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

package org.sigoa.refimpl.go.evaluation;

import java.io.Serializable;

import org.sigoa.spec.go.IIndividual;
import org.sigoa.spec.go.evaluation.IEvaluatorPipe;
import org.sigoa.spec.jobsystem.IHost;

/**
 * The parallel evaluator pipe makes use of the executor's parallelization
 * abilities.
 *
 * @param <G>
 *          The genotype
 * @param <PP>
 *          The phenotype
 * @author Thomas Weise
 */
public class ParallelEvaluatorPipe<G extends Serializable, PP extends Serializable>
    extends SequentialEvaluatorPipe<G, PP> implements IEvaluatorPipe<G, PP> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new evaluation pipe.
   */
  public ParallelEvaluatorPipe() {
    super();
  }

  /**
   * Write a new individual into the pipe. This default implementation only
   * checks the possible errors and throws exceptions if needed, but in
   * principle does nothing.
   *
   * @param individual
   *          The new individual to be written.
   * @throws NullPointerException
   *           if <code>individual</code> is <code>null</code>.
   */
  @Override
  public void write(final IIndividual<G, PP> individual) {
    IHost h;

    if (individual == null)
      throw new NullPointerException();

    h = this.getHost();

    if (h != null) {
      h.executeJob(new EvaluatorJob(individual));
    } else {
      this.getEvaluator().evaluate(individual);
      this.output(individual);
    }
  }

  /**
   * Propagate an <code>eof</code> to the next pipe stage if needed.
   */
  @Override
  public void eof() {
    IHost h;

    h = this.getHost();
    if (h != null)
      h.flushJobs();
    super.eof();
  }

  /**
   * An accessor to the <code>output</code> method which outputs an
   * individual to the next pipe level. This method calls
   * <code>write</code> of the next pipe stage if needed.
   *
   * @param individual
   *          The individual to be written to the next pipe stage.
   */
  final void doOutput(final IIndividual<G, PP> individual) {
    this.output(individual);
  }

  /**
   * The job interface for evaluation.
   *
   * @author Thomas Weise
   */
  private final class EvaluatorJob implements Runnable {

    /**
     * The individual to be evaluated.
     */
    private final IIndividual<G, PP> m_eval;

    /**
     * Create an evaluator job.
     *
     * @param eval
     *          The individual to be evaluated.
     */
    EvaluatorJob(final IIndividual<G, PP> eval) {
      super();
      this.m_eval = eval;
    }

    /**
     * This method is asynchronously invoked by the executor. It performs
     * the work of the job.
     */
    public void run() {
      ParallelEvaluatorPipe.this.doGetEvaluator().evaluate(this.m_eval);
      ParallelEvaluatorPipe.this.doOutput(this.m_eval);
    }

  }
}
