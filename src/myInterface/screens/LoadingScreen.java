package myInterface.screens;

import java.awt.Color;
import java.awt.Graphics;

import myMain.Board;

public class LoadingScreen extends MyScreen {
	
	private String destination;
	private String extraString;
	private int countdown;
	private int initial;

	public LoadingScreen(Board b, int inWidth, int inHeight, String dest, String extra, int minLoadFrames) {
		super(b);
		this.title = "Debug Screen";
		this.width = inWidth;
		this.height = inHeight;
		this.destination = dest;
		this.extraString = extra;
		this.countdown = minLoadFrames;
		this.initial = minLoadFrames;
		this.init();
	}
	
	public LoadingScreen(Board b) {
		this(b, b.windowWidth, b.windowHeight, "Menu", "Main", 100);
	}

	@Override
	public void act() {
		if (countdown <= 0) {
			this.b.switchScreen(destination, extraString);
		} else {
			countdown -= 1;
		}
	}

	@Override
	public void draw(Graphics g) {
		// Sets background color.
		b.setBackground(Color.BLACK);
		g.setColor(Color.GRAY);
		g.draw3DRect(100, height - 60, (int) ((width - 200) * ((initial - countdown) / (float)initial)), 40, true);
		g.setColor(Color.WHITE);
		g.fill3DRect(100, height - 60, (int) ((width - 200) * ((initial - countdown) / (float)initial)), 40, true);
	}
	
	@Override
	public void init() {
		this.mim.initInterface(this.b.state, this);
	}

	@Override
	public void transition() {
		// TODO: Transition system.
	}

}
