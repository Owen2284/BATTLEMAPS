/*
 *  Button.java
 *
 *  Class that represents a button in the game.
 *
 */

package myInterface.buttons;

import myMain.Board;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import myGame.Sprite;
import myInterface.MyTextMetrics;

public class Button extends Sprite {

	// Fields
	private String id;
	private Color colorInner = Color.WHITE;
	private Color colorBorder = Color.BLACK;
	private Color colorHover = Color.YELLOW;
	private Color colorText = Color.BLACK;
	private String buttonText;
	private boolean centerText = true;
	private boolean resizeText = true;
	private int exec;
	private String add;
	private boolean useAdd;
	private String belongsTo;
	private int ownerX;
	private int ownerY;
	private String hoverText = "";

	// Constructors
	public Button(int x, int y) {

		super(x, y);
		this.width = 128;
		this.height = 32;
		this.id = "";
		this.buttonText = "NULL";
		this.exec = -1;
		this.add = "";
		this.useAdd = false;
		this.belongsTo = "Basic";
		this.ownerX = this.x;
		this.ownerY = this.y;

	}

	public Button(int x, int y, String in_id) {

		super(x, y);
		this.width = 128;
		this.height = 32;
		this.id = in_id;
		this.buttonText = "NULL";
		this.exec = -1;
		this.add = "";
		this.useAdd = false;
		this.belongsTo = "Basic";
		this.ownerX = this.x;
		this.ownerY = this.y;

	}

	public Button(int x, int y, String in_id, String b_text, int ex_int) {

		super(x, y);
		this.width = 128;
		this.height = 32;
		this.id = in_id;
		this.buttonText = b_text;
		this.exec = ex_int;
		this.add = "";
		this.useAdd = false;
		this.belongsTo = "Basic";
		this.ownerX = this.x;
		this.ownerY = this.y;

	}

	public Button(int x, int y, String in_id, String b_text, int ex_int, String add_str) {

		super(x, y);
		this.width = 128;
		this.height = 32;
		this.id = in_id;
		this.buttonText = b_text;
		this.exec = ex_int;
		this.add = add_str;
		this.useAdd = true;
		this.belongsTo = "Basic";
		this.ownerX = this.x;
		this.ownerY = this.y;

	}

	public Button(int x, int y, int wid, int hei, String in_id, String b_text, int ex_int) {

		super(x, y);
		this.width = wid;
		this.height = hei;
		this.id = in_id;
		this.buttonText = b_text;
		this.exec = ex_int;
		this.add = "";
		this.useAdd = false;
		this.belongsTo = "Basic";
		this.ownerX = this.x;
		this.ownerY = this.y;

	}

	public Button(int x, int y, int wid, int hei, String in_id, String b_text, int ex_int, String add_str) {

		super(x, y);
		this.width = wid;
		this.height = hei;
		this.id = in_id;
		this.buttonText = b_text;
		this.exec = ex_int;
		this.add = add_str;
		this.useAdd = true;
		this.belongsTo = "Basic";
		this.ownerX = this.x;
		this.ownerY = this.y;

	}

	public Button(Button that) {
		super(that.getX(), that.getY());
		this.vis = that.isVisible();		
		this.width = that.getWidth();
		this.height = that.getHeight();
		this.id = that.getID();	
		this.buttonText = that.getButtonText();
		this.exec = that.getExecutionNumber();
		this.add =  that.getAdditionalString();
		this.useAdd = that.hasAdditionalString();
		this.belongsTo = that.getOwner();
		this.colorInner = that.getColorInner();
		this.colorBorder = that.getColorBorder();
		this.colorText = that.getColorText();
		this.colorHover = that.getColorHover();
		this.ownerX = that.getOwnerX();
		this.ownerY = that.getOwnerY();
	}

	// Accessors
	public String getID() {return this.id;}
	public Color getColorInner() {return this.colorInner;}
	public Color getColorBorder() {return this.colorBorder;}
	public Color getColorText() {return this.colorText;}
	public Color getColorHover() {return this.colorHover;}
	public String getButtonText() {return this.buttonText;}
	public int getExecutionNumber() {return this.exec;}
	public String getAdditionalString() {return this.add;}
	public boolean hasAdditionalString() {return this.useAdd;}
	public String getOwner() {return this.belongsTo;}
	public int getOwnerX() {return this.ownerX;}
	public int getOwnerY() {return this.ownerY;}
	public String getHoverText() {return hoverText;}
	public boolean hasHoverText() {return !hoverText.equals("");}

