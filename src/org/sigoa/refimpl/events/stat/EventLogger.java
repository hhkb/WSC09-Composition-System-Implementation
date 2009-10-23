/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-08-24
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.events.stat.EventLogger.java
 * Last modification: 2007-08-24
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

import java.io.Serializable;
import java.util.logging.Level;

import org.sfc.text.TextUtils;
import org.sfc.utils.Log;
import org.sigoa.spec.events.IErrorEvent;
import org.sigoa.spec.events.IEvent;
import org.sigoa.spec.events.IEventListener;

/**
 * This event listener writes events to the default log
 *
 * @author Thomas Weise
 */
public class EventLogger implements Serializable, IEventListener {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the globally shared default instance of the event logger
   */
  public static final IEventListener EVENT_LOGGER = new EventLogger();

  /**
   * Create the event logger
   */
  protected EventLogger() {
    super();
  }

  /**
   * Receive an event.
   *
   * @param event
   *          The event to be processed.
   */
  public void receiveEvent(final IEvent event) {
    Object o;

    o = event.getEventSource();

    this.log(null,
        "eventQueue", //$NON-NLS-1$
        event.getCreationTime(),
        ((event instanceof IErrorEvent) ? ((IErrorEvent) event).getError()
            : null), null, ((o != null) ? TextUtils.classToString(o
            .getClass()) : null), null);
  }

  /**
   * Log something.
   *
   * @param level
   *          the log level (optional, if not used <code>null</code>)
   * @param message
   *          the message (optional, if not used <code>null</code>)
   * @param millis
   *          the system time (optional, if not used <code>-1</code>)
   * @param error
   *          the error (optional, if not used <code>null</code>)
   * @param thread
   *          the thread (optional, if not used <code>null</code>)
   * @param sourceClass
   *          the source class name (optional, if not used
   *          <code>null</code>)
   * @param sourceMethod
   *          the source method name (optional, if not used
   *          <code>null</code>)
   */
  public void log(final Level level, final String message,
      final long millis, final Throwable error, final Thread thread,
      final String sourceClass, final String sourceMethod) {
    try {
      Log.getLog().log(level, message, millis, error, thread, sourceClass,
          sourceMethod);
    } catch (Throwable tt) {//
    }
  }

  /**
   * Resolve this comparator at deserialization.
   *
   * @return The right comparator instance.
   */
  private final Object readResolve() {
    if (this.getClass() == EventLogger.class)
      return EventLogger.EVENT_LOGGER;
    return this;
  }

}
