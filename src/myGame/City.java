/*
 *  City.java
 *
 *  Class used to represent city and it's infrastructure.
 *
 */

package myGame;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import myData.Stats;
import myMain.Board;

public class City {

	// City base info.
	private String cID;
	private int x;
	private int y;
	private String name;
	private Player owner;

	// City structure info.
	private ArrayList<Block> blocks;
	private ArrayList<Building> buildings;
	private int length; // x distance of city grid
	private int width; // y distance of distance grid
	
	// City administrative info.
	private OrdinanceBook ob;
	
	// Stats module.
	private Stats stats = new Stats();

	// Constants
	public static final int CITY_SIZE = 32;
	public static final int GRID_OFFSET_X = 200;
	public static final int GRID_OFFSET_Y = 90;

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
		this.owner = null;
		this.ob = new OrdinanceBook("data/ordinances.csv");
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
		this.owner = that.getOwner();
		this.ob = that.getAllOrdinances();
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
				Block tempBlock = new Block(tempID, i, j);

				// Adds block to the ArrayList.
				tempBlocks.add(tempBlock);
			}
		}

		// Returns the ArrayList of blocks.
		return tempBlocks;
	}

	// Basic accessors
	public String getID() {return this.cID;}
	public int getX() {return this.x;}
	public int getY() {return this.y;}
	public String getName() {return this.name;}
	public Player getOwner() {return this.owner;}
	public int getLength() {return this.length;}
	public int getWidth() {return this.width;}

	public Block getBlock(String inID) {
		int index = -1;
		for (int i = 0; i < (this.getLength() * this.getWidth()); ++i) {
			if (this.blocks.get(i).getID().equals(inID)) {
				index = i;
			}
		}
		if (index >= 0) {return this.blocks.get(index);} else {return null;}
	}

	public Block getBlock(int inX, int inY) {
		int index = -1;
		for (int i = 0; i < (this.getLength() * this.getWidth()); ++i) {
			if (this.blocks.get(i).getX() == inX && this.blocks.get(i).getY() == inY) {
				index = i;
			}
		}
		if (index >= 0) {return this.blocks.get(index);} else {return null;}
	}
	
	public ArrayList<Block> getGrid() {return this.blocks;}
	
	public Point getMousePosOnGrid(Point in) {
		// Scan through all blocks to test for mouse collision.
		for (Block blok : this.blocks) {
			if (blok.isOver(in)) {
				return new Point(blok.getX(), blok.getY());
			}
		}	
		// Return out of bounds point if no collision.
		return new Point(-1, -1);
	}

	public ArrayList<Building> getBuildings() {return this.buildings;}
	public int getPoint(String key) {return getPointSet().get(key);}
	
	public PointSet getPointSet() {
		PointSet ret = new PointSet();
		for (Building b : this.buildings) {
			ret.add(b.getPointSet());
		}
		ret.add(this.ob.sumActive());
		return ret;
	}

	public Rectangle getBounds() {return new Rectangle(x, y, CITY_SIZE, CITY_SIZE);}
	
	public boolean[][] getCollisionMap() {
		
		// Creates collision array.
		boolean[][] colMap = new boolean[this.width][this.length];
		for (int i = 0; i < this.width; ++i) {
			for (int j = 0; j < this.length; ++j) {
				colMap[i][j] = false;
			}
		}
		
		// Mapping each building onto the array.
		for (Building bldg : this.buildings) {
			int pointerX = bldg.getX();
			int pointerY = bldg.getY();
			for (String line : bldg.getBlueprintAsString().split("\n")) {
				for (String character : line.split("|")) {
					if (character.equals("T") && pointerX < this.length && pointerY < this.width) {
						colMap[pointerY][pointerX] = true;
					}
					pointerX += 1;
				}
				pointerX = bldg.getX();
				pointerY += 1;
			}
		}
		
		// Map output
		if (Board.DEBUG_TRACE) {
			for (boolean[] row : colMap) {
				for (boolean b : row) {
					if (b) {
						System.out.print("T");
					} else {
						System.out.print("F");
					}
				}
				System.out.println();
			}
		}
		
		// Returns collision map.
		return colMap;
	
	}
	
	public boolean hasSpaceFor(Building inBuilding, int inX, int inY) {
		// Tests if building can fit at specified location.
		boolean[][] colMap = this.getCollisionMap();
		int currX = inX;
		int currY = inY;
		String blueprint = inBuilding.getBlueprintAsString();
		for (String line : blueprint.split("\n")) {
			for (String character : line.split("|")) {
				// Checks if the blueprint is requesting space at the current position.
				if (character.equals("T") && currX < this.length && currY < this.width) {
					// Returns false if a building is already occupying the space.
					if (colMap[currY][currX]) {
						return false;
					}
				}
				currX += 1;
			}
			currX = inX;
			currY += 1;
		}
		
		// Returns true if building doesn't collide with other buildings.
		return true;
		
	}
	
	// Ordinances super-accessors.
	public OrdinanceBook getAllOrdinances() {return this.ob;}
	public Ordinance getOrdinance(String name) {return this.ob.get(name);}
	public boolean getOrdinanceActivity(String name) {return this.ob.isActive(name);}
	
    // Stats super-accessors.
    public int getStat(String in) {return this.stats.get(in);}    
    public Stats getAllStats() {return this.stats;}
 	public String highest(String[] cats) {return stats.highest(cats);}
 	public String lowest(String[] cats) {return stats.lowest(cats);}
 	public double average(String[] cats) {return stats.average(cats);}

	// Mutators
	public void setID(String inID) {this.cID = inID;}
	public void setX(int inX) {this.x = inX;}
	public void setY(int inY) {this.y = inY;}
	public void setName(String inName) {this.name = inName; incStat("Times renamed", 1);}
	public void setOwner(Player in) {this.owner = in; incStat("Times owner changed", 1);}
	public void setLength(int inLength) {this.length = inLength;}
	public void setWidth(int inWidth) {this.width = inWidth;}

	public void addBuilding(Building inBuilding, int inX, int inY) {
		inBuilding.setX(inX); inBuilding.setY(inY);
		this.buildings.add(inBuilding);
		incStat("Building blueprints placed", 1);
	}
	
	public void removeBuilding(Building inBuilding) {
		this.buildings.remove(inBuilding);
		incStat("Building removed", 1);
	}
	
	public void removeBuildingAt(int inX, int inY) {
		Building foundBldg = findBuildingAt(inX, inY);
		removeBuilding(foundBldg);
	}
	
	public Building popBuildingAt(int inX, int inY) {
		Building foundBldg = findBuildingAt(inX, inY);
		removeBuilding(foundBldg);
		return foundBldg;
	}
	
	public Building getBuildingAt(int inX, int inY) {
		return findBuildingAt(inX, inY);
	}
	
	private Building findBuildingAt(int inX, int inY) {
		// Scans through buildings.
		for (Building building : this.buildings) {
			// Checks if building is in range of the coordinates.
			if ( (inX >= building.getX() && inX < (building.getX() + building.getBlueprintSize()[0]) ) && (inY >= building.getY() && inY < (building.getY() + building.getBlueprintSize()[1]) ) ) {
				// Checks if any part of the building matches the input coordinates.
				int currX = building.getX();
				int currY = building.getY();
				String blueprint = building.getBlueprintAsString();
				for (String line : blueprint.split("\n")) {
					for (String character : line.split("|")) {
						// Checks if the blueprint is requesting space at the current position.
						if (currX < this.length && currY < this.width) {
							if (character.equals("T") && currX == inX && currY == inY) {
								// Returns false if a building is already occupying the space.
								return building;
							}
						}
						currX += 1;
					}
					currX = building.getX();
					currY += 1;
				}
			}
		}
		
		// Returns null if no building is found.
		return null;
		
	}
	
	public int getEmptyGridArea() {
		// Initialisation
		boolean[][] colMap = getCollisionMap();
		int count = 0;
		
		// Counts the empty spaces.
		for (int i = 0; i < this.width; ++i) {
			for (int j = 0; j < this.length; ++j) {
				if (!colMap[i][j]) {++count;}
			}
		}
		
		// Returns the count.
		return count;
		
	}
	
	public boolean hasAreaFor(Building building) {
		return (getEmptyGridArea() >= building.getBlueprintArea());
	}
	
	// Ordinance super-mutators
	public void enactOrdinance(String name) {
		ob.enact(name);
		stats.inc("Ordinances enacted", 1);
	}
	
	public void repealOrdinance(String name) {
		ob.repeal(name);
		stats.inc("Ordinances repealed", 1);
	}
	
	// Stats super-mutators.
	public void setStat(String in, int val) {stats.set(in, val);}
	public void incStat(String in, int val) {stats.inc(in, val);}
	public void decStat(String in, int val) {stats.dec(in, val);}
	
	// Graphical methods.
	public void centerGrid() {
		// TODO: Center grid function.
	}

	// Overrides
	public String toString() {
		return "City \"" + this.getName() + "\" at (" + Integer.toString(this.getX()) + "," + Integer.toString(this.getY()) +  "). ID is *" + this.getID() + "*.";
	}

}