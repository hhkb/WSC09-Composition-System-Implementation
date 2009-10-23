/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-18
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.gcd.lgp.GCDLGPVM.java
 * Last modification: 2007-11-18
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

package test.org.dgpf.gcd.lgp;

import org.dgpf.lgp.base.EIndirection;
import org.dgpf.lgp.base.LGPProgram;
import org.dgpf.lgp.single.LGPParameters;
import org.dgpf.lgp.single.LGPVM;
import org.dgpf.vm.objectives.error.IErrorInformation;

import test.org.dgpf.gcd.IGCDResultInfo;

/**
 * The linear genetic programming gcd vm.
 * 
 * @author Thomas Weise
 */
public class GCDLGPVM extends LGPVM<GCDLGPVMProvider> implements
    IErrorInformation, IGCDResultInfo {

  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

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
  public GCDLGPVM(final LGPParameters params, final GCDLGPVMProvider host) {
    super((params != null) ? params : LGPParameters.DEFAULT_PARAMETERS,
        host);
  }

  /**
   * Initialize this vm
   */
  @Override
  public final void beginSimulation() {
    int i;

    super.beginSimulation();

    i = this.m_simId;

    EIndirection.GLOBAL.write(0, this.m_host.m_a[i], this);
    EIndirection.GLOBAL.write(1, this.m_host.m_b[i], this);

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
  public final void beginIndividual(final LGPProgram what) {
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
    return (EIndirection.GLOBAL.read(2, this) - this.m_host.m_result[this.m_simId - 1]);
  }

  /**
   * obtain the result value
   * 
   * @return the result value
   */
  public int getGCDResult() {
    return EIndirection.GLOBAL.read(2, this);
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
