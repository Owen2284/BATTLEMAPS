/*
 *  Board.java
 *
 *  Main graphics object for BATTLEMAPS.
 *
 */

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
	private int window_width;
	private int window_height;
	
	private long random_map_seed = System.currentTimeMillis();
	private Random random_events = new Random();
	private Random random_map = new Random(random_map_seed);
	private Game game;
	private BuildingFactory build_dex = new BuildingFactory();
	private ImageLibrary il = new ImageLibrary(100);

	private String state = "";
	private MyInterfaceManager mim;
	private HoverWindow mouse_window = new HoverWindow(0,0);

	private int player_whose_go_it_is = 1;										// OWEN: Move into game.

	// Constants
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
		window_width = width;
		window_height = height;
		WINDOW_CENTER_X = (window_width - 400) / 2;

		// Sets up graphical interface.
		initBoard();
		
		// Retrieves all resources needed at startup.
		initImages();

		// Initialise interface manager.
		mim = new MyInterfaceManager(window_width, window_height, il);

		// Checks for debug screen choice.
		if (DEBUG_MAPS) {
			state = "DEBUG";
			mim.setInterface(state, game, player_whose_go_it_is);
		} else {
			// Sets up the game state.
			game = initGame("Normal");

			// Sets game state.
			state = "Map";

			// Sets up buttons.
			mim.setInterface(state, game, player_whose_go_it_is);
		}

		// Sorts out hover window.
		mouse_window.setOpen(false);

	}

	private void initBoard() {

		// Set up JPanel settings.
		this.setFocusable(true);
		this.setBackground(Color.BLUE);
		this.setPreferredSize(new Dimension(window_width, window_height));

		// Adds a mouse listener to the panel.
		this.addMouseListener(this);

		// Initialise timer.
		timer = new Timer(DELAY, this);
		timer.start();

	}

	private void initImages() {

		if (DEBUG_LOAD) {System.out.println("Beginning image load.");}

		il.loadImage(1, "Images/MouseSelect.png");
		il.loadImage(2, "Images/MouseBuild.png");
		il.loadImage(3, "Images/MouseMove.png");
		il.loadImage(4, "Images/MouseDestroy.png");
		il.loadImage(11, "Images/Flag0.png");
		il.loadImage(12, "Images/Flag1.png");
		il.loadImage(13, "Images/Flag2.png");
		il.loadImage(14, "Images/Flag3.png");
		il.loadImage(15, "Images/Flag4.png");
		il.loadImage(16, "Images/Flag5.png");
		il.loadImage(17, "Images/Flag6.png");
		il.loadImage(18, "Images/Flag7.png");
		il.loadImage(19, "Images/Flag8.png");
		il.loadImage(21, "Images/CityHighlight.png");
		il.loadImage(22, "Images/CityNeighbour.png");

	}

	public Game initGame(String mapType) {

		// Initialisation.
		int city_count = 10;								// Number of cities on the map.
		int map_height = window_height - City.CITY_SIZE;	// Height of map (y length)
		int map_width = window_width - City.CITY_SIZE;		// Width of map (x length)
		int cities_per_player = 1;							// Number of cities each player stars with.
		int player_count = 4;								// Number of players in a game.
		boolean give_connected_cities = false;				// Unused at the moment.

		switch (mapType) {
			case "Normal":
				break;
			case "Small":
				city_count = 5;
				map_height = (window_height / 2) - City.CITY_SIZE;
				map_width = (window_width / 2) - City.CITY_SIZE;
				break;
			case "Large":
				city_count = 25;
				map_height = (window_height * 2) - City.CITY_SIZE;
				map_width = (window_width * 2) - City.CITY_SIZE;
				player_count = 8;
				break;
			case "Full":
				city_count = 12;
				map_height = window_height - City.CITY_SIZE;
				map_width = window_width - City.CITY_SIZE;
				cities_per_player = 3;
				break;
		}

		// Creates the game objects and sets it up.
		Map newMap = new Map(city_count, map_width, map_height, BORDER_SIZE, random_map);
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
		for (int i = 0; i < player_count; ++i) {
			game.addPlayer(new Player(i, "Player " + i));
		}

		// Gives a random city to each player.
		ArrayList<City> allCities = game.getMap().getCities();
		ArrayList<Player> allPlayers = game.getPlayers();
		for (int c = 0; c < cities_per_player; ++c) {
			for (int i = 0; i < allPlayers.size(); ++i) {
				int cityNumber = random_map.nextInt(allCities.size());
				while (!allCities.get(cityNumber).getOwner().equals("NONE")) {
					cityNumber = random_map.nextInt(allCities.size());
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

		random_map = new Random(r);
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
		g.drawRect(0,0,window_width-1,window_height-1);

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
				ArrayList<City> current_city_neighbours = tempMap.getNeighboursOf(currentCity.getName());
				for (City neighbour : current_city_neighbours) {
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
		g.fillRect(0, window_height - 40, window_width, window_height);
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, 80, 21);
		g.drawRect(0, window_height - 40, window_width, window_height);
		g.drawString("Turn " + Integer.toString(game.getTurn()), 4, 16);

		// Draws mouse hover window.
		if (mouse_window.isOpen()) {
			mouse_window.draw(g);          
		}

	}

	private void drawCity(Graphics g) {

		// Sets background color.
		Color backgroundColor = new Color(0,200,0);
		this.setBackground(backgroundColor);

		// Gets the city name and city.
		String cityName = state.substring(5);
		City this_city = game.getMap().getCityByName(cityName);

		// Draws city squares.
		ArrayList<Block> cityBlocks = this_city.getGrid();
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
		g.fillRect(0, window_height - 40, window_width, 40);								// Bottom bar.
		g.fillRect(750, 0, 250, window_height - 40);										// City buttons box.
		g.fillRect((window_width / 2) - 64, 0, 128, 32);									// City name box.
		g.fillRect(0, window_height / 2, City.GRID_OFFSET_X - 20, window_height - 40);		// Stats box.

		g.setColor(Color.BLACK);
		g.drawRect(0, 0, 80, 21); 															// Turn counter.
		g.drawRect(0, window_height - 40, window_width, window_height);						// Bottom bar.
		g.drawRect(750, 0, 250, window_height - 40);										// City buttons box.
		g.drawRect((window_width / 2) - 64, 0, 128, 32);									// City name box.
		g.drawRect(0, window_height / 2, City.GRID_OFFSET_X - 20, (window_height / 2) - 40);	// Stats box.

		// Draws text.
		g.drawString("Turn " + Integer.toString(game.getTurn()), 4, 16);
		g.drawString(cityName, (window_width / 2) - (5 * cityName.length()), 22);
		g.drawString(cityName, 5, window_height / 2 + 5 + MyTextMetrics.getTextSize("Text")[1]);
		int offset = 3;
		String[] the_keys = {"Population", "Happiness", "Military", "Technology", "Nature", "Diplomacy", "Commerce", "Industry"};
		for (int i = 0; i < the_keys.length; ++i) {
			if (the_keys[i].equals("Military")) {++offset;}
			g.drawString(the_keys[i] + ": \t " + this_city.getStat(the_keys[i]), 5, window_height / 2 + (5 + MyTextMetrics.getTextSize("Text")[1]) * (i + offset));
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
		boolean still_hovered = false;
		String hovered_city = "";

		// Check for mouse hover windows.
		for (int i = 0; i < tempCities.size(); ++i) {
			
			// Gets the city being operated on.
			City currentCity = tempCities.get(i);

			// Checks if city is being hovered over.
			Rectangle cityBounds = currentCity.getBounds();
			if (cityBounds.contains(mim.getMousePos())) {
				still_hovered = true;
				hovered_city = currentCity.getName();
			}

		}

		// Closes mouse hover window if no hover is detected.
		if (!still_hovered) {
			mouse_window.setActive(false);
			mouse_window.setOpen(false);
		} else {
			// Sorts out the window if there is a hover.
			if (mouse_window.isOpen()) {
				mouse_window.update(mim.getMousePos());
				if ((mouse_window.getX() + mouse_window.getWidth()) > window_width) {
					mouse_window.setX(window_width - mouse_window.getWidth());
				}
				if ((mouse_window.getY() + mouse_window.getHeight()) > window_height - 40) {
					mouse_window.setY(window_height - mouse_window.getHeight() - 40);
				}                
			} else {
				mouse_window = new HoverWindow(mim.getMousePos().x, mim.getMousePos().y);
				mouse_window.setActive(true);
				mouse_window.setContent(hovered_city);
				mouse_window.update(mim.getMousePos());
				if ((mouse_window.getX() + mouse_window.getWidth()) > window_width) {
					mouse_window.setX(window_width - mouse_window.getWidth());
				}
				if ((mouse_window.getY() + mouse_window.getHeight()) > window_height - 40) {
					mouse_window.setY(window_height - mouse_window.getHeight() - 40);
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

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseClicked(MouseEvent e) {

		// Allows for only 1 button press.
		boolean click_this_frame = false;

		// Get buttons being hovered over.
		ArrayList<Button> hovered_buttons = mim.getHoveredButtons();

		// Execute code of clicked button.
		for (Button button : hovered_buttons) {
			if (!click_this_frame) {
				click_this_frame = true;
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
					mim.setInterface(state, game, player_whose_go_it_is);
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
			String debug_add = add;
			if (debug_add.equals("")) {
				System.out.println("EXECUTING BUTTON CODE NUMBER " + exec + " WITH NO ADDITIONAL STRING");     
			} else {
				System.out.println("EXECUTING BUTTON CODE NUMBER " + exec + " WITH '" + debug_add + "'.");        		
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

		// Creating arrays for important values.
		String[] the_keys = {"Residential", "Happiness", "Military", "Diplomacy", "Technology", "Commerce", "Nature", "Industry"};
		Color[] the_colors = {new Color(200,200,200), new Color(234,242,10), new Color(194,2,50), new Color(235,237,175), new Color(10,242,231), new Color(83,74,240), new Color(17,153,42), new Color(245,155,66)};

		switch (exec) {
			case -1: break;                                                 // Null action.
			case 0: break;                                                  // Null action.
			case 1: state = "Map"; mim.setInterface(state, game, player_whose_go_it_is); break;              // Go to map action.
			case 2: state = "City-" + add; mim.setInterface(state, game, player_whose_go_it_is); break;      // Go to city action. 
			case 3: state = "Menu-" + add; mim.setInterface(state, game, player_whose_go_it_is); break;      // Go to menu action.
			case 4:                                                         // End turn action.
				// Move to the next player.
				player_whose_go_it_is += 1;
				if (player_whose_go_it_is > game.getPlayers().size()) {
					// Other end of turn code.
					// Increment game turn.
					player_whose_go_it_is = 1;
					game.incTurn();
				}
				break;
			case 5:                                                         // View players.
				if (!mim.checkWindowsFor("Player Info")) {
					// Creating the new window.
					wind = new InfoWindow("Player Info", (window_width - 400) / 2, 100);
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
						Button pb5 = new Button(wind.getX() + 220, wind.getY() + 30 + (-2) + InfoWindow.TOP_BAR_HEIGHT + i * (MyTextMetrics.getTextSize("TEST")[1] + wind.getLineSpacing() + 5) + 2 * (MyTextMetrics.getTextSize("TEST")[1] + wind.getLineSpacing()));
						pb5.setHeight(MyTextMetrics.getTextSize("View Player")[1]);
						pb5.setWidth(MyTextMetrics.getTextSize("View Player")[0]);
						pb5.setID(wind.getTitle() + "_" + currentPlayer.getName());
						pb5.setButtonText("View Player");
						pb5.setExecutionNumber(8);
						pb5.setAdditionalString(currentPlayer.getName());
						pb5.setAdditionalStringUsage(true);
						pb5.setOwner("Window");
						wind.addWindowButton(pb5);
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
					wind = new InfoWindow("Game Stats", (window_width - 400) / 2, 100);
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
					wind = new InfoWindow("Player - " + add, (window_width - 400) / 2, 100);
					butt = wind.getCloseButton();
					cont = add;
					wind.setContent(cont);
					// Close Player Info window.
					mim.removeWindowFull("Player Info");
					mim.addWindowFull(wind);
				}
				break;
			case 9:                                                         // Opens debug info window.
				if (!mim.checkWindowsFor("DEBUG_INFO_WINDOW")) {
					// Create window.
					wind = new InfoWindow("DEBUG_INFO_WINDOW", (window_width - 400) / 2, 100);
					butt = wind.getCloseButton();
					cont = "Current map seed:- " + random_map_seed;
					wind.setContent(cont);
					mim.addWindowFull(wind);
				}
				break;
			case 10:                                                        // Opens debug action window.
				if (!mim.checkWindowsFor("DEBUG_ACTION_WINDOW")) {
					// Create window.
					int DEBUG_GRID_ROWS = 5;
					int DEBUG_GRID_COLS = 5;
					gridow = new GridWindow("DEBUG_ACTION_WINDOW", (window_width - 400) / 2, 100, DEBUG_GRID_ROWS, DEBUG_GRID_COLS);
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
				mim.setInterface(state, game, player_whose_go_it_is);
				mim.removeWindowFull("DEBUG_LAUNCH");
				break;
			case 13:														// Toggles map between optimised and initial routes.
				// Changes the map's display mode.
				game.toggleDRD();
				break;
			case 14:														// Random map seed entry.
				Scanner r = new Scanner(System.in);
				System.out.print("New seed:- ");
				random_map_seed = r.nextLong();
				random_map = new Random(random_map_seed);
				if (DEBUG_TRACE) {System.out.println("New seed accepted.");}
				break;
			case 15: 														// Jump to debug start screen.
				state = "DEBUG"; 
				mim.setInterface(state, game, player_whose_go_it_is); 
				break; 
			case 16:														// Open city add building menu.
				gridow = new GridWindow("Add Building", WINDOW_CENTER_X, 100, 5, 2);
				gridow.setButtonWidth(175);
				gridow.setButtonHeight(40);
				gridow.setButtonGap(10);
				int lalala = 0;
				int lololo = 0;
				for (int lelele = 0; lelele < the_keys.length; ++lelele) {
					String key = the_keys[lelele];
					Color lliw = the_colors[lelele];
					Button gwb = new Button(0,0,"City_Build_" + key, key, 21, key);
					gwb.setColorInner(the_colors[lelele]);
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
				List<String> the_buildings = build_dex.getCategory(add);
				listow = new ListWindow("Add Building - " + add + " Buildings", WINDOW_CENTER_X, 100);
				listow.setHeight(400);
				// Set grid x.
				listow.setGridY(60);
				listow.setButtonHeight(30);
				// Set button width.
				// Set button gap.
				listow.setContentX(150);
				// Set content y.
				Color this_color = new Color(255,255,255);
				for (int i = 0; i < the_keys.length; ++i) {
					if (the_keys[i].equals(add)) {
						this_color = the_colors[i];
					}
				}
				listow.setContent("Click on a button to select a building.");
				for (String bld : the_buildings) {
					Button bld_b = new Button(0, 50, "Build_" + bld.replace(" ", "_"), bld, 22, bld);
					bld_b.setColorInner(this_color);
					listow.addListButton(bld_b);
				}
				listow.fillGridList();
				mim.addWindowFull(listow);
				mim.removeWindowFull("Add Building");
				break;
			case 22:														// Add building info to city building category window.

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
		}    

	}

}