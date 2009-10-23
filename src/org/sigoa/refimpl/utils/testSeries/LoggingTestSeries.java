/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-29
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.utils.testSeries.TestSeries.java
 * Last modification: 2007-09-29
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

package org.sigoa.refimpl.utils.testSeries;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.nio.channels.FileLock;

import org.sfc.io.CanonicalFile;
import org.sfc.io.Files;
import org.sfc.io.IO;
import org.sfc.io.TextWriter;
import org.sfc.io.TextWriterPrintStream;
import org.sfc.io.writerProvider.SingleTextWriterProvider;
import org.sfc.net.Net;
import org.sfc.text.TextUtils;
import org.sfc.utils.ErrorUtils;
import org.sfc.utils.Log;
import org.sigoa.refimpl.pipe.EqualObjectiveDegenerator;
import org.sigoa.refimpl.pipe.stat.IndividualPrinterPipe;
import org.sigoa.refimpl.pipe.stat.ObjectivePrinterPipe;
import org.sigoa.spec.go.IIndividual;
import org.sigoa.spec.go.IPopulation;
import org.sigoa.spec.pipe.IPipeIn;

/**
 * The base class for all test series
 * 
 * @author Thomas Weise
 */
public abstract class LoggingTestSeries extends TestSeries {

  /**
   * The directory
   */
  private final CanonicalFile m_directory;

  /**
   * the current directory
   */
  private CanonicalFile m_currentDir;

  /**
   * the identifier string
   */
  private final String m_identifier;

  /**
   * the log file
   */
  TextWriter m_personalLog;

  /**
   * a verbose test series verbose produces more status log
   */
  private volatile boolean m_verbose;

  /**
   * the data grouping
   */
  private final EDataGrouping m_grouping;

  /**
   * the logging selection
   */
  private volatile long m_logging;

  /**
   * The success filter.
   */
  private volatile ISuccessFilter<?> m_filter;

  /**
   * Create a new test series
   * 
   * @param dir
   *          the directory where the magic is to take place
   * @param maxRuns
   *          the maximum runs this test series is running
   * @param grouping
   *          how the data should be grouped
   */
  protected LoggingTestSeries(final Object dir,
      final EDataGrouping grouping, final int maxRuns) {
    super(maxRuns);

    StringBuilder sb;
    int i;
    File f;
    CanonicalFile d;

    this.m_verbose = (dir != null);

    sb = new StringBuilder();

    sb.append(this.getClass().getSimpleName());
    i = sb.lastIndexOf("."); //$NON-NLS-1$
    if (i >= 0)
      sb.delete(0, i + 1);

    sb.append('@');
    sb.append(Net.LOCAL_HOST.toString());
    this.m_identifier = sb.toString().replace('/', '.').replace('\\', '.')
        .replace(':', '.');

    d = IO.getFile(dir);

    if (d != null) {
      this.m_directory = d;
      d.mkdirs();
      f = Files.createFileInside(d, this.m_identifier,
          Files.TEXT_FILE_SUFFIX);

      this.m_personalLog = new TextWriter(f);
    } else {
      this.m_personalLog = null;
      this.m_directory = Files.TEMP_DIR;
    }

    this.m_currentDir = this.m_directory;

    this.m_grouping = ((grouping != null) ? grouping : EDataGrouping.ALL);
  }

  /**
   * Determine that the given data should be logged.
   * 
   * @param what
   *          the selection identifying the data that should be logged or
   *          <code>null<code>, if everything should be logged
   */
  public synchronized void selectForLogging(final LoggingSelection what) {
    if (what != null)
      this.m_logging |= what.m_flag;
    else
      this.m_logging = -1l;
  }

  /**
   * Determine that the given data should not be logged anymore.
   * 
   * @param what
   *          the selection identifying the data that should be logged; if
   *          <code>what==null</code>, all loggers will be deselected
   */
  public synchronized void stopLogging(final LoggingSelection what) {
    if (what != null)
      this.m_logging &= (~what.m_flag);
    else
      this.m_logging = 0;
  }

  /**
   * Check whether the given data is logged
   * 
   * @param what
   *          the data selection we want to know about
   * @return <code>true</code> if and only if this data is logged
   */
  protected boolean isLogging(final LoggingSelection what) {
    return ((what != null) && ((this.m_logging & what.m_flag) != 0));
  }

  /**
   * Obtain the success filter
   * 
   * @return the success filter
   */
  public ISuccessFilter<?> getSuccessFilter() {
    return this.m_filter;
  }

  /**
   * Set the success filter
   * 
   * @param filter
   *          the success filter
   */
  public void setSuccessFilter(final ISuccessFilter<?> filter) {
    this.m_filter = filter;
  }

  /**
   * Returns whether this test series is verbose or not.
   * 
   * @return <code>true</code> if status information is logged,
   *         <code>false</code> otherwise
   * @see #setVerbose(boolean)
   */
  public boolean isVerbose() {
    return this.m_verbose;
  }

