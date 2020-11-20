package org.jainy.personal;

public class Child extends Parent {
  
  protected final String className = this.getClass().getName();

  /**
   * @param args
   */
  public static void main(String[] args) {
   
    System.out.println(new Child().className);

  }

}
