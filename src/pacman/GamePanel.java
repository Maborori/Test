package pacman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

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

        // Add a label to display "Du hast gewonnen!"
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
        removeAll();

        // Load the "gameover" sprite
        ImageIcon gameOverIcon = new ImageIcon("src/images/gameover.png");

        // Adjust the size of the gameOverIcon
        Image image = gameOverIcon.getImage();
        int newWidth = 700; // Set desired width
        int newHeight = 200; // Set desired height
        Image resizedImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        gameOverIcon = new ImageIcon(resizedImage);

        JLabel gameOverLabel = new JLabel(gameOverIcon);

        // Set the size and position of the gameOverLabel
        gameOverLabel.setBounds((getWidth() - newWidth) / 2, (getHeight() - newHeight) / 2, newWidth, newHeight);

        setLayout(null); // Use absolute positioning
        add(gameOverLabel);

        // Load and scale the "Menu" and "Restart" button images
        ImageIcon menuIcon0 = scaleImageIcon(new ImageIcon("src/images/menu0.png"), 2);
        ImageIcon menuIcon1 = scaleImageIcon(new ImageIcon("src/images/menu1.png"), 2);
        ImageIcon menuIcon2 = scaleImageIcon(new ImageIcon("src/images/menu2.png"), 2);

        ImageIcon restartIcon0 = scaleImageIcon(new ImageIcon("src/images/restart0.png"), 2);
        ImageIcon restartIcon1 = scaleImageIcon(new ImageIcon("src/images/restart1.png"), 2);
        ImageIcon restartIcon2 = scaleImageIcon(new ImageIcon("src/images/restart2.png"), 2);

        // Create the buttons
        JButton menuButton = createButton(menuIcon0, menuIcon1, menuIcon2);
        JButton restartButton = createButton(restartIcon0, restartIcon1, restartIcon2);

        int buttonY = (getHeight() - newHeight) / 2 + newHeight + 20; // Place below the gameOverLabel

        menuButton.setBounds((getWidth() / 2 + 20), buttonY, menuIcon0.getIconWidth(), menuIcon0.getIconHeight());
        restartButton.setBounds((getWidth() / 2 - restartIcon0.getIconWidth() - 20), buttonY, restartIcon0.getIconWidth(), restartIcon0.getIconHeight());

        // Add action listeners to the buttons
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnToMainMenu();
            }
        });

        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });

        // Add the buttons to the panel
        add(menuButton);
        add(restartButton);

        // Repaint the panel
        revalidate();
        repaint();

        // Stop the timer to pause the game
        timer.stop();
    }

    private ImageIcon scaleImageIcon(ImageIcon icon, double scale) {
        Image img = icon.getImage();
        int width = (int) (img.getWidth(null) * scale);
        int height = (int) (img.getHeight(null) * scale);
        Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImg);
    }

    private JButton createButton(ImageIcon icon0, ImageIcon icon1, ImageIcon icon2) {
        JButton button = new JButton(icon0);
        button.setRolloverIcon(icon1);
        button.setPressedIcon(icon2);
        button.setPreferredSize(new Dimension(icon0.getIconWidth(), icon0.getIconHeight()));
        button.setBackground(Color.BLACK);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        return button;
    }

    private void returnToMainMenu() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.setContentPane(new MainMenu());
        frame.revalidate();
        frame.repaint();
    }

    public void restartGame() {
        GamePanel gamePanel = new GamePanel();
        gamePanel.startGame();
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(GamePanel.this);
        frame.setContentPane(gamePanel);
        frame.revalidate();
        frame.repaint();
        gamePanel.requestFocusInWindow();
    }
}