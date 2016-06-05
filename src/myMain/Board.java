/*
 *  Board.java
 *
 *  Main graphics object for BATTLEMAPS.
 *
 */

package myMain;

import myData.MyValidator;
import myGame.Building;
import myGame.BuildingFactory;
import myGame.City;
import myGame.Game;
import myGame.Map;
import myGame.Player;
import myGraphics.ImageLibrary;
import myInterface.Button;
import myInterface.CityScreen;
import myInterface.CommandLine;
import myInterface.DebugScreen;
import myInterface.ErrorWindow;
import myInterface.ImageTestScreen;
import myInterface.MapScreen;
import myInterface.MenuScreen;
import myInterface.MyInterfaceManager;
import myInterface.MyScreen;
import myInterface.MyTextMetrics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.SwingUtilities;

public class Board extends JPanel implements ActionListener, MouseListener, KeyListener {

	private static final long serialVersionUID = 7198933434060052457L;
	
	// Timing and size variables
	public Timer timer;
	public long lastTimeStamp = System.currentTimeMillis();
	public int windowWidth;
	public int windowHeight;
	
	// Game engine variables
	public MyScreen scr;
	public MyInterfaceManager mim;
	public ImageLibrary il = new ImageLibrary(100);
	public Game game;
	public BuildingFactory buildDex = new BuildingFactory();
	public MyValidator val = new MyValidator();
	public CommandLine cmd = new CommandLine(Board.DEBUG_ERROR);
	public ButtonExecutor exe = new ButtonExecutor(this);
	
	// Random variables
	public long randomMapSeed = System.currentTimeMillis();
	public Random randomMap = new Random(randomMapSeed);	
	public Random randomEvents = new Random();
	public Random randomTrivial = new Random();
	
	// Tracking variables
	public String state = "";

	// Private constants
	public final String GAMENAME = "BATTLEMAPS";
	public final String VERSIONNUMBER = "Version 0.3";
	public final String VERSIONNAME = "The Menu Update";
	public final String VERSIONINFO = "This version of the game is focussed on adding functional menus to the game.";
	public final String VERSIONCOMPLETION = "0%";
	public final int BORDER_SIZE = 40;
	public final int DELAY = 15;
	public final int ERRORX = 0;
	public final int ERRORY = 40;
	
	// Public constants
	public static int WINDOW_CENTER_X;
	public static final String DEFAULT_FONT_TYPE = "Trebuchet MS";
	public static final int DEFAULT_FONT_ATT = Font.PLAIN;
	public static final int DEFAULT_FONT_SIZE = 14; 
	public static final Font DEFAULT_FONT = new Font(DEFAULT_FONT_TYPE, DEFAULT_FONT_ATT, DEFAULT_FONT_SIZE);
	
	// Debug constants
	public static final boolean DEBUG_CMD = true;				// Allows printing of messages when true.
	public static final boolean DEBUG_EVENTS = true;			// Reports mouse clicks, screen changing, etc.
	public static final boolean DEBUG_TRACE = true;				// Displays data about variables and data structures.
	public static final boolean DEBUG_LAUNCH = true;			// Shows progress of game launch.
	public static final boolean DEBUG_LOAD = true;				// Outputs status of files loaded into the game.
	public static final boolean DEBUG_WINDOW = true;			// Allows opening of debug windows.
	public static final boolean DEBUG_MAPS = true;				// Begins game on map select.
	public static final boolean DEBUG_ERROR = true;				// Details any errors that occur at runtime.
	public static final boolean DEBUG_FPS = false;				// Displays FPS counter and time to act/draw.

	// Construction and initialisation.
	public Board(int width, int height) {

		// Store necessary values.
		windowWidth = width;
		windowHeight = height;
		WINDOW_CENTER_X = (windowWidth - 400) / 2;
		
		// Outputs the game's version info.
		initMessage();

		// Sets up graphical interface.
		initBoard();
		
		// Retrieves all resources needed at startup.
		initImages();
		
		// Sets up the MyValidator object.
		initValidator();

		// Initialise interface manager.
		mim = new MyInterfaceManager(this);

		// Checks for debug screen choice.
		if (DEBUG_MAPS) {
			switchScreen("DEBUG", "");
		} else {
			// Sets up the game state.
			game = initGame("Normal");

			// Sets game state and screen.
			switchScreen("Map", "");
		}

	}
	
