/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-29
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.io.TextWriterPrintStream.java
 * Last modification: 2007-09-29
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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * This class constitutes a print stream that writes to a text writer. This
 * can, for example, be used to pipe the {@link java.lang.System#out} or
 * {@link java.lang.System#err} to a text writer.
 * 
 * @author Thomas Weise
 */
public class TextWriterPrintStream extends PrintStream {

  /**
   * the text writer to write to
   */
  private final TextWriter m_writer;

  /**
   * Create a new text writer print stream.
   * 
   * @param o
   *          the target
   */
  public TextWriterPrintStream(final Object o) {
    super(new ByteArrayOutputStream(), false);

    this.m_writer = ((o instanceof TextWriter) ? ((TextWriter) o)
        : new TextWriter(o));
  }

  /**
   * Flush the stream. This is done by writing any buffered output bytes to
   * the underlying output stream and then flushing that stream.
   * 
   * @see java.io.OutputStream#flush()
   */
  @Override
  public void flush() {
    this.m_writer.flush();
  }

  /**
   * Close the stream. This is done by flushing the stream and then closing
   * the underlying output stream.
   * 
   * @see java.io.OutputStream#close()
   */
  @Override
  public void close() {
    try {
      this.m_writer.close();
    } catch (Throwable tt) {
      //
    }
  }

  /**
   * Flush the stream and check its error state. The internal error state
   * is set to <code>true</code> when the underlying output stream throws
   * an <code>IOException</code> other than
   * <code>InterruptedIOException</code>, and when the
   * <code>setError</code> method is invoked. If an operation on the
   * underlying output stream throws an <code>InterruptedIOException</code>,
   * then the <code>PrintStream</code> converts the exception back into
   * an interrupt by doing:
   * 
   * <pre>
   * Thread.currentThread().interrupt();
   * </pre>
   * 
   * or the equivalent.
   * 
   * @return True if and only if this stream has encountered an
   *         <code>IOException</code> other than
   *         <code>InterruptedIOException</code>, or the
   *         <code>setError</code> method has been invoked
   */
  @Override
  public boolean checkError() {
    this.flush();
    return (this.m_writer.getLastIOException() != null);
  }

  /*
   * Exception-catching, synchronized output operations, which also
   * implement the write() methods of OutputStream
   */

  /**
   * Write the specified byte to this stream. If the byte is a newline and
   * automatic flushing is enabled then the <code>flush</code> method
   * will be invoked.
   * <p>
   * Note that the byte is written as given; to write a character that will
   * be translated according to the platform's default character encoding,
   * use the <code>print(char)</code> or <code>println(char)</code>
   * methods.
   * 
   * @param b
   *          The byte to be written
   * @see #print(char)
   * @see #println(char)
   */
  @Override
  public void write(final int b) {
    this.m_writer.writeByte(b);
  }

  /**
   * Write <code>len</code> bytes from the specified byte array starting
   * at offset <code>off</code> to this stream. If automatic flushing is
   * enabled then the <code>flush</code> method will be invoked.
   * <p>
   * Note that the bytes will be written as given; to write characters that
   * will be translated according to the platform's default character
   * encoding, use the <code>print(char)</code> or
   * <code>println(char)</code> methods.
   * 
   * @param buf
   *          A byte array
   * @param off
   *          Offset from which to start taking bytes
   * @param len
   *          Number of bytes to write
   */
  @Override
  public void write(final byte buf[], final int off, final int len) {
    int i, j;

    for (i = off, j = len; j > 0; i++, j--) {
      this.m_writer.writeChar(buf[i]);
    }
  }

  /* Methods that do not terminate lines */

  /**
   * Print a boolean value. The string produced by <code>{@link
   * java.lang.String#valueOf(boolean)}</code>
   * is translated into bytes according to the platform's default character
   * encoding, and these bytes are written in exactly the manner of the
   * <code>{@link #write(int)}</code> method.
   * 
   * @param b
   *          The <code>boolean</code> to be printed
   */
  @Override
  public void print(final boolean b) {
    this.m_writer.writeBoolean(b);
  }

  /**
   * Print a character. The character is translated into one or more bytes
   * according to the platform's default character encoding, and these
   * bytes are written in exactly the manner of the
   * <code>{@link #write(int)}</code> method.
   * 
   * @param c
   *          The <code>char</code> to be printed
   */
  @Override
  public void print(final char c) {
    this.m_writer.writeChar(c);
  }

  /**
   * Print an integer. The string produced by <code>{@link
   * java.lang.String#valueOf(int)}</code>
   * is translated into bytes according to the platform's default character
   * encoding, and these bytes are written in exactly the manner of the
   * <code>{@link #write(int)}</code> method.
   * 
   * @param i
   *          The <code>int</code> to be printed
   * @see java.lang.Integer#toString(int)
   */
  @Override
  public void print(final int i) {
    this.m_writer.writeInt(i);
  }

  /**
   * Print a long integer. The string produced by <code>{@link
   * java.lang.String#valueOf(long)}</code>
   * is translated into bytes according to the platform's default character
   * encoding, and these bytes are written in exactly the manner of the
   * <code>{@link #write(int)}</code> method.
   * 
   * @param l
   *          The <code>long</code> to be printed
   * @see java.lang.Long#toString(long)
   */
  @Override
  public void print(final long l) {
    this.m_writer.writeLong(l);
  }

  /**
   * Print a floating-point number. The string produced by <code>{@link
   * java.lang.String#valueOf(float)}</code>
   * is translated into bytes according to the platform's default character
   * encoding, and these bytes are written in exactly the manner of the
   * <code>{@link #write(int)}</code> method.
   * 
   * @param f
   *          The <code>float</code> to be printed
   * @see java.lang.Float#toString(float)
   */
  @Override
  public void print(final float f) {
    this.m_writer.writeFloat(f);
  }

  /**
   * Print a double-precision floating-point number. The string produced by
   * <code>{@link java.lang.String#valueOf(double)}</code> is translated
   * into bytes according to the platform's default character encoding, and
   * these bytes are written in exactly the manner of the <code>{@link
   * #write(int)}</code>
   * method.
   * 
   * @param d
   *          The <code>double</code> to be printed
   * @see java.lang.Double#toString(double)
   */
  @Override
  public void print(final double d) {
    this.m_writer.writeDouble(d);
  }

  /**
   * Print an array of characters. The characters are converted into bytes
   * according to the platform's default character encoding, and these
   * bytes are written in exactly the manner of the
   * <code>{@link #write(int)}</code> method.
   * 
   * @param s
   *          The array of chars to be printed
   * @throws NullPointerException
   *           If <code>s</code> is <code>null</code>
   */
  @Override
  public void print(final char s[]) {
    this.m_writer.write(s);
  }

  /**
   * Print a string. If the argument is <code>null</code> then the string
   * <code>"null"</code> is printed. Otherwise, the string's characters
   * are converted into bytes according to the platform's default character
   * encoding, and these bytes are written in exactly the manner of the
   * <code>{@link #write(int)}</code> method.
   * 
   * @param s
   *          The <code>String</code> to be printed
   */
  @Override
  public void print(final String s) {
    this.m_writer.write(s);
  }

  /**
   * Print an object. The string produced by the <code>{@link
   * java.lang.String#valueOf(Object)}</code>
   * method is translated into bytes according to the platform's default
   * character encoding, and these bytes are written in exactly the manner
   * of the <code>{@link #write(int)}</code> method.
   * 
   * @param obj
   *          The <code>Object</code> to be printed
   * @see java.lang.Object#toString()
   */
  @Override
  public void print(final Object obj) {
    this.m_writer.writeObject(obj);
  }

  /* Methods that do terminate lines */

  /**
   * Terminate the current line by writing the line separator string. The
   * line separator string is defined by the system property
   * <code>line.separator</code>, and is not necessarily a single
   * newline character (<code>'\n'</code>).
   */
  @Override
  public void println() {
    this.m_writer.newLine();
  }

  /**
   * Print a boolean and then terminate the line. This method behaves as
   * though it invokes <code>{@link #print(boolean)}</code> and then
   * <code>{@link #println()}</code>.
   * 
   * @param x
   *          The <code>boolean</code> to be printed
   */
  @Override
  public synchronized void println(final boolean x) {
    this.print(x);
    this.m_writer.newLine();
  }

  /**
   * Print a character and then terminate the line. This method behaves as
   * though it invokes <code>{@link #print(char)}</code> and then
   * <code>{@link #println()}</code>.
   * 
   * @param x
   *          The <code>char</code> to be printed.
   */
  @Override
  public synchronized void println(final char x) {
    this.print(x);
    this.m_writer.newLine();
  }

  /**
   * Print an integer and then terminate the line. This method behaves as
   * though it invokes <code>{@link #print(int)}</code> and then
   * <code>{@link #println()}</code>.
   * 
   * @param x
   *          The <code>int</code> to be printed.
   */
  @Override
  public synchronized void println(final int x) {
    this.print(x);
    this.m_writer.newLine();
  }

  /**
   * Print a long and then terminate the line. This method behaves as
   * though it invokes <code>{@link #print(long)}</code> and then
   * <code>{@link #println()}</code>.
   * 
   * @param x
   *          a The <code>long</code> to be printed.
   */
  @Override
  public synchronized void println(final long x) {
    this.print(x);
    this.m_writer.newLine();
  }

  /**
   * Print a float and then terminate the line. This method behaves as
   * though it invokes <code>{@link #print(float)}</code> and then
   * <code>{@link #println()}</code>.
   * 
   * @param x
   *          The <code>float</code> to be printed.
   */
  @Override
  public synchronized void println(final float x) {
    this.print(x);
    this.m_writer.newLine();
  }

  /**
   * Print a double and then terminate the line. This method behaves as
   * though it invokes <code>{@link #print(double)}</code> and then
   * <code>{@link #println()}</code>.
   * 
   * @param x
   *          The <code>double</code> to be printed.
   */
  @Override
  public synchronized void println(final double x) {
    this.print(x);
    this.m_writer.newLine();
  }

  /**
   * Print an array of characters and then terminate the line. This method
   * behaves as though it invokes <code>{@link #print(char[])}</code> and
   * then <code>{@link #println()}</code>.
   * 
   * @param x
   *          an array of chars to print.
   */
  @Override
  public synchronized void println(final char x[]) {
    this.print(x);
    this.m_writer.newLine();
  }

  /**
   * Print a String and then terminate the line. This method behaves as
   * though it invokes <code>{@link #print(String)}</code> and then
   * <code>{@link #println()}</code>.
   * 
   * @param x
   *          The <code>String</code> to be printed.
   */
  @Override
  public synchronized void println(final String x) {
    this.print(x);
    this.m_writer.newLine();
  }

  /**
   * Print an Object and then terminate the line. This method behaves as
   * though it invokes <code>{@link #print(Object)}</code> and then
   * <code>{@link #println()}</code>.
   * 
   * @param x
   *          The <code>Object</code> to be printed.
   */
  @Override
  public synchronized void println(final Object x) {
    this.print(x);
    this.m_writer.newLine();
  }

  /**
   * Appends the specified character sequence to this output stream.
   * <p>
   * An invocation of this method of the form <tt>out.append(csq)</tt>
   * behaves in exactly the same way as the invocation
   * 
   * <pre>
   * out.print(csq.toString())
   * </pre>
   * 
   * <p>
   * Depending on the specification of <tt>toString</tt> for the
   * character sequence <tt>csq</tt>, the entire sequence may not be
   * appended. appended. For instance, invoking then <tt>toString</tt>
   * method of a character buffer will return a subsequence whose content
   * depends upon the buffer's position and limit.
   * 
   * @param csq
   *          The character sequence to append. If <tt>csq</tt> is
   *          <tt>null</tt>, then the four characters <tt>"null"</tt>
   *          are appended to this output stream.
   * @return This character stream
   * @since 1.5
   */
  @Override
  public PrintStream append(final CharSequence csq) {
    this.m_writer.append(csq);
    return this;
  }

  /**
   * Appends a subsequence of the specified character sequence to this
   * output stream.
   * <p>
   * An invocation of this method of the form <tt>out.append(csq, start,
   * end)</tt>
   * when <tt>csq</tt> is not <tt>null</tt>, behaves in exactly the
   * same way as the invocation
   * 
   * <pre>
   * out.print(csq.subSequence(start, end).toString())
   * </pre>
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
   * @return This character stream
   * @throws IndexOutOfBoundsException
   *           If <tt>start</tt> or <tt>end</tt> are negative,
   *           <tt>start</tt> is greater than <tt>end</tt>, or
   *           <tt>end</tt> is greater than <tt>csq.length()</tt>
   * @since 1.5
   */
  @Override
  public PrintStream append(final CharSequence csq, final int start,
      final int end) {
    this.m_writer.append(csq, start, end);
    return this;
  }

  /**
   * Appends the specified character to this output stream.
   * <p>
   * An invocation of this method of the form <tt>out.append(c)</tt>
   * behaves in exactly the same way as the invocation
   * 
   * <pre>
   * out.print(c)
   * </pre>
   * 
   * @param c
   *          The 16-bit character to append
   * @return This output stream
   * @since 1.5
   */
  @Override
  public PrintStream append(final char c) {
    print(c);
    return this;
  }

}
