/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-08-28
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.gui.simulation.SimulationFrame.java
 * Last modification: 2007-08-28
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

package org.sfc.gui.simulation;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;

import javax.swing.JButton;
import javax.swing.JComponent;

import org.sfc.gui.ComponentUtils;
import org.sfc.gui.SfcComponent;
import org.sfc.gui.windows.SfcFrame;
import org.sfc.parallel.simulation.IStepable;
import org.sfc.parallel.simulation.SimulationThread;

/**
 * A simulation window
 *
 * @author Thomas Weise
 */
public class SimulationFrame extends SfcFrame implements IStepable {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the internal simulation thread
   */
  private final SimulationThread m_thread;

  /**
   * the internal simulation control
   */
  private final SimulationControl m_simCtrl;

  /**
   * the simulation control
   */
  private volatile JComponent m_simVis;

  /**
   * the component to be updated after each step
   */
  private volatile JComponent m_update;

  /**
   * the simulation
   */
  private volatile IStepable m_sim;

  /**
   * Create a new simulation frame
   */
  public SimulationFrame() {
    super();
    SfcComponent s;

    this.m_thread = this.createSimulationThread(this);
    this.m_simCtrl = this.createSimulationControl(this.m_thread);

    this.setContentPane(s = new SfcComponent());
    s.setLayout(new GridBagLayout());

    ComponentUtils.putGrid(s, this.m_simCtrl, 0, 1, 1, 1, 1, 0,
        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);

    this.setSize(700, 500);
  }

  /**
   * Start the worker thread
   */
  public void start() {
    this.m_thread.start();
  }

  /**
   * Close this window
   */
  @Override
  public void close() {
    this.m_thread.abort();
    super.dispose();
  }

  /**
   * Set the visualization components
   *
   * @param vis
   *          the visualization component (for example a scrollpane)
   * @param upd
   *          the component to update in each step (can be the same as
   *          <code>vis</code>, or some component contained in
   *          <code>vis</code>)
   */
  public synchronized void setVisualization(final JComponent vis,
      final JComponent upd) {
    Container c;
    LayoutManager l;
    c = this.getContentPane();
    if (this.m_simVis != null) {
      l = c.getLayout();
      c.remove(this.m_simVis);
      l.removeLayoutComponent(this.m_simVis);
    }
    this.m_simVis = ((vis == null) ? upd : vis);
    this.m_update = ((upd == null) ? vis : upd);

    if (vis != null) {
      ComponentUtils.putGrid(c, vis, 0, 0, 1, 1, 1, 1,
          GridBagConstraints.CENTER, GridBagConstraints.BOTH);
    }

    this.repaint();
  }

  /**
   * Add a button to the internal simulation control
   *
   * @param button
   *          the button to be added
   */
  public void addControlButton(final JButton button) {
    this.m_simCtrl.addButton(button);
  }

  /**
   * Set the simulation to be used by this frame
   *
   * @param stepable
   *          the stepable simulation interface
   */
  public void setSimulation(final IStepable stepable) {
    this.m_sim = stepable;
  }

  /**
   * The internal method for creating the simulation thread
   *
   * @param stepable
   *          the stepable
   * @return the new <code>SimulationThread</code>
   */
  protected SimulationThread createSimulationThread(
      final IStepable stepable) {
    return new SimulationThread(stepable);
  }

  /**
   * Create a new simulation control
   *
   * @param thread
   *          the thread
   * @return the <code>SimulationControl</code> to be used by this frame
   */
  protected SimulationControl createSimulationControl(
      final SimulationThread thread) {
    return new SimulationControl(thread);
  }

  /**
   * Update the visualization
   */
  protected synchronized void updateVisualization() {
    JComponent j;
    j = this.m_update;
    if (j != null)
      j.repaint();

  }

  /**
   * Perform a step of the activity, for example a single simulation step
   */
  public synchronized void step() {
    IStepable s;
    s = this.m_sim;
    if (s != null) {
      s.step();
      this.updateVisualization();
    }

  }

}
