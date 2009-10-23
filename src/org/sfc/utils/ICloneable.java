/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-11-22
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.utils.ICloneable.java
 * Last modification: 2006-11-22
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

package org.sfc.utils;

/**
 * This interface is common to cloneable sfc objects.
 * 
 * @see Cloneable
 * @author Thomas Weise
 */
public interface ICloneable extends Cloneable {
  /**
   * Creates and returns a copy of this object. The precise meaning of
   * "copy" may depend on the class of the object. The general intent is
   * that, for any object <code>x</code>, the expression:
   * 
   * <pre>
   * x.clone() != x
   * </pre>
   * 
   * will be true, and that the expression:
   * 
   * <pre>
   * x.clone().getClass() == x.getClass()
   * </pre>
   * 
   * will be <code>true</code>, but these are not absolute requirements.
   * While it is typically the case that:
   * 
   * <pre>
   * x.clone().equals(x)
   * </pre>
   * 
   * </blockquote> will be <code>true</code>, this is not an absolute
   * requirement.
   * <p>
   * By convention, the returned object should be obtained by calling
   * <code>super.clone</code>. If a class and all of its superclasses
   * (except <code>Object</code>) obey this convention, it will be the
   * case that <code>x.clone().getClass() == x.getClass()</code>.
   * </p>
   * <p>
   * By convention, the object returned by this method should be
   * independent of this object (which is being cloned). To achieve this
   * independence, it may be necessary to modify one or more fields of the
   * object returned by <code>super.clone</code> before returning it.
   * Typically, this means copying any mutable objects that comprise the
   * internal "deep structure" of the object being cloned and replacing the
   * references to these objects with references to the copies. If a class
   * contains only primitive fields or references to immutable objects,
   * then it is usually the case that no fields in the object returned by
   * <code>super.clone</code> need to be modified.
   * </p>
   * 
   * @return a clone of this instance.
   * @see java.lang.Cloneable
   */
  public abstract Object clone();
}
