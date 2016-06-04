package myInterface;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import myGame.Block;
import myGame.Building;
import myGame.City;
import myMain.Board;

public class CityScreen extends MyScreen {
	
	private City theCity = null;
	
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
	public void act() {}

	@Override
	public void draw(Graphics g) {
		
		// Sets background colour.
		Color backgroundColor = new Color(0,200,0);
		b.setBackground(backgroundColor);

		// Gets the city name and city.
		if (theCity == null) {
			theCity = game.getMap().getCityByName(b.state.substring(5));
		}

		// Check to see if any city block are being hovered over.
		Point blockPoint = theCity.getMousePosOnGrid(mim.getMousePos());
		
		// Draws city squares.
		ArrayList<Block> cityBlocks = theCity.getGrid();
		for (int i = 0; i < cityBlocks.size(); ++i) {
			Block currentBlock = cityBlocks.get(i);
			g.setColor(Color.WHITE);
			g.fillRect(City.GRID_OFFSET_X + (currentBlock.getX() * Block.BLOCK_SIZE), City.GRID_OFFSET_Y + (currentBlock.getY() * Block.BLOCK_SIZE), Block.BLOCK_SIZE, Block.BLOCK_SIZE);
			g.setColor(Color.BLACK);
			g.drawRect(City.GRID_OFFSET_X + (currentBlock.getX() * Block.BLOCK_SIZE), City.GRID_OFFSET_Y + (currentBlock.getY() * Block.BLOCK_SIZE), Block.BLOCK_SIZE, Block.BLOCK_SIZE);
		}
		
		// Draws buildings.
		for (Building building : theCity.getBuildings()) {
			String blueprint = building.getBlueprintAsString();
			int blptX = City.GRID_OFFSET_X + (building.getX() * Block.BLOCK_SIZE);
			int blptY = City.GRID_OFFSET_Y + (building.getY() * Block.BLOCK_SIZE);
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
				blptX = City.GRID_OFFSET_X + (building.getX() * Block.BLOCK_SIZE);
				blptY += Block.BLOCK_SIZE;
			}
		}
		
		// Carries out additional block drawing if there is a mouse collision.
		if (blockPoint.x >= 0 && blockPoint.x <= theCity.getWidth()) {
			
			// Draw mouse over'd city square.
			g.setColor(Color.RED);
			g.fillRect(City.GRID_OFFSET_X + (blockPoint.x * Block.BLOCK_SIZE), City.GRID_OFFSET_Y + (blockPoint.y * Block.BLOCK_SIZE), Block.BLOCK_SIZE, Block.BLOCK_SIZE);
			g.setColor(Color.BLACK);
			g.drawRect(City.GRID_OFFSET_X + (blockPoint.x * Block.BLOCK_SIZE), City.GRID_OFFSET_Y + (blockPoint.y * Block.BLOCK_SIZE), Block.BLOCK_SIZE, Block.BLOCK_SIZE);
			
			// Draw building blueprint.		
			if (mim.getMouseBuilding() != null) {
				Building mB = mim.getMouseBuilding();
				String mBString = mB.getBlueprintAsString();
				int blptX = City.GRID_OFFSET_X + (blockPoint.x * Block.BLOCK_SIZE);
				int blptY = City.GRID_OFFSET_Y + (blockPoint.y * Block.BLOCK_SIZE);
				for (String line : mBString.split("\n")) {
					for (String character : line.split("|")) {
						if (character.equals("T")) {
							if (blptX < City.GRID_OFFSET_X + (theCity.getWidth() * Block.BLOCK_SIZE) && blptY < City.GRID_OFFSET_Y + (theCity.getWidth() * Block.BLOCK_SIZE)) {
								g.setColor(Color.BLUE);
								g.fillRect(blptX, blptY, Block.BLOCK_SIZE, Block.BLOCK_SIZE);
							}
							g.setColor(Color.BLACK);
							g.drawRect(blptX, blptY, Block.BLOCK_SIZE, Block.BLOCK_SIZE);
						}
						blptX += Block.BLOCK_SIZE;
					}
					blptX = City.GRID_OFFSET_X + (blockPoint.x * Block.BLOCK_SIZE);
					blptY += Block.BLOCK_SIZE;
				}
			
			}
			
		}

		// Draws GUI.
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 80, 21);															// Turn counter.
		g.fillRect(0, this.height - 40, this.width, 40);									// Bottom bar.
		g.fillRect(750, 0, 250, this.height - 40);											// City buttons box.
		g.fillRect(0, this.height / 2, City.GRID_OFFSET_X - 50, this.height - 40);			// Points box.

		g.setColor(Color.BLACK);
		g.drawRect(0, 0, 80, 21); 															// Turn counter.
		g.drawRect(0, this.height - 40, this.width, this.height);							// Bottom bar.
		g.drawRect(750, 0, 250, this.height - 40);											// City buttons box.
		g.drawRect(0, this.height / 2, City.GRID_OFFSET_X - 50, (this.height / 2) - 40);	// Points box.

		// Draws text.
		g.drawString("Turn " + Integer.toString(game.getTurn()), 4, 16);
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
	}
	
	@Override
	public void init() {
		this.mim.setInterface(this.b.state, this.game);
	}

	@Override
	public void transition() {
		// TODO: Transition system.
	}

}
