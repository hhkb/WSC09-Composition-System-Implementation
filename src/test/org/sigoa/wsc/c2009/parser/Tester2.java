/*
 * Copyright (c) 2006 Thomas Weise
 *
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-01-07 15:26:24
 * Original Filename: examples.Test.java
 * Version          : 2.0.0
 * Last modification: 2006-04-10
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
 *
 * STRONG WARNING   : This file is an experimental test file. Anything you
 *                    perform may result in unexpected, unintend or even
 *                    catastrophical results.
 */

package test.org.sigoa.wsc.c2009.parser;

import java.util.List;

import org.sfc.xml.sax.SAXWriter;

/**
 * The default skeleton of a test class. This class does nothing, it is
 * just copied when a new test class is needed.
 *
 * @author Thomas Weise
 */
public class Tester2 {

  /**
   * The main routine.
   *
   * @param p_args
   *          The comment line arguments.
   */
  public static void main(String[] p_args) {
    OntologyElement ont;
    List<Service> servs;
    List<Solution> sols;
    Stage q;
    SAXWriter w;

    String dir = "c:\\wsc\\";//$NON-NLS-1$

    ont = OntologyReader.readOntology(dir + "Taxonomy.owl"); //$NON-NLS-1$
    System.out.println("ontology"); //$NON-NLS-1$
    servs = WSDLReader.readWSDL(ont, dir + "Services.wsdl");//$NON-NLS-1$
    System.out.println("wsdl"); //$NON-NLS-1$
    
    
    WSLAReader.readWSLA(servs, dir + "Servicelevelagreements.wsla");//$NON-NLS-1$
    System.out.println("wsla"); //$NON-NLS-1$
    
    q = Stage.readChallenge(dir + "Challenge.wsdl", ont);//$NON-NLS-1$   
    System.out.println("challenge"); //$NON-NLS-1$
    
    sols = BPELSolutionParser.readBPEL(dir + "Solution.bpel",//$NON-NLS-1$
        servs);
    System.out.println("solution"); //$NON-NLS-1$

    w = new SAXWriter(dir + "check.xml");//$NON-NLS-1$
    w.formatOn();
    w.startDocument();
    Stage.process(w, q, sols);
    w.endDocument();
    w.release();
    System.out.println("check"); //$NON-NLS-1$
  }
}
