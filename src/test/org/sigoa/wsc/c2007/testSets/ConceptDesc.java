/*
 * Copyright (c) 2006 Thomas Weise
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-05-02 10:13:15
 * Original Filename: test.org.sigoa.wsc.c2007.testSets.data.ConceptDesc.java
 * Version          : 1.0.3
 * Last modification: 2006-05-08
 *                by: Thomas Weise
 * 
 * License          : GNU LESSER GENERAL PUBLIC LICENSE
 *                    Version 2.1, February 1999
 *                    You should have received a copy of this license along
 *                    with this library; if not, write to the Free Software
 *                    Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 *                    MA 02111-1307, USA or download the license under
 *                    http://www.gnu.org/copyleft/lesser.html.
 *                    
 * Warranty         : This software is provided "as is" without any
 *                    warranty; without even the implied warranty of
 *                    merchantability or fitness for a particular purpose.
 *                    See the Gnu Lesser General Public License for more
 *                    details.
 */

package test.org.sigoa.wsc.c2007.testSets;

import org.sfc.collections.lists.SimpleList;
import org.sfc.xml.sax.SAXWriter;
import org.sigoa.refimpl.stoch.Randomizer;

/**
 * A concept description. It contains the generalization-specialization
 * hierarchy.
 * 
 * @author Thomas Weise
 */
final class ConceptDesc extends Named {
  /**
   * The generalizing concept.
   */
  ConceptDesc m_parent;

  /**
   * The child concepts.
   */
  SimpleList<ConceptDesc> m_children;

  /**
   * Create a new concept desc.
   * 
   * @param parent
   *          The parent concept.
   */
  ConceptDesc(final ConceptDesc parent) {
    super("c"); //$NON-NLS-1$
    this.m_parent = parent;
    if (parent != null)
      parent.m_children.add(this);
    this.m_children = new SimpleList<ConceptDesc>();
  }

  /**
   * Create an ontology.
   * 
   * @param depth
   *          The ontology's depth.
   * @param width
   *          The ontology's width.
   * @param dest
   *          The destinition files.
   * @param roots
   *          The count of the ontology's roots.
   * @return The ontology's root concepts.
   * @param r
   *          The randomizer to be used.
   */
  static final SimpleList<ConceptDesc> createOntology(final int depth,
      final int width, final int roots, final Object dest,
      final Randomizer r) {
    SimpleList<ConceptDesc> l;
    int i, j, k, z;
    ConceptDesc c1;
    SAXWriter x;
    String s;
    ConceptDesc cd;

    l = new SimpleList<ConceptDesc>(
        ((int) (roots * Math.pow(width, depth))));

    for (i = roots; i > 0; i--) {
      ontology(depth, width, l, null);
    }

    for (i = (100 * l.size()); i >= 0; i--) {
      j = r.nextInt(l.size());
      k = r.nextInt(l.size());
      c1 = l.get(j);
      l.set(j, l.get(k));
      l.set(k, c1);
    }

    s = "http://www.w3.org/2001/XMLSchema"; //$NON-NLS-1$

    x = new SAXWriter(dest);
    x.startDocument();

    x.startElement("schema");//$NON-NLS-1$
    x.startPrefixMapping(null, s);
    x.startPrefixMapping("IPO",//$NON-NLS-1$
        "http://www.altova.com/IPO");//$NON-NLS-1$
    x.attribute("targetNamespace",//$NON-NLS-1$ 
        "http://www.altova.com/IPO");//$NON-NLS-1$

    for (z = l.size() - 1; z >= 0; z--) {
      cd = l.get(z);
      x.startElement(s, "complexType");//$NON-NLS-1$
      x.attribute("name", cd.m_name);//$NON-NLS-1$
      x.startElement(s, "complexContent");//$NON-NLS-1$

      if (cd.m_parent != null) {
        x.startElement(s, "extension");//$NON-NLS-1$
        x.attribute("base",//$NON-NLS-1$
            "IPO:" + cd.m_parent.m_name);//$NON-NLS-1$
      }

      x.startElement(s, "sequence");//$NON-NLS-1$

      x.startElement(s, "element");//$NON-NLS-1$
      x.attribute("name",//$NON-NLS-1$ 
          new Named("p").m_name);//$NON-NLS-1$
      x.attribute("type",//$NON-NLS-1$ 
          "xs:string");//$NON-NLS-1$
      x.endElement();
      x.startElement(s, "element");//$NON-NLS-1$
      x.attribute("name",//$NON-NLS-1$ 
          new Named("p").m_name);//$NON-NLS-1$
      x.attribute("type",//$NON-NLS-1$ 
          "xs:string");//$NON-NLS-1$
      x.endElement();

      x.endElement();

      if (cd.m_parent != null)
        x.endElement();
      x.endElement();
      x.endElement();
    }

    x.endElement();
    x.endDocument();
    x.release();

    x = null;
    s = null;
    cd = null;
    System.gc();
    System.gc();
    System.gc();
    System.gc();
    System.gc();
    System.gc();
    return l;
  }

  /**
   * Perform the creation.
   * 
   * @param depth
   *          The ontology's depth.
   * @param width
   *          The ontology's width.
   * @param list
   *          The list to add the concepts to.
   * @param root
   *          The root concept.
   */
  private static final void ontology(final int depth, final int width,
      final SimpleList<ConceptDesc> list, final ConceptDesc root) {
    int i;
    ConceptDesc ch;

    for (i = width; i > 0; i--) {
      list.add(ch = new ConceptDesc(root));
      if (depth > 1) {
        ontology(depth - 1, width, list, ch);
      }
    }
  }

  /**
   * Check wether this concept subsumes anoter one or not.
   * 
   * @param cd
   *          The concept to check if it is susbsumed by this one.
   * @return <code>true</code> if and only if it subsumed by this one.
   */
  final boolean subsumes(final ConceptDesc cd) {
    ConceptDesc c;
    for (c = cd; c != null; c = c.m_parent) {
      if (c == this)
        return true;
    }
    return false;
  }

  /**
   * Obtain a random specialization of this concept desc.
   * 
   * @param r
   *          The randomizer to use.
   * @return The random specialization.
   */
  final ConceptDesc randomSpec(final Randomizer r) {
    ConceptDesc x;
    int l;
    SimpleList<ConceptDesc> c;

    main: for (;;) {
      x = this;

      for (;;) {
        if (r.nextBoolean())
          return x;

        c = x.m_children;
        l = c.size();
        if ((l <= 0) || (r.nextInt(4) <= 0))
          continue main;
        x = c.get(r.nextInt(l));
        if (r.nextBoolean())
          return x;
      }
    }
  }

  /**
   * Obtain a random generalization of this concept desc.
   * 
   * @param r
   *          The randomizer to use.
   * @return The random generalization.
   */
  final ConceptDesc randomGen(final Randomizer r) {
    ConceptDesc x;

    main: for (;;) {
      x = this;

      for (;;) {
        if (r.nextBoolean())
          return x;

        x = x.m_parent;
        if ((x == null) || (r.nextInt(4) <= 0))
          continue main;
        if (r.nextBoolean())
          return x;
      }
    }
  }
}
