package myInterface.screens;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import myGame.ActionHandler;
import myGame.City;
import myGame.Land;
import myGame.Map;
import myGame.Route;
import myGame.Traveller;
import myInterface.MyTextMetrics;
import myMain.Board;

public class MapScreen extends MyScreen {
	
	private final boolean trippyMode = false;
	private final boolean invasionMode = false;
	
	private boolean showPoints = false;
	
	private ArrayList<Traveller> travellers = new ArrayList<Traveller>();

	public MapScreen(Board b, int inWidth, int inHeight) {
		super(b);
		this.title = "Map Screen";
		this.width = inWidth;
		this.height = inHeight;
		this.init();
		for (City c : this.game.getMap().getCities()) {
			ArrayList<City> n = this.game.getMap().getNeighboursOf(c.getName());
			travellers.add(new Traveller(c, n.get(b.randomTrivial.nextInt(n.size()))));
		}
	}
	
	public MapScreen(Board b) {
		this(b, b.windowWidth, b.windowHeight);
	}

	@Override
	public void act() {
		
		// Initialisation
		Map tempMap = this.game.getMap();
		
		// Processes map movement.
		if (mim.isPressed(3)) {
			tempMap.incScrollX(mim.getMouseDiff()[0]);
			if (tempMap.getScrollX() > 0) {tempMap.setScrollX(0);}
			if (tempMap.getScrollX() - this.width < -1 * tempMap.getLength() - tempMap.getBorder()) {tempMap.setScrollX((-1 * tempMap.getLength() - tempMap.getBorder()) + this.width);}
			tempMap.incScrollY(mim.getMouseDiff()[1]);
			if (tempMap.getScrollY() > 0) {tempMap.setScrollY(0);}
			if (tempMap.getScrollY() - this.height < -1 * tempMap.getWidth() - tempMap.getBorder()) {tempMap.setScrollY((-1 * tempMap.getWidth() - tempMap.getBorder()) + this.height);}
			mim.shiftButtonsBounded("Screen", game.getMap().getScrollX(), game.getMap().getScrollY());
		}
		
		// Updates travellers.
		for (int i = 0; i < travellers.size(); ++i) {
			Traveller t = travellers.get(i);
			if (!t.near()) {
				t.move();
			} else {
				ArrayList<City> a = this.game.getMap().getCities();
				City c = a.get(b.randomTrivial.nextInt(a.size()));
				ArrayList<City> n = this.game.getMap().getNeighboursOf(c.getName());
				travellers.add(new Traveller(c, n.get(b.randomTrivial.nextInt(n.size()))));
				if (!invasionMode) {travellers.remove(t);}
			}
		}
		
	}

