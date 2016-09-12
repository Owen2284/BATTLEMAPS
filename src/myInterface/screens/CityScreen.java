package myInterface.screens;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import myGame.Block;
import myGame.Building;
import myGame.City;
import myInterface.MyTextMetrics;
import myMain.Board;

public class CityScreen extends MyScreen {
	
	private City theCity = null;
	private boolean ownerViewing = false;
	
	public CityScreen(Board b, int inWidth, int inHeight) {
		super(b);
		this.title = "City Screen";
		this.width = inWidth;
		this.height = inHeight;
		this.init();
	}
	
	public CityScreen(Board b) {
		this(b, b.windowWidth, b.windowHeight);
	}

	@Override
	public void act() {
		
		// Gets the city name and city.
		if (theCity == null) {
			theCity = game.getMap().getCityByName(b.state.substring(5));
			ownerViewing = theCity.hasOwner(game.getActivePlayer());
		}
		
		// City updating.
		theCity.act(width, height);
		
		// Enabling/disabling the city buttons.
		if (ownerViewing) {
			mim.getButtonByID("City_Build").setActive(theCity.getEmptyGridArea() > 0);
			mim.getButtonByID("City_Move").setActive(theCity.getBuildings().size() > 0);
			mim.getButtonByID("City_Remove").setActive(theCity.getBuildings().size() > 0);
			mim.getButtonByID("City_Ordinances").setActive(theCity.getOrdinancesForThisTurn() > 0);
		}
		
		// Hover window processing
		Building hovered = theCity.getBuildingAt(
				theCity.getMousePosOnGrid(mim.getMousePos()).x, 
				theCity.getMousePosOnGrid(mim.getMousePos()).y);
		
		if (hovered != null) {
			mim.getMouseWindow().setContent("\"" + hovered.getName() + "\" the " + hovered.getType() + "\nHealth: " + Double.toString(hovered.getHealthPercentage()) + "%\nStatus:" + hovered.getStatus());
		}
		mim.updateHoverWindow(hovered != null);
		
	}

	@Override
	public void draw(Graphics g) {
		
		// Sets background colour.
		Color backgroundColor = new Color(0,200,0);
		b.setBackground(backgroundColor);

		// Gets the city name and city.
		if (theCity == null) {
			theCity = game.getMap().getCityByName(b.state.substring(5));
			ownerViewing = theCity.hasOwner(game.getActivePlayer());
		}

		// Check to see if any city block are being hovered over.
		Point blockPoint = theCity.getMousePosOnGrid(mim.getMousePos());
		
		// Draws city squares.
		ArrayList<Block> cityBlocks = theCity.getGrid();
		for (int i = 0; i < cityBlocks.size(); ++i) {
			Block currentBlock = cityBlocks.get(i);
			g.setColor(Color.WHITE);
			g.fillRect(theCity.getGridX() + (currentBlock.getX() * Block.BLOCK_SIZE), theCity.getGridY() + (currentBlock.getY() * Block.BLOCK_SIZE), Block.BLOCK_SIZE, Block.BLOCK_SIZE);
			g.setColor(Color.BLACK);
			g.drawRect(theCity.getGridX() + (currentBlock.getX() * Block.BLOCK_SIZE), theCity.getGridY() + (currentBlock.getY() * Block.BLOCK_SIZE), Block.BLOCK_SIZE, Block.BLOCK_SIZE);
		}
		
		// Draws buildings.
		for (Building building : theCity.getBuildings()) {
			String blueprint = building.getBlueprintAsString();
			int blptX = theCity.getGridX() + (building.getX() * Block.BLOCK_SIZE);
			int blptY = theCity.getGridY() + (building.getY() * Block.BLOCK_SIZE);
			for (String line : blueprint.split("\n")) {
				for (String character : line.split("|")) {
					if (character.equals("T")) {
						g.setColor(building.getColor());
						g.fillRect(blptX, blptY, Block.BLOCK_SIZE, Block.BLOCK_SIZE);
						g.setColor(Color.BLACK);
						g.drawRect(blptX, blptY, Block.BLOCK_SIZE, Block.BLOCK_SIZE);
					}
					blptX += Block.BLOCK_SIZE;
				}
				blptX = theCity.getGridX() + (building.getX() * Block.BLOCK_SIZE);
				blptY += Block.BLOCK_SIZE;
			}
		}
		
		// Carries out additional block drawing if there is a mouse collision.
		if (blockPoint.x >= 0 && blockPoint.x <= theCity.getWidth()) {
			
			// Draw mouse over'd city square.
			g.setColor(Color.RED);
			g.fillRect(theCity.getGridX() + (blockPoint.x * Block.BLOCK_SIZE), theCity.getGridY() + (blockPoint.y * Block.BLOCK_SIZE), Block.BLOCK_SIZE, Block.BLOCK_SIZE);
			g.setColor(Color.BLACK);
			g.drawRect(theCity.getGridX() + (blockPoint.x * Block.BLOCK_SIZE), theCity.getGridY() + (blockPoint.y * Block.BLOCK_SIZE), Block.BLOCK_SIZE, Block.BLOCK_SIZE);
			
			// Draw building blueprint.		
			if (mim.getMouseBuilding() != null) {
				Building mB = mim.getMouseBuilding();
				String mBString = mB.getBlueprintAsString();
				int blptX = theCity.getGridX() + (blockPoint.x * Block.BLOCK_SIZE);
				int blptY = theCity.getGridY() + (blockPoint.y * Block.BLOCK_SIZE);
				for (String line : mBString.split("\n")) {
					for (String character : line.split("|")) {
						if (character.equals("T")) {
							if (blptX < theCity.getGridX() + (theCity.getWidth() * Block.BLOCK_SIZE) && blptY < theCity.getGridY() + (theCity.getWidth() * Block.BLOCK_SIZE)) {
								g.setColor(Color.BLUE);
								g.fillRect(blptX, blptY, Block.BLOCK_SIZE, Block.BLOCK_SIZE);
							}
							g.setColor(Color.BLACK);
							g.drawRect(blptX, blptY, Block.BLOCK_SIZE, Block.BLOCK_SIZE);
						}
						blptX += Block.BLOCK_SIZE;
					}
					blptX = theCity.getGridX() + (blockPoint.x * Block.BLOCK_SIZE);
					blptY += Block.BLOCK_SIZE;
				}
			
			}
			
		}

		// Draws GUI.
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 80, 21);															// Turn counter.
		g.fillRect(0, this.height - 40, this.width, 40);									// Bottom bar.
		g.fillRect(750, 0, this.width - 750, this.height - 40);								// City buttons box.
		g.fillRect(0, this.height / 2, 150, this.height - 40);								// Points box.
		g.fillRect((this.width / 2) - 50, 0, 100, 20);										// Screen name box.

