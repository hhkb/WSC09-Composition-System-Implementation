/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-11
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.clustering.ClusteringAlgorithm.java
 * Last modification: 2006-12-11
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

package org.sigoa.refimpl.clustering;

import java.io.Serializable;

import org.sfc.utils.Utils;
import org.sigoa.refimpl.clustering.distanceMeasures.DistanceUtils;
import org.sigoa.refimpl.go.BufferedPassThroughPipe;
import org.sigoa.spec.clustering.IClusteringAlgorithm;
import org.sigoa.spec.clustering.IDistanceMeasure;
import org.sigoa.spec.clustering.IIndividualDistanceMeasure;
import org.sigoa.spec.go.IIndividual;

/**
 * The base class for <code>IClusteringAlgorithm</code>-implementations.
 *
 * @param <G>
 *          The genotype.
 * @param <PP>
 *          The phenotype.
 * @author Thomas Weise
 */
public abstract class ClusteringAlgorithm<G extends Serializable, PP extends Serializable>
    extends BufferedPassThroughPipe<G, PP> implements
    IClusteringAlgorithm<G, PP> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the distance measure
   */
  IIndividualDistanceMeasure<G, PP> m_indDist;

  /**
   * Create a new pass through algorithm instance.
   *
   * @param count
   *          The initial selection size.
   * @param distanceMeasure
   *          the distance measure to be used - if this is
   *          <code>null</code> a default distance measure will be
   *          applied
   * @throws IllegalArgumentException
   *           if <code>count&lt;=0</code>.
   */
  @SuppressWarnings("unchecked")
  protected ClusteringAlgorithm(final int count,
      final IIndividualDistanceMeasure<G, PP> distanceMeasure) {
    super(false, count);

    IDistanceMeasure<?> d;

    if (distanceMeasure != null) {
      this.m_indDist = distanceMeasure;
    } else {
      d = DistanceUtils.DEFAULT_INDIVIDUAL_DM;
      this.m_indDist = ((IIndividualDistanceMeasure<G, PP>) (d));
    }
  }

  /**
   * Set the distance measure function to be used to determine the distance
   * between two individuals.
   *
   * @param measure
   *          the distance measure function to be used
   * @throws NullPointerException
   *           if <code>measure==null</code>
   */
  public void setIndividualDistanceMeasure(
      final IIndividualDistanceMeasure<G, PP> measure) {
    if (measure == null)
      throw new NullPointerException();
    this.m_indDist = measure;
  }

  /**
   * Obtain the distance measure used for the internal computation of the
   * distance between two individuals.
   *
   * @return the distance measure used for the internal computations
   */
  public IIndividualDistanceMeasure<G, PP> getIndividualDistanceMeasure() {
    return this.m_indDist;
  }

  /**
   * Process the buffered population.
   *
   * @param source
   *          The buffered source population.
   * @param size
   *          The count of individuals in the population array.
   * @param pass
   *          the number of individuals allowed to pass
   */
  protected abstract void doProcess(final IIndividual<G, PP>[] source,
      final int size, final int pass);

  /**
   * Process the buffered population.
   *
   * @param source
   *          The buffered source population.
   * @param size
   *          The count of individuals in the population array.
   */
  @Override
  protected void process(final IIndividual<G, PP>[] source, final int size) {
    int pass, s, i, j;
    PP p1;

    pass = this.getPassThroughCount();
    if (pass < size) {
      main: {
        s = size;
        inner: for (i = (s - 2); i >= 0; i--) {
          p1 = source[i].getPhenotype();
          for (j = (s - 1); j > i; j--) {
            if (Utils.testEqualDeep(p1, source[j].getPhenotype())) {
              source[j] = source[--s];
              if (s <= pass)
                break main;
              continue inner;
            }
          }
        }

        this.doProcess(source, s, pass);
        return;
      }
    } else
      s = size;

    for (--s; s >= 0; s--) {
      this.output(source[s]);
    }
  }

  /**
   * Invoke the do-process method of the specified clustering algorithm.
   *
   * @param source
   *          The buffered source population.
   * @param size
   *          The count of individuals in the population array.
   * @param pass
   *          the number of individuals allowed to pass
   * @param algo
   *          the algorithm
   * @param <G>
   *          the genotpe
   * @param <PP>
   *          the phenotype
   */
  protected static final <G extends Serializable, PP extends Serializable> void invokeDoProcess(
      final IIndividual<G, PP>[] source, final int size, final int pass,
      final ClusteringAlgorithm<G, PP> algo) {
    algo.doProcess(source, size, pass);
  }
}
