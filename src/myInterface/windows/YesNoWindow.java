package myInterface.windows;

import java.awt.Color;

import myInterface.Button;

public class YesNoWindow extends InfoWindow {

	public YesNoWindow(String title, int x, int y) {
		super(title, x, y);
		this.height = 160;
		this.content = "Yes or No?";
		Button yes = new Button((this.width / 2) - 64, this.height - 100, title.replace(" ", "_") + "Yes", "Yes", 0, "");
		yes.setColorInner(Color.GREEN);
		Button no = new Button((this.width / 2) - 64, this.height - 60, title.replace(" ", "_") + "No", "No", 0, "");
		no.setColorInner(Color.RED);
		addWindowButton(yes);
		addWindowButton(no);
	}

	public Button getYes() {return this.getWindowButtons().get(0);}
	public Button getNo() {return this.getWindowButtons().get(1);}
	
	public void setYesExec(int in) {getYes().setExecutionNumber(in);}
	public void setYesAdd(String in) {getYes().setAdditionalString(in);}
	
	public void setNoExec(int in) {getNo().setExecutionNumber(in);}
	public void setNoAdd(String in) {getNo().setAdditionalString(in);}
	
}
