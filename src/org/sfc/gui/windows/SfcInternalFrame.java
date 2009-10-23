/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.gui.windows.SfcInternalFrame.java
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
import java.io.Serializable;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import org.sfc.scoped.DefaultCloseable;
import org.sfc.scoped.ICloseable;

/**
 * This is the stack implementation of <code>JInternalFrame</code>,
 * incorporating the <code>IWindow</code> interface.
 * 
 * @author Thomas Weise
 */
public class SfcInternalFrame extends JInternalFrame implements IWindow {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The globally shared window listener constant
   */
  private static final InternalFrameListener CLOSER = new InternalFrameListener() {

    public void internalFrameOpened(InternalFrameEvent e) {
      //
    }

    public void internalFrameClosing(InternalFrameEvent e) {
      ((IWindow) (e.getInternalFrame())).closeByUser();
    }

    public void internalFrameClosed(InternalFrameEvent e) {
      //
    }

    public void internalFrameIconified(InternalFrameEvent e) {
      //
    }

    public void internalFrameDeiconified(InternalFrameEvent e) {
      //
    }

    public void internalFrameActivated(InternalFrameEvent e) {
      //
    }

    public void internalFrameDeactivated(InternalFrameEvent e) {
      //
    }
  };

  /**
   * the internal closeable instance
   */
  private ICloseable m_closeable;

  /**
   * Create a new <code>SfcInternalFrame</code>.
   */
  public SfcInternalFrame() {
    super();
    this.m_closeable = new InternalFrameCloseable();
    this.addInternalFrameListener(CLOSER);
  }

  /**
   * Returns the icon associated to the window.
   * 
   * @return The icon of the window or null if no icon is assigned.
   */
  public Image getIcon() {
    Icon icon;

    icon = this.getFrameIcon();
    if (icon instanceof ImageIcon) {
      return ((ImageIcon) icon).getImage();
    }

    return null;
  }

  /**
   * Sets the icon of the window.
   * 
   * @param icon
   *          The icon of the window.
   */
  public void setIcon(final Image icon) {
    this.setFrameIcon(new ImageIcon(icon));
  }

  /**
   * Returns the underlying os-window of the <code>IWindow</code>.
   * 
   * @return The window-instance of this <code>IWindow</code>.
   */
  public Window getWindow() {
    Container c;

    c = getParent();
    while (c != null) {
      if (c instanceof Window)
        return ((Window) c);
      if (c instanceof IWindow) {
        return ((IWindow) c).getWindow();
      }
      c = c.getParent();
    }

    return SwingUtilities.getWindowAncestor(this);
  }

  /**
   * Returns the underlying frame of the <code>IWindow</code>.
   * 
   * @return The frame-instance of this <code>IWindow</code>.
   */
  public Frame getFrame() {
    Container c;

    c = getParent();
    while (c != null) {
      if (c instanceof Frame)
        return ((Frame) c);
      if (c instanceof IWindow) {
        return ((IWindow) c).getFrame();
      }
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
  }

  /**
   * This method will be called when the user decided to close the window
   * via some input like hitting the "close"-button in the system menu. You
   * can emulate a user-close action by calling this method. This method
   * closes the window and exists the system. You might override it to
   * alter behavior.
   */
  public void closeByUser() {
    this.close();
  }

  /**
   * This method must only be called for windows that have been closed and
   * disposed.
   */
  public synchronized void reuse() {
    if (this.m_closeable != null)
      throw new IllegalStateException();
    this.m_closeable = new InternalFrameCloseable();
  }

  /**
   * The internal frame closeable
   * 
   * @author Thomas Weise
   */
  private final class InternalFrameCloseable extends DefaultCloseable
      implements Serializable {
    /**
     * The serial version uid.
     */
    private static final long serialVersionUID = 1;

    /**
     * the constructor
     */
    InternalFrameCloseable() {
      super();
    }

    /**
     * This method is automatically called when the object gets disposed.
     * You should perform your cleanup in this method. If you override this
     * method, don't forget to call the super method.
     */
    @Override
    protected void dispose() {
      SfcInternalFrame.this.dispose();
      super.dispose();
    }
  }
}
