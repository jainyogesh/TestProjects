import sun.reflect.Reflection;


public class StaticUtilTest {
	
	public static void main(String[] args){
		System.out.println(StaticUtil.getString());
		
		try {
			//Class.forName("DTOObject");
			StaticUtilTest.class.getClassLoader().loadClass("StaticUtil");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}


}
