/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-02-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.net.server.IConnectionHandler.java
 * Last modification: 2007-02-26
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

/**
 * This interface is used to handle the connections that are established to
 * the server.
 *
 * @author Thomas Weise
 */
public interface IConnectionHandler {

  /**
   * This method is called for each connection established to the server.
   *
   * @param socket
   *          the incoming connection
   * @throws Throwable
   *           if something goes wrong
   */
  public abstract void onConnection(final Socket socket) throws Throwable;

}
