/*
 * Copyright (c) 2007 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 * 
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2007-12-30
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.math.matrix.Multiplication.java
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

/**
 * Matrix Multiplication With this static class matrix multiplication is
 * handled for the Matrix class. We multiply like: mxl * lxn = mxn We use
 * three basic concepts here: 1. Strassen's recursion algorithm With
 * Strassen's recursion algorithm the complexity of matrix multiplication
 * can be lowered to n^ld 7 which is something like n^2,807 (from n^3). To
 * lower memory consumption, the Winograd order of operation is used, so we
 * only need (1/3)(1-0.25^r)(m*max(l,n)+l*n) doubles for temporary
 * variables, where r is the recursion depth. 2. After the cutoff, the
 * lower boundary from where on Strassen's recursion won't increase
 * performance anymore, we use Winograd's method of the normal matrix
 * multiplication. Complexity: multiplications: 0.5 n^3 - n^2 additions : 2
 * n^3 + 3 n^2 - 2 n Two different variants are implemented, one for even
 * and one for odd l's, where the even one is slightly faster because of
 * the correction terms needed in the odd variant. 3. For l<=3, m<=3, n <=
 * 3 hard coded methods are used, since they are fastest. They also
 * decrease array access by storing reused matrix elements on the stack,
 * where the count of variables used is minimized. The code-generator for
 * these small matrix multiplicators can be found in the archive
 * /doc/matrix/code_generators.zip. It is not guaranteed that the generated
 * code will be the global optimum, however, it is a local optimum. Some
 * ideas on how Strassen's algorithm should work are taken from the DGEFMM
 * package, version 1.0, November 12, 1996, which was created for the PRISM
 * project and sponsored by an agency of the United States government. "A
 * Portable Implementation of Strassen's Algorithm" by Steven
 * Huss-Ledermann, Elaine M. Jacobson, Jeremy R. Johnson, Anna Tsao and
 * Thomas Turnbull. The operation order, memory calculation formula, the
 * fixup formula and the idea with the "dimension" (which is originally
 * FORTRAN) was taken from there. mulvec_vec_mat, mulmat_vec_vec and
 * mulmat_t_vec_vec come close to FORTRAN's dger and dgemv routines. If
 * matrix dimensions are odd, then we need to decompose them. A11, B11 and
 * C11 are then of even dimensions and can be used in Strassen's
 * algorithms.
 * 
 * <pre>
 *      |       |   |   |       |   |   |       |   |  
 *      |  A11  |a12|   |  B11  |b12|   |  C11  |c12|
 *      |       |   | X |       |   | = |       |   |
 *      |_______|___|   |_______|___|   |_______|___|
 *      |  a21  |a22|   |  b21  |b22|   |  c21  |c22|
 * </pre>
 * 
 * For the terms cut away, we need the following corrections:
 * 
 * <pre>
 *      C11 = (a12 * b21) + C11
 * 
 *  
 *          |     |    |     |     |   | b12 |   |     |
 *          | c12 | =  | A11 | a12 | X | ___ | + | c12 |
 *          |     |    |     |     |   |     |   |     |
 *                                     | b22 |
 * 
 *                                    |       |   |
 *    |     |     |   |     |     |   |  B11  |b12|   |     |     |
 *    | c21 | c22 | = | a21 | a22 | X |       |   | + | c21 | c22 |
 *    |     |     |   |     |     |   |_______|___|   |     |     |
 *                                    |  b21  |b22|
 * </pre>
 * 
 * Another important issue in this file is the power function, which allows
 * to raise matrices quickly to positive powers using the quick power
 * algorithm of Legendre (Théorie des nombres, 1798). Also, many arithmetic
 * and trigonometric operations on matrices are done here by emulating them
 * with their appropriate Taylor Serieses.
 */
public final class Multiplication {

  // public static void test()
  // {
  // double mat_a[], matB[], mat_c[], mat_d[], swap[];
  //    
  // int l, m, n, q, i, j, k;
  // long a, b, c;
  // double d;
  //    
  // for(q = 6; q < 100000; q+=5)
  // {
  // System.out.println("checking: " + q + " (" + (q-5) + "<l<" + q +")");
  // for(l = (q-5); l < q; l++)
  // {
  // for(m = 1; m < q; m++)
  // {
  // for(n = 1; n < q; n++)
  // {
  //           
  // d = l*m*n;
  // System.gc();
  // mat_a = new double[m*l];
  // System.gc();
  // matB = new double[l*n];
  // System.gc();
  // mat_c = new double[m*n];
  // System.gc();
  // mat_d = new double[m*n];
  // System.gc();
  // swap = new double[n+m+l];
  // System.gc();
  //            
  // k = 0;
  // for(i = 0; i < m; i++)
  // {
  // for(j = 0; j < l; j++)
  // {
  // mat_a[k++] = Math.round(Math.random() * d);
  // }
  // }
  //            
  // k = 0;
  // for(i = 0; i < l; i++)
  // {
  // for(j = 0; j < n; j++)
  // {
  // matB[k++] = Math.round(Math.random() * d);
  // }
  // }
  //    
  // a = System.currentTimeMillis();
  // mul(l, m, n,
  // mat_a,
  // matB,
  // mat_c );
  // b = System.currentTimeMillis();
  //    
  //      
  // muln3( l, m, n,
  // mat_a, 0, l,
  // matB, 0, n,
  // mat_d, 0, n,
  // swap);
  // c = System.currentTimeMillis();
  //            
  // mat_a = null; matB = null; swap = null;
  // System.gc();
  //            
  // k = 0;
  // for(i = 0; i < m; i++)
  // {
  // for(j = 0; j < n; j++)
  // {
  // if(mat_c[k] != mat_d[k])
  // {
  // System.out.println(" Error found: l=" + l + " m=" + m + " n=" + n);
  // System.out.println("i=" + i + " j="+j);
  // System.out.println("mat_c[i,j]=" + mat_c[k]+ " mat_d[i,j]=" + mat_d[k]
  // );
  // System.exit(0);
  // }
  // k++;
  // }
  // }
  //            
  // mat_c = null; mat_d = null;
  // System.gc();
  // }
  // }
  // }
  // }
  // }

  /**
   * The dimension sum where fast strassen multiplication will start to
   * take effect
   */
  public static final int MUL_CUTOFF = 100;

  /**
   * The l dimension where fast strassen multiplication will start to take
   * effect
   */
  public static final int MUL_L_CUTOFF = 25;

  /**
   * The m dimension where fast strassen multiplication will start to take
   * effect
   */
  public static final int MUL_M_CUTOFF = 25;

  /**
   * The n dimension where fast strassen multiplication will start to take
   * effect
   */
  public static final int MUL_N_CUTOFF = 25;

  /**
   * routine for matrix addition
   * 
   * @param m
   *          m (height) dimension of the matrices
   * @param n
   *          n (_used_width) dimension of the matrices
   * @param matA
   *          array of double containing matrix a
   * @param ofsA
   *          offset where matrix a starts in the array mat_a
   * @param dimA
   *          the dimension of a, the real width of the matrix
   * @param matB
   *          array of double containing matrix b
   * @param ofsB
   *          offset where matrix b starts in the array matB
   * @param dimB
   *          the dimension of b, the real width of the matrix
   * @param matResult
   *          array to put the resulting matrix in, can be the same as
   *          matrix a or b if ofsResult >= ofsA or ofsB
   * @param ofsResult
   *          offset where the resulting starts in the array matResult
   * @param dimResult
   *          the dimension of the resulting matrix, the real width of the
   *          matrix
   */
  private static void add(int m, int n, double[] matA, int ofsA, int dimA,
      double[] matB, int ofsB, int dimB, double[] matResult,
      int ofsResult, int dimResult) {
    int la, lb, lc, lj, da, db, dr, dm;

    da = dimA;
    db = dimB;
    dr = dimResult;
    dm = m;

    la = ofsA + (dm * da) - (da -= n) - 1;
    lb = ofsB + (dm * db) - (db -= n) - 1;
    lc = ofsResult + (dm * dr) - (dr -= n) - 1;

    for (; dm > 0; dm--) {
      for (lj = n; lj > 0; lj--) {
        matResult[lc--] = matA[la--] + matB[lb--];
      }
      la -= da;
      lb -= db;
      lc -= dr;
    }
  }

