/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-01
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.reproduction.CrossoverPipe.java
 * Last modification: 2007-09-06
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

package org.sigoa.refimpl.go.reproduction;

import java.io.Serializable;

import org.sigoa.spec.go.IIndividual;
import org.sigoa.spec.go.IIndividualFactory;
import org.sigoa.spec.go.reproduction.ICrossover;
import org.sigoa.spec.go.reproduction.ICrossoverPipe;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * A simple crossover pipe implementation which forwards crossover to a
 * single crossover instance.
 * 
 * @param <G>
 *          The genotype
 * @param <PP>
 *          The phenotype
 * @author Thomas Weise
 */
public class CrossoverPipe<G extends Serializable, PP extends Serializable>
    extends HysteresisReproductionPipe<G, PP> implements
    ICrossoverPipe<G, PP> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The crossover rate.
   */
  private volatile double m_rate;

  /**
   * The buffered individual.
   */
  private volatile IIndividual<G, PP> m_buffer;

  /**
   * Create a new crossover pipe.
   * 
   * @param steadyState
   *          <code>true</code> if and only if this pipeline is steady
   *          state, <code>false</code> otherwise
   * @param hysteresis
   *          <code>true</code> if hysteresis should be applied,
   *          <code>false</code> otherwise
   * @param rate
   *          The initial crossover rate, must be in <code>[1,0]</code>
   * @throws IllegalArgumentException
   *           if <code>rate</code> not in <code>[1,0]</code>
   */
  public CrossoverPipe(final boolean steadyState,
      final boolean hysteresis, final double rate) {
    super(steadyState, hysteresis);
    if (Double.isNaN(rate) || (rate < 0) || (rate > 1))
      throw new IllegalArgumentException();
    this.m_rate = rate;
  }

  /**
   * Create a new crossover pipe.
   * 
   * @param steadyState
   *          <code>true</code> if and only if this pipeline is steady
   *          state, <code>false</code> otherwise
   * @param rate
   *          The initial crossover rate, must be in <code>[1,0]</code>
   * @throws IllegalArgumentException
   *           if <code>rate</code> not in <code>[1,0]</code>
   */
  public CrossoverPipe(final boolean steadyState, final double rate) {
    this(steadyState, false, rate);
  }

  /**
   * Create a new crossover pipe.
   * 
   * @param rate
   *          The initial crossover rate, must be in <code>[1,0]</code>
   * @throws IllegalArgumentException
   *           if <code>rate</code> not in <code>[1,0]</code>
   */
  public CrossoverPipe(final double rate) {
    this(true, rate);
  }

  /**
   * Set the crossover rate. The crossover rate denotes the fraction of
   * individuals that will undergo a crossover before being passed on. The
   * other individuals will be passed on without any modification.
   * 
   * @param rate
   *          the crossover rate which must be in <code>[0,1]</code>
   * @throws IllegalArgumentException
   *           if <code>rate</code> not in <code>[0,1]</code>
   */
  public synchronized void setCrossoverRate(final double rate) {
    if (Double.isNaN(rate) || (rate < 0) || (rate > 1))
      throw new IllegalArgumentException();
    this.m_rate = rate;
  }

  /**
   * Get the crossover rate. The crossover rate denotes the fraction of
   * individuals that will undergo a crossover before being passed on. The
   * other individuals will be passed on without any modification.
   * 
   * @return the crossover rate
   */
  public synchronized double getCrossoverRate() {
    return this.m_rate;
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
    IIndividual<G, PP> parent, x1, x2, cx;
    G g1, g2;
    IRandomizer r;
    ICrossover<G> c;
    IIndividualFactory<G, PP> f;

    if (individual == null)
      throw new NullPointerException();

    synchronized (this) {
      r = this.getRandomizer();

      if (this.m_rate <= r.nextDouble()) {
        this.output(individual);
        return;
      }

      parent = this.m_buffer;
      if (parent == null) {
        this.m_buffer = individual;
        return;
      }

      c = this.getCrossover();

      g1 = individual.getGenotype();
      g2 = parent.getGenotype();
      f = this.getIndividualFactory();

      x1 = f.createIndividual(individual, parent);
      x1.setGenotype(c.crossover(g1, g2, r));

      x2 = f.createIndividual(parent, individual);
      x2.setGenotype(c.crossover(g2, g1, r));

      this.m_buffer = null;
    }

    if (this.m_hystersis) {
      if (parent.getFitness() < individual.getFitness()) {
        cx = individual;
      } else {
        cx = parent;
        parent = individual;
      }

      if (!(copyEvaluation(parent, x1))) {
        copyEvaluation(cx, x1);
        copyEvaluation(cx, x2);
      } else {
        copyEvaluation(parent, x2);
      }
    } else
      cx = individual;

    if (this.m_steadyState) {
      this.output(cx);
      this.output(parent);
    }

    this.output(x1);
    this.output(x2);
  }

  /**
   * Tell the pipe that all individuals have been written to it. This
   * method calls the <code>eof()</code> method of the next pipe stage.
   */
  @Override
  public void eof() {
    synchronized (this) {
      if (this.m_buffer != null) {
        this.output(this.m_buffer);
        this.m_buffer = null;
      }
    }

    super.eof();
  }
}
