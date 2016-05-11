package myInterface;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import myGame.City;
import myGame.Map;
import myGame.Player;
import myGame.Route;
import myMain.Board;

public class MapScreen extends MyScreen {
	
	private boolean trippyMode = false;

	public MapScreen(Board b, int inWidth, int inHeight) {
		super(b);
		this.title = "Map Screen";
		this.width = inWidth;
		this.height = inHeight;
		this.init();
	}
	
	public MapScreen(Board b) {
		this(b, b.windowWidth, b.windowHeight);
	}

	@Override
	public void act() {
		
		// Initialisation
		Map tempMap = this.game.getMap();
		ArrayList<City> tempCities = tempMap.getCities();        
		boolean stillHovered = false;
		String hoveredCity = "";

		// Check for mouse hover windows.
		for (int i = 0; i < tempCities.size(); ++i) {
			
			// Gets the city being operated on.
			City currentCity = tempCities.get(i);

			// Checks if city is being hovered over.
			Rectangle cityBounds = game.getScrolledBoundsName(currentCity.getName());
			if (cityBounds.contains(mim.getMousePos())) {
				stillHovered = true;
				hoveredCity = currentCity.getName();
				mim.getMouseWindow().setContent(hoveredCity);
			}

		}
		
		// Updates the hover window.
		mim.updateHoverWindow(stillHovered);
		
		// Processes map movement.
		if (mim.isPressed(3)) {
			tempMap.incScrollX(mim.getMouseDiff()[0]);
			if (tempMap.getScrollX() > 0) {tempMap.setScrollX(0);}
			if (tempMap.getScrollX() - this.width < -1 * tempMap.getLength() - tempMap.getBorder()) {tempMap.setScrollX((-1 * tempMap.getLength() - tempMap.getBorder()) + this.width);}
			tempMap.incScrollY(mim.getMouseDiff()[1]);
			if (tempMap.getScrollY() > 0) {tempMap.setScrollY(0);}
			if (tempMap.getScrollY() - this.height < -1 * tempMap.getWidth() - tempMap.getBorder()) {tempMap.setScrollY((-1 * tempMap.getWidth() - tempMap.getBorder()) + this.height);}
		}
		
	}

	@Override
	public void draw(Graphics g) {
		
		// Draws map of all cities and routes.
		Map tempMap = game.getMap();
		ArrayList<City> tempCities = tempMap.getCities();
		Color routeColor = new Color(255,255,255);
		Color borderColor = new Color(0,0,0);
		Color islandColor = new Color(0,200,0);

		// Sets background color.
		b.setBackground(Color.BLUE);

		// Island drawing loop.
		for (int i = 0; i < tempCities.size(); ++i) {
			
			// Gets the city being operated on.
			City currentCity = tempCities.get(i);

			// Draws island for the city.
			g.setColor(islandColor);
			g.fillOval(currentCity.getX() + tempMap.getScrollX() - City.CITY_SIZE / 2, currentCity.getY() + tempMap.getScrollY() - City.CITY_SIZE / 2, City.CITY_SIZE * 2, City.CITY_SIZE * 2);
			g.setColor(borderColor);
			g.drawOval(currentCity.getX() + tempMap.getScrollX() - City.CITY_SIZE / 2, currentCity.getY() + tempMap.getScrollY()- City.CITY_SIZE / 2, City.CITY_SIZE * 2, City.CITY_SIZE * 2);

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
		for (int i = 0; i < tempCities.size(); ++i) {
			
			// Gets the city being operated on.
			City currentCity = tempCities.get(i);

			// Determines the image to be used for the city.
			int cityImage = 11;
			if (!currentCity.getOwner().equals("NONE")){
				ArrayList<Player> allPlayers = game.getPlayers();
				for (int j = 0; j < allPlayers.size(); ++j) {
					if (allPlayers.get(j).getID().equals(currentCity.getOwner())) {
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

		// Draws GUI.
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 80, 21);
		g.fillRect(0, height - 40, width, height);
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, 80, 21);
		g.drawRect(0, height - 40, width, height);
		g.drawString("Turn " + Integer.toString(game.getTurn()), 4, 16);
		
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