  /**
   * routine for matrix subtraction
   * 
   * @param m
   *          m (height) dimension of the matrices
   * @param n
   *          n (_used_width) dimension of the matrices
   * @param matA
   *          array of double containing matrix a
   * @param ofsA
   *          offset where matrix a starts in the array matA
   * @param dimA
   *          the dimension of a, the real width of the matrix
   * @param matB
   *          array of double containing matrix b
   * @param ofsB
   *          offset where matrix b starts in the array matB
   * @param dimB
   *          the dimension of b, the real width of the matrix
   * @param matResult
   *          array to put the resulting matrix in, can be the same as
   *          matrix a or b if ofsResult >= ofsA or ofsB
   * @param ofsResult
   *          offset where the resulting starts in the array matResult
   * @param dimResult
   *          the dimension of the resulting matrix, the real width of the
   *          matrix
   */
  private static void sub(int m, int n, double[] matA, int ofsA, int dimA,
      double[] matB, int ofsB, int dimB, double[] matResult,
      int ofsResult, int dimResult) {
    int la, lb, lc, lj, da, db, dr, dm;

    da = dimA;
    db = dimB;
    dr = dimResult;
    dm = m;

    la = ofsA + (dm * da) - (da -= n) - 1;
    lb = ofsB + (dm * db) - (db -= n) - 1;
    lc = ofsResult + (dm * dr) - (dr -= n) - 1;

    for (; dm > 0; dm--) {
      for (lj = n; lj > 0; lj--) {
        matResult[lc--] = matA[la--] - matB[lb--];
      }
      la -= da;
      lb -= db;
      lc -= dr;
    }
  }

  /**
   * internal routine for vector multiplication, performs matResult =
   * matResult + (vec_a * vec_b^T)
   * 
   * @param m
   *          m (height) dimension of the result matrix = height of vector
   *          vec_a
   * @param n
   *          n (_used_width) dimension of the result matrix = height of
   *          vector vec_b
   * @param vec_a
   *          array of double containing vector a
   * @param ofsA
   *          offset where vector a starts in the array vec_a
   * @param dimA
   *          the dimension of a, the real width of the vector
   * @param vec_b
   *          array of double containing vector b
   * @param ofsB
   *          offset where vector b starts in the array vec_b
   * @param matResult
   *          array to put the resulting matrix in
   * @param ofsResult
   *          offset where the resulting starts in the array matResult
   * @param dimResult
   *          the dimension of the resulting matrix, the real width of the
   *          matrix
   */
  private static void mulvecVecMat(int m, int n, double[] vec_a, int ofsA,
      int dimA, double[] vec_b, int ofsB, double[] matResult,
      int ofsResult, int dimResult) {
    int lj, la, or, oa, dr, dm;
    double ls;

    or = ofsResult;
    oa = ofsA;
    dr = dimResult;
    dm = m;

    or += (dm * dr) - (dr -= n) - 1;
    oa += dm * dimA;

    for (; dm > 0; dm--) {
      ls = vec_a[oa -= dimA];
      la = ofsB + n;

      for (lj = n; lj > 0; lj--) {
        matResult[or--] += ls * vec_b[--la];
      }
      or -= dr;
    }
  }

  /**
   * internal routine for matrix-vector multiplication, performs vec_result =
   * vec_result + (matA * vec_b)
   * 
   * @param m
   *          m (height) dimension of the matrix a = height of vector
   *          vec_result
   * @param n
   *          n (_used_width) dimension of the matrix a = height of vector
   *          vec_a
   * @param matA
   *          array of double containing matrix a
   * @param ofsA
   *          offset where vector a starts in the array matA
   * @param dimA
   *          the dimension of a, the real width of the matrix
   * @param vec_b
   *          array of double containing vector b
   * @param ofsB
   *          offset where vector b starts in the array vec_b
   * @param dimB
   *          the dimension of b, the real width of the vector
   * @param vec_result
   *          array to put the resulting vector in
   * @param ofsResult
   *          offset where the resulting starts in the array vec_result
   * @param dimResult
   *          the dimension of the resulting vector, the real width of the
   *          vector
   */
  private static void mulmatVecVec(int m, int n, double[] matA, int ofsA,
      int dimA, double[] vec_b, int ofsB, int dimB, double[] vec_result,
      int ofsResult, int dimResult) {
    int lj, la, or, oa, ob, da, dm;
    double ls;

    or = ofsResult;
    oa = ofsA;
    ob = ofsB;
    da = dimA;
    dm = m;

    or += dm * dimResult;
    oa += (dm * da) - (da -= n) - 1;
    ob += n * dimB;

    for (; dm > 0; dm--) {
      ls = 0.0;
      la = ob;

      for (lj = n; lj > 0; lj--) {
        ls += matA[oa--] * vec_b[la -= dimB];
      }

      vec_result[or -= dimResult] = ls;
      oa -= da;
    }
  }

  /**
   * internal routine for matrix-vector multiplication, performs
   * vec_result^T = vec_result^T + (vec_b^T * matA^T)
   * 
   * @param m
   *          m (height) dimension of the matrix a = height of vector
   *          vec_result
   * @param n
   *          n (_used_width) dimension of the matrix a = height of vector
   *          vec_a
   * @param matA
   *          array of double containing matrix a
   * @param ofsA
   *          offset where vector a starts in the array matA
   * @param dimA
   *          the dimension of a, the real width of the matrix
   * @param vec_b
   *          array of double containing vector b
   * @param ofsB
   *          offset where vector b starts in the array vec_b
   * @param vec_result
   *          array to put the resulting vector in
   * @param ofsResult
   *          offset where the resulting starts in the array vec_result
   */
  private static void mulmatTVecVec(int m, int n, double[] matA, int ofsA,
      int dimA, double[] vec_b, int ofsB, double[] vec_result,
      int ofsResult) {
    int lj, la, lb, or, oa, ob, on;
    double ls;

    or = ofsResult;
    oa = ofsA;
    ob = ofsB;
    on = n;

    or += on;
    oa += (m * dimA) - (dimA - on);
    ob += m;

    for (; on > 0; on--) {
      ls = 0.0;
      la = --oa;
      lb = ob;

      for (lj = m; lj > 0; lj--) {
        ls += matA[la] * vec_b[--lb];
        la -= dimA;
      }

      vec_result[--or] = ls;
    }
  }

