/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 *
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.io.Base64.java
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

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Arrays;

import org.sfc.utils.ErrorUtils;

/**
 * A small utility for base64 encoding and decoding. It uses the RFC 2045
 * for internet message bodies to convert binary data to a representation
 * that will not be compromised by any transfer mechanism that would
 * probably obscure control characters and such and such.
 * 
 * @author Thomas Weise
 */
public class Base64 {
  /**
   * The preferred encoding for base64-data.
   */
  private static final String PREFERED_ENCODING = "UTF-8"; //$NON-NLS-1$

  /**
   * The default table to cenvert binary data to base 64 data.
   */
  private static final char[] DEFAULT_TABLE = { 'A', 'B', 'C', 'D', 'E',
      'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
      'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e',
      'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
      's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4',
      '5', '6', '7', '8', '9', '+', '/' };

  // /**
  // * This array is a lookup table that translates 6-bit positive integer
  // * index values into their "Alternate Base64 Alphabet" equivalents.
  // * This is NOT the real Base64 Alphabet as per in Table 1 of RFC 2045.
  // * This alternate alphabet does not use the capital letters. It is
  // * designed for use in environments where "case folding" occurs.
  // */
  // private static final char[] ALTERNATE_TABLE = {
  // '!', '"', '#', '$', '%', '&', '\'', '(', ')', ',', '-', '.', ':',
  // ';', '<', '>', '@', '[', ']', '^', '`', '_', '{', '|', '}', '~',
  // 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
  // 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
  // '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '?' };

  /**
   * The default padding character.
   */
  private static final char DEFAULT_PADDING = '=';

  /**
   * The default line break character sequence.
   */
  private static final char[] DEFAULT_LINE_BREAK = new char[] { '\r', '\n' };

  /**
   * The default line length.
   */
  private static final int DEFAULT_LINE_LENGTH = 48;

  /**
   * The empty char array.
   */
  private static final char[] EMPTY_CHARS = new char[0];

  /**
   * The empty byte array.
   */
  private static final byte[] EMPTY_BYTES = new byte[0];

  /**
   * The default line header.
   */
  private static final char[] DEFAULT_LINE_HEADER = EMPTY_CHARS;

  /**
   * The default base64 encoder/decoder.
   */
  public static final Base64 DEFAULT = new Base64(DEFAULT_TABLE,
      DEFAULT_PADDING, DEFAULT_LINE_BREAK, DEFAULT_LINE_LENGTH,
      DEFAULT_LINE_HEADER);

  /**
   * This encoder creates "small" output, without any line breaking or line
   * heading. It should be used whenever the content will never be included
   * into any source or file which could be viewed by a human.
   */
  public static final Base64 SMALL = new Base64(DEFAULT_TABLE,
      DEFAULT_PADDING, null, -1, null);

  /**
   * The array used for binary-to-character conversation.
   */
  final char[] m_binToChar;

  /**
   * The table for character-to-binary conversation.
   */
  final int[] m_charToBin;

  /**
   * The padding character.
   */
  final char m_padding;

  /**
   * The line break.
   */
  final char[] m_lineBreak;

  /**
   * The maximum line length.
   */
  final int m_maxLineLength;

  /**
   * The line header.
   */
  final char[] m_lineHeader;

