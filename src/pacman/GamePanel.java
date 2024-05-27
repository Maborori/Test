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
    private static final int NUM_GHOSTS = 4;
    private Timer timer;
    private Pacman pacman;
    private Maze maze;
    private final int DELAY = 16; // Verzögert den Timer des Spiels um 16 Millisekunden, sodass eine stabile Bildrate gewährleistet wird
    

    public GamePanel() {

        // Bestimmung der Bildgröße
        setPreferredSize(new Dimension(1080, 720));
        setBackground(Color.BLACK);

        // Initialisierung der Pacman-, Labyrinth- und Timerklasse
        pacman = new Pacman();
        maze = new Maze();
        timer = new Timer(DELAY, this);

        ghosts = new ArrayList<>();

        // Durch den Aufruf dieser Methode werden die Geister innerhalb des Spielfelds als physische Elemente erzeugt
        spawnGhosts();

        // Verwendung eines Key Listeners und eines Inputhandlers, welche die Steuerung von Pacman implementieren
        InputHandler inputHandler = new InputHandler(pacman);
        addKeyListener(inputHandler);

        // Ermöglicht es dem JPanel(GamePanel) auf die Tastatureingaben des Key Listeners zu reagieren
        setFocusable(true);

        // Verwendung eines weiteren Key Listeners, sodass mit der Taste 'R' das Spiel jederzeit neugestartet werden kann
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    restartGame();
                }
            }
        });
    }

    // Methode zur Erzeugung der Geister innerhalb des Spiel
    private void spawnGhosts() {

        // Koordinaten, die angeben, wo die Geister erzeugt werden
        int x = 520;
        int y = 300;

        // Erstellung einer for-Schleife, um jeden einzelnen Geist zu erzeugen und jedem Geist eine individuelle Farbe (wird durch die jeweilige Bilddatei angegeben) zuzuweisen
        for (int i = 0; i < NUM_GHOSTS; i++) {
            System.out.println("Spawning ghost " + i + "x: " + x + "y: " + y);
            String imagePath = String.format("/images/ghost_%d.png", i);
            Ghost ghost = new Ghost(x, y, imagePath, maze); // Adjust image path as needed
            ghosts.add(ghost);
        }
    }

    // Sorgt dafür, dass die Spielgrafiken und Spielelemente gezeichnet, bzw. generiert werden
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

    // Uberprüft auf Kollisionen zwischen Pacman und den Geistern
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

    // Gibt an was passiert, wenn ein ActionEvent durch den Timer ausgelöst wird
    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint();
    }

    private void updateGame() {

        // Erstellung von Variablen, um die Position von Pacman zu speichern
        int oldX = pacman.getX();
        int oldY = pacman.getY();

        //Aktualisierung der Position und Bewegung der Geister und von Pacman 
        for (Ghost ghost : ghosts) {
            ghost.update();
        }

        pacman.update();

        // Setzt die Position von Pacman bei der Kollision mit einer Wand auf die vorherige zurück
        if (maze.checkWallCollision(pacman.getBounds())) {
            pacman.setPosition(oldX, oldY);
        }

        // Ermöglicht Pacman das "Essen" der Perlen, wenn er mit ihnen kollidiert
        if (maze.checkPelletCollision(pacman.getBounds())) {
        }

        // Zeigt die Siegesmeldung an, wenn alle Perlen "gegessen" wurden 
        if (maze.allPelletsConsumed()) {
            showWinScreen();
        }

        // Zeigt die Niederlagemeldung an, wenn Pacman mit einem Geist kollidiert
        if (checkPacmanGhostCollision()) {
            showLossScreen();
        }
    }

    // Startet den Spiel-Timer
    public void startGame() {
        timer.start();
    } 

    // Zeigt den Gewinnbildschirm an, wenn alle Perlen gesammelt wurden
    private void showWinScreen() {

        JLabel winLabel = new JLabel("Du hast gewonnen!");
        winLabel.setForeground(Color.WHITE);
        winLabel.setFont(new Font("Arial", Font.BOLD, 36));
        winLabel.setHorizontalAlignment(SwingConstants.CENTER);
        winLabel.setVerticalAlignment(SwingConstants.CENTER);
        add(winLabel);

        revalidate();
        repaint();

        timer.stop();
    }

    // Zeigt den Niederlagebildschirm an, wenn alle Perlen gesammelt wurden
    private void showLossScreen() {

        // Lädt die Bilddatei, die für die "Gameover" Anzeige erforderlich ist
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

    // Startet das Spiel neu
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