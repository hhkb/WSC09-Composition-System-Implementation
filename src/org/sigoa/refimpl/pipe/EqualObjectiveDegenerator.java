/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-08-31
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.pipe.EqualObjectiveDegenerator.java
 * Last modification: 2007-08-31
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
import java.util.List;

import org.sigoa.spec.go.IIndividual;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * This pipe allows only individuals with different objective values to
 * pass with probability <i>1</i>. Individuals with similar objectives can
 * pass only with probability <i>(1-p)<sup>n</sup></i>, where <i>n</i>
 * is the number of individuals that prevail them.
 * 
 * @param <G>
 *          The genotype.
 * @param <PP>
 *          The phenotype.
 * @author Thomas Weise
 */
public class EqualObjectiveDegenerator<G extends Serializable, PP extends Serializable>
    extends BufferedPipeBase<G, PP> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the probability with which an element will not pass equal objective
   * set
   */
  private double m_prob;

  /**
   * Create a new equal objective degenerator.
   * 
   * @param prob
   *          the probability with which an element will not pass per equal
   *          objective
   */
  public EqualObjectiveDegenerator(final double prob) {
    super();
    this.m_prob = Math.max(0.0d, Math.min(1.0d, prob));
  }

  /**
   * Create a new equal objective degenerator.
   */
  public EqualObjectiveDegenerator() {
    this(0.2d);
  }

  /**
   * Compare two individuals regarding their objective values.
   * 
   * @param i1
   *          the first individual
   * @param i2
   *          the second individual
   * @return the comparison result: <code>true</code> for equal
   *         objectives, <code>false</code> for different ones
   */
  private static final boolean compare(final IIndividual<?, ?> i1,
      final IIndividual<?, ?> i2) {
    int i;

    i = i1.getObjectiveValueCount();
    for (--i; i >= 0; i--) {
      if (Double.compare(i1.getObjectiveValue(i), i2.getObjectiveValue(i)) != 0)
        return false;
    }

    return true;
  }

  /**
   * Store an individual into the internal buffer.
   * 
   * @param individual
   *          The individual to be buffered.
   */
  @SuppressWarnings("unchecked")
  protected void bufferIndividual(final IIndividual<G, PP> individual) {
    int s, i;
    IIndividual<G, PP>[] buf, b2;
    IIndividual<G, PP> in;
    IRandomizer r;

    s = this.m_bufferSize;
    r = null;
    buf = this.m_buffer;

    if (s > 0) {
      for (i = (s - 1); i >= 0; i--) {
        in = buf[i];
        if (in == individual) {
          this.m_bufferSize = s;
          return;
        }
        if (compare(in, individual)) {
          if (r == null)
            r = this.getRandomizer();
          if (r.nextDouble() < this.m_prob)
            buf[i] = buf[--s];
        }
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

    synchronized (individuals) {
      for (IIndividual<G, PP> in : individuals) {
        this.bufferIndividual(in);
      }
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
    synchronized (this) {
      this.bufferIndividual(individual);
    }
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

      for (--bs; bs >= 0; bs--) {
        this.output(buf[bs]);
        buf[bs] = null;
      }

      this.m_bufferSize = 0;
    }
  }
}
