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
	
	// Function for advancing one item through a sequence of strings. Loops at end.
	public static String sequenceAdvance(String current, String[] sequence, boolean forwards) {
		for (int i = 0; i < sequence.length; ++i) {
			if (sequence[i].equals(current)) {
				if (forwards) {
					if (i + 1 < sequence.length) {
						return sequence[i+1];
					} else {
						return sequence[0];
					}
				} else {
					if (i - 1 >= 0) {
						return sequence[i-1];
					} else {
						return sequence[sequence.length - 1];
					}
				}
			}
		}
		return "";
	}
	
	// Function for turning an integer to a signed string representation of itself.
	public static String signedString(int in) {
		if (in >= 0) {
			return "+" + Integer.toString(in);
		} else {
			return Integer.toString(in);
		}
	}

}
