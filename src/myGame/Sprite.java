package myGame;

import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

public class Sprite {

    // Fields
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected boolean vis;
    protected Image image;

    // Constructor
    public Sprite(int x, int y) {

        this.x = x;
        this.y = y;
        this.vis = true;

    }

    // Accessors
    protected void getImageDimensions() {

        this.width = image.getWidth(null);
        this.height = image.getHeight(null);

    }

    protected void loadImage(String imageName) {

        ImageIcon ii = new ImageIcon(imageName);
        this.image = ii.getImage();
        this.getImageDimensions();
        
    }

    public Image getImage() {
        return this.image;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public boolean isVisible() {
        return this.vis;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    // Mutators
    public void setX(int x1) {this.x = x1;}

    public void setY(int y1) {this.y = y1;}

    public void incX() {
        this.x += 1;
    }

    public void incY() {
        this.y += 1;
    }

    public void incX(int i) {
        this.x += i;
    }

    public void incY(int i) {
        this.y += i;
    }

    public void decX() {
        this.x -= 1;
    }

    public void decY() {
        this.y -= 1;
    }

    public void decX(int i) {
        this.x -= i;
    }

    public void decY(int i) {
        this.y -= i;
    }

    public void setVisible(Boolean visible) {
        this.vis = visible;
    }

}