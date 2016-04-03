package myInterface;

import java.awt.Graphics;

import myGraphics.ImageLibrary;
import myMain.Board;

public interface MyScreen {
	
	public void act();
	
	public void draw(Graphics g, Board b, ImageLibrary il);
	public void transition();

}
