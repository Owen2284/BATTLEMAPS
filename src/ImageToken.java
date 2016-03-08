package myGraphics;

public class ImageToken {
	
	private int x;
	private int y;
	private int imageNumber;

	public ImageToken(int inX, int inY, int inImg) {
		this.x = inX;
		this.y = inY;
		this.imageNumber = inImg;
	}

	public int getX() {return this.x;}
	public int getY() {return this.y;}
	public int getImage() {return this.imageNumber;}

	public void setX(int in) {this.x = in;}
	public void setY(int in) {this.y = in;}
	public void setImage(int in) {this.imageNumber = in;}

}