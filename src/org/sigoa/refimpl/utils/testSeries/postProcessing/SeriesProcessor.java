/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-10-02
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.utils.testSeries.postProcessing.SeriesProcessor.java
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

import org.sfc.io.CanonicalFile;
import org.sfc.io.Files;
import org.sfc.io.IO;
import org.sfc.io.TextWriter;
import org.sfc.io.writerProvider.FileTextWriterProvider;
import org.sfc.io.writerProvider.IWriterProvider;

/**
 * A series processor
 * 
 * @author Thomas Weise
 */
public class SeriesProcessor implements ISeriesProcessor {

  /**
   * The writer provider
   */
  private IWriterProvider<TextWriter> m_provider;

  /**
   * the type of the processor
   */
  private final EProcessorTypes m_type;

  /**
   * the prefix
   */
  private final String m_prefix;

  /**
   * the ouput directory
   */
  private CanonicalFile m_dir;

  /**
   * the text writer to write to
   */
  private TextWriter m_out;

  /**
   * the name of the current series
   */
  private String m_seriesName;

  /**
   * the name of the current run
   */
  private String m_runName;

  /**
   * the number of runs expected in the current series
   */
  private int m_runCnt;

  /**
   * Create a new series processor
   * 
   * @param dir
   *          the directory
   * @param prefix
   *          the prefix
   * @param type
   *          the type of the processor
   */
  protected SeriesProcessor(final Object dir, final String prefix,
      final EProcessorTypes type) {
    super();

    this.m_type = ((type != null) ? type : EProcessorTypes.ALL);
    this.m_dir = IO.getFile(dir);
    this.m_dir.mkdirs();
    this.m_prefix = prefix;

  }

  /**
   * Begin the series of the given name
   * 
   * @param name
   *          the name of the series
   * @param runCnt
   *          the number of runs to expect
   */
  public void beginSeries(final String name, final int runCnt) {
    CanonicalFile f;

    this.m_seriesName = name;
    this.m_runName = null;
    this.m_runCnt = runCnt;

    switch (this.m_type) {
    case SERIES: {
      this.m_out = this.m_provider.provideWriter(name);
      break;
    }

    case RUN: {
      f = IO.getFile(new File(this.m_dir, name));
      f.mkdirs();
      this.m_provider = new FileTextWriterProvider(f, this.m_prefix);
      break;
    }

    default:
    }
  }

  /**
   * begin a new run
   * 
   * @param name
   *          the name of the run
   */
  public void beginRun(final String name) {

    this.m_runName = name;

    if (this.m_type == EProcessorTypes.RUN)
      this.m_out = this.m_provider.provideWriter(name);
  }

  /**
   * end a run
   */
  public void endRun() {
    TextWriter w;

    this.m_runName = null;

    switch (this.m_type) {
    case RUN: {
      try {
        w = this.m_out;
        if (w != null)
          w.release();
      } finally {
        this.m_out = null;
      }
      break;
    }

    case SERIES: {
      w = this.m_out;
      if (w != null)
        w.flush();
      break;
    }
    default:
    }
  }

  /**
   * Process the given run
   * 
   * @param data
   *          the data of the run
   */
  public void processRun(final double[][][] data) {
    //
  }

  /**
   * end the current series
   */
  public void endSeries() {
    TextWriter w;

    this.m_seriesName = null;
    this.m_runCnt = 0;

    switch (this.m_type) {
    case SERIES: {
      try {
        w = this.m_out;
        if (w != null)
          w.release();
      } finally {
        this.m_out = null;
      }
      break;
    }
    case ALL: {
      w = this.m_out;
      if (w != null)
        w.flush();
      break;
    }
    default:
    }
  }

  /**
   * begin processing the data
   */
  public void beginProcessing() {

    this.m_seriesName = null;
    this.m_runCnt = 0;
    this.m_runName = null;

    switch (this.m_type) {
    case ALL: {
      this.m_out = new TextWriter(new File(this.m_dir, this.m_prefix
          + Files.TEXT_FILE_SUFFIX));
      break;
    }

    case SERIES: {
      this.m_provider = new FileTextWriterProvider(this.m_dir,
          this.m_prefix);
      break;
    }

    default:
    }
  }

  /**
   * end processing the data
   */
  public void endProcessing() {
    if (this.m_type == EProcessorTypes.ALL) {
      try {
        if (this.m_out != null)
          this.m_out.release();
      } finally {
        this.m_out = null;
      }
    }

  }

  /**
   * Obtain the textwriter currently used to write to.
   * 
   * @return the textwriter currently used to write to
   */
  protected final TextWriter getOut() {
    return this.m_out;
  }

  /**
   * Obtain the name of the given series
   * 
   * @return the name of the given series
   */
  protected final String getSeriesName() {
    return this.m_seriesName;
  }

  /**
   * Obtain the name of the current run.
   * 
   * @return the name of the current run
   */
  protected final String getRunName() {
    return this.m_runName;
  }

  /**
   * Obtain the number of runs in the current series
   * 
   * @return the number of runs in the current series
   */
  protected final int getRunCount() {
    return this.m_runCnt;
  }
}
