/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-22 ${time}
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.jobsystem.IHost.java
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

import org.sigoa.spec.events.IEvent;
import org.sigoa.spec.events.IEventListener;
import org.sigoa.spec.simulation.ISimulationManager;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * The <code>IHost</code>-interface is exposed by
 * <code>IJobSystems</code> to the jobs that are run by them. It allows
 * the jobs to perform additional operations. The <code>IHost</code>-
 * interface must be implemented by all threads of an job system.
 * Therefore, a job will always run inside a thread which exposes the
 * <code>IHost</code>- interface. You can obtain the current host by
 * calling <code>JobSystemUtils.getCurrentHost()</code>.
 *
 * @see JobSystemUtils
 * @author Thomas Weise
 * @version 1.0.0
 */
public interface IHost extends IEventListener {
  /**
   * Execute a job. This method can only be called from a job that is
   * already running within an <code>IJobSystem</code>-instance. With
   * it, the job may enqueue additional tasks. If the job added is neither
   * in the state <code>INITIALIZED</code> or <code>RUNNING</code> it
   * is discarted.
   *
   * @param job
   *          the job to execute
   * @return an instance of <code>IRunning</code> allowing you to wait
   *         for the job to complete
   * @throws NullPointerException
   *           if <code>job==null</code>.
   */
  public abstract IWaitable executeJob(final Runnable job);

  /**
   * Wait until all sub-jobs of the current job are finished.
   */
  public abstract void flushJobs();

  /**
   * Obtain the job information interface provided at job creation.
   *
   * @param <G>
   *          the genotype of the optimizer job
   * @param <PP>
   *          the phenotype of the optimizer job
   * @return The job information interface.
   */
  public abstract <G extends Serializable, PP extends Serializable> IJobInfo<G, PP> getJobInfo();

  /**
   * Obtain the simulation manager of this executor.
   *
   * @return The simulation manager of the executor running this job.
   */
  public abstract ISimulationManager getSimulationManager();

  /**
   * Forward an event to the executor owning this host.
   *
   * @param event
   *          The event to be processed.
   */
  public abstract void receiveEvent(final IEvent event);

  /**
   * Call this method if the current thread should do something else, like
   * working on another job, because the current job could only perform a
   * wait operation. It is not specified for how long control will be
   * transfered to another job.
   */
  public abstract void defer();

  /**
   * Obtain the unique identifier of the optimization process this task is
   * part of.
   *
   * @return the unique identifier of the optimization process this task is
   *         part of
   */
  public abstract Serializable getOptimizationId();

/**
 * Returns the randomizer for the jobs running in this host.
 * @return the randomizer for the jobs running in this host
 */
  public abstract IRandomizer getRandomizer();
}