  /**
   * internal routine for normal matrix multiplication this routine has
   * been replaced by mulwinograd_even and mulwinograd_odd, which were
   * faster for matrices below 60x60 in my tests.
   * 
   * @param l
   *          width of matrix a and height of matrix b
   * @param m
   *          height of matrix a = height of resulting matrix
   * @param n
   *          width of matrix b = width of resulting matrix
   * @param matA
   *          array of double containing matrix a
   * @param ofsA
   *          offset where matrix a starts in the array matA
   * @param dimA
   *          the dimension of a, the real width of the matrix
   * @param matB
   *          array of double containing matrix b
   * @param ofsB
   *          offset where matrix b starts in the array matB
   * @param dimB
   *          the dimension of b, the real width of the matrix
   * @param matResult
   *          array to put the resulting matrix in
   * @param ofsResult
   *          offset where the resulting starts in the array matResult
   * @param dimResult
   *          the dimension of the resulting matrix, the real width of the
   *          matrix
   * @param swap
   *          small swap memory used to speed up by increasing locality, l
   *          items used starting at ofset 0
   */
  /*
   * private static void muln3 (int l, int m, int n, double matA[], int
   * ofsA, int dimA, double matB[], int ofsB, int dimB, double matResult[],
   * int ofsResult, int dimResult, double swap[]) { int li, lj, lk, la,
   * lstart_a, lb, lc; double ls; lb = m - 1; lstart_a = ofsA + (lb * dimA) +
   * l; dimA -= l; lb = ofsResult + (lb * dimResult) + n; l--; ofsB += l *
   * dimB; for(lj = (n-1); lj >= 0; lj--) { lc = --lb; la = ofsB + lj;
   * for(lk = l; lk >= 0; lk--) { swap[lk] = matB[la]; la -= dimB; } la =
   * lstart_a; for(li = m; li > 0; li--) { ls = 0.0; for(lk = l; lk >= 0;
   * lk--) { ls += matA[--la] * swap[lk]; } la -= dimA; matResult[lc] = ls;
   * lc -= dimResult; } } }
   */
  /**
   * internal routine for normal matrix multiplication of _even_ l we use
   * winograds method which saves multiplications (the version of winograds
   * algorithm for even l is slightly faster than the one for odd l)
   * 
   * @param l
   *          width of matrix a and height of matrix b
   * @param m
   *          height of matrix a = height of resulting matrix
   * @param n
   *          width of matrix b = width of resulting matrix
   * @param matA
   *          array of double containing matrix a
   * @param ofsA
   *          offset where matrix a starts in the array matA
   * @param dimA
   *          the dimension of a, the real width of the matrix
   * @param matB
   *          array of double containing matrix b
   * @param ofsB
   *          offset where matrix b starts in the array matB
   * @param dimB
   *          the dimension of b, the real width of the matrix
   * @param matResult
   *          array to put the resulting matrix in
   * @param ofsResult
   *          offset where the resulting starts in the array matResult
   * @param dimResult
   *          the dimension of the resulting matrix, the real width of the
   *          matrix
   * @param swap
   *          small swap memory used to speed up by increasing locality, l
   *          items used starting at ofset 0
   */
  private static void mulwinogradEven(int l, int m, int n, double matA[],
      int ofsA, int dimA, double matB[], int ofsB, int dimB,
      double matResult[], int ofsResult, int dimResult, double swap[]) {
    int li, lj, lk, la, lb, lc, ld, le, ldim, llhalf, dr;
    double ls, lt;

    dr = dimResult;
    ldim = dimA - l;
    la = ofsA + (m * dimA) - ldim;
    llhalf = l >> 1;
    for (li = (m - 1); li >= 0; li--) {
      ls = 0.0;

      for (lj = llhalf; lj > 0; lj--)
        ls += matA[--la] * matA[--la];

      la -= ldim;
      swap[li] = ls;
    }

    la = ofsB + (l * dimB) + n;
    for (li = (n - 1); li >= 0; li--) {
      ls = 0.0;
      lb = --la;

      for (lj = llhalf; lj > 0; lj--)
        ls += matB[lb -= dimB] * matB[lb -= dimB];

      swap[li + m] = ls;
    }

    la = ofsA + (m * dimA) + l;
    le = ofsResult + (m * dr) - (dr -= n);

    for (li = (m - 1); li >= 0; li--) {
      la -= dimA;
      lc = ofsB + (l * dimB) + n;

      for (lj = (n - 1); lj >= 0; lj--) {
        ls = 0;
        lb = la;
        ld = --lc;

        for (lk = llhalf; lk > 0; lk--) {
          lt = matB[ld -= dimB];
          ls += (matA[--lb] + matB[ld -= dimB]) * (matA[--lb] + lt);
        }

        ls -= swap[li] + swap[m + lj];

        matResult[--le] = ls;
      }
      le -= dr;
    }
  }

  /**
   * internal routine for normal matrix multiplication of _odd_ l we use
   * winograds method which saves multiplications (the version of winograds
   * algorithm for even l is slightly faster than the one for odd l)
   * 
   * @param l
   *          width of matrix a and height of matrix b
   * @param m
   *          height of matrix a = height of resulting matrix
   * @param n
   *          width of matrix b = width of resulting matrix
   * @param matA
   *          array of double containing matrix a
   * @param ofsA
   *          offset where matrix a starts in the array matA
   * @param dimA
   *          the dimension of a, the real width of the matrix
   * @param matB
   *          array of double containing matrix b
   * @param ofsB
   *          offset where matrix b starts in the array matB
   * @param dimB
   *          the dimension of b, the real width of the matrix
   * @param matResult
   *          array to put the resulting matrix in
   * @param ofsResult
   *          offset where the resulting starts in the array matResult
   * @param dimResult
   *          the dimension of the resulting matrix, the real width of the
   *          matrix
   * @param swap
   *          small swap memory used to speed up by increasing locality, l
   *          items used starting at ofset 0
   */
  private static void mulwinogradOdd(int l, int m, int n, double matA[],
      int ofsA, int dimA, double matB[], int ofsB, int dimB,
      double matResult[], int ofsResult, int dimResult, double swap[]) {
    int li, lj, lk, la, lb, lc, ld, le, ldim, llhalf, dr, dl;
    double ls, lt, lu, lv;

    dl = l;
    dr = dimResult;

    dl &= ~1;
    ldim = dimA - dl;
    la = ofsA + (m * dimA) - ldim;
    llhalf = dl >> 1;
    for (li = (m - 1); li >= 0; li--) {
      ls = 0.0;

      for (lj = llhalf; lj > 0; lj--)
        ls += matA[--la] * matA[--la];

      la -= ldim;
      swap[li] = ls;
    }

    la = ofsB + (dl * dimB) + n;
    for (li = (n - 1); li >= 0; li--) {
      ls = 0.0;
      lb = --la;

      for (lj = llhalf; lj > 0; lj--)
        ls += matB[lb -= dimB] * matB[lb -= dimB];

      swap[li + m] = ls;
    }

    la = ofsA + (m * dimA) + dl;
    le = ofsResult + (m * dr) - (dr -= n);

    for (li = (m - 1); li >= 0; li--) {
      la -= dimA;
      lu = matA[la];
      lc = ofsB + (dl * dimB) + n;

      for (lj = (n - 1); lj >= 0; lj--) {
        ls = 0;
        lb = la;
        ld = --lc;
        lv = lu * matB[lc];

        for (lk = llhalf; lk > 0; lk--) {
          lt = matB[ld -= dimB];
          ls += (matA[--lb] + matB[ld -= dimB]) * (matA[--lb] + lt);
        }

        ls -= swap[li] + swap[m + lj] - lv;
        matResult[--le] = ls;
      }
      le -= dr;
    }
  }

  /**
   * internal routine to check wether strassen's recursion should be
   * performed or not
   * 
   * @param l
   *          width of matrix a and height of matrix b
   * @param m
   *          height of matrix a = height of resulting matrix
   * @param n
   *          width of matrix b = width of resulting matrix
   * @return true if recursion shall be performed, false otherwise
   */
  private static boolean muldoRecursion(int l, int m, int n) {
    int li_min, li_max;

    if (m < n) {
      li_min = m;
      li_max = n;
    } else {
      li_min = n;
      li_max = m;
    }

    if (li_min > l)
      li_min = l;
    else if (li_max < l)
      li_max = l;

    return ((((((long) l * (long) m * /* (long) */n) > ((MUL_M_CUTOFF
        * (long) n * /* (long) */l)
        + (MUL_N_CUTOFF * (long) m * /* (long) */l) + (MUL_L_CUTOFF
        * (long) n * /* (long) */m))) || (li_min > MUL_CUTOFF))) && (li_max > MUL_CUTOFF));
  }

