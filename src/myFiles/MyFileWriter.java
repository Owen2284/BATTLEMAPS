/*
 *	MyFileWriter.java
 *
 *	A file writing class that ties together all the file writing functions.
 *
 */

package myFiles;

import java.util.ArrayList;
import java.io.*;

public class MyFileWriter {

	public MyFileWriter() {}

	public void writeTextFile(ArrayList<String> inLines, String inFile) {
		try {
			PrintWriter writer = new PrintWriter(inFile, "UTF-8");
			for (String line : inLines) {
				writer.println(line);
			}
			writer.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (UnsupportedEncodingException e) {
			System.out.println(e);
		}
	}

}