  /**
   * Create a new Base64 encoder.
   * 
   * @param binToChar
   *          The array used for binary-to-character encoding. This array
   *          must contain exactly 64 characters. If this parameter is
   *          null, the default base 64 encoding table will be used.
   * @param padding
   *          The padding character to be used at the end of sequences with
   *          lengths % 3 != 0. If 0 is passed, the default padding will be
   *          used. This character must no equal any of the characters the
   *          translation table <code>binToChar</code>.
   * @param lineBreak
   *          The character sequence to be inserted as line break. This
   *          parameter can be null, in that case the default line break
   *          will be used. Line breaking will only occure when encoding
   *          and maxLineLength is > 0.
   * @param maxLineLength
   *          The maximum length of a line in characters. If this parameter
   *          is <= 0, no line breaking will be performed. The line length
   *          only regards the count of data character output, neither the
   *          line header nor the line break characters.
   * @param lineHeader
   *          A character sequence preceeding every line of output. If this
   *          is null, no preceeding character sequence will be attached to
   *          the front of the output lines. If a sequence of characters is
   *          provided, make sure that it neither contains the padding
   *          character nor any of the characters the translation table
   *          <code>binToChar</code>.
   */
  public Base64(final char[] binToChar, final char padding,
      final char[] lineBreak, final int maxLineLength,
      final char[] lineHeader) {
    super();

    int i, k, l, lmaxLineLength;
    char[] lbinToChar, llineBreak, llineHeader;
    char lpadding;

    if (lineHeader == null)
      llineHeader = DEFAULT_LINE_HEADER;
    else
      llineHeader = lineHeader;
    if (lineBreak == null)
      llineBreak = DEFAULT_LINE_BREAK;
    else
      llineBreak = lineBreak;
    if (maxLineLength <= 0)
      lmaxLineLength = -1;
    else
      lmaxLineLength = maxLineLength;
    if (padding == '\0')
      lpadding = DEFAULT_PADDING;
    else
      lpadding = padding;

    if (binToChar == null) {
      lbinToChar = DEFAULT_TABLE;
    } else {
      if (binToChar.length != 64) {
        throw new IllegalArgumentException();
      }
      lbinToChar = binToChar;
    }

    this.m_binToChar = lbinToChar;

    k = (lbinToChar[0] & 0xff);
    for (i = 63; i > 0; i--) {
      l = (lbinToChar[i] & 0xff);
      if (l > k)
        k = l;
    }

    this.m_charToBin = new int[k + 1];

    for (i = k; i >= 0; i--) {
      this.m_charToBin[i] = -1;
    }

    for (i = 63; i >= 0; i--) {
      this.m_charToBin[lbinToChar[i] & 0xff] = i;
    }

    this.m_padding = lpadding;
    i = llineHeader.length;

    if (i > 0) {
      this.m_lineHeader = llineHeader;
      k = llineBreak.length;
      llineHeader = new char[k + i];
      System.arraycopy(llineBreak, 0, llineHeader, 0, k);
      System.arraycopy(this.m_lineHeader, 0, llineHeader, k, i);
      llineBreak = llineHeader;
    } else {
      this.m_lineHeader = null;
    }

    this.m_lineBreak = llineBreak;
    this.m_maxLineLength = lmaxLineLength;
  }

  /**
   * Returns a copy of the lookup table for binary-to-character
   * conversation.
   * 
   * @return The lookup table for binary-to-character conversation.
   */
  public char[] getBinToCharLookup() {
    return this.m_binToChar.clone();
  }

  /**
   * Returns a copy of the lookup table for character-to-binary
   * conversation.
   * 
   * @return The lookup table for character-to-binary conversation.
   */
  public int[] getCharToBinLookup() {
    return this.m_charToBin.clone();
  }

  /**
   * Returns the padding that will be appended on odd data lengths (% 3 !=
   * 0).
   * 
   * @return The padding that will be appended on odd data lengths (% 3 !=
   *         0).
   */
  public char getPadding() {
    return this.m_padding;
  }

  /**
   * Returns a copy of the line break character sequence-
   * 
   * @return A copy of the line break character sequence-
   */
  public char[] getLineBreak() {
    return this.m_lineBreak.clone();
  }

  /**
   * Returns the maximum length of a line in characters.
   * 
   * @return The maximum length of a line in characters.
   */
  public int getMaxLineLength() {
    return this.m_maxLineLength;
  }

  /**
   * Get a copy of the character sequence that will preceed any data in
   * every line of the output.
   * 
   * @return The character sequence that will preceed any data in every
   *         line of the output.
   */
  public char[] getLineHeader() {
    return this.m_lineHeader.clone();
  }

