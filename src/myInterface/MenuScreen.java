package myInterface;

import java.awt.Graphics;

import myMain.Board;

public class MenuScreen extends MyScreen {
	
	public MenuScreen(Board b, int inWidth, int inHeight) {
		super(b);
		this.title = "Menu Screen";
		this.width = inWidth;
		this.height = inHeight;
		this.init();
	}
	
	public MenuScreen(Board b) {
		this(b, b.windowWidth, b.windowHeight);
	}

	@Override
	public void act() {}

	@Override
	public void draw(Graphics g) {
		// TODO: Create menu screen.
	}
	
	@Override
	public void init() {
		this.mim.setInterface(this.b.state, this.game);
	}

	@Override
	public void transition() {
		// TODO: Transition system.
	}

}
