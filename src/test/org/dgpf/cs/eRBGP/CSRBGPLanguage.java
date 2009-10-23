/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-04-20
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.cs.eRBGP.CSRBGPLanguage.java
 * Last modification: 2008-04-20
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

package test.org.dgpf.cs.eRBGP;

import org.dgpf.eRbgp.net.DefaultRBGPNetLanguage;
import org.dgpf.rbgp.net.DefaultNetSymbolSet;
import org.sigoa.refimpl.genomes.tree.INodeFactory;

/**
 * the cs rbgp language
 * 
 * @author Thomas Weise
 */
public class CSRBGPLanguage extends DefaultRBGPNetLanguage {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * The node factories
   */
  private static final INodeFactory[] FACTORIES = new INodeFactory[] {//
  EnterCSAction.ENTER_CS_FACTORY };

  /**
   * the weights
   */
  private static final double[] WEIGHTS = new double[] {//
  1d,// SendAction.SEND_FACTORY
  };

  /**
   * Create a new default rbgp language
   * 
   * @param s
   *          the symbol set
   */
  public CSRBGPLanguage(final DefaultNetSymbolSet s) {
    super(new DefaultRBGPNetLanguage(s), FACTORIES, WEIGHTS);
  }
}
