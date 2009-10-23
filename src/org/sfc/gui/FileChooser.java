/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2007-02-23
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.gui.FileChoose.java
 * Last modification: 2007-02-23
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

package org.sfc.gui;

import java.awt.Component;
import java.awt.HeadlessException;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.sfc.io.CanonicalFile;
import org.sfc.io.Files;

/**
 * This one creates a file chooser that stores the last directory visited.
 * 
 * @author Thomas Weise
 */
public class FileChooser extends JFileChooser {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1L;

  /** The last directory visited by a file chooser */
  private static String s_directory = null;

  /**
   * Create a new FileChooser
   */
  public FileChooser() {
    super(s_directory);

  }

  /**
   * Pops a custom file chooser dialog with a custom approve button. For
   * example, the following code pops up a file chooser with a "Run
   * Application" button (instead of the normal "Save" or "Open" button):
   * 
   * @param parent
   *          the parent component of the dialog; can be null
   * @param approveButtonText
   *          the text of the ApproveButton
   * @return the return state of the file chooser on popdown:
   *         JFileChooser.CANCEL_OPTION, JFileChooser.APPROVE_OPTION,
   *         JFileCHooser.ERROR_OPTION if an error occurs or the dialog is
   *         dismissed.
   * @exception HeadlessException
   *              if GraphicsEnvironment.isHeadless() returns true.
   */
  @Override
  public int showDialog(final Component parent,
      final String approveButtonText) {
    int i;

    i = super.showDialog(parent, approveButtonText);
    s_directory = getCurrentDirectory().toString();

    return i;
  }

  /**
   * Pops up an "Open File" file chooser dialog. Note that the text that
   * appears in the approve button is determined by the L&F.
   * 
   * @param parent
   *          the parent component of the dialog, can be null; see
   *          showDialog for details
   * @return the return state of the file chooser on popdown:
   *         JFileChooser.CANCEL_OPTION, JFileChooser.APPROVE_OPTION,
   *         JFileCHooser.ERROR_OPTION if an error occurs or the dialog is
   *         dismissed
   * @exception HeadlessException
   *              if GraphicsEnvironment.isHeadless() returns true.
   */
  @Override
  public int showOpenDialog(final Component parent)
      throws HeadlessException {
    int i;

    i = super.showOpenDialog(parent);
    s_directory = getCurrentDirectory().toString();

    return i;
  }

  /**
   * Pops up an "Save File" file chooser dialog. Note that the text that
   * appears in the approve button is determined by the L&F.
   * 
   * @param parent
   *          the parent component of the dialog, can be null; see
   *          showDialog for details
   * @return the return state of the file chooser on popdown:
   *         JFileChooser.CANCEL_OPTION, JFileChooser.APPROVE_OPTION,
   *         JFileCHooser.ERROR_OPTION if an error occurs or the dialog is
   *         dismissed
   * @exception HeadlessException
   *              if GraphicsEnvironment.isHeadless() returns true.
   */
  @Override
  public int showSaveDialog(final Component parent)
      throws HeadlessException {
    int i;

    i = super.showSaveDialog(parent);
    s_directory = getCurrentDirectory().toString();
    return i;
  }

  /**
   * Returns the selected file. This can be set either by the programmer
   * via setFile or by a user action, such as either typing the filename
   * into the UI or selecting the file from a list in the UI.
   * 
   * @return the selected file
   */
  @Override
  public File getSelectedFile() {
    File f;
    FileFilter ff;

    f = super.getSelectedFile();

    if (f != null) {
      ff = getFileFilter();
      if (ff instanceof Filter) {
        f = ((Filter) ff).performExtension(f);
      }

      return CanonicalFile.canonicalize(f);
    }

    return f;
  }

  /**
   * Returns a list of selected files if the file chooser is set to allow
   * multiple selection.
   * 
   * @return the list of selected files
   */
  @Override
  public final File[] getSelectedFiles() {
    File[] f;
    FileFilter ff;
    Filter fff;
    int i;

    f = super.getSelectedFiles();
    ff = getFileFilter();

    if (ff instanceof Filter) {
      fff = (Filter) ff;
      for (i = (f.length - 1); i >= 0; i--) {
        f[i] = CanonicalFile.canonicalize(fff.performExtension(f[i]));
      }
    }

    return f;
  }

  /**
   * This is a private class for a single file filter
   */
  public static class Filter extends FileFilter {
    /** the description of this filter */
    private String m_description;

    /** the file extension identified by this filter */
    private String m_extension;

    /**
     * Create a new filter object.
     * 
     * @param description
     *          the description of this filter
     * @param extension
     *          the file extension identified by this filter
     */
    public Filter(final String description, final String extension) {
      super();
      setFeatures(description, extension);
    }

    /**
     * Sets the features of this file filter.
     * 
     * @param description
     *          the description of this filter
     * @param extension
     *          the file extension identified by this filter
     */
    public void setFeatures(final String description,
        final String extension) {
      String ex;

      ex = extension;
      this.m_description = description;

      while (ex.startsWith(File.separator))
        ex = ex.substring(File.separator.length());

      this.m_extension = ex.trim().toLowerCase();
    }

    /**
     * Sets the extension of the file to the extension of this filter if it
     * has not yet one.
     * 
     * @param file
     *          The file to set the extension of.
     * @return Either the original file file if it already has an extension
     *         or a new file with the extension added.
     */
    public File performExtension(final File file) {
      String s;

      if (file == null)
        return null;
      if (file.isDirectory())
        return file;

      s = file.toString();
      if (Files.getExtension(s) == null)
        return new File(Files.createName(null, s, this.m_extension));
      return file;
    }

    /**
     * Whether the given file is accepted by this filter.
     * 
     * @param f
     *          The file to check
     * @return true if this filter accepts the file, false otherwise
     */
    @Override
    public boolean accept(final File f) {
      String s;
      if (f.isDirectory())
        return true;
      s = Files.getExtension(f.toString());
      if (s == null)
        return false;
      return s.endsWith(this.m_extension);
    }

    /**
     * The description of this filter. For example: "JPG and GIF Images"
     * 
     * @return the description
     */
    @Override
    public String getDescription() {
      return this.m_description;
    }
  }

}