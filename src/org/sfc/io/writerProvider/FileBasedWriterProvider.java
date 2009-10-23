/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2007-09-02
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.pipe.stat.writerProviders.FileBasedWriterProvider.java
 * Last modification: 2007-09-02
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

package org.sfc.io.writerProvider;

import java.io.File;
import java.io.Serializable;
import java.io.Writer;

import org.sfc.io.CanonicalFile;
import org.sfc.io.IO;
import org.sfc.text.TextUtils;

/**
 * the file based writer provider works on files of some type.
 * 
 * @param <T>
 *          the type of writer provided. This can be for example a normal
 *          {@link java.io.Writer}, a {@link org.sfc.io.TextWriter} or a
 *          writer or for example a {@link org.sfc.xml.sax.SAXWriter}.
 * @author Thomas Weise
 */
public abstract class FileBasedWriterProvider<T extends Writer> implements
    Serializable, IWriterProvider<T> {

  /**
   * the destination directory.
   */
  private final CanonicalFile m_directory;

  /**
   * the prefix of the file names
   */
  private final String m_prefix;

  /**
   * the suffix of the file names
   */
  private final String m_suffix;

  /**
   * <code>true</code> if and only if the information for the writer
   * provider should be used, it will be ignored otherwise.
   */
  private boolean m_useInfo;

  /**
   * Create a new file text writer provider.
   * 
   * @param directory
   *          the directory to place the files in
   * @param prefix
   *          the prefix for the names
   * @param suffix
   *          the suffix for the names
   */
  public FileBasedWriterProvider(final Object directory,
      final String prefix, final String suffix) {
    super();
    this.m_directory = IO.getFile(directory);
    this.m_prefix = prefix;
    this.m_suffix = suffix;
    this.m_useInfo = true;
  }

  /**
   * Obtain the next file. final Object info
   * 
   * @param info
   *          An object containing information to be used for the writer
   *          creation. This could be an instance of {@link Integer} or a
   *          string. <code>info</code> is ignored if it is
   *          <code>null</code>.
   * @return file
   */
  protected CanonicalFile createFile(final Object info) {
    String name;
    File f;
    CanonicalFile g;

    name = this.createName(info);

    f = this.m_directory;
    if (f != null)
      f = new File(f, name);
    else
      f = new File(name);

    g = CanonicalFile.canonicalize(f);
    if (g != null)
      return g;

    g = IO.getFile(f);
    if (g != null)
      return g;

    g = IO.getFile(this.m_directory + name);
    if (g != null)
      return g;

    g = IO.getFile(name);
    return g;
  }

  /**
   * Create a name for the new output writer.
   * 
   * @param info
   *          An object containing information to be used for the writer
   *          creation. This could be an instance of {@link Integer} or a
   *          string. <code>info</code> is ignored if it is
   *          <code>null</code>.
   * @return the name
   */
  protected String createName(final Object info) {
    StringBuilder sb;

    sb = new StringBuilder();
    if (this.m_prefix != null)
      sb.append(this.m_prefix);

    if (this.m_useInfo) {
      if (info instanceof Number) {
        TextUtils.appendLong(((Number) info).longValue(), 5, sb);
      } else if (info != null)
        TextUtils.append(info, sb);
    }

    if (this.m_suffix != null)
      sb.append(this.m_suffix);

    return sb.toString();
  }

  /**
   * Returns <code>true</code> if and only if the information for the
   * writer provider is used, <code>false</code> otherwise.
   * 
   * @return <code>true</code> if and only if the information for the
   *         writer provider is used, <code>false</code> otherwise
   */
  public boolean usesInfo() {
    return this.m_useInfo;
  }

  /**
   * Set whether the information for the writer provider should be used or
   * not
   * 
   * @param uses
   *          <code>true</code> if and only if the information for the
   *          writer provider should be used used, <code>false</code> if
   *          it should be ignored
   */
  public void setUsesInfo(final boolean uses) {
    this.m_useInfo = uses;
  }
}
