/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-14
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.collections.buffers.BasicBuffer.java
 * Last modification: 2007-11-14
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

import java.io.Serializable;

import org.sfc.text.Textable;

/**
 * The basic buffer class.
 * 
 * @param <OT>
 *          the object type
 * @author Thomas Weise
 */
public abstract class BasicBuffer<OT> extends Textable implements
    Serializable {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * Allocate a new object
   * 
   * @return the new object
   */
  public abstract OT allocate();

  /**
   * Dispose an object
   * 
   * @param object
   *          the object to be disposed
   */
  public abstract void dispose(final OT object);

  /**
   * Create a new instance of the buffered object type.
   * 
   * @return the new instance, or <code>null</code> if the creation
   *         failed somehow
   */
  protected abstract OT create();
}
