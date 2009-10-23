/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-10-04
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.utils.testSeries.postProcessing.Plotter.java
 * Last modification: 2007-10-04
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

import org.sfc.io.CanonicalFile;
import org.sfc.io.IO;
import org.sfc.io.StreamUtils;
import org.sfc.io.csv.NumericCSVReader;
import org.sfc.parallel.SfcThread;
import org.sfc.text.TextUtils;
import org.sfc.utils.Log;

/**
 * This class utilizes gnuplot in order to draw emf plots of the data
 * samples in a given directory.
 * 
 * @author Thomas Weise
 */
public class Plotter implements Runnable {
  /**
   * the set dimension commands
   */
  private static final String[] DIMENSION_CMDS = new String[] {
      "set xrange [", //$NON-NLS-1$
      "set yrange [", //$NON-NLS-1$
      "set zrange [", //$NON-NLS-1$
  };

  /**
   * the default terminal type
   */
  private static final String DEFAULT_TERMINAL = "emf"; //$NON-NLS-1$

  /**
   * the gnuplot command
   */
  private final String m_gnuplotCmd;

  /**
   * the directory to plot
   */
  private final CanonicalFile m_dir;

  /**
   * the minimums
   */
  private double[] m_mins;

  /**
   * the maximums
   */
  private double[] m_maxs;

  /**
   * the process
   */
  private Process m_gp;

  /**
   * the internal string builder
   */
  private final StringBuilder m_sb;

  /**
   * the terminal type
   */
  private String m_terminal;

  /**
   * print output to stdout?
   */
  private boolean m_verbose;

  /**
   * how to plot
   */
  private EWith m_with;

  /**
   * Create a new plotter
   * 
   * @param gnuplotCmd
   *          the command line used to invoke gnuplot
   * @param directory
   *          the directory to parse
   */
  public Plotter(final String gnuplotCmd, final Object directory) {
    super();
    this.m_gnuplotCmd = gnuplotCmd;
    this.m_dir = IO.getFile(directory);
    this.m_sb = new StringBuilder();
    this.m_terminal = DEFAULT_TERMINAL;
    this.m_verbose = true;
    this.m_with = EWith.LINES;
  }

  /**
   * set the terminal type
   * 
   * @param type
   *          the terminal type, <code>null</code> for default
   */
  public void setTerminal(final String type) {
    this.m_terminal = ((type != null) ? type : DEFAULT_TERMINAL);
  }

  /**
   * get the terminal type
   * 
   * @return the terminal type
   */
  public String getTerminal() {
    return this.m_terminal;
  }

  /**
   * Set how to plot the data
   * 
   * @param with
   *          the plot type (<code>null</code> for default)
   */
  public void setWith(final EWith with) {
    this.m_with = ((with != null) ? with : EWith.LINES);
  }

  /**
   * Obtain the plot type
   * 
   * @return the plot type
   */
  public EWith getWith() {
    return this.m_with;
  }

  /**
   * set the number of dimensions
   * 
   * @param dims
   *          the number of dimensions
   */
  public void setDimensions(final int dims) {
    double[] d;
    int x;

    x = Math.min(DIMENSION_CMDS.length, dims);
    this.m_mins = d = new double[x];
    Arrays.fill(d, Double.NaN);
    this.m_maxs = d = new double[x];
    Arrays.fill(d, Double.NaN);
  }

  /**
   * set the given minimum
   * 
   * @param dim
   *          the dimension
   * @param min
   *          the minimum
   */
  public void setMin(final int dim, final double min) {
    this.m_mins[dim] = min;
  }

  /**
   * set the given maximum
   * 
   * @param dim
   *          the dimension
   * @param max
   *          the maximum
   */
  public void setMax(final int dim, final double max) {
    this.m_maxs[dim] = max;
  }

  /**
   * compute the dimensions
   */
  public void computeDimensions() {
    final double[][][] vals;
    double[][] vals2;
    double[] vals3;
    final double[] min, max;
    final int dims;
    int i, j, k;
    double d;

    vals = NumericCSVReader.readDirectory(this.m_dir, false, false);

    if ((vals == null) || (vals.length <= 0) || (vals[0].length <= 0))
      return;

    dims = Math.min(DIMENSION_CMDS.length, vals[0][0].length);

    if (dims <= 0)
      return;

    this.m_mins = min = new double[dims];
    Arrays.fill(min, Double.MAX_VALUE);
    this.m_maxs = max = new double[dims];
    Arrays.fill(max, Double.MIN_VALUE);

    for (i = (vals.length - 1); i >= 0; i--) {
      vals2 = vals[i];
      for (j = (vals2.length - 2); j >= 0; j--) {
        vals3 = vals2[j];
        for (k = Math.min(dims, vals3.length) - 1; k >= 0; k--) {
          d = vals3[k];
          if (d < min[k])
            min[k] = d;
          if (d > max[k])
            max[k] = d;
        }
      }
    }

  }

