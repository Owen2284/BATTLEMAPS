/*
 *  Player.java
 *
 *  Class used to player data.
 *
 */

package myGame;

import java.util.HashMap;

public class Player {

    // Fields
    private String id;
    private String name;
    private String type = "CPU";
    private String commander = "NONE";
    private String colour = "Grey";
    private HashMap stats = new HashMap();

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

    // Private construction code.


    // Accessors
    public String getID() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public String getCommander() {
        return this.commander;
    }

    public int getStat(String key) {
        return (int)this.stats.get(key);
    }

    public boolean hasStatOver(String key, int dc) {
        return (int)this.stats.get(key) > dc;
    }

    // Mutators
    public void setID(String inID) {
        this.id = inID;
    }

    public void setName(String inName) {
        this.name = inName;
    }

    public void setType(String in) {
        this.type = in;
    }

    public void setCommander(String inCommander) {
        this.commander = inCommander;
    }

    public void incStat(String key, int amt) {
        this.stats.put(key, (int)this.stats.get(key) + amt);
    }

    // Overwrites.
    public String toString() {
        return "Player \"" + this.name + "\"";
    }

}