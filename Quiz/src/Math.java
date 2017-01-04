import java.math.BigDecimal;


public class Math {

	public static void main(String[] args) {
		System.out.println(2.0 - 1.1);
		double a = 2.0;
		double b = 1.1;
		double c = a - b;
		System.out.println(c);
		
		float d = 2.0f;
		float e = 1.1f;
		float f = d - e ;
		System.out.println(f);
		
		BigDecimal g = new BigDecimal("2.0");
		BigDecimal h = new BigDecimal("1.1");
		BigDecimal i = g.subtract(h);
		System.out.println(i);
		
		BigDecimal j = new BigDecimal(2.0);
		BigDecimal k = new BigDecimal(1.1);
		BigDecimal l = j.subtract(k);
		System.out.println(l);
		
	}

}
