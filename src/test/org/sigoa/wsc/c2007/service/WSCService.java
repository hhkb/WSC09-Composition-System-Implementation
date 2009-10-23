/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-06-07
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.wsc.c2007.service.WSCService.java
 * Last modification: 2007-06-07
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

package test.org.sigoa.wsc.c2007.service;

import java.net.Socket;

import org.sfc.net.protocols.http.HttpProtocolHandler;
import org.sfc.net.protocols.http.HttpRequest;
import org.sfc.text.TextUtils;

import test.org.sigoa.wsc.c2007.composition.Composer;
import test.org.sigoa.wsc.c2007.kb.KnowledgeBase;


/**
 * The wsc web service.
 *
 * @author Thomas Weise
 */
public class WSCService extends HttpProtocolHandler {

  /**
   * the command names
   */
  private final char[][] m_names;

  /**
   * the commands
   */
  private final IServiceCommand[] m_commands;

  /**
   * the knowledge base
   */
  KnowledgeBase m_kb;

  /**
   * the completion interface
   */
  // Completion m_cc;
  /**
   * the query flag
   */
  private volatile boolean m_query;

//  /**
//   * the wsdl directory
//   */
//  String m_wsdlDir;
//
//  /**
//   * the xsd file
//   */
//  String m_xsdFile;

  /**
   * Create a new dmc service.
   *
   * @param commands
   *          the commands
   */
  public WSCService(final IServiceCommand[] commands) {
    super();

    char[][] ch;
    int i;

    this.m_commands = commands;
    i = commands.length;
    ch = new char[i][];
    for (--i; i >= 0; i--) {
      ch[i] = commands[i].getText();
    }
    this.m_names = ch;
  }

  /**
   * This method handles the http request.
   *
   * @param request
   *          the request to handle
   * @throws Throwable
   *           if something goes wrong
   */
  @Override
  protected void handleRequest(final HttpRequest request) throws Throwable {

    char[] cc, n;
    char[][] nn;
    int cl, i, l, z;
    IServiceCommand c;

    cc = request.getContent();
    cl = request.getContentLength();
    nn = this.m_names;

    for (z = (nn.length - 1); z >= 0; z--) {
      n = nn[z];
      l = n.length;
      i = TextUtils.indexOf(cc, 0, cl, n, 0, l, 0);
      if (i >= 0) {
        while ((i < cl) && (cc[i] != '>'))
          i++;
        if (i < cl)
          i++;

        l = (TextUtils.indexOf(cc, 0, cl, n, 0, l, i) - 2);
        c = this.m_commands[z];
        c.perform(request.getOutputWriter(), cc, i, l, this);
        this.m_query = (c instanceof PerformQuery);

        return;
      }
    }
  }

  /**
   * This method is called for each connection established to the server.
   *
   * @param socket
   *          the incoming connection
   * @throws Throwable
   *           if something goes wrong
   */
  @Override
  public void onConnection(final Socket socket) throws Throwable {
    super.onConnection(socket);
    if (this.m_query) {
      this.m_query = false;
      // System.out.println("query done."); //$NON-NLS-1$

      // line inserted due to SPONTANEOUS RULE CHANGE
      // this.m_cc = null;

      // line inserted due to SPONTANEOUS RULE CHANGE
      this.m_kb = null;

      Composer.refreshCache();
      doGC();
    }
  }

  /**
   * Perform the garbage collection
   */
  static final void doGC() {
    int i;
    for (i = 5; i >= 0; i--) {
      System.gc();
      System.runFinalization();
      System.gc();
    }
  }
}
