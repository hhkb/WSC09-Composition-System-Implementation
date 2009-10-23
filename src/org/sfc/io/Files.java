/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 *
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.io.Files.java
 * Last modification: 2008-05-15
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
import java.net.URL;

import org.sfc.text.TextUtils;
import org.sfc.utils.ErrorUtils;

/**
 * The common files an application should know about.
 * 
 * @author Thomas Weise
 */
public final class Files {

  /** the character that separates the file extension from the file name */
  public static final char EXTENSION_SEPARATOR = '.';

  /** the character that separates the file extension from the file name */
  public static final String EXTENSION_SEPARATOR_STR = //
  String.valueOf(EXTENSION_SEPARATOR);

  /**
   * The standard suffix for text file
   */
  public static final String TEXT_FILE_SUFFIX = "txt";//$NON-NLS-1$

  /**
   * The java home directory.
   */
  public static final CanonicalFile HOME_DIR;

  /**
   * The user directory.
   */
  public static final CanonicalFile USER_DIR;

  /**
   * The directory for the temporary files.
   */
  public static final CanonicalFile TEMP_DIR;

  /**
   * The java virtual machine.
   */
  public static final CanonicalFile JVM_PATH;

  /**
   * The possible names of the java executables.
   */
  private static final String[] JVM_NAMES = { "wjview.exe",//$NON-NLS-1$
      "jview.exe",//$NON-NLS-1$
      "jrew.exe",//$NON-NLS-1$
      "jre.exe",//$NON-NLS-1$
      "java.exe",//$NON-NLS-1$
      "javaw.exe",//$NON-NLS-1$
      "jre",//$NON-NLS-1$
      "java"//$NON-NLS-1$
  };

  /**
   * Possible locations for the jvm relative to the search paths.
   */
  private static final String[] JVM_LOCATIONS = { File.separator,
      ".." + File.separatorChar + //$NON-NLS-1$
          "System32" + File.separatorChar,//$NON-NLS-1$
      ".." + File.separatorChar,//$NON-NLS-1$
      "bin" + File.separatorChar//$NON-NLS-1$
  };

  static {
    String s;
    File f;

    s = System.getProperty("java.home"); //$NON-NLS-1$

    if ((s != null) && (s.length() > 0)) {
      f = new File(s);
    } else {
      f = new File("."); //$NON-NLS-1$
    }

    HOME_DIR = CanonicalFile.canonicalize(f);

    s = System.getProperty("user.dir"); //$NON-NLS-1$
    if ((s == null) || (s.length() <= 0)) {
      f = HOME_DIR;
    } else {
      f = new File(s);
    }

    USER_DIR = CanonicalFile.canonicalize(f);

    s = System.getProperty("java.io.tmpdir"); //$NON-NLS-1$
    if ((s == null) || (s.length() <= 0)) {
      f = USER_DIR;
    } else {
      f = new File(s);
    }

    TEMP_DIR = CanonicalFile.canonicalize(f);

    JVM_PATH = CanonicalFile.canonicalize(findJVM(HOME_DIR, USER_DIR,
        TEMP_DIR));
  }

  /**
   * Find out the java virtual machine's location.
   * 
   * @param dirs
   *          The search directories.
   * @return The canonical jvm location, or <code>null</code> if it could
   *         not be found.
   */
  private static final File findJVM(final CanonicalFile... dirs) {
    int i, j, k;
    File f;

    {
      for (i = (dirs.length - 1); i >= 0; i--) {
        for (j = 0; j < JVM_LOCATIONS.length; j++) {
          for (k = 0; k < JVM_NAMES.length; k++) {
            f = new File(dirs[i], JVM_LOCATIONS[j] + JVM_NAMES[k]);
            if (f.exists() && f.isFile()) {
              return f;
            }
          }
        }
      }
      return null;
    }
  }

  /**
   * Returns the name of the file without extension.
   * 
   * @param file
   *          The file to return the pure name of.
   * @return The pure file name, with extension removed.
   */
  public static final String getFileName(File file) {
    int i;
    String s;

    if (file == null)
      return null;
    s = file.getName();
    if (s == null)
      return null;

    if ((i = s.lastIndexOf(EXTENSION_SEPARATOR)) >= 0) {
      s = s.substring(0, i);
    }

    if ((s == null) || (s.length() <= 0))
      return null;
    return s;
  }

  /**
   * Returns the name of the file without extension.
   * 
   * @param file
   *          The file to return the pure name of.
   * @return The pure file name, with extension removed.
   */
  public static final String getFileName(final URL file) {
    String n;
    int i, j;

    if (file == null)
      return null;

    n = file.getPath();
    i = n.lastIndexOf('/');
    j = n.lastIndexOf(EXTENSION_SEPARATOR);

    if (j <= i)
      j = n.length();
    if (i > 0) {
      n = n.substring(i + 1, j);
    }

    if ((n == null) || (n.length() <= 0))
      return null;
    return n;
  }

  /**
   * Extracts the extension from a file name.
   * 
   * @param original
   *          The file name to get the extension from
   * @return The string containing the extension, for example if original
   *         was c:\log.txt, txt would be returned.
   */
  public static final String getExtension(final String original) {
    int i, l;
    String o;

    o = original;

    if ((o == null) || ((l = original.length()) <= 0))
      return null;

    i = original.lastIndexOf(EXTENSION_SEPARATOR);
    if ((i < 0) || (i >= (l - 1)))
      return null;

    o = o.substring(i + 1).toLowerCase();

    return o;
  }

