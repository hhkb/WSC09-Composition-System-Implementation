/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.io.IO.java
 * Last modification: 2006-11-26
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

package org.sfc.io;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URI;
import java.net.URL;

import org.sfc.scoped.IReferenceCounted;
import org.sfc.security.CallStack;
import org.sfc.security.SfcSecurityManager;
import org.sfc.utils.ErrorUtils;

/**
 * The class <code>IO</code> comes with easy-to-use methods to access
 * files and streams.
 * 
 * @author Thomas Weise
 */
public final class IO {
  /**
   * The file protocol.
   */
  private static final String FILE_PROTOCOL = "file";//$NON-NLS-1$

  /**
   * The trial constant for files
   */
  private static final int TRY_FILE = 1;

  /**
   * The trial constant for byte streams, such as <code>InputStream</code>
   * and <code>OutputStream</code>.
   */
  private static final int TRY_BYTE_STREAM = (TRY_FILE << 1);

  /**
   * The trial constant for character streams, such as <code>Reader</code>
   * and <code>Writer</code>.
   */
  private static final int TRY_CHAR_STREAM = (TRY_BYTE_STREAM << 1);

  /**
   * The trial constant for urls.
   */
  private static final int TRY_URL = (TRY_CHAR_STREAM << 1);

  /**
   * The trial constant for uris.
   */
  private static final int TRY_URI = (TRY_URL << 1);

  /**
   * The trial constant for stream sources.
   */
  private static final int TRY_STREAM_SOURCE = (TRY_URI << 1);

  /**
   * The trial constant for stream results.
   */
  private static final int TRY_STREAM_RESULT = (TRY_STREAM_SOURCE << 1);

  /**
   * The trial constant for input sources.
   */
  private static final int TRY_INPUT_SOURCE = (TRY_STREAM_RESULT << 1);

  /**
   * The trial constant for resource urls.
   */
  private static final int TRY_RESOURCE_URL = (TRY_INPUT_SOURCE << 1);

  /**
   * The trial constant for resource streams.
   */
  private static final int TRY_RESOURCE_STREAM = (TRY_RESOURCE_URL << 1);

  /**
   * The class relay count when requesting resources: org.sfc.io.IO
   * org.sfc.io.IO
   */
  static final int CLASS_RELAY_COUNT = 2;

  /**
   * Create a new <code>IO</code> instance. You can do this - i can.
   */
  private IO() {
    ErrorUtils.doNotCall();
  }

  /**
   * Returns the file representation of an object.
   * 
   * @param object
   *          The object to get the file representation of.
   * @return The file representation of the object or null, if none could
   *         be found.
   */
  public static final CanonicalFile getFile(final Object object) {
    return doGetFile(object, -1);
  }

  /**
   * Returns the input stream representation of an object. The return value
   * is an instance of <code>IReferenceCounted</code>. You must close it
   * using the <code>relase()</code>-method. The initial reference count
   * is always <code>1</code>, so calling <code>release()</code> on
   * the return value will always lead to the the stream being closed.
   * 
   * @param object
   *          The object to get the input stream representation of.
   * @return The input stream representation of the object or null, if none
   *         could be found.
   */
  public static final ReferenceCountedInputStream getInputStream(
      final Object object) {
    return doGetInputStream(object, -1);
  }

  /**
   * Returns the output stream representation of an object. The return
   * value is an instance of <code>IReferenceCounted</code>. You must
   * close it using the <code>relase()</code>-method. The initial
   * reference count is always <code>1</code>, so calling
   * <code>release()</code> on the return value will always lead to the
   * the stream being closed.
   * 
   * @param object
   *          The object to get the output stream representation of.
   * @return The output stream representation of the object or null, if
   *         none could be found.
   */
  public static final ReferenceCountedOutputStream getOutputStream(
      final Object object) {
    return doGetOutputStream(object, -1);
  }

  /**
   * Returns the reader representation of an object. The return value is an
   * instance of <code>IReferenceCounted</code>. You must close it using
   * the <code>relase()</code>-method. The initial reference count is
   * always <code>1</code>, so calling <code>release()</code> on the
   * return value will always lead to the the stream being closed.
   * 
   * @param object
   *          The object to get the reader representation of.
   * @return The reader representation of the object or null, if none could
   *         be found.
   */
  public static final ReferenceCountedReader getReader(final Object object) {
    return doGetReader(object, -1);
  }

