package test.org.dgpf.benchmark.tunableModel;

import org.dgpf.benchmark.TunableModel;
import org.sfc.text.TextUtils;

/**
 * 
 * 
 * @author Locu
 *
 */
public class IsOverfittingTests {
  /**
   * 
   */
  private static TunableModel tunableModel;
  
  /**
   * 
   */
  private static int[] neutralityLevel;
  
  /**
   * 
   */
  private static int[] epistasisLevel;
  
  /**
   * 
   */
  private static int[] fitnessFunktionCount;
  
  /**
   * 
   */
  private static byte[][] input;
  
  /**
   * 
   */
  private static boolean[] isOverfitted;
  
  /**
   * 
   */
  private static int testSize;
  
  /**
   * 
   */
  private static int[] phenotypeLength;
  
  /**
   * 
   * @param args
   */
  public static void main(String[] args)
  {
    tunableModel = new TunableModel();
    generateTests();
    int i, f;
    boolean c, b;
    
    i = 0;
    f = 0;
    
    for( i=0; i < testSize; ++i )
    {
      try {
        tunableModel.setInternalNeutralityDifficultyLevel(neutralityLevel[i]);
        tunableModel.setInternalEpistasisDifficultyLevel(epistasisLevel[i]);
        tunableModel.setFunctionalObjectiveFunctionCount(fitnessFunktionCount[i]);
        tunableModel.setPhenotypeLength(phenotypeLength[i]);
      } catch (Exception e) {
        e.printStackTrace();
      }
      
      System.out.println( "Test Nr. " + i ); //$NON-NLS-1$
      System.out.println( "Input: " + TextUtils.toString(input[i]) ); //$NON-NLS-1$
      
      b = tunableModel.isOverfitted(input[i], input[i].length<<3);
      c = true;
      
      if( b != isOverfitted[i] )
      {
        System.out.println( "Overfitted: " + c + " statt " + isOverfitted[i] );  //$NON-NLS-1$//$NON-NLS-2$
        c = false;
      }
      
      System.out.println( "Correct: " + c ); //$NON-NLS-1$
      System.out.println();
      
      if( !c )
        ++f;
    }
    
    System.out.println( testSize + " Tests, " + (testSize - f) + " correct, "   //$NON-NLS-1$//$NON-NLS-2$
        + f + " incorrect"); //$NON-NLS-1$
  }
  
  /**
   * 
   */
  private static void generateTests(){
    testSize = 4;
    neutralityLevel = new int[testSize];
    epistasisLevel = new int[testSize];
    fitnessFunktionCount = new int[testSize];
    input = new byte[testSize][1];
    isOverfitted = new boolean[testSize];
    phenotypeLength = new int[testSize];
    
    neutralityLevel[0] = 0;
    epistasisLevel[0] = 0;
    fitnessFunktionCount[0] = 1;
    input[0] = new byte[]{(byte) 0x55 };
    isOverfitted[0] = false;
    phenotypeLength[0] = 8;
    
    neutralityLevel[1] = 0;
    epistasisLevel[1] = 0;
    fitnessFunktionCount[1] = 1;
    input[1] = new byte[]{(byte) 0x55, (byte) 0x55 };
    isOverfitted[1] = true;
    phenotypeLength[1] = 8;
    
    neutralityLevel[2] = 2;
    epistasisLevel[2] = 3;
    fitnessFunktionCount[2] = 2;
    input[2] = new byte[]{(byte) 0xcc, (byte) 0xc3, (byte) 0xff, (byte) 0x0c };
    isOverfitted[2] = false;
    phenotypeLength[2] = 8;
    
    neutralityLevel[3] = 2;
    epistasisLevel[3] = 3;
    fitnessFunktionCount[3] = 2;
    input[3] = new byte[]{(byte) 0x0c, (byte) 0x0c, (byte) 0x0c, (byte) 0xcc };
    isOverfitted[3] = true;
    phenotypeLength[3] = 8;
  }
}
