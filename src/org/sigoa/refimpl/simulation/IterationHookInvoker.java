/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-08-04
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.simulation.IterationHookInvoker.java
 * Last modification: 2007-08-04
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

package org.sigoa.refimpl.simulation;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.sigoa.refimpl.go.ImplementationBase;
import org.sigoa.spec.go.algorithms.IIterationHook;
import org.sigoa.spec.simulation.ISimulationManager;

/**
 * The iteration hook invoker.
 * 
 * @author Thomas Weise
 */
public class IterationHookInvoker extends
    ImplementationBase<Serializable, Serializable> implements
    IIterationHook {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The default, shared instance.
   */
  public static final IIterationHook ITERATION_HOOK_INVOKER = new IterationHookInvoker();

  /**
   * the internal before iteration hook constant
   */
  private static final Method HOOK_BI;

  /**
   * the internal after iteration hook constant
   */
  private static final Method HOOK_AI;

  static {
    Method m;
    try {
      m = IIterationHook.class.getDeclaredMethod(
          "beforeIteration", new Class[] { long.class }); //$NON-NLS-1$
    } catch (Throwable t) {
      m = null;
    }

    HOOK_BI = m;

    try {
      m = IIterationHook.class.getDeclaredMethod(
          "afterIteration", new Class[] { long.class }); //$NON-NLS-1$
    } catch (Throwable t) {
      m = null;
    }

    HOOK_AI = m;
  }

  /**
   * This method is invoked before the iteration with index
   * <code>iteration</code>.
   * 
   * @param iteration
   *          the index of the iteration
   */
  public void beforeIteration(final long iteration) {
    invokeBeforeIterationHook(this.getSimulationManager(), iteration);
  }

  /**
   * This method is invoked after the iteration with index
   * <code>iteration</code>.
   * 
   * @param iteration
   *          the index of the iteration
   */
  public void afterIteration(final long iteration) {
    invokeAfterIterationHook(this.getSimulationManager(), iteration);
  }

  /**
   * Resolve this comparator at deserialization.
   * 
   * @return The right comparator instance.
   */
  private final Object readResolve() {
    if (this.getClass() == IterationHookInvoker.class)
      return IterationHookInvoker.ITERATION_HOOK_INVOKER;
    return this;
  }

  /**
   * Invoke the before-iteration hook method of the given simulation
   * manager.
   * 
   * @param manager
   *          the given simulation manager
   * @param iteration
   *          the iteration
   */
  public static final void invokeBeforeIterationHook(
      final ISimulationManager manager, final long iteration) {
    if ((HOOK_BI != null) && (manager != null)) {

      manager.invokeHook(HOOK_BI, new Serializable[] { Long
          .valueOf(iteration) });
    }
  }

  /**
   * Invoke the after-iteration hook method of the given simulation
   * manager.
   * 
   * @param manager
   *          the given simulation manager
   * @param iteration
   *          the iteration
   */
  public static final void invokeAfterIterationHook(
      final ISimulationManager manager, final long iteration) {
    if ((HOOK_BI != null) && (manager != null)) {

      manager.invokeHook(HOOK_AI, new Serializable[] { Long
          .valueOf(iteration) });
    }
  }
}
