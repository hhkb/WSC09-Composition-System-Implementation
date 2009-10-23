/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-02-25
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.net.protocols.http.HttpProtocolHandler.java
 * Last modification: 2007-02-25
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

package org.sfc.net.protocols.http;

import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.util.StringTokenizer;

import org.sfc.net.server.IConnectionHandler;
import org.sfc.text.TextUtils;

/**
 * A very simple http protocol handler. It is not able to handle binary
 * encoded data but processes only texts.
 *
 * @author Thomas Weise
 */
public class HttpProtocolHandler implements IConnectionHandler {

  /**
   * the methods
   */
  private static final EHttpMethod[] METHODS = EHttpMethod.values();

  /**
   * the content length
   */
  private static final String CONTENT_LENGTH = "Content-Length:"; //$NON-NLS-1$

  /**
   * the transfer encoding
   */
  private static final String TRANSFER_ENCODING = "Transfer-Encoding:"; //$NON-NLS-1$

  /**
   * chunked transfer encoding
   */
  private static final String CHUNKED = "chunked";//$NON-NLS-1$

  /**
   * the content length chars
   */
  private static final char[] CONTENT_LENGTH_CH = (CONTENT_LENGTH + ' ')
      .toCharArray();

  /**
   * the http chars
   */
  private static final char[] HTTP1_0 = "HTTP/1.1 ".toCharArray(); //$NON-NLS-1$

  /**
   * the cr-lf chars
   */
  private static final char[] CRLF = TextUtils.CRLF.toCharArray();

  /**
   * the content type line
   */
  private static final char[] CONTENT_TYPE_CH = "Content-type: " //$NON-NLS-1$
  .toCharArray();

  /**
   * the request synchronizer
   */
  private final Object m_requestSync;

  /**
   * the request object
   */
  private HttpRequest m_request;

  /**
   * Create a new http protocol handler
   */
  public HttpProtocolHandler() {
    super();
    this.m_requestSync = new Object();
    this.m_request = this.createRequest();
  }

  /**
   * Create a new http request.
   *
   * @return the new http request
   */
  protected HttpRequest createRequest() {
    return new HttpRequest();
  }

  /**
   * Read the content
   *
   * @param contentLength
   *          the content length
   * @param r
   *          the reader
   * @param request
   *          the request
   * @throws Throwable
   *           if something goes wrong
   */
  private final void readContent(final int contentLength, final Reader r,
      final HttpRequest request) throws Throwable {
    char[] ch, x;
    int p, v, w, m;

    ch = request.m_charBuffer;
    m = ((contentLength > 0) ? contentLength : Integer.MAX_VALUE);

    p = 0;
    for (;;) {
      w = (Math.min(m, ch.length) - p);
      v = r.read(ch, p, w);
      if (v < 0)
        break;
      p += v;
      if (p > m)
        break;

      if (p >= ch.length) {
        x = new char[p << 1];
        System.arraycopy(ch, 0, x, 0, p);
        ch = x;
      }
    }

    request.m_charBuffer = ch;
    request.m_contentLength = p;
  }

  /**
   * Read the chunked content
   *
   * @param r
   *          the reader
   * @param request
   *          the request
   * @throws Throwable
   *           if something goes wrong
   */
  private final void readContentChunked(final Reader r,
      final HttpRequest request) throws Throwable {
    char[] ch, b;
    int s, read, nr, rd, rs;

    ch = request.m_charBuffer;
    b = request.m_numBuf;
    read = 0;
    for (;;) {
      s = 0;
      for (;;) {
        rd = r.read();
        if (rd < 0)
          break;
        if (rd == '\r') {
          if (s == 0)
            continue;
          r.read();
          break;
        }
        if (rd == '\n') {
          if (s == 0)
            continue;
          break;
        }
        b[s++] = ((char) rd);
      }

      if (s <= 0)
        break;
      rs = Integer.parseInt(String.valueOf(b, 0, s), 16);
      if (rs <= 0)
        break;
      nr = (rs + read);
      if (nr > ch.length) {
        b = new char[nr << 1];
        System.arraycopy(ch, 0, b, 0, read);
        ch = b;
        b = request.m_numBuf;
      }

      r.read(ch, read, rs);
      read = nr;
    }

    request.m_charBuffer = ch;
    request.m_contentLength = read;
  }

