package org.jainy.personal;

import java.util.HashMap;
import java.util.Iterator;


public class MapTest {

  /**
   * @param args
   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub
   /* HashMap<String,String> map = new HashMap<String,String>();
    map.put("a", "a");
    map.put("b","b");
    for(Map.Entry<String, String> kv : map.entrySet()){
      System.out.println(kv.getKey());
      kv.setValue("a'");
      
    }
    
    System.out.println(map);*/
    
    HashMap m = new HashMap(2,3);
    m.put("1", "1");
    m.put("2", "1");
    m.put("3", "1");
    m.put("4", "1");
    m.put("5", "1");
    m.put("6", "1");
    System.out.println(m);
    
    Iterator itr = m.entrySet().iterator();
    synchronized(MapTest.class)  {
      
      while(itr.hasNext()){
        itr.next();
        itr.remove();
       
      }
      
    }
    System.out.println(m);

  }

}