  /**
   * internal routine for strassen's matrix multiplication Internal
   * Strassen (fmm) routine used in the recursion. It only works on
   * even-sized matrices. This is Winograd's variant of Strassen's
   * algorithm. The sequence of operations has been altered to reduce the
   * amount of temporary storage.
   * 
   * @param l
   *          width of matrix a and height of matrix b
   * @param m
   *          height of matrix a = height of resulting matrix
   * @param n
   *          width of matrix b = width of resulting matrix
   * @param matA
   *          array of double containing matrix a
   * @param ofsA
   *          offset where matrix a starts in the array matA
   * @param dimA
   *          the dimension of a, the real width of the matrix
   * @param matB
   *          array of double containing matrix b
   * @param ofsB
   *          offset where matrix b starts in the array matB
   * @param dimB
   *          the dimension of b, the real width of the matrix
   * @param matResult
   *          array to put the resulting matrix in
   * @param ofsResult
   *          offset where the resulting starts in the array matResult
   * @param dimResult
   *          the dimension of the resulting matrix, the real width of the
   *          matrix
   * @param swap
   *          small swap memory used to speed up by increasing locality, l
   *          items used starting at ofset 0
   * @param swaofs
   *          offset, from where on we can use the swap
   */
  private static void mulstrassen(int l, int m, int n, double matA[],
      int ofsA, int dimA, double matB[], int ofsB, int dimB,
      double matResult[], int ofsResult, int dimResult, double swap[],
      int swaofs) {
    int llhalf, lm_half, ln_half, la_12, la_21, la_22, lb_12, lb_21, lb_22, lresult_12, lresult_21, lresult_22, ltem1, ltem2, lnext_temp;

    if (muldoRecursion(l, m, n)) {
      // get sizes of the quaters of our matrices
      llhalf = l >> 1;
      lm_half = m >> 1;
      ln_half = n >> 1;

      // get the offsets of the submatrices
      la_12 = ofsA + llhalf;
      la_21 = ofsA + (lm_half * dimA);
      la_22 = la_21 + llhalf;

      lb_12 = ofsB + ln_half;
      lb_21 = ofsB + (llhalf * dimB);
      lb_22 = lb_21 + ln_half;

      lresult_12 = ofsResult + ln_half;
      lresult_21 = ofsResult + (lm_half * dimResult);
      lresult_22 = lresult_21 + ln_half;

      // get the temporary array's offsets
      ltem1 = swaofs;
      ltem2 = ltem1 + lm_half * ((llhalf > ln_half) ? llhalf : ln_half);
      lnext_temp = ltem2 + (llhalf * ln_half);

      /* tem1 = a11 - a21 */
      sub(lm_half, llhalf, matA, ofsA, dimA, matA, la_21, dimA, swap,
          ltem1, llhalf);

      /* tem2 = b22 - b12 */
      sub(llhalf, ln_half, matB, lb_22, dimB, matB, lb_12, dimB, swap,
          ltem2, ln_half);

      /* result_11 = tem1 * tem2 */
      mulstrassen(llhalf, lm_half, ln_half, swap, ltem1, llhalf, swap,
          ltem2, ln_half, matResult, ofsResult, dimResult, swap,
          lnext_temp);

      /* tem1 = a21 + a22 */
      add(lm_half, llhalf, matA, la_21, dimA, matA, la_22, dimA, swap,
          ltem1, llhalf);

      /* tem2 = b12 - b11 */
      sub(llhalf, ln_half, matB, lb_12, dimB, matB, ofsB, dimB, swap,
          ltem2, ln_half);

      /* result_22 = tem1 * tem2 */
      mulstrassen(llhalf, lm_half, ln_half, swap, ltem1, llhalf, swap,
          ltem2, ln_half, matResult, lresult_22, dimResult, swap,
          lnext_temp);

      /* tem1 = tem1 - a11 */
      sub(lm_half, llhalf, swap, ltem1, llhalf, matA, ofsA, dimA, swap,
          ltem1, llhalf);

      /* tem2 = b22 - tem2 */
      sub(llhalf, ln_half, matB, lb_22, dimB, swap, ltem2, ln_half, swap,
          ltem2, ln_half);

      /* result_21 = tem1 * tem2 */
      mulstrassen(llhalf, lm_half, ln_half, swap, ltem1, llhalf, swap,
          ltem2, ln_half, matResult, lresult_21, dimResult, swap,
          lnext_temp);

      /* tem1 = a_12 - tem1 */
      sub(lm_half, llhalf, matA, la_12, dimA, swap, ltem1, llhalf, swap,
          ltem1, llhalf);

      /* tem2 = b21 - tem2 */
      sub(llhalf, ln_half, matB, lb_21, dimB, swap, ltem2, ln_half, swap,
          ltem2, ln_half);

      /* result_12 = tem1 * b_22 */
      mulstrassen(llhalf, lm_half, ln_half, swap, ltem1, llhalf, matB,
          lb_22, dimB, matResult, lresult_12, dimResult, swap, lnext_temp);

      /* result_12 = result_12 + result_22 */
      add(lm_half, ln_half, matResult, lresult_12, dimResult, matResult,
          lresult_22, dimResult, matResult, lresult_12, dimResult);

      /* tem1 = a_11 * b_11 */
      mulstrassen(llhalf, lm_half, ln_half, matA, ofsA, dimA, matB, ofsB,
          dimB, swap, ltem1, ln_half, swap, lnext_temp);

      /* result_21 = result_21 + tem1 */
      add(lm_half, ln_half, matResult, lresult_21, dimResult, swap, ltem1,
          ln_half, matResult, lresult_21, dimResult);

      /* result_12 = result_12 + result_21 */
      add(lm_half, ln_half, matResult, lresult_21, dimResult, matResult,
          lresult_12, dimResult, matResult, lresult_12, dimResult);

      /* result_21 = result_21 + result_11 */
      add(lm_half, ln_half, matResult, lresult_21, dimResult, matResult,
          ofsResult, dimResult, matResult, lresult_21, dimResult);

      /* result_22 = result_22 + result_21 */
      add(lm_half, ln_half, matResult, lresult_21, dimResult, matResult,
          lresult_22, dimResult, matResult, lresult_22, dimResult);

      /* result_11 = a_12 * b_21 */
      mulstrassen(llhalf, lm_half, ln_half, matA, la_12, dimA, matB,
          lb_21, dimB, matResult, ofsResult, dimResult, swap, lnext_temp);

      /* result_11 = result_11 + tem1 */
      add(lm_half, ln_half, matResult, ofsResult, dimResult, swap, ltem1,
          ln_half, matResult, ofsResult, dimResult);

      /* tem1 = a_22 * tem2 */
      mulstrassen(llhalf, lm_half, ln_half, matA, la_22, dimA, swap,
          ltem2, ln_half, swap, ltem1, ln_half, swap, lnext_temp);

      /* result_21 = result_21 + tem1 */
      add(lm_half, ln_half, matResult, lresult_21, dimResult, swap, ltem1,
          ln_half, matResult, lresult_21, dimResult);

      // correction of missed termes
      llhalf = l & (~1);
      lm_half = m & (~1);
      ln_half = n & (~1);

      if ((l & 1) != 0) {
        mulvecVecMat(lm_half, ln_half, matA, ofsA + llhalf, dimA, matB,
            ofsB + (llhalf * dimB), matResult, ofsResult, dimResult);
      }

      if ((n & 1) != 0) {
        mulmatVecVec(lm_half, l, matA, ofsA, dimA, matB, ofsB + ln_half,
            dimB, matResult, ofsResult + ln_half, dimResult);
      }

      if ((m & 1) != 0) {
        mulmatTVecVec(l, n, matB, ofsB, dimB, matA, ofsA
            + (lm_half * dimA), matResult, ofsResult
            + (lm_half * dimResult));
      }
    } else {
      if ((l & 1) == 0) {
        mulwinogradEven(l, m, n, matA, ofsA, dimA, matB, ofsB, dimB,
            matResult, ofsResult, dimResult, swap);
      } else {
        mulwinogradOdd(l, m, n, matA, ofsA, dimA, matB, ofsB, dimB,
            matResult, ofsResult, dimResult, swap);
      }
    }
  }

  /** the static interface for fast multiplication */
  private static interface IDirectMul {
    /**
     * perform a fast matrix multiplication
     * 
     * @param matA
     *          the source matrix a data
     * @param matB
     *          the source matrix B data
     * @param result
     *          the result matrix to be filled with the multiplication's
     *          result.
     */
    public void mul(double matA[], double matB[], double result[]);
  }