  /**
   * This method is called for each connection established to the server.
   *
   * @param socket
   *          the incoming connection
   * @throws Throwable
   *           if something goes wrong
   */
  public void onConnection(final Socket socket) throws Throwable {
    BufferedReader br;
    StringTokenizer t;
    String m;
    EHttpMethod hm;
    int i, contentLength;
    boolean b;
    HttpRequest r;
    Writer w;
    CharArrayWriter ww;
    boolean enc;

    try {
      br = new BufferedReader(new InputStreamReader(socket
          .getInputStream()));
      try {
        t = new StringTokenizer(br.readLine());

        if (t.hasMoreTokens()) {
          m = t.nextToken();

          hm = null;
          for (i = (METHODS.length - 1); i >= 0; i--) {
            if (METHODS[i].isMethod(m)) {
              hm = METHODS[i];
              break;
            }
          }

          if (hm != null) {

            r = this.allocateRequest();
            try {
              r.m_method = hm;
              if (t.hasMoreTokens())
                r.m_query = t.nextToken();

              b = true;
              contentLength = 0;
              // if (hm == EHttpMethod.POST) {
              i = 0;
              enc = false;
              while (i < 2) {
                t = new StringTokenizer(br.readLine().trim());
                if (!(t.hasMoreTokens())) {
                  b = false;
                  break;
                }
                m = t.nextToken();
                if (CONTENT_LENGTH.equalsIgnoreCase(m)) {
                  contentLength = Integer.parseInt(t.nextToken());
                  i++;
                } else if (TRANSFER_ENCODING.equalsIgnoreCase(m)) {
                  enc = CHUNKED.equalsIgnoreCase(t.nextToken());
                  i++;
                }
              }
              // }

              while (b) {
                m = br.readLine().trim();
                if ((m.length() <= 0) || (TextUtils.CRLF.equals(m)))
                  b = false;
              }

              if ((contentLength > 0) || (hm == EHttpMethod.POST)
                  || (hm == EHttpMethod.PUT) || enc) {
                if (enc)
                  this.readContentChunked(br, r);
                else
                  this.readContent(contentLength, br, r);
              }

              this.handleRequest(r);
              w = new OutputStreamWriter(socket.getOutputStream());

              try {

                w.write(HTTP1_0);
                w.write(r.m_response.getText());
                w.write(CRLF);

                ww = r.m_writer;
                contentLength = ww.size();
                w.write(CONTENT_LENGTH_CH);
                w.write(Integer.toString(contentLength));
                w.write(CRLF);

                m = r.m_contentType;
                if (m != null) {
                  w.write(CONTENT_TYPE_CH);
                  w.write(m);
                  w.write(CRLF);
                }

                w.write(CRLF);
                if (contentLength > 0)
                  ww.writeTo(w);

              } finally {
                w.close();
              }

            } finally {
              this.disposeRequest(r);
            }
          }
        }
      } finally {
        br.close();
      }
    } finally {
      socket.close();
    }
  }

  /**
   * This method handles the http request.
   *
   * @param request
   *          the request to handle
   * @throws Throwable
   *           if something goes wrong
   */
  protected void handleRequest(final HttpRequest request) throws Throwable {
    //
  }

  /**
   * allocate a request
   *
   * @return the request
   */
  private final HttpRequest allocateRequest() {
    HttpRequest r;
    synchronized (this.m_requestSync) {
      r = this.m_request;
      if (r != null) {
        this.m_request = r.m_next;
        return r;
      }
    }
    return this.createRequest();
  }

  /**
   * dispose a request
   *
   * @param request
   *          the request to dispose
   */
  private final void disposeRequest(final HttpRequest request) {
    request.reset();
    synchronized (this.m_requestSync) {
      request.m_next = this.m_request;
      this.m_request = request;
    }
  }

}
