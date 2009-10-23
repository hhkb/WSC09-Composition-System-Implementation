/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-05-17
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.utils.testSeries.postProcessing.ObjectiveLoader.java
 * Last modification: 2007-05-17
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

package org.sigoa.refimpl.utils.testSeries.postProcessing;

import java.io.File;
import java.util.Arrays;

import org.sfc.collections.lists.SimpleList;
import org.sfc.io.CanonicalFile;
import org.sfc.io.IO;
import org.sfc.io.csv.NumericCSVReader;
import org.sfc.io.loaders.ReaderLoader;
import org.sfc.text.TextUtils;
import org.sfc.utils.ErrorUtils;
import org.sfc.utils.Utils;
import org.sigoa.refimpl.go.Individual;

/**
 * The objective loader
 * 
 * @author Thomas Weise
 */
public class ObjectiveLoader {

  /**
   * the objectives chars
   */
  private static final char[] OBJECTIVES = Individual.OBJECTIVES
      .toCharArray();

  /**
   * This method loads the objective values from the best individuals files
   * in a directory.
   * 
   * @param dir
   *          the directory
   * @return the array of best objectives
   */
  public static final double[][][] loadBestIndividualObjectives(
      final Object dir) {
    ReaderLoader loader;
    CanonicalFile f;
    File[] fs;
    int c, y, l, s, e, q;
    final double[][][] res;
    char[] data;
    SimpleList<double[]> lst;
    double[] dd;
    double[][] xx;

    f = IO.getFile(dir);
    if ((f == null) || (!(f.isDirectory())) || (!(f.exists()))
        || ((fs = f.listFiles()) == null) || ((y = fs.length) <= 0)) {
      return NumericCSVReader.EMPTY_DOUBLE3;
    }

    Arrays.sort(fs);
    res = new double[y][][];
    Arrays.fill(res, NumericCSVReader.EMPTY_DOUBLE2);
    loader = new ReaderLoader();
    lst = new SimpleList<double[]>();

    for (c = 0; c < y; c++) {
      loader.load(fs[c]);
      l = loader.getLength();
      if (l > 0) {
        data = loader.getData();
        e = 0;
        while ((s = TextUtils.indexOf(data, 0, l, OBJECTIVES, 0,
            OBJECTIVES.length, e)) >= e) {
          s += OBJECTIVES.length;
          if ((e = TextUtils.indexOf(data, 0, l, '}', s)) <= s)
            break;
          dd = NumericCSVReader.stringsToDoubleArray(String.valueOf(data,
              s, e - s).split(",")); //$NON-NLS-1$
          if ((dd != null) && (dd.length > 0)) {
            inner: {
              for (q = (lst.m_count - 1); q >= 0; q--) {
                if (Utils.testDoubleArrayEqual(lst.get(q), dd))
                  break inner;
              }
              lst.add(dd);
            }
          }
        }

        loader.reset();

        e = lst.m_count;
        if (e > 0) {
          res[c] = xx = new double[e][];
          System.arraycopy(lst.m_data, 0, xx, 0, e);
          lst.m_count = 0;
        }
      }
    }

    return res;
  }

  /**
   * the forbidden constructor
   */
  private ObjectiveLoader() {
    ErrorUtils.doNotCall();
  }
}
