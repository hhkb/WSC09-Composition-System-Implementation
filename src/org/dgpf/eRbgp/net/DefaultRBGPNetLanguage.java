/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-04-18
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.eRbgp.net.DefaultRBGPNetLanguage.java
 * Last modification: 2008-04-18
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

package org.dgpf.eRbgp.net;

import org.dgpf.eRbgp.base.genetics.DefaultRBGPLanguage;
import org.dgpf.rbgp.net.DefaultNetSymbolSet;
import org.sigoa.refimpl.genomes.tree.INodeFactory;

/**
 * the default network language
 * 
 * @author Thomas Weise
 */
public class DefaultRBGPNetLanguage extends DefaultRBGPLanguage {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * The node factories
   */
  private static final INodeFactory[] FACTORIES = new INodeFactory[] { SendAction.SEND_FACTORY };

  /**
   * the weights
   */
  private static final double[] WEIGHTS = new double[] {//
  0.5d,// SendAction.SEND_FACTORY
  };

  /**
   * Create a new default rbgp language
   * 
   * @param s
   *          the symbol set
   */
  public DefaultRBGPNetLanguage(final DefaultNetSymbolSet s) {
    super(new DefaultRBGPLanguage(s), FACTORIES, WEIGHTS);
  }

  /**
   * Create a new node factory set.
   * 
   * @param inherit
   *          a selector to inherite from
   * @param factories
   *          the factories to select from
   * @param weights
   *          their weights
   */
  protected DefaultRBGPNetLanguage(final DefaultRBGPNetLanguage inherit,
      final INodeFactory[] factories, final double[] weights) {
    super(inherit, factories, weights);
  }
}