  /**
   * Returns the writer representation of an object. The return value is an
   * instance of <code>IReferenceCounted</code>. You must close it using
   * the <code>relase()</code>-method. The initial reference count is
   * always <code>1</code>, so calling <code>release()</code> on the
   * return value will always lead to the the stream being closed.
   * 
   * @param object
   *          The object to get the writer representation of.
   * @param preferedEncoding
   *          The prefered encoding, UTF-8, for example, or
   *          <code>null</code> if it plays no role.
   * @return The writer representation of the object or null, if none could
   *         be found.
   * @see #getWriter(Object)
   */
  public static final ReferenceCountedWriter getWriter(
      final Object object, final String preferedEncoding) {
    return doGetWriter(object, -1, preferedEncoding);
  }

  /**
   * Returns the writer representation of an object. The return value is an
   * instance of <code>IReferenceCounted</code>. You must close it using
   * the <code>relase()</code>-method. The initial reference count is
   * always <code>1</code>, so calling <code>release()</code> on the
   * return value will always lead to the the stream being closed. * doG
   * doG#release()
   * 
   * @param object
   *          The object to get the writer representation of.
   * @return The writer representation of the object or null, if none could
   *         be found.
   * @see #getWriter(Object, String)
   */
  public static final ReferenceCountedWriter getWriter(final Object object) {
    return doGetWriter(object, -1, null);
  }

  /**
   * Returns the uri representation of an object.
   * 
   * @param object
   *          The object to get the uri representation of.
   * @return The uri representation of the object or null, if none could be
   *         found.
   */
  public static final URI getUri(final Object object) {
    return doGetUri(object, -1);
  }

  /**
   * Returns the url representation of an object.
   * 
   * @param object
   *          The object to get the url representation of.
   * @return The url representation of the object or null, if none could be
   *         found.
   */
  public static final URL getUrl(final Object object) {
    return doGetUrl(object, -1);
  }

  /**
   * Returns the resource url representation of an object.
   * 
   * @param object
   *          The object to get the resource url representation of.
   * @param additional
   *          The additional non-resource related classes.
   * @return The resource url representation of the object or null, if none
   *         could be found.
   */
  public static final URL getResourceUrl(final Object object,
      final int additional) {
    return doGetResourceUrl(object, -1, additional);
  }

  /**
   * Returns the resource url representation of an object.
   * 
   * @param object
   *          The object to get the resource url representation of.
   * @return The resource url representation of the object or null, if none
   *         could be found.
   */
  public static final URL getResourceUrl(final Object object) {
    return getResourceUrl(object, 1);
  }

  /**
   * Returns the resource stream representation of an object. The return
   * value is an instance of <code>IReferenceCounted</code>. You must
   * close it using the <code>relase()</code>-method. The initial
   * reference count is always <code>1</code>, so calling
   * <code>release()</code> on the return value will always lead to the
   * the stream being closed. * doG doG#release()
   * 
   * @param object
   *          The object to get the resource stream representation of.
   * @param additional
   *          The additional non-resource related classes.
   * @return The resource stream representation of the object or null, if
   *         none could be found.
   */
  public static final ReferenceCountedInputStream getResourceInputStream(
      final Object object, final int additional) {
    return doGetResourceInputStream(object, -1, additional);
  }

  /**
   * Returns the resource stream representation of an object. The return
   * value is an instance of <code>IReferenceCounted</code>. You must
   * close it using the <code>relase()</code>-method. The initial
   * reference count is always <code>1</code>, so calling
   * <code>release()</code> on the return value will always lead to the
   * the stream being closed. * doG doG#release()
   * 
   * @param object
   *          The object to get the resource stream representation of.
   * @return The resource stream representation of the object or null, if
   *         none could be found.
   */
  public static final ReferenceCountedInputStream getResourceInputStream(
      final Object object) {
    return getResourceInputStream(object, 1);
  }

