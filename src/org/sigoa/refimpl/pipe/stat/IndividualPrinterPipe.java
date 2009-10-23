/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-22
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.pipe.stat.IndividualPrinterPipe.java
 * Last modification: 2008-05-20
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

package org.sigoa.refimpl.pipe.stat;

import java.io.Serializable;

import org.sfc.io.TextWriter;
import org.sfc.io.writerProvider.IWriterProvider;
import org.sigoa.spec.go.IIndividual;

/**
 * this special pipe stores the individuals it receives to an output writer
 * 
 * @param <G>
 *          The genotype.
 * @param <PP>
 *          The phenotype.
 * @author Thomas Weise
 */
public class IndividualPrinterPipe<G extends Serializable, PP extends Serializable>
    extends PrinterPipe<G, PP, TextWriter> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1L;

  /**
   * the start timer
   */
  private final long m_start;

  /**
   * the iteration start.
   */
  private long m_startIt;

  /**
   * Create a print pipe.
   * 
   * @param provider
   *          the text writer provider to be used to obtain the output
   *          writers
   * @param flushing
   *          <code>true</code> if and only if this pipe is flushing
   *          after every output, <code>false</code> otherwise
   * @throws NullPointerException
   *           if <code>provider==null</code>
   */
  public IndividualPrinterPipe(final IWriterProvider<TextWriter> provider,
      final boolean flushing) {
    super(provider, flushing);
    this.m_startIt = this.m_start = System.currentTimeMillis();
  }

  /**
   * Create a print pipe.
   * 
   * @param provider
   *          the text writer provider to be used to obtain the output
   *          writers
   * @throws NullPointerException
   *           if <code>provider==null</code>
   */
  public IndividualPrinterPipe(final IWriterProvider<TextWriter> provider) {
    this(provider, true);
  }

  /**
   * Write an individual's data to the output.
   * 
   * @param individual
   *          The new individual to be written.
   * @param dest
   *          the text writer to write to
   */
  @Override
  protected void outputIndividual(final IIndividual<G, PP> individual,
      final TextWriter dest) {

    // dest.write(individual.toString());
    dest.writeObject(individual);
    dest.newLine();
    dest.newLine();

  }

  /**
   * This method is called when an iteration ends.
   * 
   * @param w
   *          the text writer.
   * @param iteration
   *          the index of the iteration that just ended
   */
  @Override
  protected void onIterationEnd(final TextWriter w, final long iteration) {
    long t;
    t = System.currentTimeMillis();

    w.ensureNewLine();
    w.newLine();
    w.write("___ END ___"); //$NON-NLS-1$
    w.newLine();
    w.write("Iteration     : "); //$NON-NLS-1$
    w.writeLong(iteration);
    w.newLine();
    w.write("Individuals   : "); //$NON-NLS-1$
    w.writeInt(this.getIndividualCount());
    w.newLine();
    w.write("Start-Time    : "); //$NON-NLS-1$
    w.writeTime(this.m_startIt, false);
    w.newLine();
    w.write("End-Time      : "); //$NON-NLS-1$
    w.writeTime(t, false);
    w.newLine();
    w.write("Iteration-Time: "); //$NON-NLS-1$
    w.writeTime((t - this.m_startIt), true);
    w.newLine();
    w.write("Begin-Time    : "); //$NON-NLS-1$
    w.writeTime(this.m_start, false);
    w.newLine();
    w.write("Total-Time    : "); //$NON-NLS-1$
    w.writeTime((t - this.m_start), true);
    this.m_startIt = t;

    super.onIterationEnd(w, iteration);
  }

}
