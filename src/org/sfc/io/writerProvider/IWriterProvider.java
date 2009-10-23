/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-12-28
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.pipe.stat.IWriterProvider.java
 * Last modification: 2006-12-28
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

import java.io.Serializable;
import java.io.Writer;

/**
 * Instances of this interface supply activities with writers that they can
 * use to store their stuff in.
 * 
 * @param <T>
 *          the type of writer provided. This can be for example a normal
 *          {@link java.io.Writer}, a {@link org.sfc.io.TextWriter} or a
 *          writer or for example a {@link org.sfc.xml.sax.SAXWriter}.
 * @author Thomas Weise
 */
public interface IWriterProvider<T extends Writer> extends Serializable {

  /**
   * Obtain the next output writer.
   * 
   * @param info
   *          An object containing information to be used for the writer
   *          creation. This could be an instance of {@link Integer} or a
   *          string
   * @return the next output writer
   */
  public abstract T provideWriter(final Object info);
}
