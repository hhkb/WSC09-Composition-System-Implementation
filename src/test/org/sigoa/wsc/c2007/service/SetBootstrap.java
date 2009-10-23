/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-06-07
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.wsc.c2007.service.SetBootstrap.java
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

import test.org.sigoa.wsc.c2007.kb.KnowledgeBase;


/**
 * The wait time command
 *
 * @author Thomas Weise
 */
public class SetBootstrap implements IServiceCommand {

  /**
   * the set bootstrap time string
   */
  private static final char[] SET_BOOTSTRAP = "setBootstrap".toCharArray(); //$NON-NLS-1$

  /**
   * The standard response
   */
  private static final char[] RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><soapenv:Body><ns1:setBootstrapResponse soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:ns1=\"http://server.wsc07.tub.de/xsd\"><setBootstrapReturn xsi:type=\"xsd:string\">Steffen Bleul and Thomas Weise\nDistributed Systems\nUniversity of Kassel</setBootstrapReturn></ns1:setBootstrapResponse></soapenv:Body></soapenv:Envelope>".toCharArray(); //$NON-NLS-1$

  /**
   * The service command text.
   *
   * @return The service command text
   */
  public char[] getText() {
    return SET_BOOTSTRAP;
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

    int i, j;
    String xsd, wsdl;
    long t;

    KnowledgeBase kb;

    service.m_kb = null;

    kb = new KnowledgeBase();

    i = start;
    while ((i < end) && (text[i] != '>'))
      i++;
    if (i < end)
      i++;
    j = i;
    while ((j < end) && (text[j] != '<'))
      j++;

    xsd = String.valueOf(text, i, j - i);

    i = j + 1;
    while ((i < end) && (text[i] != '>'))
      i++;
    if (i < end)
      i++;
    while ((i < end) && (text[i] != '>'))
      i++;
    if (i < end)
      i++;
    j = i;
    while ((j < end) && (text[j] != '<'))
      j++;

    wsdl = String.valueOf(text, i, j - i);

    service.m_kb = kb;
    t = System.currentTimeMillis();
    kb.initialize(xsd, wsdl);

    System.out
        .println("Done initializing after " + (System.currentTimeMillis() - t) + //$NON-NLS-1$
            "ms."); //$NON-NLS-1$

    // write the response
    writer.write(RESPONSE);
  }
}