	// Prints a information message.
	private void initMessage() {
		cmd.debug("");
		cmd.debug("-----------------------------------------------");
		cmd.debug(GAMENAME);
		cmd.debug("-----------------------------------------------");
		cmd.debug(VERSIONNUMBER);
		cmd.debug(VERSIONNAME);
		cmd.debug(VERSIONINFO);
		cmd.debug(VERSIONCOMPLETION);
		cmd.debug("-----------------------------------------------");
		cmd.debug("");
	}

	// Performs all Swing-related setup.
	private void initBoard() {

		// Set up JPanel settings.
		this.setFocusable(true);
		this.setBackground(Color.BLUE);
		this.setPreferredSize(new Dimension(windowWidth, windowHeight));

		// Adds a mouse listener to the panel.
		this.addMouseListener(this);
		
		// Adds a key listener to the panel.
		this.addKeyListener(this);

		// Initialise timer.
		timer = new Timer(DELAY, this);
		timer.start();

	}

	// Loading images function
	private void initImages() {

		if (DEBUG_LOAD) {cmd.debug("Beginning image load.");}

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
		il.loadImage(41, "images/TravellerLand.png");
		il.loadImage(42, "images/TravellerSea.png");
		il.loadImage(43, "images/TravellerAir.png");
		il.loadImage(51, "images/IconMilitary.png");
		il.loadImage(52, "images/IconTechnology.png");
		il.loadImage(53, "images/IconNature.png");
		il.loadImage(54, "images/IconDiplomacy.png");
		il.loadImage(55, "images/IconCommerce.png");
		il.loadImage(56, "images/IconIndustry.png");
		il.loadImage(57, "images/IconPopulation.png");
		il.loadImage(58, "images/IconHappiness.png");
		
	}
	
	public void initValidator() {
		val.addRule("Random Seed", "[0-9]+", 1, 12);
		val.addRule("City Name", "[a-zA-Z0-9\\- ]+", 1, 32);
	}

