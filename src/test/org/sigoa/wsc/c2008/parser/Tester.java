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

package test.org.sigoa.wsc.c2008.parser;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;

import org.sfc.xml.sax.SAXWriter;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import de.vs.unikassel.solution_checker.SchemaChecker;

/**
 * @author Thomas Weise
 */
public class Tester {

  /**
   * Parameter identifier for the output folder
   */
  public static String DIR_STRING = "-dir"; //$NON-NLS-1$

  /** */
  public static String SOLUTION_STRING = "-solution";//$NON-NLS-1$

  /** */
  public static String OUTPUT_STRING = "-output";//$NON-NLS-1$

  /**
   * Parameter identifier for the server manual
   */
  public static String MANUAL_STRING = "--?";//$NON-NLS-1$

  /**
   * Searches arguments for parameters and its values.
   * 
   * @param parametername
   *          The identifier for the parameter.
   * @param args
   *          The main arguments of this program.
   * @return The parameter value or null.
   */
  public static String parameterGrabber(String parametername, String[] args) {
    for (int i = 0; i < args.length; i++) {
      if (args[i].compareTo(parametername) == 0) {
        if ((i + 1) < args.length) {
          return args[i + 1];
        }// else {
        return null;
        // }
      }
    }

    return null;
  }

  /**
   * Checks for an available parameter.
   * 
   * @param parametername
   *          The identifier for the parameter.
   * @param args
   *          The main arguments of this program.
   * @return True if the parameter is found among the arguments.
   */
  public static boolean parameterChecker(String parametername,
      String[] args) {
    for (int i = 0; i < args.length; i++) {
      if (args[i].compareTo(parametername) == 0) {
        return true;
      }
    }

    return false;
  }

  /**
   * Prints the manual of this program on the standard output.
   */
  public static void printManual() {
    System.out
        .println("------------------------------------------------------");//$NON-NLS-1$
    System.out
        .println("------------- Web Service Challenge Tester -----------");//$NON-NLS-1$
    System.out
        .println("------------------------------------------------------");//$NON-NLS-1$
    System.out.println("");//$NON-NLS-1$
    System.out.println("Example:");//$NON-NLS-1$
    System.out
        .println("java test.org.sigoa.wsc.c2008.parser.Tester [--?] -dir . -solution solution.bpel -output check.xml");//$NON-NLS-1$
    System.out.println("");//$NON-NLS-1$
    System.out
        .println("dir: The source folder of your challenge and solution.");//$NON-NLS-1$
    System.out.println("solution: The filename of your solution.");//$NON-NLS-1$
    System.out.println("output: The name of the outputfile.");//$NON-NLS-1$
    System.out.println("?: This help screen.");//$NON-NLS-1$
  }

