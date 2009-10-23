/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-25
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.events.ErrorEvent.java
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

import org.sfc.text.TextUtils;
import org.sfc.utils.ErrorUtils;
import org.sigoa.spec.events.IErrorEvent;

/**
 * The default implementation of the <code>IErrorEvent</code>-interface.
 *
 * @author Thomas Weise
 */
public class ErrorEvent extends Event implements IErrorEvent {

  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The error stored in this event.
   */
  private final Throwable m_error;

  /**
   * Create a new error event. The source of the event should be an object
   * which can easily be serialized without dragging many other objects
   * into this. Normally, <code>IEvent</code>s are used in the context
   * of optimization processes where the source will be the id assigned to
   * the optimization process.
   *
   * @param aSource
   *          The object on which caused the error.
   * @param error
   *          The error.
   * @throws NullPointerException
   *           if aSource is null.
   */
  public ErrorEvent(final Serializable aSource, final Throwable error) {
    super(aSource);
    if (error == null)
      this.m_error = new NullPointerException();
    else
      this.m_error = error;
  }

  /**
   * Create a new error event using the current optimization id as source.
   *
   * @param error
   *          The error.
   * @throws NullPointerException
   *           if called from outside a host thread
   */
  public ErrorEvent(final Throwable error) {
    super(getCurrentId());
    if (error == null)
      this.m_error = new NullPointerException();
    else
      this.m_error = error;
  }

  /**
   * Obtain the throwable error that occured somewhere in the system.
   *
   * @return The throwable error that occured somewhere in the system.
   */
  public Throwable getError() {
    return this.m_error;
  }

  /**
   * Compare this error event with another object.
   *
   * @param o
   *          The object to compare to.
   * @return <code>true</code> if and only if the other object is an
   *         error event with exactly the same contents as this one.
   */
  @Override
  public boolean equals(final Object o) {
    if (o == this)
      return true;
    if ((o instanceof IErrorEvent) && (super.equals(o))) {
      return ErrorUtils.testErrorEqual(((IErrorEvent) o).getError(),
          this.m_error);
    }
    return false;
  }

  /**
   * Append this object's textual representation to a string builder.
   *
   * @param sb
   *          The string builder to append to.
   * @see #toString()
   */
  @Override
  public void toStringBuilder(final StringBuilder sb) {
    super.toStringBuilder(sb);
    sb.append(TextUtils.LINE_SEPARATOR);
    sb.append("error  : "); //$NON-NLS-1$
    TextUtils.appendError(this.m_error, sb);
  }
}
