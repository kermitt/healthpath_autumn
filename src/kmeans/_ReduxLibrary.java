package kmeans;

import java.text.DecimalFormat;

public class _ReduxLibrary {
	public static double roundTwoDecimals(double d) {
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(d));
	}
}
