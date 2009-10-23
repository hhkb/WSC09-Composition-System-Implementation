/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-22
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.gcd.rbgp.GCDRBGPVM.java
 * Last modification: 2007-09-22
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

package test.org.dgpf.gcd.rbgp;

import org.dgpf.rbgp.base.RBGPProgram;
import org.dgpf.rbgp.base.Variable;
import org.dgpf.rbgp.single.RBGPParameters;
import org.dgpf.rbgp.single.RBGPVM;
import org.dgpf.vm.objectives.error.IErrorInformation;

import test.org.dgpf.gcd.IGCDResultInfo;

/**
 * A classifier vm that
 * 
 * @author Thomas Weise
 */
public class GCDRBGPVM extends RBGPVM<GCDRBGPVMProvider> implements
    IErrorInformation, IGCDResultInfo {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * in 1
   */
  private final Variable m_in1;

  /**
   * in 2
   */
  private final Variable m_in2;

  /**
   * out
   */
  private final Variable m_out;

  /**
   * the simulation id
   */
  private int m_simId;

  /**
   * Create a new virtual machine.
   * 
   * @param params
   *          the classifier parameters needed
   * @param host
   *          the host
   */
  public GCDRBGPVM(final RBGPParameters params,
      final GCDRBGPVMProvider host) {
    super((params != null) ? params : RBGPParameters.DEFAULT_PARAMETERS,
        host);

    this.m_in1 = this.m_symbols.getVariable(0);
    this.m_in2 = this.m_symbols.getVariable(1);
    this.m_out = this.m_symbols.getVariable(2);
  }

  /**
   * Initialize this vm
   */
  @Override
  public final void beginSimulation() {
    int i;

    super.beginSimulation();

    i = this.m_simId;

    this.setValue(this.m_in1, this.m_host.m_a[i]);
    this.setValue(this.m_in2, this.m_host.m_b[i]);

    this.m_simId = (i + 1);
  }

  /**
   * Begin the simulation of the specified individual.
   * 
   * @param what
   *          the individual
   * @throws NullPointerException
   *           if <code>what</code> is <code>null</code>.
   * @throws IllegalStateException
   *           if this simulation is already running.
   */
  @Override
  public final void beginIndividual(final RBGPProgram what) {
    super.beginIndividual(what);
    this.m_simId = 0;
  }

  /**
   * Obtain the absolute error
   * 
   * @return the absolute error
   */
  public final double getAbsoluteError() {
    return Math.abs(this.getRawError());
  }

  /**
   * Obtain the square error
   * 
   * @return the square error
   */
  public final double getSquareError() {
    double d;

    d = this.getRawError();
    return (d * d);
  }

  /**
   * Obtain the raw error
   * 
   * @return the raw error
   */
  public final double getRawError() {
    return (this.getNextValue(this.m_out) - this.m_host.m_result[this.m_simId - 1]);
  }

  /**
   * obtain the result value
   * 
   * @return the result value
   */
  public int getGCDResult() {
    return this.getNextValue(this.m_out);
  }

  /**
   * obtain the first value
   * 
   * @return the first value
   */
  public int getA() {
    return this.m_host.m_a[this.m_simId - 1];
  }

  /**
   * obtain the second value
   * 
   * @return the second value
   */
  public int getB() {
    return this.m_host.m_b[this.m_simId - 1];
  }

  /**
   * obtain the result value
   * 
   * @return the result value
   */
  public int getGCD() {
    return this.m_host.m_result[this.m_simId - 1];

  }
}
