/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-22
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.utils.HashUtils.java
 * Last modification: 2006-11-22
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
 * This class contains standard helper methods to create proper hash codes.
 */
public final class HashUtils {
  /**
   * The internal constructor prohibits object instantiation.
   */
  private HashUtils() {
    ErrorUtils.doNotCall();
  }

  /**
   * Create a hash code of an integer value.
   * 
   * @param value
   *          The integer value to create a hash code off.
   * @return The hash code of the integer value.
   */
  public static final int intHashCode(final int value) {
    int v;

    v = (value + 0xad4497fc);
    v += ~(v << 9);
    v ^= (v >>> 14);
    v += (v << 4);
    v ^= (v >>> 10);

    return v;
  }

  /**
   * Obtain the hashcode of a random object. This method does not care if
   * the object provided is an array or not.
   * 
   * @param object
   *          The object to obtain the hash code off.
   * @return The hash code of the object.
   * @see #objectHashCode2(Object)
   */
  public static final int objectHashCode(final Object object) {
    if (object == null)
      return 0xa37f4372;
    return intHashCode(object.hashCode());
  }

  /**
   * Create a hash code of a short value.
   * 
   * @param value
   *          The short value to create a hash code off.
   * @return The hash code of the short value.
   */
  public static final int shortHashCode(final short value) {
    return intHashCode(value ^ (value << 15));
  }

  /**
   * Create a hash code of a byte value.
   * 
   * @param value
   *          The byte value to create a hash code off.
   * @return The hash code of the byte value.
   */
  public static final int byteHashCode(final byte value) {
    return intHashCode(value ^ (value << 17));
  }

  /**
   * Create a hash code of a character value.
   * 
   * @param value
   *          The character value to create a hash code off.
   * @return The hash code of the character value.
   */
  public static final int charHashCode(final char value) {
    return intHashCode(value ^ (value << 21));
  }

  /**
   * Create a hash code of a boolean value.
   * 
   * @param value
   *          The boolean value to create a hash code off.
   * @return The hash code of the boolean value.
   */
  public static final int booleanHashCode(final boolean value) {
    return (value ? 1231 : 1237);
  }

  /**
   * Create a hash code of a long value.
   * 
   * @param value
   *          The long value to create a hash code off.
   * @return The hash code of the long value.
   */
  public static final int longHashCode(final long value) {
    return intHashCode((int) (value ^ (value >>> 32)));
  }

  /**
   * Create a hash code of a float value.
   * 
   * @param value
   *          The float value to create a hash code off.
   * @return The hash code of the float value.
   */
  public static final int floatHashCode(final float value) {
    return intHashCode(Float.floatToIntBits(value));
  }

  /**
   * Create a hash code of a double value.
   * 
   * @param value
   *          The double value to create a hash code off.
   * @return The hash code of the double value.
   */
  public static final int doubleHashCode(final double value) {
    return longHashCode(Double.doubleToLongBits(value));
  }

  /**
   * Combine two hash codes to obtain a third one representing both.
   * 
   * @param pHashCode_1
   *          The first hash code.
   * @param pHashCode_2
   *          The second hash code.
   * @return A hash code representing both hash codes <strong>and</strong>
   *         their sequence.
   */
  public static final int combineHashCodes(final int pHashCode_1,
      final int pHashCode_2) {
    return intHashCode(intHashCode(pHashCode_1) - pHashCode_2);
  }

  /**
   * Obtain the hashcode of a byte array.
   * 
   * @param array
   *          The array to obtain the hash code off.
   * @return The hash code of the array.
   */
  public static final int byteArrayHashCode(final byte[] array) {
//    int i, s;
//
//    if (array == null)
//      return -3413;
//    i = array.length;
//    if (i <= 0)
//      return -928547;
//
//    s = byteHashCode(array[0]);
//    for (--i; i > 0; i--) {
//      s = combineHashCodes(s, array[i]);
//    }
//
//    return s;
    return Arrays.hashCode(array);
  }

  /**
   * Obtain the hashcode of a short array.
   * 
   * @param array
   *          The array to obtain the hash code off.
   * @return The hash code of the array.
   */
  public static final int shortArrayHashCode(final short[] array) {
//    int i, s;
//
//    if (array == null)
//      return 0xC43413;
//    i = array.length;
//    if (i <= 0)
//      return 0x3148547;
//
//    s = shortHashCode(array[0]);
//    for (--i; i > 0; i--) {
//      s = combineHashCodes(s, array[i]);
//    }
//
//    return s;
    return Arrays.hashCode(array);
  }

  /**
   * Obtain the hashcode of a int array.
   * 
   * @param array
   *          The array to obtain the hash code off.
   * @return The hash code of the array.
   */
  public static final int intArrayHashCode(final int[] array) {
//    int i, s;
//
//    if (array == null)
//      return 0xCF34222;
//    i = array.length;
//    if (i <= 0)
//      return 0xAA0331;
//
//    s = intHashCode(array[0]);
//    for (--i; i > 0; i--) {
//      s = combineHashCodes(s, array[i]);
//    }
//
//    return s;
    return Arrays.hashCode(array);
  }

