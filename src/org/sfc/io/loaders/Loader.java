/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2007-10-21
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.io.loaders.Loader.java
 * Last modification: 2007-10-21
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

/**
 * The internal base class for loaders. It allows you to load given streams
 * completely into memory, and to concatenate them consecutively. It is
 * reusable.
 * 
 * @param <AT>
 *          the array type
 * @param <ST>
 *          the source type
 * @author Thomas Weise
 */
abstract class Loader<AT, ST> {

  /**
   * the default buffer size
   */
  protected static final int DEFAULT_BUFFER_SIZE = (8 * 1024);

  /**
   * the buffer
   */
  private AT m_buffer;

  /**
   * the internal data array
   */
  private AT m_data;

  /**
   * the length
   */
  private int m_length;

  /**
   * the data length
   */
  private int m_dl;

  /**
   * Create a new Loader.
   */
  public Loader() {
    this(DEFAULT_BUFFER_SIZE);
  }

  /**
   * Create a new Loader.
   * 
   * @param bufferSize
   *          The initial read buffer size. You can leave this this
   *          parameter 0 and a reasonable default will be used.
   */
  public Loader(final int bufferSize) {
    super();

    int bs;

    if (bufferSize <= 0)
      bs = DEFAULT_BUFFER_SIZE;
    else
      bs = bufferSize;

    this.m_dl = bs;

    this.m_data = this.createArray(bs);
    this.m_buffer = this.createArray(bs);
  }

  /**
   * Create a data array of the given size
   * 
   * @param size
   *          the size of the data array
   * @return the new array
   */
  abstract AT createArray(final int size);

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
  abstract int readFromSource(final ST source, final AT dest)
      throws Exception;

  /**
   * Close the given source
   * 
   * @param source
   *          the source to close
   * @throws Exception
   *           if something goes wrong
   */
  abstract void closeSource(final ST source) throws Exception;

  /**
   * Obtain the loaded data
   * 
   * @return the data loaded
   */
  public AT getData() {
    return this.m_data;
  }

  /**
   * Obtain the length of the data read.
   * 
   * @return the length of the data read
   */
  public int getLength() {
    return this.m_length;
  }

  /**
   * Load all the data from the given source.
   * 
   * @param source
   *          the source to load from
   * @return <code>true</code> if and only if loading was successful,
   *         <code>false</code> otherwise
   */
  public abstract boolean load(final Object source);

  /**
   * Load data from the given source
   * 
   * @param source
   *          the source
   * @return <code>true</code> if and only if loading was successful,
   *         <code>false</code> otherwise
   */
  public boolean loadFrom(final ST source) {
    int i, j, l;
    AT b, d;

    try {
      j = this.m_length;
      d = this.m_data;
      b = this.m_buffer;

      while ((i = this.readFromSource(source, b)) > 0) {
        l = (j + i);
        if (l >= this.m_dl) {
          d = this.createArray(this.m_dl = (l << 1));
          System.arraycopy(this.m_data, 0, d, 0, j);
          this.m_data = d;
        }

        System.arraycopy(b, 0, d, j, i);
        j = l;
      }

      this.m_length = j;
      return true;
    } catch (Exception ioe) {
      return false;
    } finally {
      try {
        this.closeSource(source);
      } catch (Exception ioe2) {
        return false;
      }
    }
  }

  /**
   * Reset this loader so it can be reused
   */
  public void reset() {
    this.m_length = 0;
  }
}
