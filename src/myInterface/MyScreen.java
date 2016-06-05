package myInterface;

import java.awt.Graphics;

import myGame.Game;
import myGraphics.ImageLibrary;
import myMain.Board;

public abstract class MyScreen {
	
	protected String title = "Screen";
	
	protected int width = 1000;
	protected int height = 600;
	
	protected Board b;
	protected MyInterfaceManager mim;
	protected ImageLibrary il;
	protected Game game;
	
	protected MyScreen(Board parent) {
		this.b = parent;
		this.mim = this.b.mim;
		this.il = this.b.il;
		this.game = this.b.game;
	}
	
	public String getTitle() {return title;}
	public void setTitle(String title) {this.title = title;}
	
	public void act() {
		// N/A
	}
	
	public void draw(@SuppressWarnings("unused") Graphics g) {
		// N/A
	}
	
	public void init() {
		// N/A
	}
	
	public void transition() {
		// N/A
	}

}
