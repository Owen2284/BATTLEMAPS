/*
 *  Block.java
 *
 *  Class used to store details about a single segment of a city's infrastructure.
 *
 */

package myGame;

import java.awt.Point;
import java.awt.Rectangle;

public class Block {

	private String id;
	private int x;
	private int y;

	public static final int BLOCK_SIZE = 20;
	
	// Constructor for empty block.
	public Block(int inX, int inY) {
		this.id = "";
		this.x = inX;
		this.y = inY;
	}

	// Constructor for block with a building.
	public Block(String inID, int inX, int inY) {
		this.id = inID;
		this.x = inX;
		this.y = inY;
	}

	// Accessors
	public String getID() {return this.id;}

	public int[] getCoordinates() {
		int[] coords = new int[2];
		coords[0] = this.x;
		coords[1] = this.y;
		return coords;
	}

	public int getX() {return this.x;}
	public int getY() {return this.y;}
	public Rectangle getBounds() {return new Rectangle(x * BLOCK_SIZE + City.GRID_OFFSET_X, y * BLOCK_SIZE + City.GRID_OFFSET_Y, BLOCK_SIZE, BLOCK_SIZE);}
	public boolean isOver(Point p) {return this.getBounds().contains(p);} 

	// Mutators
	public void setID(String inID) {this.id = inID;}

	public void setCoordinates(int inX, int inY) {this.x = inX; this.y = inY;}

	public void setX(int inX) {this.x = inX;}

	public void setY(int inY) {this.y = inY;}



	// Overwrites.
	public String toString() {
		return "Block " + this.getID() + " at (" + Integer.toString(this.getX()) + "," + Integer.toString(this.getY()) +  ")";
	}

}