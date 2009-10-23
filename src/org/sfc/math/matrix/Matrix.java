/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-30
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.math.matrix.Matrix.java
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
import java.text.NumberFormat;

import org.sfc.math.Mathematics;
import org.sfc.text.TextUtils;

/**
 * This is the main class for matrix operations. It contains a highly
 * optimized matrix class that is capable of all the common matrix
 * operations, arithmetics and such and such. Often special techniques are
 * applied to boost the speed, like Strassen's and Winograd's algorithms
 * for multiplication, or Legendre's power algorithm, LaPlace's determinant
 * calculation and so on. Some mathematic operations like exp, sin or ln
 * which normally don't match with matrices but are sometimes used in
 * statistics like the Kalman-Filter are made available due the usage of
 * Taylor's Series. Also, we have here some routines that make this packet
 * especially usefull for statistics: you can append, insert and remove
 * columns and rows or concatenate matrices together in a fast way.
 * Intelligent memory mangement is used, invoking allocations as few as
 * possible.
 */
public final class Matrix implements Cloneable, Serializable {
  /**
   * the serial version uid
   */
  private static final long serialVersionUID = 1;

  /** the data of the matrix */
  private double m_data[];

  /** the height of the matrix */
  private int m_m;

  /** the width of the matrix */
  private int m_n;

  /** string delimiters for matrix reading/printing */
  private static final char[] DELIMITERS = { '{', '}', ',', ',', ' ' };

  /**
   * Creates an empty m x n matrix (filled with 0).
   * 
   * @param m
   *          height of the matrix
   * @param n
   *          width of the matrix
   */
  public Matrix(final int m, final int n) {
    super();
    if ((m <= 0) || (n <= 0))
      throw new IllegalArgumentException();
    this.m_data = new double[m * n];
    this.m_m = m;
    this.m_n = n;
  }

  /**
   * Creates an empty m x n matrix (filled with 0), but already allocates
   * memory for more rows and columns. You can make no assumptions if so
   * allocated additional memory is really used for rows or columns like
   * proposed. It is possible that you allocate memory for 10 additional
   * rows, but then add columns consuming that memory. This constructor
   * thus is especially usefull when handling growing matrices. If you want
   * to use circular appending, this constructor should be your choice.
   * 
   * @param m
   *          initial height of the matrix, can be zero
   * @param n
   *          initial width of the matrix
   * @param max_rows
   *          count of rows to allocate memory for
   * @param maxCols
   *          count of columns to allocate memory for
   */
  public Matrix(final int m, final int n, final int max_rows,
      final int maxCols) {
    super();
    int mr, mc;

    if ((m < 0) || (n <= 0))
      throw new IllegalArgumentException();

    this.m_m = m;
    this.m_n = n;

    if (max_rows < m)
      mr = m;
    else
      mr = max_rows;

    if (maxCols < n)
      mc = n;
    else
      mc = maxCols;

    this.m_data = new double[mr * mc];
  }

  /**
   * Creates a m x n matrix filled with fill.
   * 
   * @param m
   *          height of the matrix
   * @param n
   *          width of the matrix
   * @param fill
   *          value to fill the matrix with
   */
  public Matrix(int m, int n, double fill) {
    super();

    int li;

    if ((m <= 0) || (n <= 0))
      throw new IllegalArgumentException();

    this.m_data = new double[li = (n * m)];
    this.m_m = m;
    this.m_n = n;

    if (fill != 0.0) {
      for (--li; li >= 0; li--)
        this.m_data[li] = fill;
    }
  }

  /**
   * Creates a m x n matrix that uses data as data.
   * 
   * @param m
   *          height of the matrix
   * @param n
   *          width of the matrix
   * @param data
   *          array of values of the matrix, this vector is not copied but
   *          directly used!!
   */
  public Matrix(int m, int n, double data[]) {
    super();

    if ((m <= 0) || (n <= 0))
      throw new IllegalArgumentException();

    this.m_data = data;
    this.m_m = m;
    this.m_n = n;
  }

  /**
   * Creates a m x n matrix filled with fill.
   * 
   * @param m
   *          height of the matrix
   * @param n
   *          width of the matrix
   * @param fill
   *          array of values to fill into the matrix
   * @param start
   *          the start index from where to read data from fill
   */
  public Matrix(int m, int n, double fill[], int start) {
    super();

    int li;

    if ((m <= 0) || (n <= 0))
      throw new IllegalArgumentException();

    this.m_data = new double[li = (n * m)];
    this.m_m = m;
    this.m_n = n;

    System.arraycopy(fill, start, this.m_data, 0, li);
  }

  /**
   * creates a new matrix where the dimension and the matrix data is taken
   * from a two dimensional array
   * 
   * @param d
   *          data to initialize the new matrix with
   */
  public Matrix(double[][] d) {
    this(d.length, d[0].length);

    int li, lj;

    li = this.m_m - 1;
    lj = this.m_n * li;
    for (; li >= 0; li--) {
      System.arraycopy(d[li], 0, this.m_data, lj, this.m_n);
      lj -= this.m_n;
    }
  }

  /**
   * Creates a matrix by parsing a two dimensional array of String.
   * 
   * @param strings
   *          the two dimensional array of String
   * @param format
   *          the format to use for printing, if it is null, the standard
   *          Double.parseDouble is used. normally, you'd use
   *          NumberFormat.getNumberInstance()
   */
  public Matrix(String[][] strings, NumberFormat format) {
    super();

    this.m_m = 0;
    this.m_n = 0;
    this.m_data = null;

    fromStrings(strings, format);
  }

  /**
   * Creates a matrix by parsing a String.
   * 
   * @param string
   *          the String containing the matrix data
   * @param format
   *          the format to use for printing, if it is null, the standard
   *          Double.parseDouble is used. normally, you'd use
   *          NumberFormat.getNumberInstance()
   * @param delimiters
   *          string of the delimiter characters to use if this string is
   *          null, the default delimiters are used: '{' for each opening
   *          row and the matrix itself '}' for each closing row and the
   *          matrix itself ',' between each element ',' between each row ' '
   *          after each ','
   */
  public Matrix(String string, NumberFormat format, String delimiters) {
    super();

    this.m_m = 0;
    this.m_n = 0;
    this.m_data = null;

    fromString(string, format, delimiters);
  }

  /**
   * returns an identity matrix of dimension x dimension-dimension :-)
   * 
   * @param dimension
   *          the dimension of the new square identity matrix
   * @return I(dimension)
   */
  public static Matrix identity(int dimension) {
    int lj, d;
    Matrix lm;

    lm = new Matrix(dimension, dimension);

    lj = (dimension * dimension) - 1;
    d = (dimension + 1);

    for (; lj >= 0; lj -= d)
      lm.m_data[lj] = 1.0;

    return lm;
  }

  /**
   * returns the height of the matrix
   * 
   * @return the height of the matrix
   */
  public int getHeight() {
    return this.m_m;
  }

  /**
   * returns the width of the matrix
   * 
   * @return the width of the matrix
   */
  public int getWidth() {
    return this.m_n;
  }

  /**
   * returns the item in cell i j
   * 
   * @param i
   *          row index of the cell zero-base (y)
   * @param j
   *          col index of the cell zero-base (x)
   * @return value in cell i j
   */
  public double get(int i, int j) {
    return this.m_data[(i * this.m_n) + j];
  }

  /**
   * sets the value of the item in cell i j
   * 
   * @param i
   *          row index of the cell zero-base (y)
   * @param j
   *          col index of the cell zero-base (x)
   * @param value
   *          the value to set
   * @return the new value in cell i j (value)
   */
  public double set(int i, int j, double value) {
    return this.m_data[(i * this.m_n) + j] = value;
  }

  /**
   * adds a value to the item in cell i j
   * 
   * @param i
   *          row index of the cell zero-base (y)
   * @param j
   *          col index of the cell zero-base (x)
   * @param value
   *          the value to add to cell i j
   * @return the new value in cell i j
   */
  public double setAdd(int i, int j, double value) {
    return this.m_data[(i * this.m_n) + j] += value;
  }

  /**
   * subtracts a value to the item in cell i j
   * 
   * @param i
   *          row index of the cell zero-base (y)
   * @param j
   *          col index of the cell zero-base (x)
   * @param value
   *          the value to sub from cell i j
   * @return the new value in cell i j
   */
  public double setSub(int i, int j, double value) {
    return this.m_data[(i * this.m_n) + j] -= value;
  }

  /**
   * multiplies a value to the item in cell i j
   * 
   * @param i
   *          row index of the cell zero-base (y)
   * @param j
   *          col index of the cell zero-base (x)
   * @param value
   *          the value to multiply with cell i j
   * @return the new value in cell i j
   */
  public double setMul(int i, int j, double value) {
    return this.m_data[(i * this.m_n) + j] *= value;
  }

  /**
   * divides the item in cell i j by a value
   * 
   * @param i
   *          row index of the cell zero-base (y)
   * @param j
   *          col index of the cell zero-base (x)
   * @param value
   *          the value to divide cell i j by
   * @return the new value in cell i j
   */
  public double setDiv(int i, int j, double value) {
    return this.m_data[(i * this.m_n) + j] /= value;
  }

  /**
   * clears this matrix, sets all cells to zero
   */
  public void clear() {
    for (int li = (this.m_data.length - 1); li >= 0; li--) {
      this.m_data[li] = 0;
    }
  }

  /**
   * swaps two rows
   * 
   * @param row1
   *          the index of the first row, zero-based (y)
   * @param row2
   *          the index of the second row, zero-based (y)
   * @return the matrix itself
   */
  public Matrix swapRows(int row1, int row2) {

    double lswap;
    int lidx, r1, r2;

    lidx = this.m_n;
    r1 = (row1 * lidx);
    r2 = (row2 * lidx);
    lidx--;

    for (; lidx >= 0; lidx--, r1++, r2++) {
      lswap = this.m_data[r1];
      this.m_data[r1] = this.m_data[r2];
      this.m_data[r2] = lswap;
    }

    return this;
  }

  /**
   * swaps two columns
   * 
   * @param col1
   *          the index of the first col, zero-based (x)
   * @param col2
   *          the index of the second col, zero-based (x)
   * @return the matrix itself
   */
  public Matrix swapCols(int col1, int col2) {

    double lswap;
    int lidx, c1, c2;

    c1 = col1;
    c2 = col2;
    for (lidx = (this.m_n - 1); lidx >= 0; lidx--, c1 += this.m_n, c2 += this.m_n) {
      lswap = this.m_data[c1];
      this.m_data[c1] = this.m_data[c2];
      this.m_data[c2] = lswap;
    }

    return this;
  }

  /**
   * copies the matrix
   * 
   * @return a perfect copy of this matrix
   */
  public Matrix copy() {
    Matrix lm;

    lm = new Matrix(this.m_m, this.m_n);
    System.arraycopy(this.m_data, 0, lm.m_data, 0, lm.m_data.length);

    return lm;
  }

  /**
   * the implementation of Cloneable.clone()
   * 
   * @return returns a copy of this matrix
   */
  @Override
  public Object clone() {
    return copy();
  }

  /**
   * transposes the matrix inplace if possible, re-uses the matrix' memory,
   * otherwise allocates a new piece of memory
   * 
   * @return this matrix, transposed (data overwritten)
   */
  public Matrix transpose_inplace() {
    double[] ldata;
    double ls;
    int li, lj, la1, lb1, lb2;

    la1 = this.m_m * this.m_n;

    if (this.m_m == this.m_n) {
      la1 -= 2;
      lb1 = (this.m_m - 2) * this.m_m + this.m_m - 1;

      for (li = (this.m_m - 1); li > 0; li--) {
        lb2 = lb1;

        for (lj = (li - 1); lj >= 0; lj--) {
          ls = this.m_data[la1];
          this.m_data[la1] = this.m_data[lb2];
          this.m_data[lb2] = ls;
          lb2 -= this.m_m;
          la1--;
        }

        lb1 -= this.m_m + 1;
        la1 -= (this.m_m - li + 1);
      }
    } else {
      ldata = new double[la1];

      li = this.m_m;
      this.m_m = this.m_n;
      this.m_n = li;

      lb1 = la1;
      la1--;

      for (--li; li >= 0; li--) {
        lb2 = --lb1;
        for (lj = (this.m_m - 1); lj >= 0; lj--, la1--, lb2 -= this.m_n) {
          ldata[lb2] = this.m_data[la1];
        }
      }

      this.m_data = ldata;
    }

    return this;
  }

  /**
   * creates a new matrix which is the transposed matrix of this one
   * 
   * @return a new matrix, this one, but transposed
   */
  public Matrix transpose() {
    Matrix lm;
    int li, lj, la1, lb1, lb2;

    lm = new Matrix(this.m_n, this.m_m);
    la1 = this.m_m * this.m_n;// this.m_data.length;
    li = this.m_m;
    lb1 = la1;
    la1--;

    for (--li; li >= 0; li--) {
      lb2 = --lb1;
      for (lj = (this.m_n - 1); lj >= 0; lj--, la1--, lb2 -= this.m_m) {
        lm.m_data[lb2] = this.m_data[la1];
      }
    }

    return lm;
  }

  /**
   * returns the minimum value of the matrix
   * 
   * @return the minimum cell value of this matrix
   */
  public double min() {
    double lmin;
    int li;

    lmin = Double.MAX_VALUE;
    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--)
      if (this.m_data[li] < lmin)
        lmin = this.m_data[li];

