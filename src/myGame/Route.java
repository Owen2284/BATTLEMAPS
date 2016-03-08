/*
 *  Route.java
 *
 *  Class used to represent a route between cities.
 *
 */

package myGame;

public class Route {

    // Fields
    private String[] points = new String[2];
    private String type = "Normal";

    // Constructor for routes if inputs are cities.
    public Route(City c1, City c2) {
        this.points[0] = c1.getName();
        this.points[1] = c2.getName();
    }

    // Constructor for routes if inputs are strings.
    public Route(String c1, String c2) {
        this.points[0] = c1;
        this.points[1] = c2;
    }

    // Accessors
    public String[] getPoints() {return this.points;}

    public String getStart() {return this.points[0];}

    public String getEnd() {return this.points[1];}

    // Returns one destination city if the argument is the other city.
    public String getDestination(String inCityName) {

        if (inCityName.equals(this.points[0])) {
            return this.points[1];
        } else if (inCityName.equals(this.points[1])) {
            return this.points[0];
        } else {
            return null; 
        }
  
    }

    // Returns true if route is connected to specified city name.
    public boolean isConnected(String inCityName) {

        if (inCityName.equals(this.points[0])) {
            return true;
        } else if (inCityName.equals(this.points[1])) {
            return true;
        } else {
            return false; 
        }
  
    }

    // Returns true if route is connected to both specified cities.
    public boolean isConnected(String inCityName1, String inCityName2) {

        if (inCityName1.equals(this.points[0]) && inCityName2.equals(this.points[1])) {
            return true;
        } else if (inCityName1.equals(this.points[1]) && inCityName2.equals(this.points[0])) {
            return true;
        } else {
            return false; 
        }
  
    }

    public String getType() {return this.type;}

    public boolean sharesCityWith(Route that) {

        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                if (this.points[i].equals(that.getPoints()[j])) {return true;}
            }
        }

        return false;

    }

    // Mutators
    public void setType(String in) {this.type = in;}

    // Display methods.
    public String toString() {
        return "Route from \"" + this.points[0] + "\" to \"" + this.points[1] + "\".";
    }

}