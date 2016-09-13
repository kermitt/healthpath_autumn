package healthpath.reader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import common.Caller;

public class Test_DateMath {

	public static void main(String...strings) {
		date_math(); 
	}
	
	
	
	static void date_math() { 

		
		String dob = "6/15/1916";
		String dod = "12/15/2013";

		Date start = getDate(dob);
		Date end = getDate(dod);
		
		long delta = Math.abs(end.getTime() - start.getTime());
		long days = delta / ( 24 * 60 * 60 * 1000 );
		long years = days /= 365; 
		Caller.log( years + "  dob: " + dob + "   dod: " + dod );
		
	}
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");		
	public static Date getDate(String s) {
		try {
			return sdf.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
}
