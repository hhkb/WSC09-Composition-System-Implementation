/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-17
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.genomes.doubleString.reproduction.DoubleStringEditor.java
 * Last modification: 2007-12-17
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

package org.sigoa.refimpl.genomes.doubleString.reproduction;

import java.util.Arrays;

import org.sfc.math.Mathematics;
import org.sigoa.refimpl.genomes.string.StringEditor;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * An editor for <code>double</code> strings.
 * 
 * @author Thomas Weise
 */
public class DoubleStringEditor extends StringEditor<double[]> {

  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the maximum allowed value for a double gene
   */
  static final double MAX = (0.003 * Double.MAX_VALUE);

  /**
   * the minimum allowed value for a double gene
   */
  static final double MIN = (-MAX);

  /**
   * the maximum span
   */
  static final double MAX_SPAN = (MAX - MIN);

  /**
   * the number of sigma levels
   */
  private static final int SIGMA_COUNT = 5;

  /**
   * the default sigmas
   */
  private static final double[] DEFAULT_SIGMAS = computeSigmas(MAX_SPAN);

  /**
   * The maximum values of the genes
   */
  final double[] m_maxValues;

  /**
   * The maximum values of the genes
   */
  final double[] m_minValues;

  /**
   * The spans values of the genes
   */
  final double[] m_spans;

  /**
   * the sigmas
   */
  final double[][] m_sigmas;

  /**
   * Instantiate a new string editor operation
   * 
   * @param granularity
   *          the number of elements a gene of the genotype consists of
   * @param minLength
   *          the minimum length in genes of a genotype
   * @param maxLength
   *          the maximum length in genes of a genotype
   * @param maxValues
   *          the maximum values of the genes
   * @param minValues
   *          the minimum values of the genes
   * @throws IllegalArgumentException
   *           if
   *           <code>granularity &lt;=0||minLength&lt;=0||maxLength&lt;minLength</code>
   */
  public DoubleStringEditor(final int granularity, final int minLength,
      final int maxLength, final double[] minValues,
      final double[] maxValues) {
    super(granularity, minLength, maxLength);

    double[] spans, mi, ma;
    double[][] sigmas;
    int i;
    final int c;
    double d1, d2;

    this.m_maxValues = ma = maxValues.clone();
    this.m_minValues = mi = minValues.clone();

    c = mi.length;
    this.m_spans = spans = new double[c];
    this.m_sigmas = sigmas = new double[c][];

    for (i = (c - 1); i >= 0; i--) {
      d1 = ma[i];
      d2 = mi[i];

      if (!(Mathematics.isNumber(d1)))
        d1 = MAX;
      else
        d1 = Math.min(d1, MAX);

      if (!(Mathematics.isNumber(d2)))
        d2 = MIN;
      else
        d2 = Math.max(d2, MIN);

      if (d1 < d2) {
        ma[i] = d2;
        mi[i] = d2 = d1;
        d1 = ma[1];
      }
      d1 -= d2;

      if (!(Mathematics.isNumber(d1)))
        d1 = MAX_SPAN;
      else
        d1 = Math.min(MAX_SPAN, Math.max(0d, d1));

      spans[i] = d1;

      sigmas[i] = ((d1 < MAX_SPAN) ? computeSigmas(d1) : DEFAULT_SIGMAS);
    }

  }

  /**
   * Check whether a given individual is valid or not.
   * 
   * @param data
   *          the individual data
   * @return <code>true</code> if and only if the data is valid
   */
  public final boolean isValid(final double[] data) {
    int l, g;
    double[] min, max;

    if (data == null)
      return false;

    l = data.length;
    g = (l / this.getGranularity());
    if ((g < this.getMinLength()) || (g > this.getMaxLength()))
      return false;

    min = this.m_minValues;
    max = this.m_maxValues;

    for (l = (Math.min(l, Math.min(min.length, max.length)) - 1); l >= 0; l--) {
      if ((data[l] < min[l]) || (data[l] > max[l]))
        return false;
    }

    return true;
  }

