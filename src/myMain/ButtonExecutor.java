/*
 * ButtonExecutor.java
 * 
 * Stores all code for button execution routines.
 * 
 */

package myMain;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import myData.MyStrings;
import myGame.Action;
import myGame.Building;
import myGame.City;
import myGame.Options;
import myGame.Ordinance;
import myGame.OrdinanceBook;
import myGame.Player;
import myInterface.MyTextMetrics;
import myInterface.buttons.Button;
import myInterface.screens.ActionScreen;
import myInterface.screens.ImageTestScreen;
import myInterface.screens.MapScreen;
import myInterface.windows.GridWindow;
import myInterface.windows.InfoWindow;
import myInterface.windows.InputWindow;
import myInterface.windows.ListWindow;
import myInterface.windows.MenuWindow;
import myInterface.windows.YesNoCancelWindow;
import myInterface.windows.YesNoWindow;

public class ButtonExecutor {
	
	// Field
	private Board b;

	// Constructor
	public ButtonExecutor(Board b) {
		this.b = b;
	}
	
	// Function that runs the appropriate code for buttons than are clicked on.
	public void execute(int exec) {
		execute(exec, "");
	}
	
	public void execute(int exec, String add) {
		execute(new Button(0,0,"","",exec,add));
	}
	
	public void execute(Button button) {

		if (Board.DEBUG_TRACE) {
			String debugAdd = button.getAdditionalString();
			if (debugAdd.equals("")) {
				b.cmd.debug("EXECUTING BUTTON CODE NUMBER " + button.getExecutionNumber() + " WITH NO ADDITIONAL STRING");     
			} else {
				b.cmd.debug("EXECUTING BUTTON CODE NUMBER " + button.getExecutionNumber() + " WITH '" + debugAdd + "'.");        		
			}
		}
		
		int exec = button.getExecutionNumber();
		String add = button.getAdditionalString();

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
			b.switchScreen("Map", "");
		}
		else if (exec == 2) {												// Go to city action. 
			b.switchScreen("City", add);
		}
		else if (exec == 3) {												// Go to menu action.
			b.switchScreen("Menu", add);
		}
		else if (exec == 4) { 												// End turn action.
			b.game.nextPlayer();
		}
		else if (exec == 5)	{												// View players.
			if (!b.mim.checkWindowsFor("Players")) {
				// Creating the new window.
				wind = new InfoWindow("Players", (b.windowWidth - 400) / 2, 100);
				butt = wind.getCloseButton();
				cont = "| Player Name\t| Commander Name\n|------------------------------------------------------------\n";
				for (int i = 0; i < b.game.getPlayers().size(); ++i) {
					// Generate table rows.
					currentPlayer = b.game.getPlayers().get(i);
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
				b.mim.addWindowFull(wind);
			}
			if (Board.DEBUG_TRACE) {b.mim.debug("Buttons");}
		}
		else if (exec == 6) { 												// View stats.
			if (!b.mim.checkWindowsFor("Stats")) {
				// Creating window.
				wind = new InfoWindow("Stats", (b.windowWidth - 400) / 2, 100);
				butt = wind.getCloseButton();
				cont = "Stats\n------\nWhat do I even put here.";
				wind.setContent(cont);
				b.mim.addWindowFull(wind);
			}
		}
		else if (exec == 7) {												// Close window.
			b.mim.getWindow(add).close();
		}
		else if (exec == 8) { 												// Open full player window.
			if (!b.mim.checkWindowsFor("Player - " + add)) {
				// Create window.
				wind = new InfoWindow("Player - " + add, (b.windowWidth - 400) / 2, 100);
				butt = wind.getCloseButton();
				cont = add;
				wind.setContent(cont);
				wind.addReturnButton(5, "");
				// Close Player Info window.
				b.mim.removeWindowFull("Player Info");
				// Add window to manager.
				b.mim.addWindowFull(wind);
			}
		}
		else if (exec == 9) {												// Opens debug info window.
			if (!b.mim.checkWindowsFor("DEBUG_INFO_WINDOW")) {
				// Create window.
				wind = new InfoWindow("DEBUG_INFO_WINDOW", (b.windowWidth - 400) / 2, 100);
				butt = wind.getCloseButton();
				cont = "Current map seed:- " + b.randomMapSeed;
				wind.setContent(cont);
				b.mim.addWindowFull(wind);
			}
		}
		else if (exec == 10) {												// Opens debug action window.
			if (!b.mim.checkWindowsFor("DEBUG_ACTION_WINDOW")) {
				// Create window.
				int DEBUG_GRID_ROWS = 5;
				int DEBUG_GRID_COLS = 5;
				gridow = new GridWindow("DEBUG_ACTION_WINDOW", Board.WINDOW_CENTER_X, 100, DEBUG_GRID_ROWS, DEBUG_GRID_COLS);
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
					// Button 16: Print buttons.
					Button d16 = new Button(0, 0, "DEBUG_B_Print_Buttons", "Print Buttons", 25); d16.setColorInner(Color.GREEN);
					gridow.addGridButton(3, 0, d16);
					// Button 17: Print windows.
					Button d17 = new Button(0, 0, "DEBUG_B_Print_Windows", "Print Windows", 26); d17.setColorInner(Color.GREEN);
					gridow.addGridButton(3, 1, d17);
					// Button 18: Print screen.
					Button d18 = new Button(0, 0, "DEBUG_B_Print_Screen", "Print Screen", 34); d18.setColorInner(Color.GREEN);
					gridow.addGridButton(3, 2, d18);
					// Button 19: Print mouse.
					Button d19 = new Button(0, 0, "DEBUG_B_Print_Mouse", "Print Mouse", 32); d19.setColorInner(Color.GREEN);
					gridow.addGridButton(3, 3, d19);
					// Button 20: Print accepted keys.
					Button d20 = new Button(0, 0, "DEBUG_B_Print_Keys", "Print Keys", 48); d20.setColorInner(Color.GREEN);
					gridow.addGridButton(3, 4, d20);
					// Button 21: BuildDex dump.
					Button d21 = new Button(0, 0, "DEBUG_B_Dump_BuildDex", "Dump BuildDex", 33); d21.setColorInner(Color.GREEN);
					gridow.addGridButton(4, 0, d21);
					// Button 25: Close game.
					Button d25 = new Button(0, 0, "DEBUG_B_Close", "Close Game", 11); d25.setColorInner(Color.GREEN);
					gridow.addGridButton(4, 4, d25);
				}
				b.mim.addWindowFull(gridow);
			}
		}
		else if (exec == 11) {												// Closes the game.
			// TODO: Implement quitting.
		}
		else if (exec == 12) { 												// Generates a new random game.
			// Sets up the game state.
			b.game = b.initGame(add);
			b.switchScreen("Map", "");
		}
		else if (exec == 13) {												// Toggles map between optimised and initial routes.
			b.game.toggleDRD();
		}
		else if (exec == 14) { 												// Random map seed entry via command line.
			Scanner r = new Scanner(System.in);
			System.out.print("[!] New seed:- ");
			b.randomMapSeed = r.nextLong();
			b.randomMap = new Random(b.randomMapSeed);
			b.cmd.alert("New seed accepted.");
			r.close();
		}
		else if (exec == 15) {												// Jump to debug start screen.
			b.switchScreen("DEBUG", "");
		}														
		else if (exec == 16) {												// Open city add building menu.
			
			// Reset mouse data.
			b.mim.setMouseMode("Pointer");
			
			// Creating window.
			gridow = new GridWindow("Add Building", Board.WINDOW_CENTER_X, 100, 5, 2);
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
			b.mim.addWindowFull(gridow);
			
		}
		else if (exec == 17) { 												// Activate city build building mode.
			// Gets the city name and city.
			String cityName = b.state.substring(5);
			City thisCity = b.game.getMap().getCityByName(cityName);
						
			if (thisCity.hasAreaFor(b.buildDex.getBuilding(add))) {
				b.mim.setMouseMode("Build");			
				b.mim.setMouseBuilding(b.buildDex.getBuilding(add));
				b.mim.removeWindowMaxFull("Add Building", 0, 12);
			} else {
				b.createErrorWindow("No room in the city for this building.");
			}
		}
		else if (exec == 18) { 												// Activate city move building mode.
			// Gets the city name and city.
			String cityName = b.state.substring(5);
			City thisCity = b.game.getMap().getCityByName(cityName);
			
			// Checks building count.
			if (thisCity.getBuildings().size() > 0) {
				b.mim.setMouseMode("Move");
			} else {
				b.createErrorWindow("No buildings to move.");
			}
		}
		else if (exec == 19) {												// Activate city remove building mode.
			// Gets the city name and city.
			String cityName = b.state.substring(5);
			City thisCity = b.game.getMap().getCityByName(cityName);
			
			// Checks building count.
			if (thisCity.getBuildings().size() > 0) {
				b.mim.setMouseMode("Destroy");
			} else {
				b.createErrorWindow("No buildings to remove.");
			}
		}
		else if (exec == 20) { 												// Open city rename city window.
			// Reset mouse.
			b.mim.setMouseMode("Pointer");
			// Generate basic input window.
			InputWindow inpu = new InputWindow("City Name Change", Board.WINDOW_CENTER_X, 125, b.mim.getInputString());
			inpu.setContent("Please enter a new city name.");
			inpu.getWindowButtons().get(0).setExecutionNumber(37);
			inpu.getWindowButtons().get(0).setAdditionalString(add);
			b.mim.addWindowFull(inpu);
		}
		else if (exec == 21) {												// Open city building category window.
			List<String> the_buildings = b.buildDex.getCategory(add);
			listow = new ListWindow("Add Building - " + add + " Buildings", Board.WINDOW_CENTER_X, 50, 7);
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
			b.mim.addWindowFull(listow);
			b.mim.removeWindowFull("Add Building");
		}
		else if (exec == 22) {														// Add building info to city building category window.
			// Splits the additional string into the necessary parts.
			String windowTitle = add.split("\\|")[0];
			String buildingType = add.split("\\|")[1];

			// Retrieves the window and building specified in the additional string.
			listow = (ListWindow) b.mim.getWindow(windowTitle);
			bldg = b.buildDex.getBuilding(buildingType);

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
			b.mim.setMouseMode("Pointer");
			
			// Getting data and setting up window.
			OrdinanceBook ords = b.game.getMap().getCityByName(add).getAllOrdinances();
			listow = new ListWindow("City Ordinances", Board.WINDOW_CENTER_X, 50, 7);
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
			b.mim.addWindowFull(listow);
		}
		else if (exec == 24) {												// Open city info window.
			b.mim.setMouseMode("Pointer");
			InfoWindow info = new InfoWindow("City Info", Board.WINDOW_CENTER_X, 100);
			info.setContent("No info to show.");
			b.mim.addWindowFull(info);
		}
		else if (exec == 25) {												// Print buttons.
			b.mim.debug("Buttons");
		}
		else if (exec == 26) { 												// Print windows.
			b.mim.debug("Windows");
		}
		else if (exec == 27) {												// Up button in ListWindow.
			listow = (ListWindow) b.mim.getWindow(add);
			listow.decPageNumber();
			listow.fillGridList();
			listow.setDownVis(true);
			listow.setUpVis(!listow.isAtMin());
		}
		else if (exec == 28) {												// Down button in ListWindow.
			listow = (ListWindow) b.mim.getWindow(add);
			listow.incPageNumber();
			listow.fillGridList();
			listow.setUpVis(true);
			listow.setDownVis(!listow.isAtMax());
		}														
		else if (exec == 29) {												// Return button handling.
			// Split execution string into window to open's exec code, window to open's add string, and the window to close's name.
			String[] breakdown = add.split("\\|");
			
			if (Board.DEBUG_TRACE) {b.cmd.debug("RETURN BUTTON EXECUTION: " + breakdown[0] + ", " + breakdown[1] + ", " + breakdown[2] + ".");}

			// Opens the window to open.
			this.execute(Integer.parseInt(breakdown[0]), breakdown[1]);
			
			// Closes the window to close.
			b.mim.removeWindowFull(breakdown[2]);
		}
		else if (exec == 30) {												// Up button in Add Building ListWindow.
			listow = (ListWindow) b.mim.getWindow(add);
			this.execute(27, add);
			listow.setContent("Click on a button to select\na building.");
			listow.getWindowButtons().get(0).setVisible(false);
			listow.clearImages();
		}
		else if (exec == 31) {												// Down button in Add Building ListWindow.
			listow = (ListWindow) b.mim.getWindow(add);
			this.execute(28, add);
			listow.setContent("Click on a button to select\na building.");
			listow.getWindowButtons().get(0).setVisible(false);
			listow.clearImages();
		}
		else if (exec == 32) { 												// Mouse display.
			b.mim.debug("Mouse");
		}
		else if (exec == 33) {												// BuildDex dumper.
			System.out.println();
			System.out.println(b.buildDex);
		}
		else if (exec == 34) {												// Screen state printer.
			b.cmd.debug(b.scr.getTitle());
			b.cmd.debug(b.state);
		} else if (exec == 35) {											// Opens actions window.
			wind = new InfoWindow("Actions Window", Board.WINDOW_CENTER_X, 100);
			cont = "COMING SOON";
			wind.setContent(cont);
			b.mim.addWindowFull(wind);
		} else if (exec == 36) {											// Test input window.
			InputWindow inpu = new InputWindow("TEST_INPUT", Board.WINDOW_CENTER_X, 125, b.mim.getInputString());
			inpu.setContent("Please enter some test data.");
			b.mim.addWindowFull(inpu);
		} else if (exec == 37) {											// Renaming city code.
			InputWindow inpu = (InputWindow) b.mim.getWindow("City Name Change");
			String newName = inpu.getInputString();
			if (b.val.validate("City Name", newName)) {
				if (b.game.isUniqueName(newName)) {
					b.game.updateName(add, newName);
					b.state = "City-" + newName;
					b.createQuickWindow("Success!", "The name of this city is now " + newName + "!");
					inpu.close();
				} else {
					b.createErrorWindow("Name is already in use.");
				}
			} else {
				b.createErrorWindow("Invalid name.\nPlease use only letters, numbers, hyphens and space.\nThe name must also be between 1 and 32 characters long.");
			}
		} 
		else if (exec == 38) {												// Clear input window code.
			InfoWindow inpu = b.mim.getWindow(add);
			String initContent = inpu.getContent();
			inpu.clearText();
			inpu.setContent(initContent);
		}
		else if (exec == 39) { 												// Random map seed entry via input window.
			// Generate basic input window.
			InputWindow inpu = new InputWindow("Seed Entry", Board.WINDOW_CENTER_X, 125, b.mim.getInputString());
			inpu.setContent("Please enter a seed for the RNG.\nSeeds must be a long between 0 and 999999999999.");
			inpu.getWindowButtons().get(0).setExecutionNumber(40);
			b.mim.addWindowFull(inpu);
		}
		else if (exec == 40) {												// Random seed entry code.
			InputWindow inpu = (InputWindow) b.mim.getWindow("Seed Entry");
			String newSeed = inpu.getInputString();
			if (b.val.validate("Random Seed", newSeed)) {
				b.randomMap = new Random(Long.parseLong(newSeed));
				b.createQuickWindow("Random Seed Accepted", "New seed = '" + newSeed + "'.");
				inpu.close();
			} else {
				b.createErrorWindow("Invalid seed.");
			}
		}
		else if (exec == 41) {												// Add ordinance info to ordinance window.

			// Retrieves the window and building specified in the additional string.
			listow = (ListWindow) b.mim.getWindow("City Ordinances");
			Ordinance ord = b.game.getCityByName(b.state.substring(5)).getOrdinance(add);

			// Begins preparing the text content.
			listow.setContentX(175);
			cont = ord.getName() + "\n \n";
			String startingDescription = ord.getDesc().replace("$city", b.state.substring(5));
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
			if (b.game.getCityByName(b.state.substring(5)).getOrdinanceActivity(add)) {
				buttStr = "Repeal";
			}
			listow.getWindowButtons().get(0).setButtonText(buttStr);;
			
		}
		else if (exec == 42) {												// Enact/repeal ordinance.
			// Getting city's ordinance book.
			City cit = b.game.getCityByName(b.state.substring(5));
			
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
			b.mim.getWindow("City Ordinances").close();
			b.createQuickWindow("Success!", add + " has been " + enactStr + " in " + b.state.substring(5) + ".");
			
		}
		else if (exec == 43) {												// Up button in Ordinance window.
			listow = (ListWindow) b.mim.getWindow(add);
			this.execute(27, add);
			listow.setContent("Ordinances available in " + b.state.substring(5) + ":\n\n");
			listow.setContentX(0);
			listow.getWindowButtons().get(0).setVisible(false);
			listow.clearImages();
		}
		else if (exec == 44) {												// Down button in Ordinance window.
			listow = (ListWindow) b.mim.getWindow(add);
			this.execute(28, add);
			listow.setContent("Ordinances available in " + b.state.substring(5) + ":\n\n");
			listow.setContentX(0);
			listow.getWindowButtons().get(0).setVisible(false);
			listow.clearImages();
		}
		else if (exec == 45) {												// Image tester screen code
			b.switchScreen("ImageTester", "1");
		}
		else if (exec == 46) {												// Image tester previous image
			ImageTestScreen its = (ImageTestScreen) b.scr;
			its.setImageNumber(its.getImageNumber() - 1);
			if (its.getImageNumber() < 1) {
				its.setImageNumber(b.il.getMax());
			}
		}
		else if (exec == 47) {												// Image tester next image
			ImageTestScreen its = (ImageTestScreen) b.scr;
			its.setImageNumber(its.getImageNumber() + 1);
			if (its.getImageNumber() > b.il.getMax()) {
				its.setImageNumber(1);
			}
		}
		else if (exec == 48) {												// Print accepted keys.
			b.mim.debug("Keys");
		}
		else if (exec == 49) {												// Quit confirmation.
			YesNoWindow yn = new YesNoWindow("Quit?", Board.WINDOW_CENTER_X, 300);
			yn.setContent("Are you sure you want to quit?");
			yn.setNoExec(11);
			yn.setNoExec(50);
			yn.setNoAdd("Quit?");
			b.mim.addWindowFull(yn);
		}
		else if (exec == 50) {												// YNC test code.
			YesNoCancelWindow ync = new YesNoCancelWindow("TEST", Board.WINDOW_CENTER_X, 300);
			b.mim.addWindowFull(ync);
		}
		else if (exec == 51) {												// Apply button code in Options.
			b.opt.writeToFile(Board.CONFIGFILE, Board.OPTIONSFILE);
			b.createQuickWindow("Saved!", "Options changes saved.\nSome changes will require a game restart.");
		}
		else if (exec == 52) {												// Confirm button code in Options.
			b.opt.writeToFile(Board.CONFIGFILE, Board.OPTIONSFILE);
			execute(3,"Main");
			b.createQuickWindow("Saved!", "Options changes saved.\nSome changes will require a game restart.");
		}
		else if (exec == 53) {												// Cancel button code in Options.
			b.opt = new Options(Board.CONFIGFILE, Board.OPTIONSFILE);
			execute(3,"Main");
			b.createQuickWindow("Cancelled", "Options reverted to intital values.");
		}
		else if (exec == 54) {												// Toggle boolean option.
			b.opt.setStatus(add, !b.opt.getStatus(add));
			button.setButtonText(Boolean.toString(b.opt.getStatus(add)));
		}
		else if (exec == 55) {												// Inc/dec integer option.
			String key = add.split("\\|")[0];
			int value = Integer.parseInt(add.split("\\|")[1]);
			int llim = Integer.parseInt(add.split("\\|")[2]);
			int ulim = Integer.parseInt(add.split("\\|")[3]);
			boolean loop = Boolean.parseBoolean(add.split("\\|")[4]);
			b.opt.setValue(key, b.opt.getValue(key) + value);
			if (loop) {
				if (b.opt.getValue(key) > ulim) {b.opt.setValue(key, llim);}
				if (b.opt.getValue(key) < llim) {b.opt.setValue(key, ulim);}
			} else {
				if (b.opt.getValue(key) > ulim) {b.opt.setValue(key, ulim);}
				if (b.opt.getValue(key) < llim) {b.opt.setValue(key, llim);}
			}
			button.setButtonText(Integer.toString(b.opt.getValue(key)));
		}
		else if (exec == 56) {												// Set integer option.
			String key = add.split("\\|")[0];
			int value = Integer.parseInt(add.split("\\|")[1]);
			b.opt.setValue(key, value);
			button.setButtonText(Integer.toString(b.opt.getValue(key)));
		}
		else if (exec == 57) {												// Special volume control option.
			execute(new Button(0,0,"","",55,add));
			execute(3,b.state.substring(5));
			b.mim.getWindow("Audio Options").forceOpen();
		}
		else if (exec == 58) {												// Button setting looping.
			String next = MyStrings.sequenceAdvance(button.getButtonText(), add.split("\\|"), true);
			button.setButtonText(next);
		}
		else if (exec == 59) {												// Random skirmish button.
			b.game = b.initGame("Random");
			execute(1);
		}
		else if (exec == 60) {												// Open generic input window.
			String title = add.split("\\|")[0];
			String conte = add.split("\\|")[1];
			String execu = add.split("\\|")[2];
			String addit = add.split("\\|")[3];
			InputWindow inpu = new InputWindow(title, Board.WINDOW_CENTER_X, 125, b.mim.getInputString());
			inpu.setContent(conte);
			inpu.getWindowButtons().get(0).setExecutionNumber(Integer.parseInt(execu));
			inpu.getWindowButtons().get(0).setAdditionalString(addit);
			b.mim.addWindowFull(inpu);
		}
		else if (exec == 61) {												// Set game name.
			InputWindow input = (InputWindow) b.mim.getWindow("Name Game");
			String gameName = input.getInputString();
			InfoWindow skirmish = b.mim.getWindow("Create Skirmish");
			Button nameButton = skirmish.getWindowButtons().get(0);
			if (b.val.validate("Game Name", gameName)) {
				nameButton.setButtonText(gameName);
			} else {
				b.createErrorWindow("Game name is too long/short, or contains invalid characters.");
			}
			input.close();
			b.createQuickWindow("Success", "This game will now be called \"" + gameName + "\"!");
		}
		else if (exec == 62) {												// Inc/dec integer button text.
			int value = Integer.parseInt(add.split("\\|")[0]);
			int llim = Integer.parseInt(add.split("\\|")[1]);
			int ulim = Integer.parseInt(add.split("\\|")[2]);
			boolean loop = Boolean.parseBoolean(add.split("\\|")[3]);
			int newValue = Integer.parseInt(button.getButtonText()) + value;
			if (loop) {
				if (newValue > ulim) {newValue = llim;}
				if (newValue < llim) {newValue = ulim;}
			} else {
				if (newValue > ulim) {newValue = ulim;}
				if (newValue < llim) {newValue = llim;}
			}
			button.setButtonText(Integer.toString(newValue));
		}
		else if (exec == 63) {												// Skirmish creation button.
			InfoWindow skirmish = b.mim.getWindow("Create Skirmish");
			ArrayList<Button> skb = skirmish.getWindowButtons();			
			if (Integer.parseInt(skb.get(1).getButtonText()) <= Integer.parseInt(skb.get(5).getButtonText())) {
				b.game = b.initGame(skb.get(0).getButtonText(), 
						Integer.parseInt(skb.get(1).getButtonText()), 
						Integer.parseInt(skb.get(2).getButtonText()),
						Integer.parseInt(skb.get(3).getButtonText()), 
						skb.get(4).getButtonText(), 
						Integer.parseInt(skb.get(5).getButtonText()), 
						Integer.parseInt(skb.get(6).getButtonText()), 
						skb.get(7).getButtonText(), 
						Integer.parseInt(skb.get(8).getButtonText())
						);
				execute(1);
			} else {
				b.createErrorWindow("Not enough cities for the requested amount of players.");
			}
		}
		else if (exec == 64) {												// Go to action screen.
			b.switchScreen("Action", "");
		}
		else if (exec == 65) {												// Open friendly action window.
			int INIT_X = 100;
			int INIT_Y = 100;
			GridWindow actWindow = new GridWindow("Actions for " + add + " - Friendly", INIT_X, INIT_Y, 4, 6);
			actWindow.setWidth(b.windowWidth - 2 * INIT_X);
			actWindow.setHeight(b.windowHeight - 2 * INIT_Y);
			int colNum = 0;
			for (String categ : b.buildDex.getCategories()) {
				if ((!categ.equals("Residential")) && (!categ.equals("Happiness"))) {
					for (int power = 1; power <= 3; ++power) {
						actWindow.addGridButton(power, colNum, new Button(0,0,"Action_Friendly_" + add.replace(" ", "_") + "_" + categ + "_" + Integer.toString(power), categ + " " + Integer.toString(power), 67, add + "|" + categ + "|" + Integer.toString(power)));
					}
					colNum += 1;
				}
			}
			b.mim.addWindowFull(actWindow);
		}
		else if (exec == 66) {												// Open enemy action window.
			int INIT_X = 100;
			int INIT_Y = 100;
			GridWindow actWindow = new GridWindow("Actions for " + add + " - Enemy", INIT_X, INIT_Y, 4, 6);
			actWindow.setWidth(b.windowWidth - 2 * INIT_X);
			actWindow.setHeight(b.windowHeight - 2 * INIT_Y);
			int colNum = 0;
			for (String categ : b.buildDex.getCategories()) {
				if ((!categ.equals("Residential")) && (!categ.equals("Happiness"))) {
					for (int power = 1; power <= 3; ++power) {
						actWindow.addGridButton(power, colNum, new Button(0,0,"Action_Enemy_" + add.replace(" ", "_") + "_" + categ + "_" + Integer.toString(power), categ + " " + Integer.toString(power), 68, add + "|" + categ + "|" + Integer.toString(power)));
					}
					colNum += 1;
				}
			}
			b.mim.addWindowFull(actWindow);
		} 
		else if (exec == 67) {											// Add defensive action to action queue.
			String citName = add.split("\\|")[0];
			String categ = add.split("\\|")[1];
			int powerLev = Integer.parseInt(add.split("\\|")[2]);
			//b.game.addAction(new Action(categ, powerLev, b.game.getCityByName(citName), "Defend"));
			// TODO: Deduct point cost of action when creating action.
			b.createQuickWindow("Action Launched", "The " + categ + " " + Integer.toString(powerLev) + " action has been launched on " + citName + ".");
		} 
		else if (exec == 68) {											// Add offensive action to action queue.
			String citName = add.split("\\|")[0];
			String categ = add.split("\\|")[1];
			int powerLev = Integer.parseInt(add.split("\\|")[2]);
			//b.game.addAction(new Action(categ, powerLev, b.game.getCityByName(citName), "Attack"));
			// TODO: Deduct point cost of action when creating action.
			b.createQuickWindow("Action Launched", "The " + categ + " " + Integer.toString(powerLev) + " action has been launched on " + citName + ".");
		} 
		else if (exec == 69) {											// Show points button - Map screen
			MapScreen pScr = (MapScreen) b.scr;
			pScr.togglePointsDisplay();
			Button pluMin = b.mim.getButtonByID("Map_Show_Points");
			if (pluMin.getButtonText().equals("+")) {
				pluMin.setButtonText("-");
			} else {
				pluMin.setButtonText("+");
			}
		}
		else if (exec == 70) {											// Show points button - Action screen
			ActionScreen pScr = (ActionScreen) b.scr;
			pScr.togglePointsDisplay();
			Button pluMin = b.mim.getButtonByID("Action_Show_Points");
			if (pluMin.getButtonText().equals("+")) {
				pluMin.setButtonText("-");
			} else {
				pluMin.setButtonText("+");
			}
		}
		else if (exec == 71) {											// Pause menu.
			MenuWindow pauseMenu = new MenuWindow("Game Menu", (b.scr.getParent().windowWidth - 400) / 2, 0, 10);
			pauseMenu.setHeight(b.scr.getParent().windowHeight - 40);
			pauseMenu.setGridX(20);
			pauseMenu.setGridY(10);
			pauseMenu.setButtonGap(12);
			pauseMenu.setButtonWidth(pauseMenu.getWidth() - 40);
			pauseMenu.removeCloseButton();
			
			String[] titles = {"Return to Game", "-", "Players", "Stats", "History", "Options", "-", "Save Game", "Load Game", "Back to Menu"};
			int[] exes = {7, -1, 72, 73, 74, 75, -1, 76, 77, 78};
			String[] adds = {pauseMenu.getTitle(), "", "", "", "", "", "", "", "", ""};
			boolean[] vises = {true, false, true, true, true, true, false, true, true, true};
			Color[] colors = {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE};
			pauseMenu.addMenuButtons(titles, exes, adds, vises, colors);
			
			b.mim.addWindowFull(pauseMenu);
		}
		else if (exec == 72) {											// Open player window from pause menu.
			execute(5);
			execute(7, "Game Menu");
			b.mim.getWindow("Players").addReturnButton(79, "Players");
		}
		else if (exec == 73) {											// Open stats window from pause window.
			execute(6);
			execute(7, "Game Menu");
			b.mim.getWindow("Stats").addReturnButton(79, "Stats");
		}
		else if (exec == 74) {											// Open history window from pause window.
			
		}
		else if (exec == 75) {											// Open options window from pause window.
			
		}
		else if (exec == 76) {											// Open save game window from pause window.
			
		}
		else if (exec == 77) {											// Open load game window from pause window.
			
		}
		else if (exec == 78) {											// Return to menu button in pause menu.
			YesNoWindow soQuitYeah = new YesNoWindow("Return to the Main Menu?", Board.WINDOW_CENTER_X, 300);
			soQuitYeah.setYesExec(3);
			soQuitYeah.setYesAdd("Single Skirmish");
			soQuitYeah.setNoToClose();
			soQuitYeah.setContent("Are you sure you want to quit?\nAny unsaved progress will be lost.");
			soQuitYeah.setContentY(-3);
			b.mim.addWindowFull(soQuitYeah);
		}
		else if (exec == 79) {											// Reopen pause menu.
			execute(7, add);
			execute(71);
		}
		else {
			b.cmd.alert("ButtonExecutor: No entry for execution code " + Integer.toString(exec) + ".");
		}

	}

}
