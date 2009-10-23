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

package test.rest;

import org.sfc.io.TextWriter;
import org.sfc.text.TextUtils;

/**
 * The default skeleton of a test class. This class does nothing, it is
 * just copied when a new test class is needed.
 * 
 * @author Thomas Weise
 */
public class Roulette {

  /**
   * the population size
   */
  private static final int P = 1000;

  /** */
  private static final double SUM= (((P-1)*P)>>>1);
  /** */
  private static final double ISUM= 1d/SUM;
  
  /**
   * maximize?
   */
  static boolean M=false;
 
  /**
   * compute the probability
   * 
   * @param rank
   * @return x
   */
  private static final double prob(final int rank) {
    return P*(M? (rank*ISUM):((P-rank-1)*ISUM));
  }

  /**
   * The main routine.
   * 
   * @param p_args
   *          The comment line arguments.
   */
  public static void main(String[] p_args) {
    int rank, i;
    double[] s;
    TextWriter tw;

    s = new double[2];
    tw = new TextWriter("e:\\1.txt"); //$NON-NLS-1$

    for (i = 0; i < 2; i++) {
      M=(i==0);
      if (i > 0) {
        tw.newLine();
        tw.newLine();
        tw.newLine();
      }
      for (rank = 0; rank < P; rank++) {
        tw.ensureNewLine();
        tw.writeInt(rank);
        tw.writeCSVSeparator();
        tw.writeDouble(prob(rank));
        s[i] += prob(rank);
      }
    }
    tw.release();
    System.out.println();
    System.out.println(TextUtils.toString(s));
  }
}