  /**
   * Set whether this test series should be verbose or not.
   * 
   * @param verbose
   *          <code>true</code> if status information should be logged,
   *          <code>false</code> otherwise
   * @see #isVerbose()
   */
  public void setVerbose(final boolean verbose) {
    this.m_verbose = verbose;
  }

  /**
   * Log the given text
   * 
   * @param text
   *          the text to be logged
   */
  protected void log(final Object text) {
    TextWriter w;

    if ((w = this.m_personalLog) != null) {
      w.ensureNewLine();
      w.writeTime(System.currentTimeMillis(), false);
      w.write(':');
      w.write(' ');
      w.writeObject2(text);
      w.flush();
    } else
      Log.getLog().log(text);
  }

  /**
   * this method is called before each iteration
   * 
   * @param iteration
   *          the iteration number
   */
  @Override
  protected void beforeIteration(final int iteration) {
    EDataGrouping dg;
    CanonicalFile f;

    if (this.m_verbose) {
      this.log(((iteration <= 1) ? "" : TextUtils.LINE_SEPARATOR) + //$NON-NLS-1$
          "Begin of iteration #" + iteration); //$NON-NLS-1$
    }

    dg = this.m_grouping;
    if (dg != EDataGrouping.ALL) {
      f = CanonicalFile.canonicalize(new File(this.m_directory, this
          .getSeriesName()));
      f.mkdirs();
      if (dg == EDataGrouping.RUN)
        f = Files.createDirectoryInside(f);
      this.m_currentDir = f;

      if (this.m_verbose)
        this.log("Output directory is " + this.m_currentDir);//$NON-NLS-1$
    }

    super.beforeIteration(iteration);
  }

  /**
   * Obtain the success prefix
   * 
   * @return the success prefix
   */
  String getSuccessPrefix() {
    return LoggingSelection.FINAL_SUCCESS.getPrefix();
  }

  /**
   * Obtain the result individual prefix
   * 
   * @return the result individual prefix
   */
  String getResultIndividualsPrefix() {
    return LoggingSelection.RESULT_INDIVIDUALS.getPrefix();
  }

  /**
   * Obtain the result objectives prefix
   * 
   * @return the result objectives prefix
   */
  String getResultObjectivesPrefix() {
    return LoggingSelection.RESULT_OBJECTIVES.getPrefix();
  }

  /**
   * This method is invoked after each test run and lets you process all
   * the non-prevailed individuals that are produced by the optimizer. The
   * <code>results</code>-array is sorted with a sorter pipe.
   * 
   * @param results
   *          the results of the optimization process for postprocessing
   */
  @Override
  @SuppressWarnings("unchecked")
  protected void handleResults(final IPopulation<?, ?> results) {

    File f;
    TextWriter w;
    FileOutputStream fos;
    ISuccessFilter<Serializable> filter;
    FileLock x;
    int i, j, s, tc, sc, oc;
    double[] v;
    IIndividual<?, ?> uin;
    boolean nl;
    EqualObjectiveDegenerator<?, ?> nef;

    if (this.isLogging(LoggingSelection.RESULT_INDIVIDUALS)) {

      f = Files.createFileInside(this.getCurrentDirectory(), this
          .getResultIndividualsPrefix(), Files.TEXT_FILE_SUFFIX);
      if (f != null) {
        if (this.m_verbose)
          this.log("Writing all resulting individuals to " + f); //$NON-NLS-1$

        w = new TextWriter(f);

        nef = new EqualObjectiveDegenerator<Serializable, Serializable>(1d);
        nef.setOutputPipe(//
            new IndividualPrinterPipe<Serializable, Serializable>(//
                new SingleTextWriterProvider(w)));
        results.writeToPipe((IPipeIn) (nef));
      }
    }

    if (this.isLogging(LoggingSelection.RESULT_OBJECTIVES)) {

      f = Files.createFileInside(this.getCurrentDirectory(), this
          .getResultObjectivesPrefix(), Files.TEXT_FILE_SUFFIX);
      if (f != null) {
        if (this.m_verbose)
          this
              .log("Writing all objectives of the resulting individuals to " //$NON-NLS-1$
                  + f);

        w = new TextWriter(f);

        results.writeToPipe(//
            (IPipeIn) new ObjectivePrinterPipe<Serializable, Serializable>(//
                new SingleTextWriterProvider(w)));
      }
    }

    if (this.isLogging(LoggingSelection.FINAL_SUCCESS)
        && ((filter = ((ISuccessFilter) (this.m_filter))) != null)) {

      s = (results.size() - 1);
      oc = 0;
      sc = 0;
      tc = 0;
      if (s >= 0) {
        v = new double[results.get(0).getObjectiveValueCount()];

        outer: for (i = s; i >= 0; i--) {
          uin = results.get(0);

          for (j = s; j > i; j--) {
            if (uin.equals(results.get(j)))
              continue outer;
          }

          tc++;
          for (j = (v.length - 1); j >= 0; j--)
            v[j] = uin.getObjectiveValue(j);
          if (filter.isSuccess(v)) {
            sc++;
            if (filter.isOverfitted(uin.getPhenotype()))
              oc++;
          }
        }
      }

      try {
        switch (this.m_grouping) {
        case RUN:
        case SERIES: {
          f = new File(this.m_directory, this.getSeriesName());
          break;
        }
        default:
          f = this.getCurrentDirectory();
        }

        f = new File(f, this.getSuccessPrefix()
            + Files.EXTENSION_SEPARATOR_STR + Files.TEXT_FILE_SUFFIX);
        nl = f.createNewFile();

        fos = new FileOutputStream(f, true);
        try {
          x = fos.getChannel().lock();
          if (x != null) {
            try {

              w = new TextWriter(fos);
              try {
                if (nl) {
                  w.write("total"); //$NON-NLS-1$
                  w.writeCSVSeparator();
                  w.write("different"); //$NON-NLS-1$
                  w.writeCSVSeparator();
                  w.write("success");//$NON-NLS-1$
                  w.writeCSVSeparator();
                  w.write("correct");//$NON-NLS-1$
                }
                w.newLine();
                w.writeInt(s + 1);
                w.writeCSVSeparator();
                w.writeInt(tc);
                w.writeCSVSeparator();
                w.writeInt(sc);
                w.writeCSVSeparator();
                w.writeInt(sc - oc);

                w.flush();
                fos.flush();

                x.release();
                x = null;
              } finally {
                w.close();
                fos = null;
              }
            } finally {
              if (x != null)
                x.release();
            }
          }
        } finally {
          if (fos != null)
            fos.close();
        }
      } catch (Throwable t) {
        this.log(t);
      }
    }

    super.handleResults(results);
  }

