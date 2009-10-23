/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-04-14
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.comparators.adaptive.DecisiveParetoComparator.java
 * Last modification: 2008-04-14
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

package org.sigoa.refimpl.go.comparators.adaptive;

import java.io.Serializable;
import java.util.Arrays;

import org.sfc.math.Mathematics;
import org.sigoa.refimpl.go.algorithms.ea.EA;
import org.sigoa.refimpl.go.comparators.ParetoComparator;
import org.sigoa.refimpl.pipe.Pipeline;
import org.sigoa.spec.go.IIndividual;
import org.sigoa.spec.go.evaluation.IEvaluatorPipe;
import org.sigoa.spec.pipe.IPipe;
import org.sigoa.spec.pipe.IPipeIn;

/**
 * This adaptive comparator is able to judge whether an objective is
 * decisive or whether it should not take part in the comparisons
 * 
 * @author Thomas Weise
 */
public class DecisiveParetoComparator extends ParetoComparator implements
    IPipe<Serializable, Serializable> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The output pipe.
   */
  private volatile IPipeIn<Serializable, Serializable> m_output;

  /**
   * the classes
   */
  final double[][] m_classes;

  /**
   * the class count
   */
  final int[] m_classCount;

  /**
   * the buffer
   */
  private final double[] m_buffer;

  /**
   * which objectives are decisive?
   */
  volatile boolean[] m_decisive;

  /**
   * which objectives are decisive 2?
   */
  private volatile boolean[] m_decisive2;

  // /**
  // * the pseudo random integer
  // */
  // private int m_pseudoRand;

  /**
   * the classes needed
   */
  private final int[] m_minClasses;

  /**
   * Create a new decisive pareto comparator.
   * 
   * @param minClasses
   *          the minimum required classes per objective to make it
   *          decisive
   */
  public DecisiveParetoComparator(final int[] minClasses) {
    this(minClasses, minClasses);
  }

  /**
   * Create a new decisive pareto comparator.
   * 
   * @param minClasses
   *          the minimum required classes per objective to make it
   *          decisive
   * @param trackClasses
   *          the number of classes to be tracked per objective
   */
  protected DecisiveParetoComparator(final int[] minClasses,
      final int[] trackClasses) {
    super();

    int l, j;
    double[][] d;

    l = minClasses.length;
    this.m_minClasses = minClasses.clone();

    this.m_buffer = new double[l];
    this.m_classCount = new int[l];
    this.m_classes = d = new double[l][];

    Arrays.fill(this.m_decisive = new boolean[l], true);

    this.m_decisive2 = new boolean[l];

    for (--l; l >= 0; l--) {
      j = Math.max(this.m_minClasses[l] = minClasses[l], trackClasses[l]);

      if (j > 1) {
        Arrays.fill(d[l] = new double[j], Double.POSITIVE_INFINITY);
      }
    }
  }

  /**
   * Insert the value v into the double array
   * 
   * @param v
   *          the value
   * @param lst
   *          the array
   * @param s
   *          the number of elements already stored
   * @return true if it was checked in
   */
  private static final boolean checkIns(final double v,
      final double[] lst, final int s) {
    int i;

    i = Arrays.binarySearch(lst, 0, s, v);
    if (i >= 0)
      return false;
    i = ((-i) - 1);
    System.arraycopy(lst, i, lst, i + 1, lst.length - i - 1);
    lst[i] = v;

    return true;
  }

  /**
   * Write a new individual into the pipe. This default implementation only
   * checks the possible errors and throws exceptions if needed, but in
   * principle does nothing.
   * 
   * @param individual
   *          The new individual to be written.
   * @throws NullPointerException
   *           if <code>individual</code> is <code>null</code>.
   */
  public void write(
      final IIndividual<Serializable, Serializable> individual) {
    int i, cs;
    final double[][] classes;
    final int[] cc;
    final double[] buffer;
    double[] s;
    IPipeIn<Serializable, Serializable> x;

    if (individual != null) {
      buffer = this.m_buffer;
      classes = this.m_classes;
      cc = this.m_classCount;
      i = (buffer.length - 1);

      check: {
        synchronized (buffer) {

          // buffer + check
          for (; i >= 0; i--) {
            if (!(Mathematics.isNumber(buffer[i] = individual
                .getObjectiveValue(i)))) {
              break check;
            }
          }

          // process
          for (i = (buffer.length - 1); i >= 0; i--) {

            // the classes
            s = classes[i];
            if (s != null) {
              cs = cc[i];
              if (cs < s.length) {
                if (checkIns(buffer[i], s, cs)) {
                  cc[i] = (cs + 1);
                }
              }
            }
          }
        }
      }
      x = this.m_output;
      if (x != null)
        x.write(individual);
    }
  }

  /**
   * process the data
   */
  void processData() {
    final boolean[] decisive2;
    final double[][] classes;
    final int[] sc, minClasses;
    int i;

    decisive2 = this.m_decisive2;

    sc = this.m_classCount;
    classes = this.m_classes;
    minClasses = this.m_minClasses;    
    for (i = (decisive2.length - 1); i >= 0; i--) {
      decisive2[i] = ((classes[i] == null) || (sc[i] >= minClasses[i]));
    }
    
    for (i = (decisive2.length - 1); i >= 0; i--) {
     if(!(decisive2[i])){
       System.out.println();
     }
    }

    this.m_decisive2 = this.m_decisive;
    this.m_decisive = decisive2;
  }

  /**
   * Propagate an <code>eof</code> to the next pipe stage if needed.
   */
  public void eof() {
    final double[][] d;
    int i;
    IPipeIn<Serializable, Serializable> p;

    synchronized (this.m_buffer) {
      this.processData();

      Arrays.fill(this.m_classCount, 0);
      d = this.m_classes;
      for (i = (d.length - 1); i >= 0; i--) {
        Arrays.fill(d[i], Double.POSITIVE_INFINITY);
      }
    }

    p = this.m_output;
    if (p != null)
      p.eof();
  }

  /**
   * Compare two individuals with each other. The comparison is performed
   * only on the base of <code>count</code> objective values starting
   * with the <code>from</code><sup>th</sup> one.
   * 
   * @param o1
   *          The first individual to be compared.
   * @param o2
   *          The second individual to be compared.
   * @param from
   *          the index of the first objective value to be taken into
   *          consideration
   * @param to
   *          the exclusive last index of the comparison
   * @return a negative value if o1 prevails o2, a positive value if o2
   *         prevails o1 and 0 if non of them prevails the other one.
   */
  @Override
  protected int compare(final IIndividual<?, ?> o1,
      final IIndividual<?, ?> o2, final int from, final int to) {
    int i, s, j;
    final boolean[] d;

    s = 0;
    d = this.m_decisive;
    for (i = from; i < to; i++) {
      if (d[i]) {
        j = Double.compare(o1.getObjectiveValue(i), o2
            .getObjectiveValue(i));
        if (j < 0) {
          if (s == 0)
            s = -1;
          else if (s > 0)
            return 0;
        } else if (j > 0) {
          if (s == 0)
            s = 1;
          else if (s < 0)
            return 0;
        }
      }
    }

    return s;
  }

  /**
   * Set the ouput pipe.
   * 
   * @param pipe
   *          The output pipe.
   */
  @SuppressWarnings("unchecked")
  public void setOutputPipe(
      final IPipeIn<? super Serializable, ? super Serializable> pipe) {
    this.m_output = ((IPipeIn<Serializable, Serializable>) pipe);
  }

  /**
   * Obtain the output pipe.
   * 
   * @return The output pipe, or <code>null</code> if none is set.
   */
  public IPipeIn<? super Serializable, ? super Serializable> getOutputPipe() {
    return this.m_output;
  }

  /**
   * Prepare an evolutionary algorithm with an auto objective
   * 
   * @param ea
   *          the ea
   * @param c
   *          the adaptive tired comparator to insert into the ea
   */
  @SuppressWarnings("unchecked")
  public static final void prepareEA(final EA<?, ?> ea,
      final DecisiveParetoComparator c) {
    Pipeline<?, ?> p;
    int i;

    p = ea.getPipeline();
    for (i = (p.size() - 1); i >= 0; i--) {
      if (p.get(i) instanceof IEvaluatorPipe) {
        p.add(i + 1, (IPipe) (c));
      }
    }
  }
}
