/*
 * Copyright (c) 2006 Thomas Weise
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-05-02 09:19:31
 * Original Filename: test.org.sigoa.wsc.c2007.testSets.data.TestSetGenerator.java
 * Version          : 1.0.3
 * Last modification: 2006-05-08
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

package test.org.sigoa.wsc.c2007.testSets;

import java.io.File;

import org.sfc.collections.lists.SimpleList;
import org.sfc.io.IO;
import org.sigoa.refimpl.stoch.Randomizer;

/**
 * This class generates test data sets that can be used to test
 * test.org.sigoa.wsc challenge algorithms. A challenge consists of four parts:
 * <ol>
 * <li>The challenge file contains the provided input and the wanted
 * output parameters.</li>
 * <li>The solution file contains the solutions a correct composition
 * algorithm would yield. Notice: It is possible that there exist
 * additional or even simpler solutions because the whole challenge is
 * created randomized. This is not very likely to happen, though.</li>
 * <li>An ontology file. In the form of a XSD XML-Schema, the taxonomy of
 * of the parameter types of the services are stored.</li>
 * <li>The web service definitions, stored in form of wsdl-files.</li>
 * </ol>
 * 
 * @author Thomas Weise
 */
public final class TestSetGenerator {

  /**
   * the main program called at startup
   * 
   * @param args
   *          the command line arguments
   */
  public static void main(final String[] args) {
    TestSetGenerator.createChallenge("E:\\wsc\\examples\\13\\",//$NON-NLS-1$
        "E:\\wsc\\examples\\14\\challenge.xml", //$NON-NLS-1$
        "E:\\wsc\\examples\\14\\solution.xml",//$NON-NLS-1$
        "E:\\wsc\\examples\\14\\config.xsd",//$NON-NLS-1$    
        new ChallengeDesc[] {// 
        new ChallengeDesc(7, 1, 8, 1),//
        },// 
        2000, 8, 6, 3, 40);

  }

  /**
   * Create a new test.org.sigoa.wsc challenge.
   * 
   * @param wsddir
   *          The destination directory to write the wsdl files to.
   * @param challengeFile
   *          The file to contain the challenge data.
   * @param solutionFile
   *          The file to contain the solution data.
   * @param xsdFile
   *          The xsd file to contain the ontology.
   * @param challenges
   *          An array with the challenges to be created.
   * @param serviceCount
   *          The count of services to create.
   * @param parameterCount
   *          The input/output parameter count per service.
   * @param ontologyDepth
   *          The depth of the ontology.
   * @param ontologyWidth
   *          The width of the ontology.
   * @param ontologyRoots
   *          The count of the ontology's root concepts.
   */
  public static final void createChallenge(final Object wsddir,
      final Object challengeFile, final Object solutionFile,
      final Object xsdFile, final ChallengeDesc[] challenges,
      final int serviceCount, final int parameterCount,
      final int ontologyDepth, final int ontologyWidth,
      final int ontologyRoots) {
    SolutionDesc[] s;
    SimpleList<ConceptDesc> ont;
    File wd;
    Randomizer r;
    int i, j;

    wd = IO.getFile(wsddir);
    r = new Randomizer();

    s = new SolutionDesc[challenges.length];
    for (i = (challenges.length - 1); i >= 0; i--) {
      System.gc();
      s[i] = new SolutionDesc(challenges[i]);
      challenges[i].m_provided = Math.min(challenges[i].m_provided,
          ontologyRoots >> 2);
      challenges[i].m_wanted = Math.min(challenges[i].m_wanted,
          ontologyRoots >> 2);
      System.gc();
    }

    System.gc();
    System.gc();
    System.gc();
    SolutionDesc.storeSolutions(solutionFile, s, challenges);

    System.gc();
    System.gc();
    System.gc();
    ont = ConceptDesc.createOntology(ontologyDepth, ontologyWidth,
        ontologyRoots, xsdFile, r);
    System.gc();
    System.gc();
    System.gc();

    j = serviceCount;
    for (i = (challenges.length - 1); i >= 0; i--) {
      System.gc();
      s[i].query(ont, challenges[i], wd, parameterCount, r);
      System.gc();
      j -= (challenges[i].m_solutionDepth * challenges[i].m_solutionWidth);
    }

    System.gc();
    System.gc();
    System.gc();
    SolutionDesc.storeChallenges(challengeFile, s);
    System.gc();
    System.gc();
    System.gc();

    for (i = (ont.size() - 1); i >= 0; i--) {
      ont.get(i).m_children = null;
    }

    s = null;
    System.gc();
    System.gc();
    System.gc();

    for (; j > 0; j--) {
      if ((j % 100) == 0)
        System.gc();
      randomService(ont, wd, parameterCount, r);
    }
  }

  /**
   * Create a new random wsdl document.
   * 
   * @param destDir
   *          The destination directory to write the wsdl files to.
   * @param pc
   *          The input/output parameter count per service.
   * @param concepts
   *          The concepts known.
   * @param r
   *          The randomizer.
   */
  static final void randomService(final SimpleList<ConceptDesc> concepts,
      final File destDir, final int pc, final Randomizer r) {
    ServiceDesc s;
    int i;
    SimpleList<ConceptDesc> l;

    s = new ServiceDesc();

    l = s.m_in;
    for (i = pc; i > 0; i--) {
      l.add(concepts.get(r.nextInt(concepts.size())));
    }

    l = s.m_out;
    for (i = pc; i > 0; i--) {
      l.add(concepts.get(r.nextInt(concepts.size())));
    }

    s.storeWsdl(destDir);
  }
}
