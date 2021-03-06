/*
 *  GridWindow.java
 *
 *  A window that allows arranging of objects in a grid.
 *
 */

package myInterface.windows;

import myMain.Board;

import java.awt.Point;
import java.util.ArrayList;

import myInterface.buttons.Button;

public class GridWindow extends InfoWindow {

	protected Button[][] gridButtons;

	protected final int initRows;
	protected final int initCols;

	protected int buttonWidth = 80;
	protected int buttonHeight = 40;
	protected int buttonGap = 20;
	protected int gridX = 20;
	protected int gridY = 20;

	public GridWindow(String title, int x, int y, int rows, int columns) {
		super(title, x, y);
		initRows = rows;
		initCols = columns;
		gridButtons = new Button[rows][columns];
	}

	// Accessors
	public Button getGridButton(int row, int col) {return gridButtons[row][col];}

	public int[] getGridSize() {
		int[] sizes = {initRows, initCols};
		return sizes;
	}

	public int getButtonWidth() {return this.buttonWidth;}
	public int getButtonHeight() {return this.buttonHeight;}
	public int getButtonGap() {return this.buttonGap;}
	public int getGridX() {return this.gridX;}
	public int getGridY() {return this.gridY;}
	public int getGridRows() {return gridButtons.length;}
	public int getGridCols() {return gridButtons[0].length;}

	public ArrayList<Button> getAllButtons() {

		// Gets close and window buttons.
		ArrayList<Button> prevButtons = super.getAllButtons();

		// Adds grid buttons.
		for (Button[] subButtons : gridButtons) {
			for (Button b : subButtons) {
				if (b != null) {prevButtons.add(b);}
			}
		}

		// Returns all of the buttons.
		return prevButtons;

	}

	// Mutators
	public void addGridButton(int row, int col, Button obj) {
		if (row <= initRows - 1 && col <= initCols - 1) {
			obj.setOwner("Window"); 
			gridButtons[row][col] = obj;
		} else {
			if (Board.DEBUG_ERROR) {System.out.println("ERROR: GridWindow, addGridButton(" + row + ", " + col + ", " + obj.getButtonText() + ") has invalid co-ordinates.");}
		}
	}

	public void removeGridButton(int row, int col) {gridButtons[row][col] = null;}
	public Object popGridButton(int row, int col) {Button temp = gridButtons[row][col]; gridButtons[row][col] = null; temp.setOwner("Basic"); return temp;}
	public void removeAllGridButtons() {gridButtons = new Button[initRows][initCols];}

	public void removeAllButtons() {
		super.removeAllButtons();
		this.removeAllGridButtons();
	}

	public void resetGridButtons() {
		for (int i = 0; i < initRows; ++i) {
			for (int j = 0; j < initCols; ++j) {
				gridButtons[i][j] = new Button(0,0,0,0,"BLANK","-",0);
			}
		}
	}

	public void setButtonWidth(int in) {buttonWidth = in;}
	public void setButtonHeight(int in) {buttonHeight = in;}
	public void setButtonGap(int in) {buttonGap = in;}
	public void setGridX(int in) {this.gridX = in;}
	public void setGridY(int in) {this.gridY = in;}
	
	public void resizeGridButtons() {
		
		int fixedGridX = this.gridX;
		int fixedGridY = this.gridY;
		int fixedButtonGap = this.buttonGap;
		
		int fixedAreaX = this.width - (2 * fixedGridX);
		int fixedAreaY = this.height - TOP_BAR_HEIGHT - (2 * fixedGridY);
		
		int rowCount = this.gridButtons.length;
		int colCount = this.gridButtons[0].length;
		
		int newButtonWidth = (fixedAreaX - ((colCount - 1) * fixedButtonGap) ) / colCount;
		int newButtonHeight = (fixedAreaY - ((rowCount - 1) * fixedButtonGap) ) / rowCount;
		
		this.buttonWidth = newButtonWidth;
		this.buttonHeight = newButtonHeight;
		
	}
	
	public void resizeGridGaps() {
		
		int oldGridX = this.gridX;
		int oldGridY = this.gridY;
		int fixedButtonWidth = this.buttonWidth;
		int fixedButtonHeight = this.buttonHeight;
		
		int fixedAreaX = this.width - (2 * oldGridX);
		int fixedAreaY = this.height - TOP_BAR_HEIGHT - (2 * oldGridY);
		
		int rowCount = this.gridButtons.length;
		int colCount = this.gridButtons[0].length;
		
		int newButtonGapX = (fixedAreaX - (fixedButtonWidth * colCount)) / (colCount - 1);
		int newButtonGapY = (fixedAreaY - (fixedButtonHeight * rowCount)) / (rowCount - 1);
		
		// Set new button gap.
		this.buttonGap = Math.min(newButtonGapX, newButtonGapY);
		
		// Re-adjust grid X.
		this.gridX = (this.width - (fixedButtonWidth * colCount) - (this.buttonGap * (colCount - 1))) / 2;
		// Re-adjust grid Y.
		this.gridY = (this.height - TOP_BAR_HEIGHT - (fixedButtonHeight * rowCount) - (this.buttonGap * (rowCount - 1))) / 2;

	}

	// Updaters
	public void update(Point p) {

		super.update(p);

		// Updates grid buttons.
		for (int row = 0; row < gridButtons.length; ++row) {
			for (int col = 0; col < gridButtons[row].length; ++col) {
				// Sets buttons coordinates and dimensions appropriately.
				Button curr = gridButtons[row][col];
				if (open) {	
					if (curr != null) {
						curr.setX(this.x + gridX + col * (buttonWidth + buttonGap));
						curr.setY(this.y + TOP_BAR_HEIGHT + gridY + row * (buttonHeight + buttonGap));
						curr.setHeight(buttonHeight);
						curr.setWidth(buttonWidth);
					}
				} else {
					if (curr != null) {
						curr.setX(-buttonWidth);
						curr.setY(-buttonHeight);
						curr.setHeight(buttonHeight);
						curr.setWidth(buttonWidth);
					}
				}
			}
		}

	}

}