package myData;

import java.util.ArrayList;

import myMain.Board;

public class MyValidator {

	ArrayList<MyValidationRule> catalogue;
	
	public MyValidator() {
		catalogue = new ArrayList<MyValidationRule>();
	}
	
	public void addRule(MyValidationRule r) {
		catalogue.add(r);
	}
	
	public void addRule(String name, String regex) {
		catalogue.add(new MyValidationRule(name, regex));
	}
	
	public void addRule(String name, int max, int min) {
		catalogue.add(new MyValidationRule(name, min, max));
	}
	
	public void addRule(String name, String regex, int min, int max) {
		catalogue.add(new MyValidationRule(name, regex, min, max));
	}
	
	public void removeRule(String name) {
		int toRemove = -1;
		for (int i = 0; i < this.catalogue.size(); ++i) {
			if (this.catalogue.get(i).getName().equals(name)) {
				toRemove = i;
			}
		}
		if (toRemove >= 0) {this.catalogue.remove(toRemove);}
	}
	
	private MyValidationRule findRule(String name) {
		for (MyValidationRule r : catalogue) {
			if (r.getName().equals(name)) {
				return r;
			}
		}
		if (Board.DEBUG_ERROR) {System.out.println("ERROR: MyValidator, findRule(" + name + ") did not find a validation rule.");}
		return null;
	}	
	
	public boolean validate(String name, String data) {
		return this.findRule(name).validate(data);
	}
	
}
