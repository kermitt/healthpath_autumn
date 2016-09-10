package healthpath.one.time.prep;

import common.Caller;
import common.PSVReader;
import common.Config;
import java.util.*;

import common.GeneralWriter;
/*
 * RULES:
 *   1: REMOVE redactions and their mates
 *   2: REMOVE personIds which only appear one time
 * In the 22_24_29 dataset these two rules start w/ a dataset of 4.3 million rows
 * and reduces that to 3.2 million rows
 * ...
 * Last time I ran this ( on 1 laptop ) this took 1550428 milsec ( i.e., 25 minutes ) 
 * 
 * Happily...  this is a ONE-TIME-COST :) 
 */

public class RemoveNoise extends PSVReader {


	private LinkedHashMap<String, String> features = Config.getFeatures();
	public Map<String, List<_Entry>> people = new HashMap<>();


	public void marshal(String filepath) {
		Caller.log("WRITING TO: " + filepath + " with ");
		int row_count = 0;
		GeneralWriter gw = new GeneralWriter(filepath);
		gw.step1_of_2(Config.getHeader());
		for (String personId : people.keySet()) {
			List<_Entry> entries = people.get(personId);
			for (_Entry entry : entries) {
				gw.step2_of_2(entry.entry);
				row_count++;
			}
		}
		Caller.log("Total row count: " + row_count);
	}

	private List<_Entry> removeRedactedDyads(List<_Entry> entries) {
		List<Integer> remove = new ArrayList<>();

		for (int i = 0; i < entries.size(); i++) {
			_Entry A = entries.get(i);
			for (int j = 0; j < entries.size(); j++) {
				if (i != j && !remove.contains(i) && !remove.contains(j)) {
					_Entry B = entries.get(j);
					if (A.isPositive != B.isPositive) {
						if (B.composite.equals(A.composite)) {
							remove.add(i);
							remove.add(j);
						}
					}
				}
			}
		}
		Collections.sort(remove);
		Collections.reverse(remove);
		for (int i = 0; i < remove.size(); i++) {
			int killIndex = remove.get(i);
			entries.remove(killIndex);
		}

		return entries;
	}

	public int totalRecords = 0;

	private void removeOneOffs() {
		// REMOVE ONE OFFS!
		// One-off entries are not information
		// They are noise
		List<String> remove = new ArrayList<>();
		for (String pid : people.keySet()) {
			if (people.get(pid).size() == 1) {
				remove.add(pid);
			}
		}

		for (String pid : remove) {
			people.remove(pid);
		}
	}

	public void winnow() {
		int totalPeople = people.size();
		int totalRemovedTrueOneOffs = totalPeople - people.size();
		Caller.log("One off count: " + totalRemovedTrueOneOffs);
		for (String pid : people.keySet()) {
			List<_Entry> entries = people.get(pid);
			entries = removeRedactedDyads(entries);
		}
		removeOneOffs();

		int totalCleanedRemovedTrueOneOffs = totalPeople - people.size();
		Caller.log("Redacted dyad count: " + totalCleanedRemovedTrueOneOffs);
	}

	@Override
	public void populate(String entry) {
		try {

			_Entry _entry = new _Entry(entry);

			String[] pieces = entry.split(",");
			if (pieces.length == features.size()) {

				String pid = pieces[0]; // the person_id

				String composite = "";
				int index = 0;

				for (String feature : features.keySet()) {
					String type = features.get(feature);
					if (type.equals(Config.STRING)) {
						composite += pieces[index];
					} else if (type.equals(Config.DATE)) {
						int days_since_epoch = getDaysSinceEpoch_yyyyddmmhhmmss(pieces[index]);
						composite += "" + days_since_epoch;
					} else if (type.equals(Config.NUMBER)) {
						double d = Double.parseDouble(pieces[index]);
						composite += "" + Math.abs(d);
						_entry.setIsPositive(d);

					} else if (type.equals(Config.MONEY)) {
						double d = Double.parseDouble(pieces[index]);
						composite += "" + Math.abs(d);
						_entry.setIsPositive(d);
					} else if (type.equals(Config.BOOLEAN)) {
						composite += pieces[index];
					}
					index++;
					composite += "|";
				}
				if (!people.containsKey(pid)) {
					List<_Entry> list = new ArrayList<>();
					people.put(pid, list);
				} else {
				}
				_entry.setCompositeKey(composite);
				people.get(pid).add(_entry);
			} else {
				Caller.log("SKIP " + entry);
			}
		} catch (Exception e) {
			Caller.log("ACK! " + entry + " \t" + e.getMessage());
			System.exit(0);
		}
	}
}