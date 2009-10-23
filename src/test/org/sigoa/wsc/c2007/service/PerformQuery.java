/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-06-07
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.wsc.c2007.service.PerformQuery.java
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

import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.Writer;

import test.org.sigoa.wsc.c2007.challenge.ChallengeList;
import test.org.sigoa.wsc.c2007.challenge.ChallengeReader;
import test.org.sigoa.wsc.c2007.challenge.SolutionWriter;
import test.org.sigoa.wsc.c2007.composition.Composer;


/**
 * The performQuery command
 *
 * @author Thomas Weise
 */
public class PerformQuery implements IServiceCommand {

  /**
   * the set wait time string
   */
  private static final char[] PERFORM_QUERY = "performQuery".toCharArray(); //$NON-NLS-1$

  /**
   * The standard response, part 1
   */
  private static final char[] RESPONSE_1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><soapenv:Body><ns1:performQueryResponse soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:ns1=\"http://server.wsc07.tub.de/xsd\"><performQueryReturn xsi:type=\"xsd:string\">".toCharArray();//$NON-NLS-1$

  /**
   * The standard response, part 2
   */
  private static final char[] RESPONSE_2 = "</performQueryReturn></ns1:performQueryResponse></soapenv:Body></soapenv:Envelope>".toCharArray(); //$NON-NLS-1$

  /**
   * The knowledge base has not been initialized
   */
  private static final char[] NO_KB_ERROR = (String.valueOf(RESPONSE_1)
      + "Sorry, the knowledge base has not been initialized yet. Please perform a bootsrap first." + //$NON-NLS-1$
  String.valueOf(RESPONSE_2)).toCharArray();

  /**
   * lt
   */
  private static final char[] LT = "&lt;".toCharArray();//$NON-NLS-1$

  /**
   * gt
   */
  private static final char[] GT = "&gt;".toCharArray();//$NON-NLS-1$

  // /**
  // * The standard response
  // */
  // private static final char[] RESPONSE = "<?xml version=\"1.0\"
  // encoding=\"UTF-8\"?><soapenv:Envelope
  // xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"
  // xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"
  // xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><soapenv:Body><ns1:performQueryResponse
  // soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"
  // xmlns:ns1=\"http://server.wsc07.tub.de/xsd\"><performQueryReturn
  // xsi:type=\"xsd:string\">I have tried to wait for 0 msecs. My solution
  // for query is:
  // snufel@gmx.de</performQueryReturn></ns1:performQueryResponse></soapenv:Body></soapenv:Envelope>".toCharArray();
  // //$NON-NLS-1$

  /**
   * The service command text.
   *
   * @return The service command text
   */
  public char[] getText() {
    return PERFORM_QUERY;
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

    ChallengeList l;
    long t, t2;
    int s, e;

    if (service.m_kb != null) {
      s = start;
      e = end;
      while ((s < e) && (text[s] != '>'))
        s++;
      if (s < e)
        s++;
      e--;
      while ((e > s) && (text[e] != '<'))
        e--;

      e = decode(text, s, e);

      l = ChallengeReader.readChallenges(new CharArrayReader(text, s, e
          - s), service.m_kb);

      t = System.currentTimeMillis();
      t2 = System.nanoTime();
      Composer.compose(l);
      l.waitFor();
      t = System.currentTimeMillis() - t;
      t2 = ((System.nanoTime() - t2) / 1000000);
      System.out.println("Done composing after " + //$NON-NLS-1$
          Math.min(t, t2) + " ms."); //$NON-NLS-1$

      writer.write(RESPONSE_1);
      writeSolution(l, writer);
      writer.write(RESPONSE_2);
    } else {
      writer.write(NO_KB_ERROR);
    }
  }

  /**
   * Write the solution.
   *
   * @param l
   *          the challenge list
   * @param writer
   *          the writer
   * @throws Throwable
   *           whenever it likes to
   */
  private static final void writeSolution(final ChallengeList l,
      final Writer writer) throws Throwable {
    CharArrayWriter cw;
    SolutionWriter w;
    char[] b, cc;
    int i, j, s;

    cw = new CharArrayWriter();
    w = new SolutionWriter(cw);
    w.startDocument();
    w.printAllSolutions(l);
    w.endDocument();
    w.flush();

    i = 0;
    b = cw.toCharArray();
    s = b.length;

    main: for (j = 0; j < s; j++) {
      switch (b[j]) {
      case '<': {
        cc = LT;
        break;
      }
      case '>': {
        cc = GT;
        break;
      }
      default:
        continue main;
      }

      writer.write(b, i, j - i);
      writer.write(cc);
      i = j + 1;
    }

    writer.write(b, i, s - i);
  }

  /**
   * decode the received data
   *
   * @param text
   *          the text
   * @param start
   *          the start
   * @param end
   *          the end
   * @return the new length of the text
   */
  private static final int decode(final char[] text, final int start,
      final int end) {
    int i, l, s;
    boolean b;

    l = end;
    s = 0;
    b = false;
    for (i = (end - 1); i >= start; i--) {
      switch (text[i]) {
      case ';': {
        s = 1;
        break;
      }
      case 't': {
        if (s == 1)
          s = 2;
        else
          s = 0;
        break;
      }
      case 'l': {
        if (s == 2) {
          b = true;
          s = 3;
        } else
          s = 0;
        break;
      }
      case 'g': {
        if (s == 2) {
          b = false;
          s = 3;
        } else
          s = 0;
        break;
      }
      case '&': {
        if (s == 3) {
          text[i] = (b ? '<' : '>');
          l -= 3;
          System.arraycopy(text, i + 4, text, i + 1, l - i - 1);
        } else
          s = 0;
        break;
      }
      default: {
        s = 0;
        break;
      }
      }
    }
    return l;
  }

}
