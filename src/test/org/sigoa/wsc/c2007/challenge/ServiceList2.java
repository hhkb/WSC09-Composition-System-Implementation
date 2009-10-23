/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-06-11
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.wsc.c2007.challenge.ServiceListList2.java
 * Last modification: 2007-06-11
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

package test.org.sigoa.wsc.c2007.challenge;

import org.sfc.collections.IArrayConstructor;
import org.sfc.collections.lists.SimpleList;

import test.org.sigoa.wsc.c2007.kb.ServiceList;


/**
 * The service list class
 *
 * @author Thomas Weise
 */
public class ServiceList2 extends SimpleList<ServiceList> {

  /**
   * The concept array constructor
   */
  public static final IArrayConstructor<ServiceList> SERVICE_LIST_ARRAY_CONSTRUCTOR = new IArrayConstructor<ServiceList>() {
    public static final long serialVersionUID = 1;

    public ServiceList[] createArray(final int length) {
      return new ServiceList[length];
    }

    private final Object readResolve() {
      return SERVICE_LIST_ARRAY_CONSTRUCTOR;
    }

    private final Object writeReplace() {
      return SERVICE_LIST_ARRAY_CONSTRUCTOR;
    }
  };

  /**
   * Create a new concept list.
   *
   * @param initialSize
   *          The initial size. Use -1 for don't care.
   */
  public ServiceList2(final int initialSize) {
    super(initialSize, SERVICE_LIST_ARRAY_CONSTRUCTOR);
  }

  /**
   * Create a new simple list.
   *
   * @param copy
   *          the list to copy from
   */
  @SuppressWarnings("unchecked")
  public ServiceList2(final SimpleList<ServiceList> copy) {
    super(copy);
  }

}
