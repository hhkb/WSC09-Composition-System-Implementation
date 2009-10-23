/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2007-10-21
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.io.csv.CSVReader.java
 * Last modification: 2007-10-21
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

package org.sfc.io.csv;

import java.util.Arrays;

import org.sfc.collections.lists.SimpleList;
import org.sfc.io.loaders.ReaderLoader;

/**
 * This class helps us to efficiently read the contents of CSV-files.
 * 
 * @author Thomas Weise
 */
public class CSVReader {

  /**
   * the empty string array
   */
  private static final String[][] EMPTY_STRING_2_ARRAY = new String[0][0];

  /**
   * Process csv-data stored in a character array.
   * 
   * @param data
   *          the data
   * @param start
   *          the starting index of the data
   * @param length
   *          the length of the data
   * @param removeDuplicates
   *          <code>true</code> if and only if duplicate lines should not
   *          be added to the list
   * @param append
   *          the list to append to
   */
  public static final void parseCSVData(final char[] data,
      final int start, final int length, final boolean removeDuplicates,
      final SimpleList<Object> append) {
    int isi, i, k, end;
    String[] buf;
    Object[] bufD;
    boolean nl;
    char ch;
    SimpleList<Object> buffer;

    if ((length <= 0) || (data == null) || ((end = data.length) < start))
      return;

    end = Math.min(end, length + start);
    nl = true;

    buffer = new SimpleList<Object>();

    isi = start;
    i = start;
    main: for (;; i++) {
      if (i < end)
        ch = data[i];
      else if (i == end)
        ch = '\n';
      else
        break;

      if ((ch == '\n') || (ch == '\r') || (ch == ',') || (ch == ';')
          || (ch == '\t') || (ch==' ')) {

        k = (i - 1);
        if (isi <= k) {
          while ((isi <= k) && (data[isi] <= ' '))
            isi++;
          while ((k >= isi) && (data[k] <= ' '))
            k--;
        }

        if (k >= isi)
          buffer.add(new String(data, isi, k - isi + 1));
        else if (!nl)
          buffer.add(""); //$NON-NLS-1$

        isi = (i + 1);

        if ((ch == '\n') || (ch == '\r')) {
          if (nl)
            continue main;
          nl = true;

          k = buffer.m_count;
          if (k > 0) {
            buf = new String[k];
            System.arraycopy(buffer.m_data, 0, buf, 0, k);
            buffer.m_count = 0;

            if (removeDuplicates) {
              bufD = append.m_data;
              for (k = (append.m_count - 1); k >= 0; k--) {
                if (Arrays.equals((Object[]) (bufD[k]), buf))
                  continue main;
              }
            }

            append.add(buf);
          }

        } else
          nl = false;
      } else
        nl = false;

    }
  }


  /**
   * Process csv-data stored in a character array.
   * 
   * @param source
   *          the data source
   * @param removeDuplicates
   *          <code>true</code> if and only if duplicate lines should not
   *          be added to the list
   * @return the data
   */
  public static final String[][] readCSVFile(final Object source,
      final boolean removeDuplicates) {
    ReaderLoader l;
    SimpleList<Object> s;
    int i;
    String[][] ss;

    if (source == null)
      return EMPTY_STRING_2_ARRAY;
    l = new ReaderLoader();

    if (!(l.load(source)))
      return EMPTY_STRING_2_ARRAY;
    s = new SimpleList<Object>();
    parseCSVData(l.getData(), 0, l.getLength(), removeDuplicates, s);

    i = s.m_count;
    if (i <= 0)
      return EMPTY_STRING_2_ARRAY;

    ss = new String[i][];
    System.arraycopy(s.m_data, 0, ss, 0, i);
    return ss;
  }
}
