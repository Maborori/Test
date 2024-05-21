package pacman;

import javax.swing.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


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
        setPreferredSize(new Dimension(1080, 720));
        setBackground(Color.BLACK);
        pacman = new Pacman();
        maze = new Maze();
        timer = new Timer(DELAY, this);
        ghosts = new ArrayList<>();

        spawnGhosts();
        spawnGhosts();
        spawnGhosts();
        lastFrameTime = System.currentTimeMillis();

        // Add key listener for controlling Pacman
        InputHandler inputHandler = new InputHandler(pacman);
        addKeyListener(inputHandler);
        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    restartGame();
                }
            }
        });
    }
    private void updateDeltaTime() {
        long currentTime = System.currentTimeMillis();
        deltaTime = currentTime - lastFrameTime;
        lastFrameTime = currentTime;
    }
    private void spawnGhosts() {

        // Spawn ghosts randomly
        int x = 520;
        int y = 300;
        for (int i = 0; i < NUM_GHOSTS; i++) {

            System.out.println("Spawning ghost " + i + "x: " + x + "y: " + y);
            String imagePath = String.format("/images/ghost_%d.png", i);
            Ghost ghost = new Ghost(x, y, imagePath, maze); // Adjust image path as needed
            ghosts.add(ghost);
        }
    }
    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        super.paintComponent(g);
        maze.draw(g);
        pacman.draw(g);
        for (Ghost ghost : ghosts) {
            ghost.draw(g);
        }
    }
    private boolean checkPacmanGhostCollision() {
        Rectangle pacmanBounds = pacman.getBounds();
        for (Ghost ghost : ghosts) {
            Rectangle ghostBounds = ghost.getBounds();
            if (pacmanBounds.intersects(ghostBounds)) {
                return true;
            }
        }

        return false;
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

        for (Ghost ghost : ghosts) {
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

        if (maze.allPelletsConsumed()) {
            showWinScreen();
        }

        if (checkPacmanGhostCollision()) {
            showLossScreen();
        }
    }
    public void startGame() {
        timer.start();
    }
    private void showWinScreen() {
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
    private void showLossScreen() {
        removeAll(); // Alle vorhandenen Komponenten entfernen

        // Laden der "Game Over"-Bilder
        ImageIcon[] gameOverIcons = new ImageIcon[3];
        for (int i = 0; i < 3; i++) {
            gameOverIcons[i] = new ImageIcon("src/images/gameover" + i + ".jpg");
        }

        // JLabel erstellen, um die "Game Over"-Bilder anzuzeigen
        JLabel gameOverLabel = new JLabel();
        gameOverLabel.setHorizontalAlignment(SwingConstants.CENTER); // Horizontal zentriert
        gameOverLabel.setVerticalAlignment(SwingConstants.CENTER); // Vertikal zentriert

        // JLabel zum Panel hinzufügen
        setLayout(new BorderLayout());
        add(gameOverLabel, BorderLayout.CENTER); // In der Mitte des Panels platzieren


        // Initialisieren und Starten des Timers für die "Game Over"-Animation
        int animationDelay = 200; // Verzögerung zwischen den Bildern
        Timer gameOverTimer = new Timer(animationDelay, new ActionListener() {
            private int currentIndex = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                // Aktualisieren des JLabels mit dem aktuellen Bild
                gameOverLabel.setIcon(gameOverIcons[currentIndex]);
                currentIndex = (currentIndex + 1) % gameOverIcons.length;
            }
        });
        gameOverTimer.start(); // Timer starten

        // JButton erstellen, um das Spiel neu zu starten
        JButton restartButton = new JButton("Spiel neu starten");
        restartButton.setFont(new Font("Arial", Font.BOLD, 24)); // Schriftart und -größe festlegen
        restartButton.addActionListener(e -> restartGame()); // ActionListener für den Button hinzufügen

        // JPanel für den Button erstellen und zum unteren Rand des Panels hinzufügen
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Zentrierte Ausrichtung
        buttonPanel.add(restartButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Panel neu zeichnen
        revalidate();
        repaint();

        // Timer stoppen, um das Spiel zu pausieren
        timer.stop();
    }




    public void restartGame() {
        Game.main(new String[0]);
        SwingUtilities.getWindowAncestor(this).dispose();
    }
}
