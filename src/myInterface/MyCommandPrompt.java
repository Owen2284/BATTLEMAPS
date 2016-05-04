package myInterface;

import java.util.Scanner;

public class MyCommandPrompt {

	private Scanner in = new Scanner(System.in);

	public MyCommandPrompt() {}
	
	// Basic output function.
	public void print(String text) {
		System.out.println(text);
	}

	// Method for formatting status messages correctly.
	public void status(String text) {
		System.out.println("[*] " + text);
	}

	// Method for formatting error messages correctly.
	public void error(String text) {
		System.out.println("[X] ERROR: " + text);
	}

	// Method to get a user command as individual keywords.
	public String[] getNextCommand() {
		return getCommandString().split(" ");
	}

	// Method to get user input as a single string.
	public String getCommandString() {
		System.out.print("cmd> ");
		return in.nextLine();
	}
	
	// Returns a string from a question.
	public String getAnswer(String question) {
		System.out.print("[?] " + question + ": ");
		return in.nextLine();
	}

	// Method that returns a boolean answer to a yes or no question.
	public boolean getAnswerYN(String question) {
		String answer = getAnswer(question + " (Y/N)");
		return (answer.isEmpty() || answer.toLowerCase().charAt(0) == 'y');
	}

}
