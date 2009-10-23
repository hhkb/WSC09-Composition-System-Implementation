/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-29
 * Creator          : Thomas Weise
 * Original Filename: test.org.sfc.net.protocols.http.DummyHttpServer.java
 * Last modification: 2006-12-29
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

package test.org.sfc.net.protocols.http;

import java.io.CharArrayWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.sfc.net.protocols.http.HttpProtocolHandler;
import org.sfc.net.protocols.http.HttpRequest;
import org.sfc.net.server.Server;

/**
 * the dummy http test class
 *
 * @author Thomas Weise
 */
public class DummyHttpServer {

  /**
   * the main routine
   *
   * @param args
   *          the arguments
   */
  public static void main(final String[] args) {
    new Server(null, 8000,new DummyHttpHandler()).start();
  }

  /**
   * the dummy http protocol handler
   *
   * @author Thomas Weise
   */
  static final class DummyHttpHandler extends HttpProtocolHandler {
    /**
     * This method handles the http request.
     *
     * @param request
     *          the request to handle
     * @throws Throwable
     *           if something goes wrong
     */
    @Override
    protected void handleRequest(final HttpRequest request)
        throws Throwable {
      Writer w;

      w = request.getOutputWriter();
      String s;

      int cl;

      w.write("<html><header><title>" + //$NON-NLS-1$
          request.getQuery() + "</title></header><body><div>" + //$NON-NLS-1$
          "Method: " + request.getMethod() + //$NON-NLS-1$
          "<br/>" + //$NON-NLS-1$
          "Request: " + request.getQuery() + //$NON-NLS-1$
          "<br/>" + //$NON-NLS-1$
          "Content-Length:" + (cl = request.getContentLength()) + //$NON-NLS-1$
          "<br/>");//$NON-NLS-1$

      if (cl > 0) {
        s = String.valueOf(request.getContent(), 0, cl);
        w.write("Content: ");//$NON-NLS-1$
        s.replaceAll("<", //$NON-NLS-1$
                    "&lt;");//$NON-NLS-1$
        s.replaceAll(">", //$NON-NLS-1$
                    "&gt;");//$NON-NLS-1$
        s.replaceAll("&", //$NON-NLS-1$
                    "&amp;");//$NON-NLS-1$
        w.write(s);
        w.write("<br/>");//$NON-NLS-1$
      }

      w.write("</div></body></html>");//$NON-NLS-1$

      OutputStreamWriter os;
      os = new OutputStreamWriter(System.out);
      ((CharArrayWriter)w).writeTo(os);
      os.close();

    }
  }

}
