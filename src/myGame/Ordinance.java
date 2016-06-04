package myGame;

import java.util.ArrayList;

public class Ordinance {
	
	// Fields
	private String name;
	private String desc;
	private PointSet points;
	private int effectNum;
	
	// Constructor
	public Ordinance(String inName, String inDesc, int[] inPoints, int inEffect) {
		this.name = inName;
		this.desc = inDesc;
		points = new PointSet();
		points.set("Military", inPoints[0]);
		points.set("Technology", inPoints[1]);
		points.set("Nature", inPoints[2]);
		points.set("Diplomacy", inPoints[3]);
		points.set("Commerce", inPoints[4]);
		points.set("Industry", inPoints[5]);
		points.set("Population", inPoints[6]);
		points.set("Happiness", inPoints[7]);
		this.effectNum = inEffect;
	}

	// Basic accessors.
	public String getName() {return name;}
	public String getDesc() {return desc;}
	public String getDesc(String cityName) {return desc.split("$city")[0] + cityName + desc.split("$city")[1];}
	public int getEffectNum() {return effectNum;}
	
	// Points accessors.
	public int getPoint(String key) {return points.get(key);}
	public PointSet getPointSet() {return points;}
	public ArrayList<String> getPositives() {return points.getPositives();}
	public ArrayList<String> getNegatives() {return points.getNegatives();}
	public ArrayList<String> getNonZeroes() {return points.getNonZeroes();}	
	
	// Basic mutators.
	public void setEffectNum(int effectNum) {this.effectNum = effectNum;}

}
