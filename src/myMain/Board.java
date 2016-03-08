/*
 *  Board.java
 *
 *  Main graphics object for BATTLEMAPS.
 *
 */

package myMain;

import myGame.Block;
import myGame.Building;
import myGame.BuildingFactory;
import myGame.City;
import myGame.Game;
import myGame.Map;
import myGame.NameGenerator;
import myGame.Player;
import myGame.Route;
import myGraphics.ImageLibrary;
import myGraphics.ImageToken;
import myInterface.Button;
import myInterface.GridWindow;
import myInterface.HoverWindow;
import myInterface.InfoWindow;
import myInterface.ListWindow;
import myInterface.MyInterfaceManager;
import myInterface.MyTextMetrics;
import myInterface.MyWindow;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.SwingUtilities;

public class Board extends JPanel implements ActionListener, MouseListener {

	// Variables
	private Timer timer;
	private int windowWidth;
	private int windowHeight;
	
	private long randomMapSeed = System.currentTimeMillis();
	private Random randomEvents = new Random();
	private Random randomMap = new Random(randomMapSeed);
	private Game game;
	private BuildingFactory buildDex = new BuildingFactory();
	private ImageLibrary il = new ImageLibrary(100);

	private String state = "";
	private MyInterfaceManager mim;
	private HoverWindow mouseWindow = new HoverWindow(0,0);

	private int playerWhoseGoItIs = 1;										// OWEN: Move into game.

	// Constants
	private final String GAME_NAME = "BATTLEMAPS";
	private final String VERSION_NUMBER = "InDev version 0.2";
	private final String VERSION_GOAL = "";
	private final String VERSION_INFO = "";
	private final int BORDER_SIZE = 40;
	private final int DELAY = 15;
	public static int WINDOW_CENTER_X;
	public static final String DEFAULT_FONT_TYPE = "Trebuchet MS";
	public static final int DEFAULT_FONT_ATT = Font.PLAIN;
	public static final int DEFAULT_FONT_SIZE = 14; 
	public static final Font DEFAULT_FONT = new Font(DEFAULT_FONT_TYPE, DEFAULT_FONT_ATT, DEFAULT_FONT_SIZE);

	// Debug constants
	public static final boolean DEBUG_EVENTS = true;			// Reports mouse clicks, screen changing, etc.
	public static final boolean DEBUG_TRACE = true;				// Displays data about variables and data structures.
	public static final boolean DEBUG_LAUNCH = true;			// Shows progress of game launch.
	public static final boolean DEBUG_LOAD = true;				// Outputs status of files loaded into the game.
	public static final boolean DEBUG_WINDOW = true;			// Allows opening of debug windows.
	public static final boolean DEBUG_MAPS = true;				// Begins game on map select.
	public static final boolean DEBUG_ERROR = true;				// Details any errors that occur at runtime.

	// Construction and initialisation.
	public Board(int width, int height) {

		// Store neccessary values.
		windowWidth = width;
		windowHeight = height;
		WINDOW_CENTER_X = (windowWidth - 400) / 2;

		// Sets up graphical interface.
		initBoard();
		
		// Retrieves all resources needed at startup.
		initImages();

		// Initialise interface manager.
		mim = new MyInterfaceManager(windowWidth, windowHeight, il);

		// Checks for debug screen choice.
		if (DEBUG_MAPS) {
			state = "DEBUG";
			mim.setInterface(state, game, playerWhoseGoItIs);
		} else {
			// Sets up the game state.
			game = initGame("Normal");

			// Sets game state.
			state = "Map";

			// Sets up buttons.
			mim.setInterface(state, game, playerWhoseGoItIs);
		}

		// Sorts out hover window.
		mouseWindow.setOpen(false);

	}

	private void initBoard() {

		// Set up JPanel settings.
		this.setFocusable(true);
		this.setBackground(Color.BLUE);
		this.setPreferredSize(new Dimension(windowWidth, windowHeight));

		// Adds a mouse listener to the panel.
		this.addMouseListener(this);

		// Initialise timer.
		timer = new Timer(DELAY, this);
		timer.start();

	}

