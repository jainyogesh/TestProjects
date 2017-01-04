package com.personal.memory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MemoryTest {

  /**
   * @param args
   * @throws InterruptedException 
   */
  public static void main(String[] args) throws InterruptedException {
    List<DataObject> dataList = new LinkedList<DataObject>();
    
    while(true){
      dataList.add(new DataObject());
      Thread.sleep(0, 1);
    }
  }

}
