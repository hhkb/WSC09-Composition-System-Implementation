/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-08-31
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.pipe.NonPrevalenceDegenerator.java
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

import org.sfc.utils.Utils;
import org.sigoa.spec.go.IComparator;
import org.sigoa.spec.go.IIndividual;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * <p>
 * This pipe allows only non-prevailed individuals to pass with probability
 * <i>1</i>. Prevailed individuals can pass only with probability <i>(1-p)<sup>n</sup></i>,
 * where <i>n</i> is the number of individuals that prevail them.
 * </p>
 * <p>
 * This filter is somewhat in between of {@link NonEqualFilter} and
 * {@link NonPrevalenceFilter}. For <i>p==0</i> it equals the
 * {@link NonEqualFilter} and for <i>p==1</i> it equals
 * {@link NonPrevalenceFilter}.
 * </p>
 * 
 * @param <G>
 *          The genotype.
 * @param <PP>
 *          The phenotype.
 * @author Thomas Weise
 */
public class NonPrevalenceDegenerator<G extends Serializable, PP extends Serializable>
    extends BufferedPipeBase<G, PP> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the probability with which an element will not pass per prevalence
   */
  private double m_prob;

  /**
   * Create a new prevalence degenerator.
   * 
   * @param prob
   *          the probability with which an element will not pass per
   *          prevalence
   */
  public NonPrevalenceDegenerator(final double prob) {
    super();
    this.m_prob = Math.max(0.0d, Math.min(1.0d, prob));
  }

  /**
   * Create a new prevalence degenerator.
   */
  public NonPrevalenceDegenerator() {
    this(0.3d);
  }

  /**
   * Store an individual into the internal buffer.
   * 
   * @param individual
   *          The individual to be buffered.
   * @param comparator
   *          the comparator to be applied
   */
  @SuppressWarnings("unchecked")
  protected void bufferIndividual(final IIndividual<G, PP> individual,
      final IComparator comparator) {
    int s, i, c;
    IIndividual<G, PP>[] buf, b2;
    IIndividual<G, PP> in;
    G g;
    boolean add;
    IRandomizer r;

    s = this.m_bufferSize;
    r = null;
    buf = this.m_buffer;
    add = true;
    if (s > 0) {
      g = individual.getGenotype();
      for (i = (s - 1); i >= 0; i--) {
        in = buf[i];
        if ((in == individual) || Utils.testEqualDeep(in.getGenotype(), g))
        // || (in.equals(individual))
        {
          this.m_bufferSize = s;
          return;
        }
        c = comparator.compare(in, individual);
        if (c > 0) {
          if (r == null)
            r = this.getRandomizer();
          if (r.nextDouble() < this.m_prob)
            buf[i] = buf[--s];
        }
        if (c < 0) {
          add = add
              & (((r == null) ? (r = this.getRandomizer()) : r)
                  .nextDouble() >= this.m_prob);
          // this.m_bufferSize = s;
          // return;
        }
      }
    }
    if (add) {
      if (s >= buf.length) {
        b2 = new IIndividual[s << 1];
        System.arraycopy(buf, 0, b2, 0, s);
        this.m_buffer = buf = b2;
      }

      buf[s] = individual;
      this.m_bufferSize = (s + 1);
    } else
      this.m_bufferSize = s;
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
    IComparator c;

    c = this.getComparator();
    synchronized (individuals) {
      for (IIndividual<G, PP> in : individuals) {
        this.bufferIndividual(in, c);
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
      this.bufferIndividual(individual, this.getComparator());
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
