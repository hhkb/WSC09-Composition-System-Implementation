/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-03-13
 * Creator          : Thomas Weise
 * Original Filename: test.org.dgpf.cs.rbgp.CSActionSet.java
 * Last modification: 2008-03-13
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

package test.org.dgpf.cs.rbgp;

import org.dgpf.rbgp.base.Action;
import org.dgpf.rbgp.net.DefaultNetActionSet;

/**
 * the action set for critical section problems
 * 
 * @author Thomas Weise
 */
public class CSActionSet extends DefaultNetActionSet {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the default cs actions
   */
  public static final CSActionSet DEFAULT_CS_ACTIONS = new CSActionSet();

  /**
   * the cs action
   */
  private final Action<?> m_csAction;

  /**
   * Create a new default cs set
   */
  public CSActionSet() {
    super(false);
    this.m_csAction = new EnterCSAction(this);
  }

  /**
   * obtain the cs action
   * 
   * @return the cs action
   */
  public Action<?> getEnterCsAction() {
    return this.m_csAction;
  }

}
