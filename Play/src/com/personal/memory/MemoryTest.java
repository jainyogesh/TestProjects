package com.personal.memory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public class MemoryTest {

  /**
   * @param args
   * @throws InterruptedException 
   */
  public static void main(String[] args) throws InterruptedException {
    Collection<DataObject> collection;
    //collection = new LinkedList<DataObject>();
    //collection = new HashSet<>();
    Map<Integer,DataObject> map;
    //map = new HashMap<>();
    map = new WeakHashMap<>();
    
    while(true){
      //collection.add(new DataObject());

      DataObject dataObject = new DataObject();
      map.put(dataObject.hashCode(), dataObject);

      //Thread.sleep(0, 1);
    }
  }

}
