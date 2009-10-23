/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-28
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.events.stat.EventPrinter.java
 * Last modification: 2006-12-28
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

package org.sigoa.refimpl.events.stat;

import org.sfc.io.TextWriter;
import org.sigoa.spec.events.IEvent;
import org.sigoa.spec.events.IEventListener;

/**
 * An event printer is a text writer that is able to output events
 *
 * @author Thomas Weise
 */
public class EventPrinter extends TextWriter implements IEventListener {

  /**
   * Create a new event printer.
   *
   * @param out
   *          The output destination.
   */
  public EventPrinter(final Object out) {
    super(out);
  }

  /**
   * Receive an event.
   *
   * @param event
   *          The event to be processed.
   */
  public synchronized void receiveEvent(final IEvent event) {
    this.writeObject(event);
    this.flush();
  }

}
