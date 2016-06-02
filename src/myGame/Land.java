package myGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Random;

public class Land {
	
	private Polygon terrain;
	private Color landColor = new Color(0,200,0);
	private Color borderColor = Color.BLACK;
	
	private ArrayList<Line2D.Float> debugMergeLines = null;

	public Land(int centerX, int centerY, int pointCount, int landMin, 
			int landMax, int smoothingCount, double smoothingRate, Random r) {
		
		// Create arrays for the polygon.
		int[] pointsX = new int[pointCount * smoothingCount];
		int[] pointsY = new int[pointCount * smoothingCount];
		
		// Loop for calculating each main point.
		for (double i = 0.0; i < (double) (pointCount * smoothingCount); i += smoothingCount) {
			
			// Get degree and angle around the island the point will be.
			double smoothFactor = ((smoothingCount / 2) * smoothingRate);
			double piPercent = ((i - smoothFactor) / (pointCount * smoothingCount));
			double varyPercent = (smoothFactor / (pointCount * smoothingCount));
			
			// Get random values for distance from city.
			double randomX = (double) r.nextInt(landMax - landMin);
			double randomY = (double) r.nextInt(landMax - landMin);
			
			// Loop for generating all smoothing points.
			for (int j = (int) i; j < ((int) i) + smoothingCount; ++j) {
				
				// Getting value between 0 and smoothing count.
				int value = j - (int) i;
				
				// Calculating exact angle for the smoothing point.
				double angle = (2*Math.PI * (piPercent + (value * varyPercent)));
				
				// Calculating x and y positions for the angle.
				double factorX = Math.sin(angle);
				double factorY = Math.cos(angle);
				
				// Calculating the exact point coordinates.
				int newX = centerX + (int) ((randomX + landMin) * factorX);
				int newY = centerY + (int) ((randomY + landMin) * factorY);
				
				// Store point coordinates in the arrays.
				pointsX[j] = newX;
				pointsY[j] = newY;
				
			}
			
		}
		
		// Creating polygon.
		this.terrain = new Polygon(pointsX, pointsY, pointCount * smoothingCount);
		
	}
	
	public Polygon getTerrain() {return terrain;}
	public void setTerrain(Polygon in) {this.terrain = in;}

	public Color getLandColor() {return landColor;}
	public void setLandColor(Color in) {this.landColor = in;}

	public Color getBorderColor() {return borderColor;}
	public void setBorderColor(Color in) {this.borderColor = in;}
	
	public boolean overlaps(Land that) {
		
		// Getting points.
		int[] ax = this.terrain.xpoints;
		int[] ay = this.terrain.ypoints;
		int[] bx = that.getTerrain().xpoints;
		int[] by = that.getTerrain().ypoints;
		
		// Checking for overlaps.
		for (int i = 0; i < ax.length; ++i) {
			Point a = new Point(ax[i], ay[i]);
			if (that.getTerrain().contains(a)) {
				return true;
			}
		}
		for (int j = 0; j < bx.length; ++j) {
			Point b = new Point(bx[j], by[j]);
			if (this.terrain.contains(b)) {
				return true;
			}
		}
		
		return false;
		
	}
	
