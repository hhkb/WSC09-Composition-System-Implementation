/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-05
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.math.Mathematics.java
 * Last modification: 2007-03-05
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

package org.sfc.math;

import org.sfc.utils.ErrorUtils;

/**
 * This class contains some mathematic utilities.
 * 
 * @author Thomas Weise
 */
public final class Mathematics extends ConstantSet {
  // mathematical constants

  /**
   * sqrt 2
   */
  public static final double SQRT_2 = Math.sqrt(2d);

  /**
   * sqrt 3
   */
  public static final double SQRT_3 = Math.sqrt(3d);

  /**
   * sqrt 5
   */
  public static final double SQRT_5 = Math.sqrt(5d);

  /**
   * e ^ pi
   */
  public static final double E_PI = Math.pow(Math.E, Math.PI);

  /**
   * pi ^ e
   */
  public static final double PI_E = Math.pow(Math.PI, Math.E);

  /**
   * The natural logarithm of 10
   */
  public static final double LN_10 = Math.log(10.0d);

  /**
   * The natural logarithm of 2
   */
  public static final double LN_2 = Math.log(2.0d);

  /**
   * Euler-Mascheroni constant
   */
  public static final double EULER_MASCHERONI = 0.57721566490153286060651209008240243d;

  /**
   * Golden Ratio
   */
  public static final double GOLDEN_RATIO = 1.61803398874989484820458683436563811d;

  /**
   * Plastic Constant
   */
  public static final double PLASTIC_CONSTANT = 1.3247179572447460259609088544780973d;

  /**
   * Embree-Trefethen
   */
  public static final double EMBREE_TREFETHEN = 0.70258d;

  /**
   * Feigenbaum
   */
  public static final double FEIGENBAUM_DELTA = 4.66920160910299067185320382046620161d;

  /**
   * Feigenbaum
   */
  public static final double FEIGENBAUM_ALPHA = 2.50290787509589282228390287321821578d;

  /**
   * Twin Prime
   */
  public static final double TWIN_PRIME = 0.66016181584686957392781211001455577d;

  /**
   * Meissel-Mertens
   */
  public static final double MEISSEL_MERTENS = 0.26149721284764278375542683860869585d;

  /**
   * Bruns
   */
  public static final double BRUNS_TWIN_PRIMES = 1.9021605823d;

  /**
   * Bruns
   */
  public static final double BRUNS_QUADRUPELT_PRIMES = 0.8705883800d;

  /**
   * de Bruijn-Newman
   */
  public static final double DE_BRUIJN_NEWMAN = -2.7e-9d;

  /**
   * Catalan
   */
  public static final double CATALAN = 0.91596559417721901505460351493238411d;

  /**
   * Landau-Ramanujan
   */
  public static final double LANDAU_RAMANUJAN = 0.76422365358922066299069873125009232d;

  /**
   * Viswanath
   */
  public static final double VISWANATH = 1.13198824d;

  /**
   * Ramanujan-Soldner
   */
  public static final double RAMANUJAN_SOLDNER = 1.45136923488338105028396848589202744d;

  /**
   * Erdos-Borwein
   */
  public static final double ERDOS_BORWEIN = 1.60669515241529176378330152319092458d;

  /**
   * Bernstein
   */
  public static final double BERNSTEIN = 0.28016949902386913303d;

  /**
   * Gauss-Kuzwin
   */
  public static final double GAUSS_KUZWIN = 0.30366300289873265859744812190155623d;

  /**
   * Hafner-Sarnak-McCurley
   */
  public static final double HAFNER_SARNAK_MCCURLEY = 0.35323637185499598454d;

  /**
   * Golomb-Dickman
   */
  public static final double GOLOMB_DICKMAN = 0.62432998854355087099293638310083724d;

  /**
   * Cahen
   */
  public static final double CAHAN = 0.6434105463d;

  /**
   * Laplace Limit
   */
  public static final double LAPLACE_LIMIT = 0.66274341934918158097474209710925290d;

