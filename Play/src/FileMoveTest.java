import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;



public class FileMoveTest {

  private static final String SOURCE = "source";
  private static final String DESTINATION = "destination";
  private static int noOfRetries = 1;
  private static int retryDelay = 0;
  private static FileMoveTest _instance = null;
  
  public static void main(String[] args) throws Exception{
    if(args.length > 0){
      String arg1 = args[0];
      if(arg1.equals("--help") || arg1.equals("-help")){
        System.out.println("Usage: java " + FileMoveTest.class.getName() + " [noOfRetries retryDelay]");
        System.exit(0);
      }
      
      try {
        noOfRetries = Integer.parseInt(arg1);
        
        if(args.length > 1){
          retryDelay = Integer.parseInt(args[1]);
        }
      } catch (NumberFormatException e) {
        System.err.println("noOfRetries | retryDelay should be valid integers");
        System.exit(1);
      }
    }
    
    _instance = new FileMoveTest();
    _instance.testDestinationNonExistent();
    _instance.testDestinationExistent();
    _instance.testDestinationOpen();
  }
  
  public File move(String source, String destination) throws Exception{
    File srcFile = new File(source);
    File destFile = new File(destination);
    if(!srcFile.exists()){
      throw new FileNotFoundException("Source File: " + source + " does not exists");
    }
    for(int i=0 ; i < noOfRetries; i++){
      try {
        Files.move(srcFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        break;
      } catch (IOException e) {
        //The file move failed. If retryDelay is greater then 0, then wait for that amount of time
        if(retryDelay > 0 && i  < noOfRetries-1){
          try {
            Thread.sleep(retryDelay);
          } catch (InterruptedException e1) {
           //Ignore
          }
        }else{
          throw e;
        }
      }
    }
    return destFile;
  }
  
  private File createFile(String name) throws Exception{
    OutputStream out = null;
    File file = new File(name);
    try{
      out = new PrintStream(new BufferedOutputStream( new FileOutputStream(file, false)));
      out.write(name.getBytes());
    }finally{
      out.close();
    }
    return file;
  }
  
  private void deleteFile(String name) throws Exception{
    File file = new File(name);
    if(file.exists() && !file.delete()){
      throw new Exception("File: " + name + " cannot be deleted");
    }
  }
  
  private void testDestinationNonExistent() throws Exception{
    createFile(SOURCE);
    File destFile = move(SOURCE,DESTINATION);
    try{
      if(destFile.length() != SOURCE.getBytes().length){
        throw new Exception("testDestinationNonExistent failed. Length expected:" + SOURCE.getBytes().length + " got:" + destFile.length());
      }
      System.out.println("testDestinationNonExistent passed!!");
    }finally{
      deleteFile(DESTINATION);
    }
  }
  
  private void testDestinationExistent() throws Exception{
    File srcFile = createFile(SOURCE);
    if(srcFile.length() != SOURCE.getBytes().length){
      throw new Exception("testDestinationExistent failed during SOURCE creation. Length expected:" + SOURCE.getBytes().length + " got:" + srcFile.length());
    }
    
    File destFile = createFile(DESTINATION);
    if(destFile.length() != DESTINATION.getBytes().length){
      throw new Exception("testDestinationExistent failed during DESTINATION creation. Length expected:" + DESTINATION.getBytes().length + " got:" + destFile.length());
    }
    
    File destNewFile = move(SOURCE,DESTINATION);
    try{
      if(destNewFile.length() != SOURCE.getBytes().length){
        throw new Exception("testDestinationExistent failed. Length expected:" + SOURCE.getBytes().length + " got:" + destNewFile.length());
      }
      System.out.println("testDestinationExistent passed!!");
    }finally{
      deleteFile(DESTINATION);
    }
  }
  
  private void testDestinationOpen() throws Exception{
    File srcFile = createFile(SOURCE);
    if(srcFile.length() != SOURCE.getBytes().length){
      throw new Exception("testDestinationOpen failed during SOURCE creation. Length expected:" + SOURCE.getBytes().length + " got:" + srcFile.length());
    }
    
    File destFile = createFile(DESTINATION);
    if(destFile.length() != DESTINATION.getBytes().length){
      throw new Exception("testDestinationOpen failed during DESTINATION creation. Length expected:" + DESTINATION.getBytes().length + " got:" + destFile.length());
    }
    
    
    
    BufferedReader rd = new BufferedReader( new FileReader(destFile));
    String line = null;
    while(( line = rd.readLine()) != null){
      //Let it read the line but never close the stream
    }
    
    File destNewFile = move(SOURCE,DESTINATION);
    try{
      if(destNewFile.length() != SOURCE.getBytes().length){
        throw new Exception("testDestinationOpen failed. Length expected:" + SOURCE.getBytes().length + " got:" + destNewFile.length());
      }
      System.out.println("testDestinationOpen passed!!");
    }finally{
     // deleteFile(DESTINATION);
    }
  }

}
