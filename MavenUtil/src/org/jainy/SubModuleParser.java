package org.jainy;

import java.util.ArrayList;
import java.util.List;

public class SubModuleParser extends CodeParser {

  private List<String> subModulesList = new ArrayList<String>();

  public SubModuleParser(String dir) {
    super(dir);
  }

  @Override
  protected void processModule(ModuleInfo moduleInfo) throws Exception {
    this.subModulesList.add(moduleInfo.getArtifactId());
  }

  public List<String> getSubModulesList() {
    return subModulesList;
  }

}
