package org.jainy.personal;

public class Parent {

  
  protected final String className = getClass().getName();
  /**
   * @param args
   */
  public static void main(String[] args) {
    System.out.println(new Parent().className);

  }

}
