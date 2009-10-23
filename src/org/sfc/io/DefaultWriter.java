/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.io.DefaultWriter.java
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
import java.io.Writer;

/**
 * This class is a wrapper for <code>Writer</code>s that makes them
 * reference counted.
 * 
 * @author Thomas Weise
 */
public class DefaultWriter extends ReferenceCountedWriter {
  /**
   * The internal writer.
   */
  final Writer m_out;

  /**
   * Create a reference counted <code>Writer</code>.
   * 
   * @param original
   *          The underlying object.
   * @throws NullPointerException
   *           if <code>out==null</code>
   */
  public DefaultWriter(final Writer original) {
    super();
    if (original == null)
      throw new NullPointerException();
    this.m_out = original;
  }

  /**
   * Return the name of the character encoding being used by this stream.
   * <p>
   * If the encoding has an historical name then that name is returned;
   * otherwise the encoding's canonical name is returned.
   * <p>
   * If this instance was created with the constructor then the returned
   * name, being unique for the encoding, may differ from the name passed
   * to the constructor. This method may return <tt>null</tt> if the
   * stream has been closed.
   * </p>
   * 
   * @return The historical name of this encoding, or possibly
   *         <code>null</code> if the stream has been closed
   */
  @Override
  public String getEncoding() {
    return defaultGetEncoding(this.m_out);
  }

  /**
   * Dispose this reference counted writer.
   * 
   * @throws IOException
   *           If the underlying was closed and caused an IOException.
   */
  @Override
  protected void dispose() throws IOException {
    try {
      this.m_out.close();
    } finally {
      super.dispose();
    }
  }

  /**
   * Write a single character.
   * 
   * @param c
   *          The character to be written.
   * @exception IOException
   *              If an I/O error occurs
   */
  @Override
  public void write(final int c) throws IOException {
    this.m_out.write(c);
  }

  /**
   * Write a portion of an array of characters.
   * 
   * @param cbuf
   *          Buffer of characters to be written
   * @param off
   *          Offset from which to start reading characters
   * @param len
   *          Number of characters to be written
   * @exception IOException
   *              If an I/O error occurs
   */
  @Override
  public void write(final char[] cbuf, final int off, final int len)
      throws IOException {
    this.m_out.write(cbuf, off, len);
  }

  /**
   * Write a portion of a string.
   * 
   * @param str
   *          String to be written
   * @param off
   *          Offset from which to start reading characters
   * @param len
   *          Number of characters to be written
   * @exception IOException
   *              If an I/O error occurs
   */
  @Override
  public void write(final String str, final int off, final int len)
      throws IOException {
    this.m_out.write(str, off, len);
  }

  /**
   * Flush the stream.
   * 
   * @exception IOException
   *              If an I/O error occurs
   */
  @Override
  public void flush() throws IOException {
    this.m_out.flush();
  }
}
