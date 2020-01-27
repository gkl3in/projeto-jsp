package utils;

public class GenericTools {

	public static Boolean isNull(String value) {

		if (value.isEmpty() || value == null ) {
			return true;
		} else {
			return false;
		}
	}
}
