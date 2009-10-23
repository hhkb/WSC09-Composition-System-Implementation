/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.io.DefaultInputStream.java
 * Last modification: 2006-11-26
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
import java.io.InputStream;

/**
 * This class is a wrapper for <code>InputStream</code>s that makes them
 * reference counted.
 * 
 * @author Thomas Weise
 */
public class DefaultInputStream extends ReferenceCountedInputStream {
  /**
   * The internal input stream.
   */
  private final InputStream m_is;

  /**
   * Create a reference counted <code>InputStream</code>.
   * 
   * @param original
   *          The underlying object.
   */
  public DefaultInputStream(final InputStream original) {
    super();
    this.m_is = original;
  }

  /**
   * Dispose the reference counted input stream.
   * 
   * @throws IOException
   *           If the underlying was closed and caused an IOException.
   */
  @Override
  protected void dispose() throws IOException {
    try {
      this.m_is.close();
    } finally {
      super.dispose();
    }
  }

  /**
   * Reads the next byte of data from this input stream. The value byte is
   * returned as an <code>int</code> in the range <code>0</code> to
   * <code>255</code>. If no byte is available because the end of the
   * stream has been reached, the value <code>-1</code> is returned. This
   * method blocks until input data is available, the end of the stream is
   * detected, or an exception is thrown.
   * <p>
   * This method simply performs <code>in.read()</code> and returns the
   * result.
   * 
   * @return the next byte of data, or <code>-1</code> if the end of the
   *         stream is reached.
   * @exception IOException
   *              if an I/O error occurs.
   */
  @Override
  public int read() throws IOException {
    return this.m_is.read();
  }

  /**
   * Reads up to <code>byte.length</code> bytes of data from this input
   * stream into an array of bytes. This method blocks until some input is
   * available.
   * <p>
   * This method simply performs the call <code>read(b, 0, b.length)</code>
   * and returns the result. It is important that it does <i>not</i> do
   * <code>in.read(b)</code> instead; certain subclasses of
   * <code>DefaultInputStream</code> depend on the implementation
   * strategy actually used.
   * 
   * @param b
   *          the buffer into which the data is read.
   * @return the total number of bytes read into the buffer, or
   *         <code>-1</code> if there is no more data because the end of
   *         the stream has been reached.
   * @exception IOException
   *              if an I/O error occurs.
   * @see DefaultInputStream#read(byte[], int, int)
   */
  @Override
  public int read(final byte b[]) throws IOException {
    return this.m_is.read(b);
  }

  /**
   * Reads up to <code>len</code> bytes of data from this input stream
   * into an array of bytes. This method blocks until some input is
   * available.
   * <p>
   * This method simply performs <code>in.read(b, off, len)</code> and
   * returns the result.
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
   * @exception IOException
   *              if an I/O error occurs.
   */
  @Override
  public int read(final byte b[], final int off, final int len)
      throws IOException {
    return this.m_is.read(b, off, len);
  }

  /**
   * Skips over and discards <code>n</code> bytes of data from the input
   * stream. The <code>skip</code> method may, for a variety of reasons,
   * end up skipping over some smaller number of bytes, possibly
   * <code>0</code>. The actual number of bytes skipped is returned.
   * <p>
   * This method simply performs <code>in.skip(n)</code>.
   * 
   * @param n
   *          the number of bytes to be skipped.
   * @return the actual number of bytes skipped.
   * @exception IOException
   *              if an I/O error occurs.
   */
  @Override
  public long skip(final long n) throws IOException {
    return this.m_is.skip(n);
  }

  /**
   * Returns the number of bytes that can be read from this input stream
   * without blocking.
   * <p>
   * This method simply performs <code>in.available()</code> and returns
   * the result.
   * 
   * @return the number of bytes that can be read from the input stream
   *         without blocking.
   * @exception IOException
   *              if an I/O error occurs.
   */
  @Override
  public int available() throws IOException {
    return this.m_is.available();
  }

  /**
   * Marks the current position in this input stream. A subsequent call to
   * the <code>reset</code> method repositions this stream at the last
   * marked position so that subsequent reads re-read the same bytes.
   * <p>
   * The <code>readlimit</code> argument tells this input stream to allow
   * that many bytes to be read before the mark position gets invalidated.
   * <p>
   * This method simply performs <code>in.mark(readlimit)</code>.
   * 
   * @param readlimit
   *          the maximum limit of bytes that can be read before the mark
   *          position becomes invalid.
   * @see DefaultInputStream#reset()
   */
  @Override
  public void mark(final int readlimit) {
    this.m_is.mark(readlimit);
  }

  /**
   * Repositions this stream to the position at the time the
   * <code>mark</code> method was last called on this input stream.
   * <p>
   * This method simply performs <code>in.reset()</code>.
   * <p>
   * Stream marks are intended to be used in situations where you need to
   * read ahead a little to see what's in the stream. Often this is most
   * easily done by invoking some general parser. If the stream is of the
   * type handled by the parse, it just chugs along happily. If the stream
   * is not of that type, the parser should toss an exception when it
   * fails. If this happens within readlimit bytes, it allows the outer
   * code to reset the stream and try another parser.
   * 
   * @exception IOException
   *              if the stream has not been marked or if the mark has been
   *              invalidated.
   * @see DefaultInputStream#mark(int)
   */
  @Override
  public void reset() throws IOException {
    this.m_is.reset();
  }

  /**
   * Tests if this input stream supports the <code>mark</code> and
   * <code>reset</code> methods. This method simply performs
   * <code>in.markSupported()</code>.
   * 
   * @return <code>true</code> if this stream type supports the
   *         <code>mark</code> and <code>reset</code> method;
   *         <code>false</code> otherwise.
   * @see java.io.InputStream#mark(int)
   * @see java.io.InputStream#reset()
   */
  @Override
  public boolean markSupported() {
    return this.m_is.markSupported();
  }
}
