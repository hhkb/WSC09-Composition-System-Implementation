/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-14
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.refimpl.clustering.ObjectiveCluster.java
 * Last modification: 2006-12-14
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

package org.sigoa.refimpl.clustering;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.sigoa.refimpl.go.Individual;
import org.sigoa.spec.go.IIndividual;

/**
 * this class represents a cluster by implementing the
 * <code>ICluster</code>-interface
 *
 * @param <G>
 *          The genotype.
 * @param <PP>
 *          The phenotype.
 * @author Thomas Weise
 */
public class ObjectiveCluster<G extends Serializable, PP extends Serializable>
    extends ArrayList<IIndividual<G, PP>> implements List<IIndividual<G, PP>> {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * the sum coordinate vector
   */
  private double[] m_sum;

  /**
   * the center individual.
   */
  private IIndividual<G, PP> m_center;

  /**
   * Create a new cluster.
   */
  public ObjectiveCluster() {
    super();
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
  public IIndividual<G, PP> set(final int index,
      final IIndividual<G, PP> element) {
    IIndividual<G, PP> p;
    int i;
    double[] s;

    if (element == null)
      throw new NullPointerException();

    p = super.set(index, element);

    s = this.m_sum;
    for (i = (s.length - 1); i >= 0; i--) {
      s[i] = ((s[i] - p.getObjectiveValue(i)) + element
          .getObjectiveValue(i));
    }

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
  public boolean add(final IIndividual<G, PP> o) {
    double[] s;
    int i;

    if (o == null)
      throw new NullPointerException();
    super.add(o);

    s = this.m_sum;
    if (s == null) {
      i = o.getObjectiveValueCount();
      this.m_sum = s = new double[i];
      this.m_center = new Individual<G, PP>(i);
    } else
      i = s.length;

    for (--i; i >= 0; i--) {
      s[i] += o.getObjectiveValue(i);
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
  public void add(final int index, final IIndividual<G, PP> element) {
    double[] s;
    int i;

    if (element == null)
      throw new NullPointerException();

    super.add(index, element);

    s = this.m_sum;
    if (s == null) {
      i = element.getObjectiveValueCount();
      this.m_sum = s = new double[i];
      this.m_center = new Individual<G, PP>(i);
    } else
      i = s.length;

    for (--i; i >= 0; i--) {
      s[i] += element.getObjectiveValue(i);
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
  public IIndividual<G, PP> remove(final int index) {
    IIndividual<G, PP> p;
    double[] s;
    int i;

    p = super.remove(index);

    s = this.m_sum;
    for (i = (s.length - 1); i >= 0; i--) {
      s[i] -= p.getObjectiveValue(i);
    }

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
    Arrays.fill(this.m_sum, 0.0d);
    super.clear();
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
  public boolean addAll(final Collection<? extends IIndividual<G, PP>> c) {
    boolean b;

    b = false;

    for (IIndividual<G, PP> p : c) {
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
      final Collection<? extends IIndividual<G, PP>> c) {
    boolean b;
    int i;

    b = false;
    i = index;
    for (IIndividual<G, PP> p : c) {
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
    int i, j, k;
    double[] s;
    IIndividual<G, PP> in;

    s = this.m_sum;
    k = (s.length - 1);

    for (i = fromIndex; i < toIndex; i++) {
      in = this.get(i);
      for (j = k; j >= 0; j--) {
        s[j] -= in.getObjectiveValue(j);
      }
    }

    super.removeRange(fromIndex, toIndex);
  }

  /**
   * Obtain an individual that represents the center of this cluster in
   * objective space. It has no fitness assigned, no genotype and no
   * phenotype.
   *
   * @return an individual representing the center of the cluster
   */
  public IIndividual<G, PP> getCenterIndividual() {
    int i;
    double[] s;
    double sz;
    IIndividual<G, PP> in;

    in = this.m_center;
    s = this.m_sum;
    sz = this.size();
    for (i = (s.length - 1); i >= 0; i--) {
      in.setObjectiveValue(i, s[i] / sz);
    }
    return in;
  }
}