	@SuppressWarnings("unused")
	public Game initGame(String mapType) {

		// Initialisation.
		int cityCount = 10;										// Number of cities on the map.
		int mapHeight = windowHeight;							// Height of map (y length)
		int mapWidth = windowWidth;								// Width of map (x length)
		int citiesPerPlayer = 1;								// Number of cities each player stars with.
		int playerCount = 4;									// Number of players in a game.
		boolean giveConnectedCities = false;					// Unused at the moment.
		int landPoints = 12;									// Aliasing on land.
		int landMax = (int) (1.6 * (double) City.CITY_SIZE);	// Max distance from city land points can be.
		int landMin = (int) (2.4 * (double) City.CITY_SIZE);	// Min distance from city land points should be.
		int landSmoothnessCount = 3;							// Amount of points surrounding previous point + 1.
		double landSmoothnessRate = 0.36;						// Closeness to previous land point.

		switch (mapType) {
			case "Normal":
				break;
			case "Small":
				cityCount = 5;
				mapHeight = (windowHeight / 2);
				mapWidth = (windowWidth / 2);
				break;
			case "Large":
				cityCount = 25;
				mapHeight = (windowHeight * 2);
				mapWidth = (windowWidth * 2);
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
		Map newMap = new Map(cityCount, mapWidth, mapHeight, BORDER_SIZE, randomMap, 
				landPoints, landMax, landMin, landSmoothnessCount, landSmoothnessRate);
		Game game = new Game(newMap);
		if (DEBUG_LAUNCH) {
			cmd.debug("");
			cmd.debug("MAP DEBUG LOG START:");
			ArrayList<String> log = game.getMap().getDebugLog();
			for (int i = 0; i < log.size(); ++i) {
				cmd.debug("  " + log.get(i));
			}
			cmd.debug("MAP DEBUG LOG END.");
			cmd.debug("");
		}

		// Adds players to the game.
		for (int i = 0; i < playerCount; ++i) {
			game.addPlayer(new Player(i, "Player " + Integer.toString(i + 1)));
		}

		// Gives a random city to each player.
		ArrayList<City> allCities = game.getMap().getCities();
		ArrayList<Player> allPlayers = game.getPlayers();
		for (int c = 0; c < citiesPerPlayer; ++c) {
			for (int i = 0; i < allPlayers.size(); ++i) {
				int cityNumber = randomMap.nextInt(allCities.size());
				while (!allCities.get(cityNumber).getOwner().equals("NONE")) {
					cityNumber = randomMap.nextInt(allCities.size());
					if (DEBUG_LAUNCH) {cmd.debug("Retrying giving city to " + allPlayers.get(i).getName());}
				}
				allCities.get(cityNumber).setOwner(allPlayers.get(i).getID());
				if (DEBUG_LAUNCH) {cmd.debug("Giving " + allCities.get(cityNumber).getName() + " to " + allPlayers.get(i).getName());}
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
		
		// Drawing start time.
		lastTimeStamp = System.currentTimeMillis();

		// Provide graphics to MyTextMetrics.
		MyTextMetrics.setGraphics(g);

		// Draws the super.
		super.paintComponent(g);

		// Sets starting colours and fonts.
		g.setFont(DEFAULT_FONT);
		g.setColor(Color.BLACK);

		// Draws screen border.
		g.drawRect(0,0,windowWidth-1,windowHeight-1);

		// Draws the screen.
		scr.draw(g);

		// Draws the MIM objects.
		mim.drawAll(g, this);
		
		// Draws FPS details.
		if (DEBUG_FPS) {
			double timeToDraw = System.currentTimeMillis() - lastTimeStamp;
			g.setColor(Color.BLACK);
			g.drawString("FPS: " + (1000.0 / timeToDraw), 400, 20);
			g.drawString("Time to Draw: " + timeToDraw + "ms", 400, 40);
		}

		// Ensures all stuff syncs up on all platforms.
		Toolkit.getDefaultToolkit().sync();

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		act(e);

		repaint();
		
	}

	private void act(@SuppressWarnings("unused") ActionEvent e) {

		// Gets mouse position.
		Point mp = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(mp, this);
		mim.setMousePos(mp);

		// Performs screen-specific actions.
		scr.act();
		
		mim.act();

		mim.updateWindows();

	}

	@Override
	public void mousePressed(MouseEvent e) {
		mim.mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mim.mouseReleased(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		mim.mouseReleased(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		mim.mouseReleased(e);
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		// Allows for only 1 button press.
		boolean clickThisFrame = false;

		// Get buttons being hovered over.
		ArrayList<Button> hoveredButtons = mim.getHoveredButtons();

		// Execute code of clicked button.
		for (Button button : hoveredButtons) {
			if (!clickThisFrame && (e.getButton() == MouseEvent.BUTTON1) ) {
				clickThisFrame = true;
				if (button.hasAdditionalString()) {
					exe.execute(button.getExecutionNumber(), button.getAdditionalString());
				} else {
					exe.execute(button.getExecutionNumber(), "");
				}
			}
		}

		// Checks other clickable objects.
		if (state.equals("Map")) {

			// Check for clicking on cities.
			ArrayList<City> tempCities = game.getMap().getCities();

			for (int i = 0; i < tempCities.size(); ++i) {
				
				// Gets the city being operated on.
				City currentCity = tempCities.get(i);

				// Determines the image to be used for the city.
				Rectangle cityBounds = game.getScrolledBoundsName(currentCity.getName());

				// Checks for clicks, and executes city code if necessary.
				if (cityBounds.contains(mim.getMousePos()) && e.getButton() == MouseEvent.BUTTON1) {
					switchScreen("City", currentCity.getName());
				}

			}

		} else if (state.substring(0,4).equals("Menu")) {
			// Nothing.
		} else if (state.substring(0,4).equals("City")) {
			
			// Gets the city name and city.
			String cityName = state.substring(5);
			City thisCity = game.getMap().getCityByName(cityName);
			
			// Building placement checks.
			Point blockPoint = thisCity.getMousePosOnGrid(mim.getMousePos());
			
			// Runs left click events.
			if (e.getButton() == MouseEvent.BUTTON1 && !clickThisFrame) {
			
				// Checks if there is a blueprint to be placed.
				if (mim.getMouseBuilding() != null) {
					// Checks that the mouse is over the grid.
					if (blockPoint.x >= 0 && blockPoint.x < thisCity.getWidth() && blockPoint.y >= 0 && blockPoint.y < thisCity.getLength()) {
						// Checks if the building is within the boundaries of the city.
						Building bldg = mim.getMouseBuilding();
						int[] bldgSize = bldg.getBlueprintSize();
						if ((blockPoint.x + bldgSize[0] <= thisCity.getWidth()) && (blockPoint.y + bldgSize[1] <= thisCity.getLength())) {
							// Checks if building collides with another building.
							if (thisCity.hasSpaceFor(bldg, blockPoint.x, blockPoint.y)) {
								// Places building into the city.
								if (!mim.getMouseMode().equals("Move")) {
									bldg.varyColor(randomTrivial.nextInt(41) - 20, randomTrivial.nextInt(41) - 20, randomTrivial.nextInt(41) - 20);
								}
								thisCity.addBuilding(bldg, blockPoint.x, blockPoint.y);
								if (!mim.getMouseMode().equals("Move")) {
									mim.setMouseMode("Pointer");
								} else {
									mim.setMouseMode("Move");
								}
							} else {
								mim.addWindow(new ErrorWindow("ERROR", ERRORX, ERRORY, "Building overlaps with other building(s)."));
							}
						} else {
							mim.addWindow(new ErrorWindow("ERROR", ERRORX, ERRORY, "Building is partially outside of the city grid."));
						}
					} else {
						mim.addWindow(new ErrorWindow("ERROR", ERRORX, ERRORY, "Building is off of the city grid."));
					}
				}
				// Run checks for building movement and deletion selection.
				else {
					if (mim.getMouseMode().equals("Move")) {
						if (blockPoint.x > 0 && blockPoint.y > 0) {
							Building moveBldg = thisCity.popBuildingAt(blockPoint.x, blockPoint.y);
							if (moveBldg != null) {
								mim.setMouseBuilding(moveBldg);
							}
						}
					} else if (mim.getMouseMode().equals("Destroy")) {
						if (blockPoint.x > 0 && blockPoint.y > 0) {
							thisCity.removeBuildingAt(blockPoint.x, blockPoint.y);
						}
					}
				}
			}
			
			// Runs right click events.
			else if (e.getButton() == MouseEvent.BUTTON3 && !clickThisFrame) {
				mim.setMouseMode("Pointer");
			}
			
			// Runs middle click events.
			else if (e.getButton() == MouseEvent.BUTTON2 && !clickThisFrame) {
				if (mim.getMouseBuilding() != null) {
					mim.getMouseBuilding().rotate();
				}
			}
			
		}

	}
	
	// Methods that respond monitor key presses.
	@Override
	public void keyTyped(KeyEvent e) {
		if (DEBUG_EVENTS) {cmd.debug("'" + e.getKeyChar() + "' TYPED.");}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (DEBUG_EVENTS) {cmd.debug("'" + e.getKeyChar() + "' PRESSED.");}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (DEBUG_EVENTS) {cmd.debug("'" + e.getKeyChar() + "' RELEASED.");}
		mim.setLetter(Character.toString(e.getKeyChar()), true);
	}
	
	// Screen transition function.
	public void switchScreen(String screenName, String extra) {
		switch (screenName) {
			case "Map":
				if (DEBUG_EVENTS) {cmd.debug("Switching to Map Screen.");}
				state = "Map";
				scr = new MapScreen(this);
				break;
			case "City":
				if (DEBUG_EVENTS) {cmd.debug("Switching to City Screen.");}
				state = "City-" + extra;
				scr = new CityScreen(this);
				break;
			case "Menu":
				if (DEBUG_EVENTS) {cmd.debug("Switching to Menu Screen.");}
				state = "Menu-" + extra;
				scr = new MenuScreen(this);
				break;
			case "DEBUG":
				if (DEBUG_EVENTS) {cmd.debug("Switching to Debug Screen.");}
				state = "DEBUG";
				scr = new DebugScreen(this);
				break;
			case "ImageTester":
				if (DEBUG_EVENTS) {cmd.debug("Switching to Image Test Screen.");}
				state = "ImageTester";
				scr = new ImageTestScreen(this);
				break;
		}
	}
	
	public void createQuickWindow(String title, String content) {
		mim.addWindow(new ErrorWindow(title, ERRORX, ERRORY, content));
	}
	
	public void createErrorWindow(String in) {
		this.createQuickWindow("ERROR", in);
	}

}