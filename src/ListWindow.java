/*
 *	ListWindow.java
 *
 *	Allows for windows to be created with a button list panel and a content panel.
 *
 */

package myInterface;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

public class ListWindow extends GridWindow {
	
	private ArrayList<Button> buttonList = new ArrayList<Button>();

	private int pageNumber = 0;
	private Button upButton;
	private Button downButton;

	// Constructor
	public ListWindow(String title, int inX, int inY, int rows) {

		super(title, inX, inY, rows, 1);
		this.upButton = new Button(0, 0, TOP_BAR_HEIGHT, TOP_BAR_HEIGHT, this.title + "_up", "/\\", 27, title);
		this.upButton.setOwner("Window");
		this.downButton = new Button(0, 0, TOP_BAR_HEIGHT, TOP_BAR_HEIGHT, this.title + "_down", "\\/", 28, title);
		this.downButton.setOwner("Window");
		this.buttonWidth = 160;
		this.buttonHeight = 40;
		this.buttonGap = 20;
		this.gridX = 20;
		this.gridY = 20;
		this.contentX = 200;
		this.contentY = 0;
		this.resetGridButtons();

	}

	// Accessors
	public ArrayList<Button> getCurrentButtons() {
		ArrayList<Button> returnButtons = new ArrayList<Button>();
		for (int i = this.pageNumber * initRows; i < this.buttonList.size() && i < (this.pageNumber + 1) * initRows; ++i) {
			returnButtons.add(this.buttonList.get(i));
		}
		System.out.println("Current buttons at page " + this.pageNumber + " is " + returnButtons);
		return returnButtons;
	}

	public int getMaxPage() {return (buttonList.size() - 1) / initRows;}

	public boolean isAtMax() {return this.getMaxPage() == this.pageNumber;}
	public boolean isAtMin() {return 0 == this.pageNumber;}

	public ArrayList<Button> getAllButtons() {

		// Gets all grid window buttons.
		ArrayList<Button> prevButtons = super.getAllButtons();

		// Adds up and down buttons.
		prevButtons.add(this.upButton);
		prevButtons.add(this.downButton);

		// Returns all of the buttons.
		return prevButtons;

	}

	// Mutators
	public void setUpVis(boolean vis) {this.upButton.setVisible(vis);}
	public void setUpExec(int in) {this.upButton.setExecutionNumber(in);}
	public void setDownVis(boolean vis) {this.downButton.setVisible(vis);}
	public void setDownExec(int in) {this.downButton.setExecutionNumber(in);}
	public void addListButton(Button in) {this.buttonList.add(in); in.setOwner("Window"); this.fillGridList();}

	public void fillGridList() {

		// Gets the list of buttons that should be displayed currently.
		ArrayList<Button> theButtons = getCurrentButtons();

		// Add the appropriate buttons to the grid.
		for (int i = 0; i < theButtons.size(); ++i) {
			Button listButton = theButtons.get(i);
			Button gridButton = getGridButton(i, 0);
			gridButton.cloneFromNoCoords(listButton);
			gridButton.setVisible(true);
		}

		// Make empty buttons invisible.
		int startButton = initRows - (initRows - theButtons.size());
		for (int i = startButton; i < initRows; ++i) {
			Button gridButton = getGridButton(i, 0);
			gridButton.setVisible(false);
		}

	}

	public void removeAllButtons() {
		super.removeAllButtons();
		this.upButton = null;
		this.downButton = null;
		this.clearButtonList();
	}

	public void clearButtonList() {buttonList = new ArrayList<Button>();}
	public void incPageNumber() {if (this.getMaxPage() > this.pageNumber) {this.pageNumber += 1;}}
	public void decPageNumber() {if (this.pageNumber > 0) {this.pageNumber -= 1;}}

	// Updaters
	public void update(Point p) {

		super.update(p);

		// Update up and down arrow coordinates.
		upButton.setX(this.x + this.gridX + (this.buttonWidth / 2) - (upButton.getWidth() / 2));
		upButton.setY(this.y + TOP_BAR_HEIGHT * 2);
		downButton.setX(this.x + this.gridX + (this.buttonWidth / 2) - (downButton.getWidth() / 2));
		downButton.setY(this.y + this.animationHeight - (TOP_BAR_HEIGHT * 2));

		this.setUpVis(!this.isAtMin());
		this.setDownVis(!this.isAtMax());

	}

}