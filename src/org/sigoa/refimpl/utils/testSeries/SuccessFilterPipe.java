/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-29
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.utils.testSeries.SuccessFilterPipe.java
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

import org.sfc.io.TextWriter;
import org.sigoa.refimpl.adaptation.actions.AbortAction;
import org.sigoa.refimpl.pipe.Pipe;
import org.sigoa.spec.go.IIndividual;
import org.sigoa.spec.go.IOptimizer;

/**
 * This special pipe helps us to filter success events.
 * 
 * @param <PP>
 *          the phenotype
 * @author Thomas Weise
 */
public class SuccessFilterPipe<PP extends Serializable> extends
    Pipe<Serializable, PP> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the success filter
   */
  private final ISuccessFilter<PP> m_filter;

  /**
   * terminates the ea if a perfect success is encountered
   */
  private final boolean m_terminateOnPerfectSuccess;

  /**
   * the optimizer
   */
  private final IOptimizer<Serializable, PP> m_optimizer;

  /**
   * the iteration index
   */
  private int m_iteration;

  /**
   * the text writer
   */
  private final TextWriter m_output;

  /**
   * the success output writer
   */
  private final TextWriter m_successOutput;

  /**
   * Create a new pipe.
   * 
   * @param filter
   *          the success filter
   * @param terminateOnPerfectSuccess
   *          terminates the ea if a perfect success is encountered
   * @param optimizer
   *          the optimizer
   * @param output
   *          the writer to write to
   * @param successOutput
   *          the writer to write the successful individuals to
   */
  public SuccessFilterPipe(final ISuccessFilter<PP> filter,
      final boolean terminateOnPerfectSuccess,
      final IOptimizer<Serializable, PP> optimizer,
      final TextWriter output, final TextWriter successOutput) {
    super();
    this.m_filter = filter;
    this.m_terminateOnPerfectSuccess = terminateOnPerfectSuccess;
    this.m_optimizer = optimizer;
    this.m_output = output;
    if (output != null) {
      output.ensureNewLine();
      output.write("generation");//$NON-NLS-1$
      output.writeCSVSeparator();
      output.write("event"); //$NON-NLS-1$
    }
    this.m_successOutput = successOutput;
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
  public void write(final IIndividual<Serializable, PP> individual) {
    TextWriter t;

    this.output(individual);

    if (this.m_filter.isSuccess(individual)) {
      t = this.m_successOutput;
      if (t != null) {
        t.ensureNewLine();
        t.write("iteration: "); //$NON-NLS-1$
        t.writeInt(this.m_iteration);
        t.ensureNewLine();
        t.writeObject2(individual);
        t.flush();
      }

      t = this.m_output;
      if (t != null) {
        t.ensureNewLine();
        t.writeInt(this.m_iteration);
        t.writeCSVSeparator();
        t.write('s');
      }
      if (!(this.m_filter.isOverfitted(individual.getPhenotype()))) {
        if (t != null) {
          t.ensureNewLine();
          t.writeInt(this.m_iteration);
          t.writeCSVSeparator();
          t.write('p');

          if (this.m_terminateOnPerfectSuccess) {
            t.newLine();
            t.newLine();
            t.writeObject2(individual);
            t.newLine();
          }
        }

        if (this.m_terminateOnPerfectSuccess)
          AbortAction.ABORT_ACTION.perform(this.m_optimizer);
      }
      if (t != null)
        t.flush();
    }
  }

  /**
   * Propagate an <code>eof</code> to the next pipe stage if needed.
   */
  @Override
  public void eof() {
    this.m_iteration++;
    super.eof();
  }
}