	private void initImages() {

		if (DEBUG_LOAD) {System.out.println("Beginning image load.");}

		il.loadImage(1, "images/MouseSelect.png");
		il.loadImage(2, "images/MouseBuild.png");
		il.loadImage(3, "images/MouseMove.png");
		il.loadImage(4, "images/MouseDestroy.png");
		il.loadImage(11, "images/Flag0.png");
		il.loadImage(12, "images/Flag1.png");
		il.loadImage(13, "images/Flag2.png");
		il.loadImage(14, "images/Flag3.png");
		il.loadImage(15, "images/Flag4.png");
		il.loadImage(16, "images/Flag5.png");
		il.loadImage(17, "images/Flag6.png");
		il.loadImage(18, "images/Flag7.png");
		il.loadImage(19, "images/Flag8.png");
		il.loadImage(21, "images/CityHighlight.png");
		il.loadImage(22, "images/CityNeighbour.png");
		il.loadImage(31, "images/BlueprintBlock.png");

	}

	public Game initGame(String mapType) {

		// Initialisation.
		int cityCount = 10;									// Number of cities on the map.
		int mapHeight = windowHeight - City.CITY_SIZE;		// Height of map (y length)
		int mapWidth = windowWidth - City.CITY_SIZE;		// Width of map (x length)
		int citiesPerPlayer = 1;							// Number of cities each player stars with.
		int playerCount = 4;								// Number of players in a game.
		boolean giveConnectedCities = false;				// Unused at the moment.

		switch (mapType) {
			case "Normal":
				break;
			case "Small":
				cityCount = 5;
				mapHeight = (windowHeight / 2) - City.CITY_SIZE;
				mapWidth = (windowWidth / 2) - City.CITY_SIZE;
				break;
			case "Large":
				cityCount = 25;
				mapHeight = (windowHeight * 2) - City.CITY_SIZE;
				mapWidth = (windowWidth * 2) - City.CITY_SIZE;
				playerCount = 8;
				break;
			case "Full":
				cityCount = 12;
				citiesPerPlayer = 3;
				break;
			case "Packed":
				cityCount = 20;
				playerCount = 8;
				break;
		}

		// Creates the game objects and sets it up.
		Map newMap = new Map(cityCount, mapWidth, mapHeight, BORDER_SIZE, randomMap);
		Game game = new Game(newMap);
		if (DEBUG_LAUNCH) {
			System.out.println("");
			System.out.println("MAP DEBUG LOG START:");
			ArrayList<String> log = game.getMap().getDebugLog();
			for (int i = 0; i < log.size(); ++i) {
				System.out.println("  " + log.get(i));
			}
			System.out.println("MAP DEBUG LOG END.");
			System.out.println("");
		}

		// Adds players to the game.
		for (int i = 0; i < playerCount; ++i) {
			game.addPlayer(new Player(i, "Player " + i));
		}

		// Gives a random city to each player.
		ArrayList<City> allCities = game.getMap().getCities();
		ArrayList<Player> allPlayers = game.getPlayers();
		for (int c = 0; c < citiesPerPlayer; ++c) {
			for (int i = 0; i < allPlayers.size(); ++i) {
				int cityNumber = randomMap.nextInt(allCities.size());
				while (!allCities.get(cityNumber).getOwner().equals("NONE")) {
					cityNumber = randomMap.nextInt(allCities.size());
					if (DEBUG_LAUNCH) {System.out.println("Retrying giving city to " + allPlayers.get(i).getName());}
				}
				allCities.get(cityNumber).setOwner(allPlayers.get(i).getID());
				if (DEBUG_LAUNCH) {System.out.println("Giving " + allCities.get(cityNumber).getName() + " to " + allPlayers.get(i).getName());}
			}
		}

		// Increments game turn.
		game.incTurn();

		// Returns the game object.
		return game;

	}

