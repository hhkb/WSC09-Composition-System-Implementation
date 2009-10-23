/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-12-28
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.io.TextWriter.java
 * Last modification: 2007-10-21
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

package org.sfc.io;

import java.io.IOException;
import java.io.ObjectOutput;
import java.io.Writer;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.sfc.text.ITextable;
import org.sfc.text.TextUtils;

/**
 * A text writer can be used to write structured/typed data into an
 * internal buffer which then can be flushed to the output. All output is
 * piped through its {@link #write(char[], int, int)}-method. Furthermore,
 * if formatting is turned on {@link #formatOn()}, the input is piped from
 * {@link #write(char[], int, int)} to {@link #format(char[], int, int)},
 * which performs additional formatting, like indentation. Classes derived
 * from <code>TextWriter</code> may override this method in order to
 * perform formatting.
 * 
 * @author Thomas Weise
 */
public class TextWriter extends DefaultWriter implements ObjectOutput {
  /**
   * the serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the default newline sequence.
   */
  private static final char[] DEFAULT_NEWLINE = TextUtils.LINE_SEPARATOR
      .toCharArray();

  // new char[] { '\n' };

  // 

  /**
   * the character omit array
   */
  private static final boolean[] OMIT;

  static {
    OMIT = new boolean[32];
    Arrays.fill(OMIT, true);
    OMIT['\t'] = false;
  }

  /**
   * the default csv separator.
   */
  private static final char[] DEFAULT_CSV_SEPARATOR = TextUtils.CSV_SEPARATOR
      .toCharArray();

  // /**
  // * The internal indentation buffer.
  // */
  // private static char[] s_indent = new char[] { ' ', ' ', ' ', ' ', ' ',
  // ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
  // ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '
  // };

  /**
   * the buffer.
   */
  private transient char[] m_buffer;

  /**
   * the data.
   */
  private transient char[] m_data;

  /**
   * the current buffer length.
   */
  private int m_length;

  /**
   * a string builder used as buffer.
   */
  private transient StringBuilder m_buffer2;

  /**
   * <code>true</code> if indentation is required at the next output
   * operation.
   */
  private boolean m_isNewLine;

  /**
   * The number instance to be used.
   */
  private NumberFormat m_numberInstance;

  /**
   * <code>true</code> if and only if all numeric output should be
   * localized.
   */
  private boolean m_localizeNumbers;

  /**
   * the newline characters.
   */
  private final char[] m_newLine;

  /**
   * the csv separator
   */
  private char[] m_csvSeparator;

  /**
   * The calendar to be used internally.
   */
  private final Calendar m_calendar;

  /**
   * the last exception caught.
   */
  private IOException m_t;

  /**
   * the characters not to be omitted from the output.
   */
  private boolean[] m_omit;

  /**
   * The indentation count.
   */
  private int m_indent;

  /**
   * >= 0 as long as indentation will be performed.
   */
  private int m_isFormatting;

  /**
   * The indentation factor.
   */
  private int m_indentFactor;

  /**
   * Create a new text writer.
   * 
   * @param out
   *          The output stream to write to.
   * @param numberFormat
   *          the numberformat to be used to format numeric output
   * @param newLine
   *          the new-line character sequence
   * @param csvSeparator
   *          the csv separator
   * @param indentFactor
   *          the indent factor
   */
  protected TextWriter(final Writer out, final NumberFormat numberFormat,
      final char[] newLine, final char[] csvSeparator,
      final int indentFactor) {
    super(out);

    int i;
    char[] x;
    boolean[] b;

    this.m_buffer = new char[16];
    this.m_data = new char[16];
    this.m_buffer2 = new StringBuilder();
    this.m_isNewLine = true;
    this.m_numberInstance = ((numberFormat == null) ? NumberFormat
        .getNumberInstance() : numberFormat);
    this.m_newLine = ((newLine == null) ? DEFAULT_NEWLINE : newLine);
    this.m_csvSeparator = ((csvSeparator != null) ? csvSeparator
        : DEFAULT_CSV_SEPARATOR);
    this.m_calendar = new GregorianCalendar();

    this.m_omit = b = OMIT.clone();
    x = this.m_csvSeparator;
    for (i = (x.length - 1); i >= 0; i--) {
      if (x[i] < 32)
        b[x[i]] = false;
    }
    x = this.m_newLine;
    for (i = (x.length - 1); i >= 0; i--) {
      if (x[i] < 32)
        b[x[i]] = false;
    }
    this.m_indentFactor = ((indentFactor > 0) ? indentFactor : 4);
  }

