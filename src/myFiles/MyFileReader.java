/*
 *	MyFileReader.java
 *
 *	A file reading class that ties together all the file reading functions, and outputs files as
 *	arrays of lines.
 */

package myFiles;

import java.util.ArrayList;
import java.io.*;

public class MyFileReader {

	public MyFileReader() {}

	public ArrayList<String> readTextFile(String filename) {

		// Creates the list to hold all of the file's lines.
		ArrayList<String> lines = new ArrayList<String>();

		try {

			// Creates reader to retrieve data.
			FileReader reader = new FileReader(filename);
			BufferedReader input = new BufferedReader(reader);

			try {

				// Loops through file and loads all the data into the list.
				String newString;
				while ( (newString=input.readLine()) != null) {lines.add(newString.replace("\n", "").replace("\r", ""));}

			}
			catch (IOException error) {     // In case of a readLine error.
				System.out.println(error); 
			}

		}
		catch (FileNotFoundException error) {   // In case file does not exist.
			System.out.println(error);
			return null;
		}

		// Returns the list of lines.
		return lines;  

	}

}