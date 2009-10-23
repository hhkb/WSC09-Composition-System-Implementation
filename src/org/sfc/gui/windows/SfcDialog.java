/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.gui.windows.SfcDialog.java
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

import java.awt.Container;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.WindowConstants;

import org.sfc.scoped.ICloseable;

/**
 * This is the stk implementation of <code>JDialog</code>, incorporating
 * the <code>IWindow</code> interface.
 * 
 * @author Thomas Weise
 */
public class SfcDialog extends JDialog implements ICloseable, IWindow {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the internal closeable instance
   */
  private ICloseable m_closeable;

  /**
   * Create a new <code>SfcDialog</code>.
   * 
   * @param owner
   *          The owning frame of the dialog.
   */
  public SfcDialog(final Frame owner) {
    super(owner);
    this.m_closeable = new WindowCloseable(this);
    this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    this.addWindowListener(SfcFrame.CLOSER);
  }

  /**
   * Returns the icon associated to the window.
   * 
   * @return The icon of the window or null if no icon is assigned.
   */
  public Image getIcon() {
    return null;
  }

  /**
   * Sets the icon of the window.
   * 
   * @param icon
   *          The icon of the window.
   */
  public void setIcon(Image icon) {
    //
  }

  /**
   * Returns the underlying os-window of the <code>IWindow</code>.
   * 
   * @return The window-instance of this <code>IWindow</code>.
   */
  public Window getWindow() {
    return this;
  }

  /**
   * Returns the underlying frame of the <code>IWindow</code>.
   * 
   * @return The frame-instance of this <code>IWindow</code>.
   */
  public Frame getFrame() {
    Window w;
    Container c;

    w = getOwner();
    if (w == null)
      return null;

    if (w instanceof Frame) {
      return ((Frame) w);
    }

    c = w.getParent();
    while (c != null) {
      if (c instanceof Frame)
        return ((Frame) c);
      c = c.getParent();
    }

    c = this;
    while (c != null) {
      if (c instanceof Frame)
        return ((Frame) c);
      c = c.getParent();
    }

    return null;
  }

  /**
   * Close the object. You must not perform any operations with the object
   * afterwards nor should you allow any reference to point on this object.
   * Also you must not access any member variables of the object. Call this
   * method when you don't need an object anymore, allowing it to perform
   * some cleanup.
   */
  public synchronized void close() {
    this.m_closeable.close();
    this.m_closeable = null;
  }

  /**
   * This method must only be called for windows that have been closed and
   * disposed.
   */
  public synchronized void reuse() {
    if (this.m_closeable != null)
      throw new IllegalStateException();
    this.m_closeable = new WindowCloseable(this);
  }

  /**
   * This method will be called when the user decided to close the window
   * via some input like hitting the "close"-button in the system menu. You
   * can emulate a user-close action by calling this method. This method
   * closes the dialog. You may override it to provide alternative
   * behavior.
   */
  public void closeByUser() {
    this.close();
  }
}
