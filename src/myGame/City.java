/*
 *  City.java
 *
 *  Class used to represent city and it's infrastructure.
 *
 */

package myGame;

import java.awt.Rectangle;
import java.util.ArrayList;

public class City {

	// City base info.
	private String cID;
	private int x;
	private int y;
	private String name;
	private String oID;

	// City structure info.
	private ArrayList<Block> blocks;
	private ArrayList<Building> buildings;
	private int length; // x distance of city grid
	private int width; // y distance of distance grid

	// Constants
	public static final int CITY_SIZE = 32;
	public static final int GRID_OFFSET_X = 120;
	public static final int GRID_OFFSET_Y = 60;

	// Constructor for only ID and x and y coordinates.
	public City(String inID, int inX, int inY) {
		this.cID = inID;
		this.x = inX;
		this.y = inY;
		this.name = "";
		this.length = 10;
		this.width = 10;
		this.blocks = assembleBlocks();
		this.buildings = new ArrayList<Building>();
		this.oID = "NONE";
	}

	// Constructor for ID, x and y coordinates and city name.
	public City(String inID, int inX, int inY, String inName) {
		this.cID = inID;
		this.x = inX;
		this.y = inY;
		this.name = inName;
		this.length = 10;
		this.width = 10;
		this.blocks = assembleBlocks();
		this.buildings = new ArrayList<Building>();
		this.oID = "NONE";
	}

	// Constructor for ID, x and y coordinates, city name, city length and city width.
	public City(String inID, int inX, int inY, String inName, int inLength, int inWidth) {
		this.cID = inID;
		this.x = inX;
		this.y = inY;
		this.name = inName;
		this.length = inLength;
		this.width = inWidth;
		this.blocks = assembleBlocks();
		this.buildings = new ArrayList<Building>();
		this.oID = "NONE";
	}

	// Constructor for other city.
	public City(City that) {
		this.cID = new String(that.getID());
		this.x = that.getX();
		this.y = that.getY();
		this.name = new String(that.getName());
		this.length = that.getLength();
		this.width = that.getWidth();
		this.blocks = that.getGrid();
		this.buildings = that.getBuildings();
		this.oID = new String(that.getOwner());
	}

	// Common constructor code.
	private ArrayList<Block> assembleBlocks() {

		// Creates return ArrayList.
		ArrayList<Block> tempBlocks = new ArrayList<Block>();

		for (int j = 0; j < this.getWidth(); ++j) {
			for (int i = 0; i < this.getLength(); ++i) {
				// Creating block ID.
				int tempNumber = (i+1) + (j*10);
				String tempID = "B";
				if (tempNumber <= 99) {
					tempID += "0";
				}
				if (tempNumber <= 9) {
					tempID += "0";
				}
				tempID += Integer.toString(tempNumber);

				// Creates city block.
				Block tempBlock = new Block(tempID, i+1, j+1);

				// Adds block to the ArrayList.
				tempBlocks.add(tempBlock);
			}
		}

		// Returns the ArrayList of blocks.
		return tempBlocks;
	}

	// Accessors
	public String getID() {return this.cID;}

	public int getX() {return this.x;}

	public int getY() {return this.y;}

	public String getName() {return this.name;}

	public String getOwner() {return this.oID;}

	public int getLength() {return this.length;}

	public int getWidth() {return this.width;}

	public Block getBlock(String inID) {

		int index = -1;

		for (int i = 0; i < (this.getLength() * this.getWidth()); ++i) {
			if (this.blocks.get(i).getID().equals(inID)) {
				index = i;
			}
		}

		if (index >= 0) {
			return this.blocks.get(index);
		} else {
			return null;
		}
	}

	public Block getBlock(int inX, int inY) {

		int index = -1;

		for (int i = 0; i < (this.getLength() * this.getWidth()); ++i) {
			if (this.blocks.get(i).getX() == inX && this.blocks.get(i).getY() == inY) {
				index = i;
			}
		}

		if (index >= 0) {
			return this.blocks.get(index);
		} else {
			return null;
		}

	}

	public ArrayList<Block> getGrid() {return this.blocks;}

	public ArrayList<Building> getBuildings() {return this.buildings;}

	public int getStat(String key) {
		int total = 0;
		for (Building item : buildings) {
			total += item.getStat(key);
		}
		return total;
	}

	public Rectangle getBounds() {return new Rectangle(x, y, CITY_SIZE, CITY_SIZE);}

	// Mutators
	public void setID(String inID) {this.cID = inID;}

	public void setX(int inX) {this.x = inX;}

	public void setY(int inY) {this.y = inY;}

	public void setName(String inName) {this.name = inName;}

	public void setOwner(String in) {this.oID = in;}

	public void setLength(int inLength) {this.length = inLength;}

	public void setWidth(int inWidth) {this.width = inWidth;}

	public void addBuilding(Building in_building, int in_x, int in_y) {}

	public String toString() {
		return "City \"" + this.getName() + "\" at (" + Integer.toString(this.getX()) + "," + Integer.toString(this.getY()) +  "). ID is *" + this.getID() + "*.";
	}

}