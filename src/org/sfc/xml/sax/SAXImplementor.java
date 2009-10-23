/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-22
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.xml.sax.SAXImplementor.java
 * Last modification: 2006-11-22
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

import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.EntityResolver2;
import org.xml.sax.ext.LexicalHandler;

/**
 * This class has no semantic function, it just implements all the usefull
 * sax interfaces.
 * 
 * @author Thomas Weise
 */
public class SAXImplementor extends ContentHandlerImplementor implements
    EntityResolver, DTDHandler, ErrorHandler, LexicalHandler,
    EntityResolver2, DeclHandler {

  /**
   * the last exception.
   */
  private Exception m_lastException;

  /**
   * Create a new <code>SAXImplementor</code>. This constructor ensures
   * that <code>SAXImplementor</code>s will only be created from
   * subclasses and not by hand.
   */
  protected SAXImplementor() {
    super();
  }

  /**
   * Set the last exception.
   * 
   * @param e
   *          new the last exception
   */
  protected void setLastException(final Exception e) {
    if (e != null)
      this.m_lastException = e;
  }

  /**
   * Obtain and clear the last exception.
   * 
   * @return the last exception that occured, or <code>null</code> if
   *         none occured
   */
  public Exception getLastException() {
    Exception e;

    e = this.m_lastException;
    this.m_lastException = null;
    return e;
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
  public InputSource resolveEntity(final String publicId,
      final String systemId) {
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
  public void notationDecl(final String name, final String publicId,
      final String systemId) {
    // does nothing
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
  public void warning(final SAXParseException exception) {
    // does nothing
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
  public void error(final SAXParseException exception) {
    // does nothing
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
  public void fatalError(final SAXParseException exception) {
    // does nothing
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
  public void startDTD(final String name, final String publicId,
      final String systemId) {
    // does nothing
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
  public void endDTD() {
    // does nothing
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
   * Report the start of a CDATA section.
   * <p>
   * The contents of the CDATA section will be reported through the regular
   * {@link org.xml.sax.ContentHandler#characters characters} event; this
   * event is intended only to report the boundary.
   * </p>
   * 
   * @see #endCDATA
   */
  public void startCDATA() {
    // does nothing
  }

  /**
   * Report the end of a CDATA section.
   * 
   * @see #startCDATA
   */
  public void endCDATA() {
    // does nothing
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
  public void comment(final char ch[], final int start, final int length) {
    // does nothing
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
   *              ... comments and PIs from the prolog (as usual)
   *              startDTD (&quot;rootName&quot;, source.getPublicId (), source.getSystemId ());
   *              startEntity (&quot;[dtd]&quot;);
   *              ... declarations, comments, and PIs from the external subset
   *              endEntity (&quot;[dtd]&quot;);
   *              endDTD ();
   *              ... then the rest of the document (as usual)
   *              startElement (..., &quot;rootName&quot;, ...);
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
  public InputSource resolveEntity(final String name,
      final String publicId, final String baseUri, final String systemId) {
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
  public void elementDecl(final String name, final String model) {
    // does nothing
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
    // does nothing
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
  public void internalEntityDecl(final String name, final String value) {
    // does nothing
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
  public void externalEntityDecl(final String name, final String publicId,
      final String systemId) {
    // does nothing
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
  public String getXMLVersion() {
    return null;
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
  public String getEncoding() {
    return null;
  }

}
