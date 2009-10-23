/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-02-23
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.jobsystem.ThreadActivity.java
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

package org.sigoa.refimpl.jobsystem;

import org.sigoa.spec.jobsystem.IActivity2;

/**
 * This activity provides also thread capabilities
 *
 * @author Thomas Weise
 */
public class ThreadActivity extends org.sfc.parallel.ThreadActivity
    implements IActivity2 {
  /**
   * Create a new activity the uses own threads.
   *
   * @param threadGroup
   *          the thread group to be used or <code>null</code> if none is
   *          required
   */
  protected ThreadActivity(final ThreadGroup threadGroup) {
    super(threadGroup);
  }
}
