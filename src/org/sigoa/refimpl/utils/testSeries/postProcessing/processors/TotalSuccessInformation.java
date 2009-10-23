/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-10-02
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.utils.testSeries.postProcessing.processors.SeriesSuccessInformation.java
 * Last modification: 2007-10-02
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

package org.sigoa.refimpl.utils.testSeries.postProcessing.processors;

import java.util.Arrays;
import java.util.List;

import org.sfc.collections.CollectionUtils;
import org.sfc.io.TextWriter;
import org.sigoa.refimpl.stoch.StatisticInfo;
import org.sigoa.refimpl.utils.testSeries.ParameterizedTestSeries;
import org.sigoa.refimpl.utils.testSeries.postProcessing.EProcessorTypes;

/**
 * The series-based success information gatherer
 * 
 * @author Thomas Weise
 */
public class TotalSuccessInformation extends SuccessBasedSeriesProcessor {

  /**
   * the internal success info list
   */
  private final List<SuccessInfo> m_list;

  /**
   * the parameter titles
   */
  private final String[] m_titles;

  /**
   * Create a new success based series processor
   * 
   * @param dir
   *          the directory
   * @param prefix
   *          the prefix
   * @param success
   *          the success definition
   * @param titles
   *          the parameter titles
   */
  public TotalSuccessInformation(final Object dir, final String prefix,
      final ISuccessDefinition success, final String[] titles) {
    super(dir, prefix, EProcessorTypes.ALL, success);
    this.m_list = CollectionUtils.createList();
    this.m_titles = titles;
  }

  /**
   * begin processing the data
   */
  @Override
  public void beginProcessing() {
    super.beginProcessing();
    this.m_list.clear();
  }

  /**
   * Begin the series of the given name
   * 
   * @param name
   *          the name of the series
   * @param runCnt
   *          the number of runs to expect
   */
  @Override
  public void beginSeries(final String name, final int runCnt) {
    super.beginSeries(name, runCnt);
    this.m_list.add(new SuccessInfo(runCnt, name));
  }

  /**
   * Process the given success individual.
   * 
   * @param individual
   *          the success individual to be processed
   * @param generation
   *          the generation in which this individual occured
   * @return <code>true</code> if and only if the success-based
   *         processing should be continued, <code>false</code> if that
   *         was all that we wanted to know
   */
  @Override
  protected boolean processSuccess(final double[] individual,
      final int generation) {
    SuccessInfo f;

    f = this.m_list.get(this.m_list.size() - 1);

    f.m_successes++;
    f.m_info.append(generation);
    return false;
  }

  /**
   * end processing the data
   */
  @Override
  public void endProcessing() {
    SuccessInfo[] f;
    int i, j;
    TextWriter w;
    SuccessInfo x;
    String[] y;

    w = this.getOut();
    if (w != null) {

      y = this.m_titles;
      for (j = (y.length - 1); j >= 0; j--) {
        w.write(y[j]);
        w.writeCSVSeparator();
      }

      w.write("successes");//$NON-NLS-1$
      w.writeCSVSeparator();
      w.write("runs");//$NON-NLS-1$
      w.writeCSVSeparator();
      w.write("success ratio");//$NON-NLS-1$
      w.writeCSVSeparator();
      w.write("avg_succes_generation");//$NON-NLS-1$
      w.writeCSVSeparator();
      w.write("success_generation_var");//$NON-NLS-1$

      i = this.m_list.size();
      f = this.m_list.toArray(new SuccessInfo[i]);
      Arrays.sort(f);

      for (--i; i >= 0; i--) {
        w.ensureNewLine();
        x = f[i];
        y = ParameterizedTestSeries.parseParameterString(x.m_name);
        for (j = (y.length - 1); j >= 0; j--) {
          w.write(y[j]);
          w.writeCSVSeparator();
        }
        // w.write(x.m_name);
        // w.writeCSVSeparator();
        w.writeInt(x.m_successes);
        w.writeCSVSeparator();
        w.writeInt(x.m_runs);
        w.writeCSVSeparator();
        w.writeDouble(((double) (x.m_successes)) / ((double) (x.m_runs)));
        w.writeCSVSeparator();
        w.writeDouble((x.m_successes > 0) ? x.m_info.getAverage() : -1);
        w.writeCSVSeparator();
        w.writeDouble((x.m_successes > 0) ? x.m_info.getVariance() : -1);

      }

    }

    super.endProcessing();
  }

  /**
   * the internal success information
   * 
   * @author Thomas Weise
   */
  private static final class SuccessInfo implements
      Comparable<SuccessInfo> {

    /**
     * the series name
     */
    final String m_name;

    /**
     * the statistic info success generation record of the series
     */
    final StatisticInfo m_info;

    /**
     * the number of runs
     */
    final int m_runs;

    /**
     * the number of success
     */
    int m_successes;

    /**
     * create a new success info record
     * 
     * @param runs
     *          the number of runs
     * @param name
     *          the series name
     */
    SuccessInfo(final int runs, final String name) {
      super();
      this.m_info = new StatisticInfo();
      this.m_runs = runs;
      this.m_name = name;
    }

    /**
     * compare two success infos
     * 
     * @param o
     *          the info to compare with
     * @return the comparison result
     */
    public int compareTo(final SuccessInfo o) {
      int i;

      if (o == null)
        return -1;

      i = Double.compare(((double) (this.m_successes))
          / ((double) (this.m_runs)), ((double) (o.m_successes))
          / ((double) (o.m_runs)));
      if (i != 0)
        return i;

      i = Double.compare(o.m_info.getAverage(), this.m_info.getAverage());
      if (i != 0)
        return i;

      return String.CASE_INSENSITIVE_ORDER.compare(o.m_name, this.m_name);
    }
  }
}
