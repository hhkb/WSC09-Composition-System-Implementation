/*
 * Copyright (c) 2007 Stefan Niemczyk for Bachelor thesis
 * A Tunable Benchmark Problem for Genetic Programming of Algorithms
 * 
 * E-Mail           : niemczyk@uni-kassel.net
 * Creation Date    : 2007-12-29
 * Creator          : Stefan Niemczyk
 * Original Filename: org.dgpf.benchmark.TunableModel.java
 * Version          : 0.1.0
 * Last modification: 2007-12-29
 *                by: Stefan Niemczyk
 *                
 */

package org.dgpf.benchmark;

// import java.util.ArrayList;
// import java.util.Random;

/**
 * This class is a benchmark for genetic Programm. It creats testcases und
 * calculate the fitness for a given individual. It is possible to set the
 * level of difficulty for reggedness, neutrality, epistasis and affinity
 * for Overfitting. Its also possible to set the Type of Overfitting!
 * 
 * @author Stefan Niemczyk
 */
public class PreTunableModel {
  //
  // /**
  // * This is the integer value for a correct byte ( 0101 0101 ).
  // *
  // * @autor Stefan Niemczyk
  // * @since Version 0.1.0
  // */
  // private final int SOLUTION_VALUE = 85;
  //
  // /**
  // * The testcases used to evaluate a individual.
  // *
  // * @autor Stefan Niemczyk
  // * @since Version 0.1.0
  // */
  // private int[][] testcases;
  //
  // /**
  // * True if testcases are used, false if only the
  // * <code>optimalTestcase</code> is used.
  // *
  // * @autor Stefan Niemczyk
  // * @since Version 0.1.0
  // */
  // // private boolean enabledTestcaseEvaluation;
  // /**
  // * The number of testcases.
  // *
  // * @autor Stefan Niemczyk
  // * @since Version 0.1.0
  // */
  // private int testcaseSize;
  //
  // /**
  // * The noise level for the testcases. Possible values are 0 - 10. 0 No
  // * noise else (<code>testcaseSize<code> / 2) * (noiseLevel / 10)
  // *
  // * @autor Stefan Niemczyk
  // * @since Version 0.1.0
  // */
  // private int noiseLevel;
  //
  // /**
  // * The incompleteness level for the testcases. Possible values are 0 -
  // * 10. 0 No noise else (<code>testcaseSize<code> / 2) * (incompleteness
  // / 10)
  // *
  // * @autor Stefan Niemczyk
  // * @since Version 0.1.0
  // */
  // private int incompletenessLevel;
  //
  // /**
  // * @autor Stefan Niemczyk
  // * @since Version 0.1.0
  // */
  // private int ruggednessDifficultyLevel;
  //
  // /**
  // * The level of difficulty for epistasis. Possible values are 0 - 10. 0
  // * No epistasis else value Bits are mapped to other value Bits
  // *
  // * @autor Stefan Niemczyk
  // * @since Version 0.1.0
  // */
  // private int epistasisDifficultyLevel;
  //
  // /**
  // * The level of difficulty for neutrality. Possible values are 0 - 10.
  // 0
  // * No neutrality else value Bits are summon to 1 Bit. If the number of
  // 1
  // * in the Bitstring ist >= the number of 0 than the Bit is 1 else 0.
  // *
  // * @autor Stefan Niemczyk
  // * @since Version 0.1.0
  // */
  // private int neutralityDifficultyLevel;
  //
  // /**
  // * This is the number of noise-errorpositions for each testcase.
  // *
  // * @autor Stefan Niemczyk
  // * @since Version 0.1.0
  // */
  // private int internNoiseLevel;
  //
  // /**
  // * This is the number of incompleteness-errorpositions for each
  // testcase.
  // *
  // * @autor Stefan Niemczyk
  // * @since Version 0.1.0
  // */
  // private int internIncompletenessLevel;
  //
  // /**
  // * @autor Stefan Niemczyk
  // * @since Version 0.1.0
  // */
  // private int internRuggednessDifficultyLevel;
  //
  // /**
  // * @autor Stefan Niemczyk
  // * @since Version 0.1.0
  // */
  // private int internEpistasisDifficultyLevel;
  //
  // /**
  // * @autor Stefan Niemczyk
  // * @since Version 0.1.0
  // */
  // private int internNeutralityDifficultyLevel;
  //
  // /**
  // *
  // */
  // private int[] epistasisMapping;
  //
  // /**
  // *
  // */
  // private int[] ruggednessMapping;
  //
  // /**
  // * The size of the phenotype in bits!
  // *
  // * @autor Stefan Niemczyk
  // * @since Version 0.1.0
  // */
  // private int phenotypeLength;
  //
  // /**
  // * The count of functional objective functions.
  // *
  // * @autor Stefan Niemczyk
  // * @since Version 0.1.0
  // */
  // private int functionalObjectiveFunctionsCount;
  //
  // /**
  // *
  // */
  // public PreTunableModel() {
  // this.epistasisMapping = new int[1];
  //
  // try {
  // this.setNeutralityDifficultyLevel(0);
  // this.setEpistasisDifficultyLevel(0);
  // this.generateTestcases(1, 0, 0);
  // this.setRuggednessDifficultyLevel(0);
  // this.setFunctionalObjectiveFunctionsCount(1);
  // } catch (Exception e) {
  // //
  // }
  //
  // this.setPhenotypeLength(8);
  // }
  //
  // /**
  // * Write the input array <code>in</code> to the output array
  // * <code>out</code> while downsizing it to the degree of
  // * <code>internNeutralityDifficultyLevel</code>.
  // *
  // * @param in
  // * the input
  // * @param out
  // * the output
  // * @param size
  // * the number of bits
  // * @return the new number of bits
  // */
  // public final int neutrality(final byte[] in, final byte[] out,
  // final int size) {
  // int i, b, j, o, k, l, z, mu;
  //
  // o = 0;
  // j = 0;
  // b = 0;
  // k = 0;
  // l = 0;
  // z = 0;
  // mu = this.internNeutralityDifficultyLevel;
  //
  // for (i = 0; i < size; i++) {
  // if ((i & 0x7) == 0)
  // b = in[i >>> 3];
  //
  // if ((b & 1) != 0)
  // j++;
  // else
  // j--;
  // b >>>= 1;
  //
  // if ((++k) >= mu) {
  // if (j >= 0)
  // o |= (1 << l);
  // j = 0;
  // k = 0;
  // if ((++l) >= 8) {
  // l = 0;
  // out[z++] = (byte) o;
  // o = 0;
  // }
  // }
  // }
  // // if (k > 0) {
  // // if (j >= 0)
  // // o |= (1 << l);
  // // l++;
  // // }
  // if (l > 0) {
  // out[z] = (byte) o;
  // }
  //
  // return ((z << 3) + l);
  // }
  //
  // /**
  // * Write the input array <code>in</code> to the output array
  // * <code>out</code> while changing it to the degree of
  // * <code>internEpistasisDifficultyLevel</code>.
  // *
  // * @param in
  // * the input
  // * @param out
  // * the output
  // * @param size
  // * the number of bits
  // */
  // public final void epistasis(final byte[] in, final byte[] out,
  // final int size) {
  // long b, j, o, k, l, z, p, mu;
  // int i, a;
  //
  // o = 0;
  // j = 0;
  // b = 0;
  // k = 0;
  // l = 0;
  // z = 0;
  // p = 0;
  // a = 0;
  // mu = this.internEpistasisDifficultyLevel;
  //
  // for (i = 0; i < size; i++) {
  // if ((i & 0x7) == 0)
  // b = in[i >>> 3l];
  //
  // if ((b & 1) != 0)
  // o |= (1l << k);
  //
  // b >>>= 1l;
  // if ((++k) >= mu) {
  // if( mu < 21)
  // z = this.epistasisMapping[(int)o];
  // else
  // z = this.generateMapping(o, mu);
  // for (j = 0; j < mu; ++j) {
  // p |= ((z & 1l) << l);
  // z >>>= 1l;
  // if ((++l) >= 8) {
  // l = 0;
  // out[a++] = (byte) p;
  // p = 0;
  // }
  // }
  // k = 0;
  // o = 0;
  // }
  // }
  //
  // if (k != 0) {
  // z = this.generateMapping(o, k);
  //
  // for (j = 0; j < k; ++j) {
  // p |= ((z & 1l) << l);
  // z >>>= 1l;
  // if ((++l) >= 8) {
  // l = 0;
  // out[a++] = (byte) p;
  // p = 0;
  // }
  // }
  // }
  //
  // if (l != 0)
  // out[a] = (byte) p;
  // }
  //
  // /**
  // * @param in
  // * @param bits
  // * @return sdg
  // */
  // public long generateMapping(long in, long bits) {
  // long j, n, k, r, i;
  // boolean b;
  //
  // if (bits == 1)
  // return in;
  //
  // j = 0;
  // n = 1l << bits;
  // k = 0;
  // r = 0;
  //
  // if (in < (n >> 1l)) {
  // for (j = 0; j < bits; j++) {
  // b = false;
  //
  // for (k = (bits - 2l); k >= 0; k--) {
  // b ^= ((in & (1l << ((j + k) % bits))) != 0);
  // }
  // if (b)
  // r |= (1l << j);
  // }
  // return r;
  // }
  //
  // i = in - (n >> 1l);
  // for (j = 0; j < bits; j++) {
  // b = false;
  //
  // for (k = (bits - 2l); k >= 0; k--) {
  // b ^= ((i & (1l << ((j + k) % bits))) != 0);
  // }
  // if (!b)
  // r |= (1l << j);
  // }
  // return r;
  // }
  //
  // /**
  // * <xv
  // *
  // * @return jk
  // */
  // public int getNeutralityDifficultyLevel() {
  // return this.neutralityDifficultyLevel;
  // }
  //
  // /**
  // * @param p_n
  // */
  // public void setNeutralityDifficultyLevel(int p_n) {
  // // if (p_n > 10 || p_n < 0)
  // // throw new Exception(p_n + " is out of range"); //$NON-NLS-1$
  //
  // this.neutralityDifficultyLevel = p_n;
  //
  // this.internNeutralityDifficultyLevel = p_n;// + 1;
  // }
  //
  // /**
  // * @return asdf
  // */
  // public int getSOLUTION_VALUE() {
  // return this.SOLUTION_VALUE;
  // }
  //
  // /**
  // * @return asdf
  // */
  // public int getEpistasisDifficultyLevel() {
  // return this.epistasisDifficultyLevel;
  // }
  //
  // /**
  // * @param p_e
  // */
  // public void setEpistasisDifficultyLevel(int p_e) {
  // // if (p_e > 10 || p_e < 0)
  // // throw new Exception(p_e + " is out of range"); //$NON-NLS-1$
  //
  // this.epistasisDifficultyLevel = p_e;
  // this.internEpistasisDifficultyLevel = p_e;// + 1;
  //
  // if( p_e > 20 )
  // return;
  //    
  // this.epistasisMapping = new int[(1 << p_e)];// + 1)];
  // for (int i = (1<<p_e) - 1; i >= 0; --i) {
  // this.epistasisMapping[i] = (int) this.generateMapping(i, p_e);// + 1);
  // }
  // }
  //
  // /**
  // * @return regt
  // */
  // public int[][] getTestcases() {
  // return this.testcases;
  // }
  //
  // /**
  // * @param p_size
  // * @param p_noiseLevel
  // * @param p_incompletenessLevel
  // */
  // @SuppressWarnings("unchecked")
  // public void generateTestcases(int p_size, int p_noiseLevel,
  // int p_incompletenessLevel) {
  // int p, i, j, r;
  // Random random;
  // ArrayList<Integer> pos, pos2;
  // int testcase[];
  //
  // p = this.phenotypeLength;
  // i = 0;
  // j = 0;
  // r = 0;
  // random = new Random();
  //
  // this.noiseLevel = p_noiseLevel;
  // this.incompletenessLevel = p_incompletenessLevel;
  // this.internNoiseLevel = (int) ((p >> 1) * (p_noiseLevel / 10d));
  // this.internIncompletenessLevel = (int) ((p >> 1) *
  // (p_incompletenessLevel / 10d));
  //
  // this.testcaseSize = p_size;
  // this.testcases = new int[p_size][p];
  //
  // pos = new ArrayList<Integer>();
  // for (i = p - 1; i >= 0; --i)
  // pos.add(new Integer(i));
  //
  // for (i = 0; i < p_size; ++i) {
  // testcase = new int[p];
  // for (j = p - 1; j >= 0; --j)
  // testcase[j] = (j ^ 1) & 1;
  //
  // pos2 = (ArrayList<Integer>) pos.clone();
  // for (j = this.internIncompletenessLevel - 1; j >= 0; --j) {
  // r = random.nextInt(pos2.size());
  // testcase[pos2.get(r).intValue()] = 2;
  // pos2.remove(r);
  // }
  //
  // for (j = this.internNoiseLevel - 1; j >= 0; --j) {
  // r = random.nextInt(pos2.size());
  // testcase[pos2.get(r).intValue()] ^= 1;
  // pos2.remove(r);
  // }
  //
  // this.testcases[i] = testcase;
  // }
  // }
  //
  // /**
  // * @return asdf
  // */
  // public int getTestcaseSize() {
  // return this.testcaseSize;
  // }
  //
  // /**
  // * @return this.
  // */
  // public int getNoiseLevel() {
  // return this.noiseLevel;
  // }
  //
  // /**
  // * @return feh
  // */
  // public int getIncompletenessLevel() {
  // return this.incompletenessLevel;
  // }
  //
  // /**
  // * @return feg
  // */
  // public int getPhenotypeLength() {
  // return this.phenotypeLength;
  // }
  //
  // /**
  // * @param p_phenotypeLength
  // */
  // public void setPhenotypeLength(int p_phenotypeLength) {
  // this.phenotypeLength = p_phenotypeLength;
  //
  // this.generateTestcases(this.testcaseSize, this.noiseLevel,
  // this.incompletenessLevel);
  //
  // this.setRuggednessDifficultyLevel(this.ruggednessDifficultyLevel);
  // }
  //
  // /**
  // * @return ag
  // */
  // public int getFunctionalObjectiveFunctionsCount() {
  // return this.functionalObjectiveFunctionsCount;
  // }
  //
  // /**
  // * @param p_functionalObjectiveFunctionsCount
  // */
  // public void setFunctionalObjectiveFunctionsCount(
  // int p_functionalObjectiveFunctionsCount) {
  // this.functionalObjectiveFunctionsCount =
  // p_functionalObjectiveFunctionsCount;
  // }
  //
  // /**
  // * @param in
  // * @param index
  // * @param bitCount
  // * @return asdf
  // */
  // private int calculateFunktionalObjectivFitness(byte[] in, int index,
  // int bitCount) {
  // int i, j, z, o, ts, c, p, fitness;
  // int[][] tc;
  //
  // i = 0;
  // j = 0;
  // tc = this.testcases;
  // ts = this.testcaseSize;
  // o = 0;
  // z = index;
  // c = this.functionalObjectiveFunctionsCount;
  //
  // fitness = 0;
  // p = this.phenotypeLength;
  //
  // while (z < bitCount && i < p) {
  // o = (in[z >> 3] >> (z & 7)) & 1;
  //
  // for (j = 0; j < ts; ++j) {
  // if (tc[j][i] > 1)
  // continue;
  //
  // if (tc[j][i] != o)
  // ++fitness;
  //
  // // System.out.println( tc[j][i] + " " + o ); //$NON-NLS-1$
  // }
  //
  // z += c;
  // ++i;
  // }
  //
  // for (; i < this.phenotypeLength; ++i) {
  // for (j = 0; j < ts; ++j) {
  // if (tc[j][i] > 1)
  // continue;
  //
  // if (tc[j][i] != (i & 1))
  // ++fitness;
  // }
  // }
  //
  // return fitness;
  // }
  //
  // /**
  // * @param in
  // * @param index
  // * @return sdaf
  // */
  // public int funktionalObjectiveFunktion(byte[] in, int index) {
  // byte[] neutralityResult, epistasisResult;
  // int neutralityBitCount;
  //
  // if (this.neutralityDifficultyLevel != 0) {
  // neutralityResult = new byte[in.length];
  // neutralityBitCount = this.neutrality(in, neutralityResult,
  // in.length << 3);
  // } else {
  // neutralityBitCount = in.length << 3;
  // neutralityResult = in;
  // }
  //
  // if (this.epistasisDifficultyLevel != 0) {
  // epistasisResult = new byte[(neutralityBitCount >> 3) + 1];
  // this
  // .epistasis(neutralityResult, epistasisResult, neutralityBitCount);
  // } else {
  // epistasisResult = neutralityResult;
  // }
  //
  // if (index > this.functionalObjectiveFunctionsCount)
  // return -1;// TODO anstädige fehlermeldung oder anderen wert?
  //
  // int fitness;
  //
  // fitness = this.calculateFunktionalObjectivFitness(epistasisResult,
  // index, neutralityBitCount);
  //    
  // if (this.ruggednessDifficultyLevel > 0)
  // fitness = this.ruggednessMapping[fitness];
  //
  // return fitness;
  // }
  //
  // /**
  // * @param p_testcases
  // */
  // public void setTestcases(int[][] p_testcases) {
  // this.testcases = p_testcases;
  // }
  //
  //
  //
  // /**
  // * Create the r mapping table for a given gamma and m.
  // *
  // * @param gamma
  // * the gamma
  // * @param m
  // * the m value
  // * @return the lookup table
  // */
  // private final int[] createRLookupTable(final int gamma, final int m) {
  // int i;
  // int[] t1;
  //
  // t1 = new int[m + 1];
  // for (i = m; i >= 0; i--)
  // t1[i] = i;
  //
  // permutate(gamma, t1, m);
  //
  // return t1;
  // }
  //  
  // /**
  // * compute
  // *
  // * @param gamma
  // * @param list
  // * @param q
  // */
  // private final void permutate(final int gamma, final int[] list,
  // final int q) {
  // int j, k, start, max, g, lastUpper;
  // 
  // g = gamma;
  // max = (q * (q - 1)) >>> 1;
  // lastUpper = ((q >>> 1) * ((q + 1) >>> 1));
  // if (g <= lastUpper) {
  // // j = (int) (((q + 2) * .5d) - Math.sqrt((((q + 2) * (q + 2)) *
  // // .25d)
  // // - q - g));
  // 
  // j = (int) (((q + 2) * .5d) - Math.sqrt((q * q) * 0.25d + 1 - g));
  // 
  // k = (g - (q + 2) * j + (j * j) + q);
  // g = k + 1 + (((q + 2) * j - (j * j) - q - 1) << 1) - (j - 1);
  // } else {
  // 
  // // j = (int)(
  // // (((q%2)+1)*.5d)+Math.sqrt( (((q%2)+1)*((q%2)+1))*.25d - (q%2) + g
  // // -1 -lastUpper)
  // // );
  // 
  // j = (int) ((((q % 2) + 1) * .5d) + Math.sqrt((1 - (q % 2)) * 0.25d
  // + g - 1 - lastUpper));
  // 
  // k = g - (((j - (q % 2)) * (j - 1)) + 1 + lastUpper);
  // 
  // g = max - k - (2 * j * j - j) - (q % 2) * (-2 * j + 1);
  // }
  // 
  // if (g <= 0)
  // start = 0;
  // else {
  // start = q - (int) (0.5d + Math.sqrt(0.25 + ((max - g) << 1))) - 1;
  // }
  // 
  // k = 0;
  // for (j = 1; j <= start; j++) {
  // k = (j >>> 1);
  // list[j] = ((j & 1) != 0) ? (q - k) : k;
  // }
  // 
  // for (; j <= q; j++) {
  // list[j] = ((start & 1) != 0) ? (q - ++k) : ++k;
  // }
  // 
  // j = ((q - start - 1) * (q - start)) >>> 1;
  // j = max - j;
  // j = g - j;
  // 
  // basicPermutate(start + 1, list, j, q);
  // }
  //  
  // /**
  // * perform the basic permutation
  // *
  // * @param start
  // * the start
  // * @param list
  // * the list
  // * @param gamma
  // * the gamma
  // * @param q
  // * the q
  // */
  // private final void basicPermutate(final int start,
  // final int[] list, final int gamma, final int q) {
  // int i, j, t;
  // 
  // if (gamma <= 0)
  // return;
  // 
  // basicPermutate(start, list, gamma - 1, q);
  // j = q;
  // i = j - gamma;
  // t = list[i];
  // list[i] = list[j];
  // list[j] = t;
  // }
  //
  // 
  //
  // /**
  // * @return sdf
  // */
  // public int getRuggednessDifficultyLevel() {
  // return this.ruggednessDifficultyLevel;
  // }
  //
  // /**
  // * @param p_ruggednessDifficultyLevel
  // */
  // public void setRuggednessDifficultyLevel(int
  // p_ruggednessDifficultyLevel) {
  // // if (p_ruggednessDifficultyLevel > 10
  // // || p_ruggednessDifficultyLevel < 0)
  // // throw new Exception(p_ruggednessDifficultyLevel + " is out of
  // // range"); //$NON-NLS-1$
  //
  // this.ruggednessDifficultyLevel = p_ruggednessDifficultyLevel;
  // this.internRuggednessDifficultyLevel = p_ruggednessDifficultyLevel;
  //
  // this.ruggednessMapping = this.createRLookupTable(
  // this.internRuggednessDifficultyLevel,
  // (this.phenotypeLength - this.incompletenessLevel)
  // * this.testcaseSize);
  // }
  //
  // /**
  // * @param in
  // * @return asdf
  // */
  // public int nonfunktionalObjectiveFunktion(byte[] in) {
  // return in.length;
  // }
  //
  // /**
  // * @param in
  // * @return agsd
  // */
  // public boolean isOverfitted(final byte[] in) {
  // int i, j, c, o, z, size;
  // byte[] neutralityResult, epistasisResult;
  // int neutralityBitCount;
  //
  // if (this.neutralityDifficultyLevel != 0) {
  // neutralityResult = new byte[in.length];
  // neutralityBitCount = this.neutrality(in, neutralityResult,
  // in.length << 3);
  // } else {
  // neutralityBitCount = in.length << 3;
  // neutralityResult = in;
  // }
  //
  // if (this.epistasisDifficultyLevel != 0) {
  // epistasisResult = new byte[(neutralityBitCount >> 3) + 1];
  // this
  // .epistasis(neutralityResult, epistasisResult, neutralityBitCount);
  // } else {
  // epistasisResult = neutralityResult;
  // }
  //
  // c = this.functionalObjectiveFunctionsCount;
  // size = this.phenotypeLength * c;
  //
  // if ((neutralityBitCount & 7) != 0)
  // ++neutralityBitCount;
  //
  // if (size != neutralityBitCount)
  // return true;
  //
  // z = 0;
  //
  // for (i = 0; i < neutralityBitCount; ++i) {
  // o = (z & 1);
  // for (j = 0; j < c; ++j) {
  // if (((neutralityResult[i >>> 3] >>> (i & 7)) & 1) == o)
  // return true;
  // ++i;
  // }
  // --i;
  // ++z;
  // }
  //
  // return false;
  // }

