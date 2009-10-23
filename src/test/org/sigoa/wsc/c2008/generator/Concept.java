/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-02-11
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.wsc.c2008.generator.Concept.java
 * Last modification: 2008-02-11
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

import java.util.Collections;
import java.util.List;

import org.sfc.collections.CollectionUtils;
import org.sfc.utils.Utils;
import org.sfc.xml.sax.SAXWriter;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * The concept class for the data set generation
 * 
 * @author Thomas Weise
 */
public final class Concept extends IDObject<Concept> {
  /**
   * the randomizer
   */
  private static final IRandomizer RANDOM = Rand.RANDOM;

  /**
   * the children concepts
   */
  List<Concept> m_children;

  /**
   * the parent concepts
   */
  Concept m_parent;

  /**
   * the maximum depth
   */
  int m_maxDepth;

  /**
   * the root distance
   */
  int m_rootDist;

  /**
   * allowed
   */
  private boolean m_allowed;

  /**
   * Create a new concept
   * 
   * @param children
   *          the children
   * @param parent
   */
  Concept(final Concept parent, final boolean children) {
    super();
    this.m_parent = parent;
    if (children) {
      this.m_children = CollectionUtils.createList();
    } else {
      this.m_children = null;
    }
    this.m_allowed = true;
  }

  /**
   * Create a tree of semantic concepts
   * 
   * @param numConcepts
   *          the number of concepts to create
   * @return the root concept
   */
  public static final Concept createConceptTree(final int numConcepts) {
    final Concept c;
    int i;

    i = computeChildCount(numConcepts);
    c = new Concept(null, i > 0);

    if (i > 0) {
      fillIn(c, i, numConcepts - i);
      Utils.invokeGC();
    }

    // if (!(new File("e:\\x.xml").exists()))//$NON-NLS-1$
    // c.serialize("e:\\x.xml"); //$NON-NLS-1$
    return c;
  }

  /**
   * Fill in the concept.
   * 
   * @param fill
   *          the concept to fill
   * @param len
   * @param remaining
   *          the remaining children
   */
  private static final void fillIn(final Concept fill, final int len,
      final int remaining) {
    final List<Concept> cs;
    final double[] d;
    int i, r, c, cc, md, xxx;
    Concept x;
    double s;

    cs = fill.m_children;
    d = new double[len - 1];

    if (len >= 2) {
      do {
        s = 0d;
        for (i = (len - 2); i >= 0; i--) {
          s += (d[i] = RANDOM.nextDouble());
        }
      } while (s <= 0d);
    } else
      s = 0d;

    r = xxx = (remaining - 1);
    md = 1;
    cs.add(x = new Concept(fill, false));
    x.m_rootDist = (fill.m_rootDist + 1);

    for (i = (len - 2); i >= 0; i--) {
      c = Math.min(r, ((int) (Math.round((d[i] / s) * xxx))));
      r -= c;
      if (i <= 0)
        c += r;

      cc = computeChildCount(c);
      cs.add(x = new Concept(fill, cc > 0));
      x.m_rootDist = (fill.m_rootDist + 1);
      if (cc > 0) {
        fillIn(x, cc, c - cc);
        md = Math.max(md, x.m_maxDepth);
      }
    }

    fill.m_maxDepth = (md + 1);
    Collections.shuffle(cs);

  }

  /**
   * Compute a good number of children
   * 
   * @param max
   *          the maximum number of children
   * @return the number of children
   */
  private static final int computeChildCount(final int max) {
    int i;

    if (max <= 0)
      return 0;

    do {
      i = (int) (RANDOM.nextNormal(3, 2));
    } while ((i < 0) || (i >= max));

    return ((max > 1) ? (i + 2) : (i + 1));
  }

