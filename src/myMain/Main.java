package myMain;

import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Main extends JFrame {

	private static final long serialVersionUID = 6282844503926506907L;

	public Main() {
        
        initUI();

    }
    
    private void initUI() {
        
        // Gets board panel.
        this.add(new Board(this));
        
        // Sets up the frame.
        this.setResizable(false);
        this.pack();
        
        // Modifies frame fields.
        this.setTitle("BATTLEMAPS");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Making cursor invisible (http://stackoverflow.com/questions/1984071/how-to-hide-cursor-in-a-swing-application)
        
        // Transparent 16 x 16 pixel cursor image.
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

        // Create a new blank cursor.
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
            cursorImg, new Point(0, 0), "blank cursor");

        // Set the blank cursor to the JFrame.
        this.setCursor(blankCursor);

    }

    public static void main(String[] args) {
        
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                Main prog = new Main();
                prog.setVisible(true);
            }

        });

    }

}