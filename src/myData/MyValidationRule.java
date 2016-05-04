package myData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyValidationRule {
	
	// Fields
	private String name;
	private String regex;
	private int min;
	private int max;
	
	// Constructors
	public MyValidationRule(String name, String regex) {
		this.name = name;
		this.regex = regex;
		this.min = 0;
		this.max = 2147483647;
	}
	
	public MyValidationRule(String name, int min, int max) {
		this.name = name;
		this.regex = ".";
		this.min = min;
		this.max = max;
	}

	public MyValidationRule(String name, String regex, int min, int max) {
		this.name = name;
		this.regex = regex;
		this.min = min;
		this.max = max;
	}
	
	public String getName() {return this.name;}
	public String getRegex() {return this.regex;}
	public int getMinLength() {return this.min;}
	public int getMaxLength() {return this.max;}
	
	public boolean validate(String in) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(in);
		return ((matcher.matches()) && (in.length() >= this.min) && (in.length() <= this.max));
	}
	
}