  /**
   * You can encode an array of byte to an char array base64 with this one.
   * Line breaks are inserted to apply the "maximum 76 characters per
   * line"- limit of rfc 2045. (see the section regarding that below) To
   * increase conversion speed, <code>this.m_maxLineLength</code> set by
   * the constructor parameter <code>maxLineLength</code> is approximated
   * with an accurancy of +3, meaning your line could contain more base64
   * characters then specified, but at most 3. If you want a line length
   * that is more accurate, you have to use the encoders created by
   * <code>create_encoder()</code>.
   * 
   * @param data
   *          The data array.
   * @param start
   *          The starting offset into this array, where to start encoding.
   * @param length
   *          The count of bytes to encode.
   * @return An array of char containing the base64 encoded data.
   * @see #Base64(char[], char, char[], int, char[])
   * @see #createEncoder(Writer)
   * @see #m_maxLineLength
   */
  public char[] encode(final byte[] data, final int start, final int length) {
    // int i, j, l, rest, lint, k, x, s;
    // char[] ret, lineHeader;
    //
    // if ((start > (i = data.length)) || (length <= 0)) {
    // return EMPTY_CHARS;
    // }
    //
    // if (start < 0)
    // s = 0;
    // else
    // s = start;
    //
    // lineHeader = this.m_lineHeader;
    //
    // i -= s;
    // if (i > length)
    // i = length;
    //
    // j = ((rest = (i % 3)) > 0) ? 1 : 0;
    // j += i /= 3;
    //
    // x = (this.m_maxLineLength >> 2);
    // if (x > 0) {
    // ret = new char[(((j * 4) + (((j - 1) / x) *
    // this.m_lineBreak.length)) + ((lineHeader != null) ?
    // lineHeader.length
    // : 0))];
    // } else {
    // ret = new char[((j * 4) + ((lineHeader != null) ? lineHeader.length
    // : 0))];
    // }
    //
    // if (lineHeader != null) {
    // System.arraycopy(this.m_lineHeader, 0, ret, 0,
    // this.m_lineHeader.length);
    // }
    //
    // l = 0;
    // for (j = s, k = 0; i > 0; i--) {
    // lint = (data[j++] & 0xff);
    // lint |= (data[j++] & 0xff) << 8;
    // lint |= (data[j++] & 0xff) << 16;
    //
    // ret[k++] = this.m_binToChar[lint & 0x3f];
    // ret[k++] = this.m_binToChar[(lint >> 6) & 0x3f];
    // ret[k++] = this.m_binToChar[(lint >> 12) & 0x3f];
    // ret[k++] = this.m_binToChar[(lint >> 18) & 0x3f];
    //
    // if ((++l) == x) {
    // System.arraycopy(this.m_lineBreak, 0, ret, k,
    // this.m_lineBreak.length);
    // k += this.m_lineBreak.length;
    // l = 0;
    // }
    // }
    //
    // if (rest > 0) {
    // lint = 0;
    // for (i = 0; i < rest; i++) {
    // lint |= (data[j++] & 0xff) << (8 * i);
    // }
    //
    // ret[k++] = this.m_binToChar[lint & 0x3f];
    // ret[k++] = this.m_binToChar[(lint >>> 6) & 0x3f];
    //
    // if (rest > 1) {
    // ret[k++] = this.m_binToChar[(lint >>> 12) & 0x3f];
    // } else {
    // ret[k++] = this.m_padding;
    // }
    //
    // ret[k++] = this.m_padding;
    // }
    //
    // return ret;
    return this.encode(data, start, length, null);
  }

  /**
   * Find out how many characters are needed to encode the specified bytes.
   * 
   * @param data
   *          The data array.
   * @param start
   *          The starting offset into this array, where to start encoding.
   * @param length
   *          The count of bytes to encode.
   * @return the count of characters needed for the encoding.
   */
  public int getEncodedSize(final byte[] data, final int start,
      final int length) {
    int i, j, x, s;
    char[] lineHeader;

    if ((start > (i = data.length)) || (length <= 0))
      return 0;

    if (start < 0)
      s = 0;
    else
      s = start;

    lineHeader = this.m_lineHeader;

    i -= s;
    if (i > length)
      i = length;

    j = (((i % 3)) > 0) ? 1 : 0;
    j += i /= 3;

    x = (this.m_maxLineLength >> 2);
    if (x > 0)
      return (((j * 4) + (((j - 1) / x) * this.m_lineBreak.length)) + ((lineHeader != null) ? lineHeader.length
          : 0));
    return ((j * 4) + ((lineHeader != null) ? lineHeader.length : 0));

  }

