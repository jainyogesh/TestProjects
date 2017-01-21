package yjain.personal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GetDataFromWeb {

	private static String cookie;
	private static final String url = "http://userhierarchy.appspot.com/uh?user=";
	private static final Pattern outerPattern = Pattern.compile("addDataRow\\(.*\n.*\\)");
	private static final Pattern innerPattern = Pattern.compile("\".*\n.*\"");
	
	static {
		FileReader r = null;
		BufferedReader br = null;
		try{
			r = new FileReader("/Users/yogeshjain/Documents/cookie.txt");
			br = new BufferedReader(r);
			cookie = br.readLine();
		}catch (Exception e){
			throw new RuntimeException(e);
		}finally{
			try{
				if(br != null){
					br.close();
				}
				if(r != null){
					r.close();
				}
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		}
	}

	public static List<String[]> getEmployeeData(String email){
		Connection conn = Jsoup.connect(url + email);
		conn.cookie("ACSID", cookie);
		conn.timeout(60000);
		Document doc = null;
		try {
			doc = conn.get();
		} catch (IOException e) {
			System.err.println("Error occured for email: " + email);
			e.printStackTrace();
		}
		return parseResponse(email, doc);
	}

	private static List<String[]> parseResponse(String email, Document doc) {
		Element head = doc.head();
		Elements elements = head.getElementsByAttributeValue("type", "text/javascript");
		List<String[]> directReports = new ArrayList<>();
		for(int i=0; i < elements.size(); i++){
			Element e = elements.get(i);
			String data = e.data();
			if(data.contains("addDataRow")){
				Matcher m = outerPattern.matcher(data);
				while(m.find()){
					String matched = m.group();
					Matcher m1 = innerPattern.matcher(matched);
					while(m1.find()){
						String finalMatch = m1.group();
						//String[] values = finalMatch.split(",");
						String[] values = parse(finalMatch, '"');
						String managerEmail = Utils.sanitizeValue(values[1]);
						if(managerEmail.equals(email)){
							directReports.add(values);
						}
					}
				}
			}
		}
		return directReports;
	}
	
	private static String[] parse(String value, char escapeChar){
		char[] valueArr = value.toCharArray();
		List<String> values = new ArrayList<>();
		int i = 0;
		StringBuilder bl = new StringBuilder();
		for(char c : valueArr){
			if (c == escapeChar){
				if(i == 0){
					i++;
					continue;
				}else{
					i--;
					values.add(bl.toString());
					bl = new StringBuilder();
					continue;
				}
			}
			if(i != 0){
				bl.append(c);
			}
		}
		return values.toArray(new String[0]);
	}

}