  /**
   * @param bitSize
   * @param input
   * @param output
   * @return dasf
   */
  // public int neutrality2( final byte[] input,
  // final byte[] output, final int bitSize ){
  // int outputIndex, i, neutralityCount, majorityValue, shiftCount,
  // outputValue, outputValueIndex;
  // byte currentByte;
  //    
  // outputIndex = 0;
  // i = 0;
  // neutralityCount = this.internNeutralityDifficultyLevel;
  // majorityValue = 0;
  // shiftCount = 0;
  // outputValue = 0;
  // outputValueIndex = 0;
  //    
  // currentByte = 0;
  //    
  // for( i = 0; i < bitSize; ++i )
  // {
  // if( (i & 7) == 0 )
  // currentByte = input[(i>>>3)];
  //      
  // if((currentByte & 1) == 0)
  // --majorityValue;
  // else
  // ++majorityValue;
  //      
  // currentByte >>>= 1;
  //      
  // ++shiftCount;
  //      
  // if( shiftCount >= neutralityCount )
  // {
  // if( majorityValue >= 0 )
  // outputValue |= (1<<outputValueIndex);
  //        
  // shiftCount = 0;
  // majorityValue = 0;
  // ++outputValueIndex;
  //        
  // if( outputValueIndex >= 8 )
  // {
  // output[outputIndex] = (byte) outputValue;
  // ++outputIndex;
  // outputValueIndex = 0;
  // outputValue = 0;
  // }
  // }
  // }
  //    
  // if( shiftCount > 0 )
  // {
  // if( majorityValue >= 0 )
  // outputValue |= (1<<outputValueIndex);
  // ++outputValueIndex;
  // }
  //    
  // if( outputValueIndex > 0 )
  // {
  // output[outputIndex] = (byte) outputValue;
  // }
  //    
  // return (outputIndex<<3) + outputValueIndex;
  // }
}
