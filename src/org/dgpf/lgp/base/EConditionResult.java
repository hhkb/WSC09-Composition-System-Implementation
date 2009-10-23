/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-02-21
 * Creator          : Thomas Weise
 * Original Filename: org.dgpf.lgp.base.EConditionResult.java
 * Last modification: 2007-02-21
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


/**
 * This enum contains the possible condition results
 * 
 * @author Thomas Weise
 */
public enum EConditionResult {
  /**
   * The condition can never become <code>true</code>
   */
  FALSE,
  /**
   * the condition can never become <code>false</code>
   */
  TRUE,
  /**
   * the condition can become either <code>true</code> or
   * <code>false</code>
   */
  UNDECIDED;

  /**
   * Check how the outcome of the given condition will be.
   * 
   * @param condition
   *          the condition
   * @param indirection1
   *          the first indirection
   * @param index1
   *          the first index
   * @param indirection2
   *          the second indirection
   * @param index2
   *          the second index
   * @return the expression's result
   */
  public static final EConditionResult getResult(
      final ECondition condition, final EIndirection indirection1,
      final int index1, final EIndirection indirection2, final int index2) {
    long a, b;

    if ((condition == null) || (condition == ECondition.TRUE))
      return TRUE;
    if (condition == ECondition.FALSE)
      return FALSE;

    if (indirection1 == indirection2) {
      if (index1 == index2) {
        switch (condition) {
        case NC_AND_NZ:
        case S_XOR_O:
        case NZ:
        case C:
        case N_Z_AND_S_EXOR_O: {
          return FALSE;
        }

        case S_EXOR_O:
        case Z:
        case NC:
        case Z_OR_S_XOR_O: {
          return TRUE;
        }

        default: {
          break;
        }
        }
      }

      if (indirection1 == EIndirection.CONSTANT) {
        if (index1 > index2) {
          if ((condition == ECondition.GREATER)
              || (condition == ECondition.GREATER_OR_EQUAL)
              || (condition == ECondition.NOT_EQUAL))
            return TRUE;
          if ((condition == ECondition.LESS)
              || (condition == ECondition.LESS_OR_EQUAL)
              || (condition == ECondition.EQUAL))
            return FALSE;
        }

        if (index1 < index2) {
          if ((condition == ECondition.GREATER)
              || (condition == ECondition.GREATER_OR_EQUAL)
              || (condition == ECondition.NOT_EQUAL))
            return FALSE;
          if ((condition == ECondition.LESS)
              || (condition == ECondition.LESS_OR_EQUAL)
              || (condition == ECondition.EQUAL))
            return TRUE;
        }

        a = (index1 & 0xFFFFFFFFl);
        b = (index2 & 0xFFFFFFFFl);

        if (a > b) {
          if ((condition == ECondition.ABOVE)
              || (condition == ECondition.ABOVE_OR_EQUAL)
              || (condition == ECondition.NOT_EQUAL))
            return TRUE;
          if ((condition == ECondition.BELOW)
              || (condition == ECondition.BELOW_OR_EQUAL)
              || (condition == ECondition.EQUAL))
            return FALSE;
        }

        if (a < b) {
          if ((condition == ECondition.ABOVE)
              || (condition == ECondition.ABOVE_OR_EQUAL)
              || (condition == ECondition.NOT_EQUAL))
            return FALSE;
          if ((condition == ECondition.BELOW)
              || (condition == ECondition.BELOW_OR_EQUAL)
              || (condition == ECondition.EQUAL))
            return TRUE;
        }
      }
    }

    if (indirection1 == EIndirection.CONSTANT) {
      if (index1 == Integer.MAX_VALUE) {
        if (condition == ECondition.GREATER_OR_EQUAL)
          return TRUE;
        if (condition == ECondition.LESS)
          return FALSE;
        return UNDECIDED;
      }
      if (index1 == Integer.MIN_VALUE) {
        if (condition == ECondition.LESS_OR_EQUAL)
          return TRUE;
        if (condition == ECondition.GREATER)
          return FALSE;
        return UNDECIDED;
      }
      if (index1 == 0xffffffff) {
        if (condition == ECondition.ABOVE_OR_EQUAL)
          return TRUE;
        if (condition == ECondition.BELOW)
          return FALSE;
        return UNDECIDED;
      }
      if (index1 == 0) {
        if (condition == ECondition.BELOW_OR_EQUAL)
          return TRUE;
        if (condition == ECondition.ABOVE)
          return FALSE;
        return UNDECIDED;
      }
    }

    else if (indirection2 == EIndirection.CONSTANT) {
      if (index2 == Integer.MIN_VALUE) {
        if (condition == ECondition.GREATER_OR_EQUAL)
          return TRUE;
        if (condition == ECondition.LESS)
          return FALSE;
        return UNDECIDED;
      }
      if (index2 == Integer.MAX_VALUE) {
        if (condition == ECondition.LESS_OR_EQUAL)
          return TRUE;
        if (condition == ECondition.GREATER)
          return FALSE;
        return UNDECIDED;
      }
      if (index2 == 0) {
        if (condition == ECondition.ABOVE_OR_EQUAL)
          return TRUE;
        if (condition == ECondition.BELOW)
          return FALSE;
        return UNDECIDED;
      }
      if (index2 == 0xffffffff) {
        if (condition == ECondition.BELOW_OR_EQUAL)
          return TRUE;
        if (condition == ECondition.ABOVE)
          return FALSE;
        return UNDECIDED;
      }
    }

    return UNDECIDED;
  }
}
