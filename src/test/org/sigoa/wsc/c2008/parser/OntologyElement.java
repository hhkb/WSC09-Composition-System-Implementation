/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : TODO
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.wsc.c2008.parser.OntElement.java
 * Last modification: TODO
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

package test.org.sigoa.wsc.c2008.parser;

import java.util.List;
import java.util.Map;

import org.sfc.collections.CollectionUtils;
import org.sfc.text.Textable;

/**
 * The concept or class
 * 
 * @author Thomas Weise
 */
public class OntologyElement extends Textable implements
    Comparable<OntologyElement> {
  /**
   * the concept name
   */
  final String m_name;

  /**
   * the children
   */
  private final List<OntologyElement> m_children;

  /**
   * the parent
   */
  OntologyElement m_parent;

  /**
   * the parent name
   */
  String m_parentName;

  /**
   * Create a new ontology element
   * 
   * @param name
   *          the name
   * @param isInstance
   *          true if this is an instance, false if it is a concept
   */
  public OntologyElement(final String name, final boolean isInstance) {
    super();
    this.m_name = name;
    if (isInstance)
      this.m_children = null;
    else
      this.m_children = CollectionUtils.createList();
  }

  /**
   * Find an ontology element of the given name
   * 
   * @param name
   *          the name
   * @return the element, or <code>null</code> if none could be found
   */
  public final OntologyElement find(final String name) {
    int i;
    List<OntologyElement> l;
    OntologyElement q;

    if (this.m_name.equals(name))
      return this;

    l = this.m_children;
    if (l == null)
      return null;
    for (i = (l.size() - 1); i >= 0; i--) {
      q = l.get(i).find(name);
      if (q != null)
        return q;
    }

    return null;
  }

  /**
   * the depth
   * 
   * @return the depth
   */
  public final int depth() {
    if (this.m_parent == null)
      return 0;
    return (this.m_parent.depth() + 1);
  }

  /**
   * put all instances to a map
   * 
   * @param el
   *          the map
   */
  public final void instsToMap(final Map<String, OntologyElement> el) {
    List<OntologyElement> oe;
    int i;

    oe = this.m_children;
    if (oe == null) {
      el.put(this.m_name, this);
    } else {
      for (i = oe.size() - 1; i >= 0; i--) {
        oe.get(i).instsToMap(el);
      }
    }

  }

  /**
   * Obtain the connection from this ontology element to another one
   * 
   * @param oe
   *          the ontology element
   * @param sb
   *          the string builder
   */
  public final void getConnection(final OntologyElement oe,
      final StringBuilder sb) {
    if (this.subsumes(oe))
      this.getConnectionil(oe, sb);
    else
      oe.getConnectionil(this, sb);
  }

  /**
   * Obtain the connection from this ontology element to another one
   * 
   * @param oe
   *          the ontology element
   * @param sb
   *          the string builder
   */
  private final void getConnectionil(final OntologyElement oe,
      final StringBuilder sb) {
    OntologyElement x;

    if (this == oe) {
      sb.append(this.m_name);
      return;
    }

    if (this.m_children == null) {
      this.m_parent.getConnectionil(oe, sb);
      sb.append('-');
      sb.append(this.m_name);
      return;
    }

    x = oe;
    while (x != null) {
      if (x != oe)
        sb.append('-');
      sb.append(x.m_name);
      if (x == this)
        return;
      x = x.m_parent;
    }
  }

  /**
   * Check whether element o1 subsumes element o2
   * 
   * @param o1
   *          the first element
   * @param o2
   *          the second one
   * @return true if so, false otherwise
   */
  public final boolean subsumes(final String o1, final String o2) {
    OntologyElement e1, e2;

    e1 = this.find(o1);
    if (e1 == null)
      return false;

    e2 = this.find(o2);
    if (e2 == null)
      return false;

    return e1.subsumes(e2);
  }

  /**
   * @param c1
   *          the concept
   * @param c2
   * @return thep aret
   */
  public static final OntologyElement getCommonParent(
      final OntologyElement c1, final OntologyElement c2) {
    OntologyElement x, y;

    for (x = c1; x != null; x = x.m_parent) {
      for (y = c2; y != null; y = y.m_parent) {
        if (y == x)
          return y;
      }
    }

    return null;
  }

  /**
   * perform insertion
   * 
   * @param l
   * @param el
   */
  public static final void insertSpec(final List<OntologyElement> l,
      final OntologyElement el) {
    int i;
    OntologyElement e;

    for (i = (l.size() - 1); i >= 0; i--) {
      e = l.get(i);
      if (e.subsumes(el))
        l.remove(i);
      else if (el.subsumes(e))
        return;
    }

    l.add(el);
  }

  /**
   * Check whether this element subsumes another one
   * 
   * @param e
   *          the element
   * @return true if so, false otherwise
   */
  public final boolean subsumes(final OntologyElement e) {
    OntologyElement f;

    if (this == e)
      return true;

    if (this.m_children == null)
      return ((this.m_parent != null) ? this.m_parent.subsumes(e) : false);

    f = e;
    while (f != null) {
      if (f == this)
        return true;
      f = f.m_parent;
    }

    return false;
  }

  /**
   * Check whether this element subsumes another one
   * 
   * @param e
   *          the element
   * @return true if so, false otherwise
   */
  public final int subsumesDist(final OntologyElement e) {
    OntologyElement f;
    int i;

    if (e == this)
      return 0;

    if (this.m_children == null) {
      if (this.m_parent == null)
        return Integer.MAX_VALUE;
      i = (this.m_parent.subsumesDist(e) + 1);
      if (i >= Integer.MAX_VALUE)
        return i;
      return i + 1;
    }

    f = e;
    i = 0;
    while (f != null) {
      if (f == this)
        return i;
      f = f.m_parent;
      i++;
    }

    return Integer.MAX_VALUE;
  }

  /**
   * Obtain the human readable representation of this textable object.
   * 
   * @return the human readable representation of this textable object
   */
  @Override
  public String toString() {
    return this.m_name;
  }

  /**
   * set the parent
   * 
   * @param e
   *          the parent
   */
  final void setParent(final OntologyElement e) {
    if (e != null) {
      this.m_parent = e;
      e.m_children.add(this);
    }
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
    sb.append(this.m_name);
  }

  /**
   * @param o
   * @return
   */
  public int compareTo(OntologyElement o) {
    return this.m_name.compareTo(o.m_name);
  }
}
