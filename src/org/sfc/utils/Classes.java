/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 *
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-12-21
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.utils.Classes.java
 * Last modification: 2006-12-21
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

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * A class for class hierarchy utilities.
 *
 * @author Thomas Weise
 */
public final class Classes {

/**
 * the empty class array
 */
  public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
  /**
   * the empty object array
   */
  public static final Object[] EMPTY_OBJECT_ARRAY = EMPTY_CLASS_ARRAY;

  /**
   * the one object array
   */
  private static final Object[] ONE_OBJECT_ARRAY = new Object[] { new Object() };


  /**
   * This method tries to find a static member with the value of the
   * specified constant. This method assumes that the member is declared in
   * the same class that is also the class of the object.
   *
   * @param o
   *          the constant to search
   * @return the found static member, or <code>null</code> if none could
   *         be found
   */
  public static final Field findStaticField(final Object o) {
    Class<?> c;

    if (o == null)
      return null;

    c = o.getClass();
    return findStaticField(o,c,c);
  }

    /**
     * This method tries to find a static member with the value of the
     * specified constant. This method assumes that the member is declared
     * in the same class that is also the class of the object.
     *
     * @param o
     *          the constant to search
     * @param search
     *          the class to be searched
     * @return the found static member, or <code>null</code> if none
     *         could be found
     */
  public static final Field findStaticField(final Object o,
      final Class<?> search) {
    Class<?> c;

    if (o == null)
      return null;

    c = o.getClass();
    return findStaticField(o,search,c);
  }

  /**
   * This method tries to find a static member with the value of the
   * specified constant. This method assumes that the member is declared in
   * the same class that is also the class of the object.
   *
   * @param o
   *          the constant to search
   * @param cc
   *          the class
   * @param oc
   *          the class of o
   * @return the found static member, or <code>null</code> if none could
   *         be found
   */
  private static final Field findStaticField(final Object o,
      final Class<?> cc, final Class<?> oc) {
    Class<?>  c2;
    Method m;
    Constructor<?> ccd;
    Field[] ff;
    Field f;
    int i;
    Object q;

    if (o == null)
      return null;

      ff = cc.getFields();

        for (i = (ff.length - 1); i >= 0; i--) {
          f = ff[i];

          if (Modifier.isStatic(f.getModifiers())
              && f.getType().isAssignableFrom(oc)) {
            try {
              q = f.get(null);
              if (o.equals(q)) {
                return f;
              }
            } catch (Throwable t) {
              //
            }
          }
        }


      c2 = cc.getEnclosingClass();
      if(c2 != null)
      {
      f = findStaticField(o,c2,oc);
      if(f!=null) return f;
      }

      m = cc.getEnclosingMethod();
      if (m != null)
      {
        f = findStaticField(o,m.getDeclaringClass(),oc);
      if(f!=null) return f;
      }

          ccd = cc.getEnclosingConstructor();
          if (ccd != null) {
        f = findStaticField(o,ccd.getDeclaringClass(),oc);
      if(f!=null) return f;
      }


    return null;
  }

  /**
   * Find a parameterless getter method in <code>o</code> the returns the
   * value <code>value</code>.
   *
   * @param o
   *          the object to investigate -- this is either the instance of a
   *          class or the class itself. In the latter case, only static
   *          getters can be found.
   * @param value
   *          the value returned by the optional getter
   * @return the getter method, or <code>null</code> if none could be
   *         found.
   */
  public static final Method findGetter(final Object o, final Object value) {
    Object obj, res;
    Class<?> cls;
    Method[] mm;
    Method m;
    int i;
    Class<?> vc;

    if (o == null)
      return null;

    if (o instanceof Class) {
      obj = null;
      cls = ((Class<?>) o);
    } else {
      obj = o;
      cls = o.getClass();
    }

    if (value != null)
      vc = value.getClass();
    else
      vc = null;

    mm = cls.getMethods();
    for (i = (mm.length - 1); i >= 0; i--) {
      m = mm[i];
      if ((vc == null) || (m.getReturnType().isAssignableFrom(vc))) {
        if (Modifier.isStatic(m.getModifiers()) || (obj != null)) {
          if((m.getParameterTypes().length <= 0)&&//
              (m.getName().startsWith("get"))) { //$NON-NLS-1$
            try {
              res = m.invoke(obj, EMPTY_OBJECT_ARRAY);
              if (res == value)
                return m;
            } catch (Throwable t) {
              //
            }
          }
        }
      }
    }

    return null;
  }

  /**
   * Ensure that the following classes are loaded and ready.
   *
   * @param classes
   *          The classes to load.
   */
  public static final void loadClasses(final Class<?>[] classes) {
    int f;
    Object[] p;
    try {
      for (Class<?> c : classes) {

        // querry all possible members
        for (Field m : c.getFields()) {
          f = m.getModifiers();
          if (Modifier.isStatic(f) && Modifier.isPublic(f)) {
            try {
              m.get(null);
            } catch (Throwable t) {
              //
            }
          }
        }

        // try to invoke all possible methods (with wrong parameters)
        for (Method m : c.getMethods()) {
          p = ((m.getParameterTypes().length == 0) ? ONE_OBJECT_ARRAY
              : EMPTY_OBJECT_ARRAY);
          try {
            m.invoke(null, p);
          } catch (Throwable t) {
            //
          }
        }

        // try to invoke all possible constructors (with wrong parameters)
        for (Constructor<?> m : c.getConstructors()) {
          p = ((m.getParameterTypes().length == 0) ? ONE_OBJECT_ARRAY
              : EMPTY_OBJECT_ARRAY);
          try {
            m.newInstance(p);
          } catch (Throwable t) {
            //
          }
        }

        try {
          c.getClassLoader().loadClass(c.getCanonicalName());
        } catch (Throwable t) {
          //
        }

        try {
          loadClasses(c.getDeclaredClasses());
        } catch (Throwable x) {//
        }


        try {
          loadClasses(new Class<?>[] {
              c.getSuperclass()
              });
        } catch (Throwable x) {//
        }

      }
    } catch (Throwable q) {//

    }
  }

/**
 * Clone <code>object</code> via reflection.
 *
 * @param object
 *          the object to be cloned
 * @param <OT>
 *          the object type
 * @return a copy of the object, or the object itself if it could not be
 *         cloned
 */
  @SuppressWarnings("unchecked")
  public  static final <OT> OT clone(final OT object){
    Class<?> c;
    Object cpy;
    int i;

    if(object instanceof Cloneable){

    c = object.getClass();
    if(c.isArray()){
      cpy = Array.newInstance(c.getComponentType(), i = Array.getLength(object));
      System.arraycopy(object, 0, cpy, 0, i);
      return (OT)cpy;
    }
      try {
        return (OT)(c.getDeclaredMethod("clone", (Class[])null) //$NON-NLS-1$
                      .invoke(object, (Object[])null));
        } catch(Throwable t){//
        ErrorUtils.onError(t);
          }
      }


    return object;
  }

  /**
   * You cannot instantiate this class.
   */
  private Classes() {
    ErrorUtils.doNotCall();
  }
}