  /**
   * Create a new text writer.
   * 
   * @param out
   *          The output stream to write to.
   * @param numberFormat
   *          the numberformat to be used to format numeric output
   * @param newLine
   *          the new-line character sequence
   * @param csvSeparator
   *          the csv separator
   * @param indentFactor
   *          the indent factor
   */
  public TextWriter(final Writer out, final NumberFormat numberFormat,
      final String newLine, final String csvSeparator,
      final int indentFactor) {
    this(out, numberFormat, ((newLine == null) ? null : newLine
        .toCharArray()), ((csvSeparator == null) ? null : csvSeparator
        .toCharArray()), indentFactor);
  }

  /**
   * Create a new text writer.
   * 
   * @param out
   *          The output stream to write to.
   */
  public TextWriter(final Writer out) {
    this(out, null, (char[]) null, null, -1);
  }

  /**
   * Create a new text writer.
   * 
   * @param out
   *          The output stream to write to.
   */
  public TextWriter(final Object out) {
    this(IO.getWriter(out));
  }

  /**
   * This method will return <code>true</code> if and only if the item
   * printed next would be the first thing on a new line.
   * 
   * @return This method will return <code>true</code> if and only if the
   *         item printed next would be the first thing on a new line.
   */
  public boolean isNewLine() {
    return this.m_isNewLine;
  }

  /**
   * Append a new line.
   */
  public void newLine() {
    this.write(this.m_newLine, 0, this.m_newLine.length);
  }

  /**
   * Append a new line.
   */
  public void ensureNewLine() {
    if (!(this.m_isNewLine))
      this.write(this.m_newLine, 0, this.m_newLine.length);
  }

  /**
   * Set the csv separator.
   * 
   * @param separator
   *          the csv separator
   */
  public void setCSVSeparator(final String separator) {
    char[] x, z;
    int i, j;
    boolean[] b;
    char t;

    if (separator == null)
      throw new NullPointerException();

    b = this.m_omit;
    x = this.m_csvSeparator;
    z = this.m_newLine;
    main: for (i = (x.length - 1); i >= 0; i--) {
      t = x[i];
      if (t < 32) {
        for (j = (z.length - 1); j >= 0; j--) {
          if (t == z[i])
            continue main;
        }
        b[t] = true;
      }
    }
    this.m_csvSeparator = x = separator.toCharArray();

    for (i = (x.length - 1); i >= 0; i--) {
      if (x[i] < 32)
        b[x[i]] = false;
    }

    b['\t'] = false;
  }

  /**
   * Obtain the csv separator.
   * 
   * @return the csv separator
   */
  public String getCSVSeparator() {
    return String.valueOf(this.m_csvSeparator);
  }

  /**
   * Obtain the string used to separate lines.
   * 
   * @return the string used to separate lines
   */
  public String getLineSeparator() {
    return String.valueOf(this.m_newLine);
  }

  /**
   * Append a textable object.
   * 
   * @param it
   *          the textable object
   */
  public void writeTextable(final ITextable it) {
    StringBuilder sb;

    sb = this.m_buffer2;
    it.toStringBuilder(sb);
    this.flushSb(sb);
  }

  /**
   * Append a csv separator.
   */
  public void writeCSVSeparator() {
    this.write(this.m_csvSeparator, 0, this.m_csvSeparator.length);
  }

  /**
   * Set the format to be used for localized numeric output.
   * 
   * @param format
   *          the number format
   */
  public void setNumberFormat(final NumberFormat format) {
    if (format == null)
      throw new NullPointerException();
    this.m_numberInstance = format;
  }

  /**
   * Obtain the number format.
   * 
   * @return the number format
   */
  public NumberFormat getNumberFormat() {
    return this.m_numberInstance;
  }

  /**
   * Set whether or not numeric output should be localized.
   * 
   * @param localize
   *          <code>true</code> if numeric output should be localized,
   *          <code>false</code> if the default java formats should be
   *          used.
   */
  public void setLocalizeNumbers(final boolean localize) {
    this.m_localizeNumbers = localize;
  }

  /**
   * Check whether or not numeric output is localized.
   * 
   * @return <code>true</code> if numeric output should be localized,
   *         <code>false</code> if the default java formats should be
   *         used.
   */
  public boolean areNumbersLocalized() {
    return this.m_localizeNumbers;
  }

