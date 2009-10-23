/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-12-21
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.utils.Utils.java
 * Last modification: 2006-12-21
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

package org.sfc.utils;

import java.util.Arrays;

/**
 * A class for unclassified utilities.
 * 
 * @author Thomas Weise
 */
public final class Utils {

  /**
   * Check two objects for equality.
   * 
   * @param o1
   *          The first object.
   * @param o2
   *          The second object.
   * @return <code>true</code> if and only if the two objects are equal.
   */
  public static final boolean testEqual(final Object o1, final Object o2) {
    if (o1 == o2)
      return true;
    if ((o1 == null) || (o2 == null))
      return false;
    return o1.equals(o2);
  }

  /**
   * Check two objects for equality also considering array equality.
   * 
   * @param o1
   *          The first object.
   * @param o2
   *          The second object.
   * @return <code>true</code> if and only if the two objects are equal.
   */
  public static final boolean testEqualDeep(final Object o1,
      final Object o2) {
    Class<?> c1, c2;
    Object[] x1, x2;
    int i;

    if (o1 == o2)
      return true;
    if ((o1 == null) || (o2 == null))
      return false;

    if (o1.equals(o2))
      return true;

    c1 = o1.getClass();
    c2 = o2.getClass();
    if (c1 != c2)
      return false;
    if (!(c1.isArray()))
      return false;

    if (c1 == byte[].class)
      return Arrays.equals((byte[]) o1, (byte[]) o2);
    if (c1 == short[].class)
      return Arrays.equals((short[]) o1, (short[]) o2);
    if (c1 == int[].class)
      return Arrays.equals((int[]) o1, (int[]) o2);
    if (c1 == long[].class)
      return Arrays.equals((long[]) o1, (long[]) o2);
    if (c1 == float[].class)
      return testFloatArrayEqual((float[]) o1, (float[]) o2);
    if (c1 == double[].class)
      return testDoubleArrayEqual((double[]) o1, (double[]) o2);
    if (c1 == char[].class)
      return Arrays.equals((char[]) o1, (char[]) o2);
    if (c1 == boolean[].class)
      return Arrays.equals((boolean[]) o1, (boolean[]) o2);

    x1 = ((Object[]) o1);
    x2 = ((Object[]) o2);

    i = x1.length;
    if (i != x2.length)
      return false;

    for (--i; i >= 0; i--) {
      if (!testEqualDeep(x1[i], x2[i]))
        return false;
    }
    return true;
  }

  /**
   * You cannot instantiate this class.
   */
  private Utils() {
    ErrorUtils.doNotCall();
  }

  /**
   * Returns <tt>true</tt> if the two specified arrays of doubles are
   * <i>equal</i> to one another. Two arrays are considered equal if both
   * arrays contain the same number of elements, and all corresponding
   * pairs of elements in the two arrays are equal.
   * 
   * @param a
   *          one array to be tested for equality.
   * @param a2
   *          the other array to be tested for equality.
   * @return <tt>true</tt> if the two arrays are equal.
   */
  public static boolean testDoubleArrayEqual(final double[] a,
      final double[] a2) {
    int length;

    if (a == a2)
      return true;
    if ((a == null) || (a2 == null))
      return false;

    length = a.length;
    if (a2.length != length)
      return false;

    for (--length; length >= 0; length--)
      if (Double.compare(a[length], a2[length]) != 0)
        return false;

    return true;
  }

  /**
   * Returns <tt>true</tt> if the two specified arrays of floats are
   * <i>equal</i> to one another. Two arrays are considered equal if both
   * arrays contain the same number of elements, and all corresponding
   * pairs of elements in the two arrays are equal.
   * 
   * @param a
   *          one array to be tested for equality.
   * @param a2
   *          the other array to be tested for equality.
   * @return <tt>true</tt> if the two arrays are equal.
   */
  public static boolean testFloatArrayEqual(final float[] a,
      final float[] a2) {
    int length;

    if (a == a2)
      return true;
    if ((a == null) || (a2 == null))
      return false;

    length = a.length;
    if (a2.length != length)
      return false;

    for (--length; length >= 0; length--)
      if (Float.compare(a[length], a2[length]) != 0)
        return false;

    return true;
  }

  /**
   * A useful utility method that can be used on a regular basis in order
   * to invoke the garbage collector and to perform other system cleanup
   * tasks.
   */
  public static final void invokeGC() {
    int i;
    long l1, l2;
    Runtime r;

    r = Runtime.getRuntime();
    l1 = r.freeMemory();
    i = 10;

    do {
      r.gc();
      r.runFinalization();
      l2 = l1;
      l1 = r.freeMemory();
    } while (((--i) > 0) && (l1 != l2));
  }
}
