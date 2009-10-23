/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-07-03
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.clustering.algorithms.ClassClustering.java
 * Last modification: 2007-07-03
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

package org.sigoa.refimpl.clustering.algorithms;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.sfc.math.Mathematics;
import org.sigoa.refimpl.clustering.ClusteringAlgorithm;
import org.sigoa.refimpl.clustering.ClusteringAlgorithm2;
import org.sigoa.refimpl.go.Individual;
import org.sigoa.refimpl.pipe.NoEofPipe;
import org.sigoa.spec.clustering.IClusteringAlgorithm;
import org.sigoa.spec.clustering.IDistanceMeasure;
import org.sigoa.spec.clustering.IIndividualDistanceMeasure;
import org.sigoa.spec.go.IIndividual;
import org.sigoa.spec.pipe.IPipeIn;

/**
 * <p>
 * This clustering algorithm uses a prefix class selection before applying
 * a secondary algorithm, if needed.
 * </p>
 * <p>
 * Class clustering first preserves border solutions. It the checks whether
 * the remaining elements can be grouped into classes and sufficiently few
 * classes exist so that we can preserve at least one element per class.
 * Otherwise a secondary clustering algorithm is used.
 * </p>
 * <p>
 * Also if the classing is possible, the remaining elements are clustered
 * with a secondary algorithm.
 * </p>
 *
 * @param <G>
 *          the genotype
 * @param <PP>
 *          the phenotype
 * @author Thomas Weise
 */
