/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2007-03-26
 * Creator          : Thomas Weise
 * Original Filename: sfc.simulation.SimulationThread.java
 * Last modification: 2007-03-26
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
package org.sfc.parallel.simulation;

import org.sfc.parallel.SfcThread;

/**
 * This is a thread that is able to drive a simulation
 * 
 * @author Thomas Weise
 */
public class SimulationThread extends SfcThread {

  /**
   * the simulation to be driven
   */
  private final IStepable m_sim;

  /**
   * the current speed
   */
  private volatile int m_speed;

  /**
   * the synchronizer
   */
  private final Object m_sync;

  /**
   * create the simulation thread
   * 
   * @param stepable
   *          the stepable component
   */
  public SimulationThread(final IStepable stepable) {
    super();
    this.m_speed = 0;
    this.m_sync = new Object();
    this.m_sim = stepable;
  }

  /**
   * Obtain the stepable component driven by this thread
   * 
   * @return the stepable component driven by this thread
   */
  public IStepable getStepable() {
    return this.m_sim;
  }

  /**
   * Perform the work of this thread.
   */
  @Override
  protected void doRun() {
    long beginTime, nextTime, t;

    beginTime = System.currentTimeMillis();
    while (this.isRunning()) {

      while ((t = System.currentTimeMillis()) < (nextTime = getNextTime(
          this.m_speed, beginTime))) {
        try {
          synchronized (this.m_sync) {
            this.m_sync.wait(nextTime - beginTime);
          }
        } catch (InterruptedException ie) {
          return;
        }
      }
      beginTime = t;

      this.step();
    }
  }

  /**
   * Perform a simulation step
   */
  public void step() {
    this.m_sim.step();
  }

  /**
   * Set the speed.
   * 
   * @param newSpeed
   *          the new simulation speed
   */
  public void setSpeed(final int newSpeed) {
    this.m_speed = newSpeed;
    synchronized (this.m_sync) {
      this.m_sync.notifyAll();
    }
  }

  /**
   * Abort this thread.
   */
  @Override
  protected void doAbort() {
    this.m_speed = 1000;
    synchronized (this.m_sync) {
      this.m_sync.notifyAll();
    }
    super.doAbort();
  }

  /**
   * This method is used to compute a suitable delay from a factor.
   * 
   * @param factor
   *          the higher this value (0&lt;<code>factor</code>&lt;100),
   *          the higher should the delay be
   * @return the delay.
   */
  protected long computeTimeDelay(final int factor) {
    return (3 * factor);
  }

  /**
   * compute the next time
   * 
   * @param speed
   *          the current speed
   * @param beginTime
   *          the begin time
   * @return the next time
   */
  private final long getNextTime(final int speed, final long beginTime) {
    if (speed <= 0)
      return Long.MAX_VALUE;
    if (speed >= 100)
      return (beginTime - 1);
    return (beginTime + computeTimeDelay((100 - speed) * 4));
  }

}