  /**
   * You can encode an array of byte to an char array base64 with this one.
   * Line breaks are inserted to apply the "maximum 76 characters per
   * line"- limit of rfc 2045. (see the section regarding that below) To
   * increase conversion speed, <code>this.m_maxLineLength</code> set by
   * the constructor parameter <code>maxLineLength</code> is approximated
   * with an accurancy of +3, meaning your line could contain more base64
   * characters then specified, but at most 3. If you want a line length
   * that is more accurate, you have to use the encoders created by
   * <code>create_encoder()</code>.
   * <p>
   * If the character array passed in is smaller than needed, a new one is
   * created. If it is larger than needed, the rest is filled up using
   * '\0'.
   * 
   * @param data
   *          The data array.
   * @param start
   *          The starting offset into this array, where to start encoding.
   * @param length
   *          The count of bytes to encode.
   * @param dest
   *          the destination character array, or <code>null</code> if a
   *          new one should be created
   * @return An array of char containing the base64 encoded data. This will
   *         be <code>dest</code> if it is not <code>null</code> and
   *         large enough to hold the data, or a new character array
   *         otherwise
   * @see #Base64(char[], char, char[], int, char[])
   * @see #createEncoder(Writer)
   * @see #m_maxLineLength
   */
  public char[] encode(final byte[] data, final int start,
      final int length, final char[] dest) {
    int i, j, l, rest, lint, k, x, s;
    char[] ret, lineHeader;

    if ((start > (i = data.length)) || (length <= 0)) {
      return EMPTY_CHARS;
    }

    if (start < 0)
      s = 0;
    else
      s = start;

    lineHeader = this.m_lineHeader;

    i -= s;
    if (i > length)
      i = length;

    j = ((rest = (i % 3)) > 0) ? 1 : 0;
    j += i /= 3;

    x = (this.m_maxLineLength >> 2);
    if (x > 0) {
      l = (((j * 4) + (((j - 1) / x) * this.m_lineBreak.length)) + ((lineHeader != null) ? lineHeader.length
          : 0));
    } else {
      l = ((j * 4) + ((lineHeader != null) ? lineHeader.length : 0));
    }
    if ((dest == null) || (dest.length < l)) {
      ret = new char[l];
    } else {
      ret = dest;
      Arrays.fill(dest, l, dest.length, ((char) 0));
    }

    if (lineHeader != null) {
      System.arraycopy(this.m_lineHeader, 0, ret, 0,
          this.m_lineHeader.length);
    }

    l = 0;
    for (j = s, k = 0; i > 0; i--) {
      lint = (data[j++] & 0xff);
      lint |= (data[j++] & 0xff) << 8;
      lint |= (data[j++] & 0xff) << 16;

      ret[k++] = this.m_binToChar[lint & 0x3f];
      ret[k++] = this.m_binToChar[(lint >> 6) & 0x3f];
      ret[k++] = this.m_binToChar[(lint >> 12) & 0x3f];
      ret[k++] = this.m_binToChar[(lint >> 18) & 0x3f];

      if ((++l) == x) {
        System.arraycopy(this.m_lineBreak, 0, ret, k,
            this.m_lineBreak.length);
        k += this.m_lineBreak.length;
        l = 0;
      }
    }

    if (rest > 0) {
      lint = 0;
      for (i = 0; i < rest; i++) {
        lint |= (data[j++] & 0xff) << (8 * i);
      }

      ret[k++] = this.m_binToChar[lint & 0x3f];
      ret[k++] = this.m_binToChar[(lint >>> 6) & 0x3f];

      if (rest > 1) {
        ret[k++] = this.m_binToChar[(lint >>> 12) & 0x3f];
      } else {
        ret[k++] = this.m_padding;
      }

      ret[k++] = this.m_padding;
    }

    return ret;
  }

