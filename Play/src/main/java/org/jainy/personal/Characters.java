package org.jainy.personal;

import java.util.regex.Pattern;

public class Characters {

  /**
   * @param args
   */
  public static void main(String[] args) {
    String X = "[a-zA-Z0-9-/?:().,'+ \\r\\n]"; 
    int length = 75; //26+26+ 10 + 13[/-?:().,'+ CrLf] = 75
    int result = test(X);
    if(length != result){
        System.out.println("X is wrong. Current no. of values " + result + " expected " + length);
    }else{
      System.out.println("X is correct");
    }
    
    String Y = "[A-Z0-9-/?:().,'+= !\"%&*;<>]";
    length = 56; //26 + 10 + 12[ .,-()/='+:?] + 8[!"%&*;<>]
    result = test(Y);
    if(length != result){
      System.out.println("Y is wrong. Current no. of values " + result + " expected " + length);
    }else{
      System.out.println("Y is correct");
    }
    String Z = "[a-zA-Z0-9-/?:().,'+=\\r\\n@#{ !\"%&*;<>_]";
    length = 88; //26 + 26 + 10 + 17[.,-()/='+:?@#CrLf {] + 9[!"%&*;<>_]
    result = test(Z);
    if(length != result){
      System.out.println("Z is wrong. Current no. of values " + result + " expected " + length);
    }else{
      System.out.println("Z is correct");
    }
  }

  private static int test(String X) {
    int ctr = 0;
    try {
      Pattern myPattern = Pattern.compile(X);
      for (int i = 0; i < 256; i++) {
        if (myPattern.matcher(Character.toString((char) i)).matches()){
          //System.out.println(i + " " + Character.toString((char) i));
          ctr++;
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return ctr;
  }
}
