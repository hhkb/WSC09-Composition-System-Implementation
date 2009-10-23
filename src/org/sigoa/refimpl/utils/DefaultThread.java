/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-22
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.utils.DefaultThread.java
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

package org.sigoa.refimpl.utils;

import org.sfc.parallel.SfcThread;
import org.sigoa.spec.jobsystem.IActivity2;

/**
 * The base class for all threads that are used by sigoa reference
 * implementations.
 *
 * @author Thomas Weise
 */
public abstract class DefaultThread extends SfcThread implements IActivity2 {


  /**
   * Allocates a new <code>SfcThread</code> object.
   */
  protected DefaultThread() {
    this((String) null);
  }

  /**
   * Allocates a new <code>Thread</code> object.
   *
   * @param name
   *          the name of the new thread.
   */
  protected DefaultThread(final String name) {
    this((ThreadGroup) null, name);
  }

  /**
   * Allocates a new <code>Thread</code> object.
   *
   * @param group
   *          the thread group.
   * @param name
   *          the name of the new thread.
   * @exception SecurityException
   *              if the current thread cannot create a thread in the
   *              specified thread group.
   */
  protected DefaultThread(final ThreadGroup group, final String name) {
    super(group, name);
  }

  //
}