  /** a list of fast multiplication instances */
  private static final IDirectMul[][][] FAST_MULTIPLIERS = {
  // l=1, that means all mx1 x 1xn multiplications
      {
      // m=1, that means all 1x1 x 1xn multiplications
          {
          // n=1, that means the 1x1 x 1x1 multiplication
              new IDirectMul() {
                public void mul(double matA[], double matB[],
                    double result[]) {
                  result[0] = (matA[0] * matB[0]);
                }
              },
              // n=2, that means the 1x1 x 1x2 multiplication
              new IDirectMul() {
                public void mul(double matA[], double matB[],
                    double result[]) {
                  double d00 = matA[0];
                  result[0] = (d00 * matB[0]);
                  result[1] = (d00 * matB[1]);
                }
              },
              // n=3, that means the 1x1 x 1x3 multiplication
              new IDirectMul() {
                public void mul(double matA[], double matB[],
                    double result[]) {
                  double d00 = matA[0];
                  result[0] = (d00 * matB[0]);
                  result[1] = (d00 * matB[1]);
                  result[2] = (d00 * matB[2]);
                }
              } },
          // m=2, that means all 2x1 x 1xn multiplications
          {
          // n=1, that means the 2x1 x 1x1 multiplication
              new IDirectMul() {
                public void mul(double matA[], double matB[],
                    double result[]) {
                  double d00 = matB[0];
                  result[0] = (matA[0] * d00);
                  result[1] = (matA[1] * d00);
                }
              },
              // n=2, that means the 2x1 x 1x2 multiplication
              new IDirectMul() {
                public void mul(double matA[], double matB[],
                    double result[]) {
                  double d00 = matA[0];
                  double d01 = matB[0];
                  result[0] = (d00 * d01);
                  double d02 = matB[1];
                  result[1] = (d00 * d02);
                  d00 = matA[1];
                  result[2] = (d00 * d01);
                  result[3] = (d00 * d02);
                }
              },
              // n=3, that means the 2x1 x 1x3 multiplication
              new IDirectMul() {
                public void mul(double matA[], double matB[],
                    double result[]) {
                  double d00 = matA[0];
                  double d01 = matB[0];
                  result[0] = (d00 * d01);
                  double d02 = matB[1];
                  result[1] = (d00 * d02);
                  double d03 = matB[2];
                  result[2] = (d00 * d03);
                  d00 = matA[1];
                  result[3] = (d00 * d01);
                  result[4] = (d00 * d02);
                  result[5] = (d00 * d03);
                }
              } },
          // m=3, that means all 3x1 x 1xn multiplications
          {
          // n=1, that means the 3x1 x 1x1 multiplication
              new IDirectMul() {
                public void mul(double matA[], double matB[],
                    double result[]) {
                  double d00 = matB[0];
                  result[0] = (matA[0] * d00);
                  result[1] = (matA[1] * d00);
                  result[2] = (matA[2] * d00);
                }
              },
              // n=2, that means the 3x1 x 1x2 multiplication
              new IDirectMul() {
                public void mul(double matA[], double matB[],
                    double result[]) {
                  double d00 = matA[0];
                  double d01 = matB[0];
                  result[0] = (d00 * d01);
                  double d02 = matB[1];
                  result[1] = (d00 * d02);
                  d00 = matA[1];
                  result[2] = (d00 * d01);
                  result[3] = (d00 * d02);
                  d00 = matA[2];
                  result[4] = (d00 * d01);
                  result[5] = (d00 * d02);
                }
              },
              // n=3, that means the 3x1 x 1x3 multiplication
              new IDirectMul() {
                public void mul(double matA[], double matB[],
                    double result[]) {
                  double d00 = matA[0];
                  double d01 = matB[0];
                  result[0] = (d00 * d01);
                  double d02 = matB[1];
                  result[1] = (d00 * d02);
                  double d03 = matB[2];
                  result[2] = (d00 * d03);
                  d00 = matA[1];
                  result[3] = (d00 * d01);
                  result[4] = (d00 * d02);
                  result[5] = (d00 * d03);
                  d00 = matA[2];
                  result[6] = (d00 * d01);
                  result[7] = (d00 * d02);
                  result[8] = (d00 * d03);
                }
              } } },
      // l=2, that means all mx2 x 2xn multiplications
      {
      // m=1, that means all 1x2 x 2xn multiplications
          {
          // n=1, that means the 1x2 x 2x1 multiplication
              new IDirectMul() {
                public void mul(double matA[], double matB[],
                    double result[]) {
                  result[0] = (matA[0] * matB[0]) + (matA[1] * matB[1]);
                }
              },
              // n=2, that means the 1x2 x 2x2 multiplication
              new IDirectMul() {
                public void mul(double matA[], double matB[],
                    double result[]) {
                  double d00 = matA[0];
                  double d01 = matA[1];
                  result[0] = (d00 * matB[0]) + (d01 * matB[2]);
                  result[1] = (d00 * matB[1]) + (d01 * matB[3]);
                }
              },
              // n=3, that means the 1x2 x 2x3 multiplication
              new IDirectMul() {
                public void mul(double matA[], double matB[],
                    double result[]) {
                  double d00 = matA[0];
                  double d01 = matA[1];
                  result[0] = (d00 * matB[0]) + (d01 * matB[3]);
                  result[1] = (d00 * matB[1]) + (d01 * matB[4]);
                  result[2] = (d00 * matB[2]) + (d01 * matB[5]);
                }
              } },
          // m=2, that means all 2x2 x 2xn multiplications
          {
          // n=1, that means the 2x2 x 2x1 multiplication
              new IDirectMul() {
                public void mul(double matA[], double matB[],
                    double result[]) {
                  double d00 = matB[0];
                  double d01 = matB[1];
                  result[0] = (matA[0] * d00) + (matA[1] * d01);
                  result[1] = (matA[2] * d00) + (matA[3] * d01);
                }
              },
              // n=2, that means the 2x2 x 2x2 multiplication
              new IDirectMul() {
                public void mul(double matA[], double matB[],
                    double result[]) {
                  double d00 = matA[0];
                  double d01 = matB[0];
                  double d02 = matA[1];
                  double d03 = matB[2];
                  result[0] = (d00 * d01) + (d02 * d03);
                  double d04 = matB[1];
                  double d05 = matB[3];
                  result[1] = (d00 * d04) + (d02 * d05);
                  d02 = matA[2];
                  double d07 = matA[3];
                  result[2] = (d02 * d01) + (d07 * d03);
                  result[3] = (d02 * d04) + (d07 * d05);
                }
              },
              // n=3, that means the 2x2 x 2x3 multiplication
              new IDirectMul() {
                public void mul(double matA[], double matB[],
                    double result[]) {
                  double d00 = matA[0];
                  double d01 = matB[0];
                  double d02 = matA[1];
                  double d03 = matB[3];
                  result[0] = (d00 * d01) + (d02 * d03);
                  double d04 = matB[1];
                  double d05 = matB[4];
                  result[1] = (d00 * d04) + (d02 * d05);
                  double d06 = matB[2];
                  double d07 = matB[5];
                  result[2] = (d00 * d06) + (d02 * d07);
                  d02 = matA[2];
                  double d09 = matA[3];
                  result[3] = (d02 * d01) + (d09 * d03);
                  result[4] = (d02 * d04) + (d09 * d05);
                  result[5] = (d02 * d06) + (d09 * d07);
                }
              } },
          // m=3, that means all 3x2 x 2xn multiplications
          {
          // n=1, that means the 3x2 x 2x1 multiplication
              new IDirectMul() {
                public void mul(double matA[], double matB[],
                    double result[]) {
                  double d00 = matB[0];
                  double d01 = matB[1];
                  result[0] = (matA[0] * d00) + (matA[1] * d01);
                  result[1] = (matA[2] * d00) + (matA[3] * d01);
                  result[2] = (matA[4] * d00) + (matA[5] * d01);
                }
              },
              // n=2, that means the 3x2 x 2x2 multiplication
              new IDirectMul() {
                public void mul(double matA[], double matB[],
                    double result[]) {
                  double d00 = matA[0];
                  double d01 = matB[0];
                  double d02 = matA[1];
                  double d03 = matB[2];
                  result[0] = (d00 * d01) + (d02 * d03);
                  double d04 = matB[1];
                  double d05 = matB[3];
                  result[1] = (d00 * d04) + (d02 * d05);
                  d02 = matA[2];
                  double d07 = matA[3];
                  result[2] = (d02 * d01) + (d07 * d03);
                  result[3] = (d02 * d04) + (d07 * d05);
                  d07 = matA[4];
                  double d09 = matA[5];
                  result[4] = (d07 * d01) + (d09 * d03);
                  result[5] = (d07 * d04) + (d09 * d05);
                }
              },
              // n=3, that means the 3x2 x 2x3 multiplication
              new IDirectMul() {
                public void mul(double matA[], double matB[],
                    double result[]) {
                  double d00 = matA[0];
                  double d01 = matB[0];
                  double d02 = matA[1];
                  double d03 = matB[3];
                  result[0] = (d00 * d01) + (d02 * d03);
                  double d04 = matB[1];
                  double d05 = matB[4];
                  result[1] = (d00 * d04) + (d02 * d05);
                  double d06 = matB[2];
                  double d07 = matB[5];
                  result[2] = (d00 * d06) + (d02 * d07);
                  d02 = matA[2];
                  double d09 = matA[3];
                  result[3] = (d02 * d01) + (d09 * d03);
                  result[4] = (d02 * d04) + (d09 * d05);
                  result[5] = (d02 * d06) + (d09 * d07);
                  d09 = matA[4];
                  double d11 = matA[5];
                  result[6] = (d09 * d01) + (d11 * d03);
                  result[7] = (d09 * d04) + (d11 * d05);
                  result[8] = (d09 * d06) + (d11 * d07);
                }
              } } },
      // l=3, that means all mx3 x 3xn multiplications
      {
      // m=1, that means all 1x3 x 3xn multiplications
          {
          // n=1, that means the 1x3 x 3x1 multiplication
              new IDirectMul() {
                public void mul(double matA[], double matB[],
                    double result[]) {
                  result[0] = (matA[0] * matB[0]) + (matA[1] * matB[1])
                      + (matA[2] * matB[2]);
                }
              },
              // n=2, that means the 1x3 x 3x2 multiplication
              new IDirectMul() {
                public void mul(double matA[], double matB[],
                    double result[]) {
                  double d00 = matA[0];
                  double d01 = matA[1];
                  double d02 = matA[2];
                  result[0] = (d00 * matB[0]) + (d01 * matB[2])
                      + (d02 * matB[4]);
                  result[1] = (d00 * matB[1]) + (d01 * matB[3])
                      + (d02 * matB[5]);
                }
              },
              // n=3, that means the 1x3 x 3x3 multiplication
              new IDirectMul() {
                public void mul(double matA[], double matB[],
                    double result[]) {
                  double d00 = matA[0];
                  double d01 = matA[1];
                  double d02 = matA[2];
                  result[0] = (d00 * matB[0]) + (d01 * matB[3])
                      + (d02 * matB[6]);
                  result[1] = (d00 * matB[1]) + (d01 * matB[4])
                      + (d02 * matB[7]);
                  result[2] = (d00 * matB[2]) + (d01 * matB[5])
                      + (d02 * matB[8]);
                }
              } },
          // m=2, that means all 2x3 x 3xn multiplications
          {
          // n=1, that means the 2x3 x 3x1 multiplication
              new IDirectMul() {
                public void mul(double matA[], double matB[],
                    double result[]) {
                  double d00 = matB[0];
                  double d01 = matB[1];
                  double d02 = matB[2];
                  result[0] = (matA[0] * d00) + (matA[1] * d01)
                      + (matA[2] * d02);
                  result[1] = (matA[3] * d00) + (matA[4] * d01)
                      + (matA[5] * d02);
                }
              },
              // n=2, that means the 2x3 x 3x2 multiplication
              new IDirectMul() {
                public void mul(double matA[], double matB[],
                    double result[]) {
                  double d00 = matA[0];
                  double d01 = matB[0];
                  double d02 = matA[1];
                  double d03 = matB[2];
                  double d04 = matA[2];
                  double d05 = matB[4];
                  result[0] = (d00 * d01) + (d02 * d03) + (d04 * d05);
                  double d06 = matB[1];
                  double d07 = matB[3];
                  double d08 = matB[5];
                  result[1] = (d00 * d06) + (d02 * d07) + (d04 * d08);
                  d04 = matA[3];
                  double d10 = matA[4];
                  double d11 = matA[5];
                  result[2] = (d04 * d01) + (d10 * d03) + (d11 * d05);
                  result[3] = (d04 * d06) + (d10 * d07) + (d11 * d08);
                }
              },
              // n=3, that means the 2x3 x 3x3 multiplication
              new IDirectMul() {
                public void mul(double matA[], double matB[],
                    double result[]) {
                  double d00 = matA[0];
                  double d01 = matB[0];
                  double d02 = matA[1];
                  double d03 = matB[3];
                  double d04 = matA[2];
                  double d05 = matB[6];
                  result[0] = (d00 * d01) + (d02 * d03) + (d04 * d05);
                  double d06 = matB[1];
                  double d07 = matB[4];
                  double d08 = matB[7];
                  result[1] = (d00 * d06) + (d02 * d07) + (d04 * d08);
                  double d09 = matB[2];
                  double d10 = matB[5];
                  double d11 = matB[8];
                  result[2] = (d00 * d09) + (d02 * d10) + (d04 * d11);
                  d04 = matA[3];
                  double d13 = matA[4];
                  double d14 = matA[5];
                  result[3] = (d04 * d01) + (d13 * d03) + (d14 * d05);
                  result[4] = (d04 * d06) + (d13 * d07) + (d14 * d08);
                  result[5] = (d04 * d09) + (d13 * d10) + (d14 * d11);
                }
              } },
          // m=3, that means all 3x3 x 3xn multiplications
          {
          // n=1, that means the 3x3 x 3x1 multiplication
              new IDirectMul() {
                public void mul(double matA[], double matB[],
                    double result[]) {
                  double d00 = matB[0];
                  double d01 = matB[1];
                  double d02 = matB[2];
                  result[0] = (matA[0] * d00) + (matA[1] * d01)
                      + (matA[2] * d02);
                  result[1] = (matA[3] * d00) + (matA[4] * d01)
                      + (matA[5] * d02);
                  result[2] = (matA[6] * d00) + (matA[7] * d01)
                      + (matA[8] * d02);
                }
              },
              // n=2, that means the 3x3 x 3x2 multiplication
              new IDirectMul() {
                public void mul(double matA[], double matB[],
                    double result[]) {
                  double d00 = matA[0];
                  double d01 = matB[0];
                  double d02 = matA[1];
                  double d03 = matB[2];
                  double d04 = matA[2];
                  double d05 = matB[4];
                  result[0] = (d00 * d01) + (d02 * d03) + (d04 * d05);
                  double d06 = matB[1];
                  double d07 = matB[3];
                  double d08 = matB[5];
                  result[1] = (d00 * d06) + (d02 * d07) + (d04 * d08);
                  d04 = matA[3];
                  double d10 = matA[4];
                  double d11 = matA[5];
                  result[2] = (d04 * d01) + (d10 * d03) + (d11 * d05);
                  result[3] = (d04 * d06) + (d10 * d07) + (d11 * d08);
                  d11 = matA[6];
                  double d13 = matA[7];
                  double d14 = matA[8];
                  result[4] = (d11 * d01) + (d13 * d03) + (d14 * d05);
                  result[5] = (d11 * d06) + (d13 * d07) + (d14 * d08);
                }
              },
              // n=3, that means the 3x3 x 3x3 multiplication
              new IDirectMul() {
                public void mul(double matA[], double matB[],
                    double result[]) {
                  double d00 = matA[0];
                  double d01 = matB[0];
                  double d02 = matA[1];
                  double d03 = matB[3];
                  double d04 = matA[2];
                  double d05 = matB[6];
                  result[0] = (d00 * d01) + (d02 * d03) + (d04 * d05);
                  double d06 = matB[1];
                  double d07 = matB[4];
                  double d08 = matB[7];
                  result[1] = (d00 * d06) + (d02 * d07) + (d04 * d08);
                  double d09 = matB[2];
                  double d10 = matB[5];
                  double d11 = matB[8];
                  result[2] = (d00 * d09) + (d02 * d10) + (d04 * d11);
                  d04 = matA[3];
                  double d13 = matA[4];
                  double d14 = matA[5];
                  result[3] = (d04 * d01) + (d13 * d03) + (d14 * d05);
                  result[4] = (d04 * d06) + (d13 * d07) + (d14 * d08);
                  result[5] = (d04 * d09) + (d13 * d10) + (d14 * d11);
                  d14 = matA[6];
                  double d16 = matA[7];
                  double d17 = matA[8];
                  result[6] = (d14 * d01) + (d16 * d03) + (d17 * d05);
                  result[7] = (d14 * d06) + (d16 * d07) + (d17 * d08);
                  result[8] = (d14 * d09) + (d16 * d10) + (d17 * d11);
                }
              } } } };