  /**
   * Obtain a buffer of the given size.
   * 
   * @param size
   *          the size of the buffer needed
   * @return the buffer
   */
  private final char[] getBuffer(final int size) {
    char[] ch;
    ch = this.m_buffer;
    if (ch.length < size) {
      return this.m_buffer = new char[(size * 3) >>> 1];
    }
    return ch;
  }

  /**
   * Flush the contents of this text buffer.
   */
  @Override
  public void flush() {
    Writer w;

    w = this.m_out;
    // if (w != null) {
    try {
      w.write(this.m_data, 0, this.m_length);
      this.m_length = 0;
      w.flush();
    } catch (IOException ioe) {
      this.m_t = ioe;
    }
    // }
  }

  /**
   * Dispose this reference counted writer.
   * 
   * @throws IOException
   *           If the underlying was closed and caused an IOException.
   */
  @Override
  protected void dispose() throws IOException {
    this.flush();

    try {
      super.dispose();
    } catch (IOException ioe) {
      this.m_t = ioe;
      throw ioe;
    }
  }

  /**
   * Write a single character. The character to be written is contained in
   * the 16 low-order bits of the given integer value; the 16 high-order
   * bits are ignored.
   * 
   * @param c
   *          int specifying a character to be written.
   */
  @Override
  public void write(final int c) {
    char[] ch;
    ch = this.m_buffer;
    ch[0] = ((char) c);
    this.write(ch, 0, 1);
  }

  /**
   * Write an array of characters.
   * 
   * @param cbuf
   *          Array of characters to be written
   */
  @Override
  public void write(final char cbuf[]) {
    this.write(cbuf, 0, cbuf.length);
  }

  /**
   * Buffer some character data. This method copies the data directly into
   * the internal buffer.
   * 
   * @param cbuf
   *          Array of characters
   * @param off
   *          Offset from which to start writing characters
   * @param len
   *          Number of characters to write
   */
  protected void buffer(final char cbuf[], final int off, final int len) {
    int l, nl;
    char[] ch2;

    l = this.m_length;
    ch2 = this.m_data;
    nl = (l + len);
    if (nl > ch2.length) {
      ch2 = new char[(nl * 3) >>> 1];
      System.arraycopy(this.m_data, 0, ch2, 0, l);
      this.m_data = ch2;
    }
    System.arraycopy(cbuf, off, ch2, l, len);
    this.m_length = nl;
    this.m_isNewLine = (ch2[nl - 1] == '\n');
  }

  /**
   * Write a portion of an array of characters. This method is invoked by
   * all other routines of the text writer.
   * 
   * @param cbuf
   *          Array of characters
   * @param off
   *          Offset from which to start writing characters
   * @param len
   *          Number of characters to write
   */
  @Override
  public void write(final char cbuf[], final int off, final int len) {
    int i, j, end;
    char ch;
    boolean[] b;
    boolean zet;

    i = off;
    end = (i + len);
    b = this.m_omit;
    zet = (this.m_isFormatting > 0);

    main: for (;;) {
      for (j = i; j < end; j++) {
        ch = cbuf[j];
        if ((ch < 32) && b[ch]) {
          if (i < j) {
            if (zet)
              this.format(cbuf, i, j - i);
            else
              this.buffer(cbuf, i, j - i);
          }
          i = j + 1;
          continue main;
        }
      }
      break main;
    }

    if (i < j) {
      if (zet)
        this.format(cbuf, i, j - i);
      else
        this.buffer(cbuf, i, j - i);
    }
  }

  /**
   * Write a string.
   * 
   * @param str
   *          String to be written
   */
  @Override
  public void write(final String str) {
    this.write(str, 0, str.length());
  }

  /**
   * Write a portion of a string.
   * 
   * @param str
   *          A String
   * @param off
   *          Offset from which to start writing characters
   * @param len
   *          Number of characters to write
   */
  @Override
  public void write(final String str, final int off, final int len) {
    char cbuf[];

    cbuf = this.getBuffer(len);
    str.getChars(off, off + len, cbuf, 0);
    this.write(cbuf, 0, len);
  }

  /**
   * Appends the specified character sequence to this writer.
   * 
   * @param csq
   *          The character sequence to append. If <tt>csq</tt> is
   *          <tt>null</tt>, then the four characters <tt>"null"</tt>
   *          are appended to this writer.
   * @return This writer
   */
  @Override
  public Writer append(final CharSequence csq) {
    if (csq == null)
      this.write(String.valueOf(null));
    else
      this.append(csq, 0, csq.length());
    return this;
  }

