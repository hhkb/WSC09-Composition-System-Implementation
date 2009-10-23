/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-25
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.events.Event.java
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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.sfc.text.ITextable;
import org.sfc.text.TextUtils;
import org.sigoa.spec.events.IEvent;
import org.sigoa.spec.jobsystem.JobSystemUtils;

/**
 * The standard event implementation.
 */
public class Event extends java.util.EventObject implements IEvent,
    ITextable {

  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The creation time time stamp.
   */
  private final long m_creationTime;

  /**
   * Constructs am Event. The source of the event should be an object which
   * can easily be serialized without dragging many other objects into
   * this. Normally, <code>IEvent</code>s are used in the context of
   * optimization processes where the source will be the id assigned to the
   * optimization process.
   *
   * @param aSource
   *          the source of the event
   * @throws NullPointerException
   *           if aSource is null.
   */
  public Event(final Serializable aSource) {
    super(check(aSource));
    this.m_creationTime = System.currentTimeMillis();
  }

  /**
   * Create an event using the current optimization id as source.
   *
   * @throws NullPointerException
   *           if called from outside a host thread
   */
  public Event() {
    this(getCurrentId());
  }

  /**
   * check an event source
   *
   * @param aSource
   *          the source to be checked
   * @return <code>aSource</code>
   * @throws NullPointerException
   *           if aSource is null.
   */
  private static final Serializable check(Serializable aSource) {
    if (aSource == null)
      throw new NullPointerException();
    return aSource;
  }

  /**
   * Obtain the serializable source identifier.
   *
   * @return the serializable source identifier
   */
  public Serializable getEventSource() {
    return ((Serializable) (this.source));
  }

  /**
   * The time stamp (java time) when the event was created.
   *
   * @return The time stamp when the event was create.
   * @see System#currentTimeMillis()
   */
  public long getCreationTime() {
    return this.m_creationTime;
  }

  /**
   * Stores the <code>Event</code> into the stream.
   *
   * @param s
   *          The output stream.
   * @throws IOException
   *           If something io-like went wrong.
   */
  private final void writeObject(final ObjectOutputStream s)
      throws IOException {
    s.defaultWriteObject();
    s.writeObject(this.source);
  }

  /**
   * Reconstitute the <code>Event</code> instance from a stream (that is,
   * deserialize it).
   *
   * @param s
   *          The input stream.
   * @throws IOException
   *           If something io-like went wrong.
   * @throws ClassNotFoundException
   *           If a needed class could not be found.
   */
  private final void readObject(final ObjectInputStream s)
      throws IOException, ClassNotFoundException {
    s.defaultReadObject();
    this.source = s.readObject();
  }

  /**
   * Compare this event with another object.
   *
   * @param o
   *          The object to compare to.
   * @return <code>true</code> if and only if the other object is an
   *         event with exactly the same contents as this one.
   */
  @Override
  public boolean equals(final Object o) {
    IEvent e;

    if (o == this)
      return true;
    if (!(o instanceof IEvent))
      return false;

    e = ((IEvent) o);
    return ((e.getCreationTime() == this.m_creationTime) && (e
        .getEventSource().equals(this.source)));
  }

  /**
   * Obtain the current optimization process id.
   *
   * @return the current optimization process id
   * @throws NullPointerException
   *           if called outside a host thread
   */
  protected static final Serializable getCurrentId() {
    return JobSystemUtils.getCurrentHost().getOptimizationId();
  }

  /**
   * Obtain the human readable representation of this event.
   *
   * @return the human readable representation of this event
   */
  @Override
  public String toString() {
    StringBuilder sb;
    sb = new StringBuilder();
    this.toStringBuilder(sb);
    return sb.toString();
  }

  /**
   * Append this object's textual representation to a string builder.
   *
   * @param sb
   *          The string builder to append to.
   * @see #toString()
   */
  public void toStringBuilder(final StringBuilder sb) {
    sb.append("class  : "); //$NON-NLS-1$
    TextUtils.appendClass(this.getClass(), sb);
    sb.append(TextUtils.LINE_SEPARATOR);
    sb.append("created: "); //$NON-NLS-1$
    TextUtils.appendTime(this.m_creationTime, false, sb);
    sb.append(TextUtils.LINE_SEPARATOR);
    sb.append("source : ");//$NON-NLS-1$
    sb.append(this.source);
  }
}
