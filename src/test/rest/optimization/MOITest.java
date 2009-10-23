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

package test.rest.optimization;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Comparator;

/**
 * The default skeleton of a test class. This class does nothing, it is
 * just copied when a new test class is needed.
 * 
 * @author Thomas Weise
 */
public class MOITest {
  /**
   * @param x
   * @return y
   */
  private static final double f1(final double x) {
    return (Math.pow(Math.exp(1 / (Math.abs(x) + 1)), 3) * Math.sin(x * 6));
  }

  /**
   * @param x
   * @return y
   */
  private static final double f2(final double x) {
    return (10 * ((Math.exp(1 / (1 + Math.abs(x + 1) + Math
        .abs((((int) ((x + 1) * 9)) % 5))))) - 1.8));
  }

  /**
   * The main routine.
   * 
   * @param p_args
   *          The comment line arguments.
   */
  public static void main(String[] p_args) {
    int i, j, k;
    double d, z, v,u1,u2;
    double[][] y;
    double min, max;

    min = -3;
    max = 7;
    
    try {
      System.setOut(new PrintStream(
          new FileOutputStream("E:\\test\\p.txt"))); //$NON-NLS-1$
    } catch (Throwable ttt) {/* */
    }

    y = new double[100000][3];
    i = y.length - 1;
    v = 4.0d / i;
    for (; i >= 0; i--) {
      y[i][0] = d = ((v * i) - 2);
      u1 = f1(d);
      u2=f2(d);
      if((u1<min)||(u2<min)||(u1>max)||(u2>max))
        {
        u1=-1e5;
        u2=-1e5;
        }
      y[i][1] = u1;
      y[i][2] = u2;
//      System.out.println(d+"\t" +u1+//$NON-NLS-1$
//          "\t"+u2);//$NON-NLS-1$
    }
//    System.out.close();
    
    

    i = y.length - 1;
    main: for (j = i; j >= 0; j--) {
      for (k = i; k >= 0; k--) {
        if (((y[j][1] < y[k][1]) && (y[j][2] <= y[k][2]))
            || ((y[j][2] < y[k][2]) && (y[j][1] <= y[k][1]))) {
          y[j] = y[i];
          i--;
          continue main;
        }
      }
    }

    Arrays.sort(y, 0, i + 1, new Comparator<double[]>() {
      public int compare(double[] o1, double[] o2) {
        return Double.compare(o1[0], o2[0]);
      }
    });

    d = -2.0d;
    v *= 1.99999999;
    for (j = 0; j <= i; j++) {
      z = y[j][0];
      if (Math.abs(z - d) > v) {
        if (j > 0)
          System.out.println(d + "\t1.0");//$NON-NLS-1$
        System.out.println(d + "\t0.0");//$NON-NLS-1$
        System.out.println(z + "\t0.0");//$NON-NLS-1$
        System.out.println(z + "\t1.0");//$NON-NLS-1$
      }
      d = z;
    }
    System.out.println(d + "\t1.0");//$NON-NLS-1$
    System.out.println(d + "\t0.0");//$NON-NLS-1$
    System.out.println("2.0\t0.0");//$NON-NLS-1$
    System.out.close();
  }
}