  /**
   * The main routine.
   * 
   * @param p_args
   *          The comment line arguments.
   */
  public static void main(String[] p_args) {
    if ((p_args == null) || (p_args.length == 0)) {
      System.out.println("[Tester:] StartUp");//$NON-NLS-1$
      System.out.println("[Tester:] No parameters defined!");//$NON-NLS-1$
      System.out.println("");//$NON-NLS-1$
      Tester.printManual();
      System.exit(1);
    }

    System.out
        .println("------------------------------------------------------");//$NON-NLS-1$
    System.out
        .println("------------- Web Service Challenge Tester -----------");//$NON-NLS-1$
    System.out
        .println("------------------------------------------------------");//$NON-NLS-1$

    System.out.println("[Tester:] ReadingParameters:");//$NON-NLS-1$

    if (Tester.parameterChecker(Tester.MANUAL_STRING, p_args)) {
      Tester.printManual();
      System.exit(0);
    }

    String dir = ".";//$NON-NLS-1$

    if (!Tester.parameterChecker(Tester.DIR_STRING, p_args)) {
      System.out.println("[Tester:] No directory defined!");//$NON-NLS-1$
      System.out.println("[Tester:] Analyzing local directory."); //$NON-NLS-1$   	  
    } else {
      dir = Tester.parameterGrabber(Tester.DIR_STRING, p_args);
      System.out.println("[Tester:] Dir: " + dir);//$NON-NLS-1$
    }

    String solutionfilename = "solution.bpel";//$NON-NLS-1$

    if (!Tester.parameterChecker(Tester.SOLUTION_STRING, p_args)) {
      System.out.println("[Tester:] No solution filename defined!");//$NON-NLS-1$
      System.out
          .println("[Tester:] Using default name: " + solutionfilename);//$NON-NLS-1$    	  
    } else {
      solutionfilename = Tester.parameterGrabber(Tester.SOLUTION_STRING,
          p_args);
      System.out.println("[Tester:] Solutionfile: " + solutionfilename);//$NON-NLS-1$
    }
    
    System.out.println("[Tester:] Checking BPEL schema...");
    
    SchemaChecker checker = new SchemaChecker();
    
    Schema schema = SchemaChecker.getBPELSchema();
    
    DocumentBuilder parser = null;
	try {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		parser = factory.newDocumentBuilder();
	} catch (ParserConfigurationException e1) {		
		e1.printStackTrace();
	}
	
    Document document = null;
    
	try {		
		document = parser.parse(new File(dir + "\\\\" + solutionfilename));
	} catch (SAXException e1) {
		e1.printStackTrace();
	} catch (IOException e1) {
		e1.printStackTrace();
	}
    
	if (!checker.checkSchema(document, schema))
		return;
	
	System.out.println("[Tester:] Checking BPEL schema... done");
	
    String outputfilename = "check.xml";//$NON-NLS-1$

    if (!Tester.parameterChecker(Tester.OUTPUT_STRING, p_args)) {
      System.out.println("[Tester:] No output filename defined!");//$NON-NLS-1$
      System.out
          .println("[Tester:] Using default name: " + outputfilename); //$NON-NLS-1$ 
    } else {
      outputfilename = Tester.parameterGrabber(Tester.OUTPUT_STRING,
          p_args);
      System.out.println("[Tester:] Outputfile: " + outputfilename);//$NON-NLS-1$
    }

    System.out.println("");//$NON-NLS-1$

    OntologyElement ont;
    List<Service> servs;
    List<Solution> sols;
    Stage q;
    SAXWriter w;

    String ontologyfile = dir + "\\\\Taxonomy.owl";//$NON-NLS-1$

    System.out.println("[Tester:] Reading taxonomy: " + ontologyfile);//$NON-NLS-1$
    ont = OntologyReader.readOntology(ontologyfile);
    System.out.println("[Tester:] Reading taxonomy... done");//$NON-NLS-1$

    String servicesfile = dir + "\\\\Services.wsdl";//$NON-NLS-1$

    System.out.println("[Tester:] Reading services: " + servicesfile);//$NON-NLS-1$
    servs = WSDLReader.readWSDL(ont, servicesfile);
    System.out.println("[Tester:] Reading services... done");//$NON-NLS-1$

    String challengesfile = dir + "\\\\Challenge.wsdl";//$NON-NLS-1$

    System.out.println("[Tester:] Reading challenges: " + challengesfile);//$NON-NLS-1$
    q = Stage.readChallenge(challengesfile, ont);
    System.out.println("[Tester:] Reading challenges... done");//$NON-NLS-1$

    String solutionsfile = dir + "\\\\" + solutionfilename;//$NON-NLS-1$

    System.out.println("[Tester:] Reading solutions: " + solutionsfile);//$NON-NLS-1$
    sols = BPELSolutionParser.readBPEL(solutionsfile, servs);
    System.out.println("[Tester:] Reading solutions... done");//$NON-NLS-1$

    String checksfile = dir + "\\\\" + outputfilename;//$NON-NLS-1$

    System.out.println("");//$NON-NLS-1$
    System.out.println("[Tester:] Writing conclusions: " + checksfile);//$NON-NLS-1$
    w = new SAXWriter(checksfile);
    w.formatOn();
    w.startDocument();
    Stage.process(w, q, sols);
    w.endDocument();
    w.release();
    System.out.println("[Tester:] Writing conclusions... done");//$NON-NLS-1$

  }
}
