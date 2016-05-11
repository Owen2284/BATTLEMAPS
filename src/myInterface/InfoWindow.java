/*
 *  InfoWindow.java
 *
 *  A window that appears to display the game's information to the player.
 *
 */

package myInterface;

import myGraphics.ImageToken;
import myGraphics.ImageLibrary;
import myMain.Board;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class InfoWindow implements MyWindow {

	protected String title;
	protected int x;
	protected int y;	
	protected int width = 400;
	protected int height = 300;
	protected boolean open = true;

	protected String content = "";
	protected int contentX = 0;
	protected int contentY = 0;

	protected int animationWidth = 0;
	protected int animationHeight = 0;
	protected int animationSpeed = 1;
	protected boolean isMoving = false;
	
	protected Color topBarColor = Color.WHITE;
	
	protected int lineSpacing = 0;

	protected ArrayList<ImageToken> windowImages = new ArrayList<ImageToken>();
	protected ArrayList<Button> windowButtons = new ArrayList<Button>();
	protected Button closeButton;
	protected Button returnButton;

	public static final int TOP_BAR_HEIGHT = 24;

	// Constructor
	public InfoWindow(String title, int x, int y) {
		this.title = title;
		this.x = x;
		this.y = y;
		this.closeButton = new Button(width - TOP_BAR_HEIGHT + x, y, TOP_BAR_HEIGHT, TOP_BAR_HEIGHT, this.title + "_close", "X", 7, this.title);
		this.closeButton.setColorHover(Color.RED);
		this.closeButton.setOwner("Window");
		this.closeButton.setResizeText(false);
		this.returnButton = new Button(x, y, TOP_BAR_HEIGHT, TOP_BAR_HEIGHT, this.title + "_return", "<", 0);
		this.returnButton.setColorHover(Color.BLUE);
		this.returnButton.setOwner("Window");
		this.returnButton.setResizeText(false);
		this.returnButton.setVisible(false);
		this.open = true;
	}

	// Accessors
	public int getX() {return this.x;}
	public int getY() {return this.y;}
	public int getWidth() {return this.width;}
	public int getHeight() {return this.height;}
	public boolean isOpen() {return this.open;}
	public String getTitle() {return this.title;}
	public String getContent() {return this.content;}
	public int getContentX() {return this.contentX;}
	public int getContentY() {return this.contentY;}
	public int getLineSpacing() {return this.lineSpacing;}
	public ArrayList<Button> getWindowButtons() {return this.windowButtons;}
	public Button getCloseButton() {return this.closeButton;}

	@SuppressWarnings("unchecked")
	public ArrayList<Button> getAllButtons() {
		ArrayList<Button> allButtons = (ArrayList<Button>)this.windowButtons.clone();
		allButtons.add(closeButton);
		allButtons.add(returnButton);
		return allButtons;
	}

	public ImageToken getImage(int index) {
		if (index < this.windowImages.size()) {
			return this.windowImages.get(index);
		} else {
			System.out.println("ERROR: InfoWindow, getImage(" + index + ") has an index that is out of range.");
			return null;
		}
	}

	public ArrayList<ImageToken> getAllImages() {return this.windowImages;}

	public String getAnimationStatus() {
		if (this.animationWidth == this.width && this.animationHeight == this.height) {
			return "Open";
		} else if (this.animationWidth == 0 && this.animationHeight == 0) {
			return "Closed";
		} else {
			return "In progress";
		}
	}

	public Rectangle getTopBarBounds() {
		int tbX = this.x;
		int tbWidth = this.width;
		if (closeButton == null) {tbWidth -= TOP_BAR_HEIGHT - 1;}
		if (returnButton == null) {tbX += TOP_BAR_HEIGHT + 1; tbWidth -= TOP_BAR_HEIGHT - 2;}
		return new Rectangle(tbX, this.y, tbWidth, TOP_BAR_HEIGHT);
	}

	public boolean isOverTopBar(Point p) {return this.getTopBarBounds().contains(p);}
	public boolean isMoving() {return this.isMoving;}

	// Mutators
	public void setX(int in) {this.x = in;}
	public void setY(int in) {this.y = in;}
	public void setWidth(int in) {this.width = in;}
	public void setHeight(int in) {this.height = in;}
	public void setOpen(boolean in) {this.open = in;}
	public void setTitle(String in) {this.title = in;}
	public void setContent(String in) {this.content = in;}
	public void setContentX(int in) {this.contentX = in;}
	public void setContentY(int in) {this.contentY = in;}
	public void setLineSpacing(int in) {this.lineSpacing = in;}
	public void addWindowButton(Button in) {
		in.setOwner("Window");
		in.setOwnerX(in.getOwnerX());
		in.setOwnerY(in.getOwnerY());
		this.windowButtons.add(in);
	}

	public void addReturnButton(int exec, String add) {
		this.returnButton.setExecutionNumber(29);
		this.returnButton.setAdditionalString(exec + "|" + add + "|" + this.title);
		this.returnButton.setAdditionalStringUsage(true);
		this.returnButton.setVisible(true);
	}

	public void removeAllButtons() {this.closeButton = null; this.returnButton = null; this.windowButtons.clear();}
	public void addImage(int inX, int inY, int inImg) {this.windowImages.add(new ImageToken(inX, inY, inImg));}
	public void clearImages() {this.windowImages.clear();}
	public void setMoving(boolean in) {this.isMoving = in;}
	public void removeCloseButton() {this.closeButton = null;}
	public void removeReturnButton() {this.returnButton = null;}

	// Graphical methods
	public void draw(Graphics g, Board b, ImageLibrary il) {

		// Draws the main window box.
		g.setColor(Color.WHITE);
		g.fillRect(this.x, this.y, this.animationWidth, this.animationHeight);
		g.setColor(Color.BLACK);
		g.drawRect(this.x, this.y, this.animationWidth, this.animationHeight);

		// Draws everything that should appear once the window is fully open.
		if (this.getAnimationStatus().equals("Open")) {

			// Draws top bar.
			g.setColor(this.topBarColor);
			g.fillRect(this.x, this.y, this.animationWidth, TOP_BAR_HEIGHT);
			g.setColor(Color.BLACK);			
			g.drawRect(this.x, this.y, this.animationWidth, TOP_BAR_HEIGHT);
			g.drawString(this.title, this.x + (this.animationWidth / 2) - (this.title.length() * 4), this.y + (TOP_BAR_HEIGHT / 2) + 5);

			// Draws content.
			int x1 = this.x + this.contentX - 80;
			int y1 = this.y + this.contentY + 30 + TOP_BAR_HEIGHT;
			for (String line : this.content.split("\n")) {
				for (String segment : line.split("\t")) {
			    	g.drawString(segment, x1 += 100, y1);
			    }
			    x1 = this.x + contentX - 80;
			    y1 += MyTextMetrics.getTextSize("TEST")[1] + lineSpacing;
			}

			// Draws images.
			for (ImageToken tok : this.windowImages) {
				g.drawImage(il.getImage(tok.getImage()), tok.getX(), tok.getY(), b);
			}

		}

	}

	public void update(Point p) {

		// Code that handles the expanding and contracting of a window.
		if (this.open) {
			if (this.animationWidth <= this.width) {
				this.animationWidth += ((this.width / 20) * this.animationSpeed) + 1;
				if (this.animationWidth > this.width) {
					this.animationWidth = this.width;
				}
			}
			if (this.animationHeight <= this.height) {
				this.animationHeight += ((this.height / 20) * this.animationSpeed) + 1;
				if (this.animationHeight > this.height) {
					this.animationHeight = this.height;
				}
			}
		} else {
			if (this.animationWidth >= 0) {
				this.animationWidth -= ((this.width / 20) * this.animationSpeed) + 1;
				if (this.animationWidth < 0) {
					this.animationWidth = 0;
				}
			}
			if (this.animationHeight >= 0) {
				this.animationHeight -= ((this.height / 20) * this.animationSpeed) + 1;
				if (this.animationHeight < 0) {
					this.animationHeight = 0;
				}
			}
		}
		
		// Code for updating window buttons.
		for (int i = 0; i < windowButtons.size(); ++i) {
			// Sets buttons coordinates appropriately.
			Button curr = windowButtons.get(i);
			if (this.open) {	
				curr.setX(this.x + curr.getOwnerX());
				curr.setY(this.y + TOP_BAR_HEIGHT + curr.getOwnerY());
			}
		}
		
		// Code for updating images.
		for (int i = 0; i < windowImages.size(); ++i) {
			// Sets image coordinates appropriately.
			ImageToken curr = windowImages.get(i);
			if (this.open) {	
				curr.setX(this.x + curr.getInitX());
				curr.setY(this.y + TOP_BAR_HEIGHT + curr.getInitY());
			}
		}
		
		// Code for moving close button.
		if (this.closeButton != null) {
			this.closeButton.setX(this.width - TOP_BAR_HEIGHT + this.x);
			this.closeButton.setY(this.y);
		}
		
		// Code for moving return button.
		if (this.returnButton != null) {
			this.returnButton.setX(this.x);
			this.returnButton.setY(this.y);
		}

	}
	
	public void clearText() {
		content = "";
	}
	
	public void clearAll() {
		content = "";
		windowButtons.clear();
		windowImages.clear();
	}

	public void close() {
		this.open = false;
	}

	// Overrides
	public String toString() {
		return "Window: " + this.title;
	}

}