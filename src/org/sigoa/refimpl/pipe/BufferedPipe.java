/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-30
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.pipe.BufferedPipe.java
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
import java.util.Arrays;
import java.util.List;

import org.sigoa.spec.go.IIndividual;

/**
 * This class defines a pipe that uses an internal buffer in which it
 * stores all the individuals that are written to it. If <code>eof</code>
 * is called, it hands over this buffer to a buffer processing routine.
 *
 * @param <G>
 *          The genotype.
 * @param <PP>
 *          The phenotype.
 * @author Thomas Weise
 */
public abstract class BufferedPipe<G extends Serializable, PP extends Serializable>
    extends BufferedPipeBase<G, PP> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;


  /**
   * <code>true</code> if all duplicates should be removed from the
   * buffer before processing is done.
   */
  private boolean m_removeDuplicates;

  /**
   * Create a new buffered pipe.
   */
  protected BufferedPipe() {
    this(false);
  }

  /**
   * Create a new buffered pipe.
   *
   * @param removeDuplicates
   *          <code>true</code> if all duplicates should be removed from
   *          the buffer before processing is done.
   */
  protected BufferedPipe(final boolean removeDuplicates) {
    super();
    this.m_removeDuplicates = removeDuplicates;
  }

  /**
   * Store an individual into the internal buffer.
   *
   * @param individual
   *          The individual to be buffered.
   */
  @SuppressWarnings("unchecked")
  protected synchronized void bufferIndividual(
      final IIndividual<G, PP> individual) {
    int s, i;
    IIndividual<G, PP>[] buf, b2;

    s = this.m_bufferSize;
    buf = this.m_buffer;
    if (this.m_removeDuplicates) {
      for (i = (s - 1); i >= 0; i--) {
        if (buf[i] == individual)
          return;
      }
    }

    if (s >= buf.length) {
      b2 = new IIndividual[s << 1];
      System.arraycopy(buf, 0, b2, 0, s);
      this.m_buffer = buf = b2;
    }

    buf[s] = individual;
    this.m_bufferSize = (s + 1);
  }

  /**
   * Store a list of individual into the internal buffer.
   *
   * @param individuals
   *          The list of individuals to be buffered.
   */
  @SuppressWarnings("unchecked")
  @Override
  protected synchronized void bufferIndividuals(
      final List<IIndividual<G, PP>> individuals) {
    int s, c, n;
    IIndividual<G, PP>[] buf, b2;
    IIndividual<G, PP> ind;

    synchronized (individuals) {

      c = individuals.size();
      if (c <= 0)
        return;
      s = this.m_bufferSize;
      buf = this.m_buffer;
      if (s <= 0) {
        this.m_buffer = buf = individuals.toArray(buf);
        this.m_bufferSize = c;
        return;
      }

      n = (s + c);
      ind = buf[0];
      if (n > buf.length) {
        b2 = new IIndividual[n << 1];
        System.arraycopy(buf, 1, b2, c + 1, s - 1);
        this.m_buffer = buf = b2;
      } else {
        System.arraycopy(buf, 1, buf, c + 1, s - 1);
      }
      individuals.toArray(buf);
      buf[c] = ind;

      if (this.m_removeDuplicates) {
        main: for (s = (n - 1); s > 0; s--) {
          ind = buf[s];
          for (c = (s - 1); c >= 0; c--) {
            if (buf[c] == ind) {
              n--;
              buf[s] = buf[n];
              buf[n] = null;
              continue main;
            }
          }
        }
      }

      this.m_bufferSize = (n);
    }
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
    if (individual == null)
      throw new NullPointerException();
    this.bufferIndividual(individual);
  }

  /**
   * Returns <code>true</code> if it is ensured no individual record will
   * occure twice in the array handed to <code>process</code>. This does
   * not guarantee that no equal individual records will occure, it just
   * prevents one instance from occuring twice.
   *
   * @return <code>true</code> if and only if duplicate prevention is
   *         turned on.
   */
  protected boolean isRemovingDuplicates() {
    return this.m_removeDuplicates;
  }

  /**
   * If duplicate prevention is turned on, it is ensured no individual
   * record will occure twice in the array handed to <code>process</code>.
   * This does not guarantee that no equal individual records will occure,
   * it just prevents one instance from occuring twice. This prevention
   * algorithm, of course, takes additional processing time.
   *
   * @param removeDuplicates
   *          <code>true</code> if and only if duplicate prevention is
   *          turned on.
   */
  protected void setRemoveDuplicates(final boolean removeDuplicates) {
    this.m_removeDuplicates = removeDuplicates;
  }



  /**
   * This method passes an array containing all the buffered individuals to
   * <code>process</code>.
   */
  @Override
  protected synchronized void doEof() {
    IIndividual<G, PP>[] buf;
    int bs;

    bs = this.m_bufferSize;

    if (bs > 0) {
      buf = this.m_buffer;
      this.process(buf, bs);
      Arrays.fill(buf, 0, bs, null);
      this.m_bufferSize = 0;
    }
  }

  /**
   * Process the buffered population.
   *
   * @param source
   *          The buffered source population.
   * @param size
   *          The count of individuals in the population array.
   */
  protected abstract void process(final IIndividual<G, PP>[] source,
      final int size);
}
