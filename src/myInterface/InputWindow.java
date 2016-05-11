package myInterface;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import myGraphics.ImageLibrary;
import myMain.Board;

public class InputWindow extends InfoWindow {
	
	private String[] rootString;
	private int startPos;
	
	private int boxX = 10;
	private int boxY = 50;

	public InputWindow(String title, int x, int y, String[] inRoot) {
		super(title, x, y);
		rootString = inRoot;
		startPos = inRoot[0].length();
		this.addWindowButton(new Button(250, 120, title + "_confirm", "Confirm", 0));
		this.addWindowButton(new Button(20, 120, title + "_clear", "Clear", 38, title));
		this.height = 180;
	}
	
	public String getInputString() {
		if (rootString[0].length() <= startPos) {
			return "";
		} else {
			return rootString[0].substring(startPos);
		}
	}
	
	public void update(Point p) {
		super.update(p);
	}
	
	public void clearText() {
		super.clearText();
		startPos = rootString[0].length();
	}
	
	public void clearAll() {
		super.clearAll();
		startPos = rootString[0].length();
	}
	
	public void draw(Graphics g, Board b, ImageLibrary il) {
		super.draw(g, b, il);
		
		// Drawing of text entry box.
		if (this.getAnimationStatus().equals("Open")) {
			g.setColor(new Color(240,240,240));
			g.fillRect(this.x + this.boxX, this.y + this.boxY + TOP_BAR_HEIGHT, this.width - 10 - this.boxX, 22);
			g.setColor(Color.BLACK);
			g.drawRect(this.x + this.boxX, this.y + this.boxY + TOP_BAR_HEIGHT, this.width - 10 - this.boxX, 22);
			g.drawString(this.getInputString(), this.x + this.boxX + 2, 
					this.y + this.boxY + MyTextMetrics.getTextSize(this.getInputString())[1] + TOP_BAR_HEIGHT + 2);
		}
		
	}

}
