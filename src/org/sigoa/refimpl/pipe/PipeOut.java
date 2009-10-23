/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-30
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.pipe.PipeOut.java
 * Last modification: 2006-11-30
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

package org.sigoa.refimpl.pipe;

import java.io.Serializable;

import org.sigoa.refimpl.go.ImplementationBase;
import org.sigoa.spec.go.IIndividual;
import org.sigoa.spec.pipe.IPipeIn;
import org.sigoa.spec.pipe.IPipeOut;

/**
 * The basic implementation of the <code>IPipeOut</code>-interface.
 *
 * @param <G>
 *          The genotype.
 * @param <PP>
 *          The phenotype.
 * @author Thomas Weise
 */
public class PipeOut<G extends Serializable, PP extends Serializable>
    extends ImplementationBase<G, PP> implements IPipeOut<G, PP> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The output pipe.
   */
  private volatile IPipeIn<G, PP> m_output;

  /**
   * Create a new io pipe.
   */
  protected PipeOut() {
    super();
    // this.m_callEOF = true;
  }

  /**
   * Propagate an <code>eof</code> to the next pipe stage if needed.
   */
  public void eof() {
    IPipeIn<G, PP> p;

    p = this.m_output;
    if (p != null)
      p.eof();
  }

  /**
   * Output an individual to the next pipe level. This method calls
   * <code>write</code> of the next pipe stage if needed.
   *
   * @param individual
   *          The individual to be written to the next pipe stage.
   */
  protected void output(final IIndividual<G, PP> individual) {
    IPipeIn<G, PP> x;
    x = this.m_output;
    if (x != null)
      x.write(individual);
  }

  /**
   * Set the ouput pipe. If <code>pipe==null</code>, no output will be
   * performed.
   *
   * @param pipe
   *          The output pipe.
   */
  @SuppressWarnings("unchecked")
  public void setOutputPipe(final IPipeIn<? super G, ? super PP> pipe) {
    this.m_output = ((IPipeIn<G, PP>) pipe);
  }

  /**
   * Obtain the output pipe.
   *
   * @return The output pipe, or <code>null</code> if none is set.
   */
  public IPipeIn<? super G, ? super PP> getOutputPipe() {
    return this.m_output;
  }

  // /**
  // * Set whether this pipe should call eof on the output pipe after
  // writing
  // * its output to it or not. The default setting is <code>true</code>.
  // *
  // * @param callEOF
  // * <code>true</code> if and only if this pipe should call
  // * <code>eof</code> on the output pipe after finishing writing
  // * its output to it, <code>false</code> otherwise.
  // */
  // public void setCallEOF(final boolean callEOF) {
  // this.m_callEOF = callEOF;
  // }
  //
  // /**
  // * Obtain whether this pipe calls <code>eof</code> of the output pipe
  // * after finishing its work.
  // *
  // * @return <code>true</code> if and only if this pipe is calling the
  // * <code>eof</code>-method of the output pipe after finishing
  // * writing its output.
  // */
  // public boolean isCallingEOF() {
  // return this.m_callEOF;
  // }

}
