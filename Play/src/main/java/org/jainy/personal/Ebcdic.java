package org.jainy.personal; /** ========================================================
 * @project   Core Switching Application
 * @subsystem Message Parsing
 * @copyright Copyright 2003, Distra Pty Ltd
 * =========================================================
 */


import java.util.Hashtable;

/**
 * EBCDIC encoder.
 * 
 * @version $Revision: 61027 $
 * @date    27/11/2003 - last mods $Date: 2013-10-04 02:58:43 +0530 (Fri, 04 Oct 2013) $
 * @author  Long Nguyen - last mods $Author: rohanl $
 */
 
/*
  ASCII	 HP	 IBM  AT&T   NCR   Diebold
!	21	   4F	  5A	  5A    4F      4F
[	5B	   4A	  5B	  AD    4A      4A
\ 5C          E0          E0      6A
]	5D	   5A	  5D	  BD    5A      5A
^	5E	   5F	  5E	  5F    5F      5F
*/
 
public class Ebcdic {
  
  public static final String __ID = "$Id: Ebcdic.java 61027 2013-10-03 21:28:43Z rohanl $";

  private final int[] from;
  private final int[] to;
  
  private final static String defaultType = "HP";
  private static Hashtable typesMap = new Hashtable();
  
  // = CONSTRUCTOR
  protected Ebcdic(final int[] from, final int[] to) {
    this.from = from;
    this.to = to;
  }
  
  /**
   * Returns an ASCII-EBCDIC converter
   * 
   * @return the converter
   */

  public static Ebcdic Get() {
    return Get(null);
  }
  
  public static Ebcdic Get(String ebcdicType) {
    return createTypedInstance(ebcdicType);
  }
  
  private static synchronized Ebcdic createTypedInstance(String instanceType) {
    if (instanceType == null || instanceType == "") {
      instanceType = defaultType;
    }
    
    instanceType = instanceType.toUpperCase();
    
    if (typesMap.containsKey(instanceType)) {
      return (Ebcdic)typesMap.get(instanceType);
    }
    
    Ebcdic typeInstance = null;
    String instanceName = "com.distra.pm.util.Ebcdic" + instanceType;
    
    try {
      typeInstance = (Ebcdic)Class.forName(instanceName).getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      e.printStackTrace();
      throw new IllegalArgumentException("EBCDIC type = '" + instanceName + "' character set does not exist");
    }
    
    if (typeInstance != null) {
      typesMap.put( instanceType, typeInstance);
    }
    
    return typeInstance;
  }
  
  // = INTERFACE: Encoder
  /**
   * Converts an ASCII byte to an EBCDIC byte
   * 
   * @param b the ASCII byte
   * @return the EBCDIC byte
   */
  public byte fromAscii(byte b) {
    int v = from[b+128];
    return (byte)(v<128 ? v : v-256);
  }

  /**
   * Converts an EBCDIC byte to an ASCII byte
   * 
   * @param b the EBCDIC byte
   * @return the ASCII byte
   */
  public byte toAscii(byte b) {
    int v = to[b+128];
    return (byte)(v<128 ? v : v-256);
  }
  
  public static void main(String[] args){
    Ebcdic ebcdic = Ebcdic.Get();
    for (int i = 0; i < 256; i++) {
      char c = (char)i;
      System.out.println(i + "-->" + Character.toString(c) + ebcdic.fromAscii((byte)c));
    }
  }
  
}