  /**
   * You can decode an array of char (base64) to a byte array. The char
   * array may contain any count of whitespaces and line breaks.
   * 
   * @param data
   *          The base64 data array. This data will be destroyed during the
   *          decoding process. If you want to preserve this array, you
   *          need to create a copy of it before calling this method.
   * @param start
   *          The starting offset into this array, where to start decoding.
   * @param length
   *          The count of bytes to decode.
   * @return An array of byte containing the decoded data.
   * @see #createDecoder(OutputStream)
   */
  public byte[] decode(final char[] data, final int start, final int length) {
    int i, j, l, b, k, lint, c, s;
    byte[] ret;
    char x;

    if ((start > (i = data.length)) || (length <= 0)) {
      return EMPTY_BYTES;
    }

    if (start < 0)
      s = 0;
    else
      s = start;

    i -= s;
    if (i > length)
      i = length;
    i += s;

    for (l = 0, j = s, b = 0; j < i; j++) {
      x = data[j];

      if (x == this.m_padding) {
        if ((++b) > 2) {
          b = 2;
          break;
        }
      }

      c = (x & 0xff);
      if ((c < this.m_charToBin.length) && (this.m_charToBin[c] >= 0)) {
        if (b > 0)
          break;
        data[l++] = x;
      }
    }

    ret = new byte[(3 * (i = (l / 4))) + ((b > 0) ? (3 - b) : 0)];

    // if(b > 0) i--;

    for (j = 0, k = 0; i > 0; i--) {
      lint = this.m_charToBin[data[j++]]
          | (this.m_charToBin[data[j++]] << 6)
          | (this.m_charToBin[data[j++]] << 12)
          | (this.m_charToBin[data[j++]] << 18);
      ret[k++] = (byte) (lint & 0xff);
      ret[k++] = (byte) ((lint >> 8) & 0xff);
      ret[k++] = (byte) ((lint >> 16) & 0xff);
    }

    if (b > 0) {

      lint = this.m_charToBin[data[j++]]
          | (this.m_charToBin[data[j++]] << 6);
      ret[k++] = (byte) (lint & 0xff);
      if (b == 1) {
        lint |= (this.m_charToBin[data[j++]] << 12);
        ret[k++] = (byte) ((lint >>> 8) & 0xff);
      }
    }

    return ret;
  }

  /**
   * This method creates a base64-encoder based on the settings of this
   * object.
   * 
   * @param destination
   *          An instance of <code>Writer</code> where the encoded data
   *          should be written to. Notice: Closing the encoder will also
   *          close this writer.
   * @return An instance of <code>OutputStream</code> that can be written
   *         to. All data written to that stream will immediately be
   *         encoded and written to the writer <code>destination</code>.
   *         When the returned <code>OutputStream</code> is closed, all
   *         output that is still available will be flushed. Padding
   *         characters will be appended then if needed.
   */
  public ReferenceCountedOutputStream createEncoder(
      final Writer destination) {
    return new Encoder(destination);
  }

  /**
   * This method creates a base64-decoder based on the settings of this
   * object.
   * 
   * @param destination
   *          An instance of <code>OutputStream</code> where the decoded
   *          data should be written to. Notice: Closing this decoder will
   *          also close the output stream.
   * @return An instance of <code>Writer</code> that can be written to.
   *         All data written to that stream will immediately be decoded
   *         and written to the output stream <code>destination</code>.
   *         When the returned <code>Writer</code> is closed, all output
   *         that is still available will be flushed. The decoder will
   *         automatically be closed when a padding- character is
   *         encountered.
   */
  public ReferenceCountedWriter createDecoder(
      final OutputStream destination) {
    return new Decoder(destination);
  }

  /**
   * This encoder can be used as a normal output stream, where the data
   * written to it will immediately be base64-encoded to a writer.
   * 
   * @author Thomas Weise
   */
  protected class Encoder extends ReferenceCountedOutputStream {
    /**
     * The destination writer.
     */
    private final Writer m_writer;

    /**
     * The integer buffer for the data store.
     */
    private int m_buffer;

    /**
     * The current buffer length.
     */
    private int m_bufferLen;

    /**
     * The current line length.
     */
    private int m_lineLength;