		g.setColor(Color.BLACK);
		g.drawRect(0, 0, 80, 21); 															// Turn counter.
		g.drawRect(0, this.height - 40, this.width, this.height);							// Bottom bar.
		g.drawRect(750, 0, this.width - 750, this.height - 40);								// City buttons box.
		g.drawRect(0, this.height / 2, 150, (this.height / 2) - 40);						// Points box.
		g.drawRect((this.width / 2) - 50, 0, 100, 20);										// Screen name box.

		// Draws text.
		g.drawString("Turn " + Integer.toString(game.getTurn()), 4, 16);
		g.drawString(this.title, (this.width / 2) - (MyTextMetrics.getTextSizeFlat(this.title)[0] / 2), 16);
		g.drawString(theCity.getName(), 5, this.height / 2 + 5 + MyTextMetrics.getTextSizeFlat("Text")[1]);
		g.drawString(theCity.getName(), 6, this.height / 2 + 5 + MyTextMetrics.getTextSizeFlat("Text")[1]);
		int offset = 3;
		String[] theKeys = {"Population", "Happiness", "Military", "Technology", "Nature", "Diplomacy", "Commerce", "Industry"};
		int imgNum = 57;
		for (int i = 0; i < theKeys.length; ++i) {
			if (theKeys[i].equals("Military")) {++offset; imgNum = 51;}
			g.drawImage(il.getImage(imgNum), 5, ((this.height / 2) - 13) + (5 + MyTextMetrics.getTextSizeFlat("Text")[1]) * (i + offset), b);
			String positivity = "+";
			if (theCity.getPoint(theKeys[i]) < 0 || theKeys[i].equals("Population")) {
				positivity = "";
			}
			g.drawString(positivity + theCity.getPoint(theKeys[i]), 35, this.height / 2 + (5 + MyTextMetrics.getTextSizeFlat("Text")[1]) * (i + offset));
			++imgNum;
		}
		if (ownerViewing) {
			String ordString = "Ordinance changes this turn: " + Integer.toString(theCity.getOrdinancesForThisTurn());
			g.drawString(ordString, 750 + ((this.width - 750) / 2) - (MyTextMetrics.getTextSizeFlat(ordString)[0] / 2), 352);
		}
		
		// Hover window processing
		Building hovered = theCity.getBuildingAt(
				theCity.getMousePosOnGrid(mim.getMousePos()).x, 
				theCity.getMousePosOnGrid(mim.getMousePos()).y);
		if (hovered != null) {
			mim.getMouseWindow().setOpen(true);
			mim.getMouseWindow().setActive(true);
		}
		mim.updateHoverWindow(hovered != null);
		
	}
	
	@Override
	public void init() {
		this.mim.initInterface(this.b.state, this);
	}

	@Override
	public void transition() {
		// TODO: Transition system.
	}

}
