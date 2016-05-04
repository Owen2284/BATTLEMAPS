package myInterface;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import myGame.City;
import myGame.Game;
import myGame.Map;
import myGame.Player;
import myGame.Route;
import myGraphics.ImageLibrary;
import myMain.Board;

public class MapScreen extends MyScreen {
	
	private Game game;

	public MapScreen(int inWidth, int inHeight, Game inGame) {
		this.title = "Map Screen";
		this.width = inWidth;
		this.height = inHeight;
		this.game = inGame;
	}

	public String getTitle() {return title;}
	public void setTitle(String title) {this.title = title;}

	@Override
	public void act(Point p) {
		
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
			Rectangle cityBounds = currentCity.getBounds();
			if (cityBounds.contains(p)) {
				stillHovered = true;
				hoveredCity = currentCity.getName();
			}

		}

		// Closes mouse hover window if no hover is detected.
		if (!stillHovered) {
			mouseWindow.setActive(false);
			mouseWindow.setOpen(false);
		} else {
			// Sorts out the window if there is a hover.
			if (mouseWindow.isOpen()) {
				mouseWindow.update(mim.getMousePos());
				if ((mouseWindow.getX() + mouseWindow.getWidth()) > windowWidth) {
					mouseWindow.setX(windowWidth - mouseWindow.getWidth());
				}
				if ((mouseWindow.getY() + mouseWindow.getHeight()) > windowHeight - 40) {
					mouseWindow.setY(windowHeight - mouseWindow.getHeight() - 40);
				}                
			} else {
				mouseWindow = new HoverWindow(mim.getMousePos().x, mim.getMousePos().y);
				mouseWindow.setActive(true);
				mouseWindow.setContent(hoveredCity);
				mouseWindow.update(mim.getMousePos());
				if ((mouseWindow.getX() + mouseWindow.getWidth()) > windowWidth) {
					mouseWindow.setX(windowWidth - mouseWindow.getWidth());
				}
				if ((mouseWindow.getY() + mouseWindow.getHeight()) > windowHeight - 40) {
					mouseWindow.setY(windowHeight - mouseWindow.getHeight() - 40);
				}
			}
		}
		
	}

	@Override
	public void draw(Graphics g, Board b, ImageLibrary il, MyInterfaceManager mim) {
		
		// Draws map of all cities and routes.
		Map tempMap = game.getMap();
		ArrayList<City> tempCities = tempMap.getCities();
		Color routeColor = new Color(255,255,255);
		Color borderColor = new Color(0,0,0);
		Color islandColor = new Color(0,200,0);

		// Sets background color.
		g.setColor(Color.BLUE);
		g.drawRect(0, 0, this.width, this.height);

		// Island drawing loop.
		for (int i = 0; i < tempCities.size(); ++i) {
			
			// Gets the city being operated on.
			City currentCity = tempCities.get(i);

			// Draws island for the city.
			g.setColor(islandColor);
			g.fillOval(currentCity.getX() - City.CITY_SIZE / 2, currentCity.getY() - City.CITY_SIZE / 2, City.CITY_SIZE * 2, City.CITY_SIZE * 2);
			g.setColor(borderColor);
			g.drawOval(currentCity.getX() - City.CITY_SIZE / 2, currentCity.getY() - City.CITY_SIZE / 2, City.CITY_SIZE * 2, City.CITY_SIZE * 2);

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
				g.drawLine(currentCity.getX() + City.CITY_SIZE / 2, currentCity.getY() + City.CITY_SIZE / 2, otherCity.getX() + City.CITY_SIZE / 2, otherCity.getY() + City.CITY_SIZE /2);
			}

		}

		// Hover over drawing loop.
		for (int i = 0; i < tempCities.size(); ++i) {
			
			// Gets the city being operated on.
			City currentCity = tempCities.get(i);

			// Determines the image to be used for the city.
			Rectangle cityBounds = currentCity.getBounds();
			if (cityBounds.contains(mim.getMousePos())) {
				g.drawImage(il.getImage(21), currentCity.getX() - 2, currentCity.getY() - 2, b);
				ArrayList<City> currentCityNeighbours = tempMap.getNeighboursOf(currentCity.getName());
				for (City neighbour : currentCityNeighbours) {
					g.drawImage(il.getImage(22), neighbour.getX() - 2, neighbour.getY() - 2, b);
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
			g.drawImage(il.getImage(cityImage), currentCity.getX(), currentCity.getY(), b);
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
	public void transition() {
		// TODO: Transition system.

	}

}