    /**
     * Create a new <code>Encoder</code> based on the settings of the
     * enclosing Base64 instance.
     * 
     * @param destination
     *          The writer to write the encoded content to.
     */
    protected Encoder(final Writer destination) {
      super();

      this.m_writer = destination;
      this.m_buffer = 0;
      this.m_bufferLen = 0;
      this.m_lineLength = 0;

      if (Base64.this.m_lineHeader != null) {
        try {
          destination.write(Base64.this.m_lineHeader);
        } catch (Throwable t) {
          ErrorUtils.onError(t);
        }
      }
    }

    /**
     * Writes the specified byte to this output stream. The general
     * contract for <code>write</code> is that one byte is written to the
     * output stream. The byte to be written is the eight low-order bits of
     * the argument <code>b</code>. The 24 high-order bits of
     * <code>b</code> are ignored.
     * <p>
     * Subclasses of <code>OutputStream</code> must provide an
     * implementation for this method.
     * 
     * @param pbyte
     *          the <code>byte</code>.
     * @exception IOException
     *              if an I/O error occurs. In particular, an
     *              <code>IOException</code> may be thrown if the output
     *              stream has been closed.
     */
    @Override
    public synchronized void write(final int pbyte) throws IOException {
      int bl;
      int pb;

      bl = this.m_bufferLen;
      pb = (this.m_buffer | ((pbyte & 0xff) << (bl << 3)));

      if (bl < 2) {
        bl++;
      } else {
        for (bl = 4; bl > 0; bl--) {
          this.print(Base64.this.m_binToChar[pb & 0x3f]);
          pb >>= 6;
        }
      }

      this.m_bufferLen = bl;
      this.m_buffer = pb;
    }

    /**
     * Print the specified character to the internal writer.
     * 
     * @param ch
     *          The character to print.
     * @throws IOException
     *           Can come from the writer.
     */
    private final void print(final char ch) throws IOException {

      if (Base64.this.m_maxLineLength > 0) {
        this.m_lineLength++;

        if (this.m_lineLength > Base64.this.m_maxLineLength) {
          this.m_writer.write(Base64.this.m_lineBreak);
          this.m_lineLength = 1;
        }
      }

      this.m_writer.write(ch);
    }

    /**
     * The internal dispose method.
     * 
     * @throws IOException
     *           If the underlying was closed and caused an IOException.
     */
    @Override
    protected void dispose() throws IOException {
      int bl, b, i;

      try {
        try {
          bl = this.m_bufferLen;
          if (bl > 0) {

            b = this.m_buffer;

            for (i = 3; i >= 0; i--) {

              if (bl >= 0) {
                this.print(Base64.this.m_binToChar[b & 0x3f]);
                b >>= 6;
              } else {
                this.print(Base64.this.m_padding);
              }
              bl--;
            }
          }
        } finally {
          this.m_writer.close();
        }
      } finally {
        super.dispose();
      }
    }
  }

  /**
   * This decoder can be used as a normal writer, where the data written to
   * it will immediately be base64-decoded to an output-stream.
   * 
   * @author Thomas Weise
   */
  protected class Decoder extends ReferenceCountedWriter {
    /**
     * The ouput-stream to write to.
     */
    private final OutputStream m_output;

    /**
     * The integer buffer for the data store.
     */
    private int m_buffer;

    /**
     * The current buffer length.
     */
    private int m_bufferLen;

    /**
     * Create a new decoder with an output stream assigned to it.
     * 
     * @param output
     *          The output-stream assigned to the decoder.
     */
    protected Decoder(final OutputStream output) {
      super();
      this.m_output = output;
      this.m_buffer = 0;
      this.m_bufferLen = 0;
    }

    /**
     * Return the name of the character encoding being used by this stream.
     * <p>
     * If the encoding has an historical name then that name is returned;
     * otherwise the encoding's canonical name is returned.
     * <p>
     * If this instance was created with the constructor then the returned
     * name, being unique for the encoding, may differ from the name passed
     * to the constructor. This method may return <tt>null</tt> if the
     * stream has been closed.
     * </p>
     * 
     * @return The historical name of this encoding, or possibly
     *         <code>null</code> if the stream has been closed
     */
    @Override
    public final String getEncoding() {
      return PREFERED_ENCODING;
    }