  /**
   * Main routine for matrix multiplication
   * 
   * @param l
   *          width of matrix a and height of matrix b
   * @param m
   *          height of matrix a = height of resulting matrix
   * @param n
   *          width of matrix b = width of resulting matrix
   * @param matA
   *          array of double containing matrix a
   * @param matB
   *          array of double containing matrix b
   * @param matResult
   *          array to put the resulting matrix in
   */
  public static void mul(int l, int m, int n, double matA[],
      double matB[], double matResult[]) {
    int ll2, lm2, ln2, lj;
    double lt;

    if ((l <= 3) && (m <= 3) && (n <= 3)) {
      FAST_MULTIPLIERS[l - 1][m - 1][n - 1].mul(matA, matB, matResult);
    } else {
      lj = 0;
      ll2 = l;
      lm2 = m;
      ln2 = n;

      while (muldoRecursion(ll2, lm2, ln2)) {
        ll2 = ll2 >> 1;
        lm2 = lm2 >> 1;
        ln2 = ln2 >> 1;
        lj++;
      }

      if (lj != 0) {
        lt = (1.0 / 3.0) * (1.0 - Math.pow(0.25, /* (double) */lj));
        lj = (int) Math.ceil(lt * ((l * n) + (m * ((l > n) ? l : n))));
      }

      lm2 += ln2;

      mulstrassen(l, m, n, matA, 0, l, matB, 0, n, matResult, 0, n,
          new double[lj + lm2], lm2);
    }
  }

