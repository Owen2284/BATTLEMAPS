package myInterface;

import java.util.ArrayList;

public class CommandLine {
	
	private ArrayList<String> history;
	private boolean debug = false;
	
	private final int HISTSIZE = 100;
	
	public CommandLine() {
		history = new ArrayList<String>();
	}
	
	public CommandLine(boolean b) {
		history = new ArrayList<String>();
		debug = b;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> getCommandHistory() {return (ArrayList<String>) history.clone();}

	private void print(String c, String m) {
		System.out.println("[" + c + "] " + m);
		history.add(m);
		if (history.size() > HISTSIZE) {
			history.remove(0);
		}
	}
	
	public void alert(String m) {print("!", m);}
	
	public void error(String m) {print("X", m);}
	
	public void debug(String m) {if (debug) {print("?", m);}}
	
	public void general(String m) {print(".", m);}
	
	public void custom(String c, String m) {print(c, m);}

}