  /**
   * Serialize this taxonomy to some external file
   * 
   * @param out
   *          the target file
   */
  public final void serialize(final Object out) {
    SAXWriter w;
    try {
      w = new SAXWriter(out);
      try {
        w.startDocument();
        w.startElement("taxonomy"); //$NON-NLS-1$
        this.toSAXWriter(w, true);
        w.endElement();
        w.endDocument();
      } finally {
        w.close();
      }
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

  /**
   * Serialize this object to xml
   * 
   * @param w
   *          the sax writer to write to
   */
  @Override
  public void toSAXWriter(final SAXWriter w) {
    this.toSAXWriter(w, false);
  }

  /**
   * recursively serialize this object and all its children
   * 
   * @param w
   *          the xml writer to write to
   * @param deep
   *          <code>true</code> if and only if the child concepts should
   *          also be added
   */
  public final void toSAXWriter(final SAXWriter w, final boolean deep) {
    List<Concept> c;
    int l;

    c = this.m_children;
    l = ((c != null) ? c.size() : 0);
    w.startElement((l > 0) ? "concept" : //$NON-NLS-1$
        "instance"); //$NON-NLS-1$
    w.attribute("name", this.toString()); //$NON-NLS-1$

    if (deep && (c != null)) {
      for (--l; l >= 0; l--) {
        c.get(l).toSAXWriter(w, true);
      }
    }

    w.endElement();
    w.flush();
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
    sb
        .append(((this.m_children != null) && (this.m_children.size() > 0)) ? "con" : //$NON-NLS-1$
            "inst"); //$NON-NLS-1$
    super.toStringBuilder(sb);
  }

  /**
   * Copy this concept
   * 
   * @return the copy
   */
  @Override
  final Concept copy() {
    Concept c, q;
    List<Concept> cc, cc2;
    int i;

    c = super.copy();
    i = (c.m_children != null) ? (c.m_children.size()) : 0;
    if (i <= 0) {
      c.m_children = null;
    } else {
      cc2 = this.m_children;
      c.m_children = cc = CollectionUtils.createList();
      for (--i; i >= 0; i--) {
        cc.add(q = cc2.get(i).copy());
        q.m_parent = c;
      }
    }
    return c;
  }

  /**
   * remove the given child
   * 
   * @param child
   *          the child index
   */
  final void removeChild(final Concept child) {
    if (Rand.remove(this.m_children, child)) {
      if (this.m_children.size() <= 0)
        this.m_parent.removeChild(this);
    }
  }

  /**
   * get the relation to the concept c
   * 
   * @param c
   *          the concept
   * @return -1 if this one subsumes c, 1 vice versa, 0 for none
   */
  public final int relation(final Concept c) {
    Concept q;

    q = c;
    while (q != null) {
      if (q == this)
        return -1;
      q = q.m_parent;
    }

    q = this;
    while (q != null) {
      if (q == c)
        return -1;
      q = q.m_parent;
    }

    return 0;
  }

  /**
   * Take a concept from this concept.
   * 
   * @return the concept taken, or <code>null</code> if none could be
   *         taken
   */
  public final Concept takeConcept() {
    Concept c;
    double i;

    this.m_allowed = false;

    c = null;
    for (i = 4d; i < 100; i += 0.4) {
      c = this.takeConcept1(i);
      if (c != null)
        break;
      // c = this.takeConcept1(i);
      // if (c != null)
      // break;
    }
    if (c == null)
      c = this.takeConcept2();

    if (c == null)
      return null;

    return c;
  }

  /**
   * Take a concept from this concept.
   * 
   * @param pwr
   *          the power
   * @return the concept taken, or <code>null</code> if none could be
   *         taken
   */
  private final Concept takeConcept1(final double pwr) {
    List<Concept> cc;
    Concept c, d;
    int cnt, i, j;
    // double rd;

    cc = this.m_children;
    if (cc == null)
      return null;

    cnt = cc.size();
    if (cnt <= 0)
      return null;

    i = RANDOM.nextInt(cnt);
    j = i;

    // rd = this.m_rootDist + 1;

    do {
      c = cc.get(j);
      if ((c.m_children != null) && (c.m_children.size() > 0)) {
        if ((c.m_allowed) && ((c.m_maxDepth * RANDOM.nextDouble() <= pwr))) {
          this.removeChild(c);
          c.m_allowed = false;
          this.m_allowed = false;
          return c;
        }

        d = c.takeConcept1(pwr);
        if (d != null) {
          this.m_allowed = false;
          d.m_allowed = false;
          return d;
        }
      }
      j = ((j + 1) % cnt);
    } while (j != i);

    return null;
  }

  /**
   * Take a concept from this concept.
   * 
   * @return the concept taken, or <code>null</code> if none could be
   *         taken
   */
  private final Concept takeConcept2() {
    List<Concept> cc;
    Concept c, d;
    int cnt, i, j;

    cc = this.m_children;
    if (this.m_children == null)
      return null;
    cnt = cc.size();
    if (cnt <= 0)
      return null;

    i = RANDOM.nextInt(cnt);
    j = i;

    do {
      c = cc.get(j);

      d = c.takeConcept2();
      if ((d != null) && (d.m_allowed)) {
        this.m_allowed = false;
        d.m_allowed = false;
        return d;
      }

      if ((c.m_children != null) && (c.m_children.size() > 0)
          && (c.m_allowed)) {
        this.removeChild(c);
        c.m_allowed = false;
        this.m_allowed = false;
        return c;
      }

      j = ((j + 1) % cnt);
    } while (j != i);

    return null;
  }

  /**
   * serialize this concept
   * 
   * @param l
   *          the list
   */
  final void instancesToList(final List<Concept> l) {
    List<Concept> c;
    int i;

    c = this.m_children;
    if (c != null) {
      i = c.size();
      if (i > 0) {
        for (--i; i >= 0; i--) {
          c.get(i).instancesToList(l);
        }
      }
    } else
      l.add(this);
  }

  // /**
  // * remove all instances from the list
  // * @param l the instance list
  // */
  // final void removeInstances(final ConceptList l) {
  // Concept[] c;
  // int i;
  //
  // c = this.m_children;
  // i = c.length;
  // if (i > 0) {
  // for (--i; i >= 0; i--) {
  // c[i].removeInstances(l);
  // }
  // } else
  // l.remove(this);
  //    
  // }
  /**
   * draw a random instance
   * 
   * @return a random instance
   */
  final Concept randomInstance() {
    List<Concept> c;
    int i;

    c = this.m_children;
    if (c == null)
      return this;
    i = c.size();
    if (i <= 0)
      return null;
    return c.get(RANDOM.nextInt(i)).randomInstance();
  }

  /**
   * draw a random instance
   * 
   * @return a random instance
   */
  final Concept randomDirectInstance() {
    List<Concept> c;
    int i, j;

    c = this.m_children;
    if (c == null)
      return this;
    i = c.size();
    do {
      j = RANDOM.nextInt(i);
    } while ((c.get(j).m_children != null)
        && (c.get(j).m_children.size() > 0));

    return c.get(j);
  }

  /**
   * remove all instances of this concept from the given list
   * 
   * @param cd
   *          the list
   */
  final void removeAllInstances(final List<Concept> cd) {
    List<Concept> c;
    int i;

    c = this.m_children;

    Rand.remove(cd, this);

    if (c != null) {
      for (i = (c.size() - 1); i >= 0; i--) {
        c.get(i).removeAllInstances(cd);
      }
    }
  }
  
  /**
   * @return the m_children
   */
  public List<Concept> getM_children() {
  	return m_children;
  }
  
}
