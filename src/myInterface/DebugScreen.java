package myInterface;

import java.awt.Color;
import java.awt.Graphics;

import myMain.Board;

public class DebugScreen extends MyScreen {

	public DebugScreen(Board b, int inWidth, int inHeight) {
		super(b);
		this.title = "Debug Screen";
		this.width = inWidth;
		this.height = inHeight;
		this.init();
	}

	@Override
	public void act() {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(Graphics g) {
		// Sets background color.
		b.setBackground(Color.BLACK);
	}
	
	@Override
	public void init() {
		this.mim.setInterface(this.b.state, this.game);
	}

	@Override
	public void transition() {
		// TODO Auto-generated method stub
	}

}
