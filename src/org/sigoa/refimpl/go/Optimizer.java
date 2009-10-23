/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-03
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.go.Optimizer.java
 * Last modification: 2007-02-23
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
import java.util.List;

import org.sfc.collections.CollectionUtils;
import org.sigoa.refimpl.jobsystem.Activity;
import org.sigoa.refimpl.pipe.Pipe;
import org.sigoa.spec.adaptation.IAdaptable;
import org.sigoa.spec.adaptation.IRule;
import org.sigoa.spec.go.IOptimizer;
import org.sigoa.spec.jobsystem.EActivityState;
import org.sigoa.spec.jobsystem.IActivity;

/**
 * The base class for implementors of the <code>IOptimizer</code>-interface.
 *
 * @param <G>
 *          the genotype
 * @param <PP>
 *          the phenotype
 * @author Thomas Weise
 */
public class Optimizer<G extends Serializable, PP extends Serializable>
    extends Pipe<G, PP> implements IOptimizer<G, PP>, IActivity,
    IAdaptable<IOptimizer<G, PP>> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the inner activity
   */
  private InnerActivity m_innerActivity;

  /**
   * the internal rule set
   */
  private final List<IRule<IOptimizer<G, PP>>> m_rules;

  /**
   * Create a new optimizer.
   */
  protected Optimizer() {
    super();
    this.m_innerActivity = new InnerActivity();
    this.m_rules = CollectionUtils.createList();
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
  public void reuse() {
    Activity x;
    boolean f, t, r;

    x = this.m_innerActivity;
    if (x != null) {
      f = x.isFinal();
      r = x.isRunning();
      t = x.isTerminated();

      if ((f && (!t)) || r)
        throw new IllegalStateException();
      if (!(f || r))
        return;
    }

    this.m_innerActivity = new InnerActivity();
    this.reset();
  }

  /**
   * An activity becomes final if it is either TERMINATING or TERMINATED.
   *
   * @return <code>true</code> if and only if this activity is final,
   *         <code>false</code> otherwise.
   */
  public boolean isFinal() {
    return this.m_innerActivity.isFinal();
  }

  /**
   * Reset this optimization algorithm. This method should be overriden and
   * clear the populations and such and such of the algorithm so it is
   * virgin again.
   */
  public void reset() {
    //
  }

  /**
   * This method does the work of the optimizer.
   */
  protected void doRun() {
    //
  }

  /**
   * Run the optimizer.
   *
   * @see #doRun()
   */
  public void run() {
    try {
      this.m_innerActivity.start();
      this.doRun();
    } finally {
      this.finished();
    }
  }

  /**
   * Returns <code>true</code> if and only if the activity is in the
   * state RUNNING.
   *
   * @return <code>true</code> if the activity is running,
   *         <code>false</code> otherwise
   */
  public boolean isRunning() {
    return this.m_innerActivity.isRunning();
  }

  /**
   * Returns <code>true</code> if and only if the activity is in the
   * state TERMINATED.
   *
   * @return <code>true</code> if the activity is terminated,
   *         <code>false</code> otherwise
   */
  public boolean isTerminated() {
    return this.m_innerActivity.isTerminated();
  }

  /**
   * Abort an activity. Aborting an activity means telling it to gently
   * stop all its doing. A call to this method must not block and just
   * trigger asynchronously some action leading to the activity's shutdow.
   * If the activity is not yet in the states <code>TERMINATING</code> or
   * <code>TERMINATED</code>, calling this method leads to an immediate
   * transition to <code>TERMINATING</code>.
   *
   * @see #doAbort()
   */
  public void abort() {
    this.m_innerActivity.abort();
  }

  /**
   * This method performs the shutdown. It is called by
   * <code>abort()</code> at most once.
   *
   * @see #abort()
   */
  protected void doAbort() {
    //
  }

  /**
   * Set that this activity is finished. This leads to a transition into
   * the state <code>TERMINATED</code>.
   */
  protected void finished() {
    this.m_innerActivity.finished();
  }

  /**
   * Perform the work of the startup. This method is called by
   * <code>start()</code> at most once.
   */
  protected void doStart() {
    //
  }

  /**
   * Wait until the activity has finished its doing.
   *
   * @param interruptible
   *          <code>true</code> if the waiting should be aborted if the
   *          current thread is interrupted - <code>false</code> if
   *          interrupting should be ignored (in this case this method may
   *          return prematurely).
   * @return <code>true</code> if the wait operation was interrupted,
   *         <code>false</code> otherwise
   */
  public boolean waitFor(final boolean interruptible) {
    return this.m_innerActivity.waitFor(interruptible);
  }

  /**
   * Get a copy of the internal rule list.
   *
   * @return a list with the internal rules
   */
  @SuppressWarnings("unchecked")
  public List<IRule<IOptimizer<G, PP>>> getRules() {
    List<IRule<IOptimizer<G, PP>>> l, m;
    int i;

    l = this.m_rules;
    synchronized (l) {
      i = l.size();
      if (i <= 0)
        return (List<IRule<IOptimizer<G, PP>>>) (CollectionUtils.EMPTY_LIST);
      m = CollectionUtils.createList();
      m.addAll(l);
    }
    return m;
  }

  /**
   * Add a rule to this adaptable object.
   *
   * @param rule
   *          the rule to be added
   * @throws IllegalArgumentException
   *           if <code>rule==null</code>
   */
  public void addRule(final IRule<IOptimizer<G, PP>> rule) {
    if (rule == null)
      throw new IllegalArgumentException();
    synchronized (this.m_rules) {
      this.m_rules.add(rule);
    }
  }

  /**
   * Remove a rule to this adaptable object.
   *
   * @param rule
   *          the rule to be removed
   * @throws IllegalArgumentException
   *           if <code>rule==null</code>
   */
  public void removeRule(final IRule<IOptimizer<G, PP>> rule) {
    if (rule == null)
      throw new IllegalArgumentException();
    synchronized (this.m_rules) {
      this.m_rules.add(rule);
    }
  }

  /**
   * apply the adaptation rules this method should be called regularily
   */
  protected void applyRules() {
    List<IRule<IOptimizer<G, PP>>> l;
    int i;
    IRule<IOptimizer<G, PP>> x;

    l = this.m_rules;
    synchronized (l) {
      for (i = (l.size() - 1); i >= 0; i--) {
        x = l.get(i);
        if (x.getCondition().evaluate(this))
          x.getAction().perform(this);
      }
    }
  }

  /**
   * the inner activity
   *
   * @author Thomas Weise
   */
  private final class InnerActivity extends Activity {

    /**
     * create a new inner activity
     */
    InnerActivity() {
      super();
    }

    /**
     * This method performs the shutdown. It is called by
     * <code>abort()</code> at most once.
     *
     * @see #abort()
     */
    @Override
    protected final void doAbort() {
      Optimizer.this.doAbort();
      super.doAbort();
    }

    /**
     * Perform the work of the startup. This method is called by
     * <code>start()</code> at most once.
     *
     * @see #start()
     */
    @Override
    protected final void doStart() {
      Optimizer.this.doStart();
      super.doStart();
    }

    /**
     * Set that this activity is finished. This leads to a transition into
     * the state <code>TERMINATED</code>.
     */
    @Override
    protected final void finished() {
      super.finished();
    }
  }
}
