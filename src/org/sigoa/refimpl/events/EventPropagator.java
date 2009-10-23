/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-25
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.events.EventPropagator.java
 * Last modification: 2006-11-25
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

package org.sigoa.refimpl.events;

import java.io.Serializable;

import org.sfc.collections.lists.SimpleList;
import org.sigoa.spec.events.IEvent;
import org.sigoa.spec.events.IEventListener;
import org.sigoa.spec.events.IEventSource;

/**
 * The default implementation of the event source interface.
 *
 * @author Thomas Weise
 */
public class EventPropagator implements Serializable, IEventSource,
    IEventListener {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * The internal list of event listeners.
   */
  private final SimpleList<IEventListener> m_listeners;

  /**
   * Create a new event source.
   */
  public EventPropagator() {
    super();
    this.m_listeners = new SimpleList<IEventListener>();// CollectionUtils.createList();
  }

  /**
   * Register an event lister that should receive events from this source.
   *
   * @param listener
   *          The event listener that should now be able to receive events
   *          from this source.
   * @throws NullPointerException
   *           if <code>listener</code> is <code>null</code>.
   */
  public synchronized void addEventListener(final IEventListener listener) {
    if (listener == null)
      throw new NullPointerException();
    this.m_listeners.add(listener);
  }

  /**
   * Detach an event listener from this event source.
   *
   * @param listener
   *          The event listener to be detached from this event source.
   */
  public synchronized void removeEventListener(
      final IEventListener listener) {
    if (listener == null)
      throw new NullPointerException();
    this.m_listeners.removeFast(listener);// remove(listener);
  }

  /**
   * Propagate an event to all listeners.
   *
   * @param event
   *          The event to be processed.
   * @throws NullPointerException
   *           if <code>listener</code> is <code>null</code>.
   */
  public synchronized void receiveEvent(final IEvent event) {
    int i;
    SimpleList<IEventListener> l;

    l = this.m_listeners;
    for (i = (l.size() - 1); i >= 0; i--) {
      l.get(i).receiveEvent(event);
    }
  }
}