  /**
   * Appends a subsequence of the specified character sequence to this
   * writer.
   * 
   * @param csq
   *          The character sequence from which a subsequence will be
   *          appended. If <tt>csq</tt> is <tt>null</tt>, then
   *          characters will be appended as if <tt>csq</tt> contained
   *          the four characters <tt>"null"</tt>.
   * @param start
   *          The index of the first character in the subsequence
   * @param end
   *          The index of the character following the last character in
   *          the subsequence
   * @return This writer
   * @throws IndexOutOfBoundsException
   *           If <tt>start</tt> or <tt>end</tt> are negative,
   *           <tt>start</tt> is greater than <tt>end</tt>, or
   *           <tt>end</tt> is greater than <tt>csq.length()</tt>
   */
  @Override
  public Writer append(final CharSequence csq, final int start,
      final int end) {
    char[] ch;
    int i, j, len;

    if (csq == null)
      this.write(String.valueOf(null));
    else {
      len = (end - start);
      ch = this.getBuffer(len);
      for (i = (end - 1), j = (len - 1); j >= 0; j--, i--) {
        ch[j] = csq.charAt(i);
      }

      this.write(ch, 0, len);
    }
    return this;
  }

  /**
   * Appends the specified character to this writer.
   * 
   * @param c
   *          The 16-bit character to append
   * @return This writer
   */
  @Override
  public Writer append(final char c) {
    this.write(c);
    return this;
  }

  /**
   * Write the contents of a string builder.
   * 
   * @param sb
   *          the string builder
   * @throws NullPointerException
   *           if <code>sb==null</code>
   */
  public void writeStringBuilder(final StringBuilder sb) {
    int l;
    char[] ch;

    l = sb.length();
    ch = this.getBuffer(l);
    sb.getChars(0, l, ch, 0);
    this.write(ch, 0, l);
  }

  /**
   * Write a string builder to the output and clear it afterwards.
   * 
   * @param sb
   *          the string builder
   */
  protected final void flushSb(final StringBuilder sb) {
    int l;
    char[] ch;

    l = sb.length();
    ch = this.getBuffer(l);
    sb.getChars(0, l, ch, 0);
    this.write(ch, 0, l);
    sb.setLength(0);
  }

  /**
   * Buffer a object to this text writer.
   * 
   * @param object
   *          The object to be buffered.
   */
  public void writeObject(final Object object) {

    if (object instanceof ITextable)
      this.writeTextable((ITextable) object);
    else if (object instanceof StringBuilder)
      this.writeStringBuilder((StringBuilder) object);
    else
      this.write(String.valueOf(object));
  }

  /**
   * Buffer a object to this text writer while considering if it is an
   * array. If so, the object's (= the array's contents) will also be
   * buffered).
   * 
   * @param object
   *          The object to be buffered.
   */
  public void writeObject2(final Object object) {
    StringBuilder sb;

    if (object instanceof ITextable)
      this.writeTextable((ITextable) object);
    else if (object instanceof StringBuilder)
      this.writeStringBuilder((StringBuilder) object);
    else {
      sb = this.m_buffer2;
      TextUtils.append(object, sb);
      // ((ITextable) object).toStringBuilder(sb);
      this.flushSb(sb);
    }
  }

  /**
   * Writes a <code>boolean</code> value to this output stream using a
   * short representation based on '0' and '1' for <code>false</code> and
   * <code>true</code>.
   * 
   * @param v
   *          the boolean to be written.
   */
  public void writeBoolean2(final boolean v) {
    char[] ch;

    ch = this.getBuffer(1);
    ch[0] = (v ? '1' : '0');
    this.write(ch, 0, 1);
  }

  /**
   * Writes a <code>boolean</code> value to this output stream.
   * 
   * @param v
   *          the boolean to be written.
   */
  public void writeBoolean(final boolean v) {
    StringBuilder sb;

    sb = this.m_buffer2;
    sb.append(v);
    this.flushSb(sb);
  }

  /**
   * Writes to the output stream the eight low- order bits of the argument
   * <code>v</code>.
   * 
   * @param v
   *          the byte value to be written.
   */
  public void writeByte(final int v) {
    this.writeInt((byte) v);
  }

  /**
   * Writes two bytes to the output.
   * 
   * @param v
   *          the <code>short</code> value to be written.
   */
  public void writeShort(final int v) {
    this.writeInt((short) v);
  }

