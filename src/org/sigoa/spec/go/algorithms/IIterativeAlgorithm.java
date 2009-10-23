/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-08
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.go.algorithms.IIterativeAlgorithm.java
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

package org.sigoa.spec.go.algorithms;

/**
 * This interface is common to all iterative algorithms.
 *
 * @author Thomas Weise
 */
public interface IIterativeAlgorithm {
/**
 * Returns the number of current iteration. The first iteration will have
 * the number 0.
 * @return the current iteration
 */
  public  abstract  long  getIteration();

/**
 * Add the specified iteration hook.
 * @param hook the iteration hook to be added
 * @throws IllegalArgumentException if <code>hook==null</code>
 */
  public  abstract  void  addIterationHook(final IIterationHook hook);

/**
 * Remove the specified iteration hook.
 * @param hook the iteration hook to be removed
 * @throws IllegalArgumentException if <code>hook==null</code>
 */
  public  abstract  void  removeIterationHook(final IIterationHook hook);
}
