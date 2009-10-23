/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-12-19
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.io.binary.BitStringInputStream.java
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

import java.io.DataInput;

import org.sfc.io.ReferenceCountedInputStream;

/**
 * This class helps to read structured data from a bit string. Other than
 * java's own <code>ByteArrayInputStream</code>s, such streams here are
 * reuseable and allow you to easily integrate gray coding or other coding
 * schemas.
 * 
 * @author Thomas Weise
 */
public class BinaryInputStream extends ReferenceCountedInputStream
    implements DataInput {

  /**
   * this mask allows all bits before its index
   */
  private static final int[] MASK = new int[33];

  static {
    int i, s;

    s = 0;
    for (i = 0; i < 32; i++) {
      MASK[i] = s;
      s += (1 << i);
    }
    MASK[32] = 0xffffffff;
  }

  /**
   * the bit string
   */
  private byte m_buf[];

  /**
   * the current position
   */
  private int m_pos;

  /**
   * the currently marked position in the stream
   */
  private int m_mark;

  /**
   * the position behind the last valid character in the stream
   */
  private int m_last;

  /**
   * Create a new bit string input stream
   */
  public BinaryInputStream() {
    super();
  }

  /**
   * Clear this input stream.
   */
  public void clear() {
    this.m_buf = null;
    this.m_pos = 0;
    this.m_last = 0;
    this.m_mark = 0;
  }

  /**
   * Initialize a <code>BinaryInputStream</code> that uses
   * <code>buf</code> as its buffer array.
   * 
   * @param buf
   *          the input buffer.
   */
  public void init(final byte buf[]) {
    this.init(buf, 0, buf.length << 3);
  }

  /**
   * Initialize a <code>BinaryInputStream</code> that uses
   * <code>buf</code> as its buffer array. The initial value of
   * <code>pos</code> is <code>offset</code> and the initial value of
   * <code>count</code> is the minimum of <code>offset+length</code>
   * and <code>buf.length*8</code>. The buffer array is not copied. The
   * buffer's mark is set to the specified offset.
   * 
   * @param buf
   *          the input buffer.
   * @param offset
   *          the offset in the buffer of the first bit to read.
   * @param length
   *          the maximum number of bit to read from the buffer.
   */
  public void init(final byte buf[], final int offset, final int length) {
    this.m_buf = buf;
    this.m_pos = offset;
    this.m_last = Math.min(offset + length, buf.length << 3);
    this.m_mark = offset;
  }

  /**
   * Read the next <code>n</code> bits (if possible).
   * <p>
   * All other read methods of this stream delegate their work to this
   * routine. If this method is overriden, in order to provide a grey
   * coding, for example, all other methods will also use the grey coding
   * automatically.
   * <p>
   * If the end of the data is reached, before all of the bits could be
   * read, the remaining bits are set to 0.
   * 
   * @param n
   *          the count of bits to read (<code>0 &lt;= n &lt;= 32</code>)
   * @return the next <code>n</code> bits
   * @throws IllegalArgumentException
   *           if <code>n &lt;0 || n &gt;32</code>
   */
  public int readBits(final int n) {
    int pos, last, byteIndex, bitIndex, res, m;
    byte[] buf;

    if (n == 0)
      return 0;

    if ((n < 0) || (n > 32))
      throw new IllegalArgumentException();

    pos = this.m_pos;
    last = this.m_last;

    if (pos >= last)
      return 0;

    m = n;

    last -= pos; // the count of available bits
    if (last > m)
      last = m;
    this.m_pos = (pos + last);

    buf = this.m_buf;

    byteIndex = (pos >>> 3);
    bitIndex = (pos & 7);

    res = (buf[byteIndex] & 0xff);
    res >>>= bitIndex;
    bitIndex = (8 - bitIndex);
    while (last > bitIndex) {
      res |= ((buf[++byteIndex] & 0xff) << bitIndex);
      bitIndex += 8;
    }

    return (res & MASK[last]);
  }

  /**
   * Reads the next byte of data from this input stream. The value byte is
   * returned as an <code>int</code> in the range <code>0</code> to
   * <code>255</code>. If no byte is available because the end of the
   * stream has been reached, the value <code>-1</code> is returned.
   * <p>
   * This <code>read</code> method cannot block.
   * 
   * @return the next byte of data, or <code>-1</code> if the end of the
   *         stream has been reached.
   */
  @Override
  public int read() {
    if (this.m_pos >= this.m_last)
      return -1;
    return this.readBits(8);
  }

  /**
   * Reads up to <code>len</code> bytes of data into an array of bytes
   * from this input stream.
   * 
   * @param b
   *          the buffer into which the data is read.
   * @param off
   *          the start offset of the data.
   * @param len
   *          the maximum number of bytes read.
   * @return the total number of bytes read into the buffer, or
   *         <code>-1</code> if there is no more data because the end of
   *         the stream has been reached.
   */
  @Override
  public int read(final byte b[], final int off, final int len) {
    int l, p, r;

    for (l = len, p = off; l > 0; l--, p++) {
      r = this.read();
      if (r == -1)
        return (p - off);
      b[p] = ((byte) r);
    }
    return len;
  }

  /**
   * Skips <code>n</code> bytes of input from this input stream. Fewer
   * bytes might be skipped if the end of the input stream is reached. The
   * actual number <code>k</code> of bytes to be skipped is returned.
   * 
   * @param n
   *          the number of bytes to be skipped.
   * @return the actual number of bytes skipped.
   */
  @Override
  public long skip(final long n) {
    return this.skipBytes((int) n);
  }

  /**
   * Skips <code>n</code> bits of input from this input stream. Fewer
   * bits might be skipped if the end of the input stream is reached. The
   * actual number <code>k</code> of bits to be skipped is returned.
   * 
   * @param n
   *          the number of bits to be skipped.
   * @return the actual number of bits skipped.
   */
  public int skipBits(final int n) {
    int k;

    k = Math.min(this.m_last - this.m_pos, n);
    this.m_pos += k;

    return k;
  }

  /**
   * Returns the number of bytes that can be read from this input stream.
   * 
   * @return the number of bytes that can be read from the input stream
   */
  @Override
  public int available() {
    return ((this.m_last - this.m_pos) >>> 3);
  }

  /**
   * Returns the number of bits that can be read from this input stream.
   * 
   * @return the number of bits that can be read from the input stream
   */
  public int availableBits() {
    return (this.m_last - this.m_pos);
  }

  /**
   * Tests if this <code>InputStream</code> supports mark/reset. The
   * <code>markSupported</code> method of
   * <code>ByteArrayInputStream</code> always returns <code>true</code>.
   */
  @Override
  public boolean markSupported() {
    return true;
  }

  /**
   * Set the current marked position in the stream. ByteArrayInputStream
   * objects are marked at position zero by default when constructed. They
   * may be marked at another position within the buffer by this method.
   * <p>
   * If no mark has been set, then the value of the mark is the offset
   * passed to the constructor (or 0 if the offset was not supplied).
   * <p>
   * Note: The <code>readAheadLimit</code> for this class has no meaning.
   */
  @Override
  public void mark(int readAheadLimit) {
    this.m_mark = this.m_pos;
  }

  /**
   * Resets the buffer to the marked position. The marked position is 0
   * unless another position was marked or an offset was specified in the
   * constructor.
   */
  @Override
  public void reset() {
    this.m_pos = this.m_mark;
  }

  /**
   * Reads some number of bytes from the input stream and stores them into
   * the buffer array <code>b</code>. The number of bytes actually read
   * is returned as an integer.
   * 
   * @param b
   *          the buffer into which the data is read.
   * @return the total number of bytes read into the buffer, or
   *         <code>-1</code> is there is no more data because the end of
   *         the stream has been reached.
   */
  @Override
  public int read(byte b[]) {
    return read(b, 0, b.length);
  }

  /**
   * Reads some bytes from an input stream and stores them into the buffer
   * array <code>b</code>.
   * 
   * @param b
   *          the buffer into which the data is read.
   */
  public void readFully(final byte b[]) {
    this.read(b);
  }

  /**
   * Reads <code>len</code> bytes from an input stream.
   * 
   * @param b
   *          the buffer into which the data is read.
   * @param off
   *          an int specifying the offset into the data.
   * @param len
   *          an int specifying the number of bytes to read.
   */
  public void readFully(final byte b[], final int off, final int len) {
    this.read(b, off, len);
  }

  /**
   * Makes an attempt to skip over <code>n</code> bytes of data from the
   * input stream, discarding the skipped bytes.
   * 
   * @param n
   *          the number of bytes to be skipped.
   * @return the number of bytes actually skipped.
   */
  public int skipBytes(final int n) {
    int k;

    k = Math.min(this.m_last - this.m_pos, n << 3);
    this.m_pos += k;

    return (k >>> 3);
  }

  /**
   * Reads one input bit and returns <code>true</code> if that bit is
   * nonzero, <code>false</code> if that byte is zero.
   * 
   * @return the <code>boolean</code> value read.
   */
  public boolean readBoolean() {
    return (this.readBits(1) != 0);
  }

  /**
   * Reads and returns one input byte. The byte is treated as a signed
   * value in the range <code>-128</code> through <code>127</code>,
   * inclusive.
   * 
   * @return the 8-bit value read.
   */
  public byte readByte() {
    return (byte) (this.readBits(8));
  }

  /**
   * Reads one input byte, zero-extends it to type <code>int</code>, and
   * returns the result, which is therefore in the range <code>0</code>
   * through <code>255</code>.
   * 
   * @return the unsigned 8-bit value read.
   */
  public int readUnsignedByte() {
    return this.readBits(8);
  }

  /**
   * Reads two input bytes and returns a <code>short</code> value.
   * 
   * @return the 16-bit value read.
   */
  public short readShort() {
    return ((short) (this.readBits(16)));
  }

  /**
   * Reads two input bytes and returns an <code>int</code> value in the
   * range <code>0</code> through <code>65535</code>.
   * 
   * @return the unsigned 16-bit value read.
   */
  public int readUnsignedShort() {
    return this.readBits(16);
  }

  /**
   * Reads an input <code>char</code> and returns the <code>char</code>
   * value. A Unicode <code>char</code> is made up of two bytes.
   * 
   * @return the Unicode <code>char</code> read.
   */
  public char readChar() {
    return ((char) (this.readBits(16)));
  }

  /**
   * Reads four input bytes and returns an <code>int</code> value.
   * 
   * @return the <code>int</code> value read.
   */
  public int readInt() {
    return this.readBits(32);
  }

  /**
   * Reads eight input bytes and returns a <code>long</code> value.
   * 
   * @return the <code>long</code> value read.
   */
  public long readLong() {
    return (((this.readBits(32) & 0xffffffffL) << 32L) | (this
        .readBits(32) & 0xffffffffL));
  }

  /**
   * Reads four input bytes and returns a <code>float</code> value.
   * 
   * @return the <code>float</code> value read.
   */
  public float readFloat() {
    return Float.intBitsToFloat(this.readInt());
  }

  /**
   * Reads eight input bytes and returns a <code>double</code> value.
   * 
   * @return the <code>double</code> value read.
   */
  public double readDouble() {
    return Double.longBitsToDouble(this.readLong());
  }

  /**
   * Reads the next line of text from the input stream. It reads successive
   * bytes, converting each byte separately into a character, until it
   * encounters a line terminator or end of file; the characters read are
   * then returned as a <code>String</code>. Note that because this
   * method processes bytes, it does not support input of the full Unicode
   * character set.
   * 
   * @return the next line of text from the input stream, or <CODE>null</CODE>
   *         if the end of file is encountered before a byte can be read.
   */
  public String readLine() {
    char[] lineBuffer;
    char buf[];

    buf = lineBuffer = new char[128];

    int room = buf.length;
    int offset = 0;
    int c, p;

    loop: while (true) {
      switch (c = this.read()) {
      case -1:
      case '\n':
        break loop;

      case '\r':
        p = this.m_pos;
        int c2 = this.read();
        if ((c2 != '\n') && (c2 != -1)) {
          this.m_pos = p;
        }
        break loop;

      default:
        if (--room < 0) {
          buf = new char[offset + 128];
          room = buf.length - offset - 1;
          System.arraycopy(lineBuffer, 0, buf, 0, offset);
          lineBuffer = buf;
        }
        buf[offset++] = (char) c;
        break;
      }
    }
    if ((c == -1) && (offset == 0)) {
      return null;
    }
    return String.copyValueOf(buf, 0, offset);

  }

  /**
   * Reads in a string that has been encoded using a <a
   * href="#modified-utf-8">modified UTF-8</a> format. The general
   * contract of <code>readUTF</code> is that it reads a representation
   * of a Unicode character string encoded in modified UTF-8 format; this
   * string of characters is then returned as a <code>String</code>.
   * 
   * @return a Unicode string.
   */
  public String readUTF() {
    int len;
    StringBuilder sb;

    len = this.readBits(16);
    sb = new StringBuilder(len);

    for (; len > 0; len--)
      sb.append(this.readChar());

    return sb.toString();
  }
}