	public Game initGame(String mapType, long r) {

		randomMap = new Random(r);
		return initGame(mapType);

	}

	@Override
	public void paintComponent(Graphics g) {

		// Provide graphics to MyTextMetrics.
		MyTextMetrics.setGraphics(g);

		// Draws the super.
		super.paintComponent(g);

		// Sets starting colours and fonts.
		g.setFont(DEFAULT_FONT);
		g.setColor(Color.BLACK);

		// Draws screen border.
		g.drawRect(0,0,windowWidth-1,windowHeight-1);

		// Draws appropriate screen.
		if (state.equals("Map")) {
			drawMap(g);
		} else if (state.substring(0,4).equals("Menu")) {
			drawMenu(g);
		} else if (state.substring(0,4).equals("City")) {
			drawCity(g);
		} else if (state.equals("DEBUG")) {
			drawDebug(g);
		}

		// Draws the MIM objects.
		mim.drawAll(g, this);

		// Ensures all stuff sync up on all platforms.
		Toolkit.getDefaultToolkit().sync();

	}

	private void drawMenu(Graphics g) {
		// Nothing
	}

	private void drawMap(Graphics g) {

		// Draws map of all cities and routes.
		Map tempMap = game.getMap();
		ArrayList<City> tempCities = tempMap.getCities();
		Color routeColor = new Color(255,255,255);
		Color borderColor = new Color(0,0,0);
		Color islandColor = new Color(0,200,0);

		// Sets background color.
		this.setBackground(Color.BLUE);

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
				g.drawImage(il.getImage(21), currentCity.getX() - 2, currentCity.getY() - 2, this);
				ArrayList<City> currentCityNeighbours = tempMap.getNeighboursOf(currentCity.getName());
				for (City neighbour : currentCityNeighbours) {
					g.drawImage(il.getImage(22), neighbour.getX() - 2, neighbour.getY() - 2, this);
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
			g.drawImage(il.getImage(cityImage), currentCity.getX(), currentCity.getY(), this);
		}

		// Draws GUI.
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 80, 21);
		g.fillRect(0, windowHeight - 40, windowWidth, windowHeight);
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, 80, 21);
		g.drawRect(0, windowHeight - 40, windowWidth, windowHeight);
		g.drawString("Turn " + Integer.toString(game.getTurn()), 4, 16);

		// Draws mouse hover window.
		if (mouseWindow.isOpen()) {
			mouseWindow.draw(g, this, il);          
		}

	}

	private void drawCity(Graphics g) {

		// Sets background color.
		Color backgroundColor = new Color(0,200,0);
		this.setBackground(backgroundColor);

		// Gets the city name and city.
		String cityName = state.substring(5);
		City thisCity = game.getMap().getCityByName(cityName);

		// Draws city squares.
		ArrayList<Block> cityBlocks = thisCity.getGrid();
		for (int i = 0; i < cityBlocks.size(); ++i) {
			Block currentBlock = cityBlocks.get(i);
			if (currentBlock.isOver(mim.getMousePos())) {
				g.setColor(Color.RED);
			} else {
				g.setColor(Color.WHITE);
			}
			g.fillRect(City.GRID_OFFSET_X + (currentBlock.getX() * Block.BLOCK_SIZE), City.GRID_OFFSET_Y + (currentBlock.getY() * Block.BLOCK_SIZE), Block.BLOCK_SIZE, Block.BLOCK_SIZE);
			g.setColor(Color.BLACK);
			g.drawRect(City.GRID_OFFSET_X + (currentBlock.getX() * Block.BLOCK_SIZE), City.GRID_OFFSET_Y + (currentBlock.getY() * Block.BLOCK_SIZE), Block.BLOCK_SIZE, Block.BLOCK_SIZE);
		}

		// Draws GUI.
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 80, 21);															// Turn counter.
		g.fillRect(0, windowHeight - 40, windowWidth, 40);								// Bottom bar.
		g.fillRect(750, 0, 250, windowHeight - 40);										// City buttons box.
		g.fillRect((windowWidth / 2) - 64, 0, 128, 32);									// City name box.
		g.fillRect(0, windowHeight / 2, City.GRID_OFFSET_X - 20, windowHeight - 40);		// Stats box.

		g.setColor(Color.BLACK);
		g.drawRect(0, 0, 80, 21); 															// Turn counter.
		g.drawRect(0, windowHeight - 40, windowWidth, windowHeight);						// Bottom bar.
		g.drawRect(750, 0, 250, windowHeight - 40);										// City buttons box.
		g.drawRect((windowWidth / 2) - 64, 0, 128, 32);									// City name box.
		g.drawRect(0, windowHeight / 2, City.GRID_OFFSET_X - 20, (windowHeight / 2) - 40);	// Stats box.

		// Draws text.
		g.drawString("Turn " + Integer.toString(game.getTurn()), 4, 16);
		g.drawString(cityName, (windowWidth / 2) - (5 * cityName.length()), 22);
		g.drawString(cityName, 5, windowHeight / 2 + 5 + MyTextMetrics.getTextSize("Text")[1]);
		int offset = 3;
		String[] theKeys = {"Population", "Happiness", "Military", "Technology", "Nature", "Diplomacy", "Commerce", "Industry"};
		for (int i = 0; i < theKeys.length; ++i) {
			if (theKeys[i].equals("Military")) {++offset;}
			g.drawString(theKeys[i] + ": \t " + thisCity.getStat(theKeys[i]), 5, windowHeight / 2 + (5 + MyTextMetrics.getTextSize("Text")[1]) * (i + offset));
		}

	}

	public void drawDebug(Graphics g) {

		// Sets background color.
		this.setBackground(Color.BLACK);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		act(e);

		repaint();
		
	}

	private void act(ActionEvent e) {

		// Gets mouse position.
		Point mp = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(mp, this);
		mim.setMousePos(mp);

		// Performs screen-specific actions.
		if (state.equals("Map")) {
			actMap();
		} else if (state.substring(0,4).equals("Menu")) {
			actMenu();
		} else if (state.substring(0,4).equals("City")) {
			actCity();
		} else if (state.equals("DEBUG")) {
			actDebug();
		}

		mim.updateWindows();

	}

	public void actMap() {

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
			if (cityBounds.contains(mim.getMousePos())) {
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

	public void actMenu() {

	}

	public void actCity() {

	}

	public void actDebug() {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println("PRESSED RUNNING");
		mim.mousePressed();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		System.out.println("RELEASED RUNNING");
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		System.out.println("ENTERED RUNNING");
	}

	@Override
	public void mouseExited(MouseEvent e) {
		System.out.println("EXITED RUNNING");
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		System.out.println("CLICKED RUNNING");

		// Allows for only 1 button press.
		boolean clickThisFrame = false;

		// Get buttons being hovered over.
		ArrayList<Button> hoveredButtons = mim.getHoveredButtons();

		// Execute code of clicked button.
		for (Button button : hoveredButtons) {
			if (!clickThisFrame) {
				clickThisFrame = true;
				if (button.hasAdditionalString()) {
					buttonExecution(button.getExecutionNumber(), button.getAdditionalString());
				} else {
					buttonExecution(button.getExecutionNumber(), "");
				}
			}
		}

		// Checks other menu objects.
		if (state.equals("Map")) {

			// Check for clicking on cities.
			ArrayList<City> tempCities = game.getMap().getCities();

			for (int i = 0; i < tempCities.size(); ++i) {
				
				// Gets the city being operated on.
				City currentCity = tempCities.get(i);

				// Determines the image to be used for the city.
				Rectangle cityBounds = currentCity.getBounds();

				if (cityBounds.contains(mim.getMousePos())) {
					state = "City-" + currentCity.getName();
					mim.setInterface(state, game, playerWhoseGoItIs);
				}

			}

		} else if (state.substring(0,4).equals("Menu")) {
			// Nothing.
		} else if (state.substring(0,4).equals("City")) {
			// Nothing.
		}

	}

	private void buttonExecution(int exec, String add) {

		if (DEBUG_TRACE) {
			String debugAdd = add;
			if (debugAdd.equals("")) {
				System.out.println("EXECUTING BUTTON CODE NUMBER " + exec + " WITH NO ADDITIONAL STRING");     
			} else {
				System.out.println("EXECUTING BUTTON CODE NUMBER " + exec + " WITH '" + debugAdd + "'.");        		
			}
		}

		// Creating commonly used variables.
		InfoWindow wind;
		Button butt;
		String cont;
		ArrayList vect;
		ArrayList arr;
		GridWindow gridow;
		Player currentPlayer;
		InfoWindow currentWindow;
		boolean found;
		ListWindow listow;
		Building bldg;

		// Creating arrays for important values.
		String[] theKeys = {"Residential", "Happiness", "Military", "Diplomacy", "Technology", "Commerce", "Nature", "Industry"};
		Color[] theColors = {new Color(200,200,200), new Color(234,242,10), new Color(194,2,50), new Color(235,237,175), new Color(10,242,231), new Color(83,74,240), new Color(17,153,42), new Color(245,155,66)};

		switch (exec) {
			case -1: break;                                                 // Null action.
			case 0: break;                                                  // Null action.
			case 1: state = "Map"; mim.setInterface(state, game, playerWhoseGoItIs); break;              // Go to map action.
			case 2: state = "City-" + add; mim.setInterface(state, game, playerWhoseGoItIs); break;      // Go to city action. 
			case 3: state = "Menu-" + add; mim.setInterface(state, game, playerWhoseGoItIs); break;      // Go to menu action.
			case 4:                                                         // End turn action.
				// Move to the next player.
				playerWhoseGoItIs += 1;
				if (playerWhoseGoItIs > game.getPlayers().size()) {
					// Other end of turn code.
					// Increment game turn.
					playerWhoseGoItIs = 1;
					game.incTurn();
				}
				break;
			case 5:                                                         // View players.
				if (!mim.checkWindowsFor("Player Info")) {
					// Creating the new window.
					wind = new InfoWindow("Player Info", (windowWidth - 400) / 2, 100);
					butt = wind.getCloseButton();
					cont = "| Player Name\t| Commander Name\n|------------------------------------------------------------\n";
					int COL_WIDTH = 30;
					for (int i = 0; i < game.getPlayers().size(); ++i) {
						// Generate table rows.
						currentPlayer = game.getPlayers().get(i);
						cont += "| " + currentPlayer.getName() + "\t";
						cont += "| " + currentPlayer.getCommander() + "\t";
						cont += "\n";
						// Generate player buttons.
						Button pb = new Button(wind.getX() + 218, wind.getY() + 26 + InfoWindow.TOP_BAR_HEIGHT + i * (MyTextMetrics.getTextSize("TEST")[1] + wind.getLineSpacing() + 5) + 2 * (MyTextMetrics.getTextSize("TEST")[1] + wind.getLineSpacing()));
						pb.setHeight(MyTextMetrics.getTextSize("View Player")[1] + 4);
						pb.setWidth(MyTextMetrics.getTextSize("View Player")[0] + 2);
						pb.setID(wind.getTitle() + "_" + currentPlayer.getName());
						pb.setButtonText("View Player");
						pb.setExecutionNumber(8);
						pb.setAdditionalString(currentPlayer.getName());
						pb.setAdditionalStringUsage(true);
						pb.setOwner("Window");
						wind.addWindowButton(pb);
					}
					wind.setContent(cont);
					wind.setLineSpacing(5);
					mim.addWindowFull(wind);
				}
				if (DEBUG_TRACE) {mim.debug("Buttons");}
				break;
			case 6:                                                         // View stats.
				if (!mim.checkWindowsFor("Game Stats")) {
					// Creating window.
					wind = new InfoWindow("Game Stats", (windowWidth - 400) / 2, 100);
					butt = wind.getCloseButton();
					cont = "Stats\n------\nWhat do I even put here.";
					wind.setContent(cont);
					mim.addWindowFull(wind);
				}
				break;
			case 7:                                                         // Close window.
				mim.removeWindowFull(add);
				break;
			case 8:                                                         // Open full player window.
				if (!mim.checkWindowsFor("Player - " + add)) {
					// Create window.
					wind = new InfoWindow("Player - " + add, (windowWidth - 400) / 2, 100);
					butt = wind.getCloseButton();
					cont = add;
					wind.setContent(cont);
					wind.addReturnButton(5, "");
					// Close Player Info window.
					mim.removeWindowFull("Player Info");
					// Add window to manager.
					mim.addWindowFull(wind);
				}
				break;
			case 9:                                                         // Opens debug info window.
				if (!mim.checkWindowsFor("DEBUG_INFO_WINDOW")) {
					// Create window.
					wind = new InfoWindow("DEBUG_INFO_WINDOW", (windowWidth - 400) / 2, 100);
					butt = wind.getCloseButton();
					cont = "Current map seed:- " + randomMapSeed;
					wind.setContent(cont);
					mim.addWindowFull(wind);
				}
				break;
			case 10:                                                        // Opens debug action window.
				if (!mim.checkWindowsFor("DEBUG_ACTION_WINDOW")) {
					// Create window.
					int DEBUG_GRID_ROWS = 5;
					int DEBUG_GRID_COLS = 5;
					gridow = new GridWindow("DEBUG_ACTION_WINDOW", (windowWidth - 400) / 2, 100, DEBUG_GRID_ROWS, DEBUG_GRID_COLS);
					gridow.setGridX(10);
					gridow.setGridY(10);
					gridow.setButtonWidth(60);
					gridow.setButtonHeight(30);
					butt = gridow.getCloseButton();
					// Creating grid buttons.
					if (true) {
						// Button 1: New game.
						Button d1 = new Button(0, 0);
						d1.setID("DEBUG_B_New_Game");
						d1.setColorInner(Color.GREEN);
						d1.setExecutionNumber(15);
						d1.setButtonText("New Game");
						gridow.addGridButton(0, 0, d1);
						// Button 2: Show debug routes.
						Button d2 = new Button(0, 0);
						d2.setID("DEBUG_B_Switch_Routes");
						d2.setColorInner(Color.GREEN);
						d2.setExecutionNumber(13);
						d2.setButtonText("Switch Routes");
						gridow.addGridButton(0, 1, d2);
						// Button 21: Print buttons.
						Button d21 = new Button(0, 0, "DEBUG_B_Print_Buttons", "Print Buttons", 25); d21.setColorInner(Color.GREEN);
						gridow.addGridButton(4, 0, d21);
						// Button 22: Print windows.
						Button d22 = new Button(0, 0, "DEBUG_B_Print_Windows", "Print Windows", 26); d22.setColorInner(Color.GREEN);
						gridow.addGridButton(4, 1, d22);
						// Button 23: Print mouse.
						Button d23 = new Button(0, 0, "DEBUG_B_Print_Mouse", "Print Mouse", 32); d23.setColorInner(Color.GREEN);
						gridow.addGridButton(4, 2, d23);
						// Button 25: Close game.
						Button d25 = new Button(0, 0);
						d25.setID("DEBUG_B_Close");
						d25.setColorInner(Color.GREEN);
						d25.setExecutionNumber(11);
						d25.setButtonText("Close Game");
						gridow.addGridButton(4, 4, d25);
					}
					mim.addWindowFull(gridow);
				}
				break;
			case 11:                                                        // Closes the game.
				// OWEN: Broken atm.
				break;
			case 12:														// Generates a new random game.
				// Sets up the game state.
				game = initGame(add);
				state = "Map";
				mim.setInterface(state, game, playerWhoseGoItIs);
				mim.removeWindowFull("DEBUG_LAUNCH");
				break;
			case 13:														// Toggles map between optimised and initial routes.
				// Changes the map's display mode.
				game.toggleDRD();
				break;
			case 14:														// Random map seed entry.
				Scanner r = new Scanner(System.in);
				System.out.print("New seed:- ");
				randomMapSeed = r.nextLong();
				randomMap = new Random(randomMapSeed);
				if (DEBUG_TRACE) {System.out.println("New seed accepted.");}
				break;
			case 15: 														// Jump to debug start screen.
				state = "DEBUG"; 
				mim.setInterface(state, game, playerWhoseGoItIs); 
				break; 
			case 16:														// Open city add building menu.
				gridow = new GridWindow("Add Building", WINDOW_CENTER_X, 100, 5, 2);
				gridow.setButtonWidth(175);
				gridow.setButtonHeight(40);
				gridow.setButtonGap(10);
				int lalala = 0;
				int lololo = 0;
				for (int lelele = 0; lelele < theKeys.length; ++lelele) {
					String key = theKeys[lelele];
					Color lliw = theColors[lelele];
					Button gwb = new Button(0,0,"City_Build_" + key, key, 21, key);
					gwb.setColorInner(theColors[lelele]);
					gridow.addGridButton(lololo, lalala, gwb);
					++lalala;
					if (lalala >= 2) {lalala = 0; ++lololo;}
					if (key.equals("Happiness")) {++lololo;}
				}
				mim.addWindowFull(gridow);
				break;
			case 17:														// Activate city build building mode.
				mim.setMouseMode("Build");
				break;
			case 18:														// Activate city move building mode.
				mim.setMouseMode("Move");
				break;
			case 19:														// Activate city remove building mode.
				mim.setMouseMode("Destroy");
				break;
			case 20:														// Open city rename city window.
				
				break;
			case 21:														// Open city builing category window.
				List<String> the_buildings = buildDex.getCategory(add);
				listow = new ListWindow("Add Building - " + add + " Buildings", WINDOW_CENTER_X, 50, 7);
				listow.setHeight(500);
				listow.setGridY(60);
				listow.setButtonHeight(30);
				listow.setContentX(175);
				listow.setContentY(0);
				butt = new Button(listow.getContentX() + (listow.getWidth() / 4), listow.getHeight() - 60, "Begin_Mouse_Placement_Button", "Place Building", 17, "NULL");
				butt.setVisible(false);
				listow.addWindowButton(butt);
				listow.addReturnButton(16, "");
				Color this_color = new Color(255,255,255);
				for (int i = 0; i < theKeys.length; ++i) {
					if (theKeys[i].equals(add)) {
						this_color = theColors[i];
					}
				}
				listow.setContent("Click on a button to select\na building.");
				for (String bld : the_buildings) {
					Button bldb = new Button(0, 50, "Build_" + bld.replace(" ", "_"), bld, 22, listow.getTitle() + "|" + bld);
					bldb.setColorInner(this_color);
					listow.addListButton(bldb);
				}
				listow.fillGridList();
				listow.setUpExec(30);
				listow.setDownExec(31);
				mim.addWindowFull(listow);
				mim.removeWindowFull("Add Building");
				break;
			case 22:														// Add building info to city building category window.

				// Splits the addtional string into the neccessary parts.
				String windowTitle = add.split("\\|")[0];
				String buildingType = add.split("\\|")[1];

				// Retrieves the window and building specified in the additional string.
				listow = (ListWindow) mim.getWindow(windowTitle);
				bldg = buildDex.getBuilding(buildingType);

				// Begins preparing the text content.
				cont = bldg.getType().toUpperCase() + "\n" + bldg.getCategory() + " Building\n \n";
				String startingDescription = bldg.getDescription();
				String formattedDescription = "";

				// Splits the description over multiple lines if it is too long.
				while (MyTextMetrics.getTextSize(startingDescription)[0] > listow.getWidth() + 20 - (listow.getWidth() - listow.getContentX())) {
					startingDescription = "Description too long.";
				}
				formattedDescription += startingDescription;
				cont += formattedDescription + "\n \n";

				// COntinues adding text to the content variable.
				cont += "Max Health: " + bldg.getMaxHealth() + "\nTurns to Build: " + (bldg.getBuildTime() * -1) + "\n \n";
				cont += "Stats: \n" + bldg.getAllStatsAsString() + "\n \n";
				cont += "Blueprint: \n \n";

				// Creates the images to display the building blueprint.
				String bldgBlpt = bldg.getBlueprintAsString();
				int blptX = listow.getX() + listow.getContentX() + InfoWindow.TOP_BAR_HEIGHT;
				int blptY = listow.getY() + listow.getContentY() + (MyTextMetrics.getTextSize(cont)[1] * MyTextMetrics.getCountOf("\n", cont)) + InfoWindow.TOP_BAR_HEIGHT + 5;
				listow.clearImages();
				for (String line : bldgBlpt.split("\n")) {
					for (String character : line.split("|")) {
						if (character.equals("T")) {listow.addImage(blptX, blptY, 31);}
						blptX += 15;
					}
					blptX = listow.getX() + listow.getContentX() + InfoWindow.TOP_BAR_HEIGHT;
					blptY += 15;
				}

				// Sets the content in the window.
				listow.setContent(cont);

				// Edits the visibility of the Place Building button.
				listow.getWindowButtons().get(0).setVisible(true);
				listow.getWindowButtons().get(0).setAdditionalString(buildingType);

				break;
			case 23:														// Open city ordinances window.

				break;
			case 24:														// Open city info window.

				break;
			case 25:														// Print buttons.
				mim.debug("Buttons");
				break;
			case 26:														// Print windows.
				mim.debug("Windows");
				break;
			case 27:														// Up button in ListWindow.
				listow = (ListWindow) mim.getWindow(add);
				listow.decPageNumber();
				listow.fillGridList();
				listow.setDownVis(true);
				listow.setUpVis(!listow.isAtMin());
				break;
			case 28:														// Down button in ListWindow.
				listow = (ListWindow) mim.getWindow(add);
				listow.incPageNumber();
				listow.fillGridList();
				listow.setUpVis(true);
				listow.setDownVis(!listow.isAtMax());
				break;
			case 29:														// Return button handling.
				
				// Split execution string into windowto open's exec code, window to open's add string, and the window to close's name.
				String[] breakdown = add.split("\\|");
				
				if (DEBUG_TRACE) {System.out.println("RETURN BUTTON EXECUTION: " + breakdown[0] + ", " + breakdown[1] + ", " + breakdown[2] + ".");}

				// Opens the window to open.
				buttonExecution(Integer.parseInt(breakdown[0]), breakdown[1]);
				
				// Closes the window to close.
				mim.removeWindowFull(breakdown[2]);
				break;
			case 30:														// Up button in Add Building ListWindow.
				listow = (ListWindow) mim.getWindow(add);
				buttonExecution(27, add);
				listow.setContent("Click on a button to select\na building.");
				listow.getWindowButtons().get(0).setVisible(false);
				listow.clearImages();
				break;
			case 31:														// Down button in Add Building ListWindow.
				listow = (ListWindow) mim.getWindow(add);
				buttonExecution(28, add);
				listow.setContent("Click on a button to select\na building.");
				listow.getWindowButtons().get(0).setVisible(false);
				listow.clearImages();
				break;
			case 32:
				mim.debug("Mouse");
				break;
		}    

	}

}