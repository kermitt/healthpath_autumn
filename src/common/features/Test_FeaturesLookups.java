package common.features;

import java.util.LinkedHashMap;

import common.Caller;

public class Test_FeaturesLookups {
	public static void main(String...strings) {
		Test_FeaturesLookups tests = new Test_FeaturesLookups();
		tests.getFeatures_forClaims();
		
	}
	private void getFeatures_forClaims() { 
		LinkedHashMap <String,String>lhm = FeaturesLookup_Claims.getFeatures();
		boolean isOk = 14 == lhm.size();
		Caller.log(isOk);
	}
}
