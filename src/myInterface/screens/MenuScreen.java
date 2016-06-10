package myInterface.screens;

import java.awt.Color;
import java.awt.Graphics;

import myMain.Board;

public class MenuScreen extends MyScreen {
	
	private String menuState = "";
	
	public MenuScreen(Board b, int inWidth, int inHeight) {
		super(b);
		this.title = "Menu Screen";
		this.width = inWidth;
		this.height = inHeight;
		this.menuState = b.state.substring(5);
		this.init();
	}
	
	public MenuScreen(Board b) {
		this(b, b.windowWidth, b.windowHeight);
	}

	@Override
	public void act() {
		
	}

	@Override
	public void draw(Graphics g) {
		b.setBackground(new Color(120,0,0));
		
		g.drawString(menuState, 20, 20);
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