  /**
   * Writes a <code>char</code> value
   * 
   * @param v
   *          the <code>char</code> value to be written.
   */
  public void writeChar(final int v) {
    this.write(v);
  }

  /**
   * Writes an <code>int</code> value
   * 
   * @param v
   *          the <code>int</code> value to be written.
   */
  public void writeInt(final int v) {
    if (this.m_localizeNumbers)
      this.writeLocalizedInt(v);
    else
      this.writeRawInt(v);
  }

  /**
   * Writes an <code>int</code> value in the standard manner
   * 
   * @param v
   *          the <code>int</code> value to be written.
   */
  public void writeRawInt(final int v) {
    StringBuilder sb;

    sb = this.m_buffer2;
    sb.append(v);
    this.flushSb(sb);
  }

  /**
   * Write an integer in a formatted manner.
   * 
   * @param v
   *          the integer to be written.
   */
  public void writeLocalizedInt(final int v) {
    this.write(this.m_numberInstance.format(v));
  }

  /**
   * Writes a <code>long</code> value
   * 
   * @param v
   *          the <code>long</code> value to be written.
   */
  public void writeLong(final long v) {
    if (this.m_localizeNumbers)
      this.writeLocalizedLong(v);
    else
      this.writeRawLong(v);
  }

  /**
   * Writes a <code>long</code> value in the default manner
   * 
   * @param v
   *          the <code>long</code> value to be written.
   */
  public void writeRawLong(final long v) {
    StringBuilder sb;

    sb = this.m_buffer2;
    sb.append(v);
    this.flushSb(sb);
  }

  /**
   * Writes a <code>long</code> value formatted according to the locale
   * set
   * 
   * @param v
   *          the <code>long</code> value to be written.
   */
  public void writeLocalizedLong(final long v) {
    this.write(this.m_numberInstance.format(v));
  }

  /**
   * Writes a <code>float</code> value
   * 
   * @param v
   *          the <code>float</code> value to be written.
   */
  public void writeFloat(final float v) {
    this.writeDouble(v);
  }

  /**
   * Writes a <code>double</code> value
   * 
   * @param v
   *          the <code>double</code> value to be written.
   */
  public void writeDouble(final double v) {
    if (this.m_localizeNumbers)
      this.writeLocalizedDouble(v);
    else
      this.writeRawDouble(v);
  }

  /**
   * Writes a <code>double</code> value in the raw, java-typical format.
   * 
   * @param v
   *          the <code>double</code> value to be written.
   */
  public void writeRawDouble(final double v) {
    StringBuilder sb;

    sb = this.m_buffer2;
    sb.append(v);
    this.flushSb(sb);
  }

  /**
   * Writes a <code>double</code> value formatting it to make it more
   * readable.
   * 
   * @param v
   *          the <code>double</code> value to be written.
   */
  public void writeLocalizedDouble(final double v) {
    this.write(this.m_numberInstance.format(v));
  }

  /**
   * Write an array of bytes base64-encoded.
   * 
   * @param b
   *          the byte array to be written
   */
  public void write(final byte[] b) {
    this.write(b, 0, b.length);
  }

  /**
   * Write an array of bytes base64-encoded.
   * 
   * @param b
   *          the bytes to be written.
   * @param off
   *          the offset to start
   * @param len
   *          the count of byes to write
   */
  public void write(final byte[] b, final int off, final int len) {
    this.writeBase64(b, off, len);
  }

  /**
   * Write an array of bytes and encoding it using base64 encoding.
   * 
   * @param b
   *          the bytes to be written.
   * @param off
   *          the offset to start
   * @param len
   *          the count of byes to write
   */
  public void writeBase64(final byte[] b, final int off, final int len) {
    int l;
    Base64 use;
    char[] buf;

    use = Base64.DEFAULT;
    l = use.getEncodedSize(b, off, len);
    buf = this.getBuffer(l);
    buf = use.encode(b, off, len, buf);
    this.write(buf, 0, l);
  }

  /**
   * Write a string as bytes (which are base64-encoded).
   * 
   * @param s
   *          the string to be written
   */
  public void writeBytes(final String s) {
    this.write(TextUtils.stringToBytes(s, TextUtils.DEFAULT_ENCODING));
  }

  /**
   * Write a string.
   * 
   * @param s
   *          the string to be written
   */
  public void writeChars(final String s) {
    this.write(s);
  }

