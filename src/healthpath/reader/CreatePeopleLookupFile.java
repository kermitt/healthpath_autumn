package healthpath.reader;

import java.util.HashSet;
import java.util.Set;

import common.Caller;
import common.Config;
import common.GeneralWriter;

public class CreatePeopleLookupFile {

	// static String OUT_FILE = Config.RESULTS_HOME + "TEST" +
	// Config.step0_prepped;

	public static void main(String... strings) {
		long t1 = System.currentTimeMillis();
		_ReadRawData fup = new _ReadRawData();

		String rawDataFile = Config.HOME + Config.RAW_INPUT_FILE;
		// String rawDataFile = Config.HOME + "tmp_raw.csv";

		fup.read_psv(10000000, rawDataFile);
		long t2 = System.currentTimeMillis();
		Caller.context_note("The end in " + (t2 - t1) + " milsec ");

		Caller.log("Unique " + fup.data.size() + " from " + fup.loop);
		Caller.note(" .................. ");

		String masterDeathFile = Config.HOME + "HealthPath_Deaths_EXPORT.txt";

		long t3 = System.currentTimeMillis();
		_ReadMasterDeathList rmdl = new _ReadMasterDeathList();
		rmdl.read_psv(10000000, masterDeathFile);
		long t4 = System.currentTimeMillis();
		Caller.context_note("The end in " + (t4 - t3) + " milsec ");

		Caller.log("Unique " + rmdl.data.size() + " from " + rmdl.loop);

		Set<String> onlyInMasterDeathList = new HashSet<>();
		Set<String> onlyInRawData = new HashSet<>();
		Set<String> inBoth = new HashSet<>();

		GeneralWriter gw = new GeneralWriter(Config.HOME + "dead_people.psv");
		gw.step1_of_2("pid|gender|dob|dod|age");

		for (String id : fup.data.keySet()) {
			if (rmdl.data.containsKey(id)) {
				inBoth.add(id);
				String info = rmdl.data.get(id);
				gw.step2_of_2(info);

			} else {
				onlyInRawData.add(id);

				// gw.step2_of_2(id + "||||");// 4 pipes?! To keep this .psv
				// balanced

			}
		}

		for (String id : rmdl.data.keySet()) {
			if (fup.data.containsKey(id)) {
				// do nothing
			} else {
				onlyInMasterDeathList.add(id);
			}
		}
		Caller.note(" ............ ");
		Caller.log(onlyInMasterDeathList.size() + " Only in the Master Death List");
		Caller.log(onlyInRawData.size() + " Only in the raw data");
		Caller.log(inBoth.size() + " Intersection");
	}

}
