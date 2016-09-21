package myInterface.management;

import java.awt.Color;
import java.util.ArrayList;

import myGame.City;
import myGame.Game;
import myGame.Options;
import myGame.Player;
import myGraphics.ImageLibrary;
import myInterface.MyTextMetrics;
import myInterface.buttons.Button;
import myInterface.buttons.ImageButton;
import myInterface.screens.MyScreen;
import myInterface.windows.GridWindow;
import myInterface.windows.HoverWindow;
import myInterface.windows.InfoWindow;
import myInterface.windows.MenuWindow;
import myMain.Board;

public class MyInterfaceFactory {
	
	public static void initInterface(MyInterfaceManager mim, String state, MyScreen scr) {
		
		// Gather coords.
		int[] BOTTOMBARX = {64, scr.getParent().windowWidth / 4, (scr.getParent().windowWidth / 2) - 64, 3 * (scr.getParent().windowWidth / 4) - 128, scr.getParent().windowWidth - 192};
		int BOTTOMBARY = scr.getParent().windowHeight - 36;
		
		// Clears lists.
		mim.clearButtons();
		mim.clearWindows();
		
		// Reset mouse.
		mim.setMouseMode("Pointer");
		
		// Removes hover window.
		mim.setMouseWindow(new HoverWindow(-200, -200));

		if (Board.DEBUG_LAUNCH) {System.out.println("Initialising interface for " + state + ".");}
		
		// Acquires variables from the parent.
		int windowWidth = scr.getParent().windowWidth;
		int windowHeight = scr.getParent().windowHeight;
		Options opt = scr.getParent().opt;
		Game game = scr.getParent().game;
		ImageLibrary il = scr.getParent().il;

		if (state.equals("Map")) {
			
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
				ImageButton imgb = new ImageButton(c.getX(), c.getY(), "City_Button_" + c.getName().replace(" ", "_"), "", 2, c.getName(), il.getImage(cityImage));
				imgb.setOwner("Screen");
				imgb.setWidth(31);
				imgb.setHeight(31);
				imgb.setHoverText(c.getName());
				mim.addButton(imgb);
			}
			mim.shiftButtonsBounded("Screen", game.getMap().getScrollX(), game.getMap().getScrollY());

			// End turn button.
			Button et = new Button(BOTTOMBARX[4], scr.getParent().windowHeight - 36, "Map_End_Turn", "End Turn", 4);
			mim.addButton(et);
			
			// Actions window button.
			Button ac = new Button(BOTTOMBARX[2], scr.getParent().windowHeight - 36, "Map_To_Actions", "View Actions", 64);
			mim.addButton(ac);

			// Pause menu button.
			Button pm = new Button(BOTTOMBARX[0], scr.getParent().windowHeight - 36, "Map_Pause_Menu", "Game Menu", 71);
			mim.addButton(pm);
			
			// Show points button.
			Button pts = new Button(0, windowHeight - 60, 20, 20, "Map_Show_Points", "+", 69);
			pts.setDrawShadow(false);
			mim.addButton(pts);
			
			mim.addDebug();

		}
		else if (state.equals("Action")) {
			
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
				ImageButton imgb = new ImageButton(c.getX(), c.getY(), "City_Button_" + c.getName().replace(" ", "_"), "", 66, c.getName(), il.getImage(cityImage));
				if (c.getOwner() != null && c.getOwner().equals(game.getActivePlayer())) {
					imgb.setExecutionNumber(65);
				}
				imgb.setOwner("Screen");
				imgb.setWidth(31);
				imgb.setHeight(31);
				imgb.setHoverText(c.getName());
				mim.addButton(imgb);
			}
			mim.shiftButtonsBounded("Screen", game.getMap().getScrollX(), game.getMap().getScrollY());

			// End turn button.
			Button et = new Button(BOTTOMBARX[4], BOTTOMBARY, "Action_End_Turn", "End Turn", 4);
			mim.addButton(et);
			
			// Map button.
			Button ac = new Button(BOTTOMBARX[2], BOTTOMBARY, "Action_To_Map", "View Map", 1);
			mim.addButton(ac);
			
			// Pause menu button.
			Button pm = new Button(BOTTOMBARX[0], BOTTOMBARY, "Action_Pause_Menu", "Game Menu", 71);
			mim.addButton(pm);

			// Show points button.
			Button pts = new Button(0, windowHeight - 60, 20, 20, "Action_Show_Points", "+", 70);
			pts.setDrawShadow(false);
			mim.addButton(pts);
			
			mim.addDebug();
			
		}
		else if (state.substring(0,4).equals("City")) {

			// Get the city.
			String theName = state.substring(5);
			City theCity = game.getCityByName(theName);

			// Back to map button.
			mim.addButton(new Button(BOTTOMBARX[4], BOTTOMBARY, "City_Back_To_Map", "Back to Map", 1));
			
			// Pause menu button.
			Button pm = new Button(BOTTOMBARX[0], scr.getParent().windowHeight - 36, "City_Pause_Menu", "Game Menu", 71);
			mim.addButton(pm);

			// City editing buttons.
			if (game.getActivePlayer().equals(theCity.getOwner())) {
				mim.addButton(new Button(770, 20, windowWidth - 790, 40, "City_Build", "Add Building", 16));
				mim.addButton(new Button(770, 90, windowWidth - 790, 40, "City_Move", "Move Building", 18));
				mim.addButton(new Button(770, 160, windowWidth - 790, 40, "City_Remove", "Remove Building", 19));
				mim.addButton(new Button(770, 290, windowWidth - 790, 40, "City_Ordinances", "City Ordinances", 23, theName));
				mim.addButton(new Button(770, 400, windowWidth - 790, 40, "City_Rename", "Rename City", 20, theName));
			}
			mim.addButton(new Button(770, 470, windowWidth - 790, 40, "City_Info", "View City Info", 24));
			
			mim.addDebug();

		} else if (state.substring(0,4).equals("Menu")) {
			
			String submenu = state.substring(5);
			int textHeight = MyTextMetrics.getTextSizeFlat("Test")[1];
			
			MenuWindow winMenu = new MenuWindow("Main Menu", (scr.getParent().windowWidth - 400) / 2, 0, 10);
			winMenu.setHeight(scr.getParent().windowHeight);
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
	
			} else if (submenu.equals("Single Campaign")) {
				
				winMenu.setTitle("Single Player - Campaign");
				
				String[] titles = {"Chapter Select","","Load Chapter in Progress","","","","","","","Back"};
				int[] exes = {3,-1,3,-1,-1,-1,-1,-1,-1,3};
				String[] adds = {"Single Campaign Chapters","","Single Campaign Load","","","","","","","Single Main"};
				boolean[] vises = {true,false,true,false,false,false,false,false,false,true};
				Color[] colors = {Color.GREEN,Color.GREEN,Color.GREEN,Color.GREEN,Color.GREEN,Color.GREEN,Color.GREEN,Color.GREEN,Color.GREEN,Color.GREEN};
				winMenu.addMenuButtons(
						titles,
						exes,
						adds,
						vises,
						colors
				);
				
			} else if (submenu.equals("Single Mission")) {
				
				winMenu.setTitle("Single Player - Mission");
				
				String[] titles = {"Mission Select","","Load Mission in Progress","","Play Custom Mission","","","","","Back"};
				int[] exes = {3,-1,3,-1,3,-1,-1,-1,-1,3};
				String[] adds = {"Single Mission Select","", "Single Mission Load","","Single Mission Custom","","","","","Single Main"};
				boolean[] vises = {true,false,true,false,true,false,false,false,false,true};
				Color[] colors = {Color.GREEN,Color.GREEN,Color.GREEN,Color.GREEN,Color.GREEN,Color.GREEN,Color.GREEN,Color.GREEN,Color.GREEN,Color.GREEN};
				winMenu.addMenuButtons(
						titles,
						exes,
						adds,
						vises,
						colors
				);
				
			} else if (submenu.equals("Single Skirmish")) {
				
				winMenu.setTitle("Single Player - Skirmish");
				
				String[] titles = {"Create New Skirmish","Play Random Skirmish","Play Skirmish Template","","Load Skirmish Game","","","","","Back"};
				int[] exes = {3,59,3,-1,3,-1,-1,-1,-1,3};
				String[] adds = {"Single Skirmish Create","","Single Skirmish Templates","","Single Skirmish Load","","","","","Single Main"};
				boolean[] vises = {true,true,true,false,true,false,false,false,false,true};
				Color[] colors = {Color.GREEN,Color.GREEN,Color.GREEN,Color.GREEN,Color.GREEN,Color.WHITE,Color.GREEN,Color.WHITE,Color.WHITE,Color.GREEN};
				winMenu.addMenuButtons(
						titles,
						exes,
						adds,
						vises,
						colors
				);
				
			} else if (submenu.equals("Single Domination")) {
				
				winMenu.setTitle("Single Player - Domination");
				
				String[] titles = {"Create New Domination","Play Random Domination","","Load Domination Game","","","","","","Back"};
				int[] exes = {3,-1,-1,3,-1,-1,-1,-1,-1,3};
				String[] adds = {"Single Domination Create","","","Single Domination Load","","","","","","Single Main"};
				boolean[] vises = {true,true,false,true,false,false,false,false,false,true};
				Color[] colors = {Color.GREEN,Color.GREEN,Color.GREEN,Color.GREEN,Color.GREEN,Color.GREEN,Color.GREEN,Color.GREEN,Color.GREEN,Color.GREEN};
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
				newWindow.setHeight(scr.getParent().windowHeight);
				newWindow.setWidth(scr.getParent().windowWidth - 100);
				newWindow.setLineSpacing(3);
				String content = "";
				content += "Game Name:\n\n";
				newWindow.addWindowButton(new Button(240, MyTextMetrics.getTextSizeComplex(content, 3)[1], 160, 
						textHeight + 1, "Single_Game_Name", "Test Game", 60, "Name Game|Please enter a name for your game.|61| "));
				content += "Number of Players:\n\n";
				newWindow.addWindowButton(new Button(240, MyTextMetrics.getTextSizeComplex(content, 3)[1], 160, 
						textHeight + 1, "Single_Player_Count", "4", 62, "1|2|8|true"));
				content += "Map Width:\n";
				newWindow.addWindowButton(new Button(240, MyTextMetrics.getTextSizeComplex(content, 3)[1], 160, 
						textHeight + 1, "Single_Map_Width", "1000", 62, "200|1000|2000|true"));
				content += "Map Height:\n";
				newWindow.addWindowButton(new Button(240, MyTextMetrics.getTextSizeComplex(content, 3)[1], 160, 
						textHeight + 1, "Single_Map_Height", "600", 62, "200|600|2000|true"));
				content += "Land Type:\n\n";
				newWindow.addWindowButton(new Button(240, MyTextMetrics.getTextSizeComplex(content, 3)[1], 160, 
						textHeight + 1, "Single_Land_Type", "Smoothed", 58, "Smoothed|Rugged|Crazy"));
				content += "Number of Cities:\n";
				newWindow.addWindowButton(new Button(240, MyTextMetrics.getTextSizeComplex(content, 3)[1], 160, 
						textHeight + 1, "Single_City_Number", "12", 62, "1|2|40|true"));
				content += "Spacing between Cities:\n\n";
				newWindow.addWindowButton(new Button(240, MyTextMetrics.getTextSizeComplex(content, 3)[1], 160, 
						textHeight + 1, "Single_City_Spacing", "100", 62, "50|50|200|true"));
				content += "Victory Condition:\n";
				newWindow.addWindowButton(new Button(240, MyTextMetrics.getTextSizeComplex(content, 3)[1], 160, 
						textHeight + 1, "Single_Victory_Condition", "Conquest", 58, "Conquest|Not Conquest"));
				content += "Max Turns:\n";
				newWindow.addWindowButton(new Button(240, MyTextMetrics.getTextSizeComplex(content, 3)[1], 160, 
						textHeight + 1, "Single_Max_Turns", "200", 62, "50|50|1000|true"));
				content += "\n";
				newWindow.setContent(content);
				newWindow.addWindowButton(new Button((newWindow.getWidth() - 138), newWindow.getHeight() - 74, "Single_Skirmish_Create_Go", "Go!", 63, ""));
				newWindow.addWindowButton(new Button(10, newWindow.getHeight() - 74, "Single_Skirmish_Create_Back", "Back", 3, "Single Skirmish"));
				newWindow.setButtonsColorInner(Color.GREEN);
				newWindow.removeCloseButton();
				mim.addWindowFullForce(newWindow);
				
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
				
				winMenu = null;
				mim.addWindowFull(MyInterfaceFactory.initWindow(submenu, scr));
				
			} else if (submenu.equals("Options Video")) {
				
				winMenu = null;
				mim.addWindowFull(MyInterfaceFactory.initWindow(submenu, scr));
							
			} else if (submenu.equals("Options Audio")) {
				
				winMenu = null;
				mim.addWindowFull(MyInterfaceFactory.initWindow(submenu, scr));
				
			} else if (submenu.equals("Options Gameplay")) {
				
				winMenu = null;
				mim.addWindowFull(MyInterfaceFactory.initWindow(submenu, scr));
				
			} else if (submenu.equals("Options Online")) {
				
				winMenu = null;
				mim.addWindowFull(MyInterfaceFactory.initWindow(submenu, scr));
				
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
			
			if (winMenu != null) {mim.addWindowFullForce(winMenu);}
			
			mim.addDebug();
			

		} else if (state.equals("DEBUG")) {

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
			dwb = new Button(0, 0, "DEBUG_MENU_Random", "Randomised Map", 12, "Random");
			dwb.setColorInner(Color.RED);
			dw.addGridButton(1, 0, dwb);
			dwb = new Button(0, 0, "DEBUG_MENU_Seed", "Enter Seed", 39);
			dwb.setColorInner(Color.GREEN);
			dw.addGridButton(1, 3, dwb);
			dwb = new Button(0, 0, "DEBUG_MENU_Images", "Image Tester", 45);
			dwb.setColorInner(Color.ORANGE);
			dw.addGridButton(3, 0, dwb);
			dwb = new Button(0, 0, "DEBUG_MENU_Menu", "Main Menu", 3, "Main");
			dwb.setColorInner(Color.BLUE);
			dw.addGridButton(3, 3, dwb);

			mim.addWindowFullForce(dw);
			
		}
		else if (state.equals("ImageTester")) {
			
			// Image changing buttons.
			mim.addButton(new Button(10, windowHeight / 2 - 20, 40, 40, "Image_Test_Left", "<", 46));
			mim.addButton(new Button(windowWidth - 50, windowHeight / 2 - 20, 40, 40, "Image_Test_Right", ">", 47));
			
			// Back button.
			mim.addButton(new Button(windowWidth / 2 - 40, windowHeight - 40, 80, 30, "Image_Test_Back", "Back", 15));
			
		}
		
	}
	
	public static InfoWindow initWindow(String name, MyScreen scr) {
		
		if (Board.DEBUG_LAUNCH) {System.out.println("Creating the window " + name + ".");}
				
		// Acquiring relevant variables.
		int windowWidth = scr.getParent().windowWidth;
		int windowHeight = scr.getParent().windowHeight;
		Options opt = scr.getParent().opt;
		Game game = scr.getParent().game;
		ImageLibrary il = scr.getParent().il;
		int textHeight = MyTextMetrics.getTextSizeFlat("Test")[1];
		
		if (name.equals("Options Main")) {
			
			MenuWindow newWindow = new MenuWindow("Options", (windowWidth - 400) / 2, 0, 10);
			newWindow.setHeight(scr.getParent().windowHeight);
			newWindow.setGridX(10);
			newWindow.setGridY(10);
			newWindow.setButtonGap(15);
			newWindow.setButtonWidth(newWindow.getWidth() - 20);
			newWindow.removeCloseButton();
			
			if (scr.getTitle().equals("Menu Screen")) {
				String[] titles = {"Video","Audio","Gameplay","Online","Debug","-","-","Apply","Confirm","Cancel"};
				int[] exes = {3,3,3,3,10,-1,-1,51,52,53};
				String[] adds = {"Options Video","Options Audio","Options Gameplay","Options Online","","","","Options Main","Main","Main"};
				boolean[] vises = {true,true,true,true,scr.getParent().DEBUG_MASTER,false,false,true,true,true};
				Color[] colors = {Color.PINK,Color.PINK,Color.PINK,Color.PINK,Color.DARK_GRAY,Color.WHITE,Color.WHITE,Color.PINK,Color.PINK,Color.PINK};
				newWindow.addMenuButtons(
						titles,
						exes,
						adds,
						vises,
						colors
				);
			} else {
				String[] titles = {"Video","Audio","Gameplay","Online","-","-","-","Apply","Confirm","Cancel"};
				int[] exes = {3,3,3,3,-1,-1,-1,51,80,81};
				String[] adds = {"Options Video","Options Audio","Options Gameplay","Options Online","","","","Options Main","Options","Options"};
				boolean[] vises = {true,true,true,true,false,false,false,true,true,true};
				Color[] colors = {Color.PINK,Color.PINK,Color.PINK,Color.PINK,Color.DARK_GRAY,Color.WHITE,Color.WHITE,Color.PINK,Color.PINK,Color.PINK};
				newWindow.addMenuButtons(
						titles,
						exes,
						adds,
						vises,
						colors
				);
			}

			return newWindow;
			
		} else if (name.equals("Options Video")) {
			
			InfoWindow newWindow = new InfoWindow("Video Options", 50, 100);
			newWindow.setHeight(scr.getParent().windowHeight);
			newWindow.setWidth(scr.getParent().windowWidth - 100);
			newWindow.setLineSpacing(3);
			String content = "";
			content += "Fullscreen:\n";
			newWindow.addWindowButton(new Button(240, MyTextMetrics.getTextSizeComplex(content,3)[1], 160, 
					textHeight + 1, "Option_Fullscreen", Boolean.toString(scr.getParent().opt.getStatusFullscreen()), 
					54, "fullscreen"));
			content += "Resolution:\n\n";
			newWindow.addWindowButton(new Button(240, MyTextMetrics.getTextSizeComplex(content,3)[1], 160, 
					textHeight + 1, "Option_Resolution", 
					"-", 0, ""));
			content += "Show FPS:\n";
			newWindow.addWindowButton(new Button(240, MyTextMetrics.getTextSizeComplex(content,3)[1], 160, 
					textHeight + 1, "Option_FPS", 
					Boolean.toString(scr.getParent().opt.getStatusFPS()), 54, "fpscounter"));
			content += "Show Time to Act/Draw:\n\n";
			newWindow.addWindowButton(new Button(240, MyTextMetrics.getTextSizeComplex(content,3)[1], 160, 
					textHeight + 1, "Option_TTAD", 
					Boolean.toString(scr.getParent().opt.getStatusTTAD()), 54, "timetoactdraw"));
			content += "Gui Scale:\n\n";
			newWindow.addWindowButton(new Button(240, MyTextMetrics.getTextSizeComplex(content,3)[1], 160, 
					textHeight + 1, "Option_Scale", 
					Integer.toString(scr.getParent().opt.getValue("guiscale")), 55, "guiscale|1|1|4|true"));
			content += "Primary Monitor:\n\n";
			newWindow.addWindowButton(new Button(240, MyTextMetrics.getTextSizeComplex(content,3)[1], 160, 
					textHeight + 1, "Option_Monitor", 
					Integer.toString(scr.getParent().opt.getValue("monitor")), 55, 
					"monitor|1|0|" + Integer.toString(Board.SCREENCOUNT - 1) + "|true"));
			content += "\n";
			newWindow.setContent(content);
			newWindow.addWindowButton(new Button(160, newWindow.getHeight() - 74, "Options_Goto_Audio", "Audio", 3, "Options Audio"));
			newWindow.addWindowButton(new Button(310, newWindow.getHeight() - 74, "Options_Goto_Gameplay", "Gameplay", 3, "Options Gameplay"));
			newWindow.addWindowButton(new Button(460, newWindow.getHeight() - 74, "Options_Goto_Online", "Online", 3, "Options Online"));
			newWindow.addWindowButton(new Button((newWindow.getWidth() - 138), newWindow.getHeight() - 74, "Options_Goto_Back", "Back", 3, "Options Main"));
			newWindow.setButtonsColorInner(Color.PINK);
			newWindow.removeCloseButton();
			return newWindow;
						
		} else if (name.equals("Options Audio")) {
			
			InfoWindow newWindow = new InfoWindow("Audio Options", 50, 100);
			newWindow.setHeight(scr.getParent().windowHeight);
			newWindow.setWidth(scr.getParent().windowWidth - 100);
			newWindow.setLineSpacing(3);
			String content = "";
			content += "Master Volume:\t" + scr.getParent().opt.getValueVolumeMaster() + "\n";
			newWindow.addWindowButton(new Button(300, MyTextMetrics.getTextSizeComplex(content, 3)[1], 40, 
					textHeight + 1, "Option_Master_Down", "-", 57, "volumemaster|-10|0|100|false"));
			newWindow.addWindowButton(new Button(350, MyTextMetrics.getTextSizeComplex(content, 3)[1], 40, 
					textHeight + 1, "Option_Master_Up", "+", 57, "volumemaster|10|0|100|false"));
			content += "Music Volume:\t" + scr.getParent().opt.getValueVolumeMusic() + "\n";
			newWindow.addWindowButton(new Button(300, MyTextMetrics.getTextSizeComplex(content, 3)[1], 40, 
					textHeight + 1, "Option_Music_Down", "-", 57, "volumemusic|-10|0|100|false"));
			newWindow.addWindowButton(new Button(350, MyTextMetrics.getTextSizeComplex(content, 3)[1], 40, 
					textHeight + 1, "Option_Music_Up", "+", 57, "volumemusic|10|0|100|false"));
			content += "SFX Volume:\t" + scr.getParent().opt.getValueVolumeSound() + "\n";
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
			return newWindow;
			
		} else if (name.equals("Options Gameplay")) {
			
			InfoWindow newWindow = new InfoWindow("Gameplay Options", 50, 100);
			newWindow.setHeight(scr.getParent().windowHeight);
			newWindow.setWidth(scr.getParent().windowWidth - 100);
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
			return newWindow;
			
		} else if (name.equals("Options Online")) {
			
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
			return newWindow;
			
		}
		return null;
	}

}