  /**
   * Alladi-Grinstead
   */
  public static final double ALLADI_GRINSTEAD = 0.8093940205d;

  /**
   * Lengyel
   */
  public static final double LENGYEL = 1.0986858055d;

  /**
   * Levy
   */
  public static final double LEVY = 3.27582291872181115978768188245384386d;

  /**
   * Apery
   */
  public static final double APERY = 1.20205690315959428539973816151144999d;

  /**
   * Mill
   */
  public static final double MILL = 1.30637788386308069046861449260260571d;

  /**
   * Backhouse
   */
  public static final double BACKHOUSE = 1.45607494858268967139959535111654356d;

  /**
   * Porter
   */
  public static final double PORTER = 1.4670780794d;

  /**
   * Lieb
   */
  public static final double LIEB = 1.5396007178d;

  /**
   * Niven
   */
  public static final double NIVEN = 1.70521114010536776428855145343450816d;

  /**
   * Sierpinski
   */
  public static final double SIERPINSKI = 2.58498175957925321706589358738317116d;

  /**
   * Khinchin
   */
  public static final double KHINCHIN = 2.68545200106530644530971483548179569d;

  /**
   * Fransen-Robinson
   */
  public static final double FRANSEN_ROBINSON = 2.80777024202851936522150118655777293d;

  /**
   * Parabolic
   */
  public static final double PARABOLIC_CONSTANT = 2.29558714939263807403429804918949039d;

  /**
   * Omega
   */
  public static final double OMEGA = 0.56714329040978387299996866221035555d;

  /**
   * the highest possible integer prime
   */
  public static final int MAX_PRIME = 2147483629;// getMaxPrime();

  /**
   * a threshold for computations
   */
  public static final double EPS = (10 * Double.MIN_VALUE);

  /**
   * The sqare root of the maximum integer.
   */
  private static final int SQRT_MAX_INT = ((int) (Math
      .sqrt(Integer.MAX_VALUE)));

  /**
   * The sqare root of the maximum integer threshold.
   */
  private static final int SQRT_MAX_INT_TH = (SQRT_MAX_INT * SQRT_MAX_INT);

  /**
   * Check whether <code>d</code> is a number and neither infinite nor
   * NaN.
   * 
   * @param d
   *          the double value to check
   * @return <code>true</code> if and only if <code>d</code> is a
   *         normal number,<code>false</code> otherwise
   */
  public static final boolean isNumber(final double d) {
    return (!(Double.isInfinite(d) || Double.isNaN(d)));
  }

  /**
   * Wrap a value so it fits into 0...mod-1
   * 
   * @param value
   *          the value to be wrapped
   * @param mod
   *          the modulo operator
   * @return the wrapped value
   */
  public static final int modulo(final int value, final int mod) {
    if (mod == 0) {
      if (value < 0)
        return Integer.MIN_VALUE;
      return Integer.MAX_VALUE;
    }
    if (value < 0)
      return (((value % mod) + mod) % mod);
    return (value % mod);
  }

  /**
   * Wrap a value so it fits into 0...mod-1
   * 
   * @param value
   *          the value to be wrapped
   * @param mod
   *          the modulo operator
   * @return the wrapped value
   */
  public static final long modulo(final long value, final long mod) {
    if (mod == 0l) {
      if (value < 0l)
        return Long.MIN_VALUE;
      return Long.MAX_VALUE;
    }
    if (value < 0)
      return (((value % mod) + mod) % mod);
    return (value % mod);
  }

  /**
   * Perform a division which returns a result rounded to the next higher
   * integer.
   * 
   * @param dividend
   *          the divident
   * @param divisor
   *          the divisor
   * @return the result
   */
  public static final int ceilDiv(final int dividend, final int divisor) {

    int r;

    if (divisor == 0)
      return ((dividend > 0) ? Integer.MAX_VALUE : Integer.MIN_VALUE);
    r = (dividend / divisor);

    if ((r * divisor) == dividend)
      return r;

    if (dividend < 0) {
      if (divisor < 0)
        return (r + 1);
      return (r - 1);
    }
    if (divisor < 0)
      return (r - 1);
    return (r + 1);
  }

