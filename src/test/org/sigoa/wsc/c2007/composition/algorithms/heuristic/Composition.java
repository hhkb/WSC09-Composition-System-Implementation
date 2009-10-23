/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-06-11
 * Creator          : Thomas Weise
 * Original Filename: test.org.sigoa.wsc.c2007.composition.algorithms.heuristic.Composition.java
 * Last modification: 2007-06-11
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

package test.org.sigoa.wsc.c2007.composition.algorithms.heuristic;

import test.org.sigoa.wsc.c2007.challenge.Challenge;
import test.org.sigoa.wsc.c2007.challenge.ServiceList2;
import test.org.sigoa.wsc.c2007.composition.Composer;
import test.org.sigoa.wsc.c2007.kb.Concept;
import test.org.sigoa.wsc.c2007.kb.ConceptList;
import test.org.sigoa.wsc.c2007.kb.Service;
import test.org.sigoa.wsc.c2007.kb.ServiceList;

/**
 * This class represents a composition.
 *
 * @author Thomas Weise
 */
public final class Composition /*implements Comparable<Composition>*/ {
  /**
   * The services stored in the composition.
   */
  final ServiceList m_services;

  /**
   * The list of parameters still unknown.
   */
  ConceptList m_unknown;

  /**
   * The list of known concepts.
   */
  ConceptList m_known;

  /**
   * The count of eliminated concepts.
   */
  final int m_eliminated;

  /**
   * Create a new composition.
   *
   * @param add
   *          The service to be added.
   * @param known
   *          The list of known concepts.
   * @param unknown
   *          The list of unknown concepts.
   */
  public Composition(final Service add, final ConceptList known,
      final ConceptList unknown) {
    super();

    this.m_services = new ServiceList(1);
    this.m_services.add(add);
    this.m_eliminated = this.compute(add, known, unknown, 0);
  }

  /**
   * Create a new composition.
   *
   * @param add
   *          The service to be added.
   * @param superC
   *          The super composition.
   */
  public Composition(final Service add, final Composition superC) {
    super();

    ServiceList sl, sl2;

    sl2 = superC.m_services;
    sl = new ServiceList(sl2.m_count + 1);
    sl.addSimpleList(sl2);
    sl.add(add);
    this.m_services = sl;

    this.m_eliminated = this.compute(add, superC.m_known,
        superC.m_unknown, superC.m_eliminated);
  }

  /**
   * Compute the internal data.
   *
   * @param add
   *          The service to be added.
   * @param known
   *          The list of known concepts.
   * @param unknown
   *          The list of unknown concepts.
   * @param eliminated2
   *          The count of eliminated concepts before.
   * @return The count of eliminated concepts after.
   */
  private final int compute(final Service add, final ConceptList known,
      final ConceptList unknown, int eliminated2) {
    int i, eliminated;
    boolean b;
    ConceptList kl, wl;
    Concept[] n;

    kl = known;
    wl = new ConceptList(unknown.m_count + 5);
    wl.addSimpleList(unknown);
    b = true;
    eliminated = eliminated2;
    n = add.getInputs();
    for (i = (n.length - 1); i >= 0; i--) {
      if (kl.includesSpecializationOf(n[i]) >= 0) {
        eliminated++;
      } else {
        b = false;
        wl.add(n[i]);// addSpecific
      }
    }

    if (b) {
      kl = new ConceptList(known.m_count + 5);
      kl.addSimpleList(known);
    }

    n = add.getOutputs();
    for (i = (n.length - 1); i >= 0; i--) {
      eliminated += wl.removeSubsumptions(n[i]);
      if (b) {
        if (kl.includesSpecializationOf(n[i]) < 0) {
          kl.add(n[i]);
        }
      }
    }

    this.m_known = kl;
    this.m_unknown = wl;
    return eliminated;
  }

  /**
   * Returns whether a service is contained in this composition or not.
   *
   * @param service
   *          The service to look for.
   * @return <code>true</code> if and only if a service is contained in
   *         this composition, <code>false</code> otherwise.
   */
  public final boolean contains(final Service service) {
    return this.m_services.contains(service);
  }

  /**
   * Obtain the string representation of this composition.
   *
   * @return The string representation of this composition.
   */
  @Override
  public final String toString() {
    return '{' + this.m_services.toString() + '}';
  }

  /**
   * Return whether this composition is a valid solution.
   *
   * @return <code>true</code> if and only if this composition is a valid
   *         solution.
   */
  public final boolean isPerfect() {
    return (this.m_unknown.m_count <= 0);
  }

