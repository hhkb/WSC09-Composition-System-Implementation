/*
 * Copyright (c) 2006 Thomas Weise
 *
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-01-07 15:26:24
 * Original Filename: examples.Test.java
 * Version          : 2.0.0
 * Last modification: 2006-04-10
 *                by: Thomas Weise
 *
 * License          : GNU LESSER GENERAL PUBLIC LICENSE
 *                    Version 2.1, February 1999
 *                    You should have received a copy of this license along
 *                    with this library; if not, write to the Free Software
 *                    Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 *                    MA 02111-1307, USA or download the license under
 *                    http://www.gnu.org/copyleft/lesser.html.
 *
 * Warranty         : This software is provided "as is" without any
 *                    warranty; without even the implied warranty of
 *                    merchantability or fitness for a particular purpose.
 *                    See the Gnu Lesser General Public License for more
 *                    details.
 *
 * STRONG WARNING   : This file is an experimental test file. Anything you
 *                    perform may result in unexpected, unintend or even
 *                    catastrophical results.
 */

package test.rest;

import java.util.Arrays;

import org.sfc.io.TextWriter;

/**
 * The default skeleton of a test class. This class does nothing, it is
 * just copied when a new test class is needed.
 * 
 * @author Thomas Weise
 */
public class VarietyWriteout {

  /**
   * the individuals
   */
  private static final int[][] INDIVIDUALS = new int[][] { { 1, 7 },//
      { 2, 4 },//
      { 6, 2 },//
      { 10, 1 },//
      { 1, 8 },//
      { 2, 7 },//
      { 3, 5 },//
      { 2, 9 },//
      { 3, 7 },//
      { 4, 6 },//
      { 5, 5 },//
      { 7, 3 },//
      { 8, 4 },//
      { 7, 7 },//
      { 9, 9 },//
  };

  /**
   * x
   */
  private static final int CNT = INDIVIDUALS.length;

  /**
   * the share power
   */
  private static final double SHARE_POWER = -16d;

  /**
   * the minimum share
   */
  private static final double MIN_SHARE = Math.exp(SHARE_POWER);

  /**
   * the share factor
   */
  private static final double SHARE_FACTOR = 1.0d / (1 - MIN_SHARE);

  /**
   * Perform a sharing.
   * 
   * @param value
   *          a positive value between 0 and max, the smaller the worst
   * @return the sharing value between 0 and 1 to multiply a fitness with
   *         (the larger the worst)
   */
  protected static final double share(final double value) {
    if (value <= 0d)
      return 1d;
    if (value >= 1d)
      return 0d;

    return (Math.exp(SHARE_POWER * value) - MIN_SHARE) * SHARE_FACTOR;
  }

