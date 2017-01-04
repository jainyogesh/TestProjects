
public class Test {

	public static void main(String[] args) {
		String include = System.getProperty("include");
		System.out.println(include);
		System.out.println(include);

	}
	
	public static String cleanToBeTokenizedString(String str)
	  {
	    String ret = "";
	    if (str != null && !"".equals(str.trim()))
	    {
	      ret = str.trim().replaceAll("[\\s]*,[\\s]*", ",");
	    }

	    return ret;
	  }

}
