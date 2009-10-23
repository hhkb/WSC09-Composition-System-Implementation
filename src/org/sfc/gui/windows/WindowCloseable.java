/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.gui.windows.WindowCloseable.java
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

package org.sfc.gui.windows;

import java.awt.Window;
import java.io.Serializable;

import org.sfc.scoped.DefaultCloseable;

/**
 * The internal closeable for windows
 * 
 * @author Thomas Weise
 */
final class WindowCloseable extends DefaultCloseable implements
    Serializable {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The underlying windw
   */
  private final Window m_window;

  /**
   * Create a new window closeable.
   * 
   * @param window
   *          The window this closeable works on.
   */
  WindowCloseable(final Window window) {
    super();
    this.m_window = window;
  }

  /**
   * This method is automatically called when the object gets disposed. You
   * should perform your cleanup in this method. If you override this
   * method, don't forget to call the super method.
   */
  @Override
  protected void dispose() {
    this.m_window.dispose();
    super.dispose();
  }
}
