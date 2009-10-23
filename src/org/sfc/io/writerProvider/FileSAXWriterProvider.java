/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-12-28
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.pipe.stat.writerProviders.FileSAXWriterProvider.java
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

import org.sfc.io.CanonicalFile;
import org.sfc.xml.sax.SAXWriter;

/**
 * This text writer provider generates output files that are situated in a
 * specified directory. It returns {@link org.sfc.xml.sax.SAXWriter}s
 * 
 * @author Thomas Weise
 */
public class FileSAXWriterProvider extends
    FileBasedWriterProvider<SAXWriter> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1L;

  /**
   * Create a new file text writer provider.
   * 
   * @param directory
   *          the directory to place the files in
   * @param prefix
   *          the file name prefix
   */
  public FileSAXWriterProvider(final Object directory, final String prefix) {
    super(directory, prefix, ".xml"); //$NON-NLS-1$
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
  public SAXWriter provideWriter(final Object info) {
    CanonicalFile g;

    g = this.createFile(info);

    return ((g != null) ? new SAXWriter(g) : null);
  }
}
