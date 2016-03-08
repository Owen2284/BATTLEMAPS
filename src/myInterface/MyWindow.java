/*
 *  MyWindow.java
 *
 *  Interface for creating windows that appear in the game. Comes with animation handlers.
 *
 */

package myInterface;

import myMain.Board;
import myGraphics.ImageLibrary;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public interface MyWindow {

	int x = 0;
	int y = 0;
	int width = 160;
	int height = 40;

	int animation_width = 0;
	int animation_height = 0;

	public void draw(Graphics g, Board b, ImageLibrary il);

	public void update(Point p);

	public void close();

}