  /**
   * Returns the file representation of an object.
   * 
   * @param object
   *          The object to get the file representation of.
   * @param trials
   *          The transformations that still can be tried out.
   * @return The file representation of the object or null, if none could
   *         be found.
   */
  static final CanonicalFile doGetFile(final Object object,
      final int trials) {
    String s;
    File f;
    URI u;
    URL u2;
    int t;

    if ((object == null) || ((trials & TRY_FILE) == 0))
      return null;

    t = (trials ^ TRY_FILE);

    abc: {
      if (object instanceof File) {
        f = ((File) object);
        break abc;
      }

      u = doGetUri(object, t);
      if (u != null) {
        try {
          f = new File(u);
          break abc;
        } catch (Throwable tx) {
          //
        }
      }

      u2 = doGetUrl(object, t);
      if ((u2 != null)
          && (FILE_PROTOCOL.equalsIgnoreCase(u2.getProtocol()))) {
        s = u2.getPath();
        if (s != null) {
          f = new File(normalize(s));
          break abc;
        }
      }

      if (object instanceof String) {
        f = new File((String) object);
        break abc;
      }

      return null;
    }

    return CanonicalFile.canonicalize(f);
  }

  /**
   * Returns the input stream representation of an object.
   * 
   * @param object
   *          The object to get the input stream representation of.
   * @param trials
   *          The transformations that still can be tried out.
   * @return The input stream representation of the object or null, if none
   *         could be found.
   */
  static final ReferenceCountedInputStream doGetInputStream(
      final Object object, final int trials) {
    InputStream is;
    File file;
    URL url;
    int t;

    if ((object == null) || ((trials & TRY_BYTE_STREAM) == 0))
      return null;

    t = (trials ^ TRY_BYTE_STREAM);

    abc: {
      if (object instanceof InputStream) {
        if (object instanceof ReferenceCountedInputStream) {
          return ((ReferenceCountedInputStream) object);
        }
        is = ((InputStream) object);
        break abc;
      }

      file = doGetFile(object, t);
      if (file != null) {
        try {
          is = new FileInputStream(file);
          break abc;
        } catch (Throwable tx) {
          //
        }
      }

      url = doGetUrl(object, t);
      if (url != null) {
        try {
          is = url.openStream();
          if (is != null)
            break abc;
        } catch (Throwable tx) {
          //
        }

        try {
          is = url.openConnection().getInputStream();
          if (is != null)
            break abc;
        } catch (Throwable tx) {
          //
        }
      }

      if (object instanceof String) {
        try {
          is = new FileInputStream((String) object);
          break abc;
        } catch (Throwable tx) {
          //
        }
      }

      return null;
    }

    return new DefaultInputStream(is);
  }

  /**
   * Returns the output stream representation of an object.
   * 
   * @param object
   *          The object to get the output stream representation of.
   * @param trials
   *          The transformations that still can be tried out.
   * @return The output stream representation of the object or null, if
   *         none could be found.
   */
  static final ReferenceCountedOutputStream doGetOutputStream(
      final Object object, int trials) {
    OutputStream os;
    File file;
    URL url;
    int t;

    if ((object == null) || ((trials & TRY_BYTE_STREAM) == 0))
      return null;

    t = (trials ^ TRY_BYTE_STREAM);

    abc: {
      if (object instanceof OutputStream) {
        if (object instanceof ReferenceCountedOutputStream) {
          return ((ReferenceCountedOutputStream) object);
        }
        os = ((OutputStream) object);
        break abc;
      }

      file = doGetFile(object, t);
      if (file != null) {
        try {
          os = new FileOutputStream(file);
          break abc;
        } catch (Throwable tx) {
          //            
        }
      }

      url = doGetUrl(object, t);
      if (url != null) {
        try {
          os = url.openConnection().getOutputStream();
          if (os != null)
            break abc;
        } catch (Throwable tx) {
          //
        }
      }

      if (object instanceof String) {
        try {
          os = new FileOutputStream((String) object);
          break abc;
        } catch (Throwable tx) {
          //
        }
      }

      return null;
    }

    return new DefaultOutputStream(os);
  }

