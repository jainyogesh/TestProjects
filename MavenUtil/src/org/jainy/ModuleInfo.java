package org.jainy;

import org.apache.maven.model.Model;

public class ModuleInfo {

  private Model mavenModel;
  private String path;
  private boolean processed = false;

  public ModuleInfo(Model mavenModel, String path) {
    this.mavenModel = mavenModel;
    this.path = path;
  }

  public String getArtifactId() {
    return this.mavenModel.getArtifactId();
  }

  public String getGroupId() {
    return this.mavenModel.getGroupId();
  }

  public String getPath() {
    return this.path;
  }

  public String getParentId() {
    if (this.mavenModel.getParent() != null )
      return this.mavenModel.getParent().getArtifactId();
    
    return null;
  }

  public String getKey() {
    return this.mavenModel.getArtifactId();
  }
  
  public Model getMavenModel(){
    return this.mavenModel;
  }

  @Override
  public String toString() {
    return "ModuleInfo [mavenModel=" + mavenModel + ", path=" + path + "]";
  }

  public boolean isProcessed() {
    return processed;
  }

  public void setProcessed(boolean processed) {
    this.processed = processed;
  }
}
