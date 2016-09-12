package myGame;

public class Action {

	private String name;
	private String type;
	private Player author;
	private City target;
	private String intent;
	private int age;
	private boolean countered;
	
	public Action(String inName, String inType, Player inAuthor, City inCity, String inIntent) {
		this.type = inType;
		this.name = inName;
		this.target = inCity;
		this.author = inAuthor;
		this.age = 0;
		this.intent = inIntent;
		this.countered = false;
	}
	
	public String getName() {return this.name;}
	public String getType() {return this.type;}
	public City getTarget() {return this.target;}
	public String getTargetName() {return this.target.getName();}
	public Player getTargetPlayer() {return this.target.getOwner();}
	public String getTargetPlayerName() {return this.target.getOwner().getName();}
	public Player getAuthor() {return this.author;}
	public String getIntent() {return this.intent;}
	public int getAge() {return this.age;}
	public boolean isCountered() {return this.countered;}
	
	public void setName(String in) {this.name = in;}
	public void setType(String in) {this.type = in;}
	public void setTarget(City in) {this.target = in;}
	public void setAuthor(Player in) {this.author = in;}
	public void setIntent(String in) {this.intent = in;}
	public void setAge(int in) {this.age = in;}
	public void incAge() {this.age += 1;}
	public void setCountered(boolean in) {this.countered = in;}
	
}
