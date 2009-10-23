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

import org.sfc.io.CanonicalFile;
import org.sfc.io.Files;
import org.sfc.io.IO;
import org.sfc.io.csv.CSVReader;
import org.sfc.io.csv.NumericCSVReader;
import org.sfc.io.loaders.ReaderLoader;
import org.sigoa.refimpl.stoch.StatisticInfo;
import org.sigoa.refimpl.utils.testSeries.postProcessing.ObjectiveLoader;

/**
 * The standard settings for the gcd problem
 * 
 * @author Thomas Weise
 */
public class ElectionFilter {
  /**
   * the base directory
   */
  public static final String IN_DIR = "D:\\Programming\\dgpfTests\\election\\election (rbgp, erbgp, false variety)"; //$NON-NLS-1$

  /**
   * the base directory
   */
  public static final String OUT_DIR = "e:\\results"; //$NON-NLS-1$

  /**
   * the success info
   */
  private final StatisticInfo m_success;

  /**
   * the success info
   */
  private final StatisticInfo m_perfect;

  /**
   * the total number
   */
  private int m_total;

  /**
   * the constructor
   */
  private ElectionFilter() {
    super();
    this.m_success = new StatisticInfo();
    this.m_perfect = new StatisticInfo();
  }

  /**
   * pump the date
   * 
   * @param in
   *          the input file
   * @param out
   *          the output file
   */
  private static final void pump(final CanonicalFile in,
      final CanonicalFile out) {
    File[] fs;
    File q, t;
    int i, j;
    ReaderLoader l;
    String s;
    ElectionFilter rbgp, erbgp;

    rbgp = new ElectionFilter();
    erbgp = new ElectionFilter();

    l = new ReaderLoader();

    fs = in.listFiles();
    for (i = (fs.length - 1); i >= 0; i--) {
      q = fs[i];
      if (q.exists() && q.isDirectory()) {
        t = new File(q, "resultIndividuals.txt"); //$NON-NLS-1$
        if (t.exists()) {
          if (l.load(t)) {
            j = l.getLength();
            if (j > 0) {
              s = new String(l.getData(), 0, j);
            } else {
              s = null;
            }
            l.reset();
            if (s != null) {
              if (s.contains("TreeRBGPProgram")) {//$NON-NLS-1$
                if (t.exists()) {
                  erbgp.process(IO.getFile(q), new File(out, "ergbp"));//$NON-NLS-1$
                }
              } else {
                if (s.contains("RBGPProgram")) {//$NON-NLS-1$
                  if (t.exists()) {
                    rbgp.process(IO.getFile(q), new File(out, "rgbp"));//$NON-NLS-1$
                  }
                }
              }
            }
          }
        }
      }
    }

    System.out.println(in.getName());
    System.out.println("rbgp"); //$NON-NLS-1$
    // rbgp.dump(new File(out, "rbgp"));//$NON-NLS-1$
    System.out.println(rbgp.m_total);
    System.out.println(rbgp.m_success);
    System.out.println(rbgp.m_perfect);
    System.out.println();
    System.out.println();
    System.out.println("erbgp"); //$NON-NLS-1$
    System.out.println(erbgp.m_total);
    System.out.println(erbgp.m_success);
    System.out.println(erbgp.m_perfect);
  }

  /**
   * dump
   * 
   * @param d
   *          the dest
   * @param data
   *          the data
   * @param out
   */
  private void dump(final File d, final File out, final double[][][][] data) {
    // TextWriter tw;
    // int i, j, k, l;
    // double[][][] line2;
    // double[][] line, linex;

    d.mkdirs();

    NumericCSVReader.doublesToCSV(data, new int[] { -1, 1, 0 }, Files
        .createFileInside(out, "series",//$NON-NLS-1$
            "txt"));//$NON-NLS-1$

    //    
    // tw = new TextWriter(Files.createFileInside(out,
    // "series",//$NON-NLS-1$
    // "txt"));//$NON-NLS-1$
    // i = data.length;
    // for (--i; i >= 0; i--) {
    // tw.ensureNewLine();
    // tw.newLine();
    // tw.newLine();
    //
    // line2 = data[i];
    //
    // for (l = (line2.length - 1); l >= 0; l--) {
    // if (line2[l].length > 0)
    // break;
    // }
    // for (k = 0; k <= l; k++) {
    // if (line2[k].length > 0)
    // break;
    // }
    //
    // linex = line2[k];
    // for (; k <= l; k++) {
    // line = line2[k];
    // if (line.length <= 0)
    // line = linex;
    // else
    // linex = line;
    //
    // for (j = 0; j < line.length; j++) {
    // if ((line[j] != null) && (line[j].length == 2)) {
    // tw.ensureNewLine();
    // tw.writeInt(k);
    // tw.writeCSVSeparator();
    // tw.writeDouble(line[j][1]);
    // tw.writeCSVSeparator();
    // tw.writeDouble(line[j][0]);
    // }
    // }
    // tw.flush();
    // }
    // }
    //
    // tw.release();
  }

