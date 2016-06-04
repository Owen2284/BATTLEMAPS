package myInterface;

import java.awt.Point;

public class ErrorWindow extends InfoWindow {
	
	private int timeToClose = 160;

	public ErrorWindow(String inTitle, int inX, int inY, String error) {
		super(inTitle, inX, inY);
		this.height = 100;
		this.width = 200;
		this.animationSpeed = 4;
		this.content = error;
		this.contentX = -10;
		this.removeAllButtons();
		
		while (MyTextMetrics.getTextSizeComplex(this.content)[0] + 20 + this.contentX > this.width) {
			this.width += 10;
		}
		
	}
	
	// Getters
	public int getTimeToClose() {return this.timeToClose;}
	
	// Setters
	public void setTimeToClose(int in) {this.open = true; this.timeToClose = in;}
	
	// Overrides
	public void update(Point p) {
		
		// Run super's updates.
		super.update(p);
		
		// Decrease timer and terminate window if done.
		if (this.timeToClose <= 0) {
			this.open = false;
		} else {
			--this.timeToClose;
		}
		
	}

}
