package myInterface.buttons;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

public class ImageButton extends Button {
	
	public ImageButton(int x, int y, String in_id, String b_text, int ex_int, String add_str) {
		super(x, y, in_id, b_text, ex_int, add_str);
	}

	public ImageButton(int x, int y, String in_id, String b_text, int ex_int, String add_str, Image img) {
		super(x, y, in_id, b_text, ex_int, add_str);
		this.image = img;
		// TODO: Correctly implement this.
		//this.width = img.getWidth(null);
		//this.height = img.getHeight(null);
	}

	public ImageButton(ImageButton that) {
		super(that);
		this.image = that.getImage();
	}
	
	// Graphical methods
	public void drawImage(Graphics g) {
		if (image == null) {
			System.out.print("LOOK");
		}
		if (image != null && vis) {
			g.drawImage(image, x, y, null);
		}
	}
	
	public void drawAll(Graphics g, Point p) {
		super.drawAll(g, p);
		this.drawImage(g);
	}

	public void drawBasic(Graphics g, Point p) {                   
		super.drawBasic(g, p);
		this.drawImage(g);
	} 

}
