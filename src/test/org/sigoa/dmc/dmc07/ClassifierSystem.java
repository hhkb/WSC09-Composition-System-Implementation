/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-05-09
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.dmc.ClassifierSystem.java
 * Last modification: 2007-05-09
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

package test.org.sigoa.dmc.dmc07;

import java.io.Serializable;

import org.sfc.text.JavaTextable;
import org.sfc.text.TextUtils;

/**
 * A rule set for classifying.
 *
 * @author Thomas Weise
 */
public class ClassifierSystem extends JavaTextable implements Serializable {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the classes
   */
  private static final EClasses[] CLASSES = EClasses.values();

  /**
   * the default classes
   */
  public static final EClasses DEFAULT_CLASS = CLASSES[CLASSES.length - 1];

  /**
   * the dataset len
   */
  public static final int DATASET_LEN = (20 - 3);

  /**
   * the bits needed to represent a class
   */
  static final int CLASS_BITS;
  static {
    int j;

    j = 0;
    while ((1 << j) < CLASSES.length)
      j++;

    CLASS_BITS = (j + 1);
  }

  /**
   * The granularity
   */
  public static final int GRANULARITY = ((DATASET_LEN * 2) + CLASS_BITS);

  /**
   * the max deviation
   */
  private static final int MAX_DEV = (DATASET_LEN / 5);

  /**
   * the rules
   */
  private final byte[][] m_rules;

  /**
   * the results
   */
  private final EClasses[] m_results;

  // /**
  // * Decode a genotype to a rule.
  // *
  // * @param genotype
  // * the genotype
  // */
  // public RBGPNetVM(final byte[] genotype) {
  // super();
  //
  // int i, j, c;
  // byte[][] rules;
  // byte[] rule;
  // EClasses[] results;
  // BinaryInputStream bis;
  //
  // c = ((genotype.length * 8) / GRANULARITY);
  // bis = new BinaryInputStream();
  // bis.init(genotype);
  //
  // this.m_rules = rules = new byte[c][DATASET_LEN];
  // this.m_results = results = new EClasses[c];
  //
  // for (i = 0; i < c; i++) {
  // rule = rules[i];
  // for (j = 0; j < DATASET_LEN; j++) {
  // rule[j] = (byte) (bis.readBits(2));
  // }
  // results[i] = (CLASSES[bis.readBits(CLASS_BITS)
  // % EClasses.CLASS_COUNT]);
  // }
  // }

  /**
   * Create a classifier system.
   *
   * @param rules
   *          the rules
   * @param results
   *          the results
   */
  public ClassifierSystem(final byte[][] rules, final EClasses[] results) {
    super();
    this.m_results = results;
    this.m_rules = rules;
  }

  /**
   * Classify a data set.
   *
   * @param data
   *          the data set
   * @return the classification
   */
  public EClasses classify(final byte[] data) {
    byte[][] rules;
    byte[] rule;
    int i, j, ec, lec, leci;

    rules = this.m_rules;

    lec = Integer.MAX_VALUE;
    leci = 0;

    main: for (i = (rules.length - 1); i >= 0; i--) {
      rule = rules[i];
      ec = 0;
      for (j = (rule.length - 1); j >= 0; j--) {
        switch (rule[j]) {
        case 0: {
          if (data[j] != 0)
            if ((++ec) > MAX_DEV)
              continue main;
          break;
        }
        case 1: {
          if (data[j] < 1) // != 1
            if ((++ec) > MAX_DEV)
              continue main;
          break;
        }
        case 2: {
          if (data[j] > 1)// <= 0)
            if ((++ec) > MAX_DEV)
              continue main;
          break;
        }
        }
      }

      if (ec <= 0)
        return this.m_results[i];
      if (ec < lec) {
        lec = ec;
        leci = i;
      }
    }

    if (lec <= MAX_DEV)
      return this.m_results[leci];
    return EClasses.N;
  }

  // /**
  // * Classify a data set.
  // *
  // * @param data
  // * the data set
  // * @return the classification
  // */
  // public EClasses classify(final byte[] data) {
  // byte[][] rules;
  // byte[] rule;
  // int i, j, ec, lec, leci, dcc, ldcc;
  //
  // rules = this.m_rules;
  //
  // ldcc = lec = Integer.MAX_VALUE;
  // leci = 0;
  //
  // main: for (i = (rules.length - 1); i >= 0; i--) {
  // rule = rules[i];
  // ec = 0;
  // dcc = 0;
  // for (j = (rule.length - 1); j >= 0; j--) {
  // switch (rule[j]) {
  // case 0: {
  // if (data[j] != 0)
  // if ((++ec) > MAX_DEV)
  // continue main;
  // break;
  // }
  // case 1: {
  // if (data[j] < 1) // != 1
  // if ((++ec) > MAX_DEV)
  // continue main;
  // break;
  // }
  // case 2: {
  // if (data[j] <= 1)// <= 0)
  // if ((++ec) > MAX_DEV)
  // continue main;
  // break;
  // }
  //
  // default: {
  // dcc++;
  // break;
  // }
  // }
  // }
  //
  // if (ec <= 0)
  // return this.m_results[i];
  // if ((ec < lec) && (dcc < ldcc)) {
  // lec = ec;
  // leci = i;
  // ldcc = dcc;
  // }
  // }
  //
  // if (lec <= MAX_DEV)
  // return this.m_results[leci];
  // return EClasses.N;
  // }

  /**
   * Append this object's textual representation to a string builder.
   *
   * @param sb
   *          The string builder to append to.
   * @see #toString()
   */
  @Override
  public void toStringBuilder(final StringBuilder sb) {
    byte[][] rules;
    byte[] rule;
    EClasses[] results;
    int i, j;

    rules = this.m_rules;
    results = this.m_results;

    for (i = (rules.length - 1); i >= 0; i--) {
      rule = rules[i];
      for (j = 0; j < rule.length; j++)
        sb.append(rule[j]);
      sb.append(' ');
      sb.append(results[i]);
      if (i > 0)
        sb.append(TextUtils.LINE_SEPARATOR);
    }
  }

  /**
   * Serializes the parameters of the constructor of this object.
   *
   * @param sb
   *          the string builder
   * @param indent
   *          an optional parameter denoting the indentation
   */
  @Override
  protected void javaParametersToStringBuilder(final StringBuilder sb,
      final int indent) {
    EClasses[] results;
    String ecc;
    int i;

    sb.append("new ");//$NON-NLS-1$
    TextUtils.append(this.m_rules, sb);
    sb.append(',');
    sb.append(' ');
    sb.append("new EClasses[] {"); //$NON-NLS-1$
    results = this.m_results;
    ecc = EClasses.class.getCanonicalName();
    for (i = 0; i < results.length; i++) {
      if (i > 0) {
        sb.append(',');
        sb.append(' ');
      }
      sb.append(ecc);
      sb.append('.');
      sb.append(results[i]);
    }
    sb.append('}');
  }

  /**
   * Obtain the size of this rule.
   *
   * @return the size of this rule
   */
  public int getSize() {
    return this.m_results.length;
  }

  /**
   * Obtain the result array.
   *
   * @return the result array
   */
  public EClasses[] getResults() {
    return this.m_results.clone();
  }

  /**
   * Obtain the rules.
   *
   * @return the rules
   */
  public byte[][] getRules() {
    byte[][] r;
    int i;

    r = this.m_rules.clone();
    for (i = (r.length - 1); i >= 0; i--) {
      r[i] = r[i].clone();
    }

    return r;
  }
}
