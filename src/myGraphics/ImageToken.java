package myGraphics;

public class ImageToken {
	
	private int x;
	private int y;
	private int imageNumber;
	private int initX;
	private int initY;

	public ImageToken(int inX, int inY, int inImg) {
		this.x = inX;
		this.y = inY;
		this.imageNumber = inImg;
		this.initX = inX;
		this.initY = inY;
	}

	public int getX() {return this.x;}
	public int getY() {return this.y;}
	public int getImage() {return this.imageNumber;}
	public int getInitX() {return this.initX;}
	public int getInitY() {return this.initY;}

	public void setX(int in) {this.x = in;}
	public void setY(int in) {this.y = in;}
	public void setImage(int in) {this.imageNumber = in;}

}