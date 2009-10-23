/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-22
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.jobsystem.IJobInfo.java
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

import java.io.Serializable;

import org.sigoa.spec.go.IOptimizationInfo;
import org.sigoa.spec.security.ISecurityInfo;

/**
 * This interfaces identifies a job that should be performed by the SIGOA
 * system. A job is basically a configuration for search algorithms. This
 * job interface provides the system with information like how many
 * processors may be used at max and what permissions are granted to the
 * code.
 *
 * @param <G>
 *          The genotype.
 * @param <PP>
 *          The phenotype.
 * @author Thomas Weise
 * @version 1.0.0
 */
public interface IJobInfo<G extends Serializable, PP extends Serializable> {
  /**
   * Obtain the execution layer properties of this job.
   *
   * @return The execution layer properties of this job.
   */
  public abstract IExecutionInfo getExecutionInfo();

  /**
   * Obtain the security info of this job.
   *
   * @return The security info interface.
   */
  public abstract ISecurityInfo getSecurityInfo();

  /**
   * Obtain the optimization information record.
   *
   * @return The optimization information record for this job.
   */
  public abstract IOptimizationInfo<G, PP> getOptimizationInfo();

}
