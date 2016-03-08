/*
 *	MyFileHandler.java
 *
 *	Ties together MyFileReader and MyFileWriter into one class.
 *
 */

package myFiles;

import java.util.ArrayList;

public class MyFileHandler {

	MyFileReader r = new MyFileReader();
	MyFileWriter w = new MyFileWriter();

	public MyFileHandler() {}

	public ArrayList<String> readTextFile(String inFile) {
		return r.readTextFile(inFile);
	}

	public void writeTextFile(ArrayList<String> inLines, String inFile) {
		w.writeTextFile(inLines, inFile);
	}

}