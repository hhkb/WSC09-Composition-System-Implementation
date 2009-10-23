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
 * The wsdl reader
 *
 * @author Thomas Weise
 */
public class WSDLReader extends SAXReader {
  /**
   * the root ontology
   */
  private final Map<String, OntologyElement> m_ont;

  /**
   * the elements to be services
   */
  private final List<Service> m_services;

  /**
   * the service map
   */
  private final Map<String, Service> m_map;

  /**
   * the current service
   */
  Service m_cur;

  /**
   * the where add attribute
   */
  boolean m_add;

  /**
   * the wsdl reader
   *
   * @param root
   *          the root
   */
  private WSDLReader(final OntologyElement root) {
    super();
    this.m_ont = CollectionUtils.createMap();
    // this.m_root = root;
    root.instsToMap(this.m_ont);
    this.m_services = CollectionUtils.createList();
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
    Service v;
    boolean b;
    String s;
    // int i;

    if ("semMessageExt".equals(localName)) {//$NON-NLS-1$
      s = atts.getValue("", "id");//$NON-NLS-1$//$NON-NLS-2$
      b = s.contains("RequestMessage");//$NON-NLS-1$
      if (b)
        s = s.substring(0, s.length() - "RequestMessage".length());//$NON-NLS-1$
      else
        s = s.substring(0, s.length() - "ResponseMessage".length());//$NON-NLS-1$
      s = s + "Service";//$NON-NLS-1$
      this.m_add = b;

      v = this.m_map.get(s);
      if (v == null) {
        this.m_services.add(this.m_cur = new Service(s));
        this.m_map.put(s, this.m_cur);
      }

      // for (i = (this.m_services.size() - 1); i >= 0; i--) {
      // v = this.m_services.get(i);
      // if (s.equals(v.m_name)) {
      // this.m_cur = v;
      // return;
      // }
      // }
      //
      // this.m_services.add(this.m_cur = new Service(s));
    }
  }

  /*
   * @param ch the characters from the XML document @param start the start
   * position in the array @param length the number of characters to read
   * from the array
   *
   * @see #ignorableWhitespace
   * @see org.xml.sax.Locator
   */
  @Override
  public void characters(final char ch[], final int start, final int length) {
    String s;
    int i;
    OntologyElement e;

    s = new String(ch, start, length);
    i = s.indexOf("http://www.ws-challenge.org/wsc08.owl#");//$NON-NLS-1$
    if (i >= 0) {
      i += "http://www.ws-challenge.org/wsc08.owl#".length(); //$NON-NLS-1$
      s = s.substring(i).trim();

      e = this.m_ont.get(s);// this.m_root.find(s);
      if (e != null) {
        if (this.m_add)
          this.m_cur.m_in.add(e);
        else
          this.m_cur.m_out.add(e);
      }
    }

  }

  /**
   * Read an wsdl from a file
   *
   * @param ont
   *          the ontology;
   * @param file
   *          the file
   * @return the ontology
   */
  public static final List<Service> readWSDL(OntologyElement ont,
      final Object file) {
    WSDLReader r;
    List<Service> l;

    r = new WSDLReader(ont);
    XML.saxParse(file, r);
    l = r.m_services;
    r = null;

    Utils.invokeGC();

    return l;
  }
}
