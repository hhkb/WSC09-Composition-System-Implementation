/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.gui.windows.SfcFrame.java
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

import java.awt.Frame;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.sfc.scoped.ICloseable;

/**
 * This is the stk implementation of <code>JFrame</code>, incorporating
 * the <code>IWindow</code> interface.
 * 
 * @author Thomas Weise
 */
public class SfcFrame extends JFrame implements IWindow {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The globally shared window listener constant
   */
  static final WindowListener CLOSER = new WindowListener() {
    public void windowOpened(final WindowEvent e) {
      //
    }

    public void windowClosing(final WindowEvent e) {
      ((IWindow) (e.getWindow())).closeByUser();
    }

    public void windowClosed(final WindowEvent e) {
      //
    }

    public void windowIconified(final WindowEvent e) {
      //
    }

    public void windowDeiconified(final WindowEvent e) {
      //
    }

    public void windowActivated(final WindowEvent e) {
      //
    }

    public void windowDeactivated(final WindowEvent e) {
      //
    }

  };

  /**
   * the internal closeable instance
   */
  private ICloseable m_closeable;

  /**
   * Create a new <code>SfcFrame</code>.
   */
  public SfcFrame() {
    super();
    this.m_closeable = new WindowCloseable(this);
    this.addWindowListener(CLOSER);
  }

  /**
   * Returns the icon associated to the window.
   * 
   * @return The icon of the window or null if no icon is assigned.
   */
  public Image getIcon() {
    return this.getIconImage();
  }

  /**
   * Sets the icon of the window.
   * 
   * @param icon
   *          The icon of the window.
   */
  public void setIcon(Image icon) {
    this.setIconImage(icon);
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
    return this;
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
  }

  /**
   * This method will be called when the user decided to close the window
   * via some input like hitting the "close"-button in the system menu. You
   * can emulate a user-close action by calling this method. This method
   * closes the window and exists the system. You might override it to
   * alter behavior.
   */
  public synchronized void closeByUser() {
    this.close();
    if (this.getDefaultCloseOperation() == EXIT_ON_CLOSE) {
      System.exit(0);
    }
  }

  /**
   * Sets the operation that will happen by default when the user initiates
   * a "close" on this frame.
   * 
   * @param operation
   *          the operation which should be performed when the user closes
   *          the frame
   * @exception IllegalArgumentException
   *              if defaultCloseOperation value isn't one of the above
   *              valid values
   * @see WindowConstants
   * @throws SecurityException
   *           if <code>EXIT_ON_CLOSE</code> has been specified and the
   *           <code>SecurityManager</code> will not allow the caller to
   *           invoke <code>System.exit</code>
   */
  @Override
  public synchronized void setDefaultCloseOperation(int operation) {
    super.setDefaultCloseOperation(operation);
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

}
