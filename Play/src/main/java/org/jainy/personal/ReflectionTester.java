package org.jainy.personal;


public class ReflectionTester {

  /**
   * @param args
   * @throws NoSuchFieldException 
   * @throws SecurityException 
   * @throws ClassNotFoundException 
   * @throws IllegalAccessException 
   * @throws IllegalArgumentException 
   */
  public static void main(String[] args) throws SecurityException, NoSuchFieldException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException {
   // System.out.println(Reflector.getFlag());
    
    Class cls = Class.forName("org.jainy.personal.Reflector");
    System.out.println(cls.getModifiers());
   /* Field field = cls.getDeclaredField("flag");
    field.setAccessible(true);
    field.setBoolean(null, false);
    field.setAccessible(false);
    
    System.out.println(Reflector.getFlag());*/
  }

}
