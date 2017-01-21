package yjain.personal;

public class Utils {

	public static String sanitizeValue(String value){
		return value.replaceAll("\"", "").trim();
	}

}
