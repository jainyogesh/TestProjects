import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Collections {

	public static void main(String [] args){
		
		//What's the output
		test1();
		test2();
	}

	private static void test1() {
		List col = new ArrayList();
		col.add(1);
		col.add(2);
		col.add(0);
		col.remove(0);
		
		System.out.println(col);
	}

	private static void test2() {
		Collection col = new ArrayList();
		col.add(1);
		col.add(2);
		col.add(0);
		col.remove(0);
		
		System.out.println(col);
	}
}
