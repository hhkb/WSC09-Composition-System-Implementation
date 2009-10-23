/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-22
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.gcd.GCDTestSeries.java
 * Last modification: 2007-09-22
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

package test.rest.experimentFilter;

import java.io.File;

import org.sfc.collections.lists.SimpleList;
import org.sfc.io.CanonicalFile;
import org.sfc.io.Files;
import org.sfc.io.IO;
import org.sfc.io.csv.NumericCSVReader;
import org.sigoa.refimpl.stoch.StatisticInfo;
import org.sigoa.refimpl.utils.testSeries.postProcessing.ObjectiveLoader;

/**
 * The standard settings for the gcd problem
 * 
 * @author Thomas Weise
 */
public class CSFilter {
  /**
   * the base directory
   */
  public static final String IN_DIR = "E:\\cs"; //$NON-NLS-1$

  /**
   * the base directory
   */
  public static final String OUT_DIR = IN_DIR + "\\results"; //$NON-NLS-1$

  /**
   * the success info
   */
  private final StatisticInfo[] m_result;


  /**
   * the constructor
   */
  private CSFilter() {
    super();
    this.m_result = new StatisticInfo[] {//
    new StatisticInfo(),//
        new StatisticInfo(),//
        new StatisticInfo(),//
        new StatisticInfo(),//
    };
  }

  /**
   * the minimum generations
   */
  private static final int MIN_GEN = 320;

  /**
   * pump the data
   * 
   * @param in
   * @param out
   */
  private final void pump(final CanonicalFile in, final File out) {
    File[] fs;
    double[][][] data, d2;
    double[][] x;
    int c, v, k;
    SimpleList<double[]> l;

    fs = in.listFiles();
    if ((fs == null) || ((c = fs.length) <= 0))
      return;

    l = new SimpleList<double[]>();
    for (--c; c >= 0; c--) {
      data = ObjectiveLoader.loadBestIndividualObjectives(new File(fs[c],
          "bestIndividuals"));//$NON-NLS-1$

      if ((data != null) && (data.length > MIN_GEN)) {
        d2 = new double[MIN_GEN + 1][][];
        System.arraycopy(data, 0, d2, 0, MIN_GEN + 1);

        x = data[MIN_GEN];
        k = 0;
        for (v = (x.length - 1); v >= 0; v--) {
          l.add(x[v].clone());
          if (x[v][0] < x[k][0]) {
            k = v;
          }
        }

        for (v = Math.min(this.m_result.length, x[k].length) - 1; v >= 0; v--) {
          this.m_result[v].append(x[k][v]);
        }

        NumericCSVReader.doublesToCSV(d2, new int[] { -1, 1, 0 }, Files
            .createFileInside(out, "series",//$NON-NLS-1$
                "txt"));//$NON-NLS-1$

      }
    }

    System.out.println();
    System.out.println(out.getName());
    for (v = (this.m_result.length - 1); v >= 0; v--) {
      System.out.println("objective " + v);//$NON-NLS-1$
      System.out.println(this.m_result[v]);
    }

    x = new double[l.m_count][];
    System.arraycopy(l.m_data, 0, x, 0, l.m_count);
    data = new double[][][] { x };
    NumericCSVReader.doublesToCSV(data, new int[] { 0, 1, 2 }, new File(
        out, "plot.txt")//$NON-NLS-1$
        );

  }

  /**
   * the main program called at startup
   * 
   * @param args
   *          the command line arguments
   */
  public static void main(String[] args) {
    File[] ff;
    File f;
    CanonicalFile cf, cf2;
    int i;

    cf = IO.getFile(OUT_DIR);
    Files.delete(cf);

    ff = IO.getFile(IN_DIR).listFiles();

    cf.mkdirs();
    for (i = (ff.length - 1); i >= 0; i--) {
      f = ff[i];
      if (f.isDirectory()) {
        cf2 = IO.getFile(new File(cf, f.getName()));
        cf2.mkdirs();
        new CSFilter().pump(IO.getFile(new File(f, "test")), //$NON-NLS-1$
            cf2);
      }
    }
  }
}
