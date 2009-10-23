/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-22 06:18:01
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.Individual.java
 * Version          : 1.0.1
 * Last modification: 2008-05-15
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

package org.sigoa.refimpl.go;

import java.io.Serializable;

import org.sfc.text.IJavaTextable;
import org.sfc.text.TextUtils;
import org.sfc.text.Textable;
import org.sfc.utils.ErrorUtils;
import org.sfc.utils.ICloneable;
import org.sfc.utils.Utils;
import org.sigoa.spec.go.IIndividual;
import org.sigoa.spec.go.OptimizationUtils;

/**
 * The default implementation of the individual record.
 * 
 * @param <G>
 *          The genotype.
 * @param <PP>
 *          The phenotype.
 * @author Thomas Weise
 */
public class Individual<G extends Serializable, PP extends Serializable>
    extends Textable implements IIndividual<G, PP>, ICloneable {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the genotype
   */
  public static final String GENOTYPE = "genotype  : "; //$NON-NLS-1$

  /**
   * the phenotype
   */
  public static final String PHENOTYPE = "phenotype : "; //$NON-NLS-1$

  /**
   * objectives
   */
  public static final String OBJECTIVES = "objectives: {"; //$NON-NLS-1$

  /**
   * fitness
   */
  public static final String FITNESS = "fitness   : "; //$NON-NLS-1$

  /**
   * the same as
   */
  public static final String SAMEAS = "same as genotype"; //$NON-NLS-1$

  /**
   * the genotype
   */
  private static final char[] CHR_GENOTYPE = GENOTYPE.toCharArray();

  /**
   * the phenotype
   */
  private static final char[] CHR_PHENOTYPE = PHENOTYPE.toCharArray();

  /**
   * objectives
   */
  private static final char[] CHR_OBJECTIVES = OBJECTIVES.toCharArray();

  /**
   * fitness
   */
  private static final char[] CHR_FITNESS = FITNESS.toCharArray();

  /**
   * the placeholder
   */
  private static final char[] CHR_PLACEHOLDER = "            ".toCharArray(); //$NON-NLS-1$

  /**
   * the same as
   */
  private static final char[] CHR_SAMEAS = (SAMEAS + TextUtils.LINE_SEPARATOR)
      .toCharArray();

  /**
   * The objective values stored in this individual.
   */
  private double[] m_objectiveValues;

  /**
   * The assigned fitness.
   */
  private double m_fitness;

  /**
   * The genotype stored in this individual record.
   */
  private G m_genotype;

  /**
   * The phenotype stored in this individual record.
   */
  private PP m_phenotype;

  /**
   * Create a new individual record for the specified count of objective
   * functions.
   * 
   * @param objectiveFunctionCount
   *          The count of objective functions this individual record is
   *          good for.
   * @throws IllegalArgumentException
   *           If <code>objectiveFunctionCount</code> is < 1.
   */
  public Individual(final int objectiveFunctionCount) {
    super();
    if (objectiveFunctionCount < 1)
      throw new IllegalArgumentException();
    this.m_objectiveValues = new double[objectiveFunctionCount];
    this.clear();
  }

  /**
   * Clear all the fitness and objective values which means setting them to
   * <code>Double.POSITIVE_INFINITY</code>.
   * 
   * @see #getFitness()
   * @see #getObjectiveValue(int)
   * @see #clear()
   */
  public void clearEvaluation() {
    int i;
    double[] d;

    this.clearFitness();
    d = this.m_objectiveValues;
    for (i = (d.length - 1); i >= 0; i--) {
      d[i] = Double.POSITIVE_INFINITY;
    }
  }

  /**
   * Clear this individual record. Calling this method resets all fields of
   * this individual record.
   * 
   * @see #clearEvaluation()
   */
  public void clear() {
    this.clearEvaluation();
    this.m_genotype = null;
    this.m_phenotype = null;
  }

  /**
   * Obtain the genotype stored in the individual record.
   * 
   * @return The genotype stored in the individual record or
   *         <code>null</code> if no genotype is currently stored.
   * @see #setGenotype(Serializable)
   */
  public G getGenotype() {
    return this.m_genotype;
  }

  /**
   * Store a genotype in this individual record.
   * 
   * @param genotype
   *          The genotype to be stored.
   * @see #getGenotype()
   */
  public void setGenotype(final G genotype) {
    this.m_genotype = genotype;
  }

  /**
   * Obtain the phenotype stored in the individual record.
   * 
   * @return The phenotype stored in the individual record or
   *         <code>null</code> if no phenotype has been stored or
   *         produced yet.
   * @see #setPhenotype(Serializable)
   */
  public PP getPhenotype() {
    return this.m_phenotype;
  }

  /**
   * Store a phenotype in this individual record.
   * 
   * @param phenotype
   *          The phenotype to be stored.
   * @see #getPhenotype()
   */
  public void setPhenotype(final PP phenotype) {
    this.m_phenotype = phenotype;
  }

  /**
   * Obtain the count of the objective values rating this individual which
   * is also the count of objective functions applied.
   * 
   * @return The count of objective functions applied.
   * @see #getObjectiveValue(int)
   */
  public int getObjectiveValueCount() {
    return this.m_objectiveValues.length;
  }

  /**
   * Obtain the objective value produced by the objective function at the
   * specified index.
   * 
   * @param index
   *          The index of the objective function providing this objective
   *          value. This value must be >= 0 and <
   *          {@link #getObjectiveValueCount()}.
   * @return The objective value stored at the specified index, the smaller
   *         it is, the fitter is the individual according to the specified
   *         objective function.
   * @throws ArrayIndexOutOfBoundsException
   *           if <code>index</code> &lt; 0 or &gt;=
   *           <code>getObjectiveValueCount</code>.
   * @see #getObjectiveValueCount()
   * @see #setObjectiveValue(int, double)
   */
  public double getObjectiveValue(final int index) {
    return this.m_objectiveValues[index];
  }

  /**
   * Set the objective value produced by the objective function at the
   * specified index.
   * 
   * @param index
   *          The index of the objective function providing this objective
   *          value. This value must be >= 0 and <
   *          {@link #getObjectiveValueCount()}.
   * @param value
   *          The objective value stored at the specified index, the
   *          smaller it is, the fitter is the individual according to the
   *          specified objective function.
   * @throws ArrayIndexOutOfBoundsException
   *           if <code>index</code> &lt; 0 or &gt;=
   *           <code>getObjectiveValueCount</code>.
   * @throws IllegalArgumentException
   *           if <code>value</code> is NaN.
   * @see #getObjectiveValueCount()
   * @see #getObjectiveValue(int)
   */
  public void setObjectiveValue(final int index, final double value) {
    if (Double.isNaN(value))
      throw new IllegalArgumentException();
    this.m_objectiveValues[index] = value;
  }

  /**
   * Obtain the fitness of this individual. This value will be computed
   * from the objective values by a fitness assignment process.
   * 
   * @return The fitness of this individual, computed from the objective
   *         values by a fitness assignment process.
   * @see #setFitness(double)
   */
  public double getFitness() {
    return this.m_fitness;
  }

  /**
   * Set the fitness value of this individual as result of a computation
   * from the objective values by a fitness assignment process.
   * 
   * @param fitness
   *          The fitness value to be assigned to this individual.
   * @throws IllegalArgumentException
   *           if <code>fitness</code> is NaN or infinite.
   * @see #getFitness()
   */
  public void setFitness(final double fitness) {
    if (Double.isNaN(fitness) || Double.isInfinite(fitness))
      throw new IllegalArgumentException();
    this.m_fitness = fitness;
  }

  /**
   * Clear the fitness of this individual.
   */
  public void clearFitness() {
    this.m_fitness = OptimizationUtils.WORST;
  }

  /**
   * Check if this individual equals to an object. The fitness does not
   * play any role in this comparison.
   * 
   * @param o
   *          The object to compare with.
   * @return <code>true</code> if and only if <code>o</code> is also an
   *         <code>IIndividual</code>-record holding exactly the same
   *         information as this object.
   */
  @Override
  public boolean equals(final Object o) {
    IIndividual<?, ?> ind;
    int i;
    double[] d;

    if (o == this)
      return true;
    if (!(o instanceof IIndividual))
      return false;

    ind = ((IIndividual<?, ?>) o);

    // if (Double.compare(ind.getFitness(), this.m_fitness) != 0)
    // return false;

    d = this.m_objectiveValues;
    i = d.length;
    if (ind.getObjectiveValueCount() != i)
      return false;

    for (--i; i >= 0; i--) {
      if (Double.compare(ind.getObjectiveValue(i), d[i]) != 0)
        return false;
    }

    return (Utils.testEqualDeep(this.m_genotype, ind.getGenotype()) && //
    Utils.testEqualDeep(this.m_phenotype, ind.getPhenotype()));
  }

  /**
   * Store the human-readable representation of this individual into a
   * string builder.
   * 
   * @param sb
   *          the string builder
   */
  @Override
  public void toStringBuilder(final StringBuilder sb) {
    double[] d;
    int i;
    char ch;
    G g;
    PP p;

    sb.append(CHR_GENOTYPE);
    i = sb.length();
    g = this.m_genotype;
    TextUtils.append(g, sb);

    ch = sb.charAt(sb.length() - 1);
    if ((ch != '\r') && (ch != '\n')) {
      sb.append(TextUtils.LINE_SEPARATOR);
    }

    if (g instanceof IJavaTextable) {
      sb.append(CHR_PLACEHOLDER);
      i = sb.length();
      ((IJavaTextable) (g)).javaToStringBuilder(sb, 0);

      ch = sb.charAt(sb.length() - 1);
      if ((ch != '\r') && (ch != '\n')) {
        sb.append(TextUtils.LINE_SEPARATOR);
      }
    }

    sb.append(CHR_PHENOTYPE);

    p = this.m_phenotype;

    if (p != g) {
      i = sb.length();
      TextUtils.append(p, sb);

      ch = sb.charAt(sb.length() - 1);
      if ((ch != '\r') && (ch != '\n')) {
        sb.append(TextUtils.LINE_SEPARATOR);
      }

      if (p instanceof IJavaTextable) {
        sb.append(CHR_PLACEHOLDER);
        i = sb.length();
        ((IJavaTextable) (p)).javaToStringBuilder(sb, 0);

        ch = sb.charAt(sb.length() - 1);
        if ((ch != '\r') && (ch != '\n')) {
          sb.append(TextUtils.LINE_SEPARATOR);
        }
      }
    } else {
      sb.append(CHR_SAMEAS);
    }

    // if (sb.indexOf("\n",i) >= 0)sb.append(TextUtils.LINE_SEPARATOR);
    // //$NON-NLS-1$

    sb.append(CHR_OBJECTIVES);
    d = this.m_objectiveValues;
    i = 0;
    for (;;) {
      sb.append(d[i]);
      i++;
      if (i >= d.length)
        break;
      if (i < d.length) {
        sb.append(',');
        sb.append(' ');
      }
    }
    sb.append('}');
    sb.append(TextUtils.LINE_SEPARATOR);
    sb.append(CHR_FITNESS);
    sb.append(this.m_fitness);
  }

  /**
   * Create a copy of this individual record.
   * 
   * @return a copy of this individual record
   */
  @Override
  @SuppressWarnings("unchecked")
  public Object clone() {
    Individual<G, PP> c;

    try {
      c = (Individual<G, PP>) (super.clone());
      c.m_objectiveValues = c.m_objectiveValues.clone();
      return c;
    } catch (CloneNotSupportedException cc) {
      ErrorUtils.onError(cc);
      return null;// will not happen
    }
  }

  /**
   * Check whether the two individual records have equal objective values.
   * 
   * @param i1
   *          the first individual record
   * @param i2
   *          the second individual record
   * @return <code>true</code> if and only if the objective values are
   *         equal
   */
  public static final boolean testObjectivesEqual(
      final IIndividual<?, ?> i1, final IIndividual<?, ?> i2) {
    int i;

    if (i1 == i2)
      return true;
    if ((i1 == null) || (i2 == null))
      return false;
    if ((i1 instanceof Individual) && (i2 instanceof Individual)) {
      return Utils.testDoubleArrayEqual(
          ((Individual<?, ?>) i1).m_objectiveValues,
          ((Individual<?, ?>) i2).m_objectiveValues);
    }

    i = i1.getObjectiveValueCount();
    if (i != i2.getObjectiveValueCount())
      return false;

    for (--i; i >= 0; i--) {
      if (Double.compare(i1.getObjectiveValue(i), i2.getObjectiveValue(i)) != 0)
        return false;
    }
    return true;
  }

}
