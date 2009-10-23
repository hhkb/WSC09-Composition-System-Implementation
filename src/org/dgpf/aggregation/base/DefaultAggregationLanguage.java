/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-02
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.aggregation.base.DefaultAggregationLanguage.java
 * Last modification: 2007-03-02
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

package org.dgpf.aggregation.base;

import org.dgpf.aggregation.base.constructs.FormulaFactory;
import org.dgpf.aggregation.base.constructs.PartFactory;
import org.dgpf.aggregation.base.constructs.ProgramFactory;
import org.dgpf.symbolicRegression.vector.real.DefaultRealLanguage;
import org.sigoa.refimpl.genomes.tree.INodeFactory;

/**
 * The default high level language
 * 
 * @author Thomas Weise
 */
public final class DefaultAggregationLanguage extends DefaultRealLanguage {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /**
   * The node factories
   */
  private static final INodeFactory[] FACTORIES = new INodeFactory[] {//
  PartFactory.PART_FACTORY,//
      FormulaFactory.FORMULA_FACTORY,//

  };

  /**
   * the weights
   */
  private static final double[] WEIGHTS = new double[] {//
  1.0d,// PartFactory.PART_FACTORY
      1.0d,// FormulaFactory.FORMULA_FACTORY,//
  };

  // /**
  // * the default language
  // */
  // public static final NodeFactorySet DEFAULT_AGGREGATION_LANGUAGE = new
  // DefaultAggregationLanguage();

  /**
   * Create a new default language.
   * 
   * @param memorySize
   *          the memory size
   */
  public DefaultAggregationLanguage(final int memorySize) {
    super(new DefaultRealLanguage(memorySize), FACTORIES, WEIGHTS,
        ProgramFactory.PROGRAM_FACTORY);
  }

  // /**
  // * the read resolver
  // *
  // * @return the default language
  // */
  // private final Object readResolve() {
  // if (this.getClass() == DefaultAggregationLanguage.class)
  // return DEFAULT_AGGREGATION_LANGUAGE;
  // return this;
  // }
  //
  // /**
  // * the write resolver
  // *
  // * @return the default language
  // */
  // private final Object writeReplace() {
  // return this.readResolve();
  // }
}
