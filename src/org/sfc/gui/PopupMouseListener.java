/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2007-02-23
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.gui.PopupMouseListener.java
 * Last modification: 2007-02-23
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

package org.sfc.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;

import javax.swing.JPopupMenu;

/**
 * This class is a mouse listener that can be used to trigger popup menus.
 * 
 * @author Thomas Weise
 */
public class PopupMouseListener implements MouseListener, Serializable {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1L;

  /**
   * the popup menu
   */
  private final JPopupMenu m_menu;

  /**
   * create a new popup mouse listener
   * 
   * @param pm
   *          the popupmenu
   */
  public PopupMouseListener(final JPopupMenu pm) {
    super();
    this.m_menu = pm;
  }

  /**
   * Check whether the event triggers a popup menu or not
   * 
   * @param e
   *          the mouse event of concern
   * @return <code>true</code> if and only if the event triggers the
   *         popup menu, <code>false</code> otherwise
   */
  protected boolean shouldPopup(final MouseEvent e) {
    return e.isPopupTrigger();
  }

  /**
   * popup the popup menu
   * 
   * @param e
   *          the mouse event of concern
   */
  protected void popup(final MouseEvent e) {
    this.m_menu.show(e.getComponent(), e.getX(), e.getY());
  }

  /**
   * Concern a mouse event an perform the required actions
   * 
   * @param e
   *          the mouse event of concern
   */
  protected void processMouseEvent(final MouseEvent e) {
    if (this.shouldPopup(e))
      this.popup(e);
  }

  /**
   * Invoked when the mouse button has been clicked (pressed and released)
   * on a component.
   * 
   * @param e
   *          the mouse event
   */
  public void mouseClicked(MouseEvent e) {
    this.processMouseEvent(e);
  }

  /**
   * Invoked when a mouse button has been pressed on a component.
   * 
   * @param e
   *          the mouse event
   */
  public void mousePressed(MouseEvent e) {
    this.processMouseEvent(e);
  }

  /**
   * Invoked when a mouse button has been released on a component.
   * 
   * @param e
   *          the mouse event
   */
  public void mouseReleased(MouseEvent e) {
    this.processMouseEvent(e);
  }

  /**
   * Invoked when the mouse enters a component.
   * 
   * @param e
   *          the mouse event
   */
  public void mouseEntered(MouseEvent e) {
    this.processMouseEvent(e);
  }

  /**
   * Invoked when the mouse exits a component.
   * 
   * @param e
   *          the mouse event
   */
  public void mouseExited(MouseEvent e) {
    this.processMouseEvent(e);
  }

}
