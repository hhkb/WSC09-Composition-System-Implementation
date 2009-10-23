/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-29
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.utils.testSeries.ParameterizedTestSeries.java
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
import java.util.Iterator;
import java.util.List;

import org.sfc.collections.CollectionUtils;
import org.sfc.collections.iterators.ICombinatorics;
import org.sfc.collections.iterators.IteratorBase;
import org.sfc.io.Files;
import org.sfc.io.TextWriter;
import org.sfc.text.TextUtils;

/**
 * A parameterized test series works on the
 * 
 * @author Thomas Weise
 */
public abstract class ParameterizedTestSeries extends LoggingTestSeries
    implements ICombinatorics {

  /**
   * log the parameters
   */
  public static final LoggingSelection RUN_PARAMETERS = //
  new LoggingSelection("parameters");//$NON-NLS-1$

  /**
   * the empty object array
   */
  static final String[] EMPTY_OBJECT_ARRAY = new String[0];

  /**
   * the empty parameter iterator
   */
  public static final Iterator<Object[]> EMPTY_ITERATOR = new IteratorBase<Object[]>() {
    /**
     * the serial version uid
     */
    private static final long serialVersionUID = 1;

    public boolean hasNext() {
      return true;
    }

    public Object[] next() {
      return EMPTY_OBJECT_ARRAY;
    }

    @Override
    public int getCombinationCount() {
      return 1;
    }
  };

  /**
   * The default test series title
   */
  private static final String DEFAULT_TITLE = "test"; //$NON-NLS-1$

  /**
   * the titles
   */
  private final String[] m_titles;

  /**
   * the short titles for the directory name
   */
  private final String[] m_st;

  /**
   * the parameter iterator
   */
  private final Iterator<Object[]> m_iterator;

  /**
   * the current parameters
   */
  private Object[] m_parameters;

  /**
   * Create a new parameterized test series
   * 
   * @param dir
   *          the directory to write to
   * @param grouping
   *          how the data should be grouped
   * @param maxRuns
   *          the maximum runs this test series is running
   */
  protected ParameterizedTestSeries(final Object dir,
      final EDataGrouping grouping, final int maxRuns) {
    this(dir, grouping, maxRuns, null, null);
  }

  /**
   * Create a new parameterized test series
   * 
   * @param titles
   *          the titles of the test series parameters
   * @param iterator
   *          the parameter iterator
   * @param dir
   *          the directory to write to
   * @param grouping
   *          how the data should be grouped
   * @param maxRuns
   *          the maximum runs this test series is running
   */
  protected ParameterizedTestSeries(final Object dir,
      final EDataGrouping grouping, final int maxRuns,
      final String[] titles, final Iterator<Object[]> iterator) {
    super(dir, grouping, maxRuns);

    RUN_PARAMETERS.getPrefix();

    // int l, x;

    this.m_titles = ((titles != null) ? titles : EMPTY_OBJECT_ARRAY);
    this.m_iterator = (iterator != null) ? iterator : EMPTY_ITERATOR;

    this.m_st = ((titles != null) ? createShortTitles(titles)
        : EMPTY_OBJECT_ARRAY);
  }

  /**
   * Obtain the number of maximum possible combinations
   * 
   * @return the number of maximum possible combinations or <code>-1</code>
   *         if it could not be determined
   */
  public int getCombinationCount() {
    final Iterator<Object[]> it;
    int i;

    it = this.m_iterator;
    if (it == EMPTY_ITERATOR)
      return 1;
    if (!(it instanceof ICombinatorics))
      return 1;

    i = ((ICombinatorics) (it)).getCombinationCount();
    if (i <= 0)
      return 1;
    return i;
  }

  /**
   * Limit the test runs to the number of configurations
   */
  public void limitRunsToAllConfigurations() {
    int max;
    max = this.getCombinationCount();
    this.log("limiting runs to " + max); //$NON-NLS-1$
    this.setMaxRuns(max);
  }

  /**
   * Create short titles for the configurations
   * 
   * @param titles
   *          the titles
   * @return the short titles
   */
  private static final String[] createShortTitles(final String[] titles) {

    String[] st;
    String s, t;
    StringBuilder sb;
    int l, i, j, x, y, tl;
    char ch;

    l = titles.length;
    sb = new StringBuilder();
    st = new String[l];
    l--;

    main: for (i = l; i >= 0; i--) {
      t = titles[i].trim().toLowerCase();
      tl = t.length() - 1;

      sb.append(t.charAt(0));
      x = 0;
      for (;;) {
        x++;
        y = Integer.MAX_VALUE;
        j = t.indexOf(' ', x);
        if ((j > 0) && (j < y))
          y = j;
        j = t.indexOf('_', x);
        if ((j > 0) && (j < y))
          y = j;
        j = t.indexOf('-', x);
        if ((j > 0) && (j < y))
          y = j;
        j = t.indexOf(',', x);
        if ((j > 0) && (j < y))
          y = j;

        if (y >= tl)
          break;

        x = y + 1;
        ch = t.charAt(x);
        if (ch > 32)
          sb.append(ch);
      }

      s = sb.toString().trim();
      sb.setLength(0);

      checker: {
        for (j = l; j > i; j--) {
          if (st[j].equals(s))
            break checker;
        }
        st[i] = s;
        continue main;
      }

      s = ""; //$NON-NLS-1$
      outer: for (x = 1; x <= t.length(); x++) {
        s = t.substring(0, x);
        for (j = l; j > i; j--) {
          if (st[j].equals(s))
            continue outer;
        }
        break;
      }
      st[i] = s;
    }

    return st;
  }

  /**
   * This method is called for setting up the parameters
   * 
   * @param parameters
   *          the parameters
   */
  protected void setupParameters(final Object[] parameters) {
    //
  }

  /**
   * Obtain the name for the series. This method is used if and only if the
   * data grouping involves at least the series level.
   * 
   * @return the name for the series directory
   */
  @Override
  protected String getSeriesName() {
    StringBuilder sb;
    String[] st;
    int i;

    st = this.m_st;

    if (st.length > 0) {

      sb = new StringBuilder();
      for (i = (st.length - 1); i >= 0; i--) {
        sb.append(st[i]);
        sb.append('=');
        if (this.m_parameters[i] instanceof Boolean)
          sb.append(((Boolean) (this.m_parameters[i])).booleanValue() ? 1
              : 0);
        else
          TextUtils.append(this.m_parameters[i], sb);
        if (i > 0)
          sb.append(',');
      }

      return sb.toString();
    }
    return DEFAULT_TITLE;
  }

  /**
   * Log the parameters to the given text writer
   * 
   * @param w
   *          the text writer
   */
  protected void writeParameters(final TextWriter w) {
    int i;
    String[] titles;

    titles = this.m_titles;
    if (titles.length > 0) {
      for (i = (titles.length - 1); i >= 0; i--) {
        w.ensureNewLine();
        w.write(titles[i]);
        w.writeChar(':');
        w.writeChar(' ');
        w.writeObject2(this.m_parameters[i]);
      }
    }
  }

  /**
   * this method is called before each iteration
   * 
   * @param iteration
   *          the iteration number
   */
  @Override
  protected void beforeIteration(final int iteration) {
    String t;
    File f;
    TextWriter tw;

    if (this.m_iterator.hasNext()) {
      this.setupParameters(this.m_parameters = this.m_iterator.next());
    }

    super.beforeIteration(iteration);

    if ((this.isVerbose()) && (this.m_personalLog != null))
      this.writeParameters(this.m_personalLog);

    if (this.isLogging(RUN_PARAMETERS)) {

      t = RUN_PARAMETERS.getPrefix();
      if (this.getDataGrouping().ordinal() < EDataGrouping.SERIES
          .ordinal()) {
        t = this.getSeriesName() + Files.EXTENSION_SEPARATOR_STR + t;
      }

      f = Files.createFileInside(this.getCurrentDirectory(), t,
          Files.TEXT_FILE_SUFFIX);

      if (this.isVerbose())
        this.log("Logging parameters to " + f); //$NON-NLS-1$

      tw = new TextWriter(f);
      this.writeParameters(tw);
      tw.release();
    }
  }

  /**
   * Give access to the current parameters
   * 
   * @return the current parameters
   */
  protected Object[] getParameters() {
    return this.m_parameters;
  }

  /**
   * Parse the name of a directory/series and obtain the parameter values
   * valid for it.
   * 
   * @param name
   *          the directory/series name
   * @return the array of parameter values
   */
  public static final String[] parseParameterString(final String name) {
    List<String> sl;
    int s, e;

    sl = CollectionUtils.createList();

    e = 0;
    for (;;) {
      s = name.indexOf('=', e);
      if (s < 0)
        break;
      e = name.indexOf(',', s);
      if (e < 0)
        e = name.length();
      sl.add(0, name.substring(s + 1, e).trim());
    }

    return sl.toArray(new String[sl.size()]);
  }

  /**
   * Setup the individual evaluator parameters
   */
  protected void setupIndividualEvaluatorParameters() {
    this.setupParameters(this.m_parameters = this.m_iterator.next());
  }

  /**
   * Create an individual evaluator
   * 
   * @return the new individual evaluator
   */
  @Override
  public synchronized IndividualEvaluator createIndividualEvaluator() {
    IndividualEvaluator x;

    this.setupIndividualEvaluatorParameters();
    x = super.createIndividualEvaluator();
    this.setupParameters(this.m_parameters = this.m_iterator.next());
    return x;
  }

  // /**
  // * This method can be used to evaluate a single phenotype.
  // *
  // * @param genotype
  // * the genotype (or <code>null</code>)
  // * @param phenotype
  // * the phenotype to be evaluated (or <code>null</code>)
  // * @param iterations
  // * the number of test iterations to perform
  // * @return an individual record containing the evaluated data
  // */
  // @Override
  // public IIndividual<?, ?>[] evaluate(final Serializable genotype,
  // final Serializable phenotype, final int iterations) {
  // if (this.m_iterator.hasNext()) {
  // this.setupParameters(this.m_parameters = this.m_iterator.next());
  // }
  // return super.evaluate(genotype, phenotype, iterations);
  // }

  /**
   * Obtain the result individual prefix
   * 
   * @return the result individual prefix
   */
  @Override
  String getResultIndividualsPrefix() {
    String pre;

    pre = super.getResultIndividualsPrefix();
    if (this.getDataGrouping().ordinal() >= EDataGrouping.RUN.ordinal())
      return pre;

    return this.getSeriesName() + Files.EXTENSION_SEPARATOR_STR + pre;
  }

  /**
   * Obtain the result objectives prefix
   * 
   * @return the result objectives prefix
   */
  @Override
  String getResultObjectivesPrefix() {
    String pre;

    pre = super.getResultObjectivesPrefix();
    if (this.getDataGrouping().ordinal() >= EDataGrouping.RUN.ordinal())
      return pre;

    return this.getSeriesName() + Files.EXTENSION_SEPARATOR_STR + pre;
  }

  /**
   * Obtain the success prefix
   * 
   * @return the success prefix
   */
  @Override
  String getSuccessPrefix() {
    String pre;

    pre = super.getSuccessPrefix();
    if (this.getDataGrouping().ordinal() >= EDataGrouping.SERIES.ordinal())
      return pre;

    return this.getSeriesName() + Files.EXTENSION_SEPARATOR_STR + pre;
  }

}
