/*
 *	MyFileEncryptor.java
 *
 *	Class to encrypt text files.
 *
 */

package myFiles;

import java.util.ArrayList;

public class MyFileEncryptor {
	
	private String style = "Caesar";
	private MyFileHandler rw = new MyFileHandler();

	public MyFileEncryptor(String inStyle) {this.style = inStyle;}

	public static void main(String[] args) {

		String arg_file = args[0];
		String arg_style = args[1];
		String arg_key = args[2];
		String arg_e_d = args[3];

		MyFileEncryptor enc = new MyFileEncryptor(arg_style);
		ArrayList<String> file = enc.readTextFile(arg_file);
		
		if (arg_e_d.equals("Encrypt")) {
			file = enc.encryptFile(file, arg_key);
		} else if (arg_e_d.equals("Decrypt")) {
			file = enc.decryptFile(file, arg_key);
		}

		enc.writeTextFile(file, arg_file);

	}

	public ArrayList<String> readTextFile(String inFile) {
		return rw.readTextFile(inFile);
	}

	public void writeTextFile(ArrayList<String> inLines, String inFile) {
		rw.writeTextFile(inLines, inFile);
	}

	// Ecryption methods.
	public ArrayList<String> encryptFile(ArrayList<String> inFile, String inKey) {

		// Creates new list to store encrypted lines.
		ArrayList<String> encryptedLines = new ArrayList<String>();

		// Loops through each line of the file, and encrypts the line.
		for (String line : inFile) {
			// Code for line by line encryptors.
			if (this.style.equals("Caesar")) {
				encryptedLines.add(encryptLine(line, inKey));
			}
			// Code for entire file encryptors.
			else if (false) {
				// Pass
			}

		}

		// Returns the encrypted file.
		return encryptedLines;

	}

	public String encryptLine(String inLine, String inKey) {

		String returnLine = "";

		if (this.style.equals("Caesar")) {
			String[] CHARACTERS = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", 
								   "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", 
								   "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", " ", ",", ".", "|", "!", "?", "#", "(", 
								   ")", "[", "]", "{", "}", "'", "\"", "*", "@"};
			String[] splitLine = inLine.split("");
			for (String splitChar : splitLine) {
				for (int i = 0; i < CHARACTERS.length; ++i) {
					if (splitChar.equals(CHARACTERS[i])) {
						int arrayIndex = (i + Integer.parseInt(inKey)) % CHARACTERS.length;
						if (arrayIndex < 0) {
							arrayIndex += CHARACTERS.length;
						}
						returnLine += CHARACTERS[arrayIndex];
					}
				}
			}
		}

		return returnLine;

	}

	// Decryption method.
	public ArrayList<String> decryptFile(ArrayList<String> inFile, String inKey) {

		// Creates new list to store decrypted lines.
		ArrayList<String> decryptedLines = new ArrayList<String>();

		// Code for decrypting a Caesar cipher.
		if (this.style.equals("Caesar")) {
			decryptedLines = this.encryptFile(inFile, Integer.toString(-1 * Integer.parseInt(inKey)));
		}

		// Returns the decrypted file.
		return decryptedLines;

	}

}