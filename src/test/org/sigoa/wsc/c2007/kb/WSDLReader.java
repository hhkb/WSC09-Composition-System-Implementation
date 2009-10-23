/*
 * Copyright (c) 2006 Thomas Weise
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-04-21 06:46:43
 * Original Filename: test.org.sigoa.wsc.c2007.kb.WSDLReader.java
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

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.sfc.io.IO;
import org.sfc.xml.XML;
import org.sfc.xml.sax.ContentHandlerImplementor;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 * The <code>WSDLReader</code> parses service descriptions into a service
 * register. This internal class is used by the knowledge base.
 * 
 * @author Thomas Weise
 */
public final class WSDLReader extends ContentHandlerImplementor implements
    FileFilter {
  // /**
  // * The element containing the operation's name.
  // */
  // private static final String OPERATION_ELEM = "operation";
  // /**
  // * The attribute containing the operation name.
  // */
  // private static final String OPERATION_ATTR = "name";
  /**
   * The element containing the input message's name.
   */
  private static final String INPUT_ELEM = "input"; //$NON-NLS-1$

  // /**
  // * The element containing the output message's name.
  // */
  // private static final String OUTPUT_ELEM = "output";

  /**
   * The attribute containing the input and ouput message's name.
   */
  private static final String IO_ATTR = "message";//$NON-NLS-1$

  /**
   * The element containing either the input or output message's body and
   * name.
   */
  private static final String MESSAGE_ELEM = "message";//$NON-NLS-1$

  /**
   * The element containing either the input or output message's name.
   */
  private static final String MESSAGE_ATTR = "name";//$NON-NLS-1$

  /**
   * The element containing a conceptual part name.
   */
  private static final String PART_ELEM = "part";//$NON-NLS-1$

  /**
   * The attribute containing a conceptual part name.
   */
  private static final String PART_ATTR = "type";//$NON-NLS-1$

  /**
   * The root tag of the wsdl documents.
   */
  private static final String ROOT_ELEM = "definitions";//$NON-NLS-1$

  /**
   * The wsdl file extension.
   */
  private static final String WSDEXTENSION = ".wsdl";//$NON-NLS-1$

  /**
   * The knowledge base used to resolve the concepts.
   */
  private final KnowledgeBase m_kb;

  /**
   * The SimpleLists with the input/output concepts.
   */
  private final ConceptList[] m_messages;

  /**
   * The message names.
   */
  private final String[] m_messageNames;

  /**
   * The SimpleList index.
   */
  private int m_SimpleListIndex;

  /**
   * The current service name.
   */
  private String m_name;

  /**
   * <code>true</code> if and only if we're parsing recursively.
   */
  private boolean m_recursive;

  /**
   * Create a new wsdl reader.
   * 
   * @param kb
   *          The knowledge base to be used to resolve the concepts.
   */
  @SuppressWarnings("unchecked")
  public WSDLReader(final KnowledgeBase kb) {
    super();
    this.m_kb = kb;

    ConceptList[] l;
    int i;

    l = new ConceptList[2];

    for (i = (l.length - 1); i >= 0; i--) {
      l[i] = new ConceptList(-1);
    }

    this.m_messages = l;
    this.m_messageNames = new String[2];
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
    ConceptList l;
    String s;
    int i;

    // if(localName.equals(OPERATION_ELEM))
    // {
    // this.m_name = atts.getValue("", OPERATION_ATTR);
    // }
    // else
    {
      if (localName.equals(INPUT_ELEM)) {
        if (atts.getValue("", IO_ATTR).endsWith(this.m_messageNames[1])) //$NON-NLS-1$
        {
          l = this.m_messages[1];
          this.m_messages[1] = this.m_messages[0];
          this.m_messages[0] = l;
        }
      } else {
        if (localName.equals(MESSAGE_ELEM)) {
          this.m_messageNames[this.m_SimpleListIndex] = atts.getValue(
              "", MESSAGE_ATTR); //$NON-NLS-1$
        } else {
          if (localName.equals(PART_ELEM)) {
            s = atts.getValue("", PART_ATTR); //$NON-NLS-1$
            if (s != null) {
              i = s.indexOf(':');

              this.m_messages[this.m_SimpleListIndex].add(this.m_kb
                  .getConcept((i >= 0) ? s.substring(i + 1) : s));
            }
          }
        }
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
    ConceptList l;
    Concept[] i, o;
    int x, y, z;
    Concept c;

    if (localName.equals(MESSAGE_ELEM)) {
      this.m_SimpleListIndex++;
    } else {
      if (localName.equals(ROOT_ELEM)) {
        l = this.m_messages[0];
        x = l.m_count;
        i = l.m_data;

        main1: for (y = (x - 1); y > 0; y--) {
          c = i[y];
          for (z = (y - 1); z >= 0; z--) {
            if (c.subsumes(i[z])) {
              x--;
              continue main1;
            }
            if (i[z].subsumes(c)) {
              i[z] = c;
              x--;
              continue main1;
            }
          }
        }
        l.m_count = x;
        i = new Concept[x];
        l.copyToArray(i, x);
        l.clear();

        l = this.m_messages[1];
        x = l.m_count;
        o = l.m_data;

        main2: for (y = (x - 1); y > 0; y--) {
          c = o[y];
          for (z = (y - 1); z >= 0; z--) {
            if (c.subsumes(o[z])) {
              x--;
              continue main2;
            }
            if (o[z].subsumes(c)) {
              o[z] = c;
              x--;
              continue main2;
            }
          }
        }
        l.m_count = x;
        o = new Concept[x];
        l.copyToArray(o, x);
        l.clear();

        // l = this.m_messages[0];
        // x = l.size();
        // i = new Concept[x];
        // l.copyToArray(i, x);
        // l.clear();
        //
        // l = this.m_messages[1];
        // x = l.size();
        // o = new Concept[x];
        // l.copyToArray(o, x);
        // l.clear();

        this.m_SimpleListIndex = 0;
        this.m_messageNames[0] = null;
        this.m_messageNames[1] = null;

        this.m_kb.storeService(new Service(this.m_name, i, o));
        this.m_name = null;        
      }
    }
  }

  /**
   * Parse an wsdl-file, resolve all i/o concepts with the knowledge base
   * and store the services found in the service registry.
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
    String s;
    File f;

    this.m_messages[0].clear();
    this.m_messages[1].clear();
    this.m_SimpleListIndex = 0;
    this.m_messageNames[0] = null;
    this.m_messageNames[1] = null;

    f = IO.getFile(source);
    s = f.getName();
    this.m_name = s.substring(0, s.lastIndexOf('.'));

    XML.saxParseContentOnly((source instanceof String) ? f : source, this);
  }

  /**
   * Parse a directory wsdl-file, resolve all i/o concepts with the
   * knowledge base and store the services found in the service registry.
   * 
   * @param directory
   *          The directory containing all the files.
   * @param recursive
   *          <code>true</code> if and only if sub-directories should
   *          also be parsed.
   */
  public final void parseDirectory(final Object directory,
      final boolean recursive) {
    this.m_recursive = recursive;
    IO.getFile(directory).listFiles(this);
  }

  /**
   * That method is a hack to obtain files and directories fast and with
   * few memory consumption. Tests whether or not the specified abstract
   * pathname should be included in a pathname SimpleList.
   * 
   * @param pathname
   *          The abstract pathname to be tested
   * @return <code>true</code> if and only if <code>pathname</code>
   *         should be included
   */
  public final boolean accept(final File pathname) {
    if (pathname.isFile()
        && (pathname.getName().toLowerCase().endsWith(WSDEXTENSION))) {
      try {
        this.parse(pathname);
      } catch (Throwable t) {
        t.printStackTrace();
      }
    } else {
      if (this.m_recursive && pathname.isDirectory()) {
        pathname.listFiles(this);
      }
    }

    return false;
  }
}
