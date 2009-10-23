/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2007-02-23
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.gui.ComponentUtils.java
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

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;

import javax.swing.JButton;

import org.sfc.gui.resources.icons.Icons;
import org.sfc.gui.windows.IWindow;
import org.sfc.utils.ErrorUtils;

/**
 * This class provides utils for graphical components.
 * 
 * @author Thomas Weise
 */
public final class ComponentUtils {
  /** the top inset to be used normally */
  public static final int INSET_TOP = 4;

  /** the left inset to be used normally */
  public static final int INSET_LEFT = INSET_TOP;

  /** the bottom inset to be used normally */
  public static final int INSET_BOTTOM = INSET_TOP;

  /** the right inset to be used normally */
  public static final int INSET_RIGHT = INSET_TOP;

  /** This insets used by default for grid layouts */
  public static final Insets INSETS = new Insets(INSET_TOP, INSET_LEFT,
      INSET_BOTTOM, INSET_RIGHT);

  /** This insets used by default for grid layouts that have no insets */
  public static final Insets NOINSETS = new Insets(0, 0, 0, 0);

  /** The default padding x */
  private static final int DEFAULT_IPAD_X = 1;

  /** The default padding y */
  private static final int DEFAULT_IPAD_Y = 1;

  /**
   * Find the window instance that matches to the given object.
   * 
   * @param o
   *          the object to use as root for searching
   * @return the window that object belongs to, or <code>null</code> if
   *         none exists/could be found
   */
  public static final IWindow getWindow(final Object o) {
    Component c;

    if (o instanceof IWindow)
      return ((IWindow) o);
    if (o instanceof Component) {
      for (c = ((Component) o); c != null; c = c.getParent()) {
        if (c instanceof IWindow)
          return ((IWindow) c);
      }
    }
    return null;
  }

  /**
   * Puts the grid constraints for a component.
   * 
   * @param container
   *          The container to add the component to.
   * @param component
   *          The component to create the constraints for.
   * @param gridX
   *          The initial gridx value.
   * @param gridY
   *          The initial gridy value.
   * @param gridWidth
   *          The initial gridwidth value.
   * @param gridHeight
   *          The initial gridheight value.
   * @param weightX
   *          The initial weightx value.
   * @param weightY
   *          The initial weighty value.
   * @param anchor
   *          The initial anchor value.
   * @param fill
   *          The initial fill value.
   * @param insets
   *          The insets to be used.
   */
  public static final void insetPutGrid(final Container container,
      final Component component, final int gridX, final int gridY,
      final int gridWidth, final int gridHeight, final double weightX,
      final double weightY, final int anchor, final int fill,
      final Insets insets) {
    LayoutManager l;
    l = container.getLayout();
    if (l instanceof GridBagLayout) {
      ((GridBagLayout) (l)).setConstraints(component,
          new GridBagConstraints(gridX, gridY, gridWidth, gridHeight,
              weightX, weightY, anchor, fill, insets, DEFAULT_IPAD_X,
              DEFAULT_IPAD_Y));
    }
    container.add(component);
  }

  /**
   * Puts the grid constraints for a component.
   * 
   * @param container
   *          The container to add the component to.
   * @param component
   *          The component to create the constraints for.
   * @param gridX
   *          The initial gridx value.
   * @param gridY
   *          The initial gridy value.
   * @param gridWidth
   *          The initial gridwidth value.
   * @param gridHeight
   *          The initial gridheight value.
   * @param weightX
   *          The initial weightx value.
   * @param weightY
   *          The initial weighty value.
   * @param anchor
   *          The initial anchor value.
   * @param fill
   *          The initial fill value.
   */
  public static final void putGrid(final Container container,
      final Component component, final int gridX, final int gridY,
      final int gridWidth, final int gridHeight, final double weightX,
      final double weightY, final int anchor, final int fill) {
    LayoutManager l;
    l = container.getLayout();
    if (l instanceof GridBagLayout) {
      ((GridBagLayout) (l)).setConstraints(component,
          new GridBagConstraints(gridX, gridY, gridWidth, gridHeight,
              weightX, weightY, anchor, fill, INSETS, DEFAULT_IPAD_X,
              DEFAULT_IPAD_Y));
    }
    container.add(component);
  }

  /**
   * Puts the grid constraints for a component.
   * 
   * @param container
   *          The container to add the component to.
   * @param component
   *          The component to create the constraints for.
   * @param gridX
   *          The initial gridx value.
   * @param gridY
   *          The initial gridy value.
   * @param gridWidth
   *          The initial gridwidth value.
   * @param gridHeight
   *          The initial gridheight value.
   * @param weightX
   *          The initial weightx value.
   * @param weightY
   *          The initial weighty value.
   * @param anchor
   *          The initial anchor value.
   * @param fill
   *          The initial fill value.
   */
  public static final void noInsetsPutGrid(final Container container,
      final Component component, final int gridX, final int gridY,
      final int gridWidth, final int gridHeight, final double weightX,
      final double weightY, final int anchor, final int fill) {
    LayoutManager l;
    l = container.getLayout();
    if (l instanceof GridBagLayout) {
      ((GridBagLayout) (l)).setConstraints(component,
          new GridBagConstraints(gridX, gridY, gridWidth, gridHeight,
              weightX, weightY, anchor, fill, NOINSETS, DEFAULT_IPAD_X,
              DEFAULT_IPAD_Y));
    }
    container.add(component);
  }

  /**
   * Create a new ok button.
   * 
   * @return a new ok button.
   */
  public static final JButton createOkButton() {
    JButton jb;

    jb = new JButton("Ok"); //$NON-NLS-1$
    jb.setIcon(Icons.OK);
    return jb;
  }

  /**
   * The component utils forbidden constructor
   */
  private ComponentUtils() {
    ErrorUtils.doNotCall();
  }

}
