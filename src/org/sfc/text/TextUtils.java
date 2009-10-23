/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 *
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-12-21
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.text.TextUtils.java
 * Last modification: 2007-08-03
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

package org.sfc.text;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.sfc.utils.ErrorUtils;
import org.sfc.xml.XML;

/**
 * some utils concerning text and strings
 * 
 * @author Thomas Weise
 */
public final class TextUtils {

  /**
   * the windows-type linebreak cr-lf
   */
  public final static String CRLF = "\r\n"; //$NON-NLS-1$

  /**
   * The newline string.
   */
  public static final String LINE_SEPARATOR = System
      .getProperty("line.separator"); //$NON-NLS-1$

  /**
   * the default encoding.
   */
  public static final String DEFAULT_ENCODING = XML.PREFERED_ENCODING;

  /**
   * The line break characters.
   */
  private static final char[] LINE_SEPARATOR_CHARS = LINE_SEPARATOR
      .toCharArray();

  /**
   * The csv separator characters.
   */
  private static final char[] CSV_SEPARATOR_CHARS = { '\t' };

  /**
   * The csv separating string.
   */
  public static final String CSV_SEPARATOR = String
      .valueOf(CSV_SEPARATOR_CHARS);

  /**
   * The maximum length of a positive long string.
   */
  private static final int MAX_LONG_STR_LEN = Long
      .toString(Long.MAX_VALUE).length();

  /**
   * The ISO 8601 date format.
   */
  public static final DateFormat ISO_8601 = new SimpleDateFormat(
      "yyyy-MM-dd'T'HH:mm:ss.SZ"); //$NON-NLS-1$

