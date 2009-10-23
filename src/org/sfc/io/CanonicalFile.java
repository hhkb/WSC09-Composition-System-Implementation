/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.io.CanonicalFile.java
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

import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * This file class always represents canonical files.
 * 
 * @author Thomas Weise
 */
public class CanonicalFile extends File {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * Create a canonical file by canonicalizing a normal file.
   * 
   * @param file
   *          The file to canonicalize.
   */
  public CanonicalFile(final File file) {
    this((AccessController.doPrivileged(new Canonicalizer(file))));
  }

  /**
   * Canonicalize a file so it is unique in this system.
   * 
   * @param file
   *          The file to canonicalize.
   * @return The canonicalized file.
   */
  public static final CanonicalFile canonicalize(final File file) {
    if (file instanceof CanonicalFile)
      return ((CanonicalFile) file);
    return new CanonicalFile(file);
  }

  /**
   * Create a canonical file by specifying a canonical path.
   * 
   * @param filename
   *          The canonical path.
   */
  private CanonicalFile(final String filename) {
    super(filename);
  }

  /**
   * Returns the abstract pathname of this abstract pathname's parent, or
   * <code>null</code> if this pathname does not name a parent directory.
   * <p>
   * The <em>parent</em> of an abstract pathname consists of the
   * pathname's prefix, if any, and each name in the pathname's name
   * sequence except for the last. If the name sequence is empty then the
   * pathname does not name a parent directory.
   * 
   * @return The abstract pathname of the parent directory named by this
   *         abstract pathname, or <code>null</code> if this pathname
   *         does not name a parent
   */
  @Override
  public File getParentFile() {
    String s;

    s = getParent();
    return (s != null) ? new CanonicalFile(s) : null;
  }

  /**
   * Returns the absolute form of this abstract pathname. Equivalent to
   * <code>new&nbsp;File(this.{@link #getAbsolutePath}())</code>.
   * 
   * @return The absolute abstract pathname denoting the same file or
   *         directory as this abstract pathname
   */
  @Override
  public File getAbsoluteFile() {
    return this;
  }

  /**
   * Returns the canonical form of this abstract pathname. Equivalent to
   * <code>new&nbsp;File(this.{@link #getCanonicalPath}())</code>.
   * 
   * @return The canonical pathname string denoting the same file or
   *         directory as this abstract pathname
   */
  @Override
  public File getCanonicalFile() {
    return this;
  }

  /**
   * Returns the absolute pathname string of this abstract pathname.
   * <p>
   * If this abstract pathname is already absolute, then the pathname
   * string is simply returned as if by the
   * <code>{@link java.io.File#getPath}</code> method. If this abstract
   * pathname is the empty abstract pathname then the pathname string of
   * the current user directory, which is named by the system property
   * <code>user.dir</code>, is returned. Otherwise this pathname is
   * resolved in a system-dependent way. On UNIX systems, a relative
   * pathname is made absolute by resolving it against the current user
   * directory. On Microsoft WindowManager systems, a relative pathname is
   * made absolute by resolving it against the current directory of the
   * drive named by the pathname, if any; if not, it is resolved against
   * the current user directory.
   * 
   * @return The absolute pathname string denoting the same file or
   *         directory as this abstract pathname
   * @see java.io.File#isAbsolute()
   */
  @Override
  public String getAbsolutePath() {
    return getPath();
  }

  /**
   * Returns the canonical pathname string of this abstract pathname.
   * <p>
   * A canonical pathname is both absolute and unique. The precise
   * definition of canonical form is system-dependent. This method first
   * converts this pathname to absolute form if necessary, as if by
   * invoking the {@link #getAbsolutePath} method, and then maps it to its
   * unique form in a system-dependent way. This typically involves
   * removing redundant names such as <tt>"."</tt> and <tt>".."</tt>
   * from the pathname, resolving symbolic links (on UNIX platforms), and
   * converting drive letters to a standard case (on Microsoft
   * WindowManager platforms).
   * <p>
   * Every pathname that denotes an existing file or directory has a unique
   * canonical form. Every pathname that denotes a nonexistent file or
   * directory also has a unique canonical form. The canonical form of the
   * pathname of a nonexistent file or directory may be different from the
   * canonical form of the same pathname after the file or directory is
   * created. Similarly, the canonical form of the pathname of an existing
   * file or directory may be different from the canonical form of the same
   * pathname after the file or directory is deleted.
   * 
   * @return The canonical pathname string denoting the same file or
   *         directory as this abstract pathname
   */
  @Override
  public String getCanonicalPath() {
    return getPath();
  }

  /**
   * Tests whether this abstract pathname is absolute. The definition of
   * absolute pathname is system dependent. On UNIX systems, a pathname is
   * absolute if its prefix is <code>"/"</code>. On Microsoft
   * WindowManager systems, a pathname is absolute if its prefix is a drive
   * specifier followed by <code>"\\"</code>, or if its prefix is
   * <code>"\\\\"</code>.
   * 
   * @return <code>true</code> if this abstract pathname is absolute,
   *         <code>false</code> otherwise
   */
  @Override
  public boolean isAbsolute() {
    return true;
  }

  /**
   * This small private class helps to canonicalize a file by tunnelling
   * this operation through a <code>PrivilegedAction</code>.
   * 
   * @author Thomas Weise
   */
  private static final class Canonicalizer implements
      PrivilegedAction<String> {
    /**
     * The file to canonicalize.
     */
    private final File m_file;

    /**
     * The constructor of the canonicalizer.
     * 
     * @param file
     *          The to be canonicalized.
     */
    Canonicalizer(final File file) {
      super();
      this.m_file = file;
    }

    /**
     * Perform the canonicalization.
     * 
     * @return The canonicalized file.
     */
    public final String run() {
      try {
        return this.m_file.getCanonicalPath();
      } catch (IOException io) {
        return this.m_file.toString();
      }
    }
  }
}
