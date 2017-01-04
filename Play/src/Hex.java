




/**
 * Conversion ASCII binary from/to HEX
 * 
 * @author  Long Nguyen - last mods $Author: gold $
 * @version $Revision: 61259 $
 * @date    $Date: 2013-10-22 08:08:40 +0530 (Tue, 22 Oct 2013) $
 */
public final class Hex {
  // = CVS
  public static final String __ID = "$Id: Hex.java 61259 2013-10-22 02:38:40Z gold $";
  
  // = PMD
  private Hex() {}
  
  // = CONSTANTS
  public static final byte[] HEX_TO_ASCII = {
    '0', '1', '2', '3', '4', '5', '6', '7',
    '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
  };
  public static final byte[] ASCII_TO_HEX = { 
    -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,
    -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,
    -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,
     0,   1,   2,   3,   4,   5,   6,   7,   8,   9,  -1,  -1,  -1,  -1,  -1,  -1,
    -1,  10,  11,  12,  13,  14,  15,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,
    -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,
    -1,  10,  11,  12,  13,  14,  15,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,
    -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,
    -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,
    -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,
    -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,
    -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,
    -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,
    -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,
    -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,
    -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1
  };

  public static enum Nibble { LEFT, RIGHT };
  
  // = INTERFACE
  /**
   * Converts a binary buffer to printable HEX representation. Each 
   * byte in the source buffer becomes two bytes in the destination.
   * The caller must make sure that the buffers are large enough.
   * 
   * @param src   the source buffer
   * @param soff  the offset into the source buffer
   * @param slen  the number of source bytes to convert
   * @param dst   the destination buffer
   * @param doff  the offset into the destination buffer
   */
  public static void toHex(byte[] src, int soff, int slen, byte[] dst, int doff) {
    int offs = doff;
    for (int i=0; i<slen; i++) {
      byte b = src[soff+i];
      dst[offs++] = HEX_TO_ASCII[(b >> 4) & 0x0F];
      dst[offs++] = HEX_TO_ASCII[(b >> 0) & 0x0F];
    }
  }
  
  

  /**
   * Converts a byte's left or right nibble into a hex printable char.
   *
   * @param b  byte to convert
   * @param n  left or right nibble of the byte to convert
   * @return the printable char
   */
  public static char toHex(byte b, Nibble n) {
    if (n == Nibble.LEFT) {
      return (char)(HEX_TO_ASCII[(b >> 4) & 0x0F]);
    }
    else {
      return (char)(HEX_TO_ASCII[b & 0x0F]);
    }
  }

  
  
  /**
   * Converts a printable HEX buffer to binary representation. Each 
   * two bytes in the source buffer becomes one byte in the destination.
   * 
   * @param src   the source buffer
   * @param soff  the offset into the source buffer
   * @param slen  the number of source bytes to convert
   * @param dst   the destination buffer
   * @param doff  the offset into the destination buffer
   */
  public static void fromHex(byte[] src, int soff, int slen, byte[] dst, int doff) {
    if ((slen % 2) != 0) {
      throw new IllegalArgumentException("Src length " + slen + " is not a valid hexadecimal length - must be even");
    }
    if (src.length < slen) {
      throw new IllegalArgumentException("Src buffer size " + src.length + " is smaller than src length " + slen);
    }
    if (dst.length * 2 < slen) {
      throw new IllegalArgumentException("Destination buffer size " + dst.length + " is too small to hold converted src of length " + slen);
    }
    int offs = doff;
    for (int i=0; i<slen; i+=2, offs++) {
      byte b1 = src[soff+i];
      byte b2 = src[soff+i+1];
      byte leftNibble = (byte) (ASCII_TO_HEX[b1]);
      if(leftNibble == -1)
        throw new IllegalArgumentException("Character " + (char) b1 + " at index " + i + " is not a valid hexadecimal character");
      byte rightNibble = (byte) (ASCII_TO_HEX[b2]);
      if(rightNibble == -1)
        throw new IllegalArgumentException("Character " + (char) b2 + " at index " + ( i + 1 ) + " is not a valid hexadecimal character");
      dst[offs] = ( byte ) ((leftNibble << 4) | rightNibble);
    }
  } 

  
  public static void main(String[] args){
    String src = "|";
    byte[] dst = new byte[2*src.length()];
    toHex(src.getBytes(), 0, src.length(), dst, 0);
    System.out.println(new String(dst));
  }
}
