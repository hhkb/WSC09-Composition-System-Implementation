/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-28
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.stoch.IRandomizer.java
 * Last modification: 2007-10-22
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

package org.sigoa.spec.stoch;

/**
 * A versatile random number generator interface.
 * 
 * @author Thomas Weise
 * @version 1.0.0
 */
public interface IRandomizer extends IRandomNumberGenerator {
  /**
   * Obtain a new randomized boolean value.
   * 
   * @return the boolean value generated
   */
  public abstract boolean nextBoolean();

  /**
   * Obtain a randomized double, uniformely distributed in the intervall
   * [0,1).
   * 
   * @return the uniformly distributed random number
   */
  public abstract double nextDouble();

  /**
   * Obtain a randomized integer, uniformely distributed in [0,max).
   * 
   * @param max
   *          the (exclusive) maximum value
   * @return the newly created integer value
   * @exception IllegalArgumentException
   *              n is not positive.
   */
  public abstract int nextInt(int max);

  /**
   * Obtain a standard normal distributed random number.
   * 
   * @return the standard normal distributed random number
   */
  public abstract double nextGaussian();

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
  public abstract double nextNormal(final double mu, final double sigma);

  /**
   * Obtain a random number which is exponentially distributed with lamda.
   * 
   * @param lambda
   *          the lambda value
   * @return The new exponentially distributed random number.
   * @throws IllegalArgumentException
   *           if <code>lambda &lt;= 0 || Double.isNaN(lambda)</code
   */
  public abstract double nextExponential(final double lambda);

  /**
   * Obtain the next random integer.
   * 
   * @return An integer with totally randomized content.
   */
  public abstract int nextInt();

  /**
   * Generates random bytes and places them into a user-supplied byte
   * array. The number of random bytes produced is equal to the length of
   * the byte array.
   * 
   * @param bytes
   *          the non-null byte array in which to put the random bytes.
   */
  public abstract void nextBytes(final byte[] bytes);

  /**
   * Returns the next pseudorandom, uniformly distributed {@code long}
   * value from this random number generator's sequence. The general
   * contract of {@code nextLong} is that one {@code long} value is
   * pseudorandomly generated and returned.
   * 
   * @return the next pseudorandom, uniformly distributed {@code long}
   *         value from this random number generator's sequence
   */
  public abstract long nextLong();
}