    /**
     * Write a single character. The character to be written is contained
     * in the 16 low-order bits of the given integer value; the 16
     * high-order bits are ignored.
     * <p>
     * Subclasses that intend to support efficient single-character output
     * should override this method.
     * 
     * @param c
     *          int specifying a character to be written.
     * @exception IOException
     *              If an I/O error occurs
     */
    private final void do_write(final int c) throws IOException {
      int bl, cc;

      cc = (c & 0xff);

      if (cc == Base64.this.m_padding) {
        this.close();
      } else {
        if (cc < Base64.this.m_charToBin.length) {
          cc = Base64.this.m_charToBin[cc];
          if (cc >= 0) {
            bl = this.m_bufferLen;
            cc = (this.m_buffer | (cc << (6 * bl)));

            if (bl >= 3) {
              for (bl = 3; bl > 0; bl--) {
                this.m_output.write(cc & 0xff);
                cc >>= 8;
              }
            } else {
              bl++;
            }

            this.m_bufferLen = bl;
            this.m_buffer = cc;
          }

        }
      }
    }

    /**
     * Write a portion of an array of characters.
     * 
     * @param cbuf
     *          Array of characters
     * @param off
     *          Offset from which to start writing characters
     * @param len
     *          Number of characters to write
     * @exception IOException
     *              If an I/O error occurs
     */
    @Override
    public void write(final char cbuf[], final int off, final int len)
        throws IOException {
      int o, l;

      o = off;
      l = len;
      synchronized (this.lock) {
        while (l > 0) {
          this.do_write(cbuf[o++]);
          l--;
        }
      }
    }

    /**
     * Flush the stream. If the stream has saved any characters from the
     * various write() methods in a buffer, write them immediately to their
     * intended destination. Then, if that destination is another character
     * or byte stream, flush it. Thus one flush() invocation will flush all
     * the buffers in a chain of Writers and OutputStreams.
     * <p>
     * If the intended destination of this stream is an abstraction
     * provided by the underlying operating system, for example a file,
     * then flushing the stream guarantees only that bytes previously
     * written to the stream are passed to the operating system for
     * writing; it does not guarantee that they are actually written to a
     * physical device such as a disk drive.
     */
    @Override
    public void flush() {
      //
    }

    /**
     * Dispose this reference counted writer.
     * 
     * @throws IOException
     *           If the underlying was closed and caused an IOException.
     */
    @Override
    protected void dispose() throws IOException {
      int b;

      try {
        if (this.m_bufferLen > 0) {
          b = this.m_buffer;
          this.m_output.write(b & 0xff);
          b >>= 8;
          if (this.m_bufferLen > 2) {
            this.m_output.write(b & 0xff);
          }
        }
      } finally {
        try {
          this.m_output.close();
        } finally {
          super.dispose();
        }
      }
    }

  }

}

