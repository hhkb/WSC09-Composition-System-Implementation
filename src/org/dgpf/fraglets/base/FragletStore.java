/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-15
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.fraglets.base.FragletStore.java
 * Last modification: 2007-12-15
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

package org.dgpf.fraglets.base;

import java.io.Serializable;
import java.util.Arrays;

import org.dgpf.vm.base.EVirtualMachineState;
import org.dgpf.vm.base.VirtualMachine;
import org.sfc.collections.buffers.LimitedDirectBuffer;
import org.sfc.text.TextUtils;
import org.sigoa.refimpl.stoch.Randomizer;
import org.sigoa.spec.stoch.IRandomizer;

/**
 * The fraglet storage.
 * 
 * @author Thomas Weise
 */
public class FragletStore extends LimitedDirectBuffer<Fraglet> implements
    Serializable {
  /** the serial version uid */
  private static final long serialVersionUID = 1;

  /**
   * the maximum total count of fraglets
   */
  private final int m_maxCount;

  /**
   * the maximum fraglet length
   */
  public final int m_maxLength;

  /**
   * the fraglet memory
   */
  private Fraglet[] m_memory;

  /**
   * the current number of fraglets
   */
  private int m_num;

  /**
   * the total number of fraglets
   */
  private int m_total;

  /**
   * the instruction set.
   */
  public final InstructionSet m_instructions;

  /**
   * the randomizer
   */
  public final Randomizer m_randomizer;

  /**
   * the find array
   */
  private final boolean[] m_find;

  /**
   * the instruction count
   */
  private final int m_instrC;

  /**
   * the number of reactions
   */
  private int m_reactions;

  /**
   * Create a fraglet store
   * 
   * @param parameters
   *          the fraglet parameters
   */
  public FragletStore(final IFragletParameters parameters) {
    super(parameters.getMemorySize() + 3);

    int l;

    l = parameters.getMemorySize();
    this.m_memory = new Fraglet[l];
    this.m_find = new boolean[l];

    this.m_maxCount = parameters.getMaxFragletCount();
    this.m_maxLength = parameters.getMaxFragletSize();
    this.m_instructions = parameters.getInstructionSet();
    this.m_instrC = this.m_instructions.size();
    this.m_randomizer = new Randomizer();
  }

  /**
   * Append this object's textual representation to a string builder.
   * 
   * @param sb
   *          The string builder to append to.
   * @see #toString()
   */
  @Override
  public void toStringBuilder(final StringBuilder sb) {
    final InstructionSet is;
    final int i;
    int j;
    final Fraglet[] c;

    i = this.m_num;
    if (i <= 0)
      sb.append("<empty>"); //$NON-NLS-1$

    c = this.m_memory;
    is = this.m_instructions;

    for (j = 0; j < i; j++) {
      if (j > 0)
        sb.append(TextUtils.LINE_SEPARATOR);
      c[j].toStringBuilder(sb, is);
    }
  }

  /**
   * Find a fraglet that begins with the given symbol. The caller is
   * responsible for disposing the returned fraglet.
   * 
   * @param begin
   *          the symbol with which the fraglet is to begin.
   * @param remove
   *          <code>true</code> if the fraglet should be removed
   *          immediately after finding, <code>false</code> otherwise
   * @return the fraglet found, or <code>null</code> if none was found.
   *         The caller is responsible for disposing or reusing the
   *         returned fraglet.
   */
  public final Fraglet findFraglet(final int begin, final boolean remove) {
    final Fraglet[] fs;
    final boolean[] b;
    final IRandomizer rand;
    int n, z, r, x, num;
    Fraglet f, f2;
    final int ic;

    num = this.m_num;
    if (num <= 0)
      return null;
    fs = this.m_memory;

    z = num;
    b = this.m_find;
    Arrays.fill(b, 0, z, false);

    ic = this.m_instrC;

    rand = this.m_randomizer;
    for (;;) {

      r = rand.nextInt(this.m_total);

      for (n = (num - 1); (r >= (x = ((f = fs[n]).m_count))) && (n > 0); r -= x, n--) {
        //
      }

      if (b[n])
        continue;
      b[n] = true;

      if (InstructionUtils.isEqual(f.m_code[0], begin, ic)) {

        if (remove) {
          this.m_reactions++;
          this.m_total--;
          if ((--f.m_count) <= 0) {
            fs[n] = fs[--num];
            this.m_num = num;
            return f;
          }
        }

        f2 = this.allocate();
        if (f2 == null)
          return null;

        System.arraycopy(f.m_code, 0, f2.m_code, 0, f2.m_len = f.m_len);
        return f2;
      }

      if ((--z) <= 0)
        return null;
    }
  }

  /**
   * Enter a fraglet into the given virtual machine.
   * 
   * @param fraglet
   *          the fraglet
   * @param vm
   *          the virtual machine
   * @return <code>true</code> if everything went ok, <code>false</code>
   *         on error - either way, <code>fraglet</code> must not be
   *         accessed any further
   */
  public final boolean enterFraglet(final Fraglet fraglet,
      final VirtualMachine<FragletStore, FragletProgram> vm) {
    int c, i, n;
    final Fraglet[] fs;
    Fraglet f2;
    final int ic;
    int[] i1, i2;

    if ((fraglet == null) || (this.m_total >= this.m_maxCount)) {
      vm.setError();
      if (fraglet != null)
        this.dispose(fraglet);
      return false;
    }

    if ((fraglet.m_len <= 0)
        || (!(InstructionUtils.isInstruction(fraglet.m_code[0])))) {
      this.dispose(fraglet);
      return true;
    }

    ic = this.m_instrC;
    fs = this.m_memory;
    n = this.m_num;

    outer: for (i = (n - 1); i >= 0; i--) {
      f2 = fs[i];
      if ((c = f2.m_len) == fraglet.m_len) {
        i1 = f2.m_code;
        i2 = fraglet.m_code;
        for (--c; c >= 0; c--) {
          if (!(InstructionUtils.isEqual(i1[c], i2[c], ic)))
            continue outer;
        }

        f2.m_count++;
        this.m_total++;
        this.dispose(fraglet);
        this.m_reactions++;
        return true;
      }
    }

    if (n >= fs.length) {
      this.dispose(fraglet);
      if (vm != null)
        vm.setError();
      return false;
    }

    fs[n++] = fraglet;
    fraglet.m_count = 1;
    this.m_num = n;
    this.m_total++;
    this.m_reactions++;

    return true;
  }

  /**
   * Perform on execution step.
   * 
   * @param vm
   *          the virtual machine
   * @return the result of the execution
   */
  @SuppressWarnings("unchecked")
  public final EVirtualMachineState execute(
      final VirtualMachine<FragletStore, FragletProgram> vm) {

    int r, n, x;
    final Fraglet[] fs;
    Fraglet f;
    boolean b;

    n = this.m_num;
    if (n <= 0)
      return EVirtualMachineState.NOTHING;

    fs = this.m_memory;
    r = this.m_randomizer.nextInt(this.m_total);

    for (--n; (r >= (x = ((f = fs[n]).m_count))) && (n > 0); r -= x, n--) {
      //
    }

    r = f.m_code[0];
    if (InstructionUtils.isInstruction(r)) {
      b = (((Instruction<VirtualMachine<FragletStore, FragletProgram>>) // 
      (this.m_instructions.get(InstructionUtils.decodeInstruction(r,
          this.m_instrC)))).execute(vm, f));
    } else
      b = true;

    if (b) {
      all: {
        inner: {
          for (n = (this.m_num - 1); n >= 0; n--) {
            if (fs[n] == f)
              break inner;
          }
          break all;
        }
        this.m_total--;
        this.m_reactions++;

        if ((--f.m_count) <= 0) {
          this.dispose(f);
          fs[n] = fs[--this.m_num];
        }
      }
    }

    return EVirtualMachineState.CHANGED;
  }

  /**
   * Create a new instance of the buffered object type.
   * 
   * @return the new instance, or <code>null</code> if the creation
   *         failed somehow
   */
  @Override
  protected Fraglet create() {
    return new Fraglet(this.m_maxLength);
  }

  /**
   * Initialize the fraglet memory
   * 
   * @param program
   *          the fraglet program
   * @param vm
   *          the virtual machine
   */
  public void initialize(final FragletProgram program,
      final VirtualMachine<FragletStore, FragletProgram> vm) {
    int l;
    final Fraglet[] c;
    Fraglet f;
    final int[][] cc;

    c = this.m_memory;
    for (l = (this.m_num - 1); l >= 0; l--) {
      this.dispose(c[l]);
    }
    this.m_num = 0;
    this.m_total = 0;

    cc = program.m_code;
    for (l = 0; l < cc.length; l++) {
      f = this.allocate();
      if (f == null) {
        vm.setError();
        return;
      }
      f.init(cc[l]);
      if (!(this.enterFraglet(f, vm))) {
        vm.setError();
        return;
      }
    }

    this.m_reactions = 0;
  }

  /**
   * Obtain the number of different fraglets on this vm.
   * 
   * @return the number of different fraglets on this vm
   */
  public final int size() {
    return this.m_num;
  }

  /**
   * Peek the <code>index</code>th fraglet family. This method is for
   * analysis only, you must not perform anything stupid with the fraglet
   * obtained.
   * 
   * @param index
   *          the index of the wanted fraglet family
   * @return the fraglet family
   */
  public final Fraglet peekFraglet(final int index) {
    return this.m_memory[index];
  }

  /**
   * Obtain the number of reactions that took place.
   * 
   * @return the number of reactions that took place
   */
  public final int getReactionCount() {
    return this.m_reactions;
  }
}
