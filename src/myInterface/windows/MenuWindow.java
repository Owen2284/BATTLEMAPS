package myInterface.windows;

import java.awt.Color;
import java.security.InvalidParameterException;

import myInterface.Button;

public class MenuWindow extends ListWindow {

	public MenuWindow(String title, int inX, int inY, int rows) {
		super(title, inX, inY, rows);
		this.upButton.setVisible(false);
		this.downButton.setVisible(false);
	}
	
	public Button getMenuButton(int n) {
		try {
			return this.getCurrentButtons().get(n);
		} catch (IndexOutOfBoundsException e) {
			throw new IndexOutOfBoundsException(n + " exceeds the range of " + this.title + "'s buttons.");
		}
	}
	
	public void addMenuButtons(String[] titles, int[] exes, String[] adds, boolean[] vises, Color[] colors) {
		if (titles.length == exes.length && titles.length == adds.length &&
				titles.length == vises.length && titles.length == colors.length) {
			for (int i = 0; i < titles.length; ++i) {
				Button newButton = new Button(0, 0, this.title.replace(" ", "_") + "_" + titles[i].replace(" ", "_"), titles[i], exes[i], adds[i]);
				newButton.setVisible(vises[i]);
				newButton.setColorInner(colors[i]);
				this.addListButton(newButton);
			}
		} else {
			throw new InvalidParameterException("Lengths of input arrays do not match.");
		}
	}

}
