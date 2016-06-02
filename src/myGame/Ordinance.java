package myGame;

import java.util.ArrayList;
import java.util.HashMap;

public class Ordinance {
	
	private String name;
	private String desc;
	private HashMap<String, Integer> points;
	private int effectNum;

	public Ordinance(String inName, String inDesc, int[] inPoints, int inEffect) {
		this.name = inName;
		this.desc = inDesc;
		points = new HashMap<String, Integer>();
		points.put("Military", inPoints[0]);
		points.put("Technology", inPoints[1]);
		points.put("Nature", inPoints[2]);
		points.put("Diplomacy", inPoints[3]);
		points.put("Commerce", inPoints[4]);
		points.put("Industry", inPoints[5]);
		points.put("Population", inPoints[6]);
		points.put("Happiness", inPoints[7]);
		this.effectNum = inEffect;
	}

	public String getName() {return name;}
	public String getDesc() {return desc;}
	public String getDesc(String cityName) {return desc.split("$city")[0] + cityName + desc.split("$city")[1];}
	public int getEffectNum() {return effectNum;}
	
	public int getStat(String key) {return points.get(key);}
	
	public void setEffectNum(int effectNum) {
		this.effectNum = effectNum;
	}

	public ArrayList<String> getPositives() {
		ArrayList<String> ret = new ArrayList<String>();
		for (String key : points.keySet()) {
			if (getStat(key) > 0) {
				ret.add(key);
			}
		}
		return ret;
	}
	
	public ArrayList<String> getNegatives() {
		ArrayList<String> ret = new ArrayList<String>();
		for (String key : points.keySet()) {
			if (getStat(key) < 0) {
				ret.add(key);
			}
		}
		return ret;
	}
	
	public ArrayList<String> getNonZeroes() {
		ArrayList<String> ret = getPositives();
		for (String key : getNegatives()) {
			ret.add(key);
		}
		return ret;
	}

}
