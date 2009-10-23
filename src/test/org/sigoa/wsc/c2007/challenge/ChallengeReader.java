/*
 * Copyright (c) 2006 Thomas Weise
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-04-27 10:33:30
 * Original Filename: test.org.sigoa.wsc.c2007.challenge.ChallengeReader.java
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

package test.org.sigoa.wsc.c2007.challenge;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.sfc.collections.lists.SimpleList;
import org.sfc.xml.XML;
import org.sfc.xml.sax.ContentHandlerImplementor;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import test.org.sigoa.wsc.c2007.kb.ConceptList;
import test.org.sigoa.wsc.c2007.kb.KnowledgeBase;


/**
 * The challenge reader is used to read in challenges. The challenges are
 * then stored in an internal list.
 * 
 * @author Thomas Weise
 */
public final class ChallengeReader extends ContentHandlerImplementor {
  /**
   * The element type containing the name of the challenge.
   */
  private static final String COMPOSITION_ELEM = "CompositionRoutine"; //$NON-NLS-1$

  /**
   * The second possible element type containing the name of the challenge.
   */
  private static final String DISCOVERY_ELEM = "DiscoveryRoutine";//$NON-NLS-1$

  /**
   * The name of the attribute holding the challenge's name.
   */
  private static final String COMPOSITION_ATTR = "name";//$NON-NLS-1$

  /**
   * The element type containing the concepts provided.
   */
  private static final String PROVIDED_ELEM = "Provided";//$NON-NLS-1$

  /**
   * The element type containing the concepts required.
   */
  private static final String RESULTANT_ELEM = "Resultant";//$NON-NLS-1$

  /**
   * The current challenge's name.
   */
  private String m_name;

  /**
   * The stage.
   */
  private byte m_stage;

  /**
   * The stringbuilder holding the input concepts.
   */
  private final StringBuilder m_in;

  /**
   * The stringbuilder holding the output concepts.
   */
  private final StringBuilder m_out;

  /**
   * The input concept list.
   */
  private final ConceptList m_inl;

  /**
   * The output concept list.
   */
  private final ConceptList m_outl;

  /**
   * The challenges to be performed.
   */
  private final SimpleList<Challenge> m_challenges;

  /**
   * The knowledge base to store the stuff into.
   */
  private final KnowledgeBase m_base;

  /**
   * Create a new challenge reader.
   * 
   * @param base
   *          The knowledge base with the concepts and services
   */
  public ChallengeReader(final KnowledgeBase base) {
    super();
    this.m_base = base;
    this.m_in = new StringBuilder();
    this.m_out = new StringBuilder();
    this.m_inl = new ConceptList(-1);
    this.m_outl = new ConceptList(-1);
    this.m_challenges = new SimpleList<Challenge>(-1);
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
    if (localName.equals(COMPOSITION_ELEM)
        || localName.equals(DISCOVERY_ELEM)) {
      this.m_stage = 0;
      this.m_name = atts.getValue("", COMPOSITION_ATTR);//$NON-NLS-1$
    } else {

      if (localName.equals(PROVIDED_ELEM)) {
        this.m_stage = 1;
        this.m_in.setLength(0);
      } else {

        if (localName.equals(RESULTANT_ELEM)) {
          this.m_stage = 2;
          this.m_out.setLength(0);
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
    if (localName.equals(COMPOSITION_ELEM)
        || localName.equals(DISCOVERY_ELEM)) {
      this.m_stage = 0;

      this.extract(this.m_in, this.m_inl);
      this.extract(this.m_out, this.m_outl);

      this.m_challenges.add(new Challenge(this.m_name, this.m_inl,
          this.m_outl));
    } else {

      if (localName.equals(PROVIDED_ELEM)) {
        this.m_stage = 0;
      } else {
        if (localName.equals(RESULTANT_ELEM)) {
          this.m_stage = 0;
        }
      }
    }

  }

  /**
   * Receive notification of character data.
   * <p>
   * The Parser will call this method to report each chunk of character
   * data. SAX parsers may return all contiguous character data in a single
   * chunk, or they may split it into several chunks; however, all of the
   * characters in any single event must come from the same external entity
   * so that the Locator provides useful information.
   * </p>
   * 
   * @param ch
   *          the characters from the XML document
   * @param start
   *          the start position in the array
   * @param length
   *          the number of characters to read from the array
   * @see org.xml.sax.Locator
   */
  @Override
  public final void characters(final char ch[], final int start,
      final int length) {
    switch (this.m_stage) {
    case 1: {
      this.m_in.append(ch, start, length);
      break;
    }
    case 2: {
      this.m_out.append(ch, start, length);
      break;
    }
    }
  }

  /**
   * Extract the concepts from a string builder.
   * 
   * @param sb
   *          The string builder with the text.
   * @param cl
   *          The list to store the concepts into.
   */
  private final void extract(final StringBuilder sb, final ConceptList cl) {
    int i, j, k;
    String s;
    KnowledgeBase base;

    base = this.m_base;
    cl.clear();
    j = sb.length();

    for (i = (j - 1); i >= 0; i--) {
      if (sb.charAt(i) == ',') {
        s = sb.substring(i + 1, j).trim();
        k = s.indexOf(':');
        if (k >= 0)
          s = s.substring(k + 1);
        cl.add(base.getConcept(s));
        j = i;
      }
    }

    s = sb.substring(i + 1, j).trim();
    k = s.indexOf(':');
    if (k >= 0)
      s = s.substring(k + 1);
    cl.add(base.getConcept(s));
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
   * Obtain the list of challenges read.
   * 
   * @return the list of challenges read
   */
  public final SimpleList<Challenge> getChallenges() {
    return this.m_challenges;
  }

  /**
   * Read a list of challenges.
   * 
   * @param source
   *          The source to read from.
   * @param base
   *          The knowledge base with the concepts and services
   * @return The list of challenges read.
   */
  public static final ChallengeList readChallenges(final Object source,
      final KnowledgeBase base) {
    ChallengeReader r;

    try {
      r = new ChallengeReader(base);
      r.parse(source);
      return new ChallengeList(r.m_challenges);
    } catch (Throwable t) {
      t.printStackTrace();
      return new ChallengeList(new SimpleList<Challenge>());
    }
  }
}