	// Mutators
	public void setID(String in) {this.id = in;}
	public void setWidth(int in) {this.width = in;}
	public void setHeight(int in) {this.height = in;}
	public void setColorInner(Color in) {this.colorInner = in;}
	public void setColorBorder(Color in) {this.colorBorder = in;}
	public void setColorText(Color in) {this.colorText = in;}
	public void setColorHover(Color in) {this.colorHover = in;}
	public void setButtonText(String in) {this.buttonText = in;}
	public void setExecutionNumber(int in) {this.exec = in;}

	public void setAdditionalString(String in) {
		this.add = in;
		this.useAdd = true;
	}

	public void setAdditionalStringUsage(boolean in) {this.useAdd = in;}
	public void setOwner(String in) {this.belongsTo = in;}
	public void setOwnerX(int in) {this.ownerX = in;}
	public void setOwnerY(int in) {this.ownerY = in;}
	public void setCenterText(boolean in) {this.centerText = in;}
	public void setResizeText(boolean in) {this.resizeText = in;}
	public void setHoverText(String hoverText) {this.hoverText = hoverText;}

	public void cloneFrom(Button that) {
		this.x = that.getX();
		this.y = that.getY();
		this.vis = that.isVisible();
		this.width = that.getWidth();
		this.height = that.getHeight();
		this.id = that.getID();	
		this.buttonText = that.getButtonText();
		this.exec = that.getExecutionNumber();
		this.add =  that.getAdditionalString();
		this.useAdd = that.hasAdditionalString();
		this.belongsTo = that.getOwner();
		this.colorInner = that.getColorInner();
		this.colorBorder = that.getColorBorder();
		this.colorText = that.getColorText();
		this.colorHover = that.getColorHover();
	}

	public void cloneFromNoCoords(Button that) {
		this.vis = that.isVisible();
		this.width = that.getWidth();
		this.height = that.getHeight();
		this.id = that.getID();	
		this.buttonText = that.getButtonText();
		this.exec = that.getExecutionNumber();
		this.add =  that.getAdditionalString();
		this.useAdd = that.hasAdditionalString();
		this.belongsTo = that.getOwner();
		this.colorInner = that.getColorInner();
		this.colorBorder = that.getColorBorder();
		this.colorText = that.getColorText();
		this.colorHover = that.getColorHover();
	}

	// Graphics
	public void drawAll(Graphics g, Point p) {
		this.drawShadow(g);                        
		this.drawBasic(g, p);
	}

	public void drawBasic(Graphics g, Point p) {                   
		if (this.isHovering(p)) {
			this.drawHover(g);
		} else {
			this.drawButton(g); 
		}
	} 

	public void drawButton(Graphics g) {
		
		if (vis) {
			g.setColor(this.colorInner);
			g.fillRect(this.x, this.y, this.width, this.height);
			g.setColor(this.colorBorder);
			g.drawRect(this.x, this.y, this.width, this.height);
			if (buttonText != null && !buttonText.equals("")) {this.drawText(g);}
		}
		
	}

	public void drawButton(Graphics g, int inX, int inY) {
		
		if (vis) {
			g.setColor(this.colorInner);
			g.fillRect(inX, inY, this.width, this.height);
			g.setColor(this.colorBorder);
			g.drawRect(inX, inY, this.width, this.height);
			if (buttonText != null && !buttonText.equals("")) {this.drawText(g);}
		}

	}

	public void drawHover(Graphics g) {

		if (vis) {
			g.setColor(this.colorHover);
			g.fillRect(this.x, this.y, this.width, this.height);
			g.setColor(this.colorBorder);
			g.drawRect(this.x, this.y, this.width, this.height);
			if (buttonText != null && !buttonText.equals("")) {this.drawText(g);}
		}

	}

	public void drawHover(Graphics g, int inX, int inY) {

		if (vis) {
			g.setColor(this.colorHover);
			g.fillRect(inX, inY, this.width, this.height);
			g.setColor(this.colorBorder);
			g.drawRect(inX, inY, this.width, this.height);
			if (buttonText != null && !buttonText.equals("")) {this.drawText(g);}			
		}

	}

	public void drawShadow(Graphics g) {

		if (vis) {
			g.setColor(Color.BLACK);
			g.fillRect(this.x + 5, this.y + 5, this.width, this.height);
		}

	}

