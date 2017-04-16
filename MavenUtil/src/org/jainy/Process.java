package org.jainy;

public class Process {

  /**
   * @param args
   */
  public static void main(String[] args) throws Exception {

    String sourceDir = "/home/jainy/YJ/Codebase/Trunk";
    String targetDir = "/home/jainy/YJ/Codebase/Modular_Trunk";

    CodeParser sourceCodeParser = new SourceCodeParser(sourceDir);
    sourceCodeParser.parseDir();
    //System.out.println(SourceCodeParser.getModuleMap());

    TargetCodeParser targetCodeParser = new TargetCodeParser(targetDir);
    targetCodeParser.parseDir();

    System.out.println("Moved Modules: ");
    for (String path : targetCodeParser.getMovedModuleList()) {
      System.out.println(path);
    }

    System.out.println("==================================================");

    System.out.println("Only Source Modules");
    for (String path : targetCodeParser.getOnlySourceModuleList()) {
      System.out.println(path);
    }

    System.out.println("==================================================");

    System.out.println("Only Target Modules");
    for (String path : targetCodeParser.getOnlyTargetModuleList()) {
      System.out.println(path);
    }

    System.out.println("==================================================");

    System.out.println("Dependency Change Modules");
    for (String path : targetCodeParser.getDependencyChangeList()) {
      System.out.println(path);
    }

  }

}
