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

package test.org.sigoa.wsc.c2008.generator.test;

import org.sfc.utils.Utils;

import test.org.sigoa.wsc.c2008.generator.Concept;
import test.org.sigoa.wsc.c2008.generator.Problem;

/**
 * The default skeleton of a test class. This class does nothing, it is
 * just copied when a new test class is needed.
 * 
 * @author Thomas Weise
 */
public class ProblemTester {

  /**
   * The main routine.
   * 
   * @param p_args
   *          The comment line arguments.
   */
  public static void main(String[] p_args) {
    Concept c;
    Problem p;

    do {
      c = null;
      p = null;
      Utils.invokeGC();
      c = Concept.createConceptTree(10000);
      p = Problem.buildProblem(c, new int[] { 10 });
      System.out.println("blubb");//$NON-NLS-1$
    } while (p == null);

    c.serialize("E:\\temp\\tst\\taxonomy.xml"); //$NON-NLS-1$
    p.serialize("e:\\temp\\tst");//$NON-NLS-1$

    p.generateServices(5000);
  }
}
