package org.jainy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.maven.model.Dependency;

public class TargetCodeParser extends CodeParser {

  private List<String> movedModuleList = new ArrayList<String>();
  private List<String> onlySourceModuleList = new ArrayList<String>();
  private List<String> onlyTargetModuleList = new ArrayList<String>();
  private List<String> dependencyChangeList = new ArrayList<String>();

  public TargetCodeParser(String dir) {
    super(dir);
  }

  @Override
  public void parseDir() throws Exception {
    super.parseDir();
    for (ModuleInfo moduleInfo : SourceCodeParser.getModuleMap().values()) {
      if (!moduleInfo.isProcessed()) {
        onlySourceModuleList.add(moduleInfo.getPath());
      }
    }
  }

  @Override
  protected void processModule(ModuleInfo targetmoduleInfo) throws Exception {
    ModuleInfo srcModuleInfo = SourceCodeParser.getModuleMap().get(targetmoduleInfo.getKey());
    if (srcModuleInfo != null) {
      srcModuleInfo.setProcessed(true);

      String srcPath = srcModuleInfo.getPath();
      String targetPath = targetmoduleInfo.getPath();
      if (!srcPath.equalsIgnoreCase(targetPath)) {
        movedModuleList.add(srcPath + "," + targetPath);
      }

      processDependencies(srcModuleInfo, targetmoduleInfo);

    } else {
      onlyTargetModuleList.add(targetmoduleInfo.getPath());
    }

  }

  private void processDependencies(ModuleInfo src, ModuleInfo target) {
    List<Dependency> srcDependencies = src.getMavenModel().getDependencies();
    List<Dependency> targetDependencies = target.getMavenModel().getDependencies();

    if (srcDependencies.size() != targetDependencies.size()) {
      dependencyChangeList.add(src.getPath() + "," + target.getPath());
      return;
    }

    Set<String> srcDepSet = new HashSet<String>();
    for (Dependency dep : srcDependencies) {
      srcDepSet.add(dep.getArtifactId());
    }

    for (Dependency dep : targetDependencies) {
      if (!srcDepSet.contains(dep.getArtifactId())) {
        dependencyChangeList.add(src.getPath() + "," + target.getPath());
        return;
      }
    }

  }

  public List<String> getMovedModuleList() {
    return movedModuleList;
  }

  public List<String> getOnlySourceModuleList() {
    return onlySourceModuleList;
  }

  public List<String> getOnlyTargetModuleList() {
    return onlyTargetModuleList;
  }

  public List<String> getDependencyChangeList() {
    return dependencyChangeList;
  }

}
