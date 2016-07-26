/*
 *  Game.java
 *
 *  Class that contains all of the data needed to run a game.
 *
 */

package myGame;

import java.awt.Rectangle;
import java.util.ArrayList;

import myData.Stats;

public class Game {

	// Fixed fields
	private String name = "Game";
	private Map map;
	private ArrayList<Player> players = new ArrayList<Player>();
	private String victory = "Conquest";
	private int maxTurns = -1;
	
	// Changeable fields
	private int turnNumber = 0;
	private int playerWhoseGoItIs = 1;
	
	// Stats module.
	private Stats stats = new Stats();

	// Base constructor.
	public Game() {
		// Nothing.
	}

	// Slightly more complicated constructor.
	public Game(boolean random) {
		if (random) {
			this.map = new Map();
		}
	}

	// Constructor for just a list of players. This creates a random map.
	public Game(String[][] inPlayerList) {

		// Creates all players and adds them to the internal player list.
		for (int i = 0; i < inPlayerList.length; ++i) {

			Player newPlayer = new Player(i + 1, inPlayerList[i][0], inPlayerList[i][1]);
			this.players.add(newPlayer);

		}

		// Creates default map.
		this.map = new Map();

	}

	// Constructor for a player list and a map.
	public Game(String[][] inPlayerList, Map inMap) {

		// Creates all players and adds them to the internal player list.
		for (int i = 0; i < inPlayerList.length; ++i) {

			Player newPlayer = new Player(i + 1, inPlayerList[i][0], inPlayerList[i][1]);
			this.players.add(newPlayer);

		}

		// Creates default map.
		this.map = new Map(inMap);

	}

	// Constructor for a player list and a map template.
	public Game(String[][] inPlayerList, MapTemplate inMapTemplate) {

		// Creates all players and adds them to the internal player list.
		for (int i = 0; i < inPlayerList.length; ++i) {

			Player newPlayer = new Player(i + 1, inPlayerList[i][0], inPlayerList[i][1]);
			this.players.add(newPlayer);

		}

		// Creates default map.
		this.map = new Map(inMapTemplate);

	}

	// Constructor for a map.
	public Game(Map inMap) {

		// Creates default map.
		this.map = new Map(inMap);

	}

	// Constructor for a map template.
	public Game(MapTemplate inMapTemplate) {

		// Creates default map.
		this.map = new Map(inMapTemplate);

	}

	// Constructor for an ArrayList of players, a map, and a victory condition.
	public Game(ArrayList<Player> inPlayerList, Map inMap, String inVictory) {

		// Creates all players and adds them to the internal player list.
		for (int i = 0; i < inPlayerList.size(); ++i) {

			Player newPlayer = new Player(inPlayerList.get(i));
			this.players.add(newPlayer);

		}

		// Creates default map.
		this.map = new Map(inMap);

		// Sets victory condition.
		this.victory = new String(inVictory);

	}

	// Constructor for an ArrayList of players, a map template, and a victory condition.
	public Game(ArrayList<Player> inPlayerList, MapTemplate inMapTemplate, String inVictory) {

		// Creates all players and adds them to the internal player list.
		for (int i = 0; i < inPlayerList.size(); ++i) {

			Player newPlayer = new Player(inPlayerList.get(i));
			this.players.add(newPlayer);

		}

		// Creates default map.
		this.map = new Map(inMapTemplate);

		// Sets victory condition.
		this.victory = new String(inVictory);
	}

	// Constructor for an ArrayList of players, all map arguments, and a victory condition.
	public Game(ArrayList<Player> inPlayerList, int inCityCount, int inLength, int inWidth, String inVictory) {

		// Creates all players and adds them to the internal player list.
		for (int i = 0; i < inPlayerList.size(); ++i) {

			Player newPlayer = new Player(inPlayerList.get(i));
			this.players.add(newPlayer);

		}

		// Creates default map.
		this.map = new Map(inCityCount, inLength, inWidth);

		// Sets victory condition.
		this.victory = new String(inVictory);

	}

	// Private construction code.


	// Accessors
	public ArrayList<Player> getPlayers() {return this.players;}

	public Player getPlayerByID(String inID) {
		
		for (int i = 0; i < this.players.size(); ++i) {
			if (this.players.get(i).getID().equals(inID)) {
				return this.players.get(i);
			}
		}

		return null;

	}

