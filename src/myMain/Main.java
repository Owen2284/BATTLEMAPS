package myMain;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class Main extends JFrame {

    public Main() {
        
        initUI();

    }
    
    private void initUI() {
        
        // Gets board panel.
        this.add(new Board(1000, 600));
        
        // Sets up the frame.
        this.setResizable(false);
        this.pack();
        
        // Modifies frame fields.
        this.setTitle("BATTLEMAPS");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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