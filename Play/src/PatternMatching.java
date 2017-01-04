import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PatternMatching {

  /**
   * @param args
   */
  public static void main(String[] args) {
    Pattern pattern = Pattern.compile("%(.*?)%");
    Matcher matcher = pattern.matcher("%JAVA_HOME% %BASE_DIR%/executive %SWITCH_NO% -Xmx");
    while (matcher.find())
    {
        System.out.println(matcher.group(1));
    }

  }

}
