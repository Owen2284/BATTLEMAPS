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
import myMain.Board;

public class Game {

	// Fixed fields
	private String name = "Game";
	private Map map;
	private ActionHandler actor = new ActionHandler("data/actions.csv", new ActionScript());
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
	public int getActivePlayerNumber() {return playerWhoseGoItIs;}
	public Map getMap() {return this.map;}
	public String getVictoryCondition() {return this.victory;}
	public int getTurn() {return this.turnNumber;}
	public int getMaxTurns() {return this.maxTurns;}
	public boolean hasMaxTurns() {return this.maxTurns >= 0;}
	public ActionHandler getActionHandler() {return this.actor;}

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
	public int sumStatByPlayer(Player playerObj, String key) {return this.sumStatByPlayer(playerObj.getID(), key);}
	public PointSet getPointGain(Player player) {return this.map.getPointGain(player);}
	public int getPointGain(Player player, String key) {return getPointGain(player).get(key);}
	
    // Stats super-accessors.
    public int getStat(String in) {return this.stats.get(in);}    
    public Stats getAllStats() {return this.stats;}
 	public String highest(String[] cats) {return stats.highest(cats);}
 	public String lowest(String[] cats) {return stats.lowest(cats);}
 	public double average(String[] cats) {return stats.average(cats);}
 	
 	// ActionHandler accessors.
 	public ActionHandler getActor() {return this.actor;}
 	public boolean cityUnderAttack(City in) {return this.actor.cityUnderAttack(in);}
 	public boolean playerUnderAttack(Player in) {return this.actor.playerUnderAttack(in);}
 	
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
	public void nextPlayer(Board b) {
		// Move to the next player.
		this.updateEndPlayer();
		playerWhoseGoItIs += 1;
		// Check to see if current player still has cities/is in game.
		//if (!getActivePlayer().hasCities()) {
		// Increment turn counter if all players have acted.
		if (playerWhoseGoItIs > this.players.size()) {
			this.updateEndTurn();			
			this.incTurn();
			playerWhoseGoItIs = 1;
		}
		this.updateStartPlayer(b);
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
	
	// ActionHandler mutators.
	public void addAction(Action in) {this.actor.addAction(in);}
	
	// Updaters
	public void act() {
		// N/A
	}
	
	public void updateStartPlayer(Board b) {
		// Action processing.
		ArrayList<String> actionMessages = this.actor.updateStartPlayer(getActivePlayer());
		if (Board.DEBUG_TRACE && actionMessages.size() > 0) {
			for (String m : actionMessages) {
				if (m.substring(0,1).equals("#")) {
					b.createQuickWindow("Takeover Executed!", m.substring(1));
				}
			}
			System.out.println(actionMessages.toString());
		}
	}
	
	public void updateEndPlayer() {
		// City processing.
		for (City c : getCitiesOwnedBy(getActivePlayer().getID())) {
			c.endPlayerTurn();
		}
		// Action processing.
		this.actor.updateEndPlayer(getActivePlayer());
		// Setting player stats.
		for (Player p : this.players) {
			p.setStat("Cities owned", this.getCitiesOwnedBy(p.getName()).size());
		}
	}
	
	public void updateEndTurn() {
		// City processing.
		for (City c : getCities()) {
			c.endFullTurn();
		}
		// Action processing.
		this.actor.updateEndTurn();
		// Increasing player points.
		for (Player p : this.players) {
			p.getPointSet().add(getPointGain(p));
		}
	}
	
	// Overwrites
	public String toString() {
		return "Game with " + this.players.size() + " players on a " + this.map.getLength() + " by " + this.map.getWidth() + " sized map. The victory condition is set to \"" + this.victory + "\"";
	}


}