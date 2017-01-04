import java.io.File;
import java.io.IOException;


public class RenameTest {

  /**
   * @param args
   */
  public static void main(String[] args) throws Exception{
    File orig = new File("orig");
    File duplicate = new File("duplicate");
    
    if(!orig.renameTo(duplicate)){
      //In windows a file cannot be renamed to already existing file so lets delete the existing file and then perform rename operation
      if(duplicate.exists()){
        synchronized (RenameTest.class) {
          if (duplicate.delete() && orig.renameTo(duplicate)){
              //continue
          }else{
            throw new IOException("Cannot rename file");
          }
        }
      }else{
        throw new IOException("Cannot rename file");
      }
    }

  }

}
