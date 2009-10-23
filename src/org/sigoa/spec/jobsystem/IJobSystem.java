/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-22 ${time}
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.jobsystem.IJobSystem.java
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

import org.sigoa.spec.events.IEventSource;
import org.sigoa.spec.go.IOptimizer;
import org.sigoa.spec.simulation.ISimulationManager;

/**
 * Job systems provide the processing power to optimization algorithms and
 * simulators. They host the threads that do all the internal work.
 *
 * @author Thomas Weise
 * @version 1.0.0
 */
public interface IJobSystem extends IActivity2, IEventSource {
  /**
   * Executes an optimization job using the information of a given job info
   * record.
   *
   * @param job
   *          the job to execute
   * @param info
   *          the job information record
   * @param <G>
   *          the genotype of the optimizer job
   * @param <PP>
   *          the phenotype of the optimizer job
   * @return an instance of <code>IWaitable</code> allowing you to wait
   *         for the job to complete
   * @throws IllegalStateException
   *           If the executor is neither in the state
   *           <code>INITIALIZED</code> nor <code>RUNNING</code> or if
   *           the job has a processor count of <= 0 assigned.
   * @throws NullPointerException
   *           if the job or any required field of the info record is
   *           <code>null</code>.
   */
  public abstract <G extends Serializable, PP extends Serializable> IOptimizationHandle executeOptimization(
      final IOptimizer<G, PP> job, final IJobInfo<G, PP> info);

  /**
   * Obtain the simulation manager of this job system.
   *
   * @return the simulation manager of this job system
   */
  public abstract ISimulationManager getSimulationManager();
}