	public String getName() {return this.name;}
	public Player getActivePlayer() {return players.get(this.playerWhoseGoItIs - 1);}
	public Map getMap() {return this.map;}
	public String getVictoryCondition() {return this.victory;}
	public int getTurn() {return this.turnNumber;}
	public int getMaxTurns() {return this.maxTurns;}
	public boolean hasMaxTurns() {return this.maxTurns >= 0;}

	// Map super-accessors
	public ArrayList<City> getCities() {return this.map.getCities();}
	public City getCityByName(String cityName) {return this.map.getCityByName(cityName);}
	public City getCityByID(String cityID) {return this.map.getCityByID(cityID);}
	public Rectangle getScrolledBoundsName(String inName) {return this.map.getScrolledBoundsName(inName);}
	public ArrayList<Route> getRoutes() {return this.map.getRoutes();}
	public ArrayList<Route> getRoutesFromName(String cityName) {return this.map.getRoutesFromName(cityName);}
	public ArrayList<Route> getRoutesFromID(String cityID) {return this.map.getRoutesFromID(cityID);}
	public ArrayList<City> getNeighboursOf(String city_name) {return this.map.getNeighboursOf(city_name);}
	public int getLength() {return this.map.getLength();}
	public int getWidth() {return this.map.getWidth();}
	public ArrayList<String> getDebugLog() {return this.map.getDebugLog();}
	public boolean isDRD() {return this.map.isDRD();}
	public boolean isUniqueName(String in) {return this.map.isUniqueName(in);}
	public ArrayList<City> getCitiesOwnedBy(String player_id) {return this.map.getCitiesOwnedBy(player_id);}
	public int sumStatByPlayer(String player_id, String key) {return this.map.sumStatByPlayer(player_id, key);}
	
    // Stats super-accessors.
    public int getStat(String in) {return this.stats.get(in);}    
    public Stats getAllStats() {return this.stats;}
 	public String highest(String[] cats) {return stats.highest(cats);}
 	public String lowest(String[] cats) {return stats.lowest(cats);}
 	public double average(String[] cats) {return stats.average(cats);}

	// Mutators
 	public void setName(String in) {this.name = in;}
	public void addPlayer(Player inPlayer) {this.players.add(inPlayer);}
	public void setMap(Map inMap) {this.map = new Map(inMap);}
	public void setMap(MapTemplate inMap) {this.map = new Map(inMap);}
	public void setVictoryCondition(String inVictory) {this.victory = new String(inVictory);}
	public void setTurn(int inTurn) {this.turnNumber = inTurn;}
	public void incTurn() {this.turnNumber += 1;}
	public void decTurn() {this.turnNumber -= 1;}
	public void setMaxTurns(int in) {this.maxTurns = in;}
	public void nextPlayer() {
		// Move to the next player.
		this.updateEndPlayer();
		playerWhoseGoItIs += 1;
		// TODO: Check if player is still in game.
		// Increment turn counter if all players have acted.
		if (playerWhoseGoItIs > this.players.size()) {
			this.updateEndTurn();			
			this.incTurn();
			playerWhoseGoItIs = 1;
		}
	}

	// Map super-mutators.
	public void addCity(City inCity) {this.map.addCity(inCity);}
	public void addRoute(City routeStart, City routeEnd) {this.map.addRoute(routeStart, routeEnd);}
	public void addRoute(Route inRoute) {this.map.addRoute(inRoute);}
	public void setDRD(boolean in) {this.map.setDRD(in);}	// Sets Debug Route Display.
	public void toggleDRD() {this.map.toggleDRD();}			// Toggles Debug Route Display.
	public void updateName(String oldS, String newS) {this.map.updateName(oldS, newS); incStat("Name changes", 1);}
	
	// Stats super-mutators.
	public void setStat(String in, int val) {stats.set(in, val);}
	public void incStat(String in, int val) {stats.inc(in, val);}
	public void decStat(String in, int val) {stats.dec(in, val);}
	
	// Updater
	public void updateConstant() {
		// N/A
	}
	
	public void updateEndPlayer() {
		// Setting player stats.
		for (Player p : this.players) {
			p.setStat("Cities owned", this.getCitiesOwnedBy(p.getName()).size());
		}
	}
	
	public void updateEndTurn() {
		// N/A
	}
	
	// Overwrites
	public String toString() {
		return "Game with " + this.players.size() + " players on a " + this.map.getLength() + " by " + this.map.getWidth() + " sized map. The victory condition is set to \"" + this.victory + "\"";
	}


}