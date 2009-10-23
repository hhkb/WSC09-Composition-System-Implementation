/*
 * Copyright (c) 2009 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2009-05-09
 * Creator          : Thomas Weise
 * Original Filename: wsc.c2009.parser.WSLAReader.java
 * Last modification: 2009-05-09
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

import org.sfc.utils.Utils;
import org.sfc.xml.XML;
import org.sfc.xml.sax.SAXReader;
import org.xml.sax.Attributes;

/**
 * the wsla reader
 * 
 * @author Thomas Weise
 */
public class WSLAReader extends SAXReader {
  
/**
 * the services
 */
  private final List<Service> m_l;

  /**
   * 0=?, 1=throughput, 2=runtime
   */
  int m_last;

  /**
   * service
   */
  String m_serv;

  /**
   * 0=?, 1=slaParam, 2=value
   */
  int m_in;

/**
 * @param l
 */
  private WSLAReader(final List<Service> l){
    super();
    this.m_l = l;
  }
  
  /**
   * @param uri
   *          the Namespace URI, or the empty string if the element has no
   *          Namespace URI or if Namespace processing is not being
   *          performed
   * @param localName
   *          the local name (without prefix), or the empty string if
   *          Namespace processing is not being performed
   * @param qualifiedName
   *          the qualified name (with prefix), or the empty string if
   *          qualified names are not available
   * @param atts
   *          the attributes attached to the element. If there are no
   *          attributes, it shall be an empty Attributes object. The value
   *          of this object after startElement returns is undefined
   */
  @Override
  public void startElement(final String uri, final String localName,
      final String qualifiedName, final Attributes atts) {

    if ("SLAParameter".equals(localName)) {//$NON-NLS-1$
      this.m_in = 1;
    } else {
      if ("Value".equals(localName)) {//$NON-NLS-1$
        this.m_in = 2;
      } else {
        this.m_in = 0;
      }
    }
  }

  /**
   * @param uri
   *          the Namespace URI, or the empty string if the element has no
   *          Namespace URI or if Namespace processing is not being
   *          performed
   * @param localName
   *          the local name (without prefix), or the empty string if
   *          Namespace processing is not being performed
   * @param qualifiedName
   *          the qualified name (with prefix), or the empty string if
   *          qualified names are not available
   * @param atts
   *          the attributes attached to the element. If there are no
   *          attributes, it shall be an empty Attributes object. The value
   *          of this object after startElement returns is undefined
   */
  @Override
  public void endElement(final String uri, final String localName,
      final String qualifiedName) {
    this.m_in = 0;
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
    long i;
    int j;
    Service l;
    List<Service> x;

    if (this.m_in != 0) {
      s = new String(ch, start, length);

      if (this.m_in == 1) {
        if (s.indexOf("Throughput") >= 0){ //$NON-NLS-1$
          this.m_last = 1;
        }
        else {if (s.indexOf("Responsetime") >= 0){//$NON-NLS-1$
          this.m_last = 2;
        }
        else{
          this.m_last = 0; return ;}
        }
        
        j = s.indexOf("serv");//$NON-NLS-1$
        if(j>=0){
          this.m_serv = s.substring(j).trim() + "Service";//$NON-NLS-1$
        }
        
      } else {
        if(this.m_in == 2){
          i = Long.parseLong(s); 
          
          if((this.m_last != 0)&&(this.m_serv != null)){
            x= this.m_l;
            for(j = (x.size()-1); j>=0;j--){
              l=x.get(j);
              if(l.m_name.equals(this.m_serv)){
                this.m_serv = null;      
                if(this.m_last==1) l.m_throughput = i;
                else l.m_responsetime = i;
                
//                System.out.println(l.m_name + " : " +//$NON-NLS-1$
//                    this.m_last + " " +//$NON-NLS-1$
//                    i);
                
                return;
              }
            }
          }
          this.m_serv = null;
        }
      }
    }

  }

  /**
   * Read an wsla from a file
   * 
   * @param servs
   *          the services;
   * @param file
   *          the file
   */
  public static final void readWSLA(List<Service> servs, final Object file) {
    WSLAReader r;

    r = new WSLAReader(servs);
    XML.saxParse(file, r);
    
    Utils.invokeGC();
  }
}
