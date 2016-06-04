package myData;

import java.util.HashMap;

public class Stats {
	
	HashMap <String, Integer> stats;

	public Stats() {
		stats = new HashMap<String, Integer>();
	}
	
	public int get(String name) {
		Integer returner = stats.get(name);
		if (returner != null) {
			return returner;
		} else {
			return 0;
		}
	}
	
	public void set(String name, int val) {
		stats.put(name, val);
	}
	
	public void inc(String name, int val) {
		stats.put(name, get(name) + val);
	}
	
	public void dec(String name, int val) {
		stats.put(name, get(name) - val);
	}
	
	public String highest(String[] categories) {
		String high = categories[0];
		for (String s : categories) {
			if (get(high) < get(s)) {
				high = s;
			}
		}
		return high;
	}
	
	public String lowest(String[] categories) {
		String low = categories[0];
		for (String s : categories) {
			if (get(low) < get(s)) {
				low = s;
			}
		}
		return low;
	}
	
	public double average(String[] categories) {
		int total = 0;
		for (String s : categories) {
			total += get(s);
		}
		return total / (double) categories.length;
	}
	
	public String toString() {
		String str = "";
		for (String key : stats.keySet()) {
			str += key + ":\t" + get(key) + "\n";
		}
		return str;
	}

}
