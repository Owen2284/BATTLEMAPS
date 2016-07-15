package myInterface.screens;

import java.awt.Color;
import java.awt.Graphics;

import myInterface.MyTextMetrics;
import myMain.Board;

public class ImageTestScreen extends MyScreen {
	
	int imageNumber = 1;

	public ImageTestScreen(Board parent, int inWidth, int inHeight) {
		super(parent);
		this.width = inWidth;
		this.height = inHeight;
		this.init();
	}
	
	public ImageTestScreen(Board parent) {
		this(parent, parent.windowWidth, parent.windowHeight);
	}
	
	@Override
	public void act() {
		// N/A
	}
	
	@Override
	public void draw(Graphics g) {
		b.setBackground(Color.LIGHT_GRAY);
		g.setColor(Color.BLACK);
		if (il.notNull(imageNumber)) {
			g.drawImage(il.getImage(imageNumber), 100, 100, b);
		} else {
			g.drawString("NULL", 100, 100 + MyTextMetrics.getTextSizeFlat("NULL")[1]);
		}
		String s = "Image " + imageNumber;
		g.drawString(s, (this.width / 2) - (MyTextMetrics.getTextSizeFlat(s)[0] / 2), this.height - 70);
	}
	
	@Override
	public void init() {
		this.mim.initInterface(this.b.state, this.game);
	}
	
	@Override
	public void transition() {
		// N/A
	}
	
	public void setImageNumber(int i) {
		this.imageNumber = i;
	}

	public int getImageNumber() {
		return this.imageNumber;
	}

}