  /**
   * Obtain the hashcode of a long array.
   * 
   * @param array
   *          The array to obtain the hash code off.
   * @return The hash code of the array.
   */
  public static final int longArrayHashCode(final long[] array) {
//    int i, s;
//
//    if (array == null)
//      return -344;
//    i = array.length;
//    if (i <= 0)
//      return 234532542;
//
//    s = longHashCode(array[0]);
//    for (--i; i > 0; i--) {
//      s = combineHashCodes(s, longHashCode(array[i]));
//    }
//
//    return s;
    return Arrays.hashCode(array);
  }

  /**
   * Obtain the hashcode of a float array.
   * 
   * @param array
   *          The array to obtain the hash code off.
   * @return The hash code of the array.
   */
  public static final int floatArrayHashCode(final float[] array) {
//    int i, s;
//
//    if (array == null)
//      return 6654734;
//    i = array.length;
//    if (i <= 0)
//      return 447811;
//
//    s = floatHashCode(array[0]);
//    for (--i; i > 0; i--) {
//      s = combineHashCodes(s, floatHashCode(array[i]));
//    }
//
//    return s;
    return Arrays.hashCode(array);
  }

  /**
   * Obtain the hashcode of a double array.
   * 
   * @param array
   *          The array to obtain the hash code off.
   * @return The hash code of the array.
   */
  public static final int doubleArrayHashCode(final double[] array) {
//    int i, s;
//
//    if (array == null)
//      return 34;
//    i = array.length;
//    if (i <= 0)
//      return -278812;
//
//    s = doubleHashCode(array[0]);
//    for (--i; i > 0; i--) {
//      s = combineHashCodes(s, doubleHashCode(array[i]));
//    }
//
//    return s;
    return Arrays.hashCode(array);
  }

  /**
   * Obtain the hashcode of a char array.
   * 
   * @param array
   *          The array to obtain the hash code off.
   * @return The hash code of the array.
   */
  public static final int charArrayHashCode(final char[] array) {
//    int i, s;
//
//    if (array == null)
//      return -3294781;
//    i = array.length;
//    if (i <= 0)
//      return 0xACFDDD;
//
//    s = charHashCode(array[0]);
//    for (--i; i > 0; i--) {
//      s = combineHashCodes(s, array[i]);
//    }
//
//    return s;
    return Arrays.hashCode(array);
  }

  /**
   * Obtain the hashcode of a boolean array.
   * 
   * @param array
   *          The array to obtain the hash code off.
   * @return The hash code of the array.
   */
  public static final int booleanArrayHashCode(final boolean[] array) {
    // int i, s;
    //
    // if (array == null)
    // return 3891;
    // i = array.length;
    // if (i <= 0)
    // return 0x221;
    //
    // s = booleanHashCode(array[0]);
    // for (--i; i > 0; i--) {
    // s = combineHashCodes(s, array[i] ? 92 : -2837);
    // }
    //   
    // return s;
    //    
    return Arrays.hashCode(array);
  }

  /**
   * Obtain the hashcode of an object array.
   * 
   * @param array
   *          The array to obtain the hash code off.
   * @return The hash code of the array.
   */
  public static final int objectArrayHashCode(final Object[] array) {
    // int i, s;
    //
    // if (array == null)
    // return 234723874;
    // i = array.length;
    // if (i <= 0)
    // return -832476;
    //
    // s = objectHashCode(array[0]);
    // for (--i; i > 0; i--) {
    // s = combineHashCodes(s, objectHashCode(array[i]));
    // }
    //
    // return s;
    return Arrays.hashCode(array);
  }

  /**
   * Obtain the hashcode of a random object. If this object is an array, it
   * will be treated accordingly.
   * 
   * @param object
   *          The object to obtain the hash code off.
   * @return The hash code of the object.
   * @see #objectHashCode(Object)
   */
  public static final int objectHashCode2(final Object object) {
    Class<?> c;

    if (object != null) {
      c = object.getClass();
      if (c.isArray()) {
        if (c == byte[].class)
          return intHashCode(byteArrayHashCode((byte[]) object) - 1);

        if (c == short[].class)
          return intHashCode(shortArrayHashCode((short[]) object) - 2);

        if (c == int[].class)
          return intHashCode(intArrayHashCode((int[]) object) - 3);

        if (c == long[].class)
          return intHashCode(longArrayHashCode((long[]) object) - 4);

        if (c == boolean[].class)
          return intHashCode(booleanArrayHashCode((boolean[]) object) - 5);

        if (c == float[].class)
          return intHashCode(floatArrayHashCode((float[]) object) - 6);

        if (c == double[].class)
          return intHashCode(doubleArrayHashCode((double[]) object) - 7);

        if (c == char[].class)
          return intHashCode(charArrayHashCode((char[]) object) - 8);

        return combineHashCodes(objectArrayHashCode((Object[]) object), c
            .hashCode());
      }
    }

    return objectHashCode(object);
  }
}
