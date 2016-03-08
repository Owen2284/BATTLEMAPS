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
	private Point mouse_pos;											// @MIM: Remove
	private Rectangle mouse_area;
	
	private long random_map_seed = System.currentTimeMillis();
	private Random random_events = new Random();
	private Random random_map = new Random(random_map_seed);
	private Game game;
	private BuildingFactory build_dex;
	private ImageLibrary il = new ImageLibrary(100);

	private String state = "";
	private MyInterfaceManager mim;
	private ArrayList<Button> buttons = new ArrayList<Button>();				// @MIM: Remove
	private ArrayList<InfoWindow> windows = new ArrayList<InfoWindow>();		// @MIM: Remove
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
			initInterface(state);					// @MIM: Remove
		} else {
			// Sets up the game state.
			game = initGame("Normal");

			// Sets game state.
			state = "Map";

			// Sets up buttons.
			initInterface(state);					// @MIM: Remove
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

	// @MIM: Remove
	private void initInterface(String in) {

		// Clears ArrayLists.
		this.buttons.clear();
		while (windows.size() > 0) {
			buttonExecution(7, windows.get(0).getTitle());
		}

		if (DEBUG_LAUNCH) {System.out.println("Initialising interface for " + in + ".");}

		if (DEBUG_WINDOW && !state.equals("DEBUG")) {
			// Debug info window button.
			Button db = new Button((window_width / 4), window_height - 36);
			db.setID("DEBUG_INFO_WINDOW");
			db.setColorInner(Color.GREEN);
			db.setExecutionNumber(9);
			db.setButtonText("DEBUG_INFO");
			buttons.add(db);

			// Debug action window button.
			db = new Button(3*(window_width / 4) - 128, window_height - 36);
			db.setID("DEBUG_ACTION_WINDOW");
			db.setColorInner(Color.GREEN);
			db.setExecutionNumber(10);
			db.setButtonText("DEBUG_ACTION");
			buttons.add(db);

			// OWEN: Maybe add in debug log?

		}

		if (in.equals("Map")) {

			// End turn button.
			Button et = new Button(64, window_height - 36);
			et.setID("End Turn");
			et.setExecutionNumber(4);
			et.setButtonText("End Turn");
			buttons.add(et);

			// View players button.
			Button vp = new Button((window_width / 2) - 64, window_height - 36);
			vp.setID("View Players");
			vp.setExecutionNumber(5);
			vp.setButtonText("View Players");
			buttons.add(vp);

			// View stats button.
			Button vs = new Button(window_width - 192, window_height - 36);
			vs.setID("View Stats");
			vs.setExecutionNumber(6);
			vs.setButtonText("View Stats");
			buttons.add(vs);

		} else if (in.substring(0,4).equals("City")) {

			// Get the city.
			City the_city = game.getCityByName(in.substring(5));

			// Back to map button.
			Button btm = new Button(window_width - 192, window_height - 36);
			btm.setID("Back to Map");
			btm.setExecutionNumber(1);
			btm.setButtonText("Back to Map");
			buttons.add(btm);

			// City editing buttons.
			if (game.getActivePlayer(player_whose_go_it_is).getID().equals(the_city.getOwner())) {
				Button ab = new Button(770, 20, window_width - 790, 40, "City_Build", "Add Building", 16); buttons.add(ab);
				Button mb = new Button(770, 90, window_width - 790, 40, "City_Move", "Move Building", 18); buttons.add(mb);
				Button db = new Button(770, 160, window_width - 790, 40, "City_Remove", "Remove Building", 19); buttons.add(db);
				Button co = new Button(770, 290, window_width - 790, 40, "City_Ordinances", "City Ordinances", 23); buttons.add(co);
				Button rc = new Button(770, 400, window_width - 790, 40, "City_Rename", "Rename City", 20); buttons.add(rc);
			}
			Button ci = new Button(770, 470, window_width - 790, 40, "City_Info", "View City Info", 24); buttons.add(ci);

		} else if (in.substring(0,4).equals("Menu")) {

		} else if (in.equals("DEBUG")) {
			GridWindow dw = new GridWindow("DEBUG_LAUNCH", (window_width - 400) / 2, 100, 4, 3);
			dw.setGridX(10);
			dw.setGridY(10);

			Button dwb = new Button(0, 0, "DEBUG_MENU_Normal", "Normal Map", 12, "Normal");
			dwb.setColorInner(Color.GREEN);
			dw.addGridButton(0, 0, dwb); buttons.add(dwb);
			dwb = new Button(0, 0, "DEBUG_MENU_Small", "Small Map", 12, "Small");
			dwb.setColorInner(Color.GREEN);
			dw.addGridButton(1, 0, dwb); buttons.add(dwb);
			dwb = new Button(0, 0, "DEBUG_MENU_Large", "Large Map", 12, "Large");
			dwb.setColorInner(Color.GREEN);
			dw.addGridButton(2, 0, dwb); buttons.add(dwb);
			dwb = new Button(0, 0, "DEBUG_MENU_Full", "Full Map", 12, "Full");
			dwb.setColorInner(Color.GREEN);
			dw.addGridButton(3, 0, dwb); buttons.add(dwb);
			dwb = new Button(0, 0, "DEBUG_MENU_Seed", "Enter Seed", 14);
			dwb.setColorInner(Color.GREEN);
			dw.addGridButton(0, 2, dwb); buttons.add(dwb);

			buttons.add(dw.getCloseButton());

			windows.add(dw);
		}

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

		// Draws mouse cursor.
		g.drawImage(il.getImage(1), (int) mouse_pos.getX(), (int) mouse_pos.getY(), this);

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
			if (cityBounds.contains(mouse_pos)) {
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

		// @MIM: Remove

		// Places basic buttons.
		for (int i = 0; i < this.buttons.size(); ++i) {
			Button currentButton = this.buttons.get(i);
			if (!currentButton.getOwner().equals("Window")) {
				currentButton.drawShadow(g);
				if (currentButton.isHovering(mouse_pos)) {
					currentButton.drawHover(g);
				} else {
					currentButton.drawButton(g);
				}
			}            
		}

		// Draws mouse hover window.
		if (mouse_window.isOpen()) {
			mouse_window.draw(g);          
		}

		// Draws any info windows.
		for (int i = 0; i < this.windows.size(); ++i) {
			InfoWindow currentWindow = this.windows.get(i);
			currentWindow.draw(g);
			if (currentWindow.getAnimationStatus().equals("Open")) {
				for (Button item: this.buttons) {
					if (item.isHovering(mouse_pos)) {
						item.drawHover(g);
					} else {
						item.drawButton(g); 
					}  
				}
				Button currentButton = currentWindow.getCloseButton();              
				if (currentButton.isHovering(mouse_pos)) {
					currentButton.drawHover(g);
				} else {
					currentButton.drawButton(g); 
				}    
			}
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
			if (currentBlock.isOver(mouse_pos)) {
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
		for (int i = 0; i < Building.KEYS.length; ++i) {
			if (Building.KEYS[i].equals("Population")) {++offset;}
			g.drawString(Building.KEYS[i] + ": \t " + this_city.getStat(Building.KEYS[i]), 5, window_height / 2 + (5 + MyTextMetrics.getTextSize("Text")[1]) * (i + offset));
		}

		// @MIM: Remove

		// Places buttons.
		for (int i = 0; i < this.buttons.size(); ++i) {
			Button currentButton = this.buttons.get(i);
			currentButton.drawShadow(g);                        
			if (currentButton.isHovering(mouse_pos)) {
				currentButton.drawHover(g);
			} else {
				currentButton.drawButton(g);
			}            
		}

	}

	public void drawDebug(Graphics g) {

		// Sets background color.
		this.setBackground(Color.BLACK);

		// @MIM: Remove

		// Draws any info windows.
		for (int i = 0; i < this.windows.size(); ++i) {
			InfoWindow currentWindow = this.windows.get(i);
			currentWindow.draw(g);
			if (currentWindow.getAnimationStatus().equals("Open")) {
				for (Button item: this.buttons) {
					if (item.isHovering(mouse_pos)) {
						item.drawHover(g);
					} else {
						item.drawButton(g); 
					}  
				}
				Button currentButton = currentWindow.getCloseButton();              
				if (currentButton.isHovering(mouse_pos)) {
					currentButton.drawHover(g);
				} else {
					currentButton.drawButton(g); 
				}    
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		act(e);

		repaint();
		
	}

	private void act(ActionEvent e) {

		// Gets mouse position.
		mouse_pos = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(mouse_pos, this);
		mim.setMousePos(mouse_pos);

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
			if (cityBounds.contains(mouse_pos)) {
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
				mouse_window.update(mouse_pos);
				if ((mouse_window.getX() + mouse_window.getWidth()) > window_width) {
					mouse_window.setX(window_width - mouse_window.getWidth());
				}
				if ((mouse_window.getY() + mouse_window.getHeight()) > window_height - 40) {
					mouse_window.setY(window_height - mouse_window.getHeight() - 40);
				}                
			} else {
				mouse_window = new HoverWindow(mouse_pos.x, mouse_pos.y);
				mouse_window.setActive(true);
				mouse_window.setContent(hovered_city);
				mouse_window.update(mouse_pos);
				if ((mouse_window.getX() + mouse_window.getWidth()) > window_width) {
					mouse_window.setX(window_width - mouse_window.getWidth());
				}
				if ((mouse_window.getY() + mouse_window.getHeight()) > window_height - 40) {
					mouse_window.setY(window_height - mouse_window.getHeight() - 40);
				}
			}
		}

		// @MIM: Replace

		// Updates all of the currently running windows.
		for (int i = 0; i < this.windows.size(); ++i) {
			MyWindow currentWindow = this.windows.get(i);
			currentWindow.update(mouse_pos);            
		}

	}

	public void actMenu() {

	}

	public void actCity() {

	}

	public void actDebug() {

		// @MIM: Replace

		// Updates all of the currently running windows.
		for (int i = 0; i < this.windows.size(); ++i) {
			MyWindow currentWindow = this.windows.get(i);
			currentWindow.update(mouse_pos);            
		}

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

		// @MIM: Rework

		// Checks the buttons to see if they were clicked.
		for (int i = 0; i < this.buttons.size(); ++i) {
			Button currentButton = this.buttons.get(i);
			if (currentButton.isHovering(mouse_pos) && (!click_this_frame)) {
				click_this_frame = true;
				if (currentButton.hasAdditionalString()) {
					buttonExecution(currentButton.getExecutionNumber(), currentButton.getAdditionalString());
				} else {
					buttonExecution(currentButton.getExecutionNumber(), "");
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

				if (cityBounds.contains(mouse_pos)) {
					state = "City-" + currentCity.getName();
					initInterface(state);
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

		switch (exec) {
			case -1: break;                                                 // Null action.
			case 0: break;                                                  // Null action.
			case 1: state = "Map"; initInterface(state); break;               // Go to map action.
			case 2: state = "City-" + add; initInterface(state); break;      // Go to city action. 
			case 3: state = "Menu-" + add; initInterface(state); break;      // Go to menu action.
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
				// Check to see if window already exists.
				found = false;
				for (InfoWindow item : windows) {
					if (item.getTitle().equals("Player Info")) {found = true;}
				}
				if (!found) {
					// Creating the new window.
					InfoWindow wind5 = new InfoWindow("Player Info", (window_width - 400) / 2, 100);
					Button butt5 = wind5.getCloseButton();
					String cont5 = "| Player Name\t| Commander Name\n|------------------------------------------------------------\n";
					int COL_WIDTH = 30;
					ArrayList<Button> temp_buttons5 = wind5.getButtons();
					for (int i = 0; i < game.getPlayers().size(); ++i) {
						// Generate table row.
						currentPlayer = game.getPlayers().get(i);
						cont5 += "| " + currentPlayer.getName() + "\t";
						cont5 += "| " + currentPlayer.getCommander() + "\t";
						cont5 += "\n";
						// Generate player button.
						Button pb5 = new Button(wind5.getX() + 220, wind5.getY() + 30 + (-2) + InfoWindow.TOP_BAR_HEIGHT + i * (MyTextMetrics.getTextSize("TEST")[1] + wind5.getLineSpacing() + 5) + 2 * (MyTextMetrics.getTextSize("TEST")[1] + wind5.getLineSpacing()));
						pb5.setHeight(MyTextMetrics.getTextSize("View Player")[1]);
						pb5.setWidth(MyTextMetrics.getTextSize("View Player")[0]);
						pb5.setID(wind5.getTitle() + "_" + currentPlayer.getName());
						pb5.setButtonText("View Player");
						pb5.setExecutionNumber(8);
						pb5.setAdditionalString(currentPlayer.getName());
						pb5.setAdditionalStringUsage(true);
						pb5.setOwner("Window");
						temp_buttons5.add(pb5);
						buttons.add(pb5);
					}
					wind5.setContent(cont5);
					wind5.setLineSpacing(5);
					windows.add(wind5);
					buttons.add(butt5);
				}
				if (DEBUG_TRACE) {debug("Buttons");}
				break;
			case 6:                                                         // View stats.
				// Check to see if window already exists.
				found = false;
				for (InfoWindow item : windows) {
					if (item.getTitle().equals("Game Stats")) {found = true;}
				}
				if (!found) {
					// Creating window.
					InfoWindow wind6 = new InfoWindow("Game Stats", (window_width - 400) / 2, 100);
					Button butt6 = wind6.getCloseButton();
					String cont6 = "Stats\n------\nWhat do I even put here.";
					wind6.setContent(cont6);
					windows.add(wind6);
					buttons.add(butt6);
				}
				break;
			case 7:                                                         // Close window.
				for (int ii = 0; ii < this.windows.size(); ++ii) {
					currentWindow = this.windows.get(ii);
					if (currentWindow.getTitle().equals(add)) {
						currentWindow.close();
						for (Button item : currentWindow.getAllButtons()) {
							buttons.remove(item);
						}
						currentWindow.removeAllButtons();
						windows.remove(currentWindow);
					}
				}
				break;
			case 8:                                                         // Open full player window.
				if (!checkWindowsFor("Player - " + add)) {
					// Create window.
					InfoWindow wind8 = new InfoWindow("Player - " + add, (window_width - 400) / 2, 100);
					Button butt8 = wind8.getCloseButton();
					String cont8 = add;
					wind8.setContent(cont8);
					windows.add(wind8);
					buttons.add(butt8);
					// Close Player Info window.
					buttonExecution(7, "Player Info");
				}
				break;
			case 9:                                                         // Opens debug info window.
				if (!checkWindowsFor("DEBUG_INFO_WINDOW")) {
					// Create window.
					InfoWindow wind9 = new InfoWindow("DEBUG_INFO_WINDOW", (window_width - 400) / 2, 100);
					Button butt9 = wind9.getCloseButton();
					String cont9 = "Current map seed:- " + random_map_seed;
					wind9.setContent(cont9);
					windows.add(wind9);
					buttons.add(butt9);
				}
				break;
			case 10:                                                        // Opens debug action window.
				if (!checkWindowsFor("DEBUG_ACTION_WINDOW")) {
					// Create window.
					int DEBUG_GRID_ROWS = 5;
					int DEBUG_GRID_COLS = 5;
					GridWindow wind10 = new GridWindow("DEBUG_ACTION_WINDOW", (window_width - 400) / 2, 100, DEBUG_GRID_ROWS, DEBUG_GRID_COLS);
					wind10.setGridX(10);
					wind10.setGridY(10);
					wind10.setButtonWidth(60);
					wind10.setButtonHeight(30);
					Button butt10 = wind10.getCloseButton();
					// Creating grid buttons.
					if (true) {
						// Button 1: New game.
						Button d1 = new Button(0, 0);
						d1.setID("DEBUG_B_New_Game");
						d1.setColorInner(Color.GREEN);
						d1.setExecutionNumber(15);
						d1.setButtonText("New Game");
						buttons.add(d1);
						wind10.addGridButton(0, 0, d1);
						// Button 2: Show debug routes.
						Button d2 = new Button(0, 0);
						d2.setID("DEBUG_B_Switch_Routes");
						d2.setColorInner(Color.GREEN);
						d2.setExecutionNumber(13);
						d2.setButtonText("Switch Routes");
						buttons.add(d2);
						wind10.addGridButton(0, 1, d2);
						// Button 21: Print buttons.
						Button d21 = new Button(0, 0, "DEBUG_B_Print_Buttons", "Print Buttons", 25); d21.setColorInner(Color.GREEN);
						buttons.add(d21); wind10.addGridButton(4, 0, d21);
						// Button 22: Print windows.
						Button d22 = new Button(0, 0, "DEBUG_B_Print_Windows", "Print Windows", 26); d22.setColorInner(Color.GREEN);
						buttons.add(d22); wind10.addGridButton(4, 1, d22);
						// Button 25: Close game.
						Button d25 = new Button(0, 0);
						d25.setID("DEBUG_B_Close");
						d25.setColorInner(Color.GREEN);
						d25.setExecutionNumber(11);
						d25.setButtonText("Close Game");
						buttons.add(d25);
						wind10.addGridButton(4, 4, d25);
					}
					windows.add(wind10);
					buttons.add(butt10);
				}
				break;
			case 11:                                                        // Closes the game.
				// OWEN: Broken atm.
				break;
			case 12:														// Generates a new random game.
				// Sets up the game state.
				game = initGame(add);
				state = "Map";
				initInterface(state);
				buttonExecution(7, "DEBUG_LAUNCH");
				break;
			case 13:														// Toggles map between optimised and initial routes.
				// Changes the map's display mode.
				game.getMap().toggleDRD();
				break;
			case 14:														// Random map seed entry.
				Scanner r = new Scanner(System.in);
				System.out.print("New seed:- ");
				random_map_seed = r.nextLong();
				random_map = new Random(random_map_seed);
				if (DEBUG_TRACE) {System.out.println("New seed accepted.");}
				break;
			case 15: state = "DEBUG"; initInterface(state); break; 			// Jump to debug start screen.
			case 16:														// Open city add building menu.
				gridow = new GridWindow("Add Building", WINDOW_CENTER_X, 100, 5, 2);
				// Set button width.
				// Set button height.
				// Set button gap.
				// Set grid x.
				// Set grid y.
				int lalala = 0;
				int lololo = 0;
				String[] the_keys = {"Population", "Happiness", "Military", "Science", "Nature", "Diplomacy", "Commerce", "Industry"};
				Color[] the_colors = {new Color(200,200,200), new Color(234,242,10), new Color(194,2,50), new Color(10,242,231), new Color(17,153,42), new Color(235,237,175), new Color(83,74,240), new Color(245,155,66)};
				for (int lelele = 0; lelele < the_keys.length; ++lelele) {
					String key = the_keys[lelele];
					Color lliw = the_colors[lelele];
					Button gwb = new Button(0,0,"City_Build_" + key, key, 21, key);
					gwb.setColorInner(the_colors[lelele]);
					System.out.println("Adding " + key + " at (" + lalala + "," + lololo + ").");
					gridow.addGridButton(lololo, lalala, gwb);
					++lalala;
					if (lalala >= 2) {lalala = 0; ++lololo;}
					if (key.equals("Happiness")) {++lololo;}
				}
				addButtons(gridow);
				windows.add(gridow);
				break;
			case 17:														// Activate city build building mode.

				break;
			case 18:														// Activate city move building mode.

				break;
			case 19:														// Activate city remove building mode.

				break;
			case 20:														// Open city rename city window.
				
				break;
			case 21:														// Open city builing category window.

				break;
			case 22:														// Add building info to city building category window.

				break;
			case 23:														// Open city ordinances window.

				break;
			case 24:														// Open city info window.

				break;
			case 25:														// Print buttons.
				// @MIM: Change
				debug("Buttons");
				break;
			case 26:														// Print windows.
				// @MIM: Change
				debug("Windows");
				break;
			case 27:														// Up button in ListWindow.
				// Get the window in question.
				// Decrement page number.
				// Clear the list.
				break;
			case 28:														// Down button in ListWindow.
				// Get the window in question.
				// Increment page number.
				// Clear the list.
				break;
		}    

	}

	// @MIM: Remove
	public boolean checkWindowsFor(String in) {
		// Check to see if window already exists.
		boolean f = false;
		for (InfoWindow item : windows) {
			if (item.getTitle().equals(in)) {f = true;}
		}

		if (DEBUG_TRACE) {System.out.println("WINDOW PRESENCE CHECK: " + in + " = " + f);}
		
		return f;
	}

	// @MIM: Remove
	public boolean checkWindowsFor(String in, int s_start, int s_end) {
		// Check to see if window already exists.
		boolean f = false;
		for (InfoWindow item : windows) {
			if (item.getTitle().substring(s_start, s_end).equals(in)) {f = true;}
		}

		if (DEBUG_TRACE) {System.out.println("WINDOW PRESENCE CHECK: " + in + " = " + f);}
		
		return f;
	}

	// @MIM: Remove
	public void addButtons(InfoWindow window) {
		ArrayList<Button> the_buttons = window.getAllButtons();
		for (int i = 0; i < the_buttons.size(); ++i) {
			buttons.add(the_buttons.get(i));
		}
	}

	// @MIM: Remove
	public void debug(String s) {
		switch (s) {
			case "Buttons":
				System.out.println(buttons.toString());
				break;
			case "Windows":
				System.out.println(windows.toString());
				break;
		}
	}

}