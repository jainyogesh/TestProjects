package org.jainy.personal;

class Reflector {

 private static boolean flag = false;

 static{
   flag = true;
 }
 
 static boolean getFlag(){
   return flag;
 }
}
