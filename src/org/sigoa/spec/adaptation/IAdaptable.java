/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-12
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.adaptation.IAdaptable.java
 * Last modification: 2006-12-12
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

package org.sigoa.spec.adaptation;

import java.util.List;

/**
 * The <code>IAdaptable</code>-interface is common to all objects that
 * own a set of rules and peridically carry them out. This is a very simple
 * yet effective way to enable auto-adaptation. A new class MyAdaptable
 * would implement this interface as follows: public class MyAdaptable
 * implements IAdaptable<MyAdaptable>
 *
 * @author Thomas Weise
 * @version 1.0.0
 * @param <S>
 *          The generic parameter which should later be replaced by the
 *          name of the implementing class.
 */
public interface IAdaptable<S> {
  /**
   * Add a rule to this adaptable object.
   *
   * @param rule
   *          the rule to be added
   * @throws IllegalArgumentException
   *           if <code>rule==null</code>
   */
  public abstract void addRule(final IRule<S> rule);

  /**
   * Remove a rule to this adaptable object.
   *
   * @param rule
   *          the rule to be removed
   * @throws IllegalArgumentException
   *           if <code>rule==null</code>
   */
  public abstract void removeRule(final IRule<S> rule);

  /**
   * Get a copy of the internal rule list.
   *
   * @return a list with the internal rules
   */
  public abstract List<IRule<S>> getRules();

}