  /**
   * The internal space buffer.
   */
  private static char[] s_spaces = new char[] { ' ', ' ', ' ', ' ', ' ',
      ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
      ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' };

  /**
   * the space synchronizer
   */
  private static final Object SPACE_SYNC = new Object();

  /**
   * the forbidden constructor
   */
  private TextUtils() {
    ErrorUtils.doNotCall();
  }

  /**
   * Checks wether a string contains a line break or not.
   * 
   * @param string
   *          The string potentially containing a line break.
   * @return <code>true</code> if the string containes something like a
   *         linebreak, <code>false</code> otherwise.
   */
  public static final boolean containsLineSeparator(final String string) {
    int i;

    for (i = (LINE_SEPARATOR_CHARS.length - 1); i >= 0; i--) {
      if (string.indexOf(LINE_SEPARATOR_CHARS[i]) >= 0)
        return true;
    }

    return false;
  }

  /**
   * This method trims a string and returns <code>null</code> if it
   * contains no characters (or was even <code>null</code>).
   * 
   * @param string
   *          The string to preprocess.
   * @return The trimmed string, or <code>null</code> if it is empty.
   */
  public static final String preprocessString(final String string) {
    String s;
    if (string != null) {
      if ((s = string.trim()).length() > 0)
        return s;
    }

    return null;
  }

  /**
   * Append the time stored in <code>gc</code> to the given appendable.
   * The SFC-time absolute format is <code>yyyy-mm-dd-hh:mm:ss_µµµ</code>,
   * the relative time format is <code>dd-hh:mm:ss_µµµ</code>.
   * 
   * @param gc
   *          The calendar.
   * @param relative
   *          <code>true</code> if the time expression is relative.
   * @param app
   *          The string builder to write to.
   */
  public static final void appendTime(final Calendar gc,
      final boolean relative, final StringBuilder app) {
    int x, y;
    long ms;

    if (relative) {
      y = (gc.get(Calendar.DAY_OF_YEAR) - 1);
      if (y < 100)
        app.append('0');
      if (y < 10)
        app.append('0');
      app.append(y);
      app.append('-');
    } else {
      y = 1;
      app.append(gc.get(Calendar.YEAR));
      app.append('-');

      x = (gc.get(Calendar.MONTH) + 1);
      if (x < 10)
        app.append('0');
      app.append(x);
      app.append('-');

      x = gc.get(Calendar.DAY_OF_MONTH);
      if (x < 10)
        app.append('0');
      app.append(x);
      app.append('-');
    }

    x = gc.get(Calendar.HOUR_OF_DAY);
    if (y <= 0)
      x--;
    if (x < 10)
      app.append('0');
    app.append(x);
    app.append(':');

    x = gc.get(Calendar.MINUTE);
    if (x < 10)
      app.append('0');
    app.append(x);
    app.append(':');

    x = gc.get(Calendar.SECOND);
    if (x < 10)
      app.append('0');
    app.append(x);

    ms = gc.get(Calendar.MILLISECOND);
    app.append('.');
    if (ms < 100)
      app.append('0');
    if (ms < 10)
      app.append('0');
    app.append(ms);
  }

  /**
   * Create a string representation of a time expression. The SFC-time
   * absolute format is <code>yyyy-mm-dd-hh:mm:ss_µµµ</code>, the
   * relative time format is <code>dd-hh:mm:ss_µµµ</code>.
   * 
   * @param gc
   *          The calendar.
   * @param relative
   *          <code>true</code> if the time expression is relative.
   * @return The time string.
   */
  public static final String timeToString(final Calendar gc,
      final boolean relative) {
    StringBuilder sb;

    sb = new StringBuilder();
    appendTime(gc, relative, sb);
    return sb.toString();
  }

  /**
   * Append the data stored in <code>gc</code> to the given appendable.
   * The SFC-data absolute format is <code>yyyy-mm-dd</code>, the
   * relative data format is <code>dd</code>.
   * 
   * @param gc
   *          The calendar.
   * @param relative
   *          <code>true</code> if the date expression is relative.
   * @param app
   *          The string builder to write to.
   */
  public static final void appendDate(final Calendar gc,
      final boolean relative, final StringBuilder app) {
    int x, y;

    if (relative) {
      y = (gc.get(Calendar.DAY_OF_YEAR) - 1);
      if (y < 100)
        app.append('0');
      if (y < 10)
        app.append('0');
      app.append(y);
      app.append('-');
    } else {
      y = 1;
      app.append(gc.get(Calendar.YEAR));
      app.append('-');

      x = (gc.get(Calendar.MONTH) + 1);
      if (x < 10)
        app.append('0');
      app.append(x);
      app.append('-');

      x = gc.get(Calendar.DAY_OF_MONTH);
      if (x < 10)
        app.append('0');
      app.append(x);
      app.append('-');
    }
  }

  /**
   * Create a string representation of a date expression. The SFC-date
   * absolute format is <code>yyyy-mm-dd</code>, the relative date
   * format is <code>dd</code>.
   * 
   * @param gc
   *          The calendar.
   * @param relative
   *          <code>true</code> if the date expression is relative.
   * @return The time string.
   */
  public static final String dateToString(final Calendar gc,
      final boolean relative) {
    StringBuilder sb;

    sb = new StringBuilder();
    appendTime(gc, relative, sb);
    return sb.toString();
  }

  /**
   * Append the time stored in <code>gc</code> to the given appendable.
   * The SFC-time absolute format is <code>yyyy-mm-dd-hh:mm:ss_µµµ</code>,
   * the relative time format is <code>dd-hh:mm:ss_µµµ</code>.
   * 
   * @param time
   *          The time constant. This is a value of the type
   *          <code>long</code>, for example obtained by
   *          <code>System.currentMillies</code>.
   * @param relative
   *          <code>true</code> if the time expression is relative.
   * @param app
   *          The string builder to write to.
   */
  public static final void appendTime(final long time,
      final boolean relative, final StringBuilder app) {
    GregorianCalendar gc;

    gc = new GregorianCalendar();
    gc.setTimeInMillis(time);
    appendTime(gc, relative, app);
  }

  /**
   * Append the date stored in <code>gc</code> to the given appendable.
   * The SFC-date absolute format is <code>yyyy-mm-dd</code>, the
   * relative time format is <code>dd</code>.
   * 
   * @param date
   *          The date constant. This is a value of the type
   *          <code>long</code>, for example obtained by
   *          <code>System.currentMillies</code>.
   * @param relative
   *          <code>true</code> if the time expression is relative.
   * @param app
   *          The string builder to write to.
   */
  public static final void appendDate(final long date,
      final boolean relative, final StringBuilder app) {
    GregorianCalendar gc;

    gc = new GregorianCalendar();
    gc.setTimeInMillis(date);
    appendDate(gc, relative, app);
  }

  /**
   * Create a string representation of a time expression. The SFC-time
   * absolute format is <code>yyyy-mm-dd-hh:mm:ss_µµµ</code>, the
   * relative time format is <code>dd-hh:mm:ss_µµµ</code>.
   * 
   * @param time
   *          The time constant. This is a value of the type
   *          <code>long</code>, for example obtained by
   *          <code>System.currentMillies</code>.
   * @param relative
   *          <code>true</code> if the time expression is relative.
   * @return The time string.
   * @see System#currentTimeMillis()
   */
  public static final String timeToString(final long time,
      final boolean relative) {
    StringBuilder sb;

    sb = new StringBuilder();
    appendTime(time, relative, sb);
    return sb.toString();
  }

  /**
   * Create a string representation of a time expression. The SFC-time
   * absolute format is <code>yyyy-mm-dd</code>, the relative time
   * format is <code>dd</code>.
   * 
   * @param date
   *          The date constant. This is a value of the type
   *          <code>long</code>, for example obtained by
   *          <code>System.currentMillies</code>.
   * @param relative
   *          <code>true</code> if the time expression is relative.
   * @return The time string.
   * @see System#currentTimeMillis()
   */
  public static final String dateToString(final long date,
      final boolean relative) {
    StringBuilder sb;

    sb = new StringBuilder();
    appendDate(date, relative, sb);
    return sb.toString();
  }

  /**
   * Append the data of an error to a string builder.
   * 
   * @param t
   *          The error to convert.
   * @param sb
   *          The string builder to add all the error's information to.
   */
  public static final void appendError(final Throwable t,
      final StringBuilder sb) {
    String s;
    StackTraceElement[] st;
    StackTraceElement ste;
    int i;
    Throwable q;

    q = t;
    while (q != null) {
      sb.append(q.getClass().getName());
      sb.append(": "); //$NON-NLS-1$
      s = preprocessString(q.getLocalizedMessage());
      if (s == null)
        s = preprocessString(q.getMessage());
      if (s != null)
        sb.append(s);

      st = q.getStackTrace();
      if (st != null) {
        for (i = 0; i < st.length; i++) {
          ste = st[i];
          if (ste != null) {
            sb.append(LINE_SEPARATOR_CHARS);

            sb.append("     at ");//$NON-NLS-1$
            if (ste.isNativeMethod())
              sb.append("<native>");//$NON-NLS-1$

            s = preprocessString(ste.getClassName());
            if (s != null)
              sb.append(s);

            s = preprocessString(ste.getMethodName());
            if (s != null) {
              if (sb.charAt(sb.length() - 1) != '.')
                sb.append('.');
              sb.append(s);
            }

            s = preprocessString(ste.getFileName());
            if (s != null) {
              sb.append('(');
              sb.append(s);
              if (sb.charAt(sb.length() - 1) != ':')
                sb.append(':');
              sb.append(ste.getLineNumber());
              sb.append(')');
            }

          }
        }
      }

      q = q.getCause();
      if (q != null) {
        sb.append(LINE_SEPARATOR_CHARS);
        sb.append("caused by:");//$NON-NLS-1$
        sb.append(LINE_SEPARATOR_CHARS);
      }
    }
  }

  /**
   * Converts an error to a string.
   * 
   * @param t
   *          The error to convert.
   * @return A String representing all the error's information.
   */
  public static final String errorToString(final Throwable t) {
    StringBuilder sb;

    sb = new StringBuilder();
    appendError(t, sb);

    return sb.toString();
  }

  /**
   * Convert a double quickly to a string in a locale-depending way.
   * 
   * @param pdouble
   *          The double to be converted.
   * @return The string representation of this double.
   */
  public static final String doubleToLocalString(final double pdouble) {
    if (Double.isInfinite(pdouble) || Double.isNaN(pdouble))
      return Double.toString(pdouble);
    return NumberFormat.getNumberInstance().format(pdouble);
  }

  /**
   * Convert a string to a sequence of bytes.
   * 
   * @param string
   *          The string data.
   * @param encoding
   *          The encoding to be used. If this parameter is
   *          <code>null</code>, a default encoding sheme will be used.
   * @return A sequence of bytes representing the string data.
   */
  public static final byte[] stringToBytes(final String string,
      final String encoding) {
    ByteArrayOutputStream baos;
    OutputStreamWriter osw;
    byte[] b;

    b = null;
    baos = new ByteArrayOutputStream();
    try {
      try {
        osw = ((encoding != null) ? new OutputStreamWriter(baos)
            : new OutputStreamWriter(baos, encoding));

        try {
          osw.write(string);
        } finally {
          osw.close();
        }
      } finally {
        baos.close();
      }
      b = baos.toByteArray();
    } catch (Throwable t) {
      ErrorUtils.onError(t);
    }

    return b;
  }

  /**
   * Appeng the string representation of a given object to a string
   * builder.
   * 
   * @param o
   *          the object
   * @param sb
   *          the string builder
   * @param rec
   *          the recursion depth
   */
  private static final void append(final Object o, final StringBuilder sb,
      final int rec) {
    Class<?> c;
    int i, l;

    if (o == null)
      sb.append(o);
    else {
      // new
      if (o instanceof ITextable)
        ((ITextable) o).toStringBuilder(sb);
      else
      // new
      if (o instanceof Throwable) {
        appendError((Throwable) o, sb);
      } else {
        c = o.getClass();
        if (c.isArray()) {

          if (rec <= 0) {
            appendClass(c, sb);
            sb.append(' ');
          }

          sb.append('{');
          l = Array.getLength(o);
          for (i = 0; i < l; i++) {
            if (i > 0) {
              sb.append(',');
              sb.append(' ');
            }
            append(Array.get(o, i), sb, rec + 1);
          }
          sb.append('}');

        } else
          sb.append(o);
      }
    }
  }

  /**
   * Appeng the string representation of a given object to a string
   * builder.
   * 
   * @param o
   *          the object
   * @param sb
   *          the string builder
   */
  public static final void append(final Object o, final StringBuilder sb) {
    append(o, sb, 0);
  }

  /**
   * Append a bit string to a given string builder in a <code>0-1</code>
   * form of representation.
   * 
   * @param bits
   *          the bit string
   * @param sb
   *          the string builder
   */
  public static final void appendBitString(final boolean[] bits,
      final StringBuilder sb) {
    int i, l;

    if (bits != null) {
      l = bits.length;
      for (i = 0; i < l; i++) {
        sb.append(bits[i] ? '1' : '0');
      }
    }
  }

  /**
   * Obtain a bit string in a <code>0-1</code> form of representation.
   * 
   * @param bits
   *          the bit string
   * @return the string representation
   */
  public static final String toBitString(final boolean[] bits) {
    int i, l;
    StringBuilder sb;

    if ((bits != null) && ((l = bits.length) > 0)) {
      l = bits.length;
      sb = new StringBuilder(l);
      for (i = 0; i < l; i++) {
        sb.append(bits[i] ? '1' : '0');
      }
      return sb.toString();
    }
    return ""; //$NON-NLS-1$
  }

  /**
   * Obtain the string representation of a given object.
   * 
   * @param o
   *          the object
   * @return the string representation of a given object
   */
  public static final String toString(final Object o) {
    StringBuilder sb;

    sb = new StringBuilder();
    append(o, sb, 0);
    return sb.toString();
  }

  /**
   * Convert a long to a string representation.
   * 
   * @param num
   *          the number
   * @return the string representing the number
   */
  public static final String longToString(final long num) {
    return longToString(num, MAX_LONG_STR_LEN);
  }

  /**
   * Convert a long to a string representation.
   * 
   * @param num
   *          the number
   * @param minLen
   *          the minimum length
   * @return the string representing the number
   */
  public static final String longToString(final long num, final int minLen) {
    StringBuilder sb;

    sb = new StringBuilder();
    appendLong(num, minLen, sb);
    return sb.toString();
  }

  /**
   * Convert a long to a string representation.
   * 
   * @param num
   *          the number
   * @param minLen
   *          the minimum length
   * @param sb
   *          the string builder to append to
   */
  public static final void appendLong(final long num, final int minLen,
      final StringBuilder sb) {
    int l, ml, y;
    long x;

    y = sb.length();

    if (num < 0)
      x = -num;
    else
      x = num;

    sb.append(x);
    ml = Math.max(0, Math.min(MAX_LONG_STR_LEN, minLen)) + y;
    for (l = sb.length(); l <= ml; l++) {
      sb.insert(y, '0');
    }

    if (num < 0)
      sb.insert(y, '-');
  }

  /**
   * Obtain the string representation of the class
   * 
   * @param clazz
   *          the class
   * @return the string representation of the class
   */
  public static final String classToString(final Class<?> clazz) {
    StringBuilder sb;
    sb = new StringBuilder();
    appendClass(clazz, sb);
    return sb.toString();
  }

  /**
   * Append the string representation of the class to a string builder
   * 
   * @param clazz
   *          the class
   * @param sb
   *          the string builder to append to
   */
  public static final void appendClass(final Class<?> clazz,
      final StringBuilder sb) {
    String n;
    Class<?> d;
    int i;
    Object o;

    if (clazz == null) {
      sb.append((String) null);
      return;
    }

    d = clazz.getEnclosingClass();
    if (d != null) {
      appendClass(d, sb);
    }
    o = clazz.getEnclosingMethod();
    if (o == null)
      o = clazz.getEnclosingConstructor();
    if (o != null) {
      if (d != null)
        sb.append('#');
      sb.append(o);
    }
    if ((o != null) || (d != null)) {
      sb.append(':');
      o = sb;
    }

    if (clazz.isArray()) {

      i = 0;
      d = clazz;
      while (d.isArray()) {
        i++;
        d = d.getComponentType();
      }

      sb.append(classToString(d));
      for (; i > 0; i--) {
        sb.append('[');
        sb.append(']');
      }

      return;
    }

    n = clazz.getCanonicalName();
    if (n == null) {
      n = clazz.getName();
      if (n == null) {
        n = clazz.getSimpleName();
      }
    }
    if (n != null) {
      sb.append(n);
      return;
    }

    if (o != null) {
      sb.append('{');
      sb.append('}');
      return;
    }

    sb.append((String) null);
  }

  /**
   * Appends the ISO 8601-formatted duration to the string builder.
   * 
   * @param time
   *          the time duration (in milliseconds)
   * @param sb
   *          the string builder to append to
   * @see System#currentTimeMillis()
   */
  public static final void appendRelativeIsoTime(final long time,
      final StringBuilder sb) {

    long duration, milliseconds, seconds, minutes, hours;
    boolean x;

    duration = time;
    milliseconds = (time % 1000);
    duration /= 1000;
    seconds = (duration % 60);
    duration /= 60;
    minutes = (duration % 60);
    duration /= 60;
    hours = duration;

    x = false;
    sb.append('P');
    if (hours != 0) {
      x = true;
      sb.append(hours);
      sb.append('H');
    }
    if (x || (minutes != 0)) {
      sb.append(minutes);
      sb.append('M');
    }

    sb.append(seconds);
    if (milliseconds != 0) {
      sb.append('.');
      sb.append(milliseconds);
    }
    sb.append('S');
  }

  /**
   * Obtain the internal array with the given count of spaces. The return
   * value must not be modified.
   * 
   * @param count
   *          the required count of spaces
   * @return the space array
   */
  public static final char[] getSpaces(final int count) {
    char[] s;
    int i, j;
    s = s_spaces;
    if (s.length < count) {
      synchronized (SPACE_SYNC) {
        s = s_spaces;
        if (s.length < count) {
          j = (count << 1);
          s = new char[j];
          i = s_spaces.length;
          for (;;) {
            j -= i;
            if (j < 0) {
              i += j;
              if (i == 0)
                break;
              j = 0;
            }
            System.arraycopy(s_spaces, 0, s, j, i);
          }
        }
        s_spaces = s;
      }
    }

    return s;
  }

  /**
   * Append the given count of spaces to the string builder.
   * 
   * @param sb
   *          the string builder
   * @param count
   *          the count of spaces to append
   */
  public static final void appendSpaces(final StringBuilder sb,
      final int count) {
    sb.append(getSpaces(count), 0, count);
  }

  /**
   * Append a static member to a string builder
   * 
   * @param sb
   *          the string builder
   * @param m
   *          the constant member
   */
  public static final void appendStaticField(final Field m,
      final StringBuilder sb) {
    sb.append(m.getDeclaringClass().getCanonicalName());
    sb.append('.');
    sb.append(m.getName());
  }

  /**
   * Code shared by String and StringBuffer to do searches. The hayStack is
   * the character array being searched, and the needle is the string being
   * searched for.
   * 
   * @param hayStack
   *          the characters being searched.
   * @param hayStackOffset
   *          offset of the hayStack string.
   * @param hayStackCount
   *          count of the hayStack string.
   * @param needle
   *          the characters being searched for.
   * @param needleOffset
   *          offset of the needle string.
   * @param needleCount
   *          count of the needle string.
   * @param fromIndex
   *          the index to begin searching from.
   * @return the position of <code>needle</code> in <code>hayStack</code>,
   *         or <code>-1</code> if not found
   */
  public static final int indexOf(final char[] hayStack,
      final int hayStackOffset, final int hayStackCount,
      final char[] needle, final int needleOffset, final int needleCount,
      final int fromIndex) {

    char first;
    int i, max, j, k, end, fi;

    if (fromIndex >= hayStackCount) {
      return (needleCount == 0 ? hayStackCount : -1);
    }
    if (fromIndex < 0) {
      fi = 0;
    } else
      fi = fromIndex;
    if (needleCount == 0) {
      return fi;
    }

    first = needle[needleOffset];
    max = hayStackOffset + (hayStackCount - needleCount);

    for (i = (hayStackOffset + fi); i <= max; i++) {
      /* Look for first character. */
      if (hayStack[i] != first) {
        while (++i <= max && hayStack[i] != first) {
          //
        }

      }

      /* Found first character, now look at the rest of v2 */
      if (i <= max) {
        j = i + 1;
        end = j + needleCount - 1;
        for (k = needleOffset + 1; (j < end) && (hayStack[j] == needle[k]); j++, k++) {
          //
        }

        if (j == end) {
          /* Found whole string. */
          return i - hayStackOffset;
        }
      }
    }
    return -1;
  }

  /**
   * Code shared by String and StringBuffer to do searches. The hayStack is
   * the character array being searched, and the needle is the string being
   * searched for.
   * 
   * @param hayStack
   *          the characters being searched.
   * @param hayStackOffset
   *          offset of the hayStack string.
   * @param hayStackCount
   *          count of the hayStack string.
   * @param needle
   *          the character being searched for.
   * @param fromIndex
   *          the index to begin searching from.
   * @return the position of <code>needle</code> in <code>hayStack</code>,
   *         or <code>-1</code> if not found
   */
  public static final int indexOf(final char[] hayStack,
      final int hayStackOffset, final int hayStackCount,
      final char needle, final int fromIndex) {
    final int e;
    int s;

    e = (hayStackOffset + hayStackCount);
    for (s = fromIndex; s < e; s++) {
      if (hayStack[s] == needle)
        return s;
    }
    return -1;
  }
}
