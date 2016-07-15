/*
 *  Player.java
 *
 *  Class used to player data.
 *
 */

package myGame;

import java.awt.Color;
import java.util.ArrayList;

import myData.Stats;

public class Player {

    // Fields
    private String id;
    private String name;
    private String type = "CPU";
    private String commander = "NONE";
    private Color colour = Color.WHITE;
    private PointSet points = new PointSet();
 	private Stats stats = new Stats();
 	private ArrayList<City> cities = new ArrayList<City>();

    // Constructor for only a player number and name.
    public Player(int inNumber, String inName) {
        this.id = IDCreate.generateID("P", inNumber);
        this.name = inName;
    }

    // Constructor for a player number, name and commander.
    public Player(int inNumber, String inName, String inCommander) {
        this.id = IDCreate.generateID("P", inNumber);
        this.name = inName;
        this.commander = inCommander;
    }

    // Constructor for a player object.
    public Player(Player that) {
        this.id = new String(that.getID());
        this.name = new String(that.getName());
        this.commander = new String(that.getCommander());
    }

    // Basic accessors
    public String getID() {return this.id;}
    public String getName() {return this.name;}
    public String getType() {return this.type;}
    public String getCommander() {return this.commander;}
    public int getPoint(String in) {return this.points.get(in);}
    public PointSet getPointSet() {return this.points;}
    public Color getColour() {return this.colour;}
    
    // Stats super-accessors.
    public int getStat(String in) {return this.stats.get(in);}    
    public Stats getAllStats() {return this.stats;}
 	public String highest(String[] cats) {return stats.highest(cats);}
 	public String lowest(String[] cats) {return stats.lowest(cats);}
 	public double average(String[] cats) {return stats.average(cats);}

	// Basic mutators
	public void setID(String inID) {this.id = inID;}
	public void setName(String inName) {this.name = inName;}
	public void setType(String in) {this.type = in;}
	public void setCommander(String inCommander) {this.commander = inCommander;}
	public void setPoint(String key, int value) {this.points.set(key, value);}
	public void addOwnedCity(City in) {this.cities.add(in);}
	public void removeOwnedCity(City in) {this.cities.remove(in);}
	public void setColour(Color in) {this.colour = in;}
    
	// Stats super-mutators.
	public void setStat(String in, int val) {stats.set(in, val);}
	public void incStat(String in, int val) {stats.inc(in, val);}
	public void decStat(String in, int val) {stats.dec(in, val);}

    // Overwrites.
    public String toString() {
        return "Player \"" + this.name + "\"";
    }

}