/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-30
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.math.matrix.LUDecomposition.java
 * Last modification: 2007-12-30
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

package org.sfc.math.matrix;

import java.io.Serializable;

/**
 * The LU decomposition here is inspired by the Jama and the Jamaica
 * project. For an m-by-n matrix A with m >= n, the LU decomposition is an
 * m-by-n unit lower triangular matrix L, an n-by-n upper triangular matrix
 * U, and a permutation vector pivot of length m so that A(piv,:) = L*U. If
 * m < n, then L is m-by-m and U is m-by-n. Like in the Jama package, we
 * use a "left-looking", dot-product, Crout/Doolittle algorithm.
 */
public final class LUDecomposition implements Cloneable, Serializable {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /** the l/u data matrix */
  private double m_data[];

  /** the height of the matrix */
  private int m_m;

  /** the width of the matrix */
  private int m_n;

  /** the pivot list */
  private int m_pivot[];

  /** the determinante of the matrix */
  private double m_det;

  /**
   * this constructor creates the l-u decomposition of a matrix.
   * 
   * @param m
   *          height of the matrix
   * @param n
   *          width of the matrix
   * @param mat
   *          data of the matrix
   */
  public LUDecomposition(int m, int n, double mat[]) {
    super();

    int li, lj, lk, lp, la, sign, lm;
    double lswap[];
    double ls, lmax;

    this.m_data = new double[li = (m * n)];
    System.arraycopy(mat, 0, this.m_data, 0, li);
    this.m_pivot = new int[m];
    this.m_m = m;
    this.m_n = n;

    lm = m;
    this.m_det = 1.0;

    lswap = new double[lm];
    lm--;
    for (li = lm; li >= 0; li--)
      this.m_pivot[li] = li;
    sign = 1;

    for (lj = 0; lj < n; lj++) {
      lp = (lm * n) + lj;
      for (li = lm; li >= 0; li--) {
        lswap[li] = this.m_data[lp];
        lp -= n;
      }

      la = 0;
      for (li = 0; li <= lm; li++) {
        lk = (li < lj) ? li : lj;
        ls = lswap[li];
        lp = la + lk;
        la += n;

        for (--lk; lk >= 0; lk--) {
          ls -= lswap[lk] * this.m_data[--lp];
        }

        this.m_data[lp + lj] = ls;
        lswap[li] = ls;
      }

      lp = lj;
      lmax = Math.abs(lswap[lj]);
      for (li = (lj + 1); li <= lm; li++) {
        if ((ls = Math.abs(lswap[li])) > lmax) {
          lp = li;
          lmax = ls;
        }
      }

      if (lp != lj) {
        la = lp * n;
        li = lj * n;
        for (lk = (n - 1); lk >= 0; lk--) {
          ls = this.m_data[la];
          this.m_data[la++] = this.m_data[li];
          this.m_data[li++] = ls;
        }

        li = this.m_pivot[lp];
        this.m_pivot[lp] = this.m_pivot[lj];
        this.m_pivot[lj] = li;

        sign = -sign;
      }

      if (lj <= lm) {
        if ((ls = this.m_data[la = ((lj * n) + lj)]) != 0.0) {
          la += n;
          for (li = (lj + 1); li <= lm; li++) {
            this.m_data[la] /= ls;
            la += n;
          }
          this.m_det = this.m_det * ls;
        } else
          this.m_det = 0.0;
      }
    }

    this.m_det *= sign;
  }

  /**
   * internal constructor for cloning
   * 
   * @param data
   *          the original's this.m_data member
   * @param m
   *          the original's this.m_m member
   * @param n
   *          the original's this.m_n member
   * @param pivot
   *          the original's this.m_pivot member
   * @param det
   *          the original's this.m_det member
   */
  private LUDecomposition(double data[], int m, int n, int pivot[],
      double det) {
    super();

    this.m_data = new double[data.length];
    System.arraycopy(data, 0, this.m_data, 0, data.length);
    this.m_m = m;
    this.m_n = n;
    this.m_pivot = new int[pivot.length];
    System.arraycopy(pivot, 0, this.m_pivot, 0, pivot.length);
    this.m_det = det;
  }

  /**
   * returns a copy of this decomposition
   * 
   * @return a copy of this LUDecomposition
   */
  public LUDecomposition copy() {
    return new LUDecomposition(this.m_data, this.m_m, this.m_n,
        this.m_pivot, this.m_det);
  }

  /**
   * implementation of Cloneable's clone() method
   * 
   * @return a copy of this LUDecomposition
   */
  @Override
  public Object clone() {
    return copy();
  }

  /**
   * returns the determinante of the matrix given in the constructor
   * 
   * @return det[A]
   */
  public double det() {
    /*
     * double ls; int lj, lk, lz;
     */

    if (this.m_n != this.m_m)
      throw new ArithmeticException();

    return this.m_det;
  }

  /**
   * returns wether the matrix is singular or not
   * 
   * @return true if singular, false otherwise
   */
  public boolean is_singular() {
    int lj, lk, lz;

    if (this.m_m == this.m_n)
      return det() == 0.0;

    lk = 0;
    lz = this.m_n + 1;

    for (lz = ((lj = this.m_n) + 1); lj > 0; lj--) {
      if (this.m_data[lk] == 0.0)
        return true;
      lk += lz;
    }

    return false;
  }

