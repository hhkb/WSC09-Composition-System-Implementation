/**
 * Copyright (c) 2008 Stefan Niemczyk for Bachelor thesis
 * A Tunable Benchmark Problem for Genetic Programming of Algorithms
 * 
 * E-Mail           : niemczyk@uni-kassel.net
 * Creation Date    : 2008-01-26
 * Creator          : Stefan Niemczyk
 * Original Filename: org.dgpf.benchmark.TunableModel.java
 * Version          : 1.0.1
 * Last modification: 2008-01-31
 *                by: Stefan Niemczyk
 *                
 */

package org.dgpf.benchmark;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class is a tunable Model for genetic Programming. It is possible to
 * set the level of difficulty for neutrality, ruggedness, epistasis,
 * multi-objectivity and testcases. This class contains methods to set the
 * level of difficulty direct or by a scale from 0 to 10, to calculate the
 * fitness for a given individual and to identify correct and overfitted
 * individual.
 * 
 * @author Stefan Niemczyk
 */
public class TunableModel {

  /**
   * The integer value from a correct byte. (0101 0101)
   * 
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  private final int SOLUTION_VALUE = 85;

  /**
   * The testcases to evaluate a individual.
   * 
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  private int[][] testCases;

  /**
   * The number of testscases.
   * 
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  private int testCaseCount;

  /**
   * The noise level of difficulty means how many position in each testcase
   * are wrong on a scale from 0 - 10. 0 no noise 10 50% of the positions
   * in each testcase are wrong
   * 
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  private double noiseDifficultyLevel;

  /**
   * The internal noise level of difficulty means the count of wrong
   * positions in each testcase.
   * 
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  private int internalNoiseDifficultyLevel;

  /**
   * The incompleteness level of difficulty means how many positions in
   * each testcase are do not care on a scale from 0 - 10. 0 no
   * incompleteness 10 50% of the positions in each testcase are
   * incompleted
   * 
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  private double incompletenessDifficultyLevel;

  /**
   * The internal incompleteness level of difficulty means the count of
   * incompleted positions in each testcase.
   * 
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  private int internalIncompletenessDifficultyLevel;

  /**
   * The <code>neutralityDifficultyLevel</code> is the level of
   * neutrality on a scale from 0 - 10. 0 no neutrality 10 maximum level of
   * neutrality
   * 
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  private double neutralityDifficultyLevel;

  /**
   * The count of bits merged to 1 bit.
   * 
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  private int mu;

  /**
   * The level of epistasis on a scale from 0 - 10. 0 no epistasis 10
   * maximum level of epistasis
   * 
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  private double epistasisDifficultyLevel;

  /**
   * The count of bits which are set as 1 block. Each block are mapped to
   * another bitstring of the same length. This mapping is one-to-one.
   * 
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  private int eta;

  /**
   * The level of ruggedness on a scale from 0 - 10. If
   * <code>deceptive</code> is <code>true</code> 
   * <code>ruggednessDifficultyLevel</code>
   * means the level of the deceptive. 0 no ruggedness/deceptive 10 maximum
   * level of ruggedness/deceptive.
   * 
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  private double ruggednessDifficultyLevel;

  /**
   * <code>true</code> if and only if the ruggedness is deceptive, else
   * <code>false</code>. Default is <code>false</code>!
   * 
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  private boolean deceptive;

  /**
   * The internal reggedness value to generate the
   * <code>ruggednessMapping</code> list.
   * 
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  private int gamma;

  /**
   * The count of functional objective functions.
   * 
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  private int functionalObjectiveFunctionCount;

  /**
   * The mapping to modifiy the functional fitness value for a individual.
   * 
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  private int[] ruggednessMapping;

  /**
   * The mapping to modifiy the bitblock defined by the
   * <code>eta</code>. The mapping is only used when <code>eta</code>
   * is <= 21.
   * 
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  private int[] epistasisMapping;

  /**
   * The bitlength of the phenotype!!
   * 
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  private int phenotypeLength;

  /**
   * The minimum functional fitness value to say an individual is success.
   * 
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  private int minimumFitnessToSuccess;
  
  /**
   * The maximum functional fitness value.
   * 
   * @autor Stefan Niemczyk
   * @since Version 1.0.1
   */
  private int maximumFitness;
  
  /**
   * True if and only if the neutrality level was changed, else false.
   * 
   * @autor Stefan Niemczyk
   * @since Version 1.0.1
   */
  private boolean neutralityLevelChanged;
  
  /**
   * True if and only if the epistasis level was changed, else false.
   * 
   * @autor Stefan Niemczyk
   * @since Version 1.0.1
   */
  private boolean epistasisLevelChanged;
  
  /**
   * True if and only if the ruggedness level was changed, else false.
   * 
   * @autor Stefan Niemczyk
   * @since Version 1.0.1
   */
  private boolean ruggednessLevelChanged;
  
  /**
   * True if and only if the testcases was changed, else false.
   * 
   * @autor Stefan Niemczyk
   * @since Version 1.0.1
   */
  private boolean testcasesChanged;
  
  /**
   * True if and only if the phenotype length was changed, else false.
   * 
   * @autor Stefan Niemczyk
   * @since Version 1.0.1
   */
  private boolean phenotypeLengthChanged;
  
  /**
   * True if and only if the neutrality level was set direct, else false.
   * 
   * @autor Stefan Niemczyk
   * @since Version 1.0.1
   */
  private boolean neutralitySetDirect;
  
  /**
   * True if and only if the epistasis level was set direct, else false.
   * 
   * @autor Stefan Niemczyk
   * @since Version 1.0.1
   */
  private boolean epistasisSetDirect;
  
  /**
   * True if and only if the ruggedness level was set direct, else false.
   * 
   * @autor Stefan Niemczyk
   * @since Version 1.0.1
   */
  private boolean ruggednessSetDirect;
  
  /**
   * True if and only if the noise level was set direct, else false.
   * 
   * @autor Stefan Niemczyk
   * @since Version 1.0.1
   */
  private boolean noiseSetDirect;
  
  /**
   * True if and only if the incompleteness level was set direct, else false.
   * 
   * @autor Stefan Niemczyk
   * @since Version 1.0.1
   */
  private boolean incompletenessSetDirect;


  // --------------------------------

  /**
   * The Basic consturctor. Initialize all fields.
   * 
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public TunableModel() {
    this.setNeutralityDifficultyLevel(0d);
    this.setEpistasisDifficultyLevel(0d);
    this.setRuggednessDifficultyLevel(0d);
    this.setDeceptive(false);

    this.setTestCaseCount(1);
    this.setNoiseDifficultyLevel(0d);
    this.setIncompletenessDifficultyLevel(0d);

    this.setFunctionalObjectiveFunctionCount(1);

    this.setPhenotypeLength(8);
    
    this.recalculateInternalValues();
  }

  // --------------------------------

  /**
   * Generate testcases with a given number of wrong and incompeted
   * positions.
   * 
   * @param p_size
   *          The number of testcases.
   * @param p_noiseLevel
   *          The number of wrong positions in each testcase.
   * @param p_incompletenessLevel
   *          The number of incompleted positions in each testcase.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  @SuppressWarnings("unchecked")
  private void generateTestcases(int p_size, int p_noiseLevel,
      int p_incompletenessLevel) {
    int p, i, j, r, ffc, max, one, zero;
    Random random;
    ArrayList<Integer> pos, pos2;
    int testcase[];

    p = this.phenotypeLength;
    i = 0;
    j = 0;
    r = 0;
    random = new Random();

    this.testCaseCount = p_size;
    this.testCases = new int[p_size][p];

    pos = new ArrayList<Integer>();
    for (i = p - 1; i >= 0; --i)
      pos.add(new Integer(i));

    for (i = 0; i < p_size; ++i) {
      testcase = new int[p];
      for (j = p - 1; j >= 0; --j)
        testcase[j] = (j ^ 1) & 1;

      pos2 = (ArrayList<Integer>) pos.clone();
      for (j = this.internalIncompletenessDifficultyLevel - 1; j >= 0; --j) {
        r = random.nextInt(pos2.size());
        testcase[pos2.get(r).intValue()] = 2;
        pos2.remove(r);
      }

      for (j = this.internalNoiseDifficultyLevel - 1; j >= 0; --j) {
        r = random.nextInt(pos2.size());
        testcase[pos2.get(r).intValue()] ^= 1;
        pos2.remove(r);
      }

      this.testCases[i] = testcase;
    }

    byte[] correctSolution = new byte[(this.phenotypeLength >> 3) + 1];
    for (i = correctSolution.length - 1; i >= 0; --i)
      correctSolution[i] = (byte) this.SOLUTION_VALUE;

    ffc = this.functionalObjectiveFunctionCount;
    this.functionalObjectiveFunctionCount = 1;

    this.minimumFitnessToSuccess = this
        .calculateFunctionalObjectivFitness(correctSolution, 0,
            this.phenotypeLength);
    
    max = 0;
    
    for( i = 0; i < p; ++i )
    {
      one = 0;
      zero = 0;
      
      for( j = 0; j < p_size; ++j )
      {
        if( this.testCases[j][i] == 1)
          ++one;
        else if( this.testCases[j][i] == 0 )
          ++zero;
      }
      
      max += Math.max(one, zero);
    }
    
    this.maximumFitness = max;

    this.functionalObjectiveFunctionCount = ffc;
  }

  /**
   * Generates the epistasismapping. Possible for <code>eta</code> lower 
   * than 22.
   * 
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  private void generateEpistasisMapping() {
    int p_e;

    p_e = this.eta;
    this.epistasisMapping = new int[(1 << p_e)];

    for (int i = (1 << p_e) - 1; i >= 0; --i) {
      this.epistasisMapping[i] = (int) generateEpistasisMapping(i, p_e);
    }
  }

  /**
   * Caculate the epistasis mapping for a bitstring coded as long. Returns
   * the result coded as long. This methode is possible for bitstring with
   * a maximum length from 63.
   * 
   * @param in
   *          The input bitstring coded as long.
   * @param bits
   *          The count of bits (bitstring length).
   * @return Returns the calculated bitstring coded as long.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public static long generateEpistasisMapping(long in, long bits) {
    long j, n, k, r, i;
    boolean b;

    if (bits == 1)
      return in;

    j = 0;
    n = 1l << bits;
    k = 0;
    r = 0;

    if (in < (n >> 1l)) {
      for (j = 0; j < bits; j++) {
        b = false;

        for (k = (bits - 2l); k >= 0; k--) {
          b ^= ((in & (1l << ((j + k) % bits))) != 0);
        }
        if (b)
          r |= (1l << j);
      }
      return r;
    }

    i = in - (n >> 1l);
    for (j = 0; j < bits; j++) {
      b = false;

      for (k = (bits - 2l); k >= 0; k--) {
        b ^= ((i & (1l << ((j + k) % bits))) != 0);
      }
      if (!b)
        r |= (1l << j);
    }
    return r;
  }

  /**
   * Caculate the epistasis mapping for a bitstring coded as long. Returns
   * the result coded as long. This methode is possible for bitstring with
   * a maximum length from 63.
   * 
   * @param in
   *          The input bitstring coded as bytearray.
   * @param bits
   *          The count of bits (bitstring length).
   * @return Returns the calculated bitstring coded as long.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public static byte[] generateEpistasisMapping(byte[] in, long bits) {
    long j, k, r;
    int z, p;
    boolean bool;
//    byte[] br;
    byte b;

    if (bits == 1)
      return in;

    j = 0;
    k = 0;
    r = 0;
    b = 0;
    z = 0;
    p = 0;

//    br = new byte[(int) ((bits >>> 3) + 1)];
    if ((in[(int) ((bits - 1) >>> 3)] & (1 << ((bits - 1) & 7))) == 0) {
      for (j = 0; j < bits; j++) {
        bool = false;

        for (k = (bits - 2l); k >= 0; k--) {
          r = ((j + k) % bits);
          bool ^= ((in[(int) (r >>> 3)] & (1 << (r & 7))) != 0);
        }
        if (bool)
          b |= (1l << p);
//          br[(int) (j >>> 3)] |= (1l << (j & 7));
        
        if( ++p >= 8 )
        {
          in[z++] = b;
          b = 0;
          p = 0;
        }
      }
      return in;
    }

    in[(int) ((bits - 1) >> 3)] ^= (1 << ((bits - 1) & 7));
    for (j = 0; j < bits; j++) {
      bool = false;

      for (k = (bits - 2l); k >= 0; k--) {
        r = ((j + k) % bits);
        bool ^= ((in[(int) (r >>> 3)] & (1 << (r & 7))) != 0);
      }
      if (!bool)
        b |= (1l << p);
//        br[(int) (j >>> 3)] |= (1l << (j & 7));
      
      if( ++p >= 8 )
        {
          in[z++] = b;
          b = 0;
          p = 0;
        }
    }
    return in;
  }

  /**
   * Create the ruggedness mapping table for a given gamma and m.
   * 
   * @param ruggednessLevel
   *          The ruggedness level of difficulty.
   * @param max
   *          The maximum level of difficulty.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  private void generateRuggednessMapping(final int ruggednessLevel,
      final int max) {
    int i;
    int[] t1;

    t1 = new int[max + 1];
    for (i = max; i >= 0; i--)
      t1[i] = i;

    permutate(ruggednessLevel, t1, max - this.minimumFitnessToSuccess);
    
    for( i =  max; i >= this.minimumFitnessToSuccess; --i )
      t1[i] = t1[i - this.minimumFitnessToSuccess] + this.minimumFitnessToSuccess;
    for( i = 0; i <= this.minimumFitnessToSuccess; ++i )
      t1[i] = i;  

    this.ruggednessMapping = t1;
  }

  /**
   * Compute the ruggedness mapping lookup table.
   * 
   * @param ruggednessLevel
   *          The ruggedness level from this mapping.
   * @param mapping
   *          The lookup table for the mapping.
   * @param maximumValue
   *          The maximum fitness value.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  private static void permutate(final int ruggednessLevel, final int[] mapping,
      final int maximumValue) {
    int j, k, start, max, g, lastUpper;
   
   g = ruggednessLevel;
   max = (maximumValue * (maximumValue - 1)) >>> 1;
   lastUpper = ((maximumValue >>> 1) * ((maximumValue + 1) >>> 1));
   if (g <= lastUpper) {
   
   j = (int) (((maximumValue + 2) * .5d) - Math.sqrt((maximumValue * maximumValue) * 0.25d + 1 - g));
   
   k = (g - (maximumValue + 2) * j + (j * j) + maximumValue);
   g = k + 1 + (((maximumValue + 2) * j - (j * j) - maximumValue - 1) << 1) - (j - 1);
   } else {
   
   j = (int) ((((maximumValue % 2) + 1) * .5d) + Math.sqrt((1 - (maximumValue % 2)) * 0.25d
   + g - 1 - lastUpper));
   
   k = g - (((j - (maximumValue % 2)) * (j - 1)) + 1 + lastUpper);
   
   g = max - k - (2 * j * j - j) - (maximumValue % 2) * (-2 * j + 1);
   }
   
   if (g <= 0)
   start = 0;
   else {
   start = maximumValue - (int) (0.5d + Math.sqrt(0.25 + ((max - g) << 1))) - 1;
   }
   
   k = 0;
   for (j = 1; j <= start; j++) {
   k = (j >>> 1);
   mapping[j] = ((j & 1) != 0) ? (maximumValue - k) : k;
   }
   
   for (; j <= maximumValue; j++) {
   mapping[j] = ((start & 1) != 0) ? (maximumValue - ++k) : ++k;
   }
   
   j = ((maximumValue - start - 1) * (maximumValue - start)) >>> 1;
   j = max - j;
   j = g - j;
   
   basicPermutate(start + 1, mapping, j, maximumValue);
   
  }

  /**
   * Perform the basic permutation.
   * 
   * @param start
   *          The start
   * @param mapping
   *          The mapping
   * @param ruggednessLevel
   *          The ruggedness level of difficulty
   * @param maximumValue
   *          The maximum ruggedness level
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  private static void basicPermutate(final int start, final int[] mapping,
      final int ruggednessLevel, final int maximumValue) {
    int i, j, t;

    if (ruggednessLevel <= 0)
      return;

    basicPermutate(start, mapping, ruggednessLevel - 1, maximumValue);
    j = maximumValue;
    i = j - ruggednessLevel;
    t = mapping[i];
    mapping[i] = mapping[j];
    mapping[j] = t;
  }

  /**
   * Transform a input byte array to an output byte array by merge
   * <code>mu</code> bits to 1 bit. Count the number
   * of 1 in the <code>mu</code> bits, if the number
   * of 1 is equal or higher than the number of 0 the result bit becomes a
   * 1 else a 0. Returns the number of bits in the <code>output</code>
   * byte array.
   * 
   * @param input
   *          The input byte array.
   * @param output
   *          The output byte array.
   * @param bitCount
   *          The number of bits to use from the <code>input</code> byte
   *          array.
   * @param neutralityCount
   *          The number of bits merged to one bit.
   * @return Returns the number of bits in the <code>output</code> byte
   *         array.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public static int neutrality(final byte[] input, final byte[] output,
      final int bitCount, final int neutralityCount ) {
    int outputIndex, i,  majorityValue, shiftCount, outputValue, outputValueIndex;
    byte currentByte;

    outputIndex = 0;
    i = 0;
    majorityValue = 0;
    shiftCount = 0;
    outputValue = 0;
    outputValueIndex = 0;

    currentByte = 0;

    for (i = 0; i < bitCount; ++i) {
      if ((i & 7) == 0)
        currentByte = input[(i >>> 3)];

      if ((currentByte & 1) == 0)
        --majorityValue;
      else
        ++majorityValue;

      currentByte >>>= 1;

      ++shiftCount;

      if (shiftCount >= neutralityCount) {
        if (majorityValue >= 0)
          outputValue |= (1 << outputValueIndex);

        shiftCount = 0;
        majorityValue = 0;
        ++outputValueIndex;

        if (outputValueIndex >= 8) {
          output[outputIndex] = (byte) outputValue;
          ++outputIndex;
          outputValueIndex = 0;
          outputValue = 0;
        }
      }
    }

    if (outputValueIndex > 0) {
      output[outputIndex] = (byte) outputValue;
    }

    return (outputIndex << 3) + outputValueIndex;
  }

  /**
   * Write the input array <code>in</code> to the output array
   * <code>out</code> while changing it to the degree of
   * <code>eta</code>.
   * 
   * @param in
   *          The input byte array.
   * @param out
   *          The output byte array.
   * @param count
   *          The count of bits in the input array <code>in</code>.
   * @param epistasisMapping 
   *          The epistasismapping.
   * @param p_eta
   *          The epistasis level of difficulty.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public static final void epistasis(final byte[] in, final byte[] out,
      final int count, final int[] epistasisMapping, final int p_eta) {
    if (p_eta < 63)
      epistasisTransformLong(in, out, count, epistasisMapping, p_eta);
    else
      epistasisTransformByteArray(in, out, count, p_eta);
  }

  /**
   * Write the input array <code>in</code> to the output array
   * <code>out</code> while changing it to the degree of
   * <code>eta</code>. This methode can
   * handel epistasis with a level lower than 63;
   * 
   * @param in
   *          The input byte array.
   * @param out
   *          The output byte array.
   * @param count
   *          The count of bits in the input array <code>in</code>.
   * @param epistasisMapping 
   *          The epistasismapping.
   * @param p_eta
   *          The epistasis level of difficulty.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  private static final void epistasisTransformLong(final byte[] in,
      final byte[] out, final int count, final int[] epistasisMapping, 
      final int p_eta) {
    long b, j, o, k, l, z, p, m;
    int i, a;

    o = 0;
    j = 0;
    b = 0;
    k = 0;
    l = 0;
    z = 0;
    p = 0;
    a = 0;
    m = p_eta;

    for (i = 0; i < count; i++) {
      if ((i & 0x7) == 0)
        b = in[i >>> 3l];

      if ((b & 1) != 0)
        o |= (1l << k);

      b >>>= 1l;
      if ((++k) >= m) {
        if (m < 21)
          z = epistasisMapping[(int) o];
        else
          z = generateEpistasisMapping(o, m);
        for (j = 0; j < m; ++j) {
          p |= ((z & 1l) << l);
          z >>>= 1l;
          if ((++l) >= 8) {
            l = 0;
            out[a++] = (byte) p;
            p = 0;
          }
        }
        k = 0;
        o = 0;
      }
    }

    if (k != 0) {
      z = generateEpistasisMapping(o, k);

      for (j = 0; j < k; ++j) {
        p |= ((z & 1l) << l);
        z >>>= 1l;
        if ((++l) >= 8) {
          l = 0;
          out[a++] = (byte) p;
          p = 0;
        }
      }
    }

    if (l != 0)
      out[a] = (byte) p;
  }

  /**
   * Write the input array <code>in</code> to the output array
   * <code>out</code> while changing it to the degree of
   * <code>eta</code>. This methode can
   * handel epistasis with a level higher than 62;
   * 
   * @param in
   *          The input byte array.
   * @param out
   *          The output byte array.
   * @param count
   *          The count of bits in the input array <code>in</code>.
   * @param p_eta
   *          The epistasis level of difficulty.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  private static final void epistasisTransformByteArray(final byte[] in,
      final byte[] out, final int count, final int p_eta) {
    long b, j, k, l, p, m;
    int i, a;
    byte[] ba, br;

    j = 0;
    b = 0;
    k = 0;
    l = 0;
    a = 0;
    p = 0;
    m = p_eta;

    ba = new byte[(int) ((m >>> 3) + 1)];

    for (i = 0; i < count; i++) {
      if ((i & 0x7) == 0)
        b = in[i >>> 3l];

      if ((b & 1) != 0)

        b >>>= 1l;
      if ((++k) >= m) {

        br = generateEpistasisMapping(ba, k);

        for (j = 0; j < m; ++j) {
          p |= ((br[(int) (j >>> 3)] >>> (j & 7)) & 1) << l;

          if ((++l) >= 8) {
            l = 0;
            out[a++] = (byte) p;
            p = 0;
          }
        }
        k = 0;
//        ba = new byte[(int) ((m >>> 3) + 1)];
      }
    }

    if (k != 0) {
      br = generateEpistasisMapping(ba, k);

      for (j = 0; j < k; ++j) {
        p |= ((br[(int) (j >>> 3)] >>> (j & 7)) & 1) << l;

        if ((++l) >= 8) {
          l = 0;
          out[a++] = (byte) p;
          p = 0;
        }
      }
    }

    if (l != 0)
      out[a] = (byte) p;
  }

  /**
   * Compute the fitness from the fitnessfunction with the given
   * <code>index</code> for the given individual.
   * 
   * @param in
   *          The individual to compute the fitness.
   * @param index
   *          The index from the functionl objective function.
   * @param bitCount
   *          The count of bits in the byte array <code>in</code>.
   * @return Returns the fitness value for the indivudual <code>in</code>
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  private int calculateFunctionalObjectivFitness(byte[] in, int index,
      int bitCount) {
    int i, j, z, o, ts, c, p, fitness;
    int[][] tc;

    i = 0;
    j = 0;
    tc = this.testCases;
    ts = this.testCaseCount;
    o = 0;
    z = index;
    c = this.functionalObjectiveFunctionCount;

    fitness = 0;
    p = this.phenotypeLength;

    while (z < bitCount && i < p) {
      o = (in[z >>> 3] >>> (z & 7)) & 1;

      for (j = 0; j < ts; ++j) {
        try{
        if (tc[j][i] > 1)
          continue;
        }catch(Throwable tt){
          System.out.println();
        }

        if (tc[j][i] != o)
          ++fitness;
      }

      z += c;
      ++i;
    }

    for (; i < p; ++i) {
      for (j = 0; j < ts; ++j) {
        if (tc[j][i] > 1)
          continue;

        if (tc[j][i] != (i & 1))
          ++fitness;
      }
    }

    return fitness;
  }

  /**
   * Calculate the fitness for the individual <code>in</code> and the
   * fitness function index <code>index</code>. Returns the fitness
   * value.
   * 
   * @param in
   *          The individual to compute the fitness.
   * @param index
   *          The index from the functionl objective function.
   * @param length
   *          the number of bits in the byte array <code>in</code>
   * @return Returns the functional objective fitness for the individual.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public int functionalObjectiveFunction(final byte[] in,
      final int length, final int index) {
       
    byte[] result = null;
    int neutralityBitCount, fitness;

    if( this.mu > 1 )
      result = new byte[ ((length / this.mu)<<3)+1 ];
    else if( this.eta > 1 )
      result = new byte[ in.length ];
    else 
      result = in;
    
    if (this.mu > 1) {
      neutralityBitCount = neutrality(in, result, length, this.mu);
    } else {
      neutralityBitCount = length;
    }

    if (this.eta > 1) {
      if( this.mu > 1 )
        epistasis(result, result, neutralityBitCount, this.epistasisMapping, 
            this.eta);
      else
        epistasis(in, result, neutralityBitCount, this.epistasisMapping, 
          this.eta);
    } 

    if (index > this.functionalObjectiveFunctionCount)
      return -1;// TODO anstädige fehlermeldung oder anderen wert?

    fitness = this.calculateFunctionalObjectivFitness(result,
        index, neutralityBitCount);

    if (this.gamma > 0)
      fitness = this.ruggednessMapping[fitness];

    return fitness;
  }

  /**
   * Calculate the non functional objective fitness for the given
   * individual <code>in</code>. Returns this value.
   * 
   * @param in
   *          The individual to compute the fitness.
   * @param length
   *          the number of bits in the byte array
   * @return Returns the non functional objecitve fitness.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public int nonfunctionalObjectiveFunction(final byte[] in,
      final int length) {
    int maxLen;

    if ((in == null) || (length <= 0))
      return 0;
    maxLen = (in.length << 3);
    return ((length <= maxLen) ? length : maxLen);
  }

  /**
   * Returns <code>true</code> if the given individual <code>in</code>
   * is overfitted, else <code>false</code>.
   * 
   * @param in
   *          The individual to check.
   * @param length
   *          the number of bits in the byte array <code>in</code>
   * @return Returns <code>true</code> if the given individual
   *         <code>in</code> is overfitted, else <code>false</code>.
   * @autor Stefan Niemczyk
   * @since Verion 1.0.0
   */
  public boolean isOverfitted(final byte[] in, final int length) {
    int i, j, c, o, z, size, neutralityBitCount;
    byte[] result = null;

    if( this.mu > 1 )
      result = new byte[ ((length / this.mu)<<3)+1 ];
    else if( this.eta > 1 )
      result = new byte[ in.length ];
    else 
      result = in;
    
    if (this.mu > 1) {
      neutralityBitCount = neutrality(in, result, length, this.mu);
    } else {
      neutralityBitCount = length;
    }

    if (this.eta > 1) {
      if( this.mu > 1 )
        epistasis(result, result, neutralityBitCount, this.epistasisMapping, 
            this.eta);
      else
        epistasis(in, result, neutralityBitCount, this.epistasisMapping, 
          this.eta);
    } 


    c = this.functionalObjectiveFunctionCount;
    size = this.phenotypeLength * c;

//    if ((neutralityBitCount & 7) != 0)
//      ++neutralityBitCount;

    if (size != neutralityBitCount)
      return true;

    z = 0;

    if( this.mu <= 1 && this.eta <= 1 )
    {
      for (i = 0; i < neutralityBitCount; ++i) {
        o = (z & 1);
        for (j = 0; j < c; ++j) {
          if (((in[i >>> 3] >>> (i & 7)) & 1) == o)
            return true;
          ++i;
        }
        --i;
        ++z;
      }
    }
    else
    {
      for (i = 0; i < neutralityBitCount; ++i) {
        o = (z & 1);
        for (j = 0; j < c; ++j) {
          if (((result[i >>> 3] >>> (i & 7)) & 1) == o)
            return true;
          ++i;
        }
        --i;
        ++z;
      }
    }

    return false;
  }

  /**
   * Returns <code>true</code> if the given individual <code>in</code>
   * is success, else <code>false</code>.
   * 
   * @param in
   *          The individual to check.
   * @param length
   *          the number of bits in the byte array <code>in</code>
   * @return Returns <code>true</code> if the given individual
   *         <code>in</code> is correct, else <code>false</code>.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public boolean isSuccess(final byte[] in, final int length) {
    int i, ffc;

    ffc = this.functionalObjectiveFunctionCount;

    for (i = 0; i < ffc; ++i) {
      if (this.functionalObjectiveFunction(in, length, i) > this.minimumFitnessToSuccess)
        return false;
    }

    return true;
  }

  /**
   * Returns <code>true</code> if the given funvtional objectives
   * <code>objectives</code> lower then
   * <code>minimumFitnessToSuccess</code>, else <code>false</code>.
   * 
   * @param objectives
   *          The individual to check.
   * @return Returns <code>true</code> if the given individual
   *         <code>in</code> is correct, else <code>false</code>.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public boolean isSuccess(double[] objectives) {
    int i, ffc;

    ffc = this.functionalObjectiveFunctionCount;

    for (i = 0; i < ffc; ++i) {
      if (objectives[i] > this.minimumFitnessToSuccess)
        return false;
    }

    return true;
  }
  
  /**
   * Call this methode after changing some settings to recalculate all 
   * internal values.
   * 
   * @autor Stefan Niemczyk
   * @since Version 1.0.1
   */
  public void recalculateInternalValues()
  {
    if( this.phenotypeLengthChanged || this.testcasesChanged )
    {
      if( !this.noiseSetDirect )
        this.internalNoiseDifficultyLevel =
            (int) ((this.phenotypeLength >> 1) * 
                (this.noiseDifficultyLevel / 10d));
      
      if( !this.incompletenessSetDirect )
        this.internalIncompletenessDifficultyLevel =
            (int) ((this.phenotypeLength >> 1) * 
                (this.incompletenessDifficultyLevel / 10d));
      
      this.generateTestcases(this.testCaseCount, 
          this.internalNoiseDifficultyLevel, 
          this.internalIncompletenessDifficultyLevel);
    }
    
    if( this.neutralityLevelChanged || this.phenotypeLengthChanged )
    {
      if( !this.neutralitySetDirect )
        this.mu = 
          (int) Math.round(this.neutralityDifficultyLevel) +1;
    }
    
    if( this.epistasisLevelChanged || this.phenotypeLengthChanged )
    {
      if( !this.epistasisSetDirect )
        this.eta = 
          (((int) (this.epistasisDifficultyLevel)*2)*2)+1;
      
      if (this.eta < 22 )
        this.generateEpistasisMapping();
    }
    
    if( this.ruggednessLevelChanged || this.phenotypeLengthChanged )
    {
      if( this.ruggednessDifficultyLevel == 0 && 
          !this.ruggednessSetDirect)
        this.gamma = 0;
      else
      {
        int max;
        int lastRugged;
          
        max = (this.maximumFitness * 
                (this.maximumFitness + 1 ))/ 2;
        max -= this.maximumFitness;
        if( !this.ruggednessSetDirect )
        {
          lastRugged = ( this.maximumFitness / 2 );
          if( this.maximumFitness % 2 != 0 )
            lastRugged *= (( this.maximumFitness / 2 ) + 1 );
          else
            lastRugged *= ( this.maximumFitness / 2 );
          
          if( this.deceptive )
            this.gamma = 
              (int) ((this.ruggednessDifficultyLevel / 10d) * 
                  ( max - (lastRugged+1) )) + (lastRugged+1);
          else
            this.gamma = 
              (int) ((this.ruggednessDifficultyLevel / 10d) * lastRugged);
        }
      }
      
      this.generateRuggednessMapping( this.gamma, this.maximumFitness );
    }
    
    this.phenotypeLengthChanged = false;
    this.testcasesChanged = false;
    this.neutralityLevelChanged = false;
    this.epistasisLevelChanged = false;
    this.ruggednessLevelChanged = false;
  }

  // --------------------------------

  /**
   * Returns the number of testcases.
   * 
   * @return Returns the number of testcases.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public int getTestCaseCount() {
    return this.testCaseCount;
  }

  /**
   * Sets the number of Testcases.
   * 
   * @param p_testCaseCount
   *          The new number of testcases.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public void setTestCaseCount(int p_testCaseCount) {
    this.testCaseCount = p_testCaseCount;
    this.testcasesChanged = true;
  }

  /**
   * Returns the number of wrong positions in each testcase on a scale from
   * 0 - 10.
   * 
   * @return Returns the number of wrong positions in each testcase on a
   *         scale from 0 - 10.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public double getNoiseDifficultyLevel() {
    return this.noiseDifficultyLevel;
  }

  /**
   * Sets the noise level of difficulty.
   * 
   * @param p_noiseDifficultyLevel
   *          The new level of difficulty. Possible values are 0 - 10!
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public void setNoiseDifficultyLevel(double p_noiseDifficultyLevel) {
    if (p_noiseDifficultyLevel < 0d)
      this.noiseDifficultyLevel = 0d;
    else if (p_noiseDifficultyLevel > 10)
      this.noiseDifficultyLevel = 10d;
    else
      this.noiseDifficultyLevel = p_noiseDifficultyLevel;

    this.noiseSetDirect = false;
    this.testcasesChanged = true;
  }

  /**
   * Returns the number of wrong positions in each testcase.
   * 
   * @return Returns the number of wrong positions in each testcase.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public int getInternalNoiseDifficultyLevel() {
    return this.internalNoiseDifficultyLevel;
  }

  /**
   * Sets the number of wrong positions in each testcase.
   * 
   * @param p_internalNoiseDifficultyLevel
   *          The new number of wrong positions in each testcase.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public void setInternalNoiseDifficultyLevel(
      int p_internalNoiseDifficultyLevel) {
    this.internalNoiseDifficultyLevel = p_internalNoiseDifficultyLevel;

    this.noiseSetDirect = true;
    this.testcasesChanged = true;
  }
  
  /**
   * Returns the number of wrong positions in each testcase.
   * 
   * @return Returns the number of wrong positions in each testcase.
   * @autor Stefan Niemczyk
   * @since Version 1.0.2
   */
  public int getEpsylon() {
    return this.getInternalNoiseDifficultyLevel();
  }

  /**
   * Sets the number of wrong positions in each testcase.
   * 
   * @param p_epsylon
   *          The new number of wrong positions in each testcase.
   * @autor Stefan Niemczyk
   * @since Version 1.0.2
   */
  public void setEpsylon(int p_epsylon) {
    this.setInternalNoiseDifficultyLevel(p_epsylon);
  }

  /**
   * Returns the number of incompleted positions in each testcase on a
   * scale from 0 - 10.
   * 
   * @return Returns the number of incompleted positions in each testcase
   *         on a scale from 0 - 10.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public double getIncompletenessDifficultyLevel() {
    return this.incompletenessDifficultyLevel;
  }

  /**
   * Sets the number of incompleted positions in each testcase.
   * 
   * @param p_incompletenessDifficultyLevel
   *          The new level of difficulty. Possible values are 0 - 10!
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public void setIncompletenessDifficultyLevel(
      double p_incompletenessDifficultyLevel) {
    if (p_incompletenessDifficultyLevel < 0d)
      this.incompletenessDifficultyLevel = 0d;
    else if (p_incompletenessDifficultyLevel > 10d)
      this.incompletenessDifficultyLevel = 10d;
    else
      this.incompletenessDifficultyLevel = p_incompletenessDifficultyLevel;

    this.incompletenessSetDirect = false;
    this.testcasesChanged = true;
  }

  /**
   * Returns the number of incompleted positions in each testcase.
   * 
   * @return Returns the number of incompleted positions in each testcase.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public int getInternalIncompletenessDifficultyLevel() {
    return this.internalIncompletenessDifficultyLevel;
  }

  /**
   * Sets the number of wrong positions in each testcase.
   * 
   * @param p_o
   *          The new number of wrong positions in each testcase.
   * @autor Stefan Niemczyk
   * @since Version 1.0.2
   */
  public void setO(int p_o) {
    this.setInternalIncompletenessDifficultyLevel(p_o);
  }
  
   /**
   * Returns the number of incompleted positions in each testcase.
   * 
   * @return Returns the number of incompleted positions in each testcase.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public int getO() {
    return this.getInternalIncompletenessDifficultyLevel();
  }

  /**
   * Sets the number of wrong positions in each testcase.
   * 
   * @param p_internalIncompletenessDifficultyLevel
   *          The new number of wrong positions in each testcase.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public void setInternalIncompletenessDifficultyLevel(
      int p_internalIncompletenessDifficultyLevel) {
    this.internalIncompletenessDifficultyLevel = p_internalIncompletenessDifficultyLevel;

    this.incompletenessSetDirect = true;
    this.testcasesChanged = true;
  }

  /**
   * Returns the neutrality level of difficulty on a scale from 0 - 10.
   * 
   * @return Returns the neutrality level of difficulty on a scale from 0 -
   *         10.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public double getNeutralityDifficultyLevel() {
    return this.neutralityDifficultyLevel;
  }

  /**
   * Sets the neutrality level of difficulty.
   * 
   * @param p_neutralityDifficultyLevel
   *          The new level. Possible values are 0 - 10
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public void setNeutralityDifficultyLevel(
      double p_neutralityDifficultyLevel) {
    if (p_neutralityDifficultyLevel < 0d)
      this.neutralityDifficultyLevel = 0d;
    else if (p_neutralityDifficultyLevel > 10d)
      this.neutralityDifficultyLevel = 10d;
    else
      this.neutralityDifficultyLevel = p_neutralityDifficultyLevel;

    this.neutralitySetDirect = false;
    this.neutralityLevelChanged = true;
  }

  /**
   * Returns the number of bits which are merged to 1 bit.
   * 
   * @return Returns the number of bits which are merged to 1 bit.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public int getInternalNeutralityDifficultyLevel() {
    return this.mu;
  }

  /**
   * Sets the number of bits merged by neutrality to 1 bit.
   * 
   * @param p_internalNeutralityDifficultyLevel
   *          The new number of bits merged to 1 bit.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public void setInternalNeutralityDifficultyLevel(
      int p_internalNeutralityDifficultyLevel) {
    this.mu = p_internalNeutralityDifficultyLevel;
    this.neutralitySetDirect = true;
    this.neutralityLevelChanged = true;
  }
  
   /**
   * Returns the number of bits which are merged to 1 bit.
   * 
   * @return Returns the number of bits which are merged to 1 bit.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public int getMu() {
    return this.getInternalNeutralityDifficultyLevel();
  }

  /**
   * Sets the number of bits merged by neutrality to 1 bit.
   * 
   * @param p_mu
   *          The new number of bits merged to 1 bit.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public void setMu(int p_mu) {
    this.setInternalNeutralityDifficultyLevel(p_mu);
  }

  /**
   * Returns the epistasis level of difficulty on a scala from 0 - 10.
   * 
   * @return Returns the epistasis level of difficulty on a scala from 0 -
   *         10.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public double getEpistasisDifficultyLevel() {
    return this.epistasisDifficultyLevel;
  }

  /**
   * Sets the epistsis level of difficulty.
   * 
   * @param p_epistasisDifficultyLevel
   *          The new epistasis level. Possible values are 0 - 10.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public void setEpistasisDifficultyLevel(double p_epistasisDifficultyLevel) {
    if (p_epistasisDifficultyLevel < 0d)
      this.epistasisDifficultyLevel = 0d;
    else if (p_epistasisDifficultyLevel > 10d)
      this.epistasisDifficultyLevel = 10d;
    else
      this.epistasisDifficultyLevel = p_epistasisDifficultyLevel;

    this.epistasisSetDirect = false;
    this.epistasisLevelChanged = true;
  }

  /**
   * Return the number of bits, which mapped as 1 block to another block of
   * the same length. This mapping is one-to-one.
   * 
   * @return Return the number of bits, which mapped as 1 block to another
   *         block of the same length. This mapping is one-to-one.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public int getInternalEpistasisDifficultyLevel() {
    return this.eta;
  }

  /**
   * Sets the number of bits, which mapped as 1 block to another block of
   * the same length. This mapping is one-to-one.
   * 
   * @param p_internalEpistasisDifficultyLevel
   *          The new number of bits.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public void setInternalEpistasisDifficultyLevel(
      int p_internalEpistasisDifficultyLevel) {
    this.eta = p_internalEpistasisDifficultyLevel;

    this.epistasisSetDirect = true;
    this.epistasisLevelChanged = true;
  }
  
  /**
   * Return the number of bits, which mapped as 1 block to another block of
   * the same length. This mapping is one-to-one.
   * 
   * @return Return the number of bits, which mapped as 1 block to another
   *         block of the same length. This mapping is one-to-one.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public int getEta() {
    return this.getInternalEpistasisDifficultyLevel();
  }

  /**
   * Sets the number of bits, which mapped as 1 block to another block of
   * the same length. This mapping is one-to-one.
   * 
   * @param p_eta
   *          The new number of bits.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public void setEta(int p_eta) {
    this.setInternalEpistasisDifficultyLevel(p_eta);
  }

  /**
   * Returns the ruggedness level of difficulty on a scala from 0 - 10. If
   * <code>deceptive</code> is <code>true</code> this returns the
   * deceptive level of difficulty on a scala from 0 - 10.
   * 
   * @return Returns the ruggedness level of difficulty on a scala from 0 -
   *         10. If <code>deceptive</code> is <code>true</code> this
   *         returns the deceptive level of difficulty on a scala from 0 -
   *         10.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public double getRuggednessDifficultyLevel() {
    return this.ruggednessDifficultyLevel;
  }

  /**
   * Sets the ruggedness level of difficulty. If <code>deceptive</code>
   * is <code>true</code> this sets the deceptive level of difficulty.
   * 
   * @param p_ruggednessDifficultyLevel
   *          The new level. Possible values are 0 - 10.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public void setRuggednessDifficultyLevel(
      double p_ruggednessDifficultyLevel) {
    if (p_ruggednessDifficultyLevel < 0d)
      this.ruggednessDifficultyLevel = 0d;
    else if (p_ruggednessDifficultyLevel > 10d)
      this.ruggednessDifficultyLevel = 10d;
    else
      this.ruggednessDifficultyLevel = p_ruggednessDifficultyLevel;

    this.ruggednessSetDirect = false;
    this.ruggednessLevelChanged = true;
  }

  /**
   * Returns <code>true</code> if and only if the ruggednessmapping is
   * deceptive, else returns <code>false</code>.
   * 
   * @return Returns <code>true</code> if and only if the
   *         ruggednessmapping is deceptive, else returns
   *         <code>false</code>.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public boolean isDeceptive() {
    return this.deceptive;
  }

  /**
   * Set deceptive <code>true</code> if the ruggednessmapping of your
   * problem is deceptive, else set this <code>false</code>.
   * 
   * @param p_deceptive
   *          <code>true</code> if the ruggednessmapping is deceptive,
   *          else <code>false</code>.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public void setDeceptive(boolean p_deceptive) {
    this.deceptive = p_deceptive;
    
    this.ruggednessLevelChanged = true;
  }

  /**
   * Returns the internal ruggedness level of difficulty.
   * 
   * @return Returns the internal ruggedness level of difficulty.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public int getInternalRuggednessDifficultyLevel() {
    return this.gamma;
  }

  /**
   * Stes the internal ruggedness level of difficulty.
   * 
   * @param p_internalRuggednessDifficultyLevel
   *          The new internal level.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public void setInternalRuggednessDifficultyLevel(
      int p_internalRuggednessDifficultyLevel) {
    this.gamma = p_internalRuggednessDifficultyLevel;

    this.ruggednessSetDirect = true;
    this.ruggednessLevelChanged = true;
  }
  
  /**
   * Returns the internal ruggedness level of difficulty.
   * 
   * @return Returns the internal ruggedness level of difficulty.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public int getGamma() {
    return this.getInternalRuggednessDifficultyLevel();
  }

  /**
   * Stes the internal ruggedness level of difficulty.
   * 
   * @param p_gamma
   *          The new internal level.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public void setGamma(int p_gamma) {
    this.setInternalRuggednessDifficultyLevel(p_gamma);
  }

  /**
   * Returns the number of functional objecitve functions.
   * 
   * @return Returns the number of functional objecitve functions.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public int getFunctionalObjectiveFunctionCount() {
    return this.functionalObjectiveFunctionCount;
  }

  /**
   * Sets the number of functions objective functions.
   * 
   * @param p_functionalObjectiveFunctionCount
   *          The new number of functional objective functions. Must be
   *          greater than 1.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public void setFunctionalObjectiveFunctionCount(
      int p_functionalObjectiveFunctionCount) {
    if (p_functionalObjectiveFunctionCount < 1)
      this.functionalObjectiveFunctionCount = 1;
    else
      this.functionalObjectiveFunctionCount = p_functionalObjectiveFunctionCount;
  }

  /**
   * Returns the ruggedness mapping.
   * 
   * @return Returns the ruggedness mapping.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public int[] getRuggednessMapping() {
    return this.ruggednessMapping;
  }

  /**
   * Returns the epistasis mapping.
   * 
   * @return Returns the epistasis mapping.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public int[] getEpistasisMapping() {
    return this.epistasisMapping;
  }

  /**
   * Returns the bitlength of the phenotype.
   * 
   * @return Returns the bitlength of the phenotype.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public int getPhenotypeLength() {
    return this.phenotypeLength;
  }

  /**
   * Sets the bitlength of the phenotype.
   * 
   * @param p_phenotypeLength
   *          The new bitlength.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public void setPhenotypeLength(int p_phenotypeLength) {
    this.phenotypeLength = p_phenotypeLength;
    
    this.phenotypeLengthChanged = true;
  }
  
  /**
   * Returns the bitlength of the phenotype.
   * 
   * @return Returns the bitlength of the phenotype.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public int getN() {
    return this.getPhenotypeLength();
  }

  /**
   * Sets the bitlength of the phenotype.
   * 
   * @param p_n
   *          The new bitlength.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public void setN(int p_n) {
    this.setPhenotypeLength(p_n);
  }

  /**
   * Returns the correct byte value coded as integer.
   * 
   * @return Returns the correct byte value coded as integer.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public int getSOLUTION_VALUE() {
    return this.SOLUTION_VALUE;
  }

  /**
   * Returns the testcases.
   * 
   * @return Returns the testcases.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public int[][] getTestCases() {
    return this.testCases;
  }

  /**
   * Sets the test cases.
   * 
   * @param p_testCases
   *          The new set of test cases.
   * @autor Stefan Niemczyk
   * @since Version 1.0.0
   */
  public void setTestCases(int[][] p_testCases) {
    this.testCases = p_testCases;
  }
}
