/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2007-10-10
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.collections.buffers.SynchronizedObjectBuffer.java
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
 * An object buffer holds heavy-weight objects which can be reused
 * 
 * @param <OT>
 *          the object type
 * @author Thomas Weise
 */
public abstract class ObjectBuffer<OT> extends BasicBuffer<OT> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the buffer
   */
  transient Entry<OT> m_buffer;

  /**
   * the unused objects
   */
  transient Entry<OT> m_unused;

  /**
   * Create a new buffer provider
   */
  protected ObjectBuffer() {
    super();
  }

  /**
   * Allocate a new object
   * 
   * @return the new object
   */
  @Override
  public OT allocate() {
    Entry<OT> e;
    e = this.m_buffer;
    if (e != null) {
      this.m_buffer = e.m_next;
      e.m_next = this.m_unused;
      this.m_unused = e;
      return e.m_object;
    }

    return this.create();
  }

  /**
   * Dispose an object
   * 
   * @param object
   *          the object to be disposed
   */
  @Override
  public void dispose(final OT object) {
    Entry<OT> e;

    e = this.m_unused;
    if (e != null)
      this.m_unused = e.m_next;
    else
      e = new Entry<OT>();

    e.m_object = object;
    e.m_next = this.m_buffer;
    this.m_buffer = e;
  }

  /**
   * the entry class
   * 
   * @param <OTX>
   *          the entry type
   * @author Thomas Weise
   */
  static final class Entry<OTX> {
    /**
     * the object
     */
    OTX m_object;

    /**
     * the next entry
     */
    Entry<OTX> m_next;

    /**
     * Create a new entry
     */
    Entry() {
      super();
    }
  }
}
