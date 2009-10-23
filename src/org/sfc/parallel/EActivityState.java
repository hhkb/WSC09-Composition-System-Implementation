/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2007-02-23
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.parallel.EActivityState.java
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

package org.sfc.parallel;

/**
 * This internal enumeration encapsulates possible states a parallel
 * activity can have.
 * 
 * @author Thomas Weise
 */
enum EActivityState {
  /**
   * This state is common to all activities that are created and are ready
   * to run. From this state, transitions are only possible to
   * <code>RUNNING</code> or <code>TERMINATING</code>.
   */
  INITIALIZED,
  /**
   * Acitivies in this state are currently running. From this state, the
   * only possible transition is to <code>TERMINATING</code>.
   */
  RUNNING,
  /**
   * This state indicates that an activity is set to terminate, but some
   * internal action is still being performed while shuting down. It is a
   * transitional state from <code>RUNNING</code>, or
   * <code>INITIALIZED</code> to <code>TERMINATED</code>. An activity
   * in the state <code>TERMINATING</code> can/must only transcend to
   * <code>TERMINATED</code>.
   */
  TERMINATING,
  /**
   * The last state of an activity. An activity in this state has either
   * completely finished or was aborted. This is a final, terminal state -
   * no transitions are possible from here to any other state.
   */
  TERMINATED;

}
