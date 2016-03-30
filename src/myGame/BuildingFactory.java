/*
 *	BuldingFactory.java
 *
 *	Produces the buildings for use by the game.
 *
 */

package myGame;

import myMain.Board;

import java.util.List;
import java.util.ArrayList;
import java.io.FileReader;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;

public class BuildingFactory {

	private ArrayList<Building> buildings;
	private int[] timesTaken;
	
	private final String[] theKeys = {"Residential", "Happiness", "Military", "Diplomacy", "Technology", "Commerce", "Nature", "Industry"};
	private final Color[] theColors = {new Color(200,200,200), new Color(234,242,10), new Color(194,2,50), new Color(235,237,175), new Color(10,242,231), new Color(83,74,240), new Color(17,153,42), new Color(245,155,66)};

	// Constructor
	public BuildingFactory() {
		
		// Loads in list of buildings.
		buildings = readBuildings("data/buildings.csv");

		// Creates the time taken array.
		timesTaken = new int[this.buildings.size()];
		for (int i = 0; i < this.buildings.size(); ++i) {
			timesTaken[i] = 0;
		}
	}

	// Private code.
	private ArrayList<Building> readBuildings(String in_csv) {

		// Declares list for returning the buildings.
		ArrayList<Building> returnList = new ArrayList<Building>();

		// Begins the reading process.
		try (BufferedReader br = new BufferedReader(new FileReader(in_csv))) {

			// Initialisation + skipping header.
			String line = br.readLine();
			int buildingNumber = 0;
			while ((line = br.readLine()) != null) {

				if (Board.DEBUG_LOAD) {System.out.println("Loading building:- " + line);}

				// Split the line into it's comma-separated parts.
				String[] splitLine = line.split(",");

				// Begin reading data for the building construction.
				++buildingNumber;
				String type = splitLine[0];
				String category = splitLine[1];
				String desc = splitLine[2].replace("|", ",");
				String blueprint = splitLine[3];
				int turns = (Integer.parseInt(splitLine[4])) * -1;
				int health = Integer.parseInt(splitLine[5]);
				int[] stats = {Integer.parseInt(splitLine[6]), Integer.parseInt(splitLine[7]), Integer.parseInt(splitLine[8]), Integer.parseInt(splitLine[9]), Integer.parseInt(splitLine[10]), Integer.parseInt(splitLine[11]), Integer.parseInt(splitLine[12]), Integer.parseInt(splitLine[13])};

				// Get building's color.
				Color color = Color.GRAY;
				for (int i = 0; i < theKeys.length; ++i) {
					if (theKeys[i].equals(category)) {
						color = new Color(theColors[i].getRed(), theColors[i].getGreen(), theColors[i].getBlue());
					}
				}
				
				// Construct building and add it to the return list.
				Building building = new Building(type, "BLD" + buildingNumber, type, category, desc, blueprint, turns, health, stats, color);
				returnList.add(building);

			}

		} catch (IOException e) {
			System.out.println(e);
		}

		// Return all of the buildings.
		return returnList;

	}

	// Accessors
	public Building getBuilding(String in) {
		for (Building bld : buildings) {
			if (bld.getType().equals(in)) {
				++timesTaken[buildings.indexOf(bld)];
				Building returnBuilding = new Building(bld);
				returnBuilding.setID(bld.getID() + "-" + IDCreate.generateID("", timesTaken[buildings.indexOf(bld)]));
				return returnBuilding;
			}
		}
		return null;
	}

	public List<String> getCategory(String in) {
		List<String> names = new ArrayList<String>();
		for (Building bld : buildings) {
			if (bld.getCategory().equals(in)) {names.add(bld.getType());}
		}
		return names;
	}
	
	public String[] getCategories() {return this.theKeys;}
	public Color[] getColors() {return this.theColors;}

	// Overrides
	public String toString() {
		String theString = "";
		for (Building bld : buildings) {
			theString += bld.toString() + "\n";
		}
		return theString;
	}

}