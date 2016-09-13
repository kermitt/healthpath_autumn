//public class  {
package healthpath.reader;

import java.util.HashMap;
import java.util.Map;

import common.Caller;
import common.PSVReader;

public class _ReadRawData extends PSVReader {
	public Map <String, String> data = new HashMap<>(); 
	public int loop = 0;
	@Override
	public void populate(String entry) {
		try {
			String[] pieces = entry.split(",");
			if (pieces.length == 14) {
				loop++;
				String pid = pieces[0]; // the person_id

				if ( data.containsKey(pid)) {
					// do nothing
				} else {
					data.put(pid, pid);
				}
				
			}
		} catch (Exception e) {
			Caller.log("ACK! " + entry + " \t" + e.getMessage());
			System.exit(0);
		}
	}
}
