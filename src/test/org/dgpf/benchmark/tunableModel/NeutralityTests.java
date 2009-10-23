package test.org.dgpf.benchmark.tunableModel;

import org.dgpf.benchmark.TunableModel;
import org.sfc.text.TextUtils;

/**
 * 
 * @author Locu
 *
 */
public class NeutralityTests {

  /**
   * 
   */
  private static TunableModel tunalbeModel;
  
  /**
   * 
   */
  private static byte[][] testDatas;
  
  /**
   * 
   */
  private static byte[][] results;
  
  /**
   *
   */
  private static int[] neutralityLevel;
  
  /**
   * 
   */
  private static int testSize;
  
  /**
   * 
   * @param args
   */
  public static void main(String[] args)
  {
    tunalbeModel = new TunableModel();
    generateTests();
    byte[] result;
    int rbs, size, i, j, f;
    boolean c;
    
    i = 0;
    j = 0;
    f = 0;
    
    for( i=0; i < testSize; ++i )
    {
      tunalbeModel.setInternalNeutralityDifficultyLevel(neutralityLevel[i]);
      tunalbeModel.recalculateInternalValues();
      
      System.out.println( "Test Nr. " + i ); //$NON-NLS-1$
      System.out.println( "Neutrality Level: " + tunalbeModel.getInternalNeutralityDifficultyLevel() ); //$NON-NLS-1$
      System.out.println( "Input: " + TextUtils.toString(testDatas[i]) ); //$NON-NLS-1$
      
      size = ( (testDatas[i].length << 3) / (neutralityLevel[i] )) >> 3;
    
      if( (( (testDatas[i].length << 3) / (neutralityLevel[i] )) & 7) != 0 )
        ++size;
      
      result = testDatas[i].clone();
      rbs = TunableModel.neutrality(result, result, testDatas[i].length << 3, neutralityLevel[i]);
      
      System.out.println( "Output: " + TextUtils.toString(result) ); //$NON-NLS-1$
      
      if( rbs > (size << 3) )
        System.out.println( "Size ist false!!" ); //$NON-NLS-1$
      
      c = true;
      for( j = 0; j < size; ++j ){
        if( result[j] != results[i][j] )
        {
          c = false;
          break;
        }
      }
      System.out.println( "Size: " + rbs ); //$NON-NLS-1$
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
    testSize = 6;
    testDatas = new byte[testSize][4];
    results = new byte[testSize][4];
    neutralityLevel = new int[testSize];
    
    testDatas[0] = new byte[]{ (byte) 0xf0, (byte) 0xf0, (byte) 0xf0, (byte) 0xf0};
    results[0] = new byte[]{ (byte) 0xaa };
    neutralityLevel[0] = 4;
    
    testDatas[1] = new byte[]{ (byte) 0xf0, (byte) 0xf0, (byte) 0xf0, (byte) 0xf0};
    results[1] = new byte[]{ (byte) 0xf0, (byte) 0xf0, (byte) 0xf0, (byte) 0xf0};
    neutralityLevel[1] = 1;
    
    testDatas[2] = new byte[]{ (byte) 0xf0, (byte) 0xf0, (byte) 0xf0, (byte) 0xf0};
    results[2] = new byte[]{ (byte) 0x96, (byte) 0x02 };
    neutralityLevel[2] = 3;
    
    testDatas[3] = new byte[]{ (byte) 0xf0, (byte) 0xf0, (byte) 0xf0, (byte) 0xf0};
    results[3] = new byte[]{ (byte) 0x0f };
    neutralityLevel[3] = 8;
    
    testDatas[4] = new byte[]{ (byte) 0xcc, (byte) 0xcc, (byte) 0xcc, (byte) 0xcc};
    results[4] = new byte[]{ (byte) 0xcc, (byte) 0x00 };
    neutralityLevel[4] = 3;
    
    testDatas[5] = new byte[]{ (byte) 0xff, (byte) 0x00, (byte) 0xf4, (byte) 0x50, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xe0};
    results[5] = new byte[]{ (byte) 0x55, (byte) 0x55, (byte) 0x55, (byte) 0x55, (byte) 0x55, (byte) 0x55, (byte) 0x55, (byte) 0x55, (byte) 0x55, (byte) 0x55, (byte) 0x55 };
    neutralityLevel[5] = 8;
  }
}
