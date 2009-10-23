/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-10-02
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.utils.testSeries.postProcessing.TestSeriesProcessor.java
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

package org.sigoa.refimpl.utils.testSeries.postProcessing;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.sfc.collections.CollectionUtils;
import org.sfc.io.CanonicalFile;
import org.sfc.io.IO;
import org.sfc.io.csv.NumericCSVReader;
import org.sfc.utils.Log;
import org.sfc.utils.Utils;
import org.sigoa.refimpl.utils.testSeries.LoggingSelection;
import org.sigoa.refimpl.utils.testSeries.ea.EATestSeries;

/**
 * This class is able to read and process the data written out by an EA
 * test series.
 * 
 * @author Thomas Weise
 */
public class TestSeriesProcessor implements Runnable {
  /**
   * the directory string
   */
  private final String m_dirs;

  /**
   * the root directory
   */
  private final CanonicalFile m_rootDir;

  /**
   * verbose output
   */
  private final boolean m_verbose;

  /**
   * the series processors
   */
  private final List<ISeriesProcessor> m_processors;

  /**
   * create a new parameterized ea test series reader.
   * 
   * @param all
   *          should all data be processed or only the best samples?
   * @param rootDir
   *          the root directory
   * @param verbose
   *          verbose output
   */
  @SuppressWarnings("unchecked")
  public TestSeriesProcessor(final boolean all, final Object rootDir,
      final boolean verbose) {

    super();

    this.m_dirs = (all ? LoggingSelection.ALL_OBJECTIVES.getPrefix()
        : EATestSeries.BEST_OBJECTIVES.getPrefix());

    this.m_rootDir = IO.getFile(rootDir);
    this.m_verbose = verbose;
    this.m_processors = CollectionUtils.createList(-1);
  }

  /**
   * perform the work
   * 
   * @see java.lang.Thread#run()
   */
  public synchronized void run() {
    File[] f, fds;
    File f2;
    CanonicalFile d;
    int i, j, max, v;
    List<CanonicalFile> ll;
    double[][][] dd;
    CanonicalFile x;

    if (this.m_verbose)
      Log.getLog().log("beginning parsing " + this.m_rootDir); //$NON-NLS-1$

    if (this.m_rootDir != null) {
      try {
        this.beginProcessing();
        f = this.m_rootDir.listFiles();

        ll = CollectionUtils.createList();
        max = 1;
        for (i = (f.length - 1); i >= 0; i--) {
          f2 = f[i];
          if (f2.exists() && f2.isDirectory()) {

            fds = f2.listFiles();
            Arrays.sort(fds);
            ll.clear();

            for (j = 0; j < fds.length; j++) {
              d = IO.getFile(new File(fds[j], this.m_dirs));
              if ((d != null) && (d.exists()) && (d.isDirectory())) {
                v = d.listFiles().length;
                if (v < max)
                  continue;
                if (v > max)
                  max = v;
                ll.add(d);
              }

            }

            j = ll.size();
            if (j > 0) {
              if (this.m_verbose)
                Log.getLog().log("begin series " + //$NON-NLS-1$
                    f2.getName());
              this.beginSeries(f2.getName(), j);

              for (--j; j >= 0; j--) {
                x = ll.get(j);
                dd = NumericCSVReader.readDirectory(x, false, true);
                this.processRun(//
                    (dd != null) ? dd : NumericCSVReader.EMPTY_DOUBLE3,//
                    x.getParentFile().getName());
                dd = null;
                Utils.invokeGC();
              }

              this.endSeries();

              if (this.m_verbose)
                Log.getLog().log("finished series " + //$NON-NLS-1$
                    f2.getName());

              fds = null;
              ll.clear();
              Utils.invokeGC();

            }

          }

        }
      } finally {
        this.endProcessing();
      }
    }

    if (this.m_verbose)
      Log.getLog().log("finished parsing " + this.m_rootDir); //$NON-NLS-1$
  }

  /**
   * Returns <code>true</code> if verbose mode is on
   * 
   * @return <code>true</code> if verbose mode is on, <code>false</code>
   *         otherwise
   */
  protected boolean isVerbose() {
    return this.m_verbose;
  }

  /**
   * Begin the series of the given name
   * 
   * @param name
   *          the name of the series
   * @param runCnt
   *          the number of runs to expect
   */
  protected void beginSeries(final String name, final int runCnt) {
    final List<ISeriesProcessor> l;
    int i;

    l = this.m_processors;
    for (i = (l.size() - 1); i >= 0; i--) {
      l.get(i).beginSeries(name, runCnt);
    }
  }

  /**
   * Process the given run
   * 
   * @param data
   *          the data of the run
   * @param name
   *          the name of the run
   */
  protected void processRun(final double[][][] data, final String name) {
    final List<ISeriesProcessor> l;
    int i;
    ISeriesProcessor p;

    l = this.m_processors;
    for (i = (l.size() - 1); i >= 0; i--) {
      p = l.get(i);
      p.beginRun(name);
      p.processRun(data);
      p.endRun();
    }
  }

  /**
   * end the current series
   */
  protected void endSeries() {
    final List<ISeriesProcessor> l;
    int i;

    l = this.m_processors;
    for (i = (l.size() - 1); i >= 0; i--) {
      l.get(i).endSeries();
    }
  }

  /**
   * begin processing the data
   */
  protected void beginProcessing() {
    final List<ISeriesProcessor> l;
    int i;

    l = this.m_processors;
    for (i = (l.size() - 1); i >= 0; i--) {
      l.get(i).beginProcessing();
    }
  }

  /**
   * end processing the data
   */
  protected void endProcessing() {
    final List<ISeriesProcessor> l;
    int i;

    l = this.m_processors;
    for (i = (l.size() - 1); i >= 0; i--) {
      l.get(i).endProcessing();
    }
  }

  /**
   * Add a new series processor
   * 
   * @param l
   *          the series processor
   */
  public void add(final ISeriesProcessor l) {
    if (l != null)
      this.m_processors.add(l);
  }
}