  /**
   * Perform a division which returns a result rounded to the next higher
   * integer.
   * 
   * @param dividend
   *          the divident
   * @param divisor
   *          the divisor
   * @return the result
   */
  public static final long ceilDiv(final long dividend, final long divisor) {
    long r;

    if (divisor == 0)
      return ((dividend > 0) ? Long.MAX_VALUE : Long.MIN_VALUE);
    r = (dividend / divisor);

    if ((r * divisor) == dividend)
      return r;

    if (dividend < 0) {
      if (divisor < 0)
        return (r + 1);
      return (r - 1);
    }
    if (divisor < 0)
      return (r - 1);
    return (r + 1);
  }

  /**
   * check whether a given number is prime or not
   * 
   * @param num
   *          the number to check
   * @return <code>true</code> if it is, <code>false</code> otherwise
   */
  public static final boolean isPrime(final int num) {
    int i, m;

    if (num <= 1)
      return false;
    if (num <= 3)
      return true;

    if ((num & 1) == 0)
      return false;

    if (num > MAX_PRIME)
      return false;

    i = 3;

    if (num < SQRT_MAX_INT_TH) {
      do {
        if ((num % i) <= 0)
          return false;
        i += 2;
      } while ((i * i) < num);
    } else {
      m = SQRT_MAX_INT;
      do {
        if ((num % i) <= 0)
          return false;
        i += 2;
      } while (i <= m);
    }

    return true;
  }

  /**
   * Obtain the next prime greater than <code>num</code>.
   * 
   * @param num
   *          the number we want to find a greater or equal prime of
   * @return the greater prime
   */
  public static final int nextPrime(final int num) {
    int n, i, m;

    if (num >= MAX_PRIME)
      return MAX_PRIME;
    n = ((num < 3) ? 3 : (((num & 1) == 0) ? (num + 1) : num));

    main: for (;; n += 2) {
      i = 3;

      if (n < SQRT_MAX_INT_TH) {
        do {
          if ((n % i) <= 0)
            continue main;
          i += 2;
        } while ((i * i) < n);
      } else {
        m = SQRT_MAX_INT;
        do {
          if ((n % i) <= 0)
            continue main;
          i += 2;
        } while (i <= m);
      }

      return n;
    }
  }

  /**
   * Compare two doubles and return a number proportional to their
   * difference.
   * 
   * @param d1
   *          the first double
   * @param d2
   *          the second double
   * @return their comparison ratio, always a positive number...the larger,
   *         the more <code>d1</code> and <code>d2</code> differ
   */
  public static final double collate(final double d1, final double d2) {
    int c;

    c = Double.compare(d1, d2);
    if (c == 0)
      return 0d;

    if (Double.isNaN(d1)) {
      if (Double.isNaN(d2))
        return 0d;
      return Double.NaN;
    } else if (Double.isNaN(d2))
      return Double.NaN;

    if (Double.isInfinite(d1) || Double.isInfinite(d2)) {
      // return ((c < 0) ? Double.NEGATIVE_INFINITY
      // : Double.POSITIVE_INFINITY);
      return Double.POSITIVE_INFINITY;
    }

    if (d1 == 0d) {
      return Math.abs(d2);
    }
    if (d2 == 0d)
      return Math.abs(d1);

    return (Math.abs(d1 - d2) / Math.min(Math.abs(d1), Math.abs(d2)));
  }

  /**
   * Compare two doubles and return whether they are approximately equal.
   * 
   * @param d1
   *          the first double
   * @param d2
   *          the second double
   * @return their comparison ratio
   */
  public static final boolean approximatelyEqual(final double d1,
      final double d2) {
    return (collate(d1, d2) < EPS);
  }

