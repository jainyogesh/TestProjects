import java.io.*;

public class Main1 {
  public static void main(String[] args) throws IOException {
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    String s;
    while ((s = in.readLine()) != null) {
    	String[] inputs = s.split(",");
    	int len0 = inputs[0].length();
    	int len1 = inputs[1].length();
    	int shorter = len0 < len1 ? len0 : len1;
    	StringBuilder result = new StringBuilder();
       	for(int j = 0; j < shorter; j++){
    		if(inputs[0].charAt(len0-j-1) == inputs[1].charAt(len1-j-1)){
    			result.append((inputs[0].charAt(len0-j-1)));
    		}else{
       			break;
    		}
    	}
       	if(result.length() == 0){
       		System.out.println("NULL");
       	}else{
       		System.out.println(result.reverse());
       	}
    	//System.out.println(s);
    }
  }
}
