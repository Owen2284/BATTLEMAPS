package myGame;

public class Action {

	private String name;			// Action name.
	private String code;			// Action code. (What code to execute).
	private String type;			// Point type of action.
	private Player author;			// Player who created the action.
	private City target;			// City being targeted by the action.
	private String intent;			// Whether it's a friendly action or an enemy action.
	private int age;				// Number of turns since the action was created.
	private boolean countered;		// Tells if action has been stopped by the target or not.
	private int cost;				// Number of points needed to counter the action.
	
	public Action(String inCode, String inType, Player inAuthor, City inCity, String inIntent, int inCost) {
		this("", inCode, inType, inAuthor, inCity, inIntent, inCost);
	}
	
	public Action(String inName, String inCode, String inType, Player inAuthor, City inCity, String inIntent, int inCost) {
		this.name = inName;
		this.type = inType;
		this.code = inCode;
		this.target = inCity;
		this.author = inAuthor;
		this.age = 0;
		this.intent = inIntent;
		this.countered = false;
		this.cost = inCost;
	}
	
	public String getName() {return this.name;}
	public String getCode() {return this.code;}
	public String getType() {return this.type;}
	public City getTarget() {return this.target;}
	public String getTargetName() {return this.target.getName();}
	public Player getTargetPlayer() {return this.target.getOwner();}
	public String getTargetPlayerName() {return this.target.getOwner().getName();}
	public Player getAuthor() {return this.author;}
	public String getIntent() {return this.intent;}
	public int getAge() {return this.age;}
	public boolean isCountered() {return this.countered;}
	public int getCost() {return this.cost;}
	
	public void setName(String in) {this.name = in;}
	public void setCode(String in) {this.code = in;}
	public void setType(String in) {this.type = in;}
	public void setTarget(City in) {this.target = in;}
	public void setAuthor(Player in) {this.author = in;}
	public void setIntent(String in) {this.intent = in;}
	public void setAge(int in) {this.age = in;}
	public void incAge() {this.age += 1;}
	public void setCountered(boolean in) {this.countered = in;}
	public void setCost(int in) {this.cost = in;}
	
	public String toString() {
		return this.name + " (" + this.code + "), " + this.intent + " on " + this.target.getName() + " by " + this.author.getName();
	}
	
}
