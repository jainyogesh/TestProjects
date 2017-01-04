
public class StringFormatTest {

  /**
   * @param args
   */
  public static void main(String[] args) {
    String replace = "EFT_ACCOUNT";
    String formatted = String.format(" IF EXISTS (SELECT TABLE_NAME FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_NAME = '%s')\n\tDROP VIEW %s", replace,replace);
    System.out.println(formatted);

  }

}