	@Override
	public void draw(Graphics g) {
		
		// Draws map of all cities and routes.
		Map tempMap = game.getMap();
		ArrayList<City> tempCities = tempMap.getCities();
		Color routeColor = new Color(255,255,255);

		// Sets background colour.
		b.setBackground(Color.BLUE);
		
		// Get terrain temporarily.
		ArrayList<Land> tempTerrain = tempMap.getLand();

		// Terrain drawing loop.
		for (int i = 0; i < tempTerrain.size(); ++i) {
			tempTerrain.get(i).draw(g, game.getMap().getScrollX(), game.getMap().getScrollY());
		}

		// Route drawing loop.
		g.setColor(routeColor);
		for (int i = 0; i < tempCities.size(); ++i) {
			
			// Gets the city being operated on.
			City currentCity = tempCities.get(i);

			// Gets all routes from the selected city.
			ArrayList<Route> tempRoutes = tempMap.getRoutesFromName(currentCity.getName());

			// Draws all routes from the city.
			for (int j = 0; j < tempRoutes.size(); ++j) {
				Route currentRoute = tempRoutes.get(j);
				City otherCity = tempMap.getCityByName(currentRoute.getDestination(currentCity.getName()));
				if (!trippyMode) {
					// Normal operation.
					g.drawLine(currentCity.getX() + tempMap.getScrollX() + City.CITY_SIZE / 2, currentCity.getY() + tempMap.getScrollY() + City.CITY_SIZE / 2,
							otherCity.getX() + tempMap.getScrollX() + City.CITY_SIZE / 2, otherCity.getY() + tempMap.getScrollY() + City.CITY_SIZE /2);
				} else {
					// Weird map mode.
					g.drawLine(currentCity.getX() + tempMap.getScrollX() + City.CITY_SIZE / 2, currentCity.getY() + tempMap.getScrollY() + City.CITY_SIZE / 2,
							otherCity.getX() + City.CITY_SIZE / 2, otherCity.getY() + City.CITY_SIZE /2);
				}
			}

		}
		
		// Traveller drawing loop.
		for (Traveller t : travellers) {
			t.draw(g, b);
		}

		// Hover over drawing loop.
		for (int i = 0; i < tempCities.size(); ++i) {
			
			// Gets the city being operated on.
			City currentCity = tempCities.get(i);

			// Determines the image to be used for the city.
			Rectangle cityBounds = game.getScrolledBoundsName(currentCity.getName());
			if (cityBounds.contains(mim.getMousePos())) {
				g.drawImage(il.getImage(21), currentCity.getX() + tempMap.getScrollX() - 2, currentCity.getY() + tempMap.getScrollY() - 2, b);
				ArrayList<City> currentCityNeighbours = tempMap.getNeighboursOf(currentCity.getName());
				for (City neighbour : currentCityNeighbours) {
					g.drawImage(il.getImage(22), neighbour.getX() + tempMap.getScrollX() - 2, neighbour.getY() + tempMap.getScrollY() - 2, b);
				}
			}

		}

		// City drawing loop.
		mim.drawButtons(g, "Screen", false);
		
		/*
		for (int i = 0; i < tempCities.size(); ++i) {
			
			// Gets the city being operated on.
			City currentCity = tempCities.get(i);

			// Determines the image to be used for the city.
			int cityImage = 11;
			if (currentCity.getOwner() != null){
				ArrayList<Player> allPlayers = game.getPlayers();
				for (int j = 0; j < allPlayers.size(); ++j) {
					if (allPlayers.get(j).equals(currentCity.getOwner())) {
						cityImage += j + 1;
					}
				}
			}

			// Draws the city and it's name.
			if (!trippyMode) {
				g.drawImage(il.getImage(cityImage), currentCity.getX() + tempMap.getScrollX(), currentCity.getY() + tempMap.getScrollY(), b);
			} else {
				g.drawImage(il.getImage(cityImage), currentCity.getX() + tempMap.getScrollX() / 2, currentCity.getY() + tempMap.getScrollX() / 2, b);
			}
		}
		*/

		// Draws consistent GUI.
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 160, 42);
		g.fillRect(0, height - 40, width, 40);
		g.fillRect((this.width / 2) - 50, 0, 100, 20);
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, 160, 42);
		g.drawRect(0, height - 40, width, 40);
		g.drawRect((this.width / 2) - 50, 0, 100, 20);
		g.drawString("Turn " + Integer.toString(game.getTurn()), 4, 16);
		g.drawString("Player " + Integer.toString(game.getActivePlayerNumber()) + ": " + game.getActivePlayer().getName(), 4, 36);
		g.drawString(this.title, (this.width / 2) - (MyTextMetrics.getTextSizeFlat(this.title)[0] / 2), 16);
		
		// Draws points GUI.
		if (this.showPoints) {
			g.setColor(Color.WHITE);
			g.fillRect(0, height - 240, 155, 200);
			g.setColor(Color.BLACK);
			g.drawRect(0, height - 240, 155, 200);
			g.drawString("Point", 4, height - 224);
			g.drawString("Count", 54, height - 224);
			g.drawString("Gain", 104, height - 224);
			for (int imgNum = 0; imgNum < 6; ++imgNum) {
				g.setColor(Color.BLACK);
				g.drawImage(il.getImage(51 + imgNum), 12, height - 210 + (imgNum * 20), b);
				g.drawString(Integer.toString(game.getActivePlayer().getPoint(ActionHandler.POINTSLIST[imgNum])), 54, height - 198 + (imgNum * 20));
				int thisGain = game.getPointGain(game.getActivePlayer(), ActionHandler.POINTSLIST[imgNum]);
				String thisString = "";
				g.setColor(Color.RED);
				if (thisGain >= 0) {thisString = "+"; g.setColor(Color.BLUE);}
				if (thisGain == 0) {g.setColor(Color.BLACK);}
				g.drawString(thisString + Integer.toString(thisGain), 108, height - 198 + (imgNum * 20));
			}
			g.setColor(Color.BLACK);
			g.drawImage(il.getImage(57), 42, height - 82, b);
			g.drawString(Integer.toString(game.getPointGain(game.getActivePlayer(), "Population")), 70, height - 70);
			g.drawImage(il.getImage(58), 42, height - 62, b);
			g.drawString(Integer.toString(game.getPointGain(game.getActivePlayer(), "Happiness")), 70, height - 50);
		}
		
	}
	
	@Override
	public void init() {
		this.mim.initInterface(this.b.state, this);
	}

	@Override
	public void transition() {
		// TODO: Transition system.
	}
	
	// Specific screen instructions.
	public boolean isDrawingPoints() {return this.showPoints;}
	public void togglePointsDisplay() {this.showPoints = !this.showPoints;}

}