  /**
   * solves A * X = B and where the initial matrix is A, mat is B and the
   * result is to be put into result
   * 
   * @param m
   *          height of matrix B
   * @param n
   *          width of matrix B
   * @param mat
   *          the matrix B
   * @param result
   *          matrix X
   */
  public void solve(int m, int n, double mat[], double result[]) {
    int li, lj, lk, la, lb, lc, ld, le;
    double ls;

    if (m != this.m_m)
      throw new ArithmeticException();

    li = m - 1;
    la = li * n;
    for (li = (m - 1); li >= 0; li--) {
      lk = (this.m_pivot[li]) * n;
      System.arraycopy(mat, lk, result, la, n);
      la -= n;
    }

    la = -1;
    lc = 0;
    le = 0;
    for (lk = 0; lk < this.m_n; lk++) {
      la += this.m_n + 1;
      lb = la;
      li = (lk + 1);
      le = li * n;

      for (; li < this.m_n; li++) {
        ls = this.m_data[lb];
        lb += this.m_n;
        ld = le;

        for (lj = 0; lj < n; lj++) {
          result[ld++] -= result[lc + lj] * ls;
        }

        le += n;
      }
      lc += n;
    }

    la = (this.m_n * this.m_n) - 1;
    lb = this.m_n + 1;
    lc = this.m_n * n;
    for (lk = (this.m_n - 1); lk >= 0; lk--) {
      ls = this.m_data[la];
      la -= lb;

      for (lj = n; lj > 0; lj--) {
        result[--lc] /= ls;
      }

      ld = lc;
      le = ((lk - 1) * this.m_n) + lk;
      for (li = lk; li > 0; li--) {
        ls = this.m_data[le];
        for (lj = n - 1; lj >= 0; lj--) {
          result[--ld] -= result[lc + lj] * ls;
        }
        le -= this.m_n;
      }
    }
  }

  /**
   * inverts the initial matrix, the result is to be put into result
   * 
   * @param result
   *          matrix^-1
   */
  public void invert(double result[]) {
    int li, lj, lk, la, lb, lc, ld, le;
    double ls;

    lb = this.m_m;
    la = lb * lb;
    lb--;
    for (li = lb; li >= 0; li--) {
      lk = this.m_pivot[li];
      for (lj = lb; lj >= 0; lj--) {
        if (lj == lk)
          result[--la] = 1.0;
        else
          result[--la] = 0.0;
      }
    }

    lc = 0;
    for (lk = 0; lk < this.m_m; lk++) {
      ld = lc + this.m_m;
      for (li = (lk + 1); li < this.m_m; li++) {
        la = lc;
        ls = this.m_data[ld + lk];
        for (lj = 0; lj < this.m_m; lj++) {
          result[ld++] -= result[la++] * ls;
        }
      }
      lc = la;
    }

    lb = this.m_m;
    lc = lb * lb;
    la = lc - 1;
    lb++;
    for (lk = (this.m_m - 1); lk >= 0; lk--) {
      ls = this.m_data[la];
      la -= lb;
      for (lj = (this.m_m - 1); lj >= 0; lj--) {
        result[--lc] /= ls;
      }

      ld = lc;
      le = ((lk - 1) * this.m_n) + lk;
      for (li = lk; li > 0; li--) {
        ls = this.m_data[le];
        for (lj = (this.m_m - 1); lj >= 0; lj--) {
          result[--ld] -= result[lc + lj] * ls;
        }
        le -= this.m_n;
      }
    }
  }

  /**
   * returns the pivot permutation vector used in the decomposition
   * 
   * @return the pivot permutation vector used in the decomposition
   */
  public int[] get_pivot() {
    int[] lret = new int[this.m_pivot.length];
    System.arraycopy(this.m_pivot, 0, lret, 0, lret.length);
    return lret;
  }

  /**
   * returns the pivot permutation vector used in the decomposition as
   * matrix
   * 
   * @return the pivot permutation vector used in the decomposition as
   *         matrix
   */
  public Matrix get_pivot_vector() {
    double ld[];
    int li;

    li = this.m_pivot.length;
    ld = new double[li];
    for (--li; li >= 0; li--)
      ld[li] = this.m_pivot[li];

    return new Matrix(this.m_pivot.length, 1, ld);
  }

  /**
   * returns the lower triangular vector L
   * 
   * @return the lower triangular vector L
   */
  public Matrix get_l() {
    double[] ltemp;
    int li, lj;

    ltemp = new double[this.m_m * this.m_n];

    li = this.m_m - 1;
    lj = li * this.m_n;
    for (; li >= 0; li--) {
      System.arraycopy(this.m_data, lj, ltemp, lj, li);
      ltemp[lj + li] = 1.0;
      lj -= this.m_n;
    }

    return new Matrix(this.m_m, this.m_n, ltemp);
  }

  /**
   * returns the upper triangular vector U
   * 
   * @return the lower triangular vector U
   */
  public Matrix get_u() {
    double[] ltemp;
    int li, lj, lk;

    lj = this.m_n * this.m_n;
    lk = 1;
    ltemp = new double[this.m_n * this.m_n];

    for (li = (this.m_n - 1); li >= 0; li--) {
      System.arraycopy(this.m_data, --lj, ltemp, lj, lk++);
      lj -= this.m_n;
    }

    return new Matrix(this.m_m, this.m_n, ltemp);
  }

}
