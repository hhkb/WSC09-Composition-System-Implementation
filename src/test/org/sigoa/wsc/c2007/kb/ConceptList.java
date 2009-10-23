/*
 * Copyright (c) 2006 Thomas Weise
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-04-21 06:02:35
 * Original Filename: test.org.sigoa.wsc.c2007.kb.ConceptList.java
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

import org.sfc.collections.IArrayConstructor;
import org.sfc.collections.lists.SimpleList;

/**
 * A simple but fast concept list. This list is used to contain the
 * concepts.
 * 
 * @author Thomas Weise
 */
public class ConceptList extends SimpleList<Concept> {

  /**
   * The concept array constructor
   */
  public static final IArrayConstructor<Concept> CONCEPT_ARRAY_CONSTRUCTOR = new IArrayConstructor<Concept>() {
    public static final long serialVersionUID = 1;

    public Concept[] createArray(final int length) {
      return new Concept[length];
    }

    private final Object readResolve() {
      return CONCEPT_ARRAY_CONSTRUCTOR;
    }

    private final Object writeReplace() {
      return CONCEPT_ARRAY_CONSTRUCTOR;
    }
  };

  /**
   * Create a new concept list.
   * 
   * @param initialSize
   *          The initial size. Use -1 for don't care.
   */
  public ConceptList(final int initialSize) {
    super(initialSize, CONCEPT_ARRAY_CONSTRUCTOR);
  }

  /**
   * Create a new simple list.
   * 
   * @param copy
   *          the list to copy from
   */
  @SuppressWarnings("unchecked")
  public ConceptList(final SimpleList<Concept> copy) {
    super(copy);
  }

  /**
   * Check wether a subsumption of the given concept is stored in the list.
   * 
   * @param concept
   *          The concept to find a subsumption of.
   * @return The index of the concept subsuming the given one, or
   *         <code>-1</code> if no such concept exists in this list.
   */
  public final int includesSubsumptionOf(final Concept concept) {
    int i;
    Concept[] c;

    c = this.m_data;
    for (i = (this.m_count - 1); i >= 0; i--) {
      if (c[i].subsumes(concept))
        return i;
    }

    return -1;
  }

  /**
   * Add the concept if it is new to the list and delete all more general
   * concepts.
   * 
   * @param concept
   *          The concept to find a subsumption of.
   */
  public final void addSpecific(final Concept concept) {
    int i, t;
    Concept[] c;

    c = this.m_data;
    t = this.m_count;
    for (i = (t - 1); i >= 0; i--) {
      if (c[i].subsumes(concept))
        c[i] = c[--t];
    }

    this.m_count = t;
    this.add(concept);
  }

  /**
   * Check wether a specialization of the given concept is stored in the
   * list.
   * 
   * @param concept
   *          The concept to find a specialization of.
   * @return the index of the specialization, or -1 if none is included
   */
  public final int includesSpecializationOf(final Concept concept) {
    int i;
    Concept[] c;

    c = this.m_data;
    for (i = (this.m_count - 1); i >= 0; i--) {
      if (concept.subsumes(c[i]))
        return i;
    }

    return -1;
  }

  /**
   * Remove all concepts that subsume the given one from the list.
   * 
   * @param concept
   *          The concept to clear from the list.
   * @return The count of concepts removed.
   */
  public final int removeSubsumptions(final Concept concept) {
    int i, c, k;
    Concept[] cc;

    c = this.m_count;
    cc = this.m_data;
    k = c;

    for (i = (c - 1); i >= 0; i--) {
      if (cc[i].subsumes(concept)) {
        cc[i] = cc[--c];
      }
    }

    this.m_count = c;
    return (k - c);
  }
}
