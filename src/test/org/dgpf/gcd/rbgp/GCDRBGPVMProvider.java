/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-09-22
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.gcd.rbgp.GCDRBGPVMProvider.java
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

import org.dgpf.rbgp.base.RBGPMemory;
import org.dgpf.rbgp.base.RBGPProgram;
import org.dgpf.rbgp.single.RBGPParameters;
import org.dgpf.vm.base.IHostedVirtualMachineFactory;
import org.sfc.io.CanonicalFile;

import test.org.dgpf.gcd.GCDProvider;

/**
 * The gcd classifier provider
 * 
 * @author Thomas Weise
 */
public class GCDRBGPVMProvider extends
    GCDProvider<RBGPMemory, RBGPProgram> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a new gcd data record
   * 
   * @param parameters
   *          the virtual machine parameters
   * @param directory
   *          the directory to log to
   * @param scenarioCount
   *          the scenarioCount
   * @param randomized
   *          <code>true</code> if and only if the scenarios should be
   *          ranomized
   */
  @SuppressWarnings("unchecked")
  public GCDRBGPVMProvider(final RBGPParameters parameters,
      final CanonicalFile directory, final boolean randomized,
      final int scenarioCount) {
    super(parameters,
        (IHostedVirtualMachineFactory) (GCDRBGPVMFactory.FACTORY),
        GCDRBGPVM.class, directory, randomized, scenarioCount);
  }
}
