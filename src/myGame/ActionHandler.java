package myGame;

import java.util.ArrayList;
import java.util.HashMap;

import myData.MyArrays;
import myFiles.MyFileHandler;

public class ActionHandler {

	private ArrayList<Action> acts;
	private ArrayList<Action> hist;
	private HashMap<String,String[]> csvFile;
	private ActionScript script;
	
	public static String[] POINTSLIST = {"Military", "Technology", "Nature", "Diplomacy", "Commerce", "Industry"};
	
	public ActionHandler(String csvFilePath, ActionScript in) {
		this.acts = new ArrayList<Action>();
		this.hist = new ArrayList<Action>();
		this.csvFile = new HashMap<String,String[]>();
		MyFileHandler fh = new MyFileHandler();
		boolean first = true;
		for (String s : fh.readTextFile(csvFilePath)) {
			if (!first) {
				String[] csvLine = s.split(",");
				String code = csvLine[0];
				String[] dataPayload = s.substring(code.length() + 1).split(",");
				dataPayload[6] = dataPayload[6].replace("\\|", ",");
				csvFile.put(code, dataPayload);
			} else {
				first = false;
			}
		}
		this.script = in;
	}
	
	private ArrayList<Action> getActionsByCity(City target) {
		ArrayList<Action> returner = new ArrayList<Action>();
		for (Action a : acts) {
			if (a.getTarget().equals(target)) {
				returner.add(a);
			}
		}
		return returner;
	}
	
	private ArrayList<Action> getActionsByAuthor(Player target) {
		ArrayList<Action> returner = new ArrayList<Action>();
		for (Action a : acts) {
			if (a.getAuthor() != null && a.getAuthor().equals(target)) {
				returner.add(a);
			}
		}
		return returner;
	}
	
	private ArrayList<Action> getActionsByTargetPlayer(Player target) {
		ArrayList<Action> returner = new ArrayList<Action>();
		for (Action a : acts) {
			if (a.getTargetPlayer() != null && a.getTargetPlayer().equals(target)) {
				returner.add(a);
			}
		}
		return returner;
	}
	
	private ArrayList<Action> getHistoryByAuthor(Player target) {
		ArrayList<Action> returner = new ArrayList<Action>();
		for (Action a : hist) {
			if (a.getAuthor() != null && a.getAuthor().equals(target)) {
				returner.add(a);
			}
		}
		return returner;
	}
	
	private ArrayList<Action> getHistoryByTargetPlayer(Player target) {
		ArrayList<Action> returner = new ArrayList<Action>();
		for (Action a : hist) {
			if (a.getTargetPlayer() != null && a.getTargetPlayer().equals(target)) {
				returner.add(a);
			}
		}
		return returner;
	}
	
	private ArrayList<Action> getActionsByIntent(String target) {
		ArrayList<Action> returner = new ArrayList<Action>();
		for (Action a : acts) {
			if (a.getIntent().equals(target)) {
				returner.add(a);
			}
		}
		return returner;
	}
	
	public boolean cityUnderAttack(City target) {
		return MyArrays.intersection(getActionsByCity(target), getActionsByIntent("Attack")).size() != 0;
	}
	
	public boolean playerUnderAttack(Player target) {
		return MyArrays.intersection(getActionsByTargetPlayer(target), getActionsByIntent("Attack")).size() != 0;
	}
	
	public ArrayList<String> updateStartPlayer(Player pla) {
		ArrayList<Action> toExecute = new ArrayList<Action>();
		ArrayList<String> returnMessages = new ArrayList<String>();
		for (Action a : getActionsByAuthor(pla)) {
			if (a.getAge() >= 1) {
				toExecute.add(a);
			}
		}
		for (Action b : toExecute) {
			this.acts.remove(b);
			b.setAge(0);
			if (!b.isCountered()) {
				script.execute(b);
				String newMessage = "Action executed: " + b.toString();
				if (b.getCode().substring(0,3).equals("TAK")) {newMessage = "#" + newMessage;}
				returnMessages.add(newMessage);
			}
			this.hist.add(b);
		}
		return returnMessages;
	}
	
	public void updateEndPlayer(Player pla) {
		for (Action a : getActionsByAuthor(pla)) {a.incAge();}
		for (Action a : getHistoryByAuthor(pla)) {a.incAge();}
	}
	
	public void updateEndTurn() {
		
	}
	
	public void addAction(Action in) {this.acts.add(in);}
	public void setScript(ActionScript in) {this.script = in;}
	public HashMap<String,String[]> getData() {return this.csvFile;}
	
}
