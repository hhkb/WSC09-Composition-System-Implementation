/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-03-30
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.example.TSPData.java
 * Last modification: 2008-03-30
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

package test.org.sigoa.example;

import java.io.File;

import org.sfc.io.csv.NumericCSVReader;
import org.sfc.math.Mathematics;

/**
 * This class provides the example data of the travelling salesman problem.
 * 
 * @author Thomas Weise
 */
public class TSPData {

  /**
   * the x-coordinates of the cities
   */
  public static final double[] CITY_COORD_X;

  /**
   * the y-coordinates of the cities
   */
  public static final double[] CITY_COORD_Y;

  /**
   * the distance matrix
   */
  private static final double[][] DISTANCE_MATRIX;

  /**
   * the input file
   */
  private static final String INPUT_FILE = TSPData.class.getPackage()
      .getName().replace('.', File.separatorChar)
      + File.separatorChar + "brd14051.txt"; //$NON-NLS-1$

  /**
   * load the data
   */
  static {
    double[][] data;
    double[] cd;
    double a, b;
    int i, c;

    data = NumericCSVReader.readFile(INPUT_FILE, false, false);
    c = 0;
    for (i = (data.length - 1); i >= 0; i--) {
      if ((data[i] != null) && (data[i].length == 3)
          && Mathematics.isNumber(data[i][0])
          && Mathematics.isNumber(data[i][1])
          && Mathematics.isNumber(data[i][2])) {
        c++;
      }
    }

    CITY_COORD_X = new double[c];
    CITY_COORD_Y = new double[c];
    DISTANCE_MATRIX = new double[c][];
    // extract the city coordinates
    for (i = (data.length - 1); i >= 0; i--) {
      if ((data[i] != null) && (data[i].length == 3)
          && Mathematics.isNumber(data[i][0])
          && Mathematics.isNumber(data[i][1])
          && Mathematics.isNumber(data[i][2])) {
        CITY_COORD_X[--c] = data[i][1];
        CITY_COORD_Y[c] = data[i][2];
      }
    }

    // create a triangular distance matrix
    for (i = (DISTANCE_MATRIX.length - 1); i >= 0; i--) {
      DISTANCE_MATRIX[i] = cd = new double[i];
      for (c = (cd.length - 1); c >= 0; c--) {
        a = CITY_COORD_X[c] - CITY_COORD_X[i];
        b = CITY_COORD_Y[c] - CITY_COORD_Y[i];
        cd[c] = Math.sqrt((a * a) + (b * b));
      }
    }
  }

  /**
   * Compute the distance between two cities i and j
   * 
   * @param i
   *          the first city
   * @param j
   *          the second city
   * @return their distance
   */
  public static double distance(final int i, final int j) {
    return (i > j) ? DISTANCE_MATRIX[i][j] : DISTANCE_MATRIX[j][i];
  }
}
