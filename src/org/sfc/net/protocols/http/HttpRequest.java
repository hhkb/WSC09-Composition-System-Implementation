/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-02-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.net.protocols.http.HttpRequest.java
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

package org.sfc.net.protocols.http;

import java.io.CharArrayWriter;
import java.io.Writer;

/**
 * This class encapsulates a http request
 *
 * @author Thomas Weise
 */
public class HttpRequest {
  /**
   * the http request
   */
  HttpRequest m_next;

  /**
   * the character buffer
   */
  char[] m_charBuffer;

  /**
   * the char array writer
   */
  CharArrayWriter m_writer;

  /**
   * the content length
   */
  int m_contentLength;

  /**
   * the method
   */
  EHttpMethod m_method;

  /**
   * the query string
   */
  String m_query;

  /**
   * the http response
   */
  EHttpResponse m_response;

  /**
   * the content type
   */
  String m_contentType;

  /**
   * the num buffer
   */
  final char[] m_numBuf;

  /**
   * the request
   */
  protected HttpRequest() {
    super();
    this.m_charBuffer = new char[4096];
    this.m_writer = new CharArrayWriter();
    this.m_numBuf = new char[10];
    this.m_response = EHttpResponse.OK;
  }

  /**
   * Obtain the content length
   *
   * @return the content length
   */
  public int getContentLength() {
    return this.m_contentLength;
  }

  /**
   * Set the http response. Calling this method is only needed if the
   * response is different from "OK"
   *
   * @param response
   *          the new response
   */
  public void setResponse(final EHttpResponse response) {
    this.m_response = response;
  }

  /**
   * Obtain the contents
   *
   * @return the contents
   */
  public char[] getContent() {
    return this.m_charBuffer;
  }

  /**
   * Obtain the writer to write the outgoing content to.
   *
   * @return the writer to write the outgoing content to
   */
  public Writer getOutputWriter() {
    return this.m_writer;
  }

  /**
   * Obtain the method with which the request was received
   *
   * @return the method with which the request was received
   */
  public EHttpMethod getMethod() {
    return this.m_method;
  }

  /**
   * Obtain the query string, or <code>null</code> if unavailable
   *
   * @return the query string, or <code>null</code> if unavailable
   */
  public String getQuery() {
    return this.m_query;
  }

  /**
   * obtain the character array
   *
   * @param len
   *          the required length
   * @return the char array
   */
  char[] getChars(final int len) {
    char[] ch;

    ch = this.m_charBuffer;
    if (ch.length >= len)
      return ch;
    return (this.m_charBuffer = (ch = new char[len << 1]));
  }

  /**
   * Set the content type
   *
   * @param contentType
   *          the content type
   */
  public void setContentType(final String contentType) {
    this.m_contentType = contentType;
  }

  /**
   * reset the request so it is ready for reuse
   */
  protected void reset() {
    this.m_writer.reset();
    this.m_contentLength = 0;
    this.m_query = null;
    this.m_response = EHttpResponse.OK;
    this.m_contentType = null;
  }
}
