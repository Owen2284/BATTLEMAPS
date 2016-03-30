package myInterface;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.Graphics;

public class MyTextMetrics {

	public static Graphics g;
	
	public static int[] getTextSize(String text) {

		AffineTransform affine_transform = new AffineTransform();     
		FontRenderContext frc = new FontRenderContext(affine_transform,true,true);     
		Font font = g.getFont();
		int[] returner = new int[2];
		returner[0] = (int)(font.getStringBounds(text, frc).getWidth());
		returner[1] = (int)(font.getStringBounds(text, frc).getHeight());
		
		return returner;

	}

	public static int getCountOf(String target, String field) {

		int count = 0;

		for (int i = 0; i < field.length() - target.length() + 1; ++i) {
			if (field.substring(i, i + target.length()).equals(target)) {++count;}
		}

		return count;

	}

	public static void setGraphics(Graphics g1) {
		g = g1;
	}

}