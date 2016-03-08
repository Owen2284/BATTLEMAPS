/*
 *  GridWindow.java
 *
 *  A window that allows arranging of objects in a grid.
 *
 */

import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;

public class GridWindowOld extends InfoWindow {

	private Object[][] objects;

	public GridWindowOld(String title, int x, int y, int rows, int columns) {
		super(title, x, y);
		objects = new Object[rows][columns];
	}

	// Accessors
	public Object getGridObject(int row, int col) {return objects[row][col];}

	// Mutators
	public void addGridObject(int row, int col, Object obj) {objects[row][col] = obj;}

	// public Object addClonedGridObject(int row, int col, Object obj) {objects[row][col] = obj.clone();}

	public void removeGridObject(int row, int col) {objects[row][col] = null;}

	public Object popGridObject(int row, int col) {Object temp = objects[row][col]; objects[row][col] = null; return temp;}

	// Graphical methods
	public void draw(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(this.x, this.y, this.animation_width, this.animation_height);
		g.setColor(Color.BLACK);
		g.drawRect(this.x, this.y, this.animation_width, this.animation_height);
		if (this.getAnimationStatus().equals("Open")) {
			g.drawRect(this.x, this.y, this.animation_width - TOP_BAR_HEIGHT, TOP_BAR_HEIGHT);
			g.drawString(this.title, this.x + (this.animation_width / 2) - (this.title.length() * 4), this.y + (TOP_BAR_HEIGHT / 2) + 5);
			int x1 = this.x - 80;
			int y1 = this.y + 30 + TOP_BAR_HEIGHT;
			for (String line : this.content.split("\n")) {
				for (String segment : line.split("\t")) {
			    	g.drawString(segment, x1 += 100, y1);
			    }
			    x1 = this.x - 80;
			    y1 += MyTextMetrics.getTextSize("TEST")[1] + line_spacing;
			}

		}
	}

}