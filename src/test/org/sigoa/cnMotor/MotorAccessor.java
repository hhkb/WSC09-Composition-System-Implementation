/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-14
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.election.ElectionNetwork.java
 * Last modification: 2007-12-14
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

package test.org.sigoa.cnMotor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;

import org.sfc.text.TextUtils;
import org.sfc.utils.Log;
import org.sigoa.refimpl.simulation.Simulation;

/**
 * The basic network for election algorithms
 * 
 * @author Thomas Weise
 */
public class MotorAccessor extends Simulation<double[]> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the square error
   */
  private double m_sqrError;

  /**
   * Create a new network.
   * 
   * @param provider
   *          the network provider
   */
  @SuppressWarnings("unchecked")
  public MotorAccessor() {
    super();

  }

  /**
   * Obtain the square error
   * 
   * @return the square error
   */
  public double getSquareError() {
    return this.m_sqrError;
  }

  /**
   * This method is called right before the simulation begins.
   * 
   * @throws IllegalStateException
   *           if this simulation is already running or the
   *           {@link #beginIndividual(Serializable)} method has not yet
   *           been called or {@link #endIndividual()} has already been
   *           called.
   */
  @Override
  public void beginSimulation() {
    this.m_sqrError = Double.POSITIVE_INFINITY;
    super.beginSimulation();
  }

  /**
   * This method is called when the simulation has ended.
   * 
   * @throws IllegalStateException
   *           If this simulation is not yet running.
   */
  @Override
  public void endSimulation() {
    BufferedReader r;
    BufferedWriter w;
    Process p;
    double[] values;
    int i;
    String s;
    double d;

    super.endSimulation();
    try {
      try {
        p = Runtime.getRuntime().exec("mono Motion.exe");//$NON-NLS-1$
        // Log.getLog().log("program started"); //$NON-NLS-1$
        if (p != null) {
          try {
            w = new BufferedWriter(new OutputStreamWriter(p
                .getOutputStream()));
            // Log.getLog().log("writer opened"); //$NON-NLS-1$

            if (w != null) {
              try {
                r = new BufferedReader(new InputStreamReader(p
                    .getInputStream()));
                if (r != null) {
                  // Log.getLog().log("reader opened"); //$NON-NLS-1$

                  try {

                    values = this.getSimulated();

                    Log.getLog().log(
                        "writing " + TextUtils.toString(values));//$NON-NLS-1$
                    for (i = 0; i < values.length; i++) {
                      w.write(Double.toString(values[i]));
                      w.flush();
                      w.newLine();
                      w.flush();
                    }
                    w.flush();
                    // Log.getLog().log("done writing"); //$NON-NLS-1$

                    s = r.readLine();
                    Log.getLog().log("read: " + s); //$NON-NLS-1$
                    if (s != null) {
                      try {
                        d = Double.parseDouble(s.trim());
                        if (d >= d)
                          this.m_sqrError = d;
                      } catch (Throwable t3) {
                        d = Double.POSITIVE_INFINITY;
                      }
                    }

                  } finally {
                    r.close();
                  }
                }

              } finally {
                w.close();
              }
            }

          } finally {
            try {
              p.waitFor();
            } catch (Throwable t1) {
              t1.printStackTrace();
            }
          }
        }
      } catch (Throwable t) {
        t.printStackTrace();
        return;
      }
    } finally {
      Log.getLog().log("finished, now waiting"); //$NON-NLS-1$
//      ElectionFilter.waitSecs(30);
      Log.getLog().log("done waiting"); //$NON-NLS-1$
    }
  }

}
