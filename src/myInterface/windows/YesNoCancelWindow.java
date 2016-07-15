package myInterface.windows;

import myInterface.buttons.Button;

public class YesNoCancelWindow extends YesNoWindow {

	public YesNoCancelWindow(String title, int x, int y) {
		super(title, x, y);
		setHeight(getHeight() + 40);
		Button cancel = new Button((this.width / 2) - 64, this.height - 60, title.replace(" ", "_") + "Cancel", "Cancel", 0, "");
		addWindowButton(cancel);
	}
	
	public Button getCancel() {return this.getWindowButtons().get(2);}
	
	public void setCancelExec(int in) {getCancel().setExecutionNumber(in);}
	public void setCancelAdd(String in) {getCancel().setAdditionalString(in);}

}
