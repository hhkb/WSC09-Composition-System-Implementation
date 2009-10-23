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

/**
 * The default skeleton of a test class. This class does nothing, it is
 * just copied when a new test class is needed.
 * 
 * @author Thomas Weise
 */
public class ElectionObjectiveWriteout {

  /**
   * The main routine.
   * 
   * @param p_args
   *          The comment line arguments.
   */
  public static void main(String[] p_args) {
    TextWriter tw;
    final int max;
    int cor, dist;
    double r;

    tw = new TextWriter("E:\\1.txt"); //$NON-NLS-1$

    max = 16;

    for (cor = 0; cor < max; cor++) {
      for (dist = ((cor > 0) ? 1 : 0); dist <= cor; dist++) {
        r = 0;

        if (dist >= cor)
          r++;
        if (dist > 0)
          r += ((double) (dist - 1)) / (max - 1);
        r += ((max - cor) / max);

        tw.ensureNewLine();
        tw.writeInt(cor);
        tw.writeCSVSeparator();
        tw.writeInt(dist);
        tw.writeCSVSeparator();
        tw.writeDouble(r*0.5d);
      }
    }

    tw.release();
  }

}
