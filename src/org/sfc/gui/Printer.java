/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2007-02-23
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.gui.Printer.java
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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterGraphics;
import java.awt.print.PrinterJob;

/**
 * This class will be used to ease printing.
 * 
 * @author Thomas Weise
 */
public class Printer {

  /**
   * This method creates a default book. You can add pages or components to
   * this book to print them.
   * 
   * @param name
   *          The name of the printer job, or null if none specified.
   * @return A new book associated to a printer job or null if the user
   *         aborted any of the print dialogs.
   */
  public static final DefaultBook createBook(final String name) {
    PrinterJob pj;
    PageFormat pf, pf2;

    if ((pj = PrinterJob.getPrinterJob()) == null)
      return null;

    if (name != null)
      pj.setJobName(name);

    if (!(pj.printDialog()))
      return null;

    pf = pj.defaultPage();
    if ((pf2 = pj.pageDialog(pf)) == pf)
      return null;

    return new DefaultBook(pj, pf2);
  }

  /**
   * The default class for books.
   */
  public static class DefaultBook extends Book {
    /** the printerjob associated with this document */
    private final PrinterJob m_printerJob;

    /** the default pageformat to use */
    private final PageFormat m_defaultPage;

    /**
     * Create a new default book.
     * 
     * @param job
     *          The printer job to be used.
     * @param defaultPage
     *          The default page to be used.
     */
    public DefaultBook(final PrinterJob job, final PageFormat defaultPage) {
      super();

      this.m_printerJob = job;
      job.setPageable(this);
      this.m_defaultPage = defaultPage;
    }

    /**
     * Appends a single page to the end of this Book.
     * 
     * @param painter
     *          the Printable instance that renders the page
     * @throws NullPointerException
     *           If the painter argument is null
     */
    public void append(final Printable painter) {
      super.append(painter, this.m_defaultPage);
    }

    /**
     * This method returns the default page format of this book.
     * 
     * @return The page format to be applied to new pages per default.
     */
    public PageFormat getDefaultPage() {
      return this.m_defaultPage;
    }

    /**
     * Appends numPages pages to the end of this Book.
     * 
     * @param painter
     *          the Printable instance that renders the page
     * @param numPages
     *          the number of pages to be added to the this Book.
     * @throws NullPointerException
     *           If the painter argument is null
     */
    public void append(final Printable painter, final int numPages) {
      super.append(painter, this.m_defaultPage, numPages);
    }

    /**
     * Appends a single page to the end of this Book on which a component
     * should be drawn.
     * 
     * @param component
     *          a component to print
     * @throws NullPointerException
     *           If the painter argument is null
     */
    public void appendComponent(final Component component) {
      append(new ComponentPrinter(component, getNumberOfPages()));
    }

    /**
     * Appends a single page to the end of this Book on which a component
     * should be drawn.
     * 
     * @param component
     *          a component to print
     * @param page
     *          the page format to be used for printing this component
     * @throws NullPointerException
     *           If the painter argument is null
     */
    public void appendComponent(final Component component,
        final PageFormat page) {
      super.append(new ComponentPrinter(component, getNumberOfPages()),
          (page == null) ? this.m_defaultPage : page);
    }

    /**
     * This method prints the book.
     * 
     * @return true if printing was successful, false otherwise.
     */
    public boolean print() {
      try {
        this.m_printerJob.print();
        return true;
      } catch (PrinterException pe) {
        return false;
      }
    }
  }

  /**
   * This internal class allows to print components.
   */
  private static final class ComponentPrinter implements Printable {
    /** the component to be printed */
    private final Component m_component;

    /** the on that this component will be printed */
    private final int m_page;

    /**
     * Create a new ComponentPrinter.
     * 
     * @param component
     *          The component to be printed.
     * @param page
     *          The page used to print on.
     */
    public ComponentPrinter(final Component component, final int page) {
      super();
      this.m_component = component;
      this.m_page = page;
    }

    /**
     * Prints the page at the specified index into the specified
     * {@link Graphics} context in the specified format. A PrinterJob calls
     * the Printable interface to request that a page be rendered into the
     * context specified by graphics. The format of the page to be drawn is
     * specified by pageFormat. The zero based index of the requested page
     * is specified by pageIndex. If the requested page does not exist then
     * this method returns NO_SUCH_PAGE; otherwise PAGE_EXISTS is returned.
     * The Graphics class or subclass implements the
     * {@link PrinterGraphics} interface to provide additional information.
     * If the Printable object aborts the print job then it throws a
     * {@link PrinterException}.
     * 
     * @param graphics
     *          the context into which the page is drawn
     * @param pageFormat
     *          the size and orientation of the page being drawn
     * @param pageIndex
     *          the zero based index of the page to be drawn
     * @return PAGE_EXISTS if the page is rendered successfully or
     *         NO_SUCH_PAGE if pageIndex specifies a non-existent page.
     * @exception java.awt.print.PrinterException
     *              thrown when the print job is terminated.
     */
    public int print(final Graphics graphics, final PageFormat pageFormat,
        final int pageIndex) throws PrinterException {
      Dimension d;
      Graphics2D g;
      double r1, r2;
      AffineTransform f;

      if (pageIndex != this.m_page)
        return NO_SUCH_PAGE;

      d = this.m_component.getSize();
      g = ((Graphics2D) graphics);
      f = g.getTransform();
      r1 = pageFormat.getImageableWidth() / d.width;
      r2 = pageFormat.getImageableHeight() / d.getHeight();
      r1 = Math.min(r1, r2);
      g.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
      g.scale(r1, r1);

      this.m_component.printAll(graphics);

      g.setTransform(f);
      return PAGE_EXISTS;
    }
  }
}
