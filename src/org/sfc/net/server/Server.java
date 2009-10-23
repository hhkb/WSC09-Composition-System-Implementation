/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2007-02-23
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.net.server.Server.java
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

import java.net.Socket;

import org.sfc.net.Net;
import org.sfc.parallel.ThreadActivity;

/**
 * The server object provides basic ability to create server applications.
 * 
 * @author Thomas Weise
 */
public class Server extends ThreadActivity {
  /**
   * The port the server listens at.
   */
  final int m_port;

  /**
   * the connection handler
   */
  private final IConnectionHandler m_connectionHandler;

  /**
   * Create a new server.
   * 
   * @param threads
   *          The <code>ThreadGroup</code> to host all threads of the
   *          server or <code>null</code> if no special thread group is
   *          needed.
   * @param port
   *          The port the server listenes at.
   * @param connectionHandler
   *          the connection handler
   */
  public Server(final ThreadGroup threads, final int port,
      final IConnectionHandler connectionHandler) {
    this(threads, port, connectionHandler, -1, -1, -1);
  }

  /**
   * Create a new server.
   * 
   * @param threads
   *          The <code>ThreadGroup</code> to host all threads of the
   *          server or <code>null</code> if no special thread group is
   *          needed.
   * @param port
   *          The port the server listenes at.
   * @param connectionHandler
   *          the connection handler
   * @param minRetryDelay
   *          the minimum connection retry delay
   * @param maxRetryDelay
   *          the maximum delay until a connection is retried to be
   *          established
   * @param maxRetryTime
   *          the maximum time we try to reestablish the connection
   * @throws IllegalArgumentException
   *           if
   *           <code>(connectionHandler == null) || (port &lt;= 1000)</code>
   */
  public Server(final ThreadGroup threads, final int port,
      final IConnectionHandler connectionHandler,
      final long minRetryDelay, final long maxRetryDelay,
      final long maxRetryTime) {
    super(threads);

    if ((connectionHandler == null) || (port <= 1000))
      throw new IllegalArgumentException();
    this.m_connectionHandler = connectionHandler;
    this.m_port = port;
    this.addThread(this.createServerThread(minRetryDelay, maxRetryDelay,
        maxRetryTime));
  }

  /**
   * Create a new server thread
   * 
   * @param minRetryDelay
   *          the minimum connection retry delay
   * @param maxRetryDelay
   *          the maximum delay until a connection is retried to be
   *          established
   * @param maxRetryTime
   *          the maximum time we try to reestablish the connection
   * @return the new server thread
   */
  protected ServerThread createServerThread(final long minRetryDelay,
      final long maxRetryDelay, final long maxRetryTime) {
    return new ServerThread(this, this.getThreadGroup(), minRetryDelay,
        maxRetryDelay, maxRetryTime);
  }

  /**
   * Obtain the port this server component is running on.
   * 
   * @return The port this server component is running on.
   */
  public int getPort() {
    return this.m_port;
  }

  /**
   * This method is called by the server thread whenever it receives an
   * incomming connection.
   * 
   * @param socket
   *          The socket to process.
   */
  protected void onConnection(final Socket socket) {
    try {
      this.m_connectionHandler.onConnection(socket);
    } catch (Throwable t) {
      this.onError(t);
      this.closeSocket(socket);
    }
  }

  /**
   * Close a socket.
   * 
   * @param socket
   *          The socket to be closed.
   */
  protected void closeSocket(final Socket socket) {
    try {
      socket.close();
    } catch (Throwable t) {
      this.onError(t);
    }
  }

  /**
   * Obtain a string representation of this peer-to-peer node.
   * 
   * @return A string representation of this peer-to-peer node.
   */
  @Override
  public String toString() {
    return "Server " + //$NON-NLS-1$
        Net.LOCAL_HOST + " at port " + //$NON-NLS-1$
        this.getPort();
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
  }
}
