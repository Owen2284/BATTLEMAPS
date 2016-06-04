/*
 *  Route.java
 *
 *  Class used to represent a route between cities.
 *
 */

package myGame;

import java.awt.Point;
import java.awt.geom.Line2D;

public class Route {

    // Fields
    private String[] cities = new String[2];
    private Point[] points = new Point[2];
    private String type = "Normal";

    // Constructor for routes if inputs are cities.
    public Route(City c1, City c2) {
        this.cities[0] = c1.getName();
        this.cities[1] = c2.getName();
        this.points[0] = new Point(c1.getX() + (City.CITY_SIZE / 2), c1.getY() + (City.CITY_SIZE / 2));
        this.points[1] = new Point(c2.getX() + (City.CITY_SIZE / 2), c2.getY() + (City.CITY_SIZE / 2));
    }

    // Accessors
    public String[] getCities() {return this.cities;}
    public Point[] getPoints() {return this.points;}

    public String getStartCity() {return this.cities[0];}
    public String getEndCity() {return this.cities[1];}
    
    public Point getStartPoint() {return this.points[0];}
    public Point getEndPoint() {return this.points[1];}

    // Returns one destination city if the argument is the other city.
    public String getDestination(String inCityName) {

        if (inCityName.equals(this.cities[0])) {
            return this.cities[1];
        } else if (inCityName.equals(this.cities[1])) {
            return this.cities[0];
        } else {
            return null; 
        }
  
    }

    // Returns true if route is connected to specified city name.
    public boolean isConnected(String inCityName) {

        if (inCityName.equals(this.cities[0])) {
            return true;
        } else if (inCityName.equals(this.cities[1])) {
            return true;
        } else {
            return false; 
        }
  
    }

    // Returns true if route is connected to both specified cities.
    public boolean isConnected(String inCityName1, String inCityName2) {

        if (inCityName1.equals(this.cities[0]) && inCityName2.equals(this.cities[1])) {
            return true;
        } else if (inCityName1.equals(this.cities[1]) && inCityName2.equals(this.cities[0])) {
            return true;
        } else {
            return false; 
        }
  
    }

    public String getType() {return this.type;}

    public boolean sharesCityWith(Route that) {
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                if (this.cities[i].equals(that.getCities()[j])) {return true;}
            }
        }
        return false;
    }
    
    public Line2D.Float createRouteLine() {

		// Get points and coordinates.
		Point p1 = points[0];	Point p2 = points[1];
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

    // Mutators
    public void setType(String in) {this.type = in;}

    // Display methods.
    public String toString() {
        return "Route from \"" + this.points[0] + "\" to \"" + this.points[1] + "\".";
    }

}