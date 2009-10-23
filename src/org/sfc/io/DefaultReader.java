/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.io.DefaultReader.java
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
import java.io.Reader;

/**
 * This class is a wrapper for <code>Reader</code>s that makes them
 * reference counted.
 * 
 * @author Thomas Weise
 */
public class DefaultReader extends ReferenceCountedReader {
  /**
   * The internal reader.
   */
  private final Reader m_in;

  /**
   * Create a reference counted <code>Reader</code>.
   * 
   * @param original
   *          The underlying object.
   */
  public DefaultReader(final Reader original) {
    super();
    this.m_in = original;
    this.lock = original;
  }

  /**
   * Dispose this reader.
   * 
   * @throws IOException
   *           If the underlying was closed and caused an IOException.
   */
  @Override
  protected void dispose() throws IOException {
    try {
      this.m_in.close();
    } finally {
      super.dispose();
    }
  }

  /**
   * Read a single character.
   * 
   * @return The character.
   * @exception IOException
   *              If an I/O error occurs
   */
  @Override
  public int read() throws IOException {
    return this.m_in.read();
  }

  /**
   * Read characters into a portion of an array.
   * 
   * @param cbuf
   *          The buffer to fill with data.
   * @param off
   *          The offset into the buffer where to begin to write.
   * @param len
   *          The count of characters to read.
   * @return The count of characters actually read.
   * @exception IOException
   *              If an I/O error occurs
   */
  @Override
  public int read(final char[] cbuf, final int off, final int len)
      throws IOException {
    return this.m_in.read(cbuf, off, len);
  }

  /**
   * Skip characters.
   * 
   * @param n
   *          The count of characters to skip.
   * @return The count of characters actually skipped.
   * @exception IOException
   *              If an I/O error occurs
   */
  @Override
  public long skip(final long n) throws IOException {
    return this.m_in.skip(n);
  }

  /**
   * Tell whether this stream is ready to be read.
   * 
   * @return <code>true</code> if this stream is ready to be read.
   * @exception IOException
   *              If an I/O error occurs
   */
  @Override
  public boolean ready() throws IOException {
    return this.m_in.ready();
  }

  /**
   * Tell whether this stream supports the mark() operation.
   * 
   * @return <code>true</code> if marking is supported by the underlying
   *         stream.
   */
  @Override
  public boolean markSupported() {
    return this.m_in.markSupported();
  }

  /**
   * Mark the present position in the stream.
   * 
   * @param read_ahead_limit
   *          The read ahead limit.
   * @exception IOException
   *              If an I/O error occurs
   */
  @Override
  public void mark(final int read_ahead_limit) throws IOException {
    this.m_in.mark(read_ahead_limit);
  }

  /**
   * Reset the stream.
   * 
   * @exception IOException
   *              If an I/O error occurs
   */
  @Override
  public void reset() throws IOException {
    this.m_in.reset();
  }
}
