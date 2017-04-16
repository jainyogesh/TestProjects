package org.jainy;

import java.io.File;
import java.io.FileFilter;

public abstract class CodeParser {
  
  private String dir;
  
  public CodeParser(String dir){
    this.dir = dir;
  }

  public void parseDir() throws Exception {
    File sourceDir = new File(this.dir);
    if (!sourceDir.isDirectory() || !sourceDir.exists()) {
      throw new Exception("Dir " + this.dir + "is not a real directory");
    }

    parseRecursively(sourceDir);

  }

  private void parseRecursively(File sourceDir) throws Exception {
    File[] children = sourceDir.listFiles(new FileFilter() {

      @Override
      public boolean accept(File child) {
        String name = child.getName();
        if (name.startsWith(".") || name.equals("target") || name.equalsIgnoreCase("src") || name.equalsIgnoreCase("db-updates"))
          return false;

        if (child.isDirectory())
          return true;

        if (name.equalsIgnoreCase("pom.xml"))
          return true;

        return false;
      }
    });
    if (children == null || children.length == 0) {
      return;
    }

    for (File child : children) {
      if (child.isFile() && child.getName().equalsIgnoreCase("pom.xml")) {
        ModuleInfo moduleInfo = PomParser.parsePom(child, this.dir);
        processModule(moduleInfo);
      } else if (child.isDirectory()) {
        parseRecursively(child);
      }
    }
  }

  protected abstract void processModule(ModuleInfo moduleInfo) throws Exception;

}
