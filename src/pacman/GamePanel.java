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

    private void showLossScreen() {
        // Entfernt alle vorhandenen Komponenten aus dem Panel, um das Panel zu bereinigen
        removeAll();

        // Lädt das "gameover" Bild als ImageIcon aus dem angegebenen Pfad
        ImageIcon gameOverIcon = new ImageIcon("src/images/gameover.png");

        // Holt das Image-Objekt aus dem ImageIcon zur Größenanpassung
        Image image = gameOverIcon.getImage();
        int newWidth = 700; // Setzt die neue Breite des Bildes auf 700 Pixel
        int newHeight = 200; // Setzt die neue Höhe des Bildes auf 200 Pixel
        // Skaliert das Image auf die neuen Abmessungen mit einer glatten Skalierungsmethode
        Image resizedImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        // Erstellt ein neues ImageIcon mit dem skalierten Bild
        gameOverIcon = new ImageIcon(resizedImage);

        // Erstellt ein JLabel und setzt das skalierten gameOverIcon als dessen Icon
        JLabel gameOverLabel = new JLabel(gameOverIcon);

        // Berechnet die x- und y-Koordinaten, um das Label zentriert im Panel zu platzieren
        gameOverLabel.setBounds((getWidth() - newWidth) / 2, (getHeight() - newHeight) / 2, newWidth, newHeight);

        // Setzt das Layout des Panels auf null, um absolute Positionierung zu ermöglichen
        setLayout(null);
        // Fügt das gameOverLabel dem Panel hinzu
        add(gameOverLabel);

        // Lädt und skaliert die Bilder für die "Menu" Schaltfläche
        ImageIcon menuIcon0 = scaleImageIcon(new ImageIcon("src/images/menu0.png"), 2);
        ImageIcon menuIcon1 = scaleImageIcon(new ImageIcon("src/images/menu1.png"), 2);
        ImageIcon menuIcon2 = scaleImageIcon(new ImageIcon("src/images/menu2.png"), 2);

        // Lädt und skaliert die Bilder für die "Restart" Schaltfläche
        ImageIcon restartIcon0 = scaleImageIcon(new ImageIcon("src/images/restart0.png"), 2);
        ImageIcon restartIcon1 = scaleImageIcon(new ImageIcon("src/images/restart1.png"), 2);
        ImageIcon restartIcon2 = scaleImageIcon(new ImageIcon("src/images/restart2.png"), 2);

        // Erstellt die "Menu" Schaltfläche mit den drei Icons für verschiedene Zustände (normal, rollover, gedrückt)
        JButton menuButton = createButton(menuIcon0, menuIcon1, menuIcon2);
        // Erstellt die "Restart" Schaltfläche mit den drei Icons für verschiedene Zustände (normal, rollover, gedrückt)
        JButton restartButton = createButton(restartIcon0, restartIcon1, restartIcon2);

        // Berechnet die y-Position der Schaltflächen, um sie unterhalb des gameOverLabels zu platzieren
        int buttonY = (getHeight() - newHeight) / 2 + newHeight + 20; // 20 Pixel Abstand unterhalb des Labels

        // Setzt die Position und Größe der "Menu" Schaltfläche relativ zur Panelbreite
        menuButton.setBounds((getWidth() / 2 + 20), buttonY, menuIcon0.getIconWidth(), menuIcon0.getIconHeight());
        // Setzt die Position und Größe der "Restart" Schaltfläche relativ zur Panelbreite
        restartButton.setBounds((getWidth() / 2 - restartIcon0.getIconWidth() - 20), buttonY, restartIcon0.getIconWidth(), restartIcon0.getIconHeight());

        // Fügt einen ActionListener zur "Menu" Schaltfläche hinzu, um das Hauptmenü aufzurufen
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnToMainMenu(); // Ruft die Methode auf, die das Hauptmenü anzeigt
            }
        });

        // Fügt einen ActionListener zur "Restart" Schaltfläche hinzu, um das Spiel neu zu starten
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame(); // Ruft die Methode auf, die das Spiel neu startet
            }
        });

        // Fügt die "Menu" und "Restart" Schaltflächen dem Panel hinzu
        add(menuButton);
        add(restartButton);

        // Validiert das Layout des Panels und zeichnet es neu
        revalidate();
        repaint();

        // Stoppt den Timer, um das Spiel zu pausieren
        timer.stop();
    }

    // Skaliert ein ImageIcon auf die angegebene Größe und gibt das neue ImageIcon zurück
    private ImageIcon scaleImageIcon(ImageIcon icon, double scale) {
        Image img = icon.getImage(); // Holt das Image-Objekt aus dem Icon
        int width = (int) (img.getWidth(null) * scale); // Berechnet die neue Breite
        int height = (int) (img.getHeight(null) * scale); // Berechnet die neue Höhe
        // Skaliert das Image auf die neuen Abmessungen mit einer glatten Skalierungsmethode
        Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        // Gibt ein neues ImageIcon mit dem skalierten Bild zurück
        return new ImageIcon(scaledImg);
    }

    // Erstellt und konfiguriert einen JButton mit den angegebenen Icons für die verschiedenen Zustände
    private JButton createButton(ImageIcon icon0, ImageIcon icon1, ImageIcon icon2) {
        JButton button = new JButton(icon0); // Erstellt die Schaltfläche mit dem normalen Icon
        button.setRolloverIcon(icon1); // Setzt das Icon, wenn der Mauszeiger über der Schaltfläche schwebt
        button.setPressedIcon(icon2); // Setzt das Icon, wenn die Schaltfläche gedrückt wird
        button.setPreferredSize(new Dimension(icon0.getIconWidth(), icon0.getIconHeight())); // Setzt die bevorzugte Größe der Schaltfläche
        button.setBackground(Color.BLACK); // Setzt den Hintergrund der Schaltfläche auf schwarz
        button.setBorderPainted(false); // Entfernt die Standard-Rahmenzeichnung
        button.setFocusPainted(false); // Entfernt die Fokus-Markierung
        button.setContentAreaFilled(false); // Entfernt die Hintergrundfüllung
        return button; // Gibt die konfigurierte Schaltfläche zurück
    }

    // Wechselt zur Hauptmenü-Oberfläche
    private void returnToMainMenu() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this); // Holt das übergeordnete JFrame des aktuellen Panels
        frame.setContentPane(new MainMenu()); // Setzt das Content Pane des Frames auf ein neues Hauptmenü-Panel
        frame.revalidate(); // Validiert das Layout des Frames neu
        frame.repaint(); // Zeichnet den Frame neu
    }

    // Startet das Spiel neu
    public void restartGame() {
        GamePanel gamePanel = new GamePanel(); // Erstellt ein neues Spiel-Panel
        gamePanel.startGame(); // Startet das Spiel im neuen Panel
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(GamePanel.this); // Holt das übergeordnete JFrame des aktuellen GamePanels
        frame.setContentPane(gamePanel); // Setzt das Content Pane des Frames auf das neue Spiel-Panel
        frame.revalidate(); // Validiert das Layout des Frames neu
        frame.repaint(); // Zeichnet den Frame neu
        gamePanel.requestFocusInWindow(); // Setzt den Fokus auf das neue Spiel-Panel
    }
}