  /**
   * Write a string.
   * 
   * @param str
   *          the string to be written
   */
  public void writeUTF(final String str) {
    this.write(str);
  }

  /**
   * Write a time constant.
   * 
   * @param time
   *          The time constant. This is a value of the type
   *          <code>long</code>, for example obtained by
   *          <code>System.currentMillies</code>.
   * @param relative
   *          <code>true</code> if the time expression is relative.
   * @see System#currentTimeMillis()
   */
  public void writeTime(final long time, final boolean relative) {
    Calendar c;
    StringBuilder sb;

    c = this.m_calendar;
    c.setTimeInMillis(time);
    sb = this.m_buffer2;

    TextUtils.appendTime(c, relative, sb);
    this.flushSb(sb);
  }

  /**
   * Write a time according to the iso format.
   * 
   * @param time
   *          The time constant. This is a value of the type
   *          <code>long</code>, for example obtained by
   *          <code>System.currentMillies</code>.
   * @param relative
   *          <code>true</code> if the time expression is relative.
   * @see System#currentTimeMillis()
   */
  public void writeIsoTime(final long time, final boolean relative) {
    Calendar c;
    StringBuilder sb;
    if (relative) {
      sb = this.m_buffer2;
      TextUtils.appendRelativeIsoTime(time, sb);
      this.flushSb(sb);
    } else {
      c = this.m_calendar;

      c.setTimeInMillis(time);
      this.write(TextUtils.ISO_8601.format(c.getTime()));
    }
  }

  /**
   * Obtain (and clear) the last io error caught.
   * 
   * @return the last io error caught or <code>null</code> if no error
   *         occured since this method was called last
   */
  public IOException getLastIOException() {
    IOException x;
    x = this.m_t;
    this.m_t = null;
    return x;
  }

  /**
   * Increase the indentation level.
   */
  public void incIndent() {
    int x;

    x = (this.m_indent + this.m_indentFactor);
    this.m_indent = x;
    // if (x > s_indent.length) {
    // synchronized (TextWriter.class) {
    // if (x > s_indent.length) {
    // x <<= 1;
    // s_indent = new char[x];
    // Arrays.fill(s_indent, 0, x, ' ');
    // }
    // }
    // }
  }

  /**
   * Decrease the indentation level.
   */
  public void decIndent() {
    this.m_indent -= this.m_indentFactor;
  }

  /**
   * Turn formatting on.
   */
  public void formatOn() {
    this.m_isFormatting++;
  }

  /**
   * Turn formatting off.
   */
  public void formatOff() {
    this.m_isFormatting--;
  }

  /**
   * Returns the current indentation level of the writer.
   * 
   * @return The current indentation level of the writer.
   */
  public int getIndent() {
    return ((this.m_isFormatting > 0) ? this.m_indent : 0);
  }

  /**
   * Returns the indentation factor of the writer. That is the count of
   * spaces added each time the indentation is increased.
   * 
   * @return Returns the indentation factor of the writer.
   */
  public int getIndentFactor() {
    return this.m_indentFactor;
  }

  /**
   * Check whether formatting is currently turned on or not.
   * 
   * @return <code>true</code> if and only if formatting is currently
   *         turned one, <code>false</code> if no indentation will be
   *         performed.
   */
  public boolean isFormatting() {
    return (this.m_isFormatting > 0);
  }

  /**
   * Format some character data. If formatting is turned on, all data will
   * be piped through this procedure.
   * 
   * @param cbuf
   *          Array of characters
   * @param off
   *          Offset from which to start writing characters
   * @param len
   *          Number of characters to write
   */
  protected void format(final char cbuf[], final int off, final int len) {
    int i, idt, lan, ofs;
    boolean z;

    lan = (len + off);
    ofs = off;
    idt = this.m_indent;

    if (idt <= 0) {
      this.buffer(cbuf, off, len);
    } else {
      z = this.m_isNewLine;

      for (i = ofs; i < lan; i++) {
        if (cbuf[i] == '\n') {
          if (z) {
            if (i > ofs)
              this.buffer(TextUtils.getSpaces(idt), 0, idt);
          } else
            z = true;

          this.buffer(cbuf, ofs, i - ofs + 1);
          ofs = (i + 1);
        }
      }

      if (ofs < i) {
        if (z)
          this.buffer(TextUtils.getSpaces(idt), 0, idt);
        this.buffer(cbuf, ofs, i - ofs);
      }
    }
  }

}
