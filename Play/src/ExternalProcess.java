import java.io.InputStream;

//import org.apache.commons.io.IOUtils;






public class ExternalProcess {

  /**
   * @param args
   */
  public static void main(String[] args) throws Exception{
    ProcessBuilder pb = new ProcessBuilder(args);
    pb.redirectErrorStream(true);
    Process p = pb.start();
    InputStream is = p.getInputStream();
    //String s1 = IOUtils.toString(is);
    p.waitFor();
    is.close();
    //System.out.println(s1);
  }

}
