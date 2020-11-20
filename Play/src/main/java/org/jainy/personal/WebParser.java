package org.jainy.personal;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.XMLReader;


public class WebParser {

	public static void main(String[] args) throws Exception {
		
		jsoup();
		
	}
	
	static void jsoup() throws Exception{
		
		FileReader r = new FileReader("/Users/yogeshjain/Documents/cookie.txt");
		BufferedReader br = new BufferedReader(r);
		String cookie = br.readLine();
		System.out.println(cookie);
		Connection conn = Jsoup.connect("http://userhierarchy.appspot.com/uh?user=seetha.nekkanti@aciworldwide.com");
		conn.cookie("ACSID", cookie);
		conn.execute();
		Document doc = conn.get();
		Element head = doc.head();
		Elements elements = head.getElementsByAttributeValue("type", "text/javascript");
		Pattern p = Pattern.compile("addDataRow\\(.*\n.*\\)");
		Pattern p1 = Pattern.compile("\".*\n.*\"");
		for(int i=0; i < elements.size(); i++){
			Element e = elements.get(i);
			String data = e.data();
			if(data.contains("addDataRow")){
				//System.out.println("Element: " + i + " -->" +data);
				
				Matcher m = p.matcher(data);
				while(m.find()){
					String matched = m.group();
					//System.out.println("Matched: " + matched);
					Matcher m1 = p1.matcher(matched);
					while(m1.find()){
						String finalMatch = m1.group();
						System.out.println("Inner Match: " + Arrays.toString(parse(finalMatch,'"')));
					}
				}
			}
		}
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
	
	static void basic() throws Exception{
		URL url = new URL("http://www.google.com");
		URLConnection conn = url.openConnection();
		conn.connect();
		InputStream in = conn.getInputStream();
		InputStreamReader isr = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(isr);
		String line = null;
		
		while(br.ready() && (line = br.readLine()) != null){
			System.out.println(line);
		}
	}

}
