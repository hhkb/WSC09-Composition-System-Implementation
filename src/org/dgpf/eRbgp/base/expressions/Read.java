/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2008-04-17
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.eRbgp.base.expressions.And.java
 * Last modification: 2008-04-17
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

package org.dgpf.eRbgp.base.expressions;

import org.dgpf.rbgp.base.RBGPMemory;
import org.dgpf.rbgp.base.RBGPProgramBase;
import org.dgpf.rbgp.base.Symbol;
import org.dgpf.rbgp.base.SymbolSet;
import org.dgpf.vm.base.VirtualMachine;
import org.sfc.math.Mathematics;
import org.sigoa.refimpl.genomes.tree.INodeFactory;

/**
 * the logical and
 * 
 * @author Thomas Weise
 */
public class Read extends Expression {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * the cache
   */
  static Read[] CACHE;

  /**
   * the memory address
   */
  private final Symbol m_symbol;

  /**
   * Create a new read expression
   * 
   * @param symbol
   *          the symbol
   */
  public Read(final Symbol symbol) {
    super(null);
    this.m_symbol = symbol;
  }

  /**
   * Create a new read expression
   * 
   * @param index
   *          the index
   * @return the read expression
   */
  public static final Read createRead(final int index) {
    return CACHE[Mathematics.modulo(index, CACHE.length)];
  }

  /**
   * Initialize the cache
   * 
   * @param symbols
   *          the symbol set
   */
  public static final void initSymbols(final SymbolSet symbols) {
    int i;

    i = symbols.size();
    CACHE = new Read[i];

    for (--i; i >= 0; i--) {
      CACHE[i] = new Read(symbols.get(i));
    }
  }

  /**
   * Execute this construct
   * 
   * @param vm
   *          the virtual machine
   * @return an arbitrary return value
   */
  @Override
  @SuppressWarnings("unchecked")
  public int execute(VirtualMachine<RBGPMemory, RBGPProgramBase> vm) {
    return vm.m_memory.m_mem1[this.m_symbol.m_id];
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
    this.m_symbol.toStringBuilder(sb);
  }

  /**
   * Obtain the factory which deals with nodes of the same type as this
   * node.
   * 
   * @return the factory which deals with nodes of the same type as this
   *         node
   */
  @Override
  public INodeFactory getFactory() {
    return ExpressionFactory.READ_FACTORY;
  }
  
  /**
   * Serializes the parameters of the constructor of this object.
   * 
   * @param sb
   *          the string builder
   * @param indent
   *          an optional parameter denoting the indentation
   */
  @Override
  protected void javaParametersToStringBuilder(final StringBuilder sb,
      final int indent) {
    this.m_symbol.javaToStringBuilder(sb, indent);
  }
  
  /**
   * check for equality
   * 
   * @param o
   *          the object to compare with
   * @return <code>true</code> if and only if the two objects equal
   */
  @Override
  public boolean equals(final Object o) {
    if (!(super.equals(o)))
      return false;
    return ((Read) o).m_symbol.equals(this.m_symbol);
  }
}
