/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2007-01-01
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.xml.sax.SAXReader.java
 * Last modification: 2007-01-01
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
import java.util.Map;
import java.util.Set;

import org.sfc.collections.CollectionUtils;
import org.sfc.xml.XML;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.EntityResolver2;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.ext.Locator2;

/**
 * The sax reader will serve as base class for converters that generate sax
 * events from non-sax source.
 * 
 * @author Thomas Weise
 */
public class SAXReader extends SAXImplementor implements XMLReader,
    Locator, XMLFilter, Locator2 {

  /**
   * The map with the reader's properties.
   */
  private Map<String, Object> m_properties;

  /**
   * The map with the reader's features.
   */
  private final Set<String> m_features;

  /**
   * The entity resolver of this sax reader.
   */
  private EntityResolver m_entityResolver;

  /**
   * The dtd handler of this sax reader.
   */
  private DTDHandler m_dtdHandler;

  /**
   * The content handler of this sax reader.
   */
  private ContentHandler m_contentHandler;

  /**
   * The error handler of this sax reader.
   */
  private ErrorHandler m_errorHandler;

  /**
   * The locator to be used internally.
   */
  private Locator m_locator;

  /**
   * The parent xml filter to receive all events from.
   */
  private XMLReader m_parent;

  /**
   * The internal lexical handler.
   */
  private LexicalHandler m_lexicalHandler;

  /**
   * The dtd declaration handler.
   */
  private DeclHandler m_declHandler;

  /**
   * Create a new <code>SAXReader</code>.
   */
  public SAXReader() {
    super();
    this.m_properties = null;
    this.m_features = CollectionUtils.createSet();

    this.m_features.add(XML.FEATURE_NAMESPACES);
    this.m_features.add(XML.FEATURE_NAMESPACE_PREFIXES);
    this.m_features.add(XML.FEATURE_ENTITY_RESOLVER_2);
    this.m_features.add(XML.FEATURE_LOCATOR_2);

    this.m_contentHandler = null;
    this.m_dtdHandler = null;
    this.m_entityResolver = null;
    this.m_errorHandler = null;
    this.m_locator = null;
    this.m_parent = null;
    this.m_declHandler = null;
    this.m_lexicalHandler = null;
  }

  /**
   * Set the last exception.
   * 
   * @param e
   *          new the last exception
   */
  @Override
  protected void setLastException(final Exception e) {
    ErrorHandler h;
    if (e != null) {
      super.setLastException(e);
      if (e instanceof SAXParseException) {
        h = this.m_errorHandler;
        if ((h != null) && (h != this))
          try {
            h.error((SAXParseException) e);
          } catch (SAXException ee) {
            super.setLastException(ee);
          }
      }
    }
  }

  // /
  // / Configuration
  // /

  /**
   * Look up the value of a feature flag.
   * <p>
   * The feature name is any fully-qualified URI. It is possible for an
   * XMLReader to recognize a feature name but temporarily be unable to
   * return its value. Some feature values may be available only in
   * specific contexts, such as before, during, or after a parse. Also,
   * some feature values may not be programmatically accessible. (In the
   * case of an adapter for SAX1, there is no implementation-independent
   * way to expose whether the underlying parser is performing validation,
   * expanding external entities, and so forth.)
   * </p>
   * 
   * @param name
   *          The feature name, which is a fully-qualified URI.
   * @return The current value of the feature (true or false).
   * @see #setFeature
   */
  public boolean getFeature(final String name) {
    if (this.m_parent != null) {
      try {
        return this.m_parent.getFeature(name);
      } catch (SAXNotRecognizedException e) {
        this.setLastException(e);
      } catch (SAXNotSupportedException e) {
        this.setLastException(e);
      }
    }
    return this.m_features.contains(name);
  }

  /**
   * Set the value of a feature flag.
   * <p>
   * The feature name is any fully-qualified URI. It is possible for an
   * XMLReader to expose a feature value but to be unable to change the
   * current value. Some feature values may be immutable or mutable only in
   * specific contexts, such as before, during, or after a parse.
   * </p>
   * 
   * @param name
   *          The feature name, which is a fully-qualified URI.
   * @param value
   *          The requested value of the feature (true or false).
   * @see #getFeature
   */
  public void setFeature(final String name, final boolean value) {
    if (value)
      this.m_features.add(name);
    else
      this.m_features.remove(name);

    if (this.m_parent != null) {
      try {
        this.m_parent.setFeature(name, value);
      } catch (SAXNotRecognizedException e) {
        this.setLastException(e);
      } catch (SAXNotSupportedException e) {
        this.setLastException(e);
      }
    }
  }

  /**
   * Look up the value of a property.
   * <p>
   * The property name is any fully-qualified URI. It is possible for an
   * XMLReader to recognize a property name but temporarily be unable to
   * return its value. Some property values may be available only in
   * specific contexts, such as before, during, or after a parse.
   * </p>
   * <p>
   * XMLReaders are not required to recognize any specific property names,
   * though an initial core set is documented for SAX2.
   * </p>
   * <p>
   * Implementors are free (and encouraged) to invent their own properties,
   * using names built on their own URIs.
   * </p>
   * 
   * @param name
   *          The property name, which is a fully-qualified URI.
   * @return The current value of the property.
   * @see #setProperty
   */
  public Object getProperty(final String name) {
    if (XML.PROPERTY_LEXICAL_HANDLER.equals(name)) {
      return this.m_lexicalHandler;
    }
    if (XML.PROPERTY_DECL_HANDLER.equals(name)) {
      return this.m_declHandler;
    }

    if (this.m_parent != null) {
      try {
        return this.m_parent.getProperty(name);
      } catch (SAXNotRecognizedException e) {
        this.setLastException(e);
      } catch (SAXNotSupportedException e) {
        this.setLastException(e);
      }

    }
    if (this.m_properties != null)
      return this.m_properties.get(name);
    return null;
  }

  /**
   * Set the value of a property.
   * <p>
   * The property name is any fully-qualified URI. It is possible for an
   * XMLReader to recognize a property name but to be unable to change the
   * current value. Some property values may be immutable or mutable only
   * in specific contexts, such as before, during, or after a parse.
   * </p>
   * <p>
   * XMLReaders are not required to recognize setting any specific property
   * names, though a core set is defined by SAX2.
   * </p>
   * <p>
   * This method is also the standard mechanism for setting extended
   * handlers.
   * </p>
   * 
   * @param name
   *          The property name, which is a fully-qualified URI.
   * @param value
   *          The requested value for the property.
   */
  public void setProperty(final String name, final Object value) {
    if (XML.PROPERTY_LEXICAL_HANDLER.equals(name)) {
      this.setLexicalHandler((LexicalHandler) value);
    } else {
      if (XML.PROPERTY_DECL_HANDLER.equals(name)) {
        this.setDeclHandler((DeclHandler) value);
      } else {
        if (this.m_properties == null) {
          this.m_properties = CollectionUtils.createMap();
        }
        this.m_properties.put(name, value);
        if (this.m_parent != null) {
          try {
            this.m_parent.setProperty(name, value);
          } catch (SAXNotRecognizedException e) {
            this.setLastException(e);
          } catch (SAXNotSupportedException e) {
            this.setLastException(e);
          }
        }
      }
    }
  }

  /**
   * Set the parent reader.
   * <p>
   * This method allows the application to link the filter to a parent
   * reader (which may be another filter). The argument may not be null.
   * </p>
   * 
   * @param parent
   *          The parent reader.
   */
  public void setParent(final XMLReader parent) {
    if (this.m_parent != null) {
      XML.uninstallFrom(this.m_parent, this);
    }

    this.m_parent = parent;

    if (parent != null) {
      XML.installTo(parent, this);

      try {
        parent.setFeature(XML.FEATURE_ENTITY_RESOLVER_2, this.m_features
            .contains(XML.FEATURE_ENTITY_RESOLVER_2));
      } catch (Exception t) {
        this.setLastException(t);
      }

      try {
        parent.setFeature(XML.FEATURE_LOCATOR_2, this.m_features
            .contains(XML.FEATURE_LOCATOR_2));
      } catch (Exception t) {
        this.setLastException(t);
      }
    }
  }

  /**
   * Get the parent reader.
   * <p>
   * This method allows the application to query the parent reader (which
   * may be another filter). It is generally a bad idea to perform any
   * operations on the parent reader directly: they should all pass through
   * this filter.
   * </p>
   * 
   * @return The parent filter, or null if none has been set.
   */
  public XMLReader getParent() {
    return this.m_parent;
  }

  // /
  // / Handlers
  // /

  /**
   * Allow an application to register an entity resolver.
   * <p>
   * If the application does not register an entity resolver, the XMLReader
   * will perform its own default resolution.
   * </p>
   * <p>
   * Applications may register a new or different resolver in the middle of
   * a parse, and the SAX parser must begin using the new resolver
   * immediately.
   * </p>
   * 
   * @param resolver
   *          The entity resolver.
   * @see #getEntityResolver
   */
  public void setEntityResolver(final EntityResolver resolver) {
    this.m_entityResolver = resolver;
  }

  /**
   * Return the current entity resolver.
   * 
   * @return The current entity resolver, or null if none has been
   *         registered.
   * @see #setEntityResolver
   */
  public EntityResolver getEntityResolver() {
    return this.m_entityResolver;
  }

  /**
   * Allow an application to register a DTD event handler.
   * <p>
   * If the application does not register a DTD handler, all DTD events
   * reported by the SAX parser will be silently ignored.
   * </p>
   * <p>
   * Applications may register a new or different handler in the middle of
   * a parse, and the SAX parser must begin using the new handler
   * immediately.
   * </p>
   * 
   * @param handler
   *          The DTD handler.
   * @see #getDTDHandler
   */
  public void setDTDHandler(final DTDHandler handler) {
    this.m_dtdHandler = handler;
  }

  /**
   * Return the current DTD handler.
   * 
   * @return The current DTD handler, or null if none has been registered.
   * @see #setDTDHandler
   */
  public DTDHandler getDTDHandler() {
    return this.m_dtdHandler;
  }

  /**
   * Allow an application to register a content event handler.
   * <p>
   * If the application does not register a content handler, all content
   * events reported by the SAX parser will be silently ignored.
   * </p>
   * <p>
   * Applications may register a new or different handler in the middle of
   * a parse, and the SAX parser must begin using the new handler
   * immediately.
   * </p>
   * 
   * @param handler
   *          The content handler.
   * @see #getContentHandler
   */
  public void setContentHandler(final ContentHandler handler) {
    this.m_contentHandler = handler;
    if (handler != null) {
      handler.setDocumentLocator(this);
    }
  }

  /**
   * Return the current content handler.
   * 
   * @return The current content handler, or null if none has been
   *         registered.
   * @see #setContentHandler
   */
  public ContentHandler getContentHandler() {
    return this.m_contentHandler;
  }

  /**
   * Allow an application to register an error event handler.
   * <p>
   * If the application does not register an error handler, all error
   * events reported by the SAX parser will be silently ignored; however,
   * normal processing may not continue. It is highly recommended that all
   * SAX applications implement an error handler to avoid unexpected bugs.
   * </p>
   * <p>
   * Applications may register a new or different handler in the middle of
   * a parse, and the SAX parser must begin using the new handler
   * immediately.
   * </p>
   * 
   * @param handler
   *          The error handler.
   * @see #getErrorHandler
   */
  public void setErrorHandler(final ErrorHandler handler) {
    this.m_errorHandler = handler;
  }

  /**
   * Return the current error handler.
   * 
   * @return The current error handler, or null if none has been
   *         registered.
   * @see #setErrorHandler
   */
  public ErrorHandler getErrorHandler() {
    return this.m_errorHandler;
  }

  /**
   * Sets the lexical handler of this sax reader.
   * 
   * @param handler
   *          The lexical handler to be used.
   */
  public void setLexicalHandler(final LexicalHandler handler) {
    this.m_lexicalHandler = handler;
  }

  /**
   * Returns the lexical handler of this sax reader.
   * 
   * @return The <code>LexicalHandler</code> events are delegated to.
   */
  public LexicalHandler getLexicalHandler() {
    return this.m_lexicalHandler;
  }

  /**
   * Sets the decl handler of this sax reader.
   * 
   * @param handler
   *          The decl handler to be used.
   */
  public void setDeclHandler(final DeclHandler handler) {
    this.m_declHandler = handler;
  }

  /**
   * Returns the decl handler of this sax reader.
   * 
   * @return The <code>DeclHandler</code> events are delegated to.
   */
  public DeclHandler getDeclHandler() {
    return this.m_declHandler;
  }

  // /
  // / Parsing.
  // /

  /**
   * Parse an XML document.
   * <p>
   * The application can use this method to instruct the XML reader to
   * begin parsing an XML document from any valid input source (a character
   * stream, a byte stream, or a URI).
   * </p>
   * <p>
   * Applications may not invoke this method while a parse is in progress
   * (they should create a new XMLReader instead for each nested XML
   * document). Once a parse is complete, an application may reuse the same
   * XMLReader object, possibly with a different input source.
   * Configuration of the XMLReader object (such as handler bindings and
   * values established for feature flags and properties) is unchanged by
   * completion of a parse, unless the definition of that aspect of the
   * configuration explicitly specifies other behavior. (For example,
   * feature flags or properties exposing characteristics of the document
   * being parsed.)
   * </p>
   * <p>
   * During the parse, the XMLReader will provide information about the XML
   * document through the registered event handlers.
   * </p>
   * <p>
   * This method is synchronous: it will not return until parsing has
   * ended. If a client application wants to terminate parsing early, it
   * should throw an exception.
   * </p>
   * 
   * @param input
   *          The input source for the top-level of the XML document.
   * @see org.xml.sax.InputSource
   * @see #parse(java.lang.String)
   * @see #setEntityResolver
   * @see #setDTDHandler
   * @see #setContentHandler
   * @see #setErrorHandler
   */
  public void parse(final InputSource input) {
    try {
      if (this.m_parent != null) {
        this.m_parent.parse(input);
      } else {
        this.internalParse(input);
      }
    } catch (IOException e) {
      this.setLastException(e);
    } catch (SAXException e) {
      this.setLastException(e);
    }
  }

  /**
   * This internal parse method will be used to parse some stuff if no
   * parent is attached to this sax reader and we process our own data
   * rather than just receiving events from somewhere else.
   * 
   * @param input
   *          The input source where to get the data from.
   * @exception org.xml.sax.SAXException
   *              Any SAX exception, possibly wrapping another exception.
   * @exception java.io.IOException
   *              An IO exception from the parser, possibly from a byte
   *              stream or character stream supplied by the application.
   */
  protected void internalParse(final InputSource input)
      throws IOException, SAXException {
    //
  }

  /**
   * Parse an XML document from a system identifier (URI).
   * <p>
   * This method is a shortcut for the common case of reading a document
   * from a system identifier. It is the exact equivalent of the following:
   * </p>
   * 
   * <pre>
   * parse(new InputSource(systemId));
   * </pre>
   * 
   * <p>
   * If the system identifier is a URL, it must be fully resolved by the
   * application before it is passed to the parser.
   * </p>
   * 
   * @param systemId
   *          The system identifier (URI).
   * @see #parse(org.xml.sax.InputSource)
   */
  public void parse(final String systemId) {
    this.parse(new InputSource(systemId));
  }

  // /
  // / EntityResolver
  // /

  /**
   * Allow the application to resolve external entities.
   * <p>
   * The parser will call this method before opening any external entity
   * except the top-level document entity. Such entities include the
   * external DTD subset and external parameter entities referenced within
   * the DTD (in either case, only if the parser reads external parameter
   * entities), and external general entities referenced within the
   * document element (if the parser reads external general entities). The
   * application may request that the parser locate the entity itself, that
   * it use an alternative URI, or that it use data provided by the
   * application (as a character or byte input stream).
   * </p>
   * <p>
   * Application writers can use this method to redirect external system
   * identifiers to secure and/or local URIs, to look up public identifiers
   * in a catalogue, or to read an entity from a database or other input
   * source (including, for example, a dialog box). Neither XML nor SAX
   * specifies a preferred policy for using public or system IDs to resolve
   * resources. However, SAX specifies how to interpret any InputSource
   * returned by this method, and that if none is returned, then the system
   * ID will be dereferenced as a URL.
   * </p>
   * <p>
   * If the system identifier is a URL, the SAX parser must resolve it
   * fully before reporting it to the application.
   * </p>
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
  @Override
  public InputSource resolveEntity(final String publicId,
      final String systemId) {
    if (this.m_entityResolver != null) {
      try {
        return this.m_entityResolver.resolveEntity(publicId, systemId);
      } catch (IOException e) {
        this.setLastException(e);
      } catch (SAXException e) {
        this.setLastException(e);
      }
    }
    return null;
  }

  // /
  // / DTDHandler
  // /
  /**
   * Receive notification of a notation declaration event.
   * <p>
   * It is up to the application to record the notation for later
   * reference, if necessary; notations may appear as attribute values and
   * in unparsed entity declarations, and are sometime used with processing
   * instruction target names.
   * </p>
   * <p>
   * At least one of publicId and systemId must be non-null. If a system
   * identifier is present, and it is a URL, the SAX parser must resolve it
   * fully before passing it to the application through this event.
   * </p>
   * <p>
   * There is no guarantee that the notation declaration will be reported
   * before any unparsed entities that use it.
   * </p>
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
  @Override
  public void notationDecl(final String name, final String publicId,
      final String systemId) {
    if (this.m_dtdHandler != null) {
      try {
        this.m_dtdHandler.notationDecl(name, publicId, systemId);
      } catch (SAXException e) {
        this.setLastException(e);
      }
    }
  }

  /**
   * Receive notification of an unparsed entity declaration event.
   * <p>
   * Note that the notation name corresponds to a notation reported by the
   * {@link #notationDecl notationDecl} event. It is up to the application
   * to record the entity for later reference, if necessary; unparsed
   * entities may appear as attribute values.
   * </p>
   * <p>
   * If the system identifier is a URL, the parser must resolve it fully
   * before passing it to the application.
   * </p>
   * 
   * @param name
   *          The unparsed entity's name.
   * @param publicId
   *          The entity's public identifier, or null if none was given.
   * @param systemId
   *          The entity's system identifier.
   * @param notationName
   *          The name of the associated notation.
   * @see #notationDecl
   * @see org.xml.sax.Attributes
   */
  @Override
  public void unparsedEntityDecl(final String name, final String publicId,
      final String systemId, final String notationName) {
    if (this.m_dtdHandler != null) {
      try {
        this.m_dtdHandler.unparsedEntityDecl(name, publicId, systemId,
            notationName);
      } catch (SAXException e) {
        this.setLastException(e);
      }
    }
  }

  // /
  // / ContentHandler
  // /
  /**
   * Receive an object for locating the origin of SAX document events.
   * <p>
   * SAX parsers are strongly encouraged (though not absolutely required)
   * to supply a locator: if it does so, it must supply the locator to the
   * application by invoking this method before invoking any of the other
   * methods in the ContentHandler interface.
   * </p>
   * <p>
   * The locator allows the application to determine the end position of
   * any document-related event, even if the parser is not reporting an
   * error. Typically, the application will use this information for
   * reporting its own errors (such as character content that does not
   * match an application's business rules). The information returned by
   * the locator is probably not sufficient for use with a search engine.
   * </p>
   * <p>
   * Note that the locator will return correct information only during the
   * invocation SAX event callbacks after
   * {@link #startDocument startDocument} returns and before
   * {@link #endDocument endDocument} is called. The application should not
   * attempt to use it at any other time.
   * </p>
   * 
   * @param locator
   *          an object that can return the location of any SAX document
   *          event
   * @see org.xml.sax.Locator
   */
  @Override
  public void setDocumentLocator(final Locator locator) {
    this.m_locator = locator;
  }

  /**
   * Receive notification of the beginning of a document.
   * <p>
   * The SAX parser will invoke this method only once, before any other
   * event callbacks (except for {@link #setDocumentLocator 
   * setDocumentLocator}).
   * </p>
   * 
   * @see #endDocument
   */
  @Override
  public void startDocument() {
    if (this.m_contentHandler != null) {
      try {
        this.m_contentHandler.startDocument();
      } catch (SAXException e) {
        this.setLastException(e);
      }
    }
  }

  /**
   * Receive notification of the end of a document.
   * <p>
   * <strong>There is an apparent contradiction between the documentation
   * for this method and the documentation for {@link
   * org.xml.sax.ErrorHandler#fatalError}. Until this ambiguity is resolved
   * in a future major release, clients should make no assumptions about
   * whether endDocument() will or will not be invoked when the parser has
   * reported a fatalError() or thrown an exception.</strong>
   * </p>
   * <p>
   * The SAX parser will invoke this method only once, and it will be the
   * last method invoked during the parse. The parser shall not invoke this
   * method until it has either abandoned parsing (because of an
   * unrecoverable error) or reached the end of input.
   * </p>
   * 
   * @see #startDocument
   */
  @Override
  public void endDocument() {
    if (this.m_contentHandler != null) {
      try {
        this.m_contentHandler.endDocument();
      } catch (SAXException e) {
        this.setLastException(e);
      }
    }
  }

  /**
   * Begin the scope of a prefix-URI Namespace mapping.
   * <p>
   * The information from this event is not necessary for normal Namespace
   * processing: the SAX XML reader will automatically replace prefixes for
   * element and attribute names when the
   * <code>http://xml.org/sax/features/namespaces</code> feature is
   * <var>true</var> (the default).
   * </p>
   * <p>
   * There are cases, however, when applications need to use prefixes in
   * character data or in attribute values, where they cannot safely be
   * expanded automatically; the start/endPrefixMapping event supplies the
   * information to the application to expand prefixes in those contexts
   * itself, if necessary.
   * </p>
   * <p>
   * Note that start/endPrefixMapping events are not guaranteed to be
   * properly nested relative to each other: all startPrefixMapping events
   * will occur immediately before the corresponding
   * {@link #startElement startElement} event, and all
   * {@link #endPrefixMapping endPrefixMapping} events will occur
   * immediately after the corresponding {@link #endElement endElement}
   * event, but their order is not otherwise guaranteed.
   * </p>
   * <p>
   * There should never be start/endPrefixMapping events for the "xml"
   * prefix, since it is predeclared and immutable.
   * </p>
   * 
   * @param prefix
   *          the Namespace prefix being declared. An empty string is used
   *          for the default element namespace, which has no prefix.
   * @param uri
   *          the Namespace URI the prefix is mapped to
   * @see #endPrefixMapping
   * @see #startElement
   */
  @Override
  public void startPrefixMapping(final String prefix, final String uri) {
    if (this.m_contentHandler != null) {
      try {
        this.m_contentHandler.startPrefixMapping(prefix, uri);
      } catch (SAXException e) {
        this.setLastException(e);
      }
    }
  }

  /**
   * End the scope of a prefix-URI mapping.
   * <p>
   * See {@link #startPrefixMapping startPrefixMapping} for details. These
   * events will always occur immediately after the corresponding
   * {@link #endElement endElement} event, but the order of
   * {@link #endPrefixMapping endPrefixMapping} events is not otherwise
   * guaranteed.
   * </p>
   * 
   * @param prefix
   *          the prefix that was being mapped. This is the empty string
   *          when a default mapping scope ends.
   * @see #startPrefixMapping
   * @see #endElement
   */
  @Override
  public void endPrefixMapping(final String prefix) {
    if (this.m_contentHandler != null) {
      try {
        this.m_contentHandler.endPrefixMapping(prefix);
      } catch (SAXException e) {
        this.setLastException(e);
      }
    }
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
   * <p>
   * Any or all of these may be provided, depending on the values of the
   * <var>http://xml.org/sax/features/namespaces</var> and the
   * <var>http://xml.org/sax/features/namespace-prefixes</var> properties:
   * </p>
   * <ul>
   * <li>the Namespace URI and local name are required when the namespaces
   * property is <var>true</var> (the default), and are optional when the
   * namespaces property is <var>false</var> (if one is specified, both
   * must be);</li>
   * <li>the qualified name is required when the namespace-prefixes
   * property is <var>true</var>, and is optional when the
   * namespace-prefixes property is <var>false</var> (the default).</li>
   * </ul>
   * <p>
   * Note that the attribute list provided will contain only attributes
   * with explicit values (specified or defaulted): #IMPLIED attributes
   * will be omitted. The attribute list will contain attributes used for
   * Namespace declarations (xmlns* attributes) only if the
   * <code>http://xml.org/sax/features/namespace-prefixes</code> property
   * is true (it is false by default, and support for a true value is
   * optional).
   * </p>
   * <p>
   * Like {@link #characters characters()}, attribute values may have
   * characters that need more than one <code>char</code> value.
   * </p>
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
   * @see #endElement
   * @see org.xml.sax.Attributes
   */
  @Override
  public void startElement(final String uri, final String localName,
      final String qualifiedName, final Attributes atts) {
    if (this.m_contentHandler != null) {
      try {
        this.m_contentHandler.startElement(uri, localName, qualifiedName,
            atts);
      } catch (SAXException e) {
        this.setLastException(e);
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
   * @param qualifiedName
   *          the qualified XML name (with prefix), or the empty string if
   *          qualified names are not available
   */
  @Override
  public void endElement(final String uri, final String localName,
      final String qualifiedName) {
    if (this.m_contentHandler != null) {
      try {
        this.m_contentHandler.endElement(uri, localName, qualifiedName);
      } catch (SAXException e) {
        this.setLastException(e);
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
   * <p>
   * The application must not attempt to read from the array outside of the
   * specified range.
   * </p>
   * <p>
   * Individual characters may consist of more than one Java
   * <code>char</code> value. There are two important cases where this
   * happens, because characters can't be represented in just sixteen bits.
   * In one case, characters are represented in a <em>Surrogate Pair</em>,
   * using two special Unicode values. Such characters are in the so-called
   * "Astral Planes", with a code point above U+FFFF. A second case
   * involves composite characters, such as a base character combining with
   * one or more accent characters.
   * </p>
   * <p>
   * Your code should not assume that algorithms using <code>char</code>-at-a-time
   * idioms will be working in character units; in some cases they will
   * split characters. This is relevant wherever XML permits arbitrary
   * characters, such as attribute values, processing instruction data, and
   * comments as well as in data reported from this method. It's also
   * generally relevant whenever Java code manipulates internationalized
   * text; the issue isn't unique to XML.
   * </p>
   * <p>
   * Note that some parsers will report whitespace in element content using
   * the {@link #ignorableWhitespace ignorableWhitespace} method rather
   * than this one (validating parsers <em>must</em> do so).
   * </p>
   * 
   * @param ch
   *          the characters from the XML document
   * @param start
   *          the start position in the array
   * @param length
   *          the number of characters to read from the array
   * @see #ignorableWhitespace
   * @see org.xml.sax.Locator
   */
  @Override
  public void characters(final char ch[], final int start, final int length) {
    if (this.m_contentHandler != null) {
      try {
        this.m_contentHandler.characters(ch, start, length);
      } catch (SAXException e) {
        this.setLastException(e);
      }
    }
  }

  /**
   * Receive notification of ignorable whitespace in element content.
   * <p>
   * Validating Parsers must use this method to report each chunk of
   * whitespace in element content (see the W3C XML 1.0 recommendation,
   * section 2.10): non-validating parsers may also use this method if they
   * are capable of parsing and using content models.
   * </p>
   * <p>
   * SAX parsers may return all contiguous whitespace in a single chunk, or
   * they may split it into several chunks; however, all of the characters
   * in any single event must come from the same external entity, so that
   * the Locator provides useful information.
   * </p>
   * <p>
   * The application must not attempt to read from the array outside of the
   * specified range.
   * </p>
   * 
   * @param ch
   *          the characters from the XML document
   * @param start
   *          the start position in the array
   * @param length
   *          the number of characters to read from the array
   * @see #characters
   */
  @Override
  public void ignorableWhitespace(char ch[], int start, int length) {
    if (this.m_contentHandler != null) {
      try {
        this.m_contentHandler.ignorableWhitespace(ch, start, length);
      } catch (SAXException e) {
        this.setLastException(e);
      }
    }
  }

  /**
   * Receive notification of a processing instruction.
   * <p>
   * The Parser will invoke this method once for each processing
   * instruction found: note that processing instructions may occur before
   * or after the main document element.
   * </p>
   * <p>
   * A SAX parser must never report an XML declaration (XML 1.0, section
   * 2.8) or a text declaration (XML 1.0, section 4.3.1) using this method.
   * </p>
   * <p>
   * Like {@link #characters characters()}, processing instruction data
   * may have characters that need more than one <code>char</code> value.
   * </p>
   * 
   * @param target
   *          the processing instruction target
   * @param data
   *          the processing instruction data, or null if none was
   *          supplied. The data does not include any whitespace separating
   *          it from the target
   */
  @Override
  public void processingInstruction(final String target, final String data) {
    if (this.m_contentHandler != null) {
      try {
        this.m_contentHandler.processingInstruction(target, data);
      } catch (SAXException e) {
        this.setLastException(e);
      }
    }
  }

  /**
   * Receive notification of a skipped entity. This is not called for
   * entity references within markup constructs such as element start tags
   * or markup declarations. (The XML recommendation requires reporting
   * skipped external entities. SAX also reports internal entity
   * expansion/non-expansion, except within markup constructs.)
   * <p>
   * The Parser will invoke this method each time the entity is skipped.
   * Non-validating processors may skip entities if they have not seen the
   * declarations (because, for example, the entity was declared in an
   * external DTD subset). All processors may skip external entities,
   * depending on the values of the
   * <code>http://xml.org/sax/features/external-general-entities</code>
   * and the
   * <code>http://xml.org/sax/features/external-parameter-entities</code>
   * properties.
   * </p>
   * 
   * @param name
   *          the name of the skipped entity. If it is a parameter entity,
   *          the name will begin with '%', and if it is the external DTD
   *          subset, it will be the string "[dtd]"
   */
  @Override
  public void skippedEntity(final String name) {
    if (this.m_contentHandler != null) {
      try {
        this.m_contentHandler.skippedEntity(name);
      } catch (SAXException e) {
        this.setLastException(e);
      }
    }
  }

  // /
  // / ErrorHandler
  // /
  /**
   * Receive notification of a warning.
   * <p>
   * SAX parsers will use this method to report conditions that are not
   * errors or fatal errors as defined by the XML recommendation. The
   * default behaviour is to take no action.
   * </p>
   * <p>
   * The SAX parser must continue to provide normal parsing events after
   * invoking this method: it should still be possible for the application
   * to process the document through to the end.
   * </p>
   * <p>
   * Filters may use this method to report other, non-XML warnings as well.
   * </p>
   * 
   * @param exception
   *          The warning information encapsulated in a SAX parse
   *          exception.
   * @see org.xml.sax.SAXParseException
   */
  @Override
  public void warning(final SAXParseException exception) {
    if (this.m_errorHandler != null) {
      try {
        this.m_errorHandler.warning(exception);
      } catch (SAXException e) {
        this.setLastException(e);
      }
    }
  }

  /**
   * Receive notification of a recoverable error.
   * <p>
   * This corresponds to the definition of "error" in section 1.2 of the
   * W3C XML 1.0 Recommendation. For example, a validating parser would use
   * this callback to report the violation of a validity constraint. The
   * default behaviour is to take no action.
   * </p>
   * <p>
   * The SAX parser must continue to provide normal parsing events after
   * invoking this method: it should still be possible for the application
   * to process the document through to the end. If the application cannot
   * do so, then the parser should report a fatal error even if the XML
   * recommendation does not require it to do so.
   * </p>
   * <p>
   * Filters may use this method to report other, non-XML errors as well.
   * </p>
   * 
   * @param exception
   *          The error information encapsulated in a SAX parse exception.
   * @see org.xml.sax.SAXParseException
   */
  @Override
  public void error(final SAXParseException exception) {
    if (this.m_errorHandler != null) {
      try {
        this.m_errorHandler.error(exception);
      } catch (SAXException e) {
        this.setLastException(e);
      }
    }
  }

  /**
   * Receive notification of a non-recoverable error.
   * <p>
   * <strong>There is an apparent contradiction between the documentation
   * for this method and the documentation for {@link
   * org.xml.sax.ContentHandler#endDocument}. Until this ambiguity is
   * resolved in a future major release, clients should make no assumptions
   * about whether endDocument() will or will not be invoked when the
   * parser has reported a fatalError() or thrown an exception.</strong>
   * </p>
   * <p>
   * This corresponds to the definition of "fatal error" in section 1.2 of
   * the W3C XML 1.0 Recommendation. For example, a parser would use this
   * callback to report the violation of a well-formedness constraint.
   * </p>
   * <p>
   * The application must assume that the document is unusable after the
   * parser has invoked this method, and should continue (if at all) only
   * for the sake of collecting additional error messages: in fact, SAX
   * parsers are free to stop reporting any other events once this method
   * has been invoked.
   * </p>
   * 
   * @param exception
   *          The error information encapsulated in a SAX parse exception.
   * @see org.xml.sax.SAXParseException
   */
  @Override
  public void fatalError(SAXParseException exception) {
    if (this.m_errorHandler != null) {
      try {
        this.m_errorHandler.fatalError(exception);
      } catch (SAXException e) {
        this.setLastException(e);
      }
    }
  }

  // /
  // / Locator
  // /
  /**
   * Return the public identifier for the current document event.
   * <p>
   * The return value is the public identifier of the document entity or of
   * the external parsed entity in which the markup triggering the event
   * appears.
   * </p>
   * 
   * @return A string containing the public identifier, or null if none is
   *         available.
   * @see #getSystemId
   */
  public String getPublicId() {
    if (this.m_locator != null)
      return this.m_locator.getPublicId();
    return null;
  }

  /**
   * Return the system identifier for the current document event.
   * <p>
   * The return value is the system identifier of the document entity or of
   * the external parsed entity in which the markup triggering the event
   * appears.
   * </p>
   * <p>
   * If the system identifier is a URL, the parser must resolve it fully
   * before passing it to the application. For example, a file name must
   * always be provided as a <em>file:...</em> URL, and other kinds of
   * relative URI are also resolved against their bases.
   * </p>
   * 
   * @return A string containing the system identifier, or null if none is
   *         available.
   * @see #getPublicId
   */
  public String getSystemId() {
    if (this.m_locator != null)
      return this.m_locator.getSystemId();
    return null;
  }

  /**
   * Return the line number where the current document event ends. Lines
   * are delimited by line ends, which are defined in the XML
   * specification.
   * <p>
   * <strong>Warning:</strong> The return value from the method is
   * intended only as an approximation for the sake of diagnostics; it is
   * not intended to provide sufficient information to edit the character
   * content of the original XML document. In some cases, these "line"
   * numbers match what would be displayed as columns, and in others they
   * may not match the source text due to internal entity expansion.
   * </p>
   * <p>
   * The return value is an approximation of the line number in the
   * document entity or external parsed entity where the markup triggering
   * the event appears.
   * </p>
   * <p>
   * If possible, the SAX driver should provide the line position of the
   * first character after the text associated with the document event. The
   * first line is line 1.
   * </p>
   * 
   * @return The line number, or -1 if none is available.
   * @see #getColumnNumber
   */
  public int getLineNumber() {
    if (this.m_locator != null)
      return this.m_locator.getLineNumber();
    return -1;
  }

  /**
   * Return the column number where the current document event ends. This
   * is one-based number of Java <code>char</code> values since the last
   * line end.
   * <p>
   * <strong>Warning:</strong> The return value from the method is
   * intended only as an approximation for the sake of diagnostics; it is
   * not intended to provide sufficient information to edit the character
   * content of the original XML document. For example, when lines contain
   * combining character sequences, wide characters, surrogate pairs, or
   * bi-directional text, the value may not correspond to the column in a
   * text editor's display.
   * </p>
   * <p>
   * The return value is an approximation of the column number in the
   * document entity or external parsed entity where the markup triggering
   * the event appears.
   * </p>
   * <p>
   * If possible, the SAX driver should provide the line position of the
   * first character after the text associated with the document event. The
   * first column in each line is column 1.
   * </p>
   * 
   * @return The column number, or -1 if none is available.
   * @see #getLineNumber
   */
  public int getColumnNumber() {
    if (this.m_locator != null)
      return this.m_locator.getColumnNumber();
    return -1;
  }

  // /
  // / LexicalHandler
  // /
  /**
   * Report the start of DTD declarations, if any.
   * <p>
   * This method is intended to report the beginning of the DOCTYPE
   * declaration; if the document has no DOCTYPE declaration, this method
   * will not be invoked.
   * </p>
   * <p>
   * All declarations reported through
   * {@link org.xml.sax.DTDHandler DTDHandler} or
   * {@link org.xml.sax.ext.DeclHandler DeclHandler} events must appear
   * between the startDTD and {@link #endDTD endDTD} events. Declarations
   * are assumed to belong to the internal DTD subset unless they appear
   * between {@link #startEntity startEntity} and
   * {@link #endEntity endEntity} events. Comments and processing
   * instructions from the DTD should also be reported between the startDTD
   * and endDTD events, in their original order of (logical) occurrence;
   * they are not required to appear in their correct locations relative to
   * DTDHandler or DeclHandler events, however.
   * </p>
   * <p>
   * Note that the start/endDTD events will appear within the
   * start/endDocument events from ContentHandler and before the first
   * {@link org.xml.sax.ContentHandler#startElement startElement} event.
   * </p>
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
   * @see #startEntity
   */
  @Override
  public void startDTD(final String name, final String publicId,
      final String systemId) {
    if (this.m_lexicalHandler != null) {
      try {
        this.m_lexicalHandler.startDTD(name, publicId, systemId);
      } catch (SAXException e) {
        this.setLastException(e);
      }
    }
  }

  /**
   * Report the end of DTD declarations.
   * <p>
   * This method is intended to report the end of the DOCTYPE declaration;
   * if the document has no DOCTYPE declaration, this method will not be
   * invoked.
   * </p>
   * 
   * @see #startDTD
   */
  @Override
  public void endDTD() {
    if (this.m_lexicalHandler != null) {
      try {
        this.m_lexicalHandler.endDTD();
      } catch (SAXException e) {
        this.setLastException(e);
      }
    }
  }

  /**
   * Report the beginning of some internal and external XML entities.
   * <p>
   * The reporting of parameter entities (including the external DTD
   * subset) is optional, and SAX2 drivers that report LexicalHandler
   * events may not implement it; you can use the <code
   * >http://xml.org/sax/features/lexical-handler/parameter-entities</code>
   * feature to query or control the reporting of parameter entities.
   * </p>
   * <p>
   * General entities are reported with their regular names, parameter
   * entities have '%' prepended to their names, and the external DTD
   * subset has the pseudo-entity name "[dtd]".
   * </p>
   * <p>
   * When a SAX2 driver is providing these events, all other events must be
   * properly nested within start/end entity events. There is no additional
   * requirement that events from
   * {@link org.xml.sax.ext.DeclHandler DeclHandler} or
   * {@link org.xml.sax.DTDHandler DTDHandler} be properly ordered.
   * </p>
   * <p>
   * Note that skipped entities will be reported through the
   * {@link org.xml.sax.ContentHandler#skippedEntity skippedEntity} event,
   * which is part of the ContentHandler interface.
   * </p>
   * <p>
   * Because of the streaming event model that SAX uses, some entity
   * boundaries cannot be reported under any circumstances:
   * </p>
   * <ul>
   * <li>general entities within attribute values</li>
   * <li>parameter entities within declarations</li>
   * </ul>
   * <p>
   * These will be silently expanded, with no indication of where the
   * original entity boundaries were.
   * </p>
   * <p>
   * Note also that the boundaries of character references (which are not
   * really entities anyway) are not reported.
   * </p>
   * <p>
   * All start/endEntity events must be properly nested.
   * 
   * @param name
   *          The name of the entity. If it is a parameter entity, the name
   *          will begin with '%', and if it is the external DTD subset, it
   *          will be "[dtd]".
   * @see #endEntity
   * @see org.xml.sax.ext.DeclHandler#internalEntityDecl
   * @see org.xml.sax.ext.DeclHandler#externalEntityDecl
   */
  @Override
  public void startEntity(String name) {
    if (this.m_lexicalHandler != null) {
      try {
        this.m_lexicalHandler.startEntity(name);
      } catch (SAXException e) {
        this.setLastException(e);
      }
    }
  }

  /**
   * Report the end of an entity.
   * 
   * @param name
   *          The name of the entity that is ending.
   * @see #startEntity
   */
  @Override
  public void endEntity(final String name) {
    if (this.m_lexicalHandler != null) {
      try {
        this.m_lexicalHandler.endEntity(name);
      } catch (SAXException e) {
        this.setLastException(e);
      }
    }
  }

  /**
   * Report the start of a CDATA section.
   * <p>
   * The contents of the CDATA section will be reported through the regular
   * {@link org.xml.sax.ContentHandler#characters characters} event; this
   * event is intended only to report the boundary.
   * </p>
   * 
   * @see #endCDATA
   */
  @Override
  public void startCDATA() {
    if (this.m_lexicalHandler != null) {
      try {
        this.m_lexicalHandler.startCDATA();
      } catch (SAXException e) {
        this.setLastException(e);
      }
    }
  }

  /**
   * Report the end of a CDATA section.
   * 
   * @see #startCDATA
   */
  @Override
  public void endCDATA() {
    if (this.m_lexicalHandler != null) {
      try {
        this.m_lexicalHandler.endCDATA();
      } catch (SAXException e) {
        this.setLastException(e);
      }
    }
  }

  /**
   * Report an XML comment anywhere in the document.
   * <p>
   * This callback will be used for comments inside or outside the document
   * element, including comments in the external DTD subset (if read).
   * Comments in the DTD must be properly nested inside start/endDTD and
   * start/endEntity events (if used).
   * </p>
   * 
   * @param ch
   *          An array holding the characters in the comment.
   * @param start
   *          The starting position in the array.
   * @param length
   *          The number of characters to use from the array.
   */
  @Override
  public void comment(final char ch[], final int start, final int length) {
    if (this.m_lexicalHandler != null) {
      try {
        this.m_lexicalHandler.comment(ch, start, length);
      } catch (SAXException e) {
        this.setLastException(e);
      }
    }
  }

  // /
  // / EntityResolver2
  // /

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
   * </p>
   * <p>
   * This method can also be used with documents that have no DOCTYPE
   * declaration. When the root element is encountered, but no DOCTYPE
   * declaration has been seen, this method is invoked. If it returns a
   * value for the external subset, that root element is declared to be the
   * root element, giving the effect of splicing a DOCTYPE declaration at
   * the end the prolog of a document that could not otherwise be valid.
   * The sequence of parser callbacks in that case logically resembles
   * this:
   * </p>
   * 
   * <pre>
   *                ... comments and PIs from the prolog (as usual)
   *                startDTD (&quot;rootName&quot;, source.getPublicId (), source.getSystemId ());
   *                startEntity (&quot;[dtd]&quot;);
   *                ... declarations, comments, and PIs from the external subset
   *                endEntity (&quot;[dtd]&quot;);
   *                endDTD ();
   *                ... then the rest of the document (as usual)
   *                startElement (..., &quot;rootName&quot;, ...);
   * </pre>
   * 
   * <p>
   * Note that the InputSource gets no further resolution. Implementations
   * of this method may wish to invoke <code>resolveEntity</code> to gain
   * benefits such as use of local caches of DTD entities. Also, this
   * method will never be used by a (non-validating) processor that is not
   * including external parameter entities.
   * </p>
   * <p>
   * Uses for this method include facilitating data validation when
   * interoperating with XML processors that would always require
   * undesirable network accesses for external entities, or which for other
   * reasons adopt a "no DTDs" policy. Non-validation motives include
   * forcing documents to include DTDs so that attributes are handled
   * consistently. For example, an XPath processor needs to know which
   * attibutes have type "ID" before it can process a widely used type of
   * reference.
   * </p>
   * <p>
   * <strong>Warning:</strong> Returning an external subset modifies the
   * input document. By providing definitions for general entities, it can
   * make a malformed document appear to be well formed.
   * </p>
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
  @Override
  public InputSource getExternalSubset(final String name,
      final String baseUri) {
    EntityResolver e;

    e = this.m_entityResolver;
    if ((e != null) && (e instanceof EntityResolver2)
        && (this.m_features.contains(XML.FEATURE_ENTITY_RESOLVER_2))) {
      try {
        return ((EntityResolver2) e).getExternalSubset(name, baseUri);
      } catch (SAXException ee) {
        this.setLastException(ee);
      } catch (IOException ee) {
        this.setLastException(ee);
      }
    }

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
   * </p>
   * <p>
   * Parsers configured to use this resolver method will call it to
   * determine the input source to use for any external entity being
   * included because of a reference in the XML text. That excludes the
   * document entity, and any external entity returned by
   * {@link #getExternalSubset getExternalSubset()}. When a
   * (non-validating) processor is configured not to include a class of
   * entities (parameter or general) through use of feature flags, this
   * method is not invoked for such entities.
   * </p>
   * <p>
   * Note that the entity naming scheme used here is the same one used in
   * the {@link LexicalHandler}, or in the {@link
   * org.xml.sax.ContentHandler#skippedEntity
   * ContentHandler.skippedEntity()} method.
   * </p>
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
  @Override
  public InputSource resolveEntity(final String name,
      final String publicId, final String baseUri, final String systemId) {
    EntityResolver e;

    e = this.m_entityResolver;
    if ((e != null) && (e instanceof EntityResolver2)
        && (this.m_features.contains(XML.FEATURE_ENTITY_RESOLVER_2))) {
      try {
        return ((EntityResolver2) e).resolveEntity(name, publicId,
            baseUri, systemId);
      } catch (SAXException ee) {
        this.setLastException(ee);
      } catch (IOException ee) {
        this.setLastException(ee);
      }
    }

    return null;
  }

  // /
  // / DeclHandler
  // /

  /**
   * Report an element type declaration.
   * <p>
   * The content model will consist of the string "EMPTY", the string
   * "ANY", or a parenthesised group, optionally followed by an occurrence
   * indicator. The model will be normalized so that all parameter entities
   * are fully resolved and all whitespace is removed,and will include the
   * enclosing parentheses. Other normalization (such as removing redundant
   * parentheses or simplifying occurrence indicators) is at the discretion
   * of the parser.
   * </p>
   * 
   * @param name
   *          The element type name.
   * @param model
   *          The content model as a normalized string.
   */
  @Override
  public void elementDecl(final String name, final String model) {
    if (this.m_declHandler != null) {
      try {
        this.m_declHandler.elementDecl(name, model);
      } catch (SAXException ee) {
        this.setLastException(ee);
      }
    }
  }

  /**
   * Report an attribute type declaration.
   * <p>
   * Only the effective (first) declaration for an attribute will be
   * reported. The type will be one of the strings "CDATA", "ID", "IDREF",
   * "IDREFS", "NMTOKEN", "NMTOKENS", "ENTITY", "ENTITIES", a parenthesized
   * token group with the separator "|" and all whitespace removed, or the
   * word "NOTATION" followed by a space followed by a parenthesized token
   * group with all whitespace removed.
   * </p>
   * <p>
   * The value will be the value as reported to applications, appropriately
   * normalized and with entity and character references expanded.
   * </p>
   * 
   * @param e_name
   *          The name of the associated element.
   * @param a_name
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
  @Override
  public void attributeDecl(final String e_name, final String a_name,
      final String type, final String mode, final String value) {
    if (this.m_declHandler != null) {
      try {
        this.m_declHandler
            .attributeDecl(e_name, a_name, type, mode, value);
      } catch (SAXException ee) {
        this.setLastException(ee);
      }
    }
  }

  /**
   * Report an internal entity declaration.
   * <p>
   * Only the effective (first) declaration for each entity will be
   * reported. All parameter entities in the value will be expanded, but
   * general entities will not.
   * </p>
   * 
   * @param name
   *          The name of the entity. If it is a parameter entity, the name
   *          will begin with '%'.
   * @param value
   *          The replacement text of the entity.
   * @see #externalEntityDecl
   * @see org.xml.sax.DTDHandler#unparsedEntityDecl
   */
  @Override
  public void internalEntityDecl(final String name, final String value) {
    if (this.m_declHandler != null) {
      try {
        this.m_declHandler.internalEntityDecl(name, value);
      } catch (SAXException ee) {
        this.setLastException(ee);
      }
    }
  }

  /**
   * Report a parsed external entity declaration.
   * <p>
   * Only the effective (first) declaration for each entity will be
   * reported.
   * </p>
   * <p>
   * If the system identifier is a URL, the parser must resolve it fully
   * before passing it to the application.
   * </p>
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
  @Override
  public void externalEntityDecl(final String name, final String publicId,
      final String systemId) {
    if (this.m_declHandler != null) {
      try {
        this.m_declHandler.externalEntityDecl(name, publicId, systemId);
      } catch (SAXException ee) {
        this.setLastException(ee);
      }
    }
  }

  // /
  // / Locator 2
  // /
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
  @Override
  public String getXMLVersion() {
    Locator l;

    l = this.m_locator;
    if ((l != null) && (l instanceof Locator2)
        && (this.m_features.contains(XML.FEATURE_LOCATOR_2))) {
      return ((Locator2) l).getXMLVersion();
    }

    return XML.PREFERED_VERSION;
  }

  /**
   * Returns the name of the character encoding for the entity. If the
   * encoding was declared externally (for example, in a MIME Content-Type
   * header), that will be the name returned. Else if there was an
   * <em>&lt;?xml&nbsp;...encoding='...'?&gt;</em> declaration at the
   * start of the document, that encoding name will be returned. Otherwise
   * the encoding will been inferred (normally to be UTF-8, or some UTF-16
   * variant), and that inferred name will be returned.
   * <p>
   * When an {@link org.xml.sax.InputSource InputSource} is used to provide
   * an entity's character stream, this method returns the encoding
   * provided in that input stream.
   * <p>
   * Note that some recent W3C specifications require that text in some
   * encodings be normalized, using Unicode Normalization Form C, before
   * processing. Such normalization must be performed by applications, and
   * would normally be triggered based on the value returned by this
   * method.
   * <p>
   * Encoding names may be those used by the underlying JVM, and
   * comparisons should be case-insensitive.
   * 
   * @return Name of the character encoding being used to interpret * the
   *         entity's text, or null if this was not provided for a *
   *         character stream passed through an InputSource or is otherwise
   *         not yet available in the current parsing state.
   */
  @Override
  public String getEncoding() {
    Locator l;

    l = this.m_locator;
    if ((l != null) && (l instanceof Locator2)
        && (this.m_features.contains(XML.FEATURE_LOCATOR_2))) {
      return ((Locator2) l).getEncoding();
    }

    return XML.PREFERED_ENCODING;
  }

}
