/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2007-10-17
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.io.writerProvider.SingleTextWriterProvider.java
 * Last modification: 2007-10-17
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

package org.sfc.io.writerProvider;

import org.sfc.io.TextWriter;

/**
 * A text writer provider that always returns the same text writer.
 * 
 * @author Thomas Weise
 */
public class SingleTextWriterProvider implements
    IWriterProvider<TextWriter> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1L;

  /**
   * the text writer to provider
   */
  private final TextWriter m_writer;

  /**
   * Create a new single text writer
   * 
   * @param writer
   *          the text writer to return
   */
  public SingleTextWriterProvider(final TextWriter writer) {
    super();
    this.m_writer = writer;
  }

  /**
   * Obtain the next output writer.
   * 
   * @param info
   *          An object containing information to be used for the writer
   *          creation. This could be an instance of {@link Integer} or a
   *          string
   * @return the next output writer
   */
  public TextWriter provideWriter(final Object info) {
    return this.m_writer;
  }
}
