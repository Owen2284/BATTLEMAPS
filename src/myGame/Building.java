/*
 *	Building.java
 *
 *	Class used to represent a building in a city.
 */

package myGame;

import myMain.Board;

import java.awt.Color;
import java.util.ArrayList;

public class Building {

	private String name;
	private String id;
	private final String type;
	private final String category;
	private String status;
	private final String description;
	private boolean[][] blueprint;
	private int build_time;
	private final int max_health;
	private int current_health;
	private final PointSet points;
	private int x = -1;
	private int y = -1;
	private Color color;

	public static final String[] KEYS = {"Population", "Happiness", "Military", "Technology", "Nature", "Diplomacy", "Commerce", "Industry"};

	// Constructors.
	public Building(String in_name, String in_id, String in_type, String in_category, String in_description, String in_blueprint_string, int in_initial_build_time, int in_max_health, int[] stat_arr) {

		// Assigning variable values.
		this.name = in_name;
		this.id = in_id;
		this.type = in_type;
		this.category = in_category;
		this.status = "Normal";
		this.description = in_description;
		this.build_time = in_initial_build_time;
		this.max_health = in_max_health;
		this.current_health = in_max_health;

		// Constructing blueprint.		(String format = "2x3:011110")
		this.blueprint = createBlueprint(in_blueprint_string);

		// Constructing point set.
		String[] readKeys = {"Military", "Technology", "Nature", "Diplomacy", "Commerce", "Industry", "Population", "Happiness"};
		this.points = new PointSet();
		for (int i = 0; i < readKeys.length; ++i) {
			points.inc(readKeys[i], stat_arr[i]);
		}

	}
	
	public Building(String in_name, String in_id, String in_type, String in_category, String in_description, String in_blueprint_string, int in_initial_build_time, int in_max_health, int[] stat_arr, Color in_col) {

		// Assigning variable values.
		this.name = in_name;
		this.id = in_id;
		this.type = in_type;
		this.category = in_category;
		this.status = "Normal";
		this.description = in_description;
		this.build_time = in_initial_build_time;
		this.max_health = in_max_health;
		this.current_health = in_max_health;
		this.color = in_col;

		// Constructing blueprint.		(String format = "2x3:011110")
		this.blueprint = createBlueprint(in_blueprint_string);

		// Constructing stats hash map.
		String[] readKeys = {"Military", "Technology", "Nature", "Diplomacy", "Commerce", "Industry", "Population", "Happiness"};
		this.points = new PointSet();
		for (int i = 0; i < readKeys.length; ++i) {
			points.set(readKeys[i], stat_arr[i]);
		}

	}

	public Building(Building that) {

		// Copying normally accessible data.
		this.name = that.getName();
		this.id = that.getID();
		this.type = that.getType();
		this.category = that.getCategory();
		this.status = that.getStatus();
		this.description = that.getDescription();
		this.blueprint = that.getBlueprint();
		this.build_time = that.getBuildTime();
		this.max_health = that.getMaxHealth();
		this.current_health = that.getCurrentHealth();
		this.color = that.getColor();

		// Copying the stats hash map.
		this.points = new PointSet();
		for (String key : KEYS) {
			this.points.set(key, that.getPoint(key));
		}

	}

	// Private code.
	private boolean[][] createBlueprint(String in) {

		// Breaking the string into it's two major parts.
		String size = in.substring(0, 3);
		String layout = in.substring(4);

		// Breaks the size string into x and y coordinates.
		int x = Integer.parseInt(size.substring(0, 1));
		int y = Integer.parseInt(size.substring(2));

		// Turns the layout into a straightened out boolean blueprint.
		boolean[] straight_blueprint = new boolean[x * y];
		for (int i = 0; i < x * y; ++i) {
			boolean to_enter = false;
			if (layout.substring(i, i+1).equals("1")) {
				to_enter = true;
			}
			straight_blueprint[i] = to_enter; 
		}

		// Creates final blueprint array.
		boolean[][] final_blueprint = new boolean[x][y];

		// Uses straight blueprint to create the final blueprint.
		for (int i = 0; i < y; ++i) {
			for (int j = 0; j < x; ++j) {
				final_blueprint[j][i] = straight_blueprint[j + (i * x)];
			}
		}

		// Returns the finished blueprint.
		return final_blueprint;
	}

	// Accessors
	public String getName() {return this.name;}
	public String getID() {return this.id;}
	public String getType() {return this.type;}
	public String getCategory() {return this.category;}
	public String getStatus() {return this.status;}
	public String getDescription() {return this.description;}
	public boolean[][] getBlueprint() {return this.blueprint;}
	public int[] getBlueprintSize() {
		int[] return_array = {this.blueprint.length, this.blueprint[0].length};
		return return_array;
	}
	public int getBlueprintArea() {
		int the_area = 0;
		for (int y = 0; y < this.blueprint[0].length; ++y) {
			for (int x = 0; x <  this.blueprint.length; ++x) {
				boolean the_bool = this.blueprint[x][y];
				int the_add = 0;
				if (the_bool) {the_add = 1;}
				the_area += the_add;
			}
		}
		return the_area;
	}
	public String getBlueprintAsString() {
		String cont = "";
		for (int y = 0; y < this.blueprint[0].length; ++y) {
			for (int x = 0; x <  this.blueprint.length; ++x) {
				boolean the_bool = this.blueprint[x][y];
				String the_add = "F";
				if (the_bool) {the_add = "T";}
				cont += the_add;
			}
			cont += "\n";
		}
		return cont;
	}
	public int getBuildTime() {return this.build_time;}
	public boolean isBuilt() {return this.build_time == 0;}
	public int getMaxHealth() {return this.max_health;}
	public int getCurrentHealth() {return this.current_health;}
	public boolean isAtMaxHealth() {return this.current_health == this.max_health;}
	public boolean isAtZeroHealth() {return this.current_health == 0;}
	public double getHealthPercentage() {return (1.0 * this.current_health / this.max_health) * 100;}
	public int getPoint(String key) {return this.points.get(key);}
	public PointSet getPointSet() {return this.points;}
	public ArrayList<String> hasPoints() {
		return points.getPositives();
	}
	public ArrayList<String> getPointsList() {
		ArrayList<String> arr = new ArrayList<String>();
		for (String key : KEYS) {
			arr.add(key);
		}
		return arr;
	}
	public String getAllStatsAsString() {return statsToString(getPointsList());}
	public String getHasStatsAsString() {return statsToString(hasPoints());}
	private String statsToString(ArrayList<String> keyArr) {
		String finalString = "";
		for (String key : keyArr) {
			finalString += key + ": \t" + (int)this.points.get(key) + "\n";
		}
		return finalString;
	}
	public int getX() {return this.x;}
	public int getY() {return this.y;}
	public Color getColor() {return this.color;}	
	
	public ArrayList<String> getPositives() {return points.getPositives();}
	public ArrayList<String> getNegatives() {return points.getNegatives();}
	public ArrayList<String> getNonZeroes() {return points.getNonZeroes();}

	// Mutators
	public void setName(String in) {this.name = in;}
	public void setID(String in) {this.id = in;}
	public void setStatus(String in) {this.status = in;}
	public void incBuildTime() {++build_time; if (build_time > 0) {build_time = 0;}}
	public void decBuildTime() {--build_time;}
	public void setBuildTime(int in) {build_time = in; if (build_time > 0) {build_time = 0;}}
	public void incCurrentHealth() {++current_health; if (current_health > max_health) {current_health = max_health;}}
	public void decCurrentHealth() {--current_health; if (current_health < 0) {current_health = 0;}}
	public void setCurrentHealth(int in) {current_health = in; if (current_health > max_health) {current_health = max_health;} if (current_health < 0) {current_health = 0;}}
	public void setX(int in) {this.x = in;}
	public void setY(int in) {this.y = in;}
	public void setColor(Color in) {this.color = in;}
	
	public void varyColor(int rVar, int gVar, int bVar) {
		int red = (this.color.getRed() + rVar + 256) % 256; 
		int green = (this.color.getGreen() + gVar + 256) % 256;
		int blue = (this.color.getBlue() + bVar + 256) % 256;
		this.color = new Color(red, green, blue);
	}
	
	public void rotate() {
		boolean[][] oldBlpt = this.getBlueprint();
		boolean[][] newBlpt = new boolean[this.getBlueprintSize()[1]][this.getBlueprintSize()[0]];
		for (int i = 0; i < oldBlpt.length; ++i) {
			for (int j = 0; j < oldBlpt[0].length; ++j) {
				newBlpt[j][i] = oldBlpt[i][(oldBlpt[0].length - j) - 1];
			}
		}
		this.blueprint = newBlpt;
		if (Board.DEBUG_TRACE) {System.out.println("Rotating " + this.type + ".");}
	}

	// Updaters
	public void update() {
		if (this.build_time < 0) {++this.build_time;}
	}

	// Overrides
	public String toString() {
		String the_string = "Building " + this.id + ", '" + this.name + "' the " + this.type + ". Health is at " + this.getHealthPercentage() + "%, and the status is " + this.status + ".\n";
		the_string += "Description: '" + this.description + "'\n";
		the_string += "Points: " + this.points.toString() + "\n";
		for (int y = 0; y < this.blueprint[0].length; ++y) {
			for (int x = 0; x <  this.blueprint.length; ++x) {
				boolean the_bool = this.blueprint[x][y];
				String the_add = "F";
				if (the_bool) {the_add = "T";}
				the_string += the_add;
			}
			the_string += "\n";
		}
		
		return the_string;
	}

}