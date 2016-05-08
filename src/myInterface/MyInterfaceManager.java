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
import myGraphics.ImageLibrary;
import myMain.Board;

import java.awt.Color;
import java.awt.Point;
import java.awt.Graphics;
import java.util.ArrayList;

public class MyInterfaceManager {

	private int windowWidth;
	private int windowHeight;

	private ArrayList<Button> buttons = new ArrayList<Button>();
	private ArrayList<InfoWindow> windows = new ArrayList<InfoWindow>();

	private ImageLibrary il;

	private Point mousePos = new Point(0,0);
	private Point prevMousePos;
	private String mouseMode = "Pointer";
	private boolean mouseHeld = false;
	private Building mouseBuilding;
	private HoverWindow mouseWindow = new HoverWindow(0,0);

	// Constructor
	public MyInterfaceManager(int wid, int hei, ImageLibrary imgs) {
		windowWidth = wid; 
		windowHeight = hei;
		il = imgs;
	}

	// Accessors
	public ArrayList<Button> getButtonsOfOwner(String type) {
		ArrayList<Button> returner = new ArrayList<Button>();
		for (Button item : this.buttons) {
			if (item.getOwner().equals(type)) {
				returner.add(item);
			}
		}
		return returner;
	}

	public ArrayList<Button> getHoveredButtons() {
		ArrayList<Button> returner = new ArrayList<Button>();
		for (Button item : this.buttons) {
			if (item.isHovering(mousePos) && item.isVisible()) {
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
			System.out.println("ERROR: MyInterfaceManager, removeWindowFull(" + in_title + ") exited without finding the specified window.");
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
			System.out.println("ERROR: MyInterfaceManager, removeWindowFull(" + in_title + ") exited without finding the specified window.");
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

	public void setInterface(String in, Game game) {

		// Clears lists.
		this.buttons.clear();
		this.windows.clear();
		
		// Reset mouse.
		this.setMouseMode("Pointer");
		
		// Removes hover window.
		this.mouseWindow = new HoverWindow(-200, -200);

		if (Board.DEBUG_LAUNCH) {System.out.println("Initialising interface for " + in + ".");}

		if (Board.DEBUG_WINDOW && !in.equals("DEBUG")) {
			// Debug info window button.
			Button db = new Button((windowWidth / 4), windowHeight - 36);
			db.setID("DEBUG_INFO_WINDOW");
			db.setColorInner(Color.GREEN);
			db.setExecutionNumber(9);
			db.setButtonText("DEBUG_INFO");
			buttons.add(db);

			// Debug action window button.
			db = new Button(3*(windowWidth / 4) - 128, windowHeight - 36);
			db.setID("DEBUG_ACTION_WINDOW");
			db.setColorInner(Color.GREEN);
			db.setExecutionNumber(10);
			db.setButtonText("DEBUG_ACTION");
			buttons.add(db);

			// OWEN: Maybe add in debug log?

		}

		if (in.equals("Map")) {

			// End turn button.
			Button et = new Button(64, windowHeight - 36);
			et.setID("End Turn");
			et.setExecutionNumber(4);
			et.setButtonText("End Turn");
			buttons.add(et);

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

		} else if (in.substring(0,4).equals("City")) {

			// Get the city.
			City the_city = game.getCityByName(in.substring(5));

			// Back to map button.
			buttons.add(new Button(windowWidth - 192, windowHeight - 36, "CityBackToMap", "Back to Map", 1));

			// City editing buttons.
			if (game.getActivePlayer().getID().equals(the_city.getOwner())) {
				buttons.add(new Button(770, 20, windowWidth - 790, 40, "City_Build", "Add Building", 16));
				buttons.add(new Button(770, 90, windowWidth - 790, 40, "City_Move", "Move Building", 18));
				buttons.add(new Button(770, 160, windowWidth - 790, 40, "City_Remove", "Remove Building", 19));
				buttons.add(new Button(770, 290, windowWidth - 790, 40, "City_Ordinances", "City Ordinances", 23));
				buttons.add(new Button(770, 400, windowWidth - 790, 40, "City_Rename", "Rename City", 20));
			}
			buttons.add(new Button(770, 470, windowWidth - 790, 40, "City_Info", "View City Info", 24));

		} else if (in.substring(0,4).equals("Menu")) {

		} else if (in.equals("DEBUG")) {

			GridWindow dw = new GridWindow("DEBUG_LAUNCH", (windowWidth - 400) / 2, 100, 4, 4);
			dw.setGridX(10);
			dw.setGridY(10);

			Button dwb = new Button(0, 0, "DEBUG_MENU_Normal", "Normal Map", 12, "Normal");
			dwb.setColorInner(Color.RED);
			dw.addGridButton(0, 0, dwb); buttons.add(dwb);
			dwb = new Button(0, 0, "DEBUG_MENU_Small", "Small Map", 12, "Small");
			dwb.setColorInner(Color.RED);
			dw.addGridButton(1, 0, dwb); buttons.add(dwb);
			dwb = new Button(0, 0, "DEBUG_MENU_Large", "Large Map", 12, "Large");
			dwb.setColorInner(Color.RED);
			dw.addGridButton(2, 0, dwb); buttons.add(dwb);
			dwb = new Button(0, 0, "DEBUG_MENU_Full", "Full Map", 12, "Full");
			dwb.setColorInner(Color.RED);
			dw.addGridButton(3, 0, dwb); buttons.add(dwb);
			dwb = new Button(0, 0, "DEBUG_MENU_Packed", "Packed Map", 12, "Packed");
			dwb.setColorInner(Color.RED);
			dw.addGridButton(0, 1, dwb); buttons.add(dwb);
			dwb = new Button(0, 0, "DEBUG_MENU_Seed", "Enter Seed", 14);
			dwb.setColorInner(Color.GREEN);
			dw.addGridButton(0, 3, dwb); buttons.add(dwb);

			buttons.add(dw.getCloseButton());

			windows.add(dw);
			
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
			if (draw_shadows) {item.drawShadow(g);}
			if (item.isHovering(mousePos)) {
				item.drawHover(g);
			} else {
				item.drawButton(g);
			}          
		}

	}

	public void drawButtons(Graphics g, ArrayList<Button> in_buttons, boolean draw_shadows) {

		// Places the buttons.
		for (Button item : in_buttons) {
			if (item != null) {
				if (draw_shadows) {item.drawShadow(g);}
				if (item.isHovering(mousePos)) {
					item.drawHover(g);
				} else {
					item.drawButton(g);
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
	public void updateWindows() {
		
		// Storage for windows to remove.
		ArrayList<InfoWindow> windowsToGo = new ArrayList<InfoWindow>();
		
		for (InfoWindow window : windows) {
			
			// Check if the window is open or not.
			if (window.isOpen()) {
			
				// Window movement and boundary updating.
				if ((window.isOverTopBar(mousePos) && mouseHeld) || (window.isMoving())) {
					window.setMoving(true);
					window.setX(window.getX() + getMouseDiff()[0]);
					window.setY(window.getY() + getMouseDiff()[1]);
				}
				if (!mouseHeld) {
					window.setMoving(false);
				}
				if (window.getX() < 0) {window.setX(0);}
				if (window.getX() > this.windowWidth - window.getWidth()) {window.setX(this.windowWidth - window.getWidth());}
				if (window.getY() < 0) {window.setY(0);}
				if (window.getY() > this.windowHeight - window.getHeight()) {window.setY(this.windowHeight - window.getHeight());} 
				
			}
			else {
				
				// Check window to see if it needs to be removed.
				if (window.getAnimationStatus().equals("Closed")) {windowsToGo.add(window);}
				
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

	// MouseListener methods.
	public void mousePressed() {mouseHeld = true;}

	public void mouseReleased() {mouseHeld = false;}

	// Debug methods
	public void debug(String s) {
		switch (s) {
			case "Buttons":
				System.out.println(buttons.toString());
				break;
			case "Windows":
				System.out.println(windows.toString());
				break;
			case "Mouse":
				System.out.println(mousePos + "\n" + prevMousePos + "\n" + mouseMode + "\n" + mouseBuilding);
				break;
		}
	}

}