/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-12-29
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.xml.sax.SAXWriter.java
 * Last modification: 2006-12-29
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

package org.sfc.xml.sax;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.text.NumberFormat;
import java.util.Map;

import org.sfc.collections.CollectionUtils;
import org.sfc.io.IO;
import org.sfc.io.TextWriter;
import org.sfc.text.TextUtils;
import org.sfc.xml.XML;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.EntityResolver2;
import org.xml.sax.ext.LexicalHandler;

/**
 * The class <code>SAXWriter</code> allows you to serialize xml data in a
 * light-weight manner. This version does not support dtd's or other
 * exorbitant xml stuff, just plain xml-documents with namespaces and such
 * and such. This simple architecture comes with increased speed and
 * decreased memory consumption compared to the dom-serialization-approach.
 * 
 * @author Thomas Weise
 */
public class SAXWriter extends TextWriter implements EntityResolver,
    DTDHandler, ContentHandler, ErrorHandler, LexicalHandler,
    EntityResolver2, DeclHandler {
  /**
   * the sax writer's line separator.
   */
  private static final char[] LINE_SEPARATOR = new char[] { XML.LINE_SEPARATOR };

  /**
   * the text at the document start.
   */
  private static final char[] CHR_DOC_START = "<?xml version=\"".toCharArray(); //$NON-NLS-1$

  /**
   * the text used for the encoding.
   */
  private static final char[] CHR_DOC_ENCODING = "\" encoding=\"".toCharArray(); //$NON-NLS-1$

  /**
   * the text at the end of a processing instruction
   */
  private static final char[] CHR_PROC_INST_END = "\"?>".toCharArray(); //$NON-NLS-1$

  /**
   * the comment start
   */
  private static final char[] COMMENT_START = "<!--".toCharArray(); //$NON-NLS-1$

  /**
   * the comment start
   */
  private static final char[] COMMENT_END = "-->".toCharArray();//$NON-NLS-1$

  /**
   * the doctype start
   */
  private static final char[] DOCTYPE = "<!DOCTYPE".toCharArray(); //$NON-NLS-1$

  /**
   * public naming
   */
  private static final char[] PUBLIC = " PUBLIC ".toCharArray(); //$NON-NLS-1$

  /**
   * system naming
   */
  private static final char[] SYSTEM = " SYSTEM ".toCharArray(); //$NON-NLS-1$

  /**
   * an element declaration
   */
  private static final char[] ELEMENT = "<!ELEMENT ".toCharArray(); //$NON-NLS-1$

  /**
   * the attribute list definition
   */
  private static final char[] ATTLIST = "<!ATTLIST ".toCharArray(); //$NON-NLS-1$

  /**
   * an entity declaration.
   */
  private static final char[] ENTITY = "<!ENTITY ".toCharArray();//$NON-NLS-1$

  /**
   * the cdata start
   */
  private static final char[] CDATA_START = "<![CDATA[".toCharArray();//$NON-NLS-1$

  /**
   * the cdata end
   */
  private static final char[] CDATA_END = "]]>".toCharArray();//$NON-NLS-1$

  /**
   * The sax exception caught last.
   */
  SAXException m_lastSAXException;

  /**
   * the encoding used.
   */
  private final String m_encoding;

  /**
   * <code>&gt;0</code> if and only if encoding is turned on.
   */
  int m_encode;

  /**
   * a small multi-purpose buffer.
   */
  final char[] m_buffer;

  /**
   * The element stack.
   */
  private Element m_elementStack;

  /**
   * The current state.
   */
  EState m_state;

  /**
   * The internal namespace-counter.
   */
  int m_nsct;

  /**
   * The state stack.
   */
  private EState[] m_stateStack;

  /**
   * The state stack depth.
   */
  private int m_stateCnt;

  /**
   * the string builder used internally for the attributes
   */
  final StringBuilder m_attrs;

  /**
   * Create a new sax writer.
   * 
   * @param out
   *          The output stream to write to.
   * @param numberFormat
   *          the numberformat to be used to format numeric output
   * @param csvSeparator
   *          the csv separator
   * @param indentFactor
   *          the indent factor
   */
  protected SAXWriter(final Writer out, final NumberFormat numberFormat,
      final char[] csvSeparator, final int indentFactor) {
    super(out, numberFormat, LINE_SEPARATOR, csvSeparator, indentFactor);

    String s;
    Charset cs;

    s = super.getEncoding();
    if (s != null) {
      try {
        cs = Charset.forName(s);
      } catch (UnsupportedCharsetException ucse) {
        cs = null;
      }

      this.m_encoding = ((cs != null) ? cs.name() : s);
    } else
      this.m_encoding = null;

    this.m_buffer = new char[16];

    this.m_state = EState.NOTHING;
    this.m_stateStack = new EState[32];
    this.m_stateCnt = 0;
    this.m_encode = 1;
    this.m_attrs = new StringBuilder();
  }

  /**
   * Create a new sax writer.
   * 
   * @param out
   *          The output stream to write to.
   * @param numberFormat
   *          the numberformat to be used to format numeric output
   * @param csvSeparator
   *          the csv separator
   * @param indentFactor
   *          the indent factor
   */
  public SAXWriter(final Writer out, final NumberFormat numberFormat,
      final String csvSeparator, final int indentFactor) {
    this(out, numberFormat, ((csvSeparator == null) ? null : csvSeparator
        .toCharArray()), indentFactor);
  }

  /**
   * Create a new sax writer.
   * 
   * @param out
   *          The output stream to write to.
   */
  public SAXWriter(final Writer out) {
    this(out, null, (char[]) null, -1);
  }

  /**
   * Create a new sax writer.
   * 
   * @param out
   *          The output stream to write to.
   */
  public SAXWriter(final Object out) {
    this(IO.getWriter(out, XML.PREFERED_ENCODING));
  }

  /**
   * Obtain (and clear) the last sax error caught.
   * 
   * @return the last sax error caught or <code>null</code> if no error
   *         occured since this method was called last
   * @see #getLastIOException()
   */
  public SAXException getLastSAXException() {
    SAXException x;
    x = this.m_lastSAXException;
    this.m_lastSAXException = null;
    return x;
  }

  /**
   * Returns the name of the character encoding for the entity.
   * 
   * @return Name of the character encoding being used to interpret the
   *         entity's text, or null if this was not provided for a
   *         character stream passed through an InputSource or is otherwise
   *         not yet available in the current parsing state.
   */
  @Override
  public String getEncoding() {
    return this.m_encoding;
  }

  /**
   * Returns the version of XML used for the entity. This will normally be
   * the identifier from the current entity's
   * <em>&lt;?xml&nbsp;version='...'&nbsp;...?&gt;</em> declaration, or
   * be defaulted by the parser.
   * 
   * @return Identifier for the XML version being used to interpret the
   *         entity's text, or null if that information is not yet
   *         available in the current parsing state.
   */
  public String getXMLVersion() {
    return XML.PREFERED_VERSION;
  }

  /**
   * Push a state to the internal state stack.
   */
  private final void pushState() {
    int c;
    EState[] s;

    s = this.m_stateStack;
    c = this.m_stateCnt;

    if (c >= s.length) {
      s = new EState[c << 1];
      System.arraycopy(this.m_stateStack, 0, s, 0, c);
      this.m_stateStack = s;
    }

    s[c] = this.m_state;
    this.m_stateCnt = (c + 1);
  }

  /**
   * Pop a state from the internal stack.
   * 
   * @return the state popped
   */
  private final EState popState() {
    int c;

    c = this.m_stateCnt;
    if (c <= 0) {
      this.m_lastSAXException = new SAXException();
      return EState.NOTHING;
    }

    c--;
    this.m_stateCnt = c;
    return (this.m_state = this.m_stateStack[c]);
  }

  /**
   * Write a portion of an array of characters. This method is invoked by
   * all other routines of the text writer.
   * 
   * @param cbuf
   *          Array of characters
   * @param off
   *          Offset from which to start writing characters
   * @param len
   *          Number of characters to write
   */
  @Override
  public void write(final char cbuf[], final int off, final int len) {

    int i, j, end;
    char ch;
    char[] x;
    String s;

    if (this.m_encode > 0) {

      this.elementItem();// TODO

      i = off;
      end = (i + len);
      x = this.m_buffer;

      main: for (;;) {
        for (j = i; j < end; j++) {
          ch = cbuf[j];

          // check whether the character should be escaped or not

          if ((ch > 127) || (ch < 0) || (ch == '<') || (ch == '>')
          /* || (ch == '&') || (ch == '#') */|| (ch == '/') || (ch == '"')
              || (ch == '\'')) {

            if (i < j) {
              super.write(cbuf, i, j - i);
            }

            x[0] = '&';
            x[1] = '#';
            s = Integer.toString(ch);
            i = s.length();
            s.getChars(0, i, x, 2);
            x[i + 2] = ';';
            super.write(x, 0, i + 3);
            i = j + 1;
            continue main;
          }
        }
        break main;
      }

      if (i < j) {
        super.write(cbuf, i, j - i);
      }
    } else
      super.write(cbuf, off, len);
  }

  /**
   * Receive notification of the beginning of a document.
   * <p>
   * The SAX parser will invoke this method only once, before any other
   * event callbacks.
   * </p>
   * 
   * @see #endDocument
   */
  public void startDocument() {
    String s;

    if (this.m_state != EState.NOTHING) {
      this.m_lastSAXException = new SAXException();
      return;
    }

    this.m_encode--;

    if (this.isFormatting())
      this.ensureNewLine();

    super.write(CHR_DOC_START);
    this.write(this.getXMLVersion());

    s = this.getEncoding();
    if (s != null) {
      super.write(CHR_DOC_ENCODING);
      this.write(s);
    }

    super.write(CHR_PROC_INST_END);
    this.m_encode++;
  }

  /**
   * End this xml document.
   * 
   * @see #startDocument
   */
  public void endDocument() {
    while (this.m_stateCnt > 0) {
      popState();
      switch (this.m_state) {
      case CDATA: {
        this.endCDATA();
        break;
      }

      case INTERNAL_DTD:
      case DTD: {
        this.endDTD();
        break;
      }

      default:
      }
    }

    while (this.m_elementStack != null)
      this.m_elementStack = this.m_elementStack.close();
  }

  // /
  // / LexicalHandler
  // /
  /**
   * Start a dtd declaration.
   * 
   * @param name
   *          The document type name.
   * @param publicId
   *          The declared public identifier for the external DTD subset,
   *          or null if none was declared.
   * @param systemId
   *          The declared system identifier for the external DTD subset,
   *          or null if none was declared. (Note that this is not resolved
   *          against the document base URI.)
   * @see #endDTD
   */
  public void startDTD(final String name, final String publicId,
      final String systemId) {
    boolean b;
    String pname, ppublicId, psystemId;
    char[] ch;

    if (((pname = TextUtils.preprocessString(name)) == null)
        || (this.m_elementStack != null)) {
      this.m_lastSAXException = new SAXException();
      return;
    }

    switch (this.m_state) {
    case NOTHING:
    case MAPPING: {
      pushState();
      break;
    }

    default: {
      this.m_lastSAXException = new SAXException();
      return;
    }
    }

    this.m_state = EState.DTD;

    this.m_encode--;
    if (this.isFormatting())
      this.ensureNewLine();
    this.incIndent();

    this.write(DOCTYPE, 0, DOCTYPE.length);

    ch = this.m_buffer;
    ch[0] = ' ';
    this.buffer(ch, 0, 1);
    this.write(pname);

    if ((ppublicId = TextUtils.preprocessString(publicId)) != null) {
      this.write(PUBLIC, 0, PUBLIC.length);
      b = (ppublicId.charAt(0) != '"');

      if (b) {
        ch[0] = '"';
        //this.buffer(ch, 0, 1);
        this.write(ch, 0, 1);
      }
      this.write(ppublicId);
      if (b)
//        this.buffer(ch, 0, 1);
        this.write(ch, 0, 1);
    }

    if ((psystemId = TextUtils.preprocessString(systemId)) != null) {
      if (ppublicId == null)
        this.write(SYSTEM, 0, SYSTEM.length);
      else {
        ch[0] = ' ';
//        this.buffer(ch, 0, 1);
        this.write(ch, 0, 1);
      }
      b = (psystemId.charAt(0) != '"');
      if (b) {
        ch[0] = '"';
        this.buffer(ch, 0, 1);
      }
      this.write(psystemId);
      if (b)
//        this.buffer(ch, 0, 1);
        this.write(ch, 0, 1);
    }

    this.m_encode++;
  }

  /**
   * End DTD declarations.
   * 
   * @see #startDTD
   */
  public void endDTD() {
    char[] ch;

    this.m_encode--;
    ch = this.m_buffer;
    if (this.m_state != EState.DTD) {
      if (this.m_state == EState.INTERNAL_DTD) {
        ch[0] = ']';
        this.write(ch, 0, 1);
      } else {
        this.m_encode++;
        this.m_lastSAXException = new SAXException();
        return;
      }
    }
    this.popState();
    ch[0] = '>';
    this.write(ch, 0, 1);
    this.decIndent();

    this.m_encode++;
  }

  /**
   * The begin of any dtd item.
   */
  private final void dtdItem() {
    char[] ch;
    if (this.m_state != EState.INTERNAL_DTD) {
      if (this.m_state == EState.DTD) {
        this.m_encode--;
        ch = this.m_buffer;
        ch[0] = ' ';
        ch[1] = '[';
        this.write(ch, 0, 2);
        this.m_state = EState.INTERNAL_DTD;
        this.m_encode++;
      } else {
        this.m_lastSAXException = new SAXException();
      }
    }
  }

  /**
   * Do an element type declaration.
   * 
   * @param name
   *          The element type name.
   * @param model
   *          The content model as a normalized string.
   */
  public final void elementDecl(final String name, final String model) {
    String pname, pmodel;
    char[] ch;

    if (((pname = TextUtils.preprocessString(name)) == null)
        || ((pmodel = TextUtils.preprocessString(model)) == null)) {
      this.m_lastSAXException = new SAXException();
      return;
    }

    this.dtdItem();

    if (this.isFormatting())
      this.ensureNewLine();

    this.m_encode--;
    this.write(ELEMENT, 0, ELEMENT.length);

    ch = this.m_buffer;
    ch[0] = ' ';
    this.write(pname);
    this.write(ch, 0, 1);
    this.write(pmodel);

    ch[0] = '>';
    this.write(ch, 0, 1);
    this.m_encode++;
  }

  /**
   * Do an attribute type declaration.
   * 
   * @param elementName
   *          The name of the associated element.
   * @param attributeName
   *          The name of the attribute.
   * @param type
   *          A string representing the attribute type.
   * @param mode
   *          A string representing the attribute defaulting mode
   *          ("#IMPLIED", "#REQUIRED", or "#FIXED") or null if none of
   *          these applies.
   * @param value
   *          A string representing the attribute's default value, or null
   *          if there is none.
   */
  public void attributeDecl(final String elementName,
      final String attributeName, final String type, final String mode,
      final String value) {
    String eName, aName, ptype, pmode, pvalue;
    char[] ch;

    if (((eName = TextUtils.preprocessString(elementName)) == null)
        || ((aName = TextUtils.preprocessString(attributeName)) == null)
        || ((ptype = TextUtils.preprocessString(type)) == null)) {
      this.m_lastSAXException = new SAXException();
      return;
    }

    this.dtdItem();
    this.m_encode--;
    if (this.isFormatting())
      this.ensureNewLine();

    this.write(ATTLIST, 0, ATTLIST.length);
    this.write(eName);
    ch = this.m_buffer;
    ch[0] = ' ';
    this.write(ch, 0, 1);
    this.write(aName);
    this.write(ch, 0, 1);
    this.write(ptype);

    if ((pmode = TextUtils.preprocessString(mode)) != null) {
      this.write(ch, 0, 1);
      this.write(pmode);
    }

    if ((pvalue = TextUtils.preprocessString(value)) != null) {
      this.write(ch, 0, 1);
      this.write(pvalue);
    }

    ch[0] = '>';
    this.write(ch, 0, 1);

    this.m_encode++;
  }

  /**
   * Do an internal entity declaration.
   * 
   * @param name
   *          The name of the entity. If it is a parameter entity, the name
   *          will begin with '%'.
   * @param value
   *          The replacement text of the entity.
   * @see #externalEntityDecl
   * @see org.xml.sax.DTDHandler#unparsedEntityDecl
   */
  public void internalEntityDecl(final String name, final String value) {
    boolean b;
    String pname, pvalue;
    char[] ch;

    if (((pname = TextUtils.preprocessString(name)) == null)
        || ((pvalue = TextUtils.preprocessString(value)) == null)) {
      this.m_lastSAXException = new SAXException();
      return;
    }

    this.dtdItem();
    this.m_encode--;
    if (this.isFormatting())
      this.ensureNewLine();

    this.write(ENTITY, 0, ENTITY.length);
    ch = this.m_buffer;
    this.write(pname);
    ch[0] = ' ';
    this.write(ch, 0, 1);
    b = (pvalue.charAt(0) != '"');
    if (b) {
      ch[0] = '"';
      this.write(ch, 0, 1);
    }
    this.write(pvalue);
    if (b)
      this.write(ch, 0, 1);

    ch[0] = '>';
    this.write(ch, 0, 1);

    this.m_encode++;
  }

  /**
   * Do a parsed external entity declaration.
   * 
   * @param name
   *          The name of the entity. If it is a parameter entity, the name
   *          will begin with '%'.
   * @param publicId
   *          The entity's public identifier, or null if none was given.
   * @param systemId
   *          The entity's system identifier.
   * @see #internalEntityDecl
   * @see org.xml.sax.DTDHandler#unparsedEntityDecl
   */
  public void externalEntityDecl(final String name, final String publicId,
      final String systemId) {
    boolean b;
    String pname, ppublicId, psystemId;
    char[] ch;

    pname = TextUtils.preprocessString(name);
    ppublicId = TextUtils.preprocessString(publicId);
    psystemId = TextUtils.preprocessString(systemId);

    if ((pname == null) || ((ppublicId == null) && (psystemId == null))) {
      this.m_lastSAXException = new SAXException();
      return;
    }

    this.dtdItem();
    this.m_encode--;
    if (this.isFormatting())
      this.ensureNewLine();

    this.write(ENTITY, 0, ENTITY.length);
    ch = this.m_buffer;
    ch[0] = ' ';
    this.write(ch, 0, 1);
    this.write(pname);

    if (ppublicId != null) {
      this.write(PUBLIC, 0, PUBLIC.length);
      b = (ppublicId.charAt(0) != '"');
      if (b) {
        ch[0] = '"';
        this.write(ch, 0, 1);
      }
      this.write(ppublicId);
      if (b)
        this.write(ch, 0, 1);
    }

    if (psystemId != null) {
      if (ppublicId == null)
        this.write(SYSTEM, 0, SYSTEM.length);
      else {
        ch[0] = ' ';
        this.write(ch, 0, 1);
      }
      b = (psystemId.charAt(0) != '"');
      if (b) {
        ch[0] = '"';
        this.write(ch, 0, 1);
      }
      this.write(psystemId);
      if (b)
        this.write(ch, 0, 1);

    }

    this.m_encode++;
  }

  /**
   * Begin the scope of a prefix-URI Namespace mapping.
   * 
   * @param prefix
   *          the Namespace prefix being declared. An empty string is used
   *          for the default element namespace, which has no prefix.
   * @param uri
   *          the Namespace URI the prefix is mapped to
   * @see #startElement(String)
   */
  public void startPrefixMapping(final String prefix, final String uri) {
    Element e;

    switch (this.m_state) {
    case NOTHING: {
      this.m_elementStack = e = new Element(this.m_elementStack);
      this.m_state = EState.MAPPING;
      break;
    }

    case MAPPING:
    case NEW_ELEMENT: {
      e = this.m_elementStack;
      break;
    }

    default: {
      this.m_lastSAXException = new SAXException();
      return;
    }
    }

    e.mapPrefix(prefix, uri);
  }

  /**
   * The simple method to start a new element.
   * 
   * @param namespaceUri
   *          The namespace-uri of the element. This might be
   *          <code>null</code>.
   * @param name
   *          The (qualified or unqualified) name of the new element.
   * @see #startElement(String, String, String, Attributes)
   * @see #startElement(String)
   */
  public void startElement(final String namespaceUri, final String name) {
    this.startElement(namespaceUri, null, name, null);
  }

  /**
   * The simple method to start a new element.
   * 
   * @param name
   *          The (qualified or unqualified) name of the new element.
   * @see #startElement(String, String, String, Attributes)
   * @see #startElement(String, String)
   */
  public void startElement(final String name) {
    this.startElement(null, null, name, null);
  }

  /**
   * The simple method to put attributes.
   * 
   * @param name
   *          The (qualified or unqualified) name of the new element.
   * @param value
   *          The value of the attribute.
   * @see #startElement(String, String, String, Attributes)
   * @see #attribute(String, String, String)
   */
  public void attribute(final String name, final String value) {
    this.attribute(null, name, value);
  }

  /**
   * The simple method to put attributes.
   * 
   * @param namespaceUri
   *          The namespace-uri of the element. This might be
   *          <code>null</code>.
   * @param name
   *          The (qualified or unqualified) name of the new element.
   * @param value
   *          The value of the attribute.
   * @see #startElement(String, String, String, Attributes)
   * @see #attribute(String, String)
   */
  public void attribute(final String namespaceUri, final String name,
      final String value) {
    Element e;

    switch (this.m_state) {
    case NOTHING: {
      this.m_elementStack = e = new Element(this.m_elementStack);
      this.m_state = EState.MAPPING;
      break;
    }

    case MAPPING:
    case NEW_ELEMENT: {
      e = this.m_elementStack;
      break;
    }

    default: {
      this.m_lastSAXException = new SAXException();
      return;
    }
    }

    e.attribute(namespaceUri, null, name, value);
  }

  /**
   * Start an element.
   * 
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
   * @see #endElement()
   * @see org.xml.sax.Attributes
   * @see #startElement(String, String)
   * @see #startElement(String)
   */
  public void startElement(final String uri, final String localName,
      final String qualifiedName, final Attributes atts) {
    Element e;
    int i;
    char[] ch;

    switch (this.m_state) {
    case NEW_ELEMENT: {
      this.m_state = EState.MAPPING;
      this.m_encode--;
      ch = this.m_buffer;
      ch[0] = '>';
      this.write(ch, 0, 1);
      this.m_encode++;
      this.m_elementStack = e = new Element(this.m_elementStack);
      break;
    }

    case NOTHING: {
      this.m_state = EState.MAPPING;
      this.m_elementStack = e = new Element(this.m_elementStack);
      break;
    }

    case MAPPING: {
      e = this.m_elementStack;
      break;
    }

    default: {
      this.m_lastSAXException = new SAXException();
      return;
    }
    }

    if ((atts != null) && ((i = atts.getLength()) > 0)) {
      this.m_state = EState.MAPPING;

      for (--i; i >= 0; i--) {
        e.attribute(atts.getURI(i), atts.getLocalName(i),
            atts.getQName(i), atts.getValue(i));
      }
    }

    e.start(uri, localName, qualifiedName);
    this.m_state = EState.NEW_ELEMENT;
  }

  /**
   * End an element.
   * 
   * @param uri
   *          ignored
   * @param localName
   *          ignored
   * @param qualifiedName
   *          ignored
   * @see #endElement()
   */
  public void endElement(final String uri, final String localName,
      final String qualifiedName) {
    this.endElement();
  }

  /**
   * The more convenient method to end an element.
   * 
   * @see #endElement(String, String, String)
   */
  public final void endElement() {
    if (this.m_elementStack != null)
      this.m_elementStack = this.m_elementStack.close();
    else
      this.m_lastSAXException = new SAXException();
  }

  /**
   * Demark somthing that's inside an element.
   */
  private final void elementItem() {
    char[] c;

    if (this.m_state == EState.NEW_ELEMENT) {
      this.m_encode--;
      c = this.m_buffer;
      c[0] = '>';
      this.write(c, 0, 1);
      this.m_state = EState.NOTHING;
      this.m_encode++;
    }
  }

  /**
   * Print some characters
   * 
   * @param ch
   *          the characters from the XML document
   * @param start
   *          the start position in the array
   * @param length
   *          the number of characters to read from the array
   * @see org.xml.sax.Locator
   */
  public void characters(final char ch[], final int start, final int length) {
    this.write(ch, start, length);
  }

  /**
   * print a comment
   * 
   * @param ch
   *          An array holding the characters in the comment.
   * @param start
   *          The starting position in the array.
   * @param length
   *          The number of characters to use from the array.
   */
  public void comment(final char ch[], final int start, final int length) {

    this.m_encode--;
    this.elementItem();
    this.write(COMMENT_START, 0, COMMENT_START.length);
    this.m_encode++;

    this.write(ch, start, length);

    this.m_encode--;
    this.write(COMMENT_END, 0, COMMENT_END.length);
    this.m_encode++;
  }

  /**
   * print a comment
   * 
   * @param text
   *          the comment
   */
  public void comment(final String text) {

    this.m_encode--;
    this.elementItem();
    this.write(COMMENT_START, 0, COMMENT_START.length);
    this.m_encode++;

    this.write(text);

    this.m_encode--;
    this.write(COMMENT_END, 0, COMMENT_END.length);
    this.m_encode++;
  }

  /**
   * Print a processing instruction.
   * 
   * @param target
   *          the processing instruction target
   * @param data
   *          the processing instruction data, or null if none was
   *          supplied. The data does not include any whitespace separating
   *          it from the target
   */
  public void processingInstruction(final String target, final String data) {
    String ptarget, pdata;
    char[] ch;

    if ((ptarget = TextUtils.preprocessString(target)) == null) {
      this.m_lastSAXException = new SAXException();
      return;
    }

    this.elementItem();
    this.m_encode--;
    if (this.isFormatting())
      this.ensureNewLine();

    ch = this.m_buffer;
    ch[0] = '<';
    ch[1] = '?';
    this.write(ch, 0, 2);

    this.write(ptarget);
    if ((pdata = TextUtils.preprocessString(data)) != null) {
      ch[0] = ' ';
      this.write(ch, 0, 1);
      this.write(pdata);
    }

    ch[0] = '?';
    ch[1] = '>';
    this.write(ch, 0, 2);
    this.m_encode++;
  }

  /**
   * Start a CDATA section.
   * 
   * @see #endCDATA
   */
  public void startCDATA() {
    switch (this.m_state) {
    case DTD:
    case INTERNAL_DTD:
    case MAPPING:
    case NOTHING: {
      this.pushState();
      break;
    }
    case NEW_ELEMENT: {
      this.elementItem();
      this.pushState();
      break;
    }

    default: {
      this.m_lastSAXException = new SAXException();
      return;
    }
    }

    this.m_encode--;
    this.write(CDATA_START, 0, CDATA_START.length);
    this.m_state = EState.CDATA;
  }

  /**
   * End a CDATA section.
   * 
   * @see #startCDATA
   */
  public void endCDATA() {
    if (this.m_state != EState.CDATA) {
      this.m_lastSAXException = new SAXException();
      return;
    }

    this.write(CDATA_END, 0, CDATA_END.length);
    this.m_encode++;
    this.popState();
  }

  /**
   * End the scope of a prefix-URI mapping.
   * 
   * @param prefix
   *          the prefix that was being mapped. This is the empty string
   *          when a default mapping scope ends.
   * @see #startPrefixMapping
   */
  public void endPrefixMapping(final String prefix) {
    // does nothing
  }

  /**
   * Receive notification of a skipped entity.
   * 
   * @param name
   *          ignored
   */
  public void skippedEntity(final String name) {
    // does nothing
  }

  // /
  // / ErrorHandler
  // /
  /**
   * Receive notification of a warning.
   * 
   * @param exception
   *          The warning information encapsulated in a SAX parse
   *          exception.
   * @see org.xml.sax.SAXParseException
   */
  public void warning(final SAXParseException exception) {
    if (exception != null)
      this.m_lastSAXException = exception;
  }

  /**
   * Receive notification of a recoverable error.
   * 
   * @param exception
   *          The error information encapsulated in a SAX parse exception.
   */
  public void error(final SAXParseException exception) {
    if (exception != null)
      this.m_lastSAXException = exception;
  }

  /**
   * Receive notification of a non-recoverable error.
   * 
   * @param exception
   *          The error information encapsulated in a SAX parse exception.
   */
  public void fatalError(final SAXParseException exception) {
    if (exception != null)
      this.m_lastSAXException = exception;
  }

  /**
   * Allows applications to provide an external subset for documents that
   * don't explicitly define one. Documents with DOCTYPE declarations that
   * omit an external subset can thus augment the declarations available
   * for validation, entity processing, and attribute processing
   * (normalization, defaulting, and reporting types including ID). This
   * augmentation is reported through the
   * {@link LexicalHandler#startDTD startDTD()} method as if the document
   * text had originally included the external subset; this callback is
   * made before any internal subset data or errors are reported.
   * 
   * @param name
   *          Identifies the document root element. This name comes from a
   *          DOCTYPE declaration (where available) or from the actual root
   *          element.
   * @param baseUri
   *          The document's base URI, serving as an additional hint for
   *          selecting the external subset. This is always an absolute
   *          URI, unless it is null because the XMLReader was given an
   *          InputSource without one.
   * @return An InputSource object describing the new external subset to be
   *         used by the parser, or null to indicate that no external
   *         subset is provided.
   */
  public InputSource getExternalSubset(final String name,
      final String baseUri) {
    return null;
  }

  /**
   * Allows applications to map references to external entities into input
   * sources, or tell the parser it should use conventional URI resolution.
   * This method is only called for external entities which have been
   * properly declared. This method provides more flexibility than the
   * {@link EntityResolver} interface, supporting implementations of more
   * complex catalogue schemes such as the one defined by the <a href=
   * "http://www.oasis-open.org/committees/entity/spec-2001-08-06.html"
   * >OASIS XML Catalogs</a> specification.
   * 
   * @param name
   *          Identifies the external entity being resolved. Either "[dtd]"
   *          for the external subset, or a name starting with "%" to
   *          indicate a parameter entity, or else the name of a general
   *          entity. This is never null when invoked by a SAX2 parser.
   * @param publicId
   *          The public identifier of the external entity being referenced
   *          (normalized as required by the XML specification), or null if
   *          none was supplied.
   * @param baseUri
   *          The URI with respect to which relative systemIDs are
   *          interpreted. This is always an absolute URI, unless it is
   *          null (likely because the XMLReader was given an InputSource
   *          without one). This URI is defined by the XML specification to
   *          be the one associated with the "&lt;" starting the relevant
   *          declaration.
   * @param systemId
   *          The system identifier of the external entity being
   *          referenced; either a relative or absolute URI. This is never
   *          null when invoked by a SAX2 parser; only declared entities,
   *          and any external subset, are resolved by such parsers.
   * @return An InputSource object describing the new input source to be
   *         used by the parser. Returning null directs the parser to
   *         resolve the system ID against the base URI and open a
   *         connection to resulting URI.
   */
  public InputSource resolveEntity(final String name,
      final String publicId, final String baseUri, final String systemId) {
    return null;
  }

  /**
   * Receive notification of ignorable whitespace in element content.
   * 
   * @param ch
   *          the characters from the XML document
   * @param start
   *          the start position in the array
   * @param length
   *          the number of characters to read from the array
   * @see #characters
   */
  public void ignorableWhitespace(final char ch[], final int start,
      final int length) {
    if (this.isFormatting())
      this.write(ch, start, length);
  }

  // /
  // / DTDHandler
  // /
  /**
   * Receive notification of a notation declaration event.
   * 
   * @param name
   *          The notation name.
   * @param publicId
   *          The notation's public identifier, or null if none was given.
   * @param systemId
   *          The notation's system identifier, or null if none was given.
   * @see #unparsedEntityDecl
   * @see org.xml.sax.Attributes
   */
  public void notationDecl(final String name, final String publicId,
      final String systemId) {
    // does nothing
  }

  // /
  // / EntityResolver
  // /

  /**
   * Allow the application to resolve external entities.
   * 
   * @param publicId
   *          The public identifier of the external entity being
   *          referenced, or null if none was supplied.
   * @param systemId
   *          The system identifier of the external entity being
   *          referenced.
   * @return An InputSource object describing the new input source, or null
   *         to request that the parser open a regular URI connection to
   *         the system identifier.
   * @see org.xml.sax.InputSource
   */
  public InputSource resolveEntity(final String publicId,
      final String systemId) {
    return null;
  }

  /**
   * Receive notification of an unparsed entity declaration event.
   * 
   * @param name
   *          The unparsed entity's name.
   * @param publicId
   *          The entity's public identifier, or null if none was given.
   * @param systemId
   *          The entity's system identifier.
   * @param notation_name
   *          The name of the associated notation.
   * @see #notationDecl
   * @see org.xml.sax.Attributes
   */
  public void unparsedEntityDecl(final String name, final String publicId,
      final String systemId, final String notation_name) {
    // does nothing
  }

  // /
  // / ContentHandler
  // /
  /**
   * Receive an object for locating the origin of SAX document events.
   * 
   * @param locator
   *          an object that can return the location of any SAX document
   *          event
   * @see org.xml.sax.Locator
   */
  public void setDocumentLocator(final Locator locator) {
    // does nothing
  }

  /**
   * Report the beginning of some internal and external XML entities.
   * 
   * @param name
   *          The name of the entity. If it is a parameter entity, the name
   *          will begin with '%', and if it is the external DTD subset, it
   *          will be "[dtd]".
   * @see #endEntity
   * @see org.xml.sax.ext.DeclHandler#internalEntityDecl
   * @see org.xml.sax.ext.DeclHandler#externalEntityDecl
   */
  public void startEntity(final String name) {
    // does nothing
  }

  /**
   * Report the end of an entity.
   * 
   * @param name
   *          The name of the entity that is ending.
   * @see #startEntity
   */
  public void endEntity(final String name) {
    // does nothing
  }

  /**
   * the possible states of the sax writer
   * 
   * @author Thomas Weise
   */
  private static enum EState {
    /**
     * We've currently nothing to do.
     */
    NOTHING,

    /**
     * Currently we're prefix-mapping.
     */
    MAPPING,

    /**
     * Currently we're inside a fresh element.
     */
    NEW_ELEMENT,

    /**
     * Currently we're inside dtd.
     */
    DTD,

    /**
     * Currently we're inside dtd.
     */
    INTERNAL_DTD,
    /**
     * Currently we're inside dtd.
     */
    CDATA;
  }

  /**
   * Dispose this reference counted writer.
   * 
   * @throws IOException
   *           If the underlying was closed and caused an IOException.
   */
  @Override
  protected void dispose() throws IOException {
    this.endDocument();
    super.dispose();
  }

  /**
   * Write a string builder to the output and clear it afterwards.
   * 
   * @param sb
   *          the string builder
   */
  final void flushSB(final StringBuilder sb) {
    this.flushSb(sb);
  }

  /**
   * Append an attribute to a string builder.
   * 
   * @param s
   *          the value.
   * @param sb
   *          the string builder
   */
  static final void appendAttribute(final StringBuilder sb, final String s) {
    int i, l;
    char ch;

    if (s != null) {
      l = s.length();
      if (l > 0) {
        for (i = 0; i < l; i++) {
          ch = s.charAt(i);
          if ((ch <= 32) || (ch > 127) || (ch == '<') || (ch == '>')
              || (ch == '&') || (ch == '#') || (ch == '/') || (ch == '"')
              || (ch == '\'')) {
            sb.append('&');
            sb.append('#');
            sb.append((int) ch);
            sb.append(';');
          } else
            sb.append(ch);
        }
      }
    }
  }

  /**
   * This class represents the internal element stack.
   * 
   * @author Thomas Weise
   */
  private final class Element {
    /**
     * The element that contains this one.
     */
    private final Element m_owner;

    /**
     * The namespaces managed by this element.
     */
    private Map<String, String> m_namespaces;

    // /**
    // * The string builder with initial attributes.
    // */
    // private StringBuilder m_attrs;

    /**
     * The base-namespace.
     */
    private String m_baseNamespace;

    /**
     * The qualified name of the tag.
     */
    private String m_qualifiedName;

    /**
     * Create a new element.
     * 
     * @param owner
     *          The element that owns this one.
     */
    Element(final Element owner) {
      super();
      this.m_owner = owner;
    }

    /**
     * Begin a prefix-mapping.
     * 
     * @param prefix
     *          The prefix.
     * @param uri
     *          The uri to be mapped to it.
     */
    final void mapPrefix(final String prefix, final String uri) {
      Map<String, String> m;
      StringBuilder sb;
      Element e;
      String puri, pprefix;
      SAXWriter w;
      char[] ch;

      if ((puri = TextUtils.preprocessString(uri)) == null)
        return;

      if ((pprefix = TextUtils.preprocessString(prefix)) == null) {
        for (e = this; e != null; e = e.m_owner) {
          if (e.m_baseNamespace != null) {
            if (e.m_baseNamespace.equals(puri)) {
              this.m_baseNamespace = puri;
              return;
            }
            break;
          }
        }

        this.m_baseNamespace = puri;
      } else {
        for (e = this; e != null; e = e.m_owner) {
          if ((e.m_namespaces != null)
              && (puri.equals(e.m_namespaces.get(pprefix)))) {
            return;
          }
        }

        m = this.m_namespaces;
        if (m == null) {
          this.m_namespaces = m = CollectionUtils.createMap();
        }

        m.put(pprefix, puri);
        m.put(puri, pprefix);
      }

      switch (SAXWriter.this.m_state) {
      case MAPPING: {
        sb = SAXWriter.this.m_attrs;
        // this.m_attrs;
        // if (sb == null) {
        // this.m_attrs = sb = new StringBuilder();
        // }

        sb.append(' ');

        if (pprefix != null) {
          sb.append(XML.XMLNSQ);
          sb.append(pprefix);
        } else
          sb.append(XML.XMLNS);

        sb.append("=\""); //$NON-NLS-1$
        // sb.append(puri);
        appendAttribute(sb, puri);
        sb.append('"');
        break;
      }

      case NEW_ELEMENT: {
        w = SAXWriter.this;
        w.m_encode--;
        ch = w.m_buffer;
        ch[0] = ' ';
        w.write(ch, 0, 1);

        if (pprefix != null) {
          w.write(XML.XMLNSQ);
          w.write(pprefix);
        } else
          w.write(XML.XMLNS);

        ch[0] = '=';
        ch[1] = '"';

        w.write(ch, 0, 2);
        sb = w.m_attrs;
        appendAttribute(sb, puri);
        w.flushSB(sb);
        // w.write(puri);
        w.write(ch, 1, 1);
        w.m_encode++;

        break;
      }

      default:
        SAXWriter.this.m_lastSAXException = new SAXException();
      }
    }

    /**
     * Get the name of a new thing.
     * 
     * @param uri
     *          the Namespace URI, or the empty string if the element has
     *          no Namespace URI or if Namespace processing is not being
     *          performed
     * @param localName
     *          the local name (without prefix), or the empty string if
     *          Namespace processing is not being performed
     * @param qualifiedName
     *          the qualified name (with prefix), or the empty string if
     *          qualified names are not available.
     * @return The name of a new thing.
     */
    final String name(final String uri, final String localName,
        final String qualifiedName) {
      Element e;
      String plocalName, pqualifiedName, puri, pfx;
      int i;
      boolean map;

      plocalName = TextUtils.preprocessString(localName);
      pfx = null;

      if ((pqualifiedName = TextUtils.preprocessString(qualifiedName)) != null) {
        i = pqualifiedName.indexOf(XML.NAMESPACE_SEPARATOR);
        if (i > 0) {
          pfx = pqualifiedName.substring(0, i);

          if (plocalName == null) {
            plocalName = pqualifiedName.substring(i + 1);
          }
        } else {
          if (plocalName == null) {
            plocalName = pqualifiedName;
          }
        }
      }

      if ((puri = TextUtils.preprocessString(uri)) == null) {
        return plocalName;
      }

      for (e = this; e != null; e = e.m_owner) {
        pqualifiedName = e.m_baseNamespace;
        if (pqualifiedName != null) {
          if (pqualifiedName.equals(puri)) {
            return plocalName;
          }
          break;
        }
      }

      map = true;

      main: {
        if ((pfx == null)
            && ((this.m_baseNamespace != null) || (this.m_owner != null))

        /* && (this.m_base_namespace != null) */) {
          for (e = this; e != null; e = e.m_owner) {
            if (e.m_namespaces != null) {
              pfx = e.m_namespaces.get(puri);
              if (pfx != null) {
                map = false;
                break main;
              }
            }
          }

          loopy: for (;;) {
            pfx = "ns" + (SAXWriter.this.m_nsct++); //$NON-NLS-1$
            for (e = this; e != null; e = e.m_owner) {
              if (e.m_namespaces != null) {
                if (e.m_namespaces.get(pfx) != null) {
                  continue loopy;
                }
              }
            }

            break main;
          }
        }
      }

      if (map) {
        // // <EXPERIMENTAL>
        // if ((pfx == null) && (this.m_baseNamespace == null)
        // && (this.m_owner == null)) {
        // this.m_baseNamespace = puri;
        // } else
        // // </EXPERIMENTAL>
        this.mapPrefix(pfx, puri);
      }

      if (pfx != null) {
        return (pfx + XML.NAMESPACE_SEPARATOR + plocalName);
      }

      return plocalName;
    }

    /**
     * Insert an attribute.
     * 
     * @param uri
     *          the Namespace URI, or the empty string if the element has
     *          no Namespace URI or if Namespace processing is not being
     *          performed
     * @param localName
     *          the local name (without prefix), or the empty string if
     *          Namespace processing is not being performed
     * @param qualifiedName
     *          the qualified name (with prefix), or the empty string if
     *          qualified names are not available
     * @param value
     *          The value of the attribute.
     */
    final void attribute(final String uri, final String localName,
        final String qualifiedName, final String value) {
      String puri;
      StringBuilder sb;
      SAXWriter w;
      char[] ch;

      puri = this.name(uri, localName, qualifiedName);

      switch (SAXWriter.this.m_state) {
      case MAPPING: {
        sb = SAXWriter.this.m_attrs;
        // sb = this.m_attrs;
        // if (sb == null) {
        // this.m_attrs = sb = new StringBuilder();
        // }

        sb.append(' ');
        sb.append(puri);
        if (value != null) {
          sb.append("=\""); //$NON-NLS-1$
          // sb.append(value);
          appendAttribute(sb, value);
          sb.append('"');
        }

        break;
      }

      case NEW_ELEMENT: {
        w = SAXWriter.this;
        w.m_encode--;

        ch = w.m_buffer;
        ch[0] = ' ';

        w.write(ch, 0, 1);
        w.write(puri);
        if (value != null) {
          ch[0] = '=';
          ch[1] = '"';
          w.write(ch, 0, 2);
          sb = w.m_attrs;
          appendAttribute(sb, value);
          w.flushSB(sb);
          // w.write(value);
          w.write(ch, 1, 1);
        }

        w.m_encode++;
        break;
      }

      default:
        SAXWriter.this.m_lastSAXException = new SAXException();
      }
    }

    /**
     * Start a new fucking element.
     * 
     * @param uri
     *          the Namespace URI, or the empty string if the element has
     *          no Namespace URI or if Namespace processing is not being
     *          performed
     * @param localName
     *          the local name (without prefix), or the empty string if
     *          Namespace processing is not being performed
     * @param qualifiedName
     *          the qualified name (with prefix), or the empty string if
     *          qualified names are not available
     * @see org.xml.sax.Attributes
     */
    final void start(final String uri, final String localName,
        final String qualifiedName) {
      StringBuilder sb;
      SAXWriter w;
      char[] ch;

      w = SAXWriter.this;
      w.m_encode--;
      if (w.isFormatting())
        w.ensureNewLine();

      ch = w.m_buffer;
      ch[0] = '<';
      w.write(ch, 0, 1);
      w.write(this.m_qualifiedName = this.name(uri, localName,
          qualifiedName));

      // sb = this.m_attrs;
      // if (sb != null) {
      // w.write(sb.toString());
      // this.m_attrs = null;
      // }
      sb = w.m_attrs;
      if (sb.length() > 0)
        w.flushSB(sb);

      w.incIndent();
      w.m_encode++;
    }

    /**
     * Close this stack element.
     * 
     * @return The owning stack element.
     */
    final Element close() {
      SAXWriter w;
      char[] ch;

      w = SAXWriter.this;
      w.m_encode--;
      w.decIndent();
      ch = w.m_buffer;

      switch (SAXWriter.this.m_state) {
      case NOTHING: {
        ch[0] = '<';
        ch[1] = '/';
        w.write(ch, 0, 2);
        w.write(this.m_qualifiedName);
        ch[0] = '>';
        w.write(ch, 0, 1);
        // w.write("</" + this.m_qualifiedName + '>'); //$NON-NLS-1$
        break;
      }
      case NEW_ELEMENT: {
        ch[0] = '/';
        ch[1] = '>';
        w.write(ch, 0, 2);
        break;
      }

      default: {
        SAXWriter.this.m_lastSAXException = new SAXException();
      }
      }

      if (w.isFormatting())
        w.ensureNewLine();
      w.m_encode++;

      SAXWriter.this.m_state = EState.NOTHING;

      return this.m_owner;
    }

  }
}
