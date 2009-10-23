/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-01
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.reproduction.MutatorPipe.java
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
import org.sigoa.spec.go.reproduction.IMutatorPipe;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * A simple mutator pipe implementation which forwards mutation to a single
 * mutator instance.
 * 
 * @param <G>
 *          The genotype
 * @param <PP>
 *          The phenotype
 * @author Thomas Weise
 */
public class MutatorPipe<G extends Serializable, PP extends Serializable>
    extends HysteresisReproductionPipe<G, PP> implements
    IMutatorPipe<G, PP> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The mutation rate.
   */
  private volatile double m_rate;

  /**
   * Create a new mutation pipe.
   * 
   * @param steadyState
   *          <code>true</code> if and only if this pipeline is steady
   *          state, <code>false</code> otherwise
   * @param hysteresis
   *          <code>true</code> if hysteresis should be applied,
   *          <code>false</code> otherwise
   * @param rate
   *          The initial mutation rate, must be in <code>[1,0]</code>
   * @throws IllegalArgumentException
   *           if <code>rate</code> not in <code>[1,0]</code>
   */
  public MutatorPipe(final boolean steadyState, final boolean hysteresis,
      final double rate) {
    super(steadyState, hysteresis);
    if (Double.isNaN(rate) || (rate < 0) || (rate > 1))
      throw new IllegalArgumentException();
    this.m_rate = rate;
  }

  /**
   * Create a new mutation pipe.
   * 
   * @param steadyState
   *          <code>true</code> if and only if this pipeline is steady
   *          state, <code>false</code> otherwise
   * @param rate
   *          The initial mutation rate, must be in <code>[1,0]</code>
   * @throws IllegalArgumentException
   *           if <code>rate</code> not in <code>[1,0]</code>
   */
  public MutatorPipe(final boolean steadyState, final double rate) {
    this(steadyState, false, rate);
  }

  /**
   * Create a new mutation pipe.
   * 
   * @param rate
   *          The initial mutation rate, must be in <code>[1,0]</code>
   * @throws IllegalArgumentException
   *           if <code>rate</code> not in <code>[1,0]</code>
   */
  public MutatorPipe(final double rate) {
    this(true, rate);
  }

  /**
   * Set the mutation rate. The mutation rate denotes the fraction of
   * individuals that will undergo a mutation before being passed on. The
   * other individuals will be passed on without any modification.
   * 
   * @param rate
   *          the mutation rate which must be in <code>[0,1]</code>
   * @throws IllegalArgumentException
   *           if <code>rate</code> not in <code>[0,1]</code>
   */
  public synchronized void setMutationRate(final double rate) {
    if (Double.isNaN(rate) || (rate < 0) || (rate > 1))
      throw new IllegalArgumentException();
    this.m_rate = rate;
  }

  /**
   * Get the mutation rate. The mutation rate denotes the fraction of
   * individuals that will undergo a mutation before being passed on. The
   * other individuals will be passed on without any modification.
   * 
   * @return the mutation rate
   */
  public synchronized double getMutationRate() {
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
    IIndividual<G, PP> x;
    IRandomizer r;

    if (individual == null)
      throw new NullPointerException();

    r = this.getRandomizer();
    x = individual;
    synchronized (this) {
      if (this.m_rate > r.nextDouble()) {
        x = this.getIndividualFactory().createIndividual(individual);
        x.setGenotype(this.getMutator()
            .mutate(individual.getGenotype(), r));
      }
    }

    if (x != individual) {
      if (this.m_hystersis)
        copyEvaluation(individual, x);
      if (this.m_steadyState)
        this.output(individual);
    }

    this.output(x);
  }

}
