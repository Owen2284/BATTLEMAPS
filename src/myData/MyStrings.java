package myData;

public class MyStrings {
	
	// Function for buffering a string to a certain length.
	public String bufferString(String initString, int targetLength, String bufferString) {
		if (bufferString.length() >= 1) {
			String finalString = initString;
			while (finalString.length() < targetLength) {finalString += bufferString;}
			return finalString;
		} else {
			return initString;
		}
	}

}