  /**
   * Compute the logarithmus dualis of an (unsigned) integer value.
   * 
   * @param val
   *          the value
   * @return the logarithmus dualis of the (unsigned) integer
   *         <code>val</code>
   */
  public static final int ld(final int val) {
    int v, i;

    for (v = val, i = 0; v != 0; i++, v >>>= 1) {
      //
    }
    return i;
  }

  /**
   * the factorials
   */
  private static final int[] FACTORIALS = new int[] {//
  1,//
      1,//
      2,//
      6,//
      24,//
      120,//
      720,//
      5040,//
      40320,//
      362880,//
      3628800,//
      39916800,//
      479001600,//    
  };

  /**
   * compute the factorial of a number i
   * 
   * @param i
   *          the number
   * @return the factorial of <code>i</Coe
   */
  public static final int factorial(final int i) {
    if (i < 0)
      return 0;
    if (i <= 12)
      return FACTORIALS[i];
    return Integer.MAX_VALUE;
  }

  /**
   * compute the binomial coefficient n over k
   * 
   * @param n
   *          n
   * @param k
   *          k
   * @return n over k
   */
  public static final int binomial(final int n, final int k) {
    int s, i, os;

    if ((k < 0) || (k > n) || (n < 0))
      return 0;
    if (n <= 12)
      return ((FACTORIALS[n] / FACTORIALS[k]) / FACTORIALS[n - k]);

    s = 1;
    os = 1;
    for (i = (k + 1); i <= n; i++) {
      s *= i;
      if (s < os)
        return Integer.MAX_VALUE;
      os = s;
    }
    for (i = k; i > 1; i--) {
      s /= i;
    }
    return s;
  }

  /**
   * rounds r to decimals decimals
   * 
   * @param r
   *          The number to round.
   * @param decimals
   *          The count of decimals to round r to. If negative, maybe -1, r
   *          would be rounded to full decades.
   * @return r rounded to decimals decimals.
   */
  public static final double round(double r, int decimals) {
    if (r == 0)
      return r;

    double z = Math.rint(Math.log10(r));

    if (z < 0)
      z = decimals - z - 1;
    else
      z = decimals;

    if (z < 0.0)
      z = 1.0 / Math.rint(Math.pow(Math.rint(-z), 10d));
    else
      z = Math.rint(Math.pow(Math.rint(z), 10d));

    return Math.rint(r * z) / z;
  }

  /**
   * Ensure that a number is anything but zero.
   * 
   * @param val
   *          the value
   * @return a non-zero value.
   */
  public static final double notZero(final double val) {
    if (Math.abs(val) < EPS) {
      if (val < 0)
        return -EPS;
      return EPS;
    }
    return val;
  }

  /**
   * Obtain a list of all constants, including pi and e
   * 
   * @return the array of all mathematical constants
   */
  public static final double[] listConstants() {
    return listConstants(double.class, new String[] { "EPS" }); //$NON-NLS-1$
    // Field[] ff;
    // Field f;
    // int i, s, m;
    // double[] d;
    //
    //
    // ff = Mathematics.class.getDeclaredFields();
    // s = (ff.length - 1);
    // main: for (i = s; i >= 0; i--) {
    // f = ff[i];
    // if (f.getType() == double.class) {
    // m = f.getModifiers();
    // if (Modifier.isStatic(m) && Modifier.isFinal(m)) {
    // if (!("eps".equalsIgnoreCase(f.getName()))) { //$NON-NLS-1$
    // continue main;
    // }
    //
    // }
    // }
    // ff[i] = ff[--s];
    // }
    //
    // d = new double[s + 2];
    // d[s] = Math.E;
    // d[s + 1] = Math.PI;
    //
    // for (--s; s >= 0; s--) {
    // try {
    // d[s] = ff[s].getDouble(null);
    // } catch (Throwable t) {
    // d[s] = 0d;
    // }
    // }
    //
    // return d;
  }

  /**
   * the forbidden constructor
   */
  private Mathematics() {
    ErrorUtils.doNotCall();
  }
}
