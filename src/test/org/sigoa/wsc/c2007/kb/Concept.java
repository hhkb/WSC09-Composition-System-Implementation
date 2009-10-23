/*
 * Copyright (c) 2006 Thomas Weise
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-04-20 15:59:35
 * Original Filename: test.org.sigoa.wsc.c2007.kb.Concept.java
 * Version          : 1.0.3
 * Last modification: 2006-05-08
 *                by: Thomas Weise
 * 
 * License          : GNU LESSER GENERAL PUBLIC LICENSE
 *                    Version 2.1, February 1999
 *                    You should have received a copy of this license along
 *                    with this library; if not, write to the Free Software
 *                    Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 *                    MA 02111-1307, USA or download the license under
 *                    http://www.gnu.org/copyleft/lesser.html.
 *                    
 * Warranty         : This software is provided "as is" without any
 *                    warranty; without even the implied warranty of
 *                    merchantability or fitness for a particular purpose.
 *                    See the Gnu Lesser General Public License for more
 *                    details.
 */

package test.org.sigoa.wsc.c2007.kb;

import org.sfc.collections.lists.SimpleList;

/**
 * This class denotes a semantic concept. A concept is one item of the
 * taxonomy and is handled like a class type in a programming language. A
 * concept can be derived from a another one, like a subclass can be
 * derived from a class. It is then compatible to that class. Also, a
 * concept can be the subsumption (generalization) of a specialized
 * (derived) concept.
 * 
 * @author Thomas Weise
 */
public final class Concept implements Comparable<Concept> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The specializing concepts.
   */
  private transient Concept[] m_concepts;

  /**
   * The count of specializing concepts.
   */
  private transient int m_conceptCount;

  /**
   * The services providing this concept as an output.
   */
  transient Service[] m_services;

  /**
   * The count of services providing this concept as an output.
   */
  transient int m_serviceCount;

  /**
   * The generalization concept is the concept this concept is a
   * specialization of.
   */
  transient Concept m_generalization;

  /**
   * This pointer is used by the hash of the knowledge base to find the
   * next concept if hash-codes collide.
   */
  transient Concept m_next;

  /**
   * The name of this concept.
   */
  final String m_name;

  /**
   * The hash code.
   */
  final transient int m_hc;

  /**
   * The promising services.
   */
  transient SimpleList<Service> m_promising;

  /**
   * Create a new concept of the specified name.
   * 
   * @param name
   *          The name for the new concept.
   * @param next
   *          The next concept in the row.
   * @param hc
   *          The hashcode.
   */
  Concept(final String name, final Concept next, final int hc) {
    super();
    this.m_concepts = new Concept[4];
    this.m_services = new Service[4];
    this.m_name = name;
    this.m_next = next;
    this.m_hc = hc;
  }

  /**
   * Obtain the name of this concept.
   * 
   * @return The name of this concept.
   */
  @Override
  public final String toString() {
    return this.m_name;
  }

  /**
   * Obtain this concept's hash code.
   * 
   * @return This concept's hash code.
   */
  @Override
  public final int hashCode() {
    return this.m_hc;
  }

  /**
   * Check if this concept subsumes another one.
   * 
   * @param specialization
   *          The concept we want to check if it is a specialization of
   *          this concept.
   * @return <code>true</code> if and only if this concept subsumes the
   *         concept <code>specialization</code>.
   */
  public final boolean subsumes(final Concept specialization) {
    Concept s;

    for (s = specialization; s != null; s = s.m_generalization) {
      if (s == this)
        return true;
    }

    return false;
  }

  /**
   * Compare two concepts for equality.
   * 
   * @param c
   *          The concept to compare to.
   * @return A value indicating the order of two services.
   */
  public final int compareTo(final Concept c) {
    int i;

    i = (this.m_hc - c.m_hc);

    if (i == 0) {
      return this.m_name.compareTo(c.m_name);
    }

    return i;
  }

  /**
   * Add a concept to the list of child concepts.
   * 
   * @param concept
   *          The concept to be added.
   */
  final void addConcept(final Concept concept) {
    Concept[] data;
    int c;

    data = this.m_concepts;
    c = this.m_conceptCount;

    if (c >= data.length) {
      data = new Concept[c << 1];
      System.arraycopy(this.m_concepts, 0, data, 0, c);
      this.m_concepts = data;
    }

    data[c] = concept;
    this.m_conceptCount = (c + 1);
  }

  /**
   * Add a service that provides this concept as an output.
   * 
   * @param service
   *          The service that provides this concept as an output.
   */
  final void addService(final Service service) {
    Service[] data;
    int c;

    data = this.m_services;
    c = this.m_serviceCount;

    if (c >= data.length) {
      data = new Service[c << 1];
      System.arraycopy(this.m_services, 0, data, 0, c);
      this.m_services = data;
    }

    data[c] = service;
    this.m_serviceCount = (c + 1);
  }

  /**
   * Obtain the list of services that produce this concept as output
   * directly or <strong>indirectly by returning a specialized concept</strong>.
   * 
   * @param list
   *          The service list to store the services into (services are
   *          appended, the stuff already in the list is not influenced).
   */
  public final void getPromising(final SimpleList<Service> list) {
    SimpleList<Service> s;
    Concept[] c;
    int i;

    s = this.m_promising;

    if (s == null) {
      s = new SimpleList<Service>();
      s.addArray(this.m_services, this.m_serviceCount);

      c = this.m_concepts;
      for (i = (this.m_conceptCount - 1); i >= 0; i--) {
        c[i].getPromising(s);
      }
      this.m_promising = s;
    }

    list.addSimpleList(s);
  }

  /**
   * Get the count of concepts specializing this one.
   * 
   * @return The count of concepts specializing this one.
   */
  public final int getSpecializationCount() {
    return this.m_conceptCount;
  }

  /**
   * Obtain the specializing concept at index <code>index</code>.
   * 
   * @param index
   *          The index of the concept of interest.
   * @return The specializing concept at index <code>index</code>.
   */
  public final Concept getSpecialization(final int index) {
    return this.m_concepts[index];
  }

  /**
   * Obtain the concept subsuming this one directly.
   * 
   * @return The concept subsuming this one directly.
   */
  public final Concept getGeneralization() {
    return this.m_generalization;
  }

}
