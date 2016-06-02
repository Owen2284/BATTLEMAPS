package myGame;

import java.awt.Graphics;
import java.awt.Point;

import myMain.Board;

public class Traveller {
	
	double x;
	double y;
	double speed = 1;
	double toX;
	double toY;
	double gradient;

	public Traveller(City start, City destination) {
		this.x = start.getX() + (City.CITY_SIZE / 2);
		this.y = start.getY() + (City.CITY_SIZE / 2);
		this.toX = destination.getX() + (City.CITY_SIZE / 2);
		this.toY = destination.getY() + (City.CITY_SIZE / 2);
		this.gradient = (x - toX) / (double)(y - toY);
	}
	
	public void move() {
		double distX = toX - x;
		double distY = (toY - y) * gradient;
		distX = distX * Integer.signum((int)distX);
		distY = distY * Integer.signum((int)distY);
		if (distX > distY) {
			if (x < toX) {x += speed;}
			if (x > toX) {x -= speed;}
		} else {
			if (y < toY) {y += speed;}
			if (y > toY) {y -= speed;}
		}
	}
	
	public void draw(Graphics g, Board b) {
		int imgNum = 41;
		int scrollX = b.game.getMap().getScrollX();
		int scrollY = b.game.getMap().getScrollY();
		if (!b.game.getMap().isOverLand(new Point((int)this.x, (int)this.y))) {
			imgNum = 42;
		}
		g.drawImage(b.il.getImage(imgNum), (int)this.x - 4 + scrollX, (int)this.y - 4 + scrollY, b);
	}
	
	public boolean arrived() {
		return (this.x == this.toX) && (this.y == this.toY);
	}
	
	public boolean near() {
		double distX = toX - x;
		double distY = toY - y;
		distX = distX * Integer.signum((int)distX);
		distY = distY * Integer.signum((int)distY);
		return (arrived() || (distX <= speed + 1 && distY <= speed + 1));
	}

}
