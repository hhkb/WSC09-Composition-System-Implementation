/*
 * Copyright (c) 2006 Thomas Weise
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-04-27 10:33:30
 * Original Filename: test.org.sigoa.wsc.c2007.checker.SolutionReader.java
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

package test.org.sigoa.wsc.c2007.checker;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.sfc.collections.CollectionUtils;
import org.sfc.utils.Utils;
import org.sfc.xml.XML;
import org.sfc.xml.sax.ContentHandlerImplementor;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 * The challenge reader is used to read in challenges. The challenges are
 * then stored in an internal list.
 * 
 * @author Thomas Weise
 */
public final class SolutionReader extends ContentHandlerImplementor {

  /**
   * the solution list
   */
  private final List<Solution> m_solutions;

  /**
   * Create a new challenge reader.
   */
  public SolutionReader() {
    super();
    this.m_solutions = CollectionUtils.createList();
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

    s = ((localName != null) ? localName : qName).toLowerCase();
    if (s == null)
      return;
    if (s.contains("case")) { //$NON-NLS-1$

      this.m_solutions.add(new Solution(atts.getValue("name"))); //$NON-NLS-1$
      return;
    }
    if (s.contains("servicespec")) {//$NON-NLS-1$

      i = this.m_solutions.size();
      if (i > 0)
        this.m_solutions.get(i - 1).beginServiceSpec();
      return;
    }
    if (s.contains("service")) {//$NON-NLS-1$

      i = this.m_solutions.size();
      if (i > 0)
        this.m_solutions.get(i - 1).addService(atts.getValue("name"));//$NON-NLS-1$
      return;
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
    String s;
    int i;

    s = ((localName != null) ? localName : qName).toLowerCase();
    if (s == null)
      return;
    if (s.contains("servicespec")) {//$NON-NLS-1$
      i = this.m_solutions.size();
      if (i > 0)
        this.m_solutions.get(i - 1).endServiceSpec();
      return;
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
    XML.saxParseContentOnly(source, this);
  }

  /**
   * Read a list of challenges.
   * 
   * @param source
   *          The source to read from.
   * @return The list of challenges read.
   */
  @SuppressWarnings("unchecked")
  public static final List<Solution> readSolutions(final Object source) {
    SolutionReader r;
    List<Solution> l;
    int i, j;
    String n;

    try {
      r = new SolutionReader();
      r.parse(source);
      l = r.m_solutions;

      main: for (i = (l.size() - 1); i >= 0; i--) {
        n = l.get(i).m_name;

        for (j = (Main.CH.size() - 1); j >= 0; j--) {
          if (Utils.testEqual(Main.CH.get(j).getName(), n)) {
            l.get(i).m_ch = Main.CH.get(j);
            continue main;
          }
        }

        l.remove(i);
      }

      return l;
    } catch (Throwable t) {
      t.printStackTrace();
      return (List<Solution>) (CollectionUtils.EMPTY_LIST);
    }
  }
}
