/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : TODO
 * Creator          : Thomas Weise
 * Original Filename: wsc.c2008.parser.OntologyReader.java
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
import java.util.Map;

import org.sfc.collections.CollectionUtils;
import org.sfc.utils.Utils;
import org.sfc.xml.XML;
import org.sfc.xml.sax.SAXReader;
import org.xml.sax.Attributes;

/**
 * The ontology reader
 *
 * @author Thomas Weise
 */
public class OntologyReader extends SAXReader {
  /**
   * the root ontology
   */
  private final OntologyElement m_root;

  /**
   * the current ontology
   */
  private OntologyElement m_cur;

  /**
   * the elements to be resolved
   */
  private final List<OntologyElement> m_res;

  /**
   * the map
   */
  private final Map<String, OntologyElement> m_map;

  /**
   * the ontology reader
   */
  private OntologyReader() {
    super();
    this.m_root = new OntologyElement("root", false); //$NON-NLS-1$
    this.m_res = CollectionUtils.createList();
    this.m_map = CollectionUtils.createMap();
  }

  /*
   * @param uri the Namespace URI, or the empty string if the element has
   * no Namespace URI or if Namespace processing is not being performed
   * @param localName the local name (without prefix), or the empty string
   * if Namespace processing is not being performed @param qualifiedName
   * the qualified name (with prefix), or the empty string if qualified
   * names are not available @param atts the attributes attached to the
   * element. If there are no attributes, it shall be an empty Attributes
   * object. The value of this object after startElement returns is
   * undefined
   *
   * @see #endElement
   * @see org.xml.sax.Attributes
   */
  @Override
  public void startElement(final String uri, final String localName,
      final String qualifiedName, final Attributes atts) {
    OntologyElement e1, e2;
    String s;

    if ("Class".equals(localName) || //$NON-NLS-1$
        "Thing".equals(localName)) {//$NON-NLS-1$
      e1 = new OntologyElement(atts.getValue(
          "http://www.w3.org/1999/02/22-rdf-syntax-ns#",//$NON-NLS-1$
          "ID"),//$NON-NLS-1$
          "Thing".equals(localName));//$NON-NLS-1$
      this.m_map.put(e1.m_name, e1);
      this.m_cur = e1;
    } else {

      if (("subClassOf".equals(localName) || //$NON-NLS-1$
      "type".equals(localName))) {//$NON-NLS-1$
        s = atts.getValue("http://www.w3.org/1999/02/22-rdf-syntax-ns#",//$NON-NLS-1$
            "resource");//$NON-NLS-1$
        if (s != null) {
          s = s.substring(1);
          e1 = this.m_cur;
          e2 = this.m_map.get(s);
          if (e2 != null) {
            e1.setParent(e2);
          } else {
            this.m_res.add(e1);
            e1.m_parentName = s;
          }

          // e1.m_parentName = s;
          // e2 = this.m_root.find(s);
          // if (e2 != null) {
          // e1.setParent(e2);
          // }
          // this.m_resolve.add(e1);
        }
      }

    }
  }

  /*
   * @param uri the Namespace URI, or the empty string if the element has
   * no Namespace URI or if Namespace processing is not being performed
   * @param localName the local name (without prefix), or the empty string
   * if Namespace processing is not being performed @param qualifiedName
   * the qualified XML name (with prefix), or the empty string if qualified
   * names are not available
   */
  @Override
  public void endElement(final String uri, final String localName,
      final String qualifiedName) {
    OntologyElement e;

    if ("Class".equals(localName) || //$NON-NLS-1$
        "Thing".equals(localName)) {//$NON-NLS-1$
      e = this.m_cur;
      if (e.m_parentName == null) {
        if (e.m_parent == null)
          e.setParent(this.m_root);
      }

    }
  }

  /**
   * refine the ontology
   *
   * @return the ontology
   */
  private OntologyElement refine() {
    int i;
    OntologyElement e, x;

    for (i = this.m_res.size() - 1; i >= 0; i--) {
      e = this.m_res.get(i);
      x = this.m_map.get(e.m_parentName);
      if (x != null) {
        e.setParent(x);
      }
    }

    return this.m_root;
  }

  /**
   * Read an ontology from a file
   *
   * @param file
   *          the file
   * @return the ontology
   */
  public static final OntologyElement readOntology(final Object file) {
    OntologyReader r;
    OntologyElement e;

    r = new OntologyReader();
    XML.saxParse(file, r);
    e = r.refine();
    r = null;
    Utils.invokeGC();

    return e;
  }
}
