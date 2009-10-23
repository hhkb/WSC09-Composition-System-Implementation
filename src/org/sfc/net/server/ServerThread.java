/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2007-02-23
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.net.server.ServerThread.java
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

package org.sfc.net.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.sfc.net.Reconnection;
import org.sfc.parallel.SfcThread;

/**
 * A server thread acts as a server and accepts connections on a specified
 * port.
 * 
 * @author Thomas Weise
 */
public class ServerThread extends SfcThread {
  /**
   * The server sockt to be used.
   */
  ServerSocket m_socket;

  /**
   * The server connection manager.
   */
  private final ServerConnex m_connex;

  /**
   * The owning server component.
   */
  final Server m_owner;

  /**
   * Create a new server thread, that is, a thread that listens for
   * connections at a server socket.
   * 
   * @param owner
   *          The owning server component.
   * @param group
   *          The sfc-thread group owning this thread. This can be
   *          <code>null</code>.
   * @param minRetryDelay
   *          the minimum connection retry delay
   * @param maxRetryDelay
   *          the maximum delay until a connection is retried to be
   *          established
   * @param maxRetryTime
   *          the maximum time we try to reestablish the connection
   */
  protected ServerThread(final Server owner, final ThreadGroup group,
      final long minRetryDelay, final long maxRetryDelay,
      final long maxRetryTime) {
    super(group, owner.toString() + ":serverThread"); //$NON-NLS-1$
    this.m_owner = owner;
    this.m_connex = new ServerConnex(minRetryDelay, maxRetryDelay,
        maxRetryTime);
  }

  /**
   * Perform the work of this thread.
   */
  @Override
  protected void doRun() {
    ServerSocket s;
    Socket a;

    while (this.isRunning()) {
      s = this.m_connex.connect();
      if (s == null)
        continue;

      try {
        a = s.accept();

        if (a != null) {
            if (this.isRunning()) {
              this.m_owner.onConnection(a);
              a = null;
            }
          
            if (a != null) {
              this.m_owner.closeSocket(a);
            }          
        }
      } catch (SocketTimeoutException ste) {
        continue;
      } catch (Throwable t) {
        this.onError(t);
        this.closeSocket();
      }
    }

  }

  /**
   * Close the server socket.
   */
  private final void closeSocket() {
    if (this.m_socket != null) {
      try {
        this.m_socket.close();
      } catch (Throwable t) {
        this.onError(t);

      } finally {
        this.m_socket = null;
      }
    }
  }

  /**
   * This method is called whenever an error was caught. It effectively
   * replaces the <code>UncaughtExceptionHandler</code>
   * 
   * @param t
   *          the error caught
   */
  @Override
  protected void onError(final Throwable t) {
    super.onError(t);
    this.m_owner.onError(t);
  }

  /**
   * Override this method to perform something when the thread dies.
   */
  @Override
  protected void finished() {
    this.closeSocket();
    super.finished();
  }

  /**
   * Obtain a string representation of this server part of a peer-to-peer
   * node.
   * 
   * @return A string representation of this server part of a peer-to-peer
   *         node.
   */
  @Override
  public String toString() {
    return this.getName();
  }

  /**
   * This method is called if the connection should be re-established after
   * <code>timeToWait</code> ms.
   * 
   * @param timeToWait
   *          the time to wait
   */
  protected void delay(final long timeToWait) {
    this.safeSleep(timeToWait);
  }

  /**
   * Abort this thread.
   */
  @Override
  protected void doAbort() {
    this.m_owner.abort();
    super.doAbort();
  }
  
  /**
   * this method is to be called if connection re-establishing should be
   * abandoned
   */
  protected void failed() {
    this.abort();
  }

  /**
   * The internal server connection class.
   * 
   * @author Thomas Weise
   */
  private final class ServerConnex extends Reconnection<ServerSocket> {
    /**
     * Create a new server connex.
     * 
     * @param minRetryDelay
     *          the minimum connection retry delay
     * @param maxRetryDelay
     *          the maximum delay until a connection is retried to be
     *          established
     * @param maxRetryTime
     *          the maximum time we try to reestablish the connection
     */
    ServerConnex(final long minRetryDelay, final long maxRetryDelay,
        final long maxRetryTime) {
      super(minRetryDelay, maxRetryDelay, maxRetryTime);
    }

    /**
     * This method must be implemented by sub-classes. It should establish
     * the connection and may fail with an arbitrary error.
     * 
     * @return The connection-representing object if successful.
     * @throws Throwable
     *           An arbitrary error if the connection could not be
     *           established successfully.
     */
    @Override
    protected final ServerSocket doConnect() throws Throwable {
      ServerSocket s;

      s = ServerThread.this.m_socket;
      if (s != null)
        return s;

      s = new ServerSocket(ServerThread.this.m_owner.m_port);
      try {
        s.setReuseAddress(true);
      } catch (Throwable tt) {
        //
      }
      try {
        s.setSoTimeout(500);
      } catch (Throwable t) {
        s.close();
        throw t;
      }
      return ServerThread.this.m_socket = s;
    }

    /**
     * This method is called if the connection should be re-established
     * after <code>timeToWait</code> ms.
     * 
     * @param timeToWait
     *          the time to wait
     */
    @Override
    protected void delay(final long timeToWait) {
      ServerThread.this.delay(timeToWait);
    }

    /**
     * this method is to be called if connection re-establishing should be
     * abandoned
     */
    @Override
    protected void failed() {
      ServerThread.this.failed();
    }
  }
}