	public void drawShadow(Graphics g, int inX, int inY) {

		if (vis) {
			g.setColor(Color.BLACK);
			g.fillRect(inX + 5, inY + 5, this.width, this.height);
		}

	}

	public void drawText(Graphics g) {

		// Sets text color.
		g.setColor(this.colorText);	

		int FUCK_UP_CORRECTION = 4;
		
		// Check for null string.
		if (this.buttonText != null) {

			// Determines whether to resize the text or not.
			if (resizeText) {
	
				// Determines whether to center the text or not.
				if (!centerText) {
	
					// Initialises variables for text resizing.			
					int fontSize = Board.DEFAULT_FONT_SIZE;
					Rectangle buttonHitbox = this.getBounds();
					int[] textSizes = MyTextMetrics.getTextSizeFlat(buttonText);
					Rectangle textHitbox = new Rectangle(this.x + 5, this.y + 15, textSizes[0], textSizes[1]);
	
					// Shrinks text until it fits into the button fully.
					while ((!buttonHitbox.contains(textHitbox)) && (fontSize > 1)) {
						--fontSize;
						g.setFont(new Font(Board.DEFAULTFONTTYPE, Board.DEFAULT_FONT_ATT, fontSize));
						textSizes = MyTextMetrics.getTextSizeFlat(buttonText);
						textHitbox = new Rectangle(this.x + 5, this.y + 15, textSizes[0], textSizes[1]);
					}
	
					// Draws string and resets font.
					g.drawString(this.buttonText, this.x + 5, this.y + 15);
					g.setFont(Board.DEFAULT_FONT);
	
				} else {
	
					// Initialises variables for text resizing.			
					int fontSize = Board.DEFAULT_FONT_SIZE;
					Rectangle buttonHitbox = this.getBounds();
					int[] textSizes = MyTextMetrics.getTextSizeFlat(buttonText);
					// Rectangle textHitbox = new Rectangle((int)( (float)this.x + (this.width / 2.0) - (textSizes[0] / 2.0) ), (int)( (float)this.y + (3 * (this.height / 4)) - (textSizes[1] / 2.0) ), textSizes[0], textSizes[1]);
					Rectangle textHitbox = new Rectangle((int)( (float)this.x + ((this.width - textSizes[0]) / 2.0) ), (int)( (float)this.y + ((this.height - textSizes[1]) / 2.0) ), textSizes[0], textSizes[1]);
	
					// Shrinks text until it fits into the button fully.
					while ((!buttonHitbox.contains(textHitbox)) && (fontSize > 1)) {
						--fontSize;
						g.setFont(new Font(Board.DEFAULTFONTTYPE, Board.DEFAULT_FONT_ATT, fontSize));
						textSizes = MyTextMetrics.getTextSizeFlat(buttonText);
						textHitbox = new Rectangle((int)( (float)this.x + ((this.width - textSizes[0]) / 2.0) ), (int)( (float)this.y + ((this.height - textSizes[1]) / 2.0) ), textSizes[0], textSizes[1]);
					}
	
					// Draws string and resets font.
					g.drawString(this.buttonText, (int)( (float)this.x + ((this.width - textSizes[0]) / 2.0) ), (int)( (float)this.y + ((this.height - textSizes[1]) / 2.0) + (textSizes[1]) ) - FUCK_UP_CORRECTION);
					g.setFont(Board.DEFAULT_FONT);
	
				}
	
			} else {
	
				// Determines whether to center the text or not.
				if (!centerText) {
					g.drawString(this.buttonText, this.x + 5, this.y + 15);
				} else {
					int[] textSizes = MyTextMetrics.getTextSizeFlat(buttonText);
					g.drawString(this.buttonText, (int)( (float)this.x + ((this.width - textSizes[0]) / 2.0) ), (int)( (float)this.y + ((this.height - textSizes[1]) / 2.0) + (textSizes[1]) ) - FUCK_UP_CORRECTION);
				}
	
			}
			
		}

	}

	// Collision detection.
	public boolean isHovering(Point p) {
		Rectangle button_rect = this.getBounds();
		return button_rect.contains(p);
	}

	public boolean isHovering(Point p, int inX, int inY) {
		Rectangle button_rect = new Rectangle(inX, inY, this.width, this.height);
		return button_rect.contains(p);
	}

	// Method overriding
	public String toString() {
		return "Button: " + this.id;
	}

}