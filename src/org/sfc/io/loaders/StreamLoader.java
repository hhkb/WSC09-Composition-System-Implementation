/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.io.loaders.StreamLoader.java
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

package org.sfc.io.loaders;

import java.io.InputStream;

import org.sfc.io.IO;

/**
 * This one is allows you to load byte streams completely into a byte
 * array. You can also concat multiple streams that way.
 * 
 * @author Thomas Weise
 */
public class StreamLoader extends Loader<byte[], InputStream> {

  /**
   * Create a new StreamLoader.
   */
  public StreamLoader() {
    super();
  }

  /**
   * Create a new StreamLoader.
   * 
   * @param bufferSize
   *          The initial read buffer size. You can leave this this
   *          parameter 0 and a reasonable default will be used.
   */
  public StreamLoader(final int bufferSize) {
    super(bufferSize);
  }

  /**
   * A convenient method to load streams identified by an object.
   * 
   * @param source
   *          The source object to be loaded.
   * @return true if everything went ok, false otherwise.
   */
  @Override
  public boolean load(final Object source) {
    return loadFrom(IO.getInputStream(source));
  }

  /**
   * Create a data array of the given size
   * 
   * @param size
   *          the size of the data array
   * @return the new array
   */
  @Override
  byte[] createArray(final int size) {
    return new byte[size];
  }

  /**
   * Fill a given buffer with data read from a source.
   * 
   * @param source
   *          the source to read rom
   * @param dest
   *          the destination array
   * @return the number of items read
   * @throws Exception
   *           if something goes wrong
   */
  @Override
  int readFromSource(final InputStream source, final byte[] dest)
      throws Exception {
    return source.read(dest);
  }

  /**
   * Close the given source
   * 
   * @param source
   *          the source to close
   * @throws Exception
   *           if something goes wrong
   */
  @Override
  void closeSource(final InputStream source) throws Exception {
    source.close();
  }
}
