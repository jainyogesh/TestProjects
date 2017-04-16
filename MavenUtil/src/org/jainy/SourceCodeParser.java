package org.jainy;

import java.util.HashMap;

public class SourceCodeParser extends CodeParser {

  public SourceCodeParser(String dir) {
    super(dir);
  }

  private static HashMap<String, ModuleInfo> ModuleMap = new HashMap<String, ModuleInfo>();

  public static HashMap<String, ModuleInfo> getModuleMap() {
    return ModuleMap;
  }

  @Override
  protected void processModule(ModuleInfo moduleInfo) throws Exception {

    ModuleInfo earlierInfo = ModuleMap.put(moduleInfo.getKey(), moduleInfo);
    if (earlierInfo != null) {
      throw new Exception("Duplicate found " + earlierInfo + " " + moduleInfo);
    }

    // System.out.println("Added: " + moduleInfo.getKey());
  }

}
