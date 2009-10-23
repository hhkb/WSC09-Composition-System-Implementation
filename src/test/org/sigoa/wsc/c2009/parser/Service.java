/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : TODO
 * Creator          : Thomas Weise
 * Original Filename: wsc.c2008.parser.Service.java
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

package test.org.sigoa.wsc.c2009.parser;

import java.util.List;

import org.sfc.collections.CollectionUtils;
import org.sfc.text.TextUtils;
import org.sfc.text.Textable;
import org.sfc.xml.sax.SAXWriter;

/**
 * a service
 *
 * @author Thomas Weise
 */
public class Service extends Textable {
  /**
   * the input
   */
  final List<OntologyElement> m_in;

  /**
   * the output
   */
  final List<OntologyElement> m_out;

  /**
   * the name
   */
  final String m_name;

  /**
   *
   */
  boolean m_used;

  /**
   *
   */
  List<Alternatives> m_a;
  
  /**
   * throughput
   */
  long m_throughput;
  
  /**
   * runtime
   */
  long m_responsetime;

  /**
   * Create a new service
   *
   * @param name
   *          the name
   */
  public Service(final String name) {
    super();
    this.m_name = name;
    this.m_in = CollectionUtils.createList();
    this.m_out = CollectionUtils.createList();
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
   * Append this object's textual representation to a string builder.
   *
   * @param sb
   *          The string builder to append to.
   * @see #toString()
   */
  public void toSAX(final SAXWriter sb) {
    int i;

    sb.startElement("service");//$NON-NLS-1$
    sb.attribute("name", this.m_name);//$NON-NLS-1$

    sb.startElement("inputs");//$NON-NLS-1$
    for (i = 0; i < this.m_in.size(); i++) {
      if (i > 0)
        sb.append(", ");//$NON-NLS-1$
      sb.writeObject(this.m_in.get(i));
    }
    sb.endElement();

    sb.startElement("outputs");//$NON-NLS-1$
    for (i = 0; i < this.m_out.size(); i++) {
      if (i > 0)
        sb.append(", ");//$NON-NLS-1$
      sb.writeObject(this.m_out.get(i));
    }
    sb.endElement();
    sb.endElement();
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
    int i;

    sb.append("Service: "); //$NON-NLS-1$
    sb.append(this.m_name);
    sb.append(TextUtils.LINE_SEPARATOR);
    sb.append("  inputs : "); //$NON-NLS-1$
    for (i = 0; i < this.m_in.size(); i++) {
      if (i > 0)
        sb.append(", ");//$NON-NLS-1$
      this.m_in.get(i).toStringBuilder(sb);
    }
    sb.append(TextUtils.LINE_SEPARATOR);
    sb.append("  outputs: "); //$NON-NLS-1$
    for (i = 0; i < this.m_in.size(); i++) {
      if (i > 0)
        sb.append(", ");//$NON-NLS-1$
      this.m_in.get(i).toStringBuilder(sb);
    }
  }

  /**
   * @param c
   * @return
   */
  public boolean checkUsed(final OntologyElement c) {
    int i;
    for (i = this.m_out.size() - 1; i >= 0; i--) {
      if (c.subsumes(this.m_out.get(i))) {
        // this.m_used = true;
        return true;
      }
    }

    return false;
  }

  /**
   * @param servs
   *          the services
   * @param s
   * @param <T>
   */
  public static final <T> void addIfNew(final List<T> servs, final T s) {

    if (servs.contains(s))
      return;
    servs.add(s);
  }

  /**
   * @param servs
   *          the services
   * @param s
   * @param <T>
   */
  public static final <T> void addIfNew(final List<T> servs,
      final List<T> s) {
    for (int i = (s.size() - 1); i >= 0; i--)
      addIfNew(servs, s.get(i));
  }

  /**
   * have they something in common
   *
   * @param a
   * @param b
   * @return
   */
  public static final boolean hasSomethingInCommon(final List<?> a,
      final List<?> b) {
    int i;

    // if((a==null)&&(b==null)) return true;
    if (a == b)
      return true;
    if ((a == null) || (b == null))
      return false;

    for (i = a.size() - 1; i >= 0; i--) {
      if (b.contains(a.get(i)))
        return true;
    }

    return false;
  }

  /**
   * check
   *
   * @param c
   * @param given
   * @return b
   */
  public static final Service checkUsed(final OntologyElement c,
      final Stage given) {
    int i, j;
    Service x,w;
    List<Service> v;

    for (i = given.m_provided.size() - 1; i >= 0; i--) {
      if (c.subsumes(given.m_provided.get(i)))
        return null;
    }

    outer: for (i = given.m_serv.size() - 1; i >= 0; i--) {
      x = given.m_serv.get(i);
      if (x.m_used) {
        if (x.checkUsed(c)) {
          if (x.m_a == null) {
            x.m_used = true;
            return x;
          }
          v = x.m_a.get(x.m_a.size() - 1).m_servs;
          for (j = (v.size() - 1); j >= 0; j--) {
            if (!(v.get(j).checkUsed(c)))
              continue outer;
          }

          for (j = (v.size() - 1); j >= 0; j--) {
            v.get(j).m_used = true;
          }
          return x;
        }
      }
    }

    outer: for (i = given.m_serv.size() - 1; i >= 0; i--) {
      x = given.m_serv.get(i);
      if (!(x.m_used)) {
        if (x.checkUsed(c)) {
          if (x.m_a == null) {
            x.m_used = true;
            return x;
          }
          v = x.m_a.get(x.m_a.size() - 1).m_servs;
          for (j = (v.size() - 1); j >= 0; j--) {
            w=v.get(j);
            if (!(w.checkUsed(c)))
              continue outer;
          }

          for (j = (v.size() - 1); j >= 0; j--) {
            v.get(j).m_used = true;
          }
          return x;
        }
      }
    }

    return null;

    // y = null;
    // for (i = given.m_serv.size() - 1; i >= 0; i--) {
    // x = given.m_serv.get(i);
    // if (x.m_used) {
    // if ((y == null) || ((y.m_a != null) &&
    // hasSomethingInCommon(y.m_a,x.m_a))) {
    // if (x.checkUsed(c))
    // y = x;
    // }
    // }
    // }
    // // if (y != null)
    // // return y;
    //
    // for (i = given.m_serv.size() - 1; i >= 0; i--) {
    // x = given.m_serv.get(i);
    // if (!(x.m_used)) {
    // if ((y == null) || ((y.m_a != null) && hasSomethingInCommon(y.m_a,
    // x.m_a))) {
    // if (x.checkUsed(c))
    // y = x;
    // }
    // }
    // }

    // if(y==null) {
    // System.out.println(c);
    // System.out.println(given.m_serv);
    // }
    // return y;
  }

  /**
   * check
   *
   * @param s
   */
  public static final void uncheck(final List<Service> s) {
    Service x;
    for (int i = s.size() - 1; i >= 0; i--) {
      x = s.get(i);
      x.m_used = false;
      x.m_a = null;
    }
  }
}
