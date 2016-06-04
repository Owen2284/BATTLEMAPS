/*
 *  ImageLibrary.java
 *
 *  Stores images for game.
 *
 */

package myGraphics;

import myMain.Board;

import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;

public class ImageLibrary {

    // Fields
    private Image[] images;
    private int max;

    // Constructor
    public ImageLibrary(int size) {
        images = new Image[size];
        max = size;
    }

    // Loading methods.
    public void loadImage(int n, String imageName) {

        if (n >= 1 && n <= max) {
            File f = new File(imageName);
            if (f.exists() && !f.isDirectory()) {
                ImageIcon ii = new ImageIcon(imageName);
                this.images[n-1] = ii.getImage();
                if (Board.DEBUG_LOAD) {System.out.println("IMAGE ADDED TO SLOT " + (n-1) + " : " + imageName);}
            }
        }

    }

    // Accessors
    public Image getImage(int n) {

        if (n >= 1 && n <= this.max) {
            return this.images[n-1];
        }

        return null;

    }

    public Image getImage(ImageToken iT) {

        return getImage(iT.getImage());

    }

    // TODO: Get image width/height.

    public int getMax() {
        return this.max;
    }
    
    public boolean notNull(int i) {
    	return getImage(i) != null;
    }

    // Mutators


    // Operators

}