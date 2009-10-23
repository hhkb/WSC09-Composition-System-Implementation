/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-03-05
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.machine.ll.programBuilder.Construct.java
 * Last modification: 2007-03-05
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

package org.dgpf.lgp.base.programBuilder;

import org.dgpf.lgp.base.programBuilder.ProgramBuilder.BuilderCache;

/**
 * The construct
 * 
 * @author Thomas Weise
 */
public class Construct {
  /**
   * the lock object
   */
  private static final Object LOCK = new Object();

  /**
   * the construct cache
   */
  static Construct s_cache;

  static {
    new BuilderCache() {
      @Override
      protected void clear() {
        s_cache = null;
      }
    };
  }

  /**
   * the variable counter
   */
  int m_vc;

  /**
   * the global count
   */
  int m_gc;

  /**
   * the local count
   */
  int m_lc;

  /**
   * the intermediate variables
   */
  Variable m_intermed;

  /**
   * the next construct
   */
  Construct m_next;

  /**
   * Clear this construct. This method clears this construct and
   * subsequently invokes {{@link #dispose()}.
   */
  protected void clear() {
    this.m_vc = 0;
    this.m_gc = 0;
    this.m_lc = 0;
    this.m_intermed = null;
    this.dispose();
  }

  /**
   * Dispose this construct
   */
  protected void dispose() {
    synchronized (LOCK) {
      this.m_next = s_cache;
      s_cache = this;
    }
  }

  /**
   * Allocate a new construct
   * 
   * @return the new construct
   */
  static final Construct allocate() {
    Construct c;

    synchronized (LOCK) {
      c = s_cache;
      if (c != null)
        s_cache = c.m_next;
    }
    if (c == null)
      return new Construct();
    return c;
  }

}
