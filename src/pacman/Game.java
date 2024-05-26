package pacman;

import javax.swing.*;

public class Game extends JFrame {

    public void startGame() {

        JFrame frame = new JFrame("PacMan Game");


        // Set the default close operation so the game exits when the window is closed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create an instance of the GamePanel, which will contain the game logic and graphics
        MainMenu mainMenu = new MainMenu();

        // Add the game panel to the frame
        frame.add(mainMenu);

        // Pack the frame to fit the preferred size and layout of its components
        frame.pack();

        // Make the window non-resizable
        frame.setResizable(false);

        // Center the window on the screen
        frame.setLocationRelativeTo(null);

        // Make the window visible
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        Game game = new Game();
        game.startGame();

    }
}