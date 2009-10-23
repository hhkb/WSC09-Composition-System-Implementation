/*
 * Copyright (c) 2006 Thomas Weise
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-04-20 16:57:15
 * Original Filename: test.org.sigoa.wsc.c2007.kb.XSDReader.java
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

package test.org.sigoa.wsc.c2007.kb;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.sfc.xml.XML;
import org.sfc.xml.sax.ContentHandlerImplementor;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 * The xsd reader can be used to read a xsd input and extract the semantic
 * concepts. These concepts will then be stored in a knowledge base, so
 * they can be querried in future. This internal class is used by the
 * knowledge base.
 * 
 * @author Thomas Weise
 */
public final class XSDReader extends ContentHandlerImplementor {
  /**
   * The element type containing the name of the concept as attribute.
   */
  private static final String TYPE_ELEM = "complexType"; //$NON-NLS-1$

  /**
   * The name of the attribute holding the concept's name.
   */
  private static final String TYPE_ATTR = "name";//$NON-NLS-1$

  /**
   * The element type containing the name of the generalized concept as
   * attribute.
   */
  private static final String GENERALIZATION_ELEM = "extension";//$NON-NLS-1$

  /**
   * The name of the attribute holding the generalized concept's name.
   */
  private static final String GENERALIZATION_ATTR = "base";//$NON-NLS-1$

  /**
   * The knowledge base to store the stuff into.
   */
  private final KnowledgeBase m_base;

  /**
   * The current concept name.
   */
  private String m_current;

  /**
   * The current generalized concept.
   */
  private String m_generalization;

  /**
   * Create a new <code>XSDReader</code> which is able to read the
   * contents of an xsd-file and store it into a knowledge base.
   * 
   * @param base
   *          The knowledge base to store the stuff into.
   */
  public XSDReader(final KnowledgeBase base) {
    super();
    this.m_base = base;
  }

  /**
   * Receive notification of the beginning of an element.
   * <p>
   * The Parser will invoke this method at the beginning of every element
   * in the XML document; there will be a corresponding
   * {@link #endElement endElement} event for every startElement event
   * (even when the element is empty). All of the element's content will be
   * reported, in order, before the corresponding endElement event.
   * </p>
   * <p>
   * This event allows up to three name components for each element:
   * </p>
   * <ol>
   * <li>the Namespace URI;</li>
   * <li>the local name; and</li>
   * <li>the qualified (prefixed) name.</li>
   * </ol>
   * 
   * @param uri
   *          the Namespace URI, or the empty string if the element has no
   *          Namespace URI or if Namespace processing is not being
   *          performed
   * @param localName
   *          the local name (without prefix), or the empty string if
   *          Namespace processing is not being performed
   * @param qName
   *          the qualified name (with prefix), or the empty string if
   *          qualified names are not available
   * @param atts
   *          the attributes attached to the element. If there are no
   *          attributes, it shall be an empty Attributes object. The value
   *          of this object after startElement returns is undefined
   * @see org.xml.sax.Attributes
   */
  @Override
  public final void startElement(final String uri, final String localName,
      final String qName, final Attributes atts) {
    String s;
    int i;

    if (localName.equals(TYPE_ELEM)) {
      this.m_current = atts.getValue("", TYPE_ATTR);//$NON-NLS-1$
    } else {
      if (localName.equals(GENERALIZATION_ELEM)) {
        s = atts.getValue("", GENERALIZATION_ATTR);//$NON-NLS-1$
        if (s != null) {
          i = s.indexOf(':');
          if (i >= 0)
            this.m_generalization = s.substring(i + 1);
          else
            this.m_generalization = s;
        } else
          this.m_generalization = null;
      }
    }
  }

  /**
   * Receive notification of the end of an element.
   * <p>
   * The SAX parser will invoke this method at the end of every element in
   * the XML document; there will be a corresponding
   * {@link #startElement startElement} event for every endElement event
   * (even when the element is empty).
   * </p>
   * <p>
   * For information on the names, see startElement.
   * </p>
   * 
   * @param uri
   *          the Namespace URI, or the empty string if the element has no
   *          Namespace URI or if Namespace processing is not being
   *          performed
   * @param localName
   *          the local name (without prefix), or the empty string if
   *          Namespace processing is not being performed
   * @param qName
   *          the qualified XML name (with prefix), or the empty string if
   *          qualified names are not available
   */
  @Override
  public final void endElement(final String uri, final String localName,
      final String qName) {
    if (localName.equals(TYPE_ELEM) && (this.m_current != null)) {
      this.m_base.insertConcept(this.m_current, this.m_generalization);
      this.m_current = null;
      this.m_generalization = null;
    }
  }

  /**
   * Parse an xsd-file and store its concepts into the knowledge base.
   * 
   * @param source
   *          The data sourceto read and parse data from. Can be a Reader,
   *          a File, an InputSource or an InputStream.
   * @exception SAXException
   *              Any SAX exception, possibly wrapping another exception.
   * @exception IOException
   *              An IO exception from the parser, possibly from a byte
   *              stream or character stream supplied by the application.
   * @exception ParserConfigurationException
   *              if a parser cannot be created which satisfies the
   *              requested configuration.
   * @exception SAXNotRecognizedException
   *              When the underlying XMLReader does not recognize the
   *              property name.
   * @exception SAXNotSupportedException
   *              When the underlying XMLReader recognizes the property
   *              name but doesn't support the property.
   */
  public final void parse(final Object source)
      throws SAXNotRecognizedException, SAXNotSupportedException,
      SAXException, ParserConfigurationException, IOException {
    this.m_current = null;
    this.m_generalization = null;
    XML.saxParseContentOnly(source, this);
  }
}