  /**
   * Returns the reader representation of an object.
   * 
   * @param object
   *          The object to get the reader representation of.
   * @param trials
   *          The transformations that still can be tried out.
   * @return The reader representation of the object or null, if none could
   *         be found.
   */
  static final ReferenceCountedReader doGetReader(final Object object,
      final int trials) {
    File file;
    Reader r;
    InputStream is2;
    int t;

    if ((object == null) || ((trials & TRY_CHAR_STREAM) == 0))
      return null;

    t = (trials ^ TRY_CHAR_STREAM);

    abc: {
      if (object instanceof Reader) {
        if (object instanceof ReferenceCountedReader) {
          return ((ReferenceCountedReader) object);
        }
        r = ((Reader) object);
        break abc;
      }

      file = doGetFile(object, t);
      if (file != null) {
        try {
          r = new FileReader(file);
          break abc;
        } catch (Throwable tx) {
          //
        }
      }

      is2 = doGetInputStream(object, t);
      if (is2 != null) {
        r = new InputStreamReader(is2);
        break abc;
      }

      return null;
    }

    return new DefaultReader(r);
  }

  /**
   * Returns the writer representation of an object.
   * 
   * @param object
   *          The object to get the writer representation of.
   * @param trials
   *          The transformations that still can be tried out.
   * @param preferedEncoding
   *          The prefered encoding, UTF-8, for example, or
   *          <code>null</code> if it plays no role.
   * @return The writer representation of the object or null, if none could
   *         be found.
   */
  static final ReferenceCountedWriter doGetWriter(final Object object,
      final int trials, final String preferedEncoding) {
    File file;
    Writer w;
    OutputStream os;
    int t;

    if ((object == null) || ((trials & TRY_CHAR_STREAM) == 0))
      return null;

    t = (trials ^ TRY_CHAR_STREAM);
    w = null;

    abc: {
      if (object instanceof Writer) {
        if (object instanceof ReferenceCountedWriter) {
          return ((ReferenceCountedWriter) object);
        }

        w = ((Writer) object);
        break abc;
      }

      os = doGetOutputStream(object, t);
      if (os != null) {
        if (preferedEncoding != null) {
          try {
            w = new OutputStreamWriter(os, preferedEncoding);
          } catch (UnsupportedEncodingException e) {
            //
          }
        }

        if (w == null)
          w = new OutputStreamWriter(os);
        break abc;
      }

      file = doGetFile(object, t);
      if (file != null) {
        try {
          w = new FileWriter(file);
          break abc;
        } catch (Throwable tx) {
          //
        }
      }

      return null;
    }

    return new DefaultWriter(w);
  }

  /**
   * Returns the url representation of an object.
   * 
   * @param object
   *          The object to get the url representation of.
   * @param trials
   *          The transformations that still can be tried out.
   * @return The url representation of the object or null, if none could be
   *         found.
   */
  static final URL doGetUrl(final Object object, final int trials) {
//    File f;
    URL u;
    URI u2;
    int t;

    if ((object == null) || ((trials & TRY_URL) == 0))
      return null;

    t = (trials ^ TRY_URL);

    if (object instanceof URL) {
      return ((URL) object);
    }

    if (object instanceof String) {
      try {
        return new URL((String) object);
      } catch (Throwable tx) {
        //
      }
    }

    // Since Java 1.6, toURL() is deprecated
    // f = doGetFile(object, t);
    // if (f != null) {
    // try {
    // u = f.toURL();
    // if (u != null)
    // return u;
    // } catch (Throwable tx) {
    // //
    // }
    // }

    u2 = doGetUri(object, t);
    if (u2 != null) {
      try {
        u = u2.toURL();
        if (u != null)
          return u;
      } catch (Throwable tx) {
        //
      }
    }

    return null;
  }

  /**
   * Returns the uri representation of an object.
   * 
   * @param object
   *          The object to get the uri representation of.
   * @param trials
   *          The transformations that still can be tried out.
   * @return The uri representation of the object or null, if none could be
   *         found.
   */
  static final URI doGetUri(final Object object, final int trials) {
    File f;
    URI u;
    URL u2;
    int t;

    if ((object == null) || ((trials & TRY_URI) == 0))
      return null;

    t = (trials ^ TRY_URI);

    if (object instanceof URI) {
      return ((URI) object);
    }

    if (object instanceof String) {
      try {
        return new URI((String) object);
      } catch (Throwable tx) {
        //
      }
    }

    u2 = doGetUrl(object, t);
    if (u2 != null) {
      try {
        return new URI(u2.toExternalForm());
      } catch (Throwable tx) {
        //
      }
    }

    f = doGetFile(object, t);
    if (f != null) {
      u = f.toURI();
      if (u != null)
        return u;
    }

    return null;
  }

