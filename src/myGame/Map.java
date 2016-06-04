/*
 *  Map.java
 *
 *  Class used to represent a game map.
 *
 */

package myGame;

import java.util.ArrayList;
import java.util.Random;

import myData.MyArrays;
import myMain.Board;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

public class Map {

	private ArrayList<City> cities = new ArrayList<City>();
	private ArrayList<Route> routes = new ArrayList<Route>();
	private int length;
	private int width;
	private int border_size = 0;
	
	private int scrollX = 0;
	private int scrollY = 0;
	
	private ArrayList<Land> lands = new ArrayList<Land>();

	private ArrayList<String> debug_log = new ArrayList<String>();
	private ArrayList<Route> debug_routes;
	private boolean show_debug_routes = false;
	
	private final static int POINTCOUNT = 12;
	private final static int LANDMIN = (int) (1.2 * (double) City.CITY_SIZE);
	private final static int LANDMAX = (int) (2.4 * (double) City.CITY_SIZE);
	private final static int SMOOTHNESSCOUNT = 1;
	private final static double SMOOTHNESSRATE = 0.0;

	// Default constructor.
	public Map() {
		this(10, 2000, 2000, 20, new Random(), POINTCOUNT, LANDMIN, LANDMAX, SMOOTHNESSCOUNT, SMOOTHNESSRATE);
	}

	// Constructor for number of cities.
	public Map(int cityCount) {
		this(cityCount, 2000, 2000, 20, new Random(), POINTCOUNT, LANDMIN, LANDMAX, SMOOTHNESSCOUNT, SMOOTHNESSRATE);
	}

	// Constructor for number of cities, and map dimensions.
	public Map(int cityCount, int inLength, int inWidth) {
		this(cityCount, inLength, inWidth, 20, new Random(), POINTCOUNT, LANDMIN, LANDMAX, SMOOTHNESSCOUNT, SMOOTHNESSRATE);
	}

	// Constructor for number of cities, map dimansions, and a map border.
	public Map(int cityCount, int inLength, int inWidth, int inBorder) {
		this(cityCount, inLength, inWidth, inBorder, new Random(), POINTCOUNT, LANDMIN, LANDMAX, SMOOTHNESSCOUNT, SMOOTHNESSRATE);
	}

	// Constructor for number of cities, map dimensions, and a map border.
	public Map(int cityCount, int inLength, int inWidth, int inBorder, Random in_random) {
		this(cityCount, inLength, inWidth, inBorder, in_random, POINTCOUNT, LANDMIN, LANDMAX, SMOOTHNESSCOUNT, SMOOTHNESSRATE);
	}
	
	// Constructor for all of the above, plus terraforming values.
	public Map(int cityCount, int inLength, int inWidth, int inBorder, Random in_random,
			int pointCount, int landMax, int landMin, int smoothnessCount, double smoothnessRate) {
		long start = System.currentTimeMillis();
		this.length = inLength;
		this.width = inWidth;
		this.border_size = inBorder;
		this.cities = randomCities(cityCount, length, width, in_random);
		this.routes = randomRoutes();
		terraformRandom(pointCount, landMax, landMin, smoothnessCount, smoothnessRate, in_random);
		debug_log.add("Final map is:");
		for (String line: this.toString().split("\n")) {
			debug_log.add(line);
		}
		long end = System.currentTimeMillis();
		debug_log.add("Time taken to construct: " + (end - start) + "ms.");
		this.scrollX = this.border_size;
		this.scrollY = this.border_size;
	}

	// Constructor for an input map.
	@SuppressWarnings("unchecked")
	public Map(Map that) {
		this.length = that.getLength();
		this.width = that.getWidth();
		this.cities = (ArrayList<City>)that.getCities().clone();
		this.routes = (ArrayList<Route>)that.getRoutes().clone();
		this.lands = that.getLand();
		this.border_size = that.border_size;
		this.debug_log = (ArrayList<String>)that.getDebugLog().clone();
		boolean initial = that.isDRD();
		that.setDRD(true);
		this.debug_routes = (ArrayList<Route>)that.getRoutes().clone();
		that.setDRD(initial);
	}

	// Constructor for map templates.
	public Map(MapTemplate inMap) {
		// TODO: Good luck with this.
	}