  /**
   * Compute the sigma values
   * 
   * @param span
   *          the given span
   * @return the sigmas
   */
  private static final double[] computeSigmas(final double span) {
    double[] sig;
    double d1, d;
    int l;

    l = SIGMA_COUNT;
    sig = new double[l];
    d1 = span;

    d = 0.3;
    for (--l; l >= 0; l--) {
      d1 *= d;
      sig[l] = d1;
      d *= 0.6;
    }

    return sig;
  }

  /**
   * Instantiate a new string editor operation
   * 
   * @param granularity
   *          the number of elements a gene of the genotype consists of
   * @param minLength
   *          the minimum length in genes of a genotype
   * @param maxLength
   *          the maximum length in genes of a genotype
   * @param maxValue
   *          the maximum value of the genes
   * @param minValue
   *          the minimum value of the genes
   * @throws IllegalArgumentException
   *           if
   *           <code>granularity &lt;=0||minLength&lt;=0||maxLength&lt;minLength</code>
   */
  public DoubleStringEditor(final int granularity, final int minLength,
      final int maxLength, final double minValue, final double maxValue) {
    this(granularity, minLength, maxLength, createArray(maxLength,
        minValue), createArray(maxLength, maxValue));
  }

  /**
   * Create an array of the given length with the given values
   * 
   * @param len
   *          the length
   * @param value
   *          the value
   * @return the new array
   */
  private static final double[] createArray(final int len,
      final double value) {
    double[] d;

    d = new double[len];
    Arrays.fill(d, value);
    return d;
  }

  /**
   * Copy a genotype
   * 
   * @param genotype
   *          the genotype
   * @return the copy of the genotype
   */
  @Override
  public double[] copy(final double[] genotype) {
    return genotype.clone();
  }

  /**
   * Obtain the length of the genotype in elements.
   * 
   * @param genotype
   *          the genotype
   * @return its length, or 0 if the length could not be determined
   */
  @Override
  public int getLength(final double[] genotype) {
    return (genotype != null) ? genotype.length : 0;
  }

  /**
   * Create a new, uninitialized string of the given length (in elements).
   * 
   * @param length
   *          the length of the string to be created
   * @return the new string
   */
  @Override
  public double[] createGenotype(final int length) {
    return new double[length];
  }

  /**
   * Store <code>count</code> random elements into <code>genotype</code>
   * beginning at position <code>start</code>.
   * 
   * @param genotype
   *          the genotype
   * @param start
   *          the start index
   * @param count
   *          the number of elements to change
   * @param random
   *          the randomizer
   */
  @Override
  public void storeRandomElements(final double[] genotype,
      final int start, final int count, final IRandomizer random) {
    int i;
    double[] min, span;
    double mi, spa;

    min = this.m_minValues;
    span = this.m_spans;
    i = min.length;

    for (i = (start + count - 1); i >= start; i--) {

      mi = (i < min.length) ? min[i] : MIN;
      spa = (i < span.length) ? span[i] : MAX_SPAN;

      genotype[i] = (mi + (random.nextDouble() * spa));
    }

  }

  /**
   * Change <code>count</code> elements in <code>genotype</code>
   * beginning at position <code>start</code>.
   * 
   * @param genotype
   *          the genotype
   * @param start
   *          the start index
   * @param count
   *          the number of elements to change
   * @param random
   *          the randomizer
   */
  @Override
  public void changeElements(final double[] genotype, final int start,
      final int count, final IRandomizer random) {
    double[] v;
    final double[] min, max;
    double mi, ma, n;
    int i;

    min = this.m_minValues;
    max = this.m_maxValues;

    for (i = (start + count - 1); i >= start; i--) {

      ma = (i < max.length) ? max[i] : MAX;
      mi = (i < min.length) ? min[i] : MIN;
      v = ((i < this.m_sigmas.length) ? this.m_sigmas[i] : DEFAULT_SIGMAS);

      do {
        n = random.nextNormal(genotype[i], v[random.nextInt(v.length)]);
      } while ((n < mi) || (n > ma) || (n == genotype[i]));

      genotype[i] = n;
    }

  }
}