public class ClassClustering<G extends Serializable, PP extends Serializable>
    extends ClusteringAlgorithm2<G, PP> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the secondary clustering algorithm
   */
  private IClusteringAlgorithm<G, PP> m_secondary;

  /**
   * the no-eof pipe to write to
   */
  private NoEofPipe<G, PP> m_write;

  /**
   * the class buffer.
   */
  private IIndividual<G, PP>[] m_classes;

  /**
   * the class index buffer.
   */
  private int[] m_idxs;

  /**
   * Create a new class clustering algorithm instance.
   *
   * @param count
   *          The initial selection size.
   * @param distanceMeasure
   *          the distance measure to be used for individual distance
   *          measuring - if this is <code>null</code> a default distance
   *          measure will be applied
   * @param clusterDistance
   *          the cluster distance distance measure for cluster distances
   * @param secondary
   *          the secondary clustering algorithm
   * @throws IllegalArgumentException
   *           if <code>count&lt;=0</code> .
   */
  @SuppressWarnings("unchecked")
  public ClassClustering(final int count,
      final IIndividualDistanceMeasure<G, PP> distanceMeasure,
      final IDistanceMeasure<List<IIndividual<G, PP>>> clusterDistance,
      final IClusteringAlgorithm<G, PP> secondary) {

    super(count, distanceMeasure, clusterDistance);

    if (secondary == null)
      this.m_secondary = new NNearestNeighborClustering<G, PP>(count,
          distanceMeasure, 5);
    else
      this.m_secondary = secondary;

    if (!(this.m_secondary instanceof ClusteringAlgorithm))
      this.m_write = new NoEofPipe<G, PP>();

    this.m_classes = new IIndividual[count << 1];
    this.m_idxs = new int[count << 1];
  }

  /**
   * Create a new class clustering algorithm instance.
   *
   * @param count
   *          The initial selection size.
   * @param distanceMeasure
   *          the distance measure to be used for individual distance
   *          measuring - if this is <code>null</code> a default distance
   *          measure will be applied
   * @param secondary
   *          the secondary clustering algorithm
   * @throws IllegalArgumentException
   *           if <code>count&lt;=0</code> .
   */
  public ClassClustering(final int count,
      final IIndividualDistanceMeasure<G, PP> distanceMeasure,
      final IClusteringAlgorithm<G, PP> secondary) {
    this(count, distanceMeasure, null, secondary);
  }

  /**
   * Create a new class clustering algorithm instance.
   *
   * @param count
   *          The initial selection size.
   * @param distanceMeasure
   *          the distance measure to be used for individual distance
   *          measuring - if this is <code>null</code> a default distance
   *          measure will be applied
   * @throws IllegalArgumentException
   *           if <code>count&lt;=0</code> .
   */
  public ClassClustering(final int count,
      final IIndividualDistanceMeasure<G, PP> distanceMeasure) {
    this(count, distanceMeasure, null, null);
  }

  /**
   * Return the secondary clustering algorithm
   *
   * @return the secondary clustering algorithm
   */
  public IClusteringAlgorithm<G, PP> getSecondaryAlgorithm() {
    return this.m_secondary;
  }

  /**
   * Set the secondary clustering algorithm.
   *
   * @param secondary
   *          the secondary clustering algorithm
   * @throws NullPointerException
   *           if <code>secondary==null</code>
   */
  public void setSecondaryAlgorithm(
      final IClusteringAlgorithm<G, PP> secondary) {

    if (secondary == null)
      throw new NullPointerException();

    this.m_secondary = secondary;

    if (secondary instanceof ClusteringAlgorithm) {
      this.m_write = null;
      secondary.setOutputPipe(this.getOutputPipe());
    } else {
      this.m_write = new NoEofPipe<G, PP>();
      secondary.setOutputPipe(this.m_write);
      this.m_write.setOutputPipe(this.getOutputPipe());
    }
  }

  /**
   * Set the distance measure function to be used in order to the distance
   * between two clusters of individuals.
   *
   * @param measure
   *          the distance measure function to be used
   * @throws NullPointerException
   *           if <code>measure==null</code>
   */
  @Override
  public void setClusterDistanceMeasure(
      final IDistanceMeasure<List<IIndividual<G, PP>>> measure) {
    super.setClusterDistanceMeasure(measure);
    if (this.m_secondary instanceof ClusteringAlgorithm2) {
      ((ClusteringAlgorithm2<G, PP>) (this.m_secondary))
          .setClusterDistanceMeasure(measure);
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
  @Override
  public void setIndividualDistanceMeasure(
      final IIndividualDistanceMeasure<G, PP> measure) {
    super.setIndividualDistanceMeasure(measure);
    if (this.m_secondary instanceof ClusteringAlgorithm) {
      ((ClusteringAlgorithm<G, PP>) (this.m_secondary))
          .setIndividualDistanceMeasure(measure);
    }
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
  @Override
  @SuppressWarnings("unchecked")
  protected void doProcess(final IIndividual<G, PP>[] source,
      final int size, final int pass) {
    int i, s, p, c, minI, maxI, j, d;
    final int ov;
    double min, max, v;
    IIndividual<G, PP> x;// , y;
    IClusteringAlgorithm<G, PP> ca;
    IIndividual<G, PP>[] classes;
    int[] idxs;

    ov = source[0].getObjectiveValueCount() - 1;

    p = pass;
    s = size;
    // save border solutions
    for (d = ov; d >= 0; d--) {
      minI = -1;
      maxI = -1;
      min = Double.POSITIVE_INFINITY;
      max = Double.NEGATIVE_INFINITY;

      for (i = (size - 1); i >= 0; i--) {
        v = source[i].getObjectiveValue(d);
        if (Mathematics.isNumber(v)) {
          if (v < min) {
            min = v;
            minI = i;
          }
          if (v > max) {
            max = v;
            maxI = i;
          }
        }
      }

      if ((minI != maxI) && (Double.compare(min, max) != 0)) {
        if ((minI < s) && (minI >= 0)) {
          x = source[minI];
          source[minI] = source[--s];
          source[s] = x;
          this.output(x);
          if ((--p) <= 0)
            return;
          if (maxI == minI)
            continue;
          if (maxI == s) {
            maxI = minI;
          }
        }

        if ((maxI < s) && (maxI >= 0)) {
          x = source[maxI];
          source[maxI] = source[--s];
          source[s] = x;
          this.output(x);
          if ((--p) <= 0)
            return;
        }
      }
    }
    // border solutions are preserved:
    // s = number of remaining solutions in source
    // p = remaining pass count

    // create classes
    classes = this.m_classes;
    if (classes.length < s) {
      this.m_classes = classes = new IIndividual[s << 1];
      this.m_idxs = idxs = new int[s << 1];
    } else
      idxs = this.m_idxs;

    c = 0;
    outer: for (i = (s - 1); i >= 0; i--) {
      x = source[i];

      /* inner: */for (j = (c - 1); j >= 0; j--) {

        // y = classes[j];
        // for (d = ov; d >= 0; d--) {
        // if (Double.compare(y.getObjectiveValue(d), x
        // .getObjectiveValue(d)) != 0)
        // continue inner;
        // }
        // continue outer;
        if (Individual.testObjectivesEqual(x, classes[j]))
          continue outer;
      }

      classes[c] = x;
      idxs[c] = i;
      c++;
    }

    // less classes than passes?
    if (c <= p) {
      // idxs are large for small values of i, simple deletion
      for (i = 0; i < c; i++) {
        this.output(classes[i]);
        source[idxs[i]] = source[--s];
      }

      p -= c;
      if (p <= 0) {
        Arrays.fill(classes, 0, c, null);
        return;
      }
    }
    Arrays.fill(classes, 0, c, null);

    // invoke secondary algorithm
    // s = number of remaining solutions in source
    // p = remaining pass count
    ca = this.m_secondary;
    if (ca instanceof ClusteringAlgorithm) {
      invokeDoProcess(source, s, p, ((ClusteringAlgorithm<G, PP>) ca));
    } else {
      ca.setPassThroughCount(p);
      for (--s; s >= 0; s--) {
        ca.write(source[s]);
      }
      ca.eof();
    }
  }

  /**
   * Set the ouput pipe. If <code>pipe==null</code>, no output will be
   * performed.
   *
   * @param pipe
   *          The output pipe.
   */
  @Override
  public void setOutputPipe(final IPipeIn<? super G, ? super PP> pipe) {
    super.setOutputPipe(pipe);
    if (this.m_write == null)
      this.m_secondary.setOutputPipe(pipe);
    else
      this.m_write.setOutputPipe(pipe);
  }
}