	// Private construction code.
	private ArrayList<City> randomCities(int cityNumber, int mapLength, int mapWidth, Random in_x) {

		// Variable declaration.
		Random x = in_x;
		NameGenerator nameGen = new NameGenerator();
		nameGen.setRandomGenerator(x);
		ArrayList<City> returnVector = new ArrayList<City>();

		// Generate all cities.
		debug_log.add("Begin adding cities to map:");
		for (int i = 0; i < cityNumber; ++i) {
			// Creates the city.
			String tempID = IDCreate.generateID("C", i + 1);
			String aName = nameGen.newName(2);
			while (!this.uniqueName(aName, returnVector)) {
				aName = nameGen.newName(2);
			}
			
			// Performs city distance check.
			Point thisCity = new Point();
			boolean isSpacedOut = true; 
			do {
				thisCity.setLocation(x.nextInt(mapLength - (2*border_size)) + border_size + 1, x.nextInt(mapWidth - (2*border_size)) + border_size + 1);
				isSpacedOut = true;               
				if (!returnVector.isEmpty()) {
					for (int j = 0; j < returnVector.size(); ++j) {
						Point checkCity = new Point(returnVector.get(j).getX(), returnVector.get(j).getY());
						double distance = thisCity.distance(checkCity);
						if ((distance < 100)) {
							isSpacedOut = false;
						}
					}
				}
			} while(!isSpacedOut);

			// Adds city to the city ArrayList.
			City newCity = new City(tempID, (int) thisCity.getX(), (int) thisCity.getY(), aName, (10 * (x.nextInt(1) + 1)), (10 * (x.nextInt(1) + 1)));
			returnVector.add(newCity);
			debug_log.add("  Added the city of " + aName + "!");

		}
		debug_log.add("End adding cities.");
		debug_log.add("");

		return returnVector;

	}

	@SuppressWarnings("unchecked")
	private ArrayList<Route> randomRoutes() {

		int arraySize = 0;
		for (int k = 0; k < this.cities.size(); ++k) {
			arraySize += k;
		}
		String[][] allRoutes = new String[arraySize][3];
		int routeTotal = 0; 
		ArrayList<Route> returnVector = new ArrayList<Route>();

		// Create list of each possible route with it's distance.
		for (int i = 0; i < this.cities.size(); ++i) {
			for (int j = i; j < this.cities.size(); ++j) {
				// Does not store if the city is the same.
				if (i != j) {
					// Stores route points.
					allRoutes[routeTotal][0] = this.cities.get(i).getName();
					allRoutes[routeTotal][1] = this.cities.get(j).getName();
					// Determine distance between cities.
					Point p1 = new Point(this.cities.get(i).getX(), this.cities.get(i).getY());
					Point p2 = new Point(this.cities.get(j).getX(), this.cities.get(j).getY());
					Double distance = p1.distance(p2);
					// Stores that distance.
					allRoutes[routeTotal][2] = distance.toString();
					// Increases route total.
					++routeTotal;
				}
			}
		}

		// Add first X routes in the list.
		debug_log.add("Begin adding routes to map:");
		while ((!isFullyConnected(returnVector)) && (returnVector.size() < this.cities.size() * this.cities.size())) {
			debug_log.add("  Not connected at " + returnVector.size() + " routes.");
			for (int l = 0; l < 5; ++l) {
				// Finds shortest available route.
				int shortest = 0;
				while (Double.parseDouble(allRoutes[shortest][2]) <= 0) {
					Route tempRoute = new Route(this.getCityByName(allRoutes[shortest][0]), this.getCityByName(allRoutes[shortest][1]));
					debug_log.add("  INITIAL VALUE TOO SHORT: " + tempRoute.toString() + " : " + Double.toString(Double.parseDouble(allRoutes[shortest][2])));
					shortest += 1; 
				}
				for (int m = 1; m < arraySize; ++m) {
					if ( (Double.parseDouble(allRoutes[m][2]) < Double.parseDouble(allRoutes[shortest][2])) && (Double.parseDouble(allRoutes[m][2]) > 0) ) {
						shortest = m;
					}
				}
				// Adds shortest route to return vector.
				Route newRoute = new Route(this.getCityByName(allRoutes[shortest][0]), this.getCityByName(allRoutes[shortest][1]));
				returnVector.add(newRoute);
				debug_log.add("  ADDING: " + newRoute.toString() + " It's distance is " + Double.toString(Double.parseDouble(allRoutes[shortest][2])));
				// Sets entry as assigned.
				allRoutes[shortest][2] = Double.toString(-1.0 * Double.parseDouble(allRoutes[shortest][2]));
			} 
		}
		debug_log.add("  Connected at " + returnVector.size() + " routes.");
		debug_log.add("End adding routes.");
		debug_log.add("");

		debug_log.add("Storing current set of routes as debug routes.");
		debug_routes = (ArrayList<Route>)returnVector.clone();
		debug_log.add("");

		// Try and remove as many intersections as possible.
		debug_log.add("Begin intersection testing on map:");
		returnVector = this.intersectionTestingCity(returnVector);
		returnVector = this.intersectionTestingRoute(returnVector);
		debug_log.add("End intersection testing.");
		debug_log.add("");

		// Return routes.
		return returnVector;

	}

	private boolean isFullyConnected(ArrayList<Route> inRoutes) {

		// Variable declaration.
		ArrayList<String> cityQueue = new ArrayList<String>();
		ArrayList<String> connectedCities = new ArrayList<String>();

		// Pick starting city (e.g. first in array)
		cityQueue.add(this.cities.get(0).getName());
		connectedCities.add(this.cities.get(0).getName());

		// Run through all cities in the city queue.
		int queuePos = 0;
		while (cityQueue.size() > queuePos) {

			// Checks for all destinations from current city.
			for (int i = 0; i < inRoutes.size(); ++i) {
				String destination = inRoutes.get(i).getDestination(cityQueue.get(queuePos));
				// Adds city to queue and connected array if not already present.
				if ((destination != null) && (!cityQueue.contains(destination))) {
					cityQueue.add(destination);
					connectedCities.add(destination);
				}
			}
			++queuePos;
		}

		// If length of connected array equals the length of the City array, then the map is connected.
		if (connectedCities.size() == this.cities.size()) {		
			return true;
		} else {
			return false;
		}

	}

	// Checks to see if inName matches a name of a city in inCities.
	private boolean uniqueName(String inName, ArrayList<City> inCities) {

		if (!inCities.isEmpty()) {
			for (int i = 0; i < inCities.size(); ++i) {
				if (inCities.get(i).getName().equals(inName)) {
					return false;
				}
			}
		}

		return true;

	}
	
	// Checks the routes and removes any with overlapping paths.
	@SuppressWarnings("unchecked")
	private ArrayList<Route> intersectionTestingCity(ArrayList<Route> in) {
		
		// Clone initial route list.
		ArrayList<Route> full = (ArrayList<Route>)in.clone();

		// Calculate initial overlap points.
		int overs = intersectionCounterCity(full, this.cities);
		debug_log.add("  Initial route-city overlaps = " + overs);

		// Exits if there are no collisions to begin with.
		if (overs == 0) {
			return full;
		}
		// If greater than 0 collisions, begin evaluation of collisions.

		// Begin by running through each route and city.
		for (int i = 0; i < full.size(); ++i) {
			Route r = full.get(i);											// Get route.
			Line2D.Float l = r.createRouteLine();							// Converts the route into a usable line.
			for (int j = 0; j < this.cities.size(); ++j) {
				City c = this.cities.get(j);								// Get city.
				Rectangle g = c.getBounds();								// Converts the city into a rectangle.
				if (g.intersectsLine(l) && (!r.isConnected(c.getName()))) {	// Check for overlaps.
					full.remove(r);											// Removes r.
					debug_log.add("  Removed r" + r.toString().substring(1, r.toString().length()));
					if (!isFullyConnected(full)) {							// Checks if the routes still connect all cities.
						full.add(r);
						debug_log.add("  Previous route removal broke an important connection; restoring the removed route.");
					}
				}
			}
		}

		// Check how many collisions remain.
		overs = intersectionCounterCity(full, this.cities);
		debug_log.add("  Final route-city overlaps = " + overs);

		// Return final list.
		return full;

	}

	private int intersectionCounterCity(ArrayList<Route> inR, ArrayList<City> inC) {
		int collisions = 0;
		for (int i = 0; i < inR.size(); ++i) {
			Route r = inR.get(i);											// Get route.
			Line2D.Float l = r.createRouteLine();							// Converts the route into a usable line.
			for (int j = 0; j < inC.size(); ++j) {
				City c = inC.get(j);										// Get city.
				Rectangle g = c.getBounds();								// Converts the city into a rectangle.
				if (g.intersectsLine(l) && (!r.isConnected(c.getName()))) {	// Check for overlaps.
					collisions += 1;
				}
			}
		}
		return collisions;
	}

	// Checks the routes and removes any with overlapping paths.
	@SuppressWarnings("unchecked")
	private ArrayList<Route> intersectionTestingRoute(ArrayList<Route> in) {
		
		// Clone initial route list.
		ArrayList<Route> full = (ArrayList<Route>)in.clone();

		// Calculate initial overlap points.
		int overs = intersectionCounterRoute(full);
		debug_log.add("  Initial route-route overlaps = " + overs);

		// Exits if there are no overlaps to begin with.
		if (overs == 0) {
			return full;
		}
		// If greater than 0 overlaps, begin evaluation of overlaps.

		// Begin by running through each route combination.
		for (int i = 0; i < full.size(); ++i) {
			Route r1 = full.get(i);											// Get first route.
			Line2D.Float l1 = r1.createRouteLine();						// Converts the route into a usable line.
			for (int j = i + 1; j < full.size(); ++j) {
				Route r2 = full.get(j);										// Get second route.
				Line2D.Float l2 = r2.createRouteLine();					// Converts the route into another usable line.
				if (l1.intersectsLine(l2) && (!r1.sharesCityWith(r2))) {	// Check for overlaps.
					full.remove(r2);										// Removes r2, the longer route. (ArrayList is pre-sorted by length)
					debug_log.add("  Removed r" + r2.toString().substring(1, r2.toString().length()));
					if (!isFullyConnected(full)) {							// Checks if the routes still connect all cities.
						full.add(r2);
						debug_log.add("  Previous route removal broke an important connection; restoring the removed route.");
					}
				}
			}
		}

		// Check how many overlaps remain.
		overs = intersectionCounterRoute(full);
		debug_log.add("  Final route-route overlaps = " + overs);

		// Return final list.
		return full;

	}

	private int intersectionCounterRoute(ArrayList<Route> in) {
		int overlaps = 0;
		for (int i = 0; i < in.size(); ++i) {
			Route r1 = in.get(i);											// Get first route.
			Line2D.Float l1 = r1.createRouteLine();						// Converts the route into a usable line.
			for (int j = i + 1; j < in.size(); ++j) {
				Route r2 = in.get(j);										// Get second route.
				Line2D.Float l2 = r2.createRouteLine();					// Converts the route into another usable line.
				if (l1.intersectsLine(l2) && (!r1.sharesCityWith(r2))) {	// Check for overlaps.
					overlaps += 1;
				}
			}
		}
		return overlaps;
	}
	
	// Code that creates the graphical land below the cities.
	@SuppressWarnings("unchecked")
	private void terraformRandom(int pointCount, int landMin, int landMax, int smoothingCount, double smoothingRate, Random r) {
		
		debug_log.add("Begin 'random' terraforming.");
		debug_log.add(" Generating land...");
		
		ArrayList<Land> tempLand = new ArrayList<Land>();
		
		// Generates land masses.
		for (City c : this.cities) {
			
			// Get the centered city coordinates.
			int centerX = c.getX() + (City.CITY_SIZE / 2); 
			int centerY = c.getY() + (City.CITY_SIZE / 2);
			Land newLand = new Land(centerX, centerY, pointCount, landMin, landMax, smoothingCount, smoothingRate, r);
			tempLand.add(newLand);
			debug_log.add("  New terrain: " + MyArrays.arrayToString(newLand.getTerrain().xpoints, ",", "[", "]") + ", " + MyArrays.arrayToString(newLand.getTerrain().ypoints, ",", "[", "]") + ", " + (pointCount * smoothingCount) + ".");
		
		}
		
		// Merges neighbouring land masses.
		debug_log.add(" Merging land...");
		ArrayList<Land> finalLand = (ArrayList<Land>) tempLand.clone();
		boolean run = true;
		while (run) {
			run = false;
			for (int i = 0; i < tempLand.size(); ++i) {
				Land a = tempLand.get(i);
				if ((finalLand.contains(a))) {
					for (int j = i + 1; j < tempLand.size(); ++j) {
						Land b = tempLand.get(j);
						if ((a.overlaps(b)) && (!a.equals(b)) && (finalLand.contains(b))) {
							a.merge(b);
							finalLand.remove(b);
							debug_log.add("  Merging land " + i + " and " + j + "...");
							run = true;
						}
					}
				}
			}
		}
		
		this.lands = finalLand;
		
		debug_log.add("End terraforming.");
		
	}

	public int getScrollX() {
		return scrollX;
	}
	
	public void setScrollX(int in) {
		this.scrollX = in;
	}

	public void incScrollX(int in) {
		this.scrollX += in;
	}

	public int getScrollY() {
		return scrollY;
	}
	
	public void setScrollY(int in) {
		this.scrollY = in;
	}

	public void incScrollY(int in) {
		this.scrollY += in;
	}

	public ArrayList<City> getCities() {return this.cities;}

	public City getCityByName(String cityName) {

		if (!this.cities.isEmpty()) {
			for (int i = 0; i < this.cities.size(); ++i) {
				if (this.cities.get(i).getName().equals(cityName)) {
					return this.cities.get(i);
				}
			}
		}

		return null;
	}

	public City getCityByID(String cityID) {

		if (!this.cities.isEmpty()) {
			for (int i = 0; i < this.cities.size(); ++i) {
				if (this.cities.get(i).getID().equals(cityID)) {
					return this.cities.get(i);
				}
			}
		}

		return null;
	}
	
	public Rectangle getScrolledBoundsName(String inName) {
		City target = getCityByName(inName);
		if (target != null) {
			return new Rectangle(target.getX() + this.scrollX, target.getY() + this.scrollY, City.CITY_SIZE, City.CITY_SIZE);
		} else {
			if (Board.DEBUG_ERROR) {System.out.println("ERROR: Map, getScrolledBoundsName(" + inName+ ") could not find a city.");}
			return null;
		}
	}

	public ArrayList<Route> getRoutes() {
		if (!this.show_debug_routes) {
			return this.routes;
		} else {
			return this.debug_routes;
		}
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Route> getRoutesFromName(String cityName) {
		
		ArrayList<Route> returnVector = new ArrayList<Route>();
		ArrayList<Route> routes_to_use;

		if (!this.show_debug_routes) {
			routes_to_use = (ArrayList<Route>)routes.clone();
		} else {
			routes_to_use = (ArrayList<Route>)debug_routes.clone();
		}

		if (!routes_to_use.isEmpty()) {
			for (int i = 0; i < routes_to_use.size(); ++i) {
				if (routes_to_use.get(i).isConnected(cityName)) {
					returnVector.add(routes_to_use.get(i));
				}
			}
		}

		return returnVector;

	}

	public ArrayList<Route> getRoutesFromID(String cityID) {return this.getRoutesFromName(this.getCityByID(cityID).getName());}

	public ArrayList<City> getNeighboursOf(String city_name) {

		ArrayList<Route> neighbour_routes = this.getRoutesFromName(city_name);

		ArrayList<City> neighbours = new ArrayList<City>();

		for (Route r : neighbour_routes) {
			String dest = r.getDestination(city_name);
			City neighbour = getCityByName(dest);
			neighbours.add(neighbour);
		}

		return neighbours;

	}

	public int getLength() {return this.length;}

	public int getWidth() {return this.width;}
	
	public int getBorder() {return this.border_size;}

	public ArrayList<String> getDebugLog() {return this.debug_log;}

	public boolean isDRD() {return show_debug_routes;}

	public ArrayList<City> getCitiesOwnedBy(String player_id) {

		ArrayList<City> return_vector = new ArrayList<City>();
		
		for (City city : this.cities) {
			if (city.getOwner().equals(player_id)){
				return_vector.add(city);
			}
		}
		
		return return_vector;
	
	}

	public int sumStatByPlayer(String player_id, String key) {
		
		int total = 0;
		
		for (City city : this.getCitiesOwnedBy(player_id)) {
			total += city.getStat(key);
		}

		return total;
	
	}
	
	public boolean isOverLand(Point p) {
		boolean ol = false;
		for (Land l : this.lands) {
			if (l.getTerrain().contains(p)) {
				ol = true;
			}
		}
		return ol;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Land> getLand() {
		return (ArrayList<Land>) this.lands.clone();
	}

	// Mutators
	public void addCity(City inCity) {
		this.cities.add(inCity);
	}

	public void addRoute(City routeStart, City routeEnd) {
		this.routes.add(new Route(routeStart, routeEnd));
	}

	public void addRoute(Route inRoute) {
		this.routes.add(inRoute);
	}
	
	public void updateName(String oldS, String newS) {
		City theCity = this.getCityByName(oldS);
		ArrayList<Route> theRoutes = this.getRoutesFromName(theCity.getName());
		theCity.setName(newS);
		for (Route r : theRoutes) {
			String[] p = r.getCities();
			for (int i = 0; i < p.length; ++i) {
				if (p[i].equals(oldS)) {
					p[i] = newS;
				}
			}
		}
	}

	public void setDRD(boolean in) {
		// Sets Debug Route Display.
		this.show_debug_routes = in;
	}

	public void toggleDRD() {
		// Toggles Debug Route Display.
		this.show_debug_routes = !this.show_debug_routes;
	}

	// Validation.
	public boolean isUniqueName(String inName) {

		if (!this.cities.isEmpty()) {
			for (int i = 0; i < this.cities.size(); ++i) {
				if (this.cities.get(i).getName().equals(inName)) {
					return false;
				}
			}
		}

		return true;

	}

	// Transformations
	@Deprecated
	public Point[] convertRouteToPoints(Route r) {
		
		// Why the fuck didn't you store routes as points.

		// Initialises return array.
		Point[] points = new Point[2];

		// Gets the cities of the routes.
		City c1 = getCityByName(r.getCities()[0]);
		City c2 = getCityByName(r.getCities()[1]);

		// Sets points of the route based on city size constant.
		points[0] = new Point(c1.getX() + City.CITY_SIZE / 2, c1.getY() + City.CITY_SIZE / 2);
		points[1] = new Point(c2.getX() + City.CITY_SIZE / 2, c2.getY() + City.CITY_SIZE / 2);

		// Returns the points.
		return points;

	}

	@Deprecated
	public Line2D.Float createRouteLine(Point[] p) {

		// Get points and coordinates.
		Point p1 = p[0];	Point p2 = p[1];
		float r1x = p1.x;	float r2x = p2.x;
		float r1y = p1.y;	float r2y = p2.y;

		// Shorten line by minimal amount.
		if (r1x < r2x) {r1x += 1; r2x -= 1;}
		if (r1x > r2x) {r1x -= 1; r2x += 1;}
		if (r1y < r2y) {r1y += 1; r2y -= 1;}
		if (r1y > r2y) {r1y -= 1; r2y += 1;}

		// Create the line and return.
		return new Line2D.Float(r1x, r1y, r2x, r2y);

	}

	@Deprecated
	public Line2D.Float createRouteLine(Route r) {
		return this.createRouteLine(this.convertRouteToPoints(r));
	}

	// ToString override.
	public String toString() {

		String toFormat = "Map of size " + this.getLength() + " by " + this.getWidth() + ". It contains:- \n \n ";
		toFormat += this.cities.size() + " Cities:\n ";
		if (!this.cities.isEmpty()) {
			for (int i = 0; i < (this.cities.size()); ++i) {
				toFormat += "    " + this.cities.get(i).toString() + " \n ";
			}
		}
		toFormat += " \n ";
		toFormat += this.routes.size() + " Routes:\n ";
		if (!this.routes.isEmpty()) {
			for (int j = 0; j < (this.routes.size()); ++j) {
				toFormat += "    " + this.routes.get(j).toString() + " \n ";
			}
		}
		return toFormat;
	}

}