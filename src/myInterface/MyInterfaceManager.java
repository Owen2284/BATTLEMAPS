/*
 *	MyInterfaceManager.java
 *
 *	Handles the management of all windows, buttons and the mouse.
 *
 */

package myInterface;

import myGame.Building;
import myGame.City;
import myGame.Game;
import myGame.Options;
import myGame.Player;
import myGraphics.ImageLibrary;
import myInterface.buttons.Button;
import myInterface.buttons.ImageButton;
import myInterface.windows.GridWindow;
import myInterface.windows.HoverWindow;
import myInterface.windows.InfoWindow;
import myInterface.windows.MenuWindow;
import myMain.Board;
import myMain.ButtonExecutor;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyInterfaceManager {

	private int windowWidth;
	private int windowHeight;

	private ArrayList<Button> buttons = new ArrayList<Button>();
	private ArrayList<InfoWindow> windows = new ArrayList<InfoWindow>();

	private Board b;
	private CommandLine cmd;
	private ImageLibrary il;
	public ButtonExecutor exe;
	private boolean dbg;
	private Options opt;

	private Point mousePos = new Point(0,0);
	private Point prevMousePos;
	private String mouseMode = "Pointer";
	private boolean[] mouseHeld = {false, false, false};
	private Building mouseBuilding;
	private HoverWindow mouseWindow = new HoverWindow(0,0);
	
	private Map<String, Boolean> letters = new HashMap<String, Boolean>();
	String[] ACCEPTEDLETTERS = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
			"m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B",
			"C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
			"S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4", "5", "6", "7",
			"8", "9", " ", "-"};
	private String[] inputString = {""};
	
	// Constructor
	public MyInterfaceManager(Board b) {
		this.b = b;
		windowWidth = b.windowWidth; 
		windowHeight = b.windowHeight;
		il = b.il;
		cmd = b.cmd;
		exe = b.exe;
		dbg = b.DEBUG_MASTER;
		opt = b.opt;
		buildLetters();
	}
	
	// Private construction code.
	private void buildLetters() {
		for (String l : ACCEPTEDLETTERS) {
			letters.put(l, false);
		}
	}

	// Accessors
	public ArrayList<Button> getButtonsOfOwner(String type) {
		ArrayList<Button> returner = new ArrayList<Button>();
		for (Button item : this.buttons) {
			if (item != null && item.getOwner().equals(type)) {
				returner.add(item);
			}
		}
		return returner;
	}

	public ArrayList<Button> getHoveredButtons() {
		ArrayList<Button> returner = new ArrayList<Button>();
		for (Button item : this.buttons) {
			if (item != null && item.isHovering(mousePos) && item.isVisible()) {
				returner.add(item);
			}
		}
		return returner;
	}

	public InfoWindow getWindow(String title) {
		for (InfoWindow item : this.windows) {
			if (item.getTitle().equals(title)) {return item;}
		}
		System.out.println("ERROR: MyInterfaceManager, getWindow(" + title + ") exited without finding the specified window.");
		return null;
	}

	public InfoWindow getWindow(String title, int s_start, int s_end) {
		for (InfoWindow item : this.windows) {
			if (item.getTitle().substring(s_start, s_end).equals(title)) {return item;}
		}
		System.out.println("ERROR: MyInterfaceManager, getWindow(" + title + ", " + s_start + ", " + s_end + ") exited without finding the specified window.");
		return null;
	}

	public Point getMousePos() {return this.mousePos;}

	public String getMouseMode() {return this.mouseMode;}

	public int[] getMouseDiff() {
		int[] diffs = new int[2];
		diffs[0] = this.mousePos.x - this.prevMousePos.x;
		diffs[1] = this.mousePos.y - this.prevMousePos.y;
		return diffs;
	}
	
	public HoverWindow getMouseWindow() {return this.mouseWindow;}

	public Building getMouseBuilding() {return this.mouseBuilding;}

	public boolean checkWindowsFor(String in) {
		// Check to see if window already exists.
		boolean f = false;
		for (InfoWindow item : windows) {
			if (item.getTitle().equals(in)) {f = true;}
		}

		if (Board.DEBUG_TRACE) {System.out.println("WINDOW PRESENCE CHECK: " + in + " = " + f);}
		
		return f;
	}

	public boolean checkWindowsFor(String in, int s_start, int s_end) {
		// Check to see if window already exists.
		boolean f = false;
		for (InfoWindow item : this.windows) {
			if (item.getTitle().substring(s_start, s_end).equals(in)) {f = true;}
		}

		if (Board.DEBUG_TRACE) {System.out.println("WINDOW PRESENCE CHECK: " + in + " = " + f);}
		
		return f;
	}
	
	public ArrayList<Button> getButtonsToProcess(boolean hovered) {
		// Creating ArrayList to return buttons.
		ArrayList<Button> processButtons = new ArrayList<Button>();
		
		// Adding window buttons first.
		for (int wn = windows.size() - 1; wn >= 0; --wn) {
			
			// Get window in priority order.
			InfoWindow currWindow = windows.get(wn);
			
			// Obtain and check all of it's buttons.
			for (Button b : currWindow.getAllButtons()) {
				if (b != null && b.isHovering(mousePos) == hovered) {
					processButtons.add(b);
				}
			}

		}
		
		// Adding basic buttons next.
		for (Button b : this.getButtonsOfOwner("Basic")) {
			if (b != null && b.isHovering(mousePos) == hovered) {
				processButtons.add(b);
			}
		}
		
		// Adding screen buttons next.
		for (Button b : this.getButtonsOfOwner("Screen")) {
			if (b != null && b.isHovering(mousePos) == hovered) {
				processButtons.add(b);
			}
		}
		
		// Adding remaining buttons not already in the ArrayList.
		for (Button b : buttons) {
			if ((b != null) && (!processButtons.contains(b)) && (b.isHovering(mousePos) == hovered)) {
				processButtons.add(b);
			}
		}
		
		// Return the buttons.
		cmd.debug("Prioritised button queue:");
		cmd.debug(processButtons.toString());
		return processButtons;
		
	}

	// Mutators
	public void setMousePos(Point in) {this.prevMousePos = this.mousePos; this.mousePos = in;}
	public void setMouseMode(String in) {this.mouseMode = in; this.mouseBuilding = null;}
	public void setMouseBuilding(Building in) {this.mouseBuilding = in;}
	public void setMouseWindow(HoverWindow in) {this.mouseWindow = in;}
	public void addWindow(InfoWindow window) {
		if (!checkWindowsFor(window.getTitle())) {windows.add(window);}
	}
	
	public void addWindowForce(InfoWindow window) {windows.add(window);}
	
	public void addWindowSwap(InfoWindow window) {
		removeWindowMaxFull(window.getTitle());
		addWindowForce(window);
	}
	
	public void addWindowFull(InfoWindow window) {
		if (!checkWindowsFor(window.getTitle())) {addWindowFullForce(window);}
	}

	public void addWindowFullForce(InfoWindow window) {
		addButtonsFromWindow(window);
		addWindow(window);
	}

	public void addButton(Button button) {buttons.add(button);}

	public void addButtonsFromWindow(InfoWindow window) {
		ArrayList<Button> the_buttons = window.getAllButtons();
		for (int i = 0; i < the_buttons.size(); ++i) {
			buttons.add(the_buttons.get(i));
		}
	}
	
	public void removeWindowFull(InfoWindow inWindow) {
		inWindow.close();
		for (Button item : inWindow.getAllButtons()) {
			buttons.remove(item);
		}
		windows.remove(inWindow);
	}

	public void removeWindowFull(String in_title) {
		InfoWindow window_to_remove = new InfoWindow("1995", 15, 10);
		for (InfoWindow window : this.windows) {
			if (window.getTitle().equals(in_title)) {
				window_to_remove = window;
			}
		}
		window_to_remove.close();
		for (Button item : window_to_remove.getAllButtons()) {
			buttons.remove(item);
		}
		windows.remove(window_to_remove);
		if (Board.DEBUG_ERROR && window_to_remove.getTitle().equals("1995")) {
			cmd.error("ERROR: MyInterfaceManager, removeWindowFull(" + in_title + ") exited without finding the specified window.");
		}
	}
	
	public void removeWindowFull(String in_title, int start, int end) {
		InfoWindow window_to_remove = new InfoWindow("1995", 15, 10);
		for (InfoWindow window : this.windows) {
			try {
				if (window.getTitle().substring(start, end).equals(in_title)) {window_to_remove = window;}
			} catch (StringIndexOutOfBoundsException e) {
				// Continue running.
			}
		}
		window_to_remove.close();
		for (Button item : window_to_remove.getAllButtons()) {
			buttons.remove(item);
		}
		windows.remove(window_to_remove);
		if (Board.DEBUG_ERROR && window_to_remove.getTitle().equals("1995")) {
			cmd.error("ERROR: MyInterfaceManager, removeWindowFull(" + in_title + ") exited without finding the specified window.");
		}
	}
	
	public void removeWindowMaxFull(String titleIn) {
		while (checkWindowsFor(titleIn)) {
			removeWindowFull(titleIn);
		}
	}
	
	public void removeWindowMaxFull(String titleIn, int start, int end) {
		while (checkWindowsFor(titleIn, start, end)) {
			removeWindowFull(titleIn, start, end);
		}
	}

	public void initInterface(String in, Game game) {

		// Clears lists.
		this.buttons.clear();
		this.windows.clear();
		
		// Reset mouse.
		this.setMouseMode("Pointer");
		
		// Removes hover window.
		this.mouseWindow = new HoverWindow(-200, -200);

		if (Board.DEBUG_LAUNCH) {System.out.println("Initialising interface for " + in + ".");}

		if (in.equals("Map")) {
			
			// City buttons.
			for (City c : game.getCities()) {
				int cityImage = 11;
				if (c.getOwner() != null) {
					ArrayList<Player> allPlayers = game.getPlayers();
					for (int j = 0; j < allPlayers.size(); ++j) {
						if (allPlayers.get(j).equals(c.getOwner())) {
							cityImage += j + 1;
						}
					}
				}
				ImageButton imgb = new ImageButton(c.getX() + game.getMap().getScrollX(), c.getY() + game.getMap().getScrollY(), "City_Button_" + c.getName().replace(" ", "_"), "", 2, c.getName(), il.getImage(cityImage));
				imgb.setOwner("Screen");
				imgb.setWidth(31);
				imgb.setHeight(31);
				imgb.setHoverText(c.getName());
				buttons.add(imgb);
			}

			// End turn button.
			Button et = new Button(64, windowHeight - 36);
			et.setID("End Turn");
			et.setExecutionNumber(4);
			et.setButtonText("End Turn");
			et.setHoverText("TEST");
			buttons.add(et);
			
			// Actions window button.
			Button ac = new Button((windowWidth / 4), windowHeight - 36, "Actions", "View Actions", 35);
			buttons.add(ac);

			// View players button.
			Button vp = new Button((windowWidth / 2) - 64, windowHeight - 36);
			vp.setID("View Players");
			vp.setExecutionNumber(5);
			vp.setButtonText("View Players");
			buttons.add(vp);

			// View stats button.
			Button vs = new Button(windowWidth - 192, windowHeight - 36);
			vs.setID("View Stats");
			vs.setExecutionNumber(6);
			vs.setButtonText("View Stats");
			buttons.add(vs);
			
			addDebug();

		} else if (in.substring(0,4).equals("City")) {

			// Get the city.
			String theName = in.substring(5);
			City theCity = game.getCityByName(theName);

			// Back to map button.
			buttons.add(new Button(windowWidth - 192, windowHeight - 36, "CityBackToMap", "Back to Map", 1));

			// City editing buttons.
			if (game.getActivePlayer().equals(theCity.getOwner())) {
				buttons.add(new Button(770, 20, windowWidth - 790, 40, "City_Build", "Add Building", 16));
				buttons.add(new Button(770, 90, windowWidth - 790, 40, "City_Move", "Move Building", 18));
				buttons.add(new Button(770, 160, windowWidth - 790, 40, "City_Remove", "Remove Building", 19));
				buttons.add(new Button(770, 290, windowWidth - 790, 40, "City_Ordinances", "City Ordinances", 23, theName));
				buttons.add(new Button(770, 400, windowWidth - 790, 40, "City_Rename", "Rename City", 20, theName));
			}
			buttons.add(new Button(770, 470, windowWidth - 790, 40, "City_Info", "View City Info", 24));
			
			addDebug();

		} else if (in.substring(0,4).equals("Menu")) {
			
			String submenu = in.substring(5);
			int textHeight = MyTextMetrics.getTextSizeFlat("Test")[1];
			
			MenuWindow winMenu = new MenuWindow("Main Menu", (windowWidth - 400) / 2, 0, 10);
			winMenu.setHeight(windowHeight);
			winMenu.setGridX(10);
			winMenu.setGridY(10);
			winMenu.setButtonGap(15);
			winMenu.setButtonWidth(winMenu.getWidth() - 20);
			winMenu.removeCloseButton();
			
			if (submenu.equals("Main")) {
				
				String[] titles = {"Single Player", "Local Multiplayer", "Online Play", "-", "Editor", "-", "-", "Options", "-", "Quit"};
				int[] exes = {3, 3, 3, -1, 3, -1, -1, 3, -1, 49};
				String[] adds = {"Single Main", "Multi Main", "Online Main", "", "Editor Main", "", "", "Options Main", "", "Quit"};
				boolean[] vises = {true, true, true, false, true, false, false, true, false, true};
				Color[] colors = {Color.GREEN, Color.RED, Color.BLUE, Color.WHITE, Color.ORANGE, Color.WHITE, Color.WHITE, Color.PINK, Color.WHITE, Color.GRAY};
				winMenu.addMenuButtons(
						titles,
						exes,
						adds,
						vises,
						colors
				);
				
			} else if (submenu.equals("Single Main")) {
				
				winMenu.setTitle("Single Player");
				
				String[] titles = {"Campaign","Missions","","Skirmish","Domination","","Tutorial","","","Back"};
				int[] exes = {3,3,-1,3,3,-1,3,-1,-1,3};
				String[] adds = {"Single Campaign","Single Mission","","Single Skirmish","Single Domination","","Single Tutorial","","","Main"};
				boolean[] vises = {true,true,false,true,true,false,true,false,false,true};
				Color[] colors = {Color.GREEN,Color.GREEN,Color.WHITE,Color.GREEN,Color.GREEN,Color.WHITE,Color.GREEN,Color.WHITE,Color.WHITE,Color.WHITE};
				winMenu.addMenuButtons(
						titles,
						exes,
						adds,
						vises,
						colors
				);
	
			} else if (submenu.equals("Single Skirmish")) {
				
				winMenu.setTitle("Single Player - Skirmish");
				
				String[] titles = {"Create New Skirmish","Play Random Skirmish","Play Skirmish Template","","Load Game","","","","","Back"};
				int[] exes = {3,59,3,-1,3,-1,-1,-1,-1,3};
				String[] adds = {"Single Skirmish Create","","Single Skirmish Templates","","Single Skirmish Load","","","","","Single Main"};
				boolean[] vises = {true,true,true,false,true,false,false,false,false,true};
				Color[] colors = {Color.GREEN,Color.GREEN,Color.GREEN,Color.GREEN,Color.GREEN,Color.WHITE,Color.GREEN,Color.WHITE,Color.WHITE,Color.WHITE};
				winMenu.addMenuButtons(
						titles,
						exes,
						adds,
						vises,
						colors
				);
				
			} else if (submenu.equals("Single Skirmish Create")) {
			
				winMenu = null;
				InfoWindow newWindow = new InfoWindow("Create Skirmish", 50, 100);
				newWindow.setHeight(windowHeight);
				newWindow.setWidth(windowWidth - 100);
				newWindow.setLineSpacing(3);
				String content = "";
				content += "Game Name:\n\n";
				content += "Number of Players:\n\n";
				newWindow.addWindowButton(new Button(240, MyTextMetrics.getTextSizeComplex(content, 3)[1], 160, 
						textHeight + 1, "Single_Player_Count", "UNIMPLEMENTED", 54, "fullscreen"));
				content += "Map Width:\n";
				content += "Map Height:\n";
				content += "Land Type:\n\n";
				content += "Number of Cities:\n";
				content += "Spacing between Cities:\n\n";
				content += "Victory Condition:\n";
				content += "Max Turns:\n";
				content += "\n";
				newWindow.setContent(content);
				newWindow.addWindowButton(new Button((newWindow.getWidth() - 138), newWindow.getHeight() - 74, "Single_Skirmish_Back", "Back", 3, "Single Main"));
				newWindow.setButtonsColorInner(Color.GREEN);
				newWindow.removeCloseButton();
				addWindowFullForce(newWindow);
				
			} else if (submenu.equals("Multi Main")) {
				
				winMenu.setTitle("Local Multi Player");
				
				String[] titles = {"Campaign","Missions","","Skirmish","Domination","-","-","-","-","Back"};
				int[] exes = {3,3,-1,3,3,-1,-1,-1,-1,3};
				String[] adds = {"Multi Campaign","Multi Mission","","Multi Skirmish","Multi Domination","","","","","Main"};
				boolean[] vises = {true,true,false,true,true,false,false,false,false,true};
				Color[] colors = {Color.RED,Color.RED,Color.WHITE,Color.RED,Color.RED,Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE};
				winMenu.addMenuButtons(
						titles,
						exes,
						adds,
						vises,
						colors
				);
				
			} else if (submenu.equals("Online Main")) {
				
				winMenu.setTitle("Online");
				
				String[] titles = {"Host Game","Join Game","-","-","-","-","-","-","-","Back"};
				int[] exes = {3,3,-1,-1,-1,-1,-1,-1,-1,3};
				String[] adds = {"Online Host","Online Join","","","","","","","","Main"};
				boolean[] vises = {true,true,false,false,false,false,false,false,false,true};
				Color[] colors = {Color.BLUE,Color.BLUE,Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE};
				winMenu.addMenuButtons(
						titles,
						exes,
						adds,
						vises,
						colors
				);
				
			} else if (submenu.equals("Editor Main")) {
				
				winMenu.setTitle("Editor");
				
				String[] titles = {"Map","","World","","Mission","","General","","","Back"};
				int[] exes = {3,-1,3,-1,3,-1,3,-1,-1,3};
				String[] adds = {"Editor Map","","Editor World","","Editor Mission","","Editor General","","","Main"};
				boolean[] vises = {true,false,true,false,true,false,true,false,false,true};
				Color[] colors = {Color.ORANGE,Color.WHITE,Color.ORANGE,Color.WHITE,Color.ORANGE,Color.WHITE,Color.ORANGE,Color.WHITE,Color.WHITE,Color.WHITE};
				winMenu.addMenuButtons(
						titles,
						exes,
						adds,
						vises,
						colors
				);
				
			} else if (submenu.equals("Options Main")) {
				
				winMenu.setTitle("Options");
				
				String[] titles = {"Video","Audio","Gameplay","Online","Debug","-","-","Apply","Confirm","Cancel"};
				int[] exes = {3,3,3,3,10,-1,-1,51,52,53};
				String[] adds = {"Options Video","Options Audio","Options Gameplay","Options Online","","","","Options Main","Main","Main"};
				boolean[] vises = {true,true,true,true,dbg,false,false,true,true,true};
				Color[] colors = {Color.PINK,Color.PINK,Color.PINK,Color.PINK,Color.DARK_GRAY,Color.WHITE,Color.WHITE,Color.PINK,Color.PINK,Color.PINK};
				winMenu.addMenuButtons(
						titles,
						exes,
						adds,
						vises,
						colors
				);
				
			} else if (submenu.equals("Options Video")) {
				
				winMenu = null;
				InfoWindow newWindow = new InfoWindow("Video Options", 50, 100);
				newWindow.setHeight(windowHeight);
				newWindow.setWidth(windowWidth - 100);
				newWindow.setLineSpacing(3);
				String content = "";
				content += "Fullscreen:\n";
				newWindow.addWindowButton(new Button(240, MyTextMetrics.getTextSizeComplex(content,3)[1], 160, 
						textHeight + 1, "Option_Fullscreen", Boolean.toString(opt.getStatusFullscreen()), 
						54, "fullscreen"));
				content += "Resolution:\n\n";
				newWindow.addWindowButton(new Button(240, MyTextMetrics.getTextSizeComplex(content,3)[1], 160, 
						textHeight + 1, "Option_Resolution", 
						"-", 0, ""));
				content += "Show FPS:\n";
				newWindow.addWindowButton(new Button(240, MyTextMetrics.getTextSizeComplex(content,3)[1], 160, 
						textHeight + 1, "Option_FPS", 
						Boolean.toString(opt.getStatusFPS()), 54, "fpscounter"));
				content += "Show Time to Act/Draw:\n\n";
				newWindow.addWindowButton(new Button(240, MyTextMetrics.getTextSizeComplex(content,3)[1], 160, 
						textHeight + 1, "Option_TTAD", 
						Boolean.toString(opt.getStatusTTAD()), 54, "timetoactdraw"));
				content += "Gui Scale:\n\n";
				newWindow.addWindowButton(new Button(240, MyTextMetrics.getTextSizeComplex(content,3)[1], 160, 
						textHeight + 1, "Option_Scale", 
						Integer.toString(opt.getValue("guiscale")), 55, "guiscale|1|1|4|true"));
				content += "Primary Monitor:\n\n";
				newWindow.addWindowButton(new Button(240, MyTextMetrics.getTextSizeComplex(content,3)[1], 160, 
						textHeight + 1, "Option_Monitor", 
						Integer.toString(opt.getValue("monitor")), 55, 
						"monitor|1|0|" + Integer.toString(Board.SCREENCOUNT - 1) + "|true"));
				content += "\n";
				newWindow.setContent(content);
				newWindow.addWindowButton(new Button(160, newWindow.getHeight() - 74, "Options_Goto_Audio", "Audio", 3, "Options Audio"));
				newWindow.addWindowButton(new Button(310, newWindow.getHeight() - 74, "Options_Goto_Gameplay", "Gameplay", 3, "Options Gameplay"));
				newWindow.addWindowButton(new Button(460, newWindow.getHeight() - 74, "Options_Goto_Online", "Online", 3, "Options Online"));
				newWindow.addWindowButton(new Button((newWindow.getWidth() - 138), newWindow.getHeight() - 74, "Options_Goto_Back", "Back", 3, "Options Main"));
				newWindow.setButtonsColorInner(Color.PINK);
				newWindow.removeCloseButton();
				addWindowFullForce(newWindow);
							
			} else if (submenu.equals("Options Audio")) {
				
				winMenu = null;
				InfoWindow newWindow = new InfoWindow("Audio Options", 50, 100);
				newWindow.setHeight(windowHeight);
				newWindow.setWidth(windowWidth - 100);
				newWindow.setLineSpacing(3);
				String content = "";
				content += "Master Volume:\t" + opt.getValueVolumeMaster() + "\n";
				newWindow.addWindowButton(new Button(300, MyTextMetrics.getTextSizeComplex(content, 3)[1], 40, 
						textHeight + 1, "Option_Master_Down", "-", 57, "volumemaster|-10|0|100|false"));
				newWindow.addWindowButton(new Button(350, MyTextMetrics.getTextSizeComplex(content, 3)[1], 40, 
						textHeight + 1, "Option_Master_Up", "+", 57, "volumemaster|10|0|100|false"));
				content += "Music Volume:\t" + opt.getValueVolumeMusic() + "\n";
				newWindow.addWindowButton(new Button(300, MyTextMetrics.getTextSizeComplex(content, 3)[1], 40, 
						textHeight + 1, "Option_Music_Down", "-", 57, "volumemusic|-10|0|100|false"));
				newWindow.addWindowButton(new Button(350, MyTextMetrics.getTextSizeComplex(content, 3)[1], 40, 
						textHeight + 1, "Option_Music_Up", "+", 57, "volumemusic|10|0|100|false"));
				content += "SFX Volume:\t" + opt.getValueVolumeSound() + "\n";
				newWindow.addWindowButton(new Button(300, MyTextMetrics.getTextSizeComplex(content, 3)[1], 40, 
						textHeight + 1, "Option_Sound_Down", "-", 57, "volumesound|-10|0|100|false"));
				newWindow.addWindowButton(new Button(350, MyTextMetrics.getTextSizeComplex(content, 3)[1], 40, 
						textHeight + 1, "Option_Sound_Up", "+", 57, "volumesound|10|0|100|false"));
				content += "\n";
				newWindow.setContent(content);
				newWindow.addWindowButton(new Button(10, newWindow.getHeight() - 74, "Options_Goto_Video", "Video", 3, "Options Video"));
				newWindow.addWindowButton(new Button(310, newWindow.getHeight() - 74, "Options_Goto_Gameplay", "Gameplay", 3, "Options Gameplay"));
				newWindow.addWindowButton(new Button(460, newWindow.getHeight() - 74, "Options_Goto_Online", "Online", 3, "Options Online"));
				newWindow.addWindowButton(new Button((newWindow.getWidth() - 138), newWindow.getHeight() - 74, "Options_Goto_Back", "Back", 3, "Options Main"));
				newWindow.setButtonsColorInner(Color.PINK);
				newWindow.removeCloseButton();
				addWindowFullForce(newWindow);
				
			} else if (submenu.equals("Options Gameplay")) {
				
				winMenu = null;
				InfoWindow newWindow = new InfoWindow("Gameplay Options", 50, 100);
				newWindow.setHeight(windowHeight);
				newWindow.setWidth(windowWidth - 100);
				newWindow.setLineSpacing(3);
				String content = "";
				content += "Notifications:\n";
				newWindow.addWindowButton(new Button(240, MyTextMetrics.getTextSizeComplex(content, 3)[1], 160, 
						textHeight + 1, "Option_Notifications", Boolean.toString(opt.getStatus("notifications")), 
						54, "notifications"));
				content += "\n";
				newWindow.setContent(content);
				newWindow.addWindowButton(new Button(10, newWindow.getHeight() - 74, "Options_Goto_Video", "Video", 3, "Options Video"));
				newWindow.addWindowButton(new Button(160, newWindow.getHeight() - 74, "Options_Goto_Audio", "Audio", 3, "Options Audio"));
				newWindow.addWindowButton(new Button(460, newWindow.getHeight() - 74, "Options_Goto_Online", "Online", 3, "Options Online"));
				newWindow.addWindowButton(new Button((newWindow.getWidth() - 138), newWindow.getHeight() - 74, "Options_Goto_Back", "Back", 3, "Options Main"));
				newWindow.setButtonsColorInner(Color.PINK);
				newWindow.removeCloseButton();
				addWindowFullForce(newWindow);
				
			} else if (submenu.equals("Options Online")) {
				
				winMenu = null;
				InfoWindow newWindow = new InfoWindow("Online Options", 50, 100);
				newWindow.setHeight(windowHeight);
				newWindow.setWidth(windowWidth - 100);
				newWindow.setLineSpacing(3);
				String content = "";
				content += "\n";
				newWindow.setContent(content);
				newWindow.addWindowButton(new Button(10, newWindow.getHeight() - 74, "Options_Goto_Video", "Video", 3, "Options Video"));
				newWindow.addWindowButton(new Button(160, newWindow.getHeight() - 74, "Options_Goto_Audio", "Audio", 3, "Options Audio"));
				newWindow.addWindowButton(new Button(310, newWindow.getHeight() - 74, "Options_Goto_Gameplay", "Gameplay", 3, "Options Gameplay"));
				newWindow.addWindowButton(new Button((newWindow.getWidth() - 138), newWindow.getHeight() - 74, "Options_Goto_Back", "Back", 3, "Options Main"));
				newWindow.setButtonsColorInner(Color.PINK);
				newWindow.removeCloseButton();
				addWindowFullForce(newWindow);
				
			} else {
				
				// Error menu.
				
				winMenu.setTitle("Un-implemented Menu");
				
				String[] titles = {"-","-","-","-","-","-","-","-","-","Back"};
				int[] exes = {-1,-1,-1,-1,-1,-1,-1,-1,-1,3};
				String[] adds = {"","","","","","","","","","Main"};
				boolean[] vises = {false,false,false,false,false,false,false,false,false,true};
				Color[] colors = {Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE};
				winMenu.addMenuButtons(
						titles,
						exes,
						adds,
						vises,
						colors
				);
				
			}
			
			if (winMenu != null) {this.addWindowFullForce(winMenu);}
			

		} else if (in.equals("DEBUG")) {

			GridWindow dw = new GridWindow("DEBUG_LAUNCH", (windowWidth - 400) / 2, 100, 4, 4);
			dw.setGridX(10);
			dw.setGridY(10);
			dw.removeCloseButton();

			Button dwb = new Button(0, 0, "DEBUG_MENU_Normal", "Normal Map", 12, "Normal");
			dwb.setColorInner(Color.RED);
			dw.addGridButton(0, 0, dwb);
			dwb = new Button(0, 0, "DEBUG_MENU_Large", "Large Map", 12, "Large");
			dwb.setColorInner(Color.RED);
			dw.addGridButton(0, 1, dwb);
			dwb = new Button(0, 0, "DEBUG_MENU_Full", "Full Map", 12, "Full");
			dwb.setColorInner(Color.RED);
			dw.addGridButton(0, 2, dwb);
			dwb = new Button(0, 0, "DEBUG_MENU_Packed", "Packed Map", 12, "Packed");
			dwb.setColorInner(Color.RED);
			dw.addGridButton(0, 3, dwb);
			dwb = new Button(0, 0, "DEBUG_MENU_Seed", "Enter Seed", 39);
			dwb.setColorInner(Color.GREEN);
			dw.addGridButton(1, 3, dwb);
			dwb = new Button(0, 0, "DEBUG_MENU_Images", "Image Tester", 45);
			dwb.setColorInner(Color.ORANGE);
			dw.addGridButton(3, 0, dwb);
			dwb = new Button(0, 0, "DEBUG_MENU_Menu", "Main Menu", 3, "Main");
			dwb.setColorInner(Color.BLUE);
			dw.addGridButton(3, 3, dwb);

			this.addWindowFullForce(dw);
			
		}
		else if (in.equals("ImageTester")) {
			
			// Image changing buttons.
			buttons.add(new Button(10, windowHeight / 2 - 20, 40, 40, "Image_Test_Left", "<", 46));
			buttons.add(new Button(windowWidth - 50, windowHeight / 2 - 20, 40, 40, "Image_Test_Right", ">", 47));
			
			// Back button.
			buttons.add(new Button(windowWidth / 2 - 40, windowHeight - 40, 80, 30, "Image_Test_Back", "Back", 15));
			
		}

	}
	
	public void addDebug() {
		// Debug action window button.
		Button db = new Button(3*(windowWidth / 4) - 128, windowHeight - 36);
		db.setID("DEBUG_ACTION_WINDOW");
		db.setColorInner(Color.GREEN);
		db.setExecutionNumber(10);
		db.setButtonText("DEBUG_ACTION");
		buttons.add(db);
	}
	
	public void setLetter(String ch, boolean bo) {
		boolean found = false;
		// Checks for text input.
		for (String l : ACCEPTEDLETTERS) {
			if (l.equals(ch)) {
				found = true;
			}
		}
		// TODO: Enter check.
		// TODO: Backspace check.
		if (found) {
			this.letters.put(ch, bo);
		}
	}

	// Graphical methods
	public void drawAll(Graphics g, Board b) {
		this.drawButtons(g, "Basic", true);
		this.drawWindows(g, b);
		this.drawMouse(g, b);
		this.drawMouseWindow(g, b);
	}

	public void drawButtons(Graphics g, String type, boolean draw_shadows) {

		// Gets the buttons of the specified type.
		ArrayList<Button> the_buttons = this.getButtonsOfOwner(type);

		// Places the buttons.
		for (Button item : the_buttons) {
			if (item.isVisible()) {
				if (draw_shadows) {
					item.drawAll(g, mousePos);
				} else {
					item.drawBasic(g, mousePos);
				}
			}
		}

	}

	public void drawButtons(Graphics g, ArrayList<Button> in_buttons, boolean draw_shadows) {

		// Places the buttons.
		for (Button item : in_buttons) {
			if (item != null && item.isVisible()) {
				if (draw_shadows) {
					item.drawAll(g, mousePos);
				} else {
					item.drawBasic(g, mousePos);
				}
			}
		}

	}

	public void drawWindows(Graphics g, Board b) {

		// Draws mouse hover window.
		if (mouseWindow.isOpen()) {mouseWindow.draw(g, b, il);}
		
		// Loops through all windows.
		for (InfoWindow window : windows) {

			if (!window.getAnimationStatus().equals("Closed")) {
				// Draws the window.
				window.draw(g, b, il);
	
				// Draws the window's buttons if the window is open.
				if (window.getAnimationStatus().equals("Open")) {
					this.drawButtons(g, window.getAllButtons(), false);
				}
			}

		}

	}

	public void drawMouse(Graphics g, Board the_board) {

		// Determines the image to use for the mouse.
		int mouse_img = 1;
		switch(mouseMode) {
			case "Build": mouse_img=2; break;
			case "Move": mouse_img=3; break;
			case "Destroy": mouse_img=4; break;
		}

		// Draws the mouse
		g.drawImage(il.getImage(mouse_img), (int) mousePos.getX(), (int) mousePos.getY(), the_board);

	}
	
	public void drawMouseWindow(Graphics g, Board b) {
		this.mouseWindow.draw(g, b, il);
	}

	// Updaters
	public void act() {
		
		// Processes key strokes into inputString.
		for (String l : ACCEPTEDLETTERS) {
			if (letters.get(l)) {
				inputString[0] += l;
				letters.put(l, false);
				System.out.println(inputString[0]);
			}
		}
		
	}
	
	public void updateWindows() {
		
		// Storage for windows to remove.
		ArrayList<InfoWindow> windowsToGo = new ArrayList<InfoWindow>();
		InfoWindow windowToTop = null;
		boolean aWindowIsMoving = false;
		
		// Check to see if any window is currently moving.
		for (InfoWindow w : windows) {
			if (w.isMoving()) {aWindowIsMoving = true;} 
		}
		
		for (int wn = windows.size() - 1; wn >= 0; --wn) {
			
			InfoWindow window = windows.get(wn);
			
			// Check if the window is open or not.
			if (window != null && window.isOpen()) {
			
				// Window prioritisation code.
				if (mouseHeld[0] && window.isOverWindow(mousePos) && (windowToTop == null)) {
					windowToTop = window;
				}
				// Window movement and boundary updating.
				if (window.isOverTopBar(mousePos) && mouseHeld[0] && !aWindowIsMoving) {
					window.setMoving(true);
					aWindowIsMoving = true;
					window.setX(window.getX() + getMouseDiff()[0]);
					window.setY(window.getY() + getMouseDiff()[1]);
				}
				if (window.isMoving()) {
					window.setX(window.getX() + getMouseDiff()[0]);
					window.setY(window.getY() + getMouseDiff()[1]);
				}
				// WIndow movement stopping.
				if (!mouseHeld[0]) {
					window.setMoving(false);
				}
				if (window.getX() < 0) {window.setX(0);}
				if (window.getX() > this.windowWidth - window.getWidth()) {window.setX(this.windowWidth - window.getWidth());}
				if (window.getY() < 0) {window.setY(0);}
				if (window.getY() > this.windowHeight - window.getHeight()) {window.setY(this.windowHeight - window.getHeight());} 
				
			}
			else {
				
				// Check window to see if it needs to be removed.
				if (window == null) {windowsToGo.add(window);}
				else if (window.getAnimationStatus().equals("Closed")) {windowsToGo.add(window);}
				
			}
			
			// Internal window updating.
			window.update(mousePos); 			
			
		}
		
		// Window removal.
		if (windowsToGo.size() > 0) {
			for (InfoWindow window : windowsToGo) {
				this.removeWindowFull(window);
			}
		}
		
		// Window prioritisation.
		if (windowToTop != null && windows.size() > 1) {
			windows.remove(windowToTop);
			windows.add(windowToTop);
		}

		// Check for mouse hover windows.
		boolean stillHovered = false;		
		for (Button cb : this.buttons) {

			// Checks if city is being hovered over.
			if (cb != null && cb.hasHoverText() && cb.isHovering(mousePos)) {
				stillHovered = true;
				this.getMouseWindow().setContent(cb.getHoverText());
			}

		}
		
		// Updates the hover window.
		this.updateHoverWindow(stillHovered);
				
	}
	
	public void updateHoverWindow(boolean stillHovered) {
		
		if (!stillHovered) {
			this.mouseWindow.setActive(false);
			this.mouseWindow.setOpen(false);
		} else {
			// Sorts out the window if there is a hover.
			if (this.mouseWindow.isOpen()) {
				if ((this.mouseWindow.getX() + this.mouseWindow.getWidth()) > windowWidth) {
					this.mouseWindow.setX(windowWidth - this.mouseWindow.getWidth());
				}
				if ((this.mouseWindow.getY() + this.mouseWindow.getHeight()) > windowHeight - 40) {
					this.mouseWindow.setY(windowHeight - this.mouseWindow.getHeight() - 40);
				}                
			} else {
				this.mouseWindow = new HoverWindow(this.mousePos.x, this.mousePos.y);
				this.mouseWindow.setActive(true);
				this.mouseWindow.update(mousePos);
				if ((this.mouseWindow.getX() + this.mouseWindow.getWidth()) > windowWidth) {
					this.mouseWindow.setX(windowWidth - this.mouseWindow.getWidth());
				}
				if ((this.mouseWindow.getY() + this.mouseWindow.getHeight()) > windowHeight - 40) {
					this.mouseWindow.setY(windowHeight - this.mouseWindow.getHeight() - 40);
				}
			}
		}
		
		this.mouseWindow.update(this.mousePos);
		
	}
	
	public void shiftButtons(String type, int xDif, int yDif) {
		
		ArrayList<Button> buttonz = this.getButtonsOfOwner(type);
		
		for (Button but : buttonz) {
			but.incX(xDif);
			but.incY(yDif);
		}
		
	}
	
	public void shiftButtonsBounded(String type, int xVar, int yVar) {
		
		ArrayList<Button> buttonz = this.getButtonsOfOwner(type);
		
		for (Button but : buttonz) {
			but.setX(but.getOwnerX() + xVar);
			but.setY(but.getOwnerY() + yVar);
		}
		
	}

	// MouseListener methods.
	public boolean mouseClicked(MouseEvent e) {
		
		// Allows for only 1 button press.
		boolean clickThisFrame = false;

		// Get prioritised list of hovered buttons.
		ArrayList<Button> priorityButtons = getButtonsToProcess(true);
		
		// Running checks on buttons in order.
		for (Button button : priorityButtons) {
			if (!clickThisFrame && (e.getButton() == MouseEvent.BUTTON1) ) {
				clickThisFrame = true;
				exe.execute(button);
			}
		}
		
		return clickThisFrame;
		
	}
	
	public void mousePressed(MouseEvent e) {
		boolean[] newArr = {e.getButton() == MouseEvent.BUTTON1, e.getButton() == MouseEvent.BUTTON2, e.getButton() == MouseEvent.BUTTON3};
		this.mouseHeld = newArr;
	}

	public void mouseReleased(@SuppressWarnings("unused") MouseEvent e) {
		boolean[] newArr = {false, false, false};
		this.mouseHeld = newArr;
	}
	
	public boolean isPressed(int i) {
		if (i >= 1 && i <= 3) {
			return this.mouseHeld[i-1];
		} else {
			if (Board.DEBUG_ERROR) {System.out.println("ERROR: MyInterfaceManager, isPressed(" + i + ") has invalid parameter.");}
			return false;
		}
	}

	public String[] getInputString() {
		return inputString;
	}

	// Debug methods
	public void debug(String s) {
		switch (s) {
			case "Buttons":
				cmd.debug(buttons.toString());
				break;
			case "Windows":
				cmd.debug(windows.toString());
				break;
			case "Mouse":
				cmd.debug(mousePos + "\n" + prevMousePos + "\n" + mouseMode + "\n" + mouseBuilding);
				break;
			case "Keys":
				String all = "";
				for (String l : ACCEPTEDLETTERS) {
					all += l;
				}
				cmd.debug(all);
				break;
		}
	}

}