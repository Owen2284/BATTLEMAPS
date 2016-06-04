package myGame;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class PointSet {
	
	// Field
	private HashMap<String, Integer> points;

	// Constructor
	public PointSet() {
		points = new HashMap<String, Integer>();
		points.put("Military", 0);
		points.put("Technology", 0);
		points.put("Nature", 0);
		points.put("Diplomacy", 0);
		points.put("Commerce", 0);
		points.put("Industry", 0);
		points.put("Population", 0);
		points.put("Happiness", 0);
	}
	
	// Accessors
	public int get(String key) {
		if (points.keySet().contains(key)) {
			return points.get(key);
		} else {
			throw new InvalidParameterException("Invalid key entered.");
		}
	}
	
	public Set<String> keys() {return this.points.keySet();}
	
	// Basic mutators
	public void set(String key, int value) {
		if (points.keySet().contains(key)) {
			points.put(key, value);
		} else {
			throw new InvalidParameterException("Invalid key entered.");
		}
	}
	
	public void inc(String key, int value) {
		if (points.keySet().contains(key)) {
			set(key, get(key) + value);
		} else {
			throw new InvalidParameterException("Invalid key entered.");
		}
	}
	
	public void dec(String key, int value) {
		if (points.keySet().contains(key)) {
			set(key, get(key) - value);
		} else {
			throw new InvalidParameterException("Invalid key entered.");
		}
	}
	
	// Merging functions.
	public void add(PointSet that) {
		for (String key : keys()) {
			this.set(key, this.get(key) + that.get(key));
		}
	}
	
	public void subtract(PointSet that) {
		for (String key : keys()) {
			this.set(key, this.get(key) - that.get(key));
		}
	}
	
	// Static merging functions.
	public static PointSet sum(PointSet a, PointSet b) {
		PointSet c = new PointSet();
		c.add(a);
		c.add(b);
		return c;
	}
	
	public static PointSet difference(PointSet a, PointSet b) {
		PointSet c = new PointSet();
		c.add(a);
		c.subtract(b);
		return c;
	}
	
	// Positive/negative points accessors.
	public ArrayList<String> getPositives() {
		ArrayList<String> ret = new ArrayList<String>();
		for (String key : this.keys()) {
			if (get(key) > 0) {
				ret.add(key);
			}
		}
		return ret;
	}
	
	public ArrayList<String> getNegatives() {
		ArrayList<String> ret = new ArrayList<String>();
		for (String key : this.keys()) {
			if (get(key) < 0) {
				ret.add(key);
			}
		}
		return ret;
	}
	
	public ArrayList<String> getNonZeroes() {
		ArrayList<String> ret = getPositives();
		for (String key : getNegatives()) {
			ret.add(key);
		}
		return ret;
	}

}