  /**
   * This method is called whenever an error occured.
   * 
   * @param t
   *          the error
   */
  @Override
  protected void onError(final Throwable t) {
    this.cleanUp();
    this.log(t);
  }

  /**
   * this method is called after each iteration
   * 
   * @param iteration
   *          the iteration number
   * @param timeSpent
   *          the time spent in this iteration
   */
  @Override
  protected void afterIteration(final int iteration, final long timeSpent) {
    super.afterIteration(iteration, timeSpent);
    if (this.m_verbose)
      this.log("End of iteration #" + iteration + //$NON-NLS-1$
          " after " + TextUtils.timeToString(timeSpent, true));//$NON-NLS-1$
  }

  /**
   * Obtain the directory
   * 
   * @return the directory
   */
  protected CanonicalFile getRootDirectory() {
    return this.m_directory;
  }

  /**
   * Obtain the grouping of the data
   * 
   * @return the grouping of the data
   */
  public EDataGrouping getDataGrouping() {
    return this.m_grouping;
  }

  /**
   * Obtain the name for the series. This method is used if and only if the
   * data grouping involves at least the series level.
   * 
   * @return the name for the series directory
   */
  protected String getSeriesName() {
    return String.valueOf(System.currentTimeMillis());
  }

  /**
   * Obtain the directory the current test series is to leave its output
   * in.
   * 
   * @return the directory the current test series is to leave its output
   *         in
   */
  protected CanonicalFile getCurrentDirectory() {
    return this.m_currentDir;
  }

  /**
   * Obtan the identifier
   * 
   * @return the identifier
   */
  protected String getIdentifier() {
    return this.m_identifier;
  }

  /**
   * Run this test series
   */
  @Override
  protected void doRun() {
    TextWriterPrintStream ps;
    PrintStream oldErr, oldOut;

    if (this.m_verbose && (this.m_personalLog != null)) {
      this.m_personalLog.addRef();
      ps = new TextWriterPrintStream(this.m_personalLog);

      oldErr = System.err;
      oldOut = System.out;

      System.setErr(ps);
      System.setOut(ps);

      this.log("Running on " + this.getProcessorCount() + //$NON-NLS-1$
          " processors.");//$NON-NLS-1$

    } else {
      ps = null;
      oldErr = null;
      oldOut = null;
    }

    super.doRun();

    if (oldErr != null)
      System.setErr(oldErr);
    if (oldOut != null)
      System.setOut(oldOut);

    if (ps != null) {
      try {
        ps.close();
      } catch (Throwable qqq) {
        ErrorUtils.onError(qqq);
      }
    }

  }

  /**
   * Set that this activity is finished. This leads to a transition into
   * the state <code>TERMINATED</code>.
   */
  @Override
  protected void finished() {
    if (this.m_personalLog != null) {
      try {
        this.m_personalLog.release();
      } finally {
        this.m_personalLog = null;
      }
    }

    this.cleanUp();

    super.finished();
  }
}
