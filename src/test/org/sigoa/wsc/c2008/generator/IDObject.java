/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-02-08
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.wsc.c2008.generator.IDObject.java
 * Last modification: 2008-02-08
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

/**
 * an object with an unique id
 * 
 * @param <T>
 *          the id object type
 * @author Thomas Weise
 */
class IDObject<T extends IDObject<T>> extends XMLObject implements
    Cloneable {
  /**
   * the id of the object
   */
  final int m_id;

  /**
   * Create a new uniquely identifyable object
   */
  IDObject() {
    super();
    this.m_id = Rand.nextId();
  }

  /**
   * Append this object's textual representation to a string builder.
   * 
   * @param sb
   *          The string builder to append to.
   * @see #toString()
   */
  @Override
  public void toStringBuilder(final StringBuilder sb) {
    sb.append(this.m_id);
  }

  /**
   * Copy this object
   * 
   * @return a copy of this object
   */
  @SuppressWarnings("unchecked")
  T copy() {
    try {
      return ((T) (this.clone()));
    } catch (Throwable q) {
      q.printStackTrace();
      return null;
    }
  }

  /**
   * Check for equality
   * 
   * @param o
   *          the object
   * @return the equality
   */
  @Override
  @SuppressWarnings("unchecked")
  public boolean equals(final Object o) {
    return ((o == this) || ((o instanceof IDObject) && ((((IDObject) o).m_id == this.m_id))));
  }

}