  /**
   * process the directory
   * 
   * @param dir
   *          the directory
   * @param out
   */
  private final void process(final CanonicalFile dir, final File out) {
    String[][] d;
    int i, j;// , k, x, y, q;
    // File[] fs;
    // SimpleList<double[][]> l;
    // SimpleList<double[]> ll;
    // ReaderLoader r;
    // String s;
    // String[] v;
    // double[] dd;
    // double[][] xx;
    double[][][] xxx;

    this.m_total++;

    d = CSVReader.readCSVFile(new File(dir, "observeSuccess.txt"), false); //$NON-NLS-1$
    if (d != null) {
      j = -1;
      outer: for (i = 0; i < d.length; i++) {
        if (d[i].length == 2) {
          if ("s".equals(d[i][1])) { //$NON-NLS-1$
            try {
              j = Integer.parseInt(d[i][0]);
            } catch (Throwable tt) {
              j = -1;
            }
            if ((j > 0) && (j < 1e5)) {
              this.m_success.append(j);
              break outer;
            }
          }
        }
      }

      outer2: if ((j > 0) && (j < 1e5)) {
        for (i = 0; i < d.length; i++) {
          if (d[i].length == 2) {
            if ("p".equals(d[i][1])) { //$NON-NLS-1$
              try {
                j = Integer.parseInt(d[i][0]);
              } catch (Throwable tt) {
                j = -1;
              }
              if ((j > 0) && (j < 1e5)) {
                this.m_perfect.append(j);
                break outer2;
              }
            }
          }
        }
      }
    }

    // fs = new File(dir, "bestIndividuals").listFiles(); //$NON-NLS-1$
    // Arrays.sort(fs);
    // i = fs.length;
    // l = new SimpleList<double[][]>(i);
    // ll = new SimpleList<double[]>();
    // r = new ReaderLoader();
    //
    // for (j = 0; j < fs.length; j++) {
    // r.load(fs[j]);
    // k = r.getLength();
    // if (k > 0) {
    // s = new String(r.getData(), 0, k);
    // r.reset();
    // ll.clear();
    //
    // y = 0;
    // while ((x = s.indexOf("objectives: {", y)) >= y) {//$NON-NLS-1$
    // x = s.indexOf('{', x);
    // y = s.indexOf('}', x);
    // if (y > x) {
    // v = s.substring(x + 1, y).split(",");//$NON-NLS-1$
    // if (v.length == 2) {
    // dd = new double[2];
    // try {
    // dd[0] = Double.parseDouble(v[0].trim());
    // dd[1] = Double.parseDouble(v[1].trim());
    // test: {
    // for (q = ll.m_count - 1; q >= 0; q--) {
    // if ((Double.compare(ll.m_data[q][0], dd[0]) == 0)
    // && (Double.compare(ll.m_data[q][1], dd[1]) == 0))
    // break test;
    // }
    // ll.add(dd);
    // }
    // } catch (Throwable xyxx) {
    // //
    // }
    // }
    // }
    // }
    //
    // x = ll.m_count;
    // xx = new double[x][];
    // System.arraycopy(ll.m_data, 0, xx, 0, x);
    // l.add(xx);
    // ll.clear();
    // }
    // }
    //
    // i = l.m_count;
    // xxx = new double[i][][];
    // System.arraycopy(l.m_data, 0, xxx, 0, i);
    xxx = ObjectiveLoader.loadBestIndividualObjectives(new File(dir,
        "bestIndividuals")); //$NON-NLS-1$

    this.dump(dir, out, NumericCSVReader.splitAndUnite(xxx));
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
        pump(IO.getFile(f), cf2);
      }
    }
  }
}
