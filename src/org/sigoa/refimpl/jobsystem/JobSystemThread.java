/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-05-12
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.jobsystem.JobSystemThread.java
 * Last modification: 2008-05-12
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

import org.sigoa.refimpl.utils.DefaultThread;
import org.sigoa.spec.events.IEvent;
import org.sigoa.spec.jobsystem.IHost;
import org.sigoa.spec.security.ISecurityInfo;
import org.sigoa.spec.security.SecurityUtils;
import org.sigoa.spec.simulation.ISimulationManager;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * The default job system thread class
 * 
 * @param <JST>
 *          the job system type
 * @author Thomas Weise
 */
public abstract class JobSystemThread<JST extends JobSystem> extends
    DefaultThread implements IHost {

  /**
   * the job system
   */
  private final JST m_js;

  /**
   * The internal randomizer.
   */
  private final IRandomizer m_randomizer;

  /**
   * Create a new job system thread.
   * 
   * @param name
   *          The name of the new job system thread.
   * @param jobSystem
   *          The job system instance using this thread.
   * @param tg
   *          the threadgroup to use
   */
  public JobSystemThread(final String name, final JST jobSystem,
      final ThreadGroup tg) {
    super(tg, name);

    if (name == null)
      throw new NullPointerException();

    this.m_js = jobSystem;
    this.m_randomizer = jobSystem.createRandomizer();
  }

  /**
   * Obtain access to the job system
   * 
   * @return the job system
   */
  protected final JST getJobSystem() {
    return this.m_js;
  }

  /**
   * Obtain the simulation manager of this executor.
   * 
   * @return The simulation manager of the executor running this job.
   */
  public ISimulationManager getSimulationManager() {
    return this.m_js.getSimulationManager();
  }

  /**
   * Forward an event to the executor owning this host.
   * 
   * @param event
   *          The event to be processed.
   */
  public void receiveEvent(final IEvent event) {
    ISecurityInfo si;
    si = SecurityUtils.getSecurityInfoManager();
    if (si == null)
      throw new SecurityException();
    si.checkEvent(event);
    this.doReceiveEvent(event);
  }

  /**
   * Forward an event to the executor owning this host.
   * 
   * @param event
   *          The event to be processed.
   */
  protected abstract void doReceiveEvent(final IEvent event);

  /**
   * Call this method if the current thread should do something else, like
   * working on another job, because the current job could only perform a
   * wait operation. It is not specified for how long control will be
   * transfered to another job.
   */
  public void defer() {
    Thread.yield();
  }

  /**
   * Returns the randomizer for the jobs running in this host.
   * 
   * @return the randomizer for the jobs running in this host
   */
  public IRandomizer getRandomizer() {
    return this.m_randomizer;
  }
}
