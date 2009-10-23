/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-22 10:19:28
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.events.IEventSource.java
 * Version          : 1.0.0
 * Last modification: 2006-11-22
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

package org.sigoa.spec.events;

/**
 * This event allows to event listeners to register in order to receive
 * events from this source.
 *
 * @author Thomas Weise
 */
public interface IEventSource {
  /**
   * Register an event lister that should receive events from this source.
   *
   * @param listener
   *          The event listener that should now be able to receive events
   *          from this source.
   * @throws NullPointerException
   *           if <code>listener</code> is <code>null</code>.
   */
  public abstract void addEventListener(final IEventListener listener);

  /**
   * Detach an event listener from this event source.
   *
   * @param listener
   *          The event listener to be detached from this event source.
   * @throws NullPointerException
   *           if <code>listener</code> is <code>null</code>.
   */
  public abstract void removeEventListener(final IEventListener listener);
}
