import java.sql.Timestamp;
import java.text.SimpleDateFormat;


public class DateTest {

	private static final String TIME_FORMAT = "MM/dd/yyyy hh:mm:ss";
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		/*long time  = 1393413908851L;
		
		new Timestamp(time);
		String date = "02/07/1999 22:45:33";
		
		SimpleDateFormat sdf = getFormat(TIME_FORMAT);
		if (date == null){
			return null;
		}
		return sdf.format(date);
		
		SimpleDateFormat sdf = getFormat(TIME_FORMAT);
		if (date == null){
			System.out.println("null");
		}
		System.out.println(new Timestamp((sdf.parse(date)).getTime()));*/

	  java.util.Date testDate = new java.util.Date(1401561000000L);
    System.out.println(testDate);

	}
	
	private static SimpleDateFormat getFormat(String format) {
		return new SimpleDateFormat(format);
	}

}
