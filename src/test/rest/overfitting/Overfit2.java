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

package test.rest.overfitting;

import org.sfc.io.TextWriter;
import org.sigoa.refimpl.stoch.Randomizer;

/**
 * The default skeleton of a test class. This class does nothing, it is
 * just copied when a new test class is needed.
 * 
 * @author Thomas Weise
 */
public class Overfit2 {
  /**
   * the original function
   * 
   * @param d
   * @return
   */
  private static final double origF(final double d) {
    return (d-.5d)*(d-.5d);
  }

  /**
   * The main routine.
   * 
   * @param p_args
   *          The comment line arguments.
   */
  public static void main(String[] p_args) {
    TextWriter w;
    double[] x, y1, y2;
    Randomizer r;
    int i;
    boolean b;

    r = new Randomizer();
    x = new double[100];
    y1 = x.clone();
    y2 = x.clone();
    b = true;

    for (i = (x.length - 1); i >= 0; i--) {
      x[i] = i / ((double) (x.length - 1));
      y1[i] = origF(x[i]);
            
      do{
        b=!b;
      y2[i] = r.nextNormal(y1[i], 0.02);
      } while(b ^ (y2[i]<y1[i]));
    }

    w = new TextWriter("E:\\d\\plain.txt"); //$NON-NLS-1$
    for (i = (x.length - 1); i >= 0; i--) {
      w.ensureNewLine();
      w.writeDouble(x[i]);
      w.writeCSVSeparator();
      w.writeDouble(y1[i]);
    }
    w.release();

    w = new TextWriter("E:\\d\\measure.txt"); //$NON-NLS-1$
    for (i = (x.length - 1); i >= 0; i--) {
      w.ensureNewLine();
      w.writeDouble(x[i]);
      w.writeCSVSeparator();
      w.writeDouble(y2[i]);

    }
    w.release();
  }

}
