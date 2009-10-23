/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-28
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.stoch.Randomizer.java
 * Last modification: 2006-11-28
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

package org.sigoa.refimpl.stoch;

import java.io.Serializable;
import java.util.Random;

import org.sigoa.spec.stoch.IRandomizer;

/**
 * The randomizer class is the default implementation of the
 * <code>IRandomizer</code> interface. It is not synchronized. This way,
 * it is faster than the normal class <code>Random</code>.
 * 
 * @author Thomas Weise
 */
public class Randomizer extends Random implements IRandomizer {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The random multiplier.
   */
  private final static long MULTIPLIER = 0x5DEECE66DL;

  /**
   * The number to add when multiplication has been done.
   */
  private final static long ADDEND = 0xBL;

  /**
   * The mask bits.
   */
  private final static long MASK = (1L << 48) - 1;

  /**
   * An internal seed counter.
   */
  private static int s_seed = -435735;

  /**
   * The fixed seed.
   */
  private final static Long FIXED_SEED = new Long(0x574e7d8fa8b34ca3L);

  /**
   * The seed of the randomizer.
   */
  private long m_seed;

  /**
   * Create a new randomizer.
   */
  public Randomizer() {
    this(createSeed());
  }

  /**
   * Create a new randomizer.
   * 
   * @param seed
   *          The randomizer's initial seed.
   */
  public Randomizer(final long seed) {
    super(seed);
    this.m_seed = seed;
  }

  /**
   * Create a seed for this randomizer.
   * 
   * @return A seed suitable for this randomizer.
   */
  private static final long createSeed() {
    synchronized (Randomizer.class) {
      return ((s_seed++) + System.currentTimeMillis());
    }
  }

  /**
   * Set a default seed to this randomizer.
   */
  public void setDefaultSeed() {
    this.setSeed(FIXED_SEED);
  }

  /**
   * Obtain an object that encapsulates this randomizer's current seed.
   * 
   * @return an object that encapsulates the current seed.
   */
  public Serializable getSeed() {
    return new Long(this.m_seed);
  }

  /**
   * Set the seed of this randomizer.
   * 
   * @param seed
   *          the seed value for this randomizer.
   * @throws NullPointerException
   *           if <code>seed==null</code>.
   * @throws IllegalArgumentException
   *           if <code>seed</code> is wrong.
   */
  public void setSeed(final Serializable seed) {
    long l;
    if (seed == null)
      throw new NullPointerException();
    if (!(seed instanceof Long))
      throw new IllegalArgumentException();
    l = ((Long) seed).longValue();
    this.setSeed(l);
  }

  /**
   * Sets the seed of this random number generator using a single
   * {@code long} seed. The general contract of {@code setSeed} is that it
   * alters the state of this random number generator object so as to be in
   * exactly the same state as if it had just been created with the
   * argument {@code seed} as a seed. The method {@code setSeed} is
   * implemented by class {@code Random} by atomically updating the seed to
   * 
   * @param seed
   *          the initial seed
   */
  @Override
  public void setSeed(final long seed) {
    this.m_seed = seed;
    super.setSeed(seed);
  }

  /**
   * Generates the next pseudorandom number. Subclass should override this,
   * as this is used by all other methods.
   * <p>
   * The general contract of <tt>next</tt> is that it returns an
   * <tt>int</tt> value and if the argument bits is between <tt>1</tt>
   * and <tt>32</tt> (inclusive), then that many low-order bits of the
   * returned value will be (approximately) independently chosen bit
   * values, each of which is (approximately) equally likely to be
   * <tt>0</tt> or <tt>1</tt>. This is a linear congruential
   * pseudorandom number generator, as defined by D. H. Lehmer and
   * described by Donald E. Knuth in <i>The Art of Computer Programming,</i>
   * Volume 2: <i>Seminumerical Algorithms</i>, section 3.2.1.
   * 
   * @param bits
   *          random bits
   * @return the next pseudorandom value from this random number
   *         generator's sequence.
   */
  @Override
  protected int next(final int bits) {
    long nextseed;

    this.m_seed = nextseed = (((this.m_seed * MULTIPLIER) + ADDEND) & MASK);

    return (int) (nextseed >>> (48 - bits));
  }

  /**
   * Obtain a random number which is normal distributed with (mu,sigma)
   * 
   * @param mu
   *          the expected value
   * @param sigma
   *          the standard deviation
   * @return the normally distributed random number generated
   * @throws IllegalArgumentException
   *           if
   *           <code>sigma &lt; 0 || Double.isNaN(sigma) || Double.isNaN(mu)</code>
   */
  public double nextNormal(final double mu, final double sigma) {
    if ((sigma < 0d) || Double.isNaN(sigma) || Double.isNaN(mu))
      throw new IllegalArgumentException();
    return (mu + (this.nextGaussian() * sigma));
  }

  /**
   * Obtain a random number which is exponentially distributed with lamda.
   * 
   * @param lambda
   *          the lambda value
   * @return The new exponentially distributed random number.
   * @throws IllegalArgumentException
   *           if <code>lambda &lt;= 0 || Double.isNaN(lambda)</code>
   */
  public double nextExponential(final double lambda) {
    double d;

    if ((lambda <= 0d) || Double.isNaN(lambda))
      throw new IllegalArgumentException();

    do {
      d = this.nextDouble();
    } while (d <= 0.0d);

    return -Math.log(d) / lambda;
  }
}
