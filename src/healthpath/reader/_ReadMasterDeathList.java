package healthpath.reader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import common.Caller;
import common.PSVReader;

public class _ReadMasterDeathList extends PSVReader {
	public Map <String, String> data = new HashMap<>(); 
	public int loop = 0;
	private int headerSize = 0; 
	
	public _ReadMasterDeathList() { 
		String header  = "'','person_id','sex','person_id','full_name','name_prefix','first_name','middle_initial','middle_name','last_name','name_suffix','gender_code','gender','date_of_birth','date_of_death','death_verified_code','death_verified_description','social_security_number','member_indicator','current_employee_indicator'";
		headerSize = header.split(",").length;
	}
	
	
	@Override
	public void populate(String entry) {
		try {
			String[] pieces = entry.split(",");
			if (pieces.length == headerSize) {
				String gender = pieces[12];
				String dob = pieces[13];//date of birth 
				String dod = pieces[14];//date of death
				loop++;
				String pid = pieces[1]; // the person_id
				if ( data.containsKey(pid)) {
					// do nothing
				} else {
					long age = getAge(dob,dod); 
					gender = gender.replaceAll("\"","");

					if ( gender.equalsIgnoreCase("Female")) {
						gender = "F";
					} else if ( gender.equalsIgnoreCase("Male")) {
						gender = "M";
					}
					String info = pid + "|" + gender + "|" + dob + "|" + dod + "|" + age;
					info = info.replaceAll("\"","");
					data.put(pid, info);
					//Caller.log(info);
				}
			}
		} catch (Exception e) {
			Caller.log("ACK! " + entry + " \t" + e.getMessage());
			System.exit(0);
		}
	}
	
	
	private long getAge(String dob, String dod ) { 

		dob = dob.replaceAll("\"","");
		dod = dod.replaceAll("\"","");
		
		Date start = getDate(dob);
		Date end = getDate(dod);
		
		long delta = Math.abs(end.getTime() - start.getTime());
		long days = delta / ( 24 * 60 * 60 * 1000 );
		long years = days /= 365; 
		//Caller.log( years + "  dob: " + dob + "   dod: " + dod );
		return years;
	}
	
	private final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");		
	public Date getDate(String s) {
		try {
			return sdf.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
			Caller.log("? " + s );
			System.exit(0);
			return null;
		}
	}
}