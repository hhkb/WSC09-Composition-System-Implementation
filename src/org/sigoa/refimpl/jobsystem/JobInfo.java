/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-22
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.jobsystem.JobInfo.java
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

package org.sigoa.refimpl.jobsystem;

import java.io.Serializable;

import org.sigoa.refimpl.security.SecurityInfo;
import org.sigoa.spec.go.IOptimizationInfo;
import org.sigoa.spec.jobsystem.IExecutionInfo;
import org.sigoa.spec.jobsystem.IJobInfo;
import org.sigoa.spec.security.ISecurityInfo;

/**
 * The default implementation of the <code>IJobInfo</code>-interface.
 *
 * @param <G>
 *          the genotype
 * @param <PP>
 *          the phenotype
 * @author Thomas Weise
 */
public class JobInfo<G extends Serializable, PP extends Serializable>
    implements IJobInfo<G, PP> {

  /**
   * The execution info record.
   */
  private final IExecutionInfo m_execInfo;

  /**
   * The security info record.
   */
  private final ISecurityInfo m_securityInfo;

  /**
   * The optimization info record.
   */
  private final IOptimizationInfo<G, PP> m_optimizationInfo;

  /**
   * Create a new job info record.
   *
   * @param optimizationInfo
   *          The optimization information record.
   * @throws NullPointerException
   *           if <code>optimizationInfo==null</code>
   */
  public JobInfo(final IOptimizationInfo<G, PP> optimizationInfo) {
    this(null, null, optimizationInfo);
  }

  /**
   * Create a new job info record.
   *
   * @param execInfo
   *          The execution information record. This may be
   *          <code>null</code>, in which case the default
   *          implementation with infinite many possible processors is
   *          used.
   * @param securityInfo
   *          The security information record. This may be
   *          <code>null</code>, in which case the default security info
   *          implementation will be used.
   * @param optimizationInfo
   *          The optimization information record.
   * @throws NullPointerException
   *           if <code>optimizationInfo==null</code>
   */
  public JobInfo(final IExecutionInfo execInfo,
      final ISecurityInfo securityInfo,
      final IOptimizationInfo<G, PP> optimizationInfo) {
    super();

    if (optimizationInfo == null)
      throw new NullPointerException();

    this.m_execInfo = ((execInfo == null) ? new ExecutionInfo() : execInfo);

    this.m_securityInfo = ((securityInfo == null) ? new SecurityInfo()
        : securityInfo);

    this.m_optimizationInfo = optimizationInfo;
  }

  /**
   * Obtain the execution layer properties of this job.
   *
   * @return The execution layer properties of this job.
   */
  public IExecutionInfo getExecutionInfo() {
    return this.m_execInfo;
  }

  /**
   * Obtain the security info of this job.
   *
   * @return The security info interface.
   */
  public ISecurityInfo getSecurityInfo() {
    return this.m_securityInfo;
  }

  /**
   * Obtain the optimization information record.
   *
   * @return The optimization information record for this job.
   */
  public IOptimizationInfo<G, PP> getOptimizationInfo() {
    return this.m_optimizationInfo;
  }

}
