package myData;

public class MyArrays {
	
	// toString function for Object arrays.
	public static String arrayToString(Object[] inArray, String delimeter, String leftBracket, String rightBracket) {
		String returnString = leftBracket;
		for (Object obj : inArray) {
			returnString += obj + delimeter;
		}
		return (returnString.substring(0, returnString.length() - delimeter.length()) + rightBracket);
	}
	
	public static Object arrayToString(Object[] inArray, String delimeter) {
		String returnString = "";
		for (Object obj : inArray) {
			returnString += obj + delimeter;
		}
		return (returnString.substring(0, returnString.length() - delimeter.length()));
	}
	
	// toString function for int arrays.
	public static String arrayToString(int[] inArray, String delimeter, String leftBracket, String rightBracket) {
		String returnString = leftBracket;
		for (Object obj : inArray) {
			returnString += obj + delimeter;
		}
		return (returnString.substring(0, returnString.length() - delimeter.length()) + rightBracket);
	}
	
	public static Object arrayToString(int[] inArray, String delimeter) {
		String returnString = "";
		for (Object obj : inArray) {
			returnString += obj + delimeter;
		}
		return (returnString.substring(0, returnString.length() - delimeter.length()));
	}
	
}
