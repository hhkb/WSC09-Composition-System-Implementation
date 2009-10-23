/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-18
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.aggregation.net.genetics.AggregationNetProgramReproduction.java
 * Last modification: 2007-12-18
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

package org.dgpf.aggregation.net.genetics;

import java.io.Serializable;

import org.dgpf.aggregation.base.Command;
import org.dgpf.aggregation.base.IAggregationParameters;
import org.dgpf.aggregation.base.constructs.Program;
import org.dgpf.aggregation.net.AggregationNetProgram;
import org.sfc.math.Mathematics;
import org.sigoa.refimpl.genomes.intString.IntStringEditor;
import org.sigoa.refimpl.genomes.string.StringCreator;
import org.sigoa.refimpl.genomes.string.VariableLengthStringMutator;
import org.sigoa.refimpl.genomes.string.VariableLengthStringNPointCrossover;
import org.sigoa.refimpl.genomes.tree.reproduction.DefaultTreeMutator;
import org.sigoa.refimpl.genomes.tree.reproduction.NodeFactorySet;
import org.sigoa.refimpl.genomes.tree.reproduction.SubTreeExchangeCrossover;
import org.sigoa.refimpl.genomes.tree.reproduction.TreeCreator;
import org.sigoa.refimpl.go.ImplementationBase;
import org.sigoa.spec.go.reproduction.ICreator;
import org.sigoa.spec.go.reproduction.ICrossover;
import org.sigoa.spec.go.reproduction.IMutator;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * This is the basic net aggregation program reproduction operation.
 * 
 * @author Thomas Weise
 */
public class AggregationNetProgramReproduction extends
    ImplementationBase<AggregationNetProgram, Serializable> implements
    ICreator<AggregationNetProgram>, IMutator<AggregationNetProgram>,
    ICrossover<AggregationNetProgram> {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the minimum ints
   */
  private static final int[] MIN_INTS = new int[] { 0 };

  /**
   * the string editor
   */
  private final IntStringEditor m_editor;

  /**
   * the virtual machine information record
   */
  private final IAggregationParameters m_info;

  /**
   * the tree creator
   */
  private final TreeCreator m_tcreator;

  /**
   * the string creator
   */
  private final StringCreator<int[]> m_screator;

  /**
   * the tree mutator
   */
  private final DefaultTreeMutator m_tmutator;

  /**
   * the string mutator
   */
  private final VariableLengthStringMutator<int[]> m_smutator;

  /**
   * the string crossover
   */
  private final VariableLengthStringNPointCrossover<int[]> m_scrossover;

  /**
   * the memory size
   */
  private final int m_memorySize;

  /**
   * Create a new aggregation net program reproduction unit
   * 
   * @param info
   *          the information record
   */
  public AggregationNetProgramReproduction(
      final IAggregationParameters info) {
    super();

    NodeFactorySet s;

    this.m_info = info;
    this.m_editor = new IntStringEditor(1, 1, this.m_memorySize = info
        .getMemorySize());

    s = info.getLanguage();

    this.m_tcreator = new TreeCreator(s, 7, 0.25d, Program.EMPTY_PROGRAM);
    this.m_screator = new StringCreator<int[]>(this.m_editor);

    this.m_smutator = new VariableLengthStringMutator<int[]>(this.m_editor);
    this.m_tmutator = new DefaultTreeMutator(new TreeCreator(s, 4, 0.25d,
        Program.EMPTY_PROGRAM));

    this.m_scrossover = new VariableLengthStringNPointCrossover<int[]>(
        this.m_editor);
  }

  /**
   * Create a new aggregation net program
   * 
   * @param c
   *          the construct
   * @param in
   *          the input
   * @param out
   *          the output
   * @return the program
   */
  protected AggregationNetProgram build(final Program c, final int[] in,
      final int[] out) {

    int l, v, i, j;
    final int ms;
    final int[] ix, ox;

    l = Math.min(in.length, out.length);
    ms = this.m_memorySize;

    if (l <= 0) {
      ix = MIN_INTS;
      ox = MIN_INTS;
    } else {
      outer: for (i = 0; i < l; i++) {
        in[i] = v = Mathematics.modulo(in[i], ms);
        for (j = 0; j < i; j++) {
          if (v == in[j]) {
            l--;
            System.arraycopy(in, i + 1, in, i, l - i);
            System.arraycopy(out, i + 1, out, i, l - i);
            i--;
            continue outer;
          }
        }
        out[i] = Mathematics.modulo(out[i], ms);
      }
      if (l == in.length)
        ix = in;
      else {
        ix = new int[l];
        System.arraycopy(in, 0, ix, 0, l);
      }

      if (l == out.length)
        ox = out;
      else {
        ox = new int[l];
        System.arraycopy(out, 0, ox, 0, l);
      }
    }

    Command.postProcess(this.m_info, c);
    return new AggregationNetProgram(c, ox, ix);
  }

  /**
   * Create a single new random genotype
   * 
   * @param random
   *          The randomizer to be used.
   * @return The resulting genotype.
   * @throws NullPointerException
   *           if <code>random==null</code>.
   */
  public AggregationNetProgram create(final IRandomizer random) {
    return this.build(((Program) (this.m_tcreator.create(random))),
        this.m_screator.create(random), this.m_screator.create(random));
  }

  /**
   * Perform one single mutation.
   * 
   * @param source
   *          The source genotype.
   * @param random
   *          The randomizer to be used.
   * @return The resulting genotype.
   * @throws NullPointerException
   *           if <code>source==null||random==null</code>.
   */
  public AggregationNetProgram mutate(final AggregationNetProgram source,
      final IRandomizer random) {
    int[] in, out;
    boolean b;
    Command c;

    if (source == null)
      return this.create(random);

    c = source.m_root;
    out = source.m_out;
    in = source.m_in;

    b = true;
    do {
      if (random.nextInt(3) == 0) {
        in = this.m_smutator.mutate(in, random);
        b = false;
      }
      if (random.nextInt(3) == 0) {
        out = this.m_smutator.mutate(out, random);
        b = false;
      }
      if (random.nextBoolean()) {
        c = ((Command) (this.m_tmutator.mutate(c, random)));
        b = false;
      }
    } while (b);

    return this.build(source.m_root, in, out);
  }

  /**
   * Perform one single recombination/crossover.
   * 
   * @param source1
   *          The first source genotype.
   * @param source2
   *          The second source genotype.
   * @param random
   *          The randomizer to be used.
   * @return The resulting genotype.
   * @throws NullPointerException
   *           if <code>source1==null||source2==null||random==null</code>.
   */
  public AggregationNetProgram crossover(
      final AggregationNetProgram source1,
      final AggregationNetProgram source2, final IRandomizer random) {
    Program c;
    int[] in, out;
    int d;

    do {
      switch (random.nextInt(4)) {
      case 0: {
        c = source1.m_root;
        d = 1;
        break;
      }
      case 1: {
        c = source2.m_root;
        d = -1;
        break;
      }
      default: {
        c = ((Program) (SubTreeExchangeCrossover.SUB_TREE_EXCHANGE_CROSSOVER
            .crossover(source1.m_root, source2.m_root, random)));
        if (c == source1.m_root)
          d = 1;
        else if (c == source2.m_root)
          d = -1;
        else
          d = 0;
        break;
      }
      }

      switch (random.nextInt(3)) {
      case 0: {
        in = source1.m_in;
        if (d != 1)
          d = 0;
        break;
      }
      case 1: {
        in = source2.m_in;
        if (d != -1)
          d = 0;
        break;
      }
      default: {
        in = this.m_scrossover.crossover(source1.m_in, source2.m_in,
            random);
        d = 0;
        break;
      }
      }

      switch (random.nextInt(3)) {
      case 0: {
        out = source1.m_in;
        if (d != 1)
          d = 0;
        break;
      }
      case 1: {
        out = source2.m_in;
        if (d != -1)
          d = 0;
        break;
      }
      default: {
        out = this.m_scrossover.crossover(source1.m_out, source2.m_out,
            random);
        d = 0;
        break;
      }
      }
    } while (d != 0);

    return this.build(c, in, out);
  }

}