  // ######################### other arithmetic functions
  // ###########################

  /**
   * main routine for _positive_ matrix powers
   * 
   * @param l
   *          width and height of the matrix
   * @param mat
   *          array of double containing the matrix
   * @param matResult
   *          array to put the resulting matrix in
   * @param power
   *          the power to raise the matrix to
   */
  public static void power(int power, int l, double mat[],
      double matResult[]) {
    double[] ltem1, lswap, la, lb, lc;
    IDirectMul ldm;
    int ll, lj, pp;
    double lt;

    pp = power;
    ltem1 = new double[ll = (l * l)];
    lswap = new double[ll];

    lj = pp;
    ll = 0 | (1 << 3) | (2 << 6);

    for (;;) {
      if ((lj & 1) != 0) {
        ll = (ll & 511) | ((ll & 3) << 9);
        ll = (ll & (~3)) | ((ll & (3 << 6)) >>> 6);
        ll = (ll & (~(3 << 6))) | ((ll & (3 << 9)) >>> 3);
      }

      if ((lj = (lj >> 1)) <= 0)
        break;

      ll = (ll & 511) | ((ll & (3 << 6)) << 3);
      ll = (ll & (~(3 << 6))) | ((ll & (3 << 3)) << 3);
      ll = (ll & (~(3 << 3))) | ((ll & (3 << 9)) >>> 6);
    }

    switch (ll & 3) {
    case 0: {
      la = matResult;
      lb = ltem1;
      lc = lswap;
      break;
    }
    case 1: {
      la = ltem1;
      lb = matResult;
      lc = lswap;
      break;
    }
    default: {
      la = ltem1;
      lb = lswap;
      lc = matResult;
      break;
    }
    }

    for (lj = ((ll = (l * l)) - 1); lj >= 0; lj -= l + 1)
      la[lj] = 1.0;
    System.arraycopy(mat, 0, lb, 0, ll);

    if (l <= 3) {
      ldm = FAST_MULTIPLIERS[l - 1][l - 1][l - 1];

      for (;;) {
        if ((pp & 1) != 0) {
          ldm.mul(la, lb, lc);
          ltem1 = la;
          la = lc;
          lc = ltem1;
        }

        if ((pp = (pp >> 1)) <= 0)
          break;

        ldm.mul(lb, lb, lc);
        ltem1 = lc;
        lc = lb;
        lb = ltem1;
      }

    } else {
      lj = 0;
      ll = l;

      while (muldoRecursion(ll, ll, ll)) {
        ll = ll >> 1;
        lj++;
      }

      if (lj != 0) {
        lt = (1.0 / 3.0) * (1.0 - Math.pow(0.25, /* (double) */lj));
        lj = (int) Math.ceil(lt * (2 * l * l));
      }

      ll += ll;
      lswap = new double[lj + ll];

      for (;;) {
        if ((pp & 1) != 0) {
          mulstrassen(l, l, l, la, 0, l, lb, 0, l, lc, 0, l, lswap, ll);
          ltem1 = la;
          la = lc;
          lc = ltem1;
        }

        if ((pp = (pp >> 1)) <= 0)
          break;

        mulstrassen(l, l, l, lb, 0, l, lb, 0, l, lc, 0, l, lswap, ll);
        ltem1 = lc;
        lc = lb;
        lb = ltem1;
      }
    }
  }

  /**
   * main routine for e^matrix
   * 
   * @param l
   *          width and height of the matrix
   * @param mat
   *          array of double containing the matrix
   * @param matResult
   *          array to put the resulting matrix in exp(A) = I + A + (1/2 *
   *          A^2) + (1/6 * A^3) + .... + (1/x! * A^x)
   */
  public static void exp(int l, double mat[], double matResult[]) {
    double[] la, lb, lc;
    IDirectMul ldm;
    int li, lj, ll;
    double[][] lx;
    double lfak, lmul, ls, lt;
    double[] lswap, lf;
    boolean lzero;

    li = l * l;
    lx = new double[3][li];
    la = lx[0];
    lb = lx[1];
    lc = lx[2];
    lx = null;

    for (lj = li - 1; lj >= 0; lj -= l + 1)
      la[lj] = 1.0;
    System.arraycopy(mat, 0, lb, 0, li);
    System.arraycopy(la, 0, matResult, 0, li);

    if (l <= 3) {
      ldm = FAST_MULTIPLIERS[l - 1][l - 1][l - 1];
      lswap = null;
      ll = 0;
    } else {
      ldm = null;

      lj = 0;
      ll = l;

      while (muldoRecursion(ll, ll, ll)) {
        ll = ll >> 1;
        lj++;
      }

      if (lj != 0) {
        lt = (1.0 / 3.0) * (1.0 - Math.pow(0.25, /* (double) */lj));
        lj = (int) Math.ceil(lt * (2 * l * l));
      }

      ll += ll;
      lswap = new double[lj + ll];
    }

    lfak = 1.0;
    lmul = 1.0;

    do {
      lfak /= lmul;

      if ((lfak == 0.0)/* || Double.isNaN(lfak) */)
        return;
      lmul = lmul + 1.0;

      if (ldm != null)
        ldm.mul(la, lb, lc);
      else
        mulstrassen(l, l, l, la, 0, l, lb, 0, l, lc, 0, l, lswap, ll);
      /*
       * if that happens, the next loop will fail anyway lzero = true;
       * for(li = ((l*l) - 1); li >= 0; li--) { lt = lc[li];
       * if(Double.isInfinite(lt) || Double.isNaN(lt)) return; lzero &= lt ==
       * 0.0; } if(lzero) return ;
       */

      lzero = false;
      for (li = ((l * l) - 1); li >= 0; li--) {
        lt = lc[li] * lfak;
        if (lt != 0.0) {
          ls = matResult[li];
          lt += ls;
          if (ls != lt) {
            lzero = true;
            matResult[li] = lt;
          }
        }
      }

      lf = lc;
      lc = la;
      la = lf;
    } while (lzero);
  }