  /**
   * poll the process
   */
  private final void poll() {
    if (this.m_verbose) {
      StreamUtils.readAsciiInput(this.m_gp.getInputStream(), this.m_sb);
      StreamUtils.readAsciiInput(this.m_gp.getErrorStream(), this.m_sb);
      if (this.m_sb.length() > 0) {
        System.out.println(this.m_sb.toString());
        this.m_sb.setLength(0);
      }
    } else {
      StreamUtils.readInput(this.m_gp.getInputStream());
      StreamUtils.readInput(this.m_gp.getErrorStream());

    }
  }

  /**
   * perform a command
   * 
   * @param cmd
   *          the command
   */
  private final void command(final String cmd) {
    this.poll();
    SfcThread.pause(100);

    if (this.m_verbose) {
      StreamUtils.writeAsciiOutput(this.m_gp.getOutputStream(),//
          this.m_gp.getInputStream(), this.m_gp.getErrorStream(),// 
          cmd + TextUtils.LINE_SEPARATOR, this.m_sb);
    } else {
      StreamUtils.writeAsciiOutput(this.m_gp.getOutputStream(),//
          this.m_gp.getInputStream(), this.m_gp.getErrorStream(),// 
          cmd + TextUtils.LINE_SEPARATOR);
    }
    SfcThread.pause(100);
    this.poll();

  }

  /**
   * terminate the gnuplot
   */
  private final void terminate() {
    this.command("exit"); //$NON-NLS-1$
    SfcThread.pause(1000);
    this.command("exit"); //$NON-NLS-1$
    SfcThread.pause(1000);
    this.command(String.valueOf((char) 03));
    SfcThread.pause(100);
    this.command(String.valueOf((char) 04));
    SfcThread.pause(100);
    // this.m_gp.waitFor();//does not work
  }

  /**
   * set the dimension values
   */
  private final void setDimensions() {
    int i;
    final double[] min, max;

    min = this.m_mins;
    max = this.m_maxs;
    for (i = (min.length - 1); i >= 0; i--) {
      this.command(DIMENSION_CMDS[i] + String.valueOf(min[i]) + ':'
          + String.valueOf(max[i]) + ']');
    }
  }

  /**
   * set the directory
   */
  private final void setDirectory() {
    this.command("cd '" + this.m_dir.getCanonicalPath() + '\''); //$NON-NLS-1$
  }

  /**
   * set the terminal type
   */
  private final void setTerminal() {
    this.command("set terminal " + this.m_terminal); //$NON-NLS-1$
  }

  /**
   * plot the given file
   * 
   * @param f
   *          the file
   */
  private final void plot(final Object f) {
    CanonicalFile x;
    String n, q;
    int i;

    x = IO.getFile(f);
    n = x.getName();
    i = n.lastIndexOf('.');
    if (i >= 0)
      n = n.substring(0, i);
    n = n + '.' + this.m_terminal;

    this.command("set output '" //$NON-NLS-1$
        + IO.getFile(new File(x.getParentFile(), n)).getCanonicalPath()
        + '\'');

    q = ((this.m_mins.length < 3) ? "plot '" : //$NON-NLS-1$
        "splot '");//$NON-NLS-1$

    this.command(q + x.getCanonicalPath() + "' with " + //$NON-NLS-1$
        this.m_with.m_txt);
  }

/**
 * unset the key
 */
  private final void unsetKey(){
    this.command("unset key"); //$NON-NLS-1$
  }
  
  /**
   * Perform the work
   */
  public synchronized void run() {
    File[] f;
    int i;

    if (this.m_mins == null)
      this.computeDimensions();
    if (this.m_mins == null)
      return;

    try {
      this.m_gp = Runtime.getRuntime().exec(this.m_gnuplotCmd);
      try {
        this.setDimensions();
        this.setDirectory();
        this.setTerminal();
        this.unsetKey();

        f = this.m_dir.listFiles();
        for (i = (f.length - 1); i >= 0; i--) {
          this.plot(f[i]);
        }

        this.terminate();
      } finally {
        this.m_gp.destroy();
        this.m_gp = null;
      }
    } catch (Throwable t) {
      Log.getLog().log(t);
    }
  }

  /**
   * the enumeration with
   */
  public enum EWith {
    /**
     * plot with lines
     */
    LINES("lines"), //$NON-NLS-1$
    /**
     * plot with points
     */
    POINTS("points"); //$NON-NLS-1$

    /**
     * the text
     */
    final String m_txt;

    /**
     * create a new 'with'
     * 
     * @param txt
     *          the text
     */
    EWith(final String txt) {
      this.m_txt = txt;
    }
  }
}
