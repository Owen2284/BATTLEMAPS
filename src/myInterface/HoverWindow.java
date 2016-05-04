/*
 *  HoverWindow.java
 *
 *  A window that appears when objects are hovered over.
 *
 */

package myInterface;

import myGraphics.ImageLibrary;
import myMain.Board;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class HoverWindow implements MyWindow {

	private int x;
	private int y;	
	private int width = 80;
	private int height = 20;
	private boolean active = false;
	private boolean open = true;

	private String content = "";

	private int animation_width = 0;
	private int animation_height = 0;

	// Constructor
	public HoverWindow(int x, int y) {
		this.x = x;
		this.y = y;
	}

	// Accessors
	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public boolean isActive() {
		return this.active;
	}

	public boolean isOpen() {
		return this.open;
	}

	public String getContent() {
		return this.content;
	}

	public String getAnimationStatus() {
		if (this.animation_width == this.width && this.animation_height == this.height) {
			return "Open";
		} else if (this.animation_width == 0 && this.animation_height == 0) {
			return "Closed";
		} else {
			return "In progress";
		}
	}

	// Mutators
	public void setX(int in) {
		this.x = in;
	}

	public void setY(int in) {
		this.y = in;
	}

	public void setWidth(int in) {
		this.width = in;
	}

	public void setHeight(int in) {
		this.height = in;
	}

	public void setActive(boolean in) {
		this.active = in;
	}

	public void setOpen(boolean in) {
		this.open = in;
	}

	public void setContent(String in) {
		this.content = in;
	}

	public void draw(Graphics g, Board b, ImageLibrary il) {
		g.setColor(Color.WHITE);
		g.fillRect(this.x, this.y, this.animation_width, this.animation_height);
		g.setColor(Color.BLACK);
		g.drawRect(this.x, this.y, this.animation_width, this.animation_height);
		if (this.getAnimationStatus().equals("Open")) {
			g.drawString(this.content, this.x + (this.width / 2) - (MyTextMetrics.getTextSize("View Player")[0] / 2) + 5, this.y + 15);
		}
	}

	public void update(Point p) {
		this.x = p.x + 5;
		this.y = p.y + 5;
		if (active) {
			if (animation_width <= width) {
				animation_width += (width / 20) + 1;
				if (animation_width > width) {
					animation_width = width;
				}
			}
			if (animation_height <= height) {
				animation_height += (height / 20) + 1;
				if (animation_height > height) {
					animation_height = height;
				}
			}
		}
	}

	public void close() {
		this.open = false;
	}

}