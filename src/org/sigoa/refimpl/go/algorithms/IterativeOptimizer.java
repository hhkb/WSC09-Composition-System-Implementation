/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-08
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.algorithms.IterativeOptimizer.java
 * Last modification: 2006-12-08
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

package org.sigoa.refimpl.go.algorithms;

import java.io.Serializable;
import java.util.List;

import org.sfc.collections.CollectionUtils;
import org.sfc.utils.Utils;
import org.sigoa.refimpl.go.Optimizer;
import org.sigoa.spec.go.algorithms.IIterationHook;
import org.sigoa.spec.go.algorithms.IIterativeAlgorithm;
import org.sigoa.spec.jobsystem.EActivityState;

/**
 * The base class for iterative algorithms.
 * 
 * @param <G>
 *          the genotype
 * @param <PP>
 *          the phenotype
 * @author Thomas Weise
 */
public class IterativeOptimizer<G extends Serializable, PP extends Serializable>
    extends Optimizer<G, PP> implements IIterativeAlgorithm {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the current iteration
   */
  private volatile long m_iteration;

  /**
   * the total iterations
   */
  private volatile long m_totalIteration;

  /**
   * the iteration synchronizer
   */
  private final Object m_iterSync;

  /**
   * the iteration hooks.
   */
  private final List<IIterationHook> m_hooks;

  /**
   * invoke the garbage collector after each {@link #m_gcIntervall}
   * iterations
   */
  private volatile int m_gcIntervall;

  /**
   * Create a new iterative optimizer
   */
  public IterativeOptimizer() {
    super();
    this.m_iterSync = new Object();
    this.m_hooks = CollectionUtils.createList();
  }

  /**
   * Returns the number of current iteration. The first iteration will have
   * the number 0.
   * 
   * @return the current iteration
   */
  public long getIteration() {
    synchronized (this.m_iterSync) {
      return this.m_iteration;
    }
  }

  /**
   * Returns the total number of iterations performed.
   * 
   * @return the total number of iterations performed
   */
  public long getTotalIterations() {
    synchronized (this.m_iterSync) {
      return this.m_totalIteration;
    }
  }

  /**
   * Reset this optimization algorithm. This does also reset the iteration
   * counter but doesn't affect the total iteration counter.
   */
  @Override
  public void reset() {
    synchronized (this.m_iterSync) {
      this.m_iteration = 0;
    }
    super.reset();
  }

  /**
   * Normally, an optimizer can be used only once. After it has been run
   * and finished its work, it cannot be run again as defined by the
   * <code>EActivityState</code> semantics. This method allows you to
   * reset the activity state to <code>INITIALIZED</code>. Therefore,
   * you can use the optimizer again. This method also calls
   * <code>reset</code> in order to make the optimizer virgin again.
   * 
   * @throws IllegalStateException
   *           if the algorithm is currently running or being initialized.
   * @see EActivityState
   * @see #reset()
   */
  @Override
  public void reuse() {
    super.reuse();
    synchronized (this.m_iterSync) {
      this.m_totalIteration = 0;
    }
  }

  /**
   * this method is invoked for each iteration performed
   * 
   * @param index
   *          the index of the current itertion
   */
  protected void iteration(final long index) {//
  }

  /**
   * this method is invoked before each iteration is performed
   * 
   * @param index
   *          the index of the current itertion
   */
  protected void beforeIteration(final long index) {
    int i;
    List<IIterationHook> l;

    l = this.m_hooks;
    synchronized (l) {
      for (i = (l.size() - 1); i >= 0; i--)
        l.get(i).beforeIteration(index);
    }
  }

  /**
   * this method is invoked after each iteration is performed
   * 
   * @param index
   *          the index of the current itertion
   */
  protected void afterIteration(final long index) {
    int i;
    List<IIterationHook> l;

    l = this.m_hooks;
    synchronized (l) {
      for (i = (l.size() - 1); i >= 0; i--)
        l.get(i).afterIteration(index);
    }
  }

  /**
   * Add the specified iteration hook.
   * 
   * @param hook
   *          the iteration hook to be added
   * @throws IllegalArgumentException
   *           if <code>hook==null</code>
   */
  public void addIterationHook(final IIterationHook hook) {
    if (hook == null)
      throw new IllegalArgumentException();
    synchronized (this.m_hooks) {
      this.m_hooks.add(hook);
    }
  }

  /**
   * Remove the specified iteration hook.
   * 
   * @param hook
   *          the iteration hook to be removed
   * @throws IllegalArgumentException
   *           if <code>hook==null</code>
   */
  public void removeIterationHook(final IIterationHook hook) {
    if (hook == null)
      throw new IllegalArgumentException();
    synchronized (this.m_hooks) {
      this.m_hooks.remove(hook);
    }
  }

  /**
   * This method does the work of the optimizer.
   */
  @Override
  protected void doRun() {
    long l;
    int ivg;

    synchronized (this.m_iterSync) {
      l = this.m_iteration;
    }

    while (this.isRunning()) {
      this.applyRules();
      this.beforeIteration(l);
      this.iteration(l);
      this.afterIteration(l);

      synchronized (this.m_iterSync) {
        l = (++this.m_iteration);
        this.m_totalIteration++;
      }

      ivg = this.m_gcIntervall;
      if ((ivg > 0) && ((l % ivg) == 0))
        this.invokeGC();
    }
  }

  /**
   * The garbage collector will be invoked every {@link #getGCIntervall()}
   * iterations, if <code>{@link #getGCIntervall()}&gt;0</code>, never
   * otherwise
   * 
   * @return the gc intervall
   */
  public int getGCIntervall() {
    return this.m_gcIntervall;
  }

  /**
   * The garbage collector will be invoked every <code>gcIntervall</code>
   * iterations, if <code>gcIntervall}&gt;0</code>, never otherwise
   * 
   * @param gcIntervall
   *          the new gc intervall
   */
  public void setGCIntervall(final int gcIntervall) {
    this.m_gcIntervall = gcIntervall;
  }

  /**
   * The internal method used to invoke the garbage collector.
   * 
   * @see #setGCIntervall(int)
   * @see #getGCIntervall()
   */
  protected void invokeGC() {
    Utils.invokeGC();
  }
}
