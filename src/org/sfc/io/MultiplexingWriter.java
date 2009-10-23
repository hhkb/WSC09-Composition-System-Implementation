/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.io.MultiplexingWriter.java
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
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.sfc.collections.CollectionUtils;

/**
 * The multiplexing writer allows you to multiplex the output that goes
 * into one writer to multiple writers.
 * 
 * @author Thomas Weise
 */
public class MultiplexingWriter extends ReferenceCountedWriter implements
    List<Writer> {
  /**
   * The internal writer list.
   */
  private final List<Writer> m_writers;

  /**
   * Create a new multiplexing writer.
   */
  public MultiplexingWriter() {
    super();
    this.m_writers = CollectionUtils.createList();
  }

  /**
   * Write a single character. The character to be written is contained in
   * the 16 low-order bits of the given integer value; the 16 high-order
   * bits are ignored.
   * <p>
   * Subclasses that intend to support efficient single-character output
   * should override this method.
   * 
   * @param c
   *          int specifying a character to be written.
   * @exception IOException
   *              If an I/O error occurs
   */
  @Override
  public void write(final int c) throws IOException {
    IOException x;
    int y;

    x = null;
    for (y = (this.size() - 1); y >= 0; y--) {
      try {
        this.get(y).write(c);
      } catch (IOException q) {
        x = q;
      }
    }

    if (x != null)
      throw x;
  }

  /**
   * Write an array of characters.
   * 
   * @param cbuf
   *          Array of characters to be written
   * @exception IOException
   *              If an I/O error occurs
   */
  @Override
  public void write(final char cbuf[]) throws IOException {
    IOException x;
    int y;

    x = null;
    for (y = (this.size() - 1); y >= 0; y--) {
      try {
        this.get(y).write(cbuf);
      } catch (IOException q) {
        x = q;
      }
    }

    if (x != null)
      throw x;
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
    IOException x;
    int y;

    x = null;
    for (y = (this.size() - 1); y >= 0; y--) {
      try {
        this.get(y).write(cbuf, off, len);
      } catch (IOException q) {
        x = q;
      }
    }

    if (x != null)
      throw x;
  }

  /**
   * Write a string.
   * 
   * @param str
   *          String to be written
   * @exception IOException
   *              If an I/O error occurs
   */
  @Override
  public void write(final String str) throws IOException {
    IOException x;
    int y;

    x = null;
    for (y = (this.size() - 1); y >= 0; y--) {
      try {
        this.get(y).write(str);
      } catch (IOException q) {
        x = q;
      }
    }

    if (x != null)
      throw x;
  }

  /**
   * Write a portion of a string.
   * 
   * @param str
   *          A String
   * @param off
   *          Offset from which to start writing characters
   * @param len
   *          Number of characters to write
   * @exception IOException
   *              If an I/O error occurs
   */
  @Override
  public void write(final String str, final int off, final int len)
      throws IOException {
    IOException x;
    int y;

    x = null;
    for (y = (this.size() - 1); y >= 0; y--) {
      try {
        this.get(y).write(str, off, len);
      } catch (IOException q) {
        x = q;
      }
    }

    if (x != null)
      throw x;
  }

  /**
   * Appends the specified character sequence to this writer.
   * <p>
   * An invocation of this method of the form <tt>out.append(csq)</tt>
   * behaves in exactly the same way as the invocation
   * 
   * <pre>
   * out.write(csq.toString())
   * </pre>
   * 
   * <p>
   * Depending on the specification of <tt>toString</tt> for the
   * character sequence <tt>csq</tt>, the entire sequence may not be
   * appended. For instance, invoking the <tt>toString</tt> method of a
   * character buffer will return a subsequence whose content depends upon
   * the buffer's position and limit.
   * 
   * @param csq
   *          The character sequence to append. If <tt>csq</tt> is
   *          <tt>null</tt>, then the four characters <tt>"null"</tt>
   *          are appended to this writer.
   * @return This writer
   * @throws IOException
   *           If an I/O error occurs
   * @since 1.5
   */
  @Override
  public Writer append(final CharSequence csq) throws IOException {
    IOException x;
    int y;

    x = null;
    for (y = (this.size() - 1); y >= 0; y--) {
      try {
        this.get(y).append(csq);
      } catch (IOException q) {
        x = q;
      }
    }

    if (x != null)
      throw x;
    return this;
  }

  /**
   * Appends a subsequence of the specified character sequence to this
   * writer. <tt>Appendable</tt>.
   * <p>
   * An invocation of this method of the form <tt>out.append(csq, start,
   * end)</tt>
   * when <tt>csq</tt> is not <tt>null</tt> behaves in exactly the same
   * way as the invocation
   * 
   * <pre>
   * out.write(csq.subSequence(start, end).toString())
   * </pre>
   * 
   * @param csq
   *          The character sequence from which a subsequence will be
   *          appended. If <tt>csq</tt> is <tt>null</tt>, then
   *          characters will be appended as if <tt>csq</tt> contained
   *          the four characters <tt>"null"</tt>.
   * @param start
   *          The index of the first character in the subsequence
   * @param end
   *          The index of the character following the last character in
   *          the subsequence
   * @return This writer
   * @throws IndexOutOfBoundsException
   *           If <tt>start</tt> or <tt>end</tt> are negative,
   *           <tt>start</tt> is greater than <tt>end</tt>, or
   *           <tt>end</tt> is greater than <tt>csq.length()</tt>
   * @throws IOException
   *           If an I/O error occurs
   * @since 1.5
   */
  @Override
  public Writer append(final CharSequence csq, final int start,
      final int end) throws IOException {
    IOException x;
    int y;

    x = null;
    for (y = (this.size() - 1); y >= 0; y--) {
      try {
        this.get(y).append(csq, start, end);
      } catch (IOException q) {
        x = q;
      }
    }

    if (x != null)
      throw x;
    return this;
  }

  /**
   * Appends the specified character to this writer.
   * <p>
   * An invocation of this method of the form <tt>out.append(c)</tt>
   * behaves in exactly the same way as the invocation
   * 
   * <pre>
   * out.write(c)
   * </pre>
   * 
   * @param c
   *          The 16-bit character to append
   * @return This writer
   * @throws IOException
   *           If an I/O error occurs
   * @since 1.5
   */
  @Override
  public Writer append(final char c) throws IOException {
    IOException x;
    int y;

    x = null;
    for (y = (this.size() - 1); y >= 0; y--) {
      try {
        this.get(y).append(c);
      } catch (IOException q) {
        x = q;
      }
    }

    if (x != null)
      throw x;
    return this;
  }

  /**
   * Flush the stream. If the stream has saved any characters from the
   * various write() methods in a buffer, write them immediately to their
   * intended destination. Then, if that destination is another character
   * or byte stream, flush it. Thus one flush() invocation will flush all
   * the buffers in a chain of Writers and OutputStreams.
   * <p>
   * If the intended destination of this stream is an abstraction provided
   * by the underlying operating system, for example a file, then flushing
   * the stream guarantees only that bytes previously written to the stream
   * are passed to the operating system for writing; it does not guarantee
   * that they are actually written to a physical device such as a disk
   * drive.
   * 
   * @exception IOException
   *              If an I/O error occurs
   */
  @Override
  public void flush() throws IOException {
    IOException x;
    int y;

    x = null;
    for (y = (this.size() - 1); y >= 0; y--) {
      try {
        this.get(y).flush();
      } catch (IOException q) {
        x = q;
      }
    }

    if (x != null)
      throw x;
  }

  /**
   * Dispose this reference counted writer.
   * 
   * @throws IOException
   *           If the underlying was closed and caused an IOException.
   */
  @Override
  protected void dispose() throws IOException {
    IOException x;
    int y;

    x = null;
    for (y = (this.size() - 1); y >= 0; y--) {
      try {
        this.get(y).close();
      } catch (IOException q) {
        x = q;
      }
    }

    this.m_writers.clear();
    super.dispose();
    if (x != null)
      throw x;
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
  public String getEncoding() {
    String s, d;
    int y;

    s = null;
    for (y = (this.size() - 1); y >= 0; y--) {
      d = defaultGetEncoding(this.get(y));
      if (d != null) {
        if (s == null)
          s = d;
        else if (!(s.equals(d)))
          return null;
      }
    }

    return ((s != null) ? s : defaultGetEncoding(this));
  }

  /**
   * Returns the number of elements in this list. If this list contains
   * more than <tt>Integer.MAX_VALUE</tt> elements, returns
   * <tt>Integer.MAX_VALUE</tt>.
   * 
   * @return the number of elements in this list.
   */
  public int size() {
    return this.m_writers.size();
  }

  /**
   * Returns <tt>true</tt> if this list contains no elements.
   * 
   * @return <tt>true</tt> if this list contains no elements.
   */
  public boolean isEmpty() {
    return this.m_writers.isEmpty();
  }

  /**
   * Returns <tt>true</tt> if this list contains the specified element.
   * More formally, returns <tt>true</tt> if and only if this list
   * contains at least one element <tt>e</tt> such that
   * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>.
   * 
   * @param o
   *          element whose presence in this list is to be tested.
   * @return <tt>true</tt> if this list contains the specified element.
   * @throws ClassCastException
   *           if the type of the specified element is incompatible with
   *           this list (optional).
   * @throws NullPointerException
   *           if the specified element is null and this list does not
   *           support null elements (optional).
   */
  public boolean contains(final Object o) {
    return this.m_writers.contains(o);
  }

  /**
   * Returns an iterator over the elements in this list in proper sequence.
   * 
   * @return an iterator over the elements in this list in proper sequence.
   */
  public Iterator<Writer> iterator() {
    return this.m_writers.iterator();
  }

  /**
   * Returns an array containing all of the elements in this list in proper
   * sequence. Obeys the general contract of the
   * <tt>Collection.toArray</tt> method.
   * 
   * @return an array containing all of the elements in this list in proper
   *         sequence.
   * @see Arrays#asList(Object[])
   */
  public Object[] toArray() {
    return this.m_writers.toArray();
  }

  /**
   * Returns an array containing all of the elements in this list in proper
   * sequence; the runtime type of the returned array is that of the
   * specified array. Obeys the general contract of the
   * <tt>Collection.toArray(Object[])</tt> method.
   * 
   * @param a
   *          the array into which the elements of this list are to be
   *          stored, if it is big enough; otherwise, a new array of the
   *          same runtime type is allocated for this purpose.
   * @param <T>
   *          The array item type.
   * @return an array containing the elements of this list.
   * @throws ArrayStoreException
   *           if the runtime type of the specified array is not a
   *           supertype of the runtime type of every element in this list.
   * @throws NullPointerException
   *           if the specified array is <tt>null</tt>.
   */
  public <T> T[] toArray(final T[] a) {
    return this.m_writers.toArray(a);
  }

  // Modification Operations

  /**
   * Appends the specified element to the end of this list (optional
   * operation).
   * <p>
   * Lists that support this operation may place limitations on what
   * elements may be added to this list. In particular, some lists will
   * refuse to add null elements, and others will impose restrictions on
   * the type of elements that may be added. List classes should clearly
   * specify in their documentation any restrictions on what elements may
   * be added.
   * 
   * @param o
   *          element to be appended to this list.
   * @return <tt>true</tt> (as per the general contract of the
   *         <tt>Collection.add</tt> method).
   * @throws UnsupportedOperationException
   *           if the <tt>add</tt> method is not supported by this list.
   * @throws ClassCastException
   *           if the class of the specified element prevents it from being
   *           added to this list.
   * @throws NullPointerException
   *           if the specified element is null and this list does not
   *           support null elements.
   * @throws IllegalArgumentException
   *           if some aspect of this element prevents it from being added
   *           to this list.
   */
  public boolean add(final Writer o) {
    if ((o != null) && (o != this)) {
      return this.m_writers.add(o);
    }
    return false;
  }

  /**
   * Removes the first occurrence in this list of the specified element
   * (optional operation). If this list does not contain the element, it is
   * unchanged. More formally, removes the element with the lowest index i
   * such that <tt>(o==null ? get(i)==null : o.equals(get(i)))</tt> (if
   * such an element exists).
   * 
   * @param o
   *          element to be removed from this list, if present.
   * @return <tt>true</tt> if this list contained the specified element.
   * @throws ClassCastException
   *           if the type of the specified element is incompatible with
   *           this list (optional).
   * @throws NullPointerException
   *           if the specified element is null and this list does not
   *           support null elements (optional).
   * @throws UnsupportedOperationException
   *           if the <tt>remove</tt> method is not supported by this
   *           list.
   */
  public boolean remove(final Object o) {
    return this.m_writers.remove(o);
  }

  // Bulk Modification Operations

  /**
   * Returns <tt>true</tt> if this list contains all of the elements of
   * the specified collection.
   * 
   * @param c
   *          collection to be checked for containment in this list.
   * @return <tt>true</tt> if this list contains all of the elements of
   *         the specified collection.
   * @throws ClassCastException
   *           if the types of one or more elements in the specified
   *           collection are incompatible with this list (optional).
   * @throws NullPointerException
   *           if the specified collection contains one or more null
   *           elements and this list does not support null elements
   *           (optional).
   * @throws NullPointerException
   *           if the specified collection is <tt>null</tt>.
   * @see #contains(Object)
   */
  public boolean containsAll(final Collection<?> c) {
    return this.m_writers.containsAll(c);
  }

  /**
   * Appends all of the elements in the specified collection to the end of
   * this list, in the order that they are returned by the specified
   * collection's iterator (optional operation). The behavior of this
   * operation is unspecified if the specified collection is modified while
   * the operation is in progress. (Note that this will occur if the
   * specified collection is this list, and it's nonempty.)
   * 
   * @param c
   *          collection whose elements are to be added to this list.
   * @return <tt>true</tt> if this list changed as a result of the call.
   * @throws UnsupportedOperationException
   *           if the <tt>addAll</tt> method is not supported by this
   *           list.
   * @throws ClassCastException
   *           if the class of an element in the specified collection
   *           prevents it from being added to this list.
   * @throws NullPointerException
   *           if the specified collection contains one or more null
   *           elements and this list does not support null elements, or if
   *           the specified collection is <tt>null</tt>.
   * @throws IllegalArgumentException
   *           if some aspect of an element in the specified collection
   *           prevents it from being added to this list.
   */
  public boolean addAll(final Collection<? extends Writer> c) {
    return this.m_writers.addAll(c);
  }

  /**
   * Inserts all of the elements in the specified collection into this list
   * at the specified position (optional operation). Shifts the element
   * currently at that position (if any) and any subsequent elements to the
   * right (increases their indices). The new elements will appear in this
   * list in the order that they are returned by the specified collection's
   * iterator. The behavior of this operation is unspecified if the
   * specified collection is modified while the operation is in progress.
   * (Note that this will occur if the specified collection is this list,
   * and it's nonempty.)
   * 
   * @param index
   *          index at which to insert first element from the specified
   *          collection.
   * @param c
   *          elements to be inserted into this list.
   * @return <tt>true</tt> if this list changed as a result of the call.
   * @throws UnsupportedOperationException
   *           if the <tt>addAll</tt> method is not supported by this
   *           list.
   * @throws ClassCastException
   *           if the class of one of elements of the specified collection
   *           prevents it from being added to this list.
   * @throws NullPointerException
   *           if the specified collection contains one or more null
   *           elements and this list does not support null elements, or if
   *           the specified collection is <tt>null</tt>.
   * @throws IllegalArgumentException
   *           if some aspect of one of elements of the specified
   *           collection prevents it from being added to this list.
   * @throws IndexOutOfBoundsException
   *           if the index is out of range (index &lt; 0 || index &gt;
   *           size()).
   */
  public boolean addAll(final int index,
      final Collection<? extends Writer> c) {
    return this.m_writers.addAll(index, c);
  }

  /**
   * Removes from this list all the elements that are contained in the
   * specified collection (optional operation).
   * 
   * @param c
   *          collection that defines which elements will be removed from
   *          this list.
   * @return <tt>true</tt> if this list changed as a result of the call.
   * @throws UnsupportedOperationException
   *           if the <tt>removeAll</tt> method is not supported by this
   *           list.
   * @throws ClassCastException
   *           if the types of one or more elements in this list are
   *           incompatible with the specified collection (optional).
   * @throws NullPointerException
   *           if this list contains one or more null elements and the
   *           specified collection does not support null elements
   *           (optional).
   * @throws NullPointerException
   *           if the specified collection is <tt>null</tt>.
   * @see #remove(Object)
   * @see #contains(Object)
   */
  public boolean removeAll(final Collection<?> c) {
    return this.m_writers.retainAll(c);
  }

  /**
   * Retains only the elements in this list that are contained in the
   * specified collection (optional operation). In other words, removes
   * from this list all the elements that are not contained in the
   * specified collection.
   * 
   * @param c
   *          collection that defines which elements this set will retain.
   * @return <tt>true</tt> if this list changed as a result of the call.
   * @throws UnsupportedOperationException
   *           if the <tt>retainAll</tt> method is not supported by this
   *           list.
   * @throws ClassCastException
   *           if the types of one or more elements in this list are
   *           incompatible with the specified collection (optional).
   * @throws NullPointerException
   *           if this list contains one or more null elements and the
   *           specified collection does not support null elements
   *           (optional).
   * @throws NullPointerException
   *           if the specified collection is <tt>null</tt>.
   * @see #remove(Object)
   * @see #contains(Object)
   */
  public boolean retainAll(final Collection<?> c) {
    return this.m_writers.retainAll(c);
  }

  /**
   * Removes all of the elements from this list (optional operation). This
   * list will be empty after this call returns (unless it throws an
   * exception).
   * 
   * @throws UnsupportedOperationException
   *           if the <tt>clear</tt> method is not supported by this
   *           list.
   */
  public void clear() {
    this.m_writers.clear();
  }

  // Positional Access Operations

  /**
   * Returns the element at the specified position in this list.
   * 
   * @param index
   *          index of element to return.
   * @return the element at the specified position in this list.
   * @throws IndexOutOfBoundsException
   *           if the index is out of range (index &lt; 0 || index &gt;=
   *           size()).
   */
  public Writer get(final int index) {
    return this.m_writers.get(index);
  }

  /**
   * Replaces the element at the specified position in this list with the
   * specified element (optional operation).
   * 
   * @param index
   *          index of element to replace.
   * @param element
   *          element to be stored at the specified position.
   * @return the element previously at the specified position.
   * @throws UnsupportedOperationException
   *           if the <tt>set</tt> method is not supported by this list.
   * @throws ClassCastException
   *           if the class of the specified element prevents it from being
   *           added to this list.
   * @throws NullPointerException
   *           if the specified element is null and this list does not
   *           support null elements.
   * @throws IllegalArgumentException
   *           if some aspect of the specified element prevents it from
   *           being added to this list.
   * @throws IndexOutOfBoundsException
   *           if the index is out of range (index &lt; 0 || index &gt;=
   *           size()).
   */
  public Writer set(final int index, final Writer element) {
    if ((element != null) && (element != this)) {
      return this.m_writers.set(index, element);
    }

    throw new IllegalArgumentException();
  }

  /**
   * Inserts the specified element at the specified position in this list
   * (optional operation). Shifts the element currently at that position
   * (if any) and any subsequent elements to the right (adds one to their
   * indices).
   * 
   * @param index
   *          index at which the specified element is to be inserted.
   * @param element
   *          element to be inserted.
   * @throws UnsupportedOperationException
   *           if the <tt>add</tt> method is not supported by this list.
   * @throws ClassCastException
   *           if the class of the specified element prevents it from being
   *           added to this list.
   * @throws NullPointerException
   *           if the specified element is null and this list does not
   *           support null elements.
   * @throws IllegalArgumentException
   *           if some aspect of the specified element prevents it from
   *           being added to this list.
   * @throws IndexOutOfBoundsException
   *           if the index is out of range (index &lt; 0 || index &gt;
   *           size()).
   */
  public void add(final int index, final Writer element) {
    if ((element != null) && (element != this)) {
      this.m_writers.add(index, element);
    }
  }

  /**
   * Removes the element at the specified position in this list (optional
   * operation). Shifts any subsequent elements to the left (subtracts one
   * from their indices). Returns the element that was removed from the
   * list.
   * 
   * @param index
   *          the index of the element to removed.
   * @return the element previously at the specified position.
   * @throws UnsupportedOperationException
   *           if the <tt>remove</tt> method is not supported by this
   *           list.
   * @throws IndexOutOfBoundsException
   *           if the index is out of range (index &lt; 0 || index &gt;=
   *           size()).
   */
  public Writer remove(final int index) {
    return this.m_writers.remove(index);
  }

  // Search Operations

  /**
   * Returns the index in this list of the first occurrence of the
   * specified element, or -1 if this list does not contain this element.
   * More formally, returns the lowest index <tt>i</tt> such that
   * <tt>(o==null ? get(i)==null : o.equals(get(i)))</tt>, or -1 if
   * there is no such index.
   * 
   * @param o
   *          element to search for.
   * @return the index in this list of the first occurrence of the
   *         specified element, or -1 if this list does not contain this
   *         element.
   * @throws ClassCastException
   *           if the type of the specified element is incompatible with
   *           this list (optional).
   * @throws NullPointerException
   *           if the specified element is null and this list does not
   *           support null elements (optional).
   */
  public int indexOf(final Object o) {
    return this.m_writers.indexOf(o);
  }

  /**
   * Returns the index in this list of the last occurrence of the specified
   * element, or -1 if this list does not contain this element. More
   * formally, returns the highest index <tt>i</tt> such that
   * <tt>(o==null ? get(i)==null : o.equals(get(i)))</tt>, or -1 if
   * there is no such index.
   * 
   * @param o
   *          element to search for.
   * @return the index in this list of the last occurrence of the specified
   *         element, or -1 if this list does not contain this element.
   * @throws ClassCastException
   *           if the type of the specified element is incompatible with
   *           this list (optional).
   * @throws NullPointerException
   *           if the specified element is null and this list does not
   *           support null elements (optional).
   */
  public int lastIndexOf(final Object o) {
    return this.m_writers.lastIndexOf(o);
  }

  // List Iterators

  /**
   * Returns a list iterator of the elements in this list (in proper
   * sequence).
   * 
   * @return a list iterator of the elements in this list (in proper
   *         sequence).
   */
  public ListIterator<Writer> listIterator() {
    return this.m_writers.listIterator();
  }

  /**
   * Returns a list iterator of the elements in this list (in proper
   * sequence), starting at the specified position in this list. The
   * specified index indicates the first element that would be returned by
   * an initial call to the <tt>next</tt> method. An initial call to the
   * <tt>previous</tt> method would return the element with the specified
   * index minus one.
   * 
   * @param index
   *          index of first element to be returned from the list iterator
   *          (by a call to the <tt>next</tt> method).
   * @return a list iterator of the elements in this list (in proper
   *         sequence), starting at the specified position in this list.
   * @throws IndexOutOfBoundsException
   *           if the index is out of range (index &lt; 0 || index &gt;
   *           size()).
   */
  public ListIterator<Writer> listIterator(final int index) {
    return this.m_writers.listIterator(index);
  }

  // View

  /**
   * Returns a view of the portion of this list between the specified
   * <tt>fromIndex</tt>, inclusive, and <tt>toIndex</tt>, exclusive.
   * (If <tt>fromIndex</tt> and <tt>toIndex</tt> are equal, the
   * returned list is empty.) The returned list is backed by this list, so
   * non-structural changes in the returned list are reflected in this
   * list, and vice-versa. The returned list supports all of the optional
   * list operations supported by this list.
   * <p>
   * This method eliminates the need for explicit range operations (of the
   * sort that commonly exist for arrays). Any operation that expects a
   * list can be used as a range operation by passing a subList view
   * instead of a whole list. For example, the following idiom removes a
   * range of elements from a list:
   * 
   * <pre>
   * list.subList(from, to).clear();
   * </pre>
   * 
   * Similar idioms may be constructed for <tt>indexOf</tt> and
   * <tt>lastIndexOf</tt>, and all of the algorithms in the
   * <tt>Collections</tt> class can be applied to a subList.
   * <p>
   * The semantics of the list returned by this method become undefined if
   * the backing list (i.e., this list) is <i>structurally modified</i> in
   * any way other than via the returned list. (Structural modifications
   * are those that change the size of this list, or otherwise perturb it
   * in such a fashion that iterations in progress may yield incorrect
   * results.)
   * 
   * @param from_index
   *          low endpoint (inclusive) of the subList.
   * @param to_index
   *          high endpoint (exclusive) of the subList.
   * @return a view of the specified range within this list.
   * @throws IndexOutOfBoundsException
   *           for an illegal endpoint index value (fromIndex &lt; 0 ||
   *           toIndex &gt; size || fromIndex &gt; toIndex).
   */
  public List<Writer> subList(final int from_index, final int to_index) {
    return this.m_writers.subList(from_index, to_index);
  }
}
