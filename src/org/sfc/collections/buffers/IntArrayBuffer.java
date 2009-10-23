/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2007-10-10
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.collections.buffers.IntArrayBuffer.java
 * Last modification: 2007-10-10
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

package org.sfc.collections.buffers;

/**
 * A buffer for integer arrays of the same size
 * 
 * @author Thomas Weise
 */
public class IntArrayBuffer extends SynchronizedObjectBuffer<int[]> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the integer array size
   */
  private final int m_size;

  /**
   * Create a new int array buffer
   * 
   * @param size
   *          the buffer size
   */
  public IntArrayBuffer(final int size) {
    super();
    this.m_size = size;
  }

  /**
   * Create an instance of the heavy weight object
   * 
   * @return the instance of the heavy weight object
   */
  @Override
  protected int[] create() {
    return new int[this.m_size];
  }

}
