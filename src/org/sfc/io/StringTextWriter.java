/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-24
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.io.StringTextWriter.java
 * Last modification: 2007-09-24
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

import java.io.StringWriter;

/**
 * A convenience class representing a text writer writing to a string
 * buffer.
 * 
 * @author Thomas Weise
 */
public class StringTextWriter extends TextWriter {

  /**
   * the constructor
   */
  public StringTextWriter() {
    super(new StringWriter());
  }

  /**
   * Obtain the contents written to this text writer
   * 
   * @return the contents written to this text writer
   */
  @Override
  public String toString() {
    this.flush();
    return this.m_out.toString();
  }

  /**
   * Obtain the underlying string buffer of this text writer
   * 
   * @return the underlying string buffer of this text writer
   */
  public StringBuffer getBuffer() {
    this.flush();
    return ((StringWriter) (this.m_out)).getBuffer();
  }

}
