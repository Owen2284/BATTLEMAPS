package myGame;

import java.util.ArrayList;

import myData.MyArrays;

public class ActionHandler {

	private ArrayList<Action> acts;
	private ArrayList<Action> hist;
	private ActionScript script;
	
	public static String[] POINTSLIST = {"Military", "Technology", "Nature", "Diplomacy", "Commerce", "Industry"};
	
	public ActionHandler(ActionScript in) {
		this.acts = new ArrayList<Action>();
		this.hist = new ArrayList<Action>();
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
	
	private ArrayList<Action> getActionsByPlayer(Player target) {
		ArrayList<Action> returner = new ArrayList<Action>();
		for (Action a : acts) {
			if (a.getTargetPlayer().equals(target)) {
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
		return MyArrays.intersection(getActionsByPlayer(target), getActionsByIntent("Attack")).size() != 0;
	}
	
	public ArrayList<String> updateStartPlayer(Player pla) {
		ArrayList<Action> toExecute = new ArrayList<Action>();
		ArrayList<String> returnMessages = new ArrayList<String>();
		for (Action a : this.acts) {
			if (a.getAge() >= 1) {
				toExecute.add(a);
			}
		}
		for (Action b : toExecute) {
			this.acts.remove(b);
			b.setAge(0);
			if (!b.isCountered()) {
				script.execute(b);
				returnMessages.add("Action executed: " + b.toString());
			}
			this.hist.add(b);
		}
		return returnMessages;
	}
	
	public void updateEndPlayer(Player pla) {
		for (Action a : this.acts) {a.incAge();}
		for (Action a : this.hist) {a.incAge();}
	}
	
	public void updateEndTurn() {
		
	}
	
	public void addAction(Action in) {this.acts.add(in);}
	public void setScript(ActionScript in) {this.script = in;}
	
}