  /**
   * The main routine.
   * 
   * @param p_args
   *          The comment line arguments.
   */
  public static void main(String[] p_args) {
    int[] rank;
    int[] min, max;
    double[] rangeScale;
    double[][] dists;
    double[][] shares;
    double[] shareV, shareS,fitness;
    int i, j;
    double a, b, d;
    TextWriter tw;
    double minShare, maxShare, shareScale,maxRank;

    rank = new int[CNT];
    for (i = (CNT - 1); i >= 0; i--) {
      for (j = (i - 1); j >= 0; j--) {
        if (((INDIVIDUALS[i][0] < INDIVIDUALS[j][0]) && (INDIVIDUALS[i][1] <= INDIVIDUALS[j][1]))
            || ((INDIVIDUALS[i][0] <= INDIVIDUALS[j][0]) && (INDIVIDUALS[i][1] < INDIVIDUALS[j][1])))
          rank[j]++;
        else if (((INDIVIDUALS[j][0] < INDIVIDUALS[i][0]) && (INDIVIDUALS[j][1] <= INDIVIDUALS[i][1]))
            || ((INDIVIDUALS[j][0] <= INDIVIDUALS[i][0]) && (INDIVIDUALS[j][1] < INDIVIDUALS[i][1])))
          rank[i]++;
      }
    }
    
    maxRank=rank[0];
    for(i=(CNT-1);i>0;i--){
      maxRank=Math.max(rank[i],maxRank);
    }

    min = new int[2];
    max = new int[2];
    rangeScale = new double[2];
    Arrays.fill(min, Integer.MAX_VALUE);
    Arrays.fill(max, Integer.MIN_VALUE);
    Arrays.fill(rangeScale, 1d);
    for (i = 0; i < INDIVIDUALS.length; i++) {
      if (INDIVIDUALS[i][0] < min[0])
        min[0] = INDIVIDUALS[i][0];
      if (INDIVIDUALS[i][0] > max[0])
        max[0] = INDIVIDUALS[i][0];

      if (INDIVIDUALS[i][1] < min[1])
        min[1] = INDIVIDUALS[i][1];
      if (INDIVIDUALS[i][1] > max[1])
        max[1] = INDIVIDUALS[i][1];
    }
    rangeScale[0] = 1d / (max[0] - min[0]);
    rangeScale[1] = 1d / (max[1] - min[1]);

    dists = new double[CNT][CNT];
    shares = new double[CNT][CNT];
    for (i = (CNT - 1); i >= 0; i--) {
      for (j = (i - 1); j >= 0; j--) {
        if (i != j) {
          a = (INDIVIDUALS[i][0] - INDIVIDUALS[j][0]) * rangeScale[0];
          b = (INDIVIDUALS[i][1] - INDIVIDUALS[j][1]) * rangeScale[1];
          d = Math.sqrt((a * a) + (b * b));
          dists[i][j] = d;
          dists[j][i] = d;
          d = share(d / Math.sqrt(2));
          shares[i][j] = d;
          shares[j][i] = d;
        }
      }
      
    }
    
    System.out.println(shares[8][14]);
    System.out.println(shares[4][13]);
    System.out.println(shares[2][8]);

    shareV = new double[CNT];
    minShare = Double.POSITIVE_INFINITY;
    maxShare = Double.NEGATIVE_INFINITY;
    
    for (i = (CNT - 1); i >= 0; i--) {
      for (j = (CNT - 1); j >= 0; j--) {
        shareV[i] += shares[i][j];
      }      
      if (shareV[i] < minShare)
        minShare = shareV[i];
      if (shareV[i] > maxShare)
        maxShare = shareV[i];
    }
    shareS = shareV.clone();
    shareScale=1d/(maxShare-minShare);
    for (i = (CNT - 1); i >= 0; i--) {
      shareS[i] = (shareV[i] - minShare) *shareScale;
    }
    
    fitness=new double[CNT];
    for(i=(CNT-1);i>=0;i--){
      if(rank[i]<=0) fitness[i]=shareS[i];
      else fitness[i]=rank[i]+shareS[i]*Math.sqrt(maxRank);
    }

    tw = new TextWriter("E:\\1.txt"); //$NON-NLS-1$

    for (i = 0; i < CNT; i++) {
      tw.ensureNewLine();

      tw.write("\\dsRow{");//$NON-NLS-1$
      tw.writeInt(i + 1);
      tw.write("}\\dsCell{");//$NON-NLS-1$

      tw.writeInt(INDIVIDUALS[i][0]);
      tw.write("}\\dsCell{$");//$NON-NLS-1$
      tw.writeInt(INDIVIDUALS[i][1]);
      tw.write("$}\\dsCell{$");//$NON-NLS-1$

      tw.writeInt(rank[i]);
      tw.write("$}\\dsCell{");//$NON-NLS-1$

      tw.write(toStr(shareV[i],3,true));
      tw.write("}\\dsCell{");//$NON-NLS-1$
      
      tw.write(toStr(shareS[i],3,true));
      tw.write("}\\dsCell{");//$NON-NLS-1$
      

      tw.write(toStr(fitness[i],3,true));
      tw.write("}%");//$NON-NLS-1$    
    }
    
    tw.ensureNewLine();
    tw.newLine();
    tw.newLine();
    String s;
    
    for(i=1;i<=CNT;i++){
      tw.write("&\\dsHead{"); //$NON-NLS-1$
      tw.writeInt(i); 
      tw.write("}"); //$NON-NLS-1$
    }
    tw.write("\\\\%");//$NON-NLS-1$
    tw.ensureNewLine();
    tw.write("\\dsTableBody%");//$NON-NLS-1$
    for (i = 0; i < CNT; i++) {
      tw.ensureNewLine();
      tw.write("\\dsRow{\\dsHeadFormat{"+(i+1)+ //$NON-NLS-1$
          "}}"); //$NON-NLS-1$

      
      for(j=0; j < CNT; j++){
        if(j<i){
          s = toStr(shares[i][j],3,false);
        }
        else if(j>i){
          s=toStr(dists[i][j],3,false);
        }
        else s=""; //$NON-NLS-1$
        
        if(s!=""){ //$NON-NLS-1$
          s = "$\\scriptstyle{" +s +//$NON-NLS-1$
          "}$";//$NON-NLS-1$        
        }
        
      tw.write("\\dsCell{");//$NON-NLS-1$
      tw.write(s);
      tw.write("}");//$NON-NLS-1$
      }
    }

    tw.ensureNewLine();
    tw.newLine();
    tw.newLine();
    for (i = 0; i <= 1; i++) {
      tw.ensureNewLine();
      tw.write("min f"); //$NON-NLS-1$
      tw.writeInt(i + 1);
      tw.write(": ");//$NON-NLS-1$
      tw.writeInt(min[i]);
      tw.ensureNewLine();
      tw.write("max f"); //$NON-NLS-1$
      tw.writeInt(i + 1);
      tw.write(": ");//$NON-NLS-1$
      tw.writeInt(max[i]);
      tw.ensureNewLine();
      tw.write("rangeScale f"); //$NON-NLS-1$
      tw.writeInt(i + 1);
      tw.write(": ");//$NON-NLS-1$
      tw.writeDouble(rangeScale[i]);
    }

    tw.ensureNewLine();
    tw.newLine();
    tw.write("minShare: ");//$NON-NLS-1$
    tw.writeDouble(minShare);
    tw.newLine();
    tw.write("maxShare: ");//$NON-NLS-1$
    tw.writeDouble(maxShare);
    tw.newLine();
    tw.write("shareScale: ");//$NON-NLS-1$
    tw.writeDouble(shareScale);
    
    tw.release();
  }

  /**
   * stringify
   * 
   * @param a
   *          the number
   * @param scale
   * @param add
   * @return the result
   */
  private static final String toStr(final double a, final int scale,final boolean add) {
    double d;
    int v;
    String s;

    v = (int) Math.round(Math.log10(a));
    if (v < -scale) {

      v = scale - v;

      d = Math.rint(a * Math.rint(Math.pow(10d, v))) * Math.pow(10d, -v);
      s = String.valueOf(d);
      if(s.contains("E")){//$NON-NLS-1$
      s=s.replace("E-",//$NON-NLS-1$
          "\\textrm{E-}"//$NON-NLS-1$
          );
      }
//      if (d != a)
//        s = "\\approx " + s; //$NON-NLS-1$
    } else {
      d = Math.rint(a * Math.rint(Math.pow(10d, scale)))
          * Math.pow(10d, -scale);
      s = String.valueOf(d);
      if (s.length() > (scale + Math.max(v,1)+1))
        s = s.substring(0, (scale + Math.max(v,1)+1));
      while (s.charAt(s.length() - 1) == '0')
        s = s.substring(0, s.length() - 1);
      while (s.charAt(s.length() - 1) == '.')
        s = s.substring(0, s.length() - 1);
//      if (d != a)
//        s = "\\approx " + s; //$NON-NLS-1$
    }

    
    s=s.replace("0.0031199999999999995",//$NON-NLS-1$
        "0.003");//$NON-NLS-1$
    
    s=s.replace("0.0031199999999999997",//$NON-NLS-1$
    "0.003");//$NON-NLS-1$
    
    s=s.replace("7.999999999999999",//$NON-NLS-1$
    "8");//$NON-NLS-1$
    
    s=s.replace("5.6999999999999996",//$NON-NLS-1$
    "5.7");//$NON-NLS-1$
    
    s=s.replace("0.0030399999999999997",//$NON-NLS-1$
    "0.003");//$NON-NLS-1$
    
    s=s.replace("0.0015899999999999998",//$NON-NLS-1$
    "0.002");//$NON-NLS-1$
    
    s=s.replace("1.6999999999999999",//$NON-NLS-1$
    "1.7");//$NON-NLS-1$
    
    s=s.replace("2.8499999999999998",//$NON-NLS-1$
    "2.8");//$NON-NLS-1$
    
    s=s.replace("2.4999999999999998",//$NON-NLS-1$
    "2.5");//$NON-NLS-1$

    s=s.replace("2.4999999999999998",//$NON-NLS-1$
    "2.5");//$NON-NLS-1$
    
    s=s.replace("7.599999999999999",//$NON-NLS-1$
    "7.6");//$NON-NLS-1$
    
    s=s.replace("2.88999999999999988",//$NON-NLS-1$
    "2.9");//$NON-NLS-1$
    
    
    s=s.replace("1.5099999999999998",//$NON-NLS-1$
    "1.5");//$NON-NLS-1$
    
    s=s.replace("0.0030399999999999997",//$NON-NLS-1$
    "0.003");//$NON-NLS-1$
    
    s=s.replace("0.0031199999999999995",//$NON-NLS-1$
    "0.003");//$NON-NLS-1$
    
    s=s.replace("7.999999999999999",//$NON-NLS-1$
    "8");//$NON-NLS-1$
   

    
    s=s.replace("5.099999999999999",//$NON-NLS-1$
    "5.1");//$NON-NLS-1$

    
    s=s.replace("0.0020499999999999997",//$NON-NLS-1$
    "0.002");//$NON-NLS-1$

    
    s=s.replace("4.5999999999999996",//$NON-NLS-1$
    "4.6");//$NON-NLS-1$
    
    s=s.replace("0.0018599999999999999",//$NON-NLS-1$
    "0.002");//$NON-NLS-1$
   

    s=s.replace("0.0010199999999999999",//$NON-NLS-1$
    "0.001");//$NON-NLS-1$
   

    s=s.replace("3.1999999999999997",//$NON-NLS-1$
    "3.2");//$NON-NLS-1$
   

    s=s.replace("2.8899999999999998",//$NON-NLS-1$
    "2.9");//$NON-NLS-1$
   

    s=s.replace("7.769999999999999",//$NON-NLS-1$
    "7.7");//$NON-NLS-1$
   

    s=s.replace("2.5830000000000002",//$NON-NLS-1$
    "2.6");//$NON-NLS-1$
   

    s=s.replace("1.8170000000000001",//$NON-NLS-1$
    "1.8");//$NON-NLS-1$
   

    s=s.replace("1.8170000000000001",//$NON-NLS-1$
    "1.8");//$NON-NLS-1$
   

    s=s.replace("1.4950000000000001",//$NON-NLS-1$
    "1.5");//$NON-NLS-1$
   

    s=s.replace("1.541",//$NON-NLS-1$
    "1.54");//$NON-NLS-1$
    
    


    s=s.replace("2.5830000000000002",//$NON-NLS-1$
    "2.6");//$NON-NLS-1$
   

    s=s.replace("1.8170000000000001",//$NON-NLS-1$
    "1.8");//$NON-NLS-1$
   

    s=s.replace("1.7429999999999998",//$NON-NLS-1$
    "1.74");//$NON-NLS-1$
   

    s=s.replace("9.669999999999999",//$NON-NLS-1$
    "9.7");//$NON-NLS-1$
    
    


    s=s.replace("1.807",//$NON-NLS-1$
    "1.8");//$NON-NLS-1$
   

    s=s.replace("1.833",//$NON-NLS-1$
    "1.8");//$NON-NLS-1$
   

    s=s.replace("2.852",//$NON-NLS-1$
    "2.9");//$NON-NLS-1$
   

    s=s.replace("1.54",//$NON-NLS-1$
    "1.5");//$NON-NLS-1$
   
   



    s=s.replace("3.22",//$NON-NLS-1$
    "3.2");//$NON-NLS-1$
   

    s=s.replace("2.125",//$NON-NLS-1$
    "2.1");//$NON-NLS-1$
   

    s=s.replace("2.125",//$NON-NLS-1$
    "2.1");//$NON-NLS-1$
   

    s=s.replace("5.71",//$NON-NLS-1$
    "5.7");//$NON-NLS-1$
   
   



    s=s.replace("1.74",//$NON-NLS-1$
    "1.7");//$NON-NLS-1$
   

    s=s.replace("9.26",//$NON-NLS-1$
    "9.3");//$NON-NLS-1$
   

    s=s.replace("2.893",//$NON-NLS-1$
    "2.9");//$NON-NLS-1$
   

    s=s.replace("1.24",//$NON-NLS-1$
    "1.2");//$NON-NLS-1$
   
   



    s=s.replace("2.497",//$NON-NLS-1$
    "2.5");//$NON-NLS-1$
   

    s=s.replace("1.127",//$NON-NLS-1$
    "1.1");//$NON-NLS-1$
   

    s=s.replace("3.87",//$NON-NLS-1$
    "3.9");//$NON-NLS-1$
   

    s=s.replace("8.03",//$NON-NLS-1$
    "8");//$NON-NLS-1$
   


    s=s.replace("6.09",//$NON-NLS-1$
    "6.1");//$NON-NLS-1$


    s=s.replace("1.507",//$NON-NLS-1$
    "1.5");//$NON-NLS-1$
   


    s=s.replace("1.704",//$NON-NLS-1$
    "1.7");//$NON-NLS-1$
    s=s.replace("1.433",//$NON-NLS-1$
    "1.4");//$NON-NLS-1$
    
    if(!add) return s;
    return "$" + //$NON-NLS-1$
        s + "$";//$NON-NLS-1$
  }

}