  /**
   * Extracts the extension from a file.
   * 
   * @param file
   *          The file to get the extension from
   * @return The string containing the extension, for example if original
   *         was c:\log.txt, txt would be returned.
   */
  public static final String getFileExtension(final File file) {
    if (file == null)
      return null;
    return getExtension(file.toString());
  }

  /**
   * Builds a new file name out of a path, a file name and an extension.
   * 
   * @param path
   *          The path to the file. Can be left null if not of interest or
   *          already included in name.
   * @param name
   *          The file's name. Can be left null if not of interest or
   *          already included in path.
   * @param extension
   *          The file's extension. Can be left null if not of interest or
   *          already included in path or name.
   * @return The newly created file name. For example, if path was c:, name
   *         was log and extension was txt, c:\log.txt would be returned.
   */
  public final static String createName(final String path,
      final String name, final String extension) {
    StringBuilder b;
    int i;

    b = new StringBuilder();

    if ((path != null) && (path.length() > 0)) {
      b.append(path);
      if (b.lastIndexOf(File.separator) < (path.length() - 1)) {
        b.append(File.separatorChar);
      }
    }

    if ((name != null) && (name.length() > 0)) {
      if (name.startsWith(File.separator))
        b.append(name.substring(1));
      else
        b.append(name);
    }

    if ((extension != null) && (extension.length() > 0)) {

      i = b.lastIndexOf(EXTENSION_SEPARATOR_STR);
      if (i >= 0)
        b.delete(i + 1, b.length());
      else
        b.append(EXTENSION_SEPARATOR);

      if (extension.startsWith(EXTENSION_SEPARATOR_STR))
        b.setLength(b.length() - 1);

      b.append(extension.toLowerCase());
    }

    return b.toString();
  }

  /**
   * Create a guaranteed new and unique directory inside another one
   * 
   * @param directory
   *          the other directory
   * @return the new directory inside or <code>null</code> if none could
   *         be created
   */
  public static final CanonicalFile createDirectoryInside(
      final Object directory) {
    return createDirectoryInside(directory, null);
  }

  /**
   * Create a guaranteed new and unique directory inside another one
   * 
   * @param directory
   *          the other directory
   * @param suggestion
   *          the name suggestion for the directory
   * @return the new directory inside or <code>null</code> if none could
   *         be created
   */
  public static final CanonicalFile createDirectoryInside(
      final Object directory, final String suggestion) {
    CanonicalFile d;
    File ins;
    int i;
    String sug;

    d = IO.getFile(directory);
    if (d == null)
      return null;
    d.mkdirs();

    sug = TextUtils.preprocessString(suggestion);
    if (sug != null) {
      ins = new File(d, sug);
      if (ins.mkdirs())
        return CanonicalFile.canonicalize(ins);
      sug = sug + EXTENSION_SEPARATOR;
    } else
      sug = ""; //$NON-NLS-1$

    for (i = 1; i > 0; i++) {
      ins = new File(d, sug + Integer.toString(i));
      if (ins.mkdirs())
        return CanonicalFile.canonicalize(ins);
    }
    return null;
  }

  /**
   * Create a guaranteed new and unique file inside a directory
   * 
   * @param directory
   *          the directory
   * @param prefix
   *          the prefix of the file name
   * @param suffix
   *          the suffix of the file name
   * @return the new file inside the directory or <code>null</code> if
   *         none could be created
   */
  public static final CanonicalFile createFileInside(
      final Object directory, final String prefix, final String suffix) {
    CanonicalFile d;
    File ins;
    int i;
    String n1, n2;

    d = IO.getFile(directory);
    if (d == null)
      return null;

    d.mkdirs();

    n1 = TextUtils.preprocessString(prefix);
    n2 = TextUtils.preprocessString(suffix);

    if ((n1 != null) || (n2 != null)) {
      ins = new File(d, ((n1 != null) ? n1 : "") + //$NON-NLS-1$
          (((n1 != null) && (n2 != null)) ? "." : "") + //$NON-NLS-1$//$NON-NLS-2$
          ((n2 != null) ? n2 : ""));//$NON-NLS-1$
      try {
        if (ins.createNewFile())
          return CanonicalFile.canonicalize(ins);
      } catch (Throwable t) {
        ErrorUtils.onError(t);
      }
    }

    n1 = (n1 != null) ? (prefix + EXTENSION_SEPARATOR) : ""; //$NON-NLS-1$
    n2 = (n1 != null) ? (EXTENSION_SEPARATOR + suffix) : "";//$NON-NLS-1$

    for (i = 1; i > 0; i++) {
      ins = new File(d, n1 + Integer.toString(i) + n2);
      try {
        if (ins.createNewFile())
          return CanonicalFile.canonicalize(ins);
      } catch (Throwable tt) {
        ErrorUtils.onError(tt);
      }
    }
    return null;
  }

  /**
   * Delete the file or directory specified by <code>f</code>
   * 
   * @param f
   *          the file or directory to be deleted
   * @return <code>true</code> if it does no longer exist
   */
  public static final boolean delete(final File f) {
    File[] fs;
    int i;
    boolean b;

    if (f == null)
      return true;
    if (!(f.exists()))
      return true;

    b = true;
    if (f.isDirectory()) {
      fs = f.listFiles();
      for (i = (fs.length - 1); i >= 0; i--) {
        if (!(delete(fs[i]))) {
          b = false;
        }
      }
    }

    if (f.delete())
      return b;
    return false;
  }
}
