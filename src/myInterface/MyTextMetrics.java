package myInterface;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.Graphics;

public class MyTextMetrics {

	public static Graphics g;
	
	public static int[] getTextSizeFlat(String text) {

		AffineTransform affine_transform = new AffineTransform();     
		FontRenderContext frc = new FontRenderContext(affine_transform,true,true);     
		Font font = g.getFont();
		int[] returner = new int[2];
		returner[0] = (int)(font.getStringBounds(text, frc).getWidth());
		returner[1] = (int)(font.getStringBounds(text, frc).getHeight());
		
		return returner;

	}
	
	public static int[] getTextSizeComplex(String text) {
		
		return getTextSizeComplex(text, 0);

	}
	
	public static int[] getTextSizeComplex(String text, int spacing) {
		
		if (MyTextMetrics.getCountOf("\n", text) == 0) {
			return getTextSizeFlat(text);
		}

		AffineTransform affine_transform = new AffineTransform();     
		FontRenderContext frc = new FontRenderContext(affine_transform,true,true);     
		Font font = g.getFont();
		int[] sizes = {0, 0};
		for (String s : text.split("\n")) {
			int widthS = (int)(font.getStringBounds(s, frc).getWidth());
			if (sizes[0] < widthS) {sizes[0] = widthS;}
			sizes[1] += (int)(font.getStringBounds(s, frc).getHeight());
			sizes[1] += spacing;
		}
		
		return sizes;

	}

	public static int getCountOf(String target, String body) {

		int count = 0;

		for (int i = 0; i < body.length() - target.length() + 1; ++i) {
			if (body.substring(i, i + target.length()).equals(target)) {++count;}
		}

		return count;

	}

	public static void setGraphics(Graphics g1) {
		g = g1;
	}

}