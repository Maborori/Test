package pacman;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

// Die Pacman-Klasse erbt von GameObject
public class Pacman extends GameObject {
    // Bewegungsdeltas
    private int dx, dy;
    // Map zur Speicherung der Animationen für jede Richtung
    private Map<Direction, Image[]> animations;
    // Aktuelle Bewegungsrichtung
    public Direction currentDirection;
    // Index des aktuellen Bildes in der Animation
    private int currentImageIndex;
    // Verzögerung zwischen den Animationsframes
    private int animationDelay = 10;
    // Zähler für die Animation
    private int animationCounter = 0;

    // Konstruktor
    public Pacman() {
        super(50, 50, 16, 16); // Initialisiert Position und Größe mit dem Konstruktor von GameObject
        animations = new HashMap<>();
        loadAnimations(); // Lädt die Animationen
        currentDirection = Direction.RIGHT; // Standardrichtung
        currentImageIndex = 0;
    }

    // Lädt die Animationen für jede Richtung
    private void loadAnimations() {
        try {
            animations.put(Direction.RIGHT, loadAnimationImages("/images/pacman_right_", 3));
            animations.put(Direction.LEFT, loadAnimationImages("/images/pacman_left_", 3));
            animations.put(Direction.UP, loadAnimationImages("/images/pacman_up_", 3));
            animations.put(Direction.DOWN, loadAnimationImages("/images/pacman_down_", 3));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Hilfsmethode zum Laden der Animationsbilder
    private Image[] loadAnimationImages(String baseName, int count) throws IOException {
        Image[] images = new Image[count];
        for (int i = 0; i < count; i++) {
            String imagePath = baseName + i + ".png";
            URL imageUrl = getClass().getResource(imagePath);
            System.out.println("Loading image: " + imagePath + " from URL: " + imageUrl);
            if (imageUrl == null) {
                throw new IOException("Image not found: " + imagePath);
            }
            images[i] = ImageIO.read(imageUrl);
        }
        return images;
    }

    // Zeichnet das aktuelle Bild der Animation auf dem Bildschirm
    @Override
    public void draw(Graphics g) {
        g.drawImage(animations.get(currentDirection)[currentImageIndex], x, y, null);
    }

    // Aktualisiert die Position und Animation
    @Override
    public void update() {
        x += dx; // Aktualisiert die x-Position
        y += dy; // Aktualisiert die y-Position

        animationCounter++;
        if (animationCounter >= animationDelay) {
            // Wechselt zum nächsten Bild in der Animation
            currentImageIndex = (currentImageIndex + 1) % animations.get(currentDirection).length;
            animationCounter = 0; // Setzt den Animationszähler zurück
        }
    }

    // Setzt die Bewegungsdeltas und die aktuelle Richtung
    public void move(int dx, int dy, Direction direction) {
        this.dx = dx * 3; // Setzt die x-Geschwindigkeit
        this.dy = dy * 3; // Setzt die y-Geschwindigkeit
        this.currentDirection = direction; // Setzt die aktuelle Richtung
    }

    // Getter für die x-Position
    public int getX() {
        return x;
    }

    // Getter für die y-Position
    public int getY() {
        return y;
    }

    // Setzt die Position von Pacman
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}