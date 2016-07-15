package myData;

public class MyStrings {
	
	// Function for buffering a string to a certain length.
	public static String bufferString(String initString, int targetLength, String bufferString) {
		if (bufferString.length() >= 1) {
			String finalString = initString;
			while (finalString.length() < targetLength) {finalString += bufferString;}
			return finalString;
		} else {
			return initString;
		}
	}
	
	// Function for advancing one item through a sequence of strings.
	public static String sequenceAdvance(String current, String[] sequence, boolean forwards) {
		// TODO: Complete sequence advance function.
		return "";
	}

}
