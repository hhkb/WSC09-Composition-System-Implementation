/*
 * Copyright (c) 2007 Stefan Niemczyk for Bachelor thesis
 * A Tunable Benchmark Problem for Genetic Programming of Algorithms
 * 
 * E-Mail           : niemczyk@uni-kassel.net
 * Creation Date    : 2007-11-11
 * Creator          : Stefan Niemczyk
 * Original Filename: org.dgpf.benchmark.PseudoAlgorithmBenchmark.java
 * Version          : 0.1.0
 * Last modification: 2007-11-11
 *                by: Stefan Niemczyk
 *                
 */

package org.dgpf.benchmark;

import java.util.Random;

import org.sfc.io.binary.BinaryInputStream;

/**
 * This class is a benchmark for genetic Programm. It creats testcases und
 * calculate the fitness for a given individual. It is possible to set the
 * level of difficulty for reggedness, neutrality, epistasis and affinity
 * for Overfitting.
 * 
 * @author Stefan Niemczyk
 */
public class PseudoAlgorithmBenchmark {
  /**
   * This is the integer value for a correct byte ( 1010 1010 ).
   * 
   * @autor Stefan Niemczyk
   * @since Version 0.1.0
   */
  private final int SOLUTION_VALUE = 170;

  /**
   * This is the integer value for a inverted correct byte ( 0101 0101 ).
   * 
   * @autor Stefan Niemczyk
   * @since Version 0.1.0
   */
  private final int OPTIMAL_TESTCASE_VALUE = 85;

  /**
   * The size of the genotype in bytes not bits! This size can´t be set by
   * the user. Its will be set by the values for reggedness, neutrality,
   * epistasis and affinity for Overfitting.
   * 
   * @autor Stefan Niemczyk
   * @since Version 0.1.0
   */
  private int genotypeLength;

  /**
   * The size of the phenotype in bytes not bits!
   * 
   * @autor Stefan Niemczyk
   * @since Version 0.1.0
   */
  private int phenotypeLength;

  /**
   * The optimal testcase. Its the inverse from the optimal solution.
   * 
   * @autor Stefan Niemczyk
   * @since Version 0.1.0
   */
  private byte[] optimalTestcase;

  /**
   * The testcases used to evaluate a individual.
   * 
   * @autor Stefan Niemczyk
   * @since Version 0.1.0
   */
  private byte[][] testcases;

  /**
   * True if testcases are used, false if only the
   * <code>optimalTestcase</code> is used.
   * 
   * @autor Stefan Niemczyk
   * @since Version 0.1.0
   */
  private boolean enabledTestcaseEvaluation;

  /**
   * The number of testcases.
   * 
   * @autor Stefan Niemczyk
   * @since Version 0.1.0
   */
  private int testcaseSize;

  /**
   * The level of difficulty for the used testcases. 0 only the
   * <code>optimalTestcase</code> else <code>testcaseSize</code>
   * testcases and each has <code>phenotypeLength</code> / 2 * test value /
   * 10 wrong positions.
   * 
   * @autor Stefan Niemczyk
   * @since Version 0.1.0
   */
  private int testcaseDifficultyLevel;

  /**
   * @autor Stefan Niemczyk
   * @since Version 0.1.0
   */
  // private int ruggednessDifficultyLevel;
  /**
   * @autor Stefan Niemczyk
   * @since Version 0.1.0
   */
  // private int epistasisDifficultyLevel;
  /**
   * @autor Stefan Niemczyk
   * @since Version 0.1.0
   */
  // private int neutralityDifficultyLevel;
  /**
   * This is the number of errorpositions for each testcase.
   * 
   * @autor Stefan Niemczyk
   * @since Version 0.1.0
   */
  private int internTestcaseDifficultyLevel;

  /**
   * @autor Stefan Niemczyk
   * @since Version 0.1.0
   */
  // private int internRuggednessDifficultyLevel;
  /**
   * @autor Stefan Niemczyk
   * @since Version 0.1.0
   */
  // private int internEpistasisDifficultyLevel;
  /**
   * @autor Stefan Niemczyk
   * @since Version 0.1.0
   */
  // private int internNeutralityDifficultyLevel;
  /**
   * The contructor for this class. It creats the
   * <code>optimalTestcase</code> and initialize the default level of
   * difficulty for all parameters.
   * 
   * @param p_phenotypeLength
   *          The size of the phenotype.
   * @param p_testcaseSize
   *          The number of testcases. 0 if the
   *          <code>optimalTestcase</code> is the only testcase
   * @autor Stefan Niemczyk
   * @since Version 0.1.0
   */
  public PseudoAlgorithmBenchmark(int p_phenotypeLength, int p_testcaseSize) {
    this.phenotypeLength = p_phenotypeLength;
    this.testcaseSize = p_testcaseSize;
    this.genotypeLength = p_phenotypeLength;

    this.optimalTestcase = new byte[p_phenotypeLength];

    for (int i = 0; i < p_phenotypeLength; ++i)
      this.optimalTestcase[i] = (byte) this.OPTIMAL_TESTCASE_VALUE;

    if (p_testcaseSize == 0)
      this.setTestcaseDifficultyLevel(0);
    else
      this.setTestcaseDifficultyLevel(5);

  }

  /**
   * Returns true if testcases are used, false if only the
   * <code>optimalTestcase</code> is used.
   * 
   * @return Returns true if testcases are used, false if only the
   *         <code>optimalTestcase</code> is used.
   * @autor Stefan Niemczyk
   * @since Version 0.1.0
   */
  public boolean isEnabledTestcaseEvaluation() {
    return this.enabledTestcaseEvaluation;
  }

  /**
   * Returns the level of Difficulty for the testcases. Possible values are
   * 0 to 10.
   * 
   * @return Returns the level of Difficulty for the testcases.
   * @autor Stefan Niemczyk
   * @since Version 0.1.0
   */
  public int getTestcaseDifficultyLevel() {
    return this.testcaseDifficultyLevel;
  }

  /**
   * The level of difficulty for the testcases says how many positions in
   * each testcase are wrong. Possible values are 0 - 10. If the level is 0
   * only the <code>optimalTestcase</code> will be used!
   * 
   * @param p_testcaseDifficultyLevel
   *          The new testcase level of difficulty.
   * @autor Stefan Niemczyk
   * @since Version 0.1.0
   */
  public void setTestcaseDifficultyLevel(int p_testcaseDifficultyLevel) {
    this.testcaseDifficultyLevel = p_testcaseDifficultyLevel;

    if (this.testcaseDifficultyLevel < 0)
      this.testcaseDifficultyLevel = 0;
    else if (this.testcaseDifficultyLevel > 10)
      this.testcaseDifficultyLevel = 10;

    if (this.testcaseDifficultyLevel == 0) {
      this.enabledTestcaseEvaluation = false;
      this.testcases = null;
    } else {
      this.enabledTestcaseEvaluation = true;
      int max = (this.phenotypeLength * 8) / 2;
      int difficultyLevel = (int) (max * (this.testcaseDifficultyLevel / 10d));
      this.setInternTestcaseDifficultyLevel(difficultyLevel);
    }
  }

  /**
   * Returns the integer value for a correct byte ( 1010 1010 ).
   * 
   * @return Returns the integer value for a correct byte ( 1010 1010 ).
   * @autor Stefan Niemczyk
   * @since Version 0.1.0
   */
  public int getSOLUTION_VALUE() {
    return this.SOLUTION_VALUE;
  }

  /**
   * Returns the size of the genotype in bytes not bits! This size can´t be
   * set by the user. Its will be set by the values for reggedness,
   * neutrality, epistasis and affinity for Overfitting.
   * 
   * @return Returns the size of the genotype in bytes not bits!
   * @autor Stefan Niemczyk
   * @since Version 0.1.0
   */
  public int getGenotypeLength() {
    return this.genotypeLength;
  }

  /**
   * Returns the size of the phenotype in bytes not bits!
   * 
   * @return Returns the size of the phenotype in bytes not bits!
   * @autor Stefan Niemczyk
   * @since Version 0.1.0
   */
  public int getPhenotypeLength() {
    return this.phenotypeLength;
  }

  /**
   * Returns the optimal testcase. Its the inverse from the optimal
   * solution.
   * 
   * @return Returns the optimal testcase. Its the inverse from the optimal
   *         solution.
   * @autor Stefan Niemczyk
   * @since Version 0.1.0
   */
  public byte[] getOptimalTestcase() {
    return this.optimalTestcase;
  }

  /**
   * Returns the testcases used to evaluate a individual.
   * 
   * @return Returns the testcases used to evaluate a individual.
   * @autor Stefan Niemczyk
   * @since Version 0.1.0
   */
  public byte[][] getTestcases() {
    return this.testcases;
  }

  /**
   * Returns the number of Testcases.
   * 
   * @return Returns the number of Testcases.
   * @autor Stefan Niemczyk
   * @since Version 0.1.0
   */
  public int getTestcaseSize() {
    return this.testcaseSize;
  }

  /**
   * Sets the number of errorpositions for each testcase. New testcases
   * will be created after changing this value. Possible values are 0 - (
   * <code>phenotypeSize</code> * 8 ) / 2. USE THIS METHODE ONLY IF YOU
   * REALY KNOW WHAT A CHANGE DOES!!!!
   * 
   * @param p_internTestcaseDifficultyLevel
   *          The new number of errorpositions for each Testcase.
   * @autor Stefan Niemczyk
   * @since Version 0.1.0
   */
  private void setInternTestcaseDifficultyLevel(
      int p_internTestcaseDifficultyLevel) {
    this.internTestcaseDifficultyLevel = p_internTestcaseDifficultyLevel;

    this.createTestcases();
  }

  /**
   * Creats new Testcases. This is necessary after changing the value of
   * <code>internTestcaseDufficultyLevel</code>.
   * 
   * @autor Stefan Niemczyk
   * @since Version 0.1.0
   */
  private void createTestcases() {

    this.testcases = new byte[this.testcaseSize][];

    Random random = new Random();

    for (int i = 0; i < this.testcaseSize; ++i) {
      byte[] aTestCase = this.optimalTestcase.clone();
      boolean[] changes;

      // TODO hier ist ein besserer algorithmus nötig!
      for (int j = 0; j < this.internTestcaseDifficultyLevel; ++j) {
        changes = new boolean[this.phenotypeLength * 8];
        int errorPosition = random.nextInt(this.phenotypeLength * 8);

        while (errorPosition % 2 == 0 || // nur gerade stellen
            changes[errorPosition])
          errorPosition = random.nextInt(this.phenotypeLength * 8);

        changes[errorPosition] = true;
        this.negatePositionInABytearray(aTestCase, errorPosition);
      }

      this.testcases[i] = aTestCase;

      byte[] otc = aTestCase;
      BinaryInputStream bis = new BinaryInputStream();
      bis.init(otc);

      for (int k = 0; k < otc.length; ++k) {
        // System.out.print( otc[k] + " " ); //$NON-NLS-1$
        for (int j = 0; j < 8; ++j)
          System.out.print(bis.readBits(1));
      }

      System.out.println();
    }
  }

  /**
   * Negate a bit in a bytearray.
   * 
   * @param p_aByteArray
   *          The given bytearray.
   * @param p_position
   *          The position of the bit to negate.
   * @autor Stefan Niemczyk
   * @since Version 0.1.0
   */
  private void negatePositionInABytearray(byte[] p_aByteArray,
      int p_position) {
    byte xor = (byte) 0;

    switch (p_position % 8) {
    case 0:
      xor = (byte) 128;
      break;
    case 1:
      xor = (byte) 64;
      break;
    case 2:
      xor = (byte) 32;
      break;
    case 3:
      xor = (byte) 16;
      break;
    case 4:
      xor = (byte) 8;
      break;
    case 5:
      xor = (byte) 4;
      break;
    case 6:
      xor = (byte) 2;
      break;
    case 7:
      xor = (byte) 1;
      break;
    }

    p_aByteArray[p_position / 8] ^= xor;
  }

  /**
   * Returns true if the bit on position <code>p_position</code> is 1
   * else false.
   * 
   * @param p_aByteArray
   *          The given bytearray.
   * @param p_position
   *          The position of the bit to negate.
   * @return Returns true if the bit on position <code>p_position</code>
   *         is 1 else false.
   * @autor Stefan Niemczyk
   * @since Version 0.1.0
   */
  // private boolean getBitInABytearray( byte[] p_aByteArray,
  // int p_position)
  // {
  // byte b = p_aByteArray[ p_position / 8 ];
  //   
  // b >>>= p_position % 8;
  //    
  // int mod = b % 2;
  // if( mod =! 0 )
  // return false;
  //    
  // return true;
  // }
  /**
   * Returns the count of 1 in a bytearray.
   * 
   * @param p_aByteArray
   *          The given bytearray.
   * @return Returns the count of 1 in a bytearray.
   * @autor Stefan Niemczyk
   * @since Version 0.1.0
   */
  private int countOfOneInABytearray(byte[] p_aByteArray) {
    int size = p_aByteArray.length;
    int count = 0;

    for (int i = 0; i < size; ++i) {
      byte b = p_aByteArray[i];

      for (int j = 0; j < 8; ++j) {
        int mod = b % 2;
        if (mod != 0)
          ++count;

        b >>>= 1;
      }
    }

    return count;
  }

  /**
   * Calculate the functional fitnessvalue for the given
   * <code>individual</code>.
   * 
   * @param individual
   *          The individual to calculate the fitnessvalue.
   * @return Returns the calculate functional fitnessvalue for the given
   *         <code>individual</code>.
   * @autor Stefan Niemczyk
   * @since Version 0.1.0
   */
  public double getFunctionalObjectiveValue(byte[] individual) {
    byte[] adaptedIndividual;
    int l_testcaseSize = this.testcaseSize;
    int phenotyeSize = this.phenotypeLength;
    byte[] optimum = this.optimalTestcase;
    int fitnessvalue;

    if (individual.length > phenotyeSize) {
      adaptedIndividual = new byte[phenotyeSize];

      for (int i = 0; i < phenotyeSize; ++i)
        adaptedIndividual[i] = individual[i];
    } else if (individual.length < phenotyeSize) {
      adaptedIndividual = new byte[phenotyeSize];
      int individualSize = individual.length;

      for (int i = 0; i < individualSize; ++i)
        adaptedIndividual[i] = individual[i];

      for (int i = individualSize; i < phenotyeSize; ++i)
        adaptedIndividual[i] = (byte) this.OPTIMAL_TESTCASE_VALUE;
    } else
      adaptedIndividual = individual;

    // for( int i=0; i < phenotyeSize; ++i )
    // {
    // if( adaptedIndividual[i] == this.SOLUTION_VALUE ||
    // adaptedIndividual[i] == this.SOLUTION_VALUE - 256 )
    // {
    // if( i == phenotyeSize-1 )
    // return 0;
    // }
    // else
    // break;
    // }

    if (this.enabledTestcaseEvaluation) {
      fitnessvalue = 0;
      byte[] testcase;

      for (int i = l_testcaseSize - 1; i >= 0; --i) {
        byte[] xorResult = new byte[phenotyeSize];
        testcase = this.testcases[i];

        for (int j = 0; j < phenotyeSize; ++j)
          xorResult[j] = (byte) (testcase[j] ^ adaptedIndividual[j]);

        fitnessvalue += this.phenotypeLength * 8
            - this.countOfOneInABytearray(xorResult);
      }
    } else {
      byte[] xorResult = new byte[phenotyeSize];

      for (int i = 0; i < phenotyeSize; ++i)
        xorResult[i] = (byte) (optimum[i] ^ adaptedIndividual[i]);

      fitnessvalue = this.phenotypeLength * 8
          - this.countOfOneInABytearray(xorResult);
    }

    return fitnessvalue;
  }

  /**
   * Calulate the nonfunctional fitnessvalue for the given
   * <code>individual</code>.
   * 
   * @param individual
   *          The individual to calculate the fitnessvalue.
   * @return Returns the calculate nonfunctional fitnessvalue for the given
   *         <code>individual</code>.
   * @autor Stefan Niemczyk
   * @since Version 0.1.0
   */
  public double getNonFunctionalObjectiveValue(byte[] individual) {
    return individual.length;
  }

  /**
   * Calculate the overfitting value for the given individual. Returns 0 if
   * the individual is not overfitted else returns the number of error
   * positions.
   * 
   * @param individual
   *          The Individual
   * @return Returns 0 if the individual is not overfitted else returns the
   *         number of error positions.
   * @autor Stefan Niemczyk
   * @since Version 0.1.0
   */
  public int overfittingValue(byte[] individual) {
    byte[] adaptedIndividual;
    int phenotyeSize = this.phenotypeLength;
    byte[] optimum = this.optimalTestcase;

    if (individual.length > phenotyeSize) {
      adaptedIndividual = new byte[phenotyeSize];

      for (int i = 0; i < phenotyeSize; ++i)
        adaptedIndividual[i] = individual[i];
    } else if (individual.length < phenotyeSize) {
      adaptedIndividual = new byte[phenotyeSize];
      int individualSize = individual.length;

      for (int i = 0; i < individualSize; ++i)
        adaptedIndividual[i] = individual[i];

      for (int i = individualSize; i < phenotyeSize; ++i)
        adaptedIndividual[i] = (byte) this.OPTIMAL_TESTCASE_VALUE;
    } else
      adaptedIndividual = individual;

    byte[] xorResult = new byte[phenotyeSize];

    for (int i = 0; i < phenotyeSize; ++i)
      xorResult[i] = (byte) (optimum[i] ^ adaptedIndividual[i]);

    return this.phenotypeLength * 8
        - this.countOfOneInABytearray(xorResult);
  }
}
