/*
 *  Board.java
 *
 *  Main graphics object for BATTLEMAPS.
 *
 */

package myMain;

import myGame.Building;
import myGame.BuildingFactory;
import myGame.City;
import myGame.Game;
import myGame.Map;
import myGame.Ordinance;
import myGame.OrdinanceBook;
import myGame.Player;
import myGraphics.ImageLibrary;
import myInterface.Button;
import myInterface.CityScreen;
import myInterface.CommandLine;
import myInterface.DebugScreen;
import myInterface.ErrorWindow;
import myInterface.GridWindow;
import myInterface.InfoWindow;
import myInterface.InputWindow;
import myInterface.ListWindow;
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
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import javax.swing.JPanel;
import javax.swing.Timer;

import myData.MyValidator;

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
	
	// Random variables
	public long randomMapSeed = System.currentTimeMillis();
	public Random randomMap = new Random(randomMapSeed);	
	public Random randomEvents = new Random();
	public Random randomTrivial = new Random();
	
	// Tracking variables
	public String state = "";

	// Private constants
	public final String GAMENAME = "BATTLEMAPS";
	public final String VERSIONNUMBER = "InDev version 0.2";
	public final String VERSIONNAME = "The City Update";
	public final String VERSIONINFO = "This version of the game is currently focussed on implementing the functionality of the city screen.";
	public final String VERSIONCOMPLETION = "90%";
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
		mim = new MyInterfaceManager(windowWidth, windowHeight, il);

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
					buttonExecution(button.getExecutionNumber(), button.getAdditionalString());
				} else {
					buttonExecution(button.getExecutionNumber(), "");
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
		}
	}
	
	// Function that runs the appropriate code for buttons than are clicked on.
	private void buttonExecution(int exec, String add) {

		if (DEBUG_TRACE) {
			String debugAdd = add;
			if (debugAdd.equals("")) {
				cmd.debug("EXECUTING BUTTON CODE NUMBER " + exec + " WITH NO ADDITIONAL STRING");     
			} else {
				cmd.debug("EXECUTING BUTTON CODE NUMBER " + exec + " WITH '" + debugAdd + "'.");        		
			}
		}

		// Creating commonly used variables.
		InfoWindow wind;
		Button butt;
		String cont;
		GridWindow gridow;
		Player currentPlayer;
		ListWindow listow;
		Building bldg;

		// Creating arrays for important values.
		String[] theKeys = {"Residential", "Happiness", "Military", "Diplomacy", "Technology", "Commerce", "Nature", "Industry"};
		Color[] theColors = {new Color(200,200,200), new Color(234,242,10), new Color(194,2,50), new Color(235,237,175), new Color(10,242,231), new Color(83,74,240), new Color(17,153,42), new Color(245,155,66)};

		// Creating a hash map for important values.
		HashMap<String, Integer> iconMap = new HashMap<String, Integer>();
		iconMap.put("Military", 51);
		iconMap.put("Technology", 52);
		iconMap.put("Nature", 53);
		iconMap.put("Diplomacy", 54);
		iconMap.put("Commerce", 55);
		iconMap.put("Industry", 56);
		iconMap.put("Population", 57);
		iconMap.put("Happiness", 58);
		
		// Executing the code.
		if (exec <= 0) {													// Null action.
		} 
		else if (exec == 1) {												// Go to map action.
			switchScreen("Map", "");
		}
		else if (exec == 2) {												// Go to city action. 
			switchScreen("City", add);
		}
		else if (exec == 3) {												// Go to menu action.
			switchScreen("Menu", add);
		}
		else if (exec == 4) { 												// End turn action.
			game.nextPlayer();
		}
		else if (exec == 5)	{												// View players.
			if (!mim.checkWindowsFor("Player Info")) {
				// Creating the new window.
				wind = new InfoWindow("Player Info", (windowWidth - 400) / 2, 100);
				butt = wind.getCloseButton();
				cont = "| Player Name\t| Commander Name\n|------------------------------------------------------------\n";
				for (int i = 0; i < game.getPlayers().size(); ++i) {
					// Generate table rows.
					currentPlayer = game.getPlayers().get(i);
					cont += "| " + currentPlayer.getName() + "\t";
					cont += "| " + currentPlayer.getCommander() + "\t";
					cont += "\n";
					// Generate player buttons.
					Button pb = new Button(218, 26 + i * (MyTextMetrics.getTextSizeFlat("View Player")[1] + wind.getLineSpacing() + 5) + 2 * (MyTextMetrics.getTextSizeFlat("View Player")[1] + wind.getLineSpacing()));
					pb.setHeight(MyTextMetrics.getTextSizeFlat("View Player")[1] + 4);
					pb.setWidth(MyTextMetrics.getTextSizeFlat("View Player")[0] + 2);
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
		}
		else if (exec == 6) { 												// View stats.
			if (!mim.checkWindowsFor("Game Stats")) {
				// Creating window.
				wind = new InfoWindow("Game Stats", (windowWidth - 400) / 2, 100);
				butt = wind.getCloseButton();
				cont = "Stats\n------\nWhat do I even put here.";
				wind.setContent(cont);
				mim.addWindowFull(wind);
			}
		}
		else if (exec == 7) {												// Close window.
			mim.getWindow(add).close();
		}
		else if (exec == 8) { 												// Open full player window.
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
		}
		else if (exec == 9) {												// Opens debug info window.
			if (!mim.checkWindowsFor("DEBUG_INFO_WINDOW")) {
				// Create window.
				wind = new InfoWindow("DEBUG_INFO_WINDOW", (windowWidth - 400) / 2, 100);
				butt = wind.getCloseButton();
				cont = "Current map seed:- " + randomMapSeed;
				wind.setContent(cont);
				mim.addWindowFull(wind);
			}
		}
		else if (exec == 10) {												// Opens debug action window.
			if (!mim.checkWindowsFor("DEBUG_ACTION_WINDOW")) {
				// Create window.
				int DEBUG_GRID_ROWS = 5;
				int DEBUG_GRID_COLS = 5;
				gridow = new GridWindow("DEBUG_ACTION_WINDOW", WINDOW_CENTER_X, 100, DEBUG_GRID_ROWS, DEBUG_GRID_COLS);
				gridow.setGridX(10);
				gridow.setGridY(10);
				gridow.setButtonWidth(60);
				gridow.setButtonHeight(30);
				// Creating grid buttons.
				if (true) {
					// Button 1: New game.
					Button d1 = new Button(0, 0, "DEBUG_B_New_Game", "New Game", 15); d1.setColorInner(Color.GREEN);
					gridow.addGridButton(0, 0, d1);
					// Button 2: Show debug routes.
					Button d2 = new Button(0, 0, "DEBUG_B_Switch_Routes", "Switch Routes", 13); d2.setColorInner(Color.GREEN);
					gridow.addGridButton(0, 1, d2);
					// Button 6: Debug info window.
					Button d6 = new Button(0, 0, "DEBUG_B_Info_Window", "Info Window", 9); d6.setColorInner(Color.GREEN);
					gridow.addGridButton(1, 0, d6);
					// Button 7: Debug input window.
					Button d7 = new Button(0, 0, "DEBUG_B_Input_Window", "Input Window", 36); d7.setColorInner(Color.GREEN);
					gridow.addGridButton(1, 1, d7);
					// Button 20: BuildDex dump.
					Button d20 = new Button(0, 0, "DEBUG_B_Dump_BuildDex", "Dump BuildDex", 33); d20.setColorInner(Color.GREEN);
					gridow.addGridButton(3, 4, d20);
					// Button 21: Print buttons.
					Button d21 = new Button(0, 0, "DEBUG_B_Print_Buttons", "Print Buttons", 25); d21.setColorInner(Color.GREEN);
					gridow.addGridButton(4, 0, d21);
					// Button 22: Print windows.
					Button d22 = new Button(0, 0, "DEBUG_B_Print_Windows", "Print Windows", 26); d22.setColorInner(Color.GREEN);
					gridow.addGridButton(4, 1, d22);
					// Button 23: Print screen.
					Button d23 = new Button(0, 0, "DEBUG_B_Print_Screen", "Print Screen", 34); d23.setColorInner(Color.GREEN);
					gridow.addGridButton(4, 2, d23);
					// Button 24: Print mouse.
					Button d24 = new Button(0, 0, "DEBUG_B_Print_Mouse", "Print Mouse", 32); d24.setColorInner(Color.GREEN);
					gridow.addGridButton(4, 3, d24);
					// Button 25: Close game.
					Button d25 = new Button(0, 0, "DEBUG_B_Close", "Close Game", 11); d25.setColorInner(Color.GREEN);
					gridow.addGridButton(4, 4, d25);
				}
				mim.addWindowFull(gridow);
			}
		}
		else if (exec == 11) {												// Closes the game.
			// TODO: Implement quitting.
		}
		else if (exec == 12) { 												// Generates a new random game.
			// Sets up the game state.
			game = initGame(add);
			switchScreen("Map", "");
		}
		else if (exec == 13) {												// Toggles map between optimised and initial routes.
			game.toggleDRD();
		}
		else if (exec == 14) { 												// Random map seed entry via command line.
			Scanner r = new Scanner(System.in);
			System.out.print("[!] New seed:- ");
			randomMapSeed = r.nextLong();
			randomMap = new Random(randomMapSeed);
			cmd.alert("New seed accepted.");
			r.close();
		}
		else if (exec == 15) {												// Jump to debug start screen.
			switchScreen("DEBUG", "");
		}														
		else if (exec == 16) {												// Open city add building menu.
			
			// Reset mouse data.
			mim.setMouseMode("Pointer");
			
			// Creating window.
			gridow = new GridWindow("Add Building", WINDOW_CENTER_X, 100, 5, 2);
			gridow.setButtonWidth(175);
			gridow.setButtonHeight(40);
			gridow.setButtonGap(10);
			
			// Placing buttons into the window grid.
			int lalala = 0;
			int lololo = 0;
			for (int lelele = 0; lelele < theKeys.length; ++lelele) {
				String key = theKeys[lelele];
				Color lliw = theColors[lelele];
				Button gwb = new Button(0,0,"City_Build_" + key, key, 21, key);
				gwb.setColorInner(lliw);
				gridow.addGridButton(lololo, lalala, gwb);
				++lalala;
				if (lalala >= 2) {lalala = 0; ++lololo;}
				if (key.equals("Happiness")) {++lololo;}
			}
			
			// Add window to the MIM.
			mim.addWindowFull(gridow);
			
		}
		else if (exec == 17) { 												// Activate city build building mode.
			// Gets the city name and city.
			String cityName = state.substring(5);
			City thisCity = game.getMap().getCityByName(cityName);
						
			if (thisCity.hasAreaFor(buildDex.getBuilding(add))) {
				mim.setMouseMode("Build");			
				mim.setMouseBuilding(buildDex.getBuilding(add));
				mim.removeWindowMaxFull("Add Building", 0, 12);
			} else {
				mim.addWindowSwap(new ErrorWindow("ERROR", ERRORX, ERRORY, "No room in the city for the building."));
			}
		}
		else if (exec == 18) { 												// Activate city move building mode.
			// Gets the city name and city.
			String cityName = state.substring(5);
			City thisCity = game.getMap().getCityByName(cityName);
			
			// Checks building count.
			if (thisCity.getBuildings().size() > 0) {
				mim.setMouseMode("Move");
			} else {
				mim.addWindowSwap(new ErrorWindow("ERROR", ERRORX, ERRORY, "No buildings to move."));
			}
		}
		else if (exec == 19) {												// Activate city remove building mode.
			// Gets the city name and city.
			String cityName = state.substring(5);
			City thisCity = game.getMap().getCityByName(cityName);
			
			// Checks building count.
			if (thisCity.getBuildings().size() > 0) {
				mim.setMouseMode("Destroy");
			} else {
				mim.addWindowSwap(new ErrorWindow("ERROR", ERRORX, ERRORY, "No buildings to remove."));
			}
		}
		else if (exec == 20) { 												// Open city rename city window.
			// Reset mouse.
			mim.setMouseMode("Pointer");
			// Generate basic input window.
			InputWindow inpu = new InputWindow("City Name Change", WINDOW_CENTER_X, 125, mim.getInputString());
			inpu.setContent("Please enter a new city name.");
			inpu.getWindowButtons().get(0).setExecutionNumber(37);
			inpu.getWindowButtons().get(0).setAdditionalString(add);
			mim.addWindowFull(inpu);
		}
		else if (exec == 21) {												// Open city building category window.
			List<String> the_buildings = buildDex.getCategory(add);
			listow = new ListWindow("Add Building - " + add + " Buildings", WINDOW_CENTER_X, 50, 7);
			listow.setHeight(500);
			listow.setGridY(60);
			listow.setButtonHeight(30);
			listow.setContentX(175);
			listow.setContentY(0);
			butt = new Button(listow.getContentX() + (listow.getWidth() / 8), listow.getHeight() - 60, "Begin_Mouse_Placement_Button", "Place Building", 17, "NULL");
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
		}
		else if (exec == 22) {														// Add building info to city building category window.
			// Splits the additional string into the necessary parts.
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
			while (MyTextMetrics.getTextSizeComplex(startingDescription)[0] > listow.getWidth() + 20 - (listow.getWidth() - listow.getContentX())) {
				// TODO: Split description with regard to spaces.
				int splitLength = 26;
				if (startingDescription.length() < splitLength) {splitLength = startingDescription.length();}
				formattedDescription += startingDescription.substring(0, splitLength) + "\n";
				startingDescription = startingDescription.substring(splitLength);
			}
			formattedDescription += startingDescription;
			cont += formattedDescription + "\n \n";

			// Continues adding text to the content variable.
			cont += "Max Health: " + bldg.getMaxHealth() + "\nTurns to Build: " + (bldg.getBuildTime() * -1) + "\n \n";
			
			// Displays all stats of the building.
			listow.clearImages();
			cont += "Effect: \n";
			for (String key : bldg.getPositives()) {
				String ender = " per turn.";
				if (key.equals("Population") || key.equals("Happiness")) {ender = " to total.";}
				cont += "        +" + bldg.getPoint(key) + ender + "\n";
				int imgX = listow.getContentX() + InfoWindow.TOP_BAR_HEIGHT;
				int imgY = listow.getContentY() + (MyTextMetrics.getTextSizeComplex(cont)[1]) + 1;
				listow.addImage(imgX, imgY, iconMap.get(key));
			}
			cont += "\n";
			for (String key : bldg.getNegatives()) {
				String ender = " per turn.";
				if (key.equals("Population") || key.equals("Happiness")) {ender = " to total.";}
				cont += "        +" + bldg.getPoint(key) + ender + "\n";
				int imgX = listow.getContentX() + InfoWindow.TOP_BAR_HEIGHT;
				int imgY = listow.getContentY() + (MyTextMetrics.getTextSizeComplex(cont)[1]) + 1;
				listow.addImage(imgX, imgY, iconMap.get(key));
			}
			cont += "\n";

			// Creates the images to display the building blueprint.
			cont += "Blueprint: \n \n";			
			String bldgBlpt = bldg.getBlueprintAsString();
			int blptX = listow.getContentX() + InfoWindow.TOP_BAR_HEIGHT;
			int blptY = listow.getContentY() + (MyTextMetrics.getTextSizeComplex(cont)[1]) + 5;
			for (String line : bldgBlpt.split("\n")) {
				for (String character : line.split("|")) {
					if (character.equals("T")) {listow.addImage(blptX, blptY, 31);}
					blptX += 15;
				}
				blptX = listow.getContentX() + InfoWindow.TOP_BAR_HEIGHT;
				blptY += 15;
			}

			// Sets the content in the window.
			listow.setContent(cont);

			// Edits the visibility of the Place Building button.
			listow.getWindowButtons().get(0).setVisible(true);
			listow.getWindowButtons().get(0).setAdditionalString(buildingType);
			
		}
		else if (exec == 23) {												// Open city ordinances window.
			
			// Reseting mouse.
			mim.setMouseMode("Pointer");
			
			// Getting data and setting up window.
			OrdinanceBook ords = this.game.getMap().getCityByName(add).getAllOrdinances();
			listow = new ListWindow("City Ordinances", WINDOW_CENTER_X, 50, 7);
			listow.setHeight(500);
			listow.setGridY(60);
			listow.setButtonHeight(30);
			listow.setContentX(0);
			listow.setContentY(-10);
			butt = new Button((listow.getWidth() / 8) + 175, listow.getHeight() - 60, "Toggle_Ordinace", "FUCK", 42, "NULL");
			butt.setVisible(false);
			listow.addWindowButton(butt);
			
			// Placing ordinances in window.
			Color activeColor = new Color(0,255,0);
			Color inactiveColor = new Color(255,0,0);
			listow.setContent("Ordinances available in " + add + ":\n\n");
			for (Ordinance ord : ords.getAllActive()) {
				Button ordb = new Button(0, 50, "Ordinance_" + ord.getName().replace(" ", "_"), ord.getName(), 41, ord.getName());
				ordb.setColorInner(activeColor);
				listow.addListButton(ordb);
			}
			for (Ordinance ord : ords.getAllInactive()) {
				Button ordb = new Button(0, 50, "Ordinance_" + ord.getName().replace(" ", "_"), ord.getName(), 41, ord.getName());
				ordb.setColorInner(inactiveColor);
				listow.addListButton(ordb);
			}
			listow.fillGridList();
			listow.setUpExec(43);
			listow.setDownExec(44);
			mim.addWindowFull(listow);
		}
		else if (exec == 24) {												// Open city info window.
			mim.setMouseMode("Pointer");
			InfoWindow info = new InfoWindow("City Info", WINDOW_CENTER_X, 100);
			info.setContent("No info to show.");
			mim.addWindowFull(info);
		}
		else if (exec == 25) {												// Print buttons.
			mim.debug("Buttons");
		}
		else if (exec == 26) { 												// Print windows.
			mim.debug("Windows");
		}
		else if (exec == 27) {												// Up button in ListWindow.
			listow = (ListWindow) mim.getWindow(add);
			listow.decPageNumber();
			listow.fillGridList();
			listow.setDownVis(true);
			listow.setUpVis(!listow.isAtMin());
		}
		else if (exec == 28) {												// Down button in ListWindow.
			listow = (ListWindow) mim.getWindow(add);
			listow.incPageNumber();
			listow.fillGridList();
			listow.setUpVis(true);
			listow.setDownVis(!listow.isAtMax());
		}														
		else if (exec == 29) {												// Return button handling.
			// Split execution string into window to open's exec code, window to open's add string, and the window to close's name.
			String[] breakdown = add.split("\\|");
			
			if (DEBUG_TRACE) {cmd.debug("RETURN BUTTON EXECUTION: " + breakdown[0] + ", " + breakdown[1] + ", " + breakdown[2] + ".");}

			// Opens the window to open.
			buttonExecution(Integer.parseInt(breakdown[0]), breakdown[1]);
			
			// Closes the window to close.
			mim.removeWindowFull(breakdown[2]);
		}
		else if (exec == 30) {												// Up button in Add Building ListWindow.
			listow = (ListWindow) mim.getWindow(add);
			buttonExecution(27, add);
			listow.setContent("Click on a button to select\na building.");
			listow.getWindowButtons().get(0).setVisible(false);
			listow.clearImages();
		}
		else if (exec == 31) {												// Down button in Add Building ListWindow.
			listow = (ListWindow) mim.getWindow(add);
			buttonExecution(28, add);
			listow.setContent("Click on a button to select\na building.");
			listow.getWindowButtons().get(0).setVisible(false);
			listow.clearImages();
		}
		else if (exec == 32) { 												// Mouse display.
			mim.debug("Mouse");
		}
		else if (exec == 33) {												// BuildDex dumper.
			System.out.println();
			System.out.println(buildDex);
		}
		else if (exec == 34) {												// Screen state printer.
			cmd.debug(scr.getTitle());
		} else if (exec == 35) {											// Opens actions window.
			wind = new InfoWindow("Actions Window", WINDOW_CENTER_X, 100);
			cont = "COMING SOON";
			wind.setContent(cont);
			mim.addWindowFull(wind);
		} else if (exec == 36) {											// Test input window.
			InputWindow inpu = new InputWindow("TEST_INPUT", WINDOW_CENTER_X, 125, mim.getInputString());
			inpu.setContent("Please enter some test data.");
			mim.addWindowFull(inpu);
		} else if (exec == 37) {											// Renaming city code.
			InputWindow inpu = (InputWindow) mim.getWindow("City Name Change");
			String newName = inpu.getInputString();
			if (val.validate("City Name", newName)) {
				if (game.isUniqueName(newName)) {
					game.updateName(add, newName);
					this.state = "City-" + newName;
					createQuickWindow("Success!", "The name of this city is now " + newName + "!");
					inpu.close();
				} else {
					createErrorWindow("Name is already in use.");
				}
			} else {
				createErrorWindow("Invalid name.\nPlease use only letters, numbers, hyphens and space.\nThe name must also be between 1 and 32 characters long.");
			}
		} 
		else if (exec == 38) {												// Clear input window code.
			InfoWindow inpu = mim.getWindow(add);
			String initContent = inpu.getContent();
			inpu.clearText();
			inpu.setContent(initContent);
		}
		else if (exec == 39) { 												// Random map seed entry via input window.
			// Generate basic input window.
			InputWindow inpu = new InputWindow("Seed Entry", WINDOW_CENTER_X, 125, mim.getInputString());
			inpu.setContent("Please enter a seed for the RNG.\nSeeds must be a long between 0 and 999999999999.");
			inpu.getWindowButtons().get(0).setExecutionNumber(40);
			mim.addWindowFull(inpu);
		}
		else if (exec == 40) {												// Random seed entry code.
			InputWindow inpu = (InputWindow) mim.getWindow("Seed Entry");
			String newSeed = inpu.getInputString();
			if (val.validate("Random Seed", newSeed)) {
				randomMap = new Random(Long.parseLong(newSeed));
				createQuickWindow("Random Seed Accepted", "New seed = '" + newSeed + "'.");
				inpu.close();
			} else {
				createErrorWindow("Invalid seed.");
			}
		}
		else if (exec == 41) {												// Add ordinance info to ordinance window.

			// Retrieves the window and building specified in the additional string.
			listow = (ListWindow) mim.getWindow("City Ordinances");
			Ordinance ord = this.game.getCityByName(state.substring(5)).getOrdinance(add);

			// Begins preparing the text content.
			listow.setContentX(175);
			cont = ord.getName() + "\n \n";
			String startingDescription = ord.getDesc().replace("$city", state.substring(5));
			String formattedDescription = "";

			// Splits the description over multiple lines if it is too long.
			while (MyTextMetrics.getTextSizeComplex(startingDescription)[0] > listow.getWidth() + 20 - (listow.getWidth() - listow.getContentX())) {
				// TODO: Split description with regard to spaces.
				int splitLength = 26;
				if (startingDescription.length() < splitLength) {splitLength = startingDescription.length();}
				formattedDescription += startingDescription.substring(0, splitLength) + "\n";
				startingDescription = startingDescription.substring(splitLength);
			}
			formattedDescription += startingDescription;
			cont += formattedDescription + "\n \n";

			// Continues adding text to the content variable.
			listow.clearImages();
			cont += "Effect: \n \n";
			for (String key : ord.getPositives()) {
				String ender = " per turn.";
				if (key.equals("Population") || key.equals("Happiness")) {ender = " to total.";}
				cont += "        +" + ord.getPoint(key) + ender + "\n";
				int imgX = listow.getContentX() + InfoWindow.TOP_BAR_HEIGHT;
				int imgY = listow.getContentY() + (MyTextMetrics.getTextSizeComplex(cont)[1]) + 1;
				listow.addImage(imgX, imgY, iconMap.get(key));
			}
			cont += "\n";
			for (String key : ord.getNegatives()) {
				String ender = " per turn.";
				if (key.equals("Population") || key.equals("Happiness")) {ender = " to total.";}
				cont += "        +" + ord.getPoint(key) + ender + "\n";
				int imgX = listow.getContentX() + InfoWindow.TOP_BAR_HEIGHT;
				int imgY = listow.getContentY() + (MyTextMetrics.getTextSizeComplex(cont)[1]) + 1;
				listow.addImage(imgX, imgY, iconMap.get(key));
			}

			// Sets the content in the window.
			listow.setContent(cont);

			// Edits the visibility of the Enact/Repeal Ordinance button.
			listow.getWindowButtons().get(0).setVisible(true);
			listow.getWindowButtons().get(0).setAdditionalString(add);
			String buttStr = "Enact";
			if (this.game.getCityByName(state.substring(5)).getOrdinanceActivity(add)) {
				buttStr = "Repeal";
			}
			listow.getWindowButtons().get(0).setButtonText(buttStr);;
			
		}
		else if (exec == 42) {												// Enact/repeal ordinance.
			// Getting city's ordinance book.
			City cit = this.game.getCityByName(state.substring(5));
			
			// Determining action based on status of ordinance.
			String enactStr = "enacted";
			if (cit.getOrdinanceActivity(add)) {
				cit.repealOrdinance(add);
				enactStr = "repealed";
			} else {
				cit.enactOrdinance(add);
			}
			
			// Closing window and displaying message.
			// TODO: Refresh window instead of closing.
			mim.getWindow("City Ordinances").close();
			createQuickWindow("Success!", add + " has been " + enactStr + " in " + state.substring(5) + ".");
			
		}
		else if (exec == 43) {												// Up button in Ordinance window.
			listow = (ListWindow) mim.getWindow(add);
			buttonExecution(27, add);
			listow.setContent("Ordinances available in " + state.substring(5) + ":\n\n");
			listow.setContentX(0);
			listow.getWindowButtons().get(0).setVisible(false);
			listow.clearImages();
		}
		else if (exec == 44) {												// Down button in Ordinance window.
			listow = (ListWindow) mim.getWindow(add);
			buttonExecution(28, add);
			listow.setContent("Ordinances available in " + state.substring(5) + ":\n\n");
			listow.setContentX(0);
			listow.getWindowButtons().get(0).setVisible(false);
			listow.clearImages();
		}

	}
	
	public void createQuickWindow(String title, String content) {
		mim.addWindow(new ErrorWindow(title, ERRORX, ERRORY, content));
	}
	
	public void createErrorWindow(String in) {
		this.createQuickWindow("ERROR", in);
	}

}