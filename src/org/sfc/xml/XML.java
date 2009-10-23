/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-12-28
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.xml.XML.java
 * Last modification: 2006-12-28
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

package org.sfc.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.sfc.io.IO;
import org.sfc.scoped.AlreadyDisposedException;
import org.sfc.utils.ErrorUtils;
import org.w3c.dom.Document;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.EntityResolver2;
import org.xml.sax.ext.LexicalHandler;

/**
 * This class presents basic functionality for xml.
 * 
 * @author Thomas Weise
 */
public final class XML {

  /**
   * The preferred encoding for xml-data.
   */
  public static final String PREFERED_ENCODING = "UTF-8"; //$NON-NLS-1$

  /**
   * The preferred xml version.
   */
  public static final String PREFERED_VERSION = "1.0"; //$NON-NLS-1$

  /**
   * The line break character changed to \0a in xml.
   */
  public static final char LINE_SEPARATOR = '\n';

  /**
   * Separator for prefix to namespace.
   */
  public static final char NAMESPACE_SEPARATOR = ':';

  /**
   * The prefix for namespace-attributes.
   */
  public static final String XMLNS = "xmlns"; //$NON-NLS-1$

  /**
   * The prefix for namespace-attributes.
   */
  public static final String XMLNSQ = XMLNS + NAMESPACE_SEPARATOR;

  /**
   * The property for lexical handlers.
   */
  public static final String PROPERTY_LEXICAL_HANDLER = "http://xml.org/sax/properties/lexical-handler"; //$NON-NLS-1$

  /**
   * The property for decl handlers.
   */
  public static final String PROPERTY_DECL_HANDLER = "http://xml.org/sax/properties/declaration-handler"; //$NON-NLS-1$

  /**
   * <code>EntityResolver2</code>s are recognized.
   */
  public static final String FEATURE_ENTITY_RESOLVER_2 = "http://xml.org/sax/features/use-entity-resolver2"; //$NON-NLS-1$

  /**
   * The namespace-awareness feature.
   */
  public static final String FEATURE_NAMESPACES = "http://xml.org/sax/features/namespaces";//$NON-NLS-1$

  /**
   * The namespace-prefix feature.
   */
  public static final String FEATURE_NAMESPACE_PREFIXES = "http://xml.org/sax/features/namespace-prefixes"; //$NON-NLS-1$

  /**
   * <code>Locator2</code>s are recognized.
   */
  public static final String FEATURE_LOCATOR_2 = "http://xml.org/sax/features/use-locator2";//$NON-NLS-1$

  /**
   * The shared transformer factory.
   */
  private static final TransformerFactory TRANSFORMER_FACTORY;

  /**
   * The shared sax parser factory.
   */
  private static final SAXParserFactory PARSER_FACTORY;

  /**
   * The shared document builder factory.
   */
  // private static final DocumentBuilderFactory BUILDER_FACTORY;
  static {
    PARSER_FACTORY = SAXParserFactory.newInstance();
   
     PARSER_FACTORY.setNamespaceAware(true);

    TRANSFORMER_FACTORY = TransformerFactory.newInstance();

    // BUILDER_FACTORY = DocumentBuilderFactory.newInstance();
    // BUILDER_FACTORY.setNamespaceAware(true);
    // BUILDER_FACTORY.setIgnoringComments(true);
    // BUILDER_FACTORY.setXIncludeAware(true);
  }

  /**
   * the forbidden constructor.
   */
  private XML() {
    ErrorUtils.doNotCall();
  }

  /**
   * Installs an objecti to an xml reader. This method will set all the xml
   * reader's supported handlers to the object so any sax event produced by
   * the reader will be tunnelled to this object.
   * 
   * @param reader
   *          The reader to install to.
   * @param o
   *          the object to be installed
   */
  public static final void installTo(final XMLReader reader, final Object o) {

    if (o instanceof ContentHandler)
      reader.setContentHandler((ContentHandler) o);

    if (o instanceof DTDHandler)
      reader.setDTDHandler((DTDHandler) o);

    if (o instanceof EntityResolver)
      reader.setEntityResolver((EntityResolver) o);

    if (o instanceof ErrorHandler)
      reader.setErrorHandler((ErrorHandler) o);

    if (o instanceof LexicalHandler)
      try {
        reader.setProperty(PROPERTY_LEXICAL_HANDLER, o);
      } catch (Throwable t) {
        //
      }

    if (o instanceof DeclHandler)
      try {
        reader.setProperty(PROPERTY_DECL_HANDLER, o);
      } catch (Throwable t) {
        //
      }

    if (o instanceof EntityResolver2)
      try {
        reader.setFeature(FEATURE_ENTITY_RESOLVER_2, true);
      } catch (Throwable t) {
        //
      }
  }

  /**
   * Uninstalls an object from an xml reader. This method will set all the
   * xml reader's supported handlers that point currently to the object to
   * null, so it is ensured that no more events will be received from the
   * reader.
   * 
   * @param reader
   *          The reader to install to.
   * @param o
   *          the object to uninstall
   */
  public static final void uninstallFrom(final XMLReader reader,
      final Object o) {
    if (reader.getContentHandler() == o) {
      reader.setContentHandler(null);
    }

    if (reader.getDTDHandler() == o) {
      reader.setDTDHandler(null);
    }

    if (reader.getEntityResolver() == o) {
      reader.setEntityResolver(null);
    }

    if (reader.getErrorHandler() == o) {
      reader.setErrorHandler(null);
    }

    try {
      if (reader.getProperty(PROPERTY_LEXICAL_HANDLER) == o) {
        reader.setProperty(PROPERTY_LEXICAL_HANDLER, null);
      }
    } catch (Throwable t) {
      //
    }
    try {
      if (reader.getProperty(PROPERTY_DECL_HANDLER) == o) {
        reader.setProperty(PROPERTY_DECL_HANDLER, null);
      }
    } catch (Throwable t) {
      //
    }
  }

  /**
   * Converts any given object to an input source. The returned input
   * source can be closed by <code>IO.close()</code> to ensure that all
   * open streams are well, erm, closed.
   * 
   * @param object
   *          The object to convert into an input source.
   * @return The <code>InputSource</code> represented by this object, or
   *         null if the object could not be converted into an input
   *         source.
   */
  public static final InputSource getInputSource(final Object object) {
    URL url;
    InputStream is;
    Reader r;
    Object check;
    InputSource rci;
    StreamSource ss;

    if (object instanceof InputSource) {
      return ((InputSource) object);
    }

    rci = new InputSource();

    if (object instanceof StreamSource) {
      ss = ((StreamSource) object);
      rci.setByteStream(ss.getInputStream());
      rci.setCharacterStream(ss.getReader());
      rci.setPublicId(ss.getPublicId());
      rci.setSystemId(ss.getSystemId());
      return rci;
    }

    url = IO.getUrl(object);
    if (url != null) {
      check = url;
      rci.setSystemId(url.toExternalForm());
    } else {
      check = object;
    }

    is = IO.getInputStream(check);
    if (is != null) {
      rci.setByteStream(is);
      return rci;
    }

    r = IO.getReader(check);
    if (r != null) {
      rci.setCharacterStream(r);
      return rci;
    }

    if (url == null) {
      url = IO.getResourceUrl(object, 1);
      if (url != null) {
        check = url;
        rci.setSystemId(url.toExternalForm());
      } else {
        check = object;
      }
    }

    is = IO.getResourceInputStream(check, 1);
    if (is != null) {
      rci.setByteStream(is);
      return rci;
    }

    return (url != null) ? rci : null;
  }

  /**
   * Converts any given object to a source. The returned source should be
   * closed by <code>IO.close()</code> to ensure that all open streams
   * are well, erm, closed when done with it.
   * 
   * @param object
   *          The object to convert into a source.
   * @return The <code>Source</code> represented by this object, or null
   *         if the object could not be converted into a result.
   */
  public static final Source getSource(final Object object) {
    URL url;
    InputStream is;
    Reader r;
    Object check;
    StreamSource x;
    InputSource iss;

    if (object instanceof StreamSource) {
      return ((StreamSource) object);
    }

    if (object instanceof Document) {
      return new DOMSource((Document) object);
    }

    if (object instanceof DOMSource) {
      return ((DOMSource) object);
    }

    if (object instanceof SAXSource) {
      return ((SAXSource) object);
    }

    if (object instanceof SAXSource) {
      return ((SAXSource) object);
    }

    x = new StreamSource();

    if (object instanceof InputSource) {
      iss = ((InputSource) object);
      x.setInputStream(iss.getByteStream());
      x.setReader(iss.getCharacterStream());
      x.setSystemId(iss.getSystemId());
      x.setPublicId(iss.getPublicId());
      return x;
    }

    url = IO.getUrl(object);
    if (url != null) {
      check = url;
      x.setSystemId(url.toExternalForm());
    } else {
      check = object;
    }

    is = IO.getInputStream(check);
    if (is != null) {
      x.setInputStream(is);
      return x;
    }

    r = IO.getReader(check);
    if (r != null) {
      x.setReader(r);
      return x;
    }

    if (url == null) {
      url = IO.getResourceUrl(object, 1);
      if (url != null) {
        check = url;
        x.setSystemId(url.toExternalForm());
      } else {
        check = object;
      }
    }

    is = IO.getResourceInputStream(check, 1);
    if (is != null) {
      x.setInputStream(is);
      return x;
    }

    if (object instanceof Source)
      return ((Source) object);
    return ((url != null) ? x : null);
  }

  /**
   * Converts any given object to a result. The returned result should be
   * closed by <code>IO.close()</code> to ensure that all open streams
   * are well, erm, closed when done with it.
   * 
   * @param object
   *          The object to convert into a result.
   * @return The <code>Result</code> represented by this object, or null
   *         if the object could not be converted into a result.
   */
  public static final Result getResult(final Object object) {
    URL url;
    OutputStream os;
    Writer w;
    Object check;
    StreamResult x;

    if (object instanceof StreamResult) {
      return ((StreamResult) object);
    }

    x = new StreamResult();

    url = IO.getUrl(object);
    if (url != null) {
      check = url;
      x.setSystemId(url.toExternalForm());
    } else {
      check = object;
    }

    os = IO.getOutputStream(check);
    if (os != null) {
      x.setOutputStream(os);
      return x;
    }

    w = IO.getWriter(check, PREFERED_ENCODING);
    if (w != null) {
      x.setWriter(w);
      return x;
    }

    if (object instanceof Result)
      return ((Result) object);
    return ((url != null) ? x : null);
  }

  /**
   * Close the specified xml io-object.
   * 
   * @param object
   *          The object to be closed.
   * @return The <code>IOException</code> thrown during the process, or
   *         <code>null</code> if all went ok.
   */
  public static final IOException close(final Object object) {
    Reader r;
    Writer w;
    InputStream is;
    OutputStream os;
    InputSource xis;
    StreamSource xss;
    StreamResult xsr;
    IOException io1, io2;
    Object o;

    r = null;
    w = null;
    is = null;
    os = null;

    if (object instanceof SAXSource)
      o = ((SAXSource) object).getInputSource();
    else
      o = object;

    if (o instanceof InputSource) {
      xis = ((InputSource) o);
      r = xis.getCharacterStream();
      is = xis.getByteStream();
    } else {
      if (o instanceof StreamResult) {
        xsr = ((StreamResult) o);
        w = xsr.getWriter();
        os = xsr.getOutputStream();
      } else {
        if (o instanceof StreamSource) {
          xss = ((StreamSource) o);
          r = xss.getReader();
          is = xss.getInputStream();
        } else
          return null;
      }
    }

    io1 = ((r != null) ? IO.close(r) : null);
    io2 = ((w != null) ? IO.close(w) : null);
    if (io1 == null)
      io1 = io2;
    io2 = ((is != null) ? IO.close(is) : null);
    if (io1 == null)
      io1 = io2;
    io2 = ((os != null) ? IO.close(os) : null);

    return ((io1 != null) ? io1 : io2);
  }

  /**
   * Parse a file with a sax parser using this method. The sax-parser will
   * use the interfaces supplied by <code>implementor</code>. Use a
   * <code>SAXImplementor</code> object or one of its derivates here.
   * 
   * @param source
   *          The data source to read and parse data from. Can be a Reader,
   *          a File, an InputSource or an InputStream.
   * @param implementor
   *          A SAXImplementor object that comes with all the standard sax
   *          handlers. The generated events will all be tunnelled to that
   *          object.
   * @return a possible exception that occured, or <code>null</code> if
   *         no exception occured
   */
  public static final Exception saxParse(final Object source,
      final Object implementor) {
    XMLReader reader;
    InputSource is;
    IOException ioex;

    try {
      is = getInputSource(source);

      if (is != null) {
        try {
          reader = PARSER_FACTORY.newSAXParser().getXMLReader();
          installTo(reader, implementor);
          reader.parse(is);
        } finally {
          try {
            ioex = close(is);
            if (ioex != null)
              throw ioex;
          } catch (AlreadyDisposedException ade) {
            // This will normally happen
          }
        }
      }
    } catch (Exception e) {
      return e;
    } finally {
      ioex = close(source);
      if (ioex != null)
        return ioex;
    }

    return null;
  }

  /**
   * Parse a file with a sax parser using this method. The sax-parser will
   * use the interfaces supplied by <code>implementor</code>. Use a
   * <code>ContentHandlerImplementor</code> object or one of its
   * derivates here.
   * 
   * @param source
   *          The data source to read and parse data from. Can be a Reader,
   *          a File, an InputSource or an InputStream.
   * @param implementor
   *          A ContentHandlerImplementor object that comes with all the
   *          standard sax handlers. The generated events will all be
   *          tunnelled to that object.
   * @return a possible exception that occured, or <code>null</code> if
   *         no exception occured
   */
  public static final Exception saxParseContentOnly(final Object source,
      final ContentHandler implementor) {
    XMLReader reader;
    InputSource is;
    IOException ioex;

    try {
      is = getInputSource(source);

      if (is != null) {
        try {
          reader = PARSER_FACTORY.newSAXParser().getXMLReader();
          reader.setContentHandler(implementor);
          reader.parse(is);
        } finally {
          try {
            ioex = close(is);
            if (ioex != null)
              throw ioex;
          } catch (AlreadyDisposedException ade) {
            // This will normally happen
          }
        }
      }
    } catch (Exception e) {
      return e;
    } finally {
      ioex = close(source);
      if (ioex != null)
        return ioex;
    }

    return null;
  }

  /**
   * This method transforms the xml data <code>p_xml_source_data</code>
   * with the xslt style sheet <code>p_xsltTransformData</code> and
   * pushes the result into the destionation <code>p_xml_dest_data</code>.
   * All those objects can be files, streams or file names, their type will
   * automatically be detected and handled.
   * 
   * @param xmlSourceData
   *          The source xml data to be transformed.
   * @param xsltTransformData
   *          The xslt style sheet to transform the xml source data.
   * @param xmlDestData
   *          The destination to put the resulting data in.
   * @param resolver
   *          An optional resolver for uris. This might be needed if one of
   *          the data sources' origin is a stream inside another jar
   *          archive, for example. You can leave this parameter null if
   *          you don't want to specify an external uri resolver.
   * @return a possible exception that occured, or <code>null</code> if
   *         no exception occured
   */
  public static final Exception xsltTransform(final Object xmlSourceData,
      final Object xsltTransformData, final Object xmlDestData,
      final URIResolver resolver) {
    Source xmsource, xslt_source;
    Result dest;
    TransformerFactory tf;
    Transformer t;
    IOException ioex, ioex2;

    try {

      dest = getResult(xmlDestData);

      if (dest != null) {
        try {
          xmsource = getSource(xmlSourceData);
          if (xmsource != null) {
            try {
              xslt_source = getSource(xsltTransformData);
              if (xslt_source != null) {
                try {
                  if (resolver != null) {
                    tf = TransformerFactory.newInstance();
                    tf.setURIResolver(resolver);
                  } else {
                    tf = TRANSFORMER_FACTORY;
                  }

                  t = tf.newTransformer(xslt_source);

                  if (t != null) {
                    t.transform(xmsource, dest);
                  }
                } catch (Exception e) {
                  return e;
                } finally {
                  ioex = close(xslt_source);
                  if (ioex != null)
                    return ioex;
                }
              }
            } finally {
              ioex = close(xmsource);
              if (ioex != null)
                return ioex;
            }
          }
        } finally {
          ioex = close(dest);
          if (ioex != null)
            return ioex;
        }
      }
    } finally {
      ioex = close(xmlSourceData);
      ioex2 = close(xsltTransformData);
      if (ioex2 != null)
        ioex = ioex2;
      ioex2 = close(xmlDestData);
      if (ioex2 != null)
        return ioex2;
      if (ioex != null)
        return ioex;
    }
    return null;
  }
}
