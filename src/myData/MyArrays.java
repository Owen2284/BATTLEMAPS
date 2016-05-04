package myData;

public class MyArrays {
	
	// toString function for String arrays.
	public String arrayToString(String[] inArray, String delimeter, String leftBracket, String rightBracket) {
		String returnString = leftBracket;
		for (String str : inArray) {
			returnString += str + delimeter;
		}
		return (returnString.substring(0, returnString.length() - delimeter.length()) + rightBracket);
	}
	
	public String arrayToString(String[] inArray, String delimeter) {
		String returnString = "";
		for (String str : inArray) {
			returnString += str + delimeter;
		}
		return (returnString.substring(0, returnString.length() - delimeter.length()));
	}
	
}
