package pacman;

import javax.swing.*;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class WinScreen extends JFrame{

    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 500;

     public WinScreen() {
        setTitle("You Won!");
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawWinMessage(g);
            }
        };
        getContentPane().add(panel);
    }

    private void drawWinMessage(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        FontMetrics fontMetrics = g.getFontMetrics();
        String message = "Congratulations! You won!";
        int messageWidth = fontMetrics.stringWidth(message);
        int x = (SCREEN_WIDTH - messageWidth) / 2;
        int y = SCREEN_HEIGHT / 2;
        g.drawString(message, x, y);
    }
    
}
