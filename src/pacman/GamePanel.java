package pacman;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePanel extends JPanel implements ActionListener {

    private ArrayList<Ghost> ghosts;
    private static final int SPAWN_INTERVAL = 5000;
    private static final int NUM_GHOSTS = 4;
    private int spawnTimer = 0;
    private Timer timer;
    private Pacman pacman;
    private Maze maze;
    private final int DELAY = 16; // Delay for ~60 FPS
    private long lastFrameTime;
    private long deltaTime;

    public GamePanel() {
        setPreferredSize(new Dimension(800, 500));
        setBackground(Color.BLACK);
        pacman = new Pacman();
        maze = new Maze();
        timer = new Timer(DELAY, this);
        ghosts = new ArrayList();
        spawnGhosts();
        lastFrameTime = System.currentTimeMillis();
        
        // Add key listener for controlling Pacman
        InputHandler inputHandler = new InputHandler(pacman);
        addKeyListener(inputHandler);
        setFocusable(true);
    }

    private void updateDeltaTime(){
        long currentTime = System.currentTimeMillis();
        deltaTime = currentTime - lastFrameTime;
        lastFrameTime = currentTime;
    }

    private void spawnGhosts() {

    // Spawn ghosts randomly
    int x = 522;
    int y = 448;
    for (int i = 0; i < NUM_GHOSTS; i++) {
            
            System.out.println("Spawning ghost " +i +"x: " +x + "y: " +y);
            String imagePath = String.format("/images/ghost_%d.png", i);
            Ghost ghost = new Ghost(x, y, imagePath, maze); // Adjust image path as needed
            ghosts.add(ghost);
        }
    }
    

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        maze.draw(g);
        pacman.draw(g);
        for (Ghost ghost : ghosts) {
            ghost.draw(g);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint();
    }

    private void updateGame() {
        
        // Store Pacman's current position before updating
    int oldX = pacman.getX();
    int oldY = pacman.getY();

    spawnTimer += deltaTime;
    if (spawnTimer >= SPAWN_INTERVAL) {
        spawnGhosts();
        spawnTimer = 0; // Reset timer
    }

    for (Ghost ghost : ghosts){
        ghost.update();
    }

    // Update Pacman's position
    pacman.update();
    
    // Check for collisions with walls
    if (maze.checkWallCollision(pacman.getBounds())) {
        // Restore Pacman's previous position
        pacman.setPosition(oldX, oldY);
    }

    // Check for collisions with pellets
    if (maze.checkPelletCollision(pacman.getBounds())) {
        // Handle collision with pellet
    }

    if(maze.allPelletsConsumed()){
        showWinScreen();
    }
    }

    public void startGame(){
        timer.start();
    }

    private void showWinScreen(){
            removeAll();
    
    // Add a label to display "You won!"
            JLabel winLabel = new JLabel("Du hast gewonnen!");
            winLabel.setForeground(Color.WHITE);
            winLabel.setFont(new Font("Arial", Font.BOLD, 36));
            winLabel.setHorizontalAlignment(SwingConstants.CENTER);
            winLabel.setVerticalAlignment(SwingConstants.CENTER);
            add(winLabel);
            
            // Repaint the panel
            revalidate();
            repaint();
            
            // Stop the timer to pause the game
            timer.stop();
    }


    

}
