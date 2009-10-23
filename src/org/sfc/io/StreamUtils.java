/*
 * Copyright (c) 2007 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-10-04
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.io.StreamUtils.java
 * Last modification: 2007-10-04
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

import java.io.InputStream;
import java.io.OutputStream;

import org.sfc.utils.ErrorUtils;

/**
 * the stream utils class
 * 
 * @author Thomas Weise
 */
public final class StreamUtils {

  /**
   * Read (and dismiss) all the (ascii) input from a stream
   * 
   * @param is
   *          the input stream to read from
   */
  public static final void readInput(final InputStream is) {
    int i;

    try {
      while ((i = is.available()) > 0)
        for (; i > 0; i--) {
          is.read();
        }
    } catch (Throwable tt) {
      ErrorUtils.onError(tt);
    }
  }

  /**
   * Read all the (ascii) input from a stream
   * 
   * @param is
   *          the input stream to read from
   * @param sb
   *          the string builder to write to
   */
  public static final void readAsciiInput(final InputStream is,
      final StringBuilder sb) {
    int i;

    if (sb == null)
      readInput(is);
    else
      try {
        while ((i = is.available()) > 0)
          for (; i > 0; i--) {
            sb.append((char) (is.read()));
          }
      } catch (Throwable tt) {
        ErrorUtils.onError(tt);
      }
  }

  /**
   * Write the given ascii output to the output stream while polling the
   * given input stream (useful for console interaction)
   * 
   * @param os
   *          the output sream
   * @param is
   *          the input stream to poll
   * @param output
   *          the stuff to write
   */
  public static final void writeAsciiOutput(final OutputStream os,
      final InputStream is, final String output) {
    int i;

    try {
      readInput(is);
      for (i = 0; i < output.length(); i++) {
        os.write(output.charAt(i));
        os.flush();
        readInput(is);
      }
    } catch (Throwable tt) {
      ErrorUtils.onError(tt);
    }
  }

  /**
   * Write the given ascii output to the output stream while polling the
   * given input stream (useful for console interaction)
   * 
   * @param os
   *          the output sream
   * @param is
   *          the input stream to poll
   * @param is2
   *          the second input stream to poll
   * @param output
   *          the stuff to write
   */
  public static final void writeAsciiOutput(final OutputStream os,
      final InputStream is, final InputStream is2, final String output) {
    int i;

    try {
      readInput(is);
      readInput(is2);
      for (i = 0; i < output.length(); i++) {
        os.write(output.charAt(i));
        os.flush();
        readInput(is);
        readInput(is2);
      }
    } catch (Throwable tt) {
      ErrorUtils.onError(tt);
    }
  }

  /**
   * Write the given ascii output to the output stream while polling the
   * given input stream (useful for console interaction)
   * 
   * @param os
   *          the output sream
   * @param is
   *          the input stream to poll
   * @param output
   *          the stuff to write
   * @param sb
   *          the string builder to write the output to
   */
  public static final void writeAsciiOutput(final OutputStream os,
      final InputStream is, final String output, final StringBuilder sb) {
    int i;

    if (sb == null)
      writeAsciiOutput(os, is, output);
    else
      try {
        readAsciiInput(is, sb);
        for (i = 0; i < output.length(); i++) {
          os.write(output.charAt(i));
          os.flush();
          readAsciiInput(is, sb);
        }
      } catch (Throwable tt) {
        ErrorUtils.onError(tt);
      }
  }

  /**
   * Write the given ascii output to the output stream while polling the
   * given input stream (useful for console interaction)
   * 
   * @param os
   *          the output sream
   * @param is
   *          the input stream to poll
   * @param is2
   *          the second input stream to poll
   * @param output
   *          the stuff to write
   * @param sb
   *          the string builder to write the output to
   */
  public static final void writeAsciiOutput(final OutputStream os,
      final InputStream is, final InputStream is2, final String output,
      final StringBuilder sb) {
    int i;

    if (sb == null)
      writeAsciiOutput(os, is, is2, output);
    else
      try {
        readAsciiInput(is, sb);
        readAsciiInput(is2, sb);
        for (i = 0; i < output.length(); i++) {
          os.write(output.charAt(i));
          os.flush();
          readAsciiInput(is, sb);
          readAsciiInput(is2, sb);
        }
      } catch (Throwable tt) {
        ErrorUtils.onError(tt);
      }
  }

  /**
   * do not call
   */
  private StreamUtils() {
    ErrorUtils.doNotCall();
  }
}
