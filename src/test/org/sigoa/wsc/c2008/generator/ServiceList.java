/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-02-14
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.wsc.c2008.generator.ConceptList.java
 * Last modification: 2008-02-14
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

package test.org.sigoa.wsc.c2008.generator;

import org.sfc.collections.IArrayConstructor;
import org.sfc.collections.lists.SimpleList;

/**
 * the concept list
 * 
 * @author Thomas Weise
 */
public final class ServiceList extends SimpleList<Service> {

  /**
   * the array constructor
   */
  private static final IArrayConstructor<Service> CONSTR = new IArrayConstructor<Service>() {
    /**
     * The serial version uid.
     */
    private static final long serialVersionUID = 1;

    /**
     * Create an array of the given length.
     * 
     * @param length
     *          the length of the array
     * @return the new array
     */
    public Service[] createArray(final int length) {
      return new Service[length];
    }
  };

  /**
   * Create a concept list
   */
  ServiceList() {
    super(CONSTR);
  }

  /**
   * Create a new simple list.
   * 
   * @param copy
   *          the list to copy from
   */
  ServiceList(final ServiceList copy) {
    super(copy);
  }

  /**
   * Create a new simple list.
   * 
   * @param size
   *          the initial count of slots, use <code>-1</code> for don't
   *          care
   */
  ServiceList(final int size) {
    super(size, CONSTR);
  }

}
