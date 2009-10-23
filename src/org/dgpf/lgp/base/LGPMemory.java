/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-11-14
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.lgp.base.LGPMemory.java
 * Last modification: 2007-11-14
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

package org.dgpf.lgp.base;

import java.io.Serializable;
import java.util.Arrays;

import org.sfc.collections.buffers.LimitedDirectBuffer;

/**
 * A linear genetic programming memory record
 * 
 * @author Thomas Weise
 */
public final class LGPMemory extends LimitedDirectBuffer<LGPVMFrame>
    implements Serializable {
  /** the serial version uid */
  private static final long serialVersionUID = 1;

  /**
   * the global memory of the vm
   */
  public final int[] m_global;

  /**
   * the current frame
   */
  public LGPVMFrame m_frame;

  /**
   * the local size
   */
  public final int m_localSize;

  /**
   * the stack size
   */
  public final int m_stackSize;

  /**
   * Create a new lgp memory.
   * 
   * @param globalSize
   *          the global memory size
   * @param localSize
   *          the size of the local memory of the frame
   * @param stackSize
   *          the stack size
   * @param maxCallDepth
   *          the maximum call depth
   */
  public LGPMemory(final int globalSize, final int localSize,
      final int stackSize, final int maxCallDepth) {
    super(maxCallDepth);
    this.m_global = new int[globalSize];
//    this.m_frame = new LGPVMFrame(localSize, stackSize);
    this.m_localSize = localSize;
    this.m_stackSize = stackSize;
  }

  /**
   * Create a new instance of the buffered object type.
   * 
   * @return the new instance, or <code>null</code> if the creation
   *         failed somehow
   */
  @Override
  protected final LGPVMFrame create() {
    return new LGPVMFrame(this.m_localSize, this.m_stackSize);
  }

  /**
   * perform a call
   * 
   * @return the new frame
   */
  public final LGPVMFrame call() {
    LGPVMFrame f;

    f = this.allocate();
    if (f != null) {
      f.m_next = this.m_frame;
      this.m_frame = f;
    }
    return f;
  }

  /**
   * Clear this memory
   */
  public final void clear() {
    Arrays.fill(this.m_global, 0);
    this.disposeAll(this.m_frame);
    this.m_frame = this.allocate();
    this.m_frame.clear();
  }

}
