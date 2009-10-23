/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-22 ${time}
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.jobsystem.IExecutionInfo.java
 * Last modification: 2006-11-22
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
 * This interface provides the execution layer with the properties of a
 * job.
 *
 * @author Thomas Weise
 * @version 1.0.0
 *
 */
public interface IExecutionInfo {
  /**
   * Obtain the maximum count of processors that may be assigned to this
   * job.
   *
   * @return The maximum count of processors that can be assigned to this
   *         job. Values <= 0 are invalid and may lead to exceptions being
   *         thrown elsewhere.
   *
   */
  public abstract int getMaxProcessorCount();

}
