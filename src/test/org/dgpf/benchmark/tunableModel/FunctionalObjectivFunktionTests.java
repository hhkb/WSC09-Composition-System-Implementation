package test.org.dgpf.benchmark.tunableModel;

import org.dgpf.benchmark.TunableModel;
import org.sfc.text.TextUtils;

/**
 * @author Locu
 */
public class FunctionalObjectivFunktionTests {

  /**
   * 
   */
  private static TunableModel tunalbeModel;

  /**
   * 
   */
  private static int testSize;

  /**
   * 
   */
  private static int[][][] testcases;

  /**
   * 
   */
  private static byte[][] inputStrings;

  /**
   * 
   */
  private static int[] fitnessValue;

  /**
   * 
   */
  private static int[] fitnessFunktionCount;

  /**
   * 
   */
  private static int[] fitnessFunktionIndex;

  /**
   * 
   */
  private static int[] phenotypeLength;

  /**
   * @param args
   */
  public static void main(String[] args) {
    tunalbeModel = new TunableModel();
    generateTests();
    int i, f, ff;
    boolean c;

    i = 0;
    f = 0;
    ff = 0;

    try {
      tunalbeModel.setEpistasisDifficultyLevel(0);
      tunalbeModel.setNeutralityDifficultyLevel(0);
      tunalbeModel.setRuggednessDifficultyLevel(0);
    } catch (Exception e) {
      e.printStackTrace();
    }

    for (i = 0; i < testSize; ++i) {
      tunalbeModel
          .setFunctionalObjectiveFunctionCount(fitnessFunktionCount[i]);
      tunalbeModel.setPhenotypeLength(phenotypeLength[i]);
      tunalbeModel.setTestCases(testcases[i]);

      System.out.println("Test Nr. " + i); //$NON-NLS-1$
      System.out
          .println("Fitnessfunktion-Index: " + (fitnessFunktionIndex[i] + 1) //$NON-NLS-1$
              + "(" + fitnessFunktionIndex[i] + ")" //$NON-NLS-1$ //$NON-NLS-2$
              + " / " + fitnessFunktionCount[i]); //$NON-NLS-1$
      System.out.println("Input: " + TextUtils.toString(inputStrings[i])); //$NON-NLS-1$

      ff = tunalbeModel.functionalObjectiveFunction(inputStrings[i],
          inputStrings[i].length << 3, fitnessFunktionIndex[i]);

      if (ff != fitnessValue[i]) {
        c = false;
        System.out
            .println("Der Fitnesswert beträgt " + ff + " anstatt von " + fitnessValue[i]); //$NON-NLS-1$//$NON-NLS-2$
      } else
        c = true;

      System.out.println("Correct: " + c); //$NON-NLS-1$
      System.out.println();

      if (!c)
        ++f;
    }

    System.out.println(testSize
        + " Tests, " + (testSize - f) + " correct, " //$NON-NLS-1$//$NON-NLS-2$
        + f + " incorrect"); //$NON-NLS-1$
  }

  /**
   * 
   */
  private static void generateTests() {
    testSize = 11;
    testcases = new int[testSize][1][1];
    inputStrings = new byte[testSize][1];
    fitnessValue = new int[testSize];
    fitnessFunktionCount = new int[testSize];
    fitnessFunktionIndex = new int[testSize];
    phenotypeLength = new int[testSize];

    testcases[0][0] = new int[] { 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1,
        0, 1, 0 };
    inputStrings[0] = new byte[] { (byte) 0x55, (byte) 0x55 };
    fitnessValue[0] = 0;
    fitnessFunktionCount[0] = 1;
    fitnessFunktionIndex[0] = 0;
    phenotypeLength[0] = 16;

    testcases[1][0] = new int[] { 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1,
        0, 1, 0 };
    inputStrings[1] = new byte[] { (byte) 0xfd, (byte) 0x55 };
    fitnessValue[1] = 3;
    fitnessFunktionCount[1] = 1;
    fitnessFunktionIndex[1] = 0;
    phenotypeLength[1] = 16;

    testcases[2][0] = new int[] { 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1,
        0, 1, 0 };
    inputStrings[2] = new byte[] { (byte) 0x55 };
    fitnessValue[2] = 8;
    fitnessFunktionCount[2] = 1;
    fitnessFunktionIndex[2] = 0;
    phenotypeLength[2] = 16;

    testcases[3][0] = new int[] { 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1,
        0, 1, 0 };
    inputStrings[3] = new byte[] { (byte) 0x55, (byte) 0x55 };
    fitnessValue[3] = 12;
    fitnessFunktionCount[3] = 2;
    fitnessFunktionIndex[3] = 1;
    phenotypeLength[3] = 16;

    testcases[4][0] = new int[] { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
        2, 2, 2 };
    inputStrings[4] = new byte[] { (byte) 0xaa, (byte) 0xaa };
    fitnessValue[4] = 0;
    fitnessFunktionCount[4] = 2;
    fitnessFunktionIndex[4] = 1;
    phenotypeLength[4] = 16;

    testcases[5][0] = new int[] { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
        2, 2, 2 };
    inputStrings[5] = new byte[] { (byte) 0xaa, (byte) 0xaa };
    fitnessValue[5] = 0;
    fitnessFunktionCount[5] = 1;
    fitnessFunktionIndex[5] = 0;
    phenotypeLength[5] = 16;

    testcases[6] = new int[2][1];
    testcases[6][0] = new int[] { 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1,
        0, 1, 0 };
    testcases[6][0] = new int[] { 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0,
        1, 0, 1 };
    inputStrings[6] = new byte[] { (byte) 0x55, (byte) 0x55 };
    fitnessValue[6] = 16;
    fitnessFunktionCount[6] = 1;
    fitnessFunktionIndex[6] = 0;
    phenotypeLength[6] = 16;

    testcases[7] = new int[3][1];
    testcases[7][0] = new int[] { 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1,
        0, 1, 0 };
    testcases[7][0] = new int[] { 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1,
        0, 1, 0 };
    testcases[7][0] = new int[] { 1, 0, 1, 0, 1, 0, 1, 2, 1, 0, 1, 0, 1,
        0, 1, 0 };
    inputStrings[7] = new byte[] { (byte) 0xd5, (byte) 0xd5 };
    fitnessValue[7] = 1;
    fitnessFunktionCount[7] = 1;
    fitnessFunktionIndex[7] = 0;
    phenotypeLength[7] = 16;

    testcases[8][0] = new int[] { 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1,
        0, 1, 0 };
    inputStrings[8] = new byte[] { (byte) 0x55, (byte) 0x55, (byte) 0x55 };
    fitnessValue[8] = 0;
    fitnessFunktionCount[8] = 1;
    fitnessFunktionIndex[8] = 0;
    phenotypeLength[8] = 16;

    testcases[9] = new int[3][1];
    testcases[9][0] = new int[] { 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1,
        0, 1, 0 };
    testcases[9][0] = new int[] { 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1,
        0, 1, 0 };
    testcases[9][0] = new int[] { 1, 0, 1, 0, 1, 0, 1, 2, 1, 0, 1, 0, 1,
        0, 1, 0 };
    inputStrings[9] = new byte[] { (byte) 0xd5, (byte) 0xd5, (byte) 0xd5,
        (byte) 0xd5 };
    fitnessValue[9] = 1;
    fitnessFunktionCount[9] = 1;
    fitnessFunktionIndex[9] = 0;
    phenotypeLength[9] = 16;

    testcases[10][0] = new int[] { 1, 0, 1, 0, 1, 0, 1, 0 };
    inputStrings[10] = new byte[] { (byte) 0x55 };
    fitnessValue[10] = 0;
    fitnessFunktionCount[10] = 1;
    fitnessFunktionIndex[10] = 0;
    phenotypeLength[10] = 8;
  }
}