    return lmin;
  }

  /**
   * returns the maximum value of the matrix
   * 
   * @return the maximum cell value of this matrix
   */
  public double max() {
    double lmax;
    int li;

    lmax = -Double.MAX_VALUE;
    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--)
      if (this.m_data[li] > lmax)
        lmax = this.m_data[li];

    return lmax;
  }

  /**
   * returns the minimum value of on specific column of this matrix
   * 
   * @param col
   *          the column, which's minimum value shall be found
   * @return the minimum cell value of on specific column of this matrix
   */
  public double minCol(int col) {
    double lmin;
    int li, lk;

    lk = (this.m_m * this.m_n);
    lmin = Double.MAX_VALUE;
    for (li = col; li < lk; li += this.m_n)
      if (this.m_data[li] < lmin)
        lmin = this.m_data[li];

    return lmin;
  }

  /**
   * returns the maximum value of on specific column of this matrix
   * 
   * @param col
   *          the column, which's maximum value shall be found
   * @return the maximum cell value of on specific column of this matrix
   */
  public double maxCol(int col) {
    double lmax;
    int li, lk;

    lk = (this.m_m * this.m_n);
    lmax = -Double.MAX_VALUE;
    for (li = col; li < lk; li += this.m_n)
      if (this.m_data[li] > lmax)
        lmax = this.m_data[li];

    return lmax;
  }

  /**
   * returns the minimum value of on specific row of this matrix
   * 
   * @param row
   *          the row, which's minimum value shall be found
   * @return the minimum cell value of on specific row of this matrix
   */
  public double minRow(int row) {
    double lmin;
    int li, lk;

    lmin = Double.MAX_VALUE;
    li = row * this.m_n;
    lk = li + this.m_n;
    for (; li < lk; li++)
      if (this.m_data[li] < lmin)
        lmin = this.m_data[li];

    return lmin;
  }

  /**
   * returns the maximum value of on specific row of this matrix
   * 
   * @param row
   *          the row, which's maximum value shall be found
   * @return the maximum cell value of on specific row of this matrix
   */
  public double maxRow(int row) {
    double lmax;
    int li, lk;

    lmax = -Double.MAX_VALUE;
    li = row * this.m_n;
    lk = li + this.m_n;
    for (; li < lk; li++)
      if (this.m_data[li] < lmax)
        lmax = this.m_data[li];

    return lmax;
  }

  /**
   * this methods flips the matrix vertically
   * 
   * @return a new matrix with the data of this matrix but flipped
   */
  public Matrix flip() {
    int li, lj, la, lb;
    Matrix lm;

    lm = new Matrix(this.m_m, this.m_n);
    la = (this.m_m * this.m_n) - 1;
    lb = 0;

    for (li = this.m_m; li > 0; li--) {
      for (lj = (this.m_n - 1); lj >= 0; lj--) {
        lm.m_data[lb + lj] = this.m_data[la--];
      }
      lb += this.m_n;
    }

    return lm;
  }

  /**
   * this methods flips the matrix vertically inplace
   * 
   * @return this matrix but flipped (data overwritten)
   */
  public Matrix flipInplace() {
    double lswap;
    int lidx, lrow, lr_1, lr_2;

    for (lrow = (this.m_m / 2); lrow > 0; lrow--) {
      lidx = this.m_n;
      lr_1 = (lrow - 1) * lidx;
      lr_2 = (this.m_m - lrow) * lidx--;

      for (; lidx >= 0; lidx--, lr_1++, lr_2++) {
        lswap = this.m_data[lr_1];
        this.m_data[lr_1] = this.m_data[lr_2];
        this.m_data[lr_2] = lswap;
      }
    }

    return this;
  }

  /**
   * this methods flips the matrix horizontally
   * 
   * @return a new matrix with the data of this matrix but mirrored
   */
  public Matrix mirror() {
    int li, lj, la, lb;
    Matrix lm;

    lm = new Matrix(this.m_m, this.m_n);
    lb = (this.m_m * this.m_n);
    la = lb - 1;

    for (li = this.m_m; li > 0; li--) {
      for (lj = this.m_n; lj > 0; lj--) {
        lm.m_data[lb - lj] = this.m_data[la--];
      }
      lb -= this.m_n;
    }

    return lm;
  }

  /**
   * this methods flips the matrix horizontally inplace (mirror)
   * 
   * @return this matrix but mirrored (data overwritten)
   */
  public Matrix mirrorInplace() {
    double lswap;
    int lidx, lcol, lc_1, lc_2;

    for (lcol = (this.m_n / 2); lcol > 0; lcol--) {
      lc_1 = lcol - 1;
      lc_2 = this.m_n - lcol;

      for (lidx = (this.m_n - 1); lidx >= 0; lidx--, lc_1 += this.m_n, lc_2 += this.m_n) {
        lswap = this.m_data[lc_1];
        this.m_data[lc_1] = this.m_data[lc_2];
        this.m_data[lc_2] = lswap;
      }
    }
    return this;
  }

  /**
   * negates this matrix and returns a new one with the result
   * 
   * @return a negated copy of this matrix
   */
  public Matrix negate() {
    Matrix lm;
    int li;

    lm = new Matrix(this.m_m, this.m_n);
    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--)
      lm.m_data[li] = -this.m_data[li];

    return lm;
  }

  /**
   * negates this matrix inplace
   * 
   * @return this matrix, but negated, data overwritten
   */
  public Matrix negateInplace() {
    int li;

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--)
      this.m_data[li] = -this.m_data[li];

    return this;
  }

  /**
   * adds matrix a to this matrix and returns a new matrix with the result
   * 
   * @param a
   *          the matrix to be added
   * @return new matrix = this + a
   */
  public Matrix add(Matrix a) {
    Matrix lm;
    int li;

    if ((a.m_m != this.m_m) || (a.m_n != this.m_n))
      throw new ArithmeticException();

    lm = new Matrix(this.m_m, this.m_n);

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--)
      lm.m_data[li] = this.m_data[li] + a.m_data[li];

    return lm;
  }

  /**
   * adds a scalar to each cell of this matrix and returns a new matrix
   * with the result
   * 
   * @param a
   *          the scalar to add
   * @return new matrix_i_j = this_i_j + a
   */
  public Matrix addScalar(double a) {
    Matrix lm;
    int li;

    lm = new Matrix(this.m_m, this.m_n);

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--)
      lm.m_data[li] = this.m_data[li] + a;

    return lm;
  }

  /**
   * adds matrix a to this matrix by overwriting this one
   * 
   * @param a
   *          the matrix to be added to this one
   * @return this = this + a
   */
  public Matrix addInplace(Matrix a) {
    int li;

    if ((a.m_m != this.m_m) || (a.m_n != this.m_n))
      throw new ArithmeticException();

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--)
      this.m_data[li] += a.m_data[li];

    return this;
  }

  /**
   * adds a scalar to each cell of this matrix by overwriting this matrix
   * 
   * @param a
   *          the scalar value to add to each cell of this matrix
   * @return this_i_j = this_i_j + a
   */
  public Matrix addScalarInplace(double a) {
    int li;

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--)
      this.m_data[li] += a;

    return this;
  }

  /**
   * subtracts matrix a from this matrix and returns a new matrix with the
   * result
   * 
   * @param a
   *          the matrix to be subtracted from this one
   * @return new matrix = this - a
   */
  public Matrix sub(Matrix a) {
    Matrix lm;
    int li;

    if ((a.m_m != this.m_m) || (a.m_n != this.m_n))
      throw new ArithmeticException();

    lm = new Matrix(this.m_m, this.m_n);

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--)
      lm.m_data[li] = this.m_data[li] - a.m_data[li];

    return lm;
  }

  /**
   * subtracts a scalar from each cell of this matrix and returns a new
   * matrix with the result
   * 
   * @param a
   *          the scalar value to subtract from each cell
   * @return new matrix_i_j = this_i_j - a
   */
  public Matrix subScalar(double a) {
    Matrix lm;
    int li;

    lm = new Matrix(this.m_m, this.m_n);

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--)
      lm.m_data[li] = this.m_data[li] - a;

    return lm;
  }

  /**
   * subtracts matrix a from this matrix by overwriting this one
   * 
   * @param a
   *          the matrix to be subtracted from this one
   * @return this = this - a
   */
  public Matrix subInplace(Matrix a) {
    int li;

    if ((a.m_m != this.m_m) || (a.m_n != this.m_n))
      throw new ArithmeticException();

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--)
      this.m_data[li] -= a.m_data[li];

    return this;
  }

  /**
   * subtracts a scalar from each cell of this matrix by overwriting this
   * matrix
   * 
   * @param a
   *          the scalar value to subtract from each cell of this matrix
   * @return this_i_j = this_i_j - a
   */
  public Matrix subScalarInplace(double a) {
    int li;

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--)
      this.m_data[li] -= a;

    return this;
  }

  /**
   * this one does the matrix multiplication of this matrix with matrix a
   * and returns a new matrix with the result
   * 
   * @param a
   *          the matrix this matrix shall be multiplied with
   * @return a new matrix containing the result
   */
  public Matrix mul(Matrix a) {
    Matrix lmat;

    if (this.m_n != a.m_m)
      throw new ArithmeticException();

    lmat = new Matrix(this.m_m, a.m_n);

    Multiplication.mul(this.m_n, this.m_m, a.m_n, this.m_data, a.m_data,
        lmat.m_data);

    return lmat;
  }

  /**
   * multiplicates a scalar with each cell of this matrix and returns a new
   * matrix with the result
   * 
   * @param a
   *          the scalar value to multiply with each cell
   * @return new matrix_i_j = this_i_j * a
   */
  public Matrix mulScalar(double a) {
    Matrix lm;
    int li;

    lm = new Matrix(this.m_m, this.m_n);

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--)
      lm.m_data[li] = this.m_data[li] * a;

    return lm;
  }

  /**
   * multiplicates a scalar from each cell of this matrix by overwriting
   * this matrix
   * 
   * @param a
   *          the scalar value to multipy with each cell of this matrix
   * @return this_i_j = this_i_j - a
   */
  public Matrix mulScalarInplace(double a) {
    int li;

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--)
      this.m_data[li] *= a;

    return this;
  }

  /**
   * multiplicates each element of this matrix with the same element in
   * matrix a and returns a new matrix containing the result
   * 
   * @param a
   *          the matrix to be multiplied elementwise with this one
   * @return new matrix_i_j = this_i_j * a_i_j
   */
  public Matrix mulElements(Matrix a) {
    Matrix lm;
    int li;

    if ((a.m_m != this.m_m) || (a.m_n != this.m_n))
      throw new ArithmeticException();

    lm = new Matrix(this.m_m, this.m_n);

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--)
      lm.m_data[li] = this.m_data[li] * a.m_data[li];

    return lm;
  }

  /**
   * multiplicates each element of this matrix with the same element in
   * matrix a by overwriting this matrix
   * 
   * @param a
   *          the matrix to be multiplied elementwise with this one
   * @return this_i_j = this_i_j * a_i_j
   */
  public Matrix mulElementsInplace(Matrix a) {
    int li;

    if ((a.m_m != this.m_m) || (a.m_n != this.m_n))
      throw new ArithmeticException();

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--)
      this.m_data[li] *= a.m_data[li];

    return this;
  }

  /**
   * calculates the n'th power of this matrix and returns the result in a
   * new one
   * 
   * @param power
   *          the matrix to be multiplied elementwise with this one
   * @return new matrix = this^power
   */
  public Matrix power(int power) {
    Matrix lm;

    if (this.m_m != this.m_n)
      throw new ArithmeticException();

    if (power == 0)
      return identity(this.m_m);

    lm = new Matrix(this.m_m, this.m_m);

    switch ((power < 0) ? (-power) : power) {
    case 1:
      System.arraycopy(this.m_data, 0, lm.m_data, 0, this.m_m * this.m_m);
      break;
    case 2:
      Multiplication.mul(this.m_m, this.m_m, this.m_m, this.m_data,
          this.m_data, lm.m_data);
      break;
    default:
      Multiplication.power(power, this.m_m, this.m_data, lm.m_data);
      break;
    }

    if (power < 0)
      Inverse.invert(this.m_data, this.m_m, lm.m_data);
    // new LUDecomposition(this.m_m, this.m_m,
    // lm.m_data).invert(lm.m_data);

    return lm;
  }

  /**
   * calculates the a'th power of each cell of this matrix and returns a
   * new matrix with the result
   * 
   * @param a
   *          the scalar value for the exponentwith each cell
   * @return new matrix_i_j = this_i_j ^ a
   */
  public Matrix powerElementsScalar(double a) {
    Matrix lm;
    int li;

    lm = new Matrix(this.m_m, this.m_n);

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      lm.m_data[li] = Math.pow(this.m_data[li], a);
    }

    return lm;
  }

  /**
   * calculates the a'th power of each cell of this matrix and overwrites
   * this matrix
   * 
   * @param a
   *          the scalar value for the exponent with each cell
   * @return this_i_j = this_i_j ^ a
   */
  public Matrix powerElementsScalarInplace(double a) {
    int li;

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      this.m_data[li] = Math.pow(this.m_data[li], a);
    }

    return this;
  }

  /**
   * raises each element of this matrix to the power of the same element in
   * matrix a and returns a new matrix containing the result
   * 
   * @param a
   *          the matrix to use to calculate the powers elementwise of this
   *          one
   * @return new matrix_i_j = this_i_j * a_i_j
   */
  public Matrix powerElements(Matrix a) {
    Matrix lm;
    int li;

    if ((a.m_m != this.m_m) || (a.m_n != this.m_n))
      throw new ArithmeticException();

    lm = new Matrix(this.m_m, this.m_n);

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      lm.m_data[li] = Math.pow(this.m_data[li], a.m_data[li]);
    }

    return lm;
  }

  /**
   * raises each element of this matrix to the power of the same element in
   * matrix a by overwriting this matrix
   * 
   * @param a
   *          the matrix to use to calculate thoe powers elementwise of
   *          this one
   * @return this_i_j = this_i_j * a_i_j
   */
  public Matrix powerElementsInplace(Matrix a) {
    int li;
    // double ls, lt, lu;

    if ((a.m_m != this.m_m) || (a.m_n != this.m_n))
      throw new ArithmeticException();

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      this.m_data[li] = Math.pow(this.m_data[li], a.m_data[li]);
    }

    return this;
  }

  /**
   * returns the L-U decomposition of this matrix in an object of the class
   * LUDecomposition.
   * 
   * @return L-U decomposition of this matrix.
   */
  public LUDecomposition luDecompose() {
    return new LUDecomposition(this.m_m, this.m_n, this.m_data);
  }

  /**
   * returns the Q-R decomposition of this matrix in an object of the class
   * QRDecomposition.
   * 
   * @return Q-R decomposition of this matrix.
   */
  public QRDecomposition qrDecompose() {
    return new QRDecomposition(this.m_m, this.m_n, this.m_data);
  }

  /**
   * returns the Eigenvalue-decomposition of this matrix in an object of
   * the class EigenvalueDecomposition.
   * 
   * @return Eigenvalue-decomposition of this matrix.
   */
  public EigenvalueDecomposition eigenvalueDecompose() {
    if (this.m_n != this.m_m)
      throw new ArithmeticException();

    return new EigenvalueDecomposition(this.m_data, this.m_m);
  }

  /**
   * returns the singualr value decomposition of this matrix in an object
   * of the class SingularValueDecomposition.
   * 
   * @param want_u
   *          true if you want the matrix u to be calculated, false
   *          otherwise (then, you cannot use u, of course, but it will be
   *          faster)
   * @param want_v
   *          true if you want the matrix v to be calculated, false
   *          otherwise (then, you cannot use v, of course, but it will be
   *          faster)
   * @return singular value decomposition of this matrix.
   */
  public SingularValueDecomposition singularValueDecompose(boolean want_u,
      boolean want_v) {
    return new SingularValueDecomposition(this.m_m, this.m_n, this.m_data,
        want_u, want_v);
  }

  /**
   * returns the Cholesky-decomposition of this matrix in an object of the
   * class CholeskyDecomposition.
   * 
   * @return Cholesky-decomposition of this matrix.
   */
  public CholeskyDecomposition choleskyDecompose() {
    return new CholeskyDecomposition(this.m_data, this.m_m, this.m_n);
  }

  /**
   * returns wether the matrix is square, symmetric and positive definite
   * or not
   * 
   * @return true if A is square, symmetric and positive definite.
   */
  public boolean isSymmetricPositiveDefinite() {
    return new CholeskyDecomposition(this.m_data, this.m_m, this.m_n)
        .isSymmetricPositiveDefinite();
  }

  /**
   * solves A * X = B and returns a new matrix containing X (while this
   * matrix is A) if A is not square, the least-square error solution is
   * returned
   * 
   * @param mat
   *          the matrix B
   * @return Matrix containing the result X
   */
  public Matrix solve(Matrix mat) {
    Matrix lm;

    if (this.m_m == this.m_n) {
      lm = new Matrix(mat.m_m, mat.m_n);
      new LUDecomposition(this.m_m, this.m_n, this.m_data).solve(mat.m_m,
          mat.m_n, mat.m_data, lm.m_data);
    } else {
      lm = new Matrix(mat.m_n, mat.m_n);
      new QRDecomposition(this.m_m, this.m_n, this.m_data).solve(mat.m_m,
          mat.m_n, mat.m_data, lm.m_data);
    }
    return lm;
  }

  /**
   * inverts this matrix and returns a new one containing the result
   * 
   * @return new matrix = this^-1
   */
  public Matrix invert() {
    Matrix lm;

    if (this.m_m != this.m_n)
      throw new ArithmeticException();

    lm = new Matrix(this.m_m, this.m_m);
    Inverse.invert(this.m_data, this.m_m, lm.m_data);
    // new LUDecomposition(this.m_m, this.m_m,
    // this.m_data).invert(lm.m_data);

    return lm;
  }

  /**
   * invert this matrix in place by overwriting it
   * 
   * @return this = this^-1
   */
  public Matrix invertInplace() {
    if (this.m_m != this.m_n)
      throw new ArithmeticException();

    Inverse.invert(this.m_data, this.m_m, this.m_data);
    // new LUDecomposition(this.m_m, this.m_m,
    // this.m_data).invert(this.m_data);

    return this;
  }

  /**
   * returns the determinante of this matrix
   * 
   * @return det(A)
   */
  public double det() {
    if (this.m_m != this.m_n)
      throw new ArithmeticException();

    return Determinante.det(this.m_data, this.m_m);
  }

  /**
   * calculates e^matrix with the Taylor Series exp(A) = I + A + (1/2 *
   * A^2) + (1/6 * A^3) + .... + (1/x! * A^x)
   * 
   * @return new matrix = e^A
   */
  public Matrix exp() {
    Matrix lm;

    if (this.m_m != this.m_n)
      throw new ArithmeticException();

    lm = new Matrix(this.m_m, this.m_m);

    Multiplication.exp(this.m_m, this.m_data, lm.m_data);

    return lm;
  }

  /**
   * calculates e^matrix with the Taylor Series by overwriting this matrix
   * exp(A) = I + A + (1/2 * A^2) + (1/6 * A^3) + .... + (1/x! * A^x)
   * 
   * @return this = e^this
   */
  public Matrix expInplace() {
    if (this.m_m != this.m_n)
      throw new ArithmeticException();

    Multiplication.exp(this.m_m, this.m_data, this.m_data);

    return this;
  }

  /**
   * calculates e^each cell of this matrix and returns a new matrix with
   * the result
   * 
   * @return new matrix_i_j = e^this_i_j = exp(this_i_j)
   */
  public Matrix expElements() {
    Matrix lm;
    int li;

    lm = new Matrix(this.m_m, this.m_n);

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      lm.m_data[li] = Math.exp(this.m_data[li]);
    }

    return lm;
  }

  /**
   * calculates e^each cell of this matrix and overwrites this matrix with
   * the result
   * 
   * @return this_i_j = e^this_i_j = exp(this_i_j)
   */
  public Matrix expElementsInplace() {
    int li;

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      this.m_data[li] = Math.exp(this.m_data[li]);
    }

    return this;
  }

  /**
   * calculates ln matrix with the Taylor Series and returns a new one
   * containing the result ln(A) = 2[ (A-I)/(A+I) + (1/3)((A-I)/(A+I))^3) +
   * ... ++ (1/(2x+1)!)((A-I)/(A+I))^(2x+1)
   * 
   * @return new matrix = ln this
   */
  public Matrix ln() {
    Matrix lm;

    if (this.m_m != this.m_n)
      throw new ArithmeticException();

    lm = new Matrix(this.m_m, this.m_m);

    Multiplication.ln(this.m_m, this.m_data, lm.m_data);

    return lm;
  }

  /**
   * calculates ln matrix with the Taylor Series by overwriting this matrix
   * ln(A) = 2[ (A-I)/(A+I) + (1/3)((A-I)/(A+I))^3) + ... ++
   * (1/(2x+1)!)((A-I)/(A+I))^(2x+1)
   * 
   * @return this = ln this
   */
  public Matrix ln_inplace() {
    if (this.m_m != this.m_n)
      throw new ArithmeticException();

    Multiplication.ln(this.m_m, this.m_data, this.m_data);

    return this;
  }

  /**
   * calculates ln(each cell) of this matrix and returns a new matrix with
   * the result
   * 
   * @return new matrix_i_j = ln(this_i_j)
   */
  public Matrix lnElements() {
    Matrix lm;
    int li;

    lm = new Matrix(this.m_m, this.m_n);

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      lm.m_data[li] = Math.log(this.m_data[li]);
    }

    return lm;
  }

  /**
   * calculates ln(each cell) of this matrix and overwrites this matrix
   * with the result
   * 
   * @return this_i_j = ln this_i_j
   */
  public Matrix lnElementsInplace() {
    int li;

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      this.m_data[li] = Math.log(this.m_data[li]);
    }

    return this;
  }

  /**
   * calculates sin matrix with the Taylor Series and returns a new one
   * containing the result sin(A) = A - (A^3/3!) + (A^5/5!) - (A^7/7!) ...
   * 
   * @return new matrix = ln this
   */
  public Matrix sin() {
    Matrix lm;

    if (this.m_m != this.m_n)
      throw new ArithmeticException();

    lm = new Matrix(this.m_m, this.m_m);

    Multiplication.sin(this.m_m, this.m_data, lm.m_data);

    return lm;
  }

  /**
   * calculates sin matrix with the Taylor Series by overwriting this
   * matrix sin(A) = A - (A^3/3!) + (A^5/5!) - (A^7/7!) ...
   * 
   * @return this = ln this
   */
  public Matrix sinInplace() {
    if (this.m_m != this.m_n)
      throw new ArithmeticException();

    Multiplication.sin(this.m_m, this.m_data, this.m_data);

    return this;
  }

  /**
   * calculates sin(each cell) of this matrix and returns a new matrix with
   * the result
   * 
   * @return new matrix_i_j = sin(this_i_j)
   */
  public Matrix sinElements() {
    Matrix lm;
    int li;

    lm = new Matrix(this.m_m, this.m_n);

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      lm.m_data[li] = Math.sin(this.m_data[li]);
    }

    return lm;
  }

  /**
   * calculates sin(each cell) of this matrix and overwrites this matrix
   * with the result
   * 
   * @return this_i_j = sin this_i_j
   */
  public Matrix sinElementsInplace() {
    int li;

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      this.m_data[li] = Math.sin(this.m_data[li]);
    }

    return this;
  }

  /**
   * calculates cos matrix with the Taylor Series and returns a new one
   * containing the result cos(A) = I - (A^2/2!) + (A^4/4!) - (A^6/6!) ...
   * 
   * @return new matrix = ln this
   */
  public Matrix cos() {
    Matrix lm;

    if (this.m_m != this.m_n)
      throw new ArithmeticException();

    lm = new Matrix(this.m_m, this.m_m);

    Multiplication.cos(this.m_m, this.m_data, lm.m_data);

    return lm;
  }

  /**
   * calculates cos matrix with the Taylor Series by overwriting this
   * matrix cos(A) = I - (A^2/2!) + (A^4/4!) - (A^6/6!) ...
   * 
   * @return this = ln this
   */
  public Matrix cosInplace() {
    if (this.m_m != this.m_n)
      throw new ArithmeticException();

    Multiplication.cos(this.m_m, this.m_data, this.m_data);

    return this;
  }

  /**
   * calculates cos(each cell) of this matrix and returns a new matrix with
   * the result
   * 
   * @return new matrix_i_j = cos(this_i_j)
   */
  public Matrix cosElements() {
    Matrix lm;
    int li;

    lm = new Matrix(this.m_m, this.m_n);

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      lm.m_data[li] = Math.cos(this.m_data[li]);
    }

    return lm;
  }

  /**
   * calculates cos(each cell) of this matrix and overwrites this matrix
   * with the result
   * 
   * @return this_i_j = cos this_i_j
   */
  public Matrix cosElementsInplace() {
    int li;

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      this.m_data[li] = Math.cos(this.m_data[li]);
    }

    return this;
  }

  /**
   * calculates arcsin(each cell) of this matrix and returns a new matrix
   * with the result
   * 
   * @return new matrix_i_j = asin(this_i_j)
   */
  public Matrix arcsinElements() {
    Matrix lm;
    int li;

    lm = new Matrix(this.m_m, this.m_n);

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      lm.m_data[li] = Math.asin(this.m_data[li]);
    }

    return lm;
  }

  /**
   * calculates asin(each cell) of this matrix and overwrites this matrix
   * with the result
   * 
   * @return this_i_j = asin this_i_j
   */
  public Matrix arcsinElementsInplace() {
    int li;

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      this.m_data[li] = Math.asin(this.m_data[li]);
    }

    return this;
  }

  /**
   * calculates acos(each cell) of this matrix and returns a new matrix
   * with the result
   * 
   * @return new matrix_i_j = acos(this_i_j)
   */
  public Matrix arccosElements() {
    Matrix lm;
    int li;

    lm = new Matrix(this.m_m, this.m_n);

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      lm.m_data[li] = Math.acos(this.m_data[li]);
    }

    return lm;
  }

  /**
   * calculates arccos(each cell) of this matrix and overwrites this matrix
   * with the result
   * 
   * @return this_i_j = acos this_i_j
   */
  public Matrix arccosElementsInplace() {
    int li;

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      this.m_data[li] = Math.acos(this.m_data[li]);
    }

    return this;
  }

  /**
   * calculates tan(each cell) of this matrix and returns a new matrix with
   * the result
   * 
   * @return new matrix_i_j = tan(this_i_j)
   */
  public Matrix tanElements() {
    Matrix lm;
    int li;

    lm = new Matrix(this.m_m, this.m_n);

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      lm.m_data[li] = Math.tan(this.m_data[li]);
    }

    return lm;
  }

  /**
   * calculates tan(each cell) of this matrix and overwrites this matrix
   * with the result
   * 
   * @return this_i_j = tan this_i_j
   */
  public Matrix tan_Elements_inplace() {
    int li;

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      this.m_data[li] = Math.tan(this.m_data[li]);
    }

    return this;
  }

  /**
   * calculates atan(each cell) of this matrix and returns a new matrix
   * with the result
   * 
   * @return new matrix_i_j = atan(this_i_j)
   */
  public Matrix arctanElements() {
    Matrix lm;
    int li;

    lm = new Matrix(this.m_m, this.m_n);

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      lm.m_data[li] = Math.atan(this.m_data[li]);
    }

    return lm;
  }

  /**
   * calculates arctan(each cell) of this matrix and overwrites this matrix
   * with the result
   * 
   * @return this_i_j = arctan this_i_j
   */
  public Matrix arctanElementsInplace() {
    int li;

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      this.m_data[li] = Math.atan(this.m_data[li]);
    }
    return this;
  }

  /**
   * calculates sinh(each cell) of this matrix and returns a new matrix
   * with the result
   * 
   * @return new matrix_i_j = sin(this_i_j)
   */
  public Matrix sinhElements() {
    Matrix lm;
    int li;

    lm = new Matrix(this.m_m, this.m_n);

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      lm.m_data[li] = Math.sinh(this.m_data[li]);
    }

    return lm;
  }

  /**
   * calculates sinh(each cell) of this matrix and overwrites this matrix
   * with the result
   * 
   * @return this_i_j = sinh this_i_j
   */
  public Matrix sinhElementsInplace() {
    int li;

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      this.m_data[li] = Math.sinh(this.m_data[li]);
    }

    return this;
  }

  /**
   * calculates cosh(each cell) of this matrix and returns a new matrix
   * with the result
   * 
   * @return new matrix_i_j = cosh(this_i_j)
   */
  public Matrix coshElements() {
    Matrix lm;
    int li;

    lm = new Matrix(this.m_m, this.m_n);

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      lm.m_data[li] = Math.cosh(this.m_data[li]);
    }

    return lm;
  }

  /**
   * calculates cosh(each cell) of this matrix and overwrites this matrix
   * with the result
   * 
   * @return this_i_j = cosh this_i_j
   */
  public Matrix coshElementsInplace() {
    int li;

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      this.m_data[li] = Math.cosh(this.m_data[li]);
    }

    return this;
  }

  /**
   * calculates abs(each cell) of this matrix and returns a new matrix with
   * the result
   * 
   * @return new matrix_i_j = abs(this_i_j)
   */
  public Matrix absElements() {
    Matrix lm;
    int li;

    lm = new Matrix(this.m_m, this.m_n);

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      lm.m_data[li] = Math.abs(this.m_data[li]);
    }

    return lm;
  }

  /**
   * calculates abs(each cell) of this matrix and overwrites this matrix
   * with the result
   * 
   * @return this_i_j = abs this_i_j
   */
  public Matrix absElementsInplace() {
    int li;

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      this.m_data[li] = Math.abs(this.m_data[li]);
    }

    return this;
  }

  /**
   * calculates ceil(each cell) of this matrix and returns a new matrix
   * with the result
   * 
   * @return new matrix_i_j = ceil(this_i_j)
   */
  public Matrix ceilElements() {
    Matrix lm;
    int li;

    lm = new Matrix(this.m_m, this.m_n);

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      lm.m_data[li] = Math.ceil(this.m_data[li]);
    }

    return lm;
  }

  /**
   * calculates ceil(each cell) of this matrix and overwrites this matrix
   * with the result
   * 
   * @return this_i_j = ceil this_i_j
   */
  public Matrix ceilElementsInplace() {
    int li;

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      this.m_data[li] = Math.ceil(this.m_data[li]);
    }

    return this;
  }

  /**
   * calculates tanh(each cell) of this matrix and returns a new matrix
   * with the result
   * 
   * @return new matrix_i_j = tanh(this_i_j)
   */
  public Matrix tanhElements() {
    Matrix lm;
    int li;

    lm = new Matrix(this.m_m, this.m_n);

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      lm.m_data[li] = Math.tanh(this.m_data[li]);
    }

    return lm;
  }

  /**
   * calculates tanh(each cell) of this matrix and overwrites this matrix
   * with the result
   * 
   * @return this_i_j = tanh this_i_j
   */
  public Matrix tanhElementsInplace() {
    int li;

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      this.m_data[li] = Math.tanh(this.m_data[li]);
    }

    return this;
  }

  /**
   * calculates floor(each cell) of this matrix and returns a new matrix
   * with the result
   * 
   * @return new matrix_i_j = floor(this_i_j)
   */
  public Matrix floorElements() {
    Matrix lm;
    int li;

    lm = new Matrix(this.m_m, this.m_n);

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      lm.m_data[li] = Math.floor(this.m_data[li]);
    }

    return lm;
  }

  /**
   * calculates floor(each cell) of this matrix and overwrites this matrix
   * with the result
   * 
   * @return this_i_j = floor this_i_j
   */
  public Matrix floorElementsInplace() {
    int li;

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      this.m_data[li] = Math.floor(this.m_data[li]);
    }
    return this;
  }

  /**
   * calculates round(each cell) of this matrix and returns a new matrix
   * with the result
   * 
   * @return new matrix_i_j = round(this_i_j)
   */
  public Matrix roundElements() {
    Matrix lm;
    int li;

    lm = new Matrix(this.m_m, this.m_n);

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      lm.m_data[li] = Math.round(this.m_data[li]);
    }

    return lm;
  }

  /**
   * calculates round(each cell) of this matrix and overwrites this matrix
   * with the result
   * 
   * @return this_i_j = round this_i_j
   */
  public Matrix roundElementsInplace() {
    int li;

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      this.m_data[li] = Math.rint(this.m_data[li]);
    }
    return this;
  }

  /**
   * rounds each cell to decimals decimals a new matrix with the result
   * 
   * @param decimals
   *          the count of decimals to keep in each cell
   * @return new matrix_i_j = round(this_i_j, decimals)
   */
  public Matrix roundElements(int decimals) {
    Matrix lm;
    int li;

    lm = new Matrix(this.m_m, this.m_n);

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      lm.m_data[li] = Mathematics.round(this.m_data[li], decimals);
    }

    return lm;
  }

  /**
   * rounds each cell to decimals decimals and overwrites this matrix with
   * the result
   * 
   * @param decimals
   *          the count of decimals to keep in each cell
   * @return this_i_j = round(this_i_j, decimals)
   */
  public Matrix roundElementsInplace(int decimals) {
    int li;

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      this.m_data[li] = Mathematics.round(this.m_data[li], decimals);
    }
    return this;
  }

  /**
   * calculates signum(each cell) of this matrix and returns a new matrix
   * with the result
   * 
   * @return new matrix_i_j = signum(this_i_j)
   */
  public Matrix signumElements() {
    Matrix lm;
    int li;

    lm = new Matrix(this.m_m, this.m_n);

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      lm.m_data[li] = Math.signum(this.m_data[li]);
    }

    return lm;
  }

  /**
   * calculates signum(each cell) of this matrix and overwrites this matrix
   * with the result
   * 
   * @return this_i_j = signum this_i_j
   */
  public Matrix signumElementsInplace() {
    int li;

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      this.m_data[li] = Math.signum(this.m_data[li]);
    }
    return this;
  }

  /**
   * calculates sqrt(each cell) of this matrix and returns a new matrix
   * with the result
   * 
   * @return new matrix_i_j = sqrt(this_i_j)
   */
  public Matrix sqrtElements() {
    Matrix lm;
    int li;

    lm = new Matrix(this.m_m, this.m_n);

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      lm.m_data[li] = Math.sqrt(this.m_data[li]);
    }

    return lm;
  }

  /**
   * calculates sqrt(each cell) of this matrix and overwrites this matrix
   * with the result
   * 
   * @return this_i_j = sqrt this_i_j
   */
  public Matrix sqrtElementsInplace() {
    int li;

    for (li = ((this.m_m * this.m_n) - 1); li >= 0; li--) {
      this.m_data[li] = Math.sqrt(this.m_data[li]);
    }
    return this;
  }

  /**
   * internal method for resizing the data array. it enables the adding of
   * rows or columns.
   * 
   * @param needed
   *          the needed count of slots for doubles
   * @return a array of double capable of storing needed doubles
   */
  private static double[] grow(int needed) {
    try {
      return new double[needed << 1];
    } catch (Exception lex) {
      return new double[needed];
    }
  }

  /**
   * internal routine for appending rows
   * 
   * @param source
   *          original matrix' data
   * @param m
   *          height of original matrix
   * @param n
   *          width of original matrix
   * @param dest
   *          destination data array, can be the same as source
   * @param data
   *          the data for the new rows
   * @param count
   *          the count of rows to append
   * @param ofs
   *          the index where the new rows start in data
   * @param top
   *          true if the rows are to be appended at the top of the matrix
   *          or false if they are to be appended at the bottom
   */
  private static void appendRowsDirect(double[] source, int m, int n,
      double[] dest, double[] data, int count, int ofs, boolean top) {
    int lj, lk;

    lj = n * count;
    lk = n * m;

    if (top) {
      System.arraycopy(source, 0, dest, lj, lk);
      System.arraycopy(data, ofs, dest, 0, lj);
    } else {
      if (source != dest) {
        System.arraycopy(source, 0, dest, 0, lk);
      }

      System.arraycopy(data, ofs, dest, lk, lj);
    }
  }

  /**
   * Creates a new matrix by appending count rows to this matrix and
   * returns the result. The data is stored like: d_1_1 d_1_2 ... d_1_i
   * d_2_1 d_2_2 ... d_2_i... d_i_j
   * 
   * @param data
   *          the data for the new rows
   * @param count
   *          the count of rows to append
   * @param ofs
   *          the index where the new rows start in data
   * @param top
   *          true if the rows are to be appended at the top of the matrix
   *          or false if they are to be appended at the bottom
   * @return a new matrix containing the result
   */
  public Matrix appendRows(double[] data, int count, int ofs, boolean top) {
    Matrix lm;

    if ((ofs < 0) || ((data.length - ofs) < (this.m_n * count)))
      throw new IllegalArgumentException();

    lm = new Matrix(this.m_m + count, this.m_n);

    appendRowsDirect(this.m_data, this.m_m, this.m_n, lm.m_data, data,
        count, ofs, top);

    return lm;
  }

  /**
   * appends count rows to this matrix and returns this matrix. The data is
   * stored like: d_1_1 d_1_2 ... d_1_i d_2_1 d_2_2 ... d_2_i... d_i_j
   * 
   * @param data
   *          the data for the new rows
   * @param count
   *          the count of rows to append
   * @param ofs
   *          the index where the new rows start in data
   * @param top
   *          true if the rows are to be appended at the top of the matrix
   *          or false if they are to be appended at the bottom
   * @return this matrix containing the result
   */
  public Matrix appendRowsInplace(double[] data, int count, int ofs,
      boolean top) {
    int li;
    double[] ld;

    li = this.m_n * count;

    if ((ofs < 0) || ((data.length - ofs) < li))
      throw new IllegalArgumentException();

    li += this.m_n * this.m_m;

    if (li <= this.m_data.length)
      ld = this.m_data;
    else
      ld = grow(li);

    appendRowsDirect(this.m_data, this.m_m, this.m_n, ld, data, count,
        ofs, top);

    this.m_data = ld;
    this.m_m += count;
    return this;
  }

  /**
   * Creates a new matrix by appending a second matrix vertically to this
   * matrix and returns the result. The matrix width must be the same as
   * the width of this matrix
   * 
   * @param matrix
   *          the matrix for the new rows
   * @param top
   *          true if the matrix is to be appended at the top of the matrix
   *          or false if it is to be appended at the bottom
   * @return a new matrix containing the result
   */
  public Matrix appendMatrixVertically(Matrix matrix, boolean top) {
    Matrix lm;

    if (matrix.m_n != this.m_n)
      throw new IllegalArgumentException();

    lm = new Matrix(this.m_m + matrix.m_m, this.m_n);

    appendRowsDirect(this.m_data, this.m_m, this.m_n, lm.m_data,
        matrix.m_data, matrix.m_m, 0, top);

    return lm;
  }

  /**
   * Appends a matrix to this matrix vertically and returns this matrix.
   * The matrix width must be the same as the width of this matrix
   * 
   * @param matrix
   *          the matrix for the new rows
   * @param top
   *          true if the matrix is to be appended at the top of the matrix
   *          or false if it is to be appended at the bottom
   * @return this matrix containing the result
   */
  public Matrix appendMatrixVerticallyInplace(Matrix matrix, boolean top) {
    int li, lj;
    double[] ld;

    if (matrix.m_n != this.m_n)
      throw new IllegalArgumentException();

    li = this.m_n * (lj = (matrix.m_m + this.m_m));

    if (li <= this.m_data.length)
      ld = this.m_data;
    else
      ld = grow(li);

    appendRowsDirect(this.m_data, this.m_m, this.m_n, ld, matrix.m_data,
        matrix.m_m, 0, top);

    this.m_data = ld;
    this.m_m = lj;
    return this;
  }

  /**
   * internal routine for appending rows circular
   * 
   * @param source
   *          original matrix' data
   * @param m
   *          height of original matrix
   * @param n
   *          width of original matrix
   * @param dest
   *          destination data array, can be the same as source
   * @param data
   *          the data for the new rows
   * @param count
   *          the count of rows to append
   * @param ofs
   *          the index where the new rows start in data
   * @param top
   *          true if the rows are to be appended at the top of the matrix
   *          or false if they are to be appended at the bottom
   * @return The new row count
   */
  private static int appendRowsCircularDirect(double[] source, int m,
      int n, double[] dest, double[] data, int count, int ofs, boolean top) {

    int ldata_size, lsource_dest_ofs, lsource_size, lsource_ofs, lmax_m, o, c, mm;

    mm = m;
    lmax_m = dest.length / n;
    o = ofs;
    c = count;

    if (c > lmax_m) {
      if (!top) {
        o += (c - lmax_m) * n;
      }
      c = lmax_m;
    }
    ldata_size = c * n;

    if (top)
      lsource_dest_ofs = ldata_size;
    else
      lsource_dest_ofs = 0;

    lsource_ofs = 0;
    lmax_m -= c;
    if (mm > lmax_m) {
      if (!top) {
        lsource_ofs += (mm - lmax_m) * n;
      }
      mm = lmax_m;
    }
    lsource_size = mm * n;

    if (top)
      lmax_m = 0;
    else
      lmax_m = lsource_size;

    System.arraycopy(source, lsource_ofs, dest, lsource_dest_ofs,
        lsource_size);
    System.arraycopy(data, o, dest, lmax_m, ldata_size);

    return mm + c;
  }

  /**
   * Creates a new matrix by appending count rows to this matrix and
   * returns the result. The data is stored like: d_1_1 d_1_2 ... d_1_i
   * d_2_1 d_2_2 ... d_2_i... d_i_j The appending is done in a circular
   * fashion, the new matrix holds exactly the same space for data as this
   * one. If you append too many new rows, let's say at the top, some rows
   * of the original matrix' bottom maybe become thrown away.
   * 
   * @param data
   *          the data for the new rows
   * @param count
   *          the count of rows to append
   * @param ofs
   *          the index where the new rows start in data
   * @param top
   *          true if the rows are to be appended at the top of the matrix
   *          or false if they are to be appended at the bottom
   * @return a new matrix containing the result
   */
  public Matrix appendRowsCircular(double[] data, int count, int ofs,
      boolean top) {
    Matrix lm;

    if ((ofs < 0) || ((data.length - ofs) < (this.m_n * count)))
      throw new IllegalArgumentException();

    lm = new Matrix(0, this.m_n, this.m_data.length / this.m_n, this.m_n);

    lm.m_m = appendRowsCircularDirect(this.m_data, this.m_m, this.m_n,
        lm.m_data, data, count, ofs, top);

    return lm;
  }

  /**
   * Appends count rows to this matrix and returns this matrix. The data is
   * stored like: d_1_1 d_1_2 ... d_1_i d_2_1 d_2_2 ... d_2_i... d_i_j The
   * appending is done in a circular fashion, the new matrix holds exactly
   * the same space for data as this one. If you append too many new rows,
   * let's say at the top, some rows of the original matrix' bottom maybe
   * become thrown away.
   * 
   * @param data
   *          the data for the new rows
   * @param count
   *          the count of rows to append
   * @param ofs
   *          the index where the new rows start in data
   * @param top
   *          true if the rows are to be appended at the top of the matrix
   *          or false if they are to be appended at the bottom
   * @return this matrix containing the result
   */
  public Matrix appendRowsCircularInplace(double[] data, int count,
      int ofs, boolean top) {
    if ((ofs < 0) || ((data.length - ofs) < (this.m_n * count)))
      throw new IllegalArgumentException();

    this.m_m = appendRowsCircularDirect(this.m_data, this.m_m, this.m_n,
        this.m_data, data, count, ofs, top);

    return this;
  }

  /**
   * Creates a new matrix by appending a second matrix vertically to this
   * matrix and returns the result. The matrix width must be the same as
   * the width of this matrix The appending is done in a circular fashion,
   * the new matrix holds exactly the same space for data as this one. If
   * you append too many new rows, let's say at the top, some rows of the
   * original matrix' bottom maybe become thrown away.
   * 
   * @param matrix
   *          the matrix for the new rows
   * @param top
   *          true if the matrix is to be appended at the top of the matrix
   *          or false if it is to be appended at the bottom
   * @return a new matrix containing the result
   */
  public Matrix appendMatrixCircularVertically(Matrix matrix, boolean top) {
    Matrix lm;

    if (matrix.m_n != this.m_n)
      throw new IllegalArgumentException();

    lm = new Matrix(0, this.m_n, this.m_data.length / this.m_n, this.m_n);

    lm.m_m = appendRowsCircularDirect(this.m_data, this.m_m, this.m_n,
        lm.m_data, matrix.m_data, matrix.m_m, 0, top);

    return lm;
  }

  /**
   * Appends a matrix to this matrix vertically and returns this matrix.
   * The matrix width must be the same as the width of this matrix The
   * appending is done in a circular fashion, the new matrix holds exactly
   * the same space for data as this one. If you append too many new rows,
   * let's say at the top, some rows of the original matrix maybe become
   * thrown away.
   * 
   * @param matrix
   *          the matrix for the new rows
   * @param top
   *          true if the matrix is to be appended at the top of the matrix
   *          or false if it is to be appended at the bottom
   * @return this matrix containing the result
   */
  public Matrix appendMatrixCircularVerticallyInplace(Matrix matrix,
      boolean top) {
    if (matrix.m_n != this.m_n)
      throw new IllegalArgumentException();

    this.m_m = appendRowsCircularDirect(this.m_data, this.m_m, this.m_n,
        this.m_data, matrix.m_data, matrix.m_m, 0, top);

    return this;
  }

  /**
   * internal routine for appending columns
   * 
   * @param source
   *          original matrix' data
   * @param m
   *          height of original matrix
   * @param n
   *          width of original matrix
   * @param dest
   *          destination data array, can be the same as source
   * @param data
   *          the data for the new columns
   * @param count
   *          the count of columns to append
   * @param ofs
   *          the index where the new columns start in data
   * @param left
   *          true if the columns are to be appended at the left of the
   *          matrix or false if they are to be appended at the right
   */
  private static void appendColumnsDirect(double[] source, int m, int n,
      double[] dest, double[] data, int count, int ofs, boolean left) {
    int li, lj, lk, lk2, lz, mm;

    lz = n + count;
    mm = m;

    mm--;
    li = (mm * count) + ofs;
    lj = mm * n;
    lk = mm * lz;

    if (left) {
      lk2 = lk;
      lk += count;
    } else {
      lk2 = lk + n;
    }

    for (; mm >= 0; mm--) {
      System.arraycopy(source, lj, dest, lk, n);
      System.arraycopy(data, li, dest, lk2, count);
      lk -= lz;
      lk2 -= lz;
      lj -= n;
      li -= count;
    }
  }

  /**
   * Creates a new matrix by appending count columns to this matrix and
   * returns the result. The data is stored like: d_1_1 d_1_2 ... d_1_i
   * d_2_1 d_2_2 ... d_2_i... d_i_j
   * 
   * @param data
   *          the data for the new columns
   * @param count
   *          the count of columns to append
   * @param ofs
   *          the index where the new columns start in data
   * @param left
   *          true if the columns are to be appended at the left of the
   *          matrix or false if they are to be appended at the right
   * @return a new matrix containing the result
   */
  public Matrix appendColumns(double[] data, int count, int ofs,
      boolean left) {
    Matrix lm;

    if ((ofs < 0) || ((data.length - ofs) < (this.m_m * count)))
      throw new IllegalArgumentException();

    lm = new Matrix(this.m_m, this.m_n + count);

    appendColumnsDirect(this.m_data, this.m_m, this.m_n, lm.m_data, data,
        count, ofs, left);

    return lm;
  }

  /**
   * appends count columns to this matrix and returns this matrix as
   * result. The data is stored like: d_1_1 d_1_2 ... d_1_i d_2_1 d_2_2 ...
   * d_2_i... d_i_j
   * 
   * @param data
   *          the data for the new columns
   * @param count
   *          the count of columns to append
   * @param ofs
   *          the index where the new columns start in data
   * @param left
   *          true if the columns are to be appended at the left of the
   *          matrix or false if they are to be appended at the right
   * @return this matrix containing the result
   */
  public Matrix appendColumnsInplace(double[] data, int count, int ofs,
      boolean left) {
    int li;
    double[] ld;

    if ((ofs < 0) || ((data.length - ofs) < (this.m_m * count)))
      throw new IllegalArgumentException();

    if ((li = (this.m_m * (this.m_n + count))) > this.m_data.length)
      ld = grow(li);
    else
      ld = this.m_data;

    appendColumnsDirect(this.m_data, this.m_m, this.m_n, ld, data, count,
        ofs, left);

    this.m_data = ld;
    this.m_n += count;
    return this;
  }

  /**
   * Creates a new matrix by appending a matrix horizontally to this matrix
   * and returns the result. The matrix must be of the same height as this
   * one.
   * 
   * @param matrix
   *          the matrix with the new columns
   * @param left
   *          true if the matrix is to be appended at the left of the
   *          matrix or false if it is to be appended at the right
   * @return a new matrix containing the result
   */
  public Matrix appendMatrixHorizontally(Matrix matrix, boolean left) {
    Matrix lm;

    if (matrix.m_m != this.m_m)
      throw new IllegalArgumentException();

    lm = new Matrix(this.m_m, this.m_n + matrix.m_n);

    appendColumnsDirect(this.m_data, this.m_m, this.m_n, lm.m_data,
        matrix.m_data, matrix.m_n, 0, left);

    return lm;
  }

  /**
   * Appends a new matrix horizontally to this matrix and returns this
   * matrix as result. The matrix must be of the same height as this one.
   * 
   * @param matrix
   *          the matrix with the new columns
   * @param left
   *          true if the matrix is to be appended at the left of the
   *          matrix or false if it is to be appended at the right
   * @return this matrix containing the result
   */
  public Matrix appendMatrixHorizontallyInplace(Matrix matrix, boolean left) {
    int li, lj;
    double[] ld;

    if (matrix.m_m != this.m_m)
      throw new IllegalArgumentException();

    if ((li = (this.m_m * (lj = (this.m_n + matrix.m_n)))) > this.m_data.length)
      ld = grow(li);
    else
      ld = this.m_data;

    appendColumnsDirect(this.m_data, this.m_m, this.m_n, ld,
        matrix.m_data, matrix.m_n, 0, left);

    this.m_data = ld;
    this.m_n = lj;
    return this;
  }

  /**
   * internal routine for appending columns circular
   * 
   * @param source
   *          original matrix' data
   * @param m
   *          height of original matrix
   * @param n
   *          width of original matrix
   * @param dest
   *          destination data array, can be the same as source
   * @param data
   *          the data for the new columns
   * @param count
   *          the count of columns to append
   * @param ofs
   *          the index where the new columns start in data
   * @param left
   *          true if the columns are to be appended at the left of the
   *          matrix or false if they are to be appended at the right
   * @return the new column count
   */
  private static int appendColumnsCircularDirect(double[] source, int m,
      int n, double[] dest, double[] data, int count, int ofs, boolean left) {
    int lmax_n, lsource_ofs, lk, lk2, lz, ln, o, c, mm;

    mm = m;
    lmax_n = dest.length / mm;
    o = ofs;
    c = count;

    o += c * mm;
    if (c > lmax_n) {
      if (left) {
        o -= (c - lmax_n) * mm;
      }
      c = lmax_n;
    }
    lmax_n -= c;

    lsource_ofs = n * mm;
    if (n > lmax_n) {
      if (!left) {
        lsource_ofs += (n - lmax_n);
      }
      ln = lmax_n;
    } else
      ln = n;

    lz = ln + c;
    mm--;
    lk = mm * lz;

    if (left) {
      lk2 = lk;
      lk += c;
    } else {
      lk2 = lk + ln;
    }

    for (; mm >= 0; mm--) {
      lsource_ofs -= n;
      o -= c;
      System.arraycopy(source, lsource_ofs, dest, lk, ln);
      System.arraycopy(data, o, dest, lk2, c);
      lk -= lz;
      lk2 -= lz;
    }

    return lz;
  }

  /**
   * Creates a new matrix by appending count columns to this matrix in a
   * circular fashion and returns the result. Circular means that the
   * matrix is shifted to the left (if appending at the right) or to the
   * right (when appending at the left side). If it has not enought memory
   * allocated to hold all columns, the columns shifted over the edge will
   * be dropped. The data is stored like: d_1_1 d_1_2 ... d_1_i d_2_1 d_2_2
   * ... d_2_i... d_i_j
   * 
   * @param data
   *          the data for the new columns
   * @param count
   *          the count of columns to append
   * @param ofs
   *          the index where the new columns start in data
   * @param left
   *          true if the columns are to be appended at the left of the
   *          matrix or false if they are to be appended at the right
   * @return a new matrix containing the result
   */
  public Matrix appendColumnsCircular(double[] data, int count, int ofs,
      boolean left) {
    Matrix lm;

    if ((ofs < 0) || ((data.length - ofs) < (this.m_m * count)))
      throw new IllegalArgumentException();

    lm = new Matrix(this.m_m, 0, this.m_m, this.m_data.length / this.m_m);

    lm.m_n = appendColumnsCircularDirect(this.m_data, this.m_m, this.m_n,
        lm.m_data, data, count, ofs, left);

    return lm;
  }

  /**
   * Appends count columns to this matrix in a circular fashion and returns
   * this matrix as result. Circular means that the matrix is shifted to
   * the left (if appending at the right) or to the right (when appending
   * at the left side). If it has not enought memory allocated to hold all
   * columns, the columns shifted over the edge will be dropped. The data
   * is stored like: d_1_1 d_1_2 ... d_1_i d_2_1 d_2_2 ... d_2_i... d_i_j
   * 
   * @param data
   *          the data for the new columns
   * @param count
   *          the count of columns to append
   * @param ofs
   *          the index where the new columns start in data
   * @param left
   *          true if the columns are to be appended at the left of the
   *          matrix or false if they are to be appended at the right
   * @return this matrix containing the result
   */
  public Matrix appendColumnsCircularInplace(double[] data, int count,
      int ofs, boolean left) {
    if ((ofs < 0) || ((data.length - ofs) < (this.m_m * count)))
      throw new IllegalArgumentException();

    this.m_n = appendColumnsCircularDirect(this.m_data, this.m_m,
        this.m_n, this.m_data, data, count, ofs, left);

    return this;
  }

  /**
   * Creates a new matrix by appending a matrix horizontally to this matrix
   * in a circular fashion and returns the result. Circular means that the
   * matrix is shifted to the left (if appending at the right) or to the
   * right (when appending at the left side). If it has not enought memory
   * allocated to hold all columns, the columns shifted over the edge will
   * be dropped. The matrix must be of the same height as this one.
   * 
   * @param matrix
   *          the matrix with the new columns
   * @param left
   *          true if the matrix is to be appended at the left of the
   *          matrix or false if it is to be appended at the right
   * @return a new matrix containing the result
   */
  public Matrix appendMatrixCircularHorizontally(Matrix matrix,
      boolean left) {
    Matrix lm;

    if (matrix.m_m != this.m_m)
      throw new IllegalArgumentException();

    lm = new Matrix(this.m_m, 0, this.m_m, this.m_data.length / this.m_m);

    lm.m_n = appendColumnsCircularDirect(this.m_data, this.m_m, this.m_n,
        lm.m_data, matrix.m_data, matrix.m_n, 0, left);

    return lm;
  }

  /**
   * Appends a new matrix horizontally to this matrix in a circular fashion
   * and returns this matrix as result. Circular means that the matrix is
   * shifted to the left (if appending at the right) or to the right (when
   * appending at the left side). If it has not enought memory allocated to
   * hold all columns, the columns shifted over the edge will be dropped.
   * The matrix must be of the same height as this one.
   * 
   * @param matrix
   *          the matrix with the new columns
   * @param left
   *          true if the matrix is to be appended at the left of the
   *          matrix or false if it is to be appended at the right
   * @return this matrix containing the result
   */
  public Matrix appendMatrixCircularHorizontallyInplace(Matrix matrix,
      boolean left) {

    if (matrix.m_m != this.m_m)
      throw new IllegalArgumentException();

    this.m_n = appendColumnsCircularDirect(this.m_data, this.m_m,
        this.m_n, this.m_data, matrix.m_data, matrix.m_n, 0, left);

    return this;
  }

  /**
   * internal routine for appending rows
   * 
   * @param source
   *          original matrix' data
   * @param m
   *          height of original matrix
   * @param n
   *          width of original matrix
   * @param dest
   *          destination data array, can be the same as source
   * @param data
   *          the two-dimensional data for the new rows
   * @param top
   *          true if the rows are to be appended at the top of the matrix
   *          or false if they are to be appended at the bottom
   */
  private static void appendRowsDirect(double[] source, int m, int n,
      double[] dest, double[][] data, boolean top) {
    int li, lj, lk;

    lk = m * n;
    lj = data.length * n;

    if (top) {
      System.arraycopy(source, 0, dest, lj, lk);

      lj -= n;
      for (li = (data.length - 1); li >= 0; li--) {
        System.arraycopy(data[li], 0, dest, lj, n);
        lj -= n;
      }
    } else {
      if (source != dest) {
        System.arraycopy(source, 0, dest, 0, lk);
      }

      lk += lj - n;
      for (li = (data.length - 1); li >= 0; li--) {
        System.arraycopy(data[li], 0, dest, lk, n);
        lk -= n;
      }
    }
  }

  /**
   * Creates a new matrix by appending some rows to this matrix and returns
   * the result.
   * 
   * @param data
   *          the two-dimensional data for the new rows
   * @param top
   *          true if the rows are to be appended at the top of the matrix
   *          or false if they are to be appended at the bottom
   * @return a new matrix containing the result
   */
  public Matrix appendRows(double[][] data, boolean top) {
    Matrix lm;

    if (data[0].length != this.m_n)
      throw new IllegalArgumentException();

    lm = new Matrix(this.m_m + data.length, this.m_n);

    appendRowsDirect(this.m_data, this.m_m, this.m_n, lm.m_data, data, top);
    return lm;
  }

  /**
   * appends some rows to this matrix and returns this matrix.
   * 
   * @param data
   *          the two-dimensional data for the new rows
   * @param top
   *          true if the rows are to be appended at the top of the matrix
   *          or false if they are to be appended at the bottom
   * @return this matrix containing the result
   */
  public Matrix appendRowsInplace(double[][] data, boolean top) {
    int li, lj;
    double[] ld;

    if (data[0].length != this.m_n)
      throw new IllegalArgumentException();

    li = (this.m_m + (lj = data.length)) * this.m_n;

    if (li <= this.m_data.length)
      ld = this.m_data;
    else
      ld = grow(li);

    appendRowsDirect(this.m_data, this.m_m, this.m_n, ld, data, top);
    this.m_data = ld;
    this.m_m += lj;
    return this;
  }

  /**
   * internal routine for appending columns
   * 
   * @param source
   *          original matrix' data
   * @param m
   *          height of original matrix
   * @param n
   *          width of original matrix
   * @param dest
   *          destination data array, can be the same as source
   * @param data
   *          the two-dimensional data for the new rows
   * @param left
   *          true if the rows are to be appended at the top of the matrix
   *          or false if they are to be appended at the bottom
   */
  private static void appendColumnsDirect(double[] source, int m, int n,
      double[] dest, double[][] data, boolean left) {
    int li, lj, lk, lk2, lz, mm;

    mm = m;
    li = data[0].length;
    lz = n + li;
    mm--;
    lj = mm * n;
    lk = mm * lz;

    if (left) {
      lk2 = lk;
      lk += li;
    } else {
      lk2 = lk + n;
    }

    for (; mm >= 0; mm--) {
      System.arraycopy(source, lj, dest, lk, n);
      System.arraycopy(data[mm], 0, dest, lk2, li);
      lk -= lz;
      lk2 -= lz;
      lj -= n;
    }
  }

  /**
   * Creates a new matrix by appending some columns to this matrix and
   * returns the result.
   * 
   * @param data
   *          the two-dimensional data for the new columns
   * @param left
   *          true if the columns are to be appended at the left of the
   *          matrix or false if they are to be appended at the right
   * @return a new matrix containing the result
   */
  public Matrix appendColumns(double[][] data, boolean left) {
    Matrix lm;

    if (data.length != this.m_m)
      throw new IllegalArgumentException();

    lm = new Matrix(this.m_m, this.m_n + data[0].length);

    appendColumnsDirect(this.m_data, this.m_m, this.m_n, lm.m_data, data,
        left);
    return lm;
  }

  /**
   * appends some columns to this matrix and returns this matrix as result.
   * 
   * @param data
   *          the two-dimensional data for the new columns
   * @param left
   *          true if the columns are to be appended at the left of the
   *          matrix or false if they are to be appended at the right
   * @return this matrix containing the result
   */
  public Matrix appendColumnsInplace(double[][] data, boolean left) {
    int li, lj;
    double[] ld;

    if (data.length != this.m_m)
      throw new IllegalArgumentException();

    if ((li = (this.m_m * ((lj = data[0].length) + this.m_n))) > this.m_data.length)
      ld = grow(li);
    else
      ld = this.m_data;

    appendColumnsDirect(this.m_data, this.m_m, this.m_n, ld, data, left);

    this.m_data = ld;
    this.m_n += lj;
    return this;
  }

  /**
   * internal routine for row deletion
   * 
   * @param source
   *          original matrix' data
   * @param m
   *          height of original matrix
   * @param n
   *          width of original matrix
   * @param dest
   *          destination data array, can be the same as source
   * @param index
   *          the index of the row where to start with the deletion
   * @param count
   *          the count of rows to delete
   * @return new row count
   */
  private static int deleteRowsDirect(double[] source, int m, int n,
      double[] dest, int index, int count) {
    int li, lj, c, ii;

    c = count;
    ii = index;
    if ((ii >= m) || (c <= 0)) {
      if (source != dest)
        System.arraycopy(source, 0, dest, 0, m * n);
      return m;
    }

    if (ii < 0) {
      c += ii;
      ii = 0;
    }

    li = c + ii;
    if (li >= m) {
      c = m - ii;
      li = m;
    }

    lj = ii * n;
    if ((lj > 0) && (source != dest))
      System.arraycopy(source, 0, dest, 0, lj);

    System.arraycopy(source, lj * n, dest, li, (m - lj) * n);

    return m - c;
  }

  /**
   * Deletes some rows and returns a new matrix containing the result.
   * 
   * @param index
   *          the index of the row where to start with the deletion
   * @param count
   *          the count of rows to delete
   * @return a new matrix containing the result
   */
  public Matrix deleteRows(int index, int count) {
    Matrix lm;
    if (count < 0)
      throw new IllegalArgumentException();

    lm = new Matrix(this.m_m - count, this.m_n);
    lm.m_m = deleteRowsDirect(this.m_data, this.m_m, this.m_n, lm.m_data,
        index, count);
    return lm;
  }

  /**
   * Deletes some rows in this matrix and returns this matrix containing
   * the result.
   * 
   * @param index
   *          the index of the row where to start with the deletion
   * @param count
   *          the count of rows to delete
   * @return this matrix containing the result
   */
  public Matrix deleteRowsInplace(int index, int count) {
    if (count < 0)
      throw new IllegalArgumentException();

    this.m_m = deleteRowsDirect(this.m_data, this.m_m, this.m_n,
        this.m_data, index, count);
    return this;
  }

  /**
   * internal routine for column deletion
   * 
   * @param source
   *          original matrix' data
   * @param m
   *          height of original matrix
   * @param n
   *          width of original matrix
   * @param dest
   *          destination data array, can be the same as source
   * @param index
   *          the index of the column where to start with the deletion
   * @param count
   *          the count of columns to delete
   * @return new column count
   */
  private static int deleteColumnsDirect(double[] source, int m, int n,
      double[] dest, int index, int count) {
    int li, lj, lk, ll, ii, c, mm;

    ii = index;
    mm = m;
    c = count;
    if ((ii >= n) || (c <= 0)) {
      if (source != dest)
        System.arraycopy(source, 0, dest, 0, mm * n);
      return mm;
    }

    if (ii < 0) {
      c += ii;
      ii = 0;
    }

    if ((c + ii) >= n)
      c = n - ii;

    li = 0;
    lj = ii + c;
    lk = 0;
    ll = n - lj;

    for (; mm > 0; mm--) {
      System.arraycopy(source, li, dest, lk, ii);
      lk += ii;
      System.arraycopy(source, lj, dest, lk, ll);
      lk += ll;
      li += n;
      lj += n;
    }

    return n - c;
  }

  /**
   * Deletes some columns and returns a new matrix containing the result.
   * 
   * @param index
   *          the index of the column where to start with the deletion
   * @param count
   *          the count of columns to delete
   * @return a new matrix containing the result
   */
  public Matrix deleteColumns(int index, int count) {
    Matrix lm;
    if (count < 0)
      throw new IllegalArgumentException();

    lm = new Matrix(this.m_m, this.m_n - count);
    lm.m_n = deleteColumnsDirect(this.m_data, this.m_m, this.m_n,
        lm.m_data, index, count);
    return lm;
  }

  /**
   * Deletes some columns in this matrix and returns this matrix containing
   * the result.
   * 
   * @param index
   *          the index of the column where to start with the deletion
   * @param count
   *          the count of columns to delete
   * @return this matrix containing the result
   */
  public Matrix deleteColumnsInplace(int index, int count) {
    if (count < 0)
      throw new IllegalArgumentException();

    this.m_n = deleteColumnsDirect(this.m_data, this.m_m, this.m_n,
        this.m_data, index, count);
    return this;
  }

  /**
   * internal routine for inserting rows
   * 
   * @param source
   *          original matrix' data
   * @param m
   *          height of original matrix
   * @param n
   *          width of original matrix
   * @param dest
   *          destination data array, can be the same as source
   * @param data
   *          the data for the new rows
   * @param index
   *          the index where to insert the new rows
   * @param count
   *          the count of rows to insert
   * @param ofs
   *          the index where the new rows start in data
   */
  private static void insertRowsDirect(double[] source, int m, int n,
      double[] dest, double[] data, int index, int count, int ofs) {
    int li, lj;

    if (index <= 0) {
      appendRowsDirect(source, m, n, dest, data, count, ofs, true);
      return;
    }

    if (index >= m) {
      appendRowsDirect(source, m, n, dest, data, count, ofs, false);
      return;
    }

    li = index * n;
    lj = count * n;

    System.arraycopy(source, li, dest, li + lj, (m - index) * n);

    if (dest != source) {
      System.arraycopy(source, 0, dest, 0, li);
    }

    System.arraycopy(data, ofs, dest, li, lj);
  }

  /**
   * Creates a new matrix by inserting count rows to this matrix and
   * returns the result. The data is stored like: d_1_1 d_1_2 ... d_1_i
   * d_2_1 d_2_2 ... d_2_i... d_i_j
   * 
   * @param data
   *          the data for the new rows
   * @param index
   *          the index where to insert the rows
   * @param count
   *          the count of rows to append
   * @param ofs
   *          the index where the new rows start in data
   * @return a new matrix containing the result
   */
  public Matrix insertRows(double[] data, int index, int count, int ofs) {
    Matrix lm;

    if ((ofs < 0) || ((data.length - ofs) < (this.m_n * count)))
      throw new IllegalArgumentException();

    lm = new Matrix(this.m_m + count, this.m_n);

    insertRowsDirect(this.m_data, this.m_m, this.m_n, lm.m_data, data,
        index, count, ofs);

    return lm;
  }

  /**
   * Appends count rows to this matrix and returns this matrix. The data is
   * stored like: d_1_1 d_1_2 ... d_1_i d_2_1 d_2_2 ... d_2_i... d_i_j
   * 
   * @param data
   *          the data for the new rows
   * @param index
   *          the index where to insert the rows
   * @param count
   *          the count of rows to append
   * @param ofs
   *          the index where the new rows start in data
   * @return this matrix containing the result
   */
  public Matrix insertRowsInplace(double[] data, int index, int count,
      int ofs) {
    int li;
    double[] ld;

    li = this.m_n * count;

    if ((ofs < 0) || ((data.length - ofs) < li))
      throw new IllegalArgumentException();

    li += this.m_n * this.m_m;

    if (li <= this.m_data.length)
      ld = this.m_data;
    else
      ld = grow(li);

    insertRowsDirect(this.m_data, this.m_m, this.m_n, ld, data, index,
        count, ofs);

    this.m_data = ld;
    this.m_m += count;
    return this;
  }

  /**
   * Creates a new matrix by inserting a second matrix vertically into this
   * matrix and returns the result. The matrix must be of the same width as
   * this one.
   * 
   * @param matrix
   *          the matrix for the new rows
   * @param index
   *          the index where to insert the matrix
   * @return a new matrix containing the result
   */
  public Matrix insertMatrixVertically(Matrix matrix, int index) {
    Matrix lm;

    if (matrix.m_n != this.m_n)
      throw new IllegalArgumentException();

    lm = new Matrix(this.m_m + matrix.m_m, this.m_n);

    insertRowsDirect(this.m_data, this.m_m, this.m_n, lm.m_data,
        matrix.m_data, index, matrix.m_m, 0);

    return lm;
  }

  /**
   * Inserts a matrix into this matrix vertically and returns this matrix.
   * The matrix must be of the same width as this one.
   * 
   * @param matrix
   *          the matrix for the new rows
   * @param index
   *          the index where to insert the matrix
   * @return this matrix containing the result
   */
  public Matrix insertMatrixVerticallyInplace(Matrix matrix, int index) {
    int li, lj;
    double[] ld;

    if (matrix.m_n != this.m_n)
      throw new IllegalArgumentException();

    li = this.m_n * (lj = (matrix.m_m + this.m_m));

    if (li <= this.m_data.length)
      ld = this.m_data;
    else
      ld = grow(li);

    insertRowsDirect(this.m_data, this.m_m, this.m_n, ld, matrix.m_data,
        index, matrix.m_m, 0);

    this.m_data = ld;
    this.m_m = lj;
    return this;
  }

  /**
   * internal routine for inserting columns
   * 
   * @param source
   *          original matrix' data
   * @param m
   *          height of original matrix
   * @param n
   *          width of original matrix
   * @param dest
   *          destination data array, can be the same as source
   * @param data
   *          the data for the new columns
   * @param index
   *          the index where to insert the new columns
   * @param count
   *          the count of rows to insert
   * @param ofs
   *          the index where the new columns start in data
   */
  private static void insertColumnsDirect(double[] source, int m, int n,
      double[] dest, double[] data, int index, int count, int ofs) {
    int li, lj, lk, ll, lo, lp, xx;

    xx = m;
    if (index <= 0) {
      appendColumnsDirect(source, xx, n, dest, data, count, ofs, true);
      return;
    }

    if (index >= xx) {
      appendColumnsDirect(source, xx, n, dest, data, count, ofs, false);
      return;
    }

    xx--;
    li = xx * n;
    lk = n + count;
    lj = xx * lk;
    ll = n - index;
    lo = ofs + (xx * count);
    lp = lj + index;

    for (; xx >= 0; xx--) {
      System.arraycopy(source, li + index, dest, lp + count, ll);
      System.arraycopy(source, li, dest, lj, index);
      System.arraycopy(data, lo, dest, lp, count);
      li -= n;
      lj -= lk;
      lp -= lk;
      lo -= count;
    }
  }

  /**
   * Creates a new matrix by inserting count columns into this matrix and
   * returns the result. The data is stored like: d_1_1 d_1_2 ... d_1_i
   * d_2_1 d_2_2 ... d_2_i... d_i_j
   * 
   * @param data
   *          the data for the new columns
   * @param index
   *          the index where to insert the new columns
   * @param count
   *          the count of columns to append
   * @param ofs
   *          the index where the new columns start in data
   * @return a new matrix containing the result
   */
  public Matrix insertColumns(double[] data, int index, int count, int ofs) {
    Matrix lm;

    if ((ofs < 0) || ((data.length - ofs) < (this.m_m * count)))
      throw new IllegalArgumentException();

    lm = new Matrix(this.m_m, this.m_n + count);

    insertColumnsDirect(this.m_data, this.m_m, this.m_n, lm.m_data, data,
        index, count, ofs);

    return lm;
  }

  /**
   * Inserts count columns into this matrix and returns this matrix as
   * result. The data is stored like: d_1_1 d_1_2 ... d_1_i d_2_1 d_2_2 ...
   * d_2_i... d_i_j
   * 
   * @param data
   *          the data for the new columns
   * @param index
   *          the index where to insert the new columns
   * @param count
   *          the count of columns to append
   * @param ofs
   *          the index where the new columns start in data
   * @return this matrix containing the result
   */
  public Matrix insertColumnsInplace(double[] data, int index, int count,
      int ofs) {
    int li;
    double[] ld;

    if ((ofs < 0) || ((data.length - ofs) < (this.m_m * count)))
      throw new IllegalArgumentException();

    if ((li = (this.m_m * (this.m_n + count))) > this.m_data.length)
      ld = grow(li);
    else
      ld = this.m_data;

    insertColumnsDirect(this.m_data, this.m_m, this.m_n, ld, data, index,
        count, ofs);

    this.m_data = ld;
    this.m_n += count;
    return this;
  }

  /**
   * Creates a new matrix by inserting a matrix horizontally into this
   * matrix and returns the result. The matrix must be of the same height
   * as this one.
   * 
   * @param matrix
   *          the matrix with the new columns
   * @param index
   *          the index where to insert the new columns
   * @return a new matrix containing the result
   */
  public Matrix insertMatrixHorizontally(Matrix matrix, int index) {
    Matrix lm;

    if (matrix.m_m != this.m_m)
      throw new IllegalArgumentException();

    lm = new Matrix(this.m_m, this.m_n + matrix.m_n);

    insertColumnsDirect(this.m_data, this.m_m, this.m_n, lm.m_data,
        matrix.m_data, index, matrix.m_n, 0);

    return lm;
  }

  /**
   * Inserts a new matrix horizontally into this matrix and returns this
   * matrix as result. The matrix must be of the same height as this one.
   * 
   * @param matrix
   *          the matrix with the new columns
   * @param index
   *          the index where to insert the new columns
   * @return this matrix containing the result
   */
  public Matrix insertMatrixHorizontallyInplace(Matrix matrix, int index) {
    int li, lj;
    double[] ld;

    if (matrix.m_m != this.m_m)
      throw new IllegalArgumentException();

    if ((li = (this.m_m * (lj = (this.m_n + matrix.m_n)))) > this.m_data.length)
      ld = grow(li);
    else
      ld = this.m_data;

    insertColumnsDirect(this.m_data, this.m_m, this.m_n, ld,
        matrix.m_data, index, matrix.m_n, 0);

    this.m_data = ld;
    this.m_n = lj;
    return this;
  }

  /**
   * internal routine for inserting rows
   * 
   * @param source
   *          original matrix' data
   * @param m
   *          height of original matrix
   * @param n
   *          width of original matrix
   * @param dest
   *          destination data array, can be the same as source
   * @param data
   *          the tow-dimensional data for the new rows
   * @param index
   *          the index where to insert the new rows
   */
  private static void insertRowsDirect(double[] source, int m, int n,
      double[] dest, double[][] data, int index) {
    int li, lj, lk;

    if (index <= 0) {
      appendRowsDirect(source, m, n, dest, data, true);
      return;
    }

    if (index >= m) {
      appendRowsDirect(source, m, n, dest, data, false);
      return;
    }

    lk = data.length;
    li = index * n;
    lj = lk * n;

    System.arraycopy(source, li, dest, li + lj, (m - index) * n);

    if (dest != source) {
      System.arraycopy(source, 0, dest, 0, li);
    }

    li += lj - n;
    for (--lk; lk >= 0; lk--) {
      System.arraycopy(data[lk], 0, dest, li, n);
      li -= n;
    }
  }

  /**
   * Creates a new matrix by inserting some rows into this matrix and
   * returns the result.
   * 
   * @param data
   *          the two-dimensional data for the new rows
   * @param index
   *          the index where to insert the rows
   * @return a new matrix containing the result
   */
  public Matrix insertRows(double[][] data, int index) {
    Matrix lm;

    if (data[0].length != this.m_n)
      throw new IllegalArgumentException();

    lm = new Matrix(this.m_m + data.length, this.m_n);

    insertRowsDirect(this.m_data, this.m_m, this.m_n, lm.m_data, data,
        index);
    return lm;
  }

  /**
   * Inserts some rows to this matrix and returns this matrix.
   * 
   * @param data
   *          the two-dimensional data for the new rows
   * @param index
   *          the index where to insert the new rows
   * @return this matrix containing the result
   */
  public Matrix insertRowsInplace(double[][] data, int index) {
    int li, lj;
    double[] ld;

    if (data[0].length != this.m_n)
      throw new IllegalArgumentException();

    li = (this.m_m + (lj = data.length)) * this.m_n;

    if (li <= this.m_data.length)
      ld = this.m_data;
    else
      ld = grow(li);

    insertRowsDirect(this.m_data, this.m_m, this.m_n, ld, data, index);
    this.m_data = ld;
    this.m_m += lj;
    return this;
  }

  /**
   * internal routine for inserting columns
   * 
   * @param source
   *          original matrix' data
   * @param m
   *          height of original matrix
   * @param n
   *          width of original matrix
   * @param dest
   *          destination data array, can be the same as source
   * @param data
   *          the two-dimensional data for the new columns
   * @param index
   *          the index where to insert the new columns
   */
  private static void insertColumnsDirect(double[] source, int m, int n,
      double[] dest, double[][] data, int index) {
    int li, lj, lk, ll, lo, lp, mm;

    mm = m;
    if (index <= 0) {
      appendColumnsDirect(source, mm, n, dest, data, true);
      return;
    }

    if (index >= mm) {
      appendColumnsDirect(source, mm, n, dest, data, false);
      return;
    }

    mm--;
    li = mm * n;
    lo = data[0].length;
    lk = n + lo;
    lj = mm * lk;
    ll = n - index;
    lp = lj + index;

    for (; mm >= 0; mm--) {
      System.arraycopy(source, li + index, dest, lp + lo, ll);
      System.arraycopy(source, li, dest, lj, index);
      System.arraycopy(data[mm], 0, dest, lp, data[0].length);
      li -= n;
      lj -= lk;
      lp -= lk;
    }
  }

  /**
   * Creates a new matrix by inserting some columns to this matrix and
   * returns the result.
   * 
   * @param data
   *          the two-dimensional data for the new columns
   * @param index
   *          the index where to insert the new columns
   * @return a new matrix containing the result
   */
  public Matrix insertColumns(double[][] data, int index) {
    Matrix lm;

    if (data.length != this.m_m)
      throw new IllegalArgumentException();

    lm = new Matrix(this.m_m, this.m_n + data[0].length);

    insertColumnsDirect(this.m_data, this.m_m, this.m_n, lm.m_data, data,
        index);
    return lm;
  }

  /**
   * Inserts some columns into this matrix and returns this matrix as
   * result.
   * 
   * @param data
   *          the two-dimensional data for the new columns
   * @param index
   *          the index where to insert the new columns
   * @return this matrix containing the result
   */
  public Matrix insertColumnsInplace(double[][] data, int index) {
    int li, lj;
    double[] ld;

    if (data.length != this.m_m)
      throw new IllegalArgumentException();

    if ((li = (this.m_m * ((lj = data[0].length) + this.m_n))) > this.m_data.length)
      ld = grow(li);
    else
      ld = this.m_data;

    insertColumnsDirect(this.m_data, this.m_m, this.m_n, ld, data, index);

    this.m_data = ld;
    this.m_n += lj;
    return this;
  }

  /**
   * returns the two norm condition number (the ration of the largest to
   * the smallest singular value)
   * 
   * @return max{sigma[]}/min{sigma[]}
   */
  public double conditionNumber() {
    return new SingularValueDecomposition(this.m_m, this.m_n, this.m_data,
        false, false).conditionNumber();
  }

  /**
   * returns the effective numerical rank
   * 
   * @return the number of non-negligible singular values
   */
  public int rank() {
    return new SingularValueDecomposition(this.m_m, this.m_n, this.m_data,
        false, false).rank();
  }

  /**
   * returns the 1 norm of this matrix
   * 
   * @return maximum column sum
   */
  public double norm1() {
    int li, lj, la, lb;
    double ls, lc;

    ls = Double.NEGATIVE_INFINITY;
    la = this.m_m * this.m_n;

    for (li = (this.m_n - 1); li >= 0; li--) {
      lc = 0.0;
      lb = --la;
      for (lj = (this.m_m - 1); lj >= 0; lj--) {
        lc += this.m_data[lb];
        lb -= this.m_n;
      }
      ls = Math.max(ls, lc);
    }

    return ls;
  }

  /**
   * returns the 2 norm of this matrix
   * 
   * @return maximum singular value
   */
  public double norm2() {
    return new SingularValueDecomposition(this.m_m, this.m_n, this.m_data,
        false, false).norm2();
  }

  /**
   * returns the infinity norm of this matrix
   * 
   * @return maximum row sum
   */
  public double normInf() {
    int lj, la;
    double ls, lc;

    ls = Double.NEGATIVE_INFINITY;

    for (la = this.m_m * this.m_n; la > 0;) {
      lc = 0.0;

      for (lj = (this.m_n - 1); lj >= 0; lj--) {
        lc += this.m_data[--la];
      }

      ls = Math.max(ls, lc);
    }

    return ls;
  }

  /**
   * returns the frobenius norm of this matrix
   * 
   * @return sqrt of sum of the squares of all elements
   */
  public double normFrobenius() {
    int la;
    double ls;

    ls = 0.0;
    for (la = (this.m_n * this.m_m) - 1; la >= 0; la--) {
      ls = Math.hypot(ls, this.m_data[la]);
    }

    return ls;
  }

  /**
   * compares two matrices for equality
   * 
   * @param mat
   *          the matrix to compare this matrix with
   * @return true if the two matrices are equal (or very, very close to
   *         equal), false otherwise
   */
  public boolean compare(Matrix mat) {
    int la;
    double lx, ly;
    double[] lmat;

    if (mat == this)
      return true;

    lmat = mat.m_data;

    for (la = (this.m_n * this.m_m) - 1; la >= 0; la--) {
      lx = lmat[la];
      ly = this.m_data[la];
      if (!(Mathematics.approximatelyEqual(lx, ly)))
        return false;
    }

    return true;
  }

  /**
   * Compares this matrix with an object for equality. Equality can only be
   * given if the object is also a matrix.
   * 
   * @param object
   *          the matrix to compare this matrix with
   * @return true if the two matrices are equal (or very, very close to
   *         equal), false otherwise
   */
  @Override
  public boolean equals(final Object object) {
    if (object instanceof Matrix) {
      return compare((Matrix) object);
    }

    return super.equals(object);
  }

  /**
   * returns the trace of this matrix
   * 
   * @return sum of the main diagonal elements
   */
  public double trace() {
    int li;
    double ls;

    li = (this.m_n > this.m_m) ? this.m_m : this.m_n;
    li--;
    ls = 0.0;
    for (li = ((li * this.m_n) + li); li >= 0; li -= (this.m_n + 1)) {
      ls += this.m_data[li];
    }

    return ls;
  }

  /**
   * Prints this matrix in an two-dimensional array of strings using a
   * NumberFormat- object to perform formatting. This can result in a
   * nationalized output that could be complicated to be read in another
   * country.
   * 
   * @param strings
   *          the two-dimensional array of string which is to include the
   *          textual representation of this matrix
   * @param format
   *          the format to use for printing, if it is null, the default
   *          format is used
   * @param sameLength
   *          if sameLength is true, every entry in the strings array has
   *          the same length in characters afterwards, padding with white
   *          spaces is performed if needed
   * @return the length of the longest string in the strings- array
   */
  public int toStrings(String[][] strings, NumberFormat format,
      boolean sameLength) {
    int li, lj, la, lmaxLen;
    NumberFormat f;
    String ls;
    String[] lpad;

    if (format == null)
      f = NumberFormat.getNumberInstance();
    else
      f = format;

    lmaxLen = -1;
    la = this.m_n * this.m_m;

    for (li = (this.m_m - 1); li >= 0; li--) {
      for (lj = (this.m_n - 1); lj >= 0; lj--) {
        strings[li][lj] = ls = f.format(this.m_data[--la]);
        lmaxLen = Math.max(lmaxLen, ls.length());
      }
    }

    if (sameLength) {
      la = lmaxLen - 1;
      lpad = new String[la];
      lpad[0] = " "; //$NON-NLS-1$
      ls = " ";//$NON-NLS-1$
      for (li = 1; li < la; li++) {
        lpad[li] = ls = ls + " ";//$NON-NLS-1$
      }

      for (li = (this.m_m - 1); li >= 0; li--) {
        for (lj = (this.m_n - 1); lj >= 0; lj--) {
          ls = strings[li][lj];
          if ((la = ls.length()) < lmaxLen) {
            strings[li][lj] = lpad[lmaxLen - la - 1] + ls;
          }
        }
      }
    }

    return lmaxLen;
  }

  /**
   * Prints this matrix in an two-dimensional array of strings using the
   * international (anglified) format. The result can be read in any
   * country.
   * 
   * @param strings
   *          the two-dimensional array of string which is to include the
   *          textual representation of this matrix
   * @param decimals
   *          the count of decimals for wanted in the output.
   * @param use_decimals
   *          decimals will only be used if use_decimals is true, rounding
   *          will be performed then. Otherwise, no rounding will be
   *          performed.
   * @param sameLength
   *          if sameLength is true, every entry in the strings array has
   *          the same length in characters afterwards, padding with white
   *          spaces is performed if needed
   * @return the length of the longest string in the strings- array
   */
  public int toStringsRaw(String[][] strings, int decimals,
      boolean use_decimals, boolean sameLength) {
    int li, lj, la, lmaxLen;
    double lx;
    String ls;
    String[] lpad;

    lmaxLen = -1;
    la = this.m_n * this.m_m;

    for (li = (this.m_m - 1); li >= 0; li--) {
      for (lj = (this.m_n - 1); lj >= 0; lj--) {
        lx = this.m_data[--la];
        if (use_decimals)
          lx = Mathematics.round(lx, decimals);
        strings[li][lj] = ls = Double.toString(lx);
        lmaxLen = Math.max(lmaxLen, ls.length());
      }
    }

    if (sameLength) {
      la = lmaxLen - 1;
      lpad = new String[la];
      lpad[0] = " ";//$NON-NLS-1$
      ls = " ";//$NON-NLS-1$
      for (li = 1; li < la; li++) {
        lpad[li] = ls = ls + " ";//$NON-NLS-1$
      }

      for (li = (this.m_m - 1); li >= 0; li--) {
        for (lj = (this.m_n - 1); lj >= 0; lj--) {
          ls = strings[li][lj];
          if ((la = ls.length()) < lmaxLen) {
            strings[li][lj] = lpad[lmaxLen - la - 1] + ls;
          }
        }
      }
    }

    return lmaxLen;
  }

  /**
   * internal routine for string formatting
   * 
   * @param strings
   *          The strings for the single cells
   * @param m
   *          The height of the matrix
   * @param n
   *          The width of the matrix
   * @param i
   *          The maximum length of the cell strings (??)
   * @param delimiters
   *          The delimiter char array
   * @param newLine
   *          Should newlines be placed between rows?
   * @return The string representing the matrix
   */
  private static String toString(String[][] strings, int m, int n, int i,
      String delimiters, boolean newLine) {
    int li, lj;
    char[] ldelimiters;
    StringBuffer lbuf;

    if (delimiters == null) {
      ldelimiters = DELIMITERS;
    } else {
      ldelimiters = new char[DELIMITERS.length];
      for (li = 0; li < DELIMITERS.length; li++) {
        if (delimiters.length() > li)
          ldelimiters[li] = delimiters.charAt(li);
        else
          ldelimiters[li] = DELIMITERS[li];
      }
    }

    lbuf = new StringBuffer((m * 4) + (m * n * (i + 2)) + 2);

    for (li = 0; li < m; li++) {
      if (li == 0)
        lbuf.append(ldelimiters[0]);
      else
        lbuf.append(ldelimiters[4]);
      lbuf.append(ldelimiters[0]);

      for (lj = 0; lj < n; lj++) {
        lbuf.append(ldelimiters[4]);
        lbuf.append(strings[li][lj]);
        if (lj < (n - 1))
          lbuf.append(ldelimiters[2]);
      }

      lbuf.append(ldelimiters[1]);

      if (li < (m - 1)) {
        lbuf.append(ldelimiters[3]);
        if (newLine)
          lbuf.append(TextUtils.LINE_SEPARATOR);
      }
    }
    lbuf.append(ldelimiters[1]);

    return lbuf.toString();
  }

  /**
   * Prints this matrix in a string using a NumberFormat-object to perform
   * formatting. This can result in a nationalized output that could be
   * complicated to be read in another country.
   * 
   * @param format
   *          the format to use for printing, if it is null, the default
   *          format is used
   * @param sameLength
   *          if sameLength is true, every entry in the strings array has
   *          the same length in characters afterwards, padding with white
   *          spaces is performed if needed
   * @param delimiters
   *          string of the delimiter characters to use if this string is
   *          null, the default delimiters are used: '{' for each opening
   *          row and the matrix itself '}' for each closing row and the
   *          matrix itself ',' between each element ',' between each row ' '
   *          after each ','
   * @param newLine
   *          true if newline-characters are to be inserted after each row
   *          except the last one
   * @return the string representation of this matrix
   */
  public String toString(NumberFormat format, boolean sameLength,
      String delimiters, boolean newLine) {
    String[][] lstrings;

    lstrings = new String[this.m_m][this.m_n];

    return toString(lstrings, this.m_m, this.m_n, toStrings(lstrings,
        format, sameLength), delimiters, newLine);
  }

  /**
   * Prints this matrix in a string using the international (anglified)
   * format. The result can be read in any country.
   * 
   * @param decimals
   *          the count of decimals for wanted in the output.
   * @param use_decimals
   *          decimals will only be used if use_decimals is true, rounding
   *          will be performed then. Otherwise, no rounding will be
   *          performed.
   * @param sameLength
   *          if sameLength is true, every entry in the strings array has
   *          the same length in characters afterwards, padding with white
   *          spaces is performed if needed
   * @param delimiters
   *          string of the delimiter characters to use if this string is
   *          null or does not contain 5 characters, the default delimiters
   *          are used for the missing ones: '{' for each opening row and
   *          the matrix itself '}' for each closing row and the matrix
   *          itself ',' between each element ',' between each row ' '
   *          after each ','
   * @param newLine
   *          true if newline-characters are to be inserted after each row
   *          except the last one
   * @return the string representation of this matrix
   */
  public String toStringRaw(int decimals, boolean use_decimals,
      boolean sameLength, String delimiters, boolean newLine) {
    String[][] lstrings;

    lstrings = new String[this.m_m][this.m_n];

    return toString(lstrings, this.m_m, this.m_n, toStringsRaw(lstrings,
        decimals, use_decimals, sameLength), delimiters, newLine);
  }

  /**
   * the default string representation of this matrix
   * 
   * @return this matrix as string, ready to be copy-pasted as java
   *         two-dimensional array.
   */
  @Override
  public String toString() {
    return toStringRaw(0, false, true, null, true);
  }

  /**
   * Initializes this matrix by parsing a two dimensional array of String.
   * 
   * @param strings
   *          the two dimensional array of String
   * @param format
   *          the format to use for printing, if it is null, the standard
   *          Double.parseDouble is used. normally, you'd use
   *          NumberFormat.getNumberInstance()
   */
  public void fromStrings(String[][] strings, NumberFormat format) {
    int li, lj, lk, la;

    li = strings.length;
    lj = strings[0].length;
    la = li * lj;

    if ((li != this.m_m) || (lj != this.m_n)) {
      if (la > (this.m_m * this.m_n))
        this.m_data = new double[la];

      this.m_m = li;
      this.m_n = lj;
    }

    for (--li; li >= 0; li--) {
      for (lk = (lj - 1); lk >= 0; lk--) {
        if (format != null) {
          try {
            this.m_data[--la] = format.parse(strings[li][lk])
                .doubleValue();
          } catch (Throwable lt) {
            this.m_data[la] = Double.parseDouble(strings[li][lk]);
          }
        } else
          this.m_data[--la] = Double.parseDouble(strings[li][lk]);
      }
    }
  }

  /**
   * Initializes this matrix by parsing a String.
   * 
   * @param string
   *          the String containing the matrix data
   * @param format
   *          the format to use for printing, if it is null, the standard
   *          Double.parseDouble is used. normally, you'd use
   *          NumberFormat.getNumberInstance()
   * @param delimiters
   *          string of the delimiter characters to use if this string is
   *          null or does not contain 5 characters, the default delimiters
   *          are used for the missing ones: '{' for each opening row and
   *          the matrix itself '}' for each closing row and the matrix
   *          itself ',' between each element ',' between each row ' '
   *          after each ','
   */
  public void fromString(String string, NumberFormat format,
      String delimiters) {
    int li;
    char[] ldelimiters;
    String[][] ldata;
    String[] lmeta;
    StringBuffer lbuf;
    char lc;
    boolean lkill;

    if (delimiters == null) {
      ldelimiters = DELIMITERS;
    } else {
      ldelimiters = new char[DELIMITERS.length];
      for (li = 0; li < DELIMITERS.length; li++) {
        if (delimiters.length() > li)
          ldelimiters[li] = delimiters.charAt(li);
        else
          ldelimiters[li] = DELIMITERS[li];
      }
    }

    lbuf = new StringBuffer(string.trim());

    li = lbuf.indexOf("" + ldelimiters[0]);//$NON-NLS-1$
    if (li >= 0)
      lbuf.delete(0, li + 1);
    li = lbuf.lastIndexOf("" + ldelimiters[1]);//$NON-NLS-1$
    if (li >= 0)
      lbuf.delete(li, lbuf.length());

    lkill = false;
    for (li = 0; li < lbuf.length(); li++) {
      lc = lbuf.charAt(li);
      if ((lc <= ' ') || (lc == ldelimiters[0]) || (lc == ldelimiters[4])) {
        lbuf.deleteCharAt(li);
        li--;
      } else if (lc == ldelimiters[1])
        lkill = true;
      else if ((lc == ldelimiters[3]) && lkill) {
        lbuf.deleteCharAt(li);
        lkill = false;
        li--;
      }
    }

    lmeta = lbuf.toString().split(ldelimiters[1] + ""); //$NON-NLS-1$

    ldata = new String[lmeta.length][];

    for (li = (lmeta.length - 1); li >= 0; li--) {
      ldata[li] = lmeta[li].split(ldelimiters[3] + "");//$NON-NLS-1$
    }

    fromStrings(ldata, format);
  }

}
