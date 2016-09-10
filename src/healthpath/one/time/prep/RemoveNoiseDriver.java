package healthpath.one.time.prep;

import common.Caller;
import common.Config;

public class RemoveNoiseDriver {

	static String IN_FILE = Config.HOME + Config.RAW_INPUT_FILE;
	static String OUT_FILE = Config.RESULTS_HOME + "TEST" + Config.step0_prepped;
	
	public static void main(String... strings) {
		String msg = ""; 
		msg += "Running this is a 1 time thing.\n";
		msg += "And it takes minutes and minutes ( maybe 10? )\n";
		msg += "Anyhow, if you do want to run this then comment _in_ the actual code\n";
		Caller.log(msg);
		
		
		// REMOVE // to run for real
//		long t1 = System.currentTimeMillis();
//		RemoveNoise rri = new RemoveNoise();
//		rri.read_psv(   100000000, IN_FILE);
//		rri.winnow();
//		rri.marshal(OUT_FILE);
//		long t2 = System.currentTimeMillis();
//		Caller.context_note("The end in " + (t2 - t1) + " milsec ");
	}
}
