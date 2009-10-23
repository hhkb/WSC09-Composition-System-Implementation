/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2007-02-23
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.net.Reconnection.java
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

package org.sfc.net;

import org.sfc.parallel.ErrorHandler;

/**
 * This class holds some global constants that should be used to implement
 * mechanisms to reconnect broken network connections.
 * 
 * @param <Type>
 *          The type of the objects representing a connection established
 *          by this object.
 * @author Thomas Weise
 */
public abstract class Reconnection<Type> extends ErrorHandler {
  /**
   * The default minimum time to wait between to reconnect attempts.
   */
  private static final long DEFAULT_MIN_RETRY_DELAY = (1 * 1000);

  /**
   * The default maximum time to wait between to reconnect attempts.
   */
  private static final long DEFAULT_MAX_RETRY_DELAY = (30 * 60 * 1000);

  /**
   * The default maximum to allow connection retrying.
   */
  private static final long DEFAULT_MAX_RETRY_TIME = (7 * 24 * 60 * 60 * 1000);

  /**
   * The minimum time to wait between to reconnect attempts.
   */
  private final long m_minRetryDelay;

  /**
   * The maximum time to wait between to reconnect attempts.
   */
  private final long m_maxRetryDelay;

  /**
   * the maximum connection retries.
   */
  private final int m_maxRetries;

  /**
   * Create a new reconnection.
   * 
   * @param minRetryDelay
   *          the minimum connection retry delay
   * @param maxRetryDelay
   *          the maximum delay until a connection is retried to be
   *          established
   * @param maxRetryTime
   *          the maximum time we try to reestablish the connection
   */
  protected Reconnection(final long minRetryDelay,
      final long maxRetryDelay, final long maxRetryTime) {
    super();
    this.m_minRetryDelay = ((minRetryDelay > 0) ? minRetryDelay
        : DEFAULT_MIN_RETRY_DELAY);
    this.m_maxRetryDelay = ((maxRetryDelay > 0) ? maxRetryDelay
        : DEFAULT_MAX_RETRY_DELAY);

    long m;

    m = ((maxRetryTime > 0) ? maxRetryTime : DEFAULT_MAX_RETRY_TIME);

    this.m_maxRetries = Math.max(2,
        (int) (m / (this.m_minRetryDelay + this.m_maxRetries)));
  }

  /**
   * This method returns the time to wait until the next reconnection
   * attempt.
   * 
   * @param trial
   *          The number of the reconnection trial, must be between 1 and
   *          {@link #m_maxRetries}.
   * @return The new time to wait until the next reconnect attempt.
   */
  private final long getReconnectWaitTime(final int trial) {

    return Math.max(this.m_minRetryDelay, Math.min(this.m_maxRetryDelay,
        Math.round((this.m_minRetryDelay * trial)
            / ((double) (this.m_maxRetries)))));
  }

  /**
   * The internal retry count.
   */
  private int m_trials;

  /**
   * This method must be implemented by sub-classes. It should establish
   * the connection and may fail with an arbitrary error.
   * 
   * @return The connection-representing object if successful.
   * @throws Throwable
   *           An arbitrary error if the connection could not be
   *           established successfully.
   */
  protected abstract Type doConnect() throws Throwable;

  /**
   * This method is called if the connection should be re-established after
   * <code>timeToWait</code> ms.
   * 
   * @param timeToWait
   *          the time to wait
   */
  protected abstract void delay(final long timeToWait);

  /**
   * this method is to be called if connection re-establishing should be
   * abandoned
   */
  protected abstract void failed();


  /**
   * Establish the connection.
   * 
   * @return The connection-representing object if successful,
   *         <code>null</code> oterwise.
   */
  public Type connect() {
    Type t;
    Throwable e;

    try {
      t = this.doConnect();
      if (t != null) {
        this.m_trials = 0;
        return t;
      }

      e = null;
    } catch (Throwable x) {
      e = x;
    }

    this.onError(e);
    return null;
  }

  /**
   * Reset the reconnection state manually.
   */
  protected final void reset() {
    this.m_trials = 0;
  }

  /**
   * It is recommended that derived classes use this method for
   * error-processing.
   * 
   * @param t
   *          the error object
   */
  @Override
  protected void onError(final Throwable t) {
    int c, d;

    super.onError(t);

    c = this.m_trials;
    this.m_trials = (c + 1);
    d = this.m_maxRetries;

    if(c<d) this.delay(getReconnectWaitTime(c));
    else this.failed();
  }
}
