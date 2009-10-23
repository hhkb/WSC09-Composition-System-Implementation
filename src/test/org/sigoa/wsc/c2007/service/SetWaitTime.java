/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-06-07
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.wsc.c2007.service.SetWaitTime.java
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

import java.io.Writer;

/**
 * The wait time command
 *
 * @author Thomas Weise
 */
public class SetWaitTime implements IServiceCommand {

  /**
   * the set wait time string
   */
  private static final char[] SET_WAIT_TIME = "setWaitTime".toCharArray(); //$NON-NLS-1$

  /**
   * The standard response
   */
  private static final char[] RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><soapenv:Body><ns1:setWaitTimeResponse soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:ns1=\"http://server.wsc07.tub.de/xsd\"><setWaitTimeReturn xsi:type=\"xsd:long\">0</setWaitTimeReturn></ns1:setWaitTimeResponse></soapenv:Body></soapenv:Envelope>".toCharArray(); //$NON-NLS-1$

  /**
   * The service command text.
   *
   * @return The service command text
   */
  public char[] getText() {
    return SET_WAIT_TIME;
  }

  /**
   * The query response
   *
   * @param writer
   *          the output writer
   * @param text
   *          the text
   * @param start
   *          the start
   * @param end
   *          the end
   * @param service
   * @throws Throwable
   *           if something goes wrong
   */
  public void perform(final Writer writer, final char[] text,
      final int start, final int end, final WSCService service)
      throws Throwable {
    System.out.println("setWaitTime"); //$NON-NLS-1$
    writer.write(RESPONSE);
  }

}
