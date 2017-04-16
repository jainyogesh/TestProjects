package org.jainy;

public class GenerateSubModules {

  /**
   * @param args
   * @throws Exception 
   */
  public static void main(String[] args) throws Exception {
    
    String module = "/home/jainy/YJ/Codebase/Modular_Trunk/Modules/InfrastrAdmSvc";
    
    SubModuleParser subModulesParser = new SubModuleParser(module);
    subModulesParser.parseDir();

    for(String subModule: subModulesParser.getSubModulesList()){
      System.out.print("'" + subModule + "', ");
    }
  }

}
