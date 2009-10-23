/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2007-02-23
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.gui.ImagePane.java
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

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

/**
 * This is a primitive control to display an image.
 * 
 * @author Thomas Weise
 */
public  class ImagePane extends JComponent {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1L;

  /** the image to display */
  private Image m_image;

  /** true if the image may be stretched to fit size requirements */
  private boolean m_stretchable;

  /**
   * Create a new image pane.
   * 
   * @param image
   *          The image to be displayed.
   * @param stretchable
   *          True if the image may be stretched to fit size requirements.
   */
  public ImagePane(final Image image, final boolean stretchable) {
    super();
    this.m_image = image;
  }

  /**
   * Returns the image displayed by this control.
   * 
   * @return The displayed image.
   */
  public Image getImage() {
    return this.m_image;
  }

  /**
   * Sets the image to be displayed by this view.
   * 
   * @param image
   *          The new image to display.
   */
  public void setImage(final Image image) {
    Container c;

    this.m_image = image;
    invalidate();
    c = getParent();
    if (c == null)
      validate();
    else
      c.validate();
  }

  /**
   * Returns wether this image can be stretched to accomodate size
   * requirements or not.
   * 
   * @return true if this image can be stretched, false otherwise.
   */
  public boolean isStretchable() {
    return this.m_stretchable;
  }

  /**
   * Sets the stretchable behaviour of this component.
   * 
   * @param stretchable
   *          True if this component may now be stretched, false otherwise.
   */
  public void setStretchable(final boolean stretchable) {
    Container c;
    this.m_stretchable = stretchable;
    invalidate();
    c = getParent();
    if (c == null)
      validate();
    else
      c.validate();
  }

  /**
   * If the preferred size has been set to a non-null value just returns
   * it. If the image is stretchable, the super implementations value is
   * returned.
   * 
   * @return the value of the minimumSize property
   * @see ComponentUI
   */
  @Override
  public Dimension getMinimumSize() {
    if (this.m_stretchable)
      return super.getMinimumSize();
    return getPreferredSize();
  }

  /**
   * If the preferred size has been set to a non-null value just returns
   * it. If the UI delegate's getPreferedSize method returns a non-null
   * value then return that; otherwise defer to the component's layout
   * manager.
   * 
   * @return the value of the preferredSize property
   * @see ComponentUI
   */
  @Override
  public Dimension getPreferredSize() {
    Dimension d;

    d = super.getPreferredSize();

    d.width = Math.max(d.width, this.m_image.getWidth(this));
    d.height = Math.max(d.height, this.m_image.getHeight(this));

    return d;
  }

  /**
   * If the preferred size has been set to a non-null value just returns
   * it. If the image is stretchable, the super implementations value is
   * returned.
   * 
   * @return the value of the minimumSize property
   * @see ComponentUI
   */
  @Override
  public Dimension getMaximumSize() {
    Dimension d;

    if (this.m_stretchable) {
      d = super.getMaximumSize();
      d.width = Math.max(d.width, this.m_image.getWidth(this));
      d.height = Math.max(d.height, this.m_image.getHeight(this));
      return d;
    }

    return getPreferredSize();
  }

  /**
   * Invoked by Swing to draw components. Applications should not invoke
   * paint directly, but should instead use the repaint method to schedule
   * the component for redrawing. *
   * 
   * @param g
   *          the Graphics context in which to paint
   */
  @Override
  public void paint(final Graphics g) {
    Rectangle r;

    r = g.getClipBounds();

    if (r != null) {
      g.drawImage(this.m_image, r.x, r.y, r.width, r.height, this);
    } else {
      g.drawImage(this.m_image, 0, 0, this);
    }
  }
}