import java.util.Arrays;


public class ArrayTest {

  /**
   * @param args
   */
  public static void main(String[] args) {
    String[] arr = new String[2];
    
    /*for(int i=0; i<arr.length; i++){
      arr[i] = String.valueOf(i);
    }*/
    int i=0;
    for(String tmp: arr ){
      tmp = String.valueOf(++i);
    }
    
    System.out.println(Arrays.toString(arr));

  }

}