  /**
   * main routine for ln matrix
   * 
   * @param l
   *          width and height of the matrix
   * @param mat
   *          array of double containing the matrix
   * @param matResult
   *          array to put the resulting matrix in ln(A) = 2[ (A-I)/(A+I) +
   *          (1/3)((A-I)/(A+I))^3) + ... ++
   *          (1/(2x+1)!)((A-I)/(A+I))^(2x+1)
   */
  public static void ln(int l, double mat[], double matResult[]) {
    double[] la, lb, lc;
    IDirectMul ldm;
    int li, lj, ll;
    double[][] lx;
    double lfak, lmul, ls, lt;
    double[] lswap, lf;
    boolean lzero;

    li = l * l;
    lx = new double[3][li];
    la = lx[0];
    lb = lx[1];
    lc = lx[2];
    lx = null;

    System.arraycopy(mat, 0, lc, 0, li);
    System.arraycopy(mat, 0, lb, 0, li);
    for (lj = li - 1; lj >= 0; lj -= l + 1)
      lc[lj] -= 1.0;
    for (lj = li - 1; lj >= 0; lj -= l + 1)
      lb[lj] += 1.0;
    new LUDecomposition(l, l, lb).invert(lb);

    if (l <= 3) {
      ldm = FAST_MULTIPLIERS[l - 1][l - 1][l - 1];
      lswap = null;
      ll = 0;
    } else {
      ldm = null;

      lj = 0;
      ll = l;

      while (muldoRecursion(ll, ll, ll)) {
        ll = ll >> 1;
        lj++;
      }

      if (lj != 0) {
        lt = (1.0 / 3.0) * (1.0 - Math.pow(0.25, lj));
        lj = (int) Math.ceil(lt * (2 * l * l));
      }

      ll += ll;
      lswap = new double[lj + ll];
    }

    if (ldm != null)
      ldm.mul(lb, lc, la);
    else
      mulstrassen(l, l, l, lb, 0, l, lc, 0, l, la, 0, l, lswap, ll);
    System.arraycopy(la, 0, matResult, 0, l * l);

    if (ldm != null)
      ldm.mul(la, la, lb);
    else
      mulstrassen(l, l, l, la, 0, l, la, 0, l, lb, 0, l, lswap, ll);
    lfak = 1.0;
    lmul = 3.0;

    do {
      lfak /= lmul;

      if (lfak == 0.0)
        return;
      lmul = lmul + 1.0;

      if (ldm != null)
        ldm.mul(la, lb, lc);
      else
        mulstrassen(l, l, l, la, 0, l, lb, 0, l, lc, 0, l, lswap, ll);

      lzero = false;
      for (li = ((l * l) - 1); li >= 0; li--) {
        lt = lc[li] * lfak;
        if (lt != 0.0) {
          ls = matResult[li];
          lt += ls;
          if (ls != lt) {
            lzero = true;
            matResult[li] = lt;
          }
        }
      }

      lf = lc;
      lc = la;
      la = lf;
    } while (lzero);

    for (li = ((l * l) - 1); li >= 0; li--) {
      lt = matResult[li];
      matResult[li] = lt + lt;
    }
  }

  /**
   * main routine for sin matrix
   * 
   * @param l
   *          width and height of the matrix
   * @param mat
   *          array of double containing the matrix
   * @param matResult
   *          array to put the resulting matrix in sin(A) = A - (A^3/3!) +
   *          (A^5/5!) - (A^7/7!) ...
   */
  public static void sin(int l, double mat[], double matResult[]) {
    double[] la, lb, lc;
    IDirectMul ldm;
    int li, lj, ll;
    double[][] lx;
    double lfak, lmul, ls, lt;
    double[] lswap, lf;
    boolean lzero, lflip;

    li = l * l;
    lx = new double[3][li];
    la = lx[0];
    lb = lx[1];
    lc = lx[2];
    lx = null;

    System.arraycopy(mat, 0, matResult, 0, li);
    System.arraycopy(mat, 0, la, 0, li);

    if (l <= 3) {
      ldm = FAST_MULTIPLIERS[l - 1][l - 1][l - 1];
      lswap = null;
      ll = 0;
    } else {
      ldm = null;

      lj = 0;
      ll = l;

      while (muldoRecursion(ll, ll, ll)) {
        ll = ll >> 1;
        lj++;
      }

      if (lj != 0) {
        lt = (1.0 / 3.0) * (1.0 - Math.pow(0.25, lj));
        lj = (int) Math.ceil(lt * (2 * l * l));
      }

      ll += ll;
      lswap = new double[lj + ll];
    }

    if (ldm != null)
      ldm.mul(la, la, lb);
    else
      mulstrassen(l, l, l, la, 0, l, la, 0, l, lb, 0, l, lswap, ll);

    lfak = 1.0;
    lmul = 3.0;
    lflip = true;

    do {
      lfak /= lmul;

      if (lfak == 0.0)
        return;
      lmul = lmul + 1.0;

      if (ldm != null)
        ldm.mul(la, lb, lc);
      else
        mulstrassen(l, l, l, la, 0, l, lb, 0, l, lc, 0, l, lswap, ll);

      lzero = false;
      for (li = ((l * l) - 1); li >= 0; li--) {
        lt = lc[li] * lfak;
        if (lt != 0.0) {
          ls = matResult[li];
          if (lflip)
            lt -= ls;
          else
            lt += ls;
          if (ls != lt) {
            lzero = true;
            matResult[li] = lt;
          }
        }
      }

      lf = lc;
      lc = la;
      la = lf;
      lflip = !lflip;
    } while (lzero);
  }

  /**
   * main routine for sin matrix
   * 
   * @param l
   *          width and height of the matrix
   * @param mat
   *          array of double containing the matrix
   * @param matResult
   *          array to put the resulting matrix in cos(A) = I - (A^2/2!) +
   *          (A^4/4!) - (A^6/6!) ...
   */
  public static void cos(int l, double mat[], double matResult[]) {
    double[] la, lb, lc;
    IDirectMul ldm;
    int li, lj, ll;
    double[][] lx;
    double lfak, lmul, ls, lt;
    double[] lswap, lf;
    boolean lzero, lflip;

    ll = l * l;
    lx = new double[3][ll];
    la = lx[0];
    lb = lx[1];
    lc = lx[2];
    lx = null;

    System.arraycopy(mat, 0, lc, 0, ll);
    for (lj = ll - 1; lj >= 0; lj -= l + 1)
      la[lj] = 1.0;
    System.arraycopy(la, 0, matResult, 0, ll);

    if (l <= 3) {
      ldm = FAST_MULTIPLIERS[l - 1][l - 1][l - 1];
      lswap = null;
      ll = 0;
    } else {
      ldm = null;

      lj = 0;
      ll = l;

      while (muldoRecursion(ll, ll, ll)) {
        ll = ll >> 1;
        lj++;
      }

      if (lj != 0) {
        lt = (1.0 / 3.0) * (1.0 - Math.pow(0.25, lj));
        lj = (int) Math.ceil(lt * (2 * l * l));
      }

      ll += ll;
      lswap = new double[lj + ll];
    }

    if (ldm != null)
      ldm.mul(lc, lc, lb);
    else
      mulstrassen(l, l, l, lc, 0, l, lc, 0, l, lb, 0, l, lswap, ll);

    lfak = 1.0;
    lmul = 2.0;
    lflip = true;

    do {
      lfak /= lmul;

      if (lfak == 0.0)
        return;
      lmul = lmul + 1.0;

      if (ldm != null)
        ldm.mul(la, lb, lc);
      else
        mulstrassen(l, l, l, la, 0, l, lb, 0, l, lc, 0, l, lswap, ll);

      lzero = false;
      for (li = ((l * l) - 1); li >= 0; li--) {
        lt = lc[li] * lfak;
        if (lt != 0.0) {
          ls = matResult[li];
          if (lflip)
            lt -= ls;
          else
            lt += ls;
          if (ls != lt) {
            lzero = true;
            matResult[li] = lt;
          }
        }
      }

      lf = lc;
      lc = la;
      la = lf;
      lflip = !lflip;
    } while (lzero);
  }

}