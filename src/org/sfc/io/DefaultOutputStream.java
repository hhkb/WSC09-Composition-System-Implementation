/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.io.DefaultOutputStream.java
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
import java.io.OutputStream;

/**
 * This class is a wrapper for <code>OutputStream</code>s that makes
 * them reference counted.
 * 
 * @author Thomas Weise
 */
public class DefaultOutputStream extends ReferenceCountedOutputStream {
  /**
   * The internal output-stream.
   */
  private final OutputStream m_os;

  /**
   * Create a reference counted <code>OutputStream</code>.
   * 
   * @param original
   *          The underlying object.
   */
  public DefaultOutputStream(final OutputStream original) {
    super();
    this.m_os = original;
  }

  /**
   * The internal dispose method.
   * 
   * @throws IOException
   *           If the underlying was closed and caused an IOException.
   */
  @Override
  protected void dispose() throws IOException {
    try {
      this.m_os.close();
    } finally {
      super.dispose();
    }
  }

  /**
   * Writes the specified <code>byte</code> to this output stream.
   * <p>
   * The <code>write</code> method of <code>DefaultOutputStream</code>
   * calls the <code>write</code> method of its underlying output stream,
   * that is, it performs <tt>out.write(b)</tt>.
   * <p>
   * Implements the abstract <tt>write</tt> method of
   * <tt>OutputStream</tt>.
   * 
   * @param b
   *          the <code>byte</code>.
   * @exception IOException
   *              if an I/O error occurs.
   */
  @Override
  public void write(int b) throws IOException {
    this.m_os.write(b);
  }

  /**
   * Writes <code>b.length</code> bytes to this output stream.
   * <p>
   * The <code>write</code> method of <code>DefaultOutputStream</code>
   * calls its <code>write</code> method of three arguments with the
   * arguments <code>b</code>, <code>0</code>, and
   * <code>b.length</code>.
   * <p>
   * Note that this method does not call the one-argument
   * <code>write</code> method of its underlying stream with the single
   * argument <code>b</code>.
   * 
   * @param b
   *          the data to be written.
   * @exception IOException
   *              if an I/O error occurs.
   * @see DefaultOutputStream#write(byte[], int, int)
   */
  @Override
  public void write(final byte[] b) throws IOException {
    this.m_os.write(b);
  }

  /**
   * Writes <code>len</code> bytes from the specified <code>byte</code>
   * array starting at offset <code>off</code> to this output stream.
   * <p>
   * The <code>write</code> method of <code>DefaultOutputStream</code>
   * calls the <code>write</code> method of one argument on each
   * <code>byte</code> to output.
   * <p>
   * Note that this method does not call the <code>write</code> method of
   * its underlying input stream with the same arguments. Subclasses of
   * <code>DefaultOutputStream</code> should provide a more efficient
   * implementation of this method.
   * 
   * @param b
   *          the data.
   * @param off
   *          the start offset in the data.
   * @param len
   *          the number of bytes to write.
   * @exception IOException
   *              if an I/O error occurs.
   * @see DefaultOutputStream#write(int)
   */
  @Override
  public void write(final byte[] b, final int off, final int len)
      throws IOException {
    this.m_os.write(b, off, len);
  }

  /**
   * Flushes this output stream and forces any buffered output bytes to be
   * written out to the stream.
   * <p>
   * The <code>flush</code> method of <code>DefaultOutputStream</code>
   * calls the <code>flush</code> method of its underlying output stream.
   * 
   * @exception IOException
   *              if an I/O error occurs.
   */
  @Override
  public void flush() throws IOException {
    this.m_os.flush();
  }
}
