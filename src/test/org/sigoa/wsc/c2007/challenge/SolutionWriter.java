/*
 * Copyright (c) 2006 Thomas Weise
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-05-12 15:53:58
 * Original Filename: test.org.sigoa.wsc.c2007.composition.SolutionWriter.java
 * Version          : 1.0.0
 * Last modification: 2006-05-12
 *                by: Thomas Weise
 * 
 * License          : GNU LESSER GENERAL PUBLIC LICENSE
 *                    Version 2.1, February 1999
 *                    You should have received a copy of this license along
 *                    with this library; if not, write to the Free Software
 *                    Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 *                    MA 02111-1307, USA or download the license under
 *                    http://www.gnu.org/copyleft/lesser.html.
 *                    
 * Warranty         : This software is provided "as is" without any
 *                    warranty; without even the implied warranty of
 *                    merchantability or fitness for a particular purpose.
 *                    See the Gnu Lesser General Public License for more
 *                    details.
 */

package test.org.sigoa.wsc.c2007.challenge;

import org.sfc.xml.sax.SAXWriter;
import org.xml.sax.SAXException;

import test.org.sigoa.wsc.c2007.kb.Service;
import test.org.sigoa.wsc.c2007.kb.ServiceList;


/**
 * Write the compositions to a file.
 * 
 * @author Thomas Weise
 */
public class SolutionWriter extends SAXWriter {

  /**
   * Create a new sax writer.
   * 
   * @param dest
   *          Any given source object that can be converted to a
   *          <code>Writer</code> by the <code>IO</code> class.
   * @throws SAXException
   *           If the destination object cannot be open.
   */
  public SolutionWriter(final Object dest)
      throws SAXException {
    super(dest);
  }

  /**
   * Setup the whole sht.
   */
  @Override
  public final void startDocument() {
    super.startDocument();

    this.startElement("WSChallenge"); //$NON-NLS-1$
    this.attribute("type", //$NON-NLS-1$ 
        "solutions"); //$NON-NLS-1$
  }

  /**
   * Print all solutions.
   * 
   * @param challenges
   *          The challenge list.
   */
  public final void printAllSolutions(final ChallengeList challenges) {
    Challenge[] ch;
    int i;
    Solution s;

    ch = challenges.m_challenges;
    for (i = (ch.length - 1); i >= 0; i--) {
      s = ch[i].m_solution;
      if (s != null)
        this.printSolution(s);
    }

  }

  /**
   * Print a solution.
   * 
   * @param solution
   *          The services.
   */
  public final void printSolution(final Solution solution) {
    int i, j, k;
    ServiceList v;
    ServiceList2 services;
    Service ss;
    String name;

    if (solution == null)
      return;

    this.startElement("case");//$NON-NLS-1$
    name = solution.m_challenge.m_name;
    this.attribute("name", name);//$NON-NLS-1$
    name = name.substring(1);

    services = solution.m_sl;
    for (i = (services.size() - 1), j = 1; i >= 0; i--, j++) {
      v = services.get(i);

      this.startElement("serviceSpec");//$NON-NLS-1$
      this.attribute("name", name + '.' + j);//$NON-NLS-1$

      for (k = (v.size() - 1); k >= 0; k--) {
        for (ss = v.get(k); ss != null; ss = ss.nextEqual()) {
          this.startElement("service");//$NON-NLS-1$
          this.attribute("name", ss.getName());//$NON-NLS-1$
          if (i <= 0)
            this.attribute("finalized", //$NON-NLS-1$
                "true");//$NON-NLS-1$
          this.endElement();
        }
      }
    }

    for (--j; j > 0; j--) {
      this.endElement();
    }

    this.endElement();
  }

}
