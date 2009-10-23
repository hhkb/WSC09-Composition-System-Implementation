/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.gui.windows.IWindow.java
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

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.IllegalComponentStateException;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.MenuContainer;
import java.awt.Window;
import java.awt.im.InputContext;
import java.awt.image.ImageObserver;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.Locale;

import javax.accessibility.Accessible;
import javax.swing.JMenuBar;
import javax.swing.RootPaneContainer;

import org.sfc.scoped.ICloseable;

/**
 * This interface is common to all stk windows. It allows you to treat all
 * windows the same way, no matter if they are <code>JFrame</code>s,
 * <code>JDialog</code>s or even <code>JInternalFrame</code>s.
 *
 * @author Thomas Weise
 */
public interface IWindow extends ICloseable, Accessible, MenuContainer,
                                 RootPaneContainer, ImageObserver, 
                                 Serializable
  {
/**
 * Sets the menubar for this window.
 *
 * @param menubar the menubar being placed in the window
 */
  public  abstract  void          setJMenuBar   (final JMenuBar menubar);

/**
 * Returns the menubar set on this window.
 *
 * @return the menubar for this window
 */
  public  abstract  JMenuBar      getJMenuBar       ();

/**
 * Returns the icon associated to the window.
 *
 * @return  The icon of the window or null if no icon is assigned.
 */
  public  abstract  Image         getIcon          ();

/**
 * Sets the icon of the window.
 *
 * @param icon  The icon of the window.
 */
  public  abstract  void          setIcon          (final  Image  icon);

/**
 * The pack method of the window.
 */
  public  abstract  void          pack              ();

/**
 * Returns the underlying os-window of the <code>IWindow</code>.
 * @return  The window-instance of this <code>IWindow</code>.
 */
  public  abstract  Window        getWindow        ();

/**
 * Returns the underlying frame of the <code>IWindow</code>.
 * @return  The frame-instance of this <code>IWindow</code>.
 */
  public  abstract  Frame         getFrame         ();

/**
 * Sets wether the window is visible or not.
 * @param visible <code>true</code> to show the window,
 *                  <code>false</code> to hide the window.
 */
  public  abstract  void          setVisible     (final boolean visible);

/**
 * Returns wether the window is visible or not.
 * @return  <code>true</code> if the window is visible, <code>false</code>
 *          if it is hidden.
 */
  public  abstract  boolean       isVisible        ();

/**
 * This method will be called when the user decided to close the window
 * via some input like hitting the "close"-button in the system menu.
 * You can emulate a user-close action by calling this method. The exact
 * behavior of this method is not defined, it may or may not call the
 * <code>close()</code> method of the window.
 */
  public  abstract  void          closeByUser     ();

/**
 * Set the size of the frame.
 * @param width   The new width of the frame.
 * @param height  The new height of the frame.
 */
  public  abstract  void          setSize          (final  int width,
                                                    final  int height);

/**
 * Returns the current width of the frame.
 * @return The current width of the frame.
 */
  public  abstract  int           getWidth         ();

/**
 * Returns the current height of the frame.
 * @return The current height of the frame.
 */
  public  abstract  int           getHeight        ();
  
      
    
/**
 * Gets the title of the frame.  The title is displayed in the
 * frame's border.
 * @return    the title of this frame, or an empty string ("")
 *                if this frame doesn't have a title.
 * @see       #setTitle(String)
 */
  public abstract  String getTitle() ;
    
/**
 * Indicates whether this frame is resizable by the user.  
 * By default, all frames are initially resizable. 
 * @return    <code>true</code> if the user can resize this frame; 
 *                        <code>false</code> otherwise.
 * @see       java.awt.Frame#setResizable(boolean)
 */
  public abstract  boolean isResizable();
    
    
    
/**
 * Sets whether this frame is resizable by the user.  
 * @param    resizable   <code>true</code> if this frame is resizable; 
 *                       <code>false</code> otherwise.
 * @see      java.awt.Frame#isResizable
 */
  public abstract  void setResizable(final  boolean resizable);
    
/**
 * Sets the title for this frame to the specified string.
 * @param title the title to be displayed in the frame's border.
 *              A <code>null</code> value
 *              is treated as an empty string, "".
 * @see      #getTitle
 */
  public abstract  void setTitle(final  String title);
  
  


/**
 * If this Window is visible, brings this Window to the front and may make
 * it the focused Window.
 * <p>
 * Places this Window at the top of the stacking order and shows it in
 * front of any other WindowManager in this RBGPNetVM. No action will take place if this
 * Window is not visible. Some platforms do not allow WindowManager which own
 * other WindowManager to appear on top of those owned WindowManager. Some platforms
 * may not permit this RBGPNetVM to place its WindowManager above windows of native
 * applications, or WindowManager of other VMs. This permission may depend on
 * whether a Window in this RBGPNetVM is already focused. Every attempt will be
 * made to move this Window as high as possible in the stacking order;
 * however, developers should not assume that this method will move this
 * Window above all other windows in every situation.
 * <p>
 * Because of variations in native windowing systems, no guarantees about
 * changes to the focused and active WindowManager can be made. Developers must
 * never assume that this Window is the focused or active Window until this
 * Window receives a WINDOW_GAINED_FOCUS or WINDOW_ACTIVATED event. On
 * platforms where the top-most window is the focused window, this method
 * will <b>probably</b> focus this Window, if it is not already focused. On
 * platforms where the stacking order does not typically affect the focused
 * window, this method will <b>probably</b> leave the focused and active
 * WindowManager unchanged.
 * <p>
 * If this method causes this Window to be focused, and this Window is a
 * Frame or a Dialog, it will also become activated. If this Window is
 * focused, but it is not a Frame or a Dialog, then the first Frame or
 * Dialog that is an owner of this Window will be activated.
 *
 * @see       #toBack
 */
  public abstract void toFront();

/**
 * If this Window is visible, sends this Window to the back and may cause
 * it to lose focus or activation if it is the focused or active Window.
 * <p>
 * Places this Window at the bottom of the stacking order and shows it
 * behind any other WindowManager in this RBGPNetVM. No action will take place is this
 * Window is not visible. Some platforms do not allow WindowManager which are
 * owned by other WindowManager to appear below their owners. Every attempt will
 * be made to move this Window as low as possible in the stacking order;
 * however, developers should not assume that this method will move this
 * Window below all other windows in every situation.
 * <p>
 * Because of variations in native windowing systems, no guarantees about
 * changes to the focused and active WindowManager can be made. Developers must
 * never assume that this Window is no longer the focused or active Window
 * until this Window receives a WINDOW_LOST_FOCUS or WINDOW_DEACTIVATED
 * event. On platforms where the top-most window is the focused window,
 * this method will <b>probably</b> cause this Window to lose focus. In
 * that case, the next highest, focusable Window in this RBGPNetVM will receive
 * focus. On platforms where the stacking order does not typically affect
 * the focused window, this method will <b>probably</b> leave the focused
 * and active WindowManager unchanged.
 *
 * @see       #toFront
 */
  public abstract void toBack();

/**
 * Gets the input context for this window. A window always has an input context,
 * which is shared by subcomponents unless they create and set their own.
 * @return  The input context.
 * @see Component#getInputContext
 * @since 1.2
 */
  public abstract InputContext getInputContext();

/**
 * Set the cursor image to a specified cursor.
 * @param     cursor One of the constants defined
 *            by the <code>Cursor</code> class. If this parameter is null
 *            then the cursor for this window will be set to the type
 *            Cursor.DEFAULT_CURSOR.
 * @see       Component#getCursor
 * @see       Cursor
 * @since     JDK1.1
 */
  public abstract void setCursor(final  Cursor cursor);

 
/**
 * Adds a PropertyChangeListener to the listener list. The listener is
 * registered for all bound properties of this class, including the
 * following:
 * <ul>
 *    <li>this Window's font ("font")</li>
 *    <li>this Window's background color ("background")</li>
 *    <li>this Window's foreground color ("foreground")</li>
 *    <li>this Window's focusability ("focusable")</li>
 *    <li>this Window's focus traversal keys enabled state
 *        ("focusTraversalKeysEnabled")</li>
 *    <li>this Window's Set of FORWARD_TRAVERSAKEYS
 *        ("forwardFocusTraversalKeys")</li>
 *    <li>this Window's Set of BACKWARD_TRAVERSAKEYS
 *        ("backwardFocusTraversalKeys")</li>
 *    <li>this Window's Set of UCYCLE_TRAVERSAKEYS
 *        ("upCycleFocusTraversalKeys")</li>
 *    <li>this Window's Set of DOWN_CYCLE_TRAVERSAKEYS
 *        ("downCycleFocusTraversalKeys")</li>
 *    <li>this Window's focus traversal policy ("focusTraversalPolicy")
 *        </li>
 *    <li>this Window's focusable Window state ("focusableWindowState")
 *        </li>
 *    <li>this Window's always-on-top state("alwaysOnTop")</li>
 * </ul>
 * Note that if this Window is inheriting a bound property, then no
 * event will be fired in response to a change in the inherited property.
 * <p>
 * If listener is null, no exception is thrown and no action is performed.
 *
 * @param    listener  the PropertyChangeListener to be added
 *
 * @see #addPropertyChangeListener(java.lang.String,java.beans.PropertyChangeListener)
 */
  public abstract void addPropertyChangeListener(
                          final PropertyChangeListener listener);
 
/**
 * Adds a PropertyChangeListener to the listener list for a specific
 * property. The specified property may be user-defined, or one of the
 * following:
 * <ul>
 *    <li>this Window's font ("font")</li>
 *    <li>this Window's background color ("background")</li>
 *    <li>this Window's foreground color ("foreground")</li>
 *    <li>this Window's focusability ("focusable")</li>
 *    <li>this Window's focus traversal keys enabled state
 *        ("focusTraversalKeysEnabled")</li>
 *    <li>this Window's Set of FORWARD_TRAVERSAKEYS
 *        ("forwardFocusTraversalKeys")</li>
 *    <li>this Window's Set of BACKWARD_TRAVERSAKEYS
 *        ("backwardFocusTraversalKeys")</li>
 *    <li>this Window's Set of UCYCLE_TRAVERSAKEYS
 *        ("upCycleFocusTraversalKeys")</li>
 *    <li>this Window's Set of DOWN_CYCLE_TRAVERSAKEYS
 *        ("downCycleFocusTraversalKeys")</li>
 *    <li>this Window's focus traversal policy ("focusTraversalPolicy")
 *        </li>
 *    <li>this Window's focusable Window state ("focusableWindowState")
 *        </li>
 *    <li>this Window's always-on-top state("alwaysOnTop")</li>
 * </ul>
 * Note that if this Window is inheriting a bound property, then no
 * event will be fired in response to a change in the inherited property.
 * <p>
 * If listener is null, no exception is thrown and no action is performed.
 *
 * @param property_name one of the property names listed above
 * @param listener the PropertyChangeListener to be added
 *
 * @see #addPropertyChangeListener(java.beans.PropertyChangeListener)
 */
  public abstract void addPropertyChangeListener(
          final String                 property_name,
          final PropertyChangeListener listener);
 
    
    
/**
 * Set the bounds.
 * @param x The x-origin.
 * @param y The y-origin.
 * @param width The width.
 * @param height  The height.
 */
  public abstract void setBounds(final  int x,
                                 final  int y,
                                 final  int width,
                                 final  int height);

  

/** 
 * Returns the preferred size of this container.  
 * @return    an instance of <code>Dimension</code> that represents 
 *                the preferred size of this container.
 * @see       #getMinimumSize    
 * @see       LayoutManager#preferredLayoutSize(Container)
 * @see       Component#getPreferredSize
 */
  public abstract Dimension getPreferredSize();

    
/** 
 * Returns the minimum size of this container.  
 * @return    an instance of <code>Dimension</code> that represents 
 *                the minimum size of this container.
 * @see       #getPreferredSize    
 * @see       LayoutManager#minimumLayoutSize(Container)
 * @see       Component#getMinimumSize
 * @since     JDK1.1
 */
  public abstract Dimension getMinimumSize();

  
/** 
 * Returns the maximum size of this container.  
 * @return  The maximum size.
 * @see #getPreferredSize
 */
  public abstract Dimension getMaximumSize();
  
/**
 * Sets the locale of this component.  This is a bound property.
 * @param l the locale to become this component's locale
 */
  public abstract void setLocale(final Locale l);
  
/**
 * Gets the locale of this component.
 * @return this component's locale; if this component does not
 *          have a locale, the locale of its parent is returned
 * @see #setLocale
 * @exception IllegalComponentStateException if the <code>Component</code>
 *          does not have its own locale and has not yet been added to
 *          a containment hierarchy such that the locale can be determined
 *          from the containing parent
 * @since  JDK1.1
 */
  public abstract Locale getLocale();
  
/**
 * This method must only be called for windows that have been closed and
 * disposed.
 */
  public abstract void reuse();
  }