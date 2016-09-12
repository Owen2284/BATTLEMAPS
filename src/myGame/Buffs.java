package myGame;

import java.util.ArrayList;

import myData.Stats;

public class Buffs extends Stats {

	public Buffs() {
		super();
		super.set("ATTUP", 0);	// Attack power up.
		super.set("ATTDW", 0);	// Attack power down.
		super.set("DEFUP", 0);	// Defence power up.
		super.set("DEFDW", 0);	// Defence power down.
		super.set("HAPUP", 0);	// Happiness up.
		super.set("HAPDW", 0);	// Happiness down.
		super.set("POGUP", 0);	// Point gain up.
		super.set("POGDW", 0);	// Point gain down.
		super.set("BLDUP", 0);	// Building speed up.
		super.set("BLDDW", 0);	// Building speed down.
		super.set("POGST", 0);	// Point gain stop.
		super.set("POGSP", 0);	// Point gain syphon.
		super.set("POGMX", 0);	// Point gain mix up.
	}
	
	@Override
	public int get(String key) {
		for (String s : getAllBuffs()) {
			if (s.equals(key)) {
				return super.get(key);
			}
		}
		throw new IllegalArgumentException("Invalid key.");
	}
	
	@Override
	public void set(String key, int value) {
		for (String s : getAllBuffs()) {
			if (s.equals(key)) {
				super.set(key, value);
			}
		}
		throw new IllegalArgumentException("Invalid key.");
	}
	
	public boolean isActive(String key) {return get(key) != 0;}
	public String[] getAllBuffs() {
		ArrayList<String> the = new ArrayList<String>();
		for (String s : this.stats.keySet()) {
			the.add(s);
		}
		String[] ret = new String[the.size()];
		return the.toArray(ret);
	}
	
}
