/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-06
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.jobsystem.IActivity2.java
 * Last modification: 2006-12-06
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

package org.sigoa.spec.jobsystem;

/**
 * This interface is common to all activities. An activity can be started.
 * Activites most often own threads that begin to carry out work after the
 * activity has been started.
 *
 * @author Thomas Weise
 */
public interface IActivity2 extends IActivity {

  /**
   * <p>
   * Start the activity. Calling this method twice or on an activity that
   * has already finished its work will result in an
   * <code>IllegalStateException</code>. Therefore, this method must
   * only be called as long as the activity is in the state
   * <code>INITIALIZED</code>.
   * </p>
   * <p>
   * This method is optional, at least in the context of the
   * <code>IRunning</code>-interface.
   * </p>
   *
   * @throws IllegalStateException
   *           if the activity has already finished or was already started.
   */
  public abstract void start();
}