  // /**
  // * Compare this composition to another one.
  // *
  // * @param opponent
  // * The opponent.
  // * @return A value <code> > 0 </code>, if this composition is better
  // * then the other one, <code> < 0 </code> if the opponent is
  // * better, <code> 0 </code> if they're equal.
  // */
  // public final int compareTo(final Composition opponent) {
  // int ul1c, ul2c, el1, el2;
  //
  // ul1c = this.m_unknown.m_count;
  // ul2c = opponent.m_unknown.m_count;
  // if (ul1c <= 0) {
  // if (ul2c <= 0) {
  // return (opponent.m_services.m_count - this.m_services.m_count);
  // }
  // return 1;
  // }
  // if (ul2c <= 0)
  // return -1;
  //
  // el1 = this.m_eliminated;
  // el2 = opponent.m_eliminated;
  //
  // if (el1 > el2)
  // return 1;
  // else if (el2 > el1)
  // return -1;
  //
  // if (ul1c < ul2c)
  // return 1;
  // else if (ul2c < ul1c)
  // return -1;
  //
  // ul1c = this.m_services.m_count;
  // ul2c = opponent.m_services.m_count;
  // if (ul1c < ul2c)
  // return 1;
  // else if (ul2c < ul1c)
  // return -1;
  //
  // return Double.compare(((double) el1) / ((double) (ul1c + 1)),
  //        ((double) el2) / ((double) (ul2c + 1)));
  //  }

  /**
   * Obtain a service list holding the promising services.
   *
   * @return A service list holding the promising services.
   */
  final ServiceList getPromising() {
    ServiceList sl;
    ConceptList k;
    int i;

    k = this.m_unknown;
    i = k.m_count;
    sl = new ServiceList(i << 3);

    for (--i; i >= 0; i--) {
      k.m_data[i].getPromising(sl);
    }

    return sl;
  }

  /**
   * This method creates a complete solution from a single solution.
   *
   * @param composition
   *          The composition initially provided.
   * @param known
   *          The set of known parameters.
   * @param unknown2
   *          The set of parameters wanted.
   * @param clg
   *          the challenge
   * @return The complete solution.
   */
  public static final ServiceList2 complete(final Composition composition,
      final ConceptList known, ConceptList unknown2, final Challenge clg) {
    final ServiceList[] sls;
    final ServiceList2 sl;
    final ConceptList[] prov, pt;
    Concept[] cs;
    ConceptList cx, cy, unknown;
    final ConceptList wx;
    ServiceList sx, s;
    int i, j, k, z;

    if (composition == null)
      return null;

    s = composition.m_services;
    i = s.m_count;

    sl = new ServiceList2(i);
    sl.m_count = i;
    sls = sl.m_data;// new ServiceList[i];

    if (Composer.SINGLE) {
      for (--i; i >= 0; i--) {
        sls[i] = sx = new ServiceList(1);
        sx.add(s.m_data[i]);
      }

      return sl;
    }

    cx = new ConceptList(unknown2.m_count);
    cx.addSimpleList(unknown2);
    unknown = cx;

    prov = new ConceptList[i];
    pt = new ConceptList[i];
    wx = new ConceptList(-1);

    // browse the service composition from front to back
    for (--i; i >= 0; i--) {
      if (clg.solved())
        return null;

      prov[i] = cx = new ConceptList(-1);
      pt[i] = cy = new ConceptList(-1);

      // obtain the outputs of the current service
      cs = s.m_data[i].getOutputs();
      for (j = (cs.length - 1); j >= 0; j--) {
        // add it to the list of provided outputs
        cy.add(cs[j]);
        // check if it provides a wanted output parameters
        k = unknown.includesSubsumptionOf(cs[j]);
        if ((k >= 0)
            && (known.includesSpecializationOf(unknown.m_data[k]) < 0)) {
          cx.add(unknown.remove(k));
        }
      }

      // if this is not the last service in the composition, check what
      // inputs we currently can provide to that last service
      if (i > 0) {

        wx.clear();
        cs = s.m_data[i - 1].getInputs();
        for (j = (cs.length - 1); j >= 0; j--) {
          // add the input to the needed inputs if it is not provided by
          // the known inputs already
          if (known.includesSpecializationOf(cs[j]) < 0) {
            wx.add(cs[j]);
          }
        }

        // check all services we call before the service i-1
        for (j = (pt.length - 1); j >= i; j--) {
          cy = pt[j];

          for (k = (cy.m_count - 1); k >= 0; k--) {
            z = wx.includesSubsumptionOf(cy.m_data[k]);
            if (z >= 0) {
              prov[j].add(wx.remove(z));
            }
          }
        }
      }
    }

    s = new ServiceList(-1);
    cy = new ConceptList(128);
    cy.addSimpleList(known);

    // now gather the services
    for (i = (sls.length - 1); i >= 0; i--) {
      if (clg.solved())
        return null;

      sls[i] = sx = new ServiceList(-1);
      cx = prov[i];
      // get the promising services and keep only these which are promising
      // for all needed parameters
      j = (cx.m_count - 1);
      if (j >= 0) {
        cx.m_data[j].getPromising(sx);

        for (--j; j >= 0; j--) {
          s.clear();
          cx.m_data[j].getPromising(s);

          for (k = (sx.m_count - 1); k >= 0; k--) {
            if (!(s.contains(sx.m_data[k])))
              sx.remove(k);
          }
        }
      }

      // now check if all these services can really be triggered - remove
      // those which cannot be triggered
      rem_trigger: for (j = (sx.m_count - 1); j >= 0; j--) {
        cs = sx.m_data[j].getInputs();

        for (k = (cs.length - 1); k >= 0; k--) {
          if (cy.includesSpecializationOf(cs[k]) < 0) {
            sx.remove(j);
            continue rem_trigger;
          }
        }
      }

      cy.addSimpleList(cx);
    }

    return sl;
  }
}
