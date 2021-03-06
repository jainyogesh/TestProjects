
public class TryCatchFinally {

	public static void main(String[] args) {
		
		System.out.println("test1: " + test1());
		System.out.println("test2: " + test2());
		System.out.println("test3: " + test3());
		System.out.println("test4: " + test4());

	}
	
	private static String test1(){
		String result;
		try{
			result = "In try";
			return result;
		}catch(Exception e){
			result = "In catch";
		}finally{
			result = "In finally";
		}
		return result;
	}
	
	private static String test2(){
		String result;
		try{
			result = "In try";
		}catch(Exception e){
			result = "In catch";
		}finally{
			result = "In finally";
		}
		return result;
	}
	
	private static int test3(){
		int result = 0;
		try{
			result++;
			//System.out.println("Result Value in try " + result);
			return result;
		}catch(Exception e){
			result++;
		}finally{
			result++;
			//System.out.println("Result value in finally " + result);
		}
		return result;
	}

	private static int test4(){
		int result = 0;
		try{
			result++;
			//System.out.println("Result Value in try " + result);
		}catch(Exception e){
			result++;
		}finally{
			result++;
			//System.out.println("Result value in finally " + result);
		}
		return result;
	}

}