  /**
   * Returns the url representation of an object.
   * 
   * @param object
   *          The object to get the url representation of.
   * @param trials
   *          The transformations that still can be tried out.
   * @param additional
   *          The additional non-resource related classes.
   * @return The url representation of the object or null, if none could be
   *         found.
   */
  static final URL doGetResourceUrl(final Object object, final int trials,
      final int additional) {
    String s;
    CallStack cx;
    URL u;
    int i, j, t;

    if ((object == null) || ((trials & TRY_RESOURCE_URL) == 0))
      return null;
    t = (trials ^ TRY_RESOURCE_URL);

    if (object instanceof String) {
      s = ((String) object);
      cx = SfcSecurityManager.getCurrentCallStack(CLASS_RELAY_COUNT
          + additional);

      if (cx != null) {
        j = cx.size();
        for (i = 0; i < j; i++) {
          try {
            u = cx.get(i).getResource(s);
            if (u != null)
              return u;
          } catch (Throwable tx) {
            //
          }
        }
      }
    }

    return doGetUrl(object, t);
  }

  /**
   * Returns the resource input stream representation of an object.
   * 
   * @param object
   *          The object to get the input stream representation of.
   * @param trials
   *          The transformations that still can be tried out.
   * @param additional
   *          The additional non-resource related classes.
   * @return The input stream representation of the object or null, if none
   *         could be found.
   */
  static final ReferenceCountedInputStream doGetResourceInputStream(
      final Object object, final int trials, final int additional) {
    String s;
    InputStream is;
    int i, j, t;
    CallStack cx;

    if ((object == null) || ((trials & TRY_RESOURCE_STREAM) == 0)) {
      return null;
    }

    t = (trials ^ TRY_RESOURCE_STREAM);

    abc: {
      is = null;
      if (object instanceof String) {
        s = ((String) object);
        cx = SfcSecurityManager.getCurrentCallStack(CLASS_RELAY_COUNT
            + additional);
        if (cx != null) {
          j = cx.size();
          for (i = 0; i < j; i++) {
            try {
              is = cx.get(i).getResourceAsStream(s);
              if (is != null)
                break abc;
            } catch (Throwable tx) {
              //
            }
          }
        }
      }
    }

    if (is != null)
      return new DefaultInputStream(is);

    return doGetInputStream(object, t);
  }

  /**
   * This method normalizes / and \ that occure inside a string that could
   * be a file name.
   * 
   * @param string
   *          The string to work on.
   * @return The normalized string.
   */
  private static final String normalize(final String string) {
    if (File.separatorChar != '/') {
      return string.replace('/', File.separatorChar);
    }

    if ((File.separatorChar != '\\') && (string.indexOf(":\\") > 0))//$NON-NLS-1$
    {
      return string.replace('\\', File.separatorChar);
    }

    return string;
  }

  /**
   * Close the io-object denoted by <code>object</code>.
   * 
   * @param object
   *          The object to be closed.
   * @return An IOException that occured when closing the object.
   */
  public static final IOException close(final Object object) {
    try {
      if (object instanceof InputStream) {
        ((InputStream) object).close();
      } else {
        if (object instanceof OutputStream) {
          ((OutputStream) object).close();
        } else {
          if (object instanceof Reader) {
            ((Reader) object).close();
          } else {
            if (object instanceof Writer) {
              ((Writer) object).close();
            } else {
              if (object instanceof IReferenceCounted) {
                ((IReferenceCounted) object).release();
              } else {
                if (object instanceof Closeable) {
                  ((Closeable) object).close();
                }

              }
            }
          }
        }
      }

    } catch (IOException ioe) {
      return ioe;
    }
    return null;
  }
}
