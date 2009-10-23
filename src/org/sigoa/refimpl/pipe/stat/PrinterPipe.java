/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-22
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.pipe.stat.PrinterPipe.java
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

import java.io.IOException;
import java.io.Serializable;

import org.sfc.io.TextWriter;
import org.sfc.io.writerProvider.IWriterProvider;
import org.sfc.utils.ErrorUtils;
import org.sigoa.refimpl.pipe.Pipe;
import org.sigoa.spec.go.IIndividual;

/**
 * this special pipe stores the individuals it receives to an output writer
 * 
 * @param <G>
 *          The genotype.
 * @param <PP>
 *          The phenotype.
 * @param <T>
 *          the type of writer provided. This can be for example a normal
 *          {@link java.io.Writer}, a {@link org.sfc.io.TextWriter} or a
 *          writer or for example a {@link org.sfc.xml.sax.SAXWriter}.
 * @author Thomas Weise
 */
public abstract class PrinterPipe<G extends Serializable, PP extends Serializable, T extends TextWriter>
    extends Pipe<G, PP> implements IWriterProvider<T> {

  /**
   * the iteration counter
   */
  private long m_iteration;

  /**
   * the output writer
   */
  private T m_out;

  /**
   * the writer provider used.
   */
  private final IWriterProvider<T> m_provider;

  /**
   * <code>true</code> if and only if this pipe is flushing after every
   * output, <code>false</code> otherwise
   */
  private boolean m_isFlushing;

  /**
   * the number of individuals allowed to be printed per eof
   */
  private int m_limit;

  /**
   * the individual count
   */
  private int m_cnt;

  /**
   * Create a print pipe.
   * 
   * @param provider
   *          the writer provider to be used to obtain the output writers
   * @param flushing
   *          <code>true</code> if and only if this pipe is flushing
   *          after every output, <code>false</code> otherwise
   * @throws NullPointerException
   *           if <code>provider==null</code>
   */
  protected PrinterPipe(final IWriterProvider<T> provider,
      final boolean flushing) {
    super();
    if (provider == null)
      throw new NullPointerException();
    this.m_iteration = -1;
    this.m_provider = provider;
    this.m_isFlushing = flushing;
    this.m_limit = -1;
  }

  /**
   * Create a print pipe.
   * 
   * @param provider
   *          the writer provider to be used to obtain the output writers
   * @throws NullPointerException
   *           if <code>provider==null</code>
   */
  protected PrinterPipe(final IWriterProvider<T> provider) {
    this(provider, true);
  }

  /**
   * Obtain the next output writer.
   * 
   * @param iteration
   *          the current iteration
   * @return the next output writer or <code>null</code> if none could be
   *         created
   */
  public T provideWriter(final long iteration) {
    return this.provideWriter(Long.valueOf(iteration));
  }

  /**
   * This method is called when an iteration begins.
   * 
   * @param w
   *          the text writer.
   * @param iteration
   *          the index of the iteration that just ended
   */
  protected void onIterationBegin(final T w, final long iteration) {
    //
  }

  /**
   * Write an individual's data to the output.
   * 
   * @param individual
   *          The new individual to be written.
   * @param dest
   *          the text writer to write to
   */
  protected abstract void outputIndividual(
      final IIndividual<G, PP> individual, final T dest);

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
    T w;
    long l;

    super.write(individual);

    synchronized (this) {
      w = this.m_out;
      if (w == null) {

        l = (++this.m_iteration);
        this.m_out = w = this.provideWriter(this.m_iteration);
        if (w == null)
          return;
        this.onIterationBegin(w, l);
        if (this.m_isFlushing)
          w.flush();
      }

      if ((this.m_limit < 0) || ((this.m_cnt++) < this.m_limit)) {
        this.outputIndividual(individual, w);
      }
      if (this.m_isFlushing)
        w.flush();
    }
  }

  /**
   * Obtain the number of individuals in this iteration
   * 
   * @return the number of individuals in this iteration
   */
  protected final int getIndividualCount() {
    return this.m_cnt;
  }

  /**
   * This method is called when an iteration ends.
   * 
   * @param w
   *          the text writer.
   * @param iteration
   *          the index of the iteration that just ended
   */
  protected void onIterationEnd(final T w, final long iteration) {
    //
  }

  /**
   * Tell the pipe that all individuals have been written to it.
   */
  @Override
  public void eof() {
    T w;

    super.eof();

    synchronized (this) {
      w = this.m_out;
      if (w != null) {
        this.onIterationEnd(w, this.m_iteration);
        this.m_cnt = 0;
        w.flush();
        try {
          w.close();
        } catch (IOException ioe) {//
          ErrorUtils.onError(ioe);
        } finally {
          this.m_out = null;
        }
      }
    }
  }

  /**
   * finalize this object
   * 
   * @throws Throwable
   *           if something goes wring
   */
  @Override
  protected synchronized void finalize() throws Throwable {
    try {
      if (this.m_out != null) {
        try {
          this.m_out.close();
        } finally {
          this.m_out = null;
        }
      }
    } finally {
      super.finalize();
    }
  }

  /**
   * Check whether this pipe is flushing or not.
   * 
   * @return* <code>true</code> if and only if this pipe is flushing
   *          after every output, <code>false</code> otherwise
   */
  public boolean isFlushing() {
    return this.m_isFlushing;
  }

  /**
   * Set whether this pipe should be flushing or not
   * 
   * @param flushing
   *          <code>true</code> if and only if this pipe should flush
   *          after every output, <code>false</code> otherwise
   */
  public void setFlushing(final boolean flushing) {
    this.m_isFlushing = flushing;
  }

  /**
   * Limits the number of individuals allowed to be printed per iteration
   * 
   * @param limit
   *          the maximum number of individuals that may be printed per
   *          iteration (-1 stands for infinite)
   */
  public void setLimit(final int limit) {
    this.m_limit = limit;
  }

  /**
   * Obtain the maximum number of individuals that may be printed per
   * iteration
   * 
   * @return the maximum number of individuals that may be printed per
   *         iteration (-1 stands for infinite)
   */
  public int getLimit() {
    return this.m_limit;
  }

  /**
   * Obtain the next output writer.
   * 
   * @param info
   *          An object containing information to be used for the writer
   *          creation. This could be an instance of {@link Integer} or a
   *          string
   * @return the next output writer
   */
  public T provideWriter(final Object info) {
    return this.m_provider.provideWriter(info);
  }
}
