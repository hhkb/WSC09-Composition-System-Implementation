/*
 * Copyright (c) 2006 Thomas Weise
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-05-02 09:30:00
 * Original Filename: test.org.sigoa.wsc.c2007.testSets.data.ServiceDesc.java
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

import java.io.File;

import org.sfc.collections.lists.SimpleList;
import org.sfc.xml.sax.SAXWriter;

/**
 * This class is a service description, used to create solutions with.
 * 
 * @author Thomas Weise
 */
class ServiceDesc extends Named {
  /**
   * The output concepts.
   */
  final SimpleList<ConceptDesc> m_out;

  /**
   * The input concepts.
   */
  final SimpleList<ConceptDesc> m_in;

  /**
   * Create a new service.
   */
  public ServiceDesc() {
    super("service"); //$NON-NLS-1$
    this.m_out = new SimpleList<ConceptDesc>(-1);
    this.m_in = new SimpleList<ConceptDesc>(-1);
  }

  /**
   * Store this service desc as wsdl document in a directory.
   * 
   * @param dir
   *          The directory to store this service desc into.
   */
  final void storeWsdl(final File dir) {
    SAXWriter sw;
    int i, z;
    ConceptDesc c;

    sw = new SAXWriter(new File(dir, this.m_name + ".wsdl"));//$NON-NLS-1$
    sw.startDocument();

    sw.startElement("definitions");//$NON-NLS-1$
    sw.startPrefixMapping(null, "http://schemas.xmlsoap.org/wsdl/");//$NON-NLS-1$
    sw.startPrefixMapping("tns",//$NON-NLS-1$
        "http://tempuri.org/4s4c/1/3/wsdl/def/interopLab");//$NON-NLS-1$
    sw.attribute("name",//$NON-NLS-1$ 
        "interopLab");//$NON-NLS-1$
    sw.attribute("targetNamespace",//$NON-NLS-1$
        "http://tempuri.org/4s4c/1/3/wsdl/def/interopLab");//$NON-NLS-1$

    sw.startElement("message");//$NON-NLS-1$
    sw.attribute("name",//$NON-NLS-1$ 
        this.m_name + "_Request");//$NON-NLS-1$
    i = 0;
    for (z = this.m_in.size() - 1; z >= 0; z--) {
      c = this.m_in.get(z);
      sw.startElement("part");//$NON-NLS-1$
      sw.attribute("name",//$NON-NLS-1$
          "part" + i);//$NON-NLS-1$
      sw.attribute("type",//$NON-NLS-1$
          "IPO:" + c.m_name);//$NON-NLS-1$
      sw.endElement();
      i++;
    }
    sw.endElement();

    sw.startElement("message");//$NON-NLS-1$
    sw.attribute("name",//$NON-NLS-1$ 
        this.m_name + "_Response");//$NON-NLS-1$
    i = 0;
    for (z = this.m_out.size() - 1; z >= 0; z--) {
      c = this.m_out.get(z);
      sw.startElement("part");//$NON-NLS-1$
      sw.attribute("name",//$NON-NLS-1$ 
          "part" + i);//$NON-NLS-1$
      sw.attribute("type",//$NON-NLS-1$ 
          "IPO:" + c.m_name);//$NON-NLS-1$
      sw.endElement();
      i++;
    }
    sw.endElement();

    sw.startElement("portType");//$NON-NLS-1$
    sw.attribute("name",//$NON-NLS-1$
        this.m_name + "Port");//$NON-NLS-1$
    sw.startElement("operation");//$NON-NLS-1$
    sw.attribute("name", this.m_name);//$NON-NLS-1$

    sw.startElement("input");//$NON-NLS-1$
    sw.attribute("message",//$NON-NLS-1$ 
        "tns:" + this.m_name + //$NON-NLS-1$
            "_Request");//$NON-NLS-1$
    sw.endElement();

    sw.startElement("output");//$NON-NLS-1$
    sw.attribute("message",//$NON-NLS-1$ 
        "tns:" + this.m_name + //$NON-NLS-1$ 
            "_Response");//$NON-NLS-1$
    sw.endElement();

    sw.endElement();
    sw.endElement();

    sw.endElement();
    sw.endDocument();
    sw.release();
    sw = null;
    c = null;
    System.gc();
    System.gc();
    System.gc();
    System.gc();
  }
}
