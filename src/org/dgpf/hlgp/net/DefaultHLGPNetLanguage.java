/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-15
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.hlgp.net.DefaultHLGPNetLanguage.java
 * Last modification: 2007-12-15
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

package org.dgpf.hlgp.net;

import org.dgpf.hlgp.base.DefaultHLGPLanguage;
import org.dgpf.hlgp.net.constructs.instructions.SendFactory;
import org.sigoa.refimpl.genomes.tree.INodeFactory;
import org.sigoa.refimpl.genomes.tree.reproduction.NodeFactorySet;

/**
 * the default network language
 * 
 * @author Thomas Weise
 */
public class DefaultHLGPNetLanguage extends NodeFactorySet {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * The node factories
   */
  private static final INodeFactory[] FACTORIES = new INodeFactory[] {// 
  SendFactory.SEND_FACTORY,//
  };

  /**
   * the weights
   */
  private static final double[] WEIGHTS = new double[] { //
  1.0d,// send
  };

  /**
   * the default language
   */
  public static final NodeFactorySet DEFAULT_NET_LANGUAGE = new DefaultHLGPNetLanguage(
      false);

  /**
   * Create a new default language.
   * 
   * @param canTerminate
   *          Can the programs terminate themselves?
   */
  public DefaultHLGPNetLanguage(final boolean canTerminate) {
    super(new DefaultHLGPLanguage(canTerminate), FACTORIES, WEIGHTS);
  }

  /**
   * the read resolver
   * 
   * @return the default language
   */
  private final Object readResolve() {
    return DEFAULT_NET_LANGUAGE;
  }

  /**
   * the write resolver
   * 
   * @return the default language
   */
  private final Object writeReplace() {
    return DEFAULT_NET_LANGUAGE;
  }

}
