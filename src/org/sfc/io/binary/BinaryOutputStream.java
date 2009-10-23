/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-12-20
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.io.binary.BitStringOutputStream.java
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

package org.sfc.io.binary;

import java.io.DataOutput;
import java.util.Arrays;

import org.sfc.io.ReferenceCountedOutputStream;

/**
 * An output stream for bit strings.
 * 
 * @author Thomas Weise
 */
public class BinaryOutputStream extends ReferenceCountedOutputStream
    implements DataOutput {
  /**
   * the empty byte
   */
  private static final byte[] EMPTY = new byte[0];

  /**
   * this mask allows all bits before its index
   */
  private static final int[] MASK = new int[9];

  static {
    int i, s;

    s = 0;
    for (i = 0; i < 8; i++) {
      MASK[i] = s;
      s += (1 << i);
    }
    MASK[8] = 0xff;
  }

  /**
   * the internal data buffer.
   */
  private byte[] m_buffer;

  /**
   * the current position in the buffer.
   */
  private int m_pos;

  /**
   * create a new bit string output stream
   */
  public BinaryOutputStream() {
    super();
    this.m_buffer = new byte[1024];
  }

  /**
   * Write <code>n<code> bits into the output stream.
   * @param data  the data bits to be written
   * @param n the count of bits of <code>data</code> to be written
   */
  public void writeBits(final int data, final int n) {
    byte[] buf;
    int pos, np, bp, d, r;

    if (n == 0)
      return;

    if ((n < 0) || (n > 32))
      throw new IllegalArgumentException();

    buf = this.m_buffer;
    pos = this.m_pos;

    np = pos + n;
    this.m_pos = np;
    np = ((np + 7) >>> 3);
    if (np >= buf.length) {
      buf = new byte[(np * 3) >>> 1];
      System.arraycopy(this.m_buffer, 0, buf, 0, ((pos + 7) >>> 3));
      this.m_buffer = buf;
    }

    np = (pos >>> 3);
    bp = (pos & 7);
    r = n;
    d = data;
    pos = (8 - bp);
    buf[np] |= ((d & MASK[Math.min(pos, r)]) << bp);
    r -= pos;
    d >>>= pos;
    while (r > 0) {
      buf[++np] = (byte) (MASK[Math.min(r, 8)] & d);
      d >>>= 8;
      r -= 8;
    }
  }

  /**
   * Obtain the output of the stream.
   * 
   * @return a byte array containing the output of the stream
   * @throws IllegalArgumentException
   *           if <code>n &lt;0 || n &gt;32</code>
   */
  public byte[] getOutput() {
    byte[] b;
    int l;

    l = ((this.m_pos + 7) >>> 3);
    if (l <= 0)
      return EMPTY;
    b = new byte[l];
    System.arraycopy(this.m_buffer, 0, b, 0, l);
    return b;
  }

  /**
   * Obtain the count of bits currently stored.
   * 
   * @return the count of bits currently stored
   */
  public int getBitCount() {
    return this.m_pos;
  }

  /**
   * Clear the bit string output stream so it can be reused.
   */
  public void clear() {
    Arrays.fill(this.m_buffer, 0, ((this.m_pos + 7) >>> 3), (byte) 0);
    this.m_pos = 0;
  }

  /**
   * Writes the specified byte to this output stream. The general contract
   * for <code>write</code> is that one byte is written to the output
   * stream. The byte to be written is the eight low-order bits of the
   * argument <code>b</code>. The 24 high-order bits of <code>b</code>
   * are ignored.
   * <p>
   * Subclasses of <code>OutputStream</code> must provide an
   * implementation for this method.
   * 
   * @param b
   *          the <code>byte</code>.
   */
  @Override
  public void write(final int b) {
    this.writeBits(b, 8);
  }

  /**
   * Writes <code>b.length</code> bytes from the specified byte array to
   * this output stream. The general contract for <code>write(b)</code>
   * is that it should have exactly the same effect as the call
   * <code>write(b, 0, b.length)</code>.
   * 
   * @param b
   *          the data.
   * @see java.io.OutputStream#write(byte[], int, int)
   */
  @Override
  public void write(final byte b[]) {
    this.write(b, 0, b.length);
  }

  /**
   * Writes <code>len</code> bytes from the specified byte array starting
   * at offset <code>off</code> to this output stream. The general
   * contract for <code>write(b, off, len)</code> is that some of the
   * bytes in the array <code>b</code> are written to the output stream
   * in order; element <code>b[off]</code> is the first byte written and
   * <code>b[off+len-1]</code> is the last byte written by this
   * operation.
   * <p>
   * The <code>write</code> method of <code>OutputStream</code> calls
   * the write method of one argument on each of the bytes to be written
   * out. Subclasses are encouraged to override this method and provide a
   * more efficient implementation.
   * <p>
   * If <code>b</code> is <code>null</code>, a
   * <code>NullPointerException</code> is thrown.
   * <p>
   * If <code>off</code> is negative, or <code>len</code> is negative,
   * or <code>off+len</code> is greater than the length of the array
   * <code>b</code>, then an <tt>IndexOutOfBoundsException</tt> is
   * thrown.
   * 
   * @param b
   *          the data.
   * @param off
   *          the start offset in the data.
   * @param len
   *          the number of bytes to write.
   */
  @Override
  public void write(final byte b[], final int off, final int len) {
    if (b == null) {
      throw new NullPointerException();
    } else if ((off < 0) || (off > b.length) || (len < 0)
        || ((off + len) > b.length) || ((off + len) < 0)) {
      throw new IndexOutOfBoundsException();
    } else if (len == 0) {
      return;
    }
    for (int i = 0; i < len; i++) {
      write(b[off + i]);
    }
  }

  /**
   * Flushes this output stream and forces any buffered output bytes to be
   * written out. The general contract of <code>flush</code> is that
   * calling it is an indication that, if any bytes previously written have
   * been buffered by the implementation of the output stream, such bytes
   * should immediately be written to their intended destination.
   * <p>
   * If the intended destination of this stream is an abstraction provided
   * by the underlying operating system, for example a file, then flushing
   * the stream guarantees only that bytes previously written to the stream
   * are passed to the operating system for writing; it does not guarantee
   * that they are actually written to a physical device such as a disk
   * drive.
   * <p>
   * The <code>flush</code> method of does nothing.
   */
  @Override
  public void flush() {
    //
  }

  /**
   * Writes a <code>boolean</code> value to this output stream.
   * 
   * @param v
   *          the boolean to be written.
   */
  public void writeBoolean(final boolean v) {
    this.writeBits(v ? 1 : 0, 1);
  }

  /**
   * Writes to the output stream the eight low- order bits of the argument
   * <code>v</code>. The 24 high-order bits of <code>v</code> are
   * ignored.
   * 
   * @param v
   *          the byte value to be written.
   */
  public void writeByte(int v) {
    this.writeBits(v, 8);
  }

  /**
   * Writes two bytes to the output stream to represent the value of the
   * argument.
   * 
   * @param v
   *          the <code>short</code> value to be written.
   */
  public void writeShort(final int v) {
    this.writeBits(v, 16);
  }

  /**
   * Writes a <code>char</code> value, which is comprised of two bytes,
   * to the output stream.
   * 
   * @param v
   *          the <code>char</code> value to be written.
   */
  public void writeChar(final int v) {
    this.writeBits(v, 16);
  }

  /**
   * Writes an <code>int</code> value, which is comprised of four bytes,
   * to the output stream.
   * 
   * @param v
   *          the <code>int</code> value to be written.
   */
  public void writeInt(final int v) {
    this.writeBits(v, 32);
  }

  /**
   * Writes a <code>long</code> value, which is comprised of eight bytes,
   * to the output stream.
   * 
   * @param v
   *          the <code>long</code> value to be written.
   */
  public void writeLong(final long v) {
    this.writeBits((int) ((v >>> 32L) & 0xffffffffL), 32);
    this.writeBits((int) (v & 0xffffffffL), 32);
  }

  /**
   * Writes a <code>float</code> value, which is comprised of four bytes,
   * to the output stream.
   * 
   * @param v
   *          the <code>float</code> value to be written.
   */
  public void writeFloat(final float v) {
    this.writeInt(Float.floatToIntBits(v));
  }

  /**
   * Writes a <code>double</code> value, which is comprised of eight
   * bytes, to the output stream.
   * 
   * @param v
   *          the <code>double</code> value to be written.
   */
  public void writeDouble(final double v) {
    this.writeLong(Double.doubleToLongBits(v));
  }

  /**
   * Writes a string to the output stream.ter in the string are ignored.
   * 
   * @param s
   *          the string of bytes to be written.
   */
  public void writeBytes(final String s) {
    int len = s.length();
    for (int i = 0; i < len; i++) {
      this.writeBits(s.charAt(i), 8);
    }
  }

  /**
   * Writes every character in the string <code>s</code>, to the output
   * stream, in order, two bytes per character.
   * 
   * @param s
   *          the string value to be written.
   */
  public void writeChars(final String s) {
    int len = s.length();
    for (int i = 0; i < len; i++) {
      this.writeChar(s.charAt(i));
    }
  }

  /**
   * Writes two bytes of length information to the output stream followed
   * by the string data.
   * 
   * @param str
   *          the string value to be written.
   */
  public void writeUTF(final String str) {
    int i, l;
    l = str.length();
    if (l > 65536)
      l = 65536;
    this.writeBits(l, 16);
    for (i = 0; i < l; i++)
      this.writeChar(str.charAt(i));
  }

}
