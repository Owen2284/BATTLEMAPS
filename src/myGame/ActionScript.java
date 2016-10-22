package myGame;

public class ActionScript {

	public ActionScript () {}
	
	public void execute(Action in) {
		if (in.getIntent().equals("Attack")) {
			executeAttack(in);
		} else if (in.getIntent().equals("Defend")) {
			executeDefense(in);
		} else {
			throw new IllegalArgumentException("Invalid action intent.");
		}
	}
	
	public void executeAttack(Action in) {
		String actionCode = in.getCode();
		String actionPointType = in.getType();
		City actionTarget = in.getTarget();
		Player actionAttacker = in.getAuthor();
		Player actionDefender = in.getTargetPlayer();
		
		switch(actionCode) {
			case "ATTUP":		// Attack up.
				break;
			case "ATTDW":		// Attack down.
				break;
			case "DEFUP":		// Defence up.
				break;
			case "DEFDW":		// Defence down.
				break;
			case "HAPUP":		// Happiness up.
				break;
			case "HAPDW":		// Happiness down.
				break;
			case "POGUP":		// Point gain up.
				break;
			case "POGDW":		// Point gain down.
				break;
			case "BLDUP":		// Building speed up.
				break;
			case "BLDDW":		// Building speed down.
				break;
			case "ORDUP":		// Ordinances for this turn up.
				actionTarget.incOrdinancesForThisTurn();
				break;
			case "ORDDW":		// Ordinances for this turn down.
				actionTarget.decOrdinancesForThisTurn();
				break;
			case "PTSTL":		// Point steal.
				break;
			case "PTSWP":		// Point swap.
				break;
			case "POGST":		// Point gain stop.
				break;
			case "POGSP":		// Point gain siphon.
				break;
			case "POGMX":		// Point gain mix up.
				break;
			case "PTBAL":		// Point balance.
				break;
			case "TAKMI":		// Militaristic takeover.
				break;
			case "TAKDI":		// Diplomatic takeover.
				break;
			case "TAKTE":		// Technological takeover.
				break;
			case "TAKCO":		// Commercial takeover.
				break;
			case "TAKNA":		// Natural takeover.
				break;
			case "TAKIN":		// Industrial takeover.
				break;
			default:
				throw new IllegalArgumentException("Invalid action name.");
		}
		
	}
	
	public void executeDefense(Action in) {
		String actionName = in.getName();
		String actionPointType = in.getType();
		City actionTarget = in.getTarget();
		Player actionDefender = in.getTargetPlayer();
	}
	
}