	@SuppressWarnings("unchecked")
	public void merge(Land that) {
		
		boolean debugFailMerge = false;
		
		// Getting points.
		int[] ax = this.terrain.xpoints;
		int[] ay = this.terrain.ypoints;
		int[] bx = that.getTerrain().xpoints;
		int[] by = that.getTerrain().ypoints;
		
		// Creates all lines of each polygon.
		int lineCount = 0;
		ArrayList<Line2D.Float> aLines = new ArrayList<Line2D.Float>();
		while (lineCount < ax.length) {
			aLines.add(new Line2D.Float(ax[lineCount % ax.length], 
					ay[lineCount % ay.length], 
					ax[(lineCount+1) % ax.length], 
					ay[(lineCount+1) % ay.length]));
			++lineCount;
		}
		lineCount = 0;
		ArrayList<Line2D.Float> bLines = new ArrayList<Line2D.Float>();
		while (lineCount < bx.length) {
			bLines.add(new Line2D.Float(bx[lineCount % bx.length], 
					by[lineCount % by.length], 
					bx[(lineCount+1) % bx.length], 
					by[(lineCount+1) % by.length]));
			++lineCount;
		}
		
		// Constructing lines of final polygon before collision.
		ArrayList<Line2D.Float> finalLinesA = new ArrayList<Line2D.Float>();
		ArrayList<Line2D.Float> finalLinesB = new ArrayList<Line2D.Float>();
		ArrayList<Line2D.Float> finalLinesC = new ArrayList<Line2D.Float>();
		Line2D.Float failLine = null;
		int i = 0;
		int checkCount = 0;
		while (failLine == null) {
			Line2D.Float aLine = aLines.get(i);
			if (!that.getTerrain().contains(aLine.getP2())) {
				finalLinesA.add(aLine);
			} else {
				failLine = aLine;
			}
			i = (i + 1) % aLines.size();
			++checkCount;
			if (checkCount > 2 * aLines.size()) { 
				System.out.println("OVERLOOP ON MERGE EVALUATION.");
				debugFailMerge = true;
				break;
			}
		}
		
		// Determining collision line.
		Line2D.Float collideLine = null;
		int j = 0;
		if (failLine != null) {
			
			// Checking lines in second polygon.
			for (Line2D.Float bl : bLines) {
				if (failLine.intersectsLine(bl)) {
					collideLine = bl;
				}
			}
			// TODO: Solve why this line breaks the island merging.
			// Creating new line between fail line and collide line.
			//finalLinesB.add(new Line2D.Float(failLine.getP1(), collideLine.getP2()));
			
			failLine = null;
			j = (bLines.indexOf(collideLine) + 1) % bLines.size();
			
		}
		
		// Run through lines of other polygon after first collision, but before second collision.
		checkCount = 0;
		while (failLine == null) {
			Line2D.Float bLine = bLines.get(j);
			if (!this.terrain.contains(bLine.getP2())) {
				finalLinesA.add(bLine);
			} else {
				failLine = bLine;
			}
			j = (j + 1) % bLines.size();
			++checkCount;
			if (checkCount > 2 * bLines.size()) { 
				System.out.println("OVERLOOP ON MERGE CONSTRUCTION.");
				debugFailMerge = true;
				break;
			}
		}
		
		// Determining collision line.
		collideLine = null;
		if (failLine != null) {
			
			// Checking lines in first polygon.
			for (Line2D.Float al : aLines) {
				if (failLine.intersectsLine(al)) {
					collideLine = al;
				}
			}
			
			// Creating new line between fail line and collide line.
			finalLinesC.add(new Line2D.Float(failLine.getP1(), collideLine.getP2()));
		}
		
		// Filling in with rest of lines.
		for (int k = i + 1; k < aLines.size(); k++) {
			Line2D.Float cLine = aLines.get(k);
			if ((!that.getTerrain().contains(cLine.getP1())) && (!that.getTerrain().contains(cLine.getP2()))) {
				finalLinesC.add(cLine);
			}
		}
		
		// Constructing final line array.
		ArrayList<Line2D.Float> finalLines = new ArrayList<Line2D.Float>();
		for (Line2D.Float al : finalLinesA) {
			finalLines.add(al);
		}
		for (Line2D.Float bl : finalLinesB) {
			finalLines.add(bl);
		}
		for (Line2D.Float cl : finalLinesC) {
			finalLines.add(cl);
		}
		debugMergeLines = (ArrayList<Line2D.Float>) finalLines.clone();
		for (Line2D.Float ao : aLines) {
			debugMergeLines.add(ao);
		}
		for (Line2D.Float bo : bLines) {
			debugMergeLines.add(bo);
		}
		
		// Creating final polygon.
		this.terrain = new Polygon();
		int lineNum = 0;
		for (Line2D.Float l : finalLines) {
			
			// Getting points.
			int polyX = (int) l.getP1().getX();
			int polyY = (int) l.getP1().getY();
			
			// Checking if point is at (0,0), in which case it should be omitted.
			// (Shitty fix, but it works.
			if (polyX != 0 && polyY != 0) {
				// Adds the point to the polygon if it is adequately positioned.
				this.terrain.addPoint(polyX, polyY);
			}
			
			if (debugFailMerge) {
				System.out.println("POINT " + lineNum + " OF FAILED MERGE: (" + (int) l.getP1().getX() + "," + (int) l.getP1().getY() + ").");
				++lineNum;
			}
		}
		
	}
	
	public void mergeOld(Land that) {
		
		// Getting points.
		int[] ax = this.terrain.xpoints;
		int[] ay = this.terrain.ypoints;
		int[] bx = that.getTerrain().xpoints;
		int[] by = that.getTerrain().ypoints;
		
		// Determining points to be removed.
		boolean[] ar = new boolean[ax.length];
		boolean[] br = new boolean[bx.length];
		for (int i = 0; i < ax.length; ++i) {
			Point a = new Point(ax[i], ay[i]);
			ar[i] = that.getTerrain().contains(a);
		}
		for (int j = 0; j < bx.length; ++j) {
			Point b = new Point(bx[j], by[j]);
			br[j] = this.terrain.contains(b);
		}
		
		// Determining array size for resulting polygon.
		int keepCount = 0;
		for (int i = 0; i < ar.length; ++i) {
			if (!ar[i]) {++keepCount;} 
		}
		for (int j = 0; j < br.length; ++j) {
			if (!br[j]) {++keepCount;} 
		}
	
		// Removing points and constructing final polygon.
		int[] finalX = new int[keepCount];
		int[] finalY = new int[keepCount];
		int index = 0;
		
		for (int i = 0; i < ar.length; ++i) {
			if (!ar[i]) {
				finalX[index] = ax[i];
				finalY[index] = ay[i];
				++index;
			}
		}
		for (int j = 0; j < br.length; ++j) {
			if (!br[j]) {
				finalX[index] = bx[j];
				finalY[index] = by[j];
				++index;
			}
		}
		
		this.terrain = new Polygon(finalX, finalY, keepCount);
		
	}

	public void draw(Graphics g, int xVar, int yVar) {
		terrain.translate(xVar, yVar);
		g.setColor(landColor);
		g.fillPolygon(terrain);
		g.setColor(borderColor);
		g.drawPolygon(terrain);
		terrain.translate(-xVar, -yVar);
		
		// Code for merging debug display.
		/*
		if (debugMergeLines != null) {
			for (Line2D.Float l : debugMergeLines) {
				g.setColor(Color.BLACK);
				g.drawLine((int)l.getX1(), (int)l.getY1(), (int)l.getX2(), (int)l.getY2());
				g.setColor(Color.RED);
				g.drawRect((int)l.getX1(), (int)l.getY1(), 1, 1);
			}
		}
		*/
	}

}
