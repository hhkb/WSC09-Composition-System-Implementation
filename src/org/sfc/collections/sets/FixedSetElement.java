/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-12
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.collections.sets.FixedSetElement.java
 * Last modification: 2007-11-12
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

package org.sfc.collections.sets;

import java.io.Serializable;

import org.sfc.text.Textable;

/**
 * The base class of elements of fixed sets.
 * 
 * @param <FST>
 *          the fixed set type
 * @author Thomas Weise
 */
public class FixedSetElement<FST extends FixedSet<?>> extends Textable
    implements Serializable {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the id of the action
   */
  public final int m_id;

  /**
   * the owning set
   */
  public final FST m_set;

  /**
   * Create a new element.
   * 
   * @param set
   *          The owning set
   */
  @SuppressWarnings("unchecked")
  public FixedSetElement(final FST set) {
    super();

    FixedSet<FixedSetElement<?>> x;
    x = ((FixedSet<FixedSetElement<?>>) set);
    this.m_set = set;
    this.m_id = x.registerElement(this);
  }

  /**
   * Obtain this element's id.
   * 
   * @return the id of this element
   */
  public final int getId() {
    return this.m_id;
  }

  /**
   * Obtain the element set this element is part of.
   * 
   * @return the element set this element is part of
   */
  public final FST getSet() {
    return this.m_set;
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
    sb.append(this.getClass().getSimpleName());
    sb.append(':');
    sb.append(this.m_id);
  }
}
