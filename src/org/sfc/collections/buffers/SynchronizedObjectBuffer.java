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
public abstract class SynchronizedObjectBuffer<OT> extends
    ObjectBuffer<OT> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new buffer provider
   */
  protected SynchronizedObjectBuffer() {
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
    
    synchronized(this){
    e = this.m_buffer;
    if (e != null) {
      this.m_buffer = e.m_next;
      e.m_next = this.m_unused;
      this.m_unused = e;
      return e.m_object;
    }}

    return this.create();
  }

  /**
   * Dispose an object
   * 
   * @param object
   *          the object to be disposed
   */
  @Override
  public synchronized void dispose(final OT object) {
    super.dispose(object);
  }
}
