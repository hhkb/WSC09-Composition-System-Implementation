/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-18
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.gcd.lgp.GCDLGPVMProvider.java
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

import org.dgpf.lgp.base.LGPMemory;
import org.dgpf.lgp.base.LGPProgram;
import org.dgpf.lgp.single.LGPParameters;
import org.dgpf.vm.base.IHostedVirtualMachineFactory;
import org.sfc.io.CanonicalFile;

import test.org.dgpf.gcd.GCDProvider;

/**
 * The gcd lgp vm provider
 * 
 * @author Thomas Weise
 */
public class GCDLGPVMProvider extends GCDProvider<LGPMemory, LGPProgram> {
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
  public GCDLGPVMProvider(final LGPParameters parameters,
      final CanonicalFile directory, final boolean randomized,
      final int scenarioCount) {
    super(parameters,
        (IHostedVirtualMachineFactory) (GCDLGPVMFactory.FACTORY),
        GCDLGPVM.class, directory, randomized, scenarioCount);
  }
}