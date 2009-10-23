/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-07-20
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.wsc.c2007.checker.Main.java
 * Last modification: 2007-07-20
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

package test.org.sigoa.wsc.c2007.checker;

import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;

import org.sfc.io.TextWriter;

import test.org.sigoa.wsc.c2007.challenge.ChallengeList;
import test.org.sigoa.wsc.c2007.challenge.ChallengeReader;
import test.org.sigoa.wsc.c2007.kb.KnowledgeBase;


/**
 * This class is used to check one solution
 *
 * @author Thomas Weise
 */
public class Main {

  /**
   * the knowledge base
   */
  public static KnowledgeBase KB;

  /**
   * the challenges
   */
  public static ChallengeList CH;

  /**
   * the main program called at startup
   *
   * @param args
   *          the command line arguments
   */
  public static void main(final String[] args) {

    File wsdlDir, xsdFile, challengeFile, solutionFile, outFile;
    JFileChooser f;
    List<Solution> l;
    TextWriter w;

    f = new JFileChooser();
    f.setDialogTitle("Choose the directory containing the WSDL " + //$NON-NLS-1$
        "service descriptions"); //$NON-NLS-1$
    f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

    if (f.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
      System.exit(1);
    wsdlDir = f.getSelectedFile();

    f = new JFileChooser();
    f.setDialogTitle("Choose ontology file");//$NON-NLS-1$
    f.setFileSelectionMode(JFileChooser.FILES_ONLY);
    f.setCurrentDirectory(wsdlDir);
    if (f.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
      System.exit(1);
    xsdFile = f.getSelectedFile();

    f = new JFileChooser();
    f.setDialogTitle("Choose challenge file");//$NON-NLS-1$
    f.setFileSelectionMode(JFileChooser.FILES_ONLY);
    f.setCurrentDirectory(wsdlDir);
    if (f.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
      System.exit(1);
    challengeFile = f.getSelectedFile();

    f = new JFileChooser();
    f.setDialogTitle("Choose solution file");//$NON-NLS-1$
    f.setFileSelectionMode(JFileChooser.FILES_ONLY);
    f.setCurrentDirectory(wsdlDir);
    if (f.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
      System.exit(1);
    solutionFile = f.getSelectedFile();

    f = new JFileChooser();
    f.setDialogTitle("Choose output file");//$NON-NLS-1$
    f.setFileSelectionMode(JFileChooser.FILES_ONLY);
    f.setCurrentDirectory(wsdlDir);
    if (f.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
      System.exit(1);
    outFile = f.getSelectedFile();

    KB = new KnowledgeBase();
    System.out.println("initializing kb");//$NON-NLS-1$
    KB.initialize(xsdFile, wsdlDir);
    System.out.println("done initializing kb");//$NON-NLS-1$

    System.out.println("reading challenges");//$NON-NLS-1$
    CH = ChallengeReader.readChallenges(challengeFile, KB);
    System.out.println("done reading challenges");//$NON-NLS-1$

    System.out.println("reading solutions");//$NON-NLS-1$
    l = SolutionReader.readSolutions(solutionFile);
    System.out.println("done reading solutions");//$NON-NLS-1$
    w = new TextWriter(outFile);


    for (Solution s : l) {
      System.out.println("checking solution " + s.m_name);//$NON-NLS-1$
      SolutionChecker.check(s, w);
      w.flush();
      w.newLine();
      w.newLine();
      w.newLine();
      System.out.println("done checking solution " + s.m_name);//$NON-NLS-1$
    }

    w.release();
    System.out.println("finished"); //$NON-NLS-1$
  }
}
