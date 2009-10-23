/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-10
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.pipe.Pipeline.java
 * Last modification: 2006-12-10
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

package org.sigoa.refimpl.pipe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.sigoa.spec.go.IIndividual;
import org.sigoa.spec.pipe.IPipe;
import org.sigoa.spec.pipe.IPipeIn;

/**
 * A pipeline is a combination of many connected pipes.
 *
 * @param <G>
 *          The genotype.
 * @param <PP>
 *          The phenotype.
 * @author Thomas Weise
 */
public class Pipeline<G extends Serializable, PP extends Serializable>
    extends ArrayList<IPipe<G, PP>> implements IPipe<G, PP> {

  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the first pipe.
   */
  private IPipe<G, PP> m_first;

  /**
   * the output pipe
   */
  private IPipeIn<G, PP> m_out;

  /**
   * Create a new pipeline.
   */
  public Pipeline() {
    super();
  }

  /**
   * Write a new individual into the pipe.
   *
   * @param individual
   *          The new individual to be written.
   * @throws NullPointerException
   *           if <code>individual</code> is <code>null</code>.
   */
  public void write(final IIndividual<G, PP> individual) {
    if (this.m_first != null)
      this.m_first.write(individual);
    else if (this.m_out != null)
      this.m_out.write(individual);
  }

  /**
   * Tell the pipe that all individuals have been written to it.
   */
  public void eof() {
    if (this.m_first != null)
      this.m_first.eof();
    else if (this.m_out != null)
      this.m_out.eof();
  }

  /**
   * Set the ouput pipe.
   *
   * @param pipe
   *          The output pipe.
   */
  @SuppressWarnings("unchecked")
  public void setOutputPipe(final IPipeIn<? super G, ? super PP> pipe) {
    int i;
    this.m_out = ((IPipeIn<G, PP>) pipe);
    i = this.size();
    if (i > 0)
      this.get(i - 1).setOutputPipe(pipe);
  }

  /**
   * Obtain the output pipe.
   *
   * @return The output pipe, or <code>null</code> if none is set.
   */
  public IPipeIn<? super G, ? super PP> getOutputPipe() {
    return this.m_out;
  }

  /**
   * Replaces the element at the specified position in this list with the
   * specified element.
   *
   * @param index
   *          index of element to replace.
   * @param element
   *          element to be stored at the specified position.
   * @return the element previously at the specified position.
   * @throws IndexOutOfBoundsException
   *           if index out of range
   *           <tt>(index &lt; 0 || index &gt;= size())</tt>.
   * @throws NullPointerException
   *           if <code>element==null</code>
   */
  @Override
  public IPipe<G, PP> set(final int index, final IPipe<G, PP> element) {
    IPipe<G, PP> p;
    if (element == null)
      throw new NullPointerException();
    p = super.set(index, element);
    element.setOutputPipe(p.getOutputPipe());
    if (index <= 0)
      this.m_first = element;
    else
      this.get(index - 1).setOutputPipe(element);
    return p;
  }

  /**
   * Appends the specified element to the end of this list.
   *
   * @param o
   *          element to be appended to this list.
   * @return <tt>true</tt> (as per the general contract of
   *         Collection.add).
   * @throws NullPointerException
   *           if <code>o==null</code>
   */
  @Override
  public boolean add(final IPipe<G, PP> o) {
    IPipe<G, PP> p;
    if (o == null)
      throw new NullPointerException();
    super.add(o);
    if (this.m_first == null) {
      this.m_first = o;
      o.setOutputPipe(this.m_out);
    } else {
      p = this.get(this.size() - 2);
      o.setOutputPipe(p.getOutputPipe());
      p.setOutputPipe(o);
    }
    return true;
  }

  /**
   * Inserts the specified element at the specified position in this list.
   * Shifts the element currently at that position (if any) and any
   * subsequent elements to the right (adds one to their indices).
   *
   * @param index
   *          index at which the specified element is to be inserted.
   * @param element
   *          element to be inserted.
   * @throws IndexOutOfBoundsException
   *           if index is out of range
   *           <tt>(index &lt; 0 || index &gt; size())</tt>.
   * @throws NullPointerException
   *           if <code>element==null</code>
   */
  @Override
  public void add(final int index, final IPipe<G, PP> element) {
    if (element == null)
      throw new NullPointerException();
    super.add(index, element);
    if (this.m_first == null) {
      this.m_first = element;
      element.setOutputPipe(this.m_out);
    } else {
      if (index <= 0) {
        element.setOutputPipe(this.m_first);
        this.m_first = element;
      } else {
        if (index >= (this.size() - 1))
          element.setOutputPipe(this.m_out);
        else
          element.setOutputPipe(this.get(index + 1));
        this.get(index - 1).setOutputPipe(element);
      }
    }
  }

  /**
   * Removes the element at the specified position in this list. Shifts any
   * subsequent elements to the left (subtracts one from their indices).
   *
   * @param index
   *          the index of the element to removed.
   * @return the element that was removed from the list.
   * @throws IndexOutOfBoundsException
   *           if index out of range <tt>(index
   *      &lt; 0 || index &gt;= size())</tt>.
   */
  @Override
  public IPipe<G, PP> remove(final int index) {
    IPipe<G, PP> p;
    p = super.remove(index);
    if (index <= 0) {
      if (this.size() > 0) {
        this.m_first = this.get(0);
      } else {
        this.m_first = null;
      }
    } else {
      this.get(index - 1).setOutputPipe(p.getOutputPipe());
    }

    p.setOutputPipe(null);
    return p;
  }

  /**
   * Removes a single instance of the specified element from this list, if
   * it is present (optional operation). More formally, removes an element
   * <tt>e</tt> such that <tt>(o==null ? e==null :
   * o.equals(e))</tt>,
   * if the list contains one or more such elements. Returns <tt>true</tt>
   * if the list contained the specified element (or equivalently, if the
   * list changed as a result of the call).
   * <p>
   *
   * @param o
   *          element to be removed from this list, if present.
   * @return <tt>true</tt> if the list contained the specified element.
   */
  @Override
  public boolean remove(final Object o) {
    int i;
    i = this.indexOf(o);
    if (i >= 0) {
      this.remove(i);
      return true;
    }
    return false;
  }

  /**
   * Removes all of the elements from this list. The list will be empty
   * after this call returns.
   */
  @Override
  public void clear() {
    int i;
    for (i = this.size() - 1; i >= 0; i--) {
      this.get(i).setOutputPipe(null);
    }
    super.clear();
    this.m_first = null;
  }

  /**
   * Appends all of the elements in the specified Collection to the end of
   * this list, in the order that they are returned by the specified
   * Collection's Iterator. The behavior of this operation is undefined if
   * the specified Collection is modified while the operation is in
   * progress. (This implies that the behavior of this call is undefined if
   * the specified Collection is this list, and this list is nonempty.)
   *
   * @param c
   *          the elements to be inserted into this list.
   * @return <tt>true</tt> if this list changed as a result of the call.
   * @throws NullPointerException
   *           if the specified collection is null.
   */
  @Override
  public boolean addAll(final Collection<? extends IPipe<G, PP>> c) {
    boolean b;

    b = false;

    for (IPipe<G, PP> p : c) {
      if (this.add(p))
        b = true;
    }

    return b;
  }

  /**
   * Inserts all of the elements in the specified Collection into this
   * list, starting at the specified position. Shifts the element currently
   * at that position (if any) and any subsequent elements to the right
   * (increases their indices). The new elements will appear in the list in
   * the order that they are returned by the specified Collection's
   * iterator.
   *
   * @param index
   *          index at which to insert first element from the specified
   *          collection.
   * @param c
   *          elements to be inserted into this list.
   * @return <tt>true</tt> if this list changed as a result of the call.
   * @throws IndexOutOfBoundsException
   *           if index out of range <tt>(index
   *      &lt; 0 || index &gt; size())</tt>.
   * @throws NullPointerException
   *           if the specified Collection is null.
   */
  @Override
  public boolean addAll(final int index,
      final Collection<? extends IPipe<G, PP>> c) {
    boolean b;
    int i;

    b = false;
    i = index;
    for (IPipe<G, PP> p : c) {
      this.add(i++, p);
      b = true;
    }

    return b;
  }

  /**
   * Removes from this List all of the elements whose index is between
   * fromIndex, inclusive and toIndex, exclusive. Shifts any succeeding
   * elements to the left (reduces their index). This call shortens the
   * list by <tt>(toIndex - fromIndex)</tt> elements. (If
   * <tt>toIndex==fromIndex</tt>, this operation has no effect.)
   *
   * @param fromIndex
   *          index of first element to be removed.
   * @param toIndex
   *          index after last element to be removed.
   */
  @Override
  protected void removeRange(final int fromIndex, final int toIndex) {
    int i;

    if (fromIndex <= 0) {
      if (toIndex < this.size())
        this.m_first = this.get(toIndex);
      else
        this.m_first = null;
    } else {
      if (toIndex >= this.size())
        this.get(fromIndex - 1).setOutputPipe(this.m_out);
      else
        this.get(fromIndex - 1).setOutputPipe(this.get(toIndex));
    }

    for (i = fromIndex; i < toIndex; i++)
      this.get(i).setOutputPipe(null);

    super.removeRange(fromIndex, toIndex);
  }

}
