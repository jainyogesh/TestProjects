package org.jainy.personal;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;


public class URLTest {
  
  // = CONSTANTS
  protected static final char   SEPARATOR = '/';    // pathname separator
  protected static final String JAR_EXT   = ".jar"; // JAR file extension
  protected static final String ZIP_EXT   = ".zip"; // ZIP file extension
  private static   final String TEMP_DIR  = "resources"; // default dir for temp. files
  public static    final String CP_PREFIX = "cp:///"; // prefix for classpath "URLs"

  /**
   * @param args
   * @throws MalformedURLException 
   */
  public static void main(String[] args) throws Exception {
    // TODO Auto-generated method stub
    String path = "/home/jainy/UPP/Switch/install/2.5-SNAPSHOT/cartridges/amex-gcag-2.5-SNAPSHOT-cartridge.jar/";
    if(!(path.indexOf(":/") > 1)){
      path = pathToURL(path) + "/";
    }
    System.out.println(path);
    String[] paths = parsePath(path);
    System.out.println(paths[0]);
    URL url = new URL(paths[0]);
    System.out.println(url.toString());
    
 // get the context path
    String contextPath = contextPath(url.toString());

    // get absolute path
    String path1 = absolutePath(contextPath, "cartrdige.xml");

    // if entering further archives create from scratch
    if (enteredArchiveLocation(path1) != -1) {
      String[] paths1 = parsePath(path1);
      System.out.println(paths1.length);
      System.out.println(paths1[0]);
      System.out.println(paths1[1]);
      String location = paths1[0];
      URL              jarURL = new URL("jar:" + location + "!/");
      JarURLConnection c      = (JarURLConnection) jarURL.openConnection();
      // TODO set c.setUseCaches(false) to allow new files to be read
      System.out.println(c.getJarFile());
    }

  }
  
  private static String[] parsePath(String path) {
    Vector pathBits = new Vector();

/*
    // treat trailing '/' as if it was not there
    if (path.charAt(path.length()-1) == SEPARATOR)
      path = path.substring(0, path.length()-1);
*/

    // descend into dereferenced JAR and ZIP archives
    int jarIdx = enteredArchiveLocation(path);
    while (jarIdx != -1) {
      // add the JAR/ZIP part of path
      jarIdx += 4;
      pathBits.add(path.substring(0, jarIdx));
/// System.out.println("PATH ADD: " + path.substring(0, jarIdx));

      // advance past the archive
      jarIdx++;
      path   = path.substring(jarIdx);
      jarIdx = enteredArchiveLocation(path);
    }

    // add the remaining path
    pathBits.add(path);
/// System.out.println("PATH ADD: " + path);
    return (String[]) pathBits.toArray(new String[0]);
  }
  
  /**
   * Return the index of the first dereferenced archive in a path.
   * If dereference is at the end, the archive is not considered entered.
   *
   * @param  the path
   * @return  the index of the first dereferenced archive, or -1
   */
  protected static int enteredArchiveLocation(String path) {
    path = path.substring(0, path.length()-1);
    int idx = path.toLowerCase().indexOf(JAR_EXT + SEPARATOR);
    if (idx == -1)
      idx = path.toLowerCase().indexOf(ZIP_EXT + SEPARATOR);
    return idx;
  }
  
  
  /**
   * Create a context path.
   * The context path is the directory in which the
   * specified path resides. Paths ending with a '/'
   * are directories in their own right.
   *
   * If no context directory exists null is returned.
   *
   * @param  path
   * @return  the path of the directory
   */
  protected static String contextPath(String path) {
    // is empty return null
    if (path == null || path.length() == 0)
      return null;

    // if no separator return null
    int idx = path.lastIndexOf(SEPARATOR);
    if (idx == -1)
      return null;

    // remove the file and return
    return path.substring(0, idx);
  }

  /**
   * Return an absolute entry pathname
   *
   * @param  context   the full pathname of the context entry
   * @param  location  the new location
   */
  protected static String absolutePath(String context, String location) throws IOException {
    // if location is an absolute path we ignore the context
    if (location.startsWith("/"))
      return location;
    
    // handle ./ and ../
    while (location.startsWith(".")) {
      if (location.startsWith("." + SEPARATOR))
        location = location.substring(2);
      else if (location.startsWith("src" + SEPARATOR)) {
        if (context == null) {
          break;
//          throw new IOException("Illegal entry name");
        }
        location = location.substring(3);
        context = parent(context);
      }
    }

    return context == null ? location : context + SEPARATOR + location;
  }
  
  /**
   * Get pathname of parent entry
   */
  private static String parent(String path) {
    int i = path.lastIndexOf(SEPARATOR);
    if (i <= 0)
      return null;

    return path.substring(0, i);
  }
  
  public static String pathToURL(String path) {
    File f = new File(path);
    path = f.getAbsolutePath();
    return path.startsWith("/") ? "file://" + path : "file:///" + path;
  }


}