// RFC 2045 Internet Message Bodies November 1996
//
//
// 6.8. Base64 Content-Transfer-Encoding
//
// The Base64 Content-Transfer-Encoding is designed to represent
// arbitrary sequences of octets in a form that need not be humanly
// readable. The encoding and decoding algorithms are simple, but the
// encoded data are consistently only about 33 percent larger than the
// unencoded data. This encoding is virtually identical to the one used
// in Privacy Enhanced Mail (PEM) applications, as defined in RFC 1421.
//
// A 65-character subset of US-ASCII is used, enabling 6 bits to be
// represented per printable character. (The extra 65th character, "=",
// is used to signify a special processing function.)
//
// NOTE: This subset has the important property that it is represented
// identically in all versions of ISO 646, including US-ASCII, and all
// characters in the subset are also represented identically in all
// versions of EBCDIC. Other popular encodings, such as the encoding
// used by the uuencode utility, Macintosh binhex 4.0 [RFC-1741], and
// the base85 encoding specified as part of Level 2 PostScript, do not
// share these properties, and thus do not fulfill the portability
// requirements a binary transport encoding for mail must meet.
//
// The encoding process represents 24-bit groups of input bits as output
// strings of 4 encoded characters. Proceeding from left to right, a
// 24-bit input group is formed by concatenating 3 8bit input groups.
// These 24 bits are then treated as 4 concatenated 6-bit groups, each
// of which is translated into a single digit in the base64 alphabet.
// When encoding a bit stream via the base64 encoding, the bit stream
// must be presumed to be ordered with the most-significant-bit first.
// That is, the first bit in the stream will be the high-order bit in
// the first 8bit byte, and the eighth bit will be the low-order bit in
// the first 8bit byte, and so on.
//
// Each 6-bit group is used as an index into an array of 64 printable
// characters. The character referenced by the index is placed in the
// output string. These characters, identified in Table 1, below, are
// selected so as to be universally representable, and the set excludes
// characters with particular significance to SMTP (e.g., ".", CR, LF)
// and to the multipart boundary delimiters defined in RFC 2046 (e.g.,
// "-").
//
//
//
//
//
//
//
//
//
//
//
// Freed & Borenstein Standards Track [Page 24]
//
// RFC 2045 Internet Message Bodies November 1996
//
//
// Table 1: The Base64 Alphabet
//
// Value Encoding Value Encoding Value Encoding Value Encoding
// 0 A 17 R 34 i 51 z
// 1 B 18 S 35 j 52 0
// 2 C 19 T 36 k 53 1
// 3 D 20 U 37 l 54 2
// 4 E 21 V 38 m 55 3
// 5 F 22 W 39 n 56 4
// 6 G 23 X 40 o 57 5
// 7 H 24 Y 41 p 58 6
// 8 I 25 Z 42 q 59 7
// 9 J 26 a 43 r 60 8
// 10 K 27 b 44 s 61 9
// 11 L 28 c 45 t 62 +
// 12 M 29 d 46 u 63 /
// 13 N 30 e 47 v
// 14 O 31 f 48 w (pad) =
// 15 P 32 g 49 x
// 16 Q 33 h 50 y
//
// The encoded output stream must be represented in lines of no more
// than 76 characters each. All line breaks or other characters not
// found in Table 1 must be ignored by decoding software. In base64
// data, characters other than those in Table 1, line breaks, and other
// white space probably indicate a transmission error, about which a
// warning message or even a message rejection might be appropriate
// under some circumstances.
//
// Special processing is performed if fewer than 24 bits are available
// at the end of the data being encoded. A full encoding quantum is
// always completed at the end of a body. When fewer than 24 input bits
// are available in an input group, zero bits are added (on the right)
// to form an integral number of 6-bit groups. Padding at the end of
// the data is performed using the "=" character. Since all base64
// input is an integral number of octets, only the following cases can
// arise: (1) the final quantum of encoding input is an integral
// multiple of 24 bits; here, the final unit of encoded output will be
// an integral multiple of 4 characters with no "=" padding, (2) the
// final quantum of encoding input is exactly 8 bits; here, the final
// unit of encoded output will be two characters followed by two "="
// padding characters, or (3) the final quantum of encoding input is
// exactly 16 bits; here, the final unit of encoded output will be three
// characters followed by one "=" padding character.
//
// Because it is used only for padding at the end of the data, the
// occurrence of any "=" characters may be taken as evidence that the
// end of the data has been reached (without truncation in transit). No
//
//
//
// Freed & Borenstein Standards Track [Page 25]
//
// RFC 2045 Internet Message Bodies November 1996
//
//
// such assurance is possible, however, when the number of octets
// transmitted was a multiple of three and no "=" characters are
// present.
//
// Any characters outside of the base64 alphabet are to be ignored in
// base64-encoded data.
//
// Care must be taken to use the proper octets for line breaks if base64
// encoding is applied directly to text material that has not been
// converted to canonical form. In particular, text line breaks must be
// converted into CRLF sequences prior to base64 encoding. The
// important thing to note is that this may be done directly by the
// encoder rather than in a prior canonicalization step in some
// implementations.
//
// NOTE: There is no need to worry about quoting potential boundary
// delimiters within base64-encoded bodies within multipart entities
// because no hyphen characters are used in the base64 encoding.
