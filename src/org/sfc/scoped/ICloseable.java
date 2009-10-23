/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.scoped.ICloseable.java
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

package org.sfc.scoped;

import java.io.Closeable;

/**
 * This interface is common to all objects that can be closed, such as
 * frames.
 * 
 * @author Thomas Weise
 */
public interface ICloseable extends Closeable {

  /**
   * Close the object. You must not perform any operations with the object
   * afterwards nor should you allow any reference to point on this object.
   * Also you must not access any member variables of the object. Call this
   * method when you don't need an object anymore, allowing it to perform
   * some cleanup.
   * 
   * @throws AlreadyDisposedException
   *           If the object has already been disposed.
   */
  public abstract void close() throws AlreadyDisposedException